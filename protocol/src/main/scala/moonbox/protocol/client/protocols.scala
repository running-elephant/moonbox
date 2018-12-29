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
case class RequestAccessInbound(token: Option[String] = None, isLocal: Boolean) extends Inbound
case class RequestAccessOutbound(address: Option[String] = None, error: Option[String] = None) extends Outbound

case class OpenSessionInbound(token: String, database: Option[String], isLocal: Boolean = false, extraArguments: String) extends Inbound
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

case class InteractiveNextResultInbound(token: Option[String] = None, sessionId: String) extends Inbound

case class InteractiveNextResultOutbound(
	error: Option[String] = None,
	data: Option[ResultData] = None) extends Outbound

// batch mode
// support cluster runtime engine only
// support asynchronous
case class BatchQueryInbound(token: String, sqls: Seq[String], config: String) extends Inbound
case class BatchQueryOutbound(
	jobId: Option[String] = None,
	error: Option[String] = None) extends Outbound

case class BatchQueryProgressInbound(token: String, jobId: String) extends Inbound
case class BatchQueryProgressOutbound(
	message: String,
	state: Option[String]) extends Outbound

// interactive and batch
case class CancelQueryInbound(token: String, jobId: Option[String], sessionId: Option[String]) extends Inbound
case class CancelQueryOutbound(error: Option[String] = None) extends Outbound

// job and event
case class ShowRunningEventsInbound(username: String) extends Inbound
case class ShowRunningEventsOutbound(error: Option[String] = None,
								 schema: Option[Seq[String]] = None,
								 data: Option[Seq[Seq[Any]]] = None) extends Outbound


case class ShowNodeJobsInbound(username: String) extends Inbound
case class ShowNodeJobsOutbound(error: Option[String] = None,
								 schema: Option[Seq[String]] = None,
								 data: Option[Seq[Seq[Any]]] = None) extends Outbound


case class ShowClusterJobsInbound(username: String) extends Inbound
case class ShowClusterJobsOutbound(error: Option[String] = None,
								 schema: Option[Seq[String]] = None,
								 data: Option[Seq[Seq[Any]]] = None) extends Outbound



// yarn app
case class ShowNodesInfoInbound(username: String) extends Inbound
case class ShowNodesInfoOutbound(error: Option[String] = None,
								 schema: Option[Seq[String]] = None,
								 data: Option[Seq[Seq[Any]]] = None) extends Outbound


case class AddAppInbound(username: String, config: String) extends Inbound
case class AddAppOutbound(appId: Option[String] = None,
						  error: Option[String] = None) extends Outbound

case class RemoveAppInbound(username: String, appId: String) extends Inbound
case class RemoveAppOutbound(appId: Option[String] = None,
							 error: Option[String] = None) extends Outbound

case class ShowAppInbound(username: String) extends Inbound
case class ShowAppOutbound(error: Option[String] = None,
						   schema: Option[Seq[String]] = None,
						   data: Option[Seq[Seq[Any]]] = None) extends Outbound


// meta
case class ShowDatabasesInbound(token: String) extends Inbound
case class DatabaseInfo(name: String, isLogical: Boolean, properties: Map[String, String], description: String)
case class ShowDatabasesOutbound(error: Option[String] = None,
								 databases: Option[Seq[DatabaseInfo]] = None) extends Outbound

case class ShowTablesInbound(token: String, database: String = "default") extends Inbound
case class ShowTablesOutbound(error: Option[String] = None,
							  tables: Option[Seq[String]] = None) extends Outbound

case class DescribeTablesInbound(token: String, table: String, database: String) extends Inbound
case class TableInfo(name: String, properties: Map[String, String], columns: Seq[(String, String)], description: String)
case class DescribeTablesOutbound(error: Option[String] = None,
								  table: Option[TableInfo] = None) extends Outbound
