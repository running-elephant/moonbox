package moonbox.core

sealed trait IdentifiersWithDatabase {
	def database: Option[String]
}

case class MbTableIdentifier(table: String, database: Option[String])
	extends IdentifiersWithDatabase {
	def this(table: String) = this(table, None)
}

object MbTableIdentifier {
	def apply(tableName: String): MbTableIdentifier = new MbTableIdentifier(tableName)
}

case class MbFunctionIdentifier(func: String, database: Option[String])
	extends IdentifiersWithDatabase {
	def this(func: String) = this(func, None)
}

object MbFunctionIdentifier {
	def apply(func: String): MbFunctionIdentifier = new MbFunctionIdentifier(func)
}

case class MbColumnIdentifier(
	column: String,
	table: String,
	database: Option[String]) extends IdentifiersWithDatabase {
	def this(column: String, table: String) = this(column, table, None)
}
