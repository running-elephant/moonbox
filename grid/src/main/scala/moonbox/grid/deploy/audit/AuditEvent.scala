package moonbox.grid.deploy.audit

import java.util.Date

import moonbox.grid.deploy.ConnectionInfo

case class AuditEvent(
	username: String,
	action: String,
	parameters: Map[String, String] = Map.empty,
	connectionInfo: ConnectionInfo,
	time: Date = new Date()
)
