package moonbox.grid.deploy.rest.service

import java.sql.Statement

import akka.actor.ActorRef
import moonbox.catalog.JdbcCatalog
import moonbox.common.MbLogging
import moonbox.grid.deploy.rest.MoonboxConnectionCache
import moonbox.grid.deploy.rest.entities._
import moonbox.grid.deploy.security.Session

import scala.collection.mutable
import scala.collection.mutable.ListBuffer
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  *
  * @param actor
  * @param catalog
  */
class WorkbenchService(actor: ActorRef, catalog: JdbcCatalog) extends MbLogging {

  lazy val statementMap = mutable.HashMap.empty[String, Statement]

  /** execute sql
    *
    * @param executeRequest execute sql request
    * @param session        session
    * @return Future[ExecuteResult] future sql execute result
    */
  def executeQuery(executeRequest: ExecuteRequest, session: Session): Future[ExecuteResponse] = {
    var startTime: Long = 0l
    var endTime: Long = 0l

    val props = executeRequest.props
    checkProps(props)
    val newProps = props ++ Map("user" -> session("user"), "password" -> session("password"))

    try {
      startTime = System.currentTimeMillis()
      val conn = MoonboxConnectionCache.getConnection(executeRequest.consoleId, getConnectionUrl, newProps)

      val statement = conn.createStatement()
      statementMap.put(executeRequest.consoleId, statement)

      statement.setMaxRows(props("maxrows").toInt)
      if (props.contains("fetchsize")) {
        statement.setFetchSize(props("fetchsize").toInt)
      }
      if (props.contains("querytimeout")) {
        statement.setQueryTimeout(props("querytimeout").toInt)
      }
      val resultSet = statement.executeQuery(executeRequest.sql)

      val meta = resultSet.getMetaData
      val columnCount = meta.getColumnCount
      val columnList = Range(0, columnCount).map(index => meta.getColumnName(index + 1))

      val dataList = new ListBuffer[Map[String, Any]]
      while (resultSet.next()) {
        val row = Range(0, columnCount).map(index => (columnList(index), resultSet.getObject(index + 1))).toMap
        dataList.append(row)
      }
      endTime = System.currentTimeMillis()
      statementMap.remove(executeRequest.consoleId)

      val result = ExecuteResult(columns = columnList.toList, data = dataList.toList, size = dataList.size)
      val info = s"duration: ${(endTime - startTime).toFloat / 1000}s\n${dataList.size} rows Returned."
      Future(ExecuteResponse(Some(result), info))
    } catch {
      case ex: Exception =>
        log.error("execute query failed", ex)
        endTime = System.currentTimeMillis()
        val info = s"duration: ${(endTime - startTime).toFloat / 1000}s\nFailed: ${ex.getMessage}"
        Future(ExecuteResponse(info = info))
    }
  }

  /**
    *
    * @param consoleId
    * @return
    */
  def cancel(consoleId: String): Future[Unit] = {
    Future {
      if (statementMap.contains(consoleId)) {
        statementMap(consoleId).cancel()
        statementMap.remove(consoleId)
      }
    }
  }

  /**
    *
    * @param consoleId
    * @return
    */
  def reconnect(consoleId: String): Future[Unit] = {
    Future {
      MoonboxConnectionCache.removeConnection(consoleId)
    }
  }

  private def checkProps(props: Map[String, String]): Unit = {
    require(props.contains("apptype"), "props should have apptype key")
    //    require(props.contains("appname"), "props should have appname key")
    require(props.contains("maxrows"), "props should have maxrows key")
  }

  private def getConnectionUrl: String = {
    s"jdbc:moonbox://localhost:10010/default"
  }
}
