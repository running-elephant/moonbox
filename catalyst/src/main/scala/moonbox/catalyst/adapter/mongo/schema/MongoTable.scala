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

package moonbox.catalyst.adapter.mongo.schema

import com.mongodb.MongoClient
import moonbox.catalyst.core.TableMetaData
import org.apache.spark.sql.types.StructType

class MongoTable(client: MongoClient, database: String, table: String) extends TableMetaData {
  var mongoSchemaInfer = new MongoSchemaInfer

  lazy val schema: StructType = mongoSchemaInfer.inferSchema(client, database, table)

  override def getTableSchema: StructType = this.schema

  def getTableSchema(sampleSize: Int) = {
    new MongoSchemaInfer(sampleSize).inferSchema(client, database, table)
  }

  override def getTableStats = (0L, 0L)
}
