package moonbox.grid.deploy.rest.routes
import javax.ws.rs.Path

import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Route
import io.swagger.annotations._
import moonbox.grid.deploy.rest.entities.{Response, ResponseHeader}
import moonbox.grid.deploy.rest.service.{ClusterService, LoginService}
import moonbox.grid.deploy.security.Session
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}


@Api(
value = "Cluster",
consumes = "application/json",
produces = "application/json",
authorizations = Array(new Authorization("Bearer")))
@Path("/cluster")
class ClusterRoute(override val loginService: LoginService, clusterService: ClusterService) extends SecurityRoute {

	@ApiOperation(value = "List master and workers", nickname = "list", httpMethod = "GET", responseContainer = "set")
	@ApiResponses(Array(
		new ApiResponse(code = 200, message = "OK"),
		new ApiResponse(code = 451, message = "request process failed"),
		new ApiResponse(code = 500, message = "internal server error")
	))
	def list = (token: String, session: Session) => {
		get {
			onComplete(Future(clusterService.clusterInfo())) {
				case Success(nodes) =>
					complete(OK, Response(ResponseHeader(code = 200, msg = "Success", token = Some(token)), payload = Some(nodes)))
				case Failure(e) =>
					complete(OK, Response(ResponseHeader(code = 451, msg = e.getMessage, token = Some(token))))
			}
		}
	}

	override def createSecurityRoute: Array[(String, Session) => Route] = Array(
		list
	)
}
