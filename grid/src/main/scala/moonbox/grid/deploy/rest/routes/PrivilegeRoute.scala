package moonbox.grid.deploy.rest.routes

import javax.ws.rs.Path

import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Route
import io.swagger.annotations.{ApiResponse, ApiResponses, _}
import moonbox.grid.deploy.rest.entities.Response
import moonbox.grid.deploy.rest.service.{LoginService, PrivilegeService}
import moonbox.grid.deploy.security.Session

import scala.util.{Failure, Success}

@Api(
  value = "Privilege",
  consumes = "application/json",
  produces = "application/json", authorizations = Array(new Authorization("Bearer")))
@Path("/privileges")
class PrivilegeRoute(override val loginService: LoginService, privilegeService: PrivilegeService) extends SecurityRoute with SessionConverter {

  @ApiOperation(value = "list role privileges", nickname = "role", httpMethod = "GET")
  @ApiResponses(Array(
    new ApiResponse(code = 200, message = "OK"),
    new ApiResponse(code = 451, message = "Request process failed"),
    new ApiResponse(code = 500, message = "Internal server error")
  ))
  @Path("/role")
  def listRolePrivileges = (session: Session) => path("role") {
    get {
      onComplete(privilegeService.listRolePrivileges()(session)) {
        case Success(either) =>
          either.fold(
            rolePrivileges => complete(OK, Response(code = 200, msg = "Success", payload = Some(rolePrivileges))),
            exception => complete(OK, Response(code = 451, msg = exception.getMessage))
          )
        case Failure(e) =>
          complete(OK, Response(code = 451, msg = e.getMessage))
      }
    }
  }

  @ApiOperation(value = "list database privileges", nickname = "database", httpMethod = "GET")
  @ApiResponses(Array(
    new ApiResponse(code = 200, message = "OK"),
    new ApiResponse(code = 451, message = "Request process failed"),
    new ApiResponse(code = 500, message = "Internal server error")
  ))
  @Path("/database")
  def listDatabasePrivileges = (session: Session) => path("database") {
    get {
      onComplete(privilegeService.listDatabasePrivileges()(session)) {
        case Success(either) =>
          either.fold(
            dbPrivileges => complete(OK, Response(code = 200, msg = "Success", payload = Some(dbPrivileges))),
            exception => complete(OK, Response(code = 451, msg = exception.getMessage))
          )
        case Failure(e) =>
          complete(OK, Response(code = 451, msg = e.getMessage))
      }
    }
  }

  @ApiOperation(value = "list table privileges", nickname = "table", httpMethod = "GET")
  @ApiResponses(Array(
    new ApiResponse(code = 200, message = "OK"),
    new ApiResponse(code = 451, message = "Request process failed"),
    new ApiResponse(code = 500, message = "Internal server error")
  ))
  @Path("/table")
  def listTablePrivileges = (session: Session) => path("table") {
    get {
      onComplete(privilegeService.listTablePrivileges()(session)) {
        case Success(either) =>
          either.fold(
            tablePrivileges => complete(OK, Response(code = 200, msg = "Success", payload = Some(tablePrivileges))),
            exception => complete(OK, Response(code = 451, msg = exception.getMessage))
          )
        case Failure(e) =>
          complete(OK, Response(code = 451, msg = e.getMessage))
      }
    }
  }

  @ApiOperation(value = "list column privileges", nickname = "column", httpMethod = "GET")
  @ApiResponses(Array(
    new ApiResponse(code = 200, message = "OK"),
    new ApiResponse(code = 451, message = "Request process failed"),
    new ApiResponse(code = 500, message = "Internal server error")
  ))
  @Path("/column")
  def listColumnPrivileges = (session: Session) => path("column") {
    get {
      onComplete(privilegeService.listColumnPrivileges()(session)) {
        case Success(either) =>
          either.fold(
            columnPrivileges => complete(OK, Response(code = 200, msg = "Success", payload = Some(columnPrivileges))),
            exception => complete(OK, Response(code = 451, msg = exception.getMessage))
          )
        case Failure(e) =>
          complete(OK, Response(code = 451, msg = e.getMessage))
      }
    }
  }

  override protected def createSecurityRoute: Array[(Session) => Route] = Array(
    listDatabasePrivileges, listRolePrivileges, listTablePrivileges, listColumnPrivileges
  )
}
