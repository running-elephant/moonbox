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

package moonbox.util

import moonbox.util.SchemaUtil._
import org.scalatest.{BeforeAndAfterAll, FunSuite}

class SchemaUtilTest extends FunSuite with BeforeAndAfterAll {

  val json = """{"type":"struct","fields":[{"name":"a","type":"string","nullable":false,"metadata":{}},{"name":"b","type":"integer","nullable":true,"metadata":{}},{"name":"c","type":"long","nullable":true,"metadata":{}},{"name":"d","type":"double","nullable":true,"metadata":{}},{"name":"e","type":{"type":"array","elementType":"string","containsNull":false},"nullable":true,"metadata":{}},{"name":"f","type":{"type":"struct","fields":[{"name":"ff","type":"boolean","nullable":true,"metadata":{}}]},"nullable":true,"metadata":{}}]}"""

  override def beforeAll() {

  }

  override def afterAll() {

  }

  test("testSchema2SqlType") {
    val resSchema = schema2SqlType(parse(json))
    resSchema.sameElements(Array(("a", 12, false),
      ("b", 4, true),
      ("c", -5, true),
      ("d", 8, true),
      ("e", 2003, true),
      ("f", 2002, true)))
  }

  test("testParse") {
    val parsedJson = parse(json)
    parsedJson.sameElements(Array(("a", "string", false),
      ("b", "integer", true),
      ("c", "long", true),
      ("d", "double", true),
      ("e", "array", true),
      ("f", "struct", true)))
  }

}
