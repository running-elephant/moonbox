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
  val url = "jdbc:moonbox://localhost:8080/database"
  val sql = "select * from aaa"
  val prop = new Properties()
  prop.setProperty(MoonboxJDBCUtils.FETCH_SIZE, 200.toString)
  prop.setProperty(MoonboxJDBCUtils.USER_KEY, "ROOT")
  prop.setProperty(MoonboxJDBCUtils.PASSWORD_KEY, "123456")

  test("test") {
    val t1 = System.currentTimeMillis()
    conn = DriverManager.getConnection(url, prop)
    val t2 = System.currentTimeMillis()
    stmt = conn.createStatement()
    val t3 = System.currentTimeMillis()
    val count = 1000
    for (i <- 0 until count) {
      res = stmt.executeQuery(sql)
    }
    val t4 = Platform.currentTime
    println(s"Get connection spend (${t2 - t1} ms)")
    println(s"Get statement spend (${t3 - t2} ms)")
    println(s"Execute $count queries spend (${t4 - t3} ms)")
    meta = res.getMetaData
    val fieldCount = meta.getColumnCount
    println(s"Column count: $fieldCount")
    println(s"Column label: ${meta.getColumnLabel(1)}")
    println(s"Column type: ${meta.getColumnType(1)}")
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
    Class.forName("moonbox.jdbc.Driver")
  }

  override protected def afterAll() = {
    res.close()
    stmt.close()
    conn.close()
  }
}
