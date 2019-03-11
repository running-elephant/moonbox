package moonbox.grid.deploy

trait Interface

object Interface extends Interface {
	sealed trait Inbound extends Interface
	sealed trait Outbound extends Interface

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
	case class BatchQueryInbound(username: String, password: String, lang: String, sqls: Seq[String], config: Map[String, String]) extends Inbound
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


	// service
	case class SampleInbound(username: String, password: String, sql: String, database: Option[String]) extends Inbound
	case class SampleOutbound(success: Boolean, schema: Option[String] = None, data: Option[Seq[Seq[Any]]] = None, message: Option[String] = None) extends Outbound

	case class VerifyInbound(username: String, password: String, sqls: Seq[String]) extends Inbound
	case class VerifyOutbound(success: Boolean, message: Option[String] = None, result: Option[Seq[VerifyResult]] = None) extends Outbound
	case class VerifyResult(success: Boolean, message: Option[String])

	case class SchemaInbound(username: String, password: String, sql: String) extends Inbound
	case class SchemaOutbound(success: Boolean, schema: Option[String] = None, message: Option[String] = None) extends Outbound

	case class TableResourceInbound(username: String, password: String, sql: String) extends Inbound
	case class TableResourceOutbound(success: Boolean, tables: Option[Seq[String]] = None, functions: Option[Seq[String]] = None, message: Option[String] = None) extends Outbound

	case class LineageInbound(username: String, password: String, sql: String) extends Outbound
	case class LineageOutbound(success: Boolean, lineage: Option[String] = None, message: Option[String] = None) extends Outbound

	// management
	case object ClusterInfoInbound extends Inbound
	case class ClusterInfoOutbound(cluster: Seq[Seq[String]]) extends Outbound

	case object AppsInfoInbound extends Inbound
	case class AppsInfoOutbound(apps: Seq[Seq[String]]) extends Outbound
}


