package moonbox.grid.deploy2.node

import akka.actor.ActorRef

sealed trait DeployMessage extends Serializable

object DeployMessages {

	case object ElectedLeader extends DeployMessage

	case object RevokedLeadership extends DeployMessage

	case class BeginRecovery() extends DeployMessage

	case object CompleteRecovery extends DeployMessage

	case class RegisterNode(
		id: String,
		host: String,
		port: Int,
		address: ActorRef,
		cores: Int,
		memory: Long,
		jdbcPort: Int,
		restPort: Int) extends DeployMessage {
	}

	case class MasterChanged(address: ActorRef) extends DeployMessage

	sealed trait RegisterNodeResponse

	case class RegisteredNode(masterAddress: ActorRef) extends DeployMessage with RegisterNodeResponse

	case object NotMasterNode extends DeployMessage with RegisterNodeResponse

	case class RegisterNodeFailed(message: String) extends DeployMessage with RegisterNodeResponse

}
