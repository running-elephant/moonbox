package moonbox.application.interactive.spark

import moonbox.common.MbLogging
import moonbox.network.TransportContext
import moonbox.network.server.TransportServer

class SparkServer(host: String, port: Int) extends MbLogging {
	private val context = new TransportContext(new SparkServerHandler(), true)
	private val server: TransportServer = context.createServer(host, port)

	def start(): Int = {
		server.start()
	}

	def stop(): Unit = {
		server.close()
	}
}
