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

import java.sql.{Blob, CallableStatement, Clob, Connection, DatabaseMetaData, NClob, PreparedStatement, SQLException, SQLWarning, SQLXML, Savepoint, Statement, Struct}
import java.util.{Locale, Properties}
import java.util.concurrent.Executor
import java.{sql, util}

import moonbox.client.JdbcClient
import moonbox.protocol.client._
import org.apache.commons.codec.digest.DigestUtils

class MoonboxConnection(url: String, props: Properties) extends java.sql.Connection {

  import moonbox.util.MoonboxJDBCUtils._

  private var jdbcSession: JdbcSession = _
  var statement: MoonboxStatement = _
  var closed: Boolean = _
  var database: String = _
  var networkTimeout: Int = 1000 * 60 * 3
  var DEFAULT_USER_CHECK_TIMEOUT = 1000 * 60 * 5
  var runMode = "local"

  private def isLocal: Boolean = {
    runMode.equalsIgnoreCase("local")
  }

  private def openSession(client: JdbcClient, username: String, password: String, newProps: Properties): Boolean = {
    val table = {
      val tb = newProps.getProperty("table")
      if (tb != null) tb
      else newProps.getProperty("collection")
    }
    if (database == null) {
      database = newProps.getProperty(DB_NAME)
    }
    val message = OpenSessionInbound(null, Some(database), isLocal).setId(client.genMessageId)
    val resp = client.sendAndReceive(message)
    resp match {
      case OpenSessionOutbound(_, None) =>
        initSession(client, database, table, username, password, isLocal, newProps)
        true
      case other =>
        client.close()
        throw new SQLException(s"Get moonbox connection failed: $other")
    }
  }

  private def clientGen(host: String, port: Int, username: String, password: String): JdbcClient = {
    val client = new JdbcClient(host, port)
    client.connect()
    val (newClient, errorMessage) = if (client.isConnected) {
      val loginOutbound = client.sendAndReceive(LoginInbound(username, password).setId(client.genMessageId))
      loginOutbound match {
        case LoginOutbound(Some(_), None) =>
          val resp = client.sendAndReceive(RequestAccessInbound(None, isLocal).setId(client.genMessageId), DEFAULT_USER_CHECK_TIMEOUT)
          client.close()
          resp match {
            case RequestAccessOutbound(Some(address), None) =>
              val (host, port) = splitHostPort(address)
              (Some(new JdbcClient(host, port)), None)
            case other => (None, Some(s"Get moonbox connection failed: $other"))
          }
        case other: LoginOutbound => (None, Some(s"Get moonbox connection failed: error=${other.error}"))
        case e => (None, Some(s"Get moonbox connection failed: $e"))
      }
    } else (None, Some("Cannot connect to moonbox."))
    newClient.getOrElse(throw new SQLException(errorMessage.orNull))
  }

  def userCheck(): Boolean = {
    var flag = false
    val newProps = parseURL(url, props)
    val username = newProps.getProperty(USER_KEY)
    val pwd = newProps.getProperty(PASSWORD_KEY)
    runMode = Option(newProps.getProperty(MODE_KEY)).getOrElse("local")
    val (host, port) = parseHostsAndPorts(newProps.getProperty(HOSTS_AND_PORTS)).map { case (h, p) => (h, p.toInt) }.head
    val client = clientGen(host, port, username, pwd)
    client.connect()
    if (client.isConnected) {
      val resp = client.sendAndReceive(LoginInbound(username, pwd).setId(client.genMessageId), DEFAULT_USER_CHECK_TIMEOUT)
      resp match {
        case LoginOutbound(_, None) =>
          flag = openSession(client, username, pwd, newProps)
        case other =>
          client.close()
          throw new SQLException(s"Get moonbox connection failed: $other")
      }
    }
    flag
  }

  private def initSession(jdbcClient: JdbcClient, database: String, table: String, username: String, pwd: String, isLocal: Boolean, props: Properties): Unit = {
    if (pwd != null && pwd.length > 0)
      jdbcSession = JdbcSession(jdbcClient, database, table, username, DigestUtils.md5Hex(pwd), props, isLocal)
    else
      jdbcSession = JdbcSession(jdbcClient, database, table, username, pwd, props, isLocal)
    closed = false
  }

  def getSession(): JdbcSession = jdbcSession

  override def commit(): Unit = throw new SQLException("Unsupported")

  override def getHoldability: Int = 1

  override def setCatalog(catalog: String): Unit = {
    this.createStatement().executeQuery(s"use $catalog")
    database = catalog
    jdbcSession = jdbcSession.copy(database = catalog)
  }

  override def setHoldability(holdability: Int): Unit = {}

  override def prepareStatement(sql: String): PreparedStatement = throw new SQLException("Unsupported")

  override def prepareStatement(sql: String, resultSetType: Int, resultSetConcurrency: Int): PreparedStatement = throw new SQLException("Unsupported")

  override def prepareStatement(sql: String, resultSetType: Int, resultSetConcurrency: Int, resultSetHoldability: Int): PreparedStatement = throw new SQLException("Unsupported")

  override def prepareStatement(sql: String, autoGeneratedKeys: Int): PreparedStatement = throw new SQLException("Unsupported")

  override def prepareStatement(sql: String, columnIndexes: Array[Int]): PreparedStatement = throw new SQLException("Unsupported")

  override def prepareStatement(sql: String, columnNames: Array[String]): PreparedStatement = throw new SQLException("Unsupported")

  override def createClob(): Clob = throw new SQLException("Unsupported")

  override def setSchema(schema: String): Unit = {}

  override def setClientInfo(name: String, value: String): Unit = {}

  override def setClientInfo(properties: Properties): Unit = {}

  override def createSQLXML(): SQLXML = throw new SQLException("Unsupported")

  override def getCatalog: String = database

  override def createBlob(): Blob = throw new SQLException("Unsupported")

  def checkClosed(): Unit = {
    if (isClosed()) {
      throw new SQLException("Connection is already closed.")
    }
  }

  override def createStatement(): Statement = {
    checkClosed()
    statement = new MoonboxStatement(this)
    statement
  }

  override def createStatement(resultSetType: Int, resultSetConcurrency: Int): Statement = createStatement()

  override def createStatement(resultSetType: Int, resultSetConcurrency: Int, resultSetHoldability: Int): Statement = createStatement()

  override def abort(executor: Executor): Unit = this.close()

  override def setAutoCommit(autoCommit: Boolean): Unit = {}

  override def getMetaData: DatabaseMetaData = {
    new MoonboxDatabaseMetaData(this)
  }

  override def setReadOnly(readOnly: Boolean): Unit = {}

  override def prepareCall(sql: String): CallableStatement = throw new SQLException("Unsupported")

  override def prepareCall(sql: String, resultSetType: Int, resultSetConcurrency: Int): CallableStatement = throw new SQLException("Unsupported")

  override def prepareCall(sql: String, resultSetType: Int, resultSetConcurrency: Int, resultSetHoldability: Int): CallableStatement = throw new SQLException("Unsupported")

  override def setTransactionIsolation(level: Int): Unit = {}

  override def getWarnings: SQLWarning = {
    throw new SQLException("Unsupported")
  }

  override def releaseSavepoint(savepoint: Savepoint): Unit = throw new SQLException("Unsupported")

  override def nativeSQL(sql: String): String = throw new SQLException("Unsupported")

  override def isReadOnly: Boolean = false

  override def createArrayOf(typeName: String, elements: Array[AnyRef]): sql.Array = throw new SQLException("Unsupported")

  override def setSavepoint(): Savepoint = {
    throw new SQLException("Unsupported")
  }

  override def setSavepoint(name: String): Savepoint = {
    throw new SQLException("Unsupported")
  }

  override def close(): Unit = {
    if (statement != null && !statement.isClosed) {
      statement.close()
    }
    statement = null
    if (jdbcSession != null || !jdbcSession.isClosed()) {
      jdbcSession.close()
    }
    jdbcSession = null
    closed = true
  }

  override def createNClob(): NClob = throw new SQLException("Unsupported")

  override def rollback(): Unit = {}

  override def rollback(savepoint: Savepoint): Unit = {}

  override def setNetworkTimeout(executor: Executor, milliseconds: Int): Unit = {
    networkTimeout = milliseconds
  }

  override def setTypeMap(map: util.Map[String, Class[_]]): Unit = {
  }

  override def isValid(timeout: Int): Boolean = {
    if (jdbcSession == null || isClosed()) false else true
  }

  override def getAutoCommit: Boolean = false

  override def clearWarnings(): Unit = {}

  override def getSchema: String = throw new SQLException("Unsupported")

  override def getNetworkTimeout: Int = {
    networkTimeout
  }

  override def isClosed(): Boolean = {
    synchronized {
      if (jdbcSession == null || jdbcSession.isClosed()) {
        closed = true
      }
      closed
    }
  }

  override def getTransactionIsolation: Int = {
    Connection.TRANSACTION_NONE
  }

  override def createStruct(typeName: String, attributes: Array[AnyRef]): Struct = throw new SQLException("Unsupported")

  override def getClientInfo(name: String): String = throw new SQLException("Unsupported")

  override def getClientInfo: Properties = throw new SQLException("Unsupported")

  override def getTypeMap: util.Map[String, Class[_]] = throw new SQLException("Unsupported")

  override def unwrap[T](iface: Class[T]): T = {
    if (isWrapperFor(iface))
      this.asInstanceOf[T]
    else
      throw new SQLException("unwrap exception")
  }

  override def isWrapperFor(iface: Class[_]): Boolean = iface != null && iface.isAssignableFrom(getClass)
}
