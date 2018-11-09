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

package moonbox.repl.connector.jdbc

import java.sql.{Connection, DriverManager, ResultSetMetaData, Statement}
import java.util.Properties

import moonbox.repl.Utils
import moonbox.repl.connector.{ConnectionState, ConnectionType, Connector, RuntimeMode}

import scala.collection.mutable.ArrayBuffer

class JdbcConnector(_timeout: Int, isLocal: Boolean) extends Connector {
  var connection: Connection = _
  var stmt: Statement = _
  var _connectionState: ConnectionState = _
  var _fetchSize = if (isLocal) 200 else 50

  override def prepare(host: String, port: Int, user: String, pwd: String, db: String): Boolean = {
    try {
      Class.forName("moonbox.jdbc.MbDriver")
      val url = s"jdbc:moonbox://${host}:${port}/default"
      val mode = if (isLocal) "local" else "cluster"
      val prop = new Properties()
      prop.setProperty("user", user)
      prop.setProperty("password", pwd)
      prop.setProperty("mode", mode)
      connection = DriverManager.getConnection(url, prop)
      _connectionState =
        ConnectionState(host = "unknown",
          port = 0,
          connectionType = ConnectionType.JDBC,
          runtimeMode = if (isLocal) RuntimeMode.LOCAL else RuntimeMode.CLUSTER,
          username = user,
          token = "unknown",
          sessionId = "unknown",
          timeout = Utils.secondToMs(_timeout),
          fetchSize = _fetchSize,
          maxColumnLength = this.truncate,
          maxRowsShow = this.max_count
        )
      true
    } catch {
      case e: Exception =>
        e.printStackTrace()
        false
    }
  }

  override def process(sqls: Seq[String]): Unit = {
    val numShow = _connectionState.maxRowsShow
    stmt = connection.createStatement()
    stmt.setQueryTimeout(_connectionState.timeout)
    stmt.setFetchSize(_connectionState.fetchSize.toInt)
    val compositedSql = sqls.mkString("", ";", ";") :: Nil
    compositedSql.foreach { sql =>
      if (stmt.execute(sql)) {  //if can get result
        val rs = stmt.getResultSet
        val metaData: ResultSetMetaData = rs.getMetaData
        var dataBuf = ArrayBuffer[Seq[Any]]()
        val columnCount = metaData.getColumnCount
        val schema = (1 to columnCount).map { index =>
          val name = metaData.getColumnName(index)
          val typ = metaData.getColumnTypeName(index)
          s"$name($typ)"
        }
        var rowCount = 0
        while (rs.next() && rowCount < numShow) {
          val colData: Seq[Any] = (1 to columnCount).map { index =>
            rs.getObject(index)
          }
          dataBuf :+= colData
          rowCount += 1
        }
        print(Utils.showString(dataBuf, schema, numShow, _connectionState.maxColumnLength))
      }
    }
  }

  override def close(): Unit = {
    if (connection != null) {
      connection.close()
      connection = null
    }
  }

  override def shutdown(): Unit = {
    /* need to do nothing */
  }

  override def cancel(): Unit = {
    if(stmt != null){
        stmt.cancel()
    }
  }

  override def connectionState = _connectionState
}
