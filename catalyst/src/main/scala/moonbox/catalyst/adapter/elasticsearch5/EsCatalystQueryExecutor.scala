package moonbox.catalyst.adapter.elasticsearch5

import java.util.Properties

import moonbox.catalyst.adapter.elasticsearch5.client.EsRestClient
import moonbox.catalyst.adapter.elasticsearch5.rule.EsBaseOperator
import moonbox.catalyst.adapter.elasticsearch5.util.{EsRowIter, EsUtil}
import moonbox.catalyst.adapter.jdbc.JdbcRow
import moonbox.catalyst.adapter.util.SparkUtil
import moonbox.catalyst.core.plan.CatalystPlan
import moonbox.catalyst.core.{CatalystContext, CatalystQueryExecutor, Strategy}
import moonbox.common.MbLogging
import org.apache.spark.sql.{Row, UDFRegistration}
import org.apache.spark.sql.catalyst.plans.logical.LogicalPlan
import org.apache.spark.sql.types._
import org.apache.spark.sql.SaveMode

import scala.collection.JavaConverters._
import moonbox.catalyst.core.parser.udf.FunctionUtil
import moonbox.catalyst.adapter.util.SparkUtil._

import scala.collection.mutable



class EsCatalystQueryExecutor(info: Properties) extends CatalystQueryExecutor with MbLogging{

    import EsCatalystQueryExecutor._

    override val provider: String = "es"

    val index = info.getProperty(EsUtil.DB_NAME)    //database
    val typ = info.getProperty(EsUtil.TYPE_NAME)    //type

    val client: EsRestClient = {
        import scala.collection.JavaConversions._
        val cfg: Map[String, String] = info.entrySet().map(elem => elem.getKey.toString -> elem.getValue.toString).toMap
        val client = new EsRestClient(cfg)
        client
    }
    val version = client.getVersion()
    //val (schema: StructType, nestFieldSet: Set[String]) = client.getSchema(index, typ)
    val context: CatalystContext = new CatalystContext()
    //context.nestFields = nestFieldSet
    context.version = version

    override def adaptorFunctionRegister(udf: UDFRegistration) = {
        udf.register("geo_distance", geo_distance _)
        udf.register("geo_shape", geo_shape _)
        udf.register("geo_bounding_box", geo_bounding_box _)
        udf.register("geo_polygon", geo_polygon _)
    }

    override def getPlannerRule(): Seq[Strategy] = {
        Seq(EsBaseOperator)
    }

    override def getTableSchema : StructType  = {
        client.getSchema(index, typ)._1
    }

    def execute4Truncate(): Boolean = {
        client.truncateIndex(index, typ)
    }

    /** get db name list  **/
    def showAllDb(): Seq[String] = {
        client.getIndices()
    }

    /** get type name by index name(implement by mapping information) **/
    def showTableByBb(): Seq[String] = {
        client.getIndicesAndType().filter(_._1 == index).map(_._2)
    }

    /** return (doc num, doc size) **/
    def statistics(): (Long, Long) = {
        client.getStats(index)
    }

    /** update insert 1 line by id**/
    def execute4Update(id: String, data: Seq[(String, String)]): Boolean = {
        client.update(index, typ, id, data)
        true
    }

    /** insert to es by iterator, support four mode */
    def execute4Insert(iter: Iterator[Row], schema: StructType, mode: SaveMode): Unit = {
        import org.apache.spark.sql.SaveMode._
        mode match{
            case Append =>
                //batch save data to index
                val ret1 = batchInsert(iter, schema)
                logInfo(s"execute4Insert ($index,$typ) Append: batchInsert ${ret1._1} ${ret1._2}")
            case Overwrite =>
                if(client.checkExist(index, typ)) {
                    //1 delete exist index
                    val ret1 = client.deleteIndex(index)
                    logInfo(s"execute4Insert ($index,$typ) Overwrite: deleteIndex $ret1")
                    //2 create new index with schema
                    val ret2 = client.putSchema(index, typ, schema)
                    logInfo(s"execute4Insert ($index,$typ) Overwrite: deleteIndex $ret2")
                }
                //3 batch insert data to index
                val ret3 = batchInsert(iter, schema)
                logInfo(s"execute4Insert ($index,$typ) Overwrite: batchInsert ${ret3._1} ${ret3._2}")
            case ErrorIfExists =>
                //check is empty or not
                if(!client.checkExist(index, typ)){
                    //batch save data to index
                    val ret1 = batchInsert(iter, schema)
                    logInfo(s"execute4Insert ($index,$typ) ErrorIfExists: batchInsert ${ret1._1} ${ret1._2}")
                }else{
                    throw new Exception(s"SaveMode is set to ErrorIfExists and index $index $typ exists and contains data. Consider changing the SaveMode")
                }
            case Ignore =>
                if(!client.checkExist(index, typ)){
                    //batch save data to index
                    val ret1 = batchInsert(iter, schema)
                    logInfo(s"execute4Insert ($index,$typ) Ignore: batchInsert ${ret1._1} ${ret1._2}")
                }
        }
    }

    private def batchInsert(iter: Iterator[Row], schema: StructType): (Boolean, Long) = {
        val batchNum: Long = 100l  //batch number
        var currentNum: Long = 0l
        val array = scala.collection.mutable.ArrayBuffer[Seq[Any]]()
        var commitResult: Boolean = true
        var commitNumber: Long = 0

        while(iter.hasNext){
            val row: Row = iter.next()
            val seq: Seq[Any] = row.toSeq
            array += seq
            currentNum += 1
            if(currentNum >= batchNum){
                val (ret, num) = client.putBatchData(index, typ, schema, array)
                commitResult = commitResult && ret
                commitNumber += num
                array.clear()
            }
        }
        if(array.nonEmpty) {
            val (ret, num) = client.putBatchData(index, typ, schema, array)
            commitResult = commitResult && ret
            commitNumber += num
        }
        (commitResult, commitNumber)
    }


    /***
      * plan is parsed by internal JDBC, API interface
      */
    override def execute4Jdbc(plan: LogicalPlan): (Iterator[JdbcRow], Map[Int, Int], Map[String, Int]) = {
        val json = translate(plan).head //logical plan to json string
        val mapping = getColumnMapping()  // alias name(save in local name) : column name(send to server name)
        executeInternal[JdbcRow](json, plan.schema, mapping, context.limitSize, SparkUtil.resultListToJdbcRow)

    }

    /***
      * plan is parsed by SPARK, API interface
      */
    def execute[T](json: String, schema: StructType, mapping: Seq[(String, String)], limitSize: Int, convert: (Option[StructType], Seq[Any]) => T): Iterator[T] = {
        executeInternal[T](json, schema, mapping, limitSize, convert)._1

    }

    /** translate logical plan to json string **/
    override def translate(plan: LogicalPlan): Seq[String] = {
        val iterator: Iterator[CatalystPlan] = planner.plan(plan)
        context.nestFields = client.getSchema(index, typ)._2  //update nest field

        val json: String = if (iterator.hasNext) {
            val plan = iterator.next()  // do translate
            val seq: Seq[String] = plan.translate(context)
            EsUtil.buildRequest(context, seq, info)
        } else {
            "{}"
        }
        Seq(json)
    }

    def getColumnMapping(): Seq[(String, String)] = {
        if(context.hasAgg) {  context.aggElementSeq }
        else { context.projectElementSeq }
    }

    /**    seq: (Alias Name, Source Name)     **/
    def executeInternal[T](json: String,
                           schema: StructType,
                           mapping: Seq[(String, String)],
                           limitSize: Int,
                           convert: (Option[StructType], Seq[Any]) => T): (Iterator[T], Map[Int, Int], Map[String, Int]) = {

        if(log.isInfoEnabled()) { //print json, and schema
            logInfo("json" + json)
            logInfo("infer schema:" + schema.toString())
        }else{
            println("json" + json)
            println("infer schema:" + schema.toString())
        }
         /** no use iterator begin **/
//        val response = client.performScrollRequest(index, typ, json, hasLimit)
//        val data: Seq[Seq[Any]] = response.getResult(schema, colId2colNameMap(mapping))
//
//        val projectPipeLine: Seq[Seq[Any]] = FunctionUtil.doProjectFunction(data, schema, context.projectFunctionSeq)  //doProjectFunction
//        val filterPipeLine: Seq[Seq[Any]] = FunctionUtil.doFilterFunction(projectPipeLine, colName2colIdMap(mapping), context.filterFunctionSeq)
//
//        val rowsIter :Iterator[T] = filterPipeLine.map{ elem => convert(Some(schema), elem)}.iterator
//        (rowsIter, struct2Type(schema), struct2Name(schema))
        /** no use iterator end **/

        val rowsIter: EsRowIter[T] = new EsRowIter[T](index, typ, json, schema, mapping, convert, limitSize, client)
        (rowsIter.asScala, struct2Type(schema), struct2Name(schema))

    }

    def close() = {
        if(client != null) {
            client.close()
        }
    }

}


object EsCatalystQueryExecutor {
    //geo point function
    def geo_distance(distance: String, lat: Double, lon: Double): Boolean = { true }
    def geo_shape(shape_type: String, p1_lat: Double, p1_lon: Double, p2_lat: Double, p2_lon: Double, relation: String) = { true }
    def geo_bounding_box(field: String, p1_lat: Double, p1_lon: Double, p2_lat: Double, p2_lon: Double) = { true }
    def geo_polygon(field: String, lat: Seq[Double], lon: Seq[Double]) = { true }


    def struct2Name(structType: StructType): Map[String, Int] = {
        //Notice: The index should start from 1 ~ oo
        val map: Map[String, Int] = structType.fieldNames.zipWithIndex.map(e => (e._1, e._2 +1)).toMap
        map
    }

    def struct2Type(structType: StructType): Map[Int, Int] = {
        structType.fields.map{f => f.dataType}.zipWithIndex.map{ case (tpe, idx) =>
            val index = idx +1  //Notice: The index should start from 1 ~ oo
        val tp = tpe match{
            case a: ArrayType => java.sql.Types.ARRAY
            case DoubleType => java.sql.Types.DOUBLE
            case FloatType => java.sql.Types.FLOAT
            case LongType => java.sql.Types.BIGINT
            case ShortType => java.sql.Types.SMALLINT
            case IntegerType => java.sql.Types.INTEGER
            case StringType => java.sql.Types.VARCHAR
            case TimestampType => java.sql.Types.DATE
            case DateType => java.sql.Types.DATE
            case BooleanType => java.sql.Types.BOOLEAN
            case s: StructType =>  java.sql.Types.STRUCT
            case m: MapType => java.sql.Types.OTHER
            case NullType => java.sql.Types.NULL
            case _ => java.sql.Types.VARCHAR  //TODO: unknown type
        }
            (index, tp)
        }.toMap
    }
}

