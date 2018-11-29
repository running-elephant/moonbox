package moonbox.grid

import akka.actor.{Actor, Address, ExtendedActorSystem}
import akka.remote.{AssociatedEvent, DisassociatedEvent}
import moonbox.common.MbLogging


trait MbActor extends Actor with MbLogging {
	lazy val address = getAddress

	context.system.eventStream.subscribe(self, classOf[AssociatedEvent])
	context.system.eventStream.subscribe(self, classOf[DisassociatedEvent])

	override def receive: Receive = {
		case DisassociatedEvent(localAddress, remoteAddress, inbound) =>
			if (localAddress == address) {
				onDisconnected(remoteAddress)
			}
		case AssociatedEvent(localAddress, remoteAddress, inbound) =>
			if (localAddress == address) {
				onConnected(remoteAddress)
			}
		case v => handleMessage.apply(v)
	}

	def handleMessage: Receive

	def onDisconnected(remoteAddress: Address): Unit = {}

	def onConnected(remoteAddress: Address): Unit = {}

	private def getAddress: Address = {
		context.system.asInstanceOf[ExtendedActorSystem].provider.getDefaultAddress
	}
}
