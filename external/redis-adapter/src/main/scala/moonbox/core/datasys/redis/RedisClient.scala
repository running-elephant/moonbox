/*-
 * <<
 * Moonbox
 * ==
 * Copyright (C) 2016 - 2019 EDP
 * ==
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * >>
 */

package moonbox.core.datasys.redis

import moonbox.common.MbLogging
import moonbox.common.util.ParseUtils
import moonbox.common.util.Utils._
import redis.clients.jedis.Jedis

import scala.collection.JavaConverters._

class RedisClient(servers: String) extends MbLogging {

	val (host, port) = ParseUtils.parseAddresses(servers).head
	val jedis = new Jedis(host, port.getOrElse(6379))

	/*

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
	}*/

	def size[K](key: K): Long = {
		jedis.llen(serialize[K](key))
	}

	def close: Unit = {
		if (jedis.isConnected) {
			jedis.close()
		}
	}

	def put[K, E](key: K, value: E): Long = {
		jedis.rpush(serialize[K](key), serialize[E](value))
	}

	def pipePut[K, E](key: K, values: E*): Unit = {
		val p = jedis.pipelined()
		values.foreach { value =>
			p.rpush(serialize[K](key), serialize[E](value))
		}
		p.sync()
	}

	def putRaw(key: Array[Byte], value: Array[Byte]): Long = {
		jedis.rpush(key, value)
	}

	def getRawRange(key: Array[Byte], start: Long, end: Long): Seq[Array[Byte]] = {
		jedis.lrange(key, start, end).asScala
	}

	def getRange[K, E](key: K, start: Long, end: Long): Seq[E] = {
		jedis.lrange(serialize[K](key), start, end).asScala.map(deserialize[E])
	}

	def put[K, F, E](key: K, field: F, value: E): Long = {
		jedis.hset(serialize(key), serialize(field), serialize(value))
	}

	def putRaw(key: Array[Byte], field: Array[Byte], value: Array[Byte]): Long = {
		jedis.hset(key, field, value)
	}

	def get[K, F, E](key: K, field: F): E = {
		deserialize[E](jedis.hget(serialize(key), serialize(field)))
	}

	def getRaw(key: Array[Byte], field: Array[Byte]): Array[Byte] = {
		jedis.hget(key, field)
	}
}
