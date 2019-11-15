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

package moonbox.client.javaAPI

import java.lang._
import java.net.InetSocketAddress
import java.util
import java.util.{Map => JMap}

import moonbox.client.MoonboxClient
import moonbox.client.entity.{ConnectionState, JobState, MoonboxRowSet}

import scala.collection.JavaConverters._

class JavaMoonboxClient private (moonboxClient: MoonboxClient) {

  def this() = this(MoonboxClient.builder().build())
  def this(key: String, value: String) = this(MoonboxClient.builder().setConf(key, value).build())
  def this(kv: JMap[String, String]) = this(MoonboxClient.builder().setConf(kv.asScala.toMap).build())

  /** constructors */
  def newClient: JavaMoonboxClient = new JavaMoonboxClient(moonboxClient.newClient)

  /** connection related */
  def token: String = moonboxClient.token
  def sessionId: String = moonboxClient.sessionId
  def version: String = moonboxClient.version
  def isActive: Boolean = moonboxClient.isActive
  def close(): Unit = moonboxClient.close()
  def getReadTimeout: Int = moonboxClient.getReadTimeout
  def setReadTimeout(milliseconds: Int): Unit = moonboxClient.setReadTimeout(milliseconds)
  def getFetchSize: Int = moonboxClient.getFetchSize
  def setFetchSize(size: Int): Unit = moonboxClient.setFetchSize(size)
  def getMaxRows: Int = moonboxClient.getMaxRows
  def setMaxRows(size: Int): Unit = moonboxClient.setMaxRows(size)
  def getServers: util.List[InetSocketAddress] = moonboxClient.getServers.asJava
  def getConf(key: String): String = moonboxClient.getConf(key).orNull
  def getAllConf: JMap[String, String] = moonboxClient.getAllConf.asJava

  /** userSys related */
  def userInfo: Object = moonboxClient.userInfo

  /** metadata related */
  /* list */
  def listDatabases: util.List[String] = moonboxClient.listDatabases.asJava
  def listTables(database: String): util.List[String] = moonboxClient.listTables(database).asJava
  def listViews(database: String): util.List[String] = moonboxClient.listViews(database).asJava
  def listFunctions(database: String): util.List[String] = moonboxClient.listFunctions(database).asJava
  def listVariables(username: String): util.List[String] = moonboxClient.listVariables(username).asJava
  /* alter */
  /* exists */
  /* create */
  /* drop */

  /** interactive query related */
  def getConnectionState: ConnectionState = moonboxClient.getConnectionState
  def getCurrentDatabase: String = moonboxClient.getCurrentDatabase
  def setCurrentDatabase(databaseName: String): Unit = moonboxClient.setCurrentDatabase(databaseName)
  def interactiveQuery(interactiveSql: util.List[String]): MoonboxRowSet = moonboxClient.interactiveQuery(interactiveSql.asScala)
  def interactiveQuery(interactiveSql: util.List[String], fetchSize: Int): MoonboxRowSet = moonboxClient.interactiveQuery(interactiveSql.asScala, fetchSize)
  def interactiveQueryWithTimeout(interactiveSql: util.List[String], milliseconds: Int): MoonboxRowSet = moonboxClient.interactiveQuery(interactiveSql.asScala, milliseconds)
  /**
    * @param fetchSize note: ZERO means query with default fetchSize
    * @param milliseconds note: ZERO means synchronized query without timeout
    * @return
    */
  def interactiveQuery(interactiveSql: util.List[String], fetchSize: Int, milliseconds: Int): MoonboxRowSet = moonboxClient.interactiveQuery(interactiveSql.asScala, fetchSize, milliseconds)
  def interactiveQuery(interactiveSql: util.List[String], fetchSize: Int, maxRows: Int, milliseconds: Int): MoonboxRowSet = moonboxClient.interactiveQuery(interactiveSql.asScala, fetchSize, maxRows, milliseconds)
  def cancelInteractiveQuery(): Boolean = moonboxClient.cancelInteractiveQuery()

  /** batch query related */
  def batchQuery(jobSql: util.List[String], config: JMap[String, String]): String = moonboxClient.batchQuery(jobSql.asScala, config.asScala.toMap)
  def batchQueryProgress(jobId: String): JobState = moonboxClient.batchQueryProgress(jobId)
  def cancelBatchQuery(jobId: String): Boolean = moonboxClient.cancelBatchQuery(jobId)
  /* return jobId */
  def runningJobs: util.List[String] = moonboxClient.runningJobs.asJava
  def waitingJobs: util.List[String] = moonboxClient.waitingJobs.asJava
  def failedJobs: util.List[String] = moonboxClient.failedJobs.asJava
  def jobHistory: util.List[Object] = moonboxClient.jobHistory.asJava

  /** timer related */
  /** privilege related */

}
*/
