package moonbox.grid.deploy.security

import moonbox.catalog.JdbcCatalog
import moonbox.common.{MbConf, MbLogging}
import moonbox.grid.config._

class LoginManager(val conf: MbConf, catalog: JdbcCatalog) extends MbLogging {

	private val loginImpl = createLogin(conf.get(LOGIN_IMPLEMENTATION))

	def login(username: String, password: String): Session = {
		loginImpl.doLogin(username, password)
	}

	private def createLogin(loginType: String): Login = loginType.toUpperCase match {
		case "LDAP" => new LdapLogin(conf)
		case _ => new CatalogLogin(conf, catalog)
	}

}
