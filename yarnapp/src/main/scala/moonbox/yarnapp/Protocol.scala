package moonbox.yarnapp

import akka.actor.ActorRef

object Protocol {
	case class RegisterApp(address: ActorRef)
}
