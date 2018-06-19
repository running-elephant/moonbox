package moonbox.core

import moonbox.common.{MbConf, MbLogging}
import moonbox.core.catalog._
import org.apache.spark.sql.catalyst.TableIdentifier
import org.apache.spark.sql.catalyst.analysis.UnresolvedRelation
import org.apache.spark.sql.datasys.DataSystemFactory
import org.apache.spark.sql.types.StructType


object CatalogContext {
	val DEFAULT_DATABASE = "default"
}

class CatalogContext(val conf: MbConf, mbSession: MbSession) extends MbLogging {
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

	def canAccount(userId: Long): Boolean = {
		catalog.getUser(userId).account
	}

	def canGrantAccount(userId: Long): Boolean = {
		catalog.getUser(userId).grantAccount
	}

	def canGrantDdl(userId: Long): Boolean = {
		catalog.getUser(userId).grantDdl
	}

	def canGrantDmlOn(userId: Long): Boolean = {
		catalog.getUser(userId).grantDmlOn
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
			val datasys = DataSystemFactory.getInstance(database.properties, mbSession.mixcal.sparkSession)
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

	/*def tableExists(databaseId: Long, table: String): Boolean = {
		// TODO
		catalog.tableExists(databaseId, table)
	}*/

	def listTables(databaseId: Long): Seq[CatalogTable] = {
		val database = catalog.getDatabase(databaseId)
		if (database.isLogical) {
			catalog.listTables(databaseId)
		} else {
			val datasys = DataSystemFactory.getInstance(database.properties, mbSession.mixcal.sparkSession)
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
		// TODO pattern
		val database = catalog.getDatabase(databaseId)
		if (database.isLogical) {
			catalog.listTables(databaseId, pattern)
		} else {
			val datasys = DataSystemFactory.getInstance(database.properties, mbSession.mixcal.sparkSession)
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

	def getColumns(databaseId: Long, table: String): Seq[CatalogColumn] = {
		val database = catalog.getDatabase(databaseId)
		val tableIdentifier = TableIdentifier(table, Some(database.name))
		if (database.isLogical) {
			val props = catalog.getTable(databaseId, table).properties
			mbSession.mixcal.registerTable(TableIdentifier(table, Some(database.name)), props)
		} else {
			val props = DataSystemFactory.getInstance(database.properties, mbSession.mixcal.sparkSession).tableProperties(table)
			mbSession.mixcal.registerTable(tableIdentifier, props)
		}
		mbSession.mixcal.analyzedLogicalPlan(UnresolvedRelation(tableIdentifier)).schema.map { field =>
			CatalogColumn(
				name = field.name,
				dataType = field.dataType.toString,
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

	def alterFunction(funcDefinition: CatalogFunction): Unit = {
		catalog.alterFunction(funcDefinition)
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

	def createApplication(appDefinition: CatalogApplication, organization: String, ignoreIfExists: Boolean): Unit = {
		catalog.createApplication(appDefinition, organization, ignoreIfExists)
	}

	def renameApplication(organizationId: Long, organization: String, app: String, newApp: String, updateBy: Long): Unit = {
		catalog.renameApplication(organizationId, organization, app, newApp, updateBy)
	}

	def alterApplication(appDefinition: CatalogApplication): Unit = {
		catalog.alterApplication(appDefinition)
	}

	def applicationExists(organizationId: Long, application: String): Boolean = {
		catalog.applicationExists(organizationId, application)
	}

	def dropApplication(organizationId: Long, organization: String, application: String, ignoreIfNotExists: Boolean): Unit = {
		catalog.dropApplication(organizationId, organization, application, ignoreIfNotExists)
	}

	def getApplication(organizationId: Long, application: String): CatalogApplication = {
		catalog.getApplication(organizationId, application)
	}

	def listApplications(organizationId: Long): Seq[CatalogApplication] = {
		catalog.listApplications(organizationId)
	}

	def listApplications(organizationId: Long, pattern: String): Seq[CatalogApplication] = {
		catalog.listApplications(organizationId, pattern)
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

	def createUserTableRel(userTableRels: Seq[CatalogUserTableRel], user: String, organization: String, db: String, table: String): Unit = {
		catalog.createUserTableRel(userTableRels)(user, organization, db, table)
	}

	/*def createUserPhysicalTableRel(userTableRels: Seq[CatalogUserPhysicalTableRel], user: String, organization: String, db: String, table: String): Unit = {
		catalog.createUserPhysicalTableRel(userTableRels)(user, organization, db, table)
	}*/

	/*def dropUserLogicalRels(userId: Long, tableId: Long, columns: Seq[String], user: String, organization: String, db: String, table: String): Unit = {
		catalog.dropUserLogicalTableRels(userId, tableId, columns)(user, organization, db, table)
	}*/

	def dropUserTableRel(userId: Long, databaseId: Long, table: String, columns: Seq[String], user: String, organization: String, db: String) = {
		catalog.dropUserTableRels(userId, databaseId, table, columns)(user, organization, db)
	}

	/*def dropUserLogicalTableRels(tableId: Long)(organization: String, database: String, table: String): Unit = {
		catalog.dropUserLogicalTableRels(tableId)(organization, database, table)
	}

	def dropUserPhysicalTableRels(databaseId: Long, table: String)(organization: String, database: String): Unit = {
		catalog.dropUserPhysicalTableRels(databaseId, table)(organization, database)
	}*/

	/*def getUserLogicalTableRels(userId: Long, tableId: Long): Seq[CatalogUserLogicalTableRel] = {
		catalog.getUserLogicalTableRels(userId, tableId)
	}*/

	def getUserTableRels(userId: Long, databaseId: Long, table: String): Seq[CatalogUserTableRel] = {
		catalog.getUserTableRels(userId, databaseId, table)
	}

	/*def createColumns(columnDefinition: Seq[CatalogColumn], ignoreIfExists: Boolean) = {
		catalog.createColumns(columnDefinition, ignoreIfExists)
	}

	def getColumns(columns: Seq[Long]): Seq[CatalogColumn] = {
		catalog.getColumns(columns)
	}

	def getColumns(tableId: Long): Seq[CatalogColumn] = {
		catalog.getColumns(tableId)
	}*/
}
