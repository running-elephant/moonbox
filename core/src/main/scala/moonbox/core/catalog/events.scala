package moonbox.core.catalog

import moonbox.common.util.ListenerEvent

trait CatalogEventListener {
	def onEvent(event: CatalogEvent): Unit
}

trait CatalogEvent extends ListenerEvent {
	val organization: String
}

trait DatasourceEvent extends CatalogEvent {
	val datasource: String
}
case class CreateDatasourcePreEvent(organization: String, datasource: String) extends DatasourceEvent
case class CreateDatasourceEvent(organization: String, datasource: String) extends DatasourceEvent
case class DropDatasourcePreEvent(organization: String, datasource: String) extends DatasourceEvent
case class DropDatasourceEvent(organization: String, datasource: String) extends DatasourceEvent
case class RenameDatasourcePreEvent(
	organization: String,
	datasource: String,
	newDatasource: String) extends DatasourceEvent
case class RenameDatasourceEvent(
	organization: String,
	datasource: String,
	newDatasource: String) extends DatasourceEvent

trait DatabaseEvent extends CatalogEvent {
	val database: String
}
case class CreateDatabasePreEvent(organization: String, database: String) extends DatabaseEvent
case class CreateDatabaseEvent(organization: String, database: String) extends DatabaseEvent
case class DropDatabasePreEvent(organization: String, database: String) extends DatabaseEvent
case class DropDatabaseEvent(organization: String, database: String) extends DatabaseEvent
case class RenameDatabasePreEvent(
	organization: String,
	database: String,
	newDatabase: String) extends DatabaseEvent
case class RenameDatabaseEvent(
	organization: String,
	database: String,
	newDatabase: String) extends DatabaseEvent


trait TableEvent extends DatabaseEvent {
	val table: String
}
case class CreateTablePreEvent(organization: String, database: String, table: String) extends TableEvent
case class CreateTableEvent(organization: String, database: String, table: String) extends TableEvent
case class DropTablePreEvent(organization: String, database: String, table: String) extends TableEvent
case class DropTableEvent(organization: String, database: String, table: String) extends TableEvent
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


trait FunctionEvent extends DatabaseEvent {
	val function: String
}
case class CreateFunctionPreEvent(organization: String, database: String, function: String) extends FunctionEvent
case class CreateFunctionEvent(organization: String, database: String, function: String) extends FunctionEvent
case class DropFunctionPreEvent(organization: String, database: String, function: String) extends FunctionEvent
case class DropFunctionEvent(organization: String, database: String, function: String) extends FunctionEvent
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


trait ViewEvent extends DatabaseEvent {
	val view: String
}
case class CreateViewPreEvent(organization: String, database: String, view: String) extends ViewEvent
case class CreateViewEvent(organization: String, database: String, view: String) extends ViewEvent
case class DropViewPreEvent(organization: String, database: String, view: String) extends ViewEvent
case class DropViewEvent(organization: String, database: String, view: String) extends ViewEvent
case class RenameViewPreEvent(organization: String, database: String, view: String, newView: String) extends ViewEvent
case class RenameViewEvent(organization: String, database: String, view: String, newView: String) extends ViewEvent



trait OrganizationEvent extends CatalogEvent {
	val organization: String
}
case class CreateOrganizationPreEvent(organization: String) extends OrganizationEvent
case class CreateOrganizationEvent(organization: String) extends OrganizationEvent
case class DropOrganizationPreEvent(organization: String)  extends OrganizationEvent
case class DropOrganizationEvent(organization: String)  extends OrganizationEvent
case class RenameOrganizationPreEvent(organization: String, newOrganization: String)  extends OrganizationEvent
case class RenameOrganizationEvent(organization: String, newOrganization: String)  extends OrganizationEvent


trait GroupEvent extends CatalogEvent {
	val group: String
}
case class CreateGroupPreEvent(organization: String, group: String) extends GroupEvent
case class CreateGroupEvent(organization: String, group: String) extends GroupEvent
case class DropGroupPreEvent(organization: String, group: String) extends GroupEvent
case class DropGroupEvent(organization: String, group: String) extends GroupEvent
case class RenameGroupPreEvent(organization: String, group: String, newGroup: String) extends GroupEvent
case class RenameGroupEvent(organization: String, group: String, newGroup: String) extends GroupEvent


trait UserEvent extends CatalogEvent {
	val user: String
}
case class CreateUserPreEvent(organization: String, user: String) extends UserEvent
case class CreateUserEvent(organization: String, user: String) extends UserEvent
case class DropUserPreEvent(organization: String, user: String) extends UserEvent
case class DropUserEvent(organization: String, user: String) extends UserEvent
case class RenameUserPreEvent(organization: String, user: String, newUser: String) extends UserEvent
case class RenameUserEvent(organization: String, user: String, newUser: String) extends UserEvent


trait ApplicationEvent extends CatalogEvent {
	val app: String
}
case class CreateApplicationPreEvent(organization: String, app: String) extends ApplicationEvent
case class CreateApplicationEvent(organization: String, app: String) extends ApplicationEvent
case class DropApplicationPreEvent(organization: String, app: String) extends ApplicationEvent
case class DropApplicationEvent(organization: String, app: String) extends ApplicationEvent
case class RenameApplicationPreEvent(organization: String, app: String, newApp: String) extends ApplicationEvent
case class RenameApplicationEvent(organization: String, app: String, newApp: String) extends ApplicationEvent

trait TimedEventEvent extends CatalogEvent {
	val event: String
}

case class CreateTimedEventPreEvent(organization: String, event: String) extends TimedEventEvent
case class CreateTimedEventEvent(organization: String, event: String) extends TimedEventEvent
case class DropTimedEventPreEvent(organization: String, event: String) extends TimedEventEvent
case class DropTimedEventEvent(organization: String, event: String) extends TimedEventEvent
case class RenameTimedEventPreEvent(organization: String, event: String) extends TimedEventEvent
case class RenameTimedEventEvent(organization: String, event: String) extends TimedEventEvent

trait UserTableRelEvent extends CatalogEvent {
	val user: String
	val database: String
	val table: String
	val columns: Seq[String]
}
case class CreateUserTableRelPreEvent(
	organization: String,
	user: String,
	database: String,
	table: String,
	columns: Seq[String]) extends UserTableRelEvent
case class CreateUserTableRelEvent(
	organization: String,
	user: String,
	database: String,
	table: String,
	columns: Seq[String]) extends UserTableRelEvent
case class DropUserTableRelPreEvent(
	organization: String,
	user: String,
	database: String,
	table: String,
	columns: Seq[String]) extends UserTableRelEvent
case class DropUserTableRelEvent(
	organization: String,
	user: String,
	database: String,
	table: String,
	columns: Seq[String]) extends UserTableRelEvent


trait UserGroupRelEvent extends CatalogEvent

case class CreateUserGroupRelPreEvent(
	organization: String,
	group: String,
	addUsers: Seq[String]
) extends UserGroupRelEvent
case class CreateUserGroupRelEvent(
	organization: String,
	group: String,
	addUsers: Seq[String]
) extends UserGroupRelEvent
case class DropUserGroupRelPreEvent(
	organization: String,
	group: String,
	removeUsers: Seq[String]) extends UserGroupRelEvent
case class DropUserGroupRelEvent(
	organization: String,
	group: String,
	removeUsers: Seq[String]) extends UserGroupRelEvent
case class DropUserGroupRelByGroupPreEvent(
	organization: String,
	group: String,
	removeUsers: Seq[String]) extends UserGroupRelEvent
case class DropUserGroupRelByGroupEvent(
	organization: String,
	group: String,
	removeUsers: Seq[String]) extends UserGroupRelEvent

case class DropUserGroupRelByUserPreEvent(
	organization: String,
	user: String,
	affectedGroups: Seq[String]) extends UserGroupRelEvent
case class DropUserGroupRelByUserEvent(
	organization: String,
	user: String,
	affectedGroups: Seq[String]) extends UserGroupRelEvent



