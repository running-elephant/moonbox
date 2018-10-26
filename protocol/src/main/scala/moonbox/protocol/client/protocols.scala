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


// service

// compute

// interactive mode
// support local and cluster runtime engine
// support synchronize mode only
case class RequestAccessInbound() extends Inbound
case class RequestAccessOutbound(address: Option[String] = None, error: Option[String] = None) extends Outbound

case class OpenSessionInbound(token: String, database: Option[String], isLocal: Boolean) extends Inbound
case class OpenSessionOutbound(sessionId: Option[String] = None, error: Option[String] = None) extends Outbound

case class CloseSessionInbound(token: String, sessionId: String) extends Inbound
case class CloseSessionOutbound(error: Option[String]) extends Outbound

case class ResultData(cursor: String, schema: String, data: Seq[Seq[Any]], hasNext: Boolean)

case class InteractiveQueryInbound(
	token: String,
	sessionId: String,
	sqls: Seq[String],
	fetchSize: Int = 1000,
	maxRows: Long = 1000) extends Inbound
case class InteractiveQueryOutbound(
	error: Option[String] = None,
	hasResult: Boolean = false,
	data: Option[ResultData] = None) extends Outbound

case class InteractiveNextResultInbound(
	token: String,
	sessionId: String,
	cursor: String,
	fetchSize: Long
) extends Inbound

case class InteractiveNextResultOutbound(
	error: Option[String] = None,
	data: Option[ResultData] = None) extends Outbound

// batch mode
// support cluster runtime engine only
// support asynchronous
case class BatchQueryInbound(token: String, sqls: Seq[String], config: Seq[String]) extends Inbound
case class BatchQueryOutbound(
	jobId: Option[String] = None,
	error: Option[String] = None) extends Outbound

case class BatchQueryProgressInbound(token: String, jobId: String) extends Inbound
case class BatchQueryProgressOutbound(
	error: Option[String] = None,
	state: Option[String] = None) extends Outbound

// interactive and batch
case class CancelQueryInbound(token: String, jobId: String) extends Inbound
case class CancelQueryOutbound(error: Option[String] = None) extends Outbound