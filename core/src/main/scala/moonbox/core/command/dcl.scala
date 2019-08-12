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

sealed trait DCL

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


case class GrantResourceToUser(
	privileges: Seq[ResourcePrivilege],
	tableIdentifier: TableIdentifier,
	user: String) extends MbRunnableCommand with DCL {

	override def run(mbSession: MoonboxSession): Seq[Row] = {

		import mbSession.catalog._

		val db = tableIdentifier.database.getOrElse(getCurrentDb)

		require(databaseExists(db))
		require(userExists(mbSession.catalog.getCurrentOrg, user))

		val table = tableIdentifier.table

		if (table == "*") { // db level
			require(!privileges.exists(_.isColumnLevel), "Illegal grant command.")
			createDatabasePrivilege(
				CatalogDatabasePrivilege(
					user = user,
					database = db,
					privileges = privileges.map(_.NAME)
				)
			)
		} else {

			require(tableExists(db, table), s"Table or view $table does not exists")

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
				require(diff.isEmpty, s"${diff.mkString(", ")} does not exist")

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

case class RevokeResourceFromUser(
	privileges: Seq[ResourcePrivilege],
	tableIdentifier: TableIdentifier,
	user: String) extends MbRunnableCommand with DCL {
	override def run(mbSession: MoonboxSession): Seq[Row] = {

		import mbSession.catalog._

		require(userExists(getCurrentOrg, user))

		val db = tableIdentifier.database.getOrElse(getCurrentDb)

		require(databaseExists(db))

		val table = tableIdentifier.table

		if (table == "*") {
			require(!privileges.exists(_.isColumnLevel), "Illegal grant command.")
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
