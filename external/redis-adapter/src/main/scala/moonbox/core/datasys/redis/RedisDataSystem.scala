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
import moonbox.core.datasys.DataSystem
import redis.clients.jedis._

class RedisDataSystem(props: Map[String, String])
  extends DataSystem(props) with MbLogging {

  checkOptions("type", "host")

  override def tableNames(): Seq[String] = Seq()

  override def tableName(): String = props("table")

  override def tableProperties(tableName: String): Map[String, String] = {
    props.+("table" -> tableName)
  }

  override def test(): Unit = {
    var connection: Jedis = null
    try {
      connection = getConnection()
    } catch {
      case e: Exception =>
        logError("redis test failed.", e)
        throw e
    } finally {
      if (connection != null) {
        connection.close()
      }
    }
  }

  private def getConnection(): Jedis = {
    val host = props.getOrElse("host", Protocol.DEFAULT_HOST)
    val port = props.get("port").map(_.toInt).getOrElse(Protocol.DEFAULT_PORT)
    val auth = props.getOrElse("auth", null)
    val dbNum = props.get("dbNum").map(_.toInt).getOrElse(Protocol.DEFAULT_DATABASE)
    val timeout = props.get("timeout").map(_.toInt).getOrElse(Protocol.DEFAULT_TIMEOUT)
    val poolConfig: JedisPoolConfig = new JedisPoolConfig()
    poolConfig.setMaxTotal(1)
    poolConfig.setMaxIdle(1)
    poolConfig.setTestOnBorrow(false)
    poolConfig.setTestOnReturn(false)
    poolConfig.setTestWhileIdle(false)
    poolConfig.setMinEvictableIdleTimeMillis(60000)
    poolConfig.setTimeBetweenEvictionRunsMillis(30000)
    poolConfig.setNumTestsPerEvictionRun(-1)
    val pool = new JedisPool(poolConfig, host, port, timeout, auth, dbNum)
    var conn: Jedis = null
    try {
      conn = pool.getResource
    }
    catch {
      case e: Exception => throw e
    }
    conn
  }

}
