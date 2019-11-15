package moonbox.grid.deploy.rest.routes

import javax.ws.rs.Path

import akka.http.scaladsl.model.StatusCodes.OK
import akka.http.scaladsl.server.Route
import io.swagger.annotations._

@Api(value = "Logout", consumes = "application/json", produces = "application/json", authorizations = Array(new Authorization("Bearer")))
@Path("/logout")
class LogoutRoute extends CrossDomainRoute {

	@ApiOperation(value = "Logout", nickname = "logout", httpMethod = "POST")
	@ApiResponses(Array(
		new ApiResponse(code = 200, message = "OK"),
		new ApiResponse(code = 451, message = "request process failed"),
		new ApiResponse(code = 500, message = "internal server error")
	))
	def logout = path("logout") {
		post {
			complete(OK)
		}
	}

	protected override lazy val createRoute: Route = logout
}
