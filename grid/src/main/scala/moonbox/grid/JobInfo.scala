package moonbox.grid

import akka.actor.ActorRef
import moonbox.core.command.MbCommand
import moonbox.grid.JobState.JobState

case class JobInfo(
	jobId: String,
	sessionId: Option[String] = None,
	cmds: Seq[MbCommand],
	var status: JobState,
	var errorMessage: Option[String],
	username: Option[String] = None,
	submitTime: Long,
	var updateTime: Long,
	client: ActorRef
)