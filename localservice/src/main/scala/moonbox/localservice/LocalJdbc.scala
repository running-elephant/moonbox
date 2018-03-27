package moonbox.localservice

import moonbox.common.MbLogging
import org.h2.tools.Server


object LocalJdbc {
	val DEFAULT_JDBC_PORT: Int = 9092
}
class LocalJdbc(val port: Int) extends MbLogging {
	def this() = this(LocalJdbc.DEFAULT_JDBC_PORT)

	private val jdbcServer = Server.createTcpServer("-tcpPort", s"$port","-tcpAllowOthers")

	def start(): Unit = {
		try {
			jdbcServer.start()
			logInfo("Jdbc server is listening on port: " + port)
		} catch {
			case e: Exception => throw new RuntimeException(e)
		}
	}

	def stop(): Unit = {
		if (jdbcServer != null) {
			if (jdbcServer.isRunning(false)) {
				jdbcServer.stop()
				logInfo("Jdbc server is stop.")
			} else {
				logWarning("Jdbc server is not active.")
			}
		} else {
			logWarning("Jdbc server is not initialized.")
		}
	}

	def isRunning: Boolean = jdbcServer.isRunning(false)

	def isStop: Boolean = !isRunning


}
