package moonbox.jdbc

import java.sql._
import java.util.Properties

import moonbox.util.MoonboxJDBCUtils
import org.scalatest.{BeforeAndAfterAll, FunSuite}

class MoonboxResultSetTest extends FunSuite with BeforeAndAfterAll {

  var connection: Connection = _
  var stmt: Statement = _
  var res: ResultSet = _
  var sql: String = _
  var url: String = _

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
  }

  override def afterAll() {
    res.close()
    stmt.close()
    connection.close()
  }

  test("getMetaData"){
    val meta = res.getMetaData
    assert(meta != null)
  }

  test("getString"){
    while(res.next()){
      assert("a" == res.getString(1))
      assert("a" == res.getString("a"))
    }
  }

  test("getObject"){
    while(res.next()){
      assert("a" == res.getObject(1))
      assert("a" == res.getObject("a"))
    }
  }
}
