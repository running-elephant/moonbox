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

package moonbox.grid.deploy2.authenticate

import moonbox.core.CatalogContext
import moonbox.core.catalog.PasswordEncryptor

class CatalogLogin(catalogContext: CatalogContext) extends Login {

	/*override def doLogin(username: String, password: String): Option[CatalogSession] = {
		catalogContext.getUserOption(username) match {
			case Some(user) if user.password == password =>
				val catalogSession = if (user.name.equalsIgnoreCase("ROOT")) {
					CatalogSession(
						userId = user.id.get,
						userName = user.name,
						databaseId = 0,
						databaseName = "SYSTEM",
						organizationId = user.organizationId,
						organizationName = "SYSTEM"
					)
				} else {
					val currentDatabase = catalogContext.getDatabase(user.organizationId,
						formatDatabaseName(CatalogContext.DEFAULT_DATABASE))
					val currentOrganization = catalogContext.getOrganization(user.organizationId)
					CatalogSession(
						userId = user.id.get,
						userName = user.name,
						databaseId = currentDatabase.id.get,
						databaseName = currentDatabase.name,
						organizationId = currentOrganization.id.get,
						organizationName = currentOrganization.name
					)
				}
				Some(catalogSession)
			case _ => None
		}
	}*/

	override def doLogin(username: String, password: String): Boolean = {
		catalogContext.getUserOption(username) match {
			case Some(user) if user.password == PasswordEncryptor.encryptSHA(password) => true
			case _ => false
		}
	}
}
