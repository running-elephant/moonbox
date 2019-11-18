/*
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
package moonbox.jdbc

import java.sql.{ResultSetMetaData, SQLException}

import moonbox.protocol.util.SchemaUtil

class MoonboxResultSetMetaData(resultSet: MoonboxResultSet,
	jsonSchema: String
) extends ResultSetMetaData {

	private lazy val sqlTypeSchema: Array[(String, Int, Boolean)] = SchemaUtil.schemaWithSqlType(parsedSchema)
	private lazy val parsedSchema: Array[(String, String, Boolean)] = SchemaUtil.parse(jsonSchema)

	override def getSchemaName(column: Int) = ""

	override def getCatalogName(column: Int) = ""

	override def isSigned(column: Int) = true

	override def getColumnLabel(column: Int) = getColumnName(column)

	override def getColumnName(column: Int) = sqlTypeSchema(column - 1)._1

	override def getColumnTypeName(column: Int) = parsedSchema(column - 1)._2

	override def isWritable(column: Int) = false

	override def getColumnClassName(column: Int) = {
		SchemaUtil.typeNameToJavaClassName(getColumnTypeName(column))
	}

	override def isAutoIncrement(column: Int) = false

	override def isReadOnly(column: Int) = true

	override def isCurrency(column: Int) = false

	override def isSearchable(column: Int) = true

	override def isCaseSensitive(column: Int) = true

	override def getTableName(column: Int) = ""

	// TODO:
	override def getColumnType(column: Int) = sqlTypeSchema(column - 1)._2

	override def isDefinitelyWritable(column: Int) = false

	override def getColumnCount = parsedSchema.length

	override def getPrecision(column: Int) = 0

	override def getScale(column: Int) = {
		var scale = 0
		if (sqlTypeSchema(column - 1)._2 == java.sql.Types.DECIMAL) {
			val presAndScale = sqlTypeSchema(column - 1)._1.stripPrefix("decimal(").stripSuffix(")").split(",")
			if (presAndScale.length == 2) {
				scale = presAndScale(1).trim.toInt
			}
		}
		scale
	}

	override def isNullable(column: Int) = {
		if (sqlTypeSchema(column - 1)._3) ResultSetMetaData.columnNullable
		else ResultSetMetaData.columnNoNulls
	}

	override def getColumnDisplaySize(column: Int) = 0

	override def unwrap[T](iface: Class[T]) = {
		if (isWrapperFor(iface)) this.asInstanceOf[T]
		else throw new SQLException("unwrap exception")
	}

	override def isWrapperFor(iface: Class[_]) = iface != null && iface.isAssignableFrom(getClass)
}
*/
