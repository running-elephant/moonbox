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

import moonbox.catalog.jdbc._
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

		// SYSTEM org create by system
		whenReady(
			action(
				getOrganization("SYSTEM")
			)
		)(org =>
			assert(org.contains(
				OrganizationEntity(
					Some(1),
					"SYSTEM",
					Map(),
					None,
					-1,
					org.get.createTime,
					-1,
					org.get.updateTime
				)))
		)

		whenReady(
			action(
				createOrganization(OrganizationEntity(
					name = "moonboxTest1",
					config = Map(),
					createBy = 1,
					updateBy = 1))
			)
		)(id => assert(id == 2) )

		whenReady(
			action(
				createOrganization(OrganizationEntity(
					name = "moonboxTest2",
					config = Map("a" -> "b"),
					createBy = 1,
					updateBy = 1))
			)

		)(id => assert(id == 3) )

		whenReady(
			action(
				renameOrganization("moonboxTest1", "mbTest")(1)
			)
		)(affect => assert(affect == 1))

		whenReady(
			action(
				getOrganization(3)
			)
		)(org => {
			assert(org.contains(
				OrganizationEntity(Some(3), "moonboxTest2", Map("a" -> "b"), None, 1, org.get.createTime, 1, org.get.updateTime)))
		})

		whenReady(action(getOrganization("mbTest")))(org => {
			assert(org.contains(
				OrganizationEntity(Some(2), "mbTest", Map(), None, 1, org.get.createTime, 1, org.get.updateTime)))
		})

		whenReady(action(organizationExists("mbTest")))(exists => assert(exists))

		whenReady(action(listOrganizations()))(orgs => assert(orgs.length == 3))

		whenReady(action(deleteOrganization(1)))(affect => assert(affect == 1))

		whenReady(action(deleteOrganization("moonboxTest2")))(affect => assert(affect == 1))
	}

	test("user") {
		whenReady(
			action(
				getUser(1, "ROOT")
			)
		)(user => assert(user.isDefined))

		whenReady(
			action(
				createUser(UserEntity(
					name = "user1",
					password = "123456",
					organizationId = 2,
					createBy = 1,
					updateBy = 1)
				)
			)
		)(id => assert(id == 2))

		whenReady(
			action(
				createUser(UserEntity(
					name = "user2",
					password = "123456",
					organizationId = 3,
					createBy = 1,
					updateBy = 1)
				)
			)
		)(id => assert(id == 3))

		whenReady(
			action(
				updateUser(
					UserEntity(
						id = Some(2),
						name = "user1",
						password = "abcdefg",
						organizationId = 2,
						createBy = 1,
						updateBy = 2)
				)
			)
		)(affect => assert(affect == 1))

		whenReady(action(getUser(2)))(user =>
			assert(user.contains(
				UserEntity(Some(2), "user1", "abcdefg", organizationId = 2,
					createBy = 1, createTime = user.get.createTime, updateBy = 2, updateTime = user.get.updateTime)
			))
		)

		whenReady(action(userExists(2, "user1")))(exists => assert(exists))

		whenReady(action(listUsers(2)))(users => assert(users.size == 1))

		whenReady(action(listUsers(3, "user%")))(users => assert(users.size == 1))

		whenReady(action(deleteUser(2, "user1")))(affect => assert(affect == 1))

		whenReady(action(deleteUser(3)))(affect => assert(affect == 1))
	}

	test("table") {
		whenReady(
			action(
				createTable(
					TableEntity(
						name = "table1",
						tableType = "TABLE",
						description = Some("for testing"),
						databaseId = 1,
						properties = Map("key" -> "value"),
						viewText = None,
						isStream = false,
						createBy = 1,
						updateBy = 1
					)
				)
			)
		)(id => assert(id == 1))

		whenReady(action(
			createTable(
				TableEntity(
					name = "table2",
					tableType = "TABLE",
					description = Some("for testing"),
					databaseId = 1,
					properties = Map("key" -> "value"),
					viewText = None,
					isStream = true,
					createBy = 1,
					updateBy = 1
				)
			)
		))(id => assert(id == 2))

		whenReady(action(renameTable(1, "table1", "table")(1)))(affect => assert(affect == 1))

		whenReady(action(getTable(1, "table")))(table =>
			assert(table.contains(
				TableEntity(
					id = Some(1),
					name = "table",
					tableType = "TABLE",
					description = Some("for testing"),
					databaseId = 1,
					properties = Map("key" -> "value"),
					viewText = None,
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
				TableEntity(
					id = Some(2),
					name = "table2",
					tableType = "TABLE",
					description = Some("for testing"),
					databaseId = 1,
					properties = Map("key" -> "value"),
					viewText = None,
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
				TableEntity(
					id = Some(2),
					name = "table2",
					tableType = "TABLE",
					description = Some("for testing"),
					databaseId = 1,
					properties = Map("key1" -> "value"),
					viewText = None,
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
		whenReady(action(createFunction(FunctionEntity(
						name = "function",
						databaseId = 1,
						description = Some("for testing"),
						className = "className",
						methodName = None,
						createBy = 1,
						updateBy = 1
					)
				)
			)
		)(id => assert(id == 1))
		whenReady(action(getFunction(1, "function")))(func => assert(
			func.contains(
				FunctionEntity(
					id = Some(1),
					name = "function",
					databaseId = 1,
					description = Some("for testing"),
					className = "className",
					methodName = None,
					createBy = 1,
					createTime = func.get.createTime,
					updateBy = 1,
					updateTime = func.get.updateTime
				)
			)
		))

		whenReady(action(
			createFunctionResources(
				FunctionResourceEntity(
					funcId = 1,
					resourceType = "jar",
					resource = "xxx.jar",
					createBy = 1,
					updateBy = 1
				)
			)
		))(id => assert(id.contains(1)))

		whenReady(action(functionExists(1, "function")))(exists => assert(exists))
		whenReady(action(renameFunction(1, "function", "function1")(1)))(affect => assert(affect == 1))
		whenReady(action(listFunctions(1)))(funcs =>
			assert(funcs.size == 1))
	}

	test("database") {
		whenReady(action(
			createDatabase(
				DatabaseEntity(
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
				DatabaseEntity(
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
