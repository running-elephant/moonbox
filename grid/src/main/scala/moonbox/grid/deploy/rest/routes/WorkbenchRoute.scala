package moonbox.grid.deploy.rest.routes
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.model.StatusCodes.OK
import moonbox.grid.deploy.rest.service.LoginService
import moonbox.grid.deploy.security.Session

class WorkbenchRoute(override val loginService: LoginService) extends SecurityRoute {

	def createConsole = (session: Session) => path("") {
		post {
			complete(OK)
		}
	}

	override protected def createSecurityRoute: Array[(Session) => Route] = ???
}
