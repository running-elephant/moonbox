package moonbox.core.command


sealed trait PrivilegeType
object GrantDdl extends PrivilegeType
object GrantDmlOn extends PrivilegeType
object GrantAccount extends PrivilegeType

