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
import moonbox.core.command.PrivilegeType.{PrivilegeType, DDL => _, _}
import moonbox.core.command._

object CmdPrivilegeChecker extends MbLogging {
	def intercept(cmd: MbCommand, catalog: CatalogContext, userContext: SessionEnv): Boolean = {
		cmd match {
			case dml: DML => catalog.canDml(userContext.userId)
			case ddl: DDL => catalog.canDdl(userContext.userId)
			case GrantResourceToUser(_, _, _)
				 | GrantResourceToGroup(_, _, _)
				 | RevokeResourceFromUser(_, _, _)
				 | RevokeResourceFromGroup(_, _, _) => catalog.canDcl(userContext.userId)
			case CreateOrganization(_, _, _)
				 | AlterOrganizationSetName(_, _)
				 | AlterOrganizationSetComment(_, _)
				 | DropOrganization(_, _, _) => userContext.userName == "ROOT"
			case CreateSa(_, _, _, _, _)
				 | AlterSaSetName(_, _, _)
				 | AlterSaSetPassword(_, _, _)
				 | DropSa(_, _, _) => userContext.userName == "ROOT"
			case account: Account => catalog.canAccount(userContext.userId)
			case GrantGrantToUser(_, _)
				 | GrantGrantToGroup(_, _)
				 | RevokeGrantFromUser(_, _)
				 | RevokeGrantFromGroup(_, _)  => catalog.isSa(userContext.userId)
			case GrantPrivilegeToUser(privileges, _) => checkPrivileges(privileges, catalog, userContext.userId)
			case GrantPrivilegeToGroup(privileges, _) => checkPrivileges(privileges, catalog, userContext.userId)
			case RevokePrivilegeFromUser(privileges, _) => checkPrivileges(privileges, catalog, userContext.userId)
			case RevokePrivilegeFromGroup(privileges, _) => checkPrivileges(privileges, catalog, userContext.userId)
		}
	}
	private def checkPrivileges(privileges: Seq[PrivilegeType], catalog: CatalogContext, userId: Long): Boolean = {
		privileges.map {
			case PrivilegeType.ACCOUNT => catalog.canGrantAccount(userId)
			case PrivilegeType.DDL => catalog.canGrantDdl(userId)
			case PrivilegeType.DCL => catalog.canGrantDcl(userId)
		}.forall(_ == true)
	}
}
