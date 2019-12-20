package moonbox.grid.deploy.rest.routes

import javax.ws.rs.Path

import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Route
import io.swagger.annotations._
import moonbox.grid.deploy.rest.entities.{Cluster, Response}
import moonbox.grid.deploy.rest.service.{ClusterService, LoginService}
import moonbox.grid.deploy.security.Session

import scala.util.{Failure, Success}

@Api(
	value = "Cluster",
	consumes = "application/json",
	produces = "application/json", authorizations = Array(new Authorization("Bearer")))
@Path("/clusters")
class ClusterRoute(override val loginService: LoginService, clusterService: ClusterService) extends SecurityRoute with SessionConverter {

	@ApiOperation(value = "create a new cluster", nickname = "create", httpMethod = "POST")
	@ApiImplicitParams(Array(
		new ApiImplicitParam(name = "create Cluster", value = "Create Cluster Parameter Information", required = true, paramType = "body", dataType = "moonbox.grid.deploy.rest.entities.Cluster")
	))
	@ApiResponses(Array(
		new ApiResponse(code = 200, message = "OK"),
		new ApiResponse(code = 451, message = "request process failed"),
		new ApiResponse(code = 500, message = "internal server error")
	))
	def createCluster = (session: Session) => {
		post {
			entity(as[Cluster]) { cluster =>
				onComplete(clusterService.createCluster(cluster)(session)) {
					case Success(either) =>
						either.fold(
							_ => complete(OK, Response(code = 200, msg = "Success")),
							exception => complete(OK, Response(code = 451, msg = exception.getMessage))
						)
					case Failure(e) =>
						complete(OK, Response(code = 451, msg = e.getMessage))
				}
			}
		}
	}

	@ApiOperation(value = "update cluster", nickname = "update", httpMethod = "PUT")
	@ApiImplicitParams(Array(
		new ApiImplicitParam(name = "Update Cluster", value = "Update Cluster Parameter Information", required = true, paramType = "body", dataType = "moonbox.grid.deploy.rest.entities.Cluster")
	))
	@ApiResponses(Array(
		new ApiResponse(code = 200, message = "OK"),
		new ApiResponse(code = 451, message = "request process failed"),
		new ApiResponse(code = 500, message = "internal server error")
	))
	def updateCluster = (session: Session) => {
		put {
			entity(as[Cluster]) { cluster =>
				onComplete(clusterService.updateCluster(cluster)(session)) {
					case Success(either) =>
						either.fold(
							_ => complete(OK, Response(code = 200, msg = "Success")),
							exception => complete(OK, Response(code = 451, msg = exception.getMessage))
						)
					case Failure(e) =>
						complete(OK, Response(code = 451, msg = e.getMessage))
				}
			}
		}
	}

	@ApiOperation(value = "delete cluster", nickname = "delete", httpMethod = "DELETE")
	@ApiImplicitParams(Array(
		new ApiImplicitParam(name = "delete cluster", value = "Delete Cluster Parameter Information", required = true, paramType = "path", dataType = "string")
	))
	@ApiResponses(Array(
		new ApiResponse(code = 200, message = "OK"),
		new ApiResponse(code = 451, message = "request process failed"),
		new ApiResponse(code = 500, message = "internal server error")
	))
	@Path("/{clusterName}")
	def deleteCluster = (session: Session) => path(Segment) { cluster =>
		delete {
			logInfo("")
			onComplete(clusterService.deleteCluster(cluster)(session)) {
				case Success(either) =>
					either.fold(
						_ => complete(OK, Response(code = 200, msg = "Success")),
						exception => complete(OK, Response(code = 451, msg = exception.getMessage))
					)
				case Failure(e) => complete(OK, Response(code = 451, msg = e.getMessage))
			}
		}
	}

	@ApiOperation(value = "get cluster by name", nickname = "getCluster", httpMethod = "GET")
	@ApiImplicitParams(Array(
		new ApiImplicitParam(name = "clusterName", value = "cluster name", required = true, paramType = "path", dataType = "string")
	))
	@ApiResponses(Array(
		new ApiResponse(code = 200, message = "OK"),
		new ApiResponse(code = 451, message = "request process failed"),
		new ApiResponse(code = 500, message = "internal server error")
	))
	@Path("/{clusterName}")
	def getCluster = (session: Session) => path(Segment) { cluster =>
		get {
			onComplete(clusterService.getCluster(cluster)(session)) {
				case Success(either) =>
					either.fold(
						cluster => complete(OK, Response(code = 200, msg = "Success", payload = Some(cluster))),
						exception => complete(OK, Response(code = 451, msg = exception.getMessage))
					)
				case Failure(e) => complete(OK, Response(code = 451, msg = e.getMessage))
			}
		}
	}

	@ApiOperation(value = "list clusters", nickname = "list", httpMethod = "GET")
	@ApiResponses(Array(
		new ApiResponse(code = 200, message = "OK"),
		new ApiResponse(code = 451, message = "request process failed"),
		new ApiResponse(code = 500, message = "internal server error")
	))
	def listClusters = (session: Session) => {
		get {
			logInfo("list Clusters")
			onComplete(clusterService.listClusters()(session)) {
				case Success(either) =>
					either.fold(
						clusters => complete(OK, Response(code = 200, msg = "Success", payload = Some(clusters))),
						exception => complete(OK, Response(code = 451, msg = exception.getMessage))
					)
				case Failure(e) => complete(OK, Response(code = 451, msg = e.getMessage))
			}
		}
	}

	override protected def createSecurityRoute: Array[(Session) => Route] = Array(
		createCluster, updateCluster, deleteCluster, getCluster, listClusters
	)
}
