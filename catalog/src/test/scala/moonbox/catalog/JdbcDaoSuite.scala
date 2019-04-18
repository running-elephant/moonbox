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

package moonbox.catalog

import moonbox.catalog.jdbc.JdbcDao
import moonbox.catalog.config._
import moonbox.common.MbConf
import org.scalatest.FunSuite
import org.scalatest.concurrent.ScalaFutures
import slick.dbio.{DBIOAction, NoStream}

import scala.concurrent.Future

class JdbcDaoSuite extends FunSuite with ScalaFutures {

	private val conf: MbConf = new MbConf()
		.set(CATALOG_IMPLEMENTATION.key, "h2")
		.set(JDBC_CATALOG_URL.key, "jdbc:h2:mem:testdb0;DB_CLOSE_DELAY=-1")
		.set(JDBC_CATALOG_USER.key, "testUser")
		.set(JDBC_CATALOG_PASSWORD.key, "testPass")
	    .set(JDBC_CATALOG_DRIVER.key, "org.h2.Driver")

	private val jdbcDao = new JdbcDao(conf)

	import jdbcDao._

	def action[R](act: DBIOAction[R, NoStream, Nothing]): Future[R] = jdbcDao.action(act)

	test("organization") {
		whenReady(
			action(
				createOrganization(CatalogOrganization(name = "moonboxTest1", createBy = 1, updateBy = 1))
			)
		)(id => assert(id == 1) )

		whenReady(
			action(
				createOrganization(CatalogOrganization(name = "moonboxTest2", createBy = 1, updateBy = 1))
			)

		)(id => assert(id == 2) )

		whenReady(
			action(
				renameOrganization("moonboxTest1", "mbTest")(1)
			)
		)(affect => assert(affect == 1))

		whenReady(
			action(
				getOrganization(1)
			)
		)(org => {
			assert(org.contains(
				CatalogOrganization(Some(1), "mbTest", None, 1, org.get.createTime, 1, org.get.updateTime)))
		})

		whenReady(action(getOrganization("mbTest")))(org => {
			assert(org.contains(
				CatalogOrganization(Some(1), "mbTest", None, 1, org.get.createTime, 1, org.get.updateTime)))
		})

		whenReady(action(organizationExists("mbTest")))(exists => assert(exists))

		whenReady(action(listOrganizations()))(orgs => assert(orgs.length == 2))

		whenReady(action(deleteOrganization(1)))(affect => assert(affect == 1))

		whenReady(action(deleteOrganization("moonboxTest2")))(affect => assert(affect == 1))
	}

	test("group") {
		whenReady(
			action(
				createGroup(CatalogGroup(name = "group", description = Some("for testing"), organizationId = 1, createBy = 1, updateBy = 1))
			)
		)(id => assert(id == 1))

		whenReady(
			action(
				createGroup(CatalogGroup(name = "group2", organizationId = 1, createBy = 1, updateBy = 1))
			)
		)(id => assert(id == 2))

		whenReady(
			action(
				renameGroup(1, "group", "group1")(1)
			)
		)(affect => assert(affect == 1))

		whenReady(action(getGroup(1)))(group =>
			assert(group.contains(
				CatalogGroup(Some(1), "group1", Some("for testing"), 1, 1, group.get.createTime, 1, group.get.updateTime)
			)
			)
		)

		whenReady(action(getGroup(1, "group2")))(group =>
			assert(group.contains(
				CatalogGroup(Some(2), "group2", None, 1, 1, group.get.createTime, 1, group.get.updateTime)
			))
		)

		whenReady(action(groupExists(1, "group1")))(exists => assert(exists))

		whenReady(action(listGroups(1)))(groups => assert(groups.size == 2))

		whenReady(action(listGroups(1, "group%")))(groups => assert(groups.size == 2))

		whenReady(action(listGroups(1, "group1")))(groups => assert(groups.size == 1))

		whenReady(action(deleteGroup(1)))(affect => assert(affect == 1))

		whenReady(action(deleteGroup(1, "group2")))(affect => assert(affect == 1))
	}

	test("user") {
		whenReady(
			action(
				createUser(CatalogUser(
					name = "user1",
					password = "123456",
					organizationId = 1,
					createBy = 1,
					updateBy = 1)
				)
			)
		)(id => assert(id == 2))

		whenReady(
			action(
				createUser(CatalogUser(
					name = "user2",
					password = "123456",
					organizationId = 2,
					createBy = 1,
					updateBy = 1)
				)
			)
		)(id => assert(id == 3))

		whenReady(
			action(
				updateUser(
					CatalogUser(
						id = Some(2),
						name = "user1",
						password = "abcdefg",
						organizationId = 1,
						createBy = 1,
						updateBy = 2)
				)
			)
		)(affect => assert(affect == 1))

		whenReady(action(getUser(2)))(user =>
			assert(user.contains(
				CatalogUser(Some(2), "user1", "abcdefg", organizationId = 1,
					createBy = 1, createTime = user.get.createTime, updateBy = 2, updateTime = user.get.updateTime)
			))
		)

		whenReady(action(userExists(1, "user1")))(exists => assert(exists))

		whenReady(action(userExists("user1")))(exists => assert(exists))

		whenReady(action(listUsers(1)))(users => assert(users.size == 1))

		whenReady(action(listUsers(2, "user%")))(users => assert(users.size == 1))

		whenReady(action(deleteUser("user1")))(affect => assert(affect == 1))

		whenReady(action(deleteUser(3)))(affect => assert(affect == 1))
	}

	/*test("datasource") {
		whenReady(
			action(
				createDatasource(CatalogDatasource(
					name = "datasource1",
					properties = Map("key" -> "value"),
					description = Some("for testing"),
					organizationId = 1,
					createBy = 1,
					updateBy = 1
				))
			)
		)(id => assert(id == 1))

		whenReady(
			action(
				createDatasource(CatalogDatasource(
					name = "datasource2",
					properties = Map("key" -> "value"),
					description = Some("for testing"),
					organizationId = 1,
					createBy = 1,
					updateBy = 1
				))
			)
		)(id => assert(id == 2))

		whenReady(action(getDatasource(1)))(ds =>
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

		whenReady(action(renameDatasource(1, "datasource1", "datasource")(2)))(affect => assert(affect == 1))

		whenReady(action(getDatasource(1, "datasource")))(ds =>
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

		whenReady(action(datasourceExists(1, "datasource")))(exists => assert(exists))

		whenReady(action(listDatasources(1)))(dss => assert(dss.size == 2))

		whenReady(action(listDatasources(1, "datasource_")))(dss => assert(dss.size == 1))

		whenReady(action(deleteDatasource(1, "datasource")))(affect => assert(affect == 1))

		whenReady(action(deleteDatasource(2)))(affect => assert(affect == 1))
	}*/

	test("table") {
		whenReady(
			action(
				createTable(
					CatalogTable(
						name = "table1",
						description = Some("for testing"),
						databaseId = 1,
						properties = Map("key" -> "value"),
						isStream = false,
						createBy = 1,
						updateBy = 1
					)
				)
			)
		)(id => assert(id == 1))

		whenReady(action(
			createTable(
				CatalogTable(
					name = "table2",
					description = Some("for testing"),
					databaseId = 1,
					properties = Map("key" -> "value"),
					isStream = true,
					createBy = 1,
					updateBy = 1
				)
			)
		))(id => assert(id == 2))

		whenReady(action(renameTable(1, "table1", "table")(1)))(affect => assert(affect == 1))

		whenReady(action(getTable(1, "table")))(table =>
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

		whenReady(action(getTable(2)))(table =>
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

		whenReady(action(
			updateTable(
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
			)
		))(affect => assert(affect == 1))

		whenReady(action(listTables(1)))(tables => assert(tables.size == 2))

		whenReady(action(listTables(1, "table_")))(tables => assert(tables.size == 1))

		whenReady(action(deleteTable(1, "table")))(affect => assert(affect == 1))

		whenReady(action(deleteTables(1)))(affect => assert(affect == 1))
	}

	test("function") {
		whenReady(action(createFunction(CatalogFunction(
						name = "function",
						databaseId = 1,
						description = Some("for testing"),
						className = "className",
						methodName = None,
						resources = Seq(),
						createBy = 1,
						updateBy = 1
					)
				)
			)
		)(id => assert(id == 1))
		whenReady(action(getFunction(1, "function")))(func => assert(
			func.contains(
				CatalogFunction(
					id = Some(1),
					name = "function",
					databaseId = 1,
					description = Some("for testing"),
					className = "className",
					methodName = None,
					resources = Seq(),
					createBy = 1,
					createTime = func.get.createTime,
					updateBy = 1,
					updateTime = func.get.updateTime
				)
			)
		))
		whenReady(action(functionExists(1, "function")))(exists => assert(exists))
		whenReady(action(renameFunction(1, "function", "function1")(1)))(affect => assert(affect == 1))
		whenReady(action(listFunctions(1)))(funcs => assert(funcs.size == 1))
	}

	test("database") {
		whenReady(action(
			createDatabase(
				CatalogDatabase(
					name = "database",
					description = Some("for testing"),
					organizationId = 1,
					properties = Map(),
					isLogical = true,
					createBy = 1,
					updateBy = 1
				)
			)
		))(id => assert(id == 1))

		whenReady(action(
			createDatabase(
				CatalogDatabase(
					name = "database2",
					description = Some("for testing"),
					organizationId = 1,
					properties = Map(),
					isLogical = true,
					createBy = 1,
					updateBy = 1
				)
			)
		))(id => assert(id == 2))
	}
}
