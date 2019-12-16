package moonbox.grid.deploy.rest.routes

import javax.ws.rs.Path

import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Route
import io.swagger.annotations.{ApiResponse, ApiResponses, _}
import moonbox.grid.deploy.rest.entities.{Organization, Response}
import moonbox.grid.deploy.rest.service.{LoginService, OrganizationService}
import moonbox.grid.deploy.security.Session

import scala.util.{Failure, Success}

@Api(
  value = "Organization",
  consumes = "application/json",
  produces = "application/json", authorizations = Array(new Authorization("Bearer")))
@Path("/orgs")
class OrganizationRoute(override val loginService: LoginService, orgService: OrganizationService) extends SecurityRoute with SessionConverter {

  @ApiOperation(value = "create a new Org", nickname = "create", httpMethod = "POST")
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "Create Org", value = "Create Org Parameter Information", required = true, paramType = "body", dataType = "moonbox.grid.deploy.rest.entities.Organization")
  ))
  @ApiResponses(Array(
    new ApiResponse(code = 200, message = "OK"),
    new ApiResponse(code = 451, message = "Request process failed"),
    new ApiResponse(code = 500, message = "Internal server error")
  ))
  def createOrg = (session: Session) => {
    post {
      entity(as[Organization]) { org =>
        onComplete(orgService.createOrg(org)(session)) {
          case Success(_) =>
            complete(OK, Response(code = 200, msg = "Success"))
          case Failure(e) =>
            complete(OK, Response(code = 451, msg = e.getMessage))
        }
      }
    }
  }

  @ApiOperation(value = "update org", nickname = "update", httpMethod = "PUT")
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "Update Org", value = "Update Org Parameter Information", required = true, paramType = "body", dataType = "moonbox.grid.deploy.rest.entities.Organization")
  ))
  @ApiResponses(Array(
    new ApiResponse(code = 200, message = "OK"),
    new ApiResponse(code = 451, message = "Request process failed"),
    new ApiResponse(code = 500, message = "Internal server error")
  ))
  def updateOrg = (session: Session) => {
    put {
      entity(as[Organization]) { org =>
        onComplete(orgService.updateOrg(org)(session)) {
          case Success(_) =>
            complete(OK, Response(code = 200, msg = "Success"))
          case Failure(e) =>
            complete(OK, Response(code = 451, msg = e.getMessage))
        }
      }
    }
  }

  @ApiOperation(value = "delete org cascade by name", nickname = "delete", httpMethod = "DELETE")
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "Delete Org Cascade", value = "Delete Org Parameter Information", required = true, paramType = "path", dataType = "string")
  ))
  @ApiResponses(Array(
    new ApiResponse(code = 200, message = "OK"),
    new ApiResponse(code = 451, message = "Request process failed"),
    new ApiResponse(code = 500, message = "Internal server error")
  ))
  @Path("/{name}")
  def deleteOrgCascade = (session: Session) => path(Segment) { orgName =>
    delete {
      onComplete(orgService.deleteOrgCascade(orgName)(session)) {
        case Success(_) =>
          complete(OK, Response(code = 200, msg = "Success"))
        case Failure(e) =>
          complete(OK, Response(code = 451, msg = e.getMessage))
      }
    }
  }

  @ApiOperation(value = "get org by name", nickname = "get", httpMethod = "GET")
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "Get Org", value = "Get Org Information", required = true, paramType = "path", dataType = "string")
  ))
  @ApiResponses(Array(
    new ApiResponse(code = 200, message = "OK"),
    new ApiResponse(code = 451, message = "Request process failed"),
    new ApiResponse(code = 500, message = "Internal server error")
  ))
  @Path("/{name}")
  def getOrg = (session: Session) => path(Segment) { orgName =>
    get {
      onComplete(orgService.getOrg(orgName)(session)) {
        case Success(org) =>
          complete(OK, Response(code = 200, msg = "Success", payload = Some(org)))
        case Failure(e) =>
          complete(OK, Response(code = 451, msg = e.getMessage))
      }
    }
  }

  @ApiOperation(value = "list orgs", nickname = "list", httpMethod = "GET")
  @ApiResponses(Array(
    new ApiResponse(code = 200, message = "OK"),
    new ApiResponse(code = 451, message = "Request process failed"),
    new ApiResponse(code = 500, message = "Internal server error")
  ))
  def listOrgs = (session: Session) => {
    get {
      onComplete(orgService.listOrgs()(session)) {
        case Success(orgs) =>
          complete(OK, Response(code = 200, msg = "Success", payload = Some(orgs)))
        case Failure(e) =>
          complete(OK, Response(code = 451, msg = e.getMessage))
      }
    }
  }

  override protected def createSecurityRoute: Array[(Session) => Route] = Array(
    createOrg, updateOrg, getOrg, deleteOrgCascade, listOrgs
  )
}
