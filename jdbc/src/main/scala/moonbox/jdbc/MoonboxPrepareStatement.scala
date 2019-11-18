/*
package moonbox.jdbc

import java.io.{InputStream, Reader}
import java.math.BigDecimal
import java.net.URL
import java.sql._
import java.util.Calendar


class MoonboxPrepareStatement(statement: Statement, sql: String) extends PreparedStatement {

  override def setByte(parameterIndex: Int, x: Byte): Unit = throw new SQLException("Unsupported")

  override def getParameterMetaData: ParameterMetaData = throw new SQLException("Unsupported")

  override def setRef(parameterIndex: Int, x: Ref): Unit = throw new SQLException("Unsupported")

  override def clearParameters(): Unit = throw new SQLException("Unsupported")

  override def setBytes(parameterIndex: Int, x: scala.Array[Byte]): Unit = throw new SQLException("Unsupported")

  override def setBinaryStream(parameterIndex: Int, x: InputStream, length: Int): Unit = throw new SQLException("Unsupported")

  override def setBinaryStream(parameterIndex: Int, x: InputStream, length: Long): Unit = throw new SQLException("Unsupported")

  override def setBinaryStream(parameterIndex: Int, x: InputStream): Unit = throw new SQLException("Unsupported")

  override def setAsciiStream(parameterIndex: Int, x: InputStream, length: Int): Unit = throw new SQLException("Unsupported")

  override def setAsciiStream(parameterIndex: Int, x: InputStream, length: Long): Unit = throw new SQLException("Unsupported")

  override def setAsciiStream(parameterIndex: Int, x: InputStream): Unit = throw new SQLException("Unsupported")

  override def setObject(parameterIndex: Int, x: scala.Any, targetSqlType: Int): Unit = throw new SQLException("Unsupported")

  override def setObject(parameterIndex: Int, x: scala.Any): Unit = throw new SQLException("Unsupported")

  override def setObject(parameterIndex: Int, x: scala.Any, targetSqlType: Int, scaleOrLength: Int): Unit = throw new SQLException("Unsupported")

  override def setDate(parameterIndex: Int, x: Date): Unit = throw new SQLException("Unsupported")

  override def setDate(parameterIndex: Int, x: Date, cal: Calendar): Unit = throw new SQLException("Unsupported")

  override def setTimestamp(parameterIndex: Int, x: Timestamp): Unit = throw new SQLException("Unsupported")

  override def setTimestamp(parameterIndex: Int, x: Timestamp, cal: Calendar): Unit = throw new SQLException("Unsupported")

  override def setUnicodeStream(parameterIndex: Int, x: InputStream, length: Int): Unit = throw new SQLException("Unsupported")

  override def getMetaData: ResultSetMetaData = throw new SQLException("Unsupported")

  override def setBlob(parameterIndex: Int, x: Blob): Unit = throw new SQLException("Unsupported")

  override def setBlob(parameterIndex: Int, inputStream: InputStream, length: Long): Unit = throw new SQLException("Unsupported")

  override def setBlob(parameterIndex: Int, inputStream: InputStream): Unit = throw new SQLException("Unsupported")

  override def addBatch(): Unit = statement.addBatch(sql)

  override def execute(): Boolean = {
    statement.execute(sql)
  }

  override def executeQuery(): ResultSet = statement.executeQuery(sql)

  override def setNClob(parameterIndex: Int, value: NClob): Unit = throw new SQLException("Unsupported")

  override def setNClob(parameterIndex: Int, reader: Reader, length: Long): Unit = throw new SQLException("Unsupported")

  override def setNClob(parameterIndex: Int, reader: Reader): Unit = throw new SQLException("Unsupported")

  override def setArray(parameterIndex: Int, x: Array): Unit = throw new SQLException("Unsupported")

  override def setNCharacterStream(parameterIndex: Int, value: Reader, length: Long): Unit = throw new SQLException("Unsupported")

  override def setNCharacterStream(parameterIndex: Int, value: Reader): Unit = throw new SQLException("Unsupported")

  override def setURL(parameterIndex: Int, x: URL): Unit = throw new SQLException("Unsupported")

  override def setRowId(parameterIndex: Int, x: RowId): Unit = throw new SQLException("Unsupported")

  override def setSQLXML(parameterIndex: Int, xmlObject: SQLXML): Unit = throw new SQLException("Unsupported")

  override def setString(parameterIndex: Int, x: String): Unit = throw new SQLException("Unsupported")

  override def setFloat(parameterIndex: Int, x: Float): Unit = throw new SQLException("Unsupported")

  override def setNString(parameterIndex: Int, value: String): Unit = throw new SQLException("Unsupported")

  override def setBoolean(parameterIndex: Int, x: Boolean): Unit = throw new SQLException("Unsupported")

  override def setDouble(parameterIndex: Int, x: Double): Unit = throw new SQLException("Unsupported")

  override def setBigDecimal(parameterIndex: Int, x: BigDecimal): Unit = throw new SQLException("Unsupported")

  override def executeUpdate(): Int = statement.executeUpdate(sql)

  override def setTime(parameterIndex: Int, x: Time): Unit = throw new SQLException("Unsupported")

  override def setTime(parameterIndex: Int, x: Time, cal: Calendar): Unit = throw new SQLException("Unsupported")

  override def setShort(parameterIndex: Int, x: Short): Unit = throw new SQLException("Unsupported")

  override def setLong(parameterIndex: Int, x: Long): Unit = throw new SQLException("Unsupported")

  override def setCharacterStream(parameterIndex: Int, reader: Reader, length: Int): Unit = throw new SQLException("Unsupported")

  override def setCharacterStream(parameterIndex: Int, reader: Reader, length: Long): Unit = throw new SQLException("Unsupported")

  override def setCharacterStream(parameterIndex: Int, reader: Reader): Unit = throw new SQLException("Unsupported")

  override def setClob(parameterIndex: Int, x: Clob): Unit = throw new SQLException("Unsupported")

  override def setClob(parameterIndex: Int, reader: Reader, length: Long): Unit = throw new SQLException("Unsupported")

  override def setClob(parameterIndex: Int, reader: Reader): Unit = throw new SQLException("Unsupported")

  override def setNull(parameterIndex: Int, sqlType: Int): Unit = throw new SQLException("Unsupported")

  override def setNull(parameterIndex: Int, sqlType: Int, typeName: String): Unit = throw new SQLException("Unsupported")

  override def setInt(parameterIndex: Int, x: Int): Unit = throw new SQLException("Unsupported")

  override def setMaxFieldSize(max: Int): Unit = statement.setMaxFieldSize(max)

  override def getMoreResults: Boolean = statement.getMoreResults

  override def getMoreResults(current: Int): Boolean = statement.getMoreResults(current)

  override def clearWarnings(): Unit = statement.clearWarnings()

  override def getGeneratedKeys: ResultSet = statement.getGeneratedKeys

  override def closeOnCompletion(): Unit = statement.closeOnCompletion()

  override def cancel(): Unit = statement.cancel()

  override def getResultSet: ResultSet = statement.getResultSet

  override def setPoolable(poolable: Boolean): Unit = statement.setPoolable(poolable)

  override def isPoolable: Boolean = statement.isPoolable

  override def setCursorName(name: String): Unit = statement.setCursorName(name)

  override def getUpdateCount: Int = statement.getUpdateCount

  override def addBatch(sql: String): Unit = statement.addBatch(sql)

  override def getMaxRows: Int = statement.getMaxRows

  override def execute(sql: String): Boolean = statement.execute(sql)

  override def execute(sql: String, autoGeneratedKeys: Int): Boolean = execute(sql)

  override def execute(sql: String, columnIndexes: scala.Array[Int]): Boolean = execute(sql)

  override def execute(sql: String, columnNames: scala.Array[String]): Boolean = execute(sql)

  override def executeQuery(sql: String): ResultSet = statement.executeQuery(sql)

  override def getResultSetType: Int = statement.getResultSetType

  override def setMaxRows(max: Int): Unit = statement.setMaxRows(max)

  override def getFetchSize: Int = statement.getFetchSize

  override def getResultSetHoldability: Int = statement.getResultSetHoldability

  override def setFetchDirection(direction: Int): Unit = statement.setFetchDirection(direction)

  override def getFetchDirection: Int = statement.getFetchDirection

  override def getResultSetConcurrency: Int = statement.getResultSetConcurrency

  override def clearBatch(): Unit = statement.clearBatch()

  override def close(): Unit = statement.close()

  override def isClosed: Boolean = statement.isClosed

  override def executeUpdate(sql: String): Int = statement.executeUpdate(sql)

  override def executeUpdate(sql: String, autoGeneratedKeys: Int): Int = executeUpdate(sql)

  override def executeUpdate(sql: String, columnIndexes: scala.Array[Int]): Int = executeUpdate(sql)

  override def executeUpdate(sql: String, columnNames: scala.Array[String]): Int = executeUpdate(sql)

  override def getQueryTimeout: Int = statement.getQueryTimeout

  override def getWarnings: SQLWarning = statement.getWarnings

  override def setFetchSize(rows: Int): Unit = statement.setFetchSize(rows)

  override def setQueryTimeout(seconds: Int): Unit = statement.setQueryTimeout(seconds)

  override def executeBatch() = statement.executeBatch()

  override def setEscapeProcessing(enable: Boolean): Unit = statement.setEscapeProcessing(enable)

  override def getConnection: Connection = statement.getConnection

  override def getMaxFieldSize: Int = statement.getMaxFieldSize

  override def isCloseOnCompletion: Boolean = statement.isCloseOnCompletion

  override def unwrap[T](iface: Class[T]): T = {
    if (isWrapperFor(iface)) this.asInstanceOf[T]
    else throw new SQLException("unwrap exception")
  }

  override def isWrapperFor(iface: Class[_]): Boolean =
    iface != null && iface.isAssignableFrom(getClass)
}
*/
