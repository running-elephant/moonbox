/*-
 * <<
 * Moonbox
 * ==
 * Copyright (C) 2016 - 2019 EDP
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

package moonbox.core

/*sealed trait IdentifiersWithDatabase {
	def database: Option[String]
}

case class MbTableIdentifier(table: String, database: Option[String])
	extends IdentifiersWithDatabase {
	def this(table: String) = this(table, None)
}

object MbTableIdentifier {
	def apply(tableName: String): MbTableIdentifier = new MbTableIdentifier(tableName)
}

case class MbFunctionIdentifier(func: String, database: Option[String])
	extends IdentifiersWithDatabase {
	def this(func: String) = this(func, None)
}

object MbFunctionIdentifier {
	def apply(func: String): MbFunctionIdentifier = new MbFunctionIdentifier(func)
}*/

case class ColumnIdentifier(
	column: String,
	table: String,
	database: Option[String]) {
	def this(column: String, table: String) = this(column, table, None)
}
