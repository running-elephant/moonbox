package moonbox.grid.deploy.rest.service

import java.util.concurrent.TimeUnit

import akka.actor.ActorRef
import akka.pattern.ask
import akka.util.Timeout
import moonbox.common.MbLogging
import moonbox.grid.deploy.DeployMessages.{ClusterStateResponse, RequestClusterState}
import moonbox.grid.deploy.rest.entities.Node
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class GridService(actorRef: ActorRef) extends MbLogging {
	private implicit val timeout = new Timeout(30, TimeUnit.SECONDS)
	def clusterState(): Future[Seq[Node]] = {
		actorRef.ask(RequestClusterState).mapTo[ClusterStateResponse].map(_.nodes)
	}
}
