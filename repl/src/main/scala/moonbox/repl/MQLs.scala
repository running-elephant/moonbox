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

package moonbox.repl

object MQLs {
	val MQL = Seq(
		"CREATE ORG [IF NOT EXISTS] name",
		"RENAME ORG name TO name",
		"ALTER ORG name RENAME TO name",
		"ALTER ORG name SET COMMENT comment",
		"DROP ORG [IF EXISTS] name [CASCADE]",

		"CREATE SA [IF NOT EXISTS] name IN [ORG] org_name IDENTIFIED BY password",
		"RENAME SA name IN [ORG] org_name TO name",
		"ALTER SA name IN [ORG] org_name RENAME TO name",
		"ALTER SA name IN [ORG] org_name IDENTIFIED BY password",
		"DROP SA [IF EXISTS] name IN [ORG] org_name",

		"CREATE USER [IF NOT EXISTS] name IDENTIFIED BY password",
		"RENAME USER name TO name",
		"ALTER USER name RENAME TO name",
		"ALTER USER name IDENTIFIED BY password",
		"DROP USER [IF EXISTS] name",

		"GRANT ACCOUNT, DDL, DCL TO USER name",
		"REVOKE ACCOUNT, DDL, DCL FROM USER name",

		"GRANT GRANT OPTION ACCOUNT, DDL, DCL TO USER name",
		"REVOKE GRANT OPTION ACCOUNT, DDL, DCL FROM USER name",

		"GRANT SELECT(col, col), UPDATE(col, col), INSERT, DELETE, TRUNCATE ON [db.]table TO USER name",
		"REVOKE SELECT(col, col), UPDATE(col, col), INSERT, DELETE, TRUNCATE ON [db.]table FROM USER name",

		"CREATE DATABASE [IF NOT EXISTS] name [COMMENT comment]",
		"RENAME DATABASE name TO name",
		"ALTER DATABASE name RENAME TO name",
		"ALTER DATABASE name SET COMMENT comment",
		"DROP DATABASE [IF EXISTS] name [CASCADE]",

		"MOUNT TABLE [db.]name OPTIONS(key 'value', key 'value')",
		"RENAME TABLE [db.]name TO name",
		"ALTER TABLE [db.]name RENAME TO name",
		"ALTER TABLE [db.]name SET OPTIONS(key 'value', key 'value')",
		"UNMOUNT TABLE [IF EXISTS] [db.]name",

		"MOUNT DATABASE [IF NOT EXISTS] name OPTIONS(key 'value', key 'value')",
		"ALTER DATABASE name SET OPTIONS(key 'value', key 'value')",
		"UNMOUNT DATABASE [IF EXISTS] name [CASCADE]",
		"REFRESH DATABASE name",

		"CREATE [OR REPLACE] VIEW [db.]name [COMMENT comment] AS SELECT ...",
		"RENAME VIEW [db.]name TO name",
		"ALTER VIEW [db.]name RENAME TO name",
		"ALTER VIEW [db.]name SET COMMENT comment",
		"ALTER VIEW [db.]name AS SELECT ...",
		"DROP VIEW [IF EXISTS] [db.]name",

		"CREATE PROC [IF NOT EXISTS] name USING {MQL | HQL} AS (mql;mql ...)",
		"RENAME PROC name TO name",
		"ALTER PROC name RENAME TO name",
		"ALTER PROC name AS (mql;mql ...)",
		"DROP PROC [IF EXISTS] name",

		"CREATE [DEFINER user] EVENT [IF NOT EXISTS] name ON SCHEDULE AT expression [COMMENT comment] DO CALL proc",
		"RENAME EVENT name TO name",
		"ALTER EVENT name RENAME TO name",
		"ALTER DEFINER definer EVENT name",
		"ALTER EVENT name ON SCHEDULE AT expression",
		"ALTER EVENT name {ENABLE | DISABLE}",
		"DROP EVENT [IF EXITS] name",

		"SHOW DATABASES [LIKE pattern]",
		"SHOW TABLES [IN db][LIKE pattern]",
		"SHOW FUNCTIONS [IN db][LIKE pattern]",
		"SHOW USERS [LIKE pattern]",
		"SHOW PROCEDURES [LIKE pattern]",
		"SHOW GRANTS FOR user",

		"DESC DATABASE name",
		"DESC [TABLE] [db.]name",
		"DESC FUNCTION [db.]name",
		"DESC USER name",
		"DESC EVENT name",

		"USE db",

		"EXPLAIN [EXTENDED] [PLAN] SELECT ...",
		"INSERT [INTO | OVERWRITE] [db.]name [PARTITION(col, col)] SELECT ...",
		"CREATE [OR REPLACE] [CACHE] [TEMP] VIEW name AS SELECT ...",

		"%SET TRUNCATE=[Int]",
		"%SET MAX_COUNT=[Int]",
		"%SET TIMEOUT=[Int]",
		"%SET FETCH_SIZE=[Long]",
		"STATE",
		"CMDs",
		"HELP",
		"HISTORY",
		"RECONNECT",
		"QUIT",
		"EXIT"
	)
}
