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
import moonbox.repl.connector.Connector

class JdbcConnector(timeout: Int) extends Connector {
  var connection: Connection = _
  var stmt: Statement = _

  override def prepare(host: String, port: Int, user: String, pwd: String, db: String): Boolean = {
    try {
      Class.forName("moonbox.jdbc.MbDriver")
      val url = s"jdbc:moonbox://${host}:${port}/default"
      val prop = new Properties()
      prop.setProperty("user", user)
      prop.setProperty("password", pwd)
      /*prop.setProperty("fetchsize", 200.toString)*/
      connection = DriverManager.getConnection(url, prop)
      true
    } catch {
      case e: Exception =>
        e.printStackTrace()
        false
    }
  }

  override def process(sqls: Seq[String]): Unit = {
    val numShow = max_count
    stmt = connection.createStatement()
    stmt.setQueryTimeout(timeout)
    stmt.setFetchSize(200)
    val compositedSql = sqls.mkString("", ";", ";") :: Nil
    compositedSql.foreach { sql =>
      if (stmt.execute(sql)) {  //if can get result
        val rs = stmt.getResultSet
        val metaData: ResultSetMetaData = rs.getMetaData
        var dataBuf = Seq[Seq[Any]]()
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
        print(Utils.showString(dataBuf, schema, numShow, truncate))
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

}
