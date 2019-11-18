package moonbox.grid.deploy.rest.routes

import javax.ws.rs.Path

import akka.http.scaladsl.model.StatusCodes.OK
import akka.http.scaladsl.server.Route
import io.swagger.annotations._
import moonbox.grid.deploy.rest.entities.{Application, Response, ResponseHeader}
import moonbox.grid.deploy.rest.service.{ApplicationService, LoginService}
import moonbox.grid.deploy.security.Session

import scala.util.{Failure, Success}

/**
	* ApplicationRoute is responsible for create/delete/update/list and start/stop applications
	* @param loginService
	*/

@Api(
	value = "Application",
	consumes = "application/json",
	produces = "application/json", authorizations = Array(new Authorization("Bearer")))
@Path("/application")
class ApplicationRoute(override val loginService: LoginService, appService: ApplicationService) extends SecurityRoute {

	@ApiOperation(value = "Add a new application", nickname = "create", httpMethod = "POST")
	@ApiImplicitParams(Array(
		new ApiImplicitParam(name = "Create Application", value = "Create Application Parameter Information", required = true, paramType = "body", dataType = "moonbox.grid.deploy.rest.entities.Application")
	))
	@ApiResponses(Array(
		new ApiResponse(code = 200, message = "OK"),
		new ApiResponse(code = 210, message = "Wrong password"),
		new ApiResponse(code = 404, message = "Not found"),
		new ApiResponse(code = 451, message = "request process failed"),
		new ApiResponse(code = 500, message = "internal server error")
	))
	def createApp = (token: String, session: Session) => {
		post {
			entity(as[Application]) { in =>
				onComplete(appService.createApplication(in, session)) {
					case Success(_) =>
						complete(OK, Response(ResponseHeader(code = 200, msg = "Success", token = Some(token))))
					case Failure(e) =>
						complete(OK, Response(ResponseHeader(code = 451, msg = e.getMessage, token = Some(token))))
				}
			}
		}
	}

	@ApiOperation(value = "Update a application", nickname = "update", httpMethod = "PUT")
	@ApiImplicitParams(Array(
		new ApiImplicitParam(name = "Update Application", value = "Update Application Parameter Information", required = true, paramType = "body", dataType = "moonbox.grid.deploy.rest.entities.Application")
	))
	@ApiResponses(Array(
		new ApiResponse(code = 200, message = "OK"),
		new ApiResponse(code = 451, message = "request process failed"),
		new ApiResponse(code = 500, message = "internal server error")
	))
	def updateApp = (token: String, session: Session) => {
		post {
			entity(as[Application]) { in =>
				onComplete(appService.createApplication(in, session)) {
					case Success(_) =>
						complete(OK, Response(ResponseHeader(code = 200, msg = "Success", token = Some(token))))
					case Failure(e) =>
						complete(OK, Response(ResponseHeader(code = 451, msg = e.getMessage, token = Some(token))))
				}
			}
		}
	}

	@ApiOperation(value = "Get a application", nickname = "getApp", httpMethod = "GET")
	@ApiImplicitParams(Array(
		new ApiImplicitParam(name = "appName", value = "application name", required = true, paramType = "path", dataType = "string")
	))
	@ApiResponses(Array(
		new ApiResponse(code = 200, message = "OK"),
		new ApiResponse(code = 404, message = "Not found"),
		new ApiResponse(code = 451, message = "request process failed"),
		new ApiResponse(code = 500, message = "internal server error")
	))
	@Path("/{appName}")
	def getApp = (token: String, session: Session) => path(Segment) { appName =>
		get {
			logInfo(appName)
			onComplete(appService.getApplication(appName, session)) {
				case Success(appOption) =>
					appOption match {
						case Some(app) =>
							complete(OK, Response(ResponseHeader(code = 200, msg = "Success", token = Some(token)), payload = Some(app)))
						case None =>
							complete(OK, Response(ResponseHeader(code = 404, msg = "Application Not Found", token = Some(token))))
					}
				case Failure(e) =>
					complete(OK, Response(ResponseHeader(code = 451, msg = e.getMessage, token = Some(token))))
			}
		}
	}

	@ApiOperation(value = "List applications", nickname = "listApps", httpMethod = "GET", responseContainer = "set")
	@ApiResponses(Array(
		new ApiResponse(code = 200, message = "OK"),
		new ApiResponse(code = 404, message = "Not found"),
		new ApiResponse(code = 451, message = "request process failed"),
		new ApiResponse(code = 500, message = "internal server error")
	))
	def listApps = (token: String, session: Session) => get {
		onComplete(appService.listApplication(session)) {
			case Success(apps) =>
				complete(OK, Response(ResponseHeader(code = 200, msg = "Success", token = Some(token)), payload = Some(apps)))
			case Failure(e) =>
				complete(OK, Response(ResponseHeader(code = 451, msg = e.getMessage, token = Some(token))))
		}
	}

	@ApiOperation(value = "Delete a application", nickname = "delete", httpMethod = "DELETE")
	@ApiImplicitParams(Array(
		new ApiImplicitParam(name = "appName", value = "application name", required = true, dataType = "string", paramType = "path")
	))
	@ApiResponses(Array(
		new ApiResponse(code = 200, message = "OK"),
		new ApiResponse(code = 404, message = "Not found"),
		new ApiResponse(code = 451, message = "request process failed"),
		new ApiResponse(code = 500, message = "internal server error")
	))
	@Path("/{appName}")
	def deleteApp = (token: String, session: Session) => path(Segment) { appName =>
		delete {
			logInfo(appName)
			complete(OK)
		}
	}

	override def createSecurityRoute: Array[(String, Session) => Route] = Array(
		createApp, deleteApp, getApp, listApps
	)
}
