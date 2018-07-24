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

package moonbox.catalyst.core.parser.udf.udfParser

import org.scalatest.{BeforeAndAfterEach, FunSuite}

class MbUDFParserTest extends FunSuite with BeforeAndAfterEach {

  override def beforeEach() {

  }

  override def afterEach() {

  }

  test("transform sql contains udf"){
    val sql = """select group, user.age, array_map(user.age, value => value + 30), array_filter(array_map(user.age, value => value + 30), value => value < 60) from nest_table where array_exists(array_filter(array_map(user.age, value => value + 30), value => value < 60), value => value < 60)"""
    val sql1 = """select group, user.age, array_map(user.age, "x + 30") from nest_table"""
    val sql2 = """select group, user.age, array_map(user.age, "x + 30"), array_filter(array_map(user.age, value => x + 30), "x < 60") from nest_table where array_exists(array_filter(array_map(user.age, "x + 30"), "x < 60"), x => x < 60)"""
    val newSql = ParserUtil.sqlTransform(sql)
    val newSql1 = ParserUtil.sqlTransform(sql1)
    val newSql2 = ParserUtil.sqlTransform(sql2)
    println("new sql: "+newSql)
    println("new sql: "+newSql1)
    println("new sql: "+newSql2)
  }
}
