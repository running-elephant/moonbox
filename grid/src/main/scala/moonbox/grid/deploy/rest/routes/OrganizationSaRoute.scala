package moonbox.grid.deploy.rest.routes

import javax.ws.rs.Path

import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Route
import io.swagger.annotations.{ApiResponse, ApiResponses, _}
import moonbox.grid.deploy.rest.entities._
import moonbox.grid.deploy.rest.service.{LoginService, OrganizationSaService}
import moonbox.grid.deploy.security.Session

import scala.util.{Failure, Success}

@Api(
  value = "Organization-Sa",
  consumes = "application/json",
  produces = "application/json", authorizations = Array(new Authorization("Bearer")))
@Path("sas")
class OrganizationSaRoute(override val loginService: LoginService, saService: OrganizationSaService) extends SecurityRoute with SessionConverter {

  @ApiOperation(value = "create a new sa", nickname = "create", httpMethod = "POST")
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "Create Sa", value = "Create Sa Parameter Information", required = true, paramType = "body", dataType = "moonbox.grid.deploy.rest.entities.OrgSa")
  ))
  @ApiResponses(Array(
    new ApiResponse(code = 200, message = "OK"),
    new ApiResponse(code = 451, message = "Request process failed"),
    new ApiResponse(code = 500, message = "Internal server error")
  ))
  def createSa = (session: Session) => {
    post {
      entity(as[OrgSa]) { sa =>
        onComplete(saService.createSa(sa)(session)) {
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

  @ApiOperation(value = "update sa name", nickname = "rename", httpMethod = "PUT")
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "Update Sa Name", value = "Update Sa Name Parameter Information", required = true, paramType = "body", dataType = "moonbox.grid.deploy.rest.entities.OrgSaRename")
  ))
  @ApiResponses(Array(
    new ApiResponse(code = 200, message = "OK"),
    new ApiResponse(code = 451, message = "Request process failed"),
    new ApiResponse(code = 500, message = "Internal server error")
  ))
  @Path("/rename")
  def updateName = (session: Session) => {
    put {
      entity(as[OrgSaRename]) { rename =>
        onComplete(saService.updateName(rename)(session)) {
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

  @ApiOperation(value = "update sa password", nickname = "alterPassword", httpMethod = "PUT")
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "Update Sa Password", value = "Update Sa Password Parameter Information", required = true, paramType = "body", dataType = "moonbox.grid.deploy.rest.entities.OrgSaRename")
  ))
  @ApiResponses(Array(
    new ApiResponse(code = 200, message = "OK"),
    new ApiResponse(code = 451, message = "Request process failed"),
    new ApiResponse(code = 500, message = "Internal server error")
  ))
  @Path("/alterpassword")
  def updatePassword = (session: Session) => {
    put {
      entity(as[OrgSa]) { orgSa =>
        onComplete(saService.updatePassword(orgSa)(session)) {
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

  @ApiOperation(value = "delete sa by name", nickname = "delete", httpMethod = "DELETE")
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "Sa List", value = "Sa List Parameter", required = true, paramType = "body", dataType = "moonbox.grid.deploy.rest.entities.BatchOpSaSeq")
  ))
  @ApiResponses(Array(
    new ApiResponse(code = 200, message = "OK"),
    new ApiResponse(code = 451, message = "Request process failed"),
    new ApiResponse(code = 500, message = "Internal server error")
  ))
  def deleteSas = (session: Session) => {
    delete {
      entity(as[BatchOpSaSeq]) {
        batchOp =>
          onComplete(saService.deleteSas(batchOp)(session)) {
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

  @ApiOperation(value = "get sa", nickname = "get", httpMethod = "GET")
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "orgName", value = "sa org name", required = true, paramType = "path", dataType = "string"),
    new ApiImplicitParam(name = "saName", value = "sa name", required = true, paramType = "path", dataType = "string")
  ))
  @ApiResponses(Array(
    new ApiResponse(code = 200, message = "OK"),
    new ApiResponse(code = 451, message = "Request process failed"),
    new ApiResponse(code = 500, message = "Internal server error")
  ))
  @Path("/{orgName}/{saName}")
  def getSa = (session: Session) => path(Segment / Segment) { (orgName, saName) =>
    get {
      onComplete(saService.getSa(orgName, saName)(session)) {
        case Success(either) =>
          either.fold(
            sa => complete(OK, Response(code = 200, msg = "Success", payload = Some(sa))),
            exception => complete(OK, Response(code = 451, msg = exception.getMessage))
          )
        case Failure(e) =>
          complete(OK, Response(code = 451, msg = e.getMessage))
      }
    }
  }

  @ApiOperation(value = "list sas", nickname = "list", httpMethod = "GET")
  @ApiResponses(Array(
    new ApiResponse(code = 200, message = "OK"),
    new ApiResponse(code = 451, message = "Request process failed"),
    new ApiResponse(code = 500, message = "Internal server error")
  ))
  def listSas = (session: Session) => {
    get {
      onComplete(saService.listSas()(session)) {
        case Success(either) =>
          either.fold(
            sas => complete(OK, Response(code = 200, msg = "Success", payload = Some(sas))),
            exception => complete(OK, Response(code = 451, msg = exception.getMessage))
          )
        case Failure(e) =>
          complete(OK, Response(code = 451, msg = e.getMessage))
      }
    }
  }

  override protected def createSecurityRoute: Array[(Session) => Route] = Array(
    createSa, updateName, updatePassword, getSa, deleteSas, listSas
  )
}
