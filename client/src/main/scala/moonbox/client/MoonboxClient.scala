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

import java.net.InetSocketAddress

import moonbox.client.entity.{ConnectionState, JobState, MoonboxRowSet}

import scala.collection.mutable

object MoonboxClient {

	def builder(): Builder = new Builder()

	def builder(clientOptions: ClientOptions) = {
		new Builder().setConf(clientOptions.options)
	}

	class Builder {
		private val settings = mutable.Map.empty[String, String]

		def setConf(key: String, value: String): Builder = {
			settings.put(key, value)
			this
		}

		def setConf(kv: Map[String, String]): Builder = {
			settings ++= kv
			this
		}

		def build(): MoonboxClient = {
			require(settings.contains(ClientOptions.USER))
			require(settings.contains(ClientOptions.PASSWORD))
			new MoonboxClientImpl(settings.toMap)
		}
	}

}

trait MoonboxClient {

	/** constructors */
	def newClient: MoonboxClient

	/** connection related */
	def token: String

	def sessionId: String

	def version: String

	def isActive: Boolean

	def close(): Unit

	def getReadTimeout: Int

	/* time unit: ms */
	def setReadTimeout(milliseconds: Int): Unit

	def getFetchSize: Int

	def setFetchSize(size: Int): Unit

	def getMaxRows: Int

	def setMaxRows(size: Int): Unit

	def getServers: Seq[InetSocketAddress]

	def getConf(key: String): Option[String]

	def getAllConf: Map[String, String]

	/** userSys related */
	def userInfo: AnyRef

	/** metadata related */
	/* list */
	def listDatabases: Seq[String]

	def listTables(database: String): Seq[String]

	def listViews(database: String): Seq[String]

	def listFunctions(database: String): Seq[String]

	def listVariables(username: String): Seq[String]

	/* alter */
	/* exists */
	/* create */
	/* drop */

	/** interactive query related */
	def getConnectionState: ConnectionState

	def getCurrentDatabase: String

	def setCurrentDatabase(databaseName: String): Unit

	def interactiveQuery(interactiveSql: Seq[String]): MoonboxRowSet

	/* query with default fetchSize and timeout */
	def interactiveQuery(interactiveSql: Seq[String], fetchSize: Int): MoonboxRowSet

	def interactiveQueryWithTimeout(interactiveSql: Seq[String], milliseconds: Int): MoonboxRowSet

	/**
	  * @param fetchSize    note: ZERO means query with default fetchSize
	  * @param milliseconds note: ZERO means synchronized query without timeout
	  * @return
	  */
	def interactiveQuery(interactiveSql: Seq[String], fetchSize: Int, milliseconds: Int): MoonboxRowSet

	def interactiveQuery(interactiveSql: Seq[String], fetchSize: Int, maxRows: Int, milliseconds: Int): MoonboxRowSet

	def cancelInteractiveQuery(): Boolean

	/** batch query related */
	def batchQuery(jobSql: Seq[String], config: Map[String, String]): String

	/* return jobId */
	def batchQueryProgress(jobId: String): JobState

	def cancelBatchQuery(jobId: String): Boolean

	//  def batchQuery(jobSql: Seq[String], config: String): String  /* return jobId */
	def runningJobs: Seq[String]

	def waitingJobs: Seq[String]

	def failedJobs: Seq[String]

	def jobHistory: Seq[AnyRef]

	/** timer related */
	/** privilege related */

}
