package moonbox.grid.deploy.rest.service.workbench

import java.sql.Statement
import java.util.concurrent.{ConcurrentHashMap, TimeUnit}

import akka.actor.ActorRef
import akka.pattern.ask
import akka.util.Timeout
import moonbox.catalog.AbstractCatalog.User
import moonbox.catalog.{CatalogQuery, JdbcCatalog}
import moonbox.common.MbLogging
import moonbox.grid.deploy.DeployMessages.{MasterAddress, RequestMasterAddress}
import moonbox.grid.deploy.rest.entities._
import moonbox.grid.deploy.rest.routes.SessionConverter
import moonbox.grid.deploy.security.Session

import scala.collection.mutable.ListBuffer
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Await, Future}

/**
  *
  * @param actorRef
  * @param catalog
  */
class WorkbenchService(actorRef: ActorRef, catalog: JdbcCatalog) extends SessionConverter with MbLogging {

  private lazy val statementMap = new ConcurrentHashMap[String, Statement]
  private implicit val timeout = new Timeout(30, TimeUnit.SECONDS)
  private final val tcpServer: String = getTcpServer

  /** execute sql
    *
    * @param executeRequest execute sql request
    * @param session        session
    * @return Future[ExecuteResult] future sql execute result
    */
  def executeConsole(executeRequest: ExecuteRequest, session: Session): Future[ExecuteResponse] = {
    Future {
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
        msg = s"${dataList.size} Row(s) Returned"
      } catch {
        case ex: Exception =>
          log.error("execute query failed", ex)
          endTime = System.currentTimeMillis()
          msg = s"Failed: ${ex.getMessage}"
      } finally {
        val duration = (endTime - startTime).toFloat / 1000
        val info = s"Duration: ${duration}s\nResult: $msg"
        response = if (dataList.nonEmpty) {
          val result = ExecuteResult(columns = columnList.toList, data = dataList.toList, size = dataList.size)
          ExecuteResponse(Some(result), info)
        } else {
          ExecuteResponse(info = info)
        }
      }
      response
    }
  }

  /**
    *
    * @param consoleId
    * @return
    */
  def cancelConsole(consoleId: String): Future[Unit] = {
    Future {
      try {
        if (statementMap.contains(consoleId)) {
          statementMap.get(consoleId).cancel()
          statementMap.remove(consoleId)
        }
      } catch {
        case ex: Exception =>
          log.error(s"cancel $consoleId query failed", ex)
          throw ex
      }
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
    * @param query
    * @return
    */
  def createQuery(query: Query)(implicit user: User): Future[Unit] = {
    Future {
      catalog.createQuery(CatalogQuery(query.name, query.text, query.description),
        ignoreIfExists = false)
    }
  }

  /**
    *
    * @param query
    * @return
    */
  def updateQuery(query: Query)(implicit user: User): Future[Unit] = {
    Future {
      catalog.alterQuery(CatalogQuery(query.name, query.text, query.description))
    }
  }

  /**
    *
    * @param query
    * @return
    */
  def deleteQuery(query: String)(implicit user: User): Future[Unit] = {
    Future {
      catalog.dropQuery(query, ignoreIfNotExists = false)
    }
  }

  /**
    *
    * @param query
    * @return
    */
  def getQuery(query: String)(implicit user: User): Future[CatalogQuery] = {
    Future {
      catalog.getQuery(query)
    }
  }

  /**
    *
    * @return
    */
  def listQueries()(implicit user: User): Future[Seq[CatalogQuery]] = {
    Future {
      catalog.listQueries()
    }
  }

  /**
    *
    * @param props
    * @return
    */
  private def checkProps(props: Map[String, String]): Unit = {
    require(props.contains("apptype"), "props must have apptype key")
    //    require(props.contains("appname"), "props must have appname key")
    require(props.contains("maxrows"), "props must have maxrows key")
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
