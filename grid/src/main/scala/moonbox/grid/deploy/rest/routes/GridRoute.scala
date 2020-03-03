package moonbox.grid.deploy.rest.routes

import javax.ws.rs.Path

import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Route
import io.swagger.annotations._
import moonbox.grid.deploy.rest.entities.Response
import moonbox.grid.deploy.rest.service.{GridService, LoginService}
import moonbox.grid.deploy.security.Session

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}


@Api(
  value = "Grid",
  consumes = "application/json",
  produces = "application/json",
  authorizations = Array(new Authorization("Bearer")))
@Path("/grids")
class GridRoute(override val loginService: LoginService, gridService: GridService) extends SecurityRoute {

  @ApiOperation(value = "list master and workers", nickname = "list", httpMethod = "GET", responseContainer = "set")
  @ApiResponses(Array(
    new ApiResponse(code = 200, message = "OK"),
    new ApiResponse(code = 451, message = "request process failed"),
    new ApiResponse(code = 500, message = "internal server error")
  ))
  def listNodes = (session: Session) => {
    get {
      onComplete(gridService.clusterState()) {
        case Success(nodes) =>
          complete(OK, Response(code = 200, msg = "Success", payload = Some(nodes)))
        case Failure(e) =>
          complete(OK, Response(code = 451, msg = e.getMessage))
      }
    }
  }

  override def createSecurityRoute: Array[Session => Route] = Array(
    listNodes
  )
}
