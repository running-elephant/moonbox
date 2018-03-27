package moonbox.core.catalog

import moonbox.common.{MbConf, MbLogging}
import moonbox.core.catalog.jdbc.JdbcDao
import moonbox.core.config._

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global

class JdbcCatalog(conf: MbConf) extends AbstractCatalog with MbLogging {
	private val jdbcDao = new JdbcDao(conf)

	private def await[T](f: Future[T]): T = {
		Await.result(f, new FiniteDuration(conf.get(CATALOG_RESULT_AWAIT_TIMEOUT), MILLISECONDS))
	}

	// ----------------------------------------------------------------------------
	// Organization
	// ----------------------------------------------------------------------------

	override def doCreateOrganization(orgDefinition: CatalogOrganization, ignoreIfExists: Boolean): Unit = await {
		jdbcDao.organizationExists(orgDefinition.name).flatMap {
			case true =>
				ignoreIfExists match {
					case true => Future(Unit)
					case false => throw new OrganizationExistsException(orgDefinition.name)
				}
			case false =>
				jdbcDao.createOrganization(orgDefinition)
		}
	}

	override def doDropOrganization(org: String, ignoreIfNotExists: Boolean, cascade: Boolean): Unit = await {
		jdbcDao.getOrganization(org).flatMap {
			case Some(catalogOrganization) =>
				if (cascade) {
					for (
						_ <- jdbcDao.listGroups(catalogOrganization.id.get).flatMap { catalogGroups =>
							Future {
								catalogGroups.foreach { catalogGroup =>
									dropGroup(catalogOrganization.id.get, org, catalogGroup.name, ignoreIfNotExists = true, cascade = true)
								}
							}
						};
						_ <- jdbcDao.listDatabases(catalogOrganization.id.get).flatMap { catalogDatabases =>
							Future {
								catalogDatabases.foreach { catalogDatabase =>
									dropDatabase(catalogOrganization.id.get, org, catalogDatabase.name, ignoreIfNotExists = true, cascade = true)
								}
							}
						};
						_ <- jdbcDao.listUsers(catalogOrganization.id.get).flatMap { catalogUsers =>
							Future {
								catalogUsers.foreach { catalogUser =>
									dropUser(catalogOrganization.id.get, org, catalogUser.name, ignoreIfNotExists = true)
								}
							}
						};
						_ <- jdbcDao.deleteApplications(catalogOrganization.id.get)
					) yield jdbcDao.deleteOrganization(org)
				} else {
					for (
						groups <- jdbcDao.listGroups(catalogOrganization.id.get);
						databases <- jdbcDao.listDatabases(catalogOrganization.id.get);
						users <- jdbcDao.listUsers(catalogOrganization.id.get);
						applications <- jdbcDao.listApplications(catalogOrganization.id.get)
					) yield {
						if (groups.isEmpty && databases.isEmpty && users.isEmpty && applications.isEmpty) {
							jdbcDao.deleteOrganization(org)
						} else {
							throw new NonEmptyException(s"organization $org")
						}
					}
				}
			case None =>
				ignoreIfNotExists match {
					case true => Future(Unit)
					case false => throw new NoSuchOrganizationException(org)
				}
		}

	}

	override def doRenameOrganization(org: String, newOrg: String, updateBy: Long): Unit = await {
		jdbcDao.organizationExists(org).flatMap {
			case true =>
				jdbcDao.organizationExists(newOrg).flatMap {
					case false =>
						jdbcDao.renameOrganization(org, newOrg)(updateBy)
					case true => throw new OrganizationExistsException(newOrg)
				}
			case false =>
				throw new NoSuchOrganizationException(org)
		}
	}

	override def alterOrganization(orgDefinition: CatalogOrganization): Unit = await {
		jdbcDao.updateOrganization(orgDefinition)
	}

	override def getOrganization(org: String): CatalogOrganization = await {
		jdbcDao.getOrganization(org).map {
			case Some(catalogOrganization) => catalogOrganization
			case None => throw new NoSuchOrganizationException(org)
		}
	}

	override def getOrganization(org: Long): CatalogOrganization = await {
		jdbcDao.getOrganization(org).map {
			case Some(catalogOrganization) => catalogOrganization
			case None => throw new NoSuchOrganizationException(s"Id $org")
		}
	}

	override def getOrganizationOption(org: String): Option[CatalogOrganization] = await {
		jdbcDao.getOrganization(org)
	}

	override def getOrganizationOption(org: Long): Option[CatalogOrganization] = await {
		jdbcDao.getOrganization(org)
	}

	override def organizationExists(org: String): Boolean = await {
		jdbcDao.organizationExists(org)
	}

	override def listOrganizations(): Seq[CatalogOrganization] = await {
		jdbcDao.listOrganizations()
	}

	override def listOrganizations(pattern: String): Seq[CatalogOrganization] = await {
		jdbcDao.listOrganizations(pattern)
	}

	// ----------------------------------------------------------------------------
	// Group -- belong to organization
	// ----------------------------------------------------------------------------

	override def doCreateGroup(groupDefinition: CatalogGroup, ignoreIfExists: Boolean): Unit = await {
		jdbcDao.groupExists(groupDefinition.organizationId, groupDefinition.name).flatMap {
			case true =>
				ignoreIfExists match {
					case true => Future(Unit)
					case false => throw new GroupExistsException(groupDefinition.name)
				}
			case false =>
				jdbcDao.createGroup(groupDefinition)
		}
	}

	override def doDropGroup(organizationId: Long, group: String, ignoreIfNotExists: Boolean, cascade: Boolean): Unit = await {
		jdbcDao.getGroup(organizationId, group).flatMap {
			case Some(catalogGroup) =>
				if (cascade) {
					jdbcDao.userGroupRelExists(catalogGroup.id.get).flatMap {
						case true =>
							jdbcDao.clearUserGroupRels(catalogGroup.id.get)
							jdbcDao.deleteGroup(organizationId, group)
						case false =>
							jdbcDao.deleteGroup(organizationId, group)
					}
				} else {
					jdbcDao.userGroupRelExists(catalogGroup.id.get).flatMap {
						case true =>
							throw new NonEmptyException(s"Group $group")
						case false =>
							jdbcDao.deleteGroup(organizationId, group)
					}
				}
			case None =>
				ignoreIfNotExists match {
					case true => Future(Unit)
					case false => throw new NoSuchGroupException(group)
				}
		}
	}


	override def doRenameGroup(organizationId: Long, group: String, newGroup: String, updateBy: Long): Unit = await {
		jdbcDao.groupExists(organizationId, group).flatMap {
			case true =>
				jdbcDao.groupExists(organizationId, newGroup).flatMap {
					case false =>
						jdbcDao.renameGroup(organizationId, group, newGroup)(updateBy)
					case true =>
						throw new GroupExistsException(newGroup)
				}
			case false =>
				throw new NoSuchGroupException(group)
		}
	}

	override def alterGroup(groupDefinition: CatalogGroup): Unit = await {
		jdbcDao.updateGroup(groupDefinition)
	}

	override def getGroup(organizationId: Long, group: String): CatalogGroup = await {
		jdbcDao.getGroup(organizationId, group).map {
			case Some(groupOrganization) => groupOrganization
			case None => throw new NoSuchGroupException(group)
		}
	}

	override def getGroup(groupId: Long): CatalogGroup = await {
		jdbcDao.getGroup(groupId).map {
			case Some(groupOrganization) => groupOrganization
			case None => throw new NoSuchGroupException(s"Id $groupId")
		}
	}

	override def getGroups(organizationId: Long, groups: Seq[String]): Seq[CatalogGroup] = await {
		jdbcDao.getGroups(organizationId, groups)
	}

	override def getGroupOption(organizationId: Long, group: String): Option[CatalogGroup] = await {
		jdbcDao.getGroup(organizationId, group)
	}

	override def getGroupOption(groupId: Long): Option[CatalogGroup] = await {
		jdbcDao.getGroup(groupId)
	}

	override def groupExists(organizationId: Long, group: String): Boolean = await {
		jdbcDao.groupExists(organizationId, group)
	}

	override def listGroups(organizationId: Long): Seq[CatalogGroup] = await {
		jdbcDao.listGroups(organizationId)
	}

	override def listGroups(organizationId: Long, pattern: String): Seq[CatalogGroup] = await {
		jdbcDao.listGroups(organizationId, pattern)
	}

	// ----------------------------------------------------------------------------
	// User -- belong to organization
	// ----------------------------------------------------------------------------

	override def doCreateUser(userDefinition: CatalogUser, ignoreIfExists: Boolean): Unit = await {
		jdbcDao.userExists(userDefinition.name).flatMap {
			case true =>
				ignoreIfExists match {
					case true => Future(Unit)
					case false => throw new UserExistsException(userDefinition.name)
				}
			case false =>
				jdbcDao.createUser(userDefinition)
		}
	}

	override def doDropUser(organizationId: Long, organization: String, user: String, ignoreIfNotExists: Boolean): Unit = await {
		jdbcDao.getUser(organizationId, user).flatMap {
			case Some(catalogUser) =>
				jdbcDao.listUserGroupRels(organizationId).flatMap { rels =>
					rels.foreach { rel =>
						dropUserGroupRel(rel.copy(
							users = Seq(catalogUser.id.get)
						), organization, getGroup(rel.groupId).name, Seq(user))
					}
					jdbcDao.deleteUser(user)
				}
			case None =>
				ignoreIfNotExists match {
					case true => Future(Unit)
					case false => throw new NoSuchUserException(s"$user in your organization.")
				}
		}
	}

	override def doRenameUser(organizationId: Long, user: String, newUser: String, updateBy: Long): Unit = await {
		jdbcDao.userExists(organizationId, user).flatMap {
			case true =>
				jdbcDao.userExists(organizationId, newUser).flatMap {
					case false =>
						jdbcDao.renameUser(user, newUser)(updateBy)
					case true =>
						throw new UserExistsException(newUser)
				}
			case false =>
				throw new NoSuchUserException(user)
		}
	}

	override def alterUser(userDefinition: CatalogUser): Unit = await {
		jdbcDao.updateUser(userDefinition)
	}

	override def getUser(organizationId: Long, user: String): CatalogUser = await {
		jdbcDao.getUser(organizationId, user).map {
			case Some(u) => u
			case None => throw new NoSuchUserException(s"$user in your organization")
		}
	}

	override def getUsers(organizationId: Long, users: Seq[String]): Seq[CatalogUser] = await {
		jdbcDao.getUsers(organizationId, users)
	}

	override def getUser(user: Long): CatalogUser = await {
		jdbcDao.getUser(user).map {
			case Some(u) => u
			case None => throw new NoSuchUserException(s"Id $user")
		}
	}

	override def getUserOption(username: String): Option[CatalogUser] = await {
		jdbcDao.getUser(username)
	}

	override def getUserOption(organizationId: Long, user: String): Option[CatalogUser] = await {
		jdbcDao.getUser(organizationId, user)
	}

	override def getUserOption(user: Long): Option[CatalogUser] = await {
		jdbcDao.getUser(user)
	}

	override def userExists(organizationId: Long, user: String): Boolean = await {
		jdbcDao.userExists(organizationId, user)
	}

	override def listUsers(organizationId: Long): Seq[CatalogUser] = await {
		jdbcDao.listUsers(organizationId)
	}

	override def listUsers(organizationId: Long, pattern: String): Seq[CatalogUser] = await {
		jdbcDao.listUsers(organizationId, pattern)
	}

	// ----------------------------------------------------------------------------
	// Datasource -- belong to organization
	// ----------------------------------------------------------------------------

	override def doCreateDatasource(dsDefinition: CatalogDatasource, ignoreIfExists: Boolean): Unit = await {
		jdbcDao.datasourceExists(dsDefinition.organizationId, dsDefinition.name).flatMap {
			case true =>
				ignoreIfExists match {
					case true => Future(Unit)
					case false => throw new DatasourceExistsException(dsDefinition.name)
				}
			case false =>
				jdbcDao.createDatasource(dsDefinition)
		}
	}

	override def doDropDatasource(organizationId: Long, datasoruce: String, ignoreIfNotExists: Boolean): Unit = await {
		jdbcDao.datasourceExists(organizationId, datasoruce).flatMap {
			case true =>
				jdbcDao.deleteDatasource(organizationId, datasoruce)
			case false =>
				ignoreIfNotExists match {
					case true => Future(Unit)
					case false => throw new NoSuchDatasourceException(datasoruce)
				}
		}
	}

	override def doRenameDatasource(organizationId: Long, ds: String, newDs: String, updateBy: Long): Unit = await {
		jdbcDao.datasourceExists(organizationId, ds).flatMap {
			case true =>
				jdbcDao.datasourceExists(organizationId, newDs).flatMap {
					case false =>
						jdbcDao.renameDatasource(organizationId, ds, newDs)(updateBy)
					case true =>
						throw new DatasourceExistsException(newDs)
				}
			case false =>
				throw new NoSuchDatasourceException(ds)
		}
	}

	override def alterDatasource(dsDefinition: CatalogDatasource): Unit = await {
		jdbcDao.updateDatasource(dsDefinition)
	}

	override def getDatasource(organizationId: Long, datasoruce: String): CatalogDatasource = await {
		jdbcDao.getDatasource(organizationId, datasoruce).map {
			case Some(ds) => ds
			case None => throw new NoSuchDatasourceException(datasoruce)
		}
	}

	override def getDatasource(datasourceId: Long): CatalogDatasource = await {
		jdbcDao.getDatasource(datasourceId).map {
			case Some(ds) => ds
			case None => throw new NoSuchDatasourceException(s"Id $datasourceId")
		}
	}

	override def getDatasourceOption(organizationId: Long, datasoruce: String): Option[CatalogDatasource] = await {
		jdbcDao.getDatasource(organizationId, datasoruce)
	}

	override def getDatasourceOption(organizationId: Long): Option[CatalogDatasource] = await {
		jdbcDao.getDatasource(organizationId)
	}

	override def datasourceExists(organizationId: Long, datasoruce: String): Boolean = await {
		jdbcDao.datasourceExists(organizationId, datasoruce)
	}

	override def listDatasources(organizationId: Long): Seq[CatalogDatasource] = await {
		jdbcDao.listDatasources(organizationId)
	}

	override def listDatasources(organizationId: Long, pattern: String): Seq[CatalogDatasource] = await {
		jdbcDao.listDatasources(organizationId, pattern)
	}

	// ----------------------------------------------------------------------------
	// Application -- belong to organization
	// ----------------------------------------------------------------------------

	override def doCreateApplication(appDefinition: CatalogApplication, ignoreIfExists: Boolean): Unit = await {
		jdbcDao.applicationExists(appDefinition.organizationId, appDefinition.name).flatMap {
			case true =>
				ignoreIfExists match {
					case true => Future(Unit)
					case false => throw new ApplicationExistsException(appDefinition.name)
				}
			case false => jdbcDao.createApplication(appDefinition)
		}
	}

	override def doDropApplication(organizationId: Long, app: String, ignoreIfNotExists: Boolean): Unit = await {
		jdbcDao.applicationExists(organizationId, app).flatMap {
			case true =>
				jdbcDao.deleteApplication(organizationId, app)
			case false =>
				ignoreIfNotExists match {
					case true => Future(Unit)
					case false => throw new NoSuchApplicationException(app)
				}
		}
	}

	override def doRenameApplication(organizationId: Long, app: String, newApp: String, updateBy: Long): Unit = await {
		jdbcDao.applicationExists(organizationId, app).flatMap {
			case true =>
				jdbcDao.applicationExists(organizationId, newApp).flatMap {
					case false =>
						jdbcDao.renameApplication(organizationId, app, newApp)(updateBy)
					case true =>
						throw new ApplicationExistsException(newApp)
				}
			case false =>
				throw new NoSuchApplicationException(app)
		}
	}

	override def alterApplication(appDefinition: CatalogApplication): Unit = await {
		jdbcDao.updateApplication(appDefinition)
	}

	override def getApplication(organizationId: Long, app: String): CatalogApplication = await {
		jdbcDao.getApplication(organizationId, app).map {
			case Some(a) => a
			case None => throw new NoSuchApplicationException(app)
		}
	}

	override def getApplication(app: Long): CatalogApplication = await {
		jdbcDao.getApplication(app).map {
			case Some(a) => a
			case None => throw new NoSuchApplicationException(s"Id $app")
		}
	}

	override def getApplicationOption(organizationId: Long, app: String): Option[CatalogApplication] = await {
		jdbcDao.getApplication(organizationId, app)
	}

	override def getApplicationOption(app: Long): Option[CatalogApplication] = await {
		jdbcDao.getApplication(app)
	}

	override def applicationExists(organizationId: Long, app: String): Boolean = await {
		jdbcDao.applicationExists(organizationId, app)
	}

	override def listApplications(organizationId: Long): Seq[CatalogApplication] = await {
		jdbcDao.listApplications(organizationId)
	}

	override def listApplications(organizationId: Long, pattern: String): Seq[CatalogApplication] = await {
		jdbcDao.listApplications(organizationId, pattern)
	}

	// ----------------------------------------------------------------------------
	// Database -- belong to organization
	// ----------------------------------------------------------------------------

	override def doCreateDatabase(dbDefinition: CatalogDatabase, ignoreIfExists: Boolean): Unit = await {
		jdbcDao.databaseExists(dbDefinition.organizationId, dbDefinition.name).flatMap {
			case true =>
				ignoreIfExists match {
					case true => Future(Unit)
					case false => throw new DatabaseExistsException(dbDefinition.name)
				}
			case false => jdbcDao.createDatabase(dbDefinition)
		}
	}

	override def doDropDatabase(organizationId: Long, database: String, ignoreIfNotExists: Boolean, cascade: Boolean): Unit = await {
		jdbcDao.getDatabase(organizationId, database).flatMap {
			case Some(db) =>
				if (cascade) {
					for (
						x <- jdbcDao.deleteViews(db.id.get);
						y <- jdbcDao.deleteFunctions(db.id.get);
						z <- jdbcDao.deleteTables(db.id.get)
					) yield jdbcDao.deleteDatabase(organizationId, database)
				} else {
					for (
						tables <- jdbcDao.listTables(db.id.get);
						views <- jdbcDao.listViews(db.id.get);
						functions <- jdbcDao.listFunctions(db.id.get)
					) yield {
						if (tables.isEmpty && views.isEmpty && functions.isEmpty) {
							jdbcDao.deleteDatabase(organizationId, database)
						}
						else throw new NonEmptyException(s"Database $database")
					}
				}
			case None =>
				ignoreIfNotExists match {
					case true => Future(Unit)
					case false => throw new NoSuchDatabaseException(database)
				}

		}
	}

	override def doRenameDatabase(organizationId: Long, db: String, newDb: String, updateBy: Long): Unit = await {
		jdbcDao.databaseExists(organizationId, db).flatMap {
			case true =>
				jdbcDao.databaseExists(organizationId, newDb).flatMap {
					case false =>
						jdbcDao.renameDatabase(organizationId, db, newDb)(updateBy)
					case true =>
						throw new DatabaseExistsException(newDb)
				}
			case false =>
				throw new NoSuchDatabaseException(db)
		}
	}

	override def alterDatabase(dbDefinition: CatalogDatabase): Unit = await {
		jdbcDao.updateDatabase(dbDefinition)
	}

	override def getDatabase(organizationId: Long, database: String): CatalogDatabase = await {
		jdbcDao.getDatabase(organizationId, database).map {
			case Some(db) => db
			case None => throw new NoSuchDatabaseException(database)
		}
	}

	override def getDatabase(id: Long): CatalogDatabase = await {
		jdbcDao.getDatabase(id).map {
			case Some(db) => db
			case None => throw new NoSuchDatabaseException(s"Id $id")
		}
	}

	override def getDatabaseOption(organizationId: Long, database: String): Option[CatalogDatabase] = await {
		jdbcDao.getDatabase(organizationId, database)
	}

	override def getDatabaseOption(id: Long): Option[CatalogDatabase] = await {
		jdbcDao.getDatabase(id)
	}

	override def databaseExists(organizationId: Long, database: String): Boolean = await {
		jdbcDao.databaseExists(organizationId, database)
	}

	override def listDatabases(organizationId: Long): Seq[CatalogDatabase] = await {
		jdbcDao.listDatabases(organizationId)
	}

	override def listDatabases(organizationId: Long, pattern: String): Seq[CatalogDatabase] = await {
		jdbcDao.listDatabases(organizationId, pattern)
	}

	// ----------------------------------------------------------------------------
	// Table -- belong to database
	// ----------------------------------------------------------------------------

	override def doCreateTable(tableDefinition: CatalogTable, ignoreIfExists: Boolean): Unit = await {
		jdbcDao.getDatabase(tableDefinition.databaseId).flatMap {
			case Some(database) =>
				jdbcDao.tableExists(tableDefinition.databaseId, tableDefinition.name).flatMap {
					case true =>
						ignoreIfExists match {
							case true => Future(Unit)
							case false =>
								throw new TableExistsException(
									database.name,
									tableDefinition.name)
						}
					case false =>
						jdbcDao.createTable(tableDefinition)
				}
			case None =>
				throw new NoSuchDatabaseException(s"Id ${tableDefinition.databaseId}")
		}

	}

	override def doDropTable(databaseId: Long, table: String, ignoreIfNotExists: Boolean): Unit = await {
		jdbcDao.tableExists(databaseId, table).flatMap {
			case true =>
				jdbcDao.deleteTable(databaseId, table)
			case false =>
				ignoreIfNotExists match {
					case true => Future(Unit)
					case false => throw new NoSuchTableException(getDatabase(databaseId).name, table)
				}
		}
	}

	override def doRenameTable(databaseId: Long, table: String, newTable: String, updateBy: Long): Unit = await {
		jdbcDao.tableExists(databaseId, table).flatMap {
			case true =>
				jdbcDao.tableExists(databaseId, newTable).flatMap {
					case false =>
						jdbcDao.renameTable(databaseId, table, newTable)(updateBy)
					case true =>
						throw new TableExistsException(getDatabase(databaseId).name, newTable)
				}
			case false =>
				throw new NoSuchTableException(getDatabase(databaseId).name, table)
		}
	}

	override def alterTable(tableDefinition: CatalogTable): Unit = await {
		jdbcDao.updateTable(tableDefinition)
	}

	override def getTable(databaseId: Long, table: String): CatalogTable = await {
		jdbcDao.getTable(databaseId, table).map {
			case Some(t) => t
			case None => throw new NoSuchTableException(getDatabase(databaseId).name, table)
		}
	}

	override def getTable(tableId: Long): CatalogTable = await {
		jdbcDao.getTable(tableId).map {
			case Some(t) => t
			case None => throw new NoSuchTableException("", s"Id $tableId")
		}
	}

	override def getTableOption(databaseId: Long, table: String): Option[CatalogTable] = await {
		jdbcDao.getTable(databaseId, table)
	}

	override def getTableOption(tableId: Long): Option[CatalogTable] = await {
		jdbcDao.getTable(tableId)
	}

	override def tableExists(databaseId: Long, table: String): Boolean = await {
		jdbcDao.tableExists(databaseId, table)
	}

	override def listTables(databaseId: Long): Seq[CatalogTable] = await {
		jdbcDao.listTables(databaseId)
	}

	override def listTables(databaseId: Long, pattern: String): Seq[CatalogTable] = await {
		jdbcDao.listTables(databaseId, pattern)
	}

	// ----------------------------------------------------------------------------
	// Column -- belong to table
	// ----------------------------------------------------------------------------

	override def createColumns(columnDefinition: Seq[CatalogColumn], ignoreIfExists: Boolean): Unit = await {
		val groupedColumns = columnDefinition.groupBy(col => columnExists(col.tableId, col.name))
		groupedColumns.get(true) match {
			case Some(exists) =>
				ignoreIfExists match {
					case true => Future(Unit)
					case false => throw new ColumnExistsException(
						getTable(exists.head.tableId).name, exists.head.name)
				}
			case None =>
				jdbcDao.createColumns(columnDefinition)
		}
	}

	override def dropColumn(tableId: Long, column: String, ignoreIfNotExists: Boolean): Unit = await {
		jdbcDao.columnExists(tableId, column).flatMap {
			case true =>
				ignoreIfNotExists match {
					case true => Future(Unit)
					case false => throw new NoSuchColumnException(
						getTable(tableId).name, column
					)
				}
			case false =>
				jdbcDao.deleteColumn(tableId, column)
		}
	}

	override def dropColumns(tableId: Long): Unit = await {
		jdbcDao.deleteColumns(tableId)
	}

	override def alterColumn(columnDefinition: CatalogColumn): Unit = await {
		jdbcDao.updateColumn(columnDefinition)
	}

	override def getColumn(tableId: Long, column: String): CatalogColumn = await {
		jdbcDao.getColumn(tableId, column).map {
			case Some(col) => col
			case None => throw new NoSuchColumnException(getTable(tableId).name, column)
		}
	}

	override def getColumn(column: Long): CatalogColumn = await {
		jdbcDao.getColumn(column).map {
			case Some(col) => col
			case None => throw new NoSuchColumnException("", s"Id $column")
		}
	}

	override def getColumnOption(tableId: Long, column: String): Option[CatalogColumn] = await {
		jdbcDao.getColumn(tableId, column)
	}

	override def getColumnOption(column: Long): Option[CatalogColumn] = await {
		jdbcDao.getColumn(column)
	}

	override def getColumns(tableId: Long, columns: Seq[String]): Seq[CatalogColumn] = await {
		jdbcDao.getColumns(tableId, columns)
	}

	override def columnExists(tableId: Long, column: String): Boolean = await {
		jdbcDao.columnExists(tableId, column)
	}

	override def listColumns(tableId: Long): Seq[CatalogColumn] = await {
		jdbcDao.listColumns(tableId)
	}

	// ----------------------------------------------------------------------------
	// Function -- belong to database
	// ----------------------------------------------------------------------------

	override def doCreateFunction(funcDefinition: CatalogFunction, ignoreIfExists: Boolean): Unit = await {
		jdbcDao.getDatabase(funcDefinition.databaseId).flatMap {
			case Some(database) =>
				jdbcDao.functionExists(funcDefinition.databaseId, funcDefinition.name).flatMap {
					case true =>
						ignoreIfExists match {
							case true => Future(Unit)
							case false => throw new FunctionExistsException(
								database.name, funcDefinition.name)
						}
					case false =>
						jdbcDao.createFunction(funcDefinition)
				}
			case None =>
				throw new NoSuchDatabaseException(s"Id ${funcDefinition.databaseId}")
		}


	}

	override def doDropFunction(databaseId: Long, func: String, ignoreIfNotExists: Boolean): Unit = await {
		jdbcDao.functionExists(databaseId, func).flatMap {
			case true =>
				jdbcDao.deleteFunction(databaseId, func)
			case false =>
				ignoreIfNotExists match {
					case true => Future(Unit)
					case false => throw new NoSuchFunctionException(getDatabase(databaseId).name, func)
				}
		}
	}

	override def doRenameFunction(databaseId: Long, func: String, newFunc: String, updateBy: Long): Unit = await {
		jdbcDao.functionExists(databaseId, func).flatMap {
			case true =>
				jdbcDao.functionExists(databaseId, newFunc).flatMap {
					case false =>
						jdbcDao.renameFunction(databaseId, func, newFunc)(updateBy)
					case true =>
						throw new FunctionExistsException(getDatabase(databaseId).name, newFunc)
				}
			case false =>
				throw new NoSuchFunctionException(getDatabase(databaseId).name, func)
		}
	}

	override def alterFunction(funcDefinition: CatalogFunction): Unit = await {
		jdbcDao.updateFunction(funcDefinition)
	}

	override def getFunction(databaseId: Long, func: String): CatalogFunction = await {
		jdbcDao.getFunction(databaseId, func).map {
			case Some(f) => f
			case None => throw new NoSuchFunctionException(getDatabase(databaseId).name, func)
		}
	}

	override def getFunction(func: Long): CatalogFunction = await {
		jdbcDao.getFunction(func).map {
			case Some(f) => f
			case None => throw new NoSuchFunctionException("", s"Id $func")
		}
	}

	override def getFunctionOption(databaseId: Long, func: String): Option[CatalogFunction] = await {
		jdbcDao.getFunction(databaseId, func)
	}

	override def getFunctionOption(func: Long): Option[CatalogFunction] = await {
		jdbcDao.getFunction(func)
	}

	override def functionExists(databaseId: Long, func: String): Boolean = await {
		jdbcDao.functionExists(databaseId, func)
	}

	override def listFunctions(databaseId: Long): Seq[CatalogFunction] = await {
		jdbcDao.listFunctions(databaseId)
	}

	override def listFunctions(databaseId: Long, pattern: String): Seq[CatalogFunction] = await {
		jdbcDao.listFunctions(databaseId, pattern)
	}

	// ----------------------------------------------------------------------------
	// View -- belong to database
	// ----------------------------------------------------------------------------

	override def doCreateView(viewDefinition: CatalogView, ignoreIfExists: Boolean): Unit = await {
		jdbcDao.getDatabase(viewDefinition.databaseId).flatMap {
			case Some(database) =>
				jdbcDao.viewExists(viewDefinition.databaseId, viewDefinition.name).flatMap {
					case true =>
						ignoreIfExists match {
							case true => Future(Unit)
							case false => throw new ViewExistsException(
								database.name, viewDefinition.name)
						}
					case false =>
						jdbcDao.createView(viewDefinition)
				}
			case None =>
				throw new NoSuchDatabaseException(s"Id ${viewDefinition.databaseId}")
		}

	}

	override def doDropView(databaseId: Long, view: String, ignoreIfNotExists: Boolean): Unit = await {
		jdbcDao.viewExists(databaseId, view).flatMap {
			case true =>
				jdbcDao.deleteView(databaseId, view)
			case false =>
				ignoreIfNotExists match {
					case true => Future(Unit)
					case false => throw new NoSuchViewException(getDatabase(databaseId).name, view)
				}
		}
	}

	override def doRenameView(databaseId: Long, view: String, newView: String, updateBy: Long): Unit = await {
		jdbcDao.viewExists(databaseId, view).flatMap {
			case true =>
				jdbcDao.viewExists(databaseId, newView).flatMap {
					case false =>
						jdbcDao.renameView(databaseId, view, newView)(updateBy)
					case true =>
						throw new ViewExistsException(getDatabase(databaseId).name, newView)
				}
			case false =>
				throw new NoSuchViewException(getDatabase(databaseId).name, view)
		}
	}

	override def alterView(viewDefinition: CatalogView): Unit = await {
		jdbcDao.updateView(viewDefinition)
	}

	override def getView(databaseId: Long, view: String): CatalogView = await {
		jdbcDao.getView(databaseId, view).map {
			case Some(v) => v
			case None => throw new NoSuchViewException(getDatabase(databaseId).name, view)
		}
	}

	override def getView(view: Long): CatalogView = await {
		jdbcDao.getView(view).map {
			case Some(v) => v
			case None => throw new NoSuchViewException("", s"Id $view")
		}
	}

	override def getViewOption(databaseId: Long, view: String): Option[CatalogView] = await {
		jdbcDao.getView(databaseId, view)
	}

	override def getViewOption(view: Long): Option[CatalogView] = await {
		jdbcDao.getView(view)
	}

	override def viewExists(databaseId: Long, view: String): Boolean = await {
		jdbcDao.viewExists(databaseId, view)
	}

	override def listViews(databaseId: Long): Seq[CatalogView] = await {
		jdbcDao.listViews(databaseId)
	}

	override def listViews(databaseId: Long, pattern: String): Seq[CatalogView] = await {
		jdbcDao.listViews(databaseId, pattern)
	}

	// ----------------------------------------------------------------------------
	// UserGroupRel --   the relation of user - group
	// ----------------------------------------------------------------------------

	override def doCreateUserGroupRel(userGroupRels: CatalogUserGroupRel): Unit = await {
		jdbcDao.createUserGroupRel(userGroupRels)
	}

	override def doDropUserGroupRel(userGroupRels: CatalogUserGroupRel): Unit = await {
		jdbcDao.deleteUserGroupRel(userGroupRels)
	}

	override def getUserGroupRels(groupId: Long): CatalogUserGroupRel = await {
		jdbcDao.getUserGroupRel(groupId).map {
			case Some(rel) => rel
			case None => throw new NoSuchUserGroupRelException(getGroup(groupId).name)
		}
	}

	override def clearUserGroupRels(groupId: Long): Unit = await {
		jdbcDao.getUserGroupRel(groupId).map {
			case Some(rel) => jdbcDao.clearUserGroupRels(groupId)
			case None => throw new NoSuchUserGroupRelException(getGroup(groupId).name)
		}
	}

	// ----------------------------------------------------------------------------
	// UserTableRel --   the relation of user - table - column
	// ----------------------------------------------------------------------------

	override def doCreateUserTableRel(userTableRelDefinition: CatalogUserTableRel, ignoreIfExists: Boolean): Unit = await {
		jdbcDao.createUserTableRel(userTableRelDefinition)
	}

	override def doDropUserTableRel(userTableRelDefinition: CatalogUserTableRel, ignoreIfNotExists: Boolean): Unit = await {
		jdbcDao.deleteUserTableRel(userTableRelDefinition)
	}

	override def getUserTableRel(userId: Long, tableId: Long): CatalogUserTableRel = await {
		jdbcDao.getUserTableRel(userId, tableId).map {
			case Some(rel) => rel
			case None => throw new NoSuchUserTableRelException(getUser(userId).name, getTable(tableId).name)
		}
	}

	override def getUserTableRelOption(userId: Long, tableId: Long): Option[CatalogUserTableRel] = await {
		jdbcDao.getUserTableRel(userId, tableId)
	}

	override def userTableRelExists(userId: Long, tableId: Long): Boolean = await {
		jdbcDao.userTableRelExists(userId, tableId)
	}

}
