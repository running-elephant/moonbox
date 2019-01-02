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
	case class JobSubmit(username: String, sqls: Seq[String], config: String) extends JobMessage
	case class JobSubmitResponse(jobId: Option[String], message: String) extends JobMessage


	case class JobProgress(jobId: String) extends JobMessage
	case class JobProgressState(jobId: String, submitTime: Long, state: String, message: String) extends JobMessage

	case class BatchJobCancel(jobId: String) extends JobMessage
	case class BatchJobCancelResponse(jobId: String, success: Boolean, message: String) extends JobMessage

	// service
	sealed trait ServiceMessage

	// management
	sealed trait ManagementMessage
}
