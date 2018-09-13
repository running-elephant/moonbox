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

import java.io.{InputStream, Reader}
import java.math.BigDecimal
import java.sql.{Blob, Clob, Date, NClob, Ref, ResultSet, RowId, SQLException, SQLXML, Time, Timestamp}
import java.util
import java.util.Calendar

class CatalystResultSet(iterator: Iterator[JdbcRow], index2SqlType: Map[Int, Int], columnLabel2Index: Map[String, Int]) extends ResultSet {
  var currentRow: JdbcRow = _
  var wasNullFlag: Boolean = false

  @throws[SQLException]
  override def next = {
    if (iterator.hasNext) {
      currentRow = iterator.next()
      true
    } else {
      false
    }
  }

  @throws[SQLException]
  override def close() = {
  }

  @throws[SQLException]
  override def wasNull = wasNullFlag

  @throws[SQLException]
  override def getString(columnIndex: Int) = currentRow.getString(columnIndex - 1)

  @throws[SQLException]
  override def getBoolean(columnIndex: Int) = currentRow.getBoolean(columnIndex - 1)

  @throws[SQLException]
  override def getByte(columnIndex: Int) = currentRow.getByte(columnIndex - 1)

  @throws[SQLException]
  override def getShort(columnIndex: Int) = currentRow.getShort(columnIndex - 1)

  @throws[SQLException]
  override def getInt(columnIndex: Int) = currentRow.getInt(columnIndex - 1)

  @throws[SQLException]
  override def getLong(columnIndex: Int) = currentRow.getLong(columnIndex - 1)

  @throws[SQLException]
  override def getFloat(columnIndex: Int) = currentRow.getFloat(columnIndex - 1)

  @throws[SQLException]
  override def getDouble(columnIndex: Int) = currentRow.getDouble(columnIndex - 1)

  @throws[SQLException]
  override def getBigDecimal(columnIndex: Int, scale: Int) = currentRow.getDecimal(columnIndex - 1).setScale(scale)

  @throws[SQLException]
  override def getBytes(columnIndex: Int) = {
    // TODO: test the correction on the type casted
    currentRow.getAs[Array[Byte]](columnIndex - 1)
  }

  @throws[SQLException]
  override def getDate(columnIndex: Int) = {
    new Date(currentRow.getLong(columnIndex - 1))
  }

  @throws[SQLException]
  override def getTime(columnIndex: Int) = {
    new Time(currentRow.getLong(columnIndex - 1))
  }

  @throws[SQLException]
  override def getTimestamp(columnIndex: Int) = {
    currentRow(columnIndex - 1).asInstanceOf[Timestamp]
  }

  @throws[SQLException]
  override def getAsciiStream(columnIndex: Int) = {
    // TODO: getAsciiStream
    null
  }

  @throws[SQLException]
  override def getUnicodeStream(columnIndex: Int) = {
    // TODO: getUnicodeStream
    null
  }

  @throws[SQLException]
  override def getBinaryStream(columnIndex: Int) = {
    // TODO: getBinaryStream
    null
  }

  @throws[SQLException]
  override def getString(columnLabel: String) = getString(columnLabel2Index(columnLabel))

  @throws[SQLException]
  override def getBoolean(columnLabel: String) = getBoolean(columnLabel2Index(columnLabel))

  @throws[SQLException]
  override def getByte(columnLabel: String) = getByte(columnLabel2Index(columnLabel))

  @throws[SQLException]
  override def getShort(columnLabel: String) = getShort(columnLabel2Index(columnLabel))

  @throws[SQLException]
  override def getInt(columnLabel: String) = getInt(columnLabel2Index(columnLabel))

  @throws[SQLException]
  override def getLong(columnLabel: String) = getLong(columnLabel2Index(columnLabel))

  @throws[SQLException]
  override def getFloat(columnLabel: String) = getFloat(columnLabel2Index(columnLabel))

  @throws[SQLException]
  override def getDouble(columnLabel: String) = getDouble(columnLabel2Index(columnLabel))

  @throws[SQLException]
  override def getBigDecimal(columnLabel: String, scale: Int) = getBigDecimal(columnLabel2Index(columnLabel), scale)

  @throws[SQLException]
  override def getBytes(columnLabel: String) = getBytes(columnLabel2Index(columnLabel))

  @throws[SQLException]
  override def getDate(columnLabel: String) = getDate(columnLabel2Index(columnLabel))

  @throws[SQLException]
  override def getTime(columnLabel: String) = getTime(columnLabel2Index(columnLabel))

  @throws[SQLException]
  override def getTimestamp(columnLabel: String) = getTimestamp(columnLabel2Index(columnLabel))

  @throws[SQLException]
  override def getAsciiStream(columnLabel: String) = getAsciiStream(columnLabel2Index(columnLabel))

  @throws[SQLException]
  override def getUnicodeStream(columnLabel: String) = getUnicodeStream(columnLabel2Index(columnLabel))

  @throws[SQLException]
  override def getBinaryStream(columnLabel: String) = getBinaryStream(columnLabel2Index(columnLabel))

  @throws[SQLException]
  override def getWarnings = {
    // TODO: getWarnings
    null
  }

  @throws[SQLException]
  override def clearWarnings() = {
    // TODO: clearWarnings
  }

  @throws[SQLException]
  override def getCursorName = null

  @throws[SQLException]
  override def getMetaData = {
    // TODO: getMetaData
    null
  }

  @throws[SQLException]
  override def getObject(columnIndex: Int) = {
    currentRow.getAs[AnyRef](columnIndex - 1)
  }

  @throws[SQLException]
  override def getObject(columnLabel: String) = getObject(columnLabel2Index(columnLabel))

  @throws[SQLException]
  override def findColumn(columnLabel: String) = columnLabel2Index(columnLabel)

  @throws[SQLException]
  override def getCharacterStream(columnIndex: Int) = {
    // TODO: getCharacterStream
    null
  }

  @throws[SQLException]
  override def getCharacterStream(columnLabel: String) = getCharacterStream(columnLabel2Index(columnLabel))

  @throws[SQLException]
  override def getBigDecimal(columnIndex: Int) = currentRow.getDecimal(columnIndex - 1)

  @throws[SQLException]
  override def getBigDecimal(columnLabel: String) = getBigDecimal(columnLabel2Index(columnLabel))

  @throws[SQLException]
  override def isBeforeFirst = {
    // TODO: isBeforeFirst
    false
  }

  @throws[SQLException]
  override def isAfterLast = {
    // TODO: isAfterLast
    false
  }

  @throws[SQLException]
  override def isFirst = {
    // TODO: isFirst
    false
  }

  @throws[SQLException]
  override def isLast = {
    // TODO: isLast
    false
  }

  @throws[SQLException]
  override def beforeFirst() = {
  }

  @throws[SQLException]
  override def afterLast() = {
  }

  @throws[SQLException]
  override def first = false

  @throws[SQLException]
  override def last = false

  @throws[SQLException]
  override def getRow = 0

  @throws[SQLException]
  override def absolute(row: Int) = false

  @throws[SQLException]
  override def relative(rows: Int) = false

  @throws[SQLException]
  override def previous = false

  @throws[SQLException]
  override def setFetchDirection(direction: Int) = {
  }

  @throws[SQLException]
  override def getFetchDirection = 0

  @throws[SQLException]
  override def setFetchSize(rows: Int) = {
  }

  @throws[SQLException]
  override def getFetchSize = 0

  // resultSet type
  @throws[SQLException]
  override def getType = ResultSet.TYPE_FORWARD_ONLY

  @throws[SQLException]
  override def getConcurrency = 0

  @throws[SQLException]
  override def rowUpdated = false

  @throws[SQLException]
  override def rowInserted = false

  @throws[SQLException]
  override def rowDeleted = false

  @throws[SQLException]
  override def updateNull(columnIndex: Int) = {
    currentRow.values(columnIndex - 1) = null
  }

  @throws[SQLException]
  override def updateBoolean(columnIndex: Int, x: Boolean) = {
    currentRow.values(columnIndex - 1) = x
  }

  @throws[SQLException]
  override def updateByte(columnIndex: Int, x: Byte) = {
    currentRow.values(columnIndex - 1) = x
  }

  @throws[SQLException]
  override def updateShort(columnIndex: Int, x: Short) = {
    currentRow.values(columnIndex - 1) = x
  }

  @throws[SQLException]
  override def updateInt(columnIndex: Int, x: Int) = {
    currentRow.values(columnIndex - 1) = x
  }

  @throws[SQLException]
  override def updateLong(columnIndex: Int, x: Long) = {
    currentRow.values(columnIndex - 1) = x
  }

  @throws[SQLException]
  override def updateFloat(columnIndex: Int, x: Float) = {
    currentRow.values(columnIndex - 1) = x
  }

  @throws[SQLException]
  override def updateDouble(columnIndex: Int, x: Double) = {
    currentRow.values(columnIndex - 1) = x
  }

  @throws[SQLException]
  override def updateBigDecimal(columnIndex: Int, x: BigDecimal) = {
    currentRow.values(columnIndex - 1) = x
  }

  @throws[SQLException]
  override def updateString(columnIndex: Int, x: String) = {
    currentRow.values(columnIndex - 1) = x
  }

  @throws[SQLException]
  override def updateBytes(columnIndex: Int, x: scala.Array[Byte]) = {
    currentRow.values(columnIndex - 1) = x
  }

  @throws[SQLException]
  override def updateDate(columnIndex: Int, x: Date) = {
    currentRow.values(columnIndex - 1) = x.getTime
  }

  @throws[SQLException]
  override def updateTime(columnIndex: Int, x: Time) = {
    currentRow.values(columnIndex - 1) = x.getTime
  }

  @throws[SQLException]
  override def updateTimestamp(columnIndex: Int, x: Timestamp) = {
    currentRow.values(columnIndex - 1) = x.getTime
  }

  @throws[SQLException]
  override def updateAsciiStream(columnIndex: Int, x: InputStream, length: Int) = {
    // TODO: updateAsciiStream
  }

  @throws[SQLException]
  override def updateBinaryStream(columnIndex: Int, x: InputStream, length: Int) = {
  }

  @throws[SQLException]
  override def updateCharacterStream(columnIndex: Int, x: Reader, length: Int) = {
  }

  @throws[SQLException]
  override def updateObject(columnIndex: Int, x: Any, scaleOrLength: Int) = {
  }

  @throws[SQLException]
  override def updateObject(columnIndex: Int, x: Any) = {
    currentRow.values(columnIndex - 1) = x
  }

  @throws[SQLException]
  override def updateNull(columnLabel: String) = {
    updateNull(columnLabel2Index(columnLabel))
  }

  @throws[SQLException]
  override def updateBoolean(columnLabel: String, x: Boolean) = {
    updateBoolean(columnLabel2Index(columnLabel), x)
  }

  @throws[SQLException]
  override def updateByte(columnLabel: String, x: Byte) = {
    updateByte(columnLabel2Index(columnLabel), x)
  }

  @throws[SQLException]
  override def updateShort(columnLabel: String, x: Short) = {
    updateShort(columnLabel2Index(columnLabel), x)
  }

  @throws[SQLException]
  override def updateInt(columnLabel: String, x: Int) = {
    updateInt(columnLabel2Index(columnLabel), x)
  }

  @throws[SQLException]
  override def updateLong(columnLabel: String, x: Long) = {
    updateLong(columnLabel2Index(columnLabel), x)
  }

  @throws[SQLException]
  override def updateFloat(columnLabel: String, x: Float) = {
    updateFloat(columnLabel2Index(columnLabel), x)
  }

  @throws[SQLException]
  override def updateDouble(columnLabel: String, x: Double) = {
    updateDouble(columnLabel2Index(columnLabel), x)
  }

  @throws[SQLException]
  override def updateBigDecimal(columnLabel: String, x: BigDecimal) = {
    updateBigDecimal(columnLabel2Index(columnLabel), x)
  }

  @throws[SQLException]
  override def updateString(columnLabel: String, x: String) = {
    updateString(columnLabel2Index(columnLabel), x)
  }

  @throws[SQLException]
  override def updateBytes(columnLabel: String, x: scala.Array[Byte]) = {
    updateBytes(columnLabel2Index(columnLabel), x)
  }

  @throws[SQLException]
  override def updateDate(columnLabel: String, x: Date) = {
    updateDate(columnLabel2Index(columnLabel), x)
  }

  @throws[SQLException]
  override def updateTime(columnLabel: String, x: Time) = {
    updateTime(columnLabel2Index(columnLabel), x)
  }

  @throws[SQLException]
  override def updateTimestamp(columnLabel: String, x: Timestamp) = {
    updateTimestamp(columnLabel2Index(columnLabel), x)
  }

  @throws[SQLException]
  override def updateAsciiStream(columnLabel: String, x: InputStream, length: Int) = {
    updateAsciiStream(columnLabel2Index(columnLabel), x, length)
  }

  @throws[SQLException]
  override def updateBinaryStream(columnLabel: String, x: InputStream, length: Int) = {
    updateBinaryStream(columnLabel2Index(columnLabel), x, length)
  }

  @throws[SQLException]
  override def updateCharacterStream(columnLabel: String, reader: Reader, length: Int) = {
    updateCharacterStream(columnLabel2Index(columnLabel), reader, length)
  }

  @throws[SQLException]
  override def updateObject(columnLabel: String, x: Any, scaleOrLength: Int) = {
    updateObject(columnLabel2Index(columnLabel), x, scaleOrLength)
  }

  @throws[SQLException]
  override def updateObject(columnLabel: String, x: Any) = {
    updateObject(columnLabel2Index(columnLabel), x)
  }

  @throws[SQLException]
  override def insertRow() = {
  }

  @throws[SQLException]
  override def updateRow() = {
  }

  @throws[SQLException]
  override def deleteRow() = {
  }

  @throws[SQLException]
  override def refreshRow() = {
  }

  @throws[SQLException]
  override def cancelRowUpdates() = {
  }

  @throws[SQLException]
  override def moveToInsertRow() = {
  }

  @throws[SQLException]
  override def moveToCurrentRow() = {
  }

  @throws[SQLException]
  override def getStatement = null

  @throws[SQLException]
  override def getObject(columnIndex: Int, map: util.Map[String, Class[_]]) = null

  @throws[SQLException]
  override def getRef(columnIndex: Int) = null

  @throws[SQLException]
  override def getBlob(columnIndex: Int) = null

  @throws[SQLException]
  override def getClob(columnIndex: Int) = null

  @throws[SQLException]
  override def getArray(columnIndex: Int) = {
    val arr = currentRow.get(columnIndex - 1).asInstanceOf[Array[Any]]
    new JdbcArray(arr)
  }

  @throws[SQLException]
  override def getObject(columnLabel: String, map: util.Map[String, Class[_]]) = null

  @throws[SQLException]
  override def getRef(columnLabel: String) = null

  @throws[SQLException]
  override def getBlob(columnLabel: String) = null

  @throws[SQLException]
  override def getClob(columnLabel: String) = null

  @throws[SQLException]
  override def getArray(columnLabel: String) = getArray(columnLabel2Index(columnLabel))

  @throws[SQLException]
  override def getDate(columnIndex: Int, cal: Calendar) = null

  @throws[SQLException]
  override def getDate(columnLabel: String, cal: Calendar) = null

  @throws[SQLException]
  override def getTime(columnIndex: Int, cal: Calendar) = null

  @throws[SQLException]
  override def getTime(columnLabel: String, cal: Calendar) = null

  @throws[SQLException]
  override def getTimestamp(columnIndex: Int, cal: Calendar) = null

  @throws[SQLException]
  override def getTimestamp(columnLabel: String, cal: Calendar) = null

  @throws[SQLException]
  override def getURL(columnIndex: Int) = null

  @throws[SQLException]
  override def getURL(columnLabel: String) = null

  @throws[SQLException]
  override def updateRef(columnIndex: Int, x: Ref) = {
  }

  @throws[SQLException]
  override def updateRef(columnLabel: String, x: Ref) = {
  }

  @throws[SQLException]
  override def updateBlob(columnIndex: Int, x: Blob) = {
  }

  @throws[SQLException]
  override def updateBlob(columnLabel: String, x: Blob) = {
  }

  @throws[SQLException]
  override def updateClob(columnIndex: Int, x: Clob) = {
  }

  @throws[SQLException]
  override def updateClob(columnLabel: String, x: Clob) = {
  }

  @throws[SQLException]
  override def updateArray(columnIndex: Int, x: java.sql.Array) = {
  }

  @throws[SQLException]
  override def updateArray(columnLabel: String, x: java.sql.Array) = {
  }

  @throws[SQLException]
  override def getRowId(columnIndex: Int) = null

  @throws[SQLException]
  override def getRowId(columnLabel: String) = null

  @throws[SQLException]
  override def updateRowId(columnIndex: Int, x: RowId) = {
  }

  @throws[SQLException]
  override def updateRowId(columnLabel: String, x: RowId) = {
  }

  @throws[SQLException]
  override def getHoldability = 0

  @throws[SQLException]
  override def isClosed = false

  @throws[SQLException]
  override def updateNString(columnIndex: Int, nString: String) = {
  }

  @throws[SQLException]
  override def updateNString(columnLabel: String, nString: String) = {
  }

  @throws[SQLException]
  override def updateNClob(columnIndex: Int, nClob: NClob) = {
  }

  @throws[SQLException]
  override def updateNClob(columnLabel: String, nClob: NClob) = {
  }

  @throws[SQLException]
  override def getNClob(columnIndex: Int) = null

  @throws[SQLException]
  override def getNClob(columnLabel: String) = null

  @throws[SQLException]
  override def getSQLXML(columnIndex: Int) = null

  @throws[SQLException]
  override def getSQLXML(columnLabel: String) = null

  @throws[SQLException]
  override def updateSQLXML(columnIndex: Int, xmlObject: SQLXML) = {
  }

  @throws[SQLException]
  override def updateSQLXML(columnLabel: String, xmlObject: SQLXML) = {
  }

  @throws[SQLException]
  override def getNString(columnIndex: Int) = null

  @throws[SQLException]
  override def getNString(columnLabel: String) = null

  @throws[SQLException]
  override def getNCharacterStream(columnIndex: Int) = null

  @throws[SQLException]
  override def getNCharacterStream(columnLabel: String) = null

  @throws[SQLException]
  override def updateNCharacterStream(columnIndex: Int, x: Reader, length: Long) = {
  }

  @throws[SQLException]
  override def updateNCharacterStream(columnLabel: String, reader: Reader, length: Long) = {
  }

  @throws[SQLException]
  override def updateAsciiStream(columnIndex: Int, x: InputStream, length: Long) = {
  }

  @throws[SQLException]
  override def updateBinaryStream(columnIndex: Int, x: InputStream, length: Long) = {
  }

  @throws[SQLException]
  override def updateCharacterStream(columnIndex: Int, x: Reader, length: Long) = {
  }

  @throws[SQLException]
  override def updateAsciiStream(columnLabel: String, x: InputStream, length: Long) = {
  }

  @throws[SQLException]
  override def updateBinaryStream(columnLabel: String, x: InputStream, length: Long) = {
  }

  @throws[SQLException]
  override def updateCharacterStream(columnLabel: String, reader: Reader, length: Long) = {
  }

  @throws[SQLException]
  override def updateBlob(columnIndex: Int, inputStream: InputStream, length: Long) = {
  }

  @throws[SQLException]
  override def updateBlob(columnLabel: String, inputStream: InputStream, length: Long) = {
  }

  @throws[SQLException]
  override def updateClob(columnIndex: Int, reader: Reader, length: Long) = {
  }

  @throws[SQLException]
  override def updateClob(columnLabel: String, reader: Reader, length: Long) = {
  }

  @throws[SQLException]
  override def updateNClob(columnIndex: Int, reader: Reader, length: Long) = {
  }

  @throws[SQLException]
  override def updateNClob(columnLabel: String, reader: Reader, length: Long) = {
  }

  @throws[SQLException]
  override def updateNCharacterStream(columnIndex: Int, x: Reader) = {
  }

  @throws[SQLException]
  override def updateNCharacterStream(columnLabel: String, reader: Reader) = {
  }

  @throws[SQLException]
  override def updateAsciiStream(columnIndex: Int, x: InputStream) = {
  }

  @throws[SQLException]
  override def updateBinaryStream(columnIndex: Int, x: InputStream) = {
  }

  @throws[SQLException]
  override def updateCharacterStream(columnIndex: Int, x: Reader) = {
  }

  @throws[SQLException]
  override def updateAsciiStream(columnLabel: String, x: InputStream) = {
  }

  @throws[SQLException]
  override def updateBinaryStream(columnLabel: String, x: InputStream) = {
  }

  @throws[SQLException]
  override def updateCharacterStream(columnLabel: String, reader: Reader) = {
  }

  @throws[SQLException]
  override def updateBlob(columnIndex: Int, inputStream: InputStream) = {
  }

  @throws[SQLException]
  override def updateBlob(columnLabel: String, inputStream: InputStream) = {
  }

  @throws[SQLException]
  override def updateClob(columnIndex: Int, reader: Reader) = {
  }

  @throws[SQLException]
  override def updateClob(columnLabel: String, reader: Reader) = {
  }

  @throws[SQLException]
  override def updateNClob(columnIndex: Int, reader: Reader) = {
  }

  @throws[SQLException]
  override def updateNClob(columnLabel: String, reader: Reader) = {
  }

  @throws[SQLException]
  override def getObject[T](columnIndex: Int, `type`: Class[T]) = null.asInstanceOf[T]

  @throws[SQLException]
  override def getObject[T](columnLabel: String, `type`: Class[T]) = null.asInstanceOf[T]

  @throws[SQLException]
  override def unwrap[T](iface: Class[T]) = null.asInstanceOf[T]

  @throws[SQLException]
  override def isWrapperFor(iface: Class[_]) = false
}
