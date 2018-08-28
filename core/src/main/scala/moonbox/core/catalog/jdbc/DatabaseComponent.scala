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
	protected lazy val url: String = conf.get(CATALOG_URL)
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

