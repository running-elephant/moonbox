package moonbox.core.command

import moonbox.common.util.Utils
import moonbox.core.catalog._
import moonbox.core.{MbColumnIdentifier, MbSession}
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
			mbSession.catalog.alterUser(
				catalogUser.copy(
					grantAccount = grants.contains(GrantAccount),
					grantDdl = grants.contains(GrantDdl),
					grantDmlOn = grants.contains(GrantDmlOn),
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
			mbSession.catalog.getUserGroupRel(catalogGroup.id.get).users.foreach { userId =>
				val existUser = mbSession.catalog.getUser(userId)
				mbSession.catalog.alterUser(
					existUser.copy(
						grantAccount = grants.contains(GrantAccount),
						grantDdl = grants.contains(GrantDdl),
						grantDmlOn = grants.contains(GrantDmlOn),
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
			mbSession.catalog.alterUser(
				catalogUser.copy(
					grantAccount = !grants.contains(GrantAccount),
					grantDdl = !grants.contains(GrantDdl),
					grantDmlOn = !grants.contains(GrantDmlOn),
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
			mbSession.catalog.getUserGroupRel(catalogGroup.id.get).users.foreach { userId =>
				val existUser = mbSession.catalog.getUser(userId)
				mbSession.catalog.alterUser(
					existUser.copy(
						grantAccount = !grants.contains(GrantAccount),
						grantDdl = !grants.contains(GrantDdl),
						grantDmlOn = !grants.contains(GrantDmlOn),
						updateBy = ctx.userId,
						updateTime = Utils.now
					)
				)
			}
		}
		Seq.empty[Row]
	}
}

case class GrantAccountToUser(
	users: Seq[String]) extends MbRunnableCommand with DCL {

	override def run(mbSession: MbSession)(implicit ctx: CatalogSession): Seq[Row] = {
		val catalogUsers = mbSession.catalog.getUsers(ctx.organizationId, users)
		require(users.size == catalogUsers.size,
			s"User does not exist: '${users.diff(catalogUsers.map(_.name)).mkString(", ")}'")
		catalogUsers.foreach { catalogUser =>
			mbSession.catalog.alterUser(
				catalogUser.copy(
					account = true,
					updateBy = ctx.userId,
					updateTime = Utils.now
				)
			)
		}
		Seq.empty[Row]
	}
}

case class GrantAccountToGroup(
	groups: Seq[String]) extends MbRunnableCommand with DCL {
	override def run(mbSession: MbSession)(implicit ctx: CatalogSession): Seq[Row] = {
		val catalogGroups: Seq[CatalogGroup] = mbSession.catalog.getGroups(ctx.organizationId, groups)
		require(groups.size == catalogGroups.size,
			s"Group does not exist: '${groups.diff(catalogGroups.map(_.name)).mkString(", ")}'")
		catalogGroups.foreach { catalogGroup =>
			mbSession.catalog.getUserGroupRel(catalogGroup.id.get).users.foreach { userId =>
				val existUser = mbSession.catalog.getUser(userId)
				mbSession.catalog.alterUser(
					existUser.copy(
						account = true,
						updateBy = ctx.userId,
						updateTime = Utils.now
					)
				)
			}
		}
		Seq.empty[Row]
	}
}

case class RevokeAccountFromUser(
	users: Seq[String]) extends MbRunnableCommand with DCL {
	override def run(mbSession: MbSession)(implicit ctx: CatalogSession): Seq[Row] = {
		val catalogUsers = mbSession.catalog.getUsers(ctx.organizationId, users)
		require(users.size == catalogUsers.size,
			s"User does not exist: '${users.diff(catalogUsers.map(_.name)).mkString(", ")}'")
		catalogUsers.foreach { catalogUser =>
			mbSession.catalog.alterUser(
				catalogUser.copy(
					account = false,
					updateBy = ctx.userId,
					updateTime = Utils.now
				)
			)
		}
		Seq.empty[Row]
	}
}

case class RevokeAccountFromGroup(
	groups: Seq[String]) extends MbRunnableCommand with DCL {
	override def run(mbSession: MbSession)(implicit ctx: CatalogSession): Seq[Row] = {
		val catalogGroups: Seq[CatalogGroup] = mbSession.catalog.getGroups(ctx.organizationId, groups)
		require(groups.size == catalogGroups.size,
			s"Group does not exist: '${groups.diff(catalogGroups.map(_.name)).mkString(", ")}'")
		catalogGroups.foreach { catalogGroup =>
			mbSession.catalog.getUserGroupRel(catalogGroup.id.get).users.foreach { userId =>
				val existUser = mbSession.catalog.getUser(userId)
				mbSession.catalog.alterUser(
					existUser.copy(
						account = false,
						updateBy = ctx.userId,
						updateTime = Utils.now
					)
				)
			}
		}
		Seq.empty[Row]
	}
}

case class GrantDdlToUser(
	users: Seq[String]) extends MbRunnableCommand with DCL {
	override def run(mbSession: MbSession)(implicit ctx: CatalogSession): Seq[Row] = {
		val catalogUsers = mbSession.catalog.getUsers(ctx.organizationId, users)
		require(users.size == catalogUsers.size,
			s"User does not exist: '${users.diff(catalogUsers.map(_.name)).mkString(", ")}'")
		catalogUsers.foreach { catalogUser =>
			mbSession.catalog.alterUser(
				catalogUser.copy(
					ddl = true,
					updateBy = ctx.userId,
					updateTime = Utils.now
				)
			)
		}
		Seq.empty[Row]
	}
}

case class GrantDdlToGroup(
	groups: Seq[String]) extends MbRunnableCommand with DCL {
	override def run(mbSession: MbSession)(implicit ctx: CatalogSession): Seq[Row] = {
		val catalogGroups: Seq[CatalogGroup] = mbSession.catalog.getGroups(ctx.organizationId, groups)
		require(groups.size == catalogGroups.size,
			s"Group does not exist: '${groups.diff(catalogGroups.map(_.name)).mkString(", ")}'")
		catalogGroups.foreach { catalogGroup =>
			mbSession.catalog.getUserGroupRel(catalogGroup.id.get).users.foreach { userId =>
				val existUser = mbSession.catalog.getUser(userId)
				mbSession.catalog.alterUser(
					existUser.copy(
						ddl = true,
						updateBy = ctx.userId,
						updateTime = Utils.now
					)
				)
			}
		}
		Seq.empty[Row]
	}
}

case class RevokeDdlFromUser(
	users: Seq[String]) extends MbRunnableCommand with DCL {
	override def run(mbSession: MbSession)(implicit ctx: CatalogSession): Seq[Row] = {
		val catalogUsers = mbSession.catalog.getUsers(ctx.organizationId, users)
		require(users.size == catalogUsers.size,
			s"User does not exist: '${users.diff(catalogUsers.map(_.name)).mkString(", ")}'")
		catalogUsers.foreach { catalogUser =>
			mbSession.catalog.alterUser(
				catalogUser.copy(
					ddl = false,
					updateBy = ctx.userId,
					updateTime = Utils.now
				)
			)
		}
		Seq.empty[Row]
	}
}

case class RevokeDdlFromGroup(
	groups: Seq[String]) extends MbRunnableCommand with DCL {
	override def run(mbSession: MbSession)(implicit ctx: CatalogSession): Seq[Row] = {
		val catalogGroups: Seq[CatalogGroup] = mbSession.catalog.getGroups(ctx.organizationId, groups)
		require(groups.size == catalogGroups.size,
			s"Group does not exist: '${groups.diff(catalogGroups.map(_.name)).mkString(", ")}'")
		catalogGroups.foreach { catalogGroup =>
			mbSession.catalog.getUserGroupRel(catalogGroup.id.get).users.foreach { userId =>
				val existUser = mbSession.catalog.getUser(userId)
				mbSession.catalog.alterUser(
					existUser.copy(
						ddl = false,
						updateBy = ctx.userId,
						updateTime = Utils.now
					)
				)
			}
		}
		Seq.empty[Row]
	}
}

case class GrantDmlOnToUser(
	columns: Seq[MbColumnIdentifier],
	users: Seq[String]) extends MbRunnableCommand with DCL {
	override def run(mbSession: MbSession)(implicit ctx: CatalogSession): Seq[Row] = {
		val tableToColumns = columns.groupBy(col => (col.database, col.table))
		tableToColumns.keys.foreach { key =>
				val database = key._1.map(mbSession.catalog.getDatabase(ctx.organizationId, _))
				val table = mbSession.catalog.getTable(database.map(_.id.get).getOrElse(ctx.databaseId), key._2)
				val catalogColumns = tableToColumns(key).map { col =>
					CatalogColumn(
						name = col.column,
						dataType = "string",
						readOnly = false,
						tableId = table.id.get,
						createBy = ctx.userId,
						updateBy = ctx.userId
					)
				}
			val columnIds = mbSession.catalog.createColumns(catalogColumns, ignoreIfExists = true)
			mbSession.catalog.getUsers(ctx.organizationId, users).foreach { catalogUser =>
				mbSession.catalog.createUserTableRel(
					CatalogUserTableRel(
						userId = catalogUser.id.get,
						tableId = table.id.get,
						columns = columnIds,
						createBy = ctx.userId,
						updateBy = ctx.userId
					), catalogUser.name, ctx.organizationName, database.map(_.name).getOrElse(ctx.databaseName),
					table.name, catalogColumns.map(_.name), ignoreIfExists = true
				)
			}
		}
		Seq.empty[Row]
	}
}

case class GrantDmlOnToGroup(
	columns: Seq[MbColumnIdentifier],
	groups: Seq[String]) extends MbRunnableCommand with DCL {
	override def run(mbSession: MbSession)(implicit ctx: CatalogSession): Seq[Row] = {
		val userGroupRel = mbSession.catalog.getGroups(ctx.organizationId, groups).map { catalogGroup =>
			mbSession.catalog.getUserGroupRel(catalogGroup.id.get)
		}
		val tableToColumns = columns.groupBy(col => (col.database, col.table))
		tableToColumns.keys.foreach { key =>
			val database = key._1.map(mbSession.catalog.getDatabase(ctx.organizationId, _))
			val table = mbSession.catalog.getTable(database.map(_.id.get).getOrElse(ctx.databaseId), key._2)
			val catalogColumns = tableToColumns(key).map { col =>
				CatalogColumn(
					name = col.column,
					dataType = "string",
					readOnly = false,
					tableId = table.id.get,
					createBy = ctx.userId,
					updateBy = ctx.userId
				)
			}
			val columnIds = mbSession.catalog.createColumns(catalogColumns, ignoreIfExists = true)
			userGroupRel.foreach { rel =>
				mbSession.catalog.getUsers(rel.users).foreach { catalogUser =>
					mbSession.catalog.createUserTableRel(
						CatalogUserTableRel(
							userId = catalogUser.id.get,
							tableId = table.id.get,
							columns = columnIds,
							createBy = ctx.userId,
							updateBy = ctx.userId
						), catalogUser.name, ctx.organizationName, database.map(_.name).getOrElse(ctx.databaseName),
						table.name, catalogColumns.map(_.name), ignoreIfExists = true
					)
				}
			}
		}
		Seq.empty[Row]
	}
}

case class RevokeDmlOnFromUser(
	columns: Seq[MbColumnIdentifier],
	users: Seq[String]) extends MbRunnableCommand with DCL {
	override def run(mbSession: MbSession)(implicit ctx: CatalogSession): Seq[Row] = {
		Seq.empty[Row]
	}
}

case class RevokeDmlOnFromGroup(
	columns: Seq[MbColumnIdentifier],
	groups: Seq[String]) extends MbRunnableCommand with DCL {
	override def run(mbSession: MbSession)(implicit ctx: CatalogSession): Seq[Row] = {
		Seq.empty[Row]
	}
}
