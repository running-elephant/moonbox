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

import moonbox.common.util.ListenerBus


object AbstractCatalog {
	case class User(orgId: Long, org: String, userId: Long, user: String)
}


abstract class AbstractCatalog extends ListenerBus[CatalogEventListener, CatalogEvent] {

	import AbstractCatalog._

	// ----------------------------------------------------------------------------
	// Application
	// ----------------------------------------------------------------------------
	final def createApplication(
		appDefinition: CatalogApplication)(implicit by: User): Unit = {
		postToAll(CreateApplicationPreEvent(appDefinition.name))
		doCreateApplication(appDefinition)
		postToAll(CreateApplicationEvent(appDefinition.name))
	}

	final def dropApplication(app: String, ignoreIfNotExists: Boolean)(implicit by: User): Unit = {
		postToAll(DropApplicationPreEvent(app))
		doDropApplication(app, ignoreIfNotExists)
		postToAll(DropApplicationEvent(app))
	}

	protected def doCreateApplication(appDefinition: CatalogApplication)(implicit by: User): Unit

	protected def doDropApplication(app: String, ignoreIfNotExists: Boolean)(implicit by: User): Unit

	def alterApplication(appDefinition: CatalogApplication)(implicit by: User): Unit

	def getApplication(app: String): CatalogApplication

	def getApplicationOption(app: String): Option[CatalogApplication]

	def applicationExists(app: String): Boolean

	def listApplications(): Seq[CatalogApplication]

	def listApplications(pattern: String): Seq[CatalogApplication]

	// ----------------------------------------------------------------------------
	// Organization
	// ----------------------------------------------------------------------------

	final def createOrganization(
		orgDefinition: CatalogOrganization, ignoreIfExists: Boolean)(implicit by: User): Unit = {
		val org = orgDefinition.name
		postToAll(CreateOrganizationPreEvent(org))
		doCreateOrganization(orgDefinition, ignoreIfExists)
		postToAll(CreateOrganizationEvent(org))
	}

	final def dropOrganization(org: String, ignoreIfNotExists: Boolean, cascade: Boolean)(implicit by: User): Unit = {
		postToAll(DropOrganizationPreEvent(org))
		doDropOrganization(org, ignoreIfNotExists, cascade)
		postToAll(DropOrganizationEvent(org))
	}

	final def renameOrganization(org: String, newOrg: String)(implicit by: User): Unit = {
		postToAll(RenameOrganizationPreEvent(org, newOrg))
		doRenameOrganization(org, newOrg)
		postToAll(RenameOrganizationEvent(org, newOrg))
	}

	protected def doCreateOrganization(orgDefinition: CatalogOrganization, ignoreIfExists: Boolean)(implicit by: User): Unit

	protected def doDropOrganization(org: String, ignoreIfNotExists: Boolean, cascade: Boolean)(implicit by: User): Unit

	protected def doRenameOrganization(org: String, newOrg: String)(implicit by: User): Unit

	def alterOrganization(orgDefinition: CatalogOrganization)(implicit by: User): Unit

	def getOrganization(org: String): CatalogOrganization

	def getOrganizationOption(org: String): Option[CatalogOrganization]

	def organizationExists(org: String): Boolean

	def listOrganizations(): Seq[CatalogOrganization]

	def listOrganizations(pattern: String): Seq[CatalogOrganization]

	// ----------------------------------------------------------------------------
	// User -- belong to organization
	// ----------------------------------------------------------------------------

	final def createUser(
		userDefinition: CatalogUser, ignoreIfExists: Boolean)(implicit by: User): Unit = {
		postToAll(CreateUserPreEvent(userDefinition.org, userDefinition.name))
		doCreateUser(userDefinition, ignoreIfExists)
		postToAll(CreateUserEvent(userDefinition.org, userDefinition.name))
	}

	protected def doCreateUser(userDefinition: CatalogUser, ignoreIfExists: Boolean)(implicit by: User): Unit

	final def dropUser(org: String, user: String, ignoreIfNotExists: Boolean)(implicit by: User): Unit = {
		postToAll(DropUserPreEvent(org, user))
		doDropUser(org, user, ignoreIfNotExists)
		postToAll(DropUserEvent(org, user))
	}

	protected def doDropUser(org: String, user: String, ignoreIfNotExists: Boolean)(implicit by: User): Unit

	final def renameUser(org: String, user: String, newUser: String)(implicit by: User): Unit = {
		postToAll(RenameUserPreEvent(org, user, newUser))
		doRenameUser(org, user, newUser)
		postToAll(RenameUserEvent(org, user, newUser))
	}

	protected def doRenameUser(org: String, user: String, newUser: String)(implicit by: User): Unit

	def alterUser(userDefinition: CatalogUser)(implicit by: User): Unit

	def getUser(org: String, user: String): CatalogUser

	def getUserOption(org: String, user: String): Option[CatalogUser]

	def userExists(org: String, user: String): Boolean

	def listUsers(org: String): Seq[CatalogUser]

	def listUsers(org: String, pattern: String): Seq[CatalogUser]

	def listSas(): Seq[CatalogUser]

	def listSas(pattern: String): Seq[CatalogUser]


	// ----------------------------------------------------------------------------
	// Database -- belong to organization
	// ----------------------------------------------------------------------------

	final def createDatabase(dbDefinition: CatalogDatabase, ignoreIfExists: Boolean)(implicit by: User): Unit = {
		postToAll(CreateDatabasePreEvent(by.org, dbDefinition.name))
		doCreateDatabase(dbDefinition, ignoreIfExists)
		postToAll(CreateDatabaseEvent(by.org, dbDefinition.name))
	}

	protected def doCreateDatabase(dbDefinition: CatalogDatabase, ignoreIfExists: Boolean)(implicit by: User): Unit

	final def dropDatabase(db: String, ignoreIfNotExists: Boolean, cascade: Boolean)(implicit by: User): Unit = {
		postToAll(DropDatabasePreEvent(by.org, db))
		doDropDatabase(db, ignoreIfNotExists, cascade)
		postToAll(DropDatabaseEvent(by.org, db))
	}

	protected def doDropDatabase(db: String, ignoreIfNotExists: Boolean, cascade: Boolean)(implicit by: User): Unit

	final def renameDatabase(db: String, newDb: String)(implicit by: User): Unit = {
		postToAll(RenameDatabasePreEvent(by.org, db, newDb))
		doRenameDatabase(db, newDb)
		postToAll(RenameDatabaseEvent(by.org, db, newDb))
	}

	protected def doRenameDatabase(db: String, newDb: String)(implicit by: User): Unit

	def alterDatabase(dbDefinition: CatalogDatabase)(implicit by: User): Unit

	def getDatabase(db: String)(implicit by: User): CatalogDatabase

	def getDatabaseOption(db: String)(implicit by: User): Option[CatalogDatabase]

	def databaseExists(db: String)(implicit by: User): Boolean

	def listDatabases()(implicit by: User): Seq[CatalogDatabase]

	def listDatabases(pattern: String)(implicit by: User): Seq[CatalogDatabase]


	// ----------------------------------------------------------------------------
	// Table -- belong to database
	// ----------------------------------------------------------------------------

	final def createTable(tableDefinition: CatalogTable, ignoreIfExists: Boolean)(implicit by: User): Unit = {
		postToAll(CreateTablePreEvent(
			by.org, tableDefinition.database, tableDefinition.name))
		doCreateTable(tableDefinition, ignoreIfExists)
		postToAll(CreateTableEvent(
			by.org, tableDefinition.database, tableDefinition.name))
	}

	protected def doCreateTable(tableDefinition: CatalogTable, ignoreIfExists: Boolean)(implicit by: User): Unit

	final def dropTable(
		db: String, table: String, ignoreIfNotExists: Boolean)(implicit by: User): Unit = {
		postToAll(DropTablePreEvent(by.org, db, table))
		doDropTable(db, table, ignoreIfNotExists)
		postToAll(DropTableEvent(by.org, db, table))
	}

	protected def doDropTable(database: String, table: String, ignoreIfNotExists: Boolean)(implicit by: User): Unit

	final def renameTable(database: String, table: String, newTable: String)(implicit by: User): Unit = {
		postToAll(RenameTablePreEvent(by.org, database, table, newTable))
		doRenameTable(database, table, newTable)
		postToAll(RenameTableEvent(by.org, database, table, newTable))
	}

	protected def doRenameTable(database: String, table: String, newTable: String)(implicit by: User): Unit

	def alterTable(tableDefinition: CatalogTable)(implicit by: User): Unit

	def getTable(database: String, table: String)(implicit by: User): CatalogTable

	def getTableOption(database: String, table: String)(implicit by: User): Option[CatalogTable]

	def tableExists(database: String, table: String)(implicit by: User): Boolean

	def listTables(database: String)(implicit by: User): Seq[CatalogTable]

	def listTables(database: String, pattern: String)(implicit by: User): Seq[CatalogTable]


	// ----------------------------------------------------------------------------
	// Function -- belong to database
	// ----------------------------------------------------------------------------

	final def createFunction(funcDefinition: CatalogFunction, ignoreIfExists: Boolean)(implicit by: User): Unit = {
		val function = funcDefinition.name
		postToAll(CreateFunctionPreEvent(by.org, funcDefinition.database, function))
		doCreateFunction(funcDefinition, ignoreIfExists)
		postToAll(CreateFunctionEvent(by.org, funcDefinition.database, function))
	}

	protected def doCreateFunction(funcDefinition: CatalogFunction, ignoreIfExists: Boolean)(implicit by: User): Unit

	final def dropFunction(database: String, func: String, ignoreIfNotExists: Boolean)(implicit by: User): Unit = {
		postToAll(DropFunctionPreEvent(by.org, database, func))
		doDropFunction(database, func, ignoreIfNotExists)
		postToAll(DropFunctionEvent(by.org, database, func))
	}

	protected def doDropFunction(database: String, func: String, ignoreIfNotExists: Boolean)(implicit by: User): Unit

	final def renameFunction(database: String, func: String, newFunc: String)(implicit by: User): Unit = {
		postToAll(RenameFunctionPreEvent(by.org, database, func, newFunc))
		doRenameFunction(database, func, newFunc)
		postToAll(RenameFunctionEvent(by.org, database, func, newFunc))
	}

	protected def doRenameFunction(database: String, func: String, newFunc: String)(implicit by: User): Unit

	def getFunction(database: String, func: String)(implicit by: User): CatalogFunction

	def getFunctionOption(database: String, func: String)(implicit by: User): Option[CatalogFunction]

	def functionExists(database: String, func: String)(implicit by: User): Boolean

	def listFunctions(database: String)(implicit by: User): Seq[CatalogFunction]

	def listFunctions(database: String, pattern: String)(implicit by: User): Seq[CatalogFunction]

	// ----------------------------------------------------------------------------
	// Procedure -- belong to organization
	// ----------------------------------------------------------------------------

	final def createProcedure(procDefinition: CatalogProcedure, ignoreIfExists: Boolean)(implicit by: User): Unit = {
		postToAll(CreateProcedurePreEvent(by.org, procDefinition.name))
		doCreateProcedure(procDefinition, ignoreIfExists)
		postToAll(CreateProcedureEvent(by.org, procDefinition.name))
	}

	protected def doCreateProcedure(procDefinition: CatalogProcedure, ignoreIfExists: Boolean)(implicit by: User): Unit

	final def dropProcedure(proc: String, ignoreIfNotExists: Boolean)(implicit by: User): Unit = {
		postToAll(DropProcedurePreEvent(by.org, proc))
		doDropProcedure(proc, ignoreIfNotExists)
		postToAll(DropProcedureEvent(by.org, proc))
	}

	protected def doDropProcedure(proc: String, ignoreIfNotExists: Boolean)(implicit by: User): Unit

	final def renameProcedure(proc: String, newProc: String)(implicit by: User): Unit = {
		postToAll(RenameProcedurePreEvent(by.org, proc, newProc))
		doRenameProcedure(proc, newProc)
		postToAll(RenameProcedureEvent(by.org, proc, newProc))
	}

	protected def doRenameProcedure(proc: String, newProc: String)(implicit by: User): Unit

	def alterProcedure(procDefinition: CatalogProcedure)(implicit by: User): Unit

	def getProcedure(proc: String)(implicit by: User): CatalogProcedure

	def getProcedureOption(proc: String)(implicit by: User): Option[CatalogProcedure]

	def procedureExists(proc: String)(implicit by: User): Boolean

	def listProcedures()(implicit by: User): Seq[CatalogProcedure]

	def listProcedures(pattern: String)(implicit by: User): Seq[CatalogProcedure]


	// ----------------------------------------------------------------------------
	// timedEvent -- belong to organization
	// ----------------------------------------------------------------------------
	final def createTimedEvent(eventDefinition: CatalogTimedEvent, ignoreIfExists: Boolean)(implicit by: User): Unit = {
		postToAll(CreateTimedEventPreEvent(by.org, eventDefinition.name))
		doCreateTimedEvent(eventDefinition, ignoreIfExists)
		postToAll(CreateTimedEventEvent(by.org, eventDefinition.name))
	}

	protected def doCreateTimedEvent(eventDefinition: CatalogTimedEvent, ignoreIfExists: Boolean)(implicit by: User): Unit

	final def renameTimedEvent(event: String, newEvent: String)(implicit by: User): Unit = {
		postToAll(RenameTimedEventPreEvent(by.org, event))
		doRenameTimedEvent(event, newEvent)
		postToAll(RenameTimedEventEvent(by.org, event))
	}

	protected def doRenameTimedEvent(event: String, newEvent: String)(implicit by: User): Unit

	final def dropTimedEvent(event: String, ignoreIfNotExists: Boolean)(implicit by: User): Unit = {
		postToAll(DropTimedEventPreEvent(by.org, event))
		doDropTimedEvent(event, ignoreIfNotExists)
		postToAll(DropTimedEventEvent(by.org, event))
	}

	protected def doDropTimedEvent(event: String, ignoreIfNotExists: Boolean)(implicit by: User): Unit

	def alterTimedEvent(eventDefinition: CatalogTimedEvent)(implicit by: User): Unit

	def getTimedEvent(event: String)(implicit by: User): CatalogTimedEvent

	def getTimedEventOption(event: String)(implicit by: User): Option[CatalogTimedEvent]

	def timedEventExists(event: String)(implicit by: User): Boolean

	def timedEventExists(procId: Long)(implicit by: User): Boolean

	def listTimedEvents()(implicit by: User): Seq[CatalogTimedEvent]

	def listTimedEvents(pattern: String)(implicit by: User): Seq[CatalogTimedEvent]


	// ----------------------------------------------------------------------------
	// database privilege --   the privilege relation of user - database
	// ----------------------------------------------------------------------------
	final def createDatabasePrivilege(dbPrivilege: CatalogDatabasePrivilege)(implicit by: User): Unit = {
		postToAll(CreateDatabasePrivilegePreEvent(
			by.org, dbPrivilege.user, dbPrivilege.database, dbPrivilege.privileges))
		doCreateDatabasePrivilege(dbPrivilege)
		postToAll(CreateDatabasePrivilegeEvent(
			by.org, dbPrivilege.user, dbPrivilege.database, dbPrivilege.privileges))
	}

	protected def doCreateDatabasePrivilege(dbPrivilege: CatalogDatabasePrivilege)(implicit by: User): Unit

	final def dropDatabasePrivilege(user: String, database: String, privileges: Seq[String])(implicit by: User): Unit = {
		postToAll(DropDatabasePrivilegePreEvent(by.org, user, database, privileges))
		doDropDatabasePrivilege(user, database, privileges)
		postToAll(DropDatabasePrivilegeEvent(by.org, user, database, privileges))
	}

	protected def doDropDatabasePrivilege(user: String, database: String, privileges: Seq[String])(implicit by: User): Unit

	protected def getDatabasePrivilege(user: String, database: String)(implicit by: User): CatalogDatabasePrivilege


	// ----------------------------------------------------------------------------
	// table privilege --   the privilege relation of user - table
	// ----------------------------------------------------------------------------
	final def createTablePrivilege(tablePrivilege: CatalogTablePrivilege)(implicit by: User): Unit = {
		postToAll(CreateTablePrivilegePreEvent(
			by.org, tablePrivilege.user, tablePrivilege.database, tablePrivilege.table, tablePrivilege.privileges))
		doCreateTablePrivilege(tablePrivilege)
		postToAll(CreateTablePrivilegeEvent(
			by.org, tablePrivilege.user, tablePrivilege.database, tablePrivilege.table, tablePrivilege.privileges))
	}

	protected def doCreateTablePrivilege(tablePrivilege: CatalogTablePrivilege)(implicit by: User): Unit

	final def dropTablePrivilege(user: String, database: String, table: String, privileges: Seq[String])(implicit by: User): Unit = {
		postToAll(DropTablePrivilegePreEvent(by.org, user, database, table, privileges))
		doDropTablePrivilege(user, database, table, privileges)
		postToAll(DropTablePrivilegeEvent(by.org, user, database, table, privileges))
	}

	protected def doDropTablePrivilege(user: String, database: String, table: String, privileges: Seq[String])(implicit by: User): Unit

	protected def getTablePrivilege(user: String, database: String, table: String)(implicit by: User): CatalogTablePrivilege


	// ----------------------------------------------------------------------------
	// column privilege --   the privilege relation of user - table - column
	// ----------------------------------------------------------------------------
	final def createColumnPrivilege(columnPrivilege: CatalogColumnPrivilege)(implicit by: User): Unit = {
		postToAll(CreateColumnPrivilegePreEvent(
			by.org, columnPrivilege.user, columnPrivilege.database, columnPrivilege.table, columnPrivilege.privilege))
		doCreateColumnPrivilege(columnPrivilege)
		postToAll(CreateColumnPrivilegeEvent(
			by.org, columnPrivilege.user, columnPrivilege.database, columnPrivilege.table, columnPrivilege.privilege))
	}

	protected def doCreateColumnPrivilege(columnPrivilege: CatalogColumnPrivilege)(implicit by: User): Unit

	final def dropColumnPrivilege(user: String, database: String, table: String, privileges: Seq[(String, Seq[String])])(implicit by: User): Unit = {
		postToAll(DropColumnPrivilegePreEvent(by.org, user, database, table, privileges))
		doDropColumnPrivilege(user, database, table, privileges)
		postToAll(DropColumnPrivilegeEvent(by.org, user, database, table, privileges))
	}

	protected def doDropColumnPrivilege(user: String, database: String, table: String, privileges: Seq[(String, Seq[String])])(implicit by: User): Unit

	protected def getColumnPrivilege(user: String, database: String, table: String)(implicit by: User): CatalogColumnPrivilege

	override protected def doPostEvent(listener: CatalogEventListener, event: CatalogEvent): Unit = {
		listener.onEvent(event)
	}
}
