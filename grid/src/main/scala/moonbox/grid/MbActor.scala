package moonbox.grid

import akka.actor.{Actor, Address, ExtendedActorSystem}
import akka.remote.{AssociatedEvent, DisassociatedEvent}
import moonbox.common.MbLogging


trait MbActor extends Actor with MbLogging {
	protected val address = getAddress
	protected val host = address.host.orNull
	protected val port = address.port.getOrElse(0)

	checkHostPort(host, port)

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

	protected def gracefullyShutdown(): Unit = {
		context.system.terminate()
		System.exit(1)
	}

	private def checkHostPort(host: String, port: Int): Unit = {
		if (host == null) {
			logError("Host is null.")
			gracefullyShutdown()
		}
		if (port == 0) {
			logError("Port is 0.")
			gracefullyShutdown()
		}
	}
}
