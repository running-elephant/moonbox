package moonbox.grid.deploy.audit
import moonbox.common.MbLogging
import moonbox.grid.deploy.ConnectionInfo



class BlackHoleAuditLogger extends AuditLogger with MbLogging {

	override def log(user: String, action: String, param: Map[String, String])
		(implicit connectionInfo: ConnectionInfo): Unit = {
		logInfo(s"$user | $action | $param")
	}

	override def log(user: Option[String], action: String, param: Map[String, String])
		(implicit connectionInfo: ConnectionInfo): Unit = {
		logInfo(s"${user.getOrElse("Illegal user")} | $action | $param | $connectionInfo")
	}

	override def log(user: Option[String], action: String)(implicit connectionInfo: ConnectionInfo): Unit = {
		logInfo(s"${user.getOrElse("Illegal user")} | $action | ${Map()} | $connectionInfo")
	}

	override def log(user: String, action: String)(implicit connectionInfo: ConnectionInfo): Unit = {
		logInfo(s"$user | $action | ${Map()} | $connectionInfo")
	}
}
