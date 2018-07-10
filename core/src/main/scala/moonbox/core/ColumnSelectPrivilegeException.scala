package moonbox.core

class ColumnSelectPrivilegeException(message: String) extends Exception(message)
class ColumnUpdatePrivilegeException(message: String) extends Exception(message)
class TableInsertPrivilegeException(message: String) extends Exception(message)
class TableDeletePrivilegeException(message: String) extends Exception(message)
class TableTruncatePrivilegeException(message: String) extends Exception(message)
