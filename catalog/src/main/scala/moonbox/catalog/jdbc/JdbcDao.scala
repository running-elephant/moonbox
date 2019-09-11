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
			val create = tableQueries.filterNot(tableQuery => exists.contains(tableQuery.shaped.value.tableName)).map { tableQuery =>
				logInfo(s"Initializing metadata table ${tableQuery.shaped.value.tableName} in catalog database $url ")
				tableQuery.schema.create
			}
			action(DBIO.seq(create:_*)).flatMap { res =>
				action(getOrganization("SYSTEM")).flatMap {
					case Some(org) =>
						Future(org.id.get)
					case None =>
						action(createOrganization(OrganizationEntity(
							name = "SYSTEM",
							config = Map(),
							createBy = -1,
							updateBy = -1
						)))
				}.flatMap { id =>
					action(getDatabase(id, "default")).flatMap {
						case Some(db) => Future(true)
						case None =>
							action(createDatabase(DatabaseEntity(
								name = "default",
								organizationId = id,
								properties = Map(),
								isLogical = true,
								createBy = -1,
								updateBy = -1
							))).map(_ => true)
					}.flatMap { _ =>
						action(getUser(id, "ROOT")).flatMap {
							case Some(user) => Future(true)
							case None =>
								action(createUser(UserEntity(
									name = "ROOT",
									password = PasswordEncryptor.encryptSHA("123456"),
									organizationId = id,
									createBy = -1,
									updateBy = -1
								))).map(_ => true)
						}
					}
				}
			}
		}
	}

	// -----------------------------------------------------------------
	// Application
	// -----------------------------------------------------------------

	def createApplication(application: ApplicationEntity) = {
		insert(application, applications)
	}

	def deleteApplication(applicationId: Long) = {
		delete[ApplicationEntity, ApplicationEntityTable](applications, _.id === applicationId)
	}

	def updateApplication(appDefinition: ApplicationEntity) = {
		updateEntity[ApplicationEntity, ApplicationEntityTable](
			applications, _.id === appDefinition.id.get,
			appDefinition
		)
	}

	def getApplication(applicationId: Long) = {
		queryOneOption[ApplicationEntity, ApplicationEntityTable](applications, _.id === applicationId)
	}

	def getApplication(application: String) = {
		queryOneOption[ApplicationEntity, ApplicationEntityTable](applications, _.name === application)
	}

	def applicationExists(application: String) = {
		exists[ApplicationEntity, ApplicationEntityTable](applications, _.name === application)
	}

	def listApplications() = {
		list[ApplicationEntity, ApplicationEntityTable](applications)
	}

	def listApplications(pattern: String) = {
		query[ApplicationEntity, ApplicationEntityTable](
			applications, _.name.like(pattern)
		)
	}

	// -----------------------------------------------------------------
	// Organization
	// -----------------------------------------------------------------

	def createOrganization(organization: OrganizationEntity) = {
		insert(organization, organizations)
	}

	def deleteOrganization(organizationId: Long) = {
		delete[OrganizationEntity, OrganizationEntityTable](organizations, _.id === organizationId)
	}

	def deleteOrganization(organization: String) = {
		delete[OrganizationEntity, OrganizationEntityTable](organizations, _.name === organization)
	}

	def renameOrganization(organization: String, newOrganization: String)(updateBy: Long) = {
		update[OrganizationEntity, OrganizationEntityTable,
			(Rep[String], Rep[Long], Rep[Long]), (Rep[String], Rep[Long], Rep[Long]),
			(String, Long, Long)](
			organizations, _.name === organization,
			t => (t.name, t.updateBy, t.updateTime), (newOrganization, updateBy, Utils.now))
	}

	def updateOrganization(organizationDefinition: OrganizationEntity) = {
		updateEntity[OrganizationEntity, OrganizationEntityTable](
			organizations, _.id === organizationDefinition.id.get,
			organizationDefinition
		)
	}

	def getOrganization(organizationId: Long) = {
		queryOneOption[OrganizationEntity, OrganizationEntityTable](organizations, _.id === organizationId)
	}

	def getOrganization(organization: String) = {
		queryOneOption[OrganizationEntity, OrganizationEntityTable](organizations, _.name === organization)
	}

	def organizationExists(organization: String) = {
		exists[OrganizationEntity, OrganizationEntityTable](organizations, _.name === organization)
	}

	def listOrganizations() = {
		list[OrganizationEntity, OrganizationEntityTable](organizations)
	}

	def listOrganizations(pattern: String) = {
		query[OrganizationEntity, OrganizationEntityTable](
			organizations, _.name.like(pattern)
		)
	}

	// -----------------------------------------------------------------
	// User
	// -----------------------------------------------------------------

	def createUser(user: UserEntity) = {
		insert(user, users)
	}

	def deleteUser(userId: Long) = {
		delete[UserEntity, UserEntityTable](users, _.id === userId)
	}

	def deleteUser(organizationId: Long, user: String) = {
		delete[UserEntity, UserEntityTable](users,
			t => t.organizationId === organizationId && t.name === user)
	}

	def deleteUsers(organizationId: Long) = {
		delete[UserEntity, UserEntityTable](users, _.organizationId === organizationId)
	}

	def updateUser(user: UserEntity) = {
		updateEntity[UserEntity, UserEntityTable](users, _.id === user.id.get, user)
	}

	def renameUser(user: String, newUser: String)(updateBy: Long) = {
		update[UserEntity, UserEntityTable,
			(Rep[String], Rep[Long], Rep[Long]), (Rep[String], Rep[Long], Rep[Long]),
			(String, Long, Long)](
			users, _.name === user,
			t => (t.name, t.updateBy, t.updateTime), (newUser, updateBy, Utils.now))
	}

	def changePassword(user: String, newPassword: String)(updateBy: Long) = {
		update[UserEntity, UserEntityTable,
			(Rep[String], Rep[Long], Rep[Long]), (Rep[String], Rep[Long], Rep[Long]),
			(String, Long, Long)](
			users, _.name === user,
			t => (t.password, t.updateBy, t.updateTime),
			(newPassword, updateBy, Utils.now))
	}

	def setUserAccount(account: Boolean, userIds: Long*)(updateBy: Long) = {
		update[UserEntity, UserEntityTable,
			(Rep[Boolean], Rep[Long], Rep[Long]), (Rep[Boolean], Rep[Long], Rep[Long]),
			(Boolean, Long, Long)](
			users, _.id inSet userIds,
			t => (t.account, t.updateBy, t.updateTime),
			(account, updateBy, Utils.now))
	}

	def setUserDdl(ddl: Boolean, userIds: Long*)(updateBy: Long) = {
		update[UserEntity, UserEntityTable,
			(Rep[Boolean], Rep[Long], Rep[Long]), (Rep[Boolean], Rep[Long], Rep[Long]),
			(Boolean, Long, Long)](
			users, _.id inSet userIds,
			t => (t.ddl, t.updateBy, t.updateTime),
			(ddl, updateBy, Utils.now))
	}

	def setUserDcl(dcl: Boolean, userIds: Long*)(updateBy: Long) = {
		update[UserEntity, UserEntityTable,
			(Rep[Boolean], Rep[Long], Rep[Long]), (Rep[Boolean], Rep[Long], Rep[Long]),
			(Boolean, Long, Long)](
			users, _.id inSet userIds,
			t => (t.dcl, t.updateBy, t.updateTime),
			(dcl, updateBy, Utils.now))
	}

	def setUserGrantAccount(grantAccount: Boolean, userIds: Long*)(updateBy: Long) = {
		update[UserEntity, UserEntityTable,
			(Rep[Boolean], Rep[Long], Rep[Long]), (Rep[Boolean], Rep[Long], Rep[Long]),
			(Boolean, Long, Long)](
			users, _.id inSet userIds,
			t => (t.grantAccount, t.updateBy, t.updateTime),
			(grantAccount, updateBy, Utils.now))
	}

	def setUserGrantDdl(grantDdl: Boolean, userIds: Long*)(updateBy: Long) = {
		update[UserEntity, UserEntityTable,
			(Rep[Boolean], Rep[Long], Rep[Long]), (Rep[Boolean], Rep[Long], Rep[Long]),
			(Boolean, Long, Long)](
			users, _.id inSet userIds,
			t => (t.grantDdl, t.updateBy, t.updateTime),
			(grantDdl, updateBy, Utils.now))
	}

	def setUserGrantDcl(grantDcl: Boolean, userIds: Long*)(updateBy: Long) = {
		update[UserEntity, UserEntityTable,
			(Rep[Boolean], Rep[Long], Rep[Long]), (Rep[Boolean], Rep[Long], Rep[Long]),
			(Boolean, Long, Long)](
			users, _.id inSet userIds,
			t => (t.grantDcl, t.updateBy, t.updateTime),
			(grantDcl, updateBy, Utils.now))
	}

	def getUser(userId: Long) = {
		queryOneOption[UserEntity, UserEntityTable](users, _.id === userId)
	}

	def getUser(organizationId: Long, user: String) = {
		queryOneOption[UserEntity, UserEntityTable](
			users, t => t.organizationId === organizationId && t.name === user)
	}

	def userExists(organizationId: Long, user: String) = {
		exists[UserEntity, UserEntityTable](
			users, t => t.organizationId === organizationId && t.name === user)
	}

	def listUsers(organizationId: Long) = {
		query[UserEntity, UserEntityTable](users,  _.organizationId === organizationId)
	}

	def listUsers(organizationId: Long, pattern: String) = {
		query[UserEntity, UserEntityTable](
			users,  t => t.organizationId === organizationId && t.name.like(pattern))
	}

	def listSas() = {
		query[UserEntity, UserEntityTable](users,  _.isSA === true)
	}

	def listSas(pattern: String) = {
		query[UserEntity, UserEntityTable](
			users,  t => t.isSA === true && t.name.like(pattern))
	}

	// -----------------------------------------------------------------
	// Procedure
	// -----------------------------------------------------------------

	def createProcedure(procedure: ProcedureEntity) = {
		insert[ProcedureEntity, ProcedureEntityTable](procedure, procedures)
	}

	def deleteProcedure(procedureId: Long) = {
		delete[ProcedureEntity, ProcedureEntityTable](procedures, _.id === procedureId)
	}

	def deleteProcedure(organizationId: Long, procedure: String) = {
		delete[ProcedureEntity, ProcedureEntityTable](
			procedures, t => t.organizationId === organizationId && t.name === procedure)
	}

	def deleteProcedures(organizationId: Long) = {
		delete[ProcedureEntity, ProcedureEntityTable](
			procedures, _.organizationId === organizationId
		)
	}

	def renameProcedure(organizationId: Long, procedure: String, newProcedure: String)(updateBy: Long) = {
		update[ProcedureEntity, ProcedureEntityTable,
			(Rep[String], Rep[Long], Rep[Long]), (Rep[String], Rep[Long], Rep[Long]),
			(String, Long, Long)](
			procedures, t => t.organizationId === organizationId && t.name === procedure,
			t => (t.name, t.updateBy, t.updateTime), (newProcedure, updateBy, Utils.now))
	}

	def updateProcedure(procDefinition: ProcedureEntity) = {
		updateEntity[ProcedureEntity, ProcedureEntityTable](
			procedures, t => t.id === procDefinition.id.get, procDefinition
		)
	}

	def getProcedure(procedureId: Long) = {
		queryOneOption[ProcedureEntity, ProcedureEntityTable](procedures, _.id === procedureId)
	}

	def getProcedure(organizationId: Long, procedure: String) = {
		queryOneOption[ProcedureEntity, ProcedureEntityTable](
			procedures, t => t.organizationId === organizationId && t.name === procedure)
	}

	def procedureExists(organizationId: Long, procedure: String) = {
		exists[ProcedureEntity, ProcedureEntityTable](
			procedures, t => t.organizationId === organizationId && t.name === procedure)
	}

	def listProcedures(organizationId: Long) = {
		query[ProcedureEntity, ProcedureEntityTable](procedures, _.organizationId === organizationId)
	}

	def listProcedures(organizationId: Long, pattern: String) = {
		query[ProcedureEntity, ProcedureEntityTable](
			procedures, t => t.organizationId === organizationId && t.name.like(pattern))
	}

	// -----------------------------------------------------------------
	// timedevent
	// -----------------------------------------------------------------
	def createTimedEvent(event: TimedEventEntity) = {
		insert(event, timedEvents)
	}

	def deleteTimedEvent(eventId: Long) = {
		delete[TimedEventEntity, TimedEventEntityTable](
			timedEvents, _.id === eventId
		)
	}

	def deleteTimedEvent(organizationId: Long, event: String) = {
		delete[TimedEventEntity, TimedEventEntityTable](
			timedEvents, t => t.organizationId === organizationId && t.name === event
		)
	}

	def deleteTimedEvents(organizationId: Long) = {
		delete[TimedEventEntity, TimedEventEntityTable](
			timedEvents, t => t.organizationId === organizationId
		)
	}

	def renameTimedEvent(organizationId: Long, event: String, newEvent: String)(updateBy: Long) = {
		update[TimedEventEntity, TimedEventEntityTable,
			(Rep[String], Rep[Long], Rep[Long]), (Rep[String], Rep[Long], Rep[Long]),
			(String, Long, Long)](
			timedEvents, t => t.organizationId === organizationId && t.name === event,
			t => (t.name, t.updateBy, t.updateTime), (newEvent, updateBy, Utils.now))
	}

	def updateTimedEvent(eventDefinition: TimedEventEntity) = {
		updateEntity[TimedEventEntity, TimedEventEntityTable](
			timedEvents, t => t.id === eventDefinition.id.get, eventDefinition
		)
	}

	def getTimedEvent(organizationId: Long, event: String) = {
		queryOneOption[TimedEventEntity, TimedEventEntityTable](
			timedEvents, t => t.organizationId === organizationId && t.name === event
		)
	}

	def timedEventExists(organizationId: Long, event: String) = {
		exists[TimedEventEntity, TimedEventEntityTable](
			timedEvents, t => t.organizationId === organizationId && t.name === event
		)
	}

	def timedEventExists(organizationId: Long, procId: Long) = {
		exists[TimedEventEntity, TimedEventEntityTable](
			timedEvents, t => t.organizationId === organizationId && t.procedure === procId
		)
	}

	def listTimedEvents(organizationId: Long) = {
		query[TimedEventEntity, TimedEventEntityTable](
			timedEvents, t => t.organizationId === organizationId
		)
	}

	def listTimedEvents(organizationId: Long, pattern: String) = {
		query[TimedEventEntity, TimedEventEntityTable](
			timedEvents, t => t.organizationId === organizationId && t.name.like(pattern)
		)
	}

	// -----------------------------------------------------------------
	// Database
	// -----------------------------------------------------------------

	def createDatabase(database: DatabaseEntity) = {
		insert(database, databases)
	}

	def deleteDatabase(databaseId: Long) = {
		delete[DatabaseEntity, DatabaseEntityTable](databases, _.id === databaseId)
	}

	def deleteDatabase(organizationId: Long, database: String) = {
		delete[DatabaseEntity, DatabaseEntityTable](
			databases, t => t.organizationId === organizationId && t.name === database)
	}

	def deleteDatabases(organizationId: Long) = {
		delete[DatabaseEntity, DatabaseEntityTable](
			databases, t => t.organizationId === organizationId)
	}

	def renameDatabase(organizationId: Long, database: String, newDatabase: String)(updateBy: Long) = {
		update[DatabaseEntity, DatabaseEntityTable,
			(Rep[String], Rep[Long], Rep[Long]), (Rep[String], Rep[Long], Rep[Long]),
			(String, Long, Long)](
			databases, t => t.organizationId === organizationId && t.name === database,
			t => (t.name, t.updateBy, t.updateTime), (newDatabase, updateBy, Utils.now))
	}

	def updateDatabase(dbDefinition: DatabaseEntity) = {
		updateEntity[DatabaseEntity, DatabaseEntityTable](
			databases, t => t.id === dbDefinition.id.get, dbDefinition
		)
	}

	def getDatabase(databaseId: Long) = {
		queryOneOption[DatabaseEntity, DatabaseEntityTable](databases, _.id === databaseId)
	}

	def getDatabase(organizationId: Long, database: String) = {
		queryOneOption[DatabaseEntity, DatabaseEntityTable](
			databases, t => t.organizationId === organizationId && t.name === database)
	}

	def databaseExists(databaseId: Long) = {
		exists[DatabaseEntity, DatabaseEntityTable](
			databases, t => t.id === databaseId)
	}

	def databaseExists(organizationId: Long, database: String) = {
		exists[DatabaseEntity, DatabaseEntityTable](
			databases, t => t.organizationId === organizationId && t.name === database)
	}

	def listDatabases(organizationId: Long) = {
		query[DatabaseEntity, DatabaseEntityTable](databases, _.organizationId === organizationId)
	}

	def listDatabases(organizationId: Long, pattern: String) = {
		query[DatabaseEntity, DatabaseEntityTable](
			databases, t => t.organizationId === organizationId && t.name.like(pattern))
	}

	// -----------------------------------------------------------------
	// Table
	// -----------------------------------------------------------------

	def createTable(table: TableEntity) = {
		insert[TableEntity, TableEntityTable](table, tables)
	}

	def deleteTables(databaseId: Long) = {
		delete[TableEntity, TableEntityTable](
			tables, _.databaseId === databaseId
		)
	}

	def deleteTable(tableId: Long) = {
		delete[TableEntity, TableEntityTable](tables, _.id === tableId)
	}

	def deleteTable(databaseId: Long, table: String) = {
		delete[TableEntity, TableEntityTable](tables, t => t.databaseId === databaseId && t.name === table)
	}

	def updateTable(table: TableEntity) = {
		updateEntity[TableEntity, TableEntityTable](
			tables, t => t.id === table.id.get, table
		)
	}

	def renameTable(databaseId: Long, table: String, newTable: String)(updateBy: Long) = {
		update[TableEntity, TableEntityTable,
			(Rep[String], Rep[Long], Rep[Long]), (Rep[String], Rep[Long], Rep[Long]),
			(String, Long, Long)](
			tables, t => t.databaseId === databaseId && t.name === table,
			t => (t.name, t.updateBy, t.updateTime), (newTable, updateBy, Utils.now))
	}

	def getTable(tableId: Long) = {
		queryOneOption[TableEntity, TableEntityTable](tables, _.id === tableId)
	}

	def getTable(databaseId: Long, table: String) = {
		queryOneOption[TableEntity, TableEntityTable](
			tables, t => t.databaseId === databaseId && t.name === table)
	}

	def tableExists(databaseId: Long, table: String) = {
		exists[TableEntity, TableEntityTable](
			tables, t => t.databaseId === databaseId && t.name === table
		)
	}

	def listTables(databaseId: Long) = {
		query[TableEntity, TableEntityTable](tables, _.databaseId === databaseId)
	}

	def listTables(databaseId: Long, pattern: String) = {
		query[TableEntity, TableEntityTable](
			tables, t => t.databaseId === databaseId && t.name.like(pattern))
	}

	// -----------------------------------------------------------------
	// Function
	// -----------------------------------------------------------------

	def createFunction(function: FunctionEntity) = {
		insert[FunctionEntity, FunctionEntityTable](
			function, functions)
	}

	def deleteFunction(functionId: Long) = {
		delete[FunctionEntity, FunctionEntityTable](
			functions, _.id === functionId
		)
	}

	def deleteFunction(databaseId: Long, function: String) = {
		delete[FunctionEntity, FunctionEntityTable](
			functions, t => t.databaseId === databaseId && t.name === function
		)
	}

	def deleteFunctions(databaseId: Long) = {
		delete[FunctionEntity, FunctionEntityTable](
			functions, _.databaseId === databaseId
		)
	}

	def renameFunction(databaseId: Long, func: String, newFunc: String)(updateBy: Long) = {
		update[FunctionEntity, FunctionEntityTable,
			(Rep[String], Rep[Long], Rep[Long]), (Rep[String], Rep[Long], Rep[Long]),
			(String, Long, Long)](
			functions, t => t.databaseId === databaseId && t.name === func,
			t => (t.name, t.updateBy, t.updateTime), (newFunc, updateBy, Utils.now))
	}

	def getFunction(functionId: Long) = {
		queryOneOption[FunctionEntity, FunctionEntityTable](
			functions, _.id === functionId
		)
	}

	def getFunction(databaseId: Long, function: String) = {
		queryOneOption[FunctionEntity, FunctionEntityTable](
			functions, t => t.databaseId === databaseId && t.name === function
		)
	}

	def functionExists(databaseId: Long, function: String) = {
		exists[FunctionEntity, FunctionEntityTable](
			functions, t => t.databaseId === databaseId && t.name === function
		)
	}

	def listFunctions(databaseId: Long) = {
		functions.filter(_.databaseId === databaseId).join(functionResources).on {
			case (func, resource) => func.id === resource.funcId
		}.result
	}

	def listFunctions(databaseId: Long, pattern: String) = {
		functions.filter(t => t.databaseId === databaseId && t.name.like(pattern)).join(functionResources).on {
			case (func, resource) => func.id === resource.funcId
		}.result
	}

	def createFunctionResources(resources: FunctionResourceEntity*) ={
		insertMultiple[FunctionResourceEntity, FunctionResourceEntityTable](
			resources,
			functionResources
		)
	}

	def deleteFunctionResources(funcId: Long) = {
		delete[FunctionResourceEntity, FunctionResourceEntityTable](
			functionResources, _.funcId === funcId
		)
	}

	def listFunctionResources(funcId: Long) = {
		query[FunctionResourceEntity, FunctionResourceEntityTable](
			functionResources, _.funcId === funcId
		)
	}

	// -----------------------------------------------------------------
	// database privileges
	// -----------------------------------------------------------------
	def createDatabasePrivilege(dbPrivilege: DatabasePrivilegeEntity*) = {
		insertMultiple[DatabasePrivilegeEntity, DatabasePrivilegeEntityTable](
			dbPrivilege, databasePrivileges
		)
	}

	def deleteDatabasePrivilege(userId: Long, databaseId: Long, privileges: String*) = {
		delete[DatabasePrivilegeEntity, DatabasePrivilegeEntityTable](
			databasePrivileges,
			t => t.userId === userId && t.databaseId === databaseId && t.privilegeType.inSet(privileges)
		)
	}

	def deleteDatabasePrivilege(userId: Long, databaseId: Long) = {
		delete[DatabasePrivilegeEntity, DatabasePrivilegeEntityTable](
			databasePrivileges,
			t => t.userId === userId && t.databaseId === databaseId
		)
	}

	def deleteDatabasePrivilege(databaseId: Long) = {
		delete[DatabasePrivilegeEntity, DatabasePrivilegeEntityTable](
			databasePrivileges,
			t =>  t.databaseId === databaseId
		)
	}

	def deleteDatabasePrivilegeByUser(userId: Long) = {
		delete[DatabasePrivilegeEntity, DatabasePrivilegeEntityTable](
			databasePrivileges,
			t =>  t.userId === userId
		)
	}

	def getDatabasePrivilege(userId: Long, databaseId: Long, privilegeType: String) = {
		queryOneOption[DatabasePrivilegeEntity, DatabasePrivilegeEntityTable](
			databasePrivileges,
			t =>  t.userId === userId && t.databaseId === databaseId && t.privilegeType === privilegeType
		)
	}

	def getDatabasePrivilege(userId: Long, databaseId: Long) = {
		query[DatabasePrivilegeEntity, DatabasePrivilegeEntityTable](
			databasePrivileges,
			t =>  t.userId === userId && t.databaseId === databaseId
		)
	}

	def getDatabasePrivilege(userId: Long) = {
		query[DatabasePrivilegeEntity, DatabasePrivilegeEntityTable](
			databasePrivileges,
			t =>  t.userId === userId
		)
	}

	// -----------------------------------------------------------------
	// table privileges
	// -----------------------------------------------------------------
	def createTablePrivilege(tablePrivilege: TablePrivilegeEntity*) = {
		insertMultiple[TablePrivilegeEntity, TablePrivilegeEntityTable](
			tablePrivilege, tablePrivileges
		)
	}

	def deleteTablePrivilege(userId: Long, databaseId: Long, tableId: Long, privileges: Seq[String]) = {
		delete[TablePrivilegeEntity, TablePrivilegeEntityTable](
			tablePrivileges,
			t => t.userId === userId && t.databaseId === databaseId && t.tableId === tableId &&
				t.privilegeType.inSet(privileges)
		)
	}

	def deleteTablePrivilege(userId: Long, databaseId: Long, tableId: Long) = {
		delete[TablePrivilegeEntity, TablePrivilegeEntityTable](
			tablePrivileges,
			t => t.userId === userId && t.databaseId === databaseId && t.tableId === tableId
		)
	}

	def deleteTablePrivilege(databaseId: Long, tableId: Long) = {
		delete[TablePrivilegeEntity, TablePrivilegeEntityTable](
			tablePrivileges,
			t =>  t.databaseId === databaseId && t.tableId === tableId
		)
	}

	def deleteTablePrivilege(databaseId: Long) = {
		delete[TablePrivilegeEntity, TablePrivilegeEntityTable](
			tablePrivileges,
			t =>  t.databaseId === databaseId
		)
	}

	def deleteTablePrivilegeByUser(userId: Long) = {
		delete[TablePrivilegeEntity, TablePrivilegeEntityTable](
			tablePrivileges,
			t =>  t.userId === userId
		)
	}

	def getTablePrivilege(userId: Long, databaseId: Long, tableId: Long, privilegeType: String) = {
		queryOneOption[TablePrivilegeEntity, TablePrivilegeEntityTable](
			tablePrivileges,
			t =>  t.userId === userId && t.databaseId === databaseId && t.tableId === tableId && t.privilegeType === privilegeType
		)
	}

	def getTablePrivilege(userId: Long, databaseId: Long, tableId: Long) = {
		query[TablePrivilegeEntity, TablePrivilegeEntityTable](
			tablePrivileges,
			t =>  t.userId === userId && t.databaseId === databaseId && t.tableId === tableId
		)
	}

	def getTablePrivilege(userId: Long) = {
		query[TablePrivilegeEntity, TablePrivilegeEntityTable](
			tablePrivileges,
			t =>  t.userId === userId
		)
	}

	// -----------------------------------------------------------------
	// column privileges
	// -----------------------------------------------------------------
	def createColumnPrivilege(columnPrivilege: ColumnPrivilegeEntity*) = {
		insertMultiple[ColumnPrivilegeEntity, ColumnPrivilegeEntityTable](
			columnPrivilege, columnPrivileges
		)
	}

	def deleteColumnPrivilege(userId: Long, databaseId: Long, tableId: Long, columns: Seq[String], privilege: String) = {
		delete[ColumnPrivilegeEntity, ColumnPrivilegeEntityTable](
			columnPrivileges,
			t => t.userId === userId && t.databaseId === databaseId && t.tableId === tableId &&
				t.privilegeType === privilege && t.columnName.inSet(columns)
		)
	}

	def deleteColumnPrivilege(userId: Long, databaseId: Long, tableId: Long, column: String) = {
		delete[ColumnPrivilegeEntity, ColumnPrivilegeEntityTable](
			columnPrivileges,
			t => t.userId === userId && t.databaseId === databaseId && t.tableId === tableId && t.columnName === column
		)
	}

	def deleteColumnPrivilege(databaseId: Long, tableId: Long) = {
		delete[ColumnPrivilegeEntity, ColumnPrivilegeEntityTable](
			columnPrivileges,
			t =>  t.databaseId === databaseId && t.tableId === tableId
		)
	}

	def deleteColumnPrivilege(databaseId: Long) = {
		delete[ColumnPrivilegeEntity, ColumnPrivilegeEntityTable](
			columnPrivileges,
			t =>  t.databaseId === databaseId
		)
	}

	def deleteColumnPrivilegeByUser(userId: Long) = {
		delete[ColumnPrivilegeEntity, ColumnPrivilegeEntityTable](
			columnPrivileges,
			t =>  t.userId === userId
		)
	}

	def getColumnPrivilege(userId: Long, databaseId: Long, tableId: Long, privilegeType: String) = {
		query[ColumnPrivilegeEntity, ColumnPrivilegeEntityTable](
			columnPrivileges,
			t =>  t.userId === userId && t.databaseId === databaseId && t.tableId === tableId && t.privilegeType === privilegeType
		)
	}

	def getColumnPrivilege(userId: Long, databaseId: Long, tableId: Long) = {
		query[ColumnPrivilegeEntity, ColumnPrivilegeEntityTable](
			columnPrivileges,
			t =>  t.userId === userId && t.databaseId === databaseId && t.tableId === tableId
		)
	}

	def getColumnPrivilege(userId: Long) = {
		query[ColumnPrivilegeEntity, ColumnPrivilegeEntityTable](
			columnPrivileges,
			t =>  t.userId === userId
		)
	}

}



























