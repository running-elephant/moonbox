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
