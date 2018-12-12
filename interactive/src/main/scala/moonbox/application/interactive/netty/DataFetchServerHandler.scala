package moonbox.application.interactive.netty

import java.io.{PrintWriter, StringWriter}
import java.util.concurrent.TimeUnit

import akka.util.Timeout
import io.netty.channel.{ChannelHandlerContext, ChannelInboundHandlerAdapter}
import io.netty.util.ReferenceCountUtil
import moonbox.application.interactive.Runner
import moonbox.common.MbLogging
import moonbox.protocol.client.{Inbound, InteractiveNextResultInbound, InteractiveNextResultOutbound}

import scala.collection.mutable

class DataFetchServerHandler(sessionIdToJobRunner: mutable.Map[String, Runner]) extends ChannelInboundHandlerAdapter with MbLogging {

  override def channelRead(ctx: ChannelHandlerContext, msg: Any) = {
    try {
      msg match {
        case m: Inbound => handleProtoMessage(ctx, m)
        case other => logWarning(s"Unknown message type $other")
      }
    } finally {
      ReferenceCountUtil.release(msg)
    }
  }

  override def exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) = {
    val sw = new StringWriter()
    cause.printStackTrace(new PrintWriter(sw))
    logError(sw.toString)
    super.exceptionCaught(ctx, cause)
    ctx.close
  }

  private def handleProtoMessage(ctx: ChannelHandlerContext, inbound: Inbound): Unit = {
    // TODO:  fetch data from runner
    implicit val timeout: Timeout = Timeout(60, TimeUnit.SECONDS)
    inbound match {
      case InteractiveNextResultInbound(token, sessionId) =>
        sessionIdToJobRunner.get(sessionId) match {
          case Some(runner) =>
            val resultData = runner.fetchResultData()
            ctx.writeAndFlush(InteractiveNextResultOutbound(None, Some(resultData)))
          case None =>
            val errorMsg = s"DataFetch ERROR: Invalid sessionId or session lost, SessionId=$sessionId"
            ctx.writeAndFlush(InteractiveNextResultOutbound(Some(errorMsg), None))
        }
      case other => logWarning(s"Received unsupported message type: $other, do noting!")
    }
  }
}
