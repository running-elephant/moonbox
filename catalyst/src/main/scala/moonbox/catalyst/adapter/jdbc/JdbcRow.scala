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

import org.apache.spark.sql.Row

import scala.collection.mutable.ArrayBuffer

class JdbcRow extends Row {
  def this(value: Any*) = {
    this()
    values ++= (value)
  }

  lazy val values: ArrayBuffer[Any] = new ArrayBuffer()

  def iterator = values.iterator

  override def length = values.size

  override def apply(i: Int) = values(i)

  override def get(i: Int): Any = values(i)

  override def isNullAt(i: Int) = values(i) == null

  override def getInt(i: Int): Int = getAs[Int](i)

  override def getLong(i: Int): Long = getAs[Long](i)

  override def getDouble(i: Int): Double = getAs[Double](i)

  override def getFloat(i: Int): Float = getAs[Float](i)

  override def getBoolean(i: Int): Boolean = getAs[Boolean](i)

  override def getShort(i: Int): Short = getAs[Short](i)

  override def getByte(i: Int): Byte = getAs[Byte](i)

  override def getString(i: Int): String = get(i).toString

  def copy() = this

  override def toSeq = values.toSeq
}
