package moonbox.localservice

import moonbox.common.MbLogging
import redis.embedded.RedisServer

object LocalRedis {
	val DEFAULT_REDIS_PORT: Int = 6379
}
class LocalRedis(val port: Int) extends MbLogging {

	def this() = this(LocalRedis.DEFAULT_REDIS_PORT)

	private val redis = new RedisServer(port)

	def start(): Unit = {
		try {
			redis.start()
			logInfo("Redis server is listening on port: " + port)
		} catch {
			case e: Exception =>
				throw new RuntimeException(e)
		}
	}

	def stop(): Unit = {
		if (redis != null) {
			if (redis.isActive) {
				redis.stop()
				logInfo("Redis server is stop.")
			} else {
				logWarning("Redis server is not active.")
			}
		} else {
			logWarning("Redis server is not initialized.")
		}
	}

	def isRunning: Boolean = redis.isActive

	def isStop: Boolean = !isRunning
}
