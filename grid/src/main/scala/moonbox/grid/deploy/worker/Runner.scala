package moonbox.grid.deploy.worker

import akka.actor.Actor
import moonbox.common.{MbConf, MbLogging}
import moonbox.core.MbSession

class Runner(conf: MbConf, session: MbSession) extends Actor with MbLogging {
	override def receive: Receive = {
		case _ =>
	}
}
