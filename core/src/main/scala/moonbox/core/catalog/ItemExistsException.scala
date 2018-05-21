package moonbox.core.catalog

class ExistsException(msg: String) extends Exception(msg)


class GroupExistsException(group: String)
	extends ExistsException(s"Group $group already exists")

class TableExistsException(db: String, table: String)
	extends ExistsException(s"Table $table already exists in database $db")

class FunctionExistsException(db: String, func: String)
	extends ExistsException(s"Function $func already exists in database $db")

class ViewExistsException(db: String, view: String)
	extends ExistsException(s"Function $view already exists in database $db")

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

class ApplicationExistsException(app: String)
	extends ExistsException(s"Application $app already exists")

class SchedulerExistsException(scheduler: String)
	extends ExistsException(s"Event $scheduler already exists")

