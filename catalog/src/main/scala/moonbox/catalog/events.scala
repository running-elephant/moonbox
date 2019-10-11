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

import moonbox.common.util.ListenerEvent

trait CatalogEventListener {
	def onEvent(event: CatalogEvent): Unit
}

trait CatalogEvent extends ListenerEvent

trait DatabaseEvent extends CatalogEvent

case class CreateDatabasePreEvent(
	organization: String,
	database: String) extends DatabaseEvent

case class CreateDatabaseEvent(
	organization: String,
	database: String) extends DatabaseEvent

case class DropDatabasePreEvent(
	organization: String,
	database: String) extends DatabaseEvent

case class DropDatabaseEvent(
	organization: String,
	database: String) extends DatabaseEvent

case class RenameDatabasePreEvent(
	organization: String,
	database: String,
	newDatabase: String) extends DatabaseEvent

case class RenameDatabaseEvent(
	organization: String,
	database: String,
	newDatabase: String) extends DatabaseEvent


trait TableEvent extends DatabaseEvent

case class CreateTablePreEvent(
	organization: String,
	database: String,
	table: String) extends TableEvent

case class CreateTableEvent(
	organization: String,
	database: String,
	table: String) extends TableEvent

case class DropTablePreEvent(
	organization: String,
	database: String,
	table: String) extends TableEvent

case class DropTableEvent(
	organization: String,
	database: String,
	table: String) extends TableEvent

case class RenameTablePreEvent(
	organization: String,
	database: String,
	table: String,
	newTable: String) extends TableEvent

case class RenameTableEvent(
	organization: String,
	database: String,
	table: String,
	newTable: String) extends TableEvent


trait FunctionEvent extends DatabaseEvent

case class CreateFunctionPreEvent(
	organization: String,
	database: String,
	function: String) extends FunctionEvent

case class CreateFunctionEvent(
	organization: String,
	database: String,
	function: String) extends FunctionEvent

case class DropFunctionPreEvent(
	organization: String,
	database: String,
	function: String) extends FunctionEvent

case class DropFunctionEvent(
	organization: String,
	database: String,
	function: String) extends FunctionEvent

case class RenameFunctionPreEvent(
	organization: String,
	database: String,
	function: String,
	newFunction: String) extends FunctionEvent

case class RenameFunctionEvent(
	organization: String,
	database: String,
	function: String,
	newFunction: String) extends FunctionEvent

trait ApplicationEvent extends CatalogEvent

case class CreateApplicationPreEvent(
	name: String) extends ApplicationEvent

case class CreateApplicationEvent(
	name: String) extends ApplicationEvent

case class DropApplicationPreEvent(
	name: String) extends ApplicationEvent

case class DropApplicationEvent(
	name: String) extends ApplicationEvent


trait OrganizationEvent extends CatalogEvent

case class CreateOrganizationPreEvent(
	organization: String) extends OrganizationEvent

case class CreateOrganizationEvent(
	organization: String) extends OrganizationEvent

case class DropOrganizationPreEvent(
	organization: String) extends OrganizationEvent

case class DropOrganizationEvent(
	organization: String) extends OrganizationEvent

case class RenameOrganizationPreEvent(
	organization: String,
	newOrganization: String) extends OrganizationEvent

case class RenameOrganizationEvent(
	organization: String,
	newOrganization: String) extends OrganizationEvent


trait UserEvent extends CatalogEvent

case class CreateUserPreEvent(
	organization: String,
	user: String) extends UserEvent

case class CreateUserEvent(
	organization: String,
	user: String) extends UserEvent

case class DropUserPreEvent(
	organization: String,
	user: String) extends UserEvent

case class DropUserEvent(
	organization: String,
	user: String) extends UserEvent

case class RenameUserPreEvent(
	organization: String,
	user: String,
	newUser: String) extends UserEvent

case class RenameUserEvent(
	organization: String,
	user: String,
	newUser: String) extends UserEvent


trait ProcedureEvent extends CatalogEvent

case class CreateProcedurePreEvent(
	organization: String,
	procedure: String) extends ProcedureEvent

case class CreateProcedureEvent(
	organization: String,
	procedure: String) extends ProcedureEvent

case class DropProcedurePreEvent(
	organization: String,
	procedure: String) extends ProcedureEvent

case class DropProcedureEvent(
	organization: String,
	procedure: String) extends ProcedureEvent

case class RenameProcedurePreEvent(
	organization: String,
	procedure: String,
	newProcedure: String) extends ProcedureEvent

case class RenameProcedureEvent(
	organization: String,
	procedure: String,
	newProcedure: String) extends ProcedureEvent

trait TimedEventEvent extends CatalogEvent

case class CreateTimedEventPreEvent(
	organization: String,
	event: String) extends TimedEventEvent

case class CreateTimedEventEvent(
	organization: String,
	event: String) extends TimedEventEvent

case class DropTimedEventPreEvent(
	organization: String,
	event: String) extends TimedEventEvent

case class DropTimedEventEvent(
	organization: String,
	event: String) extends TimedEventEvent

case class RenameTimedEventPreEvent(
	organization: String,
	event: String) extends TimedEventEvent

case class RenameTimedEventEvent(
	organization: String,
	event: String) extends TimedEventEvent


trait DatabasePrivilegeEvent extends CatalogEvent

case class CreateDatabasePrivilegePreEvent(
	organization: String,
	user: String,
	database: String,
	privileges: Seq[String]) extends DatabasePrivilegeEvent

case class CreateDatabasePrivilegeEvent(
	organization: String,
	user: String,
	database: String,
	privileges: Seq[String]) extends DatabasePrivilegeEvent

case class DropDatabasePrivilegePreEvent(
	organization: String,
	user: String,
	database: String,
	privileges: Seq[String]) extends DatabasePrivilegeEvent

case class DropDatabasePrivilegeEvent(
	organization: String,
	user: String,
	database: String,
	privileges: Seq[String]) extends DatabasePrivilegeEvent

trait TablePrivilegeEvent extends CatalogEvent

case class CreateTablePrivilegePreEvent(
	organization: String,
	user: String,
	database: String,
	table: String,
	privileges: Seq[String]) extends TablePrivilegeEvent

case class CreateTablePrivilegeEvent(
	organization: String,
	user: String,
	database: String,
	table: String,
	privileges: Seq[String]) extends TablePrivilegeEvent

case class DropTablePrivilegePreEvent(
	organization: String,
	user: String,
	database: String,
	table: String,
	privileges: Seq[String]) extends TablePrivilegeEvent

case class DropTablePrivilegeEvent(
	organization: String,
	user: String,
	database: String,
	table: String,
	privileges: Seq[String]) extends TablePrivilegeEvent

trait ColumnPrivilegeEvent extends CatalogEvent

case class CreateColumnPrivilegePreEvent(
	organization: String,
	user: String,
	database: String,
	table: String,
	privileges: Map[String, Seq[String]]) extends ColumnPrivilegeEvent

case class CreateColumnPrivilegeEvent(
	organization: String,
	user: String,
	database: String,
	table: String,
	privileges: Map[String, Seq[String]]) extends ColumnPrivilegeEvent

case class DropColumnPrivilegePreEvent(
	organization: String,
	user: String,
	database: String,
	table: String,
	privileges: Seq[(String, Seq[String])]) extends ColumnPrivilegeEvent

case class DropColumnPrivilegeEvent(
	organization: String,
	user: String,
	database: String,
	table: String,
	privileges: Seq[(String, Seq[String])]) extends ColumnPrivilegeEvent


trait GroupEvent extends CatalogEvent

case class CreateGroupPreEvent(
	organization: String,
	group: String) extends GroupEvent

case class CreateGroupEvent(
	organization: String,
	group: String) extends GroupEvent

case class DropGroupPreEvent(
	organization: String,
	group: String) extends GroupEvent

case class DropGroupEvent(
	organization: String,
	group: String) extends GroupEvent

case class RenameGroupPreEvent(
	organization: String,
	group: String,
	newGroup: String
) extends GroupEvent

case class RenameGroupEvent(
	organization: String,
	group: String,
	newGroup: String
) extends GroupEvent

case class CreateGroupUserRelPreEvent(
	organization: String,
	group: String,
	users: Seq[String]
) extends GroupEvent

case class CreateGroupUserRelEvent(
	organization: String,
	group: String,
	users: Seq[String]
) extends GroupEvent

case class DropGroupUserRelPreEvent(
	organization: String,
	group: String,
	users: Seq[String]
) extends GroupEvent

case class DropGroupUserRelEvent(
	organization: String,
	group: String,
	users: Seq[String]
) extends GroupEvent

