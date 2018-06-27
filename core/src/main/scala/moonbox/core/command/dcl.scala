package moonbox.core.command

import moonbox.common.util.Utils
import moonbox.core.catalog._
import moonbox.core.command.PrivilegeType.PrivilegeType
import moonbox.core.{MbSession, MbTableIdentifier}
import org.apache.spark.sql.Row

sealed trait DCL

case class GrantGrantToUser(
	grants: Seq[PrivilegeType],
	users: Seq[String]) extends MbRunnableCommand with DCL {

	override def run(mbSession: MbSession)(implicit ctx: CatalogSession): Seq[Row] = {
		val catalogUsers = mbSession.catalog.getUsers(ctx.organizationId, users)
		require(users.size == catalogUsers.size,
			s"User does not exist: '${users.diff(catalogUsers.map(_.name)).mkString(", ")}'")
		catalogUsers.foreach { catalogUser =>
			val grantAccount =
				if (grants.contains(PrivilegeType.ACCOUNT)) true
				else catalogUser.grantAccount
			val grantDdl = if (grants.contains(PrivilegeType.DDL)) true
				else catalogUser.grantDdl
			val grantDcl = if (grants.contains(PrivilegeType.DCL)) true
				else catalogUser.grantDcl
			mbSession.catalog.alterUser(
				catalogUser.copy(
					grantAccount = grantAccount,
					grantDdl = grantDdl,
					grantDcl = grantDcl,
					updateBy = ctx.userId,
					updateTime = Utils.now
				)
			)
		}
		Seq.empty[Row]
	}
}

case class GrantGrantToGroup(
	grants: Seq[PrivilegeType],
	groups: Seq[String]) extends MbRunnableCommand with DCL {

	override def run(mbSession: MbSession)(implicit ctx: CatalogSession): Seq[Row] = {
		val catalogGroups: Seq[CatalogGroup] = mbSession.catalog.getGroups(ctx.organizationId, groups)
		require(groups.size == catalogGroups.size,
			s"Group does not exist: '${groups.diff(catalogGroups.map(_.name)).mkString(", ")}'")
		catalogGroups.foreach { catalogGroup =>
			mbSession.catalog.getUserGroupRelsByGroup(catalogGroup.id.get).map(_.userId).foreach { userId =>
				val existUser = mbSession.catalog.getUser(userId)
				val grantAccount =
					if (grants.contains(PrivilegeType.ACCOUNT)) true
					else existUser.grantAccount
				val grantDdl =
					if (grants.contains(PrivilegeType.DDL)) true
					else existUser.grantDdl
				val grantDcl =
					if (grants.contains(PrivilegeType.DCL)) true
					else existUser.grantDcl
				mbSession.catalog.alterUser(
					existUser.copy(
						grantAccount = grantAccount,
						grantDdl = grantDdl,
						grantDcl = grantDcl,
						updateBy = ctx.userId,
						updateTime = Utils.now
					)
				)
			}
		}
		Seq.empty[Row]
	}
}

case class RevokeGrantFromUser(
	grants: Seq[PrivilegeType],
	users: Seq[String]) extends MbRunnableCommand with DCL {

	override def run(mbSession: MbSession)(implicit ctx: CatalogSession): Seq[Row] = {
		val catalogUsers = mbSession.catalog.getUsers(ctx.organizationId, users)
		require(users.size == catalogUsers.size,
			s"User does not exist: '${users.diff(catalogUsers.map(_.name)).mkString(", ")}'")
		catalogUsers.foreach { catalogUser =>
			val grantAccount =
				if (grants.contains(PrivilegeType.ACCOUNT)) false
				else catalogUser.grantAccount
			val grantDdl = if (grants.contains(PrivilegeType.DDL)) false
			else catalogUser.grantDdl
			val grantDcl = if (grants.contains(PrivilegeType.DCL)) false
			else catalogUser.grantDcl
			mbSession.catalog.alterUser(
				catalogUser.copy(
					grantAccount = grantAccount,
					grantDdl = grantDdl,
					grantDcl = grantDcl,
					updateBy = ctx.userId,
					updateTime = Utils.now
				)
			)
		}
		Seq.empty[Row]
	}
}

case class RevokeGrantFromGroup(
	grants: Seq[PrivilegeType],
	groups: Seq[String]) extends MbRunnableCommand with DCL {

	override def run(mbSession: MbSession)(implicit ctx: CatalogSession): Seq[Row] = {
		val catalogGroups: Seq[CatalogGroup] = mbSession.catalog.getGroups(ctx.organizationId, groups)
		require(groups.size == catalogGroups.size,
			s"Group does not exist: '${groups.diff(catalogGroups.map(_.name)).mkString(", ")}'")
		catalogGroups.foreach { catalogGroup =>
			mbSession.catalog.getUserGroupRelsByGroup(catalogGroup.id.get).map(_.userId).foreach { userId =>
				val existUser = mbSession.catalog.getUser(userId)
				val grantAccount =
					if (grants.contains(PrivilegeType.ACCOUNT)) false
					else existUser.grantAccount
				val grantDdl =
					if (grants.contains(PrivilegeType.DDL)) false
					else existUser.grantDdl
				val grantDcl =
					if (grants.contains(PrivilegeType.DCL)) false
					else existUser.grantDcl
				mbSession.catalog.alterUser(
					existUser.copy(
						grantAccount = grantAccount,
						grantDdl = grantDdl,
						grantDcl = grantDcl,
						updateBy = ctx.userId,
						updateTime = Utils.now
					)
				)
			}
		}
		Seq.empty[Row]
	}
}


case class GrantPrivilegeToUser(privileges: Seq[PrivilegeType], users: Seq[String])
	extends MbRunnableCommand with DCL {
	override def run(mbSession: MbSession)(implicit ctx: CatalogSession): Seq[Row] = {
		val catalogUsers = mbSession.catalog.getUsers(ctx.organizationId, users)
		require(users.size == catalogUsers.size,
			s"User does not exist: '${users.diff(catalogUsers.map(_.name)).mkString(", ")}'")
		catalogUsers.foreach { catalogUser =>
			val account =
				if (privileges.contains(PrivilegeType.ACCOUNT)) true
				else catalogUser.account
			val ddl =
				if (privileges.contains(PrivilegeType.DDL)) true
				else catalogUser.ddl
			val dcl =
				if (privileges.contains(PrivilegeType.DCL)) true
				else catalogUser.dcl
			mbSession.catalog.alterUser(
				catalogUser.copy(
					account = account,
					ddl = ddl,
					dcl = dcl,
					updateBy = ctx.userId,
					updateTime = Utils.now
				)
			)
		}
		Seq.empty[Row]
	}
}

case class GrantPrivilegeToGroup(privileges: Seq[PrivilegeType], groups: Seq[String])
	extends MbRunnableCommand with DCL {
	override def run(mbSession: MbSession)(implicit ctx: CatalogSession): Seq[Row] = {
		val catalogGroups: Seq[CatalogGroup] = mbSession.catalog.getGroups(ctx.organizationId, groups)
		require(groups.size == catalogGroups.size,
			s"Group does not exist: '${groups.diff(catalogGroups.map(_.name)).mkString(", ")}'")
		catalogGroups.foreach { catalogGroup =>
			mbSession.catalog.getUserGroupRelsByGroup(catalogGroup.id.get).map(_.userId).foreach { userId =>
				val existUser = mbSession.catalog.getUser(userId)
				val account =
					if (privileges.contains(PrivilegeType.ACCOUNT)) true
					else existUser.account
				val ddl =
					if (privileges.contains(PrivilegeType.DDL)) true
					else existUser.ddl
				val dcl =
					if (privileges.contains(PrivilegeType.DCL)) true
					else existUser.dcl
				mbSession.catalog.alterUser(
					existUser.copy(
						account = account,
						ddl = ddl,
						dcl = dcl,
						updateBy = ctx.userId,
						updateTime = Utils.now
					)
				)
			}
		}
		Seq.empty[Row]
	}
}


case class RevokePrivilegeFromUser(privileges: Seq[PrivilegeType], users: Seq[String])
	extends MbRunnableCommand with DCL {
	override def run(mbSession: MbSession)(implicit ctx: CatalogSession): Seq[Row] = {
		val catalogUsers = mbSession.catalog.getUsers(ctx.organizationId, users)
		require(users.size == catalogUsers.size,
			s"User does not exist: '${users.diff(catalogUsers.map(_.name)).mkString(", ")}'")
		catalogUsers.foreach { catalogUser =>
			val account =
				if (privileges.contains(PrivilegeType.ACCOUNT)) false
				else catalogUser.account
			val ddl =
				if (privileges.contains(PrivilegeType.DDL)) false
				else catalogUser.ddl
			val dcl =
				if (privileges.contains(PrivilegeType.DCL)) false
				else catalogUser.dcl
			mbSession.catalog.alterUser(
				catalogUser.copy(
					account = account,
					ddl = ddl,
					dcl = dcl,
					updateBy = ctx.userId,
					updateTime = Utils.now
				)
			)
		}
		Seq.empty[Row]
	}
}

case class RevokePrivilegeFromGroup(privileges: Seq[PrivilegeType], groups: Seq[String])
	extends MbRunnableCommand with DCL {
	override def run(mbSession: MbSession)(implicit ctx: CatalogSession): Seq[Row] = {
		val catalogGroups: Seq[CatalogGroup] = mbSession.catalog.getGroups(ctx.organizationId, groups)
		require(groups.size == catalogGroups.size,
			s"Group does not exist: '${groups.diff(catalogGroups.map(_.name)).mkString(", ")}'")
		catalogGroups.foreach { catalogGroup =>
			mbSession.catalog.getUserGroupRelsByGroup(catalogGroup.id.get).map(_.userId).foreach { userId =>
				val existUser = mbSession.catalog.getUser(userId)
				val account =
					if (privileges.contains(PrivilegeType.ACCOUNT)) false
					else existUser.account
				val ddl =
					if (privileges.contains(PrivilegeType.DDL)) false
					else existUser.ddl
				val dcl =
					if (privileges.contains(PrivilegeType.DCL)) false
					else existUser.dcl
				mbSession.catalog.alterUser(
					existUser.copy(
						account = account,
						ddl = ddl,
						dcl = dcl,
						updateBy = ctx.userId,
						updateTime = Utils.now
					)
				)
			}
		}
		Seq.empty[Row]
	}
}

case class GrantResourceToUser(
	privileges: Seq[ResourcePrivilege],
	tableIdentifier: MbTableIdentifier,
	users: Seq[String]) extends MbRunnableCommand with DCL {

	override def run(mbSession: MbSession)(implicit ctx: CatalogSession): Seq[Row] = {
		// TODO table exists and mapping
		val catalogUsers = mbSession.catalog.getUsers(ctx.organizationId, users)
		val catalogDatabase = mbSession.catalog.getDatabase(ctx.organizationId, tableIdentifier.database.getOrElse(ctx.databaseName))

		// val catalogTable = mbSession.catalog.getTable(catalogDatabase.id.get, tableIdentifier.table)

		if (tableIdentifier.table == "*") { // database privilege
			val privilegeType = privileges.flatMap {
				case SelectPrivilege(columns) if columns.nonEmpty =>
					throw new Exception("Illegal grant command.")
				case UpdatePrivilege(columns) if columns.nonEmpty =>
					throw new Exception("Illegal grant command.")
				case SelectPrivilege(_) =>
					Seq(SelectPrivilege.NAME)
				case UpdatePrivilege(_) =>
					Seq(UpdatePrivilege.NAME)
				case InsertPrivilege =>
					Seq(InsertPrivilege.NAME)
				case DeletePrivilege =>
					Seq(DeletePrivilege.NAME)
				case TruncatePrivilege =>
					Seq(TruncatePrivilege.NAME)
				case AllPrivilege =>
					AllPrivilege.NAMES
			}
			catalogUsers.foreach { catalogUser =>
				val catalogDatabasePrivileges = privilegeType.map { priv =>
					CatalogDatabasePrivilege(
						userId = catalogUser.id.get,
						databaseId = catalogDatabase.id.get,
						privilegeType = priv,
						createBy = ctx.userId,
						updateBy = ctx.userId
					)
				}
				mbSession.catalog.createDatabasePrivilege(
					catalogDatabasePrivileges,
					catalogUser.name,
					ctx.organizationName,
					catalogDatabase.name)
			}
		} else {
			val catalogColumns = mbSession.catalog.getColumns(catalogDatabase.id.get, tableIdentifier.table)(mbSession)
			val (tablePrivileges, columnPrivileges) = privileges.span {
				case SelectPrivilege(columns) if columns.nonEmpty => false
				case UpdatePrivilege(columns) if columns.nonEmpty => false
				case _ => true
			}
			val privilegeType = tablePrivileges.flatMap {
				case SelectPrivilege(_) =>
					Seq(SelectPrivilege.NAME)
				case UpdatePrivilege(_) =>
					Seq(UpdatePrivilege.NAME)
				case InsertPrivilege =>
					Seq(InsertPrivilege.NAME)
				case DeletePrivilege =>
					Seq(DeletePrivilege.NAME)
				case TruncatePrivilege =>
					Seq(TruncatePrivilege.NAME)
				case AllPrivilege =>
					AllPrivilege.NAMES
			}
			catalogUsers.foreach { catalogUser =>
				val catalogTablePrivileges = privilegeType.map { priv =>
					CatalogTablePrivilege(
						userId = catalogUser.id.get,
						databaseId = catalogDatabase.id.get,
						table = tableIdentifier.table,
						privilegeType = priv,
						createBy = ctx.userId,
						updateBy = ctx.userId
					)
				}
				mbSession.catalog.createTablePrivilege(
					catalogTablePrivileges,
					catalogUser.name,
					ctx.organizationName,
					catalogDatabase.name,
					tableIdentifier.table)
			}
			catalogUsers.foreach { catalogUser =>
				val catalogColumnPrivileges = columnPrivileges.flatMap {
					case SelectPrivilege(columns) =>
						checkColumns(catalogColumns, catalogDatabase.id.get, tableIdentifier.table, columns)
						columns.map { column =>
							CatalogColumnPrivilege(
								userId = catalogUser.id.get,
								databaseId = catalogDatabase.id.get,
								table = tableIdentifier.table,
								column = column,
								privilegeType = SelectPrivilege.NAME,
								createBy = ctx.userId,
								updateBy = ctx.userId
							)
						}
					case UpdatePrivilege(columns) =>
						checkColumns(catalogColumns, catalogDatabase.id.get, tableIdentifier.table, columns)
						columns.map { column =>
							CatalogColumnPrivilege(
								userId = catalogUser.id.get,
								databaseId = catalogDatabase.id.get,
								table = tableIdentifier.table,
								column = column,
								privilegeType = UpdatePrivilege.NAME,
								createBy = ctx.userId,
								updateBy = ctx.userId
							)
						}
				}
				mbSession.catalog.createColumnPrivilege(
					catalogColumnPrivileges,
					catalogUser.name,
					ctx.organizationName,
					catalogDatabase.name,
					tableIdentifier.table)
			}
		}
		Seq.empty[Row]
	}

	private def checkColumns(catalogColumns: Seq[CatalogColumn], databaseId: Long, table: String, columns: Seq[String]): Unit = {
		val existColumns = catalogColumns.map(_.name)
		val diff = columns.diff(existColumns)
		if (diff.nonEmpty) {
			throw new NoSuchColumnException(table, diff.mkString(", "))
		}
	}

}
case class GrantResourceToGroup(
	privileges: Seq[ResourcePrivilege],
	tableIdentifier: MbTableIdentifier,
	groups: Seq[String]) extends MbRunnableCommand with DCL {
	override def run(mbSession: MbSession)(implicit ctx: CatalogSession): Seq[Row] = {
		val userGroupRel = mbSession.catalog.getGroups(ctx.organizationId, groups).flatMap { catalogGroup =>
			mbSession.catalog.getUserGroupRelsByGroup(catalogGroup.id.get)
		}
		val users = userGroupRel.map { rel => mbSession.catalog.getUser(rel.userId).name }
		GrantResourceToUser(privileges, tableIdentifier, users).run(mbSession)
		Seq.empty[Row]
	}
}

case class RevokeResourceFromUser(
	privileges: Seq[ResourcePrivilege],
	tableIdentifier: MbTableIdentifier,
	users: Seq[String]) extends MbRunnableCommand with DCL {
	override def run(mbSession: MbSession)(implicit ctx: CatalogSession): Seq[Row] = {
		val catalogUsers = mbSession.catalog.getUsers(ctx.organizationId, users)
		val catalogDatabase = mbSession.catalog.getDatabase(ctx.organizationId, tableIdentifier.database.getOrElse(ctx.databaseName))
		if (tableIdentifier.table == "*") {
			val privilegeTypes = privileges.flatMap {
				case SelectPrivilege(columns) if columns.nonEmpty =>
					throw new Exception("Illegal revoke command.")
				case UpdatePrivilege(columns) if columns.nonEmpty =>
					throw new Exception("Illegal revoke command.")
				case SelectPrivilege(_) =>
					Seq(SelectPrivilege.NAME)
				case UpdatePrivilege(_) =>
					Seq(UpdatePrivilege.NAME)
				case InsertPrivilege =>
					Seq(InsertPrivilege.NAME)
				case DeletePrivilege =>
					Seq(DeletePrivilege.NAME)
				case TruncatePrivilege =>
					Seq(TruncatePrivilege.NAME)
				case AllPrivilege =>
					AllPrivilege.NAMES
			}
			catalogUsers.foreach { catalogUser =>
				mbSession.catalog.dropDatabasePrivilege(
					catalogUser.id.get,
					catalogDatabase.id.get,
					privilegeTypes,
					catalogUser.name,
					ctx.organizationName,
					catalogDatabase.name)
			}
		} else {
			val (tablePrivileges, columnPrivileges) = privileges.span {
				case SelectPrivilege(columns) if columns.nonEmpty => false
				case UpdatePrivilege(columns) if columns.nonEmpty => false
				case _ => true
			}
			val privilegeTypes = tablePrivileges.flatMap {
				case SelectPrivilege(_) =>
					Seq(SelectPrivilege.NAME)
				case UpdatePrivilege(_) =>
					Seq(UpdatePrivilege.NAME)
				case InsertPrivilege =>
					Seq(InsertPrivilege.NAME)
				case DeletePrivilege =>
					Seq(DeletePrivilege.NAME)
				case TruncatePrivilege =>
					Seq(TruncatePrivilege.NAME)
				case AllPrivilege =>
					AllPrivilege.NAMES
			}
			catalogUsers.foreach { catalogUser =>
				mbSession.catalog.dropTablePrivilege(
					catalogUser.id.get,
					catalogDatabase.id.get,
					tableIdentifier.table,
					privilegeTypes,
					catalogUser.name,
					ctx.organizationName,
					catalogDatabase.name)
			}
			catalogUsers.foreach { catalogUser =>
				val catalogColumnPrivileges = columnPrivileges.map {
					case SelectPrivilege(columns) =>
						(SelectPrivilege.NAME, columns)
					case UpdatePrivilege(columns) =>
						(UpdatePrivilege.NAME, columns)
				}
				mbSession.catalog.dropColumnPrivilege(
					catalogUser.id.get,
					catalogDatabase.id.get,
					tableIdentifier.table,
					catalogColumnPrivileges,
					catalogUser.name,
					ctx.organizationName,
					catalogDatabase.name
				)
			}
		}
		Seq.empty[Row]
	}
}
case class RevokeResourceFromGroup(
	privileges: Seq[ResourcePrivilege],
	tableIdentifier: MbTableIdentifier,
	groups: Seq[String]) extends MbRunnableCommand with DCL {
	override def run(mbSession: MbSession)(implicit ctx: CatalogSession): Seq[Row] = {
		val userGroupRel = mbSession.catalog.getGroups(ctx.organizationId, groups).flatMap { catalogGroup =>
			mbSession.catalog.getUserGroupRelsByGroup(catalogGroup.id.get)
		}
		val users = userGroupRel.map { rel => mbSession.catalog.getUser(rel.userId).name }
		RevokeResourceFromUser(privileges, tableIdentifier, users).run(mbSession)
		Seq.empty[Row]
	}
}
