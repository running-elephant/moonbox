package moonbox.grid.deploy

import akka.actor.ActorRef
import moonbox.common.util.Utils
import moonbox.grid.{JobInfo, JobResult}
import moonbox.grid.JobState.JobState
import moonbox.grid.deploy.worker.WorkerInfo

sealed trait DeployMessage extends Serializable

object DeployMessages {

	// Worker to Master
	case class RegisterWorker(
		id: String,
		worker: ActorRef,
		cores: Int,
		memory: Int) extends DeployMessage {
	}

	case class JobStateChanged(
		jobId: String,
		state: JobState,
		result: JobResult) extends DeployMessage

	case class WorkerLatestState(workerInfo: WorkerInfo) extends DeployMessage

	case class Heartbeat(workId: String, worker: ActorRef) extends DeployMessage

	sealed trait AssignJobToWorkerResponse

	case class AssignedJob(jobId: String) extends DeployMessage with AssignJobToWorkerResponse

	case class AssignJobFailed(jobId: String, message: String) extends DeployMessage with AssignJobToWorkerResponse


	sealed trait AllocateSessionResponse
	case class AllocatedSession(sessionId: String) extends DeployMessage with AllocateSessionResponse
	case class AllocateSessionFailed(error: String) extends DeployMessage with AllocateSessionResponse

	sealed trait FreeSessionResponse
	case class FreedSession(sessionId: String) extends DeployMessage with FreeSessionResponse
	case class FreeSessionFailed(error: String) extends DeployMessage with FreeSessionResponse

	// Master to Worker
	sealed trait RegisterWorkerResponse

	case class RegisteredWorker(
		master: ActorRef) extends DeployMessage with RegisterWorkerResponse

	case class RegisterWorkerFailed(message: String) extends DeployMessage with RegisterWorkerResponse

	case class AllocateSession(username: String) extends DeployMessage
	case class FreeSession(sessionId: String) extends DeployMessage

	case class AssignJobToWorker(jobInfo: JobInfo) extends DeployMessage

	case class RemoveJobFromWorker(jobId: String) extends DeployMessage

	// Worker internal


	// Master to Worker & Client
	//case class MasterChanged(master: ActorRef, masterWebUiUrl: String) extends DeployMessage
	case object MasterChanged extends DeployMessage

}