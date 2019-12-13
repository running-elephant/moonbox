package moonbox.grid.deploy.rest.service

import akka.http.scaladsl.server.directives.Credentials
import moonbox.common.MbLogging
import moonbox.grid.config._
import moonbox.grid.deploy.rest.TokenEncoder
import moonbox.grid.deploy.rest.entities.LoginResult
import moonbox.grid.deploy.security.{LoginFailedException, LoginManager, Session}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class LoginService(loginManager: LoginManager) extends MbLogging {
  private val tokenEncoder = new TokenEncoder(loginManager.conf)

  // time unit: ms
  private val LOGIN_TIMEOUT_SECOND = loginManager.conf.get(LOGIN_TIMEOUT) / 1000

	def login(user: String, password: String): Future[Either[LoginResult, Throwable]] = {
		try {
			val session = loginManager.login(user, password)
			val token = generateToken(session)
			val roleType = session("roleType")
			Future(Left(LoginResult(token, roleType.toInt)))
		} catch {
			case e: Throwable => Future(Right(e))
		}
	}

  def generateToken(session: Session): String = {
    tokenEncoder.encode(session, LOGIN_TIMEOUT_SECOND)
  }

  def authorize(credentials: Credentials): Future[Option[Session]] = {
    credentials match {
      case Credentials.Provided(token) => Future(tokenEncoder.decode(token))
      case _ => Future(None)
    }
  }
}
