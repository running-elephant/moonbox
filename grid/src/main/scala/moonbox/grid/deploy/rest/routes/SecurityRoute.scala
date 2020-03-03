package moonbox.grid.deploy.rest.routes

import akka.http.scaladsl.model.headers.RawHeader
import akka.http.scaladsl.server.Route
import moonbox.grid.deploy.rest.service.LoginService
import moonbox.grid.deploy.security.Session

trait SecurityRoute extends CrossDomainRoute {

  val loginService: LoginService

	final override  val createRoute: Route = {
		authenticateOAuth2Async[Session]("Bearer", loginService.authorize) { session =>
			val token = loginService.generateToken(session)
			createSecurityRoute.map { r =>
				respondWithHeaders(RawHeader("Authorization", token))(r(session))
			}.reduce(_ ~ _)
		}
	}

  protected def createSecurityRoute: Array[Session => Route]

}
