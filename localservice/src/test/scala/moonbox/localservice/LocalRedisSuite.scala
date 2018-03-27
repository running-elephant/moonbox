package moonbox.localservice

import org.scalatest.{BeforeAndAfterAll, FunSuite}

class LocalRedisSuite extends FunSuite with BeforeAndAfterAll {
	var redisServer: LocalRedis = _

	override protected def beforeAll(): Unit = {
		redisServer = new LocalRedis()
	}

	override protected def afterAll(): Unit = {
		if (redisServer.isRunning) redisServer.stop()
	}

	test("start server") {
		redisServer.start()
		assert(redisServer.isRunning)
	}

	test("stop server") {
		redisServer.stop()
		assert(redisServer.isStop)
	}

}
