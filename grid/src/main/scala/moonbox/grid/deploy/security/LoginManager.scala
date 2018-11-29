/*-
 * <<
 * Moonbox
 * ==
 * Copyright (C) 2016 - 2018 EDP
 * ==
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * >>
 */

package moonbox.grid.deploy.security

import java.net.InetSocketAddress
import java.util.concurrent.{ConcurrentHashMap, TimeUnit}

import moonbox.common.MbLogging
import moonbox.common.util.{ThreadUtils, Utils}
import moonbox.core.CatalogContext
import moonbox.grid.config._
import moonbox.grid.deploy.{ConnectionInfo, ConnectionType, MbService}
import moonbox.grid.deploy.rest.TokenManager

import scala.collection.JavaConversions._

class LoginManager(catalogContext: CatalogContext, val tokenManager: TokenManager, mbService: MbService) extends MbLogging {
  private lazy val conf = catalogContext.conf
  private lazy val loginType = conf.get(LOGIN_IMPLEMENTATION.key, LOGIN_IMPLEMENTATION.defaultValueString)
  private lazy val timeout = conf.get(LOGIN_TIMEOUT.key, LOGIN_TIMEOUT.defaultValue.get / 1000) * 1000 // time unit: ms

  private lazy val loginImpl = createLogin(loginType)
  private lazy val tokenToLastActiveTime = new ConcurrentHashMap[String, Long]()
  private val tokenToSessionId = new ConcurrentHashMap[String, String]()

  private val cleanTimeoutCatalogSessionThread =
    ThreadUtils.newDaemonSingleThreadScheduledExecutor("loginManager-clean-timeout")

  cleanTimeoutCatalogSessionThread.scheduleAtFixedRate(new Runnable {
    override def run(): Unit = {
      tokenToLastActiveTime.foreach { case (u, t) =>
        if ((Utils.now - t) >= timeout) {
          logInfo(s"Token timeout, closing according session: Token=$u")
          Option(tokenToSessionId.get(u)).map(sessionId =>
			  mbService.closeSession(u, sessionId)(ConnectionInfo(
				  "system", // TODO
				  "system",
				  ConnectionType.SYSTEM
			  ))
		  )
          tokenToLastActiveTime.remove(u)
        }
      }
    }
  }, timeout, 60 * 1000, TimeUnit.MILLISECONDS)

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

  def isvalid(token: String): Boolean = {
    if (tokenToLastActiveTime.containsKey(token)) {
      tokenToLastActiveTime.update(token, System.currentTimeMillis())
      true
    } else false
  }

  private def createLogin(loginType: String): Login = loginType.toUpperCase match {
    case "LDAP" => new LdapLogin(conf)
    case _ => new CatalogLogin(catalogContext)
  }

  def putSession(token: String, sessionId: String): Unit = {
    tokenToSessionId.put(token, sessionId)
  }

  def removeSession(token: String): Unit = {
    tokenToSessionId.remove(token)
  }
}
