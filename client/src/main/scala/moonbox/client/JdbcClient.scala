package moonbox.client

import java.io.IOException
import java.net.SocketAddress
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong

import io.netty.bootstrap.Bootstrap
import io.netty.channel._
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioSocketChannel
import io.netty.handler.codec.serialization.{ClassResolvers, ObjectDecoder, ObjectEncoder}
import moonbox.common.MbLogging
import moonbox.common.message._

class JdbcClient(host: String, port: Int) extends MbLogging {

  private var channel: Channel = _
  private val messageId = new AtomicLong()
  var connected: Boolean = _
  val DEFAULT_TIMEOUT = 1000 * 15 //time unit: ms

  private val promises: ConcurrentHashMap[Long, ChannelPromise] = new ConcurrentHashMap[Long, ChannelPromise]
  private val responses: ConcurrentHashMap[Long, JdbcOutboundMessage] = new ConcurrentHashMap[Long, JdbcOutboundMessage]
  private val callbacks: ConcurrentHashMap[Long, JdbcOutboundMessage => Any] = new ConcurrentHashMap[Long, JdbcOutboundMessage => Any]

  def connect(timeout: Int = DEFAULT_TIMEOUT): Unit = {
    try {
      val workerGroup = SingleEventLoopGroup.daemonNioEventLoopGroup
      val b = new Bootstrap()
      b.group(workerGroup)
        .channel(classOf[NioSocketChannel])
        .option[java.lang.Boolean](ChannelOption.SO_KEEPALIVE, true)
        .option[java.lang.Boolean](ChannelOption.TCP_NODELAY, true)
        .option[java.lang.Integer](ChannelOption.SO_RCVBUF, 10240)
        .handler(new ChannelInitializer[SocketChannel]() {
          override def initChannel(ch: SocketChannel) = {
            ch.pipeline.addLast(new ObjectEncoder, new ObjectDecoder(Int.MaxValue, ClassResolvers.cacheDisabled(null)), new ClientInboundHandler(promises, responses, callbacks))
          }
        })
      val cf = b.connect(host, port).syncUninterruptibly()
      if (!cf.await(timeout))
        throw new IOException(s"Connecting to $host timed out (${timeout} ms)")
      else if (cf.cause != null)
        throw new IOException(s"Failed to connect to $host", cf.cause)
      this.channel = cf.channel
      logInfo(s"Connected to ${channel.remoteAddress()}")
      connected = true
    } catch {
      case e: Exception =>
        logError(e.getMessage)
        e.printStackTrace()
        this.close()
    }
  }

  def getRemoteAddress: SocketAddress = {
    if (channel != null) channel.remoteAddress()
    else throw new ChannelException("channel unestablished")
  }

  def getMessageId(): Long = messageId.getAndIncrement()

  def sendOneWayMessage(msg: Any) = send(msg)

  def sendWithCallback(msg: Any, callback: => JdbcOutboundMessage => Any) = send(msg, callback)

  def isConnected(): Boolean = connected

  def close() = {
    if (channel != null) {
      channel.close()
    }
  }

  // return null if it throws an exception
  def sendAndReceive(msg: Any, timeout: Long = DEFAULT_TIMEOUT): JdbcOutboundMessage = {
    if (msg.isInstanceOf[JdbcInboundMessage])
      send(msg)
    val id: Long = msg match {
      case echo: EchoInbound => echo.messageId
      case m: JdbcLoginInbound => m.messageId
      case m: JdbcLogoutInbound => m.messageId
      case m: JdbcQueryInbound => m.messageId
      case m: DataFetchInbound => m.dataFetchState.messageId
      case _ => throw new Exception("Unsupported message format")
    }
    try {
      if (promises.containsKey(id)) {
        if (!promises.get(id).await(timeout))
          throw new Exception(s"no response within $timeout ms")
        else
          responses.get(id)
      } else throw new Exception(s"Send message failed: $msg")
    } catch {
      case e: Exception =>
        logError(e.getMessage)
        null
    } finally release(id)
  }

  private def release(key: Long): Unit = {
    promises.remove(key)
    responses.remove(key)
  }

  def send(msg: Any) = {
    try {
      msg match {
        case inboundMessage: JdbcInboundMessage =>
          val promise = channel.newPromise()
          val id = inboundMessage match {
            case echo: EchoInbound => echo.messageId // used to echo test
            case login: JdbcLoginInbound => login.messageId
            case logout: JdbcLogoutInbound => logout.messageId
            case sqlQuery: JdbcQueryInbound => sqlQuery.messageId
            case dataFetch: DataFetchInbound => dataFetch.dataFetchState.messageId // add callback to map
            case _ => null
          }
          if (id != null)
            promises.put(id.asInstanceOf[Long], promise)
          val startTime = System.currentTimeMillis()
          channel.writeAndFlush(inboundMessage).sync()
          val timeSpent = System.currentTimeMillis() - startTime
          val logMsg = inboundMessage match {
            case login: JdbcLoginInbound =>
              login.copy(password = "***")
            case other => other
          }
          logDebug(s"Sending request $logMsg to ${getRemoteAddress(channel)} took $timeSpent ms")
        case _ => throw new Exception("Unsupported message")
      }
    } catch {
      case e: Exception => logError(e.getMessage)
    }
  }

  def getRemoteAddress(channel: Channel): String = {
    channel.remoteAddress().toString
  }

  def send(msg: Any, callback: => JdbcOutboundMessage => Any): Unit = {
    try {
      msg match {
        case inboundMessage: JdbcInboundMessage =>
          inboundMessage match {
            case login: JdbcLoginInbound =>
              callbacks.put(login.messageId, callback)
            case sqlQuery: JdbcQueryInbound =>
              callbacks.put(sqlQuery.messageId, callback)
            case dataFetch: DataFetchInbound =>
              callbacks.put(dataFetch.dataFetchState.messageId, callback) // add callback to map
            case echo: EchoInbound =>
              callbacks.put(echo.messageId, callback)
          }
          val startTime = System.currentTimeMillis()
          channel.writeAndFlush(inboundMessage).sync()
          val timeSpent = System.currentTimeMillis() - startTime
          val logMsg = inboundMessage match {
            case login: JdbcLoginInbound =>
              login.copy(password = "***")
            case other => other
          }
          logDebug(s"Sending request $logMsg to ${getRemoteAddress(channel)} took $timeSpent ms")
        case _ => throw new Exception("Unsupported message")
      }
    } catch {
      case e: Exception => logError(e.getMessage)
    }
  }

}


