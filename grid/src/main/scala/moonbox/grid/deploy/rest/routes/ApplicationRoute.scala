package moonbox.grid.deploy.rest.routes

import javax.ws.rs.Path

import akka.http.scaladsl.model.StatusCodes.OK
import akka.http.scaladsl.server.Route
import io.swagger.annotations._
import moonbox.catalog.{ApplicationExistsException, NoSuchApplicationException}
import moonbox.grid.deploy.rest.entities.{ApplicationIn, Response}
import moonbox.grid.deploy.rest.service.{ApplicationService, LoginService}
import moonbox.grid.deploy.security.Session

import scala.util.{Failure, Success}


@Api(
	value = "Application",
	consumes = "application/json",
	produces = "application/json", authorizations = Array(new Authorization("Bearer")))
@Path("/applications")
class ApplicationRoute(override val loginService: LoginService, appService: ApplicationService) extends SecurityRoute with SessionConverter {

	@ApiOperation(value = "create a new application", nickname = "create", httpMethod = "POST")
	@ApiImplicitParams(Array(
		new ApiImplicitParam(name = "Create Application", value = "Create Application Parameter Information", required = true, paramType = "body", dataType = "moonbox.grid.deploy.rest.entities.ApplicationIn")
	))
	@ApiResponses(Array(
		new ApiResponse(code = 200, message = "OK"),
		new ApiResponse(code = 404, message = "Not found"),
		new ApiResponse(code = 451, message = "request process failed"),
		new ApiResponse(code = 500, message = "internal server error")
	))
	@Path("/app")
	def createApplication = (session: Session) => {
		path("app") {
			post {
				entity(as[ApplicationIn]) { in =>
					onComplete(appService.createApplication(in)(session)) {
						case Success(either) =>
							either.fold(
								_ => complete(OK, Response(code = 200, msg = "Success")),
								exception => {
									val response = exception match {
										case e: ApplicationExistsException =>
											Response(code = 100, msg = e.getMessage)
										case e => Response(code = 451, msg = e.getMessage)
									}
									complete(OK, response)
								}
							)
						case Failure(e) =>
							complete(OK, Response(code = 451, msg = e.getMessage))
					}
				}
			}
		}
	}

	@ApiOperation(value = "update exist application", nickname = "update", httpMethod = "PUT")
	@ApiImplicitParams(Array(
		new ApiImplicitParam(name = "Update Application", value = "Update Application Parameter Information", required = true, paramType = "body", dataType = "moonbox.grid.deploy.rest.entities.ApplicationIn")
	))
	@ApiResponses(Array(
		new ApiResponse(code = 200, message = "OK"),
		new ApiResponse(code = 404, message = "application not found"),
		new ApiResponse(code = 451, message = "request process failed"),
		new ApiResponse(code = 500, message = "internal server error")
	))
	@Path("/app")
	def updateApplication = (session: Session) => {
		path("app") {
			post {
				entity(as[ApplicationIn]) { in =>
					onComplete(appService.createApplication(in)(session)) {
						case Success(either) =>
							either.fold(
								_ => complete(OK, Response(code = 200, msg = "Success")),
								exception => {
									val response = exception match {
										case e: NoSuchApplicationException =>
											Response(code = 404, msg = e.getMessage)
										case e =>
											Response(code = 451, msg = e.getMessage)
									}
									complete(OK, response)
								}
							)
						case Failure(e) =>
							complete(OK, Response(code = 451, msg = e.getMessage))
					}
				}
			}
		}
	}

	@ApiOperation(value = "get application by name", nickname = "getApp", httpMethod = "GET")
	@ApiImplicitParams(Array(
		new ApiImplicitParam(name = "appName", value = "application name", required = true, paramType = "path", dataType = "string")
	))
	@ApiResponses(Array(
		new ApiResponse(code = 200, message = "OK"),
		new ApiResponse(code = 404, message = "application not found"),
		new ApiResponse(code = 451, message = "request process failed"),
		new ApiResponse(code = 500, message = "internal server error")
	))
	@Path("/app/{appName}")
	def getApplication = (session: Session) => path("app" / Segment) { appName =>
		get {
			onComplete(appService.getApplication(appName)(session)) {
				case Success(either) =>
					either.fold(
						app => complete(OK, Response(code = 200, msg = "Success", payload = Some(app))),
						exception => {
							val response = exception match {
								case e: NoSuchApplicationException =>
									Response(code = 404, msg = e.getMessage)
								case e =>
									Response(code = 451, msg = e.getMessage)
							}
							complete(OK, response)
						}
					)
				case Failure(e) =>
					complete(OK, Response(code = 451, msg = e.getMessage))
			}
		}
	}

	@ApiOperation(value = "list all applications", nickname = "listApps", httpMethod = "GET", responseContainer = "set")
	@ApiResponses(Array(
		new ApiResponse(code = 200, message = "OK"),
		new ApiResponse(code = 451, message = "request process failed"),
		new ApiResponse(code = 500, message = "internal server error")
	))
	@Path("/apps")
	def listApplications = (session: Session) => path("apps") {
		get {
			onComplete(appService.listApplications(session)) {
				case Success(either) =>
					either.fold(
						apps => complete(OK, Response(code = 200, msg = "Success", payload = Some(apps))),
						exception => complete(OK, Response(code = 451, msg = exception.getMessage))
					)
				case Failure(e) =>
					complete(OK, Response(code = 451, msg = e.getMessage))
			}
		}
	}

	@ApiOperation(value = "delete application by name", nickname = "delete", httpMethod = "DELETE")
	@ApiImplicitParams(Array(
		new ApiImplicitParam(name = "appName", value = "application name", required = true, dataType = "string", paramType = "path")
	))
	@ApiResponses(Array(
		new ApiResponse(code = 200, message = "OK"),
		new ApiResponse(code = 404, message = "Not found"),
		new ApiResponse(code = 451, message = "request process failed"),
		new ApiResponse(code = 500, message = "internal server error")
	))
	@Path("/app/{appName}")
	def deleteApplication = (session: Session) => path("apps" / Segment) { appName =>
		delete {
			logInfo("delete " + appName)
			onComplete(appService.deleteApplication(appName)(session)) {
				case Success(either) =>
					either.fold(
						_ => complete(OK, Response(code = 200, msg = "Success")),
						exception => {
							val response = exception match {
								case e: NoSuchApplicationException =>
									Response(code = 404, msg = e.getMessage)
								case e =>
									Response(code = 451, msg = e.getMessage)
							}
							complete(OK, response)
						}
					)
				case Failure(e) =>
					complete(OK, Response(code = 451, msg = e.getMessage))
			}
		}
	}

	@ApiOperation(value = "start application by name", nickname = "start", httpMethod = "PUT")
	@ApiImplicitParams(Array(
		new ApiImplicitParam(name = "appName", value = "application name", required = true, dataType = "string", paramType = "path")
	))
	@ApiResponses(Array(
		new ApiResponse(code = 200, message = "OK"),
		new ApiResponse(code = 404, message = "application not found"),
		new ApiResponse(code = 451, message = "request process failed"),
		new ApiResponse(code = 500, message = "internal server error")
	))
	@Path("/app/{appName}/start")
	def startApplication = (session: Session) => path("app" / Segment / "start") { appName =>
		put {
			logInfo("start " + appName)
			onComplete(appService.startApplication(appName)(session)) {
				case Success(either) =>
					either.fold(
						msg => complete(OK, Response(code = 200, msg = msg)),
						exception => {
							val response = exception match {
								case e: NoSuchApplicationException =>
									Response(code = 404, msg = e.getMessage)
								case e =>
									Response(code = 451, msg = e.getMessage)
							}
							complete(OK, response)
						}
					)
				case Failure(e) =>
					complete(OK, Response(code = 451, msg = e.getMessage))
			}
		}
	}

	@ApiOperation(value = "stop application by name", nickname = "stop", httpMethod = "PUT")
	@ApiImplicitParams(Array(
		new ApiImplicitParam(name = "appName", value = "application name", required = true, dataType = "string", paramType = "path")
	))
	@ApiResponses(Array(
		new ApiResponse(code = 200, message = "OK"),
		new ApiResponse(code = 404, message = "Not found"),
		new ApiResponse(code = 451, message = "request process failed"),
		new ApiResponse(code = 500, message = "internal server error")
	))
	@Path("/app/{appName}/stop")
	def stopApplication = (session: Session) => path("app" / Segment / "stop") { appName =>
		put {
			logInfo("stop " + appName)
			onComplete(appService.stopApplication(appName)(session)) {
				case Success(either) =>
					either.fold(
						_ => complete(OK, Response(code = 200, msg = "Success")),
						exception => {
							val response = exception match {
								case e: NoSuchApplicationException =>
									Response(code = 404, msg = e.getMessage)
								case e =>
									Response(code = 451, msg = e.getMessage)
							}
							complete(OK, response)
						}
					)
				case Failure(e) =>
					complete(OK, Response(code = 451, msg = e.getMessage))
			}
		}
	}

	@ApiOperation(value = "get running application info", nickname = "appInfo", httpMethod = "GET", responseContainer = "set")
	@ApiResponses(Array(
		new ApiResponse(code = 200, message = "OK"),
		new ApiResponse(code = 451, message = "request process failed"),
		new ApiResponse(code = 500, message = "internal server error")
	))
	@Path("/app-infos")
	def applicationInfos = (session: Session) => path("app-infos") {
		get {
			logInfo("get application state")
			onComplete(appService.getApplicationInfos(session)) {
				case Success(either) =>
					either.fold(
						appInfo => complete(OK, Response(code = 200, msg = "Success", payload = Some(appInfo))),
						exception => complete(OK, Response(code = 451, msg = exception.getMessage))
					)
				case Failure(e) =>
					complete(OK, Response(code = 451, msg = e.getMessage))
			}
		}
	}

	@ApiOperation(value = "get application templates", nickname = "templates", httpMethod = "GET", responseContainer = "set")
	@ApiResponses(Array(
		new ApiResponse(code = 200, message = "OK"),
		new ApiResponse(code = 451, message = "request process failed"),
		new ApiResponse(code = 500, message = "internal server error")
	))
	@Path("/templates")
	def applicationTemplate = (session: Session) => path("templates") {
		get {
			logInfo("get application templates")
			onComplete(appService.getApplicationTemplates(session)) {
				case Success(either) =>
					either.fold(
						templates => complete(OK, Response(code = 200, msg = "Success", payload = Some(templates))),
						exception => complete(OK, Response(code = 451, msg = exception.getMessage))
					)
				case Failure(e) =>
					complete(OK, Response(code = 451, msg = e.getMessage))
			}
		}
	}

	override def createSecurityRoute: Array[Session => Route] = Array(
		applicationInfos,
		applicationTemplate,
		createApplication,
		deleteApplication,
		getApplication,
		listApplications,
		startApplication,
		stopApplication
	)
}
