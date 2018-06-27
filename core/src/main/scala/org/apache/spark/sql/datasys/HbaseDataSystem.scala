package org.apache.spark.sql.datasys

import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.sql.catalyst.plans.JoinType
import org.apache.spark.sql.catalyst.plans.logical._

class HbaseDataSystem(props: Map[String, String])(@transient val sparkSession: SparkSession) extends DataSystem(props) {
	override val name: String = "hbase"

	override protected val supportedOperators: Seq[Class[_]] = Seq(
		classOf[Project],
		classOf[Filter],
		classOf[GlobalLimit],
		classOf[LocalLimit]
	)
	override protected val supportedUDF: Seq[String] = Seq()

	override protected val supportedExpressions: Seq[Class[_]] = Seq()

	override protected val beGoodAtOperators: Seq[Class[_]] = Seq(
		classOf[Project],
		classOf[Filter],
		classOf[GlobalLimit],
		classOf[LocalLimit]
	)

	override protected val supportedJoinTypes: Seq[JoinType] = Seq()

	override protected def isSupportAll: Boolean = false

	override def fastEquals(other: DataSystem): Boolean = false

	override def buildScan(plan: LogicalPlan): DataFrame = {
null
	}

	override def tableNames(): Seq[String] = Seq()

	override def tableProperties(tableName: String): Map[String, String] = Map()

	override def tableName() = ""
}
