package moonbox.grid

import akka.actor.ActorRef
import moonbox.core.command.MbCommand
import moonbox.grid.JobState.JobState

case class JobInfo(
	jobId: String,
	cmds: Seq[MbCommand],
	var status: JobState,
	var errorMessage: Option[String],
	user: String,
	submitTime: Long,
	var updateTime: Long,
	client: ActorRef
)