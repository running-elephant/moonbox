package moonbox.grid.deploy.rest.routes

import javax.ws.rs.Path

import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Route
import io.swagger.annotations.{ApiResponse, ApiResponses, _}
import moonbox.grid.deploy.rest.entities.{BatchOpSeq, Organization, Response}
import moonbox.grid.deploy.rest.service.{LoginService, OrganizationService}
import moonbox.grid.deploy.security.Session

import scala.util.{Failure, Success}

@Api(
	value = "Organization",
	consumes = "application/json",
	produces = "application/json", authorizations = Array(new Authorization("Bearer")))
@Path("/organizations")
class OrganizationRoute(override val loginService: LoginService, organizationService: OrganizationService) extends SecurityRoute with SessionConverter {

	@ApiOperation(value = "create a new organization", nickname = "create", httpMethod = "POST")
	@ApiImplicitParams(Array(
		new ApiImplicitParam(name = "create organization", value = "Create Organization Parameter Information", required = true, paramType = "body", dataType = "moonbox.grid.deploy.rest.entities.Organization")
	))
	@ApiResponses(Array(
		new ApiResponse(code = 200, message = "OK"),
		new ApiResponse(code = 451, message = "request process failed"),
		new ApiResponse(code = 500, message = "internal server error")
	))
	def createOrganization = (session: Session) => {
		post {
			entity(as[Organization]) { org =>
				onComplete(organizationService.createOrganization(org)(session)) {
					case Success(either) =>
						either.fold(
							_ => complete(OK, Response(code = 200, msg = "Success")),
							exception => complete(OK, Response(code = 451, msg = exception.getMessage))
						)
					case Failure(e) => complete(OK, Response(code = 451, msg = e.getMessage))
				}
			}
		}
	}

	@ApiOperation(value = "update organization", nickname = "update", httpMethod = "PUT")
	@ApiImplicitParams(Array(
		new ApiImplicitParam(name = "update organization", value = "Update Organization Parameter Information", required = true, paramType = "body", dataType = "moonbox.grid.deploy.rest.entities.Organization")
	))
	@ApiResponses(Array(
		new ApiResponse(code = 200, message = "OK"),
		new ApiResponse(code = 451, message = "request process failed"),
		new ApiResponse(code = 500, message = "internal server error")
	))
	def updateOrganization = (session: Session) => {
		put {
			entity(as[Organization]) { org =>
				onComplete(organizationService.updateOrganization(org)(session)) {
					case Success(either) =>
						either.fold(
							_ => complete(OK, Response(code = 200, msg = "Success")),
							exception => complete(OK, Response(code = 451, msg = exception.getMessage))
						)
					case Failure(e) => complete(OK, Response(code = 451, msg = e.getMessage))
				}
			}
		}
	}

	@ApiOperation(value = "delete organization", nickname = "delete", httpMethod = "DELETE")
	@ApiImplicitParams(Array(
		new ApiImplicitParam(name = "organizationName", value = "Delete Organization Parameter Information", required = true, paramType = "path", dataType = "string")
	))
	@ApiResponses(Array(
		new ApiResponse(code = 200, message = "OK"),
		new ApiResponse(code = 451, message = "request process failed"),
		new ApiResponse(code = 500, message = "internal server error")
	))
	@Path("/{organizationName}")
	def deleteOrganization = (session: Session) => path(Segment) { org =>
		delete {
			onComplete(organizationService.deleteOrganization(org)(session)) {
				case Success(either) =>
					either.fold(
						_ => complete(OK, Response(code = 200, msg = "Success")),
						exception => complete(OK, Response(code = 451, msg = exception.getMessage))
					)
				case Failure(e) => complete(OK, Response(code = 451, msg = e.getMessage))
			}
		}
	}


	@ApiOperation(value = "delete organization in batch mode", nickname = "delete", httpMethod = "DELETE")
	@ApiImplicitParams(Array(
		new ApiImplicitParam(name = "list of organization names", value = "Organization Name List Parameter", required = true, paramType = "body", dataType = "moonbox.grid.deploy.rest.entities.BatchOpSeq")
	))
	@ApiResponses(Array(
		new ApiResponse(code = 200, message = "OK"),
		new ApiResponse(code = 451, message = "Request process failed"),
		new ApiResponse(code = 500, message = "Internal server error")
	))
	def deleteOrganizations = (session: Session) => {
		delete {
			entity(as[BatchOpSeq]) {
				batchOp =>
					onComplete(organizationService.deleteOrganizations(batchOp)(session)) {
						case Success(either) =>
							either.fold(
								_ => complete(OK, Response(code = 200, msg = "Success")),
								exception => complete(OK, Response(code = 451, msg = exception.getMessage))
							)
						case Failure(e) => complete(OK, Response(code = 451, msg = e.getMessage))
					}
			}
		}
	}

	@ApiOperation(value = "get organization by name", nickname = "getOrganization", httpMethod = "GET")
	@ApiImplicitParams(Array(
		new ApiImplicitParam(name = "organizationName", value = "organization name", required = true, paramType = "path", dataType = "string")
	))
	@ApiResponses(Array(
		new ApiResponse(code = 200, message = "OK"),
		new ApiResponse(code = 451, message = "request process failed"),
		new ApiResponse(code = 500, message = "internal server error")
	))
	@Path("/{organizationName}")
	def getOrganization = (session: Session) => path(Segment) { org =>
		get {
			onComplete(organizationService.getOrganization(org)(session)) {
				case Success(either) =>
					either.fold(
						org => complete(OK, Response(code = 200, msg = "Success", payload = Some(org))),
						exception => complete(OK, Response(code = 451, msg = exception.getMessage))
					)
				case Failure(e) => complete(OK, Response(code = 451, msg = e.getMessage))
			}
		}
	}

	@ApiOperation(value = "list organizations", nickname = "list", httpMethod = "GET")
	@ApiResponses(Array(
		new ApiResponse(code = 200, message = "OK"),
		new ApiResponse(code = 451, message = "request process failed"),
		new ApiResponse(code = 500, message = "internal server error")
	))
	def listOrganizations = (session: Session) => {
		get {
			onComplete(organizationService.listOrganizations()) {
				case Success(either) =>
					either.fold(
						orgs => complete(OK, Response(code = 200, msg = "Success", payload = Some(orgs))),
						exception => complete(OK, Response(code = 451, msg = exception.getMessage))
					)
				case Failure(e) => complete(OK, Response(code = 451, msg = e.getMessage))
			}
		}
	}

	override protected def createSecurityRoute: Array[(Session) => Route] = Array(
		createOrganization, updateOrganization, deleteOrganization, deleteOrganizations, getOrganization, listOrganizations
	)
}
