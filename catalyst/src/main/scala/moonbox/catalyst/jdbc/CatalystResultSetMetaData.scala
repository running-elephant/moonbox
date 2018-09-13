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

package moonbox.catalyst.jdbc

import java.sql.ResultSetMetaData
import java.sql.SQLException

class CatalystResultSetMetaData extends ResultSetMetaData{

    @throws[SQLException]
    def getColumnCount: Int = 0

    @throws[SQLException]
    def isAutoIncrement(column: Int): Boolean = false

    @throws[SQLException]
    def isCaseSensitive(column: Int): Boolean = false

    @throws[SQLException]
    def isSearchable(column: Int): Boolean = false

    @throws[SQLException]
    def isCurrency(column: Int): Boolean = false

    @throws[SQLException]
    def isNullable(column: Int): Int = 0

    @throws[SQLException]
    def isSigned(column: Int) = false

    @throws[SQLException]
    def getColumnDisplaySize(column: Int): Int = 0

    @throws[SQLException]
    def getColumnLabel(column: Int): String = null

    @throws[SQLException]
    def getColumnName(column: Int): String = null

    @throws[SQLException]
    def getSchemaName(column: Int): String = null

    @throws[SQLException]
    def getPrecision(column: Int): Int = 0

    @throws[SQLException]
    def getScale(column: Int): Int = 0

    @throws[SQLException]
    def getTableName(column: Int): String = null

    @throws[SQLException]
    def getCatalogName(column: Int): String = null

    @throws[SQLException]
    def getColumnType(column: Int) = 0

    @throws[SQLException]
    def getColumnTypeName(column: Int): String = null

    @throws[SQLException]
    def isReadOnly(column: Int): Boolean = false

    @throws[SQLException]
    def isWritable(column: Int): Boolean = false

    @throws[SQLException]
    def isDefinitelyWritable(column: Int): Boolean = false

    @throws[SQLException]
    def getColumnClassName(column: Int): String = null

    @throws[SQLException]
    def unwrap[T](iface: Class[T]): T = null.asInstanceOf[T]

    @throws[SQLException]
    def isWrapperFor(iface: Class[_]): Boolean = false
}
