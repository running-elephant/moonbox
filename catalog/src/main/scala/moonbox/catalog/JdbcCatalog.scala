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

package moonbox.catalog

import moonbox.catalog.jdbc.JdbcDao
import moonbox.catalog.config._
import moonbox.common.{MbConf, MbLogging}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

class JdbcCatalog(conf: MbConf) extends AbstractCatalog with MbLogging {
	private val jdbcDao = new JdbcDao(conf)

	private def await[T](f: Future[T]): T = {
		Await.result(f, new FiniteDuration(conf.get(JDBC_CATALOG_AWAIT_TIMEOUT), MILLISECONDS))
	}

	def close(): Unit = {
		jdbcDao.close()
	}
	// ----------------------------------------------------------------------------
	// Organization
	// ----------------------------------------------------------------------------

	override protected def doCreateOrganization(orgDefinition: CatalogOrganization, ignoreIfExists: Boolean): Unit = await {
		jdbcDao.action(jdbcDao.organizationExists(orgDefinition.name)).flatMap {
			case true =>
				ignoreIfExists match {
					case true => Future(Unit)
					case false => throw new OrganizationExistsException(orgDefinition.name)
				}
			case false =>
				jdbcDao.actionTransactionally(
					jdbcDao.createOrganization(orgDefinition).flatMap { id =>
						jdbcDao.createDatabase(CatalogDatabase(
							name = "default",
							organizationId = id,
							properties = Map(),
							isLogical = true,
							createBy = orgDefinition.createBy,
							updateBy = orgDefinition.updateBy
						))
					}
				)
		}
	}

	override protected def doDropOrganization(org: String, ignoreIfNotExists: Boolean, cascade: Boolean): Unit = await {
		jdbcDao.action(jdbcDao.getOrganization(org)).flatMap {
			case Some(catalogOrganization) =>
				if (cascade) {
					jdbcDao.actionTransactionally(
						for (
							_ <- jdbcDao.deleteGroups(catalogOrganization.id.get);
							_ <- jdbcDao.deleteDatabases(catalogOrganization.id.get);
							_ <- jdbcDao.deleteUsers(catalogOrganization.id.get);
							_ <- jdbcDao.deleteProcedures(catalogOrganization.id.get);
							_ <- jdbcDao.deleteOrganization(org)
						) yield ()
					)
				} else {
					jdbcDao.action(
						for (
							groups <- jdbcDao.listGroups(catalogOrganization.id.get);
							databases <- jdbcDao.listDatabases(catalogOrganization.id.get);
							users <- jdbcDao.listUsers(catalogOrganization.id.get);
							procedures <- jdbcDao.listProcedures(catalogOrganization.id.get)
						) yield (groups, databases, users, procedures)
					).map { case (groups, databases, users, procedures) =>
						if (groups.isEmpty && databases.isEmpty && users.isEmpty && procedures.isEmpty) {
							jdbcDao.action(jdbcDao.deleteOrganization(org))
						} else {
							throw new NonEmptyException(s"organization $org")
						}
					}
				}
			case None =>
				ignoreIfNotExists match {
					case true => Future(Unit)
					case false => throw new NoSuchOrganizationException(org)
				}
		}
	}

	override protected def doRenameOrganization(org: String, newOrg: String, updateBy: Long): Unit = await {
		jdbcDao.action(jdbcDao.organizationExists(org)).flatMap {
			case true =>
				jdbcDao.action(jdbcDao.organizationExists(newOrg)).flatMap {
					case false =>
						jdbcDao.action(jdbcDao.renameOrganization(org, newOrg)(updateBy))
					case true => throw new OrganizationExistsException(newOrg)
				}
			case false =>
				throw new NoSuchOrganizationException(org)
		}
	}

	override def alterOrganization(orgDefinition: CatalogOrganization): Unit = await {
		jdbcDao.action(jdbcDao.updateOrganization(orgDefinition))
	}

	override def getOrganization(org: String): CatalogOrganization = await {
		jdbcDao.action(jdbcDao.getOrganization(org)).map {
			case Some(catalogOrganization) => catalogOrganization
			case None => throw new NoSuchOrganizationException(org)
		}
	}

	override def getOrganization(org: Long): CatalogOrganization = await {
		jdbcDao.action(jdbcDao.getOrganization(org)).map {
			case Some(catalogOrganization) => catalogOrganization
			case None => throw new NoSuchOrganizationException(s"Id $org")
		}
	}

	override def getOrganizationOption(org: String): Option[CatalogOrganization] = await {
		jdbcDao.action(jdbcDao.getOrganization(org))
	}

	override def getOrganizationOption(org: Long): Option[CatalogOrganization] = await {
		jdbcDao.action(jdbcDao.getOrganization(org))
	}

	override def organizationExists(org: String): Boolean = await {
		jdbcDao.action(jdbcDao.organizationExists(org))
	}

	override def listOrganizations(): Seq[CatalogOrganization] = await {
		jdbcDao.action(jdbcDao.listOrganizations())
	}

	override def listOrganizations(pattern: String): Seq[CatalogOrganization] = await {
		jdbcDao.action(jdbcDao.listOrganizations(pattern))
	}

	// ----------------------------------------------------------------------------
	// Group -- belong to organization
	// ----------------------------------------------------------------------------

	override protected def doCreateGroup(groupDefinition: CatalogGroup, ignoreIfExists: Boolean): Unit = await {
		jdbcDao.action(jdbcDao.groupExists(groupDefinition.organizationId, groupDefinition.name)).flatMap {
			case true =>
				ignoreIfExists match {
					case true => Future(Unit)
					case false => throw new GroupExistsException(groupDefinition.name)
				}
			case false =>
				jdbcDao.action(jdbcDao.createGroup(groupDefinition))
		}
	}

	override protected def doDropGroup(organizationId: Long, group: String, ignoreIfNotExists: Boolean, cascade: Boolean): Unit = await {
		jdbcDao.action(jdbcDao.getGroup(organizationId, group)).flatMap {
			case Some(catalogGroup) =>
				if (cascade) {
					jdbcDao.actionTransactionally(
						for (
							_ <- jdbcDao.deleteUserGroupRelByGroup(catalogGroup.id.get);
							_ <- jdbcDao.deleteGroup(organizationId, group)
						) yield ()
					)
				} else {
					jdbcDao.action(jdbcDao.userGroupRelExists(catalogGroup.id.get)).flatMap {
						case true =>
							throw new NonEmptyException(s"Group $group")
						case false =>
							jdbcDao.action(jdbcDao.deleteGroup(organizationId, group))
					}
				}
			case None =>
				ignoreIfNotExists match {
					case true => Future(Unit)
					case false => throw new NoSuchGroupException(group)
				}
		}
	}

	override protected def doRenameGroup(organizationId: Long, group: String, newGroup: String, updateBy: Long): Unit = await {
		jdbcDao.action(jdbcDao.groupExists(organizationId, group)).flatMap {
			case true =>
				jdbcDao.action(jdbcDao.groupExists(organizationId, newGroup)).flatMap {
					case false =>
						jdbcDao.action(jdbcDao.renameGroup(organizationId, group, newGroup)(updateBy))
					case true =>
						throw new GroupExistsException(newGroup)
				}
			case false =>
				throw new NoSuchGroupException(group)
		}
	}

	override def alterGroup(groupDefinition: CatalogGroup): Unit = await {
		jdbcDao.action(jdbcDao.updateGroup(groupDefinition))
	}

	override def getGroup(organizationId: Long, group: String): CatalogGroup = await {
		jdbcDao.action(jdbcDao.getGroup(organizationId, group)).map {
			case Some(groupOrganization) => groupOrganization
			case None => throw new NoSuchGroupException(group)
		}
	}

	override def getGroup(groupId: Long): CatalogGroup = await {
		jdbcDao.action(jdbcDao.getGroup(groupId)).map {
			case Some(groupOrganization) => groupOrganization
			case None => throw new NoSuchGroupException(s"Id $groupId")
		}
	}

	override def getGroups(organizationId: Long, groups: Seq[String]): Seq[CatalogGroup] = await {
		jdbcDao.action(jdbcDao.getGroups(organizationId, groups))
	}

	override def getGroupOption(organizationId: Long, group: String): Option[CatalogGroup] = await {
		jdbcDao.action(jdbcDao.getGroup(organizationId, group))
	}

	override def getGroupOption(groupId: Long): Option[CatalogGroup] = await {
		jdbcDao.action(jdbcDao.getGroup(groupId))
	}

	override def groupExists(organizationId: Long, group: String): Boolean = await {
		jdbcDao.action(jdbcDao.groupExists(organizationId, group))
	}

	override def listGroups(organizationId: Long): Seq[CatalogGroup] = await {
		jdbcDao.action(jdbcDao.listGroups(organizationId))
	}

	override def listGroups(organizationId: Long, pattern: String): Seq[CatalogGroup] = await {
		jdbcDao.action(jdbcDao.listGroups(organizationId, pattern))
	}

	// ----------------------------------------------------------------------------
	// User -- belong to organization
	// ----------------------------------------------------------------------------

	override protected def doCreateUser(userDefinition: CatalogUser, ignoreIfExists: Boolean): Unit = await {
		jdbcDao.action(jdbcDao.userExists(userDefinition.name)).flatMap {
			case true =>
				ignoreIfExists match {
					case true => Future(Unit)
					case false => throw new UserExistsException(userDefinition.name)
				}
			case false =>
				jdbcDao.action(jdbcDao.createUser(userDefinition))
		}
	}

	override protected def doDropUser(organizationId: Long, organization: String, user: String, ignoreIfNotExists: Boolean): Unit = await {
		jdbcDao.action(jdbcDao.getUser(organizationId, user)).flatMap {
			case Some(catalogUser) =>
				// delete user group relation
				// delete user table column relation
				// delete table
				jdbcDao.actionTransactionally(
					for (
						_ <- jdbcDao.deleteUserGroupRelByUser(catalogUser.id.get);
						_ <- jdbcDao.deleteDatabasePrivilegeByUser(catalogUser.id.get);
						_ <- jdbcDao.deleteTablePrivilegeByUser(catalogUser.id.get);
						_ <- jdbcDao.deleteColumnPrivilegeByUser(catalogUser.id.get);
						_ <- jdbcDao.deleteUser(catalogUser.id.get)
					) yield ()
				)
			case None =>
				ignoreIfNotExists match {
					case true => Future(Unit)
					case false => throw new NoSuchUserException(s"$user in your organization.")
				}
		}
	}

	override protected def doRenameUser(organizationId: Long, user: String, newUser: String, updateBy: Long): Unit = await {
		jdbcDao.action(jdbcDao.userExists(organizationId, user)).flatMap {
			case true =>
				jdbcDao.action(jdbcDao.userExists(organizationId, newUser)).flatMap {
					case false =>
						jdbcDao.action(jdbcDao.renameUser(user, newUser)(updateBy))
					case true =>
						throw new UserExistsException(newUser)
				}
			case false =>
				throw new NoSuchUserException(user)
		}
	}

	override def alterUser(userDefinition: CatalogUser): Unit = await {
		jdbcDao.action(jdbcDao.updateUser(userDefinition))
	}

	override def getUser(organizationId: Long, user: String): CatalogUser = await {
		jdbcDao.action(jdbcDao.getUser(organizationId, user)).map {
			case Some(u) => u
			case None => throw new NoSuchUserException(s"$user in your organization")
		}
	}

	override def getUsers(organizationId: Long, users: Seq[String]): Seq[CatalogUser] = {
		val catalogUsers = await {
			jdbcDao.action(jdbcDao.getUsers(organizationId, users))
		}
		if (catalogUsers.size != users.size) {
			val notExistsUsers = users.diff(catalogUsers.map(_.name))
			throw new NoSuchUserException(notExistsUsers.mkString(", "))
		}
		catalogUsers
	}

	override def getUsers(userIds: Seq[Long]): Seq[CatalogUser] = await {
		jdbcDao.action(jdbcDao.getUsers(userIds))
	}

	override def getUser(user: Long): CatalogUser = await {
		jdbcDao.action(jdbcDao.getUser(user)).map {
			case Some(u) => u
			case None => throw new NoSuchUserException(s"Id $user")
		}
	}

	override def getUserOption(username: String): Option[CatalogUser] = await {
		jdbcDao.action(jdbcDao.getUser(username))
	}

	override def getUserOption(organizationId: Long, user: String): Option[CatalogUser] = await {
		jdbcDao.action(jdbcDao.getUser(organizationId, user))
	}

	override def getUserOption(user: Long): Option[CatalogUser] = await {
		jdbcDao.action(jdbcDao.getUser(user))
	}

	override def userExists(organizationId: Long, user: String): Boolean = await {
		jdbcDao.action(jdbcDao.userExists(organizationId, user))
	}

	override def listUsers(organizationId: Long): Seq[CatalogUser] = await {
		jdbcDao.action(jdbcDao.listUsers(organizationId))
	}

	override def listUsers(organizationId: Long, pattern: String): Seq[CatalogUser] = await {
		jdbcDao.action(jdbcDao.listUsers(organizationId, pattern))
	}

	// ----------------------------------------------------------------------------
	// Procedure -- belong to organization
	// ----------------------------------------------------------------------------

	override protected def doCreateProcedure(procDefinition: CatalogProcedure, ignoreIfExists: Boolean): Unit = await {
		jdbcDao.action(jdbcDao.procedureExists(procDefinition.organizationId, procDefinition.name)).flatMap {
			case true =>
				ignoreIfExists match {
					case true => Future(Unit)
					case false => throw new ProcedureExistsException(procDefinition.name)
				}
			case false =>
				jdbcDao.action(jdbcDao.createProcedure(procDefinition))
		}
	}

	override protected def doDropProcedure(organizationId: Long, proc: String, ignoreIfNotExists: Boolean): Unit = await {
		jdbcDao.action(jdbcDao.procedureExists(organizationId, proc)).flatMap {
			case true =>
				jdbcDao.action(jdbcDao.deleteProcedure(organizationId, proc))
			case false =>
				ignoreIfNotExists match {
					case true => Future(Unit)
					case false => throw new NoSuchProcedureException(proc)
				}
		}
	}

	override protected def doRenameProcedure(organizationId: Long, proc: String, newProc: String, updateBy: Long): Unit = await {
		jdbcDao.action(jdbcDao.procedureExists(organizationId, proc)).flatMap {
			case true =>
				jdbcDao.action(jdbcDao.procedureExists(organizationId, newProc)).flatMap {
					case false =>
						jdbcDao.action(jdbcDao.renameProcedure(organizationId, proc, newProc)(updateBy))
					case true =>
						throw new ProcedureExistsException(newProc)
				}
			case false =>
				throw new NoSuchProcedureException(proc)
		}
	}

	override def alterProcedure(procDefinition: CatalogProcedure): Unit = await {
		jdbcDao.action(jdbcDao.updateProcedure(procDefinition))
	}

	override def getProcedure(organizationId: Long, proc: String): CatalogProcedure = await {
		jdbcDao.action(jdbcDao.getProcedure(organizationId, proc)).map {
			case Some(a) => a
			case None => throw new NoSuchProcedureException(proc)
		}
	}

	override def getProcedure(proc: Long): CatalogProcedure = await {
		jdbcDao.action(jdbcDao.getProcedure(proc)).map {
			case Some(a) => a
			case None => throw new NoSuchProcedureException(s"Id $proc")
		}
	}

	override def getProcedureOption(organizationId: Long, proc: String): Option[CatalogProcedure] = await {
		jdbcDao.action(jdbcDao.getProcedure(organizationId, proc))
	}

	override def getProcedureOption(proc: Long): Option[CatalogProcedure] = await {
		jdbcDao.action(jdbcDao.getProcedure(proc))
	}

	override def procedureExists(organizationId: Long, proc: String): Boolean = await {
		jdbcDao.action(jdbcDao.procedureExists(organizationId, proc))
	}

	override def listProcedures(organizationId: Long): Seq[CatalogProcedure] = await {
		jdbcDao.action(jdbcDao.listProcedures(organizationId))
	}

	override def listProcedures(organizationId: Long, pattern: String): Seq[CatalogProcedure] = await {
		jdbcDao.action(jdbcDao.listProcedures(organizationId, pattern))
	}

	// ----------------------------------------------------------------------------
	// timedevent -- belong to organization
	// ----------------------------------------------------------------------------

	override protected def doCreateTimedEvent(eventDefinition: CatalogTimedEvent, ignoreIfExists: Boolean): Unit = await {
		jdbcDao.action(jdbcDao.timedEventExists(eventDefinition.organizationId, eventDefinition.name)).flatMap {
			case true =>
				ignoreIfExists match {
					case true => Future(Unit)
					case false => throw new TimedEventExistsException(eventDefinition.name)
				}
			case false =>
				jdbcDao.action(jdbcDao.createTimedEvent(eventDefinition))
		}
	}

	override protected def doRenameTimedEvent(organizationId: Long, event: String, newEvent: String, updateBy: Long): Unit = await {
		jdbcDao.action(jdbcDao.timedEventExists(organizationId, event)).flatMap {
			case true =>
				jdbcDao.action(jdbcDao.timedEventExists(organizationId, newEvent)).flatMap {
					case false =>
						jdbcDao.action(jdbcDao.renameTimedEvent(organizationId, event, newEvent)(updateBy))
					case true =>
						throw new TimedEventExistsException(newEvent)
				}
			case false =>
				throw new NoSuchTimedEventException(event)
		}
	}

	override def alterTimedEvent(eventDefinition: CatalogTimedEvent): Unit = await {
		jdbcDao.action(jdbcDao.updateTimedEvent(eventDefinition))
	}

	override def getTimedEvent(organizationId: Long, event: String): CatalogTimedEvent = await {
		jdbcDao.action(jdbcDao.getTimedEvent(organizationId, event)).map {
			case Some(a) => a
			case None => throw new NoSuchTimedEventException(event)
		}
	}

	override protected def doDropTimedEvent(organizationId: Long, event: String, ignoreIfNotExists: Boolean): Unit = await {
		jdbcDao.action(jdbcDao.timedEventExists(organizationId, event)).flatMap {
			case true =>
				jdbcDao.action(jdbcDao.deleteTimedEvent(organizationId, event))
			case false =>
				ignoreIfNotExists match {
					case true => Future(Unit)
					case false => throw new NoSuchTimedEventException(event)
				}
		}
	}

	override def timedEventExists(organizationId: Long, event: String): Boolean = await {
		jdbcDao.action(jdbcDao.timedEventExists(organizationId, event))
	}

	override def listTimedEvents(organizationId: Long): Seq[CatalogTimedEvent] = await {
		jdbcDao.action(jdbcDao.listTimedEvents(organizationId))
	}

	override def listTimedEvents(organizationId: Long, pattern: String): Seq[CatalogTimedEvent] = await {
		jdbcDao.action(jdbcDao.listTimedEvents(organizationId, pattern))
	}

	// ----------------------------------------------------------------------------
	// Database -- belong to organization
	// ----------------------------------------------------------------------------

	override protected def doCreateDatabase(dbDefinition: CatalogDatabase, ignoreIfExists: Boolean): Unit = await {
		jdbcDao.action(jdbcDao.databaseExists(dbDefinition.organizationId, dbDefinition.name)).flatMap {
			case true =>
				ignoreIfExists match {
					case true => Future(Unit)
					case false => throw new DatabaseExistsException(dbDefinition.name)
				}
			case false =>
				jdbcDao.action(jdbcDao.createDatabase(dbDefinition))
		}
	}

	override protected def doDropDatabase(organizationId: Long, database: String, ignoreIfNotExists: Boolean, cascade: Boolean): Unit = await {
		jdbcDao.action(jdbcDao.getDatabase(organizationId, database)).flatMap {
			case Some(db) =>
				if (cascade) {
					jdbcDao.actionTransactionally(
						for (
							x <- jdbcDao.deleteViews(db.id.get);
							y <- jdbcDao.deleteFunctions(db.id.get);
							z <- jdbcDao.deleteTables(db.id.get);
							_ <- jdbcDao.deleteDatabase(organizationId, database)
						) yield ()
					)
				} else {
					jdbcDao.actionTransactionally(
						for (
							tables <- jdbcDao.listTables(db.id.get);
							views <- jdbcDao.listViews(db.id.get);
							functions <- jdbcDao.listFunctions(db.id.get);
							_ <- if (tables.isEmpty && views.isEmpty && functions.isEmpty) {
								jdbcDao.deleteDatabase(organizationId, database)
							} else throw new NonEmptyException(s"Database $database")
						) yield ()
					)
				}
			case None =>
				ignoreIfNotExists match {
					case true => Future(Unit)
					case false => throw new NoSuchDatabaseException(database)
				}

		}
	}

	override protected def doRenameDatabase(organizationId: Long, db: String, newDb: String, updateBy: Long): Unit = await {
		jdbcDao.action(jdbcDao.databaseExists(organizationId, db)).flatMap {
			case true =>
				jdbcDao.action(jdbcDao.databaseExists(organizationId, newDb)).flatMap {
					case false =>
						jdbcDao.action(jdbcDao.renameDatabase(organizationId, db, newDb)(updateBy))
					case true =>
						throw new DatabaseExistsException(newDb)
				}
			case false =>
				throw new NoSuchDatabaseException(db)
		}
	}

	override def alterDatabase(dbDefinition: CatalogDatabase): Unit = await {
		jdbcDao.action(jdbcDao.updateDatabase(dbDefinition))
	}

	override def getDatabase(organizationId: Long, database: String): CatalogDatabase = await {
		jdbcDao.action(jdbcDao.getDatabase(organizationId, database)).map {
			case Some(db) => db
			case None => throw new NoSuchDatabaseException(database)
		}
	}

	override def getDatabase(id: Long): CatalogDatabase = await {
		jdbcDao.action(jdbcDao.getDatabase(id)).map {
			case Some(db) => db
			case None => throw new NoSuchDatabaseException(s"Id $id")
		}
	}

	override def getDatabaseOption(organizationId: Long, database: String): Option[CatalogDatabase] = await {
		jdbcDao.action(jdbcDao.getDatabase(organizationId, database))
	}

	override def getDatabaseOption(id: Long): Option[CatalogDatabase] = await {
		jdbcDao.action(jdbcDao.getDatabase(id))
	}

	override def databaseExists(organizationId: Long, database: String): Boolean = await {
		jdbcDao.action(jdbcDao.databaseExists(organizationId, database))
	}

	override def listDatabases(organizationId: Long): Seq[CatalogDatabase] = await {
		jdbcDao.action(jdbcDao.listDatabases(organizationId))
	}

	override def listDatabases(organizationId: Long, pattern: String): Seq[CatalogDatabase] = await {
		jdbcDao.action(jdbcDao.listDatabases(organizationId, pattern))
	}

	// ----------------------------------------------------------------------------
	// Table -- belong to database
	// ----------------------------------------------------------------------------
	// TODO check view exists
	override protected def doCreateTable(tableDefinition: CatalogTable, ignoreIfExists: Boolean): Unit = await {
		jdbcDao.action(jdbcDao.getDatabase(tableDefinition.databaseId)).flatMap {
			case Some(database) =>
				jdbcDao.action(jdbcDao.tableExists(tableDefinition.databaseId, tableDefinition.name)).flatMap {
					case true =>
						ignoreIfExists match {
							case true => Future(Unit)
							case false =>
								throw new TableExistsException(
									database.name,
									tableDefinition.name)
						}
					case false =>
						jdbcDao.action(jdbcDao.viewExists(tableDefinition.databaseId, tableDefinition.name)).flatMap {
							case true =>
								throw new TableExistsException(database.name, tableDefinition.name)
							case false =>
								jdbcDao.action(jdbcDao.createTable(tableDefinition))
						}
				}
			case None =>
				throw new NoSuchDatabaseException(s"Id ${tableDefinition.databaseId}")
		}

	}

	override protected def doDropTable(databaseId: Long, table: String, ignoreIfNotExists: Boolean): Unit = await {
		jdbcDao.action(jdbcDao.getTable(databaseId, table)).flatMap {
			case Some(catalogTable) =>
				// delete user table column relation
				// delete table
				jdbcDao.actionTransactionally(
					for (
						_ <- jdbcDao.deleteTablePrivilege(databaseId, table);
						_ <- jdbcDao.deleteColumnPrivilege(databaseId, table);
						_ <- jdbcDao.deleteTable(databaseId, table)
					) yield ()
				)
			case None =>
				ignoreIfNotExists match {
					case true => Future(Unit)
					case false => throw new NoSuchTableException(getDatabase(databaseId).name, table)
				}
		}
	}
	// TODO check view exists
	override protected def doRenameTable(databaseId: Long, table: String, newTable: String, updateBy: Long): Unit = await {
		jdbcDao.action(jdbcDao.tableExists(databaseId, table)).flatMap {
			case true =>
				jdbcDao.action(jdbcDao.tableExists(databaseId, newTable)).flatMap {
					case false =>
						jdbcDao.action(jdbcDao.renameTable(databaseId, table, newTable)(updateBy))
					case true =>
						throw new TableExistsException(getDatabase(databaseId).name, newTable)
				}
			case false =>
				throw new NoSuchTableException(getDatabase(databaseId).name, table)
		}
	}

	override def alterTable(tableDefinition: CatalogTable): Unit = await {
		jdbcDao.action(jdbcDao.updateTable(tableDefinition))
	}

	override def getTable(databaseId: Long, table: String): CatalogTable = await {
		jdbcDao.action(jdbcDao.getTable(databaseId, table)).map {
			case Some(t) => t
			case None => throw new NoSuchTableException(getDatabase(databaseId).name, table)
		}
	}

	override def getTable(tableId: Long): CatalogTable = await {
		jdbcDao.action(jdbcDao.getTable(tableId)).map {
			case Some(t) => t
			case None => throw new NoSuchTableException("", s"Id $tableId")
		}
	}

	override def getTableOption(databaseId: Long, table: String): Option[CatalogTable] = await {
		jdbcDao.action(jdbcDao.getTable(databaseId, table))
	}

	override def getTableOption(tableId: Long): Option[CatalogTable] = await {
		jdbcDao.action(jdbcDao.getTable(tableId))
	}

	override def tableExists(databaseId: Long, table: String): Boolean = await {
		jdbcDao.action(jdbcDao.tableExists(databaseId, table))
	}

	override def listTables(databaseId: Long): Seq[CatalogTable] = await {
		jdbcDao.action(jdbcDao.listTables(databaseId))
	}

	override def listTables(databaseId: Long, pattern: String): Seq[CatalogTable] = await {
		jdbcDao.action(jdbcDao.listTables(databaseId, pattern))
	}

	// ----------------------------------------------------------------------------
	// Function -- belong to database
	// ----------------------------------------------------------------------------

	override protected def doCreateFunction(funcDefinition: CatalogFunction, ignoreIfExists: Boolean): Unit = await {
		jdbcDao.action(jdbcDao.getDatabase(funcDefinition.databaseId)).flatMap {
			case Some(database) =>
				jdbcDao.action(jdbcDao.functionExists(funcDefinition.databaseId, funcDefinition.name)).flatMap {
					case true =>
						ignoreIfExists match {
							case true => Future(Unit)
							case false => throw new FunctionExistsException(
								database.name, funcDefinition.name)
						}
					case false =>
						jdbcDao.action(
							jdbcDao.createFunction(funcDefinition).flatMap { funcId =>
								val resources = funcDefinition.resources.map { resource =>
									CatalogFunctionResource(
										funcId = funcId,
										resourceType = resource.resourceType.`type`,
										resource = resource.uri,
										createBy = funcDefinition.createBy,
										createTime = funcDefinition.createTime,
										updateBy = funcDefinition.updateBy,
										updateTime = funcDefinition.updateTime
									)
								}
								jdbcDao.createFunctionResources(resources:_*)
							}
						)
				}
			case None =>
				throw new NoSuchDatabaseException(s"Id ${funcDefinition.databaseId}")
		}
	}

	override protected def doDropFunction(databaseId: Long, func: String, ignoreIfNotExists: Boolean): Unit = await {
		jdbcDao.action(jdbcDao.getFunction(databaseId, func)).flatMap {
			case Some(catalogFunction) =>
				jdbcDao.action(
					jdbcDao.deleteFunctionResources(catalogFunction.id.get).flatMap { _ =>
						jdbcDao.deleteFunction(databaseId, func)
					}
				)
			case None =>
				ignoreIfNotExists match {
					case true => Future(Unit)
					case false => throw new NoSuchFunctionException(getDatabase(databaseId).name, func)
				}
		}
	}

	override protected def doRenameFunction(databaseId: Long, func: String, newFunc: String, updateBy: Long): Unit = await {
		jdbcDao.action(jdbcDao.functionExists(databaseId, func)).flatMap {
			case true =>
				jdbcDao.action(jdbcDao.functionExists(databaseId, newFunc)).flatMap {
					case false =>
						jdbcDao.action(jdbcDao.renameFunction(databaseId, func, newFunc)(updateBy))
					case true =>
						throw new FunctionExistsException(getDatabase(databaseId).name, newFunc)
				}
			case false =>
				throw new NoSuchFunctionException(getDatabase(databaseId).name, func)
		}
	}

	override def getFunction(databaseId: Long, func: String): CatalogFunction = await {
		jdbcDao.action(jdbcDao.getFunction(databaseId, func)).flatMap {
			case Some(function) =>
				jdbcDao.action(jdbcDao.listFunctionResources(function.id.get)).flatMap { catalogResources =>
					val functionResources = catalogResources.map { catalogResource =>
						FunctionResource(catalogResource.resourceType, catalogResource.resource)
					}
					Future(function.copy(resources = functionResources))
				}
			case None => throw new NoSuchFunctionException(getDatabase(databaseId).name, func)
		}
	}

	override def functionExists(databaseId: Long, func: String): Boolean = await {
		jdbcDao.action(jdbcDao.functionExists(databaseId, func))
	}

	override def listFunctions(databaseId: Long): Seq[CatalogFunction] = await {
		jdbcDao.action(jdbcDao.listFunctions(databaseId)).flatMap { functions =>
			Future {
				functions.groupBy(_._1).toSeq.map { case (func, tuples) =>
					val resources = tuples.map { case (_, resource) =>
						FunctionResource(resource.resourceType, resource.resource)
					}
					func.copy(resources = resources)
				}
			}
		}
	}

	override def listFunctions(databaseId: Long, pattern: String): Seq[CatalogFunction] = await {
		jdbcDao.action(jdbcDao.listFunctions(databaseId, pattern)).flatMap { functions =>
			Future {
				functions.groupBy(_._1).toSeq.map { case (func, tuples) =>
					val resources = tuples.map { case (_, resource) =>
						FunctionResource(resource.resourceType, resource.resource)
					}
					func.copy(resources = resources)
				}
			}
		}
	}

	// ----------------------------------------------------------------------------
	// View -- belong to database
	// ----------------------------------------------------------------------------

	override protected def doCreateView(viewDefinition: CatalogView, replaceIfExists: Boolean): Unit = await {
		jdbcDao.action(jdbcDao.getDatabase(viewDefinition.databaseId)).flatMap {
			case Some(database) =>
				jdbcDao.action(jdbcDao.getView(viewDefinition.databaseId, viewDefinition.name)).flatMap {
					case Some(existView) =>
						replaceIfExists match {
							case true =>
								jdbcDao.action(jdbcDao.updateView(viewDefinition.copy(id = existView.id)))
							case false =>
								throw new ViewExistsException(
									database.name, viewDefinition.name)
						}
					case None =>
						jdbcDao.action(jdbcDao.tableExists(viewDefinition.databaseId, viewDefinition.name)).flatMap {
							case true =>
								throw new TableExistsException(database.name, viewDefinition.name)
							case false =>
								jdbcDao.action(jdbcDao.createView(viewDefinition))
						}
				}
			case None =>
				throw new NoSuchDatabaseException(s"Id ${viewDefinition.databaseId}")
		}

	}

	override protected def doDropView(databaseId: Long, view: String, ignoreIfNotExists: Boolean): Unit = await {
		jdbcDao.action(jdbcDao.viewExists(databaseId, view)).flatMap {
			case true =>
				jdbcDao.action(jdbcDao.deleteView(databaseId, view))
			case false =>
				ignoreIfNotExists match {
					case true => Future(Unit)
					case false => throw new NoSuchViewException(getDatabase(databaseId).name, view)
				}
		}
	}

	override protected def doRenameView(databaseId: Long, view: String, newView: String, updateBy: Long): Unit = await {
		jdbcDao.action(jdbcDao.viewExists(databaseId, view)).flatMap {
			case true =>
				jdbcDao.action(jdbcDao.viewExists(databaseId, newView)).flatMap {
					case false =>
						jdbcDao.action(jdbcDao.renameView(databaseId, view, newView)(updateBy))
					case true =>
						throw new ViewExistsException(getDatabase(databaseId).name, newView)
				}
			case false =>
				throw new NoSuchViewException(getDatabase(databaseId).name, view)
		}
	}

	override def alterView(viewDefinition: CatalogView): Unit = await {
		jdbcDao.action(jdbcDao.updateView(viewDefinition))
	}

	override def getView(databaseId: Long, view: String): CatalogView = await {
		jdbcDao.action(jdbcDao.getView(databaseId, view)).map {
			case Some(v) => v
			case None => throw new NoSuchViewException(getDatabase(databaseId).name, view)
		}
	}

	override def getView(view: Long): CatalogView = await {
		jdbcDao.action(jdbcDao.getView(view)).map {
			case Some(v) => v
			case None => throw new NoSuchViewException("", s"Id $view")
		}
	}

	override def getViewOption(databaseId: Long, view: String): Option[CatalogView] = await {
		jdbcDao.action(jdbcDao.getView(databaseId, view))
	}

	override def getViewOption(view: Long): Option[CatalogView] = await {
		jdbcDao.action(jdbcDao.getView(view))
	}

	override def viewExists(databaseId: Long, view: String): Boolean = await {
		jdbcDao.action(jdbcDao.viewExists(databaseId, view))
	}

	override def listViews(databaseId: Long): Seq[CatalogView] = await {
		jdbcDao.action(jdbcDao.listViews(databaseId))
	}

	override def listViews(databaseId: Long, pattern: String): Seq[CatalogView] = await {
		jdbcDao.action(jdbcDao.listViews(databaseId, pattern))
	}

	// ----------------------------------------------------------------------------
	// UserGroupRel --   the relation of user - group
	// ----------------------------------------------------------------------------

	override protected def doCreateUserGroupRel(userGroupRels: CatalogUserGroupRel*): Unit = await {
		// exclude exists relation
		val groupIdToRels = userGroupRels.groupBy(rels => rels.groupId).toSeq
		require(groupIdToRels.length == 1)
		jdbcDao.action(jdbcDao.getUserGroupRelByGroup(groupIdToRels.head._1)).flatMap { existsRels =>
			val existsUsers = existsRels.map(_.userId)
			val needCreateRels = groupIdToRels.head._2.filterNot(rel => existsUsers.contains(rel.userId))
			jdbcDao.action(jdbcDao.createUserGroupRel(needCreateRels:_*))
		}
	}

	override protected def doDropUserGroupRel(groupId: Long, userIds: Seq[Long]): Unit = await {
		jdbcDao.action(jdbcDao.deleteUserGroupRel(groupId, userIds))
	}

	override protected def doDropUserGroupRelByGroup(groupId: Long): Unit = await {
		jdbcDao.action(jdbcDao.deleteUserGroupRelByGroup(groupId))
	}

	override protected def doDropUserGroupRelByUser(userId: Long): Unit = await {
		jdbcDao.action(jdbcDao.deleteUserGroupRelByUser(userId))
	}

	override def getUserGroupRelsByGroup(groupId: Long): Seq[CatalogUserGroupRel] = await {
		jdbcDao.action(jdbcDao.getUserGroupRelByGroup(groupId))
	}

	override def getUserGroupRelsByUser(userId: Long): Seq[CatalogUserGroupRel] = await {
		jdbcDao.action(jdbcDao.getUserGroupRelByUser(userId))
	}

	// ----------------------------------------------------------------------------
	// database privilege --   the privilege relation of user - database
	// ----------------------------------------------------------------------------
	override protected def doCreateDatabasePrivilege(dbPrivileges: CatalogDatabasePrivilege*): Unit = await {
		val needCreate = dbPrivileges.filterNot { priv =>
			await(jdbcDao.action(jdbcDao.getDatabasePrivilege(priv.userId, priv.databaseId, priv.privilegeType))).isDefined
		}
		jdbcDao.action(jdbcDao.createDatabasePrivilege(needCreate:_*))
	}

	override protected def doDropDatabasePrivilege(userId: Long, databaseId: Long, privileges: String*): Unit = await {
		jdbcDao.action(jdbcDao.deleteDatabasePrivilege(userId, databaseId, privileges:_*))
	}

	override def getDatabasePrivilege(userId: Long, databaseId: Long, privilege: String): Option[CatalogDatabasePrivilege] = await {
		jdbcDao.action(jdbcDao.getDatabasePrivilege(userId, databaseId, privilege))
	}

	override def getDatabasePrivilege(userId: Long, databaseId: Long): Seq[CatalogDatabasePrivilege] = await {
		jdbcDao.action(jdbcDao.getDatabasePrivilege(userId, databaseId))
	}

	override def getDatabasePrivilege(userId: Long): Seq[CatalogDatabasePrivilege] = await {
		jdbcDao.action(jdbcDao.getDatabasePrivilege(userId))
	}

	// ----------------------------------------------------------------------------
	// table privilege --   the privilege relation of user - table
	// ----------------------------------------------------------------------------
	override protected def doCreateTablePrivilege(tablePrivilege: CatalogTablePrivilege*): Unit = await {
		val needCreate = tablePrivilege.filterNot { priv =>
			await(jdbcDao.action(jdbcDao.getTablePrivilege(priv.userId, priv.databaseId, priv.table, priv.privilegeType))).isDefined
		}
		jdbcDao.action(jdbcDao.createTablePrivilege(needCreate:_*))
	}

	override protected def doDropTablePrivilege(userId: Long, databaseId: Long, table: String, privileges: String*): Unit = await {
		jdbcDao.action(jdbcDao.deleteTablePrivilege(userId, databaseId, table, privileges:_*))
	}

	override def getTablePrivilege(userId: Long, databaseId: Long, table: String, privilege: String): Option[CatalogTablePrivilege] = await {
		jdbcDao.action(jdbcDao.getTablePrivilege(userId, databaseId, table, privilege))
	}

	override def getTablePrivilege(userId: Long, databaseId: Long, table: String): Seq[CatalogTablePrivilege] = await {
		jdbcDao.action(jdbcDao.getTablePrivilege(userId, databaseId, table))
	}

	override def getTablePrivilege(userId: Long): Seq[CatalogTablePrivilege] = await {
		jdbcDao.action(jdbcDao.getTablePrivilege(userId))
	}

	// ----------------------------------------------------------------------------
	// column privilege --   the privilege relation of user - table - column
	// ----------------------------------------------------------------------------
	override protected def doCreateColumnPrivilege(columnPrivilege: CatalogColumnPrivilege*): Unit = await {
		val needCreate = columnPrivilege.groupBy(_.privilegeType).flatMap { case (privilege, columnPrivileges) =>
			val userId = columnPrivileges.head.userId
			val databaseId = columnPrivileges.head.databaseId
			val table = columnPrivileges.head.table
			val existColumns = await(jdbcDao.action(jdbcDao.getColumnPrivilege(userId, databaseId, table, privilege))).map(_.column)
			columnPrivileges.filterNot(col => existColumns.contains(col.column))
		}.toSeq
		jdbcDao.action(jdbcDao.createColumnPrivilege(needCreate:_*))
	}

	override protected def doDropColumnPrivilege(userId: Long, databaseId: Long, table: String, privileges: Seq[(String, Seq[String])]): Unit = await {
		val dropActions = privileges.map { case (privilegeType, columns) =>
			jdbcDao.deleteColumnPrivilege(userId, databaseId, table, columns, privilegeType)
		}
		jdbcDao.actionTransactionally(dropActions:_*)
	}

	override def getColumnPrivilege(userId: Long, databaseId: Long, table: String, privilege: String): Seq[CatalogColumnPrivilege] = await {
		jdbcDao.action(jdbcDao.getColumnPrivilege(userId, databaseId, table, privilege))
	}

	override def getColumnPrivilege(userId: Long): Seq[CatalogColumnPrivilege] = await {
		jdbcDao.action(jdbcDao.getColumnPrivilege(userId))
	}
}
