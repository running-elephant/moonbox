package moonbox.grid.deploy.rest.routes

import javax.ws.rs.Path

import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Route
import io.swagger.annotations.{ApiResponse, ApiResponses, _}
import moonbox.grid.deploy.rest.entities.Response
import moonbox.grid.deploy.rest.service.{LoginService, StatsService}
import moonbox.grid.deploy.security.Session

import scala.util.{Failure, Success}

@Api(
  value = "Stats",
  consumes = "application/json",
  produces = "application/json", authorizations = Array(new Authorization("Bearer")))
@Path("/stats")
class StatsRoute(override val loginService: LoginService, statsService: StatsService) extends SecurityRoute with SessionConverter {

  @ApiOperation(value = "list resource stats", nickname = "resource", httpMethod = "GET")
  @ApiResponses(Array(
    new ApiResponse(code = 200, message = "OK"),
    new ApiResponse(code = 451, message = "Request process failed"),
    new ApiResponse(code = 500, message = "Internal server error")
  ))
  @Path("/resource")
  def listResourceStats = (session: Session) => {
    get {
      onComplete(statsService.listResourceStats()(session)) {
        case Success(either) =>
          either.fold(
            resourceStats => complete(OK, Response(code = 200, msg = "Success", payload = Some(resourceStats))),
            exception => complete(OK, Response(code = 451, msg = exception.getMessage))
          )
        case Failure(e) =>
          complete(OK, Response(code = 451, msg = e.getMessage))
      }
    }
  }

  override protected def createSecurityRoute: Array[(Session) => Route] = Array(
    listResourceStats
  )
}
