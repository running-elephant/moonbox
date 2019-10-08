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

package moonbox.core

import moonbox.catalog.config._
import moonbox.catalog.{CatalogUser, NoSuchUserException, PasswordEncryptor, UserExistsException, _}
import moonbox.common.{MbConf, MbLogging}
import org.scalatest.FunSuite

class MoonboxCatalogSuite extends FunSuite with MbLogging {

	private val conf: MbConf = new MbConf()
		.set(CATALOG_IMPLEMENTATION.key, "h2")
		.set(JDBC_CATALOG_URL.key, "jdbc:h2:mem:testdb0;DB_CLOSE_DELAY=-1")
		.set(JDBC_CATALOG_USER.key, "testUser")
		.set(JDBC_CATALOG_PASSWORD.key, "testPass")
		.set(JDBC_CATALOG_DRIVER.key, "org.h2.Driver")


	val catalog = new MoonboxCatalog(conf)
	catalog.addListener(new CatalogEventListener {
		override def onEvent(event: CatalogEvent): Unit = event match {
			case e => logInfo(e.toString)
		}
	})

	test("organization") {
		catalog.setCurrentUser("SYSTEM", "ROOT")
		catalog.createOrganization(CatalogOrganization(
			name = "org1",
			config = Map("a" -> "b"),
			description = Some("for testing")
		), ignoreIfExists = true)
		val organization = catalog.getOrganization("org1")
		assert(organization.name == "org1")
		assert(organization.config == Map("a" -> "b"))
		assert(organization.description.contains("for testing"))

		intercept[OrganizationExistsException] {
			catalog.createOrganization(CatalogOrganization(
				name = "org1",
				config = Map(),
				description = Some("for testing")
			), ignoreIfExists = false)
		}

		catalog.renameOrganization("org1", "org")
		intercept[NoSuchOrganizationException](
			catalog.getOrganization("org1")
		)
		assert(catalog.getOrganization("org") != null)

		val existsOrganization = catalog.getOrganization("org")
		catalog.alterOrganization(
			existsOrganization.copy(description = Some("for fun"))
		)
		val organization1 = catalog.getOrganization("org")
		assert(organization1 != null)
		assert(organization1.description.contains("for fun"))

		assert(catalog.listOrganizations().size == 2)
		assert(catalog.listOrganizations("abc").isEmpty)

		catalog.dropOrganization("org1", ignoreIfNotExists = true, cascade = true)
		assert(!catalog.organizationExists("org1"))
		intercept[NoSuchOrganizationException] {
			catalog.dropOrganization("org1", ignoreIfNotExists = false, cascade = true)
		}

		catalog.createUser(CatalogUser(
			org = "org",
			name = "sa",
			password = "123456",
			account = true,
			ddl = true,
			dcl = true,
			grantAccount = true,
			grantDdl = true,
			grantDcl = true,
			isSA = true,
			configuration = Map()
		), ignoreIfExists = true)

		intercept[UserExistsException] {
			catalog.createUser(CatalogUser(
				org = "org",
				name = "sa",
				password = "123456",
				account = true,
				ddl = true,
				dcl = true,
				grantAccount = true,
				grantDdl = true,
				grantDcl = true,
				isSA = true,
				configuration = Map()
			), ignoreIfExists = false)
		}

		val sa = catalog.getUser("org", "sa")
		assert(sa.name == "sa")
		assert(sa.password == "123456")
		assert(sa.account)
		assert(sa.ddl)
		assert(sa.dcl)
		assert(sa.grantAccount)
		assert(sa.grantDdl)
		assert(sa.grantDcl)
		assert(sa.isSA)
		assert(sa.createBy.map(_.toUpperCase()).contains("ROOT"))
	}

	test("user") {
		catalog.setCurrentUser("org", "sa")
		catalog.createUser(CatalogUser(
			org = "org",
			name = "user1",
			password = "123456"
		), ignoreIfExists = true)

		val sa = catalog.getUser("org", "user1")
		assert(sa.name == "user1")
		assert(sa.password == "123456")
		assert(!sa.account)
		assert(!sa.ddl)
		assert(!sa.dcl)
		assert(!sa.grantAccount)
		assert(!sa.grantDdl)
		assert(!sa.grantDcl)
		assert(!sa.isSA)
		assert(sa.createBy.contains("sa"))

		intercept[UserExistsException] {
			catalog.createUser(CatalogUser(
				org = "org",
				name = "user1",
				password = "123456",
				account = true,
				ddl = true,
				dcl = true,
				grantAccount = true,
				grantDdl = true,
				grantDcl = true,
				isSA = true
			), ignoreIfExists = false)
		}

		catalog.renameUser("org", "user1", "user")
		intercept[NoSuchUserException] {
			catalog.getUser("org", "user1")
		}
		val sa1 = catalog.getUser("org", "user")
		assert(sa1.name == "user")

		catalog.alterUser(
			sa1.copy(ddl = true)
		)

		assert(catalog.getUser("org", "user").ddl)

		catalog.dropUser("org", "user", ignoreIfNotExists = true)
		assert(!catalog.userExists("org", "user"))
		intercept[NoSuchUserException] {
			catalog.dropUser("org", "user", ignoreIfNotExists = false)
		}
	}

	test("database and table") {
		catalog.createDatabase(CatalogDatabase(
			name = "db",
			description = Some("for testing"),
			properties = Map(),
			isLogical = true
		), ignoreIfExists = true)
		val db = catalog.getDatabase("db")
		assert(db.name == "db")
		assert(db.description.contains("for testing"))
		assert(db.owner.contains("sa"))

		intercept[DatabaseExistsException] {
			catalog.createDatabase(CatalogDatabase(
				name = "db",
				description = Some("for testing"),
				properties = Map(),
				isLogical = true
			), ignoreIfExists = false)
		}

		catalog.renameDatabase("db", "db1")

		intercept[NoSuchDatabaseException] {
			catalog.getDatabase("db")
		}
		val db1 = catalog.getDatabase("db1")
		assert(db1.name == "db1")
		catalog.alterDatabase(db1.copy(description = Some("for fun")))

		assert(catalog.getDatabase("db1").description.contains("for fun"))

		catalog.createTable(CatalogTable(
			name = "table",
			db = Some("db1"),
			tableType = CatalogTableType.TABLE,
			description = Some("for testing"),
			properties = Map("key1" -> "value1"),
			isStream = false,
			tableSize = Some(100L)
		), ignoreIfExists = true)

		val table = catalog.getTable("db1", "table")
		assert(table.name == "table")
		assert(table.description.contains("for testing"))
		assert(table.db.contains("db1"))
		assert(table.properties == Map("key1" -> "value1"))
		assert(table.owner.contains("sa"))

		intercept[TableExistsException] {
			catalog.createTable(CatalogTable(
				name = "table",
				db = Some("db1"),
				tableType = CatalogTableType.TABLE,
				description = Some("for testing"),
				properties = Map("key1" -> "value1"),
				isStream = false
			), ignoreIfExists = false)
		}

		catalog.renameTable("db1", "table", "table1")

		intercept[NoSuchTableException] {
			catalog.getTable("db1", "table")
		}
		val table1 = catalog.getTable("db1", "table1")
		assert(table1.name == "table1")

		catalog.alterTable(table1.copy(description = Some("for fun"), properties = Map("key2" -> "value2")))

		assert(catalog.getTable("db1", "table1").description.contains("for fun"))
		assert(catalog.getTable("db1", "table1").properties == Map("key2" -> "value2"))
		catalog.dropTable("db1", "table1", ignoreIfNotExists = true)

		intercept[NoSuchTableException] {
			catalog.dropTable("db1", "table1", ignoreIfNotExists = false)
		}

		catalog.createTable(CatalogTable(
			name = "table2",
			tableType = CatalogTableType.TABLE,
			description = Some("for testing"),
			db = Some("db1"),
			properties = Map("key1" -> "value1"),
			isStream = false
		), ignoreIfExists = true)

		intercept[NonEmptyException] {
			catalog.dropDatabase("db1", ignoreIfNotExists = true, cascade = false)
		}

		assert(catalog.databaseExists("db1"))

		catalog.dropDatabase("db1", ignoreIfNotExists = false, cascade = true)

		intercept[NoSuchDatabaseException] {
			catalog.dropDatabase("db1", ignoreIfNotExists = false, cascade = false)
		}
	}

	test("function") {
		catalog.createDatabase(CatalogDatabase(
			name = "db",
			description = Some("for testing"),
			properties = Map(),
			isLogical = true
		), ignoreIfExists = true)

		catalog.createFunction(CatalogFunction(
			name = "function",
			db = Some("db"),
			description = Some("for testing"),
			className = "className",
			methodName = None,
			resources = Seq(FunctionResource(JarResource, "hdfs://localhost:8020/jar"))
		), ignoreIfExists = true)

		/*
		val function = catalog.getFunction("db", "function")
		assert(function.name == "function")
		assert(function.db.contains("db"))
		assert(function.description.contains("for testing"))
		assert(function.className == "className")
		assert(function.methodName.isEmpty)
		assert(function.resources == Seq(FunctionResource(JarResource, "hdfs://localhost:8020/jar")))
		assert(function.createBy.contains("sa"))

		intercept[FunctionExistsException] {
			catalog.createFunction(CatalogFunction(
				name = "function",
				db = Some("db"),
				description = Some("for testing"),
				className = "className",
				methodName = None,
				resources = Seq()
			), ignoreIfExists = false)
		}

		catalog.renameFunction("db", "function", "function1")

		intercept[NoSuchFunctionException] {
			catalog.getFunction("db", "function")
		}

		intercept[NonEmptyException] {
			catalog.dropDatabase("db", ignoreIfNotExists = true, cascade = false)
		}
		catalog.dropFunction("db", "function1", ignoreIfNotExists = true)

		assert(!catalog.functionExists("db", "function1"))

		intercept[NoSuchFunctionException] {
			catalog.dropFunction("db", "function1", ignoreIfNotExists = false)
		}

		catalog.createFunction(CatalogFunction(
			name = "function",
			db = Some("db"),
			description = Some("for testing"),
			className = "className",
			methodName = None,
			resources = Seq(FunctionResource(JarResource, "hdfs://localhost:8020/jar"))
		), ignoreIfExists = true)

		catalog.dropDatabase("db", ignoreIfNotExists = true, cascade = true)
		assert(!catalog.databaseExists("db"))*/

	}

	test("procedure") {
		catalog.createProcedure(CatalogProcedure(
			name = "application",
			sqls = Seq("INSERT INTO TABLE table SELECT * FROM view"),
			lang = "mql",
			description = Some("for testing")
		), ignoreIfExists = true)
		val app = catalog.getProcedure("application")
		assert(app.sqls == Seq("INSERT INTO TABLE table SELECT * FROM view"))
		assert(app.description.contains("for testing"))
		assert(app.owner.contains("sa"))

		intercept[ProcedureExistsException] {
			catalog.createProcedure(CatalogProcedure(
				name = "application",
				sqls = Seq("INSERT INTO TABLE table SELECT * FROM view"),
				lang = "mql",
				description = Some("for testing")
			), ignoreIfExists = false)
		}

		catalog.renameProcedure("application", "application1")

		intercept[NoSuchProcedureException] {
			catalog.getProcedure("application")
		}

		val app1 = catalog.getProcedure("application1")
		catalog.alterProcedure(app1.copy(
			description = Some("for fun"),
			sqls = Seq("")
		))

		assert(catalog.getProcedure("application1").description.contains("for fun"))
		assert(catalog.getProcedure("application1").sqls == Seq(""))

		catalog.dropProcedure("application1", ignoreIfNotExists = true)

		assert(!catalog.procedureExists("application1"))

		intercept[NoSuchProcedureException] {
			catalog.dropProcedure("application1", ignoreIfNotExists = false)
		}
	}


}
