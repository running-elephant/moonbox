package moonbox.jdbc

import java.sql._

import moonbox.client.JdbcClient
import moonbox.common.message._

class MoonboxStatement(connection: MoonboxConnection) extends Statement {

  var fetchSize = 1000
  var queryTimeout = 1000 * 60 * 30

  var dataFetchId: Long = _
  var totalRows: Long = _
  var maxFieldSize: Int = _
  var jdbcSession: JdbcSession = connection.getSession()
  var resultSet: MoonboxResultSet = _
  var client: JdbcClient = _
  var currentQueryId: Long = _
  var maxRows: Int = 0
  var updateCount: Int = 0
  var closed: Boolean = false
  var isResultSet: Boolean = true
  var canceled: Boolean = false

  /**
    * Check if the statement is closed.
    *
    * @return true if a reconnect was required
    */
  def checkClosed(): Boolean = {
    if (connection == null)
      throw new SQLException("Exception while execute query, because the connection is null value")
    else {
      connection.checkClosed()
      if (jdbcSession != connection.getSession()) {
        jdbcSession = connection.getSession()
        true
      } else false
    }
  }

  private def beforeAction(): Unit = {
    canceled = false
    if (client == null) {
      synchronized {
        if (client == null) {
          client = jdbcSession.jdbcClient
        }
      }
    }
  }

  override def executeQuery(sql: String): ResultSet = {
    checkClosed()
    beforeAction()
    currentQueryId = client.getMessageId()
    val message = JdbcQueryInbound(currentQueryId, getFetchSize, sql)
    val resp = client.sendAndReceive(message, getQueryTimeout)
    jdbcMessage2ResultSet(message, resp)
  }

  def jdbcMessage2ResultSet(send: JdbcInboundMessage, response: Any): ResultSet = {
    response match {
      case resp: JdbcQueryOutbound =>
        if (resp.err.isDefined) {
          // Received an error message, then throw an exception
          throw new SQLException(s"${resp.err.get}")
          // TODO: Or retry several times (retransmit the query message) ?
        } else {
          // TODO:
          if (resp.schema.isEmpty) {
            isResultSet = false
          }else{
            isResultSet = true
          }
          resultSet = new MoonboxResultSet(connection, this, resp.data.orNull, resp.schema.orNull)
          resultSet.updateResultSet(resp)
          resultSet
        }
      case dataFetch: DataFetchOutbound =>
        if (dataFetch.err.isDefined) {
          // Received an error message, then throw an exception
          throw new SQLException(s"sql query error: ${dataFetch.err.get}")
          // TODO: Or retry several times (retransmit the query message) ?
        } else {
          val fetchState = dataFetch.dataFetchState
          dataFetchId = fetchState.messageId
          totalRows = fetchState.totalRows
          resultSet = new MoonboxResultSet(connection, this, dataFetch.data.orNull, dataFetch.schema.orNull)
          resultSet.updateResultSet(dataFetch)
          resultSet
        }
      //      case cancel: JdbcCancelOutbound =>
      //        cancel.error match {
      //          case None => throw new SQLException(s"Query Canceled successfully")
      //          case Some(err) => throw new SQLException(s"Query Cancel failed: $err")
      //        }
      case null => throw new SQLException("sql query error or timeout")
      case _ => throw new SQLException("Response message type error for sql query") // TODO: retry or not ?
    }
  }

  override def executeUpdate(sql: String) = 0

  override def close() = {
    if (resultSet != null && !resultSet.isClosed) {
      resultSet.close()
    }
    resultSet = null
    jdbcSession = null
    closed = true
  }

  override def getMaxFieldSize = maxFieldSize

  override def setMaxFieldSize(max: Int) = {
    checkClosed()
    if (max > 0)
      maxFieldSize = max
  }

  override def getMaxRows = maxRows

  override def setMaxRows(max: Int) = {
    checkClosed()
    if (max > 0)
      maxRows = max
  }

  override def setEscapeProcessing(enable: Boolean) = {}

  override def getQueryTimeout = queryTimeout

  override def setQueryTimeout(seconds: Int) = {
    queryTimeout = seconds * 1000
  }

  override def cancel() = {
    beforeAction()
    val msgId = client.getMessageId()
    val cancelMessage = JdbcCancelInbound(msgId, currentQueryId)
    val cancelResp = client.sendAndReceive(cancelMessage, getQueryTimeout)
    cancelResp match {
      case JdbcCancelOutbound(_, Some(error), _) => throw new SQLException(s"Cancel query failed: $error")
      case JdbcCancelOutbound(_, None, Some(state)) => if (!state) throw new SQLException(s"Cancel query failed")
      case other => throw new SQLException(s"Cancel query error: $other")
    }
    canceled = true
  }

  override def getWarnings = null

  override def clearWarnings() = {}

  override def setCursorName(name: String) = {}

  override def execute(sql: String) = {
    checkClosed()
    executeQuery(sql)
    isResultSet
  }

  override def getResultSet = {
    checkClosed()
    resultSet
  }

  override def getUpdateCount = updateCount

  override def getMoreResults = false

  override def setFetchDirection(direction: Int) = {}

  override def getFetchDirection = 0

  override def setFetchSize(rows: Int) = {
    checkClosed()
    if (rows > 0 && maxRows > 0 && rows > maxRows)
      throw new SQLException("fetchSize may not larger than maxRows")
    fetchSize = rows
  }

  override def getFetchSize = {
    checkClosed()
    fetchSize
  }

  override def getResultSetConcurrency = 0

  override def getResultSetType = 0

  override def addBatch(sql: String) = {}

  override def clearBatch() = {}

  override def executeBatch = null

  override def getConnection = {
    checkClosed()
    connection
  }

  override def getMoreResults(current: Int) = false

  override def getGeneratedKeys = null

  override def executeUpdate(sql: String, autoGeneratedKeys: Int) = 0

  override def executeUpdate(sql: String, columnIndexes: scala.Array[Int]) = 0

  override def executeUpdate(sql: String, columnNames: scala.Array[String]) = 0

  override def execute(sql: String, autoGeneratedKeys: Int) = false

  override def execute(sql: String, columnIndexes: scala.Array[Int]) = false

  override def execute(sql: String, columnNames: scala.Array[String]) = false

  override def getResultSetHoldability = 0

  override def isClosed = closed

  override def setPoolable(poolable: Boolean) = {}

  override def isPoolable = false

  override def closeOnCompletion() = {}

  override def isCloseOnCompletion = false

  override def unwrap[T](iface: Class[T]) = null.asInstanceOf[T]

  override def isWrapperFor(iface: Class[_]) = false
}
