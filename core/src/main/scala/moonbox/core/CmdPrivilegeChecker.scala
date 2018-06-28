package moonbox.core

import moonbox.common.MbLogging
import moonbox.core.catalog.CatalogSession
import moonbox.core.command.PrivilegeType.{PrivilegeType, DDL => _, _}
import moonbox.core.command._

object CmdPrivilegeChecker extends MbLogging {
	def intercept(cmd: MbCommand, catalog: CatalogContext, session: CatalogSession): Boolean = {
		cmd match {
			case dml: DML => catalog.canDml(session.userId)
			case ddl: DDL => catalog.canDdl(session.userId)
			case GrantResourceToUser(_, _, _)
				 | GrantResourceToGroup(_, _, _)
				 | RevokeResourceFromUser(_, _, _)
				 | RevokeResourceFromGroup(_, _, _) => catalog.canDcl(session.userId)
			case CreateOrganization(_, _, _)
				 | AlterOrganizationSetName(_, _)
				 | AlterOrganizationSetComment(_, _)
				 | DropOrganization(_, _, _) => session.userName == "ROOT"
			case CreateSa(_, _, _, _)
				 | AlterSaSetName(_, _, _)
				 | AlterSaSetPassword(_, _, _)
				 | DropSa(_, _, _) => session.userName == "ROOT"
			case account: Account => catalog.canAccount(session.userId)
			case GrantGrantToUser(_, _)
				 | GrantGrantToGroup(_, _)
				 | RevokeGrantFromUser(_, _)
				 | RevokeGrantFromGroup(_, _)  => catalog.isSa(session.userId)
			case GrantPrivilegeToUser(privileges, _) => checkPrivileges(privileges, catalog, session.userId)
			case GrantPrivilegeToGroup(privileges, _) => checkPrivileges(privileges, catalog, session.userId)
			case RevokePrivilegeFromUser(privileges, _) => checkPrivileges(privileges, catalog, session.userId)
			case RevokePrivilegeFromGroup(privileges, _) => checkPrivileges(privileges, catalog, session.userId)
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
