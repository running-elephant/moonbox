/*-
 * <<
 * Moonbox
 * ==
 * Copyright (C) 2016 - 2018 EDP
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

import java.util

import moonbox.common.util.Utils
import org.scalatest.{BeforeAndAfterAll, FunSuite}

class RedisClientSuite extends FunSuite with BeforeAndAfterAll {

	var redisCache: RedisClient = _

	override protected def beforeAll(): Unit = {
		redisCache = new RedisClient("localhost:6379")
	}

	test("put") {
		redisCache.put("abc", Seq("a"))
		assert(redisCache.getRange("abc", 0, -1) == Seq(Seq("a")))

	}

	test("putRaw") {
		redisCache.putRaw(Utils.serialize[String]("abc"), Utils.serialize[Seq[String]](Seq("b")))
		val list = redisCache.getRawRange(Utils.serialize[String]("abc"), 0, -1).toList.head
		val expected = Utils.serialize[Seq[String]](Seq("b"))
		assert(util.Arrays.equals(list, expected))
	}

	/*test("cache string") {
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


	test("cache map string") {
		val email1 = "xiaoming@163.com"
		redisCache.put("user", "email", email1)
		val email2 = redisCache.get[String, String, String]("user", "email")
		assert(email1==email2)
	}

	test("cache map long") {
		val age1 = 29L
		redisCache.put("user", "age", age1)
		val age2 = redisCache.get[String, String, Long]("user", "age")
		assert(age2==age1)
	}*/

	override protected def afterAll(): Unit = {
	}
}
