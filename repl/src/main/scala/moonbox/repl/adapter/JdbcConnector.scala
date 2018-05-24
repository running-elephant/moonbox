package moonbox.repl.adapter

import java.sql.{Connection, DriverManager, ResultSetMetaData}
import java.util.Properties

class JdbcConnector(timeout: Int) extends Connector {
  var connection: Connection = _
  override def prepare(host: String, port:Int, user: String, pwd: String, db: String): Boolean = {
    try {
      Class.forName("moonbox.jdbc.MbDriver")
      val url = s"jdbc:moonbox://${host}:${port}/default"
      val prop = new Properties()
      prop.setProperty("user", user)
      prop.setProperty("password", pwd)
      /*prop.setProperty("fetchsize", 200.toString)*/
      connection = DriverManager.getConnection(url, prop)
      true
    }catch{
      case e: Exception =>
        e.printStackTrace()
        false
    }
  }

  override def process(sqls: Seq[String]): Unit = {
    val stmt = connection.createStatement()
    stmt.setQueryTimeout(timeout)
    stmt.setFetchSize(200)
    sqls.foreach { sql =>
      val rs = stmt.executeQuery(sql)
      val metaData: ResultSetMetaData = rs.getMetaData
      val colNameAndType = (1 to metaData.getColumnCount).map { index =>
        val name = metaData.getColumnName(index)
        val typ = metaData.getColumnTypeName(index)
        s"$name($typ)"
      }


      println(colNameAndType.mkString(" | "))
      while (rs.next()) {
        val colData: Seq[AnyRef] = (1 to metaData.getColumnCount).map { index =>
          rs.getObject(index)
        }
        println(colData.mkString(" | "))
      }
    }
  }

  override def close(): Unit = {
    if(connection != null) {
      connection.close()
      connection = null
    }
  }

  override def shutdown(): Unit = {

  }

}
