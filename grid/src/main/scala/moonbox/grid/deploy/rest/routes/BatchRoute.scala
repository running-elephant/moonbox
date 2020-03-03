package moonbox.grid.deploy.rest.routes

import javax.ws.rs.Path

import akka.http.scaladsl.server.Route
import akka.http.scaladsl.model.StatusCodes.OK
import io.swagger.annotations.{ApiResponse, ApiResponses, _}
import moonbox.grid.deploy.rest.entities.{BatchSubmit, Response}
import moonbox.grid.deploy.rest.service.{GridService, LoginService}
import moonbox.grid.deploy.security.Session

import scala.util.{Failure, Success}

@Api(
	value = "Batch",
	consumes = "application/json",
	produces = "application/json", authorizations = Array(new Authorization("Bearer")))
@Path("/batch")
class BatchRoute(override val loginService: LoginService, gridService: GridService) extends SecurityRoute with SessionConverter {

	@ApiOperation(value = "submit batch job", nickname = "submit", httpMethod = "POST")
	@ApiImplicitParams(Array(
		new ApiImplicitParam(name = "submit batch job", value = "Submit Batch Job Parameter Information", required = true, paramType = "body", dataType = "moonbox.grid.deploy.rest.entities.BatchSubmit")
	))
	@ApiResponses(Array(
		new ApiResponse(code = 200, message = "OK"),
		new ApiResponse(code = 451, message = "request process failed"),
		new ApiResponse(code = 500, message = "internal server error")
	))
	@Path("/submit")
	def submit = (session: Session) => path("submit") {
		post {
			entity(as[BatchSubmit]) { batchJob =>
				onComplete(gridService.submit(batchJob)(session)) {
					case Success(either) =>
						either.fold(
							jobId => complete(OK, Response(code = 200, msg = "Success", payload = Some(jobId))),
							exception => complete(OK, Response(code = 451, msg = exception.getMessage))
						)
					case Failure(e) =>
						complete(OK, Response(code = 451, msg = e.getMessage))
				}
			}
		}
	}

	@ApiOperation(value = "get batch job progress", nickname = "progress", httpMethod = "GET")
	@ApiImplicitParams(Array(
		new ApiImplicitParam(name = "jobId", value = "Get Batch Job Progress Parameter Information", required = true, paramType = "path")
	))
	@ApiResponses(Array(
		new ApiResponse(code = 200, message = "OK"),
		new ApiResponse(code = 451, message = "request process failed"),
		new ApiResponse(code = 500, message = "internal server error")
	))
	@Path("/progress/{jobId}")
	def progress = (session: Session) => path("progress" / Segment) { jobId =>
		get {
			logInfo(s"get batch job progress for $jobId")
			complete(OK)
		}
	}

	@ApiOperation(value = "cancel batch job", nickname = "cancel", httpMethod = "POST")
	@ApiImplicitParams(Array(
		new ApiImplicitParam(name = "jobId", value = "Cancel Batch Job Progress Parameter Information", required = true, paramType = "path")
	))
	@ApiResponses(Array(
		new ApiResponse(code = 200, message = "OK"),
		new ApiResponse(code = 451, message = "request process failed"),
		new ApiResponse(code = 500, message = "internal server error")
	))
	@Path("/cancel/{jobId}")
	def cancel = (session: Session) => path("cancel" / Segment) { jobId =>
		post {
			logInfo(s"cancel batch job: $jobId")
			complete(OK)
		}
	}

	override protected def createSecurityRoute: Array[(Session) => Route] = Array(
		submit, progress, cancel
	)
}
