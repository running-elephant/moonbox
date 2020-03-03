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

class ExistsException(msg: String) extends Exception(msg)


class GroupExistsException(group: String)
	extends ExistsException(s"Group $group already exists")

class TableExistsException(db: String, table: String)
	extends ExistsException(s"Table $table already exists in database $db")

class FunctionExistsException(db: String, func: String)
	extends ExistsException(s"Function $func already exists in database $db")

class ViewExistsException(db: String, view: String)
	extends ExistsException(s"View $view already exists in database $db")

class ColumnExistsException(table: String, column: String)
	extends ExistsException(s"Column $column already exists in table $table")

class UserExistsException(user: String)
	extends ExistsException(s"User $user already exists")

class DatabaseExistsException(db: String)
	extends ExistsException(s"Database $db already exists")

class DatasourceExistsException(ds: String)
	extends ExistsException(s"Datasource $ds already exists")

class OrganizationExistsException(org: String)
	extends ExistsException(s"Organization $org already exists")

class ProcedureExistsException(proc: String)
	extends ExistsException(s"Procedure $proc already exists")

class TimedEventExistsException(event: String)
	extends ExistsException(s"Event $event already exists")

class ApplicationExistsException(app: String)
	extends ExistsException(s"Application $app already exists")

class ClusterExistsException(cluster: String)
	extends ExistsException(s"Cluster $cluster already exists")

