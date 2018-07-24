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

package moonbox.catalyst.adapter.jdbc

import moonbox.catalyst.core.{Schema, TableMetaData}

class JdbcSchema(props: Map[String, String]) extends Schema {

  override def getFunctionNames: Seq[String] = Seq("max", "min", "avg", "count", "sum")

  override def getTableMetaData(name: String): TableMetaData = new JdbcTableMetaData(this, name)

  override def getTableNames: Seq[String] = Seq()

  override def getTableNames2: Seq[(String, String)] = Seq()

  override def getVersion: Seq[Int] = Seq()

}
