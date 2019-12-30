package moonbox.grid.deploy.rest.routes

import javax.ws.rs.Path

import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Route
import io.swagger.annotations.{ApiResponse, ApiResponses, _}
import moonbox.grid.deploy.rest.entities.{LogView, Response}
import moonbox.grid.deploy.rest.service.{LogService, LoginService}
import moonbox.grid.deploy.security.Session

import scala.util.{Failure, Success}

@Api(
  value = "Log",
  consumes = "application/json",
  produces = "application/json", authorizations = Array(new Authorization("Bearer")))
@Path("/logs")
class LogRoute(override val loginService: LoginService, logService: LogService) extends SecurityRoute with SessionConverter {

  @ApiOperation(value = "view log", nickname = "viewLog", httpMethod = "PUT")
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "viewLog", value = "View Log Parameter Information", required = true, paramType = "body", dataType = "moonbox.grid.deploy.rest.entities.LogView")
  ))
  @ApiResponses(Array(
    new ApiResponse(code = 200, message = "OK"),
    new ApiResponse(code = 451, message = "Request process failed"),
    new ApiResponse(code = 500, message = "Internal server error")
  ))
  @Path("/view")
  def viewLog = (session: Session) => {
    put {
      entity(as[LogView]) { logView =>
        onComplete(logService.viewLog(logView)(session)) {
          case Success(either) =>
            either.fold(
              content => complete(OK, Response(code = 200, msg = "Success", payload = Some(content))),
              exception => complete(OK, Response(code = 451, msg = exception.getMessage))
            )
          case Failure(e) => complete(OK, Response(code = 451, msg = e.getMessage))
        }
      }
    }
  }

  override protected def createSecurityRoute: Array[(Session) => Route] = Array(
    viewLog
  )
}
