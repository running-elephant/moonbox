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

package moonbox.core

import moonbox.common.MbLogging
import moonbox.core.command._

object CommandChecker extends MbLogging {

	private def require(condition: Boolean, message: String): Unit = {
		if (!condition) throw new Exception(message)
	}

	def check(cmd: MbCommand, catalog: MoonboxCatalog): Unit = {
		cmd match {
			case org: Organization =>
				require(isRoot, "Only ROOT can do this command.")
			case other =>
				require(!isRoot,
					"ROOT can only do organization relative commands.")
				notOrganization(other, catalog)
		}

		def isRoot: Boolean = {
			catalog.getCurrentOrg.equalsIgnoreCase("SYSTEM") &&
				catalog.getCurrentUser.equalsIgnoreCase("ROOT")
		}
	}

	private def notOrganization(cmd: MbCommand, catalog: MoonboxCatalog): Unit = cmd match {
		case account: Account =>
			require(catalog.catalogUser.account, "Permission denied.")

		case ddl: DDL =>
			require(catalog.catalogUser.ddl, "Permission denied.")

		case dml: DML =>

		case GrantResourceToUser(_, _, _)
			 | RevokeResourceFromUser(_, _, _) =>

			require(catalog.catalogUser.dcl, "Permission denied.")

		case GrantGrantToUser(_, _)
			 | RevokeGrantFromUser(_, _) =>

			require(catalog.catalogUser.isSA, "Permission denied.")

		case GrantPrivilegeToUser(privileges, _) =>

			require(
				privileges.map {
					case RolePrivilege.ACCOUNT => catalog.catalogUser.grantAccount
					case RolePrivilege.DDL => catalog.catalogUser.grantDdl
					case RolePrivilege.DCL => catalog.catalogUser.grantDcl
				}.forall(_ == true),
				"Permission denied."
			)

		case RevokePrivilegeFromUser(privileges, _) =>

			require(
				privileges.map {
					case RolePrivilege.ACCOUNT => catalog.catalogUser.grantAccount
					case RolePrivilege.DDL => catalog.catalogUser.grantDdl
					case RolePrivilege.DCL => catalog.catalogUser.grantDcl
				}.forall(_ == true),
				"Permission denied."
			)

		case _ =>
	}

}
