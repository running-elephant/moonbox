package org.apache.spark.sql.datasys

import moonbox.core.cache.RedisCache
import moonbox.core.execution.standalone.DataTable
import org.apache.spark.sql.{DataFrame, SaveMode, SparkSession}
import org.apache.spark.sql.catalyst.plans.JoinType
import org.apache.spark.sql.catalyst.plans.logical.LogicalPlan
import moonbox.core.config._
class RedisDataSystem(props: Map[String, String])(@transient val sparkSession: SparkSession)
	extends DataSystem(props) with Insertable {
	override val name: String = "redis"

	override def tableNames(): Seq[String] = Seq()

	override def tableName(): String = ""

	override protected def isSupportAll: Boolean = false

	override def fastEquals(other: DataSystem): Boolean = false

	override def buildScan(plan: LogicalPlan): DataFrame = sparkSession.emptyDataFrame

	override def tableProperties(tableName: String): Map[String, String] = Map()

	override protected val supportedOperators: Seq[Class[_]] = Seq()
	override protected val supportedUDF: Seq[String] = Seq()
	override protected val supportedExpressions: Seq[Class[_]] = Seq()
	override protected val beGoodAtOperators: Seq[Class[_]] = Seq()
	override protected val supportedJoinTypes: Seq[JoinType] = Seq()

	override def insert(table: DataTable, saveMode: SaveMode): Unit = {
		require(props.contains("jobId"))
		val servers = props.getOrElse(CACHE_SERVERS.key, CACHE_SERVERS.defaultValueString)
		val redisClient = new RedisCache(servers)
		val jobId = props("jobId")
		redisClient.put[String, String, String]("SCHEMA", jobId, table.schema.json)
		table.iter.foreach { row =>
			// TODO decimal
			redisClient.put(jobId, row.toSeq)
		}
		redisClient.close
		table.close()
	}
}
