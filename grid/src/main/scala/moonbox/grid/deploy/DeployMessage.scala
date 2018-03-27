package moonbox.grid.deploy

import akka.actor.ActorRef
import moonbox.common.util.Utils
import moonbox.grid.JobInfo
import moonbox.grid.JobState.JobState

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
		message: Option[String]) extends DeployMessage

	case class WorkerLatestState(id: String) extends DeployMessage

	case class Heartbeat(workId: String, worker: ActorRef) extends DeployMessage

	sealed trait AssignJobToWorkerResponse

	case class AssignedJob(jobId: String) extends DeployMessage with AssignJobToWorkerResponse

	case class AssignJobFailed(jobId: String, message: String) extends DeployMessage with AssignJobToWorkerResponse

	// Master to Worker
	sealed trait RegisterWorkerResponse

	case class RegisteredWorker(
		master: ActorRef) extends DeployMessage with RegisterWorkerResponse

	case class RegisterWorkerFailed(message: String) extends DeployMessage with RegisterWorkerResponse

	case class AssignJobToWorker(jobInfo: JobInfo) extends DeployMessage

	case class RemoveJobFromWorker(jobId: String) extends DeployMessage

	// Worker internal


	// Master to Worker & Client
	case class MasterChanged(master: ActorRef, masterWebUiUrl: String)

}