package moonbox.application.interactive.spark

import java.io.IOException

import akka.actor.ActorRef
import moonbox.common.{MbConf, MbLogging}
import moonbox.network.TransportContext

class SparkServer(host: String, port: Int, mbConf: MbConf, actorRef: ActorRef) extends MbLogging {

	def this(host: String, mbConf: MbConf, actorRef: ActorRef) {
		this(host, 0, mbConf, actorRef)
	}

	private val rpcHandler = new SparkServerHandler(mbConf, actorRef)
	private val transportContext = new TransportContext(rpcHandler, true)
	private val transportServer = transportContext.createServer(host, port)

	@throws[IOException]
	def start(): Int = {
		val bindPort = transportServer.start()
		logInfo(s"Sparkserver is listening on $host:$bindPort")
		bindPort
	}

	@throws[IOException]
	def stop(): Unit = {
		transportServer.close()
		logInfo("Sparkserver has been shutdown.")
	}
}
