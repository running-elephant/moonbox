package moonbox.grid.deploy.rest.routes

import javax.ws.rs.Path

import akka.http.scaladsl.server.Route
import akka.http.scaladsl.model.StatusCodes.OK
import io.swagger.annotations.{ApiResponse, ApiResponses, _}
import moonbox.grid.deploy.rest.service.{GridService, LoginService}
import moonbox.grid.deploy.security.Session

@Api(
	value = "Service",
	consumes = "application/json",
	produces = "application/json", authorizations = Array(new Authorization("Bearer")))
@Path("/service")
class ServiceRoute(override val loginService: LoginService, gridService: GridService) extends SecurityRoute {

	def query = (session: Session) => path("") {
		get {
			complete(OK)
		}
	}

	@ApiOperation(value = "translate sql", nickname = "translate", httpMethod = "POST")
	@ApiImplicitParams(Array(
		new ApiImplicitParam(name = "translate sql", value = "Translate SQL Parameter Information", required = true, paramType = "body", dataType = "moonbox.grid.deploy.rest.entities.BatchSubmit")
	))
	@ApiResponses(Array(
		new ApiResponse(code = 200, message = "OK"),
		new ApiResponse(code = 451, message = "request process failed"),
		new ApiResponse(code = 500, message = "internal server error")
	))
	@Path("/translation")
	def translation = (session: Session) => path("") {
		get {
			complete(OK)
		}
	}

	def verify = (session: Session) => path("") {
		get {
			complete(OK)
		}
	}

	def tableresources = (session: Session) => path("") {
		get {
			complete(OK)
		}
	}

	def schema = (session: Session) => path("") {
		get {
			complete(OK)
		}
	}

	def lineage = (session: Session) => path("") {
		get {
			complete(OK)
		}
	}

	override protected def createSecurityRoute: Array[(Session) => Route] = Array(
		query, translation, verify, tableresources, schema, lineage
	)
}
