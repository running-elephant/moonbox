package moonbox.client

import java.net.InetSocketAddress

import moonbox.client.entity.{ConnectionState, MoonboxRowSet}

import scala.collection.{Map, mutable}

object MoonboxClient{

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
  def getReadTimeout: Int  /* time unit: ms */
  def setReadTimeout(milliseconds: Int): Unit
  def getFetchSize: Int
  def setFetchSize(size: Int): Unit
  def getMaxRows: Long
  def setMaxRows(size: Long): Unit
  def getServers: Seq[InetSocketAddress]
  def getConf(key: String): Option[String]
  def getAllConf: java.util.Map[String, String]

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
  def interactiveQuery(interactiveSql: Seq[String]): MoonboxRowSet /* query with default fetchSize and timeout */
  def interactiveQuery(interactiveSql: Seq[String], fetchSize: Int): MoonboxRowSet
  def interactiveQueryWithTimeout(interactiveSql: Seq[String], milliseconds: Int): MoonboxRowSet
  /**
    * @param fetchSize note: ZERO means query with default fetchSize
    * @param milliseconds note: ZERO means synchronized query without timeout
    * @return
    */
  def interactiveQuery(interactiveSql: Seq[String], fetchSize: Int, milliseconds: Int): MoonboxRowSet
  def interactiveQuery(interactiveSql: Seq[String], fetchSize: Int, maxRows: Long, milliseconds: Int): MoonboxRowSet
  def cancelInteractiveQuery(): Boolean
  def cancelBatchQuery(jobId: String): Boolean

  /** batch query related */
  def submitJob(jobSql: Seq[String], config: java.util.Map[String, String]): String  /* return jobId */
  def submitJob(jobSql: Seq[String], config: String): String  /* return jobId */
  def runningJobs: Seq[String]
  def waitingJobs: Seq[String]
  def failedJobs: Seq[String]
  def jobHistory: Seq[AnyRef]

  /** timer related */
  /** privilege related */

}
