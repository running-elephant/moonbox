package moonbox.client

import java.util.concurrent.ConcurrentHashMap

import io.netty.channel.{Channel, ChannelHandlerContext, ChannelInboundHandlerAdapter, ChannelPromise}
import io.netty.util.ReferenceCountUtil
import moonbox.common.MbLogging
import moonbox.common.message._

class ClientInboundHandler(promises: ConcurrentHashMap[Long, ChannelPromise], responses: ConcurrentHashMap[Long, JdbcOutboundMessage], callbacks: ConcurrentHashMap[Long, JdbcOutboundMessage => Any]) extends ChannelInboundHandlerAdapter with MbLogging {

  private var ctx: ChannelHandlerContext = _

  override def channelActive(ctx: ChannelHandlerContext): Unit = {
    this.ctx = ctx
    super.channelActive(ctx)
  }

  override def channelRead(ctx: ChannelHandlerContext, msg: Any): Unit = {
    try {
      if (!callbacks.isEmpty) handleMessageWithCallback(msg)
      if (!promises.isEmpty) handleMessageWithPromise(msg)
    } finally {
      ReferenceCountUtil.release(msg)
    }
  }

  override def exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable): Unit = {
    cause.printStackTrace()
    ctx.close
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
    val callback = callbacks.get(key)
    if (callback != null) {
      callbacks.remove(key)
      callback(response)
    } else null
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
}
