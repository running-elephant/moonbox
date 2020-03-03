/*
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

package moonbox.jdbc

import java.util.{Properties, UUID}

import moonbox.client.MoonboxClient

/**
  * @param pwd md5 value of the password
  */
case class JdbcSession(moonboxClient: MoonboxClient,
                       database: String,
                       user: String,
                       pwd: String, // md5 String of the original password
                       connectionProperties: Properties,
                       isLocal: Boolean = true,
                       id: String = UUID.randomUUID().toString,
                       sessionStart: Long = System.currentTimeMillis
                      ) {

  def setReadTimeout(milliseconds: Int) = moonboxClient.setReadTimeout(milliseconds)
  def getReadTimeout = moonboxClient.getReadTimeout
  def isClosed: Boolean = !moonboxClient.isActive
  def close(): Unit = {
    if (moonboxClient != null) moonboxClient.close()
  }
}
*/
