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
