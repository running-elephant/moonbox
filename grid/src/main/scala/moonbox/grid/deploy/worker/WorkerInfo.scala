package moonbox.grid.deploy.worker

import akka.actor.ActorRef

case class WorkerInfo(
	id: String,
	cores: Int,
	memory: Long,
	endpoint: ActorRef) {
	var coresFree: Int = _
	var memoryFree: Long = _
	var lastHeartbeat: Long = _
}
