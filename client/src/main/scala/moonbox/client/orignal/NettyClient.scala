package moonbox.client.orignal

import java.io.IOException
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

import io.netty.bootstrap.Bootstrap
import io.netty.channel._
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioSocketChannel
import io.netty.handler.codec.serialization.{ClassResolvers, CompatibleObjectEncoder, ObjectDecoder, ObjectEncoder}
import io.netty.util.concurrent.DefaultThreadFactory
import moonbox.client.entity.{JobState, MoonboxRow, MoonboxRowSet}
import moonbox.client.exception.BackendException
import moonbox.client.{ClientInterface, ClientOptions}
import moonbox.protocol.client._
import moonbox.protocol.util.SchemaUtil

import scala.collection.JavaConverters._

private[client] object NettyClient {
  val daemonNioEventLoopGroup = new NioEventLoopGroup(0, new DefaultThreadFactory(this.getClass, true))
  val nioEventLoopGroup = new NioEventLoopGroup()
}

private[client] class NettyClient(clientOptions: ClientOptions) extends ClientInterface {

  import NettyClient._

  def this(host: String, port: Int, timeout: Int) = this(ClientOptions.builder().host(host).port(port).timeout(timeout).build())
  def this(host: String, port: Int) = this(ClientOptions.builder().host(host).port(port).build())
  def this(host: String) = this(ClientOptions.builder().host(host).build())
  def this() = this(ClientOptions.builder().build())

  /* val */
  private val host = clientOptions.host
  private val port = clientOptions.port
  private val CONNECTION_TIMEOUT_MILLIS = 1000 * 120 // ms
  private val promises = new ConcurrentHashMap[Long, ChannelPromise]
  private val responses = new ConcurrentHashMap[Long, Outbound]
  private val callbacks = new ConcurrentHashMap[Long, Outbound => Any]
  /* var */
  private var channel: Channel = _
  private var connected: Boolean = false
  private var readTimeout: Int = 1000 * clientOptions.timeout
  private var dataFetchClient: NettyClient = _

  @throws(classOf[Exception])
  def connect(): NettyClient = {
    if (isConnected()) {
      return this
    }
    val bootstrap = new Bootstrap()
      .group(daemonNioEventLoopGroup)
      .channel(classOf[NioSocketChannel])
      .option[java.lang.Boolean](ChannelOption.SO_KEEPALIVE, true)
      .option[java.lang.Boolean](ChannelOption.TCP_NODELAY, true)
      .option[java.lang.Integer](ChannelOption.SO_RCVBUF, 10240)
      .option[java.lang.Integer](ChannelOption.CONNECT_TIMEOUT_MILLIS, CONNECTION_TIMEOUT_MILLIS)
      .handler(new ChannelInitializer[SocketChannel]() {
        override def initChannel(ch: SocketChannel) = {
          ch.pipeline
            .addLast(new ObjectDecoder(Int.MaxValue, ClassResolvers.cacheDisabled(null)))  /* inbound */
            .addLast(new MessageHandler(promises, responses, callbacks))  /* inbound */
            .addLast(new ObjectEncoder())  /* outbound */
        }
      })
    val channelFuture = bootstrap.connect(host, port)
    if (!channelFuture.await(CONNECTION_TIMEOUT_MILLIS)) {
      throw new IOException(s"Connecting to $host timed out ($CONNECTION_TIMEOUT_MILLIS ms)")
    } else if (channelFuture.cause != null) {
      throw new IOException(s"Failed to connect to $host", channelFuture.cause)
    }
    channel = channelFuture.channel()
    this.connected = true
    this
  }

  override def setDataFetchClient(client: ClientInterface) = {
    client match {
      case c: NettyClient => dataFetchClient = c
      case other => throw new Exception(s"DataFetchClient type should be consistent with ${this.getClass.getTypeName}, but got ${other.getClass.getTypeName}")
    }
  }
  override def getRemoteAddress() = {
    if (channel != null && isConnected) channel.remoteAddress()
    else throw new ChannelException("Channel unestablished")
  }

  def genMessageId: Long = math.abs(UUID.randomUUID.getLeastSignificantBits)
  def sendWithCallback(msg: Any, callback: Any => Any) = {
    msg match {
      case in: Inbound =>
        val msgId = in.getId
        callbacks.put(msgId, callback)
        sendMessage(msg)
      case other => throw new Exception(s"Unsupported message: $other")
    }
  }
  @throws(classOf[Exception])
  def sendMessageSync(message: Any, timeout: Int = readTimeout): Outbound = {
    message match {
      case in: Inbound =>
        val messageId = in.getId
        try {
          val promise = channel.newPromise()
          promises.put(messageId, promise)
          sendMessage(in)
          if (timeout == 0) {
            promise.await()
          } else if (!promise.await(timeout)) {
            throw new Exception(s"No response within $timeout ms")
          }
          if (!promise.isSuccess) {
            throw promise.cause()
          }
          responses.get(messageId)
        } finally release(messageId)
      case other => throw new Exception(s"Unsupported message: $other")
    }
  }
  @throws(classOf[Exception]) def sendMessage(message: Any): ChannelFuture = {
    if (isActive()) {
      channel.writeAndFlush(message)
    } else throw new Exception("Channel is not active.")
  }
  def isConnected(): Boolean = this.connected
  def isActive(): Boolean = channel != null && channel.isActive
  def close(): Unit = {
    /* EventLoopGroup should not be shutdown */
    if (channel != null) channel.close()
  }

  private def release(key: Long): Unit = {
    promises.remove(key)
    responses.remove(key)
  }

  override def login(username: String, password: String): String = {
    sendMessageSync(wrapMessage(LoginInbound(username, password))) match {
      case LoginOutbound(Some(token), _) => token
      case LoginOutbound(_, Some(error)) => throw BackendException(error)
      case other => throw new Exception(s"Unknown message: $other")
    }
  }

  override def logout(token: String): Boolean = {
    sendMessageSync(wrapMessage(LogoutInbound(token))) match {
      case LogoutOutbound(None) => true
      case LogoutOutbound(error) => throw new Exception(s"Logout failed: ERROR=$error, TOKEN=$token")
      case other => throw new Exception(s"Unknown message: $other")
    }
  }

  override def openSession(token: String, database: String, isLocal: Boolean): (String, String, Int) = {
    sendMessageSync(wrapMessage(OpenSessionInbound(token, Some(database), clientOptions.extraOptions.toSeq.toMap))) match {
      case OpenSessionOutbound(Some(sessionId), Some(workerHost), Some(workerPort), None) => (sessionId, workerHost, workerPort)
      case OpenSessionOutbound(_, _, _, Some(error)) => throw BackendException(error)
      case other => throw new Exception(s"Unknown message: $other")
    }
  }

  override def closeSession(token: String, sessionId: String): Boolean = {
    sendMessageSync(wrapMessage(CloseSessionInbound(token, sessionId))) match {
      case CloseSessionOutbound(None) => true
      case CloseSessionOutbound(error) => throw new Exception(s"Close session failed: ERROR=$error, TOKEN=$token, SessionId=$sessionId")
      case other => throw new Exception(s"Unknown message: $other")
    }
  }

  override def interactiveQuery(token: String, sessionId: String, sqls: Seq[String], fetchSize: Int, timeout: Int): MoonboxRowSet = {
    val outbound = query(token, sessionId, sqls, fetchSize, timeout = timeout)
    toMoonboxRowSet(token, sessionId, outbound, timeout)
  }

  override def interactiveQuery(token: String, sessionId: String, sqls: Seq[String], fetchSize: Int, maxRows: Int, timeout: Int): MoonboxRowSet = {
    val outbound = query(token, sessionId, sqls, fetchSize, maxRows, timeout)
    toMoonboxRowSet(token, sessionId, outbound, timeout)
  }

  override def batchQuery(username: String, password: String, sqls: Seq[String], config: Map[String, String]): String = {
    checkConnected()
    sendMessageSync(wrapMessage(BatchQueryInbound(username, password, sqls, config)), getReadTimeout()) match {
      case BatchQueryOutbound(_, Some(error)) => throw new Exception(s"Batch query error: ERROR=$error")
      case BatchQueryOutbound(Some(jobId), _) => jobId
      case other => throw new Exception(s"Unknown message: $other")
    }
  }

  override def batchQueryProgress(username: String, password: String, jobId: String): JobState = {
    checkConnected()
    sendMessageSync(wrapMessage(BatchQueryProgressInbound(username, password, jobId))) match {
      case BatchQueryProgressOutbound(message, state) => JobState(message, state.getOrElse(""))
      case other => throw new Exception(s"Unknown message: $other")
    }
  }

  override def cancelInteractiveQuery(token: String, sessionId: String): Boolean = {
    checkConnected()
    sendMessageSync(wrapMessage(InteractiveQueryCancelInbound(token, sessionId))) match {
      case CancelQueryOutbound(Some(error)) => throw new Exception(s"Cancel query error: ERROR=$error, TOKEN=$token, SessionId=$sessionId")
      case CancelQueryOutbound(None) => true
      case other => throw new Exception(s"Unknown message: $other")
    }
  }

  override def cancelBatchQuery(username: String, password: String, jobId: String): Boolean = {
    checkConnected()
    sendMessageSync(wrapMessage(BatchQueryCancelInbound(username, password, jobId))) match {
      case CancelQueryOutbound(Some(error)) => throw new Exception(s"Cancel query error: ERROR=$error, USER=$username, JobId=$jobId")
      case CancelQueryOutbound(None) => true
      case other => throw new Exception(s"Unknown message: $other")
    }
  }

  override def setReadTimeout(milliseconds: Int): Unit = {
    readTimeout = milliseconds
  }
  /* time unit: ms */
  override def getReadTimeout(): Int = readTimeout

  private def wrapMessage(message: Message) = {
    /* set message id */
    message.setId(genMessageId)
  }

  /* for interactive query */
  private def query(token: String, sessionId: String, sqls: Seq[String], fetchSize: Int, maxRows: Int = Int.MinValue, timeout: Int): InteractiveQueryOutbound = {
    checkConnected()
    sendMessageSync(wrapMessage(InteractiveQueryInbound(token,sessionId, sqls, fetchSize, maxRows)), timeout) match {
      case out@InteractiveQueryOutbound(None, _) => out
      case InteractiveQueryOutbound(Some(error), _) => throw BackendException(error)
      case other => throw new Exception(s"Unknown message: $other")
    }
  }

  private def interactiveNextResult(token: String, sessionId: String, timeout: Int): ResultData = {
    dataFetchClient.sendMessageSync(wrapMessage(InteractiveNextResultInbound(Some(token), sessionId)), timeout) match {
      case InteractiveNextResultOutbound(Some(error), _) => throw BackendException(error)
      case InteractiveNextResultOutbound(_, Some(resultData)) => resultData
      case other => throw new Exception(s"Unknown message: $other")
    }
  }

  private def toMoonboxRowSet(token: String, sessionId: String, outbound: InteractiveQueryOutbound, timeout: Int): MoonboxRowSet = {
    if (outbound.data.isEmpty) {
      return new MoonboxRowSet()
    }
    var resultData: ResultData = outbound.data.get
    val rowIterator = new Iterator[MoonboxRow] {
      var internalIter = resultData.data.toIterator
      override def hasNext: Boolean = internalIter.hasNext || resultData.hasNext
      override def next(): MoonboxRow = {
        if (internalIter.hasNext) {
          new MoonboxRow(internalIter.next().toArray)
        } else if (resultData.hasNext) {
          resultData = interactiveNextResult(token, sessionId, timeout)
          internalIter = resultData.data.toIterator
          next()
        } else throw new Exception("No more iterable MoonboxRow.")
      }
    }
    val schema = outbound.data.map(_.schema).getOrElse(SchemaUtil.emptyJsonSchema)
    new MoonboxRowSet(rowIterator.asJava, schema)
  }

  private def checkConnected(): Unit = {
    if (!isActive) {
      throw new Exception("Connection unestablished.")
    }
  }
}
