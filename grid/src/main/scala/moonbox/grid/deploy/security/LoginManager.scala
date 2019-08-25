/*-
 * <<
 * Moonbox
 * ==
 * Copyright (C) 2016 - 2019 EDP
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

import java.util.concurrent.{ConcurrentHashMap, TimeUnit}

import moonbox.catalog.JdbcCatalog
import moonbox.common.util.{ThreadUtils, Utils}
import moonbox.common.{MbConf, MbLogging}
import moonbox.grid.config._
import moonbox.grid.deploy.{ConnectionInfo, ConnectionType, MoonboxService}

import scala.collection.JavaConversions._

class LoginManager(conf: MbConf, mbService: MoonboxService) extends MbLogging {
  private val catalog = new JdbcCatalog(conf)
  private val tokenEncoder = new TokenEncoder(conf)

  private val loginType = conf.get(LOGIN_IMPLEMENTATION)
  private val LOGIN_TIMEOUT_MS = conf.get(LOGIN_TIMEOUT) // time unit: ms

  private val loginImpl = createLogin(loginType)
  private val tokenToLastActiveTime = new ConcurrentHashMap[String, Long]()
  private val tokenToSessionId = new ConcurrentHashMap[String, String]()

  private val timeoutCatalogSessionCallback = new ConcurrentHashMap[String, () => Unit]()

  private val cleanTimeoutCatalogSessionThread =
    ThreadUtils.newDaemonSingleThreadScheduledExecutor("loginManager-clean-timeout")

  cleanTimeoutCatalogSessionThread.scheduleAtFixedRate(new Runnable {
    override def run(): Unit = {
      tokenToLastActiveTime.foreach { case (u, t) =>
        if ((Utils.now - t) >= LOGIN_TIMEOUT_MS) {
          logInfo(s"Token timeout, closing according session: Token = $u")
          Option(tokenToSessionId.get(u)).map(sessionId =>
            mbService.closeSession(u, sessionId)(ConnectionInfo(
              "system", // TODO
              "system",
              ConnectionType.SYSTEM
            ))
          )
          tokenToLastActiveTime.remove(u)
          Option(timeoutCatalogSessionCallback.remove(u)).foreach { callback =>
            callback()
          }
        }
      }
    }
  }, 0, LOGIN_TIMEOUT_MS, TimeUnit.MILLISECONDS)

  def addTimeoutCallback(token: String, callback: () => Unit): Unit = {
    timeoutCatalogSessionCallback.put(token, callback)
  }

  def login(org: String, username: String, password: String, forget: Boolean = false): Option[String] = {
    if (loginImpl.doLogin(org, username, password)) {
      val token = tokenEncoder.encode(org, username)
      if (!forget) {
        tokenToLastActiveTime.put(token, System.currentTimeMillis())
      }
      Some(token)
    } else None
  }

  def logout(token: String): Unit = {
    tokenToLastActiveTime.remove(token)
  }

  def isLogin(token: String): Option[(String, String)] = {
    if (tokenToLastActiveTime.containsKey(token)) {
      tokenToLastActiveTime.update(token, System.currentTimeMillis())
      tokenEncoder.decode(token)
    } else None
  }

  def decode(token: String): Option[(String, String)] = {
    tokenEncoder.decode(token)
  }

  def isvalid(token: String): Boolean = {
    if (tokenToLastActiveTime.containsKey(token)) {
      tokenToLastActiveTime.update(token, System.currentTimeMillis())
      true
    } else false
  }

  private def createLogin(loginType: String): Login = loginType.toUpperCase match {
    case "LDAP" => new LdapLogin(conf)
    case _ => new CatalogLogin(conf, catalog)
  }

  def putSession(token: String, sessionId: String): Unit = {
    tokenToSessionId.put(token, sessionId)
  }

  def removeSession(token: String): Unit = {
    tokenToSessionId.remove(token)
  }

  def getUserConfig(org: String, user: String): Map[String, String] = {
    catalog.getOrganization(org).config ++
      catalog.getUser(org, user).configuration
  }
}
