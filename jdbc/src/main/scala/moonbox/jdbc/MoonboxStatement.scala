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

import java.sql._

import moonbox.client.MoonboxClient
import moonbox.client.entity.MoonboxRowSet

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

class MoonboxStatement(connection: MoonboxConnection) extends Statement {

  private val sqlDelimiter = ';'

  private var fetchSize = 1000  /* zero means no limitation */
  private var queryTimeout = 0  /* time unit: ms */
  private var maxFieldSize: Int = 0
  private var maxRows: Int = Int.MinValue
  private var closed: Boolean = false
  private var isResultSet: Boolean = true
  private var canceled: Boolean = false
  private var resultSet: MoonboxResultSet = _
  private var batchSql: Seq[String] = Nil

  def checkClosed(): Unit = {
    if (!closed){
      if (connection != null){
        connection.checkClosed()
      }
    } else throw new SQLException("Statement has already been closed.")
  }
  private def client: MoonboxClient = connection.getSession.moonboxClient
  private def interactiveQuery(sqls: Seq[String]): MoonboxRowSet = {
    checkClosed()
    client.interactiveQuery(sqls, getFetchSize, getMaxRows, queryTimeout)
  }

  private def splitSql(sql: String, splitter: Char): Seq[String] = {
    val stack = new mutable.Stack[Char]()
    val splitIndex = new ArrayBuffer[Int]()
    for ((char, idx) <- sql.toCharArray.zipWithIndex) {
      if (char == splitter) {
        if (stack.isEmpty) splitIndex += idx
      }
      if (char == '(') stack.push('(')
      if (char == ')') stack.pop()
    }
    splits(sql, splitIndex.toArray, 0).map(_.stripPrefix(splitter.toString).trim).filter(_.length > 0)
  }
  private def splits(sql: String, idxs: scala.Array[Int], offset: Int): Seq[String] = {
    if (idxs.nonEmpty) {
      val head = idxs.head
      val (h, t) = sql.splitAt(head - offset)
      h +: splits(t, idxs.tail, head)
    } else sql :: Nil
  }

  override def executeQuery(sql: String): ResultSet = {
    canceled = false
    try {
      val rowSet = interactiveQuery(splitSql(sql, sqlDelimiter))
      isResultSet = !rowSet.isEmptySchema
      resultSet = new MoonboxResultSet(getConnection, this, rowSet)
      resultSet
    } catch {
      case e: Exception => throw new SQLException(s"Execute query failed: ${e.getMessage}")
    }
  }
  override def executeUpdate(sql: String) = throw new SQLException("Unsupported")
  override def close() = {
    if (resultSet != null && !resultSet.isClosed) {
      resultSet.close()
    }
    resultSet = null
    closed = true
  }
  override def getMaxFieldSize = maxFieldSize
  override def setMaxFieldSize(max: Int) = {
    if (max < 0) throw new SQLException("maxFieldSize may not less than zero.")
    maxFieldSize = max
  }
  override def getMaxRows = maxRows
  override def setMaxRows(max: Int) = {
//    if (max < 0) throw new SQLException("maxRows may not less than zero.")
    if (max != 0) {
      maxRows = max
    }
  }
  override def setEscapeProcessing(enable: Boolean) = {}
  override def getQueryTimeout = queryTimeout / 1000
  override def setQueryTimeout(seconds: Int) = {
    queryTimeout = seconds * 1000
  }
  override def cancel() = {
    // TODO: cancel query and avoid to effect other statements
    canceled = client.cancelInteractiveQuery()
  }
  override def getWarnings = null
  override def clearWarnings() = {}
  override def setCursorName(name: String) = {}
  override def execute(sql: String) = {
    checkClosed()
    executeQuery(sql)
    isResultSet
  }
  override def getResultSet = resultSet
  override def getUpdateCount = throw new SQLException("Unsupported")
  override def getMoreResults = false
  override def setFetchDirection(direction: Int) = {}
  override def getFetchDirection = ResultSet.FETCH_FORWARD
  override def setFetchSize(rows: Int) = {
    rows match {
      case v: Int if v < 0 => throw new SQLException("FetchSize may not less than zero")
      case v: Int if getMaxRows > 0 && v > getMaxRows => throw new SQLException("fetchSize may not larger than maxRows")
      case v: Int if v == 0 => /* no-op */
      case other => fetchSize = other
    }
  }
  override def getFetchSize = fetchSize
  override def getResultSetConcurrency = ResultSet.CONCUR_READ_ONLY
  override def getResultSetType = ResultSet.TYPE_FORWARD_ONLY
  override def addBatch(sql: String) = {
    checkClosed()
    batchSql :+= sql
  }
  override def clearBatch() = {
    batchSql = Nil
  }
  override def executeBatch = {
    // TODO: To satisfy the java sql interface
    interactiveQuery(batchSql)
    Seq.empty[Int].toArray
  }
  override def getConnection = {
    checkClosed()
    connection
  }
  override def getMoreResults(current: Int) = false
  override def getGeneratedKeys = throw new SQLException("Unsupported")
  override def executeUpdate(sql: String, autoGeneratedKeys: Int) = 0
  override def executeUpdate(sql: String, columnIndexes: scala.Array[Int]) = 0
  override def executeUpdate(sql: String, columnNames: scala.Array[String]) = 0
  override def execute(sql: String, autoGeneratedKeys: Int) = execute(sql) // TODO:
  override def execute(sql: String, columnIndexes: scala.Array[Int]) = execute(sql) // TODO:
  override def execute(sql: String, columnNames: scala.Array[String]) = execute(sql) // TODO:
  override def getResultSetHoldability = ResultSet.CLOSE_CURSORS_AT_COMMIT
  override def isClosed = closed
  override def setPoolable(poolable: Boolean) = {}
  override def isPoolable = false
  override def closeOnCompletion() = {}
  override def isCloseOnCompletion = false
  override def unwrap[T](iface: Class[T]) = {
    if (isWrapperFor(iface)) this.asInstanceOf[T]
    else throw new SQLException("unwrap exception")
  }
  override def isWrapperFor(iface: Class[_]) = iface != null && iface.isAssignableFrom(getClass)
}
