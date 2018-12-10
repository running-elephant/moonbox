/*-
 * <<
 * Moonbox
 * ==
 * Copyright (C) 2016 - 2018 EDP
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
		"CREATE SA name IDENTIFIED BY password",
		"ALTER SA name IDENTIFIED BY password",
		"RENAME SA name AS name",
		"DROP SA name",
		"CREATE ORG name",
		"RENAME ORG name AS name",
		"DROP ORG name",
		"CREATE USER name IDENTIFIED BY password",
		"ALTER USER name IDENTIFIED BY password",
		"RENAME USER name AS name",
		"DROP USER name",
		"SHOW USERS",
		"CREATE GROUP name",
		"ALTER GROUP name ADD USER name, name REMOVE USER name",
		"RENAME GROUP name AS name",
		"DROP GROUP name",
		"SHOW GROUPS",
		"DESC GROUP name",
		"CREATE DATABASE name",
		"RENAME DATABASE name AS name",
		"DROP DATABASE name",
		"USE name",
		"MOUNT TABLE name OPTIONS (k1=v1, k2=v2, k3=v3)",
		"MOUNT TABLE name OPTIONS (k1=v1, k2=v2, k3=v3)",
		"MOUNT DATASOURCE name OPTIONS (k1=v1, k2=v2, k3=v3)",
		"UNMOUNT TABLE name",
		"SHOW DATABASES",
		"USE name",
		"SHOW TABLES",
		"DESC DATABASE name",
		"USE name",
		"DESC TABLE name",
		"DESC TABLE EXTENDED name",
		"SELECT * FROM db.table",
		"CREATE VIEW name AS SELECT ...",
		"CREATE FUNCTION name OPTIONS?",
		"RENAME FUNCTION name AS name",
		"DROP FUNCTION name",
		"GRANT ACCOUNT TO USER name",
		"GRANT DDL TO USER  name",
		"GRANT ACCOUNT TO GROUP name",
		"GRANT DDL TO GROUP name",
		"GRANT DML ON db.table.* TO USER name",
		"GRANT DML ON table.col, table.col TO USER name",
		"REVOKE DDL FROM USER name",
		"REVOKE DML ON db.table.* FROM GROUP name",
		"CREATE PROCEDURE name AS (INSERT ...)",
		"CREATE EVENT name ON SCHEDULE AT '...' DO CALL name",
		"ALTER EVENT name ENABLE",
		"ALTER EVENT name DISABLE",
		"ALTER EVENT name ON SCHEDULE AT '...'",
		"ALTER DEFINER name EVENT name",
		"RENAME PROC name TO name",
		"DROP PROC name",
		"DROP EVENT name",
		"DESC EVENT name",
		"SHOW PROCEDURES",
		"SHOW EVENTS",
		"SHOW RUNNING EVENTS",
		"SHOW SYSINFO",
		"SHOW JOBS",
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
