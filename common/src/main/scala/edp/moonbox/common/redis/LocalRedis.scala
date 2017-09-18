package edp.moonbox.common.redis

import java.net.ServerSocket

import edp.moonbox.common.EdpLogging
import redis.embedded.RedisServer

class LocalRedis(var port: Int) extends EdpLogging {

	def this() = this(0)

	private val redis: RedisServer = init()

	private def init() = {
		if (port == 0) {
			val serverSocket = new ServerSocket(0)
			port = serverSocket.getLocalPort
			serverSocket.close()
		}
		new RedisServer(port)
	}

	def start(): Unit = {
		try {
			redis.start()
			logInfo("Redis Server Start.")
		} catch {
			case e: Exception => throw new RuntimeException(e)
		}
	}

	def stop(): Unit = {
		if (redis != null) {
			redis.stop()
			logInfo("Redis Server Stop")
		} else {
			logWarning("Redis Server Is Not Running")
		}
	}

	def isStop: Boolean = !isRunning

	def isRunning: Boolean = {
		redis.isActive
	}
}
