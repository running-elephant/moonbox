/*-
 * <<
 * Moonbox
 * ==
 * Copyright (C) 2016 - 2017 EDP
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

package edp.moonbox.common

import java.util
import java.util.Map.Entry

import redis.clients.jedis.{Jedis, ScanParams}

import scala.annotation.tailrec
import scala.collection.JavaConverters._


class RedisCacheClient(connect: Seq[(String, Int)]) extends CacheClient[(String, String)] {

  lazy val (host, port) = connect.find(hostPort => Util.getIpByName(hostPort._1).equals(Util.getLocalHostName)).getOrElse(connect.head)

  private val jedis = new Jedis(host, port)

  override def mapKeyValues(key: String): Iterator[(String, String)] = mapKeyValues(key, DEFAULT_FETCH_SIZE)

  override def mapKeyValues(key: String, fetchSize: Int): Iterator[(String, String)] = new Iterator[(String, String)] {

    private var currentCursor: String = _
    private var currentIterator: util.Iterator[Entry[String, String]] = _

    update(key, ScanParams.SCAN_POINTER_START)

    private def update(key: String, cursor: String, scanParams: ScanParams = new ScanParams().count(fetchSize)): Unit = {
      val hscan = jedis.hscan(key, cursor, scanParams)
      currentCursor = hscan.getStringCursor
      currentIterator = hscan.getResult.iterator()
    }

    @tailrec
    override def hasNext: Boolean = {
      if (currentIterator.hasNext) true
      else if (!currentCursor.equals(ScanParams.SCAN_POINTER_START)) {
        update(key, currentCursor)
        hasNext
      } else false
    }

    override def next(): (String, String) = {
      val entry: Entry[String, String] = currentIterator.next()
      (entry.getKey, entry.getValue)
    }
  }

  //If the field is not found or the key does not exist, a special 'nil' value is returned.
  override def mapValue(key: String, field: String): String = {
    jedis.hget(key, field)
  }

  override def mapKeyExists(key: String, field: String): Boolean = {
    jedis.hexists(key, field)
  }

  override def AllKeys(key: String): scala.collection.mutable.Set[String] = {
    jedis.keys(key).asScala
  }

  override def mapKeys(key: String): scala.collection.mutable.Set[String] = {
    jedis.hkeys(key).asScala
  }

  override def mapPutKeyValue(key: String, field: String, value: String): Long = {
    jedis.hset(key, field, value)
  }

  override def mapPutKeyValues(key: String, map: Map[String, String]): Unit = {
    import scala.collection.JavaConverters._
    jedis.hmset(key, map.asJava)
  }

  override def mapDeleteValue(key: String, field: String): Unit = {
    jedis.hdel(key, field)
  }

  override def mapDeleteValues(key: String, fieldSet: Seq[String]): Unit = {
    jedis.hdel(key, fieldSet:_*)
  }

  override def close: Unit = {
    if (jedis.isConnected) jedis.close()
  }

  override def listAdd(key: String, values: String*): Long = {
    jedis.lpush(key, values:_*)
  }

  override def listValues(key: String, start: Long, end: Long): List[String] = {
    jedis.lrange(key, start, end).asScala.toList
  }

  override def listValue(key: String, position: Long): String = {
    jedis.lindex(key, position)
  }

  override def listLength(key: String): Long = {
    jedis.llen(key)
  }
}
