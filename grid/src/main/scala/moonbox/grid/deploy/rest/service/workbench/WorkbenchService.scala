package moonbox.grid.deploy.rest.service.workbench

import java.sql.Statement
import java.util.concurrent.{ConcurrentHashMap, TimeUnit}

import akka.actor.ActorRef
import akka.pattern.ask
import akka.util.Timeout
import moonbox.catalog.JdbcCatalog
import moonbox.common.MbLogging
import moonbox.grid.deploy.DeployMessages.{MasterAddress, RequestMasterAddress}
import moonbox.grid.deploy.rest.entities._
import moonbox.grid.deploy.security.Session

import scala.collection.mutable.ListBuffer
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Await, Future}

/**
  *
  * @param actorRef
  * @param catalog
  */
class WorkbenchService(actorRef: ActorRef, catalog: JdbcCatalog) extends MbLogging {

  private lazy val statementMap = new ConcurrentHashMap[String, Statement]
  private implicit val timeout = new Timeout(30, TimeUnit.SECONDS)
  private final val tcpServer: String = getTcpServer

  /** execute sql
    *
    * @param executeRequest execute sql request
    * @param session        session
    * @return Future[ExecuteResult] future sql execute result
    */
  def executeQuery(executeRequest: ExecuteRequest, session: Session): Future[ExecuteResponse] = {
    var startTime: Long = 0l
    var endTime: Long = 0l
    var msg: String = ""
    var columnList = Seq.empty[String]
    val dataList = new ListBuffer[Map[String, Any]]
    var response: ExecuteResponse = null
    val props = executeRequest.props
    checkProps(props)
    val newProps = props ++ Map("user" -> s"${session("org")}@${session("user")}", "password" -> session("password"))

    try {
      startTime = System.currentTimeMillis()
      val conn = MoonboxConnectionCache.getConnection(executeRequest.consoleId, getConnectionUrl, newProps)

      val statement = conn.createStatement()
      statementMap.put(executeRequest.consoleId, statement)

      statement.setMaxRows(props("maxrows").toInt)

      props.get("fetchsize").map(_ => statement.setFetchSize(_))
      props.get("querytimeout").map(_ => statement.setQueryTimeout(_))

      val resultSet = statement.executeQuery(executeRequest.sql)

      val meta = resultSet.getMetaData
      val columnCount = meta.getColumnCount
      columnList = Range(0, columnCount).map(index => meta.getColumnName(index + 1))

      while (resultSet.next()) {
        val row = Range(0, columnCount).map(index => (columnList(index), resultSet.getObject(index + 1))).toMap
        dataList.append(row)
      }
      endTime = System.currentTimeMillis()
      statementMap.remove(executeRequest.consoleId)
      msg = s"${dataList.size} Rows Returned."
    } catch {
      case ex: Exception =>
        log.error("execute query failed", ex)
        endTime = System.currentTimeMillis()
        msg = s"Failed: ${ex.getMessage}."
    } finally {
      val duration = (endTime - startTime).toFloat / 1000
      val info = s"Duration: ${duration}s\nResult: $msg."
      response = if (dataList.nonEmpty) {
        val result = ExecuteResult(columns = columnList.toList, data = dataList.toList, size = dataList.size)
        ExecuteResponse(Some(result), info)
      } else {
        ExecuteResponse(info = info)
      }
    }
    Future(response)
  }

  /**
    *
    * @param consoleId
    * @return
    */
  def cancelQuery(consoleId: String): Future[Unit] = {
    try {
      if (statementMap.contains(consoleId)) {
        statementMap.get(consoleId).cancel()
        statementMap.remove(consoleId)
      }
      Future()
    } catch {
      case ex: Exception =>
        log.error(s"cancel $consoleId query failed", ex)
        Future.failed(ex)
    }
  }

  /**
    *
    * @param consoleId
    * @return
    */
  def closeConnection(consoleId: String): Future[Unit] = {
    Future {
      MoonboxConnectionCache.removeConnection(consoleId)
    }
  }

  /**
    *
    * @param props
    * @return
    */
  private def checkProps(props: Map[String, String]): Unit = {
    require(props.contains("apptype"), "props should have apptype key")
    //    require(props.contains("appname"), "props should have appname key")
    require(props.contains("maxrows"), "props should have maxrows key")
  }

  private def getTcpServer: String = {
    val address = Await.result(actorRef.ask(RequestMasterAddress).mapTo[MasterAddress], timeout.duration)
    if (address.tcpServer.nonEmpty) {
      address.tcpServer.get
    } else {
      throw new Exception("Moonbox TcpServer is not enabled.")
    }
  }

  private def getConnectionUrl: String = {
    s"jdbc:moonbox://$tcpServer/default"
  }
}
