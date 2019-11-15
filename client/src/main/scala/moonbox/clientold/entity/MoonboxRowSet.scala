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

package moonbox.client.entity

import java.util
import java.util.{Map => JMap}

import moonbox.protocol.DataType
import moonbox.protocol.util.SchemaUtil

import scala.collection.JavaConverters._

class MoonboxRowSet(_rowIterator: util.Iterator[MoonboxRow], _schema: String) extends Iterator[MoonboxRow] {

  private lazy val _parsedSchema: Array[(String, String, Boolean)] = SchemaUtil.parse(_schema)
  private lazy val _fieldNameToIndex: JMap[String, Int] = _parsedSchema.map(_._1).zipWithIndex.toMap.asJava
  private lazy val _dataTypes: Array[(String, DataType, Boolean)] = SchemaUtil.schemaToDataType(_parsedSchema)

  def this(iter: util.Iterator[MoonboxRow]) = this(iter, SchemaUtil.emptyJsonSchema)
  def this(schema: String) = this(Seq.empty[MoonboxRow].toIterator.asJava, schema)
  def this() = this(SchemaUtil.emptyJsonSchema)

  /**
    * Returns the index of a given field name.
    *
    * @throws UnsupportedOperationException when jsonSchema is not defined.
    * @throws IllegalArgumentException      when a field `name` does not exist.
    */
  def columnIndex(name: String): Int = {
    if (jsonSchema == null || jsonSchema == "") throw new UnsupportedOperationException("No schema defined.")
    if (!_fieldNameToIndex.containsKey(name)) throw new IllegalArgumentException(s"Filed name $name not found.")
    _fieldNameToIndex.get(name)
  }
  def isEmptySchema: Boolean = _parsedSchema.isEmpty
  def jsonSchema: String = _schema
  def parsedSchema: Array[(String, String, Boolean)] = _parsedSchema
  def fieldNameToIndex: JMap[String, Int] = _fieldNameToIndex
  def columnCount: Int = _parsedSchema.length
  def columnName(index: Int): String = {
    require(index >= 0 && index < columnCount, "Index illegal.")
    _parsedSchema(index)._1
  }
  def columnTypeName(index: Int): String = {
    require(index >= 0 && index < columnCount, "Index illegal.")
    _parsedSchema(index)._2
  }
  def columnDataType(index: Int): DataType = {
    require(index >= 0 && index < columnCount, "Index illegal.")
    _dataTypes(index)._2
  }
  def isNullable(index: Int): Boolean = {
    require(index >= 0 && index < columnCount, "Index illegal.")
    _parsedSchema(index)._3
  }

  override def hasNext: Boolean = _rowIterator.hasNext
  override def next(): MoonboxRow = _rowIterator.next()
}
*/
