package moonbox.grid.deploy.authenticate

import java.util.concurrent.{ConcurrentHashMap, TimeUnit}

import moonbox.common.MbLogging
import moonbox.common.util.{ThreadUtils, Utils}
import moonbox.core.CatalogContext
import moonbox.grid.config._
import scala.collection.JavaConversions._

class LoginManager(catalogContext: CatalogContext) extends MbLogging {
	private lazy val conf = catalogContext.conf
	private lazy val loginType = conf.get(LOGIN_IMPLEMENTATION.key, LOGIN_IMPLEMENTATION.defaultValueString)
	private lazy val timeout = conf.get(LOGIN_TIMEOUT.key, LOGIN_TIMEOUT.defaultValue.get)

	private lazy val loginImpl = createLogin(loginType)
	private lazy val userToLastActiveTime = new ConcurrentHashMap[String, Long]()


	private val cleanTimeoutCatalogSessionThread =
		ThreadUtils.newDaemonSingleThreadScheduledExecutor("loginManager-clean-timeout")

	cleanTimeoutCatalogSessionThread.scheduleAtFixedRate(new Runnable {
		override def run(): Unit = {
			userToLastActiveTime.foreach { case (u, t) =>
				if ((Utils.now - t) >= timeout) {
					userToLastActiveTime.remove(u)
				}
			}
		}
	}, timeout, timeout / 2, TimeUnit.MILLISECONDS)

	def login(username: String, password: String): Boolean = {
		val login = loginImpl.doLogin(username, password)
		if (login) {
			userToLastActiveTime.put(username, Utils.now)
		}
		login
	}

	def logout(username: String): Unit = {
		userToLastActiveTime.remove(username)
	}

	def isLogin(username: String): Boolean = {
		val contains = userToLastActiveTime.containsKey(username)
		if (contains) {
			userToLastActiveTime.put(username, Utils.now)
		}
		contains
	}

	private def createLogin(loginType: String): Login = loginType.toUpperCase match  {
		case "LDAP" => new LdapLogin(conf)
		case _ => new CatalogLogin(catalogContext)
	}
}
