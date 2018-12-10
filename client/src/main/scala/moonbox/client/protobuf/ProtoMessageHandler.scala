package moonbox.client.protobuf

import java.util.concurrent.ConcurrentHashMap

import io.netty.channel.{ChannelHandlerContext, ChannelInboundHandlerAdapter, ChannelPromise}
import io.netty.util.ReferenceCountUtil
import moonbox.message.protobuf._

class ProtoMessageHandler(promises: ConcurrentHashMap[Long, ChannelPromise],
                          responses: ConcurrentHashMap[Long, ProtoMessage],
                          callbacks: ConcurrentHashMap[Long, ProtoMessage => Any])
  extends ChannelInboundHandlerAdapter {

  private var ctx: ChannelHandlerContext = _

  override def channelActive(ctx: ChannelHandlerContext): Unit = {
    this.ctx = ctx
    super.channelActive(ctx)
  }

  override def channelRead(ctx: ChannelHandlerContext, msg: Any): Unit = {
    try {
      if (!promises.isEmpty) {
        handleMessageWithPromise(msg)
      }
      if (!callbacks.isEmpty) {
        handleMessageWithCallback(msg)
      }
    } finally {
      ReferenceCountUtil.release(msg)
    }
  }

  override def exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable): Unit = {
    try {
      super.exceptionCaught(ctx, cause)
    } finally {
      ctx.close()
    }
  }

  private def handleMessageWithCallback(response: Any): Any = {
    response match {
      case resp: ProtoMessage =>
        val msgId = resp.getMessageId
        if (callbacks.contains(msgId)){
          val callback = callbacks.remove(msgId)
          callback(resp)
        }
      case _ => throw new Exception("Unsupported message")
    }
  }

  private def handleMessageWithPromise(message: Any) = {
    message match {
      case resp: ProtoMessage =>
        val id = resp.getMessageId
        if (promises.containsKey(id)) {
          responses.put(id, resp)
          promises.get(id).setSuccess()
        }
      case _ => throw new Exception("Unsupported message")
    }
  }
}

