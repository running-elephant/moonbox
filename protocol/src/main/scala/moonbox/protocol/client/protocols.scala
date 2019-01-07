package moonbox.protocol.client


trait Message {
	private var messageId: Long = 0

	def setId(id: Long): this.type = {
		this.messageId = id
		this
	}

	def getId: Long = this.messageId
}

trait Inbound extends Message
trait Outbound extends Message

case class LoginInbound(username: String, password: String) extends Inbound
case class LoginOutbound(token: Option[String] = None, error: Option[String] = None) extends Outbound

case class LogoutInbound(token: String) extends Inbound
case class LogoutOutbound(error: Option[String]) extends Outbound

case class OpenSessionInbound(token: String, database: Option[String], config: Map[String, String]) extends Inbound
case class OpenSessionOutbound(sessionId: Option[String] = None, workerHost: Option[String] = None, workerPort: Option[Int] = None, error: Option[String] = None) extends Outbound

case class CloseSessionInbound(token: String, sessionId: String) extends Inbound
case class CloseSessionOutbound(error: Option[String]) extends Outbound

case class ResultData(cursor: String, schema: String, data: Seq[Seq[Any]], hasNext: Boolean)

case class InteractiveQueryInbound(
	token: String,
	sessionId: String,
	sqls: Seq[String],
	fetchSize: Int = 1000,
	maxRows: Int = 10000) extends Inbound
case class InteractiveQueryOutbound(
	error: Option[String] = None,
	data: Option[ResultData] = None) extends Outbound

case class InteractiveNextResultInbound(token: String, sessionId: String) extends Inbound

case class InteractiveNextResultOutbound(
	error: Option[String] = None,
	data: Option[ResultData] = None) extends Outbound

// batch mode
// support cluster runtime engine only
// support asynchronous
case class BatchQueryInbound(username: String, password: String, sqls: Seq[String], config: Map[String, String]) extends Inbound
case class BatchQueryOutbound(
	jobId: Option[String] = None,
	error: Option[String] = None) extends Outbound

case class BatchQueryProgressInbound(username: String, password: String, jobId: String) extends Inbound
case class BatchQueryProgressOutbound(
	message: String,
	state: Option[String]) extends Outbound

// interactive and batch
case class BatchQueryCancelInbound(username: String, password: String, jobId: String) extends Inbound
case class InteractiveQueryCancelInbound(token: String, sessionId: String) extends Inbound
case class CancelQueryOutbound(error: Option[String] = None) extends Outbound
