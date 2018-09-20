package moonbox.grid.deploy2.node

import akka.actor.ActorRef

sealed trait DeployMessage extends Serializable

object DeployMessages extends DeployMessage {

	case object ElectedLeader

	case object RevokedLeadership

	case class BeginRecovery()

	case object CompleteRecovery

	case class RegisterNode(
		id: String,
		host: String,
		port: Int,
		address: ActorRef,
		cores: Int,
		memory: Int) extends DeployMessage {
	}

	case class MasterChanged(address: ActorRef) extends DeployMessage

	sealed trait RegisterNodeResponse

	case class RegisteredNode(masterAddress: ActorRef) extends DeployMessage with RegisterNodeResponse

	case object NotMasterNode extends DeployMessage with RegisterNodeResponse

	case class RegisterNodeFailed(message: String) extends DeployMessage with RegisterNodeResponse
}
