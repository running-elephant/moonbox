package moonbox.client

import java.util.concurrent.ConcurrentHashMap

import io.netty.channel.{Channel, ChannelHandlerContext, ChannelInboundHandlerAdapter, ChannelPromise}
import io.netty.util.ReferenceCountUtil
import moonbox.common.MbLogging
import moonbox.common.message._

class JdbcClientHandler extends ChannelInboundHandlerAdapter with MbLogging {

  private var ctx: ChannelHandlerContext = _

  override def channelActive(ctx: ChannelHandlerContext): Unit = {
    this.ctx = ctx
  }

  override def channelRead(ctx: ChannelHandlerContext, msg: Any): Unit = {
    try {
      if (callbacks.nonEmpty) handleMessageWithCallback(msg)
      if (!promises.isEmpty) handleMessageWithPromise(msg)
    } finally {
      ReferenceCountUtil.release(msg)
    }
  }

  override def exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable): Unit = {
    cause.printStackTrace()
    if (ctx.channel().isActive) ctx.close
  }

  override def channelReadComplete(ctx: ChannelHandlerContext): Unit = {
  }

  private val promises: ConcurrentHashMap[Long, ChannelPromise] = new ConcurrentHashMap[Long, ChannelPromise]
  private val responses: ConcurrentHashMap[Long, JdbcOutboundMessage] = new ConcurrentHashMap[Long, JdbcOutboundMessage]
  private val callbacks: collection.mutable.Map[Long, JdbcOutboundMessage => Any] = collection.mutable.Map.empty[Long, JdbcOutboundMessage => Any]

  def send(msg: Any, callback: => JdbcOutboundMessage => Any): Unit = {
    try {
      if (ctx == null) throw new IllegalStateException("ChannelHandlerContext is null")
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
          ctx.writeAndFlush(inboundMessage).sync()
          val timeSpent = System.currentTimeMillis() - startTime
          val logMsg = inboundMessage match {
            case login: JdbcLoginInbound =>
              login.copy(password = "***")
            case other => other
          }
          logInfo(s"Sending request $logMsg to ${getRemoteAddress(ctx.channel())} took $timeSpent ms")
        case _ => throw new Exception("Unsupported message")
      }
    } catch {
      case e: Exception => logError(e.getMessage)
    }
  }

  private def handleMessageWithCallback(response: Any): Unit = {
    try {
      response match {
        case echo: EchoOutbound => callback(echo.messageId, echo)
        case login: JdbcLoginOutbound => callback(login.messageId, login)
        case query: JdbcQueryOutbound => callback(query.messageId, query)
        case dataFetch: DataFetchOutbound => callback(dataFetch.dataFetchState.messageId, dataFetch)
        case _ => throw new Exception("Unsupported message")
      }
    } catch {
      case e: Exception => logError(e.getMessage)
    }
  }

  /**
    * get and run the callback function
    *
    * @param key      callback function key
    * @param response message response from the server
    */
  private def callback(key: Long, response: JdbcOutboundMessage): Any = {
    val callback = callbacks(key)
    if (callback != null) {
      callbacks.remove(key)
      callback(response)
    } else null
  }

  def getRemoteAddress(channel: Channel): String = {
    channel.remoteAddress().toString
  }

  def send(msg: Any) = {
    try {
      if (ctx == null) throw new IllegalStateException("ChannelHandlerContext is null")
      msg match {
        case inboundMessage: JdbcInboundMessage =>
          val promise = ctx.channel().newPromise()
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
          ctx.writeAndFlush(inboundMessage).sync()
          val timeSpent = System.currentTimeMillis() - startTime
          val logMsg = inboundMessage match {
            case login: JdbcLoginInbound =>
              login.copy(password = "***")
            case other => other
          }
          logInfo(s"Sending request $logMsg to ${getRemoteAddress(ctx.channel())} took $timeSpent ms")
        case _ => throw new Exception("Unsupported message")
      }
    } catch {
      case e: Exception => logError(e.getMessage)
    }
  }

  private def handleMessageWithPromise(msg: Any) = {
    msg match {
      case resp: JdbcOutboundMessage =>
        val id = msg match {
          case echo: EchoOutbound => echo.messageId
          case login: JdbcLoginOutbound => login.messageId
          case logout: JdbcLogoutOutbound => logout.messageId
          case query: JdbcQueryOutbound => query.messageId
          case dataFetch: DataFetchOutbound => dataFetch.dataFetchState.messageId
        }
        if (promises.containsKey(id)) {
          responses.put(id, resp)
          promises.get(id).setSuccess()
        }
      case _ => throw new Exception("Unsupported message")
    }
  }

  // return null if it throws an exception
  def sendAndReceive(msg: Any, timeout: Long): JdbcOutboundMessage = {
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

}
