package moonbox.client

import java.net.InetSocketAddress

import moonbox.client.entity.ConnectionState
import moonbox.client.orignal.NettyClient
import moonbox.client.protobuf.ProtoNettyClient
import moonbox.protocol.NettyMessageType
import org.json.JSONObject

import scala.collection.mutable.ArrayBuffer
import collection.JavaConverters._

private[client] class MoonboxClientImpl(config: CaseInsensitiveMap[String]) extends MoonboxClient {

  private val clientOptions = ClientOptions.builder().options(config).build()
  private val _client: ClientInterface = {
    NettyMessageType.getMessageType(clientOptions.serializer) match {
      case NettyMessageType.JAVA_MESSAGE => new NettyClient(clientOptions)
      case NettyMessageType.PROTOBUF_MESSAGE | _ => new ProtoNettyClient(clientOptions)
    }
  }
  private var _dataFetchClient: ClientInterface = _
  private var _token: String = _
  private var _sessionId: String = _
  private var _currentDatabase: String = clientOptions.database
  private var fetchSize: Int = clientOptions.fetchSize
  private var maxRows: Long = clientOptions.maxRows

  def this(options: Map[String, String]) = this(CaseInsensitiveMap(options))

  initSession()
  def initSession(): MoonboxClientImpl = {
    if (_client != null && !_client.isConnected){
      _client.connect()
    }
    _token = _client.login(clientOptions.user.get, clientOptions.password.get)
    val resp = _client.openSession(_token, clientOptions.database, isLocal)
    _sessionId = resp._1
    val workerHost: String = resp._2
    val workerPort: Int = resp._3
    val dataFetchClient = initDataFetchClient(workerHost, workerPort)
    if (dataFetchClient != null && !dataFetchClient.isConnected){
      dataFetchClient.connect()
    }
    _client.setDataFetchClient(dataFetchClient)
    _dataFetchClient = dataFetchClient
    this
  }

  override def newClient = new MoonboxClientImpl(config)
  override def token = _token
  override def sessionId = _sessionId
  override def version = "0.3.0"
  override def isActive = _client != null && _client.isActive() && _token != null && _sessionId != null && _dataFetchClient != null && _dataFetchClient.isActive()
  override def close() = {
    closeDataFetchClient()
    closeClient()
  }

  override def getReadTimeout = _client.getReadTimeout()
  override def setReadTimeout(milliseconds: Int) = _client.setReadTimeout(milliseconds)

  override def getFetchSize = fetchSize
  override def setFetchSize(size: Int) = {
    fetchSize = size
  }

  override def getMaxRows = maxRows
  override def setMaxRows(size: Long) = {
    maxRows = size
  }

  override def getServers: Seq[InetSocketAddress] = {
    val masterAddress = _client.getRemoteAddress() match {
      case address: InetSocketAddress => address
      case other => throw new Exception(s"Unknown remote address type: ${other.getClass.getTypeName}")
    }
    if (_dataFetchClient == null || !_dataFetchClient.isActive()) {
      return masterAddress :: Nil
    }
    val workerAddress = {
      _dataFetchClient.getRemoteAddress() match {
        case address: InetSocketAddress => address
        case other => throw new Exception(s"Unknown remote address type: ${other.getClass.getTypeName}")
      }
    }
    masterAddress :: workerAddress :: Nil
  }

  override def getConf(key: String) = config.get(key)
  override def getAllConf = config.asJava

  override def userInfo = throw new Exception("Unsupported temporarily.")
  override def listDatabases = {
    val rowSet = interactiveQuery("show databases" :: Nil)
    val databases = ArrayBuffer.empty[String]
    while (rowSet.hasNext){
      databases += rowSet.next().getString(0)
    }
    databases
  }
  override def listTables(database: String) = {
    val rowSet = interactiveQuery(s"show tables in $database" :: Nil)
    val tables = ArrayBuffer.empty[String]
    while (rowSet.hasNext){
      tables += rowSet.next().getString(0)
    }
    tables
  }
  override def listViews(database: String) = {
    val rowSet = interactiveQuery(s"show views in $database" :: Nil)
    val views = ArrayBuffer.empty[String]
    while (rowSet.hasNext) {
      views += rowSet.next().getString(0)
    }
    views
  }
  override def listFunctions(database: String) = {
    val rowSet = interactiveQuery(s"show functions in $database" :: Nil)
    val functions = ArrayBuffer.empty[String]
    while (rowSet.hasNext) {
      functions += rowSet.next().getString(0)
    }
    functions
  }
  override def listVariables(username: String) = throw new Exception("Unsupported temporarily.")
  override def getConnectionState = {
    val servers = getServers
    ConnectionState(servers.head, clientOptions.user.get, getReadTimeout, Some(_token), Some(_sessionId), servers.last, isLocal)
  }
  override def getCurrentDatabase = _currentDatabase // TODO: 'use database' sql may be untracked
  override def setCurrentDatabase(databaseName: String) = {
    interactiveQuery(s"use $databaseName" :: Nil)
    _currentDatabase = databaseName
  }

  /* interactive query */
  override def interactiveQuery(interactiveSql: Seq[String]) = interactiveQuery(interactiveSql, getFetchSize, getReadTimeout)
  override def interactiveQuery(interactiveSql: Seq[String], fetchSize: Int) = interactiveQuery(interactiveSql, fetchSize, getReadTimeout)
  override def interactiveQueryWithTimeout(interactiveSql: Seq[String], milliseconds: Int) = interactiveQuery(interactiveSql, getFetchSize, milliseconds)
  override def interactiveQuery(interactiveSql: Seq[String], fetchSize: Int, milliseconds: Int) = {
    checkActive(_client)
    checkActive(_dataFetchClient)
    _client.interactiveQuery(_token, _sessionId, interactiveSql, fetchSize, milliseconds)
  }
  override def interactiveQuery(interactiveSql: Seq[String], fetchSize: Int, maxRows: Long, milliseconds: Int) = {
    checkActive(_client)
    checkActive(_dataFetchClient)
    _client.interactiveQuery(_token, _sessionId, interactiveSql, fetchSize, maxRows, milliseconds)
  }
  override def cancelInteractiveQuery() = {
    checkActive(_client)
    checkActive(_dataFetchClient)
    _client.cancelQuery(_token, null, _sessionId)
  }

  /* batch query */
  override def submitJob(jobSql: Seq[String], config: java.util.Map[String, String]): String = {
    val configJson = new JSONObject(config)
    submitJob(jobSql, configJson.toString)
  }
  override def submitJob(jobSql: Seq[String], config: String) = {
    checkActive(_client)
    _client.batchQuery(_token, jobSql, config)
  }
  override def runningJobs = throw new Exception("Unsupported temporarily.")
  override def waitingJobs = throw new Exception("Unsupported temporarily.")
  override def failedJobs = throw new Exception("Unsupported temporarily.")
  override def jobHistory = throw new Exception("Unsupported temporarily.")
  private def isLocal: Boolean = clientOptions.isLocal

  /**
    * @return generate a new netty client as the data fetch client
    */
  private def initDataFetchClient(host: String, port: Int): ClientInterface = {
    val dClient = clientOptions.serializer match {
      case "java" => new NettyClient(ClientOptions.builder(clientOptions).host(host).port(port).build())
      case "protobuf" | _ => new ProtoNettyClient(ClientOptions.builder(clientOptions).host(host).port(port).build())
    }
    dClient.setReadTimeout(getReadTimeout)
    dClient
  }

  private def checkActive(client: ClientInterface): Unit = {
    if (client == null || !client.isActive()) {
      throw new Exception("Moonbox client is not active, please reconnect.")
    }
  }

  private def closeClient(): Unit = {
    _sessionId = null
    _token = null
    if (_client != null) {
      _client.close()
    }
  }

  private def closeDataFetchClient(): Unit = {
    if (_dataFetchClient != null) {
      _dataFetchClient.close()
    }
    _dataFetchClient = null
  }
}
