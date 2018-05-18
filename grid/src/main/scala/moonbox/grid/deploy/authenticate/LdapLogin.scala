package moonbox.grid.deploy.authenticate

import moonbox.common.MbConf
import moonbox.core.catalog.CatalogSession
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

	def doLogin(username: String, password: String): Boolean = true
}
