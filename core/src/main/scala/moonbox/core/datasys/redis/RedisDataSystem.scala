package moonbox.core.datasys.redis

import moonbox.core.cache.RedisCache
import moonbox.core.config._
import moonbox.core.datasys.{DataSystem, DataSystemRegister, Insertable}
import moonbox.core.execution.standalone.DataTable
import org.apache.spark.sql.SaveMode

class RedisDataSystem(props: Map[String, String])
	extends DataSystem(props) with Insertable {

	override def tableNames(): Seq[String] = Seq()

	override def tableName(): String = ""

	override def tableProperties(tableName: String): Map[String, String] = Map()

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
