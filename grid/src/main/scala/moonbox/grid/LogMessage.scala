package moonbox.grid

import akka.actor.Actor
import moonbox.common.MbLogging

trait LogMessage extends Actor with MbLogging {
	abstract override def receive: Receive = {
		val _receive = super.receive
		new Receive {
			override def isDefinedAt(x: Any): Boolean = {
				_receive.isDefinedAt(x)
			}

			override def apply(x: Any): Unit = {
				if (!log.isTraceEnabled) {
					_receive(x)
				} else {
					val start = System.nanoTime()

					_receive(x)

					val duration = (System.nanoTime() - start) / 1000000
					log.trace(s"Handled message $x in $duration ms from ${context.sender()}.")
				}
			}
		}
	}
}
