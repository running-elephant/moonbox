package moonbox.grid.deploy.messages


sealed trait Message extends Serializable

object Message {

	// control
	sealed trait JobMessage extends Message

	case class OpenSession(username: String,
		database: Option[String], config: Map[String, String]) extends JobMessage

	case class OpenSessionResponse(
		sessionId: Option[String], workerHost: Option[String] = None, workerPort: Option[Int] = None, message: String) extends JobMessage

	case class CloseSession(sessionId: String) extends JobMessage

	case class CloseSessionResponse(
		sessionId: String,
		success: Boolean,
		message: String) extends JobMessage

	// for interactive
	case class JobQuery(sessionId: String, sqls: Seq[String], fetchSize: Int, maxRows: Int) extends JobMessage

	case class JobQueryResponse(
		success: Boolean,
		schema: String,
		data: Seq[Seq[Any]], // TODO
		hasNext: Boolean,
		message: String
	) extends JobMessage

	case class JobQueryNextResult(sessionId: String) extends JobMessage
	case class JobQueryNextResultResponse(
		success: Boolean,
		schema: String,
		data: Seq[Seq[Any]], // TODO
		hasNext: Boolean,
		message: String) extends JobMessage

	case class InteractiveJobCancel(sessionId: String) extends JobMessage
	case class InteractiveJobCancelResponse(success: Boolean, message: String) extends JobMessage

	// for batch
	case class JobSubmit(username: String, lang: String, sqls: Seq[String], config: Map[String, String]) extends JobMessage
	case class JobSubmitResponse(jobId: Option[String], message: String) extends JobMessage


	case class JobProgress(jobId: String) extends JobMessage
	case class JobProgressState(jobId: String, submitTime: Long, state: String, message: String) extends JobMessage

	case class BatchJobCancel(jobId: String) extends JobMessage
	case class BatchJobCancelResponse(jobId: String, success: Boolean, message: String) extends JobMessage

	// service
	sealed trait ServiceMessage extends Message

	case class SampleRequest(username: String, sql: String, database: Option[String]) extends ServiceMessage
	sealed trait SampleResponse extends ServiceMessage
	case class SampleFailed(message: String) extends SampleResponse
	case class SampleSuccessed(schema: String, data: Seq[Seq[Any]]) extends SampleResponse

	case class VerifyRequest(username: String, sqls: Seq[String]) extends ServiceMessage
	case class VerifyResponse(success: Boolean, message: Option[String] = None, result: Option[Seq[(Boolean, Option[String])]] = None) extends ServiceMessage

	case class TranslateRequest(username: String, sql: String, database: Option[String]) extends ServiceMessage
	case class TranslateResponse(success: Boolean, message: Option[String] = None, sql: Option[String] = None) extends ServiceMessage

	case class TableResourcesRequest(username: String, sql: String) extends ServiceMessage
	sealed trait TableResourcesResponse extends ServiceMessage
	case class TableResourcesFailed(message: String) extends TableResourcesResponse
	case class TableResourcesSuccessed(tables: Seq[String], functions: Seq[String]) extends TableResourcesResponse

	case class SchemaRequest(username: String, sql: String) extends ServiceMessage
	sealed trait SchemaResponse extends ServiceMessage
	case class SchemaFailed(message: String) extends SchemaResponse
	case class SchemaSuccessed(schema: String) extends SchemaResponse

	case class LineageRequest(username: String, sql: String) extends ServiceMessage
	sealed trait LineageResponse extends ServiceMessage
	case class LineageFailed(message: String) extends LineageResponse
	case class LineageSuccessed(lineage: String) extends LineageResponse

	// management
	sealed trait ManagementMessage extends Message
	case object ClusterInfoRequest extends ManagementMessage
	case class ClusterInfoResponse(cluster: Seq[Seq[String]]) extends ManagementMessage
	case object AppsInfoRequest extends ManagementMessage
	case class AppsInfoResponse(apps: Seq[Seq[String]]) extends ManagementMessage
}
