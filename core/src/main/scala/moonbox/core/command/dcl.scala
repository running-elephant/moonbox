package moonbox.core.command

import moonbox.common.util.Utils
import moonbox.core.catalog._
import moonbox.core.{MbColumnIdentifier, MbSession}
import org.apache.spark.sql.Row
import org.apache.spark.sql.catalyst.TableIdentifier
import org.apache.spark.sql.catalyst.analysis.UnresolvedRelation

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
			mbSession.catalog.getUserGroupRelsByGroup(catalogGroup.id.get).map(_.userId).foreach { userId =>
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
			mbSession.catalog.getUserGroupRelsByGroup(catalogGroup.id.get).map(_.userId).foreach { userId =>
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
			mbSession.catalog.getUserGroupRelsByGroup(catalogGroup.id.get).map(_.userId).foreach { userId =>
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
			mbSession.catalog.getUserGroupRelsByGroup(catalogGroup.id.get).map(_.userId).foreach { userId =>
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
			mbSession.catalog.getUserGroupRelsByGroup(catalogGroup.id.get).map(_.userId).foreach { userId =>
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
			mbSession.catalog.getUserGroupRelsByGroup(catalogGroup.id.get).map(_.userId).foreach { userId =>
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

	private def checkColumns(columns: Seq[String], mbSession: MbSession, table: String, database: String, properties: Map[String, String]): Seq[String] = {
		if (columns.contains("*")) {
			Seq("*")
		} else {
			val tableIdentifier = TableIdentifier(table, Some(database))
			mbSession.mixcal.registerTable(tableIdentifier, properties)
			val tableColumns = mbSession.mixcal.analyzedLogicalPlan(UnresolvedRelation(tableIdentifier)).schema.map(_.name)
			val diff = columns.diff(tableColumns)
			if (diff.nonEmpty) {
				throw new NoSuchColumnException(table, diff.mkString(", "))
			} else {
				columns
			}
		}
	}

	override def run(mbSession: MbSession)(implicit ctx: CatalogSession): Seq[Row] = {
		val tableToColumns = columns.groupBy(col => (col.database, col.table))
		tableToColumns.keys.foreach { case key @ (database, table) =>
			val wantToGrantColumns = tableToColumns(key).map(_.column)
			val catalogDatabase = mbSession.catalog.getDatabase(ctx.organizationId, database.getOrElse(ctx.databaseName))
			val catalogTable = mbSession.catalog.getTable(catalogDatabase.id.get, table)
			val grantColumns = checkColumns(wantToGrantColumns, mbSession, table, catalogDatabase.name, catalogTable.properties)
			mbSession.catalog.getUsers(ctx.organizationId, users).foreach { catalogUser =>
				val catalogUserTableRels = grantColumns.map { col =>
					CatalogUserTableRel(
						userId = catalogUser.id.get,
						databaseId = catalogDatabase.id.get,
						table = table,
						column = col,
						createBy = ctx.userId,
						updateBy = ctx.userId
					)
				}
				mbSession.catalog.createUserTableRel(
					catalogUserTableRels,
					catalogUser.name,
					ctx.organizationName,
					catalogDatabase.name,
					table)
			}


			/*if (catalogDatabase.isLogical) {
				val catalogTable = mbSession.catalog.getTable(catalogDatabase.id.get, table)
				val grantColumns = checkColumns(wantToGrantColumns, mbSession, table, catalogDatabase.name, catalogTable.properties)
				mbSession.catalog.getUsers(ctx.organizationId, users).foreach { catalogUser =>
					val catalogUserTableRels = grantColumns.map { col =>
						CatalogUserLogicalTableRel(
							userId = catalogUser.id.get,
							tableId = catalogTable.id.get,
							column = col,
							createBy = ctx.userId,
							updateBy = ctx.userId
						)
					}
					mbSession.catalog.createUserLogicalTableRel(
						catalogUserTableRels,
						catalogUser.name,
						ctx.organizationName,
						catalogDatabase.name,
						table)
				}
			} else {
				// TODO database level properties to table level properties
				val grantColumns = checkColumns(wantToGrantColumns, mbSession, table, catalogDatabase.name, catalogDatabase.properties)
				mbSession.catalog.getUsers(ctx.organizationId, users).foreach { catalogUser =>
					val catalogUserTableRels = grantColumns.map { col =>
						CatalogUserPhysicalTableRel(
							userId = catalogUser.id.get,
							databaseId = catalogDatabase.id.get,
							table = table,
							column = col,
							createBy = ctx.userId,
							updateBy = ctx.userId
						)
					}
					mbSession.catalog.createUserPhysicalTableRel(
						catalogUserTableRels,
						catalogUser.name,
						ctx.organizationName,
						catalogDatabase.name,
						table)
				}
			}*/
		}
		Seq.empty[Row]
	}
}

case class GrantDmlOnToGroup(
	columns: Seq[MbColumnIdentifier],
	groups: Seq[String]) extends MbRunnableCommand with DCL {
	override def run(mbSession: MbSession)(implicit ctx: CatalogSession): Seq[Row] = {
		val userGroupRel = mbSession.catalog.getGroups(ctx.organizationId, groups).flatMap { catalogGroup =>
			mbSession.catalog.getUserGroupRelsByGroup(catalogGroup.id.get)
		}
		val users = userGroupRel.map { rel => mbSession.catalog.getUser(rel.userId).name }
		GrantDmlOnToUser(columns, users).run(mbSession)
		Seq.empty[Row]
	}
}

case class RevokeDmlOnFromUser(
	columns: Seq[MbColumnIdentifier],
	users: Seq[String]) extends MbRunnableCommand with DCL {

	private def checkColumns(columns: Seq[String], existColumns: Seq[String], user: String, table: String): Seq[String] = {
		if (columns.contains("*")) {
			existColumns
		} else {
			val diff = columns.diff(existColumns)
			if (diff.nonEmpty) {
				throw new NoSuchGrantColumnException(user, table, diff.mkString(", "))
			} else {
				columns
			}
		}
	}

	override def run(mbSession: MbSession)(implicit ctx: CatalogSession): Seq[Row] = {
		val tableToColumns = columns.groupBy(col => (col.database, col.table))
		tableToColumns.keys.foreach { case key @ (database, table) =>
			val wantToRevokeColumns = tableToColumns(key).map(_.column)
			val catalogDatabase = mbSession.catalog.getDatabase(ctx.organizationId, database.getOrElse(ctx.databaseName))
			val catalogUsers = mbSession.catalog.getUsers(ctx.organizationId, users)
			catalogUsers.foreach { catalogUser =>
				val existColumns = mbSession.catalog.getUserTableRels(catalogUser.id.get, catalogDatabase.id.get, table).map(_.column)
				val revokeColumns = checkColumns(wantToRevokeColumns, existColumns, catalogUser.name, table)
				mbSession.catalog.dropUserTableRel(
					catalogUser.id.get,
					catalogDatabase.id.get,
					table,
					revokeColumns,
					catalogUser.name,
					ctx.organizationName,
					catalogDatabase.name
				)
			}

			/*if (catalogDatabase.isLogical) {
				val catalogTable = mbSession.catalog.getTable(catalogDatabase.id.get, table)
				catalogUsers.foreach { catalogUser =>
					val existColumns = mbSession.catalog.getUserLogicalTableRels(catalogUser.id.get, catalogTable.id.get).map(_.column)
					val revokeColumns = checkColumns(wantToRevokeColumns, existColumns, catalogUser.name, table)
					mbSession.catalog.dropUserLogicalRels(
						catalogUser.id.get,
						catalogTable.id.get,
						revokeColumns,
						catalogUser.name,
						ctx.organizationName,
						catalogDatabase.name, table)
				}
			} else {
				catalogUsers.foreach { catalogUser =>
					val existColumns = mbSession.catalog.getUserPhysicalTableRels(catalogUser.id.get, catalogDatabase.id.get, table).map(_.column)
					val revokeColumns = checkColumns(wantToRevokeColumns, existColumns, catalogUser.name, table)
					mbSession.catalog.dropUserPhysicalTableRel(
						catalogUser.id.get,
						catalogDatabase.id.get,
						table,
						revokeColumns,
						catalogUser.name,
						ctx.organizationName,
						catalogDatabase.name
					)
				}
			}*/
		}
		Seq.empty[Row]
	}
}

case class RevokeDmlOnFromGroup(
	columns: Seq[MbColumnIdentifier],
	groups: Seq[String]) extends MbRunnableCommand with DCL {

	override def run(mbSession: MbSession)(implicit ctx: CatalogSession): Seq[Row] = {
		val userGroupRel = mbSession.catalog.getGroups(ctx.organizationId, groups).flatMap { catalogGroup =>
			mbSession.catalog.getUserGroupRelsByGroup(catalogGroup.id.get)
		}
		val users = userGroupRel.map { rel => mbSession.catalog.getUser(rel.userId).name }
		RevokeDmlOnFromUser(columns, users).run(mbSession)
		Seq.empty[Row]
	}
}
