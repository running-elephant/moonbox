package moonbox.core.catalog.jdbc

import java.util.concurrent.{CountDownLatch, TimeUnit}

import moonbox.common.MbConf
import moonbox.common.util.Utils
import moonbox.core.catalog._
import moonbox.core.config._
import slick.dbio.Effect.{Read, Write}
import slick.jdbc.meta.MTable
import slick.sql.{FixedSqlAction, FixedSqlStreamingAction, SqlAction}

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

	def close(): Unit = {
		//do not need close this dao connection, because all users use one same database connection
	}

	def action[R](action: DBIOAction[R, NoStream, Nothing]): Future[R] = {
		database.run(action)
	}

	def actionTransactionally[R](transactionAction: DBIOAction[R, NoStream, Effect.All]): Future[R] = {
		database.run(transactionAction.transactionally)
	}

	// -----------------------------------------------------------------
	// Base method
	// -----------------------------------------------------------------

	private def insert[E, T <: BaseTable[E]](entity: E, table: TableQuery[T]): FixedSqlAction[Long, NoStream, Write] = {
		(table returning table.map(_.id)) += entity
	}

	private def insertMultiple[E, T <: BaseTable[E]](entity: Seq[E], table: TableQuery[T]): FixedSqlAction[Seq[Long], NoStream, Write] = {
		(table returning table.map(_.id)) ++= entity
	}

	private def delete[E, T <: BaseTable[E]](table: TableQuery[T], filter: T => Rep[Boolean]): FixedSqlAction[Int, NoStream, Write] = {
		table.filter(filter).delete
	}

	// Query[+E, U, C[_]]
	// map[F, G, T](f: E => F)(implicit shape: Shape[_ <: FlatShapeLevel, F, T, G]): Query[G, T, C]

	private def updateEntity[E, T <: BaseTable[E]](table: TableQuery[T], condition: T => Rep[Boolean], data: E): FixedSqlAction[Int, NoStream, Write] = {
		val update = for { t <- table if condition(t) } yield t
		update.update(data)
	}

	private def update[U, E <: BaseTable[U], F, G, T](table: TableQuery[E],
		condition: E => Rep[Boolean],
		columns: (E) => F, data: T)(implicit shape: Shape[_ <: FlatShapeLevel, F, T, G]): FixedSqlAction[Int, NoStream, Write] = {
		val update = for {t <- table if condition(t)} yield columns(t)
		update.update(data)
	}

	private def query[E, T <: BaseTable[E]](table: TableQuery[T], condition: T => Rep[Boolean]): FixedSqlStreamingAction[Seq[E], E, Read] = {
		table.filter(condition).result
	}

	private def queryOneOption[E, T <: BaseTable[E]](table: TableQuery[T],
		condition: T => Rep[Boolean]): SqlAction[Option[E], NoStream, Read] = {
		query[E, T](table, condition).headOption
	}

	private def queryOne[E, T <: BaseTable[E]](table: TableQuery[T],
		condition: T => Rep[Boolean]): SqlAction[E, NoStream, Read] = {
		query[E, T](table, condition).head
	}

	private def list[E, T <: BaseTable[E]](table: TableQuery[T]): FixedSqlStreamingAction[Seq[E], E, Read] =  {
		table.result
	}

	private def exists[E, T <: BaseTable[E]](table: TableQuery[T], condition: T => Rep[Boolean]): FixedSqlAction[Boolean, profile.API#NoStream, Read] = {
		table.filter(condition).exists.result
	}

	private def initialize(): Future[Boolean] = {
		action(MTable.getTables.map(_.map(_.name.name))).flatMap { exists =>
			val create = tableQuerys.filterNot(tableQuery => exists.contains(tableQuery.shaped.value.tableName)).map(_.schema.create)
			action(DBIO.seq(create:_*)).flatMap { res =>
				action(getUser("ROOT")).flatMap {
					case Some(user) => Future(true)
					case None =>
						action(createUser(CatalogUser(
							name = "ROOT",
							password = "123456",
							account = true,
							ddl = true,
							grantAccount = true,
							grantDdl = true,
							grantDmlOn = true,
							isSA = true,
							organizationId = -1,
							createBy = -1,
							updateBy = -1
						))).map(_ => true)
				}
			}
		}
	}

	// -----------------------------------------------------------------
	// Organization
	// -----------------------------------------------------------------

	def createOrganization(organization: CatalogOrganization) = {
		insert(organization, catalogOrganizations)
	}

	def deleteOrganization(organizationId: Long) = {
		delete[CatalogOrganization, CatalogOrganizationTable](catalogOrganizations, _.id === organizationId)
	}

	def deleteOrganization(organization: String) = {
		delete[CatalogOrganization, CatalogOrganizationTable](catalogOrganizations, _.name === organization)
	}

	def renameOrganization(organization: String, newOrganization: String)(updateBy: Long) = {
		update[CatalogOrganization, CatalogOrganizationTable,
			(Rep[String], Rep[Long], Rep[Long]), (Rep[String], Rep[Long], Rep[Long]),
			(String, Long, Long)](
			catalogOrganizations, _.name === organization,
			t => (t.name, t.updateBy, t.updateTime), (newOrganization, updateBy, Utils.now))
	}

	def updateOrganization(organizationDefinition: CatalogOrganization) = {
		updateEntity[CatalogOrganization, CatalogOrganizationTable](
			catalogOrganizations, _.id === organizationDefinition.id.get,
			organizationDefinition
		)
	}

	def getOrganization(organizationId: Long) = {
		queryOneOption[CatalogOrganization, CatalogOrganizationTable](catalogOrganizations, _.id === organizationId)
	}

	def getOrganization(organization: String) = {
		queryOneOption[CatalogOrganization, CatalogOrganizationTable](catalogOrganizations, _.name === organization)
	}

	def organizationExists(organization: String) = {
		exists[CatalogOrganization, CatalogOrganizationTable](catalogOrganizations, _.name === organization)
	}

	def listOrganizations() = {
		list[CatalogOrganization, CatalogOrganizationTable](catalogOrganizations)
	}

	def listOrganizations(pattern: String) = {
		query[CatalogOrganization, CatalogOrganizationTable](
			catalogOrganizations, _.name.like(pattern)
		)
	}

	// -----------------------------------------------------------------
	// Group
	// -----------------------------------------------------------------

	def createGroup(group: CatalogGroup) = {
		insert(group, catalogGroups)
	}

	def deleteGroup(groupId: Long) = {
		delete[CatalogGroup, CatalogGroupTable](catalogGroups, _.id === groupId)
	}

	def deleteGroup(organizationId: Long, group: String) = {
		delete[CatalogGroup, CatalogGroupTable](catalogGroups, t => t.organizationId === organizationId && t.name === group)
	}

	def deleteGroups(organizationId: Long) = {
		delete[CatalogGroup, CatalogGroupTable](catalogGroups, t => t.organizationId === organizationId)
	}

	def renameGroup(organizationId: Long, group: String, newGroup: String)(updateBy: Long) = {
		update[CatalogGroup, CatalogGroupTable,
			(Rep[String], Rep[Long], Rep[Long]), (Rep[String], Rep[Long], Rep[Long]),
			(String, Long, Long)](
			catalogGroups, t => t.organizationId === organizationId && t.name === group,
			t => (t.name, t.updateBy, t.updateTime), (newGroup, updateBy, Utils.now))
	}

	def updateGroup(groupDefinition: CatalogGroup) = {
		updateEntity[CatalogGroup, CatalogGroupTable](
			catalogGroups, _.id === groupDefinition.id.get,
			groupDefinition
		)
	}

	def getGroup(groupId: Long) = {
		queryOneOption[CatalogGroup, CatalogGroupTable](catalogGroups, _.id === groupId)
	}

	def getGroup(organizationId: Long, group: String) = {
		queryOneOption[CatalogGroup, CatalogGroupTable](catalogGroups,t =>  t.organizationId === organizationId && t.name === group)
	}

	def getGroups(organizationId: Long, groups: Seq[String]) = {
		query[CatalogGroup, CatalogGroupTable](
			catalogGroups, g => g.organizationId === organizationId && g.name.inSet(groups)
		)
	}

	def groupExists(organizationId: Long, group: String) = {
		exists[CatalogGroup, CatalogGroupTable](catalogGroups, t => t.organizationId === organizationId && t.name === group)
	}

	def listGroups(organizationId: Long) = {
		query[CatalogGroup, CatalogGroupTable](catalogGroups, _.organizationId === organizationId)
	}

	def listGroups(organizationId: Long, pattern: String) = {
		query[CatalogGroup, CatalogGroupTable](catalogGroups, t => t.organizationId === organizationId && t.name.like(pattern))
	}

	// -----------------------------------------------------------------
	// User
	// -----------------------------------------------------------------

	def createUser(user: CatalogUser) = {
		insert(user, catalogUsers)
	}

	def deleteUser(userId: Long) = {
		delete[CatalogUser, CatalogUserTable](catalogUsers, _.id === userId)
	}

	def deleteUser(user: String) = {
		delete[CatalogUser, CatalogUserTable](catalogUsers, _.name === user)
	}

	def deleteUsers(organizationId: Long) = {
		delete[CatalogUser, CatalogUserTable](catalogUsers, _.organizationId === organizationId)
	}

	def updateUser(user: CatalogUser) = {
		updateEntity[CatalogUser, CatalogUserTable](catalogUsers, _.id === user.id.get, user)
	}

	def renameUser(user: String, newUser: String)(updateBy: Long) = {
		update[CatalogUser, CatalogUserTable,
			(Rep[String], Rep[Long], Rep[Long]), (Rep[String], Rep[Long], Rep[Long]),
			(String, Long, Long)](
			catalogUsers, _.name === user,
			t => (t.name, t.updateBy, t.updateTime), (newUser, updateBy, Utils.now))
	}

	def changePassword(user: String, newPassword: String)(updateBy: Long) = {
		update[CatalogUser, CatalogUserTable,
			(Rep[String], Rep[Long], Rep[Long]), (Rep[String], Rep[Long], Rep[Long]),
			(String, Long, Long)](
			catalogUsers, _.name === user,
			t => (t.password, t.updateBy, t.updateTime),
			(newPassword, updateBy, Utils.now))
	}

	def setUserAccount(account: Boolean, users: Long*)(updateBy: Long) = {
		update[CatalogUser, CatalogUserTable,
			(Rep[Boolean], Rep[Long], Rep[Long]), (Rep[Boolean], Rep[Long], Rep[Long]),
			(Boolean, Long, Long)](
			catalogUsers, _.id inSet users,
			t => (t.account, t.updateBy, t.updateTime),
			(account, updateBy, Utils.now))
	}

	def setUserDdl(ddl: Boolean, users: Long*)(updateBy: Long) = {
		update[CatalogUser, CatalogUserTable,
			(Rep[Boolean], Rep[Long], Rep[Long]), (Rep[Boolean], Rep[Long], Rep[Long]),
			(Boolean, Long, Long)](
			catalogUsers, _.id inSet users,
			t => (t.ddl, t.updateBy, t.updateTime),
			(ddl, updateBy, Utils.now))
	}

	def setUserGrantAccount(grantAccount: Boolean, users: Long*)(updateBy: Long) = {
		update[CatalogUser, CatalogUserTable,
			(Rep[Boolean], Rep[Long], Rep[Long]), (Rep[Boolean], Rep[Long], Rep[Long]),
			(Boolean, Long, Long)](
			catalogUsers, _.id inSet users,
			t => (t.grantAccount, t.updateBy, t.updateTime),
			(grantAccount, updateBy, Utils.now))
	}

	def setUserGrantDdl(grantDdl: Boolean, users: Long*)(updateBy: Long) = {
		update[CatalogUser, CatalogUserTable,
			(Rep[Boolean], Rep[Long], Rep[Long]), (Rep[Boolean], Rep[Long], Rep[Long]),
			(Boolean, Long, Long)](
			catalogUsers, _.id inSet users,
			t => (t.grantDdl, t.updateBy, t.updateTime),
			(grantDdl, updateBy, Utils.now))
	}

	def setUserGrantDmlOn(grantDmlOn: Boolean, users: Long*)(updateBy: Long) = {
		update[CatalogUser, CatalogUserTable,
			(Rep[Boolean], Rep[Long], Rep[Long]), (Rep[Boolean], Rep[Long], Rep[Long]),
			(Boolean, Long, Long)](
			catalogUsers, _.id inSet users,
			t => (t.grantDmlOn, t.updateBy, t.updateTime),
			(grantDmlOn, updateBy, Utils.now))
	}

	def getUser(userId: Long) = {
		queryOneOption[CatalogUser, CatalogUserTable](catalogUsers, _.id === userId)
	}

	def getUser(userName: String) = {
		queryOneOption[CatalogUser, CatalogUserTable](catalogUsers, _.name === userName)
	}

	def getUser(organizationId: Long, user: String) = {
		queryOneOption[CatalogUser, CatalogUserTable](
			catalogUsers, t => t.organizationId === organizationId && t.name === user)
	}

	def getUsers(organizationId: Long, users: Seq[String]) = {
		query[CatalogUser, CatalogUserTable](
			catalogUsers, t => t.organizationId === organizationId && t.name.inSet(users)
		)
	}

	def getUsers(userIds: Seq[Long]) = {
		query[CatalogUser, CatalogUserTable](
			catalogUsers, t =>  t.id.inSet(userIds)
		)
	}

	def userExists(user: String) = {
		exists[CatalogUser, CatalogUserTable](catalogUsers, _.name === user)
	}

	def userExists(organizationId: Long, user: String) = {
		exists[CatalogUser, CatalogUserTable](
			catalogUsers, t => t.organizationId === organizationId && t.name === user)
	}

	def listUsers(organizationId: Long) = {
		query[CatalogUser, CatalogUserTable](catalogUsers,  _.organizationId === organizationId)
	}

	def listUsers(organizationId: Long, pattern: String) = {
		query[CatalogUser, CatalogUserTable](
			catalogUsers,  t => t.organizationId === organizationId && t.name.like(pattern))
	}

	// -----------------------------------------------------------------
	// Datasource
	// -----------------------------------------------------------------

	/*def createDatasource(datasource: CatalogDatasource) = {
		insert(datasource, catalogDatasources)
	}

	def deleteDatasource(datasourceId: Long) = {
		delete[CatalogDatasource, CatalogDatasourceTable](catalogDatasources, _.id === datasourceId)
	}

	def deleteDatasource(organizationId: Long, datasource: String) = {
		delete[CatalogDatasource, CatalogDatasourceTable](
			catalogDatasources, t => t.organizationId === organizationId && t.name === datasource)
	}

	def renameDatasource(organizationId: Long, datasource: String, newDatasource: String)(updateBy: Long) = {
		update[CatalogDatasource, CatalogDatasourceTable,
			(Rep[String], Rep[Long], Rep[Long]), (Rep[String], Rep[Long], Rep[Long]),
			(String, Long, Long)](
			catalogDatasources, t => t.organizationId === organizationId && t.name === datasource,
			t => (t.name, t.updateBy, t.updateTime),
			(newDatasource, updateBy, Utils.now))
	}

	def updateDatasource(dsDefinition: CatalogDatasource) = {
		updateEntity[CatalogDatasource, CatalogDatasourceTable](
			catalogDatasources, t => t.id === dsDefinition.id.get, dsDefinition
		)
	}

	def getDatasource(datasourceId: Long) = {
		queryOneOption[CatalogDatasource, CatalogDatasourceTable](catalogDatasources, _.id === datasourceId)
	}

	def getDatasource(organizationId: Long, datasource: String) = {
		queryOneOption[CatalogDatasource, CatalogDatasourceTable](
			catalogDatasources, t => t.organizationId === organizationId && t.name === datasource)
	}

	def datasourceExists(organizationId: Long, datasource: String) = {
		exists[CatalogDatasource, CatalogDatasourceTable](
			catalogDatasources, t => t.organizationId === organizationId && t.name === datasource)
	}

	def listDatasources(organizationId: Long) = {
		query[CatalogDatasource, CatalogDatasourceTable](catalogDatasources, _.organizationId === organizationId)
	}

	def listDatasources(organizationId: Long, pattern: String) = {
		query[CatalogDatasource, CatalogDatasourceTable](
			catalogDatasources, t => t.organizationId === organizationId && t.name.like(pattern))
	}*/

	// -----------------------------------------------------------------
	// Application
	// -----------------------------------------------------------------

	def createApplication(application: CatalogApplication) = {
		insert(application, catalogApplications)
	}

	def deleteApplication(applicationId: Long) = {
		delete[CatalogApplication, CatalogApplicationTable](catalogApplications, _.id === applicationId)
	}

	def deleteApplication(organizationId: Long, application: String) = {
		delete[CatalogApplication, CatalogApplicationTable](
			catalogApplications, t => t.organizationId === organizationId && t.name === application)
	}

	def deleteApplications(organizationId: Long) = {
		delete[CatalogApplication, CatalogApplicationTable](
			catalogApplications, _.organizationId === organizationId
		)
	}

	def renameApplication(organizationId: Long, application: String, newApplication: String)(updateBy: Long) = {
		update[CatalogApplication, CatalogApplicationTable,
			(Rep[String], Rep[Long], Rep[Long]), (Rep[String], Rep[Long], Rep[Long]),
			(String, Long, Long)](
			catalogApplications, t => t.organizationId === organizationId && t.name === application,
			t => (t.name, t.updateBy, t.updateTime), (newApplication, updateBy, Utils.now))
	}

	def updateApplication(appDefinition: CatalogApplication) = {
		updateEntity[CatalogApplication, CatalogApplicationTable](
			catalogApplications, t => t.id === appDefinition.id.get, appDefinition
		)
	}

	def getApplication(applicationId: Long) = {
		queryOneOption[CatalogApplication, CatalogApplicationTable](catalogApplications, _.id === applicationId)
	}

	def getApplication(organizationId: Long, application: String) = {
		queryOneOption[CatalogApplication, CatalogApplicationTable](
			catalogApplications, t => t.organizationId === organizationId && t.name === application)
	}

	def applicationExists(organizationId: Long, application: String) = {
		exists[CatalogApplication, CatalogApplicationTable](
			catalogApplications, t => t.organizationId === organizationId && t.name === application)
	}

	def listApplications(organizationId: Long) = {
		query[CatalogApplication, CatalogApplicationTable](catalogApplications, _.organizationId === organizationId)
	}

	def listApplications(organizationId: Long, pattern: String) = {
		query[CatalogApplication, CatalogApplicationTable](
			catalogApplications, t => t.organizationId === organizationId && t.name.like(pattern))
	}

	// -----------------------------------------------------------------
	// timedevent
	// -----------------------------------------------------------------
	def createTimedEvent(event: CatalogTimedEvent) = {
		insert(event, catalogTimedEvents)
	}

	def deleteTimedEvent(eventId: Long) = {
		delete[CatalogTimedEvent, CatalogTimedEventTable](
			catalogTimedEvents, _.id === eventId
		)
	}

	def deleteTimedEvent(organizationId: Long, event: String) = {
		delete[CatalogTimedEvent, CatalogTimedEventTable](
			catalogTimedEvents, t => t.organizationId === organizationId && t.name === event
		)
	}

	def renameTimedEvent(organizationId: Long, event: String, newEvent: String)(updateBy: Long) = {
		update[CatalogTimedEvent, CatalogTimedEventTable,
			(Rep[String], Rep[Long], Rep[Long]), (Rep[String], Rep[Long], Rep[Long]),
			(String, Long, Long)](
			catalogTimedEvents, t => t.organizationId === organizationId && t.name === event,
			t => (t.name, t.updateBy, t.updateTime), (newEvent, updateBy, Utils.now))
	}

	def updateTimedEvent(eventDefinition: CatalogTimedEvent) = {
		updateEntity[CatalogTimedEvent, CatalogTimedEventTable](
			catalogTimedEvents, t => t.id === eventDefinition.id.get, eventDefinition
		)
	}

	def getTimedEvent(organizationId: Long, event: String) = {
		queryOneOption[CatalogTimedEvent, CatalogTimedEventTable](
			catalogTimedEvents, t => t.organizationId === organizationId && t.name === event
		)
	}

	def timedEventExists(organizationId: Long, event: String) = {
		exists[CatalogTimedEvent, CatalogTimedEventTable](
			catalogTimedEvents, t => t.organizationId === organizationId && t.name === event
		)
	}

	def listTimedEvents(organizationId: Long) = {
		query[CatalogTimedEvent, CatalogTimedEventTable](
			catalogTimedEvents, t => t.organizationId === organizationId
		)
	}

	def listTimedEvents(organizationId: Long, pattern: String) = {
		query[CatalogTimedEvent, CatalogTimedEventTable](
			catalogTimedEvents, t => t.organizationId === organizationId && t.name.like(pattern)
		)
	}

	// -----------------------------------------------------------------
	// Database
	// -----------------------------------------------------------------

	def createDatabase(database: CatalogDatabase) = {
		insert(database, catalogDatabases)
	}

	def deleteDatabase(databaseId: Long) = {
		delete[CatalogDatabase, CatalogDatabaseTable](catalogDatabases, _.id === databaseId)
	}

	def deleteDatabase(organizationId: Long, database: String) = {
		delete[CatalogDatabase, CatalogDatabaseTable](
			catalogDatabases, t => t.organizationId === organizationId && t.name === database)
	}

	def deleteDatabases(organizationId: Long) = {
		delete[CatalogDatabase, CatalogDatabaseTable](
			catalogDatabases, t => t.organizationId === organizationId)
	}

	def renameDatabase(organizationId: Long, database: String, newDatabase: String)(updateBy: Long) = {
		update[CatalogDatabase, CatalogDatabaseTable,
			(Rep[String], Rep[Long], Rep[Long]), (Rep[String], Rep[Long], Rep[Long]),
			(String, Long, Long)](
			catalogDatabases, t => t.organizationId === organizationId && t.name === database,
			t => (t.name, t.updateBy, t.updateTime), (newDatabase, updateBy, Utils.now))
	}

	def updateDatabase(dbDefinition: CatalogDatabase) = {
		updateEntity[CatalogDatabase, CatalogDatabaseTable](
			catalogDatabases, t => t.id === dbDefinition.id.get, dbDefinition
		)
	}

	def getDatabase(databaseId: Long) = {
		queryOneOption[CatalogDatabase, CatalogDatabaseTable](catalogDatabases, _.id === databaseId)
	}

	def getDatabase(organizationId: Long, database: String) = {
		queryOneOption[CatalogDatabase, CatalogDatabaseTable](
			catalogDatabases, t => t.organizationId === organizationId && t.name === database)
	}

	def databaseExists(organizationId: Long, database: String) = {
		exists[CatalogDatabase, CatalogDatabaseTable](
			catalogDatabases, t => t.organizationId === organizationId && t.name === database)
	}

	def listDatabases(organizationId: Long) = {
		query[CatalogDatabase, CatalogDatabaseTable](catalogDatabases, _.organizationId === organizationId)
	}

	def listDatabases(organizationId: Long, pattern: String) = {
		query[CatalogDatabase, CatalogDatabaseTable](
			catalogDatabases, t => t.organizationId === organizationId && t.name.like(pattern))
	}

	// -----------------------------------------------------------------
	// Table
	// -----------------------------------------------------------------

	def createTable(table: CatalogTable) = {
		insert[CatalogTable, CatalogTableTable](table, catalogTables)
	}

	def deleteTables(databaseId: Long) = {
		delete[CatalogTable, CatalogTableTable](
			catalogTables, _.databaseId === databaseId
		)
	}

	def deleteTable(tableId: Long) = {
		delete[CatalogTable, CatalogTableTable](catalogTables, _.id === tableId)
	}

	def deleteTable(databaseId: Long, table: String) = {
		delete[CatalogTable, CatalogTableTable](catalogTables, t => t.databaseId === databaseId && t.name === table)
	}

	def updateTable(table: CatalogTable) = {
		updateEntity[CatalogTable, CatalogTableTable](
			catalogTables, t => t.id === table.id.get, table
		)
	}

	def renameTable(databaseId: Long, table: String, newTable: String)(updateBy: Long) = {
		update[CatalogTable, CatalogTableTable,
			(Rep[String], Rep[Long], Rep[Long]), (Rep[String], Rep[Long], Rep[Long]),
			(String, Long, Long)](
			catalogTables, t => t.databaseId === databaseId && t.name === table,
			t => (t.name, t.updateBy, t.updateTime), (newTable, updateBy, Utils.now))
	}

	def getTable(tableId: Long) = {
		queryOneOption[CatalogTable, CatalogTableTable](catalogTables, _.id === tableId)
	}

	def getTable(databaseId: Long, table: String) = {
		queryOneOption[CatalogTable, CatalogTableTable](
			catalogTables, t => t.databaseId === databaseId && t.name === table)
	}

	def tableExists(databaseId: Long, table: String) = {
		exists[CatalogTable, CatalogTableTable](
			catalogTables, t => t.databaseId === databaseId && t.name === table
		)
	}

	def listTables(databaseId: Long) = {
		query[CatalogTable, CatalogTableTable](catalogTables, _.databaseId === databaseId)
	}

	def listTables(databaseId: Long, pattern: String) = {
		query[CatalogTable, CatalogTableTable](
			catalogTables, t => t.databaseId === databaseId && t.name.like(pattern))
	}

	// -----------------------------------------------------------------
	// Column
	// -----------------------------------------------------------------
	/*
		def createColumns(columns: Seq[CatalogColumn]) = {
			require(Utils.allEquals(columns.map(_.tableId).toList))
			insertMultiple[CatalogColumn, CatalogColumnTable](columns, catalogColumns)
		}

		def deleteColumn(columnId: Long) = {
			delete[CatalogColumn, CatalogColumnTable](
				catalogColumns, _.id === columnId
			)
		}

		def deleteColumn(tableId: Long, column: String) = {
			delete[CatalogColumn, CatalogColumnTable](
				catalogColumns, t => t.tableId === tableId && t.name === column
			)
		}

		def deleteColumns(tableId: Long) = {
			delete[CatalogColumn, CatalogColumnTable](
				catalogColumns, _.tableId === tableId
			)
		}

		def updateColumn(column: CatalogColumn) = {
			updateEntity[CatalogColumn, CatalogColumnTable](
				catalogColumns, t => t.id === column.id.get, column
			)
		}

		def getColumn(columnId: Long) = {
			queryOneOption[CatalogColumn, CatalogColumnTable](
				catalogColumns, _.id === columnId
			)
		}

		def getColumn(tableId: Long, column: String) = {
			queryOneOption[CatalogColumn, CatalogColumnTable](
				catalogColumns, t => t.tableId === tableId && t.name === column
			)
		}

		def getColumns(columns: Seq[Long]) = {
			query[CatalogColumn, CatalogColumnTable](
				catalogColumns, t => t.id.inSet(columns)
			)
		}

		def getColumns(tableId: Long) = {
			query[CatalogColumn, CatalogColumnTable](
				catalogColumns, t => t.tableId === tableId
			)
		}

		def getColumns(tableId: Long, columns: Seq[String]) = {
			query[CatalogColumn, CatalogColumnTable](
				catalogColumns, t => t.tableId === tableId && t.name.inSet(columns)
			)
		}

		def columnExists(tableId: Long, column: String) = {
			exists[CatalogColumn, CatalogColumnTable](
				catalogColumns, t => t.tableId === tableId && t.name === column
			)
		}

		def listColumns(tableId: Long) = {
			query[CatalogColumn, CatalogColumnTable](
				catalogColumns, _.tableId === tableId
			)
		}*/

	// -----------------------------------------------------------------
	// Function
	// -----------------------------------------------------------------

	def createFunction(function: CatalogFunction) = {
		insert[CatalogFunction, CatalogFunctionTable](
			function, catalogFunctions)
	}

	def deleteFunction(functionId: Long) = {
		delete[CatalogFunction, CatalogFunctionTable](
			catalogFunctions, _.id === functionId
		)
	}

	def deleteFunction(databaseId: Long, function: String) = {
		delete[CatalogFunction, CatalogFunctionTable](
			catalogFunctions, t => t.databaseId === databaseId && t.name === function
		)
	}

	def deleteFunctions(databaseId: Long) = {
		delete[CatalogFunction, CatalogFunctionTable](
			catalogFunctions, _.databaseId === databaseId
		)
	}

	def renameFunction(databaseId: Long, func: String, newFunc: String)(updateBy: Long) = {
		update[CatalogFunction, CatalogFunctionTable,
			(Rep[String], Rep[Long], Rep[Long]), (Rep[String], Rep[Long], Rep[Long]),
			(String, Long, Long)](
			catalogFunctions, t => t.databaseId === databaseId && t.name === func,
			t => (t.name, t.updateBy, t.updateTime), (newFunc, updateBy, Utils.now))
	}

	def updateFunction(funcDefinition: CatalogFunction) = {
		updateEntity[CatalogFunction, CatalogFunctionTable](
			catalogFunctions, t => t.id === funcDefinition.id.get, funcDefinition
		)
	}

	def getFunction(functionId: Long) = {
		queryOneOption[CatalogFunction, CatalogFunctionTable](
			catalogFunctions, _.id === functionId
		)
	}

	def getFunction(databaseId: Long, function: String) = {
		queryOneOption[CatalogFunction, CatalogFunctionTable](
			catalogFunctions, t => t.databaseId === databaseId && t.name === function
		)
	}

	def functionExists(databaseId: Long, function: String) = {
		exists[CatalogFunction, CatalogFunctionTable](
			catalogFunctions, t => t.databaseId === databaseId && t.name === function
		)
	}

	def listFunctions(databaseId: Long) = {
		query[CatalogFunction, CatalogFunctionTable](
			catalogFunctions, _.databaseId === databaseId
		)
	}

	def listFunctions(databaseId: Long, pattern: String) = {
		query[CatalogFunction, CatalogFunctionTable](
			catalogFunctions, t => t.databaseId === databaseId && t.name.like(pattern)
		)
	}

	// -----------------------------------------------------------------
	// View
	// -----------------------------------------------------------------

	def createView(view: CatalogView) = {
		insert[CatalogView, CatalogViewTable](
			view, catalogViews)
	}

	def deleteView(viewId: Long) = {
		delete[CatalogView, CatalogViewTable](
			catalogViews, _.id === viewId
		)
	}

	def deleteView(databaseId: Long, view: String) = {
		delete[CatalogView, CatalogViewTable](
			catalogViews, t => t.databaseId === databaseId && t.name === view
		)
	}

	def deleteViews(databaseId: Long) = {
		delete[CatalogView, CatalogViewTable](
			catalogViews, _.databaseId === databaseId
		)
	}

	def renameView(databaseId: Long, view: String, newView: String)(updateBy: Long) = {
		update[CatalogView, CatalogViewTable,
			(Rep[String], Rep[Long], Rep[Long]), (Rep[String], Rep[Long], Rep[Long]),
			(String, Long, Long)](
			catalogViews, t => t.databaseId === databaseId && t.name === view,
			t => (t.name, t.updateBy, t.updateTime), (newView, updateBy, Utils.now))
	}

	def updateView(viewDefinition: CatalogView) = {
		updateEntity[CatalogView, CatalogViewTable](
			catalogViews, t => t.id === viewDefinition.id.get, viewDefinition
		)
	}

	def getView(viewId: Long) = {
		queryOneOption[CatalogView, CatalogViewTable](
			catalogViews, _.id === viewId
		)
	}

	def getView(databaseId: Long, view: String) = {
		queryOneOption[CatalogView, CatalogViewTable](
			catalogViews, t => t.databaseId === databaseId && t.name === view
		)
	}

	def viewExists(databaseId: Long, view: String) = {
		exists[CatalogView, CatalogViewTable](
			catalogViews, t => t.databaseId === databaseId && t.name === view
		)
	}

	def listViews(databaseId: Long) = {
		query[CatalogView, CatalogViewTable](
			catalogViews, _.databaseId === databaseId
		)
	}

	def listViews(databaseId: Long, pattern: String) = {
		query[CatalogView, CatalogViewTable](
			catalogViews, t => t.databaseId === databaseId && t.name.like(pattern)
		)
	}

	// -----------------------------------------------------------------
	// UserTableRel
	// -----------------------------------------------------------------

	def createUserTableRel(rels: CatalogUserTableRel*) = {
		insertMultiple[CatalogUserTableRel, CatalogUserTableRelTable](
			rels, catalogUserTableRels
		)
	}

	/*def createUserPhysicalTableRel(rels: CatalogUserPhysicalTableRel*) = {
		insertMultiple[CatalogUserPhysicalTableRel, CatalogUserPhysicalTableRelTable](
			rels, catalogUserPhysicalTableRels
		)
	}*/

	/*// Using for revoking columns
	def deleteUserLogicalTableRels(userId: Long, tableId: Long, columns: Seq[String]) = {
		delete[CatalogUserLogicalTableRel, CatalogUserLogicalTableRelTable](
			catalogUserLogicalTableRels,
			t => t.userId === userId && t.tableId === tableId && t.columnName.inSet(columns)
		)
	}*/

	// Using for revoking columns
	def deleteUserTableRels(userId: Long, databaseId: Long, table: String, columns: Seq[String]) = {
		delete[CatalogUserTableRel, CatalogUserTableRelTable](
			catalogUserTableRels,
			t => t.userId === userId && t.databaseId === databaseId && t.table === table && t.columnName.inSet(columns)
		)
	}

	/*// Using for unmounting table directly or dropping local database
	def deleteUserLogicalTableRels(tableId: Long) = {
		delete[CatalogUserLogicalTableRel, CatalogUserLogicalTableRelTable](
			catalogUserLogicalTableRels,
			t => t.tableId === tableId
		)
	}*/

	// Using for unmounting physical database
	def deleteUserTableRels(databaseId: Long, table: String) = {
		delete[CatalogUserTableRel, CatalogUserTableRelTable](
			catalogUserTableRels,
			t => t.databaseId === databaseId && t.table === table
		)
	}

	// Using for dropping user
	def deleteUserTableRelsByUser(userId: Long) = {
		delete[CatalogUserTableRel, CatalogUserTableRelTable](
			catalogUserTableRels,
			t => t.userId === userId
		)
	}

	/*// Using for dropping user
	def deleteUserPhysicalTableRelsByUser(userId: Long) = {
		delete[CatalogUserPhysicalTableRel, CatalogUserPhysicalTableRelTable](
			catalogUserPhysicalTableRels,
			t => t.userId === userId
		)
	}*/

	/*def getUserLogicalTableRels(userId: Long, tableId: Long) = {
		query[CatalogUserLogicalTableRel, CatalogUserLogicalTableRelTable](
			catalogUserLogicalTableRels, t => t.userId === userId && t.tableId === tableId
		)
	}*/

	def getUserTableRels(userId: Long, databaseId: Long, table: String) = {
		query[CatalogUserTableRel, CatalogUserTableRelTable](
			catalogUserTableRels, t => t.userId === userId && t.databaseId === databaseId && t.table === table
		)
	}

	// -----------------------------------------------------------------
	// UserGroupRel
	// -----------------------------------------------------------------

	def createUserGroupRel(rels: CatalogUserGroupRel*) = {
		insertMultiple[CatalogUserGroupRel, CatalogUserGroupRelTable](
			rels, catalogUserGroupRels
		)
	}

	// use for removing users from group
	def deleteUserGroupRel(groupId: Long, users: Seq[Long]) = {
		delete[CatalogUserGroupRel, CatalogUserGroupRelTable](
			catalogUserGroupRels, t => t.groupId === groupId && t.userId.inSet(users)
		)
	}

	// use for dropping user
	def deleteUserGroupRelByUser(userId: Long) = {
		delete[CatalogUserGroupRel, CatalogUserGroupRelTable](
			catalogUserGroupRels, _.userId === userId
		)
	}

	// use for dropping group
	def deleteUserGroupRelByGroup(groupId: Long) = {
		delete[CatalogUserGroupRel, CatalogUserGroupRelTable](
			catalogUserGroupRels, _.groupId === groupId
		)
	}

	def getUserGroupRelByGroup(groupId: Long) = {
		query[CatalogUserGroupRel, CatalogUserGroupRelTable](
			catalogUserGroupRels, _.groupId === groupId
		)
	}

	def getUserGroupRelByUser(userId: Long) = {
		query[CatalogUserGroupRel, CatalogUserGroupRelTable](
			catalogUserGroupRels, _.userId === userId
		)
	}

	def userGroupRelExists(groupId: Long) = {
		exists[CatalogUserGroupRel, CatalogUserGroupRelTable](
			catalogUserGroupRels, _.id === groupId
		)
	}

}



























