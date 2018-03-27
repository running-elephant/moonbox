package moonbox.core.catalog.jdbc

import java.util.concurrent.{CountDownLatch, TimeUnit}

import moonbox.common.MbConf
import moonbox.common.util.Utils
import moonbox.core.catalog._
import moonbox.core.config._
import slick.jdbc.meta.MTable

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

class JdbcDao(override val conf: MbConf) extends EntityComponent {
	import profile.api._

	private val lock = new CountDownLatch(1)
	initialize().onComplete {
		case Success(_) => lock.countDown()
		case Failure(e) => throw e
	}
	lock.await(conf.get(CATALOG_RESULT_AWAIT_TIMEOUT), TimeUnit.MILLISECONDS)

	private def action[R](action: DBIOAction[R, NoStream, Nothing]): Future[R] = {
		database.run(action)
	}

	// -----------------------------------------------------------------
	// Base method
	// -----------------------------------------------------------------

	private def insert[E, T <: BaseTable[E]](entity: E, table: TableQuery[T]): Future[Long] = action {
		(table returning table.map(_.id)) += entity
	}

	private def insertMultiple[E, T <: BaseTable[E]](entity: Seq[E], table: TableQuery[T]): Future[Seq[Long]] = action {
		(table returning table.map(_.id)) ++= entity
	}

	private def delete[E, T <: BaseTable[E]](table: TableQuery[T], filter: T => Rep[Boolean]): Future[Int] = action {
		table.filter(filter).delete
	}

	// Query[+E, U, C[_]]
	// map[F, G, T](f: E => F)(implicit shape: Shape[_ <: FlatShapeLevel, F, T, G]): Query[G, T, C]

	private def updateEntity[E, T <: BaseTable[E]](table: TableQuery[T], condition: T => Rep[Boolean], data: E): Future[Int] = action {
		val update = for { t <- table if condition(t) } yield t
		update.update(data)
	}

	private def update[U, E <: BaseTable[U], F, G, T](table: TableQuery[E],
		condition: E => Rep[Boolean],
		columns: (E) => F, data: T)(implicit shape: Shape[_ <: FlatShapeLevel, F, T, G]): Future[Int] = action {
		val update = for {t <- table if condition(t)} yield columns(t)
		update.update(data)
	}

	private def query[E, T <: BaseTable[E]](table: TableQuery[T], condition: T => Rep[Boolean]): Future[Seq[E]] = action {
		table.filter(condition).result
	}

	private def queryOneOption[E, T <: BaseTable[E]](table: TableQuery[T],
		condition: T => Rep[Boolean]): Future[Option[E]] = query[E, T](table, condition).map(_.headOption)

	private def queryOne[E, T <: BaseTable[E]](table: TableQuery[T],
		condition: T => Rep[Boolean]): Future[E] = query[E, T](table, condition).map(_.head)

	private def list[E, T <: BaseTable[E]](table: TableQuery[T]): Future[Seq[E]] = action {
		table.result
	}

	private def exists[E, T <: BaseTable[E]](table: TableQuery[T], condition: T => Rep[Boolean]): Future[Boolean] = action {
		table.filter(condition).exists.result
	}

	private def initialize(): Future[Boolean] = {
		action(MTable.getTables.map(_.map(_.name.name))).flatMap { exists =>
			val create = tableQuerys.filterNot( tableQuery => exists.contains(tableQuery.shaped.value.tableName)).map(_.schema.create)
			action(DBIO.seq(create:_*)).flatMap { res =>
				this.getUser("ROOT").flatMap {
					case Some(user) => Future(true)
					case None =>
						this.createUser(CatalogUser(
							name = "ROOT",
							password = "123456",
							account = true,
							ddl = true,
							grantAccount = true,
							grantDdl = true,
							grantDmlOn = true,
							isSA = true,
							organizationId = 0,
							createBy = 0,
							updateBy = 0
						)).flatMap(id => Future(true))
				}
			}
		}
	}

	// -----------------------------------------------------------------
	// Organization
	// -----------------------------------------------------------------

	def createOrganization(organization: CatalogOrganization): Future[Long] = {
		insert(organization, catalogOrganizations)
	}

	def deleteOrganization(organizationId: Long): Future[Int] = {
		delete[CatalogOrganization, CatalogOrganizationTable](catalogOrganizations, _.id === organizationId)
	}

	def deleteOrganization(organization: String): Future[Int] = {
		delete[CatalogOrganization, CatalogOrganizationTable](catalogOrganizations, _.name === organization)
	}

	def renameOrganization(organization: String, newOrganization: String)(updateBy: Long): Future[Int] = {
		update[CatalogOrganization, CatalogOrganizationTable,
			(Rep[String], Rep[Long], Rep[Long]), (Rep[String], Rep[Long], Rep[Long]),
			(String, Long, Long)](
			catalogOrganizations, _.name === organization,
			t => (t.name, t.updateBy, t.updateTime), (newOrganization, updateBy, Utils.now))
	}

	def updateOrganization(organizationDefinition: CatalogOrganization): Future[Int] = {
		updateEntity[CatalogOrganization, CatalogOrganizationTable](
			catalogOrganizations, _.id === organizationDefinition.id.get,
			organizationDefinition
		)
	}

	def getOrganization(organizationId: Long): Future[Option[CatalogOrganization]] = {
		queryOneOption[CatalogOrganization, CatalogOrganizationTable](catalogOrganizations, _.id === organizationId)
	}

	def getOrganization(organization: String): Future[Option[CatalogOrganization]] = {
		queryOneOption[CatalogOrganization, CatalogOrganizationTable](catalogOrganizations, _.name === organization)
	}

	def organizationExists(organization: String): Future[Boolean] = {
		exists[CatalogOrganization, CatalogOrganizationTable](catalogOrganizations, _.name === organization)
	}

	def listOrganizations(): Future[Seq[CatalogOrganization]] = {
		list[CatalogOrganization, CatalogOrganizationTable](catalogOrganizations)
	}

	def listOrganizations(pattern: String): Future[Seq[CatalogOrganization]] = {
		query[CatalogOrganization, CatalogOrganizationTable](
			catalogOrganizations, _.name.like(pattern)
		)
	}

	// -----------------------------------------------------------------
	// Group
	// -----------------------------------------------------------------

	def createGroup(group: CatalogGroup): Future[Long] = {
		insert(group, catalogGroups)
	}

	def deleteGroup(groupId: Long): Future[Int] = {
		delete[CatalogGroup, CatalogGroupTable](catalogGroups, _.id === groupId)
	}

	def deleteGroup(organizationId: Long, group: String): Future[Int] = {
		delete[CatalogGroup, CatalogGroupTable](catalogGroups, t => t.organizationId === organizationId && t.name === group)
	}

	def renameGroup(organizationId: Long, group: String, newGroup: String)(updateBy: Long): Future[Int] = {
		update[CatalogGroup, CatalogGroupTable,
			(Rep[String], Rep[Long], Rep[Long]), (Rep[String], Rep[Long], Rep[Long]),
			(String, Long, Long)](
			catalogGroups, t => t.organizationId === organizationId && t.name === group,
			t => (t.name, t.updateBy, t.updateTime), (newGroup, updateBy, Utils.now))
	}

	def updateGroup(groupDefinition: CatalogGroup): Future[Int] = {
		updateEntity[CatalogGroup, CatalogGroupTable](
			catalogGroups, _.id === groupDefinition.id.get,
			groupDefinition
		)
	}

	def getGroup(groupId: Long): Future[Option[CatalogGroup]] = {
		queryOneOption[CatalogGroup, CatalogGroupTable](catalogGroups, _.id === groupId)
	}

	def getGroup(organizationId: Long, group: String): Future[Option[CatalogGroup]] = {
		queryOneOption[CatalogGroup, CatalogGroupTable](catalogGroups,t =>  t.organizationId === organizationId && t.name === group)
	}

	def getGroups(organizationId: Long, groups: Seq[String]): Future[Seq[CatalogGroup]] = {
		query[CatalogGroup, CatalogGroupTable](
			catalogGroups, g => g.organizationId === organizationId && g.name.inSet(groups)
		)
	}

	def groupExists(organizationId: Long, group: String): Future[Boolean] = {
		exists[CatalogGroup, CatalogGroupTable](catalogGroups, t => t.organizationId === organizationId && t.name === group)
	}

	def listGroups(organizationId: Long): Future[Seq[CatalogGroup]] = {
		query[CatalogGroup, CatalogGroupTable](catalogGroups, _.organizationId === organizationId)
	}

	def listGroups(organizationId: Long, pattern: String): Future[Seq[CatalogGroup]] = {
		query[CatalogGroup, CatalogGroupTable](catalogGroups, t => t.organizationId === organizationId && t.name.like(pattern))
	}

	// -----------------------------------------------------------------
	// User
	// -----------------------------------------------------------------

	def createUser(user: CatalogUser): Future[Long] = {
		insert(user, catalogUsers)
	}

	def deleteUser(userId: Long): Future[Int] = {
		delete[CatalogUser, CatalogUserTable](catalogUsers, _.id === userId)
	}

	def deleteUser(user: String): Future[Int] = {
		delete[CatalogUser, CatalogUserTable](catalogUsers, _.name === user)
	}

	def updateUser(user: CatalogUser): Future[Int] = {
		updateEntity[CatalogUser, CatalogUserTable](catalogUsers, _.id === user.id.get, user)
	}

	def renameUser(user: String, newUser: String)(updateBy: Long): Future[Int] = {
		update[CatalogUser, CatalogUserTable,
			(Rep[String], Rep[Long], Rep[Long]), (Rep[String], Rep[Long], Rep[Long]),
			(String, Long, Long)](
			catalogUsers, _.name === user,
			t => (t.name, t.updateBy, t.updateTime), (newUser, updateBy, Utils.now))
	}

	def changePassword(user: String, newPassword: String)(updateBy: Long): Future[Int] = {
		update[CatalogUser, CatalogUserTable,
			(Rep[String], Rep[Long], Rep[Long]), (Rep[String], Rep[Long], Rep[Long]),
			(String, Long, Long)](
			catalogUsers, _.name === user,
			t => (t.password, t.updateBy, t.updateTime),
			(newPassword, updateBy, Utils.now))
	}

	def setUserAccount(account: Boolean, users: Long*)(updateBy: Long): Future[Int] = {
		update[CatalogUser, CatalogUserTable,
			(Rep[Boolean], Rep[Long], Rep[Long]), (Rep[Boolean], Rep[Long], Rep[Long]),
			(Boolean, Long, Long)](
			catalogUsers, _.id inSet users,
			t => (t.account, t.updateBy, t.updateTime),
			(account, updateBy, Utils.now))
	}

	def setUserDdl(ddl: Boolean, users: Long*)(updateBy: Long): Future[Int] = {
		update[CatalogUser, CatalogUserTable,
			(Rep[Boolean], Rep[Long], Rep[Long]), (Rep[Boolean], Rep[Long], Rep[Long]),
			(Boolean, Long, Long)](
			catalogUsers, _.id inSet users,
			t => (t.ddl, t.updateBy, t.updateTime),
			(ddl, updateBy, Utils.now))
	}

	def setUserGrantAccount(grantAccount: Boolean, users: Long*)(updateBy: Long): Future[Int] = {
		update[CatalogUser, CatalogUserTable,
			(Rep[Boolean], Rep[Long], Rep[Long]), (Rep[Boolean], Rep[Long], Rep[Long]),
			(Boolean, Long, Long)](
			catalogUsers, _.id inSet users,
			t => (t.grantAccount, t.updateBy, t.updateTime),
			(grantAccount, updateBy, Utils.now))
	}

	def setUserGrantDdl(grantDdl: Boolean, users: Long*)(updateBy: Long): Future[Int] = {
		update[CatalogUser, CatalogUserTable,
			(Rep[Boolean], Rep[Long], Rep[Long]), (Rep[Boolean], Rep[Long], Rep[Long]),
			(Boolean, Long, Long)](
			catalogUsers, _.id inSet users,
			t => (t.grantDdl, t.updateBy, t.updateTime),
			(grantDdl, updateBy, Utils.now))
	}

	def setUserGrantDmlOn(grantDmlOn: Boolean, users: Long*)(updateBy: Long): Future[Int] = {
		update[CatalogUser, CatalogUserTable,
			(Rep[Boolean], Rep[Long], Rep[Long]), (Rep[Boolean], Rep[Long], Rep[Long]),
			(Boolean, Long, Long)](
			catalogUsers, _.id inSet users,
			t => (t.grantDmlOn, t.updateBy, t.updateTime),
			(grantDmlOn, updateBy, Utils.now))
	}

	def getUser(userId: Long): Future[Option[CatalogUser]] = {
		queryOneOption[CatalogUser, CatalogUserTable](catalogUsers, _.id === userId)
	}

	def getUser(userName: String): Future[Option[CatalogUser]] = {
		queryOneOption[CatalogUser, CatalogUserTable](catalogUsers, _.name === userName)
	}

	def getUser(organizationId: Long, user: String): Future[Option[CatalogUser]] = {
		queryOneOption[CatalogUser, CatalogUserTable](
			catalogUsers, t => t.organizationId === organizationId && t.name === user)
	}

	def getUsers(organizationId: Long, users: Seq[String]): Future[Seq[CatalogUser]] = {
		query[CatalogUser, CatalogUserTable](
			catalogUsers, t => t.organizationId === organizationId && t.name.inSet(users)
		)
	}

	def userExists(user: String): Future[Boolean] = {
		exists[CatalogUser, CatalogUserTable](catalogUsers, _.name === user)
	}

	def userExists(organizationId: Long, user: String): Future[Boolean] = {
		exists[CatalogUser, CatalogUserTable](
			catalogUsers, t => t.organizationId === organizationId && t.name === user)
	}

	def listUsers(organizationId: Long): Future[Seq[CatalogUser]] = {
		query[CatalogUser, CatalogUserTable](catalogUsers,  _.organizationId === organizationId)
	}

	def listUsers(organizationId: Long, pattern: String): Future[Seq[CatalogUser]] = {
		query[CatalogUser, CatalogUserTable](
			catalogUsers,  t => t.organizationId === organizationId && t.name.like(pattern))
	}

	// -----------------------------------------------------------------
	// Datasource
	// -----------------------------------------------------------------

	def createDatasource(datasource: CatalogDatasource): Future[Long] = {
		insert(datasource, catalogDatasources)
	}

	def deleteDatasource(datasourceId: Long): Future[Int] = {
		delete[CatalogDatasource, CatalogDatasourceTable](catalogDatasources, _.id === datasourceId)
	}

	def deleteDatasource(organizationId: Long, datasource: String): Future[Int] = {
		delete[CatalogDatasource, CatalogDatasourceTable](
			catalogDatasources, t => t.organizationId === organizationId && t.name === datasource)
	}

	def renameDatasource(organizationId: Long, datasource: String, newDatasource: String)(updateBy: Long): Future[Int] = {
		update[CatalogDatasource, CatalogDatasourceTable,
			(Rep[String], Rep[Long], Rep[Long]), (Rep[String], Rep[Long], Rep[Long]),
			(String, Long, Long)](
			catalogDatasources, t => t.organizationId === organizationId && t.name === datasource,
			t => (t.name, t.updateBy, t.updateTime),
			(newDatasource, updateBy, Utils.now))
	}

	def updateDatasource(dsDefinition: CatalogDatasource): Future[Int] = {
		updateEntity[CatalogDatasource, CatalogDatasourceTable](
			catalogDatasources, t => t.id === dsDefinition.id.get, dsDefinition
		)
	}

	def getDatasource(datasourceId: Long): Future[Option[CatalogDatasource]] = {
		queryOneOption[CatalogDatasource, CatalogDatasourceTable](catalogDatasources, _.id === datasourceId)
	}

	def getDatasource(organizationId: Long, datasource: String): Future[Option[CatalogDatasource]] = {
		queryOneOption[CatalogDatasource, CatalogDatasourceTable](
			catalogDatasources, t => t.organizationId === organizationId && t.name === datasource)
	}

	def datasourceExists(organizationId: Long, datasource: String): Future[Boolean] = {
		exists[CatalogDatasource, CatalogDatasourceTable](
			catalogDatasources, t => t.organizationId === organizationId && t.name === datasource)
	}

	def listDatasources(organizationId: Long): Future[Seq[CatalogDatasource]] = {
		query[CatalogDatasource, CatalogDatasourceTable](catalogDatasources, _.organizationId === organizationId)
	}

	def listDatasources(organizationId: Long, pattern: String): Future[Seq[CatalogDatasource]] = {
		query[CatalogDatasource, CatalogDatasourceTable](
			catalogDatasources, t => t.organizationId === organizationId && t.name.like(pattern))
	}

	// -----------------------------------------------------------------
	// Application
	// -----------------------------------------------------------------

	def createApplication(application: CatalogApplication): Future[Long] = {
		insert(application, catalogApplications)
	}

	def deleteApplication(applicationId: Long): Future[Int] = {
		delete[CatalogApplication, CatalogApplicationTable](catalogApplications, _.id === applicationId)
	}

	def deleteApplication(organizationId: Long, application: String): Future[Int] = {
		delete[CatalogApplication, CatalogApplicationTable](
			catalogApplications, t => t.organizationId === organizationId && t.name === application)
	}

	def deleteApplications(organizationId: Long): Future[Int] = {
		delete[CatalogApplication, CatalogApplicationTable](
			catalogApplications, _.organizationId === organizationId
		)
	}

	def renameApplication(organizationId: Long, application: String, newApplication: String)(updateBy: Long): Future[Int] = {
		update[CatalogApplication, CatalogApplicationTable,
			(Rep[String], Rep[Long], Rep[Long]), (Rep[String], Rep[Long], Rep[Long]),
			(String, Long, Long)](
			catalogApplications, t => t.organizationId === organizationId && t.name === application,
			t => (t.name, t.updateBy, t.updateTime), (newApplication, updateBy, Utils.now))
	}

	def updateApplication(appDefinition: CatalogApplication): Future[Int] = {
		updateEntity[CatalogApplication, CatalogApplicationTable](
			catalogApplications, t => t.id === appDefinition.id.get, appDefinition
		)
	}

	def getApplication(applicationId: Long): Future[Option[CatalogApplication]] = {
		queryOneOption[CatalogApplication, CatalogApplicationTable](catalogApplications, _.id === applicationId)
	}

	def getApplication(organizationId: Long, application: String): Future[Option[CatalogApplication]] = {
		queryOneOption[CatalogApplication, CatalogApplicationTable](
			catalogApplications, t => t.organizationId === organizationId && t.name === application)
	}

	def applicationExists(organizationId: Long, application: String): Future[Boolean] = {
		exists[CatalogApplication, CatalogApplicationTable](
			catalogApplications, t => t.organizationId === organizationId && t.name === application)
	}

	def listApplications(organizationId: Long): Future[Seq[CatalogApplication]] = {
		query[CatalogApplication, CatalogApplicationTable](catalogApplications, _.organizationId === organizationId)
	}

	def listApplications(organizationId: Long, pattern: String): Future[Seq[CatalogApplication]] = {
		query[CatalogApplication, CatalogApplicationTable](
			catalogApplications, t => t.organizationId === organizationId && t.name.like(pattern))
	}

	// -----------------------------------------------------------------
	// Database
	// -----------------------------------------------------------------

	def createDatabase(database: CatalogDatabase): Future[Long] = {
		insert(database, catalogDatabases)
	}

	def deleteDatabase(databaseId: Long): Future[Int] = {
		delete[CatalogDatabase, CatalogDatabaseTable](catalogDatabases, _.id === databaseId)
	}

	def deleteDatabase(organizationId: Long, database: String): Future[Int] = {
		delete[CatalogDatabase, CatalogDatabaseTable](
			catalogDatabases, t => t.organizationId === organizationId && t.name === database)
	}

	def renameDatabase(organizationId: Long, database: String, newDatabase: String)(updateBy: Long): Future[Int] = {
		update[CatalogDatabase, CatalogDatabaseTable,
			(Rep[String], Rep[Long], Rep[Long]), (Rep[String], Rep[Long], Rep[Long]),
			(String, Long, Long)](
			catalogDatabases, t => t.organizationId === organizationId && t.name === database,
			t => (t.name, t.updateBy, t.updateTime), (newDatabase, updateBy, Utils.now))
	}

	def updateDatabase(dbDefinition: CatalogDatabase): Future[Int] = {
		updateEntity[CatalogDatabase, CatalogDatabaseTable](
			catalogDatabases, t => t.id === dbDefinition.id.get, dbDefinition
		)
	}

	def getDatabase(databaseId: Long): Future[Option[CatalogDatabase]] = {
		queryOneOption[CatalogDatabase, CatalogDatabaseTable](catalogDatabases, _.id === databaseId)
	}

	def getDatabase(organizationId: Long, database: String): Future[Option[CatalogDatabase]] = {
		queryOneOption[CatalogDatabase, CatalogDatabaseTable](
			catalogDatabases, t => t.organizationId === organizationId && t.name === database)
	}

	def databaseExists(organizationId: Long, database: String): Future[Boolean] = {
		exists[CatalogDatabase, CatalogDatabaseTable](
			catalogDatabases, t => t.organizationId === organizationId && t.name === database)
	}

	def listDatabases(organizationId: Long): Future[Seq[CatalogDatabase]] = {
		query[CatalogDatabase, CatalogDatabaseTable](catalogDatabases, _.organizationId === organizationId)
	}

	def listDatabases(organizationId: Long, pattern: String): Future[Seq[CatalogDatabase]] = {
		query[CatalogDatabase, CatalogDatabaseTable](
			catalogDatabases, t => t.organizationId === organizationId && t.name.like(pattern))
	}

	// -----------------------------------------------------------------
	// Table
	// -----------------------------------------------------------------

	def createTable(table: CatalogTable): Future[Long] = {
		insert[CatalogTable, CatalogTableTable](table, catalogTables)
	}

	def deleteTables(databaseId: Long): Future[Int] = {
		delete[CatalogTable, CatalogTableTable](
			catalogTables, _.databaseId === databaseId
		)
	}

	def deleteTable(tableId: Long): Future[Int] = {
		delete[CatalogTable, CatalogTableTable](catalogTables, _.id === tableId)
	}

	def deleteTable(databaseId: Long, table: String): Future[Int] = {
		delete[CatalogTable, CatalogTableTable](catalogTables, t => t.databaseId === databaseId && t.name === table)
	}

	def updateTable(table: CatalogTable): Future[Int] = {
		updateEntity[CatalogTable, CatalogTableTable](
			catalogTables, t => t.id === table.id.get, table
		)
	}

	def renameTable(databaseId: Long, table: String, newTable: String)(updateBy: Long): Future[Int] = {
		update[CatalogTable, CatalogTableTable,
			(Rep[String], Rep[Long], Rep[Long]), (Rep[String], Rep[Long], Rep[Long]),
			(String, Long, Long)](
			catalogTables, t => t.databaseId === databaseId && t.name === table,
			t => (t.name, t.updateBy, t.updateTime), (newTable, updateBy, Utils.now))
	}

	def getTable(tableId: Long): Future[Option[CatalogTable]] = {
		queryOneOption[CatalogTable, CatalogTableTable](catalogTables, _.id === tableId)
	}

	def getTable(databaseId: Long, table: String): Future[Option[CatalogTable]] = {
		queryOneOption[CatalogTable, CatalogTableTable](
			catalogTables, t => t.databaseId === databaseId && t.name === table)
	}

	def tableExists(databaseId: Long, table: String): Future[Boolean] = {
		exists[CatalogTable, CatalogTableTable](
			catalogTables, t => t.databaseId === databaseId && t.name === table
		)
	}

	def listTables(databaseId: Long): Future[Seq[CatalogTable]] = {
		query[CatalogTable, CatalogTableTable](catalogTables, _.databaseId === databaseId)
	}

	def listTables(databaseId: Long, pattern: String): Future[Seq[CatalogTable]] = {
		query[CatalogTable, CatalogTableTable](
			catalogTables, t => t.databaseId === databaseId && t.name.like(pattern))
	}

	// -----------------------------------------------------------------
	// Column
	// -----------------------------------------------------------------

	def createColumns(columns: Seq[CatalogColumn]): Future[Seq[Long]] = {
		require(Utils.allEquals(columns.map(_.tableId)))
		insertMultiple[CatalogColumn, CatalogColumnTable](columns, catalogColumns)
	}

	def deleteColumn(columnId: Long): Future[Int] = {
		delete[CatalogColumn, CatalogColumnTable](
			catalogColumns, _.id === columnId
		)
	}

	def deleteColumn(tableId: Long, column: String): Future[Int] = {
		delete[CatalogColumn, CatalogColumnTable](
			catalogColumns, t => t.tableId === tableId && t.name === column
		)
	}

	def deleteColumns(tableId: Long): Future[Int] = {
		delete[CatalogColumn, CatalogColumnTable](
			catalogColumns, _.tableId === tableId
		)
	}

	def updateColumn(column: CatalogColumn): Future[Int] = {
		updateEntity[CatalogColumn, CatalogColumnTable](
			catalogColumns, t => t.id === column.id.get, column
		)
	}

	def getColumn(columnId: Long): Future[Option[CatalogColumn]] = {
		queryOneOption[CatalogColumn, CatalogColumnTable](
			catalogColumns, _.id === columnId
		)
	}

	def getColumn(tableId: Long, column: String): Future[Option[CatalogColumn]] = {
		queryOneOption[CatalogColumn, CatalogColumnTable](
			catalogColumns, t => t.tableId === tableId && t.name === column
		)
	}

	/*def getColumns(tableId: Long, columns: Seq[Long]): Future[Seq[CatalogColumn]] = {
		query[CatalogColumn, CatalogColumnTable](
			catalogColumns, t => t.tableId === tableId && t.id.inSet(columns)
		)
	}*/

	def getColumns(tableId: Long, columns: Seq[String]): Future[Seq[CatalogColumn]] = {
		query[CatalogColumn, CatalogColumnTable](
			catalogColumns, t => t.tableId === tableId && t.name.inSet(columns)
		)
	}

	def columnExists(tableId: Long, column: String): Future[Boolean] = {
		exists[CatalogColumn, CatalogColumnTable](
			catalogColumns, t => t.tableId === tableId && t.name === column
		)
	}

	def listColumns(tableId: Long): Future[Seq[CatalogColumn]] = {
		query[CatalogColumn, CatalogColumnTable](
			catalogColumns, _.tableId === tableId
		)
	}

	// -----------------------------------------------------------------
	// Function
	// -----------------------------------------------------------------

	def createFunction(function: CatalogFunction): Future[Long] = {
		insert[CatalogFunction, CatalogFunctionTable](
			function, catalogFunctions)
	}

	def deleteFunction(functionId: Long): Future[Int] = {
		delete[CatalogFunction, CatalogFunctionTable](
			catalogFunctions, _.id === functionId
		)
	}

	def deleteFunction(databaseId: Long, function: String): Future[Int] = {
		delete[CatalogFunction, CatalogFunctionTable](
			catalogFunctions, t => t.databaseId === databaseId && t.name === function
		)
	}

	def deleteFunctions(databaseId: Long): Future[Int] = {
		delete[CatalogFunction, CatalogFunctionTable](
			catalogFunctions, _.databaseId === databaseId
		)
	}

	def renameFunction(databaseId: Long, func: String, newFunc: String)(updateBy: Long): Future[Int] = {
		update[CatalogFunction, CatalogFunctionTable,
			(Rep[String], Rep[Long], Rep[Long]), (Rep[String], Rep[Long], Rep[Long]),
			(String, Long, Long)](
			catalogFunctions, t => t.databaseId === databaseId && t.name === func,
			t => (t.name, t.updateBy, t.updateTime), (newFunc, updateBy, Utils.now))
	}

	def updateFunction(funcDefinition: CatalogFunction): Future[Int] = {
		updateEntity[CatalogFunction, CatalogFunctionTable](
			catalogFunctions, t => t.id === funcDefinition.id.get, funcDefinition
		)
	}

	def getFunction(functionId: Long): Future[Option[CatalogFunction]] = {
		queryOneOption[CatalogFunction, CatalogFunctionTable](
			catalogFunctions, _.id === functionId
		)
	}

	def getFunction(databaseId: Long, function: String): Future[Option[CatalogFunction]] = {
		queryOneOption[CatalogFunction, CatalogFunctionTable](
			catalogFunctions, t => t.databaseId === databaseId && t.name === function
		)
	}

	def functionExists(databaseId: Long, function: String): Future[Boolean] = {
		exists[CatalogFunction, CatalogFunctionTable](
			catalogFunctions, t => t.databaseId === databaseId && t.name === function
		)
	}

	def listFunctions(databaseId: Long): Future[Seq[CatalogFunction]] = {
		query[CatalogFunction, CatalogFunctionTable](
			catalogFunctions, _.databaseId === databaseId
		)
	}

	def listFunctions(databaseId: Long, pattern: String): Future[Seq[CatalogFunction]] = {
		query[CatalogFunction, CatalogFunctionTable](
			catalogFunctions, t => t.databaseId === databaseId && t.name.like(pattern)
		)
	}

	// -----------------------------------------------------------------
	// View
	// -----------------------------------------------------------------

	def createView(view: CatalogView): Future[Long] = {
		insert[CatalogView, CatalogViewTable](
			view, catalogViews)
	}

	def deleteView(viewId: Long): Future[Int] = {
		delete[CatalogView, CatalogViewTable](
			catalogViews, _.id === viewId
		)
	}

	def deleteView(databaseId: Long, view: String): Future[Int] = {
		delete[CatalogView, CatalogViewTable](
			catalogViews, t => t.databaseId === databaseId && t.name === view
		)
	}

	def deleteViews(databaseId: Long): Future[Int] = {
		delete[CatalogView, CatalogViewTable](
			catalogViews, _.databaseId === databaseId
		)
	}

	def renameView(databaseId: Long, view: String, newView: String)(updateBy: Long): Future[Int] = {
		update[CatalogView, CatalogViewTable,
			(Rep[String], Rep[Long], Rep[Long]), (Rep[String], Rep[Long], Rep[Long]),
			(String, Long, Long)](
			catalogViews, t => t.databaseId === databaseId && t.name === view,
			t => (t.name, t.updateBy, t.updateTime), (newView, updateBy, Utils.now))
	}

	def updateView(viewDefinition: CatalogView): Future[Int] = {
		updateEntity[CatalogView, CatalogViewTable](
			catalogViews, t => t.id === viewDefinition.id.get, viewDefinition
		)
	}

	def getView(viewId: Long): Future[Option[CatalogView]] = {
		queryOneOption[CatalogView, CatalogViewTable](
			catalogViews, _.id === viewId
		)
	}

	def getView(databaseId: Long, view: String): Future[Option[CatalogView]] = {
		queryOneOption[CatalogView, CatalogViewTable](
			catalogViews, t => t.databaseId === databaseId && t.name === view
		)
	}

	def viewExists(databaseId: Long, view: String): Future[Boolean] = {
		exists[CatalogView, CatalogViewTable](
			catalogViews, t => t.databaseId === databaseId && t.name === view
		)
	}

	def listViews(databaseId: Long): Future[Seq[CatalogView]] = {
		query[CatalogView, CatalogViewTable](
			catalogViews, _.databaseId === databaseId
		)
	}

	def listViews(databaseId: Long, pattern: String): Future[Seq[CatalogView]] = {
		query[CatalogView, CatalogViewTable](
			catalogViews, t => t.databaseId === databaseId && t.name.like(pattern)
		)
	}

	// -----------------------------------------------------------------
	// UserTableRel
	// -----------------------------------------------------------------

	def createUserTableRel(rel: CatalogUserTableRel): Future[Long] = {
		queryOneOption[CatalogUserTableRel, CatalogUserTableRelTable](
			catalogUserTableRels, t => t.userId === rel.userId && t.tableId === rel.tableId
		).flatMap {
			case Some(exists) =>
				val newRel = rel.copy(columns = (exists.columns ++ rel.columns).distinct)
				updateEntity[CatalogUserTableRel, CatalogUserTableRelTable](
					catalogUserTableRels,
					t => t.userId === rel.userId && t.tableId === rel.tableId,
					newRel
				)
				Future(exists.id.get)
			case None =>
				insert[CatalogUserTableRel, CatalogUserTableRelTable](
					rel, catalogUserTableRels
				)
		}
	}

	def deleteUserTableRel(rel: CatalogUserTableRel): Future[Int] = {
		queryOne[CatalogUserTableRel, CatalogUserTableRelTable](
			catalogUserTableRels, t => t.userId === rel.userId && t.tableId === rel.tableId
		).flatMap { exists =>
			val newColumns = exists.columns.diff(rel.columns)
			if (newColumns.isEmpty) {
				delete[CatalogUserTableRel, CatalogUserTableRelTable](
					catalogUserTableRels,
					t => t.userId === rel.userId && t.tableId === rel.tableId
				)
			} else {
				val newRel = rel.copy(columns = newColumns)
				updateEntity[CatalogUserTableRel, CatalogUserTableRelTable](
					catalogUserTableRels,
					t => t.userId === rel.userId && t.tableId === rel.tableId,
					newRel
				)
			}
		}
	}

	def getUserTableRel(userId: Long, tableId: Long): Future[Option[CatalogUserTableRel]] = {
		queryOneOption[CatalogUserTableRel, CatalogUserTableRelTable](
			catalogUserTableRels, t => t.userId === userId && t.tableId === tableId
		)
	}

	def userTableRelExists(userId: Long, tableId: Long): Future[Boolean] = {
		exists[CatalogUserTableRel, CatalogUserTableRelTable](
			catalogUserTableRels, t => t.userId === userId && t.tableId === tableId
		)
	}

	def listUserTableRels(userId: Long): Future[Seq[CatalogUserTableRel]] = {
		query[CatalogUserTableRel, CatalogUserTableRelTable](
			catalogUserTableRels, t => t.userId === userId
		)
	}

	// -----------------------------------------------------------------
	// UserGroupRel
	// -----------------------------------------------------------------

	def createUserGroupRel(rel: CatalogUserGroupRel): Future[Long] = {
		queryOneOption[CatalogUserGroupRel, CatalogUserGroupRelTable](
			catalogUserGroupRels, _.id === rel.groupId
		).flatMap {
			case Some(exists) =>
				val newRel = rel.copy(users = (exists.users ++ rel.users).distinct)
				updateEntity[CatalogUserGroupRel, CatalogUserGroupRelTable](
					catalogUserGroupRels,
					_.groupId === rel.groupId,
					newRel
				)
				Future(exists.id.get)
			case None =>
				insert[CatalogUserGroupRel, CatalogUserGroupRelTable](rel, catalogUserGroupRels)
		}
	}

	def deleteUserGroupRel(rel: CatalogUserGroupRel): Future[Int] = {
		queryOne[CatalogUserGroupRel, CatalogUserGroupRelTable](
			catalogUserGroupRels, _.id === rel.groupId
		).flatMap { exists =>
			val newUsers = exists.users.diff(rel.users)
			if (newUsers.isEmpty) {
				delete[CatalogUserGroupRel, CatalogUserGroupRelTable](
					catalogUserGroupRels, _.groupId === rel.groupId
				)
			} else {
				val newRel = rel.copy(users = newUsers)
				updateEntity[CatalogUserGroupRel, CatalogUserGroupRelTable](
					catalogUserGroupRels,
					_.groupId === rel.groupId,
					newRel
				)
			}
		}
	}

	def getUserGroupRel(groupId: Long): Future[Option[CatalogUserGroupRel]] = {
		queryOneOption[CatalogUserGroupRel, CatalogUserGroupRelTable](
			catalogUserGroupRels, _.groupId === groupId
		)
	}

	def listUserGroupRels(organizationId: Long): Future[Seq[CatalogUserGroupRel]] = {
		listGroups(organizationId).flatMap { groups =>
			val groupIds = groups.map(_.id.get)
			query[CatalogUserGroupRel, CatalogUserGroupRelTable](
				catalogUserGroupRels, t => t.groupId.inSet(groupIds)
			)
		}
	}

	def userGroupRelExists(groupId: Long): Future[Boolean] = {
		exists[CatalogUserGroupRel, CatalogUserGroupRelTable](
			catalogUserGroupRels, _.id === groupId
		)
	}

	def clearUserGroupRels(groupId: Long): Future[Int] = {
		delete[CatalogUserGroupRel, CatalogUserGroupRelTable](
			catalogUserGroupRels, _.groupId === groupId
		)
	}


}



























