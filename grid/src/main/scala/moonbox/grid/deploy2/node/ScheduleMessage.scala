package moonbox.grid.deploy2.node

import moonbox.core.command.MbCommand
import moonbox.grid.runtime.cluster.AppResourceInfo
import moonbox.grid.timer.EventEntity

sealed trait ScheduleMessage extends Serializable

object ScheduleMessage {

    case object ScheduleJob extends ScheduleMessage

    case class ReportYarnAppResource(adhocInfo: AppResourceInfo, runningJobs: Int) extends ScheduleMessage

    case class ReportNodesResource(id: String, adhocInfo: AppResourceInfo, runningJobs: Int) extends ScheduleMessage

    case class CommandInfo(jobId: String,
                           sessionId: Option[String] = None,
                           command: MbCommand,
                           seq: Int,
                           username: Option[String] = None
                          )


    case class AssignCommandToWorker(commandInfo: CommandInfo) extends ScheduleMessage
    case class RunCommand(cmdInfo: CommandInfo) extends ScheduleMessage

    case class RegisterTimedEvent(event: EventEntity) extends ScheduleMessage
    sealed trait RegisterTimedEventResponse
    case object RegisteredTimedEvent extends ScheduleMessage with RegisterTimedEventResponse
    case class RegisterTimedEventFailed(message: String) extends ScheduleMessage with RegisterTimedEventResponse

    case class UnregisterTimedEvent(group: String, name: String) extends ScheduleMessage
    sealed trait UnregisterTimedEventResponse
    case object UnregisteredTimedEvent extends ScheduleMessage with UnregisterTimedEventResponse
    case class UnregisterTimedEventFailed(message: String) extends ScheduleMessage with UnregisterTimedEventResponse

}
