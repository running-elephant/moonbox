package moonbox.grid.deploy.master

object MbMasterMessages {

	case object ScheduleJob

	case object ActiveMasterChanged

	case object CheckForWorkerTimeOut

	case class BeginRecovery()

	case object CompleteRecovery
}
