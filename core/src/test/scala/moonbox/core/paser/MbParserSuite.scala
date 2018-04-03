package moonbox.core.paser

import moonbox.core.{MbColumnIdentifier, MbFunctionIdentifier, MbTableIdentifier}
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
			"RENAME SA sa TO sa1 IN ORG org",
			"ALTER SA sa RENAME TO sa1 IN ORG org"
		)

		assertEquals(
			AlterSaSetPassword("sa", "123abc", "org"),
			"ALTER SA sa IDENTIFIED BY 123abc IN ORG org"
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

	test("datasource") {
		assertEquals(
			MountDatasource("datasource", Map("key" -> "value", "key1" -> "value1"), ignoreIfExists = false),
			"MOUNT DATASOURCE datasource OPTIONS(key 'value', key1 'value1')"
		)

		assertEquals(
			MountDatasource("datasource", Map("key" -> "value", "key1" -> "value1"), ignoreIfExists = true),
			"MOUNT DATASOURCE IF NOT EXISTS datasource OPTIONS(key = 'value', key1 = 'value1')"
		)

		assertEquals(
			AlterDatasourceSetName("datasource", "datasource1"),
			"RENAME DATASOURCE datasource TO datasource1",
			"ALTER DATASOURCE datasource RENAME TO datasource1"
		)

		assertEquals(
			AlterDatasourceSetOptions("datasource", Map("key" -> "value", "key1" -> "value1")),
			"ALTER DATASOURCE datasource SET OPTIONS(key 'value', key1 'value1')"
		)

		assertEquals(
			UnmountDatasource("datasource", ignoreIfNotExists = false),
			"UNMOUNT DATASOURCE datasource"
		)

		assertEquals(
			UnmountDatasource("datasource", ignoreIfNotExists = true),
			"UNMOUNT DATASOURCE IF EXISTS datasource"
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
			MountTableWithDatasoruce("datasource",
				Seq(
					(MbTableIdentifier("table", None), Some(
						StructType(Seq(
							StructField("name", StringType),
							StructField("age", IntegerType)
						))
					), Map("dbtable" -> "table")),
					(MbTableIdentifier("table1", None), None, Map("dbtable" -> "table1"))
				),
				isStream = false,
				ignoreIfExists = false
			),
			"WITH DATASOURCE datasource MOUNT TABLE table(name string, age int) OPTIONS(dbtable 'table'), table1 OPTIONS(dbtable 'table1')"
		)

		assertEquals(
			MountTableWithDatasoruce("datasource",
				Seq(
					(MbTableIdentifier("table", Some("db")), None, Map("dbtable" -> "table")),
					(MbTableIdentifier("table1", Some("db1")), None, Map("dbtable" -> "table1"))
				),
				isStream = true,
				ignoreIfExists = true
			),
			"WITH DATASOURCE datasource MOUNT STREAM TABLE IF NOT EXISTS db.table OPTIONS(dbtable 'table'), db1.table1 OPTIONS(dbtable 'table1')"
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
			AlterTableAddColumns(MbTableIdentifier("table", None),
				StructType(Seq(
					StructField("name", StringType),
					StructField("age", IntegerType)
				))
			),
			"ALTER TABLE table ADD COLUMNS (name string, age int)"
		)

		assertEquals(
			AlterTableChangeColumn(MbTableIdentifier("table", None),
				StructField("age", ShortType)
			),
			"ALTER TABLE table CHANGE COLUMN age smallint"
		)

		assertEquals(
			AlterTableDropColumn(
				MbTableIdentifier("table", None),
				"name"
			),
			"ALTER TABLE table DROP COLUMN name"
		)

		assertEquals(
			UnmountTable(MbTableIdentifier("table", Some("db")), ignoreIfNotExists = true),
			"UNMOUNT TABLE IF EXISTS db.table"
		)
	}

	test("function") {
		assertEquals(
			CreateFunction(MbFunctionIdentifier("func", Some("db")), Map("key" -> "value"), ignoreIfExists = true),
			"CREATE FUNCTION IF NOT EXISTS db.func OPTIONS(key 'value')"
		)

		assertEquals(
			AlterFunctionSetName(MbFunctionIdentifier("func", Some("db")), MbFunctionIdentifier("func1", Some("db"))),
			"RENAME FUNCTION db.func TO db.func1",
			"ALTER FUNCTION db.func RENAME TO db.func1"
		)

		assertEquals(
			AlterFunctionSetOptions(MbFunctionIdentifier("func", Some("db")), Map("key" -> "value")),
			"ALTER FUNCTION db.func SET OPTIONS(key 'value')"
		)

		assertEquals(
			DropFunction(MbFunctionIdentifier("func", Some("db")), ignoreIfNotExists = true),
			"DROP FUNCTION IF EXISTS db.func"
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
			"CREATE APPLICATION IF NOT EXISTS app AS CREATE TEMP VIEW view AS SELECT * FROM table, SELECT * FROM view"
		)

		assertEquals(
			CreateApplication("app", Seq("CREATE TEMP FUNCTION func OPTIONS(key 'value')", "INSERT INTO db.table1 SELECT func(col) FROM table"), ignoreIfExists = true),
			"CREATE APPLICATION IF NOT EXISTS app AS CREATE TEMP FUNCTION func OPTIONS(key 'value'), INSERT INTO db.table1 SELECT func(col) FROM table"
		)

		assertEquals(
			CreateApplication("app", Seq("SELECT * FROM table"), ignoreIfExists = true),
			"CREATE APPLICATION IF NOT EXISTS app AS SELECT * FROM table"
		)
	}

	test("show sysinfo") {
		assertEquals(
			ShowSysInfo,
			"SHOW SYSINFO"
		)
	}

	test("show datasources") {
		assertEquals(
			ShowDatasources(pattern = Some("abc%")),
			"SHOW DATASOURCES LIKE 'abc%'"
		)

		assertEquals(
			ShowDatasources(pattern = None),
			"SHOW DATASOURCES"
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

	test("desc datasource") {
		assertEquals(
			DescDatasource("ds", extended = false),
			"DESC DATASOURCE ds",
			"DESCRIBE DATASOURCE ds"
		)

		assertEquals(
			DescDatasource("ds", extended = true),
			"DESC DATASOURCE EXTENDED ds",
			"DESCRIBE DATASOURCE EXTENDED ds"
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
			SetConfiguration("key", "value"),
			"SET key = 'value'",
			"SET key 'value'"
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

	test("temp function") {
		assertEquals(
			CreateTempFunction("func", Map("key" -> "value"), replaceIfExists = false),
			"CREATE TEMP FUNCTION func OPTIONS(key 'value')",
			"CREATE TEMPORARY FUNCTION func OPTIONS(key 'value')"
		)
		assertEquals(
			CreateTempFunction("func", Map("key" -> "value"), replaceIfExists = true),
			"CREATE OR REPLACE TEMP FUNCTION func OPTIONS(key 'value')",
			"CREATE OR REPLACE TEMPORARY FUNCTION func OPTIONS(key 'value')"
		)
	}

	test("grant and revoke grant") {
		assertEquals(
			GrantGrantToUser(Seq(GrantAccount, GrantDdl, GrantDmlOn), Seq("user1", "user2")),
			"GRANT GRANT OPTION ACCOUNT, DDL, DMLON TO USER user1, user2"
		)
		assertEquals(
			GrantGrantToGroup(Seq(GrantAccount, GrantDdl, GrantDmlOn), Seq("group1", "group2")),
			"GRANT GRANT OPTION ACCOUNT, DDL, DMLON TO Group group1, group2"
		)

		assertEquals(
			RevokeGrantFromUser(Seq(GrantAccount, GrantDdl, GrantDmlOn), Seq("user1", "user2")),
			"REVOKE GRANT OPTION ACCOUNT, DDL, DMLON FROM USER user1, user2"
		)
		assertEquals(
			RevokeGrantFromGroup(Seq(GrantAccount, GrantDdl, GrantDmlOn), Seq("group1", "group2")),
			"REVOKE GRANT OPTION ACCOUNT, DDL, DMLON FROM Group group1, group2"
		)
	}

	test("grant and revoke account") {
		assertEquals(
			GrantAccountToUser(Seq("user1", "user2")),
			"GRANT ACCOUNT TO USER user1, user2"
		)

		assertEquals(
			GrantAccountToGroup(Seq("group1", "group2")),
			"GRANT ACCOUNT TO GROUP group1, group2"
		)

		assertEquals(
			RevokeAccountFromUser(Seq("user1", "user2")),
			"REVOKE ACCOUNT FROM USER user1, user2"
		)

		assertEquals(
			RevokeAccountFromGroup(Seq("group1", "group2")),
			"REVOKE ACCOUNT FROM GROUP group1, group2"
		)
	}

	test("grant and revoke ddl") {
		assertEquals(
			GrantDdlToUser(Seq("user1", "user2")),
			"GRANT DDL TO USER user1, user2"
		)

		assertEquals(
			GrantDdlToGroup(Seq("group1", "group2")),
			"GRANT DDL TO GROUP group1, group2"
		)

		assertEquals(
			RevokeDdlFromUser(Seq("user1", "user2")),
			"REVOKE DDL FROM USER user1, user2"
		)

		assertEquals(
			RevokeDdlFromGroup(Seq("group1", "group2")),
			"REVOKE DDL FROM GROUP group1, group2"
		)
	}

	test("grant dml on to user") {
		assertEquals(
			GrantDmlOnToUser(Seq(MbColumnIdentifier("*", "*", Some("db"))), Seq("user1", "user2")),
			"GRANT DML ON db.*.* TO USER user1, user2"
		)

		assertEquals(
			GrantDmlOnToUser(Seq(MbColumnIdentifier("*", "table", Some("db"))), Seq("user1", "user2")),
			"GRANT DML ON db.table.* TO USER user1, user2"
		)

		assertEquals(
			GrantDmlOnToUser(Seq(
				MbColumnIdentifier("col1", "table", Some("db")),
				MbColumnIdentifier("col2", "table", Some("db"))
			), Seq("user1", "user2")),
			"GRANT DML ON db.table.{col1, col2} TO USER user1, user2"
		)

		assertEquals(
			GrantDmlOnToUser(Seq(
				MbColumnIdentifier("col1", "table1", None),
				MbColumnIdentifier("col2", "table1", None),
				MbColumnIdentifier("col1", "table2", None),
				MbColumnIdentifier("col2", "table2", None)
			), Seq("user1", "user2")),
			"GRANT DML ON table1.{col1, col2}, table2.{col1, col2} TO USER user1, user2"
		)

		assertEquals(
			GrantDmlOnToUser(Seq(
				MbColumnIdentifier("col1", "table", Some("db1")),
				MbColumnIdentifier("col2", "table", Some("db1")),
				MbColumnIdentifier("*", "*", Some("db2"))
			), Seq("user1", "user2")),
			"GRANT DML ON db1.table.{col1, col2}, db2.*.* TO USER user1, user2"
		)

		assertEquals(
			GrantDmlOnToUser(Seq(
				MbColumnIdentifier("*", "table1", Some("db1")),
				MbColumnIdentifier("*", "table2", Some("db1")),
				MbColumnIdentifier("*", "*", Some("db2"))
			), Seq("user1", "user2")),
			"GRANT DML ON db1.{table1, table2}.*, db2.*.* TO USER user1, user2"
		)
	}

	test("revoke dml on from user") {
		assertEquals(
			RevokeDmlOnFromUser(Seq(MbColumnIdentifier("*", "*", Some("db"))), Seq("user1", "user2")),
			"REVOKE DML ON db.*.* FROM USER user1, user2"
		)

		assertEquals(
			RevokeDmlOnFromUser(Seq(MbColumnIdentifier("*", "table", Some("db"))), Seq("user1", "user2")),
			"REVOKE DML ON db.table.* FROM USER user1, user2"
		)

		assertEquals(
			RevokeDmlOnFromUser(Seq(
				MbColumnIdentifier("col1", "table", Some("db")),
				MbColumnIdentifier("col2", "table", Some("db"))
			), Seq("user1", "user2")),
			"REVOKE DML ON db.table.{col1, col2} FROM USER user1, user2"
		)

		assertEquals(
			RevokeDmlOnFromUser(Seq(
				MbColumnIdentifier("col1", "table1", None),
				MbColumnIdentifier("col2", "table1", None),
				MbColumnIdentifier("col1", "table2", None),
				MbColumnIdentifier("col2", "table2", None)
			), Seq("user1", "user2")),
			"REVOKE DML ON table1.{col1, col2}, table2.{col1, col2} FROM USER user1, user2"
		)

		assertEquals(
			RevokeDmlOnFromUser(Seq(
				MbColumnIdentifier("col1", "table", Some("db1")),
				MbColumnIdentifier("col2", "table", Some("db1")),
				MbColumnIdentifier("*", "*", Some("db2"))
			), Seq("user1", "user2")),
			"REVOKE DML ON db1.table.{col1, col2}, db2.*.* FROM USER user1, user2"
		)

		assertEquals(
			RevokeDmlOnFromUser(Seq(
				MbColumnIdentifier("*", "table1", Some("db1")),
				MbColumnIdentifier("*", "table2", Some("db1")),
				MbColumnIdentifier("*", "*", Some("db2"))
			), Seq("user1", "user2")),
			"REVOKE DML ON db1.{table1, table2}.*, db2.*.* FROM USER user1, user2"
		)
	}

	test("grant dml on to group") {
		assertEquals(
			GrantDmlOnToGroup(Seq(MbColumnIdentifier("*", "*", Some("db"))), Seq("group1", "group2")),
			"GRANT DML ON db.*.* TO GROUP group1, group2"
		)

		assertEquals(
			GrantDmlOnToGroup(Seq(MbColumnIdentifier("*", "table", Some("db"))), Seq("group1", "group2")),
			"GRANT DML ON db.table.* TO GROUP group1, group2"
		)

		assertEquals(
			GrantDmlOnToGroup(Seq(
				MbColumnIdentifier("col1", "table", Some("db")),
				MbColumnIdentifier("col2", "table", Some("db"))
			), Seq("group1", "group2")),
			"GRANT DML ON db.table.{col1, col2} TO GROUP group1, group2"
		)

		assertEquals(
			GrantDmlOnToGroup(Seq(
				MbColumnIdentifier("col1", "table1", None),
				MbColumnIdentifier("col2", "table1", None),
				MbColumnIdentifier("col1", "table2", None),
				MbColumnIdentifier("col2", "table2", None)
			), Seq("group1", "group2")),
			"GRANT DML ON table1.{col1, col2}, table2.{col1, col2} TO GROUP group1, group2"
		)

		assertEquals(
			GrantDmlOnToGroup(Seq(
				MbColumnIdentifier("col1", "table", Some("db1")),
				MbColumnIdentifier("col2", "table", Some("db1")),
				MbColumnIdentifier("*", "*", Some("db2"))
			), Seq("group1", "group2")),
			"GRANT DML ON db1.table.{col1, col2}, db2.*.* TO GROUP group1, group2"
		)

		assertEquals(
			GrantDmlOnToGroup(Seq(
				MbColumnIdentifier("*", "table1", Some("db1")),
				MbColumnIdentifier("*", "table2", Some("db1")),
				MbColumnIdentifier("*", "*", Some("db2"))
			), Seq("group1", "group2")),
			"GRANT DML ON db1.{table1, table2}.*, db2.*.* TO GROUP group1, group2"
		)
	}

	test("revoke dml on from group") {
		assertEquals(
			RevokeDmlOnFromGroup(Seq(MbColumnIdentifier("*", "*", Some("db"))), Seq("group1", "group2")),
			"REVOKE DML ON db.*.* FROM GROUP group1, group2"
		)

		assertEquals(
			RevokeDmlOnFromGroup(Seq(MbColumnIdentifier("*", "table", Some("db"))), Seq("group1", "group2")),
			"REVOKE DML ON db.table.* FROM GROUP group1, group2"
		)

		assertEquals(
			RevokeDmlOnFromGroup(Seq(
				MbColumnIdentifier("col1", "table", Some("db")),
				MbColumnIdentifier("col2", "table", Some("db"))
			), Seq("group1", "group2")),
			"REVOKE DML ON db.table.{col1, col2} FROM GROUP group1, group2"
		)

		assertEquals(
			RevokeDmlOnFromGroup(Seq(
				MbColumnIdentifier("col1", "table1", None),
				MbColumnIdentifier("col2", "table1", None),
				MbColumnIdentifier("col1", "table2", None),
				MbColumnIdentifier("col2", "table2", None)
			), Seq("group1", "group2")),
			"REVOKE DML ON table1.{col1, col2}, table2.{col1, col2} FROM GROUP group1, group2"
		)

		assertEquals(
			RevokeDmlOnFromGroup(Seq(
				MbColumnIdentifier("col1", "table", Some("db1")),
				MbColumnIdentifier("col2", "table", Some("db1")),
				MbColumnIdentifier("*", "*", Some("db2"))
			), Seq("group1", "group2")),
			"REVOKE DML ON db1.table.{col1, col2}, db2.*.* FROM GROUP group1, group2"
		)

		assertEquals(
			RevokeDmlOnFromGroup(Seq(
				MbColumnIdentifier("*", "table1", Some("db1")),
				MbColumnIdentifier("*", "table2", Some("db1")),
				MbColumnIdentifier("*", "*", Some("db2"))
			), Seq("group1", "group2")),
			"REVOKE DML ON db1.{table1, table2}.*, db2.*.* FROM GROUP group1, group2"
		)
	}
}
