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

class NoSuchDatabaseException(db: String)
	extends Exception(s"Database '$db' not found")

class NoSuchDatasourceException(user: String)
	extends Exception(s"Datasource '$user' not found")

class NoSuchProcedureException(proc: String)
	extends Exception(s"Procedure '$proc' not found")

class NoSuchTimedEventException(event: String)
	extends Exception(s"Scheduler '$event' not found")

class NoSuchTableException(db: String, table: String)
	extends Exception(s"Table or view '$table' not found in database '$db'")

class NoSuchColumnException(table: String, column: String)
	extends Exception(s"Column '$column' not found in table $table")
case class NoSuchGrantColumnException(user: String, table: String, column: String)
	extends Exception(s"Column '$column' in table $table has not been granted to user $user")

class NoSuchTablesException(table: (String, String)*)
	extends Exception(s"Table or view '${table.map {case (t, d) => s"d.t"}.mkString(",")}'")

class NoSuchFunctionException(db: String, func: String)
	extends Exception(s"Function '$func' not found in database '$db'")

class NoSuchViewException(db: String, view: String)
	extends Exception(s"View '$view' not found in database '$db'")

class NoSuchUserException(user: String)
	extends Exception(s"User '$user' not found")

class NoSuchUserTableRelException(user: String, table: String, column: String)
	extends Exception(s"Relationship of User '$user' and Column '$column' in Table '$table' not found")

class NoSuchOrganizationException(org: String)
	extends Exception(s"Organization '$org' not found")

class NoSuchAvailableTableException(table: String, user: String)
	extends Exception(s"No table '$table' available for user '$user'")

class NoSuchApplicationException(app: String)
	extends Exception(s"Application '$app' not found")

class NoSuchGroupException(group: String)
	extends Exception(s"Group '$group' not found")
