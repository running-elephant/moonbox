package moonbox.core.catalog.jdbc

import java.util.Locale

import moonbox.common.MbConf
import moonbox.common.exception.UnsupportedException
import moonbox.core.config._
import slick.jdbc.JdbcBackend.Database
import slick.jdbc._
trait DatabaseComponent {
	protected val conf: MbConf
	private lazy val implementation = conf.get(CATALOG_IMPLEMENTATION)
	private lazy val url = conf.get(CATALOG_URL)
	private lazy val user = conf.get(CATALOG_USER)
	private lazy val password = conf.get(CATALOG_PASSWORD)
	private lazy val driver = conf.get(CATALOG_DRIVER)
	protected lazy val profile = {
		implementation.toLowerCase(Locale.ROOT) match {
			case "mysql" => MySQLProfile
			case "h2" => H2Profile
			case "postgres" => PostgresProfile
			case "oracle" => OracleProfile
			case "db2" => DB2Profile
			case "derby" => DerbyProfile
			case "sqlserver" => SQLServerProfile
			case _ => throw new UnsupportedException(s"unsupported catalog backend type $implementation")
		}
	}

	def database: Database = {
		if (DatabaseComponent.database == null) {
			synchronized {
				if (DatabaseComponent.database == null) {
					Class.forName(driver)
					DatabaseComponent.database = Database.forURL(url = url, user = user, password = password, driver = driver)
				}
			}
		}
		DatabaseComponent.database
	}

}

object DatabaseComponent {
	protected  var database: Database = _
}

