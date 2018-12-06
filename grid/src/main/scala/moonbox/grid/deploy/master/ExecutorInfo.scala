package moonbox.grid.deploy.master

import akka.actor.ActorRef
import moonbox.protocol.app.ExecutorDescription

private[deploy] class ExecutorInfo(
	val startTime: Long,
	val id: String,
	val desc: ExecutorDescription,
	val driver: ActorRef
) extends Serializable {

}
