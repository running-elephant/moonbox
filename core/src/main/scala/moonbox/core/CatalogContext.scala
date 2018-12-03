/*-
 * <<
 * Moonbox
 * ==
 * Copyright (C) 2016 - 2018 EDP
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

import moonbox.common.util.Utils
import moonbox.common.{MbConf, MbLogging}
import moonbox.core.catalog._
import org.apache.spark.sql.catalyst.TableIdentifier
import org.apache.spark.sql.catalyst.analysis.UnresolvedRelation
import moonbox.core.datasys.DataSystem

object CatalogContext {
	val DEFAULT_DATABASE = "default"
}

class CatalogContext(val conf: MbConf) extends MbLogging {
	private val catalog = new JdbcCatalog(conf)

	def stop(): Unit = {
		catalog.close()
	}

	def addListener(listener: CatalogEventListener): Unit = {
		catalog.addListener(listener)
	}

	def isSa(userId: Long): Boolean = {
		catalog.getUser(userId).isSA
	}

	def canDml(userId: Long): Boolean = true

	def canDdl(userId: Long): Boolean = {
		catalog.getUser(userId).ddl
	}

	def canDcl(userId: Long): Boolean = {
		catalog.getUser(userId).dcl
	}

	def canAccount(userId: Long): Boolean = {
		catalog.getUser(userId).account
	}

	def canGrantAccount(userId: Long): Boolean = {
		catalog.getUser(userId).grantAccount
	}

	def canGrantDdl(userId: Long): Boolean = {
		catalog.getUser(userId).grantDdl
	}

	def canGrantDcl(userId: Long): Boolean = {
		catalog.getUser(userId).grantDcl
	}

	def createOrganization(
		orgDefinition: CatalogOrganization,
		ignoreIfExists: Boolean): Unit = {
		catalog.createOrganization(orgDefinition, ignoreIfExists)
	}

	def alterOrganization(orgDefinition: CatalogOrganization): Unit = {
		catalog.alterOrganization(orgDefinition)
	}

	def renameOrganization(org: String, newOrg: String, updateBy: Long): Unit = {
		catalog.renameOrganization(org, newOrg, updateBy)
	}

	def dropOrganization(name: String, ignoreIfNotExists: Boolean, cascade: Boolean): Unit = {
		catalog.dropOrganization(name, ignoreIfNotExists, cascade)
	}

	def getOrganization(name: String): CatalogOrganization = {
		catalog.getOrganization(name)
	}

	def getOrganization(organizationId: Long): CatalogOrganization = {
		catalog.getOrganization(organizationId)
	}

	def organizationExists(name: String): Boolean = {
		catalog.organizationExists(name)
	}

	def listOrganizations(): Seq[CatalogOrganization] = {
		catalog.listOrganizations()
	}

	def listOrganizations(pattern: String): Seq[CatalogOrganization] = {
		catalog.listOrganizations(pattern)
	}

	def createUser(userDefinition: CatalogUser, organization: String, ignoreIfExists: Boolean): Unit = {
		catalog.createUser(userDefinition, organization, ignoreIfExists)
	}

	def renameUser(organizationId: Long, organization: String, user: String, newUser: String, updateBy: Long): Unit = {
		catalog.renameUser(organizationId, organization,  user, newUser, updateBy)
	}

	def alterUser(userDefinition: CatalogUser): Unit = {
		catalog.alterUser(userDefinition)
	}

	def dropUser(organizationId: Long, organization: String, name: String, ignoreIfNotExists: Boolean): Unit = {
		catalog.dropUser(organizationId, organization, name, ignoreIfNotExists)
	}

	def getUser(organizationId: Long, user: String): CatalogUser = {
		catalog.getUser(organizationId, user)
	}

	def userExists(organizationId: Long, user: String): Boolean = {
		catalog.userExists(organizationId, user)
	}

	def getUser(userId: Long): CatalogUser = {
		catalog.getUser(userId)
	}

	def getUserOption(username: String): Option[CatalogUser] = {
		catalog.getUserOption(username)
	}

	def getUsers(organizationId: Long, users: Seq[String]): Seq[CatalogUser] = {
		catalog.getUsers(organizationId, users)
	}

	def getUsers(userIds: Seq[Long]): Seq[CatalogUser] = {
		catalog.getUsers(userIds)
	}

	def listUsers(organizationId: Long): Seq[CatalogUser] = {
		catalog.listUsers(organizationId)
	}

	def listUsers(organizationId: Long, pattern: String): Seq[CatalogUser] = {
		catalog.listUsers(organizationId, pattern)
	}

	def createGroup(groupDefinition: CatalogGroup, organization: String, ignoreIfExists: Boolean): Unit = {
		catalog.createGroup(groupDefinition, organization, ignoreIfExists)
	}

	def renameGroup(organizationId: Long, organization: String, group: String, newGroup: String, updateBy: Long): Unit = {
		catalog.renameGroup(organizationId, organization, group, newGroup, updateBy)
	}

	def alterGroup(groupDefinition: CatalogGroup): Unit = {
		catalog.alterGroup(groupDefinition)
	}

	def groupExists(organizationId: Long, group: String): Boolean = {
		catalog.groupExists(organizationId, group)
	}

	def dropGroup(organizationId: Long, organization: String, group: String, ignoreIfNotExists: Boolean, cascade: Boolean): Unit = {
		catalog.dropGroup(organizationId, organization, group, ignoreIfNotExists, cascade)
	}

	def getGroup(organizationId: Long, group: String): CatalogGroup = {
		catalog.getGroup(organizationId, group)
	}

	def getGroups(organizationId: Long, groups: Seq[String]): Seq[CatalogGroup] = {
		catalog.getGroups(organizationId, groups)
	}

	def listGroups(organizationId: Long): Seq[CatalogGroup] = {
		catalog.listGroups(organizationId)
	}

	def listGroups(organizationId: Long, pattern: String): Seq[CatalogGroup] = {
		catalog.listGroups(organizationId, pattern)
	}

	def createDatabase(dbDefinition: CatalogDatabase, organization: String, ignoreIfExists: Boolean): Unit = {
		catalog.createDatabase(dbDefinition, organization, ignoreIfExists)
	}

	def renameDatabase(organizationId: Long, organization: String, db: String, newDb: String, updateBy: Long): Unit = {
		catalog.renameDatabase(organizationId, organization, db, newDb, updateBy)
	}

	def alterDatabase(dbDefinition: CatalogDatabase): Unit = {
		catalog.alterDatabase(dbDefinition)
	}

	def databaseExists(organizationId: Long, db: String): Boolean = {
		catalog.databaseExists(organizationId, db)
	}

	def dropDatabase(organizationId: Long, organization: String, database: String, ignoreIfNotExists: Boolean, cascade: Boolean): Unit = {
		catalog.dropDatabase(organizationId, organization, database, ignoreIfNotExists, cascade)
	}

	def getDatabase(organizationId: Long, database: String): CatalogDatabase = {
		catalog.getDatabase(organizationId, database)
	}

	def listDatabase(organizationId: Long): Seq[CatalogDatabase] = {
		catalog.listDatabases(organizationId)
	}

	def listDatabase(organizationId: Long, pattern: String): Seq[CatalogDatabase] = {
		catalog.listDatabases(organizationId, pattern)
	}

	def createTable(tableDefinition: CatalogTable, organization: String, db: String, ignoreIfExists: Boolean): Unit = {
		catalog.createTable(tableDefinition, organization, db, ignoreIfExists)
	}

	def alterTable(tableDefinition: CatalogTable): Unit = {
		catalog.alterTable(tableDefinition)
	}


	def renameTable(databaseId: Long, organization: String, db: String, table: String, newTable: String, updateBy: Long) = {
		catalog.renameTable(databaseId, organization, db, table, newTable, updateBy)
	}

	def dropTable(databaseId: Long, organization: String, db: String, table: String, ignoreIfNotExists: Boolean): Unit = {
		catalog.dropTable(databaseId, organization, db, table, ignoreIfNotExists)
	}

	def getTable(databaseId: Long, table: String): CatalogTable = {
		val database  = catalog.getDatabase(databaseId)
		if (database.isLogical) {
			catalog.getTable(databaseId, table)
		} else {
			val datasys = DataSystem.lookupDataSystem(database.properties)
			CatalogTable(
				name = table,
				description = None,
				databaseId = database.id.get,
				properties = datasys.tableProperties(table),
				createBy = database.createBy,
				createTime = database.createTime,
				updateBy = database.updateBy,
				updateTime = database.updateTime
			)
		}
	}

	def listTables(databaseId: Long): Seq[CatalogTable] = {
		val database = catalog.getDatabase(databaseId)
		if (database.isLogical) {
			catalog.listTables(databaseId)
		} else {
			val datasys = DataSystem.lookupDataSystem(database.properties)
			val tableNames: Seq[String] = datasys.tableNames()
			tableNames.map { name =>
				CatalogTable(
					name = name,
					description = None,
					databaseId = database.id.get,
					properties = datasys.tableProperties(name),
					createBy = database.createBy,
					createTime = database.createTime,
					updateBy = database.updateBy,
					updateTime = database.updateTime
				)
			}
		}
	}

	def listTables(databaseId: Long, pattern: String): Seq[CatalogTable] = {
		val database = catalog.getDatabase(databaseId)
		if (database.isLogical) {
			catalog.listTables(databaseId, pattern)
		} else {
			val datasys = DataSystem.lookupDataSystem(database.properties)
			val tableNames: Seq[String] = datasys.tableNames()
			Utils.filterPattern(tableNames, Utils.escapeLikeRegex(pattern))
			.map { name =>
				CatalogTable(
					name = name,
					description = None,
					databaseId = database.id.get,
					properties = datasys.tableProperties(name),
					createBy = database.createBy,
					createTime = database.createTime,
					updateBy = database.updateBy,
					updateTime = database.updateTime
				)
			}
		}
	}

	def getColumns(databaseId: Long, table: String)(mbSession: MbSession): Seq[CatalogColumn] = {
		val database = catalog.getDatabase(databaseId)
		val tableIdentifier = TableIdentifier(table, Some(database.name))
		if (database.isLogical) {
			val props = catalog.getTable(databaseId, table).properties
			mbSession.mixcal.registerTable(TableIdentifier(table, Some(database.name)), props)
		} else {
			val props = DataSystem.lookupDataSystem(database.properties).tableProperties(table)
			mbSession.mixcal.registerTable(tableIdentifier, props)
		}
		mbSession.mixcal.analyzedLogicalPlan(UnresolvedRelation(tableIdentifier)).schema.map { field =>
			CatalogColumn(
				name = field.name,
				dataType = field.dataType.simpleString,
				databaseId = database.id.get,
				table = table,
				createBy = database.createBy,
				createTime = database.createTime,
				updateBy = database.updateBy,
				updateTime = database.updateTime
			)
		}
	}

	def createFunction(funcDefinition: CatalogFunction, organization: String, db: String, ignoreIfExists: Boolean): Unit = {
		catalog.createFunction(funcDefinition, organization, db, ignoreIfExists)
	}

	def renameFunction(databaseId: Long, organization: String, database: String, func: String, newFunc: String, updateBy: Long): Unit = {
		catalog.renameFunction(databaseId, organization, database, func, newFunc, updateBy)
	}

	def functionExists(databaseId: Long, function: String): Boolean = {
		catalog.functionExists(databaseId, function)
	}

	def dropFunction(databaseId: Long, organization: String, db: String, function: String, ignoreIfNotExists: Boolean): Unit = {
		catalog.dropFunction(databaseId, organization, db, function, ignoreIfNotExists)
	}

	def getFunction(databaseId: Long, function: String): CatalogFunction = {
		catalog.getFunction(databaseId, function)
	}

	def listFunctions(databaseId: Long): Seq[CatalogFunction] = {
		catalog.listFunctions(databaseId)
	}

	def listFunctions(databaseId: Long, pattern: String): Seq[CatalogFunction] = {
		catalog.listFunctions(databaseId, pattern)
	}

	def createView(viewDefinition: CatalogView, organization: String, db: String, ignoreIfExists: Boolean): Unit = {
		catalog.createView(viewDefinition, organization, db, ignoreIfExists)
	}

	def renameView(databaseId: Long, organization: String, db: String, view: String, newView: String, updateBy: Long): Unit = {
		catalog.renameView(databaseId, organization, db, view, newView, updateBy)
	}

	def alterView(viewDefinition: CatalogView): Unit = {
		catalog.alterView(viewDefinition)
	}

	def viewExists(databaseId: Long, view: String): Boolean = {
		catalog.viewExists(databaseId, view)
	}

	def dropView(databaseId: Long, organization: String, db: String, view: String, ignoreIfNotExists: Boolean): Unit = {
		catalog.dropView(databaseId, organization, db, view, ignoreIfNotExists)
	}

	def getView(databaseId: Long, view: String): CatalogView = {
		catalog.getView(databaseId, view)
	}

	def listViews(databaseId: Long): Seq[CatalogView] = {
		catalog.listViews(databaseId)
	}

	def listViews(databaseId: Long, pattern: String): Seq[CatalogView] = {
		catalog.listViews(databaseId, pattern)
	}

	def createProcedure(procDefinition: CatalogProcedure, organization: String, ignoreIfExists: Boolean): Unit = {
		catalog.createProcedure(procDefinition, organization, ignoreIfExists)
	}

	def renameProcedure(organizationId: Long, organization: String, proc: String, newproc: String, updateBy: Long): Unit = {
		catalog.renameProcedure(organizationId, organization, proc, newproc, updateBy)
	}

	def alterProcedure(procDefinition: CatalogProcedure): Unit = {
		catalog.alterProcedure(procDefinition)
	}

	def procedureExists(organizationId: Long, proc: String): Boolean = {
		catalog.procedureExists(organizationId, proc)
	}

	def dropProcedure(organizationId: Long, organization: String, proc: String, ignoreIfNotExists: Boolean): Unit = {
		catalog.dropProcedure(organizationId, organization, proc, ignoreIfNotExists)
	}

	def getProcedure(organizationId: Long, proc: String): CatalogProcedure = {
		catalog.getProcedure(organizationId, proc)
	}

	def getProcedure(procId: Long): CatalogProcedure = {
		catalog.getProcedure(procId)
	}

	def listProcedures(organizationId: Long): Seq[CatalogProcedure] = {
		catalog.listProcedures(organizationId)
	}

	def listProcedures(organizationId: Long, pattern: String): Seq[CatalogProcedure] = {
		catalog.listProcedures(organizationId, pattern)
	}

	def createTimedEvent(eventDefinition: CatalogTimedEvent, organization: String, ignoreIfExists: Boolean): Unit = {
		catalog.createTimedEvent(eventDefinition, organization, ignoreIfExists)
	}

	def renameTimedEvent(organizationId: Long, organization: String, event: String, newEvent: String, updateBy: Long): Unit = {
		catalog.renameTimedEvent(organizationId, organization, event, newEvent, updateBy)
	}

	def alterTimedEvent(eventDefinition: CatalogTimedEvent): Unit = {
		catalog.alterTimedEvent(eventDefinition)
	}

	def timedEventExists(organizationId: Long, event: String): Boolean = {
		catalog.timedEventExists(organizationId, event)
	}

	def dropTimedEvent(organizationId: Long, organization: String, event: String, ignoreIfNotExists: Boolean): Unit = {
		catalog.dropTimedEvent(organizationId, organization, event, ignoreIfNotExists)
	}

	def getTimedEvent(organizationId: Long, event: String): CatalogTimedEvent = {
		catalog.getTimedEvent(organizationId, event)
	}

	def listTimedEvents(organizationId: Long): Seq[CatalogTimedEvent] = {
		catalog.listTimedEvents(organizationId)
	}

	def listTimedEvents(organizationId: Long, pattern: String): Seq[CatalogTimedEvent] = {
		catalog.listTimedEvents(organizationId, pattern)
	}

	def createUserGroupRel(userGroupRels: Seq[CatalogUserGroupRel], organization: String, group: String, users: Seq[String]): Unit = {
		catalog.createUserGroupRel(userGroupRels:_*)(organization, group, users)
	}

	def dropUserGroupRel(groupId: Long, userIds: Seq[Long], organization: String, group: String, users: Seq[String]): Unit = {
		catalog.dropUserGroupRel(groupId, userIds, organization, group, users)
	}

	def getUserGroupRelsByGroup(groupId: Long): Seq[CatalogUserGroupRel] = {
		catalog.getUserGroupRelsByGroup(groupId)
	}

	def createDatabasePrivilege(dbPrivileges: Seq[CatalogDatabasePrivilege], user: String, organization: String, db: String): Unit = {
		catalog.createDatabasePrivilege(dbPrivileges:_*)(user, organization, db)
	}

	def dropDatabasePrivilege(userId: Long, databaseId: Long, privileges: Seq[String], user: String, organization: String, database: String): Unit = {
		catalog.dropDatabasePrivilege(userId, databaseId, privileges:_*)(user, organization, database)
	}

	def getDatabasePrivilege(userId: Long, databaseId: Long, privilege: String): Option[CatalogDatabasePrivilege] = {
		catalog.getDatabasePrivilege(userId, databaseId, privilege)
	}

	def getDatabasePrivilege(userId: Long, databaseId: Long): Seq[CatalogDatabasePrivilege] = {
		catalog.getDatabasePrivilege(userId, databaseId)
	}

	def getDatabasePrivilege(userId: Long): Seq[CatalogDatabasePrivilege] = {
		catalog.getDatabasePrivilege(userId)
	}

	def createTablePrivilege(tablePrivileges: Seq[CatalogTablePrivilege], user: String, organization: String, db: String, table: String): Unit = {
		catalog.createTablePrivilege(tablePrivileges:_*)(user, organization, db, table)
	}

	def dropTablePrivilege(userId: Long, databaseId: Long, table: String, privileges: Seq[String], user: String, organization: String, database: String): Unit = {
		catalog.dropTablePrivilege(userId, databaseId, table, privileges:_*)(user, organization, database)
	}

	def getTablePrivilege(userId: Long, databaseId: Long, table: String, privilege: String): Option[CatalogTablePrivilege] = {
		catalog.getTablePrivilege(userId, databaseId, table, privilege)
	}

	def getTablePrivilege(userId: Long, databaseId: Long, table: String): Seq[CatalogTablePrivilege] = {
		catalog.getTablePrivilege(userId, databaseId, table)
	}

	def getTablePrivilege(userId: Long): Seq[CatalogTablePrivilege] = {
		catalog.getTablePrivilege(userId)
	}

	def createColumnPrivilege(columnPrivileges: Seq[CatalogColumnPrivilege], user: String, organization: String, db: String, table: String): Unit = {
		catalog.createColumnPrivilege(columnPrivileges:_*)(user, organization, db, table)
	}

	def dropColumnPrivilege(userId: Long, databaseId: Long, table: String, privileges: Seq[(String, Seq[String])], user: String, organization: String, database: String): Unit = {
		catalog.dropColumnPrivilege(userId, databaseId, table, privileges)(user, organization, database)
	}

	def getColumnPrivilege(userId: Long, databaseId: Long, table: String, privilege: String): Seq[CatalogColumnPrivilege] = {
		catalog.getColumnPrivilege(userId, databaseId, table, privilege)
	}

	def getColumnPrivilege(userId: Long): Seq[CatalogColumnPrivilege] = {
		catalog.getColumnPrivilege(userId)
	}
}
