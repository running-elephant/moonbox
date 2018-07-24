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
import java.util.Properties

import moonbox.util.MoonboxJDBCUtils
import org.scalatest.{BeforeAndAfterAll, FunSuite}

import scala.compat.Platform

class Test extends FunSuite with BeforeAndAfterAll {

  var conn: Connection = _
  var stmt: Statement = _
  var res: ResultSet = _
  var meta: ResultSetMetaData = _
  val url = "jdbc:moonbox://127.0.0.1:10010/default"
  val sql = "select * from mysql_test_booklist"
  val prop = new Properties()
  prop.setProperty(MoonboxJDBCUtils.FETCH_SIZE, 200.toString)
  prop.setProperty(MoonboxJDBCUtils.USER_KEY, "sally")
  prop.setProperty(MoonboxJDBCUtils.PASSWORD_KEY, "123456")

  test("test") {
    val t1 = System.currentTimeMillis()
    conn = DriverManager.getConnection(url, prop)
    val t2 = System.currentTimeMillis()
    stmt = conn.createStatement()
    val t3 = System.currentTimeMillis()
    stmt.setFetchSize(10)
    res = stmt.executeQuery(sql)
    val t4 = Platform.currentTime
    println(s"Get connection spend (${t2 - t1} ms)")
    println(s"Get statement spend (${t3 - t2} ms)")
    println(s"Execute query('$sql') spend (${t4 - t3} ms)")
    printResultSet(res)
    println("----------------------------------------")
    stmt.setFetchSize(5)
    val res2 = stmt.executeQuery(sql + " limit 5")
    printResultSet(res2)
  }

  def printResultSet(res: ResultSet): Unit = {
    meta = res.getMetaData
    val fieldCount = meta.getColumnCount
    println(s"Column count: $fieldCount")
    println(s"Column 1 label: ${meta.getColumnLabel(1)}")
    println(s"Column 1 type: ${meta.getColumnType(1)}")
    println(s"Column 1 typeName: ${meta.getColumnTypeName(1)}")
    while (res.next()) {
      var seq = Seq.empty[String]
      var index = 1
      while (index <= fieldCount) {
        seq :+= res.getObject(index).toString
        index += 1
      }
      println(seq.mkString(", "))
    }
  }

  override protected def beforeAll() = {
    Class.forName("moonbox.jdbc.MbDriver")
  }

  override protected def afterAll() = {
    res.close()
    stmt.close()
    conn.close()
  }
}
