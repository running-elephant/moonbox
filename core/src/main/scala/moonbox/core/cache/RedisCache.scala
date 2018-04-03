package moonbox.core.cache

import moonbox.common.MbLogging
import moonbox.common.util.ParseUtils
import moonbox.common.util.Utils._
import redis.clients.jedis.Jedis
import scala.collection.JavaConverters._

class RedisCache(servers: String) extends Cache with MbLogging { self =>

	val (host, port) = ParseUtils.parseAddresses(servers).head
	val jedis = new Jedis(host, port.getOrElse(6379))

	override def put[K, F, E](key: K, field: F, value: E): Long = {
		jedis.hset(serialize(key), serialize(field), serialize(value))
	}

	override def get[K, F, E](key: K, field: F): E = {
		deserialize[E](jedis.hget(serialize(key), serialize(field)))
	}

	override def put[K, E](key: K, value: TraversableOnce[E]): Unit = {
		jedis.rpush(serialize(key), serialize[TraversableOnce[E]](value))
	}

	override def bulkPut[K, E, C <: TraversableOnce[E]](key: K, iter: TraversableOnce[C]): Unit = {
		iter.foreach(put[K, E](key, _))
	}

	override def get[K, E, C <: TraversableOnce[E]](key: K, start: Long, end: Long): TraversableOnce[C] = {
		jedis.lrange(serialize(key), start, end).asScala.map(deserialize[C])
	}

	override def getAsIterator[K, E, C <: TraversableOnce[E]](key: K, fetchSize: Int, total: Long): Iterator[C] = {
		require(fetchSize > 0, "fetch size should be great than zero.")
		new Iterator[C] {
			val theEnd: Long = math.min(total, self.size(key)) - 1
			var currentStart: Long = 0
			var currentIterator: java.util.Iterator[Array[Byte]] = getCurrentIterator

			override def hasNext: Boolean = {
				if (currentIterator.hasNext) true
				else if (currentEnd < theEnd) {
					currentStart = currentEnd + 1
					currentIterator = getCurrentIterator
					hasNext
				} else false
			}

			override def next(): C = deserialize[C](currentIterator.next())

			private def currentEnd: Long = math.min(theEnd, currentStart + fetchSize - 1 )

			private def getCurrentIterator: java.util.Iterator[Array[Byte]] = {
				jedis.lrange(serialize[K](key), currentStart, currentEnd).iterator()
			}
		}
	}

	override def size[K](key: K): Long = {
		jedis.llen(serialize[K](key))
	}

	override def close: Unit = {
		if (jedis.isConnected) {
			jedis.close()
		}
	}

}
