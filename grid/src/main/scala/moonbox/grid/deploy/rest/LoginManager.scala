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

package moonbox.grid.deploy.rest

import java.util.concurrent.{ConcurrentHashMap, TimeUnit}

import moonbox.catalog.JdbcCatalog
import moonbox.common.util.{ThreadUtils, Utils}
import moonbox.common.{MbConf, MbLogging}
import moonbox.grid.config._
import moonbox.grid.deploy.security.{CatalogLogin, LdapLogin, Login}
import moonbox.grid.deploy.{ConnectionInfo, ConnectionType, MoonboxService}

import scala.collection.JavaConversions._

class LoginManager(conf: MbConf, catalog: JdbcCatalog) extends MbLogging {
	private val tokenEncoder = new TokenEncoder(conf)

	// time unit: ms
	private val LOGIN_TIMEOUT_MS = conf.get(LOGIN_TIMEOUT)
	private val loginImpl = createLogin(conf.get(LOGIN_IMPLEMENTATION))


	def loginWithTicket(org: String, username: String, password: String): Option[String] = {
		if (loginImpl.doLogin(org, username, password)) {
			Some(tokenEncoder.encode(org, username, LOGIN_TIMEOUT_MS / 1000))
		} else {
			None
		}
	}

	def login(org: String, username: String, password: String): Boolean = {
		loginImpl.doLogin(org, username, password)
	}

	def isLogin(token: String): Option[(String, String)] = {
		if (tokenEncoder.isvalid(token)) {
			tokenEncoder.decode(token)
		} else None
	}


	private def createLogin(loginType: String): Login = loginType.toUpperCase match {
		case "LDAP" => new LdapLogin(conf)
		case _ => new CatalogLogin(conf, catalog)
	}

}
