package moonbox.core.cache

import moonbox.localservice.LocalRedis
import org.scalatest.{BeforeAndAfterAll, FunSuite}

class RedisCacheSuite extends FunSuite with BeforeAndAfterAll {

	var redisCache: Cache = _
	var localServer: LocalRedis = _

	override protected def beforeAll(): Unit = {
		localServer = new LocalRedis()
		localServer.start()
		redisCache = new RedisCache("localhost:6379")
	}

	test("cache string") {
		redisCache.put("abc", Seq("a", "b", "c"))
		redisCache.put("abc", Seq("d", "e", "f"))
		redisCache.bulkPut[String, String, Seq[String]]("abc", Seq(Seq("g", "h", "i"), Seq("j", "k", "l")))
		val list = redisCache.get[String, String, Seq[String]]("abc", 0L, -1L)
		val iter = redisCache.getAsIterator[String, String, Seq[String]]("abc", 2)
		assert(redisCache.size("abc") == 4)
		assert(list == Seq(Seq("a", "b", "c"), Seq("d", "e", "f"), Seq("g", "h", "i"), Seq("j", "k", "l")))
		assert(iter.toList == list)
	}

	test("cache any") {
		redisCache.put(111, Seq[Any](1, "1", 1L, Some(1)))
		redisCache.bulkPut[Int, Any, Seq[Any]](111, Seq(Seq(1, "1", 1L, Some(1))))
		val list = redisCache.get[Int, Any, Seq[Any]](111, 0L, -1L)
		val iter = redisCache.getAsIterator[Int, Any, Seq[Any]](111, 10)
		assert(redisCache.size(111) == 2)
		assert(list == Seq(Seq(1, "1", 1L, Some(1)), Seq(1, "1", 1L, Some(1))))
		assert(iter.toList == list)
	}

	override protected def afterAll(): Unit = {
		localServer.stop()
	}
}
