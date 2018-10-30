package moonbox.grid.deploy2.node

import akka.actor.ActorRef
import moonbox.core.command.MbCommand
import moonbox.grid.runtime.cluster.ClusterMessage.YarnAppInfo
import moonbox.grid.timer.EventEntity

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
		memory: Long,
		jdbcPort: Int,
		restPort: Int) extends DeployMessage {
	}

	case class MasterChanged(address: ActorRef) extends DeployMessage

	sealed trait RegisterNodeResponse

	case class RegisteredNode(masterAddress: ActorRef) extends DeployMessage with RegisterNodeResponse

	case object NotMasterNode extends DeployMessage with RegisterNodeResponse

	case class RegisterNodeFailed(message: String) extends DeployMessage with RegisterNodeResponse

	case object ScheduleJob

	case class ReportNodesResource(id: String, adhocInfo: YarnAppInfo, runningJobs: Int)

	case class CommandInfo(jobId: String,
						   sessionId: Option[String] = None,
						   command: MbCommand,
						   seq: Int,
						   username: Option[String] = None
					   )


	case class AssignCommandToWorker(commandInfo: CommandInfo)

	case class RunCommand(cmdInfo: CommandInfo)

	case class RegisterTimedEvent(event: EventEntity) extends DeployMessage

	sealed trait RegisterTimedEventResponse

	case object RegisteredTimedEvent extends DeployMessage with RegisterTimedEventResponse

	case class RegisterTimedEventFailed(message: String) extends DeployMessage with RegisterTimedEventResponse

	case class UnregisterTimedEvent(group: String, name: String) extends DeployMessage

	sealed trait UnregisterTimedEventResponse

	case object UnregisteredTimedEvent extends DeployMessage with UnregisterTimedEventResponse

	case class UnregisterTimedEventFailed(message: String) extends DeployMessage with UnregisterTimedEventResponse

}
