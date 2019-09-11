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

import java.util.Locale

import moonbox.catalog.AbstractCatalog.User
import moonbox.catalog._
import moonbox.common.{MbConf, MbLogging}


class MoonboxCatalog(val conf: MbConf) extends MbLogging {
	
	private val jdbcCatalog = new JdbcCatalog(conf)

	private implicit var userInSession: User = _

	var catalogUser: CatalogUser = _

	var catalogOrg: CatalogOrganization = _

	private var currentDb = formatDatabaseName(jdbcCatalog.defauleDb)

	private def formatDatabaseName(db: String): String = {
		db.toLowerCase(Locale.ROOT)
	}

	def setCurrentUser(org: String, user: String): this.type = synchronized {
		userInSession = {
			val orgId = jdbcCatalog.organizationId(org)
			val userId = jdbcCatalog.userId(orgId, user)
			User(orgId, org, userId, user)
		}
		catalogOrg = getOrganization(org)
		catalogUser = getUser(org, user)
		this
	}

	def setCurrentDb(db: String): Unit = synchronized {
		if (jdbcCatalog.databaseExists(db)) {
			currentDb = formatDatabaseName(db)
		} else {
			throw new NoSuchDatabaseException(db)
		}
	}

	def getCurrentDb: String = synchronized { currentDb }

	def getCurrentOrg: String = synchronized { catalogOrg.name }

	def getCurrentUser: String = synchronized { catalogUser.name }


	def createOrganization(
		orgDefinition: CatalogOrganization,
		ignoreIfExists: Boolean): Unit = {
		jdbcCatalog.createOrganization(orgDefinition, ignoreIfExists)
	}

	def alterOrganization(orgDefinition: CatalogOrganization): Unit = {
		jdbcCatalog.alterOrganization(orgDefinition)
	}

	def renameOrganization(org: String, newOrg: String): Unit = {
		jdbcCatalog.renameOrganization(org, newOrg)
	}

	def dropOrganization(name: String, ignoreIfNotExists: Boolean, cascade: Boolean): Unit = {
		jdbcCatalog.dropOrganization(name, ignoreIfNotExists, cascade)
	}

	def getOrganization(name: String): CatalogOrganization = {
		jdbcCatalog.getOrganization(name)
	}

	def organizationExists(name: String): Boolean = {
		jdbcCatalog.organizationExists(name)
	}

	def listOrganizations(): Seq[CatalogOrganization] = {
		jdbcCatalog.listOrganizations()
	}

	def listOrganizations(pattern: String): Seq[CatalogOrganization] = {
		jdbcCatalog.listOrganizations(pattern)
	}

	def createUser(userDefinition: CatalogUser, ignoreIfExists: Boolean): Unit = {
		jdbcCatalog.createUser(userDefinition, ignoreIfExists)
	}

	def renameUser(org: String, user: String, newUser: String): Unit = {
		jdbcCatalog.renameUser(org, user, newUser)
	}

	def alterUser(userDefinition: CatalogUser): Unit = {
		jdbcCatalog.alterUser(userDefinition)
	}

	def dropUser(org: String, name: String, ignoreIfNotExists: Boolean): Unit = {
		jdbcCatalog.dropUser(org, name, ignoreIfNotExists)
	}

	def userExists(org: String, user: String): Boolean = {
		jdbcCatalog.userExists(org, user)
	}

	def getUser(org: String, user: String): CatalogUser = {
		jdbcCatalog.getUser(org, user)
	}

	def getUserOption(org: String, username: String): Option[CatalogUser] = {
		jdbcCatalog.getUserOption(org, username)
	}

	def listUsers(org: String): Seq[CatalogUser] = {
		jdbcCatalog.listUsers(org)
	}

	def listUsers(org: String, pattern: String): Seq[CatalogUser] = {
		jdbcCatalog.listUsers(org, pattern)
	}

	def listUsers(org: String, pattern: Option[String]): Seq[CatalogUser] = {
		pattern match {
			case Some(p) => jdbcCatalog.listUsers(org, p)
			case None => jdbcCatalog.listUsers(org)
		}
	}

	def listSas(): Seq[CatalogUser] = {
		jdbcCatalog.listSas()
	}

	def listSas(pattern: String): Seq[CatalogUser] = {
		jdbcCatalog.listSas(pattern)
	}

	def createDatabase(dbDefinition: CatalogDatabase, ignoreIfExists: Boolean): Unit = {
		jdbcCatalog.createDatabase(dbDefinition, ignoreIfExists)
	}

	def renameDatabase(db: String, newDb: String): Unit = {
		jdbcCatalog.renameDatabase(db, newDb)
	}

	def alterDatabase(dbDefinition: CatalogDatabase): Unit = {
		jdbcCatalog.alterDatabase(dbDefinition)
	}

	def databaseExists(db: String): Boolean = {
		jdbcCatalog.databaseExists(db)
	}

	def dropDatabase(database: String, ignoreIfNotExists: Boolean, cascade: Boolean): Unit = {
		jdbcCatalog.dropDatabase(database, ignoreIfNotExists, cascade)
	}

	def getDatabase(database: String): CatalogDatabase = {
		jdbcCatalog.getDatabase(database)
	}

	def listDatabase(): Seq[CatalogDatabase] = {
		jdbcCatalog.listDatabases()
	}

	def listDatabase(pattern: String): Seq[CatalogDatabase] = {
		jdbcCatalog.listDatabases(pattern)
	}

	def listDatabase(pattern: Option[String]): Seq[CatalogDatabase] = {
		pattern match {
			case Some(p) => listDatabase(p)
			case None => listDatabase()
		}
	}

	def createTable(tableDefinition: CatalogTable, ignoreIfExists: Boolean): Unit = {
		jdbcCatalog.createTable(tableDefinition, ignoreIfExists)
	}

	def alterTable(tableDefinition: CatalogTable): Unit = {
		jdbcCatalog.alterTable(tableDefinition)
	}

	def renameTable(db: String, table: String, newTable: String) = {
		jdbcCatalog.renameTable(db, table, newTable)
	}

	def dropTable(db: String, table: String, ignoreIfNotExists: Boolean): Unit = {
		jdbcCatalog.dropTable(db, table, ignoreIfNotExists)
	}

	def getTable(database: String, table: String): CatalogTable = {
		jdbcCatalog.getTable(database, table)
	}

	def getTableOption(database: String, table: String): Option[CatalogTable] = {
		jdbcCatalog.getTableOption(database, table)
	}

	def tableExists(database: String, table: String): Boolean = {
		jdbcCatalog.tableExists(database, table)
	}

	def listTables(database: String): Seq[CatalogTable] = {
		jdbcCatalog.listTables(database)
	}

	def listTables(database: String, pattern: String): Seq[CatalogTable] = {
		jdbcCatalog.listTables(database, pattern)
	}

	def listTables(database: String, pattern: Option[String]): Seq[CatalogTable] = {
		pattern match {
			case Some(p) => listTables(database, p)
			case None => listTables(database)
		}
	}

	def createFunction(funcDefinition: CatalogFunction, ignoreIfExists: Boolean): Unit = {
		jdbcCatalog.createFunction(funcDefinition, ignoreIfExists)
	}

	def renameFunction(database: String, func: String, newFunc: String): Unit = {
		jdbcCatalog.renameFunction(database, func, newFunc)
	}

	def functionExists(database: String, function: String): Boolean = {
		jdbcCatalog.functionExists(database, function)
	}

	def dropFunction(db: String, function: String, ignoreIfNotExists: Boolean): Unit = {
		jdbcCatalog.dropFunction(db, function, ignoreIfNotExists)
	}

	def getFunction(database: String, function: String): CatalogFunction = {
		jdbcCatalog.getFunction(database, function)
	}

	def getFunctionOption(database: String, function: String): Option[CatalogFunction] = {
		jdbcCatalog.getFunctionOption(database, function)
	}


	def listFunctions(database: String): Seq[CatalogFunction] = {
		jdbcCatalog.listFunctions(database)
	}

	def listFunctions(database: String, pattern: String): Seq[CatalogFunction] = {
		jdbcCatalog.listFunctions(database, pattern)
	}

	def createProcedure(procDefinition: CatalogProcedure, ignoreIfExists: Boolean): Unit = {
		jdbcCatalog.createProcedure(procDefinition, ignoreIfExists)
	}

	def renameProcedure(proc: String, newProc: String): Unit = {
		jdbcCatalog.renameProcedure(proc, newProc)
	}

	def alterProcedure(procDefinition: CatalogProcedure): Unit = {
		jdbcCatalog.alterProcedure(procDefinition)
	}

	def procedureExists(proc: String): Boolean = {
		jdbcCatalog.procedureExists(proc)
	}

	def dropProcedure(proc: String, ignoreIfNotExists: Boolean): Unit = {
		jdbcCatalog.dropProcedure(proc, ignoreIfNotExists)
	}

	def getProcedure(proc: String): CatalogProcedure = {
		jdbcCatalog.getProcedure(proc)
	}

	def listProcedures(): Seq[CatalogProcedure] = {
		jdbcCatalog.listProcedures()
	}

	def listProcedures(pattern: String): Seq[CatalogProcedure] = {
		jdbcCatalog.listProcedures(pattern)
	}

	def listProcedures(pattern: Option[String]): Seq[CatalogProcedure] = {
		pattern match {
			case Some(p) => jdbcCatalog.listProcedures(p)
			case None => jdbcCatalog.listProcedures()
		}
	}

	def createTimedEvent(eventDefinition: CatalogTimedEvent, ignoreIfExists: Boolean): Unit = {
		jdbcCatalog.createTimedEvent(eventDefinition, ignoreIfExists)
	}

	def renameTimedEvent(event: String, newEvent: String): Unit = {
		jdbcCatalog.renameTimedEvent(event, newEvent)
	}

	def alterTimedEvent(eventDefinition: CatalogTimedEvent): Unit = {
		jdbcCatalog.alterTimedEvent(eventDefinition)
	}

	def timedEventExists(event: String): Boolean = {
		jdbcCatalog.timedEventExists(event)
	}

	def timedEventExists(procId: Long): Boolean = {
		jdbcCatalog.timedEventExists(procId)
	}

	def dropTimedEvent(event: String, ignoreIfNotExists: Boolean): Unit = {
		jdbcCatalog.dropTimedEvent(event, ignoreIfNotExists)
	}

	def getTimedEvent(event: String): CatalogTimedEvent = {
		jdbcCatalog.getTimedEvent(event)
	}

	def listTimedEvents(): Seq[CatalogTimedEvent] = {
		jdbcCatalog.listTimedEvents()
	}

	def listTimedEvents(pattern: String): Seq[CatalogTimedEvent] = {
		jdbcCatalog.listTimedEvents(pattern)
	}

	def listTimedEvents(pattern: Option[String]): Seq[CatalogTimedEvent] = {
		pattern match {
			case Some(p) => jdbcCatalog.listTimedEvents(p)
			case None => jdbcCatalog.listTimedEvents()
		}
	}

	def createDatabasePrivilege(dbPrivilege: CatalogDatabasePrivilege): Unit = {
		jdbcCatalog.createDatabasePrivilege(dbPrivilege)
	}

	def dropDatabasePrivilege(user: String, database: String, privileges: Seq[String]): Unit = {
		jdbcCatalog.dropDatabasePrivilege(user, database, privileges)
	}

	def getDatabasePrivilege(user: String, database: String): CatalogDatabasePrivilege = {
		jdbcCatalog.getDatabasePrivilege(user, database)
	}

	def createTablePrivilege(tablePrivilege: CatalogTablePrivilege): Unit = {
		jdbcCatalog.createTablePrivilege(tablePrivilege)
	}

	def dropTablePrivilege(user: String, database: String, table: String, privileges: Seq[String]): Unit = {
		jdbcCatalog.dropTablePrivilege(user, database, table, privileges)
	}

	def getTablePrivilege(user: String, database: String, table: String): CatalogTablePrivilege = {
		jdbcCatalog.getTablePrivilege(user, database, table)
	}

	def createColumnPrivilege(columnPrivilege: CatalogColumnPrivilege): Unit = {
		jdbcCatalog.createColumnPrivilege(columnPrivilege)
	}

	def dropColumnPrivilege(user: String, database: String, table: String, privileges: Seq[(String, Seq[String])]): Unit = {
		jdbcCatalog.dropColumnPrivilege(user, database, table, privileges)
	}

	def getColumnPrivilege(user: String, database: String, table: String): CatalogColumnPrivilege = {
		jdbcCatalog.getColumnPrivilege(user, database, table)
	}

	def stop(): Unit = {
		jdbcCatalog.close()
	}

	def addListener(listener: CatalogEventListener): Unit = {
		jdbcCatalog.addListener(listener)
	}

}
