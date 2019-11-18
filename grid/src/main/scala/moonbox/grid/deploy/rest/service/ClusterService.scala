package moonbox.grid.deploy.rest.service

import akka.actor.ActorRef
import moonbox.common.MbLogging
import moonbox.grid.deploy.rest.entities.Node

class ClusterService(actorRef: ActorRef) extends MbLogging {
	def clusterInfo(): Seq[Node] = {
		Seq(Node(address = "localhost", nodeType = "Master", state = "running", startTime = "2019-10-24 12:23:00", updateTime = "2019-10-24 12:24:00"))
	}
}
