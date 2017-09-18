package edp.moonbox.common.redis

import org.scalatest.FunSuite

class LocalRedisSuite extends FunSuite {

	test("Local Redis Server Fix Port") {
		val redis: LocalRedis = new LocalRedis(6379)
		redis.start()
		assert(redis.isRunning)
		assert(redis.port == 6379)
		redis.stop()
		assert(redis.isStop)
	}

	test("Local Redis Server Random Port") {
		val redis: LocalRedis = new LocalRedis
		redis.start()
		assert(redis.isRunning)
		assert(redis.port != 0)
		redis.stop()
		assert(redis.isStop)
	}
}
