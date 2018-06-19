package org.apache.spark.sql.datasys

import org.apache.spark.sql.catalyst.plans.JoinType
import org.apache.spark.sql.catalyst.plans.logical.LogicalPlan
import org.apache.spark.sql.{DataFrame, Dataset, SparkSession}

class SparkDataSystem(@transient val sparkSession: SparkSession) extends DataSystem(Map()) {
	override val name: String = "spark"

	override def isSupportAll: Boolean = true

	override def fastEquals(other: DataSystem): Boolean = {
		other match {
			case spark:SparkDataSystem => true
			case _ => false
		}
	}

	override val supportedOperators: Seq[Class[_]] = Seq()
	override val supportedUDF: Seq[String] = Seq()
	override val supportedExpressions: Seq[Class[_]] = Seq()
	override val beGoodAtOperators: Seq[Class[_]] = Seq()
	override val supportedJoinTypes: Seq[JoinType] = Seq()

	override def buildScan(plan: LogicalPlan): DataFrame = {
		Dataset.ofRows(sparkSession, plan)
	}

	override def tableNames() = { throw new UnsupportedOperationException("unsupport method tableNames") }
}
