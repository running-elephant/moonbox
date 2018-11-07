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

import java.sql._

import moonbox.client.JdbcClient
import moonbox.protocol.client._
import moonbox.util.MoonboxJDBCUtils

class MoonboxStatement(connection: MoonboxConnection) extends Statement {
  var fetchSize = 1000
  var queryTimeout = 1000 * 60 * 30
  var maxFieldSize: Int = _
  var jdbcSession: JdbcSession = connection.getSession()
  var resultSet: MoonboxResultSet = _
  var client: JdbcClient = _
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
    val message = InteractiveQueryInbound(null, null, MoonboxJDBCUtils.splitSql(sql, ';'), getFetchSize).setId(client.genMessageId)
    val resp = client.sendAndReceive(message, getQueryTimeout)
    val outbound = resp match {
      case interactiveQueryOutbound@InteractiveQueryOutbound(error, hasResult, resultData) =>
        if (error.isDefined) {
          throw new SQLException(s"Execute query error: ${error.get}")
        }
        if (resultData.isEmpty) {
          isResultSet = false
          val emptyData = Seq.empty
          val emptySchema =
            """
              |{
              |  "type": "struct",
              |  "fields": []
              |}
            """.stripMargin
          InteractiveQueryOutbound(error, hasResult, Some(ResultData(null, emptySchema, emptyData, hasNext = false)))
        } else {
          isResultSet = true
          interactiveQueryOutbound
        }
      case other => throw new SQLException(s"Execute query error: $other")
    }
    val data = outbound.data.get
    resultSet = new MoonboxResultSet(getConnection, this, data.data, data.schema, data.hasNext, Some(data.cursor))
    resultSet
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
    if (max < 0) {
      throw new SQLException(s"Invalid maxRows value: $max")
    } else {
      maxRows = max
    }
  }

  override def setEscapeProcessing(enable: Boolean) = {}

  override def getQueryTimeout = queryTimeout

  override def setQueryTimeout(seconds: Int) = {
    queryTimeout = seconds * 1000
  }

  override def cancel() = {
    beforeAction()
    val cancelMessage = CancelQueryInbound(null, None, None).setId(client.genMessageId)
    val cancelResp = client.sendAndReceive(cancelMessage, getQueryTimeout)
    cancelResp match {
      case CancelQueryOutbound(Some(error)) => throw new SQLException(s"Cancel query failed: $error")
      case CancelQueryOutbound(None) =>
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
