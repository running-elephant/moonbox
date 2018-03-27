package moonbox.grid.api

import moonbox.grid.JobInfo

case class MbRequest(username: String, request: MbApi)
case class MbResponse(username: String, request: MbApi)


sealed trait MbApi

case class JobQuery(sqls: Seq[String]) extends MbApi

case class JobSubmit(sqls: Seq[String], async: Boolean = true) extends MbApi
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
case class FetchDataSuccess(jobId: String, schema: Seq[String], date: Seq[Seq[String]]) extends MbApi
case class FetchDataFailed(jobId: String, error: String) extends MbApi
