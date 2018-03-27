package moonbox.grid.deploy.authenticate

import moonbox.common.MbConf
import moonbox.core.catalog.CatalogSession
import moonbox.grid.config._

class LdapLogin(conf: MbConf) extends Login {

	/*private val ladpUser = conf.get(LDAP)

		conf.getOrElse("auth.ldap.user", throw new Exception)
	private val ladpPassword = conf.getOrElse("auth.ldap.password", throw new Exception)
	private val ldapUrl = conf.getOrElse("auth.ldap.url", throw new Exception)
	private val ladpDc = conf.getOrElse("auth.ldap.dc", throw new Exception)
	private val ladpReadTimeout = conf.getOrElse("auth.ldap.read.timeout", throw new Exception)
	private val ladpConnectionPool = conf.getOrElse("auth.ldap.connect.pool", throw new Exception)*/

	def doLogin(username: String, password: String): Boolean = true
}
