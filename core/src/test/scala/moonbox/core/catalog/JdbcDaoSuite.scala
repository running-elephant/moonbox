package moonbox.core.catalog

import java.util.concurrent.{CountDownLatch, TimeUnit}

import moonbox.common.MbConf
import moonbox.core.catalog.jdbc.JdbcDao
import moonbox.core.catalog._
import moonbox.core.config._
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{BeforeAndAfterAll, FunSuite}

import scala.util.{Failure, Success}
import scala.concurrent.ExecutionContext.Implicits.global
class JdbcDaoSuite extends FunSuite with ScalaFutures {

	private val conf: MbConf = new MbConf()
		.set(CATALOG_IMPLEMENTATION.key, "h2")
		.set(CATALOG_URL.key, "jdbc:h2:mem:testdb0;DB_CLOSE_DELAY=-1")
		.set(CATALOG_USER.key, "testUser")
		.set(CATALOG_PASSWORD.key, "testPass")
	    .set(CATALOG_DRIVER.key, "org.h2.Driver")
	private val jdbcDao = new JdbcDao(conf)

	import jdbcDao._

	test("organization") {
		whenReady(
			createOrganization(CatalogOrganization(name = "moonboxTest1", createBy = 1, updateBy = 1))
		)(id => assert(id == 1) )
		whenReady(
			createOrganization(CatalogOrganization(name = "moonboxTest2", createBy = 1, updateBy = 1))
		)(id => assert(id == 2) )

		whenReady(renameOrganization("moonboxTest1", "mbTest")(1))(affect => assert(affect == 1))

		whenReady(getOrganization(1))(org => {
			assert(org.contains(
				CatalogOrganization(Some(1), "mbTest", None, 1, org.get.createTime, 1, org.get.updateTime)))
		})

		whenReady(getOrganization("mbTest"))(org => {
			assert(org.contains(
				CatalogOrganization(Some(1), "mbTest", None, 1, org.get.createTime, 1, org.get.updateTime)))
		})

		whenReady(organizationExists("mbTest"))(exists => assert(exists))

		whenReady(listOrganizations())(orgs => assert(orgs.length == 2))

		whenReady(deleteOrganization(1))(affect => assert(affect == 1))

		whenReady(deleteOrganization("moonboxTest2"))(affect => assert(affect == 1))
	}

	test("group") {
		whenReady(
			createGroup(CatalogGroup(name = "group", description = Some("for testing"), organizationId = 1, createBy = 1, updateBy = 1))
		)(id => assert(id == 1))

		whenReady(
			createGroup(CatalogGroup(name = "group2", organizationId = 1, createBy = 1, updateBy = 1))
		)(id => assert(id == 2))

		whenReady(
			renameGroup(1, "group", "group1")(1)
		)(affect => assert(affect == 1))

		whenReady(getGroup(1))(group =>
			assert(group.contains(
				CatalogGroup(Some(1), "group1", Some("for testing"), 1, 1, group.get.createTime, 1, group.get.updateTime)
			)
			)
		)

		whenReady(getGroup(1, "group2"))(group =>
			assert(group.contains(
				CatalogGroup(Some(2), "group2", None, 1, 1, group.get.createTime, 1, group.get.updateTime)
			))
		)

		whenReady(groupExists(1, "group1"))(exists => assert(exists))

		whenReady(listGroups(1))(groups => assert(groups.size == 2))

		whenReady(listGroups(1, "group%"))(groups => assert(groups.size == 2))

		whenReady(listGroups(1, "group1"))(groups => assert(groups.size == 1))

		whenReady(deleteGroup(1))(affect => assert(affect == 1))

		whenReady(deleteGroup(1, "group2"))(affect => assert(affect == 1))
	}

	test("user") {
		whenReady(
			createUser(CatalogUser(
				name = "user1",
				password = "123456",
				organizationId = 1,
				createBy = 1,
				updateBy = 1)
			)
		)(id => assert(id == 2))

		whenReady(
			createUser(CatalogUser(
				name = "user2",
				password = "123456",
				organizationId = 2,
				createBy = 1,
				updateBy = 1)
			)
		)(id => assert(id == 3))

		whenReady(
			updateUser(
				CatalogUser(
					id = Some(2),
					name = "user1",
					password = "abcdefg",
					organizationId = 1,
					createBy = 1,
					updateBy = 2)
			)
		)(affect => assert(affect == 1))

		whenReady(getUser(2))(user =>
			assert(user.contains(
				CatalogUser(Some(2), "user1", "abcdefg", organizationId = 1,
					createBy = 1, createTime = user.get.createTime, updateBy = 2, updateTime = user.get.updateTime)
			))
		)

		whenReady(userExists(1, "user1"))(exists => assert(exists))

		whenReady(userExists("user1"))(exists => assert(exists))

		whenReady(listUsers(1))(users => assert(users.size == 1))

		whenReady(listUsers(2, "user%"))(users => assert(users.size == 1))

		whenReady(deleteUser("user1"))(affect => assert(affect == 1))

		whenReady(deleteUser(3))(affect => assert(affect == 1))
	}

	test("datasource") {
		whenReady(
			createDatasource(CatalogDatasource(
				name = "datasource1",
				properties = Map("key" -> "value"),
				description = Some("for testing"),
				organizationId = 1,
				createBy = 1,
				updateBy = 1
			))
		)(id => assert(id == 1))

		whenReady(
			createDatasource(CatalogDatasource(
				name = "datasource2",
				properties = Map("key" -> "value"),
				description = Some("for testing"),
				organizationId = 1,
				createBy = 1,
				updateBy = 1
			))
		)(id => assert(id == 2))

		whenReady(getDatasource(1))(ds =>
			assert(ds.contains(
				CatalogDatasource(
					id = Some(1),
					name = "datasource1",
					properties = Map("key" -> "value"),
					description = Some("for testing"),
					organizationId = 1,
					createBy = 1,
					createTime = ds.get.createTime,
					updateBy = 1,
					updateTime = ds.get.updateTime
				)
			))
		)

		whenReady(renameDatasource(1, "datasource1", "datasource")(2))(affect => assert(affect == 1))

		whenReady(getDatasource(1, "datasource"))(ds =>
			assert(ds.contains(
				CatalogDatasource(
					id = Some(1),
					name = "datasource",
					properties = Map("key" -> "value"),
					description = Some("for testing"),
					organizationId = 1,
					createBy = 1,
					createTime = ds.get.createTime,
					updateBy = 2,
					updateTime = ds.get.updateTime
				)
			))
		)

		whenReady(datasourceExists(1, "datasource"))(exists => assert(exists))

		whenReady(listDatasources(1))(dss => assert(dss.size == 2))

		whenReady(listDatasources(1, "datasource_"))(dss => assert(dss.size == 1))

		whenReady(deleteDatasource(1, "datasource"))(affect => assert(affect == 1))

		whenReady(deleteDatasource(2))(affect => assert(affect == 1))
	}

	test("table") {
		whenReady(createTable(
			CatalogTable(
				name = "table1",
				description = Some("for testing"),
				databaseId = 1,
				properties = Map("key" -> "value"),
				isStream = false,
				createBy = 1,
				updateBy = 1
			)
		))(id => assert(id == 1))

		whenReady(createTable(
			CatalogTable(
				name = "table2",
				description = Some("for testing"),
				databaseId = 1,
				properties = Map("key" -> "value"),
				isStream = true,
				createBy = 1,
				updateBy = 1
			)
		))(id => assert(id == 2))

		whenReady(renameTable(1, "table1", "table")(1))(affect => assert(affect == 1))

		whenReady(getTable(1, "table"))(table =>
			assert(table.contains(
				CatalogTable(
					id = Some(1),
					name = "table",
					description = Some("for testing"),
					databaseId = 1,
					properties = Map("key" -> "value"),
					isStream = false,
					createBy = 1,
					createTime = table.get.createTime,
					updateBy = 1,
					updateTime = table.get.updateTime
				)
			))
		)

		whenReady(getTable(2))(table =>
			assert(table.contains(
				CatalogTable(
					id = Some(2),
					name = "table2",
					description = Some("for testing"),
					databaseId = 1,
					properties = Map("key" -> "value"),
					isStream = true,
					createBy = 1,
					createTime = table.get.createTime,
					updateBy = 1,
					updateTime = table.get.updateTime
				)
			))
		)

		whenReady(updateTable(
			CatalogTable(
				id = Some(2),
				name = "table2",
				description = Some("for testing"),
				databaseId = 1,
				properties = Map("key1" -> "value"),
				isStream = true,
				createBy = 1,
				updateBy = 1
			)
		))(affect => assert(affect == 1))

		whenReady(listTables(1))(tables => assert(tables.size == 2))

		whenReady(listTables(1, "table_"))(tables => assert(tables.size == 1))

		whenReady(deleteTable(1, "table"))(affect => assert(affect == 1))

		whenReady(deleteTables(1))(affect => assert(affect == 1))
	}

	test("database") {
		whenReady(createDatabase(
			CatalogDatabase(
				name = "database",
				description = Some("for testing"),
				organizationId = 1,
				createBy = 1,
				updateBy = 1
			)
		))(id => assert(id == 1))
		whenReady(createDatabase(
			CatalogDatabase(
				name = "database2",
				description = Some("for testing"),
				organizationId = 1,
				createBy = 1,
				updateBy = 1
			)
		))(id => assert(id == 2))
	}
}
