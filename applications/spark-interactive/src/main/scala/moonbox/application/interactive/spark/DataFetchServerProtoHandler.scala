/*
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

package moonbox.application.interactive.spark

import java.io.{PrintWriter, StringWriter}

import io.netty.channel.{ChannelHandlerContext, ChannelInboundHandlerAdapter}
import io.netty.util.ReferenceCountUtil
import moonbox.common.MbLogging
import moonbox.message.protobuf.{InteractiveNextResultOutbound, ProtoMessage}
import moonbox.protocol.util.ProtoOutboundMessageBuilder

import scala.collection.mutable
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class DataFetchServerProtoHandler(sessionIdToJobRunner: mutable.Map[String, Runner]) extends ChannelInboundHandlerAdapter with MbLogging {

  override def channelRead(ctx: ChannelHandlerContext, msg: Any) = {
    try {
      msg match {
        case m: ProtoMessage => Future(handleProtoMessage(ctx, m))
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

  private def handleProtoMessage(ctx: ChannelHandlerContext, message: ProtoMessage): Unit = {
    val msgId = message.getMessageId
    if (message.hasInteractiveNextResultInbound) {
      val in = message.getInteractiveNextResultInbound
      val sessionId = in.getSessionId
      logInfo(s"Received InteractiveNextResultInbound(SessionId=$sessionId)")
      sessionIdToJobRunner.get(sessionId) match {
        case Some(runner) =>
          val response = try {
            val resultData = runner.fetchResultData()
            ProtoOutboundMessageBuilder.interactiveNextResultOutbound(null, sessionId, resultData.schema, resultData.data, resultData.hasNext)
          } catch {
            case e: Exception =>
              val msg = if (e.getMessage != null) {
                e.getMessage
              } else {
                e.getStackTrace.map(_.toString).mkString("\n")
              }
              ProtoOutboundMessageBuilder.interactiveNextResultOutbound(msg, null)
          }
          ctx.writeAndFlush(buildProtoMessage(msgId, response))
        case None =>
          val errorMsg = s"DataFetch ERROR: Invalid sessionId or session lost, SessionId=$sessionId"
          val toResp = ProtoOutboundMessageBuilder.interactiveNextResultOutbound(errorMsg, null)
          ctx.writeAndFlush(buildProtoMessage(msgId, toResp))
      }
    } else {
      logWarning(s"Received unsupported message type: $message, do noting!")
    }
  }

  private def buildProtoMessage(messageId: Long, outbound: InteractiveNextResultOutbound): ProtoMessage = {
    ProtoMessage.newBuilder().setMessageId(messageId).setInteractiveNextResultOutbound(outbound).build()
  }
}
*/
