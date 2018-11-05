package moonbox.grid.deploy2.audit

import java.sql.Timestamp
import java.util.UUID

import moonbox.grid.ConnectionInfo

case class AuditInfo(action: String,
					  user: String,  //
					  connection: ConnectionInfo,
					  sql: Option[String] = None,
					  detail: Option[String] = None  //
					  )

case class AuditEvent(id: Option[Long] = None,
					  action: String,
					  user: String,
					  access: String,
					  clientIp: String,
					  moonboxIp: String,
					  sql: Option[String] = None,
					  detail: Option[String] = None,
					  uid: String = UUID.randomUUID().toString,
					  time: Timestamp = new Timestamp(System.currentTimeMillis()) )

case class PreserveAudit(event: AuditEvent)
case object ScheduleAudit

/**
  *
  * case class UserContext(
	userId: Long,
	var userName: String,
	var databaseId: Long,
	var databaseName: String,
	var isLogical: Boolean,
	var organizationId: Long,
	var organizationName: String)
  *
  * */