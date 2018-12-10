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

package moonbox.core.datasys.redis

import moonbox.core.datasys.{DataSystem, DataTable, Insertable}
import org.apache.spark.sql.SaveMode

class RedisDataSystem(props: Map[String, String])
	extends DataSystem(props) with Insertable {

	override def tableNames(): Seq[String] = Seq()

	override def tableName(): String = ""

	override def tableProperties(tableName: String): Map[String, String] = Map()

	override def insert(table: DataTable, saveMode: SaveMode): Unit = {
		throw new Exception("Unsupport operation: insert with datatalbe.")
		/*require(props.contains("jobId"))
		val servers = props.getOrElse("", "")
		val redisClient = new RedisClient(servers)
		val jobId = props("jobId")
		redisClient.put[String, String, String]("SCHEMA", jobId, table.schema.json)
		table.iter.foreach { row =>
			// TODO decimal
			redisClient.put(jobId, row.toSeq)
		}
		redisClient.close
		table.close()*/
	}
}
