package moonbox.core.paser

import moonbox.core.catalog.{FunctionResource}
import moonbox.core.{MbFunctionIdentifier, MbTableIdentifier}
import moonbox.core.command._
import moonbox.core.parser.MbParser
import org.apache.spark.sql.types._
import org.scalatest.FunSuite

class MbParserSuite extends FunSuite {
	private val parser = new MbParser()

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
			CreateSa("sa", "'123abc'", "org", ignoreIfExists = false),
			"CREATE SA sa IN ORG org IDENTIFIED BY '123abc'"
		)

		assertEquals(
			CreateSa("sa", "'123abc'", "org", ignoreIfExists = true),
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
			CreateUser("user", "123abc", ignoreIfExists = false),
			"CREATE USER user IDENTIFIED BY 123abc"
		)

		assertEquals(
			CreateUser("user", "123abc", ignoreIfExists = true),
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
			CreateGroup("group", Some("for testing"), ignoreIfExists = false),
			"CREATE GROUP group COMMENT 'for testing'"
		)

		assertEquals(
			CreateGroup("group", None, ignoreIfExists = true),
			"CREATE GROUP IF NOT EXISTS group"
		)

		assertEquals(
			AlterGroupSetName("group", "group1"),
			"RENAME GROUP group TO group1",
			"ALTER GROUP group RENAME TO group1"
		)

		assertEquals(
			AlterGroupSetComment("group", "for testing"),
			"ALTER GROUP group SET COMMENT 'for testing'"
		)

		assertEquals(
			AlterGroupSetUser("group", Seq("user1", "user2"), Seq(), addFirst = true),
			"ALTER GROUP group ADD USER user1, user2"
		)

		assertEquals(
			AlterGroupSetUser("group", Seq(), Seq("user3"), addFirst = false),
			"ALTER GROUP group REMOVE USER user3"
		)

		assertEquals(
			AlterGroupSetUser("group", Seq("user1", "user2"), Seq("user3"), addFirst = true),
			"ALTER GROUP group ADD USER user1, user2 REMOVE USER user3"
		)

		assertEquals(
			AlterGroupSetUser("group", Seq("user1", "user2"), Seq("user3"), addFirst = false),
			"ALTER GROUP group REMOVE USER user3 ADD USER user1, user2 "
		)

		assertEquals(
			DropGroup("group", ignoreIfNotExists = false, cascade = false),
			"DROP GROUP group"
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
			UnmountDatabase("database", ignoreIfNotExists = false),
			"UNMOUNT DATABASE database"
		)
	}

	test("table") {
		assertEquals(
			MountTable(MbTableIdentifier("table", None), None, Map("key" -> "value"), isStream = false, ignoreIfExists = false),
			"MOUNT TABLE table OPTIONS(key 'value')"
		)

		assertEquals(
			MountTable(MbTableIdentifier("table", Some("db")), None, Map("key" -> "value"), isStream = true, ignoreIfExists = true),
			"MOUNT STREAM TABLE IF NOT EXISTS db.table OPTIONS(key 'value')"
		)

		assertEquals(
			MountTable(MbTableIdentifier("table", None), Some(
				StructType(Seq(
					StructField("name", StringType),
					StructField("age", IntegerType)
				))
			), Map("key" -> "value"), isStream = false, ignoreIfExists = false),
			"MOUNT TABLE table(name string, age int) OPTIONS(key 'value')"
		)

		assertEquals(
			AlterTableSetName(MbTableIdentifier("table", None), MbTableIdentifier("table1", None)),
			"RENAME TABLE table TO table1",
			"ALTER TABLE table RENAME TO table1"
		)

		assertEquals(
			AlterTableSetName(MbTableIdentifier("table", Some("db")), MbTableIdentifier("table1", Some("db"))),
			"RENAME TABLE db.table TO db.table1",
			"ALTER TABLE db.table RENAME TO db.table1"
		)

		assertEquals(
			AlterTableSetOptions(MbTableIdentifier("table", Some("db")), Map("key" -> "value")),
			"ALTER TABLE db.table SET OPTIONS(key 'value')"
		)

		assertEquals(
			UnmountTable(MbTableIdentifier("table", Some("db")), ignoreIfNotExists = true),
			"UNMOUNT TABLE IF EXISTS db.table"
		)
	}

	test("function") {
		assertEquals(
			CreateFunction(MbFunctionIdentifier("func", Some("db")),
				"edp.moonbox.Function",
				None,
				Seq(FunctionResource("jar", "/temp/udf.jar")),
				ignoreIfExists = true),
			"CREATE FUNCTION IF NOT EXISTS db.func AS 'edp.moonbox.Function' USING JAR '/temp/udf.jar'"
		)

		assertEquals(
			CreateFunction(MbFunctionIdentifier("func", Some("db")),
				"edp.moonbox.Function",
				Some("test"),
				Seq(FunctionResource("jar", "/temp/udf.jar")),
				ignoreIfExists = true),
			"CREATE FUNCTION IF NOT EXISTS db.func AS 'edp.moonbox.Function' 'test' USING JAR '/temp/udf.jar'"
		)

		assertEquals(
			DropFunction(MbFunctionIdentifier("func", Some("db")), ignoreIfNotExists = true),
			"DROP FUNCTION IF EXISTS db.func"
		)
		assertEquals(
			CreateTempFunction(MbFunctionIdentifier("func", None),
				"edp.moonbox.Function",
				None,
				Seq(FunctionResource("jar", "/temp/udf.jar")), ignoreIfExists = false),
			"CREATE TEMP FUNCTION func AS 'edp.moonbox.Function' USING JAR '/temp/udf.jar'",
			"CREATE TEMPORARY FUNCTION func AS 'edp.moonbox.Function' USING JAR '/temp/udf.jar'"
		)
		assertEquals(
			DropTempFunction(MbFunctionIdentifier("func", Some("db")), ignoreIfNotExists = true),
			"DROP TEMP FUNCTION IF EXISTS db.func"
		)

	}

	test("view") {
		assertEquals(
			CreateView(MbTableIdentifier("view", Some("db")), "SELECT * FROM table", Some("for testing"), ignoreIfExists = true),
			"CREATE VIEW IF NOT EXISTS db.view COMMENT 'for testing' AS SELECT * FROM table"
		)

		assertEquals(
			AlterViewSetName(MbTableIdentifier("view", Some("db")), MbTableIdentifier("view1", Some("db"))),
			"RENAME VIEW db.view TO db.view1",
			"ALTER VIEW db.view RENAME TO db.view1"
		)

		assertEquals(
			AlterViewSetQuery(MbTableIdentifier("view", Some("db")), "SELECT * FROM table"),
			"ALTER VIEW db.view AS SELECT * FROM table"
		)

		assertEquals(
			AlterViewSetComment(MbTableIdentifier("view", Some("db")), "for testing"),
			"ALTER VIEW db.view SET COMMENT 'for testing'"
		)

		assertEquals(
			DropView(MbTableIdentifier("view", Some("db")), ignoreIfNotExists = true),
			"DROP VIEW IF EXISTS db.view"
		)
	}

	test("application") {
		assertEquals(
			CreateApplication("app", Seq("CREATE TEMP VIEW view AS SELECT * FROM table", "SELECT * FROM view"), ignoreIfExists = true),
			"CREATE APPLICATION IF NOT EXISTS app AS (CREATE TEMP VIEW view AS SELECT * FROM table; SELECT * FROM view)"
		)

		assertEquals(
			CreateApplication("app", Seq("SELECT * FROM table"), ignoreIfExists = true),
			"CREATE APPLICATION IF NOT EXISTS app AS (SELECT * FROM table)"
		)
		assertEquals(
			AlterApplicationSetName(
				"app", "app1"
			),
			"ALTER APPLICATION app RENAME TO app1",
			"RENAME APPLICATION app TO app1"
		)
		assertEquals(
			AlterApplicationSetQuery(
				"app", Seq("CREATE TEMP VIEW view AS SELECT * FROM table", "SELECT * FROM view")
			),
			"ALTER APPLICATION app AS (CREATE TEMP VIEW view AS SELECT * FROM table; SELECT * FROM view) "
		)
		assertEquals(
			DropApplication(
				"app", ignoreIfNotExists = true
			),
			"DROP APPLICATION IF EXISTS app"
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

	test("show views") {
		assertEquals(
			ShowViews(None, None),
			"SHOW VIEWS"
		)
		assertEquals(
			ShowViews(Some("db"), None),
			"SHOW VIEWS FROM db",
			"SHOW VIEWS IN db"
		)
		assertEquals(
			ShowViews(Some("db"), Some("abc%")),
			"SHOW VIEWS FROM db LIKE 'abc%'",
			"SHOW VIEWS IN db LIKE 'abc%'"
		)
		assertEquals(
			ShowViews(None, Some("abc%")),
			"SHOW VIEWS LIKE 'abc%'"
		)
	}

	test("show functions") {
		assertEquals(
			ShowFunctions(None, None),
			"SHOW FUNCTIONS"
		)
		assertEquals(
			ShowFunctions(Some("db"), None),
			"SHOW FUNCTIONS FROM db",
			"SHOW FUNCTIONS IN db"
		)
		assertEquals(
			ShowFunctions(Some("db"), Some("abc%")),
			"SHOW FUNCTIONS FROM db LIKE 'abc%'",
			"SHOW FUNCTIONS IN db LIKE 'abc%'"
		)
		assertEquals(
			ShowFunctions(None, Some("abc%")),
			"SHOW FUNCTIONS LIKE 'abc%'"
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

	test("show groups") {
		assertEquals(
			ShowGroups(None),
			"SHOW GROUPS"
		)
		assertEquals(
			ShowGroups(Some("abc%")),
			"SHOW GROUPS LIKE 'abc%'"
		)
	}

	test("show applications") {
		assertEquals(
			ShowApplications(None),
			"SHOW APPLICATIONS"
		)
		assertEquals(
			ShowApplications(Some("abc%")),
			"SHOW APPLICATIONS LIKE 'abc%'"
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
			DescTable(MbTableIdentifier("table"), extended = false),
			"DESC TABLE table",
			"DESCRIBE TABLE table"
		)

		assertEquals(
			DescTable(MbTableIdentifier("table", Some("db")), extended = false),
			"DESC TABLE db.table",
			"DESCRIBE TABLE db.table"
		)

		assertEquals(
			DescTable(MbTableIdentifier("table", Some("db")), extended = true),
			"DESC TABLE EXTENDED db.table",
			"DESCRIBE TABLE EXTENDED db.table"
		)
	}

	test("desc view") {
		assertEquals(
			DescView(MbTableIdentifier("view")),
			"DESC VIEW view",
			"DESCRIBE VIEW view"
		)

		assertEquals(
			DescView(MbTableIdentifier("view", Some("db"))),
			"DESC VIEW db.view",
			"DESCRIBE VIEW db.view"
		)
	}

	test("desc function") {
		assertEquals(
			DescFunction(MbFunctionIdentifier("func"), extended = false),
			"DESC FUNCTION func",
			"DESCRIBE FUNCTION func"
		)

		assertEquals(
			DescFunction(MbFunctionIdentifier("func", Some("db")), extended = false),
			"DESC FUNCTION db.func",
			"DESCRIBE FUNCTION db.func"
		)

		assertEquals(
			DescFunction(MbFunctionIdentifier("func", Some("db")), extended = true),
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

	test("desc group") {
		assertEquals(
			DescGroup("group"),
			"DESC GROUP group",
			"DESCRIBE GROUP group"
		)
	}

	test("set configuration") {
		assertEquals(
			SetVariables("key", "value", isGlobal = false),
			"SET key = 'value'",
			"SET key 'value'"
		)
		assertEquals(
			SetVariables("key", "value", isGlobal = false),
			"SET SESSION key = 'value'",
			"SET SESSION key 'value'"
		)
		assertEquals(
			SetVariables("key", "value", isGlobal = true),
			"SET GLOBAL key = 'value'",
			"SET GLOBAL key 'value'"
		)
	}

	test("query") {
		assertEquals(
			MQLQuery("SELECT id, name FROM table"),
			"SELECT id, name FROM table"
		)
		assertEquals(
			MQLQuery("WITH cte1 AS (SELECT * FROM table1), cte2 AS (SELECT * FROM table2) SELECT * FROM cte1 join cte2"),
			"WITH cte1 AS (SELECT * FROM table1), cte2 AS (SELECT * FROM table2) SELECT * FROM cte1 join cte2"
		)
	}

	test("insert") {
		assertEquals(
			InsertInto(MbTableIdentifier("table1", Some("db")), "SELECT * FROM table", overwrite = false),
			"INSERT INTO db.table1 SELECT * FROM table",
			"INSERT INTO TABLE db.table1 SELECT * FROM table"
		)

		assertEquals(
			InsertInto(MbTableIdentifier("table1", Some("db")), "SELECT * FROM table", overwrite = true),
			"INSERT OVERWRITE TABLE db.table1 SELECT * FROM table"
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
			GrantGrantToUser(Seq(PrivilegeType.ACCOUNT, PrivilegeType.DDL, PrivilegeType.DCL), Seq("user1", "user2")),
			"GRANT GRANT OPTION ACCOUNT, DDL, DCL TO USER user1, user2"
		)
		assertEquals(
			GrantGrantToGroup(Seq(PrivilegeType.ACCOUNT, PrivilegeType.DDL, PrivilegeType.DCL), Seq("group1", "group2")),
			"GRANT GRANT OPTION ACCOUNT, DDL, DCL TO Group group1, group2"
		)

		assertEquals(
			RevokeGrantFromUser(Seq(PrivilegeType.ACCOUNT, PrivilegeType.DDL, PrivilegeType.DCL), Seq("user1", "user2")),
			"REVOKE GRANT OPTION ACCOUNT, DDL, DCL FROM USER user1, user2"
		)
		assertEquals(
			RevokeGrantFromGroup(Seq(PrivilegeType.ACCOUNT, PrivilegeType.DDL, PrivilegeType.DCL), Seq("group1", "group2")),
			"REVOKE GRANT OPTION ACCOUNT, DDL, DCL FROM Group group1, group2"
		)
	}

	test("grant and revoke privilege") {
		assertEquals(
			GrantPrivilegeToUser(Seq(PrivilegeType.ACCOUNT, PrivilegeType.DDL), Seq("user1", "user2")),
			"GRANT ACCOUNT, DDL TO USER user1, user2"
		)

		assertEquals(
			GrantPrivilegeToGroup(Seq(PrivilegeType.ACCOUNT), Seq("group1", "group2")),
			"GRANT ACCOUNT TO GROUP group1, group2"
		)

		assertEquals(
			RevokePrivilegeFromUser(Seq(PrivilegeType.ACCOUNT), Seq("user1", "user2")),
			"REVOKE ACCOUNT FROM USER user1, user2"
		)

		assertEquals(
			RevokePrivilegeFromGroup(Seq(PrivilegeType.ACCOUNT), Seq("group1", "group2")),
			"REVOKE ACCOUNT FROM GROUP group1, group2"
		)
	}

	test("grant resource to user") {
		assertEquals(
			GrantResourceToUser(Seq(SelectPrivilege(Seq())), MbTableIdentifier("t1", Some("db")), Seq("user1", "user2")),
			"GRANT SELECT ON db.t1 TO USER user1, user2"
		)

		assertEquals(
			GrantResourceToUser(Seq(SelectPrivilege(Seq("id", "name"))), MbTableIdentifier("t1", Some("db")), Seq("user1", "user2")),
			"GRANT SELECT(id, name) ON db.t1 TO USER user1, user2"
		)

		assertEquals(
			GrantResourceToUser(Seq(SelectPrivilege(Seq("id", "name")), UpdatePrivilege(Seq("id", "name"))), MbTableIdentifier("*", Some("db")), Seq("user1", "user2")),
			"GRANT SELECT(id, name), UPDATE(id, name) ON db.* TO USER user1, user2"
		)

		assertEquals(
			GrantResourceToUser(Seq(SelectPrivilege(Seq("id", "name")),
				UpdatePrivilege(Seq("id", "name")),
				InsertPrivilege,
				DeletePrivilege
			),
				MbTableIdentifier("*", Some("db")), Seq("user1", "user2")),
			"GRANT SELECT(id, name), UPDATE(id, name), INSERT, DELETE ON db.* TO USER user1, user2"
		)
	}

	test("revoke resource from user") {
		assertEquals(
			RevokeResourceFromUser(Seq(SelectPrivilege(Seq())), MbTableIdentifier("t1", Some("db")), Seq("user1", "user2")),
			"REVOKE SELECT ON db.t1 FROM USER user1, user2"
		)

		assertEquals(
			RevokeResourceFromUser(Seq(SelectPrivilege(Seq("id", "name"))), MbTableIdentifier("t1", Some("db")), Seq("user1", "user2")),
			"REVOKE SELECT(id, name) ON db.t1 FROM USER user1, user2"
		)

		assertEquals(
			RevokeResourceFromUser(Seq(SelectPrivilege(Seq("id", "name")), UpdatePrivilege(Seq("id", "name"))), MbTableIdentifier("*", Some("db")), Seq("user1", "user2")),
			"REVOKE SELECT(id, name), UPDATE(id, name) ON db.* FROM USER user1, user2"
		)

		assertEquals(
			RevokeResourceFromUser(Seq(SelectPrivilege(Seq("id", "name")),
				UpdatePrivilege(Seq("id", "name")),
				InsertPrivilege,
				DeletePrivilege
			),
				MbTableIdentifier("*", Some("db")), Seq("user1", "user2")),
			"REVOKE SELECT(id, name), UPDATE(id, name), INSERT, DELETE ON db.* FROM USER user1, user2"
		)
	}

	test("grant resources to group") {
		assertEquals(
			GrantResourceToGroup(Seq(SelectPrivilege(Seq())), MbTableIdentifier("t1", Some("db")), Seq("group1", "group2")),
			"GRANT SELECT ON db.t1 TO GROUP group1, group2"
		)

		assertEquals(
			GrantResourceToGroup(Seq(SelectPrivilege(Seq("id", "name"))), MbTableIdentifier("t1", Some("db")), Seq("group1", "group2")),
			"GRANT SELECT(id, name) ON db.t1 TO GROUP group1, group2"
		)

		assertEquals(
			GrantResourceToGroup(Seq(SelectPrivilege(Seq("id", "name")), UpdatePrivilege(Seq("id", "name"))), MbTableIdentifier("*", Some("db")), Seq("group1", "group2")),
			"GRANT SELECT(id, name), UPDATE(id, name) ON db.* TO GROUP group1, group2"
		)

		assertEquals(
			GrantResourceToGroup(Seq(SelectPrivilege(Seq("id", "name")),
				UpdatePrivilege(Seq("id", "name")),
				InsertPrivilege,
				DeletePrivilege
			), MbTableIdentifier("*", Some("db")), Seq("group1", "group2")),
			"GRANT SELECT(id, name), UPDATE(id, name), INSERT, DELETE ON db.* TO GROUP group1, group2"
		)
	}

	test("revoke resoruces from group") {
		assertEquals(
			RevokeResourceFromGroup(Seq(SelectPrivilege(Seq())), MbTableIdentifier("t1", Some("db")), Seq("group1", "group2")),
			"REVOKE SELECT ON db.t1 FROM GROUP group1, group2"
		)

		assertEquals(
			RevokeResourceFromGroup(Seq(SelectPrivilege(Seq("id", "name"))), MbTableIdentifier("t1", Some("db")), Seq("group1", "group2")),
			"REVOKE SELECT(id, name) ON db.t1 FROM GROUP group1, group2"
		)

		assertEquals(
			RevokeResourceFromGroup(Seq(SelectPrivilege(Seq("id", "name")), UpdatePrivilege(Seq("id", "name"))), MbTableIdentifier("*", Some("db")), Seq("group1", "group2")),
			"REVOKE SELECT(id, name), UPDATE(id, name) ON db.* FROM GROUP group1, group2"
		)

		assertEquals(
			RevokeResourceFromGroup(Seq(SelectPrivilege(Seq("id", "name")),
				UpdatePrivilege(Seq("id", "name")),
				InsertPrivilege,
				DeletePrivilege
			), MbTableIdentifier("*", Some("db")), Seq("group1", "group2")),
			"REVOKE SELECT(id, name), UPDATE(id, name), INSERT, DELETE ON db.* FROM GROUP group1, group2"
		)
	}
}
