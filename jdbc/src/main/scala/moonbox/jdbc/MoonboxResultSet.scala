package moonbox.jdbc

import java.io.{InputStream, Reader}
import java.math.BigDecimal
import java.sql.{Blob, Clob, Date, NClob, Ref, ResultSet, ResultSetMetaData, RowId, SQLException, SQLXML, Time, Timestamp}
import java.util
import java.util.Calendar

import moonbox.grid.deploy.transport.model.{DataFetchInbound, DataFetchOutbound, DataFetchState, JdbcQueryOutbound}
import moonbox.util.SchemaUtil._

class MoonboxResultSet(conn: MoonboxConnection,
                       stat: MoonboxStatement,
                       var rows: Seq[Seq[Any]],
                       schema: String
                      ) extends ResultSet {

  var closed: Boolean = false
  var currentRowStart: Long = 0 // this fetched data's start index (inclusive)
  var currentRowEnd: Long = _ // this fetched data's end index (inclusive)
  /** currentRow's Index in the whole ResultSet (start with -1) */
  var currentRowId: Long = -1
  var totalRows: Long = _
  var forwardOnly: Boolean = true

  var FETCH_SIZE: Int = stat.getFetchSize
  var fetchJobId: String = _
  var currentRow: MoonboxJdbcRow = _
  lazy val index2SqlType: Map[Int, Int] = schema2SqlType(parsedSchema).map(_._2).zipWithIndex.map(p => (p._2 + 1) -> p._1).toMap
  lazy val columnLabel2Index: Map[String, Int] = parsedSchema.map(_._1).zipWithIndex.map(p => p._1 -> (p._2 + 1)).toMap
  var resultSetMetaData: ResultSetMetaData = _

  lazy val parsedSchema = parse(schema)

  def sendNextDataFetch(): DataFetchOutbound = {
    val client = stat.jdbcSession.jdbcClient
    val messageId = client.getMessageId()
    val dataFetchState = DataFetchState(messageId, client.clientId, fetchJobId, currentRowId + 1, FETCH_SIZE, stat.totalRows)
    val nextDataFetch = DataFetchInbound(dataFetchState, conn.getSession().user)
    val resp = client.sendAndReceive(nextDataFetch, stat.EXEUCTE_QUERY_TIMEOUT)
    resp match {
      case dataFetch: DataFetchOutbound => dataFetch
      case _ => throw new SQLException(s"data fetch error: $nextDataFetch")
    }
  }

  override def next = {
    var flag = false
    if (!closed && currentRowId < currentRowEnd) {
      currentRowId += 1
      currentRow = new MoonboxJdbcRow(rows((currentRowId - currentRowStart).toInt): _*)
      flag = true
    } else if (!closed && currentRowId < totalRows - 1) {
      val resp = sendNextDataFetch()
      updateResultSet(resp)
      flag = next
    }
    flag
  }

  def updateResultSet(result: JdbcQueryOutbound): Unit = {
    /** update rows, currentRowStart, currentRowEnd, currentRowId, totalRows, closed, resultSetMetaData */
    rows = result.data
    currentRowStart = 0
    currentRowEnd = result.data.size - 1
    currentRowId = currentRowStart - 1
    totalRows = result.data.size
    closed = false
    if (result.schema != null && resultSetMetaData == null)
      resultSetMetaData = new MoonboxResultSetMetaData(this, result.schema)
  }

  def updateResultSet(dataFetch: DataFetchOutbound): Unit = {
    /** update fetchJobId, rows, currentRowStart, currentRowEnd, currentRowId, totalRows, closed, resultSetMetaData */
    rows = dataFetch.data
    if (fetchJobId == null)
      fetchJobId = dataFetch.dataFetchState.jobId
    currentRowStart = dataFetch.dataFetchState.startRowIndex
    currentRowEnd = dataFetch.dataFetchState.fetchSize + currentRowStart - 1
    currentRowId = currentRowStart - 1
    if (totalRows <= 0)
      totalRows = dataFetch.dataFetchState.totalRows
    closed = false
    if (dataFetch.schema != null && resultSetMetaData == null)
      resultSetMetaData = new MoonboxResultSetMetaData(this, dataFetch.schema)
  }

  def checkClosed: Unit = {
    if (rows == null)
      throw new Exception("ResultSet is already closed")
    if (stat != null)
      stat.checkClosed
    if (conn != null)
      conn.checkClosed()
  }

  override def close() = {
    FETCH_SIZE = 0
    fetchJobId = null
    currentRow = null
    resultSetMetaData = null
    closed = true
  }

  override def wasNull = {
    checkClosed
    currentRow.wasNull
  }

  override def getString(columnIndex: Int) = currentRow.getString(columnIndex - 1)

  override def getBoolean(columnIndex: Int) = currentRow.getBoolean(columnIndex - 1)

  override def getByte(columnIndex: Int) = currentRow.getByte(columnIndex - 1)

  override def getShort(columnIndex: Int) = currentRow.getShort(columnIndex - 1)

  override def getInt(columnIndex: Int) = currentRow.getInt(columnIndex - 1)

  override def getLong(columnIndex: Int) = currentRow.getLong(columnIndex - 1)

  override def getFloat(columnIndex: Int) = currentRow.getFloat(columnIndex - 1)

  override def getDouble(columnIndex: Int) = currentRow.getDouble(columnIndex - 1)

  override def getBigDecimal(columnIndex: Int, scale: Int) = currentRow.getAs[java.math.BigDecimal](columnIndex - 1).setScale(scale)

  override def getBytes(columnIndex: Int) = currentRow.getAs[Array[Byte]](columnIndex - 1)

  override def getDate(columnIndex: Int) = new Date(currentRow.getLong(columnIndex - 1))

  override def getTime(columnIndex: Int) = new Time(currentRow.getLong(columnIndex - 1))

  override def getTimestamp(columnIndex: Int) = new Timestamp(currentRow.getLong(columnIndex - 1))

  override def getAsciiStream(columnIndex: Int) = null

  override def getUnicodeStream(columnIndex: Int) = null

  override def getBinaryStream(columnIndex: Int) = null

  override def getString(columnLabel: String) = getString(columnLabel2Index(columnLabel))

  override def getBoolean(columnLabel: String) = getBoolean(columnLabel2Index(columnLabel))

  override def getByte(columnLabel: String) = getByte(columnLabel2Index(columnLabel))

  override def getShort(columnLabel: String) = getShort(columnLabel2Index(columnLabel))

  override def getInt(columnLabel: String) = getInt(columnLabel2Index(columnLabel))

  override def getLong(columnLabel: String) = getLong(columnLabel2Index(columnLabel))

  override def getFloat(columnLabel: String) = getFloat(columnLabel2Index(columnLabel))

  override def getDouble(columnLabel: String) = getDouble(columnLabel2Index(columnLabel))

  override def getBigDecimal(columnLabel: String, scale: Int) = getBigDecimal(columnLabel2Index(columnLabel), scale)

  override def getBytes(columnLabel: String) = getBytes(columnLabel2Index(columnLabel))

  override def getDate(columnLabel: String) = getDate(columnLabel2Index(columnLabel))

  override def getTime(columnLabel: String) = getTime(columnLabel2Index(columnLabel))

  override def getTimestamp(columnLabel: String) = getTimestamp(columnLabel2Index(columnLabel))

  override def getAsciiStream(columnLabel: String) = getAsciiStream(columnLabel2Index(columnLabel))

  override def getUnicodeStream(columnLabel: String) = getUnicodeStream(columnLabel2Index(columnLabel))

  override def getBinaryStream(columnLabel: String) = getBinaryStream(columnLabel2Index(columnLabel))

  override def getWarnings = null

  override def clearWarnings() = {}

  override def getCursorName = ""

  override def getMetaData = resultSetMetaData

  override def getObject(columnIndex: Int) = currentRow.getAs[AnyRef](columnIndex - 1)

  override def getObject(columnLabel: String) = getObject(columnLabel2Index(columnLabel))

  override def findColumn(columnLabel: String) = columnLabel2Index(columnLabel)

  override def getCharacterStream(columnIndex: Int) = null

  override def getCharacterStream(columnLabel: String) = getCharacterStream(columnLabel2Index(columnLabel))

  override def getBigDecimal(columnIndex: Int) = currentRow.getAs[java.math.BigDecimal](columnIndex - 1)

  override def getBigDecimal(columnLabel: String) = getBigDecimal(columnLabel2Index(columnLabel))

  /**
    * Checks if the current position is before the first row, that means next()
    * was not called yet, and there is at least one row.
    */
  override def isBeforeFirst = {
    checkClosed
    rows != null && rows.nonEmpty && currentRowId == -1
  }

  /**
    * Checks if the current position is after the last row, that means next()
    * was called and returned false, and there was at least one row.
    */
  override def isAfterLast = {
    checkClosed
    rows != null && rows.nonEmpty && currentRowId >= totalRows
  }

  /**
    * Checks if the current position is row 1, that means next() was called
    * once and returned true.
    */
  override def isFirst = {
    checkClosed
    rows != null && rows.nonEmpty && currentRowId == 0 && currentRowId < totalRows
  }

  /**
    * Checks if the current position is the last row, that means next() was
    * called and did not yet returned false, but will in the next call.
    */
  override def isLast = {
    checkClosed
    rows != null && rows.nonEmpty && currentRowId == totalRows - 1
  }

  /**
    * Moves the current position to before the first row, that means resets the
    * result set.
    */
  override def beforeFirst() = {
    checkClosed
    if (forwardOnly)
      throw new SQLException("The resultSet is forward only")
    if (currentRowId >= 0)
      currentRowId = -1
  }

  /**
    * Moves the current position to after the last row, that means after the
    * end.
    */
  override def afterLast() = {
    checkClosed
    while (next) {}
  }

  /**
    * Moves the current position to the first row. This is the same as calling
    * beforeFirst() followed by next().
    */
  override def first = {
    checkClosed
    if (currentRowId < 0)
      next
    else {
      beforeFirst()
      next
    }
  }

  /**
    * Moves the current position to the last row.
    */
  override def last = {
    checkClosed
    absolute(-1)
  }

  override def getRow = {
    checkClosed
    if (currentRowId >= totalRows)
      0
    else
      (currentRowId + 1).toInt
  }

  /**
    * Moves the current position to a specific row.
    *
    * @param row the row number. 0 is not allowed, 1 means the first row,
    *            2 the second. -1 means the last row, -2 the row before the
    *            last row. If the value is too large, the position is moved
    *            after the last row, if if the value is too small it is moved
    *            before the first row.
    * @return true if there is a row available, false if not
    */
  override def absolute(row: Int) = {
    checkClosed
    val rowNumber = {
      if (row < 0)
        totalRows + row + 1
      else if (row > totalRows + 1)
        totalRows + 1
      else
        row
    }
    if (currentRowId >= rowNumber)
      beforeFirst()
    while (currentRowId + 1 < rowNumber)
      next
    currentRowId >= 0 && currentRowId < totalRows
  }

  /**
    * Moves the current position to a specific row relative to the current row.
    *
    * @param rows 0 means don't do anything, 1 is the next row, -1 the
    *            previous. If the value is too large, the position is moved
    *             after the last row, if if the value is too small it is moved
    *             before the first row.
    */
  override def relative(rows: Int) = {
    val temp = currentRowId + rows + 1
    val row = {
      if (temp < 0)
        0
      else if (temp > totalRows)
        totalRows + 1
      else
        temp
    }
    absolute(row.toInt)
  }

  /**
    * Moves the cursor to the last row, or row before first row if the current
    * position is the first row.
    */
  override def previous = {
    checkClosed
    relative(-1)
  }

  override def setFetchDirection(direction: Int) = {
    throw new SQLException("Unsupported setFetchDirection")
  }

  override def getFetchDirection = {
    checkClosed
    ResultSet.FETCH_FORWARD
  }

  override def setFetchSize(rows: Int) = {
    checkClosed
    if (rows > 0)
      FETCH_SIZE = rows
  }

  override def getFetchSize = FETCH_SIZE

  // resultSet type
  override def getType = ResultSet.TYPE_FORWARD_ONLY

  override def getConcurrency = 0

  override def rowUpdated = false

  override def rowInserted = false

  override def rowDeleted = false

  override def updateNull(columnIndex: Int) = {
    currentRow.values(columnIndex - 1) = null
  }

  override def updateBoolean(columnIndex: Int, x: Boolean) = {
    currentRow.values(columnIndex - 1) = x
  }

  override def updateByte(columnIndex: Int, x: Byte) = {
    currentRow.values(columnIndex - 1) = x
  }

  override def updateShort(columnIndex: Int, x: Short) = {
    currentRow.values(columnIndex - 1) = x
  }

  override def updateInt(columnIndex: Int, x: Int) = {
    currentRow.values(columnIndex - 1) = x
  }

  override def updateLong(columnIndex: Int, x: Long) = {
    currentRow.values(columnIndex - 1) = x
  }

  override def updateFloat(columnIndex: Int, x: Float) = {
    currentRow.values(columnIndex - 1) = x
  }

  override def updateDouble(columnIndex: Int, x: Double) = {
    currentRow.values(columnIndex - 1) = x
  }

  override def updateBigDecimal(columnIndex: Int, x: BigDecimal) = {
    currentRow.values(columnIndex - 1) = x
  }

  override def updateString(columnIndex: Int, x: String) = {
    currentRow.values(columnIndex - 1) = x
  }

  override def updateBytes(columnIndex: Int, x: scala.Array[Byte]) = {
    currentRow.values(columnIndex - 1) = x
  }

  override def updateDate(columnIndex: Int, x: Date) = {
    currentRow.values(columnIndex - 1) = x.getTime
  }

  override def updateTime(columnIndex: Int, x: Time) = {
    currentRow.values(columnIndex - 1) = x.getTime
  }

  override def updateTimestamp(columnIndex: Int, x: Timestamp) = {
    currentRow.values(columnIndex - 1) = x.getTime
  }

  override def updateAsciiStream(columnIndex: Int, x: InputStream, length: Int) = {}

  override def updateBinaryStream(columnIndex: Int, x: InputStream, length: Int) = {}

  override def updateCharacterStream(columnIndex: Int, x: Reader, length: Int) = {}

  override def updateObject(columnIndex: Int, x: Any, scaleOrLength: Int) = {}

  override def updateObject(columnIndex: Int, x: Any) = {
    currentRow.values(columnIndex - 1) = x
  }

  override def updateNull(columnLabel: String) = {
    updateNull(columnLabel2Index(columnLabel))
  }

  override def updateBoolean(columnLabel: String, x: Boolean) = {
    updateBoolean(columnLabel2Index(columnLabel), x)
  }

  override def updateByte(columnLabel: String, x: Byte) = {
    updateByte(columnLabel2Index(columnLabel), x)
  }

  override def updateShort(columnLabel: String, x: Short) = {
    updateShort(columnLabel2Index(columnLabel), x)
  }

  override def updateInt(columnLabel: String, x: Int) = {
    updateInt(columnLabel2Index(columnLabel), x)
  }

  override def updateLong(columnLabel: String, x: Long) = {
    updateLong(columnLabel2Index(columnLabel), x)
  }

  override def updateFloat(columnLabel: String, x: Float) = {
    updateFloat(columnLabel2Index(columnLabel), x)
  }

  override def updateDouble(columnLabel: String, x: Double) = {
    updateDouble(columnLabel2Index(columnLabel), x)
  }

  override def updateBigDecimal(columnLabel: String, x: BigDecimal) = {
    updateBigDecimal(columnLabel2Index(columnLabel), x)
  }

  override def updateString(columnLabel: String, x: String) = {
    updateString(columnLabel2Index(columnLabel), x)
  }

  override def updateBytes(columnLabel: String, x: scala.Array[Byte]) = {
    updateBytes(columnLabel2Index(columnLabel), x)
  }

  override def updateDate(columnLabel: String, x: Date) = {
    updateDate(columnLabel2Index(columnLabel), x)
  }

  override def updateTime(columnLabel: String, x: Time) = {
    updateTime(columnLabel2Index(columnLabel), x)
  }

  override def updateTimestamp(columnLabel: String, x: Timestamp) = {
    updateTimestamp(columnLabel2Index(columnLabel), x)
  }

  override def updateAsciiStream(columnLabel: String, x: InputStream, length: Int) = {
    updateAsciiStream(columnLabel2Index(columnLabel), x, length)
  }

  override def updateBinaryStream(columnLabel: String, x: InputStream, length: Int) = {
    updateBinaryStream(columnLabel2Index(columnLabel), x, length)
  }

  override def updateCharacterStream(columnLabel: String, reader: Reader, length: Int) = {
    updateCharacterStream(columnLabel2Index(columnLabel), reader, length)
  }

  override def updateObject(columnLabel: String, x: Any, scaleOrLength: Int) = {
    updateObject(columnLabel2Index(columnLabel), x, scaleOrLength)
  }

  override def updateObject(columnLabel: String, x: Any) = {
    updateObject(columnLabel2Index(columnLabel), x)
  }

  override def insertRow() = {}

  override def updateRow() = {}

  override def deleteRow() = {}

  override def refreshRow() = {}

  override def cancelRowUpdates() = {}

  override def moveToInsertRow() = {}

  override def moveToCurrentRow() = {}

  override def getStatement = {
    checkClosed
    stat
  }

  override def getObject(columnIndex: Int, map: util.Map[String, Class[_]]) = getObject(columnIndex)

  override def getRef(columnIndex: Int) = {
    throw new SQLException("unsupported ref")
  }

  override def getBlob(columnIndex: Int) = null

  override def getClob(columnIndex: Int) = null

  override def getArray(columnIndex: Int) = {
    val arr = currentRow.get(columnIndex - 1).asInstanceOf[Array[Any]]
    new JdbcArray(arr)
  }

  override def getObject(columnLabel: String, map: util.Map[String, Class[_]]) = getObject(columnLabel)

  override def getRef(columnLabel: String) = throw new SQLException("unsupported ref")

  override def getBlob(columnLabel: String) = getBlob(columnLabel2Index(columnLabel))

  override def getClob(columnLabel: String) = getClob(columnLabel2Index(columnLabel))

  override def getArray(columnLabel: String) = getArray(columnLabel2Index(columnLabel))

  override def getDate(columnIndex: Int, cal: Calendar) = null

  override def getDate(columnLabel: String, cal: Calendar) = null

  override def getTime(columnIndex: Int, cal: Calendar) = null

  override def getTime(columnLabel: String, cal: Calendar) = null

  override def getTimestamp(columnIndex: Int, cal: Calendar) = null

  override def getTimestamp(columnLabel: String, cal: Calendar) = null

  override def getURL(columnIndex: Int) = null

  override def getURL(columnLabel: String) = getURL(columnLabel2Index(columnLabel))

  override def updateRef(columnIndex: Int, x: Ref) = {}

  override def updateRef(columnLabel: String, x: Ref) = {}

  override def updateBlob(columnIndex: Int, x: Blob) = {}

  override def updateBlob(columnLabel: String, x: Blob) = {}

  override def updateClob(columnIndex: Int, x: Clob) = {}

  override def updateClob(columnLabel: String, x: Clob) = {}

  override def updateArray(columnIndex: Int, x: java.sql.Array) = {}

  override def updateArray(columnLabel: String, x: java.sql.Array) = {}

  override def getRowId(columnIndex: Int) = throw new SQLException("unsupported RowId")

  override def getRowId(columnLabel: String) = throw new SQLException("unsupported RowId")

  override def updateRowId(columnIndex: Int, x: RowId) = {}

  override def updateRowId(columnLabel: String, x: RowId) = {}

  override def getHoldability = 0

  override def isClosed = closed

  override def updateNString(columnIndex: Int, nString: String) = {}

  override def updateNString(columnLabel: String, nString: String) = {}

  override def updateNClob(columnIndex: Int, nClob: NClob) = {}

  override def updateNClob(columnLabel: String, nClob: NClob) = {}

  override def getNClob(columnIndex: Int) = null

  override def getNClob(columnLabel: String) = null

  override def getSQLXML(columnIndex: Int) = null

  override def getSQLXML(columnLabel: String) = null

  override def updateSQLXML(columnIndex: Int, xmlObject: SQLXML) = {}

  override def updateSQLXML(columnLabel: String, xmlObject: SQLXML) = {}

  override def getNString(columnIndex: Int) = getString(columnIndex)

  override def getNString(columnLabel: String) = getString(columnLabel)

  override def getNCharacterStream(columnIndex: Int) = null

  override def getNCharacterStream(columnLabel: String) = null

  override def updateNCharacterStream(columnIndex: Int, x: Reader, length: Long) = {}

  override def updateNCharacterStream(columnLabel: String, reader: Reader, length: Long) = {}

  override def updateAsciiStream(columnIndex: Int, x: InputStream, length: Long) = {}

  override def updateBinaryStream(columnIndex: Int, x: InputStream, length: Long) = {}

  override def updateCharacterStream(columnIndex: Int, x: Reader, length: Long) = {}

  override def updateAsciiStream(columnLabel: String, x: InputStream, length: Long) = {}

  override def updateBinaryStream(columnLabel: String, x: InputStream, length: Long) = {}

  override def updateCharacterStream(columnLabel: String, reader: Reader, length: Long) = {}

  override def updateBlob(columnIndex: Int, inputStream: InputStream, length: Long) = {}

  override def updateBlob(columnLabel: String, inputStream: InputStream, length: Long) = {}

  override def updateClob(columnIndex: Int, reader: Reader, length: Long) = {}

  override def updateClob(columnLabel: String, reader: Reader, length: Long) = {}

  override def updateNClob(columnIndex: Int, reader: Reader, length: Long) = {}

  override def updateNClob(columnLabel: String, reader: Reader, length: Long) = {}

  override def updateNCharacterStream(columnIndex: Int, x: Reader) = {}

  override def updateNCharacterStream(columnLabel: String, reader: Reader) = {}

  override def updateAsciiStream(columnIndex: Int, x: InputStream) = {}

  override def updateBinaryStream(columnIndex: Int, x: InputStream) = {}

  override def updateCharacterStream(columnIndex: Int, x: Reader) = {}

  override def updateAsciiStream(columnLabel: String, x: InputStream) = {}

  override def updateBinaryStream(columnLabel: String, x: InputStream) = {}

  override def updateCharacterStream(columnLabel: String, reader: Reader) = {}

  override def updateBlob(columnIndex: Int, inputStream: InputStream) = {}

  override def updateBlob(columnLabel: String, inputStream: InputStream) = {}

  override def updateClob(columnIndex: Int, reader: Reader) = {}

  override def updateClob(columnLabel: String, reader: Reader) = {}

  override def updateNClob(columnIndex: Int, reader: Reader) = {}

  override def updateNClob(columnLabel: String, reader: Reader) = {}

  override def getObject[T](columnIndex: Int, `type`: Class[T]) = throw new SQLException("unsupported")

  override def getObject[T](columnLabel: String, `type`: Class[T]) = throw new SQLException("unsupported")

  override def unwrap[T](iface: Class[T]) = throw new SQLException("unsupported unwrap")

  override def isWrapperFor(iface: Class[_]) = false
}
