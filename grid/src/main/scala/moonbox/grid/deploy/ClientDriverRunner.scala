package moonbox.grid.deploy

import akka.actor.ActorRef
import moonbox.common.{MbConf, MbLogging}

private[deploy] class ClientDriverRunner(
	conf: MbConf,
	val id: String,
	val desc: ClientDriverDescription,
	val worker: ActorRef) extends DriverRunner with MbLogging {

	override def start(): Unit = {

	}

	override def kill(): Unit = {

	}

}
