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

package moonbox.jdbc

import java.util.{Properties, UUID}

import moonbox.client.JdbcClient

/**
  *
  * @param jdbcClient
  * @param user
  * @param pwd md5 value of the password
  */
case class JdbcSession(jdbcClient: JdbcClient,
                       database: String,
                       table: String,
                       user: String,
                       pwd: String, // md5 String of the original password
                       connectionProperties: Properties,
                       id: String = UUID.randomUUID().toString,
                       sessionStart: Long = System.currentTimeMillis
                      ) {
  var closed: Boolean = false

  def close(): Unit = {
    if (jdbcClient != null) {
      jdbcClient.close()
    }
    closed = true
  }

  def isClosed(): Boolean = {
    if (jdbcClient.isActive()) {
      closed = false
    } else {
      if (jdbcClient != null) {
        jdbcClient.close()
      }
      closed = true
    }
    closed
  }
}
