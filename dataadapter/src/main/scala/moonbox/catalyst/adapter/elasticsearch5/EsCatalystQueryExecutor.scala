package moonbox.catalyst.adapter.elasticsearch5

import java.util.Properties

import moonbox.catalyst.adapter.elasticsearch5.client.EsRestClient
import moonbox.catalyst.adapter.elasticsearch5.rule.EsBaseOperator
import moonbox.catalyst.adapter.elasticsearch5.util.EsUtil
import moonbox.catalyst.adapter.jdbc.JdbcRow
import moonbox.catalyst.adapter.util.SparkUtil
import moonbox.catalyst.core.parser.udf.FunctionUtil
import moonbox.catalyst.core.plan.CatalystPlan
import moonbox.catalyst.core.{CatalystContext, CatalystQueryExecutor, Strategy}
import moonbox.common.MbLogging
import org.apache.spark.sql.UDFRegistration
import org.apache.spark.sql.catalyst.plans.logical.LogicalPlan
import org.apache.spark.sql.types._


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
    val (schema: StructType, nestFieldSet: Set[String]) = client.getSchema(index, typ)


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
        schema
    }

    /***
      * plan is parsed by internal JDBC, API interface
      */
    override def execute4Jdbc(plan: LogicalPlan): (Iterator[JdbcRow], Map[Int, Int], Map[String, Int]) = {
        executeInternal[JdbcRow](plan, SparkUtil.resultListToJdbcRow)
    }

    /***
      * plan is parsed by SPARK, API interface
      */
    override def execute[T](plan: LogicalPlan, convert: (Option[StructType], Seq[Any]) => T): Iterator[T] = {
        executeInternal[T](plan, convert)._1
    }

    def executeInternal[T](plan: LogicalPlan, convert: (Option[StructType], Seq[Any]) => T): (Iterator[T], Map[Int, Int], Map[String, Int]) = {
        val context = new CatalystContext()
        context.nestFields = nestFieldSet
        context.version = version

        val iterator: Iterator[CatalystPlan] = planner.plan(plan)

        val json: String = if (iterator.hasNext) {
            val plan = iterator.next()  // do translate
            val seq: Seq[String] = plan.translate(context)
            EsUtil.buildRequest(context, seq, info)
        } else {
            "{}"
        }

        val schema = plan.schema
        if(log.isInfoEnabled()) { //print json, and schema
            logInfo("json" + json)
            logInfo("infer schema:" + schema.toString())
        }else{
            println("json" + json)
            println("infer schema:" + schema.toString())
        }

        val response = client.performScrollRequest(index, typ, json, context.hasLimited)
        val data: Seq[Seq[Any]] = response.getResult(schema, context.colId2colNameMap)

        val projectPipeLine: Seq[Seq[Any]] = FunctionUtil.doProjectFunction(data, schema, context.projectFunctionSeq)  //doProjectFunction
        val filterPipeLine: Seq[Seq[Any]] = FunctionUtil.doFilterFunction(projectPipeLine, context.colName2colIdMap, context.filterFunctionSeq)

        val rowsIter :Iterator[T] = filterPipeLine.map{ elem => convert(Some(schema), elem)}.iterator
        (rowsIter, struct2Type(schema), struct2Name(schema))

        //val rowsIter: EsIterator[T] = new EsIterator(client, info, json, schema, context, convert)
        //(rowsIter, struct2Type(schema), struct2Name(schema))
    }


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


object EsCatalystQueryExecutor {
    //geo point function
    def geo_distance(distance: String, lat: Double, lon: Double): Boolean = { true }
    def geo_shape(shape_type: String, p1_lat: Double, p1_lon: Double, p2_lat: Double, p2_lon: Double, relation: String) = { true }
    def geo_bounding_box(field: String, p1_lat: Double, p1_lon: Double, p2_lat: Double, p2_lon: Double) = { true }
    def geo_polygon(field: String, lat: Seq[Double], lon: Seq[Double]) = { true }
}

