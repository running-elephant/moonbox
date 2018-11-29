package moonbox.grid.deploy.cluster

import akka.actor.{ActorRef, Address}
import moonbox.grid.deploy.cluster.master.DriverState.DriverState


sealed trait ClusterDeployMessages extends Serializable

object ClusterDeployMessages {

	case object ElectedLeader extends ClusterDeployMessages

	case object RevokedLeadership extends ClusterDeployMessages

	case class BeginRecovery() extends ClusterDeployMessages

	case object CompleteRecovery extends ClusterDeployMessages

	case object CheckForWorkerTimeOut extends ClusterDeployMessages

	case class RegisterWorker(
		id: String,
		host: String,
		port: Int,
		worker: ActorRef,
		address: Address,
		internalPort: Int) extends ClusterDeployMessages {
	}

	case class MasterChanged(masterRef: ActorRef) extends ClusterDeployMessages

	case class WorkerStateResponse(id: String)

	case class WorkerLatestState(id: String) extends ClusterDeployMessages

	case class Heartbeat(workerId: String, worker: ActorRef) extends ClusterDeployMessages

	case class ReconnectWorker(masterRef: ActorRef) extends ClusterDeployMessages

	sealed trait RegisterWorkerResponse

	case class RegisteredWorker(masterAddress: ActorRef) extends ClusterDeployMessages with RegisterWorkerResponse

	case object MasterInStandby extends ClusterDeployMessages with RegisterWorkerResponse

	case class RegisterWorkerFailed(message: String) extends ClusterDeployMessages with RegisterWorkerResponse

	case object SendHeartbeat extends ClusterDeployMessages

	case class LaunchDriver(driverId: String, desc: DriverDescription) extends ClusterDeployMessages

	case class DriverStateChanged(
		driverId: String,
		state: DriverState,
		exception: Option[Exception])
	extends ClusterDeployMessages

	case class KillDriver(driverId: String) extends ClusterDeployMessages
}
