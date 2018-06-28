package moonbox.core.command

object PrivilegeType extends Enumeration {
	type PrivilegeType = Value
	val DDL = Value
	val DCL = Value
	val ACCOUNT = Value

	def apply(privilege: String): PrivilegeType = privilege match {
		case "DDL" => DDL
		case "DCL" => DCL
		case "ACCOUNT" => ACCOUNT
		case _ => throw new Exception(s"Unknown privilege type $privilege.")
	}
}

trait ResourcePrivilege


object SelectPrivilege {
	val NAME = "SELECT"
}
case class SelectPrivilege(columns: Seq[String]) extends ResourcePrivilege
object UpdatePrivilege {
	val NAME = "UPDATE"
}
case class UpdatePrivilege(columns: Seq[String]) extends ResourcePrivilege
case object InsertPrivilege extends ResourcePrivilege {
	val NAME = "INSERT"
}
case object DeletePrivilege extends ResourcePrivilege {
	val NAME = "DELETE"
}
case object TruncatePrivilege extends ResourcePrivilege {
	val NAME = "TRUNCATE"
}
case object AllPrivilege extends ResourcePrivilege {
	val NAMES = Seq(
		SelectPrivilege.NAME,
		UpdatePrivilege.NAME,
		InsertPrivilege.NAME,
		DeletePrivilege.NAME,
		TruncatePrivilege.NAME
	)
}
