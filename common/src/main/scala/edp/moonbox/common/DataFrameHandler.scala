/*-
 * <<
 * Moonbox
 * ==
 * Copyright (C) 2016 - 2017 EDP
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

package edp.moonbox.common

import java.net.URI

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FileSystem, Path}
import org.apache.spark.sql.DataFrame

abstract class DataFrameHandler extends Function3[String, DataFrame, Map[String, String], Any]  {
  override def apply(jobId: String, data: DataFrame, properties: Map[String, String]) = handle(jobId, data, properties)
  def handle(jobId: String, data: DataFrame, properties: Map[String, String]): Any
}

class RedisDFHandler extends DataFrameHandler with EdpLogging {
	override def handle(jobId: String, v1: DataFrame, property: Map[String, String]): Long = {
		val redisServers: String = Util.getOrException(property, "servers")
        val servers = parse(redisServers)
        val redis = new RedisCacheClient(servers)
	    val schema = v1.schema.map {field => s"${field.name}:${field.dataType.typeName}"}.mkString(",")
        redis.mapPutKeyValue("SCHEMA", jobId, schema)
        v1.foreachPartition { part =>
            val redis = new RedisCacheClient(servers)
            part.foreach { row =>
                redis.listAdd(jobId, row.mkString(","))
        }
        redis.close
    }
    val totalSize = redis.listLength(jobId)
    redis.close
    totalSize
  }

	private def parse(servers: String): Seq[(String, Int)] = {
		servers.split(',').map { hostPort =>
			val hostAndPort = hostPort.trim.split(':')
			require(hostAndPort.length == 2)
			(hostAndPort(0), hostAndPort(1).toInt)
		}.toSeq
	}
}

class ParquetDFHandler extends DataFrameHandler {
  override def handle(jobId: String, v1: DataFrame, property: Map[String, String]): Any = {
    val inputPath: String = Util.getOrException(property, "path")
    val hadoopUri: String = Util.getOrException(property, "uri")
    val conf: Configuration = new Configuration()
    val uri: URI = new URI(hadoopUri)
    val fileSystem = FileSystem.get(uri, conf)
    val path: Path = new Path(inputPath)
    fileSystem.delete(path, true)
    fileSystem.close()
    v1.write.parquet(inputPath)
    "succeed"
  }
}
