package moonbox.grid.deploy.rest.routes

import javax.ws.rs.Path

import akka.http.scaladsl.model.StatusCodes.OK
import akka.http.scaladsl.server.Route
import io.swagger.annotations._
import moonbox.grid.deploy.rest.entities._
import moonbox.grid.deploy.rest.service.LoginService
import moonbox.grid.deploy.rest.service.workbench.WorkbenchService
import moonbox.grid.deploy.security.Session

import scala.util.{Failure, Success}

/**
  * WorkbenchRoute is responsible for workbench operation
  *
  * @param loginService
  */

@Api(
  value = "Workbench",
  consumes = "application/json",
  produces = "application/json", authorizations = Array(new Authorization("Bearer")))
@Path("/workbench")
class WorkbenchRoute(override val loginService: LoginService, workbenchService: WorkbenchService) extends SecurityRoute with SessionConverter {

  @ApiOperation(value = "console execute", nickname = "execute", httpMethod = "PUT")
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "Console Execute", value = "Console Execute Parameter Information", required = true, paramType = "body", dataType = "moonbox.grid.deploy.rest.entities.ExecuteRequest")
  ))
  @ApiResponses(Array(
    new ApiResponse(code = 200, message = "OK"),
    new ApiResponse(code = 451, message = "request process failed"),
    new ApiResponse(code = 500, message = "internal server error")
  ))
  @Path("/console/execute")
  def executeConsole = (session: Session) => {
    put {
      entity(as[ExecuteRequest]) { in =>
        onComplete(workbenchService.executeConsole(in, session)) {
          case Success(result) =>
            complete(OK, Response(code = 200, msg = "Success", payload = Some(result)))
          case Failure(e) =>
            complete(OK, Response(code = 451, msg = e.getMessage))
        }
      }
    }
  }

  @ApiOperation(value = "console cancel", nickname = "cancel", httpMethod = "GET")
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "consoleId", value = "console id", required = true, paramType = "path", dataType = "string")
  ))
  @ApiResponses(Array(
    new ApiResponse(code = 200, message = "OK"),
    new ApiResponse(code = 451, message = "request process failed"),
    new ApiResponse(code = 500, message = "internal server error")
  ))
  @Path("/console/{consoleId}/cancel")
  def cancelConsole = (session: Session) => path("console" / Segment / "cancel") { consoleId =>
    get {
      onComplete(workbenchService.cancelConsole(consoleId)) {
        case Success(_) =>
          complete(OK, Response(code = 200, msg = "Success"))
        case Failure(e) =>
          complete(OK, Response(code = 451, msg = e.getMessage))
      }
    }
  }

  @ApiOperation(value = "Recreate Connection", nickname = "recreate", httpMethod = "GET")
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "consoleId", value = "console id", required = true, paramType = "path", dataType = "string")
  ))
  @ApiResponses(Array(
    new ApiResponse(code = 200, message = "OK"),
    new ApiResponse(code = 451, message = "request process failed"),
    new ApiResponse(code = 500, message = "internal server error")
  ))
  @Path("/console/{consoleId}/reconnect")
  def reconnectConnection = (session: Session) => path("console" / Segment / "reconnect") { consoleId =>
    get {
      onComplete(workbenchService.closeConnection(consoleId)) {
        case Success(_) =>
          complete(OK, Response(code = 200, msg = "Success"))
        case Failure(e) =>
          complete(OK, Response(code = 451, msg = e.getMessage))
      }
    }
  }

  @ApiOperation(value = "Close Connection", nickname = "close", httpMethod = "GET")
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "consoleId", value = "console id", required = true, paramType = "path", dataType = "string")
  ))
  @ApiResponses(Array(
    new ApiResponse(code = 200, message = "OK"),
    new ApiResponse(code = 451, message = "request process failed"),
    new ApiResponse(code = 500, message = "internal server error")
  ))
  @Path("/console/{consoleId}/close")
  def closeConnection = (session: Session) => path("console" / Segment / "close") { consoleId =>
    get {
      onComplete(workbenchService.closeConnection(consoleId)) {
        case Success(_) =>
          complete(OK, Response(code = 200, msg = "Success"))
        case Failure(e) =>
          complete(OK, Response(code = 451, msg = e.getMessage))
      }
    }
  }

  @ApiOperation(value = "create a new query", nickname = "create", httpMethod = "POST")
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "Create Query", value = "Create Query Parameter Information", required = true, paramType = "body", dataType = "moonbox.grid.deploy.rest.entities.Query")
  ))
  @ApiResponses(Array(
    new ApiResponse(code = 200, message = "OK"),
    new ApiResponse(code = 451, message = "Request process failed"),
    new ApiResponse(code = 500, message = "Internal server error")
  ))
  @Path("/queries")
  def createQuery = (session: Session) => {
    post {
      entity(as[Query]) { query =>
        onComplete(workbenchService.createQuery(query)(session)) {
          case Success(_) =>
            complete(OK, Response(code = 200, msg = "Success"))
          case Failure(e) =>
            complete(OK, Response(code = 451, msg = e.getMessage))
        }
      }
    }
  }

  @ApiOperation(value = "update query", nickname = "update", httpMethod = "PUT")
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "Update Query", value = "Update Query Parameter Information", required = true, paramType = "body", dataType = "moonbox.grid.deploy.rest.entities.Query")
  ))
  @ApiResponses(Array(
    new ApiResponse(code = 200, message = "OK"),
    new ApiResponse(code = 451, message = "Request process failed"),
    new ApiResponse(code = 500, message = "Internal server error")
  ))
  @Path("/queries")
  def updateQuery = (session: Session) => {
    put {
      entity(as[Query]) { query =>
        onComplete(workbenchService.updateQuery(query)(session)) {
          case Success(_) =>
            complete(OK, Response(code = 200, msg = "Success"))
          case Failure(e) =>
            complete(OK, Response(code = 451, msg = e.getMessage))
        }
      }
    }
  }

  @ApiOperation(value = "delete query by name", nickname = "delete", httpMethod = "DELETE")
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "queryName", value = "query name", required = true, paramType = "path", dataType = "string")
  ))
  @ApiResponses(Array(
    new ApiResponse(code = 200, message = "OK"),
    new ApiResponse(code = 451, message = "Request process failed"),
    new ApiResponse(code = 500, message = "Internal server error")
  ))
  @Path("/queries/{queryName}")
  def deleteQuery = (session: Session) => path("queries" / Segment) { queryName =>
    delete {
      onComplete(workbenchService.deleteQuery(queryName)(session)) {
        case Success(_) =>
          complete(OK, Response(code = 200, msg = "Success"))
        case Failure(e) =>
          complete(OK, Response(code = 451, msg = e.getMessage))

      }
    }
  }

  @ApiOperation(value = "get query by name", nickname = "get", httpMethod = "GET")
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "queryName", value = "query name", required = true, paramType = "path", dataType = "string")
  ))
  @ApiResponses(Array(
    new ApiResponse(code = 200, message = "OK"),
    new ApiResponse(code = 451, message = "Request process failed"),
    new ApiResponse(code = 500, message = "Internal server error")
  ))
  @Path("/queries/{queryName}")
  def getQuery = (session: Session) => path("queries" / Segment) { queryName =>
    get {
      onComplete(workbenchService.getQuery(queryName)(session)) {
        case Success(org) =>
          complete(OK, Response(code = 200, msg = "Success", payload = Some(org)))
        case Failure(e) =>
          complete(OK, Response(code = 451, msg = e.getMessage))
      }
    }
  }

  @ApiOperation(value = "list queries", nickname = "list", httpMethod = "GET")
  @ApiResponses(Array(
    new ApiResponse(code = 200, message = "OK"),
    new ApiResponse(code = 451, message = "Request process failed"),
    new ApiResponse(code = 500, message = "Internal server error")
  ))
  @Path("/queries")
  def listQueries = (session: Session) => {
    get {
      onComplete(workbenchService.listQueries()(session)) {
        case Success(orgs) =>
          complete(OK, Response(code = 200, msg = "Success", payload = Some(orgs)))
        case Failure(e) =>
          complete(OK, Response(code = 451, msg = e.getMessage))
      }
    }
  }

  override def createSecurityRoute: Array[Session => Route] = Array(
    executeConsole, cancelConsole, reconnectConnection, closeConnection,
    createQuery, updateQuery, deleteQuery, getQuery, listQueries
  )
}
