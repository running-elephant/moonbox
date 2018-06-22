package moonbox.repl.adapter

import java.sql.{Connection, DriverManager, ResultSetMetaData}
import java.util.Properties

class JdbcConnector(timeout: Int) extends Connector {
  var connection: Connection = _

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
    val numShow = 500
    val stmt = connection.createStatement()
    stmt.setQueryTimeout(timeout)
    stmt.setFetchSize(200)
    sqls.foreach { sql =>
      if (!stmt.execute(sql)) return
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
      print(Utils.showString(dataBuf, schema, numShow, 45))
    }
  }

  override def close(): Unit = {
    if (connection != null) {
      connection.close()
      connection = null
    }
  }

  override def shutdown(): Unit = {

  }

}
