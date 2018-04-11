package moonbox.core

import moonbox.common.MbLogging
import moonbox.core.catalog.CatalogSession
import moonbox.core.command._

class PrivilegeChecker(catalog: CatalogContext, session: CatalogSession) extends MbLogging {
	def intercept(cmd: MbCommand): Boolean = {
		cmd match {
			case dml: DML => catalog.canDml(session.userId)
			case ddl: DDL => catalog.canDdl(session.userId)
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
			case GrantAccountToUser(_)
				 | RevokeAccountFromUser(_)
				 | GrantAccountToGroup(_)
				 | RevokeAccountFromGroup(_) =>  catalog.canGrantAccount(session.userId)
			case GrantDdlToUser(_)
				 | RevokeDdlFromUser(_)
				 | GrantDdlToGroup(_)
				 | RevokeDdlFromGroup(_) =>  catalog.canGrantDdl(session.userId)
			case GrantDmlOnToUser(_, _)
				 | GrantDmlOnToGroup(_, _)
				 | RevokeDmlOnFromUser(_, _)
				 | RevokeDmlOnFromGroup(_, _) => catalog.canGrantDmlOn(session.userId)
		}
	}
}
