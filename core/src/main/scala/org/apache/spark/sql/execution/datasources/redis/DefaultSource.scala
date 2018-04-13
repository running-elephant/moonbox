package org.apache.spark.sql.execution.datasources.redis

import moonbox.core.cache.RedisCache
import moonbox.core.config._
import org.apache.spark.sql._
import org.apache.spark.sql.sources._
import org.apache.spark.sql.types.StructType




class DefaultSource extends SchemaRelationProvider with CreatableRelationProvider {
	override def createRelation(sqlContext: SQLContext,
		parameters: Map[String, String], schema: StructType): BaseRelation = {
		RedisRelation(parameters, schema)(sqlContext)
	}

	override def createRelation(sqlContext: SQLContext, mode: SaveMode, parameters: Map[String, String], data: DataFrame) = {
		require(parameters.contains("jobId"))
		val servers = parameters.getOrElse(CACHE_SERVERS.key, CACHE_SERVERS.defaultValueString)
		val redisClient = new RedisCache(servers)
		val jobId = parameters("jobId")
		redisClient.put[String, String, String]("SCHEMA", jobId, data.schema.json)
		data.foreachPartition { partition =>
			val redis = new RedisCache(servers)
			partition.foreach { row =>
				redis.put[String, Any](jobId, row.toSeq)
			}
		}
		RedisRelation(parameters, data.schema)(sqlContext)
	}
}

case class RedisRelation(props: Map[String, String], userSchema: StructType)
	(@transient val context: SQLContext) extends BaseRelation with InsertableRelation {
	require(props.contains("jobId"))
	val servers = props.getOrElse(CACHE_SERVERS.key, CACHE_SERVERS.defaultValueString)
	val redisClient = new RedisCache(servers)
	val jobId = props("jobId")

	override def sqlContext: SQLContext = context

	override def schema: StructType = userSchema

	override def insert(data: DataFrame, overwrite: Boolean): Unit = {
		redisClient.put[String, String, String]("SCHEMA", jobId, data.schema.json)
		data.foreachPartition { partition =>
			val redis = new RedisCache(servers)
			partition.foreach { row =>
				// TODO decimal
				redis.put(jobId, row.toSeq)
			}
		}
	}
}
