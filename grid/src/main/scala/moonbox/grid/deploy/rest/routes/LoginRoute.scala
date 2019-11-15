package moonbox.grid.deploy.rest.routes

import javax.ws.rs.Path

import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Route
import io.swagger.annotations._
import moonbox.grid.deploy.rest.entities.{Login, Response, ResponseHeader}
import moonbox.grid.deploy.rest.service.LoginService
import moonbox.grid.deploy.security.{PasswordNotMatchException, UserNotFoundException, UsernameFormatException}

import scala.util.{Failure, Success}

@Api(value = "Login", consumes = "application/json", produces = "application/json")
@Path("/login")
class LoginRoute(loginService: LoginService) extends CrossDomainRoute {

	@ApiOperation(value = "Login and return token", nickname = "login", httpMethod = "POST")
	@ApiImplicitParams(Array(
		new ApiImplicitParam(name = "Login", value = "Login Parameter Information", required = true, paramType = "body", dataType = "moonbox.grid.deploy.rest.entities.Login")
	))
	@ApiResponses(Array(
		new ApiResponse(code = 200, message = "OK"),
		new ApiResponse(code = 210, message = "Wrong password"),
		new ApiResponse(code = 211, message = "Invalid username format"),
		new ApiResponse(code = 405, message = "User not found"),
		new ApiResponse(code = 451, message = "request process failed"),
		new ApiResponse(code = 500, message = "internal server error")
	))
	def login = path("login") {
		post {
			entity(as[Login]) { login =>
				onComplete(loginService.login(login.user, login.password)) {
					case Success(either) =>
						either.fold(
							token => {
								complete(OK, Response(ResponseHeader(code = 200, msg = "Success", token = Some(token))))
							},
							exception => {
								val header = exception match {
									case u: UserNotFoundException =>
										ResponseHeader(405, u.getMessage)
									case u: UsernameFormatException =>
										ResponseHeader(211, u.getMessage)
									case e: PasswordNotMatchException =>
										ResponseHeader(210, e.getMessage)
									case e =>
										ResponseHeader(451, e.getMessage)
								}
								complete(OK, Response(header))
							}
						)
					case Failure(e) =>
						complete(OK, Response(ResponseHeader(451, e.getMessage)))
				}
			}
		}
	}

	protected override lazy val createRoute: Route = login

}
