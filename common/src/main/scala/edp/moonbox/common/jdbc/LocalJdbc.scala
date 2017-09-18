package edp.moonbox.common.jdbc

import edp.moonbox.common.EdpLogging
import org.h2.tools.Server

class LocalJdbc(val port: Int) extends EdpLogging {
	def this()= this(9092)

	val server = Server.createTcpServer("-tcpPort", s"$port","-tcpAllowOthers")

	def start(): Unit = {
		try {
			server.start()
			logInfo("H2 Server Start.")
		} catch {
			case e: Exception => throw new RuntimeException(e)
		}
	}
	def stop(): Unit = {
		try {
			server.stop()
			logInfo("H2 Server Stop.")
		} catch {
			case e: Exception => throw new RuntimeException(e)
		}
	}


}
