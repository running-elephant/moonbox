package moonbox.grid.scheduler

import moonbox.common.util.ListenerEvent
import moonbox.grid.JobInfo

case class JobSubmitted(jobInfo: JobInfo) extends ListenerEvent

case class JobAccepted(jobInfo: JobInfo) extends ListenerEvent

case class JobReject(jobInfo: JobInfo) extends ListenerEvent

case class JobEnd(jobInfo: JobInfo) extends ListenerEvent

case class JobStart(jobInfo: JobInfo) extends ListenerEvent

case class JobGettingResult(jobInfo: JobInfo) extends ListenerEvent



