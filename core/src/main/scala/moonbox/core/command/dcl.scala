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

package moonbox.core.command

import moonbox.catalog._
import moonbox.core.MoonboxSession
import moonbox.core.command.RolePrivilege.RolePrivilege
import org.apache.spark.sql.Row
import org.apache.spark.sql.catalyst.TableIdentifier
import org.apache.spark.sql.catalyst.expressions.AttributeReference
import org.apache.spark.sql.types.StringType

sealed trait DCL

case class CreateGroup(
	name: String,
	desc: Option[String],
	ignoreIfExists: Boolean) extends MbRunnableCommand with DCL {
	override def run(mbSession: MoonboxSession): Seq[Row] = {
		mbSession.catalog.createGroup(
			CatalogGroup(name, desc), ignoreIfExists
		)
		Seq.empty[Row]
	}
}

case class AlterGroupSetName(name: String, newName: String) extends MbRunnableCommand with DCL {
	override def run(mbSession: MoonboxSession): Seq[Row] = {
		mbSession.catalog.renameGroup(name, newName)
		Seq.empty[Row]
	}
}

case class AlterGroupSetComment(name: String, comment: String) extends MbRunnableCommand with DCL {
	override def run(mbSession: MoonboxSession): Seq[Row] = {
		mbSession.catalog.alterGroup(CatalogGroup(name, Some(comment)))
		Seq.empty[Row]
	}
}
case class AlterGroupAddUser(name: String, users: Seq[String]) extends MbRunnableCommand with DCL {
	override def run(mbSession: MoonboxSession): Seq[Row] = {
		mbSession.catalog.createGroupUserRel(CatalogGroupUserRel(name, users))
		Seq.empty[Row]
	}
}

case class AlterGroupRemoveUser(name: String, users: Seq[String]) extends MbRunnableCommand with DCL {
	override def run(mbSession: MoonboxSession): Seq[Row] = {
		mbSession.catalog.deleteGroupUserRel(CatalogGroupUserRel(name, users))
		Seq.empty[Row]
	}
}

case class DropGroup(name: String, ignoreIfNotExists: Boolean, cascade: Boolean) extends MbRunnableCommand with DCL {
	override def run(mbSession: MoonboxSession): Seq[Row] = {
		mbSession.catalog.dropGroup(name, ignoreIfNotExists, cascade)
		Seq.empty[Row]
	}
}

case class GrantGrantToUser(
	grants: Seq[RolePrivilege],
	user: String) extends MbRunnableCommand with DCL {

	override def run(mbSession: MoonboxSession): Seq[Row] = {

		val catalogUser = mbSession.catalog.getUser(mbSession.catalog.getCurrentOrg, user)
		val grantAccount =
			if (grants.contains(RolePrivilege.ACCOUNT)) true
			else catalogUser.grantAccount
		val grantDdl = if (grants.contains(RolePrivilege.DDL)) true
			else catalogUser.grantDdl
		val grantDcl = if (grants.contains(RolePrivilege.DCL)) true
			else catalogUser.grantDcl
		mbSession.catalog.alterUser(
			catalogUser.copy(
				grantAccount = grantAccount,
				grantDdl = grantDdl,
				grantDcl = grantDcl
			)
		)
		
		Seq.empty[Row]
	}
}

case class GrantGrantToGroup(
	grants: Seq[RolePrivilege],
	group: String) extends MbRunnableCommand with DCL {
	override def run(mbSession: MoonboxSession): Seq[Row] = {
		mbSession.catalog.listUserInGroup(group)
				.map(user => GrantGrantToUser(grants, user))
				.foreach(_.run(mbSession))
		Seq.empty[Row]
	}
}

case class RevokeGrantFromUser(
	grants: Seq[RolePrivilege],
	user: String) extends MbRunnableCommand with DCL {

	override def run(mbSession: MoonboxSession): Seq[Row] = {
		val catalogUser = mbSession.catalog.getUser(mbSession.catalog.getCurrentOrg, user)
		val grantAccount =
			if (grants.contains(RolePrivilege.ACCOUNT)) false
			else catalogUser.grantAccount
		val grantDdl =
			if (grants.contains(RolePrivilege.DDL)) false
			else catalogUser.grantDdl
		val grantDcl =
			if (grants.contains(RolePrivilege.DCL)) false
			else catalogUser.grantDcl
		mbSession.catalog.alterUser(
			catalogUser.copy(
				grantAccount = grantAccount,
				grantDdl = grantDdl,
				grantDcl = grantDcl
			)
		)
		Seq.empty[Row]
	}
}

case class RevokeGrantFromGroup(
	grants: Seq[RolePrivilege],
	group: String) extends MbRunnableCommand with DCL {
	override def run(mbSession: MoonboxSession): Seq[Row] = {
		mbSession.catalog.listUserInGroup(group)
		    .map(user => RevokeGrantFromUser(grants, user))
		    .foreach(_.run(mbSession))
		Seq.empty[Row]
	}
}

case class GrantPrivilegeToUser(privileges: Seq[RolePrivilege], user: String)
	extends MbRunnableCommand with DCL {
	override def run(mbSession: MoonboxSession): Seq[Row] = {
		val catalogUser = mbSession.catalog.getUser(mbSession.catalog.getCurrentOrg, user)
		val account =
			if (privileges.contains(RolePrivilege.ACCOUNT)) true
			else catalogUser.account
		val ddl =
			if (privileges.contains(RolePrivilege.DDL)) true
			else catalogUser.ddl
		val dcl =
			if (privileges.contains(RolePrivilege.DCL)) true
			else catalogUser.dcl
		mbSession.catalog.alterUser(
			catalogUser.copy(
				account = account,
				ddl = ddl,
				dcl = dcl
			)
		)
		Seq.empty[Row]
	}
}

case class GrantPrivilegeToGroup(privileges: Seq[RolePrivilege], group: String)
	extends MbRunnableCommand with DCL {
	override def run(mbSession: MoonboxSession): Seq[Row] = {
		mbSession.catalog.listUserInGroup(group)
		    .map(user => GrantPrivilegeToUser(privileges, user))
		    .foreach(_.run(mbSession))
		Seq.empty[Row]
	}
}

case class RevokePrivilegeFromUser(privileges: Seq[RolePrivilege], user: String)
	extends MbRunnableCommand with DCL {
	override def run(mbSession: MoonboxSession): Seq[Row] = {
		val catalogUser = mbSession.catalog.getUser(mbSession.catalog.getCurrentOrg, user)
		val account =
			if (privileges.contains(RolePrivilege.ACCOUNT)) false
			else catalogUser.account
		val ddl =
			if (privileges.contains(RolePrivilege.DDL)) false
			else catalogUser.ddl
		val dcl =
			if (privileges.contains(RolePrivilege.DCL)) false
			else catalogUser.dcl
		mbSession.catalog.alterUser(
			catalogUser.copy(
				account = account,
				ddl = ddl,
				dcl = dcl
			)
		)
		Seq.empty[Row]
	}
}

case class RevokePrivilegeFromGroup(privileges: Seq[RolePrivilege], group: String)
	extends MbRunnableCommand with DCL {
	override def run(mbSession: MoonboxSession): Seq[Row] = {
		mbSession.catalog.listUserInGroup(group)
		    .map(user => RevokePrivilegeFromUser(privileges, user))
		    .foreach(_.run(mbSession))
		Seq.empty[Row]
	}
}

case class GrantResourceToUser(
	privileges: Seq[ResourcePrivilege],
	tableIdentifier: TableIdentifier,
	user: String) extends MbRunnableCommand with DCL {

	override def run(mbSession: MoonboxSession): Seq[Row] = {

		import mbSession.catalog._

		val db = tableIdentifier.database.getOrElse(getCurrentDb)

		requireDbExists(db)
		requireUserExists(mbSession.catalog.getCurrentOrg, user)

		val table = tableIdentifier.table

		if (table == "*") { // db level
			if (privileges.exists(_.isColumnLevel)) {
				throw new Exception("Illegal grant command.")
			}
			createDatabasePrivilege(
				CatalogDatabasePrivilege(
					user = user,
					database = db,
					privileges = privileges.map(_.NAME)
				)
			)
		} else {
			requireTableExists(db, table)

			val (tableLevel, columnLevel) = privileges.span(!_.isColumnLevel)
			if (tableLevel.nonEmpty) { // table level
				createTablePrivilege(
					CatalogTablePrivilege(
						user = user,
						database = db,
						table = tableIdentifier.table,
						privileges = tableLevel.map(_.NAME)
					)
				)
			}

			if (columnLevel.nonEmpty) { // column level
				val schema = mbSession.tableSchema(table, db).map(_.name)
				val columns = privileges.flatMap {
					case ColumnSelectPrivilege(column) =>
						column.map((_, SelectPrivilege.NAME))
					case ColumnUpdatePrivilege(column) =>
						column.map((_, SelectPrivilege.NAME))
				}

				// check column exists or not
				val diff = columns.map(_._1).distinct.diff(schema)
				if (diff.nonEmpty) {
					throw new Exception(s"${diff.mkString(", ")} does not exist")
				}

				val columnPrivilege = columns.groupBy(_._1).map { case (k, v) =>
					(k, v.map(_._2).distinct)
				}

				createColumnPrivilege(
					CatalogColumnPrivilege(
						user = user,
						database = db,
						table = table,
						privilege = columnPrivilege
					)
				)
			}

		}

		Seq.empty[Row]
	}
}

case class GrantResourceToGroup(
	privileges: Seq[ResourcePrivilege],
	tableIdentifier: TableIdentifier,
	group: String) extends MbRunnableCommand with DCL {
	override def run(mbSession: MoonboxSession): Seq[Row] = {
		mbSession.catalog.listUserInGroup(group)
				.map(user => GrantResourceToUser(privileges, tableIdentifier, user))
				.foreach(_.run(mbSession))
		Seq.empty[Row]
	}
}

case class RevokeResourceFromUser(
	privileges: Seq[ResourcePrivilege],
	tableIdentifier: TableIdentifier,
	user: String) extends MbRunnableCommand with DCL {
	override def run(mbSession: MoonboxSession): Seq[Row] = {

		import mbSession.catalog._

		requireUserExists(getCurrentOrg, user)

		val db = tableIdentifier.database.getOrElse(getCurrentDb)

		requireDbExists(db)

		val table = tableIdentifier.table

		if (table == "*") {
			if (privileges.exists(_.isColumnLevel)) {
				throw new Exception("Illegal grant command.")
			}
			dropDatabasePrivilege(user, db, privileges.map(_.NAME))
		} else {

			val (tableLevel, columnLevel) = privileges.span(!_.isColumnLevel)

			if (tableLevel.nonEmpty) {
				dropTablePrivilege(user, db, table, tableLevel.map(_.NAME))
			}

			if (columnLevel.nonEmpty) {
				val columns = privileges.flatMap {
					case ColumnSelectPrivilege(column) =>
						column.map((_, SelectPrivilege.NAME))
					case ColumnUpdatePrivilege(column) =>
						column.map((_, SelectPrivilege.NAME))
				}

				val columnPrivilege = columns.groupBy(_._1).map { case (k, v) =>
					(k, v.map(_._2).distinct)
				}
				dropColumnPrivilege(user, db, table, columnPrivilege.toSeq)
			}
		}
		Seq.empty[Row]
	}
}

case class RevokeResourceFromGroup(
	privileges: Seq[ResourcePrivilege],
	tableIdentifier: TableIdentifier,
	group: String) extends MbRunnableCommand with DCL {
	override def run(mbSession: MoonboxSession): Seq[Row] = {
		mbSession.catalog.listUserInGroup(group)
				.map(user => RevokeResourceFromUser(privileges, tableIdentifier, user))
				.foreach(_.run(mbSession))
		Seq.empty[Row]
	}
}

case class ShowGroups(pattern: Option[String]) extends MbRunnableCommand with DCL {

	override def output = {
		AttributeReference("GROUP_NAME", StringType, nullable = false)() ::
				AttributeReference("DESCRIPTION", StringType, nullable = false)() :: Nil
	}

	override def run(mbSession: MoonboxSession): Seq[Row] = {
		mbSession.catalog.listGroups(pattern)
		    .map(group => Row(group.name, group.desc.getOrElse(" - ")))
	}
}

case class ShowUsersInGroup(group: String, pattern: Option[String]) extends MbRunnableCommand with DCL {

	override def output = {
		AttributeReference("USER", StringType, nullable = false)() :: Nil
	}

	override def run(mbSession: MoonboxSession): Seq[Row] = {
		mbSession.catalog.listUserInGroup(group, pattern).map(user => Row(user))
	}
}
