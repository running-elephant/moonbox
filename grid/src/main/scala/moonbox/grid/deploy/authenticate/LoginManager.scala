package moonbox.grid.deploy.authenticate

import java.util.concurrent.{ConcurrentHashMap, TimeUnit}

import moonbox.common.MbLogging
import moonbox.common.util.{ThreadUtils, Utils}
import moonbox.core.CatalogContext
import moonbox.grid.config._
import moonbox.grid.deploy.master.MbMaster
import moonbox.grid.deploy.rest.TokenManager

import scala.collection.JavaConversions._

class LoginManager(catalogContext: CatalogContext, val tokenManager: TokenManager) extends MbLogging {
	private lazy val conf = catalogContext.conf
	private lazy val loginType = conf.get(LOGIN_IMPLEMENTATION.key, LOGIN_IMPLEMENTATION.defaultValueString)
	private lazy val timeout = conf.get(LOGIN_TIMEOUT.key, LOGIN_TIMEOUT.defaultValue.get)

	private lazy val loginImpl = createLogin(loginType)
	private lazy val tokenToLastActiveTime = new ConcurrentHashMap[String, Long]()

	private val cleanTimeoutCatalogSessionThread =
		ThreadUtils.newDaemonSingleThreadScheduledExecutor("loginManager-clean-timeout")

	cleanTimeoutCatalogSessionThread.scheduleAtFixedRate(new Runnable {
		override def run(): Unit = {
			val user = Set[String]()
			tokenToLastActiveTime.foreach { case (u, t) =>
				if ((Utils.now - t) >= timeout) {
					user.add(u)
					tokenToLastActiveTime.remove(u)
				}
			}
		}
	}, timeout, timeout / 2, TimeUnit.MILLISECONDS)

	def login(username: String, password: String): Option[String] = {
		if (loginImpl.doLogin(username, password)) {
			val token = tokenManager.encode(username)
			tokenToLastActiveTime.put(token, System.currentTimeMillis())
			Some(token)
		} else None
	}

	def logout(token: String): Unit = {
		tokenToLastActiveTime.remove(token)
	}

	def isLogin(token: String): Option[String] = {
		if (tokenToLastActiveTime.containsKey(token)) {
			tokenToLastActiveTime.update(token, System.currentTimeMillis())
			tokenManager.decode(token)
		} else None
	}

	private def createLogin(loginType: String): Login = loginType.toUpperCase match  {
		case "LDAP" => new LdapLogin(conf)
		case _ => new CatalogLogin(catalogContext)
	}
}
