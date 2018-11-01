package moonbox.protocol.app

import moonbox.protocol.app.JobState.JobState

sealed trait AppApi

case class RegisterAppRequest(id: String, batchJobId: Option[String], seq: Int, totalCores: Int, totalMemory: Long, freeCores: Int, freeMemory: Long )

case object RegisterAppResponse

case class StopBatchAppByPeace(jobId: String) extends AppApi

case class StartBatchAppByPeace(jobId: String, config: String) extends AppApi

case class StartedBatchAppResponse(jobId: String)

case class RemoveJobFromWorker(id: String) //id is JobID[batch] / SessionID[adhoc]

case class AssignTaskToWorker(taskInfo: TaskInfo) extends AppApi

case class AllocateSession(username: String, database: Option[String]) extends AppApi

case class FreeSession(sessionId: String) extends AppApi

sealed trait AllocateSessionResponse

case class AllocatedSession(sessionId: String) extends AllocateSessionResponse

case class AllocateSessionFailed(error: String) extends AllocateSessionResponse

sealed trait FreeSessionResponse

case class FreedSession(sessionId: String) extends FreeSessionResponse

case class FreeSessionFailed(error: String) extends FreeSessionResponse

case class FetchDataFromRunner(sessionId: String, jobId: String, fetchSize: Long)  //

sealed trait FetchDataFromRunnerResponse

case class FetchedDataFromRunner(jobId: String, schema: String, date: Seq[Seq[Any]], hasNext: Boolean) extends FetchDataFromRunnerResponse

case class FetchDataFromRunnerFailed(jobId: String, error: String) extends FetchDataFromRunnerResponse


case class RunJob(taskInfo: TaskInfo)

case class CancelJob(jobId: String)

case object KillRunner

case class JobStateChanged(jobId: String, taskSeq: Int, state: JobState, result: JobResult)

case class JobStateChangedResponse(jobId: String, taskSeq: Int)
