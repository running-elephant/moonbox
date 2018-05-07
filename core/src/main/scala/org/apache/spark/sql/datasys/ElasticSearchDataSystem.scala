package org.apache.spark.sql.datasys

import java.net.InetAddress
import java.util.Properties

import moonbox.catalyst.adapter.elasticsearch5.EsCatalystQueryExecutor
import moonbox.catalyst.adapter.util.SparkUtil
import moonbox.catalyst.core.parser.udf.{ArrayExists, ArrayFilter, ArrayMap}
import moonbox.common.MbLogging
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.catalyst.expressions.{Add, Alias, And, AttributeReference, CaseWhenCodegen, Divide, EqualNullSafe, EqualTo, GreaterThan, GreaterThanOrEqual, In, IsNotNull, IsNull, LessThan, LessThanOrEqual, Like, Literal, Multiply, Not, Or, Round, Substring, Subtract}
import org.apache.spark.sql.catalyst.expressions.aggregate._
import org.apache.spark.sql.catalyst.plans.JoinType
import org.apache.spark.sql.catalyst.plans.logical._
import org.apache.spark.sql.rdd.MbElasticSearchRDD
import org.apache.spark.sql.{DataFrame, Row, SparkSession}

import scala.collection.JavaConversions._
class ElasticSearchDataSystem(@transient val props: Map[String, String])(@transient val sparkSession: SparkSession) extends DataSystem(props) with MbLogging{
	require(contains("es.nodes", "es.resource"))

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
		classOf[Project],
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
		properties.put("nodes", props("es.nodes").split('/')(0))
		properties.put("database", props("es.resource").split('/')(0))
		properties.put("table", props("es.resource").split('/')(1))

		if(props.contains("es.read.field.as.array.include")){
			properties.put("es.read.field.as.array.include", props("es.read.field.as.array.include"))
		}
		properties
	}

	override def buildScan(plan: LogicalPlan): DataFrame = {
		val schema = plan.schema
		val prop: Properties = getProperties
		val executor = new EsCatalystQueryExecutor(prop)
		val json = executor.translate(plan).head
		val mapping: Seq[(String, String)] = executor.getColumnMapping()	//alias name : column name
    logInfo(json)
		val rdd = new MbElasticSearchRDD[Row](sparkSession.sparkContext, json, mapping, schema, 1, getProperties, (schema, rs) => SparkUtil.resultListToJdbcRow(schema, rs))
		sparkSession.createDataFrame(rdd, plan.schema)

	}

}
