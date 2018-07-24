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

class MoonboxResultSetMetaDataTest extends FunSuite with BeforeAndAfterAll {
  var connection: Connection = _
  var stmt: Statement = _
  var res: ResultSet = _
  var sql: String = _
  var url: String = _
  var meta: ResultSetMetaData = _

  override def beforeAll() {
    Class.forName("moonbox.jdbc.MbDriver")
    url = "jdbc:moonbox://localhost:8080/database"
    sql = "select * from aaa"
    val prop = new Properties()
    prop.setProperty(MoonboxJDBCUtils.FETCH_SIZE, 200.toString)
    prop.setProperty(MoonboxJDBCUtils.USER_KEY, "ROOT")
    prop.setProperty(MoonboxJDBCUtils.PASSWORD_KEY, "123456")
    prop.setProperty("table", "books")
    connection = DriverManager.getConnection(url, prop)
    stmt = connection.createStatement()
    res = stmt.executeQuery(sql)
    meta = res.getMetaData
  }

  override def afterAll() {

  }

  test("testGetCatalogName") {
    assert(meta.getCatalogName(1) == "database")
  }

  test("testGetColumnLabel") {
    assert("a" == meta.getColumnLabel(1))
  }

  test("testGetColumnName") {
    assert("a" == meta.getColumnName(1))
  }

  test("testGetTableName") {
    assert("books" == meta.getTableName(1))
  }

  test("testGetColumnType") {
    assert(java.sql.Types.VARCHAR == meta.getColumnType(1))
  }

}
