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

package moonbox.catalog.jdbc

import moonbox.catalog._
import moonbox.catalog.config._
import moonbox.common.util.Utils
import moonbox.common.{MbConf, MbLogging}
import slick.dbio.Effect.{Read, Write}
import slick.jdbc.meta.MTable
import slick.sql.{FixedSqlAction, FixedSqlStreamingAction, SqlAction}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

class JdbcDao(override val conf: MbConf) extends EntityComponent with MbLogging {
	import profile.api._

	initializeIfNeeded()

	def close(): Unit = {
		//do not need close this dao connection, because all users use one same database connection
	}

	def action[R](action: DBIOAction[R, NoStream, Nothing]): Future[R] = {
		database.run(action)
	}

	def actionTransactionally[R](transactionAction: DBIOAction[R, NoStream, Effect.All]): Future[R] = {
		database.run(transactionAction.transactionally)
	}

	def actionTransactionally(transactionAction: DBIOAction[_, NoStream, Effect.All]*): Future[Unit] = {
		database.run(DBIO.seq(transactionAction:_*).transactionally)
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

	private def initializeIfNeeded(): Unit = {
		if (!EntityComponent.isInitialized.getAndSet(true)) {
			// if initialize failed, throw fatal exception, then jvm exit.
			Await.result(initialize(),
				new FiniteDuration(conf.get(JDBC_CATALOG_AWAIT_TIMEOUT), MILLISECONDS))
		}
	}

	private def initialize(): Future[Boolean] = {
		action(MTable.getTables("%").map(_.map(_.name.name))).flatMap { exists =>
			val create = tableQuerys.filterNot(tableQuery => exists.contains(tableQuery.shaped.value.tableName)).map { tableQuery =>
				logInfo(s"Initializing metadata table ${tableQuery.shaped.value.tableName} in catalog database $url ")
				tableQuery.schema.create
			}
			action(DBIO.seq(create:_*)).flatMap { res =>
				action(getUser("ROOT")).flatMap {
					case Some(user) => Future(true)
					case None =>
						action(createUser(CatalogUser(
							name = "ROOT",
							password = PasswordEncryptor.encryptSHA("123456"),
							account = true,
							ddl = true,
							dcl = true,
							grantAccount = true,
							grantDdl = true,
							grantDcl = true,
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

	def setUserDcl(dcl: Boolean, users: Long*)(updateBy: Long) = {
		update[CatalogUser, CatalogUserTable,
			(Rep[Boolean], Rep[Long], Rep[Long]), (Rep[Boolean], Rep[Long], Rep[Long]),
			(Boolean, Long, Long)](
			catalogUsers, _.id inSet users,
			t => (t.dcl, t.updateBy, t.updateTime),
			(dcl, updateBy, Utils.now))
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

	def setUserGrantDcl(grantDcl: Boolean, users: Long*)(updateBy: Long) = {
		update[CatalogUser, CatalogUserTable,
			(Rep[Boolean], Rep[Long], Rep[Long]), (Rep[Boolean], Rep[Long], Rep[Long]),
			(Boolean, Long, Long)](
			catalogUsers, _.id inSet users,
			t => (t.grantDcl, t.updateBy, t.updateTime),
			(grantDcl, updateBy, Utils.now))
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
	// Procedure
	// -----------------------------------------------------------------

	def createProcedure(procedure: CatalogProcedure) = {
		insert[CatalogProcedure, CatalogProcedureTable](procedure, catalogProcedures)
	}

	def deleteProcedure(procedureId: Long) = {
		delete[CatalogProcedure, CatalogProcedureTable](catalogProcedures, _.id === procedureId)
	}

	def deleteProcedure(organizationId: Long, procedure: String) = {
		delete[CatalogProcedure, CatalogProcedureTable](
			catalogProcedures, t => t.organizationId === organizationId && t.name === procedure)
	}

	def deleteProcedures(organizationId: Long) = {
		delete[CatalogProcedure, CatalogProcedureTable](
			catalogProcedures, _.organizationId === organizationId
		)
	}

	def renameProcedure(organizationId: Long, procedure: String, newProcedure: String)(updateBy: Long) = {
		update[CatalogProcedure, CatalogProcedureTable,
			(Rep[String], Rep[Long], Rep[Long]), (Rep[String], Rep[Long], Rep[Long]),
			(String, Long, Long)](
			catalogProcedures, t => t.organizationId === organizationId && t.name === procedure,
			t => (t.name, t.updateBy, t.updateTime), (newProcedure, updateBy, Utils.now))
	}

	def updateProcedure(procDefinition: CatalogProcedure) = {
		updateEntity[CatalogProcedure, CatalogProcedureTable](
			catalogProcedures, t => t.id === procDefinition.id.get, procDefinition
		)
	}

	def getProcedure(procedureId: Long) = {
		queryOneOption[CatalogProcedure, CatalogProcedureTable](catalogProcedures, _.id === procedureId)
	}

	def getProcedure(organizationId: Long, procedure: String) = {
		queryOneOption[CatalogProcedure, CatalogProcedureTable](
			catalogProcedures, t => t.organizationId === organizationId && t.name === procedure)
	}

	def procedureExists(organizationId: Long, procedure: String) = {
		exists[CatalogProcedure, CatalogProcedureTable](
			catalogProcedures, t => t.organizationId === organizationId && t.name === procedure)
	}

	def listProcedures(organizationId: Long) = {
		query[CatalogProcedure, CatalogProcedureTable](catalogProcedures, _.organizationId === organizationId)
	}

	def listProcedures(organizationId: Long, pattern: String) = {
		query[CatalogProcedure, CatalogProcedureTable](
			catalogProcedures, t => t.organizationId === organizationId && t.name.like(pattern))
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

	/*def updateFunction(funcDefinition: CatalogFunction) = {
		updateEntity[CatalogFunction, CatalogFunctionTable](
			catalogFunctions, t => t.id === funcDefinition.id.get, funcDefinition
		)
	}*/

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
		catalogFunctions.filter(_.databaseId === databaseId).join(catalogFunctionResources).on {
			case (func, resource) => func.id === resource.funcId
		}.result
	}

	def listFunctions(databaseId: Long, pattern: String) = {
		catalogFunctions.filter(t => t.databaseId === databaseId && t.name.like(pattern)).join(catalogFunctionResources).on {
			case (func, resource) => func.id === resource.funcId
		}.result
	}

	def createFunctionResources(functionResources: CatalogFunctionResource*) ={
		insertMultiple[CatalogFunctionResource, CatalogFunctionResourceTable](
			functionResources,
			catalogFunctionResources
		)
	}

	def deleteFunctionResources(funcId: Long) = {
		delete[CatalogFunctionResource, CatalogFunctionResourceTable](
			catalogFunctionResources, _.funcId === funcId
		)
	}

	def listFunctionResources(funcId: Long) = {
		query[CatalogFunctionResource, CatalogFunctionResourceTable](
			catalogFunctionResources, _.funcId === funcId
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
	// database privileges
	// -----------------------------------------------------------------
	def createDatabasePrivilege(dbPrivilege: CatalogDatabasePrivilege*) = {
		insertMultiple[CatalogDatabasePrivilege, CatalogDatabasePrivilegeTable](
			dbPrivilege, catalogDatabasePrivileges
		)
	}

	def deleteDatabasePrivilege(userId: Long, databaseId: Long, privileges: String*) = {
		delete[CatalogDatabasePrivilege, CatalogDatabasePrivilegeTable](
			catalogDatabasePrivileges,
			t => t.userId === userId && t.databaseId === databaseId && t.privilegeType.inSet(privileges)
		)
	}

	def deleteDatabasePrivilege(userId: Long, databaseId: Long) = {
		delete[CatalogDatabasePrivilege, CatalogDatabasePrivilegeTable](
			catalogDatabasePrivileges,
			t => t.userId === userId && t.databaseId === databaseId
		)
	}

	def deleteDatabasePrivilege(databaseId: Long) = {
		delete[CatalogDatabasePrivilege, CatalogDatabasePrivilegeTable](
			catalogDatabasePrivileges,
			t =>  t.databaseId === databaseId
		)
	}

	def deleteDatabasePrivilegeByUser(userId: Long) = {
		delete[CatalogDatabasePrivilege, CatalogDatabasePrivilegeTable](
			catalogDatabasePrivileges,
			t =>  t.userId === userId
		)
	}

	def getDatabasePrivilege(userId: Long, databaseId: Long, privilegeType: String) = {
		queryOneOption[CatalogDatabasePrivilege, CatalogDatabasePrivilegeTable](
			catalogDatabasePrivileges,
			t =>  t.userId === userId && t.databaseId === databaseId && t.privilegeType === privilegeType
		)
	}

	def getDatabasePrivilege(userId: Long, databaseId: Long) = {
		query[CatalogDatabasePrivilege, CatalogDatabasePrivilegeTable](
			catalogDatabasePrivileges,
			t =>  t.userId === userId && t.databaseId === databaseId
		)
	}

	def getDatabasePrivilege(userId: Long) = {
		query[CatalogDatabasePrivilege, CatalogDatabasePrivilegeTable](
			catalogDatabasePrivileges,
			t =>  t.userId === userId
		)
	}

	// -----------------------------------------------------------------
	// table privileges
	// -----------------------------------------------------------------
	def createTablePrivilege(tablePrivilege: CatalogTablePrivilege*) = {
		insertMultiple[CatalogTablePrivilege, CatalogTablePrivilegeTable](
			tablePrivilege, catalogTablePrivileges
		)
	}

	def deleteTablePrivilege(userId: Long, databaseId: Long, table: String, privileges: String*) = {
		delete[CatalogTablePrivilege, CatalogTablePrivilegeTable](
			catalogTablePrivileges,
			t => t.userId === userId && t.databaseId === databaseId && t.table === table &&
				t.privilegeType.inSet(privileges)
		)
	}

	def deleteTablePrivilege(userId: Long, databaseId: Long, table: String) = {
		delete[CatalogTablePrivilege, CatalogTablePrivilegeTable](
			catalogTablePrivileges,
			t => t.userId === userId && t.databaseId === databaseId && t.table === table
		)
	}

	def deleteTablePrivilege(databaseId: Long, table: String) = {
		delete[CatalogTablePrivilege, CatalogTablePrivilegeTable](
			catalogTablePrivileges,
			t =>  t.databaseId === databaseId && t.table === table
		)
	}

	def deleteTablePrivilege(databaseId: Long) = {
		delete[CatalogTablePrivilege, CatalogTablePrivilegeTable](
			catalogTablePrivileges,
			t =>  t.databaseId === databaseId
		)
	}

	def deleteTablePrivilegeByUser(userId: Long) = {
		delete[CatalogTablePrivilege, CatalogTablePrivilegeTable](
			catalogTablePrivileges,
			t =>  t.userId === userId
		)
	}

	def getTablePrivilege(userId: Long, databaseId: Long, table: String, privilegeType: String) = {
		queryOneOption[CatalogTablePrivilege, CatalogTablePrivilegeTable](
			catalogTablePrivileges,
			t =>  t.userId === userId && t.databaseId === databaseId && t.table === table && t.privilegeType === privilegeType
		)
	}

	def getTablePrivilege(userId: Long, databaseId: Long, table: String) = {
		query[CatalogTablePrivilege, CatalogTablePrivilegeTable](
			catalogTablePrivileges,
			t =>  t.userId === userId && t.databaseId === databaseId && t.table === table
		)
	}

	def getTablePrivilege(userId: Long) = {
		query[CatalogTablePrivilege, CatalogTablePrivilegeTable](
			catalogTablePrivileges,
			t =>  t.userId === userId
		)
	}

	// -----------------------------------------------------------------
	// column privileges
	// -----------------------------------------------------------------
	def createColumnPrivilege(columnPrivilege: CatalogColumnPrivilege*) = {
		insertMultiple[CatalogColumnPrivilege, CatalogColumnPrivilegeTable](
			columnPrivilege, catalogColumnPrivileges
		)
	}

	def deleteColumnPrivilege(userId: Long, databaseId: Long, table: String, columns: Seq[String], privilege: String) = {
		delete[CatalogColumnPrivilege, CatalogColumnPrivilegeTable](
			catalogColumnPrivileges,
			t => t.userId === userId && t.databaseId === databaseId && t.table === table &&
				t.privilegeType === privilege && t.columnName.inSet(columns)
		)
	}

	def deleteColumnPrivilege(userId: Long, databaseId: Long, table: String, column: String) = {
		delete[CatalogColumnPrivilege, CatalogColumnPrivilegeTable](
			catalogColumnPrivileges,
			t => t.userId === userId && t.databaseId === databaseId && t.table === table && t.columnName === column
		)
	}

	def deleteColumnPrivilege(databaseId: Long, table: String) = {
		delete[CatalogColumnPrivilege, CatalogColumnPrivilegeTable](
			catalogColumnPrivileges,
			t =>  t.databaseId === databaseId && t.table === table
		)
	}

	def deleteColumnPrivilege(databaseId: Long) = {
		delete[CatalogColumnPrivilege, CatalogColumnPrivilegeTable](
			catalogColumnPrivileges,
			t =>  t.databaseId === databaseId
		)
	}

	def deleteColumnPrivilegeByUser(userId: Long) = {
		delete[CatalogColumnPrivilege, CatalogColumnPrivilegeTable](
			catalogColumnPrivileges,
			t =>  t.userId === userId
		)
	}

	def getColumnPrivilege(userId: Long, databaseId: Long, table: String, privilegeType: String) = {
		query[CatalogColumnPrivilege, CatalogColumnPrivilegeTable](
			catalogColumnPrivileges,
			t =>  t.userId === userId && t.databaseId === databaseId && t.table === table && t.privilegeType === privilegeType
		)
	}

	def getColumnPrivilege(userId: Long) = {
		query[CatalogColumnPrivilege, CatalogColumnPrivilegeTable](
			catalogColumnPrivileges,
			t =>  t.userId === userId
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



























