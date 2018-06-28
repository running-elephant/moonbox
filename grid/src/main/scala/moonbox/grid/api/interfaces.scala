package moonbox.grid.api

import moonbox.grid.JobInfo

sealed trait MbApi

case class OpenSession(username: String, database: Option[String]) extends MbApi

sealed trait OpenSessionResponse
case class OpenedSession(sessionId: String) extends MbApi with OpenSessionResponse
case class OpenSessionFailed(error: String) extends MbApi with OpenSessionResponse

case class CloseSession(sessionId: String) extends MbApi
sealed trait CloseSessionResponse
case object ClosedSession extends MbApi with CloseSessionResponse
case class CloseSessionFailed(error: String) extends MbApi with CloseSessionResponse

case class JobQuery(sessionId: String, username: String, sqls: Seq[String]) extends MbApi

case class JobSubmit(username: String, sqls: Seq[String], async: Boolean = true) extends MbApi

sealed trait JobHandleResponse
case class JobAccepted(jobId: String) extends MbApi with JobHandleResponse
case class JobRejected(error: String) extends MbApi with JobHandleResponse

sealed trait JobResultResponse
case class JobFailed(jobId: String, error: String) extends MbApi with JobResultResponse
case class JobCompleteWithCachedData(jobId: String) extends MbApi with JobResultResponse
case class JobCompleteWithExternalData(jobId: String, message: Option[String]) extends MbApi with JobResultResponse
case class JobCompleteWithDirectData(jobId: String, data: Seq[Seq[String]]) extends MbApi with JobResultResponse

case class JobProgress(jobId: String) extends MbApi
sealed trait JobProgressResponse
case class JobProgressState(jobId: String, jobInfo: JobInfo) extends MbApi with JobProgressResponse

case class JobCancel(jobId: String) extends MbApi
sealed trait JobCancelResponse
case class JobCancelSuccess(jobId: String) extends MbApi with JobCancelResponse
case class JobCancelFailed(jobId: String, error: String) extends MbApi with JobCancelResponse

case class FetchData(jobId: String, offset: Long, size: Long) extends MbApi
sealed trait FetchDataResponse
case class FetchDataSuccess(jobId: String, schema: String, date: Seq[Seq[Any]], size: Long) extends MbApi with FetchDataResponse
case class FetchDataFailed(jobId: String, error: String) extends MbApi with FetchDataResponse
