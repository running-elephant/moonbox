/*
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
package moonbox.jdbc

import java.io.{ByteArrayInputStream, InputStream, Reader, StringReader}
import java.math.BigDecimal
import java.nio.charset.Charset
import java.sql.{Blob, Clob, Date, NClob, Ref, ResultSet, ResultSetMetaData, RowId, SQLException, SQLFeatureNotSupportedException, SQLXML, Time, Timestamp}
import java.util
import java.util.{Calendar, GregorianCalendar, Locale}

import moonbox.client.entity.MoonboxRowSet
import moonbox.protocol.util.SchemaUtil

class MoonboxResultSet(conn: MoonboxConnection,
                       stat: MoonboxStatement,
                       moonboxRowSet: MoonboxRowSet
                      ) extends ResultSet {

  private lazy val columnLabelToIndex: Map[String, Int] = moonboxRowSet.parsedSchema.map(_._1).zipWithIndex.map(p => p._1 -> (p._2 + 1)).toMap
  private val cachedCalendar = new ThreadLocal[Calendar]()

  private var currentRow: Array[Any] = Array.empty[Any]
  private var currentColumnValue: Any = _
  private var rowCount: Int = _
  private var noMoreRows: Boolean = _
  private var closed: Boolean = false
  private var resultSetMetaData: ResultSetMetaData = new MoonboxResultSetMetaData(this, moonboxRowSet.jsonSchema)

  override def next = {
    if (moonboxRowSet.hasNext){
      rowCount += 1
      currentRow = moonboxRowSet.next().toArray
      true
    } else {
      noMoreRows = true
      false
    }
  }
  private def checkClosed(): Unit = {
    if (!closed){
      if (stat != null) {
        stat.checkClosed()
      }
    } else throw new SQLException("ResultSet has already been closed.")
  }
  override def close() = {
    currentRow = null
    resultSetMetaData = null
    closed = true
  }
  override def wasNull = {
    checkClosed()
    currentColumnValue == null
  }
  override def getString(columnIndex: Int) = Option(get(columnIndex)).map(_.toString).orNull
  override def getBoolean(columnIndex: Int) = Option(getAs[Boolean](columnIndex)).getOrElse(false)
  override def getByte(columnIndex: Int) = Option(getAs[Byte](columnIndex)).getOrElse(0)
  override def getShort(columnIndex: Int) = Option(getAs[Short](columnIndex)).getOrElse(0)
  override def getInt(columnIndex: Int) = Option(getAs[Int](columnIndex)).getOrElse(0)
  override def getLong(columnIndex: Int) = Option(getAs[Long](columnIndex)).getOrElse(0)
  override def getFloat(columnIndex: Int) = Option(getAs[Float](columnIndex)).getOrElse(0)
  override def getDouble(columnIndex: Int) = Option(getAs[Double](columnIndex)).getOrElse(0)
  override def getBigDecimal(columnIndex: Int, scale: Int) = Option(getBigDecimal(columnIndex)).map(_.setScale(scale)).orNull
  override def getBytes(columnIndex: Int) = getAs[Array[Byte]](columnIndex)
  override def getDate(columnIndex: Int) = getAs[Date](columnIndex)
  override def getTime(columnIndex: Int) = Option(getTimestamp(columnIndex)).map(t => new Time(t.getTime)).orNull
  override def getTimestamp(columnIndex: Int) = getAs[Timestamp](columnIndex)
  override def getAsciiStream(columnIndex: Int) = Option(getString(columnIndex)).map(s => new ByteArrayInputStream(s.getBytes(Charset.forName("UTF-8")))).orNull // TODO: test the correctness
  override def getUnicodeStream(columnIndex: Int) = throw new SQLException("Unsupported unicodeStream")
  override def getBinaryStream(columnIndex: Int) = throw new SQLException("Unsupported temporarily") // TODO: support binary stream
  override def getString(columnLabel: String) = getString(columnLabelToIndex(columnLabel))
  override def getBoolean(columnLabel: String) = getBoolean(columnLabelToIndex(columnLabel))
  override def getByte(columnLabel: String) = getByte(columnLabelToIndex(columnLabel))
  override def getShort(columnLabel: String) = getShort(columnLabelToIndex(columnLabel))
  override def getInt(columnLabel: String) = getInt(columnLabelToIndex(columnLabel))
  override def getLong(columnLabel: String) = getLong(columnLabelToIndex(columnLabel))
  override def getFloat(columnLabel: String) = getFloat(columnLabelToIndex(columnLabel))
  override def getDouble(columnLabel: String) = getDouble(columnLabelToIndex(columnLabel))
  override def getBigDecimal(columnLabel: String, scale: Int) = getBigDecimal(columnLabelToIndex(columnLabel), scale)
  override def getBytes(columnLabel: String) = getBytes(columnLabelToIndex(columnLabel))
  override def getDate(columnLabel: String) = getDate(columnLabelToIndex(columnLabel))
  override def getTime(columnLabel: String) = getTime(columnLabelToIndex(columnLabel))
  override def getTimestamp(columnLabel: String) = getTimestamp(columnLabelToIndex(columnLabel))
  override def getAsciiStream(columnLabel: String) = getAsciiStream(columnLabelToIndex(columnLabel))
  override def getUnicodeStream(columnLabel: String) = getUnicodeStream(columnLabelToIndex(columnLabel))
  override def getBinaryStream(columnLabel: String) = getBinaryStream(columnLabelToIndex(columnLabel))
  override def getWarnings = null
  override def clearWarnings() = {}
  override def getCursorName = ""
  override def getMetaData = resultSetMetaData
  override def getObject(columnIndex: Int) = getAs[AnyRef](columnIndex)
  override def getObject(columnLabel: String) = getObject(columnLabelToIndex(columnLabel))
  override def findColumn(columnLabel: String) = columnLabelToIndex(columnLabel)
  override def getCharacterStream(columnIndex: Int) = Option(getString(columnIndex)).map(s => new StringReader(s)).orNull // TODO: test the correctness
  override def getCharacterStream(columnLabel: String) = getCharacterStream(columnLabelToIndex(columnLabel))
  override def getBigDecimal(columnIndex: Int) = getAs[java.math.BigDecimal](columnIndex)
  override def getBigDecimal(columnLabel: String) = getBigDecimal(columnLabelToIndex(columnLabel))
  override def isBeforeFirst = {
    checkClosed()
    rowCount == 0
  }
  override def isAfterLast = {
    checkClosed()
    noMoreRows
  }
  override def isFirst = {
    checkClosed()
    rowCount == 1
  }
  override def isLast = throw new SQLException("Unsupported")
  override def beforeFirst() = throw new SQLException("Unsupported")
  override def afterLast() = throw new SQLException("Unsupported")
  override def first = throw new SQLException("Unsupported")
  override def last = throw new SQLException("Unsupported")
  override def getRow = {
    checkClosed()
    rowCount
  }
  override def absolute(row: Int) = throw new SQLException("Unsupported")
  override def relative(rows: Int) = throw new SQLException("Unsupported")
  override def previous = throw new SQLException("Unsupported")
  override def setFetchDirection(direction: Int) = {
    checkClosed()
    if (direction != ResultSet.FETCH_FORWARD){
      throw new SQLException("Only FETCH_FORWARD direction supported")
    }
  }
  override def getFetchDirection = {
    checkClosed()
    ResultSet.FETCH_FORWARD
  }
  override def setFetchSize(rows: Int) = checkClosed() // TODO: only support Statement.setFetchSize() now
  override def getFetchSize = stat.getFetchSize
  override def getType = ResultSet.TYPE_FORWARD_ONLY
  override def getConcurrency = ResultSet.CONCUR_READ_ONLY
  override def rowUpdated = throw new SQLFeatureNotSupportedException("rowUpdated() method is not supported.")
  override def rowInserted = throw new SQLFeatureNotSupportedException("rowInserted() method is not supported.")
  override def rowDeleted = throw new SQLFeatureNotSupportedException("rowDeleted() method is not supported.")
  override def updateNull(columnIndex: Int) = throw new SQLFeatureNotSupportedException("Method is not supported.")
  override def updateBoolean(columnIndex: Int, x: Boolean) = throw new SQLFeatureNotSupportedException("Method is not supported.")
  override def updateByte(columnIndex: Int, x: Byte) = throw new SQLFeatureNotSupportedException("Method is not supported.")
  override def updateShort(columnIndex: Int, x: Short) = throw new SQLFeatureNotSupportedException("Method is not supported.")
  override def updateInt(columnIndex: Int, x: Int) = throw new SQLFeatureNotSupportedException("Method is not supported.")
  override def updateLong(columnIndex: Int, x: Long) = throw new SQLFeatureNotSupportedException("Method is not supported.")
  override def updateFloat(columnIndex: Int, x: Float) = throw new SQLFeatureNotSupportedException("Method is not supported.")
  override def updateDouble(columnIndex: Int, x: Double) = throw new SQLFeatureNotSupportedException("Method is not supported.")
  override def updateBigDecimal(columnIndex: Int, x: BigDecimal) = throw new SQLFeatureNotSupportedException("Method is not supported.")
  override def updateString(columnIndex: Int, x: String) = throw new SQLFeatureNotSupportedException("Method is not supported.")
  override def updateBytes(columnIndex: Int, x: scala.Array[Byte]) = throw new SQLFeatureNotSupportedException("Method is not supported.")
  override def updateDate(columnIndex: Int, x: Date) = throw new SQLFeatureNotSupportedException("Method is not supported.")
  override def updateTime(columnIndex: Int, x: Time) = throw new SQLFeatureNotSupportedException("Method is not supported.")
  override def updateTimestamp(columnIndex: Int, x: Timestamp) = throw new SQLFeatureNotSupportedException("Method is not supported.")
  override def updateAsciiStream(columnIndex: Int, x: InputStream, length: Int) = throw new SQLFeatureNotSupportedException("Method is not supported.")
  override def updateBinaryStream(columnIndex: Int, x: InputStream, length: Int) = throw new SQLFeatureNotSupportedException("Method is not supported.")
  override def updateCharacterStream(columnIndex: Int, x: Reader, length: Int) = throw new SQLFeatureNotSupportedException("Method is not supported.")
  override def updateObject(columnIndex: Int, x: Any, scaleOrLength: Int) = throw new SQLFeatureNotSupportedException("Method is not supported.")
  override def updateObject(columnIndex: Int, x: Any) = throw new SQLFeatureNotSupportedException("Method is not supported.")
  override def updateNull(columnLabel: String) = updateNull(columnLabelToIndex(columnLabel))
  override def updateBoolean(columnLabel: String, x: Boolean) = updateBoolean(columnLabelToIndex(columnLabel), x)
  override def updateByte(columnLabel: String, x: Byte) = updateByte(columnLabelToIndex(columnLabel), x)
  override def updateShort(columnLabel: String, x: Short) = updateShort(columnLabelToIndex(columnLabel), x)
  override def updateInt(columnLabel: String, x: Int) = updateInt(columnLabelToIndex(columnLabel), x)
  override def updateLong(columnLabel: String, x: Long) = updateLong(columnLabelToIndex(columnLabel), x)
  override def updateFloat(columnLabel: String, x: Float) = updateFloat(columnLabelToIndex(columnLabel), x)
  override def updateDouble(columnLabel: String, x: Double) = updateDouble(columnLabelToIndex(columnLabel), x)
  override def updateBigDecimal(columnLabel: String, x: BigDecimal) = updateBigDecimal(columnLabelToIndex(columnLabel), x)
  override def updateString(columnLabel: String, x: String) = updateString(columnLabelToIndex(columnLabel), x)
  override def updateBytes(columnLabel: String, x: scala.Array[Byte]) = updateBytes(columnLabelToIndex(columnLabel), x)
  override def updateDate(columnLabel: String, x: Date) = updateDate(columnLabelToIndex(columnLabel), x)
  override def updateTime(columnLabel: String, x: Time) = updateTime(columnLabelToIndex(columnLabel), x)
  override def updateTimestamp(columnLabel: String, x: Timestamp) = updateTimestamp(columnLabelToIndex(columnLabel), x)
  override def updateAsciiStream(columnLabel: String, x: InputStream, length: Int) = updateAsciiStream(columnLabelToIndex(columnLabel), x, length)
  override def updateBinaryStream(columnLabel: String, x: InputStream, length: Int) = updateBinaryStream(columnLabelToIndex(columnLabel), x, length)
  override def updateCharacterStream(columnLabel: String, reader: Reader, length: Int) = updateCharacterStream(columnLabelToIndex(columnLabel), reader, length)
  override def updateObject(columnLabel: String, x: Any, scaleOrLength: Int) = updateObject(columnLabelToIndex(columnLabel), x, scaleOrLength)
  override def updateObject(columnLabel: String, x: Any) = updateObject(columnLabelToIndex(columnLabel), x)
  override def insertRow() = throw new SQLFeatureNotSupportedException("Method is not supported.")
  override def updateRow() = throw new SQLFeatureNotSupportedException("Method is not supported.")
  override def deleteRow() = throw new SQLFeatureNotSupportedException("Method is not supported.")
  override def refreshRow() = throw new SQLFeatureNotSupportedException("Method is not supported.")
  override def cancelRowUpdates() = throw new SQLFeatureNotSupportedException("Method is not supported.")
  override def moveToInsertRow() = throw new SQLFeatureNotSupportedException("Method is not supported.")
  override def moveToCurrentRow() = throw new SQLFeatureNotSupportedException("Method is not supported.")
  override def getStatement = {
    checkClosed()
    stat
  }
  override def getObject(columnIndex: Int, map: util.Map[String, Class[_]]) = throw new SQLFeatureNotSupportedException("Unsupported")
  override def getRef(columnIndex: Int) = throw new SQLFeatureNotSupportedException("REF unsupported")
  override def getBlob(columnIndex: Int) = throw new SQLFeatureNotSupportedException("Blob unsupported")
  override def getClob(columnIndex: Int) = throw new SQLFeatureNotSupportedException("Clob unsupported")
  override def getArray(columnIndex: Int) = {
    val values = Option(getAs[Array[Any]](columnIndex)).getOrElse(Array.empty[Any])
    val elementTypeName: String = {
      val typeName = getMetaData.getColumnTypeName(columnIndex)
      if (typeName.toLowerCase(Locale.ROOT).startsWith("array")) {
        SchemaUtil.getElementTypeInArray(typeName)
      } else null
    }
    if (elementTypeName == null) {
      new JdbcArray(values)
    } else {
      val elementType: Int = SchemaUtil.typeNameToSqlType(elementTypeName)
      new JdbcArray(elementTypeName, elementType, values)
    }
  }
  override def getObject(columnLabel: String, map: util.Map[String, Class[_]]) = throw new SQLFeatureNotSupportedException("Unsupported")
  override def getRef(columnLabel: String) = getRef(columnLabelToIndex(columnLabel))
  override def getBlob(columnLabel: String) = getBlob(columnLabelToIndex(columnLabel))
  override def getClob(columnLabel: String) = getClob(columnLabelToIndex(columnLabel))
  override def getArray(columnLabel: String) = getArray(columnLabelToIndex(columnLabel))
  override def getDate(columnIndex: Int, cal: Calendar) = {
    val ms = getDate(columnIndex).getTime
    var c = cachedCalendar.get()
    if (c == null) {
      c = Calendar.getInstance()
      cachedCalendar.set(c)
    }
    c.clear()
    c.setTimeInMillis(ms)
    var year = c.get(Calendar.YEAR)
    if (c.get(Calendar.ERA) == GregorianCalendar.BC) {
      year = 1 - year
    }
    val month = c.get(Calendar.MONTH) + 1
    val day = c.get(Calendar.DAY_OF_MONTH)
    val dateValue = (year.toLong << 9) | (month << 5) | day
    convertDate(dateValue, cal)
  }
  override def getDate(columnLabel: String, cal: Calendar) = getDate(columnLabelToIndex(columnLabel), cal)
  override def getTime(columnIndex: Int, cal: Calendar) = getTime(columnIndex) // TODO:
  override def getTime(columnLabel: String, cal: Calendar) = getTime(columnLabelToIndex(columnLabel), cal)
  override def getTimestamp(columnIndex: Int, cal: Calendar) = getTimestamp(columnIndex)
  override def getTimestamp(columnLabel: String, cal: Calendar) = getTimestamp(columnLabelToIndex(columnLabel), cal)
  override def getURL(columnIndex: Int) = throw new SQLFeatureNotSupportedException("URL unsupported")
  override def getURL(columnLabel: String) = getURL(columnLabelToIndex(columnLabel))
  override def updateRef(columnIndex: Int, x: Ref) = throw new SQLFeatureNotSupportedException("REF unsupported")
  override def updateRef(columnLabel: String, x: Ref) = updateRef(columnLabelToIndex(columnLabel), x)
  override def updateBlob(columnIndex: Int, x: Blob) = throw new SQLFeatureNotSupportedException("Blob unsupported")
  override def updateBlob(columnLabel: String, x: Blob) = updateBlob(columnLabelToIndex(columnLabel), x)
  override def updateClob(columnIndex: Int, x: Clob) = throw new SQLFeatureNotSupportedException("Clob unsupported")
  override def updateClob(columnLabel: String, x: Clob) = updateClob(columnLabelToIndex(columnLabel), x)
  override def updateArray(columnIndex: Int, x: java.sql.Array) = throw new SQLFeatureNotSupportedException("Method is not supported.")
  override def updateArray(columnLabel: String, x: java.sql.Array) = updateArray(columnLabelToIndex(columnLabel), x)
  override def getRowId(columnIndex: Int) = throw new SQLFeatureNotSupportedException("RowId is not supported.")
  override def getRowId(columnLabel: String) = getRowId(columnLabelToIndex(columnLabel))
  override def updateRowId(columnIndex: Int, x: RowId) = throw new SQLFeatureNotSupportedException("RowId is not supported.")
  override def updateRowId(columnLabel: String, x: RowId) = updateRowId(columnLabelToIndex(columnLabel), x)
  override def getHoldability = conn.getHoldability
  override def isClosed = closed
  override def updateNString(columnIndex: Int, nString: String) = throw new SQLFeatureNotSupportedException("Method is not supported.")
  override def updateNString(columnLabel: String, nString: String) = updateNString(columnLabelToIndex(columnLabel), nString)
  override def updateNClob(columnIndex: Int, nClob: NClob) = throw new SQLFeatureNotSupportedException("Method is not supported.")
  override def updateNClob(columnLabel: String, nClob: NClob) = updateNClob(columnLabelToIndex(columnLabel), nClob)
  override def getNClob(columnIndex: Int) = throw new SQLFeatureNotSupportedException("Method is not supported.")
  override def getNClob(columnLabel: String) = getNClob(columnLabelToIndex(columnLabel))
  override def getSQLXML(columnIndex: Int) = throw new SQLFeatureNotSupportedException("Method is not supported.")
  override def getSQLXML(columnLabel: String) = getSQLXML(columnLabelToIndex(columnLabel))
  override def updateSQLXML(columnIndex: Int, xmlObject: SQLXML) = throw new SQLFeatureNotSupportedException("Method is not supported.")
  override def updateSQLXML(columnLabel: String, xmlObject: SQLXML) = updateSQLXML(columnLabelToIndex(columnLabel), xmlObject)
  override def getNString(columnIndex: Int) = getString(columnIndex)
  override def getNString(columnLabel: String) = getString(columnLabel)
  override def getNCharacterStream(columnIndex: Int) = getCharacterStream(columnIndex)
  override def getNCharacterStream(columnLabel: String) = getNCharacterStream(columnLabelToIndex(columnLabel))
  override def updateNCharacterStream(columnIndex: Int, x: Reader, length: Long) = throw new SQLFeatureNotSupportedException("Method is not supported.")
  override def updateNCharacterStream(columnLabel: String, reader: Reader, length: Long) = updateNCharacterStream(columnLabelToIndex(columnLabel), reader, length)
  override def updateAsciiStream(columnIndex: Int, x: InputStream, length: Long) = throw new SQLFeatureNotSupportedException("Method is not supported.")
  override def updateBinaryStream(columnIndex: Int, x: InputStream, length: Long) = throw new SQLFeatureNotSupportedException("Method is not supported.")
  override def updateCharacterStream(columnIndex: Int, x: Reader, length: Long) = throw new SQLFeatureNotSupportedException("Method is not supported.")
  override def updateAsciiStream(columnLabel: String, x: InputStream, length: Long) = updateAsciiStream(columnLabelToIndex(columnLabel), x, length)
  override def updateBinaryStream(columnLabel: String, x: InputStream, length: Long) = updateBinaryStream(columnLabelToIndex(columnLabel), x, length)
  override def updateCharacterStream(columnLabel: String, reader: Reader, length: Long) = updateCharacterStream(columnLabelToIndex(columnLabel), reader, length)
  override def updateBlob(columnIndex: Int, inputStream: InputStream, length: Long) = throw new SQLFeatureNotSupportedException("Method is not supported.")
  override def updateBlob(columnLabel: String, inputStream: InputStream, length: Long) = updateBlob(columnLabelToIndex(columnLabel), inputStream, length)
  override def updateClob(columnIndex: Int, reader: Reader, length: Long) = throw new SQLFeatureNotSupportedException("Method is not supported.")
  override def updateClob(columnLabel: String, reader: Reader, length: Long) = updateClob(columnLabelToIndex(columnLabel), reader, length)
  override def updateNClob(columnIndex: Int, reader: Reader, length: Long) = throw new SQLFeatureNotSupportedException("Method is not supported.")
  override def updateNClob(columnLabel: String, reader: Reader, length: Long) = updateNClob(columnLabelToIndex(columnLabel), reader, length)
  override def updateNCharacterStream(columnIndex: Int, x: Reader) = throw new SQLFeatureNotSupportedException("Method is not supported.")
  override def updateNCharacterStream(columnLabel: String, reader: Reader) = updateNCharacterStream(columnLabelToIndex(columnLabel), reader)
  override def updateAsciiStream(columnIndex: Int, x: InputStream) = throw new SQLFeatureNotSupportedException("Method is not supported.")
  override def updateBinaryStream(columnIndex: Int, x: InputStream) = throw new SQLFeatureNotSupportedException("Method is not supported.")
  override def updateCharacterStream(columnIndex: Int, x: Reader) = throw new SQLFeatureNotSupportedException("Method is not supported.")
  override def updateAsciiStream(columnLabel: String, x: InputStream) = updateAsciiStream(columnLabelToIndex(columnLabel), x)
  override def updateBinaryStream(columnLabel: String, x: InputStream) = updateBinaryStream(columnLabelToIndex(columnLabel), x)
  override def updateCharacterStream(columnLabel: String, reader: Reader) = updateCharacterStream(columnLabelToIndex(columnLabel), reader)
  override def updateBlob(columnIndex: Int, inputStream: InputStream) = throw new SQLFeatureNotSupportedException("Method is not supported.")
  override def updateBlob(columnLabel: String, inputStream: InputStream) = updateBlob(columnLabelToIndex(columnLabel), inputStream)
  override def updateClob(columnIndex: Int, reader: Reader) = throw new SQLFeatureNotSupportedException("Method is not supported.")
  override def updateClob(columnLabel: String, reader: Reader) = updateClob(columnLabelToIndex(columnLabel), reader)
  override def updateNClob(columnIndex: Int, reader: Reader) = throw new SQLFeatureNotSupportedException("Method is not supported.")
  override def updateNClob(columnLabel: String, reader: Reader) = updateNClob(columnLabelToIndex(columnLabel), reader)
  override def getObject[T](columnIndex: Int, `type`: Class[T]) = getAs[T](columnIndex)
  override def getObject[T](columnLabel: String, `type`: Class[T]) = getObject(columnLabelToIndex(columnLabel), `type`)
  override def unwrap[T](iface: Class[T]) = {
    if (isWrapperFor(iface)) this.asInstanceOf[T]
    else throw new SQLException("unwrap exception")
  }
  override def isWrapperFor(iface: Class[_]) = iface != null && iface.isAssignableFrom(getClass)

  /** private funcs */
  private def get(i: Int): Any = {
    checkClosed()
    if (i < 1 || i > currentRow.length) {
      throw new SQLException(s"Invalid index: $i")
    }
    currentColumnValue = currentRow(i - 1)
    currentColumnValue
  }
  private def getAs[T](i: Int): T = Option(get(i)).map(_.asInstanceOf[T]).getOrElse(null.asInstanceOf[T])
  private def convertDate(dateValue: Long, calendar: Calendar): Date = {
    val cal = calendar.clone().asInstanceOf[Calendar]
    cal.clear()
    cal.setLenient(true)
    val year = (dateValue >>> 9).toInt
    val month = ((dateValue >>> 5) & 15).toInt
    val day = (dateValue & 31).toInt
    setCalendarFields(cal, year, month, day, 0, 0, 0, 0)
    new Date(cal.getTimeInMillis)
  }
  private def setCalendarFields(cal: Calendar, year: Int, month: Int, day: Int, hour: Int, minute: Int, second: Int, millis: Int) = {
    if (year <= 0) {
      cal.set(Calendar.ERA, GregorianCalendar.BC)
      cal.set(Calendar.YEAR, 1 - year)
    } else {
      cal.set(Calendar.ERA, GregorianCalendar.AD)
      cal.set(Calendar.YEAR, year)
    }
    // january is 0
    cal.set(Calendar.MONTH, month - 1)
    cal.set(Calendar.DAY_OF_MONTH, day)
    cal.set(Calendar.HOUR_OF_DAY, hour)
    cal.set(Calendar.MINUTE, minute)
    cal.set(Calendar.SECOND, second)
    cal.set(Calendar.MILLISECOND, millis)
  }
}
*/
