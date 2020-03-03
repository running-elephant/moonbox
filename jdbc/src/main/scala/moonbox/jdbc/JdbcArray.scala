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

import java.sql.{SQLException, SQLFeatureNotSupportedException, Types}

class JdbcArray(elementTypeName: String, elementType: Int, values: Array[Any]) extends java.sql.Array {

  def this(values: Array[Any]) = this("NULL", Types.NULL, values)

  override def getArray = values.clone()
  override def getArray(map: java.util.Map[String, Class[_]]) = throw new SQLFeatureNotSupportedException("Unsupported")
  override def getArray(index: Long, count: Int) = {
    if (index < 1 || index > values.length)
      throw new SQLException(s"Index out of bounds.")
    val newArray = Array[Any]()
    values.copyToArray(newArray, index.toInt, count)
    newArray
  }
  override def getArray(index: Long, count: Int, map: java.util.Map[String, Class[_]]) = throw new SQLFeatureNotSupportedException("Unsupported")
  override def getBaseType = elementType
  override def getBaseTypeName = elementTypeName
  override def getResultSet = throw new SQLFeatureNotSupportedException("Unsupported")
  override def getResultSet(map: java.util.Map[String, Class[_]]) = throw new SQLFeatureNotSupportedException("Unsupported")
  override def getResultSet(index: Long, count: Int) = throw new SQLFeatureNotSupportedException("Unsupported")
  override def getResultSet(index: Long, count: Int, map: java.util.Map[String, Class[_]]) = throw new SQLFeatureNotSupportedException("Unsupported")
  override def free() = {}
}
*/
