package moonbox.core

import moonbox.common.{MbConf, MbLogging}
import moonbox.core.catalog._
import org.apache.spark.sql.types.StructType


object CatalogContext {
	val DEFAULT_DATABASE = "default"
}

class CatalogContext(val conf: MbConf) extends MbLogging {
	private val catalog = new JdbcCatalog(conf)

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

	def createDatasource(dsDefinition: CatalogDatasource, organization: String, ignoreIfExists: Boolean): Unit = {
		catalog.createDatasource(dsDefinition, organization, ignoreIfExists)
	}

	def renameDatasource(organizationId: Long, organization: String, ds: String, newDs: String, updateBy: Long): Unit = {
		catalog.renameDatasource(organizationId, organization, ds, newDs, updateBy)
	}

	def alterDatasource(dsDefinition: CatalogDatasource): Unit = {
		catalog.alterDatasource(dsDefinition)
	}

	def datasourceExists(organizationId: Long, ds: String): Boolean = {
		catalog.datasourceExists(organizationId, ds)
	}

	def dropDatasource(organizationId: Long, organization: String, datasource: String, ignoreIfNotExists: Boolean): Unit = {
		catalog.dropDatasource(organizationId, organization, datasource, ignoreIfNotExists)
	}

	def getDatasource(organizationId: Long, datasource: String): CatalogDatasource = {
		catalog.getDatasource(organizationId, datasource)
	}

	def listDatasource(organizationId: Long): Seq[CatalogDatasource] = {
		catalog.listDatasources(organizationId)
	}

	def listDatasource(organizationId: Long, pattern: String): Seq[CatalogDatasource] = {
		catalog.listDatasources(organizationId, pattern)
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

	def createTable(tableDefinition: CatalogTable, columns: StructType, organization: String, db: String, ignoreIfExists: Boolean): Unit = {
		catalog.createTable(tableDefinition, columns, organization, db, ignoreIfExists)
	}

	def alterTable(tableDefinition: CatalogTable): Unit = {
		catalog.alterTable(tableDefinition)
	}

	def tableExists(databaseId: Long, table: String): Boolean = {
		catalog.tableExists(databaseId, table)
	}

	def renameTable(databaseId: Long, organization: String, db: String, table: String, newTable: String, updateBy: Long) = {
		catalog.renameTable(databaseId, organization, db, table, newTable, updateBy)
	}

	def dropTable(databaseId: Long, organization: String, db: String, table: String, ignoreIfNotExists: Boolean): Unit = {
		catalog.dropTable(databaseId, organization, db, table, ignoreIfNotExists)
	}

	def getTable(databaseId: Long, table: String): CatalogTable = {
		catalog.getTable(databaseId, table)
	}

	def listTables(databaseId: Long): Seq[CatalogTable] = {
		catalog.listTables(databaseId)
	}

	def listTables(databaseId: Long, pattern: String): Seq[CatalogTable] = {
		catalog.listTables(databaseId, pattern)
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

	def createScheduler(schedulerDefinition: CatalogScheduler, organization: String, ignoreIfExists: Boolean): Unit = {
		catalog.createScheduler(schedulerDefinition, organization, ignoreIfExists)
	}

	def renameScheduler(organizationId: Long, organization: String, scheduler: String, newScheduler: String, updateBy: Long): Unit = {
		catalog.renameScheduler(organizationId, organization, scheduler, newScheduler, updateBy)
	}

	def alterScheduler(schedulerDefinition: CatalogScheduler): Unit = {
		catalog.alterScheduler(schedulerDefinition)
	}

	def schedulerExists(organizationId: Long, scheduler: String): Boolean = {
		catalog.schedulerExists(organizationId, scheduler)
	}

	def dropScheduler(organizationId: Long, organization: String, scheduler: String, ignoreIfNotExists: Boolean): Unit = {
		catalog.dropScheduler(organizationId, organization, scheduler, ignoreIfNotExists)
	}

	def getScheduler(organizationId: Long, scheduler: String): CatalogScheduler = {
		catalog.getScheduler(organizationId, scheduler)
	}

	def listSchedulers(organizationId: Long): Seq[CatalogScheduler] = {
		catalog.listSchedulers(organizationId)
	}

	def listSchedulers(organizationId: Long, pattern: String): Seq[CatalogScheduler] = {
		catalog.listSchedulers(organizationId, pattern)
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

	def createUserTableRel(userTableRel: Seq[CatalogUserTableRel], user: String, organization: String, db: String, table: String, columns: Seq[String]): Unit = {
		catalog.createUserTableRel(userTableRel)(user, organization, db, table, columns)
	}

	def getUserTableRel(userId: Long, tableId: Long): Seq[CatalogUserTableRel] = {
		catalog.getUserTableRel(userId, tableId)
	}

	def dropUserTableRel(userId: Long, tableId: Long, columnIds: Seq[Long], user: String, organization: String, db: String, table: String, columns: Seq[String]) = {
		catalog.dropUserTableRels(userId, tableId, columnIds)(user, organization, db, table, columns)
	}


	def createColumns(columnDefinition: Seq[CatalogColumn], ignoreIfExists: Boolean) = {
		catalog.createColumns(columnDefinition, ignoreIfExists)
	}

	def getColumns(columns: Seq[Long]): Seq[CatalogColumn] = {
		catalog.getColumns(columns)
	}

	def getColumns(tableId: Long): Seq[CatalogColumn] = {
		catalog.getColumns(tableId)
	}
}
