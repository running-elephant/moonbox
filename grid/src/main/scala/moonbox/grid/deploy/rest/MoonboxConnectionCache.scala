package moonbox.grid.deploy.rest

import java.sql.{Connection, DriverManager, SQLException}
import java.util.Properties

import moonbox.common.MbLogging

import scala.collection.mutable

/**
  * Workbench Moonbox Jdbc Connection Cache Management
  */
object MoonboxConnectionCache extends MbLogging {

  private lazy val connectionMap = mutable.HashMap.empty[String, Connection]

  def getConnection(consoleId: String, url: String, props: Map[String, String]): Connection = {
    try {
      var connection: Connection = null
      if (connectionMap.contains(consoleId)) {
        connection = connectionMap(consoleId)
        if (connection.isClosed) {
          connection = newConnection(url, props)
          connectionMap.update(consoleId, connection)
        } else {
          if (connection.getClientInfo.getProperty("apptype") != props("apptype")) {
            try {
              connection.close()
            }
            connection = newConnection(url, props)
            connectionMap.update(consoleId, connection)
          }
        }
      } else {
        connection = newConnection(url, props)
        connectionMap.put(consoleId, connection)
      }
      connection
    } catch {
      case ex: SQLException =>
        throw ex
    }
  }

  def removeConnection(consoleId: String): Unit = {
    if (connectionMap.contains(consoleId)) {
      val connection = connectionMap(consoleId)
      connectionMap.remove(consoleId)
      log.info(s"remove $consoleId console jdbc connection success")
      try {
        if (!connection.isClosed) {
          connection.close()
        }
      } catch {
        case _: SQLException =>
          log.warn(s"close $consoleId console jdbc connection failed")
      }
    }
  }

  private def newConnection(url: String, props: Map[String, String]): Connection = {
    Class.forName("moonbox.jdbc.MbDriver")
    val properties = new Properties()
    props.foreach(kv => properties.put(kv._1, kv._2))
    DriverManager.getConnection(url, properties)
  }
}
