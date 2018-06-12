package moonbox.core.catalog

import moonbox.common.util.ListenerBus
import org.apache.spark.sql.types.StructType

abstract class AbstractCatalog extends ListenerBus[CatalogEventListener, CatalogEvent] {

	// ----------------------------------------------------------------------------
	// Organization
	// ----------------------------------------------------------------------------

	final def createOrganization(orgDefinition: CatalogOrganization, ignoreIfExists: Boolean): Unit = {
		val org = orgDefinition.name
		postToAll(CreateOrganizationPreEvent(org))
		doCreateOrganization(orgDefinition, ignoreIfExists)
		postToAll(CreateOrganizationEvent(org))
	}

	final def dropOrganization(org: String, ignoreIfNotExists: Boolean, cascade: Boolean): Unit = {
		postToAll(DropOrganizationPreEvent(org))
		doDropOrganization(org, ignoreIfNotExists, cascade)
		postToAll(DropOrganizationEvent(org))
	}

	final def renameOrganization(org: String, newOrg: String, updateBy: Long): Unit = {
		postToAll(RenameOrganizationPreEvent(org, newOrg))
		doRenameOrganization(org, newOrg, updateBy)
		postToAll(RenameOrganizationEvent(org, newOrg))
	}

	protected def doCreateOrganization(orgDefinition: CatalogOrganization, ignoreIfExists: Boolean): Unit

	protected def doDropOrganization(org: String, ignoreIfNotExists: Boolean, cascade: Boolean): Unit

	protected def doRenameOrganization(org: String, newOrg: String, updateBy: Long): Unit

	def organizationName(organizationId: Long): String = getOrganization(organizationId).name

	def alterOrganization(orgDefinition: CatalogOrganization): Unit

	def getOrganization(org: String): CatalogOrganization

	def getOrganization(org: Long): CatalogOrganization

	def getOrganizationOption(org: String): Option[CatalogOrganization]

	def getOrganizationOption(org: Long): Option[CatalogOrganization]

	def organizationExists(org: String): Boolean

	def listOrganizations(): Seq[CatalogOrganization]

	def listOrganizations(pattern: String): Seq[CatalogOrganization]

	// ----------------------------------------------------------------------------
	// Group -- belong to organization
	// ----------------------------------------------------------------------------

	final def createGroup(groupDefinition: CatalogGroup, organization: String, ignoreIfExists: Boolean): Unit = {
		val group = groupDefinition.name
		postToAll(CreateGroupPreEvent(organization, group))
		doCreateGroup(groupDefinition, ignoreIfExists)
		postToAll(CreateGroupEvent(organization, group))
	}

	protected def doCreateGroup(groupDefinition: CatalogGroup, ignoreIfExists: Boolean): Unit

	final def dropGroup(organizationId: Long, organization: String , group: String, ignoreIfNotExists: Boolean, cascade: Boolean): Unit = {
		postToAll(DropGroupPreEvent(organization, group))
		doDropGroup(organizationId, group, ignoreIfNotExists, cascade)
		postToAll(DropGroupEvent(organization, group))
	}

	protected def doDropGroup(organizationId: Long, group: String, ignoreIfNotExists: Boolean, cascade: Boolean): Unit

	final def renameGroup(organizationId: Long, organization: String, group: String, newGroup: String, updateBy: Long): Unit = {
		postToAll(RenameGroupPreEvent(organization, group, newGroup))
		doRenameGroup(organizationId, group, newGroup, updateBy)
		postToAll(RenameGroupEvent(organization, group, newGroup))
	}

	protected def doRenameGroup(organizationId: Long, group: String, newGroup: String, updateBy: Long): Unit

	def groupName(groupId: Long): String = getGroup(groupId).name

	def alterGroup(groupDefinition: CatalogGroup): Unit

	def getGroup(organizationId: Long, group: String): CatalogGroup

	def getGroup(groupId: Long): CatalogGroup

	def getGroups(organizationId: Long, groups: Seq[String]): Seq[CatalogGroup]

	def getGroupOption(organizationId: Long, group: String): Option[CatalogGroup]

	def getGroupOption(groupId: Long): Option[CatalogGroup]

	def groupExists(organizationId: Long, group: String): Boolean

	def listGroups(organizationId: Long): Seq[CatalogGroup]

	def listGroups(organizationId: Long, pattern: String): Seq[CatalogGroup]

	// ----------------------------------------------------------------------------
	// User -- belong to organization
	// ----------------------------------------------------------------------------

	final def createUser(userDefinition: CatalogUser, organization: String, ignoreIfExists: Boolean): Unit = {
		val user = userDefinition.name
		postToAll(CreateUserPreEvent(organization, user))
		doCreateUser(userDefinition, ignoreIfExists)
		postToAll(CreateUserEvent(organization, user))
	}

	protected def doCreateUser(userDefinition: CatalogUser, ignoreIfExists: Boolean): Unit

	final def dropUser(organizationId: Long, organization: String, user: String, ignoreIfNotExists: Boolean): Unit = {
		postToAll(DropUserPreEvent(organization, user))
		doDropUser(organizationId, organization, user, ignoreIfNotExists)
		postToAll(DropUserEvent(organization, user))
	}

	protected def doDropUser(organizationId: Long, organization: String, user: String, ignoreIfNotExists: Boolean): Unit

	final def renameUser(organizationId: Long, organization: String, user: String, newUser: String, updateBy: Long): Unit = {
		postToAll(RenameUserPreEvent(organization, user, newUser))
		doRenameUser(organizationId, user, newUser, updateBy)
		postToAll(RenameUserEvent(organization, user, newUser))
	}

	protected def doRenameUser(organizationId: Long, user: String, newUser: String, updateBy: Long): Unit

	def alterUser(userDefinition: CatalogUser): Unit

	def getUser(organizationId: Long, user: String): CatalogUser

	def getUsers(organizationId: Long, users: Seq[String]): Seq[CatalogUser]

	def getUsers(userIds: Seq[Long]): Seq[CatalogUser]

	def getUser(user: Long): CatalogUser

	def getUserOption(username: String): Option[CatalogUser]

	def getUserOption(organizationId: Long, user: String): Option[CatalogUser]

	def getUserOption(user: Long): Option[CatalogUser]

	def userExists(organizationId: Long, user: String): Boolean

	def listUsers(organizationId: Long): Seq[CatalogUser]

	def listUsers(organizationId: Long, pattern: String): Seq[CatalogUser]

	// ----------------------------------------------------------------------------
	// Datasource -- belong to organization
	// ----------------------------------------------------------------------------

	final def createDatasource(dsDefinition: CatalogDatasource, organization: String, ignoreIfExists: Boolean): Unit = {
		val ds = dsDefinition.name
		postToAll(CreateDatasourcePreEvent(organization, ds))
		doCreateDatasource(dsDefinition, ignoreIfExists)
		postToAll(CreateDatasourceEvent(organization, ds))
	}

	protected def doCreateDatasource(dsDefinition: CatalogDatasource, ignoreIfExists: Boolean): Unit

	final def dropDatasource(organizationId: Long, organization: String, ds: String, ignoreIfNotExists: Boolean): Unit = {
		postToAll(DropDatasourcePreEvent(organization, ds))
		doDropDatasource(organizationId, ds, ignoreIfNotExists)
		postToAll(DropDatasourceEvent(organization, ds))
	}

	protected def doDropDatasource(organizationId: Long, ds: String, ignoreIfNotExists: Boolean): Unit

	final def renameDatasource(organizationId: Long, organization: String, ds: String, newDs: String, updateBy: Long): Unit = {
		postToAll(RenameDatasourcePreEvent(organization, ds, newDs))
		doRenameDatasource(organizationId, ds, newDs, updateBy)
		postToAll(RenameDatasourceEvent(organization, ds, newDs))
	}

	protected def doRenameDatasource(organizationId: Long, ds: String, newDs: String, updateBy: Long): Unit

	def alterDatasource(dsDefinition: CatalogDatasource): Unit

	def getDatasource(organizationId: Long, ds: String): CatalogDatasource

	def getDatasource(ds: Long): CatalogDatasource

	def getDatasourceOption(organizationId: Long, ds: String): Option[CatalogDatasource]

	def getDatasourceOption(ds: Long): Option[CatalogDatasource]

	def datasourceExists(organizationId: Long, ds: String): Boolean

	def listDatasources(organizationId: Long): Seq[CatalogDatasource]

	def listDatasources(organizationId: Long, pattern: String): Seq[CatalogDatasource]

	// ----------------------------------------------------------------------------
	// Application -- belong to organization
	// ----------------------------------------------------------------------------

	final def createApplication(appDefinition: CatalogApplication, organization: String, ignoreIfExists: Boolean): Unit = {
		val app = appDefinition.name
		postToAll(CreateApplicationPreEvent(organization, app))
		doCreateApplication(appDefinition, ignoreIfExists)
		postToAll(CreateApplicationEvent(organization, app))
	}

	protected def doCreateApplication(appDefinition: CatalogApplication, ignoreIfExists: Boolean): Unit

	final def dropApplication(organizationId: Long, organization: String, app: String, ignoreIfNotExists: Boolean): Unit = {
		postToAll(DropApplicationPreEvent(organization, app))
		doDropApplication(organizationId, app, ignoreIfNotExists)
		postToAll(DropApplicationEvent(organization, app))
	}

	protected def doDropApplication(organizationId: Long, app: String, ignoreIfNotExists: Boolean): Unit

	final def renameApplication(organizationId: Long, organization: String, app: String, newApp: String, updateBy: Long): Unit = {
		postToAll(RenameApplicationPreEvent(organization, app, newApp))
		doRenameApplication(organizationId, app, newApp, updateBy)
		postToAll(RenameApplicationEvent(organization, app, newApp))
	}

	protected def doRenameApplication(organizationId: Long, app: String, newApp: String, updateBy: Long): Unit

	def alterApplication(appDefinition: CatalogApplication): Unit

	def getApplication(organizationId: Long, app: String): CatalogApplication

	def getApplication(app: Long): CatalogApplication

	def getApplicationOption(organizationId: Long, app: String): Option[CatalogApplication]

	def getApplicationOption(app: Long): Option[CatalogApplication]

	def applicationExists(organizationId: Long, app: String): Boolean

	def listApplications(organizationId: Long): Seq[CatalogApplication]

	def listApplications(organizationId: Long, pattern: String): Seq[CatalogApplication]

	// ----------------------------------------------------------------------------
	// timedevent -- belong to organization
	// ----------------------------------------------------------------------------
	final def createTimedEvent(eventDefinition: CatalogTimedEvent, organization: String, ignoreIfExists: Boolean): Unit = {
		val event = eventDefinition.name
		postToAll(CreateTimedEventPreEvent(organization, event))
		doCreateTimedEvent(eventDefinition, ignoreIfExists)
		postToAll(CreateTimedEventEvent(organization, event))
	}
	protected def doCreateTimedEvent(eventDefinition: CatalogTimedEvent, ignoreIfExists: Boolean): Unit

	final def renameTimedEvent(organizationId: Long, organization: String, event: String, newEvent: String, updateBy: Long): Unit = {
		postToAll(RenameTimedEventPreEvent(organization, event))
		doRenameTimedEvent(organizationId, event, newEvent, updateBy)
		postToAll(RenameTimedEventEvent(organization, event))
	}

	protected def doRenameTimedEvent(organizationId: Long, event: String, newEvent: String, updateBy: Long): Unit

	final def dropTimedEvent(organizationId: Long, organization: String, event: String, ignoreIfNotExists: Boolean): Unit = {
		postToAll(DropTimedEventPreEvent(organization, event))
		doDropTimedEvent(organizationId, event, ignoreIfNotExists)
		postToAll(DropTimedEventEvent(organization, event))
	}

	protected def doDropTimedEvent(organizationId: Long, event: String, ignoreIfNotExists: Boolean): Unit

	def alterTimedEvent(eventDefinition: CatalogTimedEvent): Unit

	def timedEventExists(organizationId: Long, event: String): Boolean

	def getTimedEvent(organizationId: Long, event: String): CatalogTimedEvent

	def listTimedEvents(organizationId: Long): Seq[CatalogTimedEvent]

	def listTimedEvents(organizationId: Long, pattern: String): Seq[CatalogTimedEvent]



	// ----------------------------------------------------------------------------
	// Database -- belong to organization
	// ----------------------------------------------------------------------------

	final def createDatabase(dbDefinition: CatalogDatabase, organization: String, ignoreIfExists: Boolean): Unit = {
		val db = dbDefinition.name
		postToAll(CreateDatabasePreEvent(organization, db))
		doCreateDatabase(dbDefinition, ignoreIfExists)
		postToAll(CreateDatabaseEvent(organization, db))
	}

	protected def doCreateDatabase(dbDefinition: CatalogDatabase, ignoreIfExists: Boolean): Unit

	final def dropDatabase(organizationId: Long, organization: String, db: String, ignoreIfNotExists: Boolean, cascade: Boolean): Unit = {
		postToAll(DropDatabasePreEvent(organization, db))
		doDropDatabase(organizationId, db, ignoreIfNotExists, cascade)
		postToAll(DropDatabaseEvent(organization, db))
	}

	protected def doDropDatabase(organizationId: Long, db: String, ignoreIfNotExists: Boolean, cascade: Boolean): Unit

	final def renameDatabase(organizationId: Long, organization: String, db: String, newDb: String, updateBy: Long): Unit = {
		postToAll(RenameDatabasePreEvent(organization, db, newDb))
		doRenameDatabase(organizationId, db, newDb, updateBy)
		postToAll(RenameDatabaseEvent(organization, db, newDb))
	}

	protected def doRenameDatabase(organizationId: Long, db: String, newDb: String, updateBy: Long): Unit

	def databaseName(databaseId: Long): String = getDatabase(databaseId).name

	def databaseOrganization(databaseId: Long): Long = getDatabase(databaseId).organizationId

	def alterDatabase(dbDefinition: CatalogDatabase): Unit

	def getDatabase(organizationId: Long, db: String): CatalogDatabase

	def getDatabase(id: Long): CatalogDatabase

	def getDatabaseOption(organizationId: Long, db: String): Option[CatalogDatabase]

	def getDatabaseOption(id: Long): Option[CatalogDatabase]

	def databaseExists(organizationId: Long, db: String): Boolean

	def listDatabases(organizationId: Long): Seq[CatalogDatabase]

	def listDatabases(organizationId: Long, pattern: String): Seq[CatalogDatabase]

	// ----------------------------------------------------------------------------
	// Table -- belong to database
	// ----------------------------------------------------------------------------

	final def createTable(tableDefinition: CatalogTable, columns: StructType, organization: String, db: String, ignoreIfExists: Boolean): Unit = {
		val table = tableDefinition.name
		postToAll(CreateTablePreEvent(organization, db, table))
		doCreateTable(tableDefinition, columns, ignoreIfExists)
		postToAll(CreateTableEvent(organization, db, table))
	}
	protected def doCreateTable(tableDefinition: CatalogTable, columns: StructType, ignoreIfExists: Boolean): Unit

	final def dropTable(databaseId: Long, organization: String, db: String, table: String, ignoreIfNotExists: Boolean): Unit = {
		postToAll(DropTablePreEvent(organization, db, table))
		doDropTable(databaseId, table, ignoreIfNotExists)
		postToAll(DropTableEvent(organization, db, table))
	}

	protected def doDropTable(databaseId: Long, table: String, ignoreIfNotExists: Boolean): Unit

	final def renameTable(databaseId: Long, organization: String, db: String, table: String, newTable: String, updateBy: Long): Unit = {
		postToAll(RenameTablePreEvent(organization, db, table, newTable))
		doRenameTable(databaseId, table, newTable, updateBy)
		postToAll(RenameTableEvent(organization, db, table, newTable))
	}

	protected def doRenameTable(databaseId: Long, table: String, newTable: String, updateBy: Long): Unit

	def alterTable(tableDefinition: CatalogTable): Unit

	def getTable(databaseId: Long, table: String): CatalogTable

	def getTable(table: Long): CatalogTable

	def getTableOption(databaseId: Long, table: String): Option[CatalogTable]

	def getTableOption(table: Long): Option[CatalogTable]

	def tableExists(databaseId: Long, table: String): Boolean

	def listTables(databaseId: Long): Seq[CatalogTable]

	def listTables(databaseId: Long, pattern: String): Seq[CatalogTable]

	// ----------------------------------------------------------------------------
	// Column -- belong to table
	// ----------------------------------------------------------------------------

	protected def createColumns(columnDefinition: Seq[CatalogColumn], ignoreIfExists: Boolean): Seq[Long]

	protected def dropColumn(tableId: Long, column: String, ignoreIfNotExists: Boolean): Unit

	protected def dropColumns(tableId: Long): Unit

	def alterColumn(columnDefinition: CatalogColumn): Unit

	def getColumn(tableId: Long, column: String): CatalogColumn

	def getColumn(column: Long): CatalogColumn

	def getColumnOption(tableId: Long, column: String): Option[CatalogColumn]

	def getColumnOption(column: Long): Option[CatalogColumn]

	def getColumns(columns: Seq[Long]): Seq[CatalogColumn]

	def getColumns(tableId: Long, columns: Seq[String]): Seq[CatalogColumn]

	def getColumns(tableId: Long): Seq[CatalogColumn]

	def columnExists(tableId: Long, column: String): Boolean

	def listColumns(tableId: Long): Seq[CatalogColumn]

	// ----------------------------------------------------------------------------
	// Function -- belong to database
	// ----------------------------------------------------------------------------

	final def createFunction(funcDefinition: CatalogFunction, organization: String, db: String, ignoreIfExists: Boolean): Unit = {
		val function = funcDefinition.name
		postToAll(CreateFunctionPreEvent(organization, db, function))
		doCreateFunction(funcDefinition, ignoreIfExists)
		postToAll(CreateFunctionEvent(organization, db, function))
	}

	protected def doCreateFunction(funcDefinition: CatalogFunction, ignoreIfExists: Boolean): Unit

	final def dropFunction(databaseId: Long, organization: String, db: String, func: String, ignoreIfNotExists: Boolean): Unit = {
		postToAll(DropFunctionPreEvent(organization, db, func))
		doDropFunction(databaseId, func, ignoreIfNotExists)
		postToAll(DropFunctionEvent(organization, db, func))
	}

	protected def doDropFunction(databaseId: Long, func: String, ignoreIfNotExists: Boolean): Unit

	final def renameFunction(databaseId: Long, organization: String, db: String, func: String, newFunc: String, updateBy: Long): Unit = {
		postToAll(RenameFunctionPreEvent(organization, db, func, newFunc))
		doRenameFunction(databaseId, func, newFunc, updateBy)
		postToAll(RenameFunctionEvent(organization, db, func, newFunc))
	}

	protected def doRenameFunction(databaseId: Long, func: String, newFunc: String, updateBy: Long): Unit

	def alterFunction(funcDefinition: CatalogFunction): Unit

	def getFunction(databaseId: Long, func: String): CatalogFunction

	def getFunction(func: Long): CatalogFunction

	def getFunctionOption(databaseId: Long, func: String): Option[CatalogFunction]

	def getFunctionOption(func: Long): Option[CatalogFunction]

	def functionExists(databaseId: Long, func: String): Boolean

	def listFunctions(databaseId: Long): Seq[CatalogFunction]

	def listFunctions(databaseId: Long, pattern: String): Seq[CatalogFunction]

	// ----------------------------------------------------------------------------
	// View -- belong to database
	// ----------------------------------------------------------------------------

	final def createView(viewDefinition: CatalogView, organization: String, db: String, ignoreIfExists: Boolean): Unit = {
		val view = viewDefinition.name
		postToAll(CreateViewPreEvent(organization, db, view))
		doCreateView(viewDefinition, ignoreIfExists)
		postToAll(CreateViewEvent(organization, db, view))
	}

	protected def doCreateView(viewDefinition: CatalogView, ignoreIfExists: Boolean): Unit

	final def dropView(databaseId: Long, organization: String, db: String, view: String, ignoreIfNotExists: Boolean): Unit = {
		postToAll(DropViewPreEvent(organization, db, view))
		doDropView(databaseId, view, ignoreIfNotExists)
		postToAll(DropViewEvent(organization, db, view))
	}

	protected def doDropView(databaseId: Long, view: String, ignoreIfNotExists: Boolean): Unit

	final def renameView(databaseId: Long, organization: String, db: String, view: String, newView: String, updateBy: Long): Unit = {
		postToAll(RenameViewPreEvent(organization, db, view, newView))
		doRenameView(databaseId, view, newView, updateBy)
		postToAll(RenameViewEvent(organization, db, view, newView))
	}

	protected def doRenameView(databaseId: Long, view: String, newView: String, updateBy: Long): Unit

	def alterView(viewDefinition: CatalogView): Unit

	def getView(databaseId: Long, view: String): CatalogView

	def getView(view: Long): CatalogView

	def getViewOption(databaseId: Long, view: String): Option[CatalogView]

	def getViewOption(view: Long): Option[CatalogView]

	def viewExists(databaseId: Long, view: String): Boolean

	def listViews(databaseId: Long): Seq[CatalogView]

	def listViews(databaseId: Long, pattern: String): Seq[CatalogView]

	// ----------------------------------------------------------------------------
	// UserGroupRel --   the relation of user - group
	// ----------------------------------------------------------------------------

	final def createUserGroupRel(userGroupRels: CatalogUserGroupRel*)(organization: String, group: String, users: Seq[String]): Unit = {
		postToAll(CreateUserGroupRelPreEvent(organization, group, users))
		doCreateUserGroupRel(userGroupRels:_*)
		postToAll(CreateUserGroupRelEvent(organization, group, users))
	}

	protected def doCreateUserGroupRel(userGroupRels: CatalogUserGroupRel*): Unit

	final def dropUserGroupRelByGroup(groupId: Long, organization: String, group: String, users: Seq[String]): Unit = {
		postToAll(DropUserGroupRelByGroupPreEvent(organization, group, users))
		doDropUserGroupRelByGroup(groupId)
		postToAll(DropUserGroupRelByGroupEvent(organization, group, users))
	}

	protected def doDropUserGroupRelByGroup(groupId: Long): Unit

	final def dropUserGroupRelByUser(userId: Long, organization: String, user: String, groups: Seq[String]): Unit = {
		postToAll(DropUserGroupRelByUserPreEvent(organization, user, groups))
		doDropUserGroupRelByUser(userId)
		postToAll(DropUserGroupRelByUserEvent(organization, user, groups))
	}

	protected def doDropUserGroupRelByUser(userId: Long): Unit

	final def dropUserGroupRel(groupId: Long, userIds: Seq[Long], organization: String, group: String, users: Seq[String]): Unit = {
		postToAll(DropUserGroupRelPreEvent(organization, group, users))
		doDropUserGroupRel(groupId, userIds)
		postToAll(DropUserGroupRelPreEvent(organization, group, users))
	}

	protected def doDropUserGroupRel(groupId: Long, userIds: Seq[Long]): Unit

	def getUserGroupRelsByGroup(groupId: Long): Seq[CatalogUserGroupRel]

	def getUserGroupRelsByUser(userId: Long): Seq[CatalogUserGroupRel]


	// ----------------------------------------------------------------------------
	// UserTableRel --   the relation of user - table - column
	// ----------------------------------------------------------------------------

	final def createUserTableRel(userTableRelDefinition: Seq[CatalogUserTableRel])(
								  user: String, organization: String, db: String, table: String, columnNames: Seq[String]): Unit = {
		postToAll(CreateUserTableRelPreEvent(organization, user, db, table, columnNames))
		doCreateUserTableRel(userTableRelDefinition)
		postToAll(CreateUserTableRelEvent(organization, user, db, table, columnNames))
	}

	protected def doCreateUserTableRel(userTableRelDefinition: Seq[CatalogUserTableRel]): Unit


	final def dropUserTableRels(userId: Long, tableId: Long, columnIds: Seq[Long])
							   (user: String, organization: String, db: String, table: String, columnNames: Seq[String]): Unit = {
		postToAll(DropUserTableRelPreEvent(organization, user, db, table, columnNames))
		doDropUserTableRels(userId, tableId, columnIds)
		postToAll(DropUserTableRelPreEvent(organization, user, db, table, columnNames))
	}

	protected def doDropUserTableRels(userId: Long, tableId: Long, columnIds: Seq[Long]): Unit

	def getUserTableRel(userId: Long, tableId: Long): Seq[CatalogUserTableRel]

	def userTableRelExists(userId: Long, tableId: Long): Boolean



	override protected def doPostEvent(listener: CatalogEventListener, event: CatalogEvent): Unit = {
		listener.onEvent(event)
	}
}
