package moonbox.client

import java.net.{InetAddress, SocketAddress}

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
  def batchQuery(token: String, sqls: Seq[String], config: String): String
  /**
    * @return JobState consists of error and\or job state
    */
  def batchQueryProgress(token: String, jobId: String): JobState
  def cancelInteractiveQuery(token: String, sessionId: String): Boolean
  def cancelBatchQuery(token: String, jobId: String): Boolean

  /* ---------------------------- client related -------------------------- */
  def connect(): ClientInterface
  def isConnected(): Boolean
  def isActive(): Boolean
  def close(): Unit
  def setReadTimeout(milliseconds: Int): Unit
  def getReadTimeout(): Int  /* time unit: ms */
  def getRemoteAddress(): SocketAddress
}
