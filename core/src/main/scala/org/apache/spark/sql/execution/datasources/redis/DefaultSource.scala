/*-
 * <<
 * Moonbox
 * ==
 * Copyright (C) 2016 - 2018 EDP
 * ==
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * >>
 */

package org.apache.spark.sql.execution.datasources.redis

import moonbox.core.cache.RedisCache
import moonbox.core.config._
import org.apache.spark.sql._
import org.apache.spark.sql.catalyst.expressions.GenericRowWithSchema
import org.apache.spark.sql.sources._
import org.apache.spark.sql.types.StructType

import scala.collection.mutable.ArrayBuffer

class DefaultSource extends SchemaRelationProvider with CreatableRelationProvider with DataSourceRegister {
	override def createRelation(sqlContext: SQLContext,
		parameters: Map[String, String], schema: StructType): BaseRelation = {
		RedisRelation(parameters, schema)(sqlContext)
	}

	override def createRelation(sqlContext: SQLContext, mode: SaveMode, parameters: Map[String, String], data: DataFrame) = {
		require(parameters.contains("jobId"))
		val servers = parameters.getOrElse("", "")
		val redisClient = new RedisCache(servers)
		val jobId = parameters("jobId")
		redisClient.put[String, String, String]("SCHEMA", jobId, data.schema.json)
		data.foreachPartition { partition =>
			val redis = new RedisCache(servers)
			def parse(raw: Seq[Any]): Seq[Any] = {
				raw.map {
						case schemaRow: GenericRowWithSchema => parse(schemaRow.values)
						case row: Row => parse(row.toSeq)
						case array: Seq[_] => parse(array)
						case elem => elem
					}
			}
			try {
				val writeBuffer = new ArrayBuffer[Seq[Any]]()
				var batchSize: Int = 200
				partition.foreach { row =>
					if (batchSize <= 0) {
						writeBuffer += parse(row.toSeq)  //put last time
						redis.pipePut[String, Any](jobId, writeBuffer:_*)

						batchSize = 200
						writeBuffer.clear()
					} else {
						batchSize = batchSize - 1
						writeBuffer += parse(row.toSeq)
					}
				}
				if (writeBuffer.nonEmpty) {
					redis.pipePut[String, Any](jobId, writeBuffer:_*)
				}
			} catch{
				case e: Exception =>
					e.printStackTrace()
					throw e
			} finally {
				redis.close
			}
		}
		redisClient.close
		RedisRelation(parameters, data.schema)(sqlContext)
	}

	override def shortName(): String = "redis"
}

case class RedisRelation(props: Map[String, String], userSchema: StructType)
	(@transient val context: SQLContext) extends BaseRelation with InsertableRelation {
	require(props.contains("jobId"))
	val servers = props.getOrElse("", "")
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
