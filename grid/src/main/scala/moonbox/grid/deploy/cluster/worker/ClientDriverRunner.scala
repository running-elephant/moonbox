package moonbox.grid.deploy.cluster.worker

import akka.actor.ActorRef
import moonbox.common.{MbConf, MbLogging}

private[deploy] class ClientDriverRunner(
	conf: MbConf,
	val id: String,
	val worker: ActorRef) extends MbLogging {

}
