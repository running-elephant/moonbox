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

package moonbox.client

import java.net.SocketAddress

import moonbox.client.entity.{JobState, MoonboxRowSet}

private[client] trait ClientInterface {
  /**
    * @return token
    */
  def login(username: String, password: String): String
  def logout(token: String): Boolean
  /**
    * @return sessionId
    */
  def openSession(token: String, database: String, isLocal: Boolean): (String, String, Int)
  def closeSession(token: String, sessionId: String): Boolean
  def interactiveQuery(token: String, sessionId: String, sqls: Seq[String], fetchSize: Int, timeout: Int): MoonboxRowSet
  def interactiveQuery(token: String, sessionId: String, sqls: Seq[String], fetchSize: Int, maxRows: Int, timeout: Int): MoonboxRowSet
  def setDataFetchClient(client: ClientInterface): Unit
  /**
    * @return jobId
    */
  def batchQuery(username: String, password: String, sqls: Seq[String], config: Map[String, String]): String
  /**
    * @return JobState consists of error and\or job state
    */
  def batchQueryProgress(username: String, password: String, jobId: String): JobState
  def cancelInteractiveQuery(token: String, sessionId: String): Boolean
  def cancelBatchQuery(username: String, password: String, jobId: String): Boolean

  /* ---------------------------- client related -------------------------- */
  def connect(): ClientInterface
  def isConnected(): Boolean
  def isActive(): Boolean
  def close(): Unit
  def setReadTimeout(milliseconds: Int): Unit
  def getReadTimeout(): Int  /* time unit: ms */
  def getRemoteAddress(): SocketAddress
}
