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

package moonbox.core.paser

import moonbox.catalog.FunctionResource
import moonbox.core.command._
import moonbox.core.parser.MoonboxParser
import org.apache.spark.sql.catalyst.{FunctionIdentifier, TableIdentifier}
import org.apache.spark.sql.types._
import org.scalatest.FunSuite

class MoonboxParserSuite extends FunSuite {
	private val parser = new MoonboxParser()

	private def assertEquals(command: MbCommand, mql: String*) = {
		val commands = mql.map(parser.parsePlan)
		assert(commands.forall(_ == command))
	}

	test("organization") {
		assertEquals(
			CreateOrganization("org1", None, ignoreIfExists = false),
			"CREATE ORGANIZATION org1"
		)

		assertEquals(
			CreateOrganization("org1", Some("for testing"), ignoreIfExists = true),
			"CREATE ORGANIZATION IF NOT EXISTS org1 COMMENT 'for testing'",
			"CREATE ORG IF NOT EXISTS org1 COMMENT \"for testing\""
		)

		assertEquals(
			AlterOrganizationSetName("org1", "org2"),
			"RENAME ORG org1 TO org2",
			"ALTER ORG org1 RENAME TO org2"
		)

		assertEquals(
			AlterOrganizationSetComment("org1", "for testing"),
			"ALTER ORG org1 SET COMMENT 'for testing'"
		)

		assertEquals(
			DropOrganization("org1", ignoreIfNotExists = true, cascade = true),
			"DROP ORG IF EXISTS org1 CASCADE"
		)

		assertEquals(
			DropOrganization("org1", ignoreIfNotExists = false, cascade = false),
			"DROP ORG org1"
		)
	}

	test("sa") {
		assertEquals(
			CreateSa("sa", "'123abc'", "org", Map(), ignoreIfExists = false),
			"CREATE SA sa IN ORG org IDENTIFIED BY '123abc'"
		)

		assertEquals(
			CreateSa("sa", "'123abc'", "org", Map(), ignoreIfExists = true),
			"CREATE SA IF NOT EXISTS sa IN ORG org IDENTIFIED BY '123abc'"
		)

		assertEquals(
			AlterSaSetName("sa", "sa1", "org"),
			"RENAME SA sa IN ORG org TO sa1 ",
			"ALTER SA sa IN ORG org RENAME TO sa1 "
		)

		assertEquals(
			AlterSaSetPassword("sa", "123abc", "org"),
			"ALTER SA sa IN ORG org IDENTIFIED BY 123abc "
		)

		assertEquals(
			DropSa("sa", "org", ignoreIfNotExists = false),
			"DROP SA sa IN ORG org"
		)

		assertEquals(
			DropSa("sa", "org", ignoreIfNotExists = true),
			"DROP SA IF EXISTS sa IN ORG org"
		)
	}

	test("user") {
		assertEquals(
			CreateUser("user", "123abc", Map(), ignoreIfExists = false),
			"CREATE USER user IDENTIFIED BY 123abc"
		)

		assertEquals(
			CreateUser("user", "123abc", Map(), ignoreIfExists = true),
			"CREATE USER IF NOT EXISTS user IDENTIFIED BY 123abc"
		)

		assertEquals(
			AlterUserSetName("user", "user1"),
			"RENAME USER user TO user1",
			"ALTER USER user RENAME TO user1"
		)

		assertEquals(
			AlterUserSetPassword("user", "123abc"),
			"ALTER USER user IDENTIFIED BY 123abc"
		)

		assertEquals(
			DropUser("user", ignoreIfNotExists = true),
			"DROP USER IF EXISTS user"
		)

		assertEquals(
			DropUser("user", ignoreIfNotExists = false),
			"DROP USER user"
		)
	}

	test("group") {
		assertEquals(
			CreateGroup("group", None, ignoreIfExists = false),
			"CREATE GROUP group"
		)

		assertEquals(
			CreateGroup("group", Some("comment"), ignoreIfExists = false),
			"CREATE GROUP group COMMENT comment"
		)

		assertEquals(
			CreateGroup("group", None, ignoreIfExists = true),
			"CREATE GROUP IF NOT EXISTS group"
		)

		assertEquals(
			AlterGroupSetName("group", "group1"),
			"ALTER GROUP group RENAME TO group1",
			"RENAME GROUP group TO group1"
		)


		assertEquals(
			AlterGroupSetComment("group", "comment"),
			"ALTER GROUP group SET COMMENT 'comment'"
		)

		assertEquals(
			AlterGroupAddUser("group", Seq("user1", "user2")),
			"ALTER GROUP group ADD USER user1, user2"
		)

		assertEquals(
			AlterGroupRemoveUser("group", Seq("user1", "user2")),
			"ALTER GROUP group REMOVE USER user1, user2"
		)

		assertEquals(
			DropGroup("group", ignoreIfNotExists = false, cascade = false),
			"DROP GROUP group"
		)

		assertEquals(
			DropGroup("group", ignoreIfNotExists = true, cascade = false),
			"DROP GROUP IF EXISTS group"
		)

		assertEquals(
			DropGroup("group", ignoreIfNotExists = true, cascade = true),
			"DROP GROUP IF EXISTS group CASCADE"
		)
	}

	test("database") {
		assertEquals(
			CreateDatabase("database", Some("for testing"), ignoreIfExists = true),
			"CREATE DATABASE IF NOT EXISTS database COMMENT 'for testing'"
		)

		assertEquals(
			AlterDatabaseSetName("database", "database1"),
			"RENAME DATABASE database TO database1",
			"ALTER DATABASE database RENAME TO database1"
		)

		assertEquals(
			AlterDatabaseSetComment("database", "for testing"),
			"ALTER DATABASE database SET COMMENT 'for testing'"
		)

		assertEquals(
			DropDatabase("database", ignoreIfNotExists = true, cascade = true),
			"DROP DATABASE IF EXISTS database CASCADE"
		)

		assertEquals(
			MountDatabase("database", Map("key" -> "value", "key1" -> "value1"), ignoreIfExists = false),
			"MOUNT DATABASE database OPTIONS(key 'value', key1 'value1')"
		)

		assertEquals(
			UnmountDatabase("database", ignoreIfNotExists = false, cascade = false),
			"UNMOUNT DATABASE database"
		)
	}

	test("table") {
		assertEquals(
			MountTable(TableIdentifier("table", None), None, Map("key" -> "value"), isStream = false, ignoreIfExists = false),
			"MOUNT TABLE table OPTIONS(key 'value')"
		)

		assertEquals(
			MountTable(TableIdentifier("table", Some("db")), None, Map("key" -> "value"), isStream = true, ignoreIfExists = true),
			"MOUNT STREAM TABLE IF NOT EXISTS db.table OPTIONS(key 'value')"
		)

		assertEquals(
			MountTable(TableIdentifier("table", None), Some(
				StructType(Seq(
					StructField("name", StringType),
					StructField("age", IntegerType)
				))
			), Map("key" -> "value"), isStream = false, ignoreIfExists = false),
			"MOUNT TABLE table(name string, age int) OPTIONS(key 'value')"
		)

		assertEquals(
			AlterTableSetName(TableIdentifier("table", None), TableIdentifier("table1", None)),
			"RENAME TABLE table TO table1",
			"ALTER TABLE table RENAME TO table1"
		)

		assertEquals(
			AlterTableSetName(TableIdentifier("table", Some("db")), TableIdentifier("table1", Some("db"))),
			"RENAME TABLE db.table TO db.table1",
			"ALTER TABLE db.table RENAME TO db.table1"
		)

		assertEquals(
			AlterTableSetOptions(TableIdentifier("table", Some("db")), Map("key" -> "value")),
			"ALTER TABLE db.table SET OPTIONS(key 'value')"
		)

		assertEquals(
			UnmountTable(TableIdentifier("table", Some("db")), ignoreIfNotExists = true),
			"UNMOUNT TABLE IF EXISTS db.table"
		)
	}

	test("function") {
		assertEquals(
			CreateFunction(FunctionIdentifier("func", Some("db")),
				"edp.moonbox.Function",
				None,
				Seq(FunctionResource("jar", "/temp/udf.jar")),
				ignoreIfExists = true),
			"CREATE FUNCTION IF NOT EXISTS db.func AS 'edp.moonbox.Function' USING JAR '/temp/udf.jar'"
		)

		assertEquals(
			CreateFunction(FunctionIdentifier("func", Some("db")),
				"edp.moonbox.Function",
				Some("test"),
				Seq(FunctionResource("jar", "/temp/udf.jar")),
				ignoreIfExists = true),
			"CREATE FUNCTION IF NOT EXISTS db.func AS 'edp.moonbox.Function' 'test' USING JAR '/temp/udf.jar'"
		)

		assertEquals(
			DropFunction(FunctionIdentifier("func", Some("db")), ignoreIfNotExists = true),
			"DROP FUNCTION IF EXISTS db.func"
		)
		assertEquals(
			CreateTempFunction(FunctionIdentifier("func", None),
				"edp.moonbox.Function",
				None,
				Seq(FunctionResource("jar", "/temp/udf.jar")), ignoreIfExists = false),
			"CREATE TEMP FUNCTION func AS 'edp.moonbox.Function' USING JAR '/temp/udf.jar'",
			"CREATE TEMPORARY FUNCTION func AS 'edp.moonbox.Function' USING JAR '/temp/udf.jar'"
		)
		assertEquals(
			DropTempFunction(FunctionIdentifier("func", Some("db")), ignoreIfNotExists = true),
			"DROP TEMP FUNCTION IF EXISTS db.func"
		)

	}

	test("view") {
		assertEquals(
			CreateView(TableIdentifier("view", Some("db")), "SELECT * FROM table", Some("for testing"), replaceIfExists = true),
			"CREATE VIEW IF NOT EXISTS db.view COMMENT 'for testing' AS SELECT * FROM table"
		)

		assertEquals(
			AlterViewSetQuery(TableIdentifier("view", Some("db")), "SELECT * FROM table"),
			"ALTER VIEW db.view AS SELECT * FROM table"
		)

		assertEquals(
			DropView(TableIdentifier("view", Some("db")), ignoreIfNotExists = true),
			"DROP VIEW IF EXISTS db.view"
		)
	}

	test("application") {
		assertEquals(
			CreateProcedure("app", Seq("CREATE TEMP VIEW view AS SELECT * FROM table", "SELECT * FROM view"),
				"mql",
				ignoreIfExists = true),
			"CREATE PROCEDURE IF NOT EXISTS app USING mql AS (CREATE TEMP VIEW view AS SELECT * FROM table; SELECT * FROM view)"
		)

		assertEquals(
			CreateProcedure("app", Seq("SELECT * FROM table"),
				"mql",
				ignoreIfExists = true),
			"CREATE PROCEDURE IF NOT EXISTS app USING mql AS (SELECT * FROM table)"
		)
		assertEquals(
			AlterProcedureSetName(
				"app", "app1"
			),
			"ALTER PROCEDURE app RENAME TO app1",
			"RENAME PROCEDURE app TO app1"
		)
		assertEquals(
			AlterProcedureSetQuery(
				"app", Seq("CREATE TEMP VIEW view AS SELECT * FROM table", "SELECT * FROM view")
			),
			"ALTER PROCEDURE app AS (CREATE TEMP VIEW view AS SELECT * FROM table; SELECT * FROM view) "
		)
		assertEquals(
			DropProcedure(
				"app", ignoreIfNotExists = true
			),
			"DROP PROCEDURE IF EXISTS app"
		)
	}

	test("event") {
		assertEquals(
			CreateTimedEvent("once", None, "* * * * * *", Some("for test"), "app1", enable = false, ignoreIfExists = false),
			"CREATE EVENT once ON SCHEDULE AT '* * * * * *' COMMENT 'for test' DO CALL app1",
			"CREATE DEFINER CURRENT_USER EVENT once ON SCHEDULE AT '* * * * * *' DISABLE COMMENT 'for test' DO CALL app1"
		)

		assertEquals(
			AlterTimedEventSetName("once", "event1"),
			"ALTER EVENT once RENAME TO event1",
			"RENAME EVENT once TO event1"
		)

		assertEquals(
			AlterTimedEventSetDefiner("once", Some("user1")),
			"ALTER DEFINER = user1 EVENT once"
		)

		assertEquals(
			AlterTimedEventSetSchedule("once", "* * * 23 2 18"),
			"ALTER EVENT once ON SCHEDULE AT '* * * 23 2 18'"
		)

		assertEquals(
			AlterTimedEventSetEnable("once", enable = true),
			"ALTER EVENT once ENABLE"
		)

		assertEquals(
			DropTimedEvent("once", ignoreIfNotExists = true),
			"DROP EVENT IF EXISTS once"
		)
	}

	test("show sysinfo") {
		assertEquals(
			ShowSysInfo,
			"SHOW SYSINFO"
		)
	}

	test("show databases") {
		assertEquals(
			ShowDatabases(Some("abc%")),
			"SHOW DATABASES LIKE 'abc%'"
		)

		assertEquals(
			ShowDatabases(None),
			"SHOW DATABASES"
		)
	}

	test("show tables") {
		assertEquals(
			ShowTables(None, None),
			"SHOW TABLES"
		)
		assertEquals(
			ShowTables(Some("db"), None),
			"SHOW TABLES FROM db",
			"SHOW TABLES IN db"
		)
		assertEquals(
			ShowTables(Some("db"), Some("abc%")),
			"SHOW TABLES FROM db LIKE 'abc%'",
			"SHOW TABLES IN db LIKE 'abc%'"
		)
		assertEquals(
			ShowTables(None, Some("abc%")),
			"SHOW TABLES LIKE 'abc%'"
		)
	}

	test("show functions") {
		assertEquals(
			ShowFunctions(None, None, true, true),
			"SHOW FUNCTIONS"
		)
		assertEquals(
			ShowFunctions(Some("db"), None, true, true),
			"SHOW FUNCTIONS FROM db",
			"SHOW FUNCTIONS IN db"
		)
		assertEquals(
			ShowFunctions(Some("db"), Some("abc%"), true, false),
			"SHOW USER FUNCTIONS FROM db LIKE 'abc%'",
			"SHOW USER FUNCTIONS IN db LIKE 'abc%'"
		)
		assertEquals(
			ShowFunctions(None, Some("abc%"), false, true),
			"SHOW SYSTEM FUNCTIONS LIKE 'abc%'"
		)
	}

	test("show users") {
		assertEquals(
			ShowUsers(None),
			"SHOW USERS"
		)
		assertEquals(
			ShowUsers(Some("abc%")),
			"SHOW USERS LIKE 'abc%'"
		)
	}

	test("show applications") {
		assertEquals(
			ShowProcedures(None),
			"SHOW PROCEDURES"
		)
		assertEquals(
			ShowProcedures(Some("abc%")),
			"SHOW PROCEDURES LIKE 'abc%'"
		)
	}

	test("desc database") {
		assertEquals(
			DescDatabase("db"),
			"DESC DATABASE db",
			"DESCRIBE DATABASE db"
		)
	}

	test("desc table") {
		assertEquals(
			DescTable(TableIdentifier("table"), extended = false),
			"DESC TABLE table",
			"DESCRIBE TABLE table"
		)

		assertEquals(
			DescTable(TableIdentifier("table", Some("db")), extended = false),
			"DESC TABLE db.table",
			"DESCRIBE TABLE db.table"
		)

		assertEquals(
			DescTable(TableIdentifier("table", Some("db")), extended = true),
			"DESC TABLE EXTENDED db.table",
			"DESCRIBE TABLE EXTENDED db.table"
		)
	}

	test("desc function") {
		assertEquals(
			DescFunction(FunctionIdentifier("func"), isExtended = false),
			"DESC FUNCTION func",
			"DESCRIBE FUNCTION func"
		)

		assertEquals(
			DescFunction(FunctionIdentifier("func", Some("db")), isExtended = false),
			"DESC FUNCTION db.func",
			"DESCRIBE FUNCTION db.func"
		)

		assertEquals(
			DescFunction(FunctionIdentifier("func", Some("db")), isExtended = true),
			"DESC FUNCTION EXTENDED db.func",
			"DESCRIBE FUNCTION EXTENDED db.func"
		)
	}

	test("desc user") {
		assertEquals(
			DescUser("user"),
			"DESC USER user",
			"DESCRIBE USER user"
		)
	}

	test("statement") {
		assertEquals(
			Statement("SELECT id, name FROM table"),
			"SELECT id, name FROM table"
		)
		assertEquals(
			Statement("WITH cte1 AS (SELECT * FROM table1), cte2 AS (SELECT * FROM table2) SELECT * FROM cte1 join cte2"),
			"WITH cte1 AS (SELECT * FROM table1), cte2 AS (SELECT * FROM table2) SELECT * FROM cte1 join cte2"
		)

		assertEquals(
			Statement("INSERT INTO db.table1 SELECT * FROM table"),

			"INSERT INTO TABLE db.table1 SELECT * FROM table"
		)

		assertEquals(
			Statement("INSERT INTO db.table1 PARTITION(a, b) COALESCE 10 SELECT * FROM table"),
			"INSERT INTO db.table1 PARTITION(a, b) COALESCE 10 SELECT * FROM table"
		)

		assertEquals(
			Statement("INSERT OVERWRITE TABLE db.table1 SELECT * FROM table"),
			"INSERT OVERWRITE TABLE db.table1 SELECT * FROM table"
		)

		assertEquals(
			Statement("analyze table t1 compute statistics"),
			"analyze table t1 compute statistics"
		)
	}

	test("temp view") {
		assertEquals(
			CreateTempView("view", "SELECT * FROM table", isCache = false, replaceIfExists = false),
			"CREATE TEMP VIEW view AS SELECT * FROM table",
			"CREATE TEMPORARY VIEW view AS SELECT * FROM table"
		)

		assertEquals(
			CreateTempView("view", "SELECT * FROM table", isCache = true, replaceIfExists = true),
			"CREATE OR REPLACE CACHE TEMP VIEW view AS SELECT * FROM table",
			"CREATE OR REPLACE CACHE TEMPORARY VIEW view AS SELECT * FROM table"
		)
	}

	test("grant and revoke grant") {
		assertEquals(
			GrantGrantToUser(Seq(RolePrivilege.ACCOUNT, RolePrivilege.DDL, RolePrivilege.DCL), "user1"),
			"GRANT GRANT OPTION ACCOUNT, DDL, DCL TO user1"
		)

		assertEquals(
			GrantGrantToGroup(Seq(RolePrivilege.ACCOUNT, RolePrivilege.DDL, RolePrivilege.DCL), "group1"),
			"GRANT GRANT OPTION ACCOUNT, DDL, DCL TO GROUP group1"
		)

		assertEquals(
			RevokeGrantFromUser(Seq(RolePrivilege.ACCOUNT, RolePrivilege.DDL, RolePrivilege.DCL), "user2"),
			"REVOKE GRANT OPTION ACCOUNT, DDL, DCL FROM user2"
		)

		assertEquals(
			RevokeGrantFromGroup(Seq(RolePrivilege.ACCOUNT, RolePrivilege.DDL, RolePrivilege.DCL), "group2"),
			"REVOKE GRANT OPTION ACCOUNT, DDL, DCL FROM GROUP group2"
		)

	}

	test("grant and revoke privilege") {
		assertEquals(
			GrantPrivilegeToUser(Seq(RolePrivilege.ACCOUNT, RolePrivilege.DDL), "user1"),
			"GRANT ACCOUNT, DDL TO user1"
		)

		assertEquals(
			GrantPrivilegeToGroup(Seq(RolePrivilege.ACCOUNT, RolePrivilege.DDL), "group1"),
			"GRANT ACCOUNT, DDL TO GROUP group1"
		)

		assertEquals(
			RevokePrivilegeFromUser(Seq(RolePrivilege.ACCOUNT), "user2"),
			"REVOKE ACCOUNT FROM USER user2"
		)

		assertEquals(
			RevokePrivilegeFromGroup(Seq(RolePrivilege.ACCOUNT), "group2"),
			"REVOKE ACCOUNT FROM GROUP group2"
		)

	}

	test("grant resource to user") {
		assertEquals(
			GrantResourceToUser(Seq(SelectPrivilege), TableIdentifier("t1", Some("db")), "user1"),
			"GRANT SELECT ON db.t1 TO USER user1"
		)

		assertEquals(
			GrantResourceToUser(Seq(ColumnSelectPrivilege(Seq("id", "name"))), TableIdentifier("t1", Some("db")), "user1"),
			"GRANT SELECT(id, name) ON db.t1 TO USER user1"
		)

		assertEquals(
			GrantResourceToUser(Seq(ColumnSelectPrivilege(Seq("id", "name")), ColumnUpdatePrivilege(Seq("id", "name"))), TableIdentifier("*", Some("db")), "user2"),
			"GRANT SELECT(id, name), UPDATE(id, name) ON db.* TO USER user2"
		)

		assertEquals(
			GrantResourceToUser(Seq(ColumnSelectPrivilege(Seq("id", "name")),
				ColumnUpdatePrivilege(Seq("id", "name")),
				InsertPrivilege,
				DeletePrivilege
			),
				TableIdentifier("*", Some("db")), "user1"),
			"GRANT SELECT(id, name), UPDATE(id, name), INSERT, DELETE ON db.* TO user1"
		)
	}

	test("grant resource to group") {
		assertEquals(
			GrantResourceToGroup(Seq(SelectPrivilege), TableIdentifier("t1", Some("db")), "group1"),
			"GRANT SELECT ON db.t1 TO GROUP group1"
		)

		assertEquals(
			GrantResourceToGroup(Seq(ColumnSelectPrivilege(Seq("id", "name"))), TableIdentifier("t1", Some("db")), "group1"),
			"GRANT SELECT(id, name) ON db.t1 TO GROUP group1"
		)

		assertEquals(
			GrantResourceToGroup(Seq(ColumnSelectPrivilege(Seq("id", "name")), ColumnUpdatePrivilege(Seq("id", "name"))), TableIdentifier("*", Some("db")), "group2"),
			"GRANT SELECT(id, name), UPDATE(id, name) ON db.* TO GROUP group2"
		)

		assertEquals(
			GrantResourceToGroup(Seq(ColumnSelectPrivilege(Seq("id", "name")),
				ColumnUpdatePrivilege(Seq("id", "name")),
				InsertPrivilege,
				DeletePrivilege
			),
				TableIdentifier("*", Some("db")), "group1"),
			"GRANT SELECT(id, name), UPDATE(id, name), INSERT, DELETE ON db.* TO GROUP group1"
		)
	}

	test("revoke resource from user") {
		assertEquals(
			RevokeResourceFromUser(Seq(SelectPrivilege), TableIdentifier("t1", Some("db")), "user1"),
			"REVOKE SELECT ON db.t1 FROM USER user1"
		)

		assertEquals(
			RevokeResourceFromUser(Seq(ColumnSelectPrivilege(Seq("id", "name"))), TableIdentifier("t1", Some("db")), "user1"),
			"REVOKE SELECT(id, name) ON db.t1 FROM user1"
		)

		assertEquals(
			RevokeResourceFromUser(Seq(ColumnSelectPrivilege(Seq("id", "name")), ColumnUpdatePrivilege(Seq("id", "name"))), TableIdentifier("*", Some("db")), "user1"),
			"REVOKE SELECT(id, name), UPDATE(id, name) ON db.* FROM user1"
		)

		assertEquals(
			RevokeResourceFromUser(Seq(ColumnSelectPrivilege(Seq("id", "name")),
				ColumnUpdatePrivilege(Seq("id", "name")),
				InsertPrivilege,
				DeletePrivilege
			),
				TableIdentifier("*", Some("db")), "user1"),
			"REVOKE SELECT(id, name), UPDATE(id, name), INSERT, DELETE ON db.* FROM USER user1"
		)
	}

	test("revoke resource from group") {
		assertEquals(
			RevokeResourceFromGroup(Seq(SelectPrivilege), TableIdentifier("t1", Some("db")), "group1"),
			"REVOKE SELECT ON db.t1 FROM GROUP group1"
		)

		assertEquals(
			RevokeResourceFromGroup(Seq(ColumnSelectPrivilege(Seq("id", "name"))), TableIdentifier("t1", Some("db")), "group1"),
			"REVOKE SELECT(id, name) ON db.t1 FROM GROUP group1"
		)

		assertEquals(
			RevokeResourceFromGroup(Seq(ColumnSelectPrivilege(Seq("id", "name")), ColumnUpdatePrivilege(Seq("id", "name"))), TableIdentifier("*", Some("db")), "group1"),
			"REVOKE SELECT(id, name), UPDATE(id, name) ON db.* FROM GROUP group1"
		)

		assertEquals(
			RevokeResourceFromGroup(Seq(ColumnSelectPrivilege(Seq("id", "name")),
				ColumnUpdatePrivilege(Seq("id", "name")),
				InsertPrivilege,
				DeletePrivilege
			),
				TableIdentifier("*", Some("db")), "group1"),
			"REVOKE SELECT(id, name), UPDATE(id, name), INSERT, DELETE ON db.* FROM GROUP group1"
		)
	}

}
