package moonbox.grid.deploy

import akka.actor.{ActorRef, Address}
import moonbox.grid.deploy.master.DriverState.DriverState


sealed trait DeployMessages extends Serializable

object DeployMessages {

	case object ElectedLeader extends DeployMessages

	case object RevokedLeadership extends DeployMessages

	case class BeginRecovery() extends DeployMessages

	case object CompleteRecovery extends DeployMessages

	case object CheckForWorkerTimeOut extends DeployMessages

	case class RegisterWorker(
		id: String,
		host: String,
		port: Int,
		worker: ActorRef,
		address: Address) extends DeployMessages {
	}

	case class MasterChanged(masterRef: ActorRef) extends DeployMessages

	case class WorkerStateResponse(id: String)

	case class WorkerLatestState(id: String) extends DeployMessages

	case class Heartbeat(workerId: String, worker: ActorRef) extends DeployMessages

	case class ReconnectWorker(masterRef: ActorRef) extends DeployMessages

	sealed trait RegisterWorkerResponse

	case class RegisteredWorker(masterAddress: ActorRef) extends DeployMessages with RegisterWorkerResponse

	case object MasterInStandby extends DeployMessages with RegisterWorkerResponse

	case class RegisterWorkerFailed(message: String) extends DeployMessages with RegisterWorkerResponse

	case object SendHeartbeat extends DeployMessages

	case class LaunchDriver(driverId: String, desc: DriverDescription) extends DeployMessages

	case class DriverStateChanged(
		driverId: String,
		state: DriverState,
		appId: Option[String],
		exception: Option[Exception])
	extends DeployMessages

	case class KillDriver(driverId: String) extends DeployMessages

	case class RegisterExecutor(
		id: String,
		host: String,
		port: Int,
		endpoint: ActorRef,
		address: Address,
		dataPort: Int
	)

	sealed trait RegisterExecutorResponse

	case class RegisteredExecutor(masterRef: ActorRef) extends RegisterExecutorResponse

	case class RegisterExecutorFailed(message: String) extends RegisterExecutorResponse

}
