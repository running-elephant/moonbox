package org.apache.spark.sql.datasys

import java.util.Properties

import moonbox.catalyst.adapter.elasticsearch5.EsCatalystQueryExecutor
import moonbox.catalyst.adapter.util.SparkUtil
import moonbox.catalyst.core.parser.udf.{ArrayExists, ArrayFilter, ArrayMap}
import moonbox.common.MbLogging
import moonbox.core.execution.standalone.{DataTable, DataTableWriter}
import org.apache.spark.sql.catalyst.expressions.{Add, Alias, And, AttributeReference, CaseWhenCodegen, Divide, EqualNullSafe, EqualTo, GreaterThan, GreaterThanOrEqual, In, IsNotNull, IsNull, LessThan, LessThanOrEqual, Like, Literal, Multiply, Not, Or, Round, Substring, Subtract}
import org.apache.spark.sql.catalyst.expressions.aggregate._
import org.apache.spark.sql.catalyst.plans.JoinType
import org.apache.spark.sql.catalyst.plans.logical._
import org.apache.spark.sql.rdd.MbElasticSearchRDD
import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.{DataFrame, Row, SaveMode, SparkSession}

import scala.collection.JavaConverters._

class ElasticSearchDataSystem(@transient val props: Map[String, String])(@transient val sparkSession: SparkSession)
		extends DataSystem(props) with Queryable with Insertable with Truncatable with MbLogging{
    import ElasticSearchDataSystem._
    require(contains(ES_NODES, ES_RESOURCE))

	override val name: String = "elasticsearch"

	override protected val supportedOperators: Seq[Class[_]] = Seq(
		classOf[Project],
		classOf[Filter],
		classOf[GlobalLimit],
		classOf[LocalLimit],
		classOf[Sort],
		classOf[Aggregate]
	)
	override protected val supportedUDF: Seq[String] = Seq(
		"geo_distance",
		"geo_shape",
		"geo_bounding_box",
		"geo_polygon"
	)

	override protected val supportedExpressions: Seq[Class[_]] = Seq(
		classOf[Literal], classOf[AttributeReference], classOf[Alias],
		classOf[IsNull], classOf[IsNotNull],
		classOf[Average], classOf[Count], classOf[Max], classOf[Min], classOf[Sum],
		classOf[Add], classOf[Subtract], classOf[Multiply], classOf[Divide],
		classOf[Like], classOf[And], classOf[In], classOf[Not],
		classOf[Or], classOf[EqualNullSafe], classOf[EqualTo], classOf[GreaterThan],
		classOf[GreaterThanOrEqual], classOf[LessThan], classOf[LessThanOrEqual], classOf[Not],
		classOf[Substring], classOf[Round], classOf[CaseWhenCodegen]
		//,classOf[ArrayMap], classOf[ArrayFilter], classOf[ArrayExists]
	)

	override protected val beGoodAtOperators: Seq[Class[_]] = Seq(
		classOf[Filter],
		classOf[GlobalLimit],
		classOf[LocalLimit],
		classOf[Sort],
		classOf[Aggregate]
	)

	override protected val supportedJoinTypes: Seq[JoinType] = Seq()

	override protected def isSupportAll: Boolean = false

	override def fastEquals(other: DataSystem): Boolean = false

	private def getProperties: Properties = {

		val properties = new Properties()
        //change es.nodes to nodes, es.resource to database and table, for mb catalyst parser
		properties.put("nodes", props(ES_NODES).split('/')(0))

        val resourceArray: Array[String] = props(ES_RESOURCE).split('/')
        if(resourceArray.length == 2) {
            properties.put("database", resourceArray(0))
            properties.put("table", resourceArray(1))
        }else if(resourceArray.length == 1){
            properties.put("database", resourceArray(0))
        }

        props.foreach{ prop =>  properties.put(prop._1, prop._2) }

		//"es.read.field.as.array.include"    //Fields/properties that should be considered as arrays/lists
        //"es.mapping.id" //The document field/property name containing the document id.

		properties
	}

	override def buildScan(plan: LogicalPlan): DataFrame = {
		val schema = plan.schema
		val prop: Properties = getProperties
		val executor = new EsCatalystQueryExecutor(prop)
		val json = executor.translate(plan).head
		val mapping: Seq[(String, String)] = executor.getColumnMapping()	//alias name : column name
		logInfo(json)
		executor.close()

		val rdd = new MbElasticSearchRDD[Row](sparkSession.sparkContext,
                                                json,
                                                mapping,
                                                schema,
                                                1,
                                                getProperties,
                                                executor.context.limitSize,
                                                (schema, rs) => SparkUtil.resultListToJdbcRow(schema, rs))
		sparkSession.createDataFrame(rdd, plan.schema)

	}

    override def buildQuery(plan: LogicalPlan): DataTable = {
        val prop: Properties = getProperties
        val executor = new EsCatalystQueryExecutor(prop)
        val schema = plan.schema
        val (iter, index2SqlType, columnLabel2Index) = executor.execute4Jdbc(plan)
        new DataTable(iter, schema, () => executor.close())

    }

    override def insert(table: DataTable, saveMode: SaveMode): Unit = {
        val prop: Properties = getProperties
        val executor = new EsCatalystQueryExecutor(prop)
        try {
            executor.execute4Insert(table.iter, table.schema, saveMode)
        }finally {
            executor.close()
        }

    }

    def update(id: String, data: Seq[(String, String)]): Unit = {
        val prop: Properties = getProperties
        val executor = new EsCatalystQueryExecutor(prop)
        try {
            executor.execute4Update(id, data)
        }finally {
            executor.close()
        }
    }

    override def tableNames(): Seq[String] = {
        val prop: Properties = getProperties
        val executor = new EsCatalystQueryExecutor(prop)
        var tablesNames: Seq[String] = Seq.empty[String]
        try {  //throw exception and close connection
            tablesNames = executor.showTableByBb()
        }finally {
            executor.close()
        }
        tablesNames
    }

    override def truncate(): Unit = {  //try  throw
        val prop: Properties = getProperties
        val executor = new EsCatalystQueryExecutor(prop)
        try {  //throw exception and close connection
            executor.execute4Truncate()
        }finally {
            executor.close()
        }
    }

    override def tableProperties(tableName: String): Map[String, String] = {
        val resource: String = props(ES_RESOURCE).split("/")(0)
        props + (ES_RESOURCE -> s"$resource/$tableName")
    }

    override def tableName(): String = {
        val res = props(ES_RESOURCE).split("/")
        if(res.length == 2){ res(1) }
        else{ throw new Exception(s"$ES_RESOURCE $res does not have 2 elements by / separator")}
    }
}

object ElasticSearchDataSystem{
    val ES_NODES: String = "es.nodes"
    val ES_RESOURCE: String = "es.resource"

}
