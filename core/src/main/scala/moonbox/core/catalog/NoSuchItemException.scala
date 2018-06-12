package moonbox.core.catalog

class NoSuchDatabaseException(db: String)
	extends Exception(s"Database '$db' not found")

class NoSuchDatasourceException(user: String)
	extends Exception(s"Datasource '$user' not found")

class NoSuchApplicationException(app: String)
	extends Exception(s"Application '$app' not found")

class NoSuchTimedEventException(event: String)
	extends Exception(s"Scheduler '$event' not found")

class NoSuchTableException(db: String, table: String)
	extends Exception(s"Table or view '$table' not found in database '$db'")

class NoSuchColumnException(table: String, column: String)
	extends Exception(s"Column '$column' not found in table $table")

class NoSuchTablesException(table: (String, String)*)
	extends Exception(s"Table or view '${table.map {case (t, d) => s"d.t"}.mkString(",")}'")

class NoSuchFunctionException(db: String, func: String)
	extends Exception(s"Function '$func' not found in database '$db'")

class NoSuchViewException(db: String, view: String)
	extends Exception(s"View '$view' not found in database '$db'")

class NoSuchUserException(user: String)
	extends Exception(s"User '$user' not found")

class NoSuchGroupException(group: String)
	extends Exception(s"Group '$group' not found")

class NoSuchUserGroupRelException(group: String)
	extends Exception(s"User Group Relationship '$group' not found")

class NoSuchUserTableRelException(user: String, table: String, column: String)
	extends Exception(s"Relationship of User '$user' and Column '$column' in Table '$table' not found")

class NoSuchOrganizationException(org: String)
	extends Exception(s"Organization '$org' not found")

class NoUserInGroupException(group: String)
	extends Exception(s"No user in group '$group'")

class NoSuchAvailableTableException(table: String, user: String)
	extends Exception(s"No table '$table' available for user '$user'")