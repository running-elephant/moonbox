package moonbox.grid.api

import moonbox.grid.JobInfo
import org.apache.spark.sql.Row

sealed trait MbApi

case class OpenSession(username: String) extends MbApi
case class OpenedSession(sessionId: String) extends MbApi
case class OpenSessionFailed(error: String) extends MbApi

case class CloseSession(sessionId: String) extends MbApi
case object ClosedSession extends MbApi
case class CloseSessionFailed(error: String) extends MbApi

case class JobQuery(sessionId: String, sqls: Seq[String]) extends MbApi

case class JobSubmit(username: String, sqls: Seq[String], async: Boolean = true) extends MbApi

case class JobAccepted(jobId: String) extends MbApi
case class JobRejected(error: String) extends MbApi
case class JobFailed(jobId: String, error: String) extends MbApi
case class JobCompleteWithCachedData(jobId: String) extends MbApi
case class JobCompleteWithExternalData(jobId: String, message: Option[String]) extends MbApi
case class JobCompleteWithDirectData(jobId: String, data: Seq[Seq[String]]) extends MbApi

case class JobProgress(jobId: String) extends MbApi
case class JobProgressResponse(jobId: String, jobInfo: JobInfo) extends MbApi

case class JobCancel(jobId: String) extends MbApi
case class JobCancelSuccess(jobId: String) extends MbApi
case class JobCancelFailed(jobId: String, error: String) extends MbApi

case class FetchData(jobId: String, offset: Long, size: Long) extends MbApi
case class FetchDataSuccess(jobId: String, schema: String, date: Seq[Seq[Any]], size: Long) extends MbApi
case class FetchDataFailed(jobId: String, error: String) extends MbApi
