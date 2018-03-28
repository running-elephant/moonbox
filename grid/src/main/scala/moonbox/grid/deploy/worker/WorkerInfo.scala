package moonbox.grid.deploy.worker

import akka.actor.ActorRef

case class WorkerInfo(
	id: String,
	cores: Int,
	memory: Int,
	endpoint: ActorRef) {
	var coresUsed: Int = _
	var memoryUsed: Int = _
	var lastHeartbeat: Long = _

	def coresFree(): Int = cores - coresUsed

	def memoryFree(): Int = memory - memoryUsed
}
