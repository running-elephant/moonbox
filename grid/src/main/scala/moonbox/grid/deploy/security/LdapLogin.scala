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

import moonbox.common.MbConf
import moonbox.grid.config._

class LdapLogin(conf: MbConf) extends Login {

	private val ladpUser = conf.get(
		LOGIN_LDAP_USER.key,
		(throw new NoSuchElementException(LOGIN_LDAP_USER.key)): String
	)
	private val ladpPassword = conf.get(
		LOGIN_LDAP_PASSWORD.key,
		(throw new NoSuchElementException(LOGIN_LDAP_PASSWORD.key)): String
	)
	private val ldapUrl = conf.get(
		LOGIN_LDAP_SERVER.key,
		(throw new NoSuchElementException(LOGIN_LDAP_SERVER.key)): String
	)
	private val ladpDc = conf.get(
		LOGIN_LDAP_DC.key,
		(throw new NoSuchElementException(LOGIN_LDAP_DC.key)): String
	)
	private val ladpReadTimeout = conf.get(
		LOGIN_LDAP_CONNECT_TIMEOUT.key,
		(throw new NoSuchElementException(LOGIN_LDAP_CONNECT_TIMEOUT.key)): Long
	)
	private val ladpConnectionPool = conf.get(
		LOGIN_LDAP_CONNECT_POOL.key,
		(throw new NoSuchElementException(LOGIN_LDAP_CONNECT_POOL.key)): Boolean
	)

	def doLogin(org: String, username: String, password: String): Boolean = true
}
