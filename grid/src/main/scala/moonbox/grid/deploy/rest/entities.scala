package moonbox.grid.deploy.rest

sealed trait RestEntity

trait Inbound extends RestEntity
trait Outbound extends RestEntity

case class LoginInbound(username: String, password: String) extends Inbound
case class LoginOutbound(token: Option[String] = None, error: Option[String] = None) extends Outbound

case class LogoutInbound(token: String) extends Inbound
case class LogoutOutbound(message: Option[String] = None, error: Option[String] = None) extends Outbound

case class SubmitInbound(token: String, sqls: Seq[String], mode: String) extends Inbound
case class SubmitOutbound(jobId: Option[String] = None,
	message: Option[String] = None,
	error: Option[String] = None,
	schema: Option[String] = None, data: Option[Seq[Seq[Any]]] = None) extends Outbound

case class ProgressInbound(token: String, jobId: String) extends Inbound
case class ProgressOutbound(jobId: String, error: Option[String] = None, status: Option[String] = None) extends Outbound

case class ResultInbound(token: String, jobId: String, offset: Long, size: Long) extends Inbound
case class ResultOutbound(jobId: String, error: Option[String] = None, schema: Option[String] = None, data: Option[Seq[Seq[Any]]] = None) extends Outbound

case class CancelInbound(token: String, jobId: String) extends Inbound
case class CancelOutbound(jobId: String, error: Option[String] = None) extends Outbound

case class QueryInbound(sessionId: String, token: String, sqls: Seq[String]) extends Inbound
case class QueryOutbound(jobId: String, error: Option[String] = None, schema: Option[String] = None, data: Option[Seq[Seq[Any]]] = None, size: Option[Long] = None) extends Outbound

case class OpenSessionInbound(token: String) extends Inbound
case class OpenSessionOutbound(sessionId: Option[String], error: Option[String]) extends Outbound

case class CloseSessionInbound(token: String, sessionId: String) extends Inbound
case class CloseSessionOutbound(error: Option[String]) extends Outbound
