package moonbox.core.catalog

import moonbox.common.{MbConf, MbLogging}
import moonbox.core.{CatalogContext, MbSession}
import moonbox.core.config._
import org.apache.spark.sql.types.StructType
import org.scalatest.FunSuite

class CatalogContextSuite extends FunSuite with MbLogging {

	private val conf: MbConf = new MbConf()
		.set(CATALOG_IMPLEMENTATION.key, "h2")
		.set(CATALOG_URL.key, "jdbc:h2:mem:testdb0;DB_CLOSE_DELAY=-1")
		.set(CATALOG_USER.key, "testUser")
		.set(CATALOG_PASSWORD.key, "testPass")
		.set(CATALOG_DRIVER.key, "org.h2.Driver")


	val catalog = new CatalogContext(conf)
	catalog.addListener(new CatalogEventListener {
		override def onEvent(event: CatalogEvent): Unit = event match {
			case e => logInfo(e.toString)
		}
	})

	test("organization") {
		catalog.createOrganization(CatalogOrganization(
			name = "org1",
			description = Some("for testing"),
			createBy = 1,
			updateBy = 1
		), ignoreIfExists = true)
		val organization = catalog.getOrganization("org1")
		assert(organization.name == "org1")
		assert(organization.description.contains("for testing"))
		assert(organization.createBy == 1)
		assert(organization.updateBy == 1)

		intercept[OrganizationExistsException] {
			catalog.createOrganization(CatalogOrganization(
				name = "org1",
				description = Some("for testing"),
				createBy = 1,
				updateBy = 1
			), ignoreIfExists = false)
		}

		catalog.renameOrganization("org1", "org", 1)
		intercept[NoSuchOrganizationException](
			catalog.getOrganization("org1")
		)
		assert(catalog.getOrganization("org") != null)

		val existsOrganization = catalog.getOrganization("org")
		catalog.alterOrganization(
			existsOrganization.copy(name = "org1", description = Some("for fun"))
		)
		val organization1 = catalog.getOrganization("org1")
		assert(organization1 != null)
		assert(organization1.description.contains("for fun"))

		assert(catalog.listOrganizations().size == 1)
		assert(catalog.listOrganizations("abc").isEmpty)

		catalog.dropOrganization("org1", ignoreIfNotExists = true, cascade = true)
		assert(!catalog.organizationExists("org1"))
		intercept[NoSuchOrganizationException] {
			catalog.dropOrganization("org1", ignoreIfNotExists = false, cascade = true)
		}
	}

	test("user") {
		catalog.createUser(CatalogUser(
			name = "sa",
			password = "123456",
			account = true,
			ddl = true,
			dcl = true,
			grantAccount = true,
			grantDdl = true,
			grantDcl = true,
			isSA = true,
			organizationId = 1,
			createBy = 1,
			updateBy = 1
		), "org1", ignoreIfExists = true)

		val sa = catalog.getUser(1, "sa")
		assert(sa.name == "sa")
		assert(sa.password == PasswordEncryptor.encryptSHA("123456"))
		assert(sa.account)
		assert(sa.ddl)
		assert(sa.dcl)
		assert(sa.grantAccount)
		assert(sa.grantDdl)
		assert(sa.grantDcl)
		assert(sa.isSA)
		assert(sa.createBy == 1)
		assert(sa.updateBy == 1)

		intercept[UserExistsException] {
			catalog.createUser(CatalogUser(
				name = "sa",
				password = "123456",
				account = true,
				ddl = true,
				dcl = true,
				grantAccount = true,
				grantDdl = true,
				grantDcl = true,
				isSA = true,
				organizationId = 1,
				createBy = 1,
				updateBy = 1
			), "org1", ignoreIfExists = false)
		}

		catalog.renameUser(1, "org1", "sa", "sa1", 2)
		intercept[NoSuchUserException] {
			catalog.getUser(1, "sa")
		}
		val sa1 = catalog.getUser(1, "sa1")
		assert(sa1.name == "sa1")
		assert(sa1.updateBy == 2)

		catalog.alterUser(
			sa1.copy(updateBy = 1)
		)

		catalog.dropUser(1, "org1", "sa1", ignoreIfNotExists = true)
		assert(!catalog.userExists(1, "sa1"))
		intercept[NoSuchUserException] {
			catalog.dropUser(1, "org1", "sa1", ignoreIfNotExists = false)
		}
	}

	test("group") {
		catalog.createGroup(CatalogGroup(
			name = "group",
			description = Some("for testing"),
			organizationId = 1,
			createBy = 1,
			updateBy = 1
		), "org1", ignoreIfExists = true)
		val group = catalog.getGroup(1, "group")
		assert(group.name == "group")
		assert(group.description.contains("for testing"))
		assert(group.organizationId == 1)
		assert(group.createBy == 1)
		assert(group.updateBy == 1)

		intercept[GroupExistsException] {
			catalog.createGroup(CatalogGroup(
				name = "group",
				description = Some("for testing"),
				organizationId = 1,
				createBy = 1,
				updateBy = 1
			), "org1", ignoreIfExists = false)
		}
		catalog.renameGroup(1, "org1", "group", "group1", 2)
		intercept[NoSuchGroupException] {
			catalog.getGroup(1, "group")
		}
		val group1 = catalog.getGroup(1, "group1")
		assert(group1.name == "group1")
		assert(group1.updateBy == 2)

		catalog.alterGroup(group1.copy(description = Some("for fun"), updateBy = 1))

		assert(catalog.getGroup(1, "group1").description.contains("for fun"))
		catalog.dropGroup(1, "org1", "group1", ignoreIfNotExists = true, cascade = false)
		assert(!catalog.groupExists(1, "group1"))
		intercept[NoSuchGroupException] {
			catalog.dropGroup(1, "org1", "group1", ignoreIfNotExists = false, cascade = false)
		}
	}

	test("database and table") {
		catalog.createDatabase(CatalogDatabase(
			name = "db",
			description = Some("for testing"),
			organizationId = 1,
			properties = Map(),
			isLogical = true,
			createBy = 1,
			updateBy = 1
		), "org1", ignoreIfExists = true)
		val db = catalog.getDatabase(1, "db")
		assert(db.name == "db")
		assert(db.description.contains("for testing"))
		assert(db.organizationId == 1)
		assert(db.createBy == 1)
		assert(db.updateBy == 1)

		intercept[DatabaseExistsException] {
			catalog.createDatabase(CatalogDatabase(
				name = "db",
				description = Some("for testing"),
				organizationId = 1,
				properties = Map(),
				isLogical = true,
				createBy = 1,
				updateBy = 1
			), "org1", ignoreIfExists = false)
		}

		catalog.renameDatabase(1, "org1", "db", "db1", 2)

		intercept[NoSuchDatabaseException] {
			catalog.getDatabase(1, "db")
		}
		val db1 = catalog.getDatabase(1, "db1")
		assert(db1.name == "db1")
		assert(db1.updateBy == 2)

		catalog.alterDatabase(db1.copy(description = Some("for fun"), updateBy = 1))

		assert(catalog.getDatabase(1, "db1").description.contains("for fun"))

		catalog.createTable(CatalogTable(
			name = "table",
			description = Some("for testing"),
			databaseId = db1.id.get,
			properties = Map("key1" -> "value1"),
			isStream = false,
			createBy = 1,
			updateBy = 1
		), "org1", "db1", ignoreIfExists = true)

		val table = catalog.getTable(db1.id.get, "table")
		assert(table.name == "table")
		assert(table.description.contains("for testing"))
		assert(table.databaseId == db1.id.get)
		assert(table.properties == Map("key1" -> "value1"))
		assert(table.createBy == 1)
		assert(table.updateBy == 1)

		intercept[TableExistsException] {
			catalog.createTable(CatalogTable(
				name = "table",
				description = Some("for testing"),
				databaseId = db1.id.get,
				properties = Map("key1" -> "value1"),
				isStream = false,
				createBy = 1,
				updateBy = 1
			),"org1", "db1", ignoreIfExists = false)
		}

		catalog.renameTable(db1.id.get, "org1", "db1","table", "table1", 2)

		intercept[NoSuchTableException] {
			catalog.getTable(db1.id.get, "table")
		}
		val table1 = catalog.getTable(db1.id.get, "table1")
		assert(table1.name == "table1")
		assert(table1.updateBy == 2)

		catalog.alterTable(table1.copy(description = Some("for fun"), properties = Map("key2" -> "value2"), updateBy = 1))

		assert(catalog.getTable(db1.id.get, "table1").description.contains("for fun"))
		assert(catalog.getTable(db1.id.get, "table1").properties == Map("key2" -> "value2"))
		catalog.dropTable(db1.id.get, "org1", "db1", "table1", ignoreIfNotExists = true)

		intercept[NoSuchTableException] {
			catalog.dropTable(db1.id.get, "org1", "db1", "table1", ignoreIfNotExists = false)
		}

		catalog.createTable(CatalogTable(
			name = "table2",
			description = Some("for testing"),
			databaseId = db1.id.get,
			properties = Map("key1" -> "value1"),
			isStream = false,
			createBy = 1,
			updateBy = 1
		), "org1", "db1", ignoreIfExists = true)

		intercept[NonEmptyException] {
			catalog.dropDatabase(1, "org1", "db1", ignoreIfNotExists = true, cascade = false)
		}

		assert(catalog.databaseExists(1, "db1"))

		catalog.dropDatabase(1, "org1", "db1", ignoreIfNotExists = false, cascade = true)

		intercept[NoSuchDatabaseException] {
			catalog.dropDatabase(1, "org1", "db1", ignoreIfNotExists = false, cascade = false)
		}
	}

	test("function") {
		catalog.createDatabase(CatalogDatabase(
			name = "db",
			description = Some("for testing"),
			organizationId = 1,
			properties = Map(),
			isLogical = true,
			createBy = 1,
			updateBy = 1
		), "org1", ignoreIfExists = true)

		val db = catalog.getDatabase(1, "db")

		catalog.createFunction(CatalogFunction(
			name = "function",
			databaseId = db.id.get,
			description = Some("for testing"),
			className = "className",
			methodName = None,
			resources = Seq(FunctionResource(JarResource, "hdfs://localhost:8020/jar")),
			createBy = 1,
			updateBy = 1
		), "org1", "db", ignoreIfExists = true)

		val function = catalog.getFunction(db.id.get, "function")
		assert(function.name == "function")
		assert(function.databaseId == db.id.get)
		assert(function.description.contains("for testing"))
		assert(function.className == "className")
		assert(function.methodName == None)
		assert(function.resources == Seq(FunctionResource(JarResource, "hdfs://localhost:8020/jar")))
		assert(function.createBy == 1)
		assert(function.updateBy == 1)

		intercept[FunctionExistsException] {
			catalog.createFunction(CatalogFunction(
				name = "function",
				databaseId = db.id.get,
				description = Some("for testing"),
				className = "className",
				methodName = None,
				resources = Seq(),
				createBy = 1,
				updateBy = 1
			), "org1", "db", ignoreIfExists = false)
		}

		catalog.renameFunction(db.id.get, "org1", "db", "function", "function1", 2)

		intercept[NoSuchFunctionException] {
			catalog.getFunction(db.id.get, "function")
		}

		intercept[NonEmptyException] {
			catalog.dropDatabase(1, "org1", "db", ignoreIfNotExists = true, cascade = false)
		}
		catalog.dropFunction(db.id.get, "org1", "db", "function", ignoreIfNotExists = true)

		assert(!catalog.functionExists(db.id.get, "function"))

		intercept[NoSuchFunctionException] {
			catalog.dropFunction(db.id.get, "org1", "db", "function", ignoreIfNotExists = false)
		}

		catalog.createFunction(CatalogFunction(
			name = "function",
			databaseId = db.id.get,
			description = Some("for testing"),
			className = "className",
			methodName = None,
			resources = Seq(FunctionResource(JarResource, "hdfs://localhost:8020/jar")),
			createBy = 1,
			updateBy = 1
		), "org1", "db", ignoreIfExists = true)

		catalog.dropDatabase(1, "org1", "db", ignoreIfNotExists = true, cascade = true)
		assert(!catalog.databaseExists(1, "db"))

	}

	test("view") {
		catalog.createDatabase(CatalogDatabase(
			name = "db",
			description = Some("for testing"),
			organizationId = 1,
			properties = Map(),
			isLogical = true,
			createBy = 1,
			updateBy = 1
		), "org1", ignoreIfExists = true)

		val db = catalog.getDatabase(1, "db")

		catalog.createView(CatalogView(
			name = "view",
			databaseId = db.id.get,
			description = Some("for testing"),
			cmd = "SELECT * FROM table",
			createBy = 1,
			updateBy = 1
		), "org1", "db", ignoreIfExists = true)
		val view = catalog.getView(db.id.get, "view")
		assert(view.name == "view")
		assert(view.description.contains("for testing"))
		assert(view.databaseId == db.id.get)
		assert(view.cmd == "SELECT * FROM table")
		assert(view.createBy == 1)
		assert(view.updateBy == 1)

		intercept[ViewExistsException] {
			catalog.createView(CatalogView(
				name = "view",
				databaseId = db.id.get,
				description = Some("for testing"),
				cmd = "SELECT * FROM table",
				createBy = 1,
				updateBy = 1
			), "org1", "db", ignoreIfExists = false)
		}

		catalog.renameView(db.id.get, "org1", "db", "view", "view1", 2)

		intercept[NoSuchViewException] {
			catalog.getView(db.id.get, "view")
		}

		val view1 = catalog.getView(db.id.get, "view1")
		catalog.alterView(view1.copy(
			name = "view",
			description = Some("for fun"),
			cmd = "SELECT * FROM table WHERE id < 100",
			updateBy = 1
		))

		assert(catalog.getView(db.id.get, "view").description.contains("for fun"))
		assert(catalog.getView(db.id.get, "view").updateBy == 1)
		assert(catalog.getView(db.id.get, "view").cmd == "SELECT * FROM table WHERE id < 100")

		intercept[NonEmptyException] {
			catalog.dropDatabase(1, "org1", "db", ignoreIfNotExists = true, cascade = false)
		}
		catalog.dropView(db.id.get, "org1", "db", "view", ignoreIfNotExists = true)

		assert(!catalog.viewExists(db.id.get, "view"))

		intercept[NoSuchViewException] {
			catalog.dropView(db.id.get, "org1", "db", "view", ignoreIfNotExists = false)
		}

		catalog.createView(CatalogView(
			name = "view",
			databaseId = db.id.get,
			description = Some("for testing"),
			cmd = "SELECT * FROM table",
			createBy = 1,
			updateBy = 1
		), "org1", "db", ignoreIfExists = false)

		catalog.dropDatabase(1, "org1", "db", ignoreIfNotExists = true, cascade = true)
		assert(!catalog.databaseExists(1, "db"))
	}

	test("application") {
		catalog.createApplication(CatalogApplication(
			name = "application",
			cmds = Seq("INSERT INTO TABLE table SELECT * FROM view"),
			organizationId = 1,
			description = Some("for testing"),
			createBy = 1,
			updateBy = 1
		), "org1", ignoreIfExists = true)
		val app = catalog.getApplication(1, "application")
		assert(app.cmds == Seq("INSERT INTO TABLE table SELECT * FROM view"))
		assert(app.description.contains("for testing"))
		assert(app.organizationId == 1)
		assert(app.createBy == 1)
		assert(app.updateBy == 1)

		intercept[ApplicationExistsException] {
			catalog.createApplication(CatalogApplication(
				name = "application",
				cmds = Seq("INSERT INTO TABLE table SELECT * FROM view"),
				organizationId = 1,
				description = Some("for testing"),
				createBy = 1,
				updateBy = 1
			), "org1", ignoreIfExists = false)
		}

		catalog.renameApplication(1, "org1", "application", "application1", 2)

		intercept[NoSuchApplicationException] {
			catalog.getApplication(1, "application")
		}

		val app1 = catalog.getApplication(1, "application1")
		catalog.alterApplication(app1.copy(
			name = "application",
			description = Some("for fun"),
			cmds = Seq(""),
			updateBy = 1
		))

		assert(catalog.getApplication(1, "application").description.contains("for fun"))
		assert(catalog.getApplication(1, "application").cmds == Seq(""))
		assert(catalog.getApplication(1, "application").updateBy == 1)

		catalog.dropApplication(1, "org1", "application", ignoreIfNotExists = true)

		assert(!catalog.applicationExists(1, "application"))

		intercept[NoSuchApplicationException] {
			catalog.dropApplication(1, "org1", "application", ignoreIfNotExists = false)
		}
	}


}
