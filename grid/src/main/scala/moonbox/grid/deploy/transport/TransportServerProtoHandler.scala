package moonbox.grid.deploy.transport

import java.io.{PrintWriter, StringWriter}
import java.net.InetSocketAddress
import java.util.concurrent.ConcurrentHashMap

import io.netty.channel.{Channel, ChannelHandlerContext, ChannelInboundHandlerAdapter}
import io.netty.util.ReferenceCountUtil
import moonbox.common.MbLogging
import moonbox.grid.deploy.{ConnectionInfo, ConnectionType, MbService}
import moonbox.message.protobuf.ProtoMessage
import moonbox.protocol.util.ProtoOutboundMessageBuilder

import scala.collection.JavaConverters._

class TransportServerProtoHandler(channelToToken: ConcurrentHashMap[Channel, String],
                                  channelToSessionId: ConcurrentHashMap[Channel, String],
                                  mbService: MbService)
  extends ChannelInboundHandlerAdapter with MbLogging {

  override def exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) = {
    val sw = new StringWriter()
    cause.printStackTrace(new PrintWriter(sw))
    logError(sw.toString)
    super.exceptionCaught(ctx, cause)
    ctx.close
  }

  override def channelRead(ctx: ChannelHandlerContext, msg: Any) = {
    try {
      msg match {
        case m: ProtoMessage => handleProtoMessage(ctx, m)
        case other => logWarning(s"Unknown message type $other")
      }
    } finally {
      ReferenceCountUtil.release(msg)
    }
  }

  override def channelInactive(ctx: ChannelHandlerContext) = {
    implicit val connection:ConnectionInfo = getConnectionInfo(ctx)
    val channel = ctx.channel()
    if (channelToToken.containsKey(channel)) {
      val token = channelToToken.remove(channel)
      try {
        if (channelToSessionId.containsKey(channel)) {
          val sessionId = channelToSessionId.remove(channel)
          logInfo(s"Closing session with sessionId: $sessionId")
          mbService.closeSession(token, sessionId)
        }
      } finally {
        mbService.logout(token)
      }
    }
    super.channelInactive(ctx)
  }

  private def getConnectionInfo(ctx: ChannelHandlerContext) : ConnectionInfo = {
    val remote = ctx.channel().remoteAddress() match {
      case i:InetSocketAddress => i.toString
      case _ => "Unknown"
    }
    val local = ctx.channel().localAddress() match {
      case i:InetSocketAddress => i.toString
      case _ => "Unknown"
    }
    ConnectionInfo(local, remote, ConnectionType.CLIENT)
  }

  private def prettyError(error: Option[String]): String = {
    error match {
      case Some(_) => s"ERROR=$error"
      case None => "SUCCESSFULLY"
    }
  }

  private def handleProtoMessage(ctx: ChannelHandlerContext, message: ProtoMessage): Unit = {
    implicit val connection:ConnectionInfo = getConnectionInfo(ctx)
    val channel = ctx.channel()
    val messageId = message.getMessageId
    val result: ProtoMessage = {
      if (message.hasLoginInbound){
        val username = message.getLoginInbound.getUesername
        val password = message.getLoginInbound.getPassword
        val outbound = mbService.login(username, password)
        logInfo(s"User($username) login completed: " + prettyError(outbound.error))
        if (outbound.token.isDefined) {
          channelToToken.put(channel, outbound.token.get)
        }
        val toResp = ProtoOutboundMessageBuilder.loginOutbound(outbound.token.orNull, outbound.error.orNull)
        ProtoMessage.newBuilder().setMessageId(messageId).setLoginOutbound(toResp).build()
      } else if (message.hasLogoutInbound) {
        val token = Option(channelToToken.get(channel)).getOrElse(message.getLogoutInbound.getToken)
        val username = mbService.decodeToken(token)
        val outbound = mbService.logout(token)
        logInfo(s"User($username), Token($token) logout completed: " + prettyError(outbound.error))
        val toResp = ProtoOutboundMessageBuilder.logoutOutbound(outbound.error.orNull)
        ProtoMessage.newBuilder().setMessageId(messageId).setLogoutOutbound(toResp).build()
      } else if (message.hasOpenSessionInbound){
        val database = message.getOpenSessionInbound.getDatabase
        val isLocal = message.getOpenSessionInbound.getIsLocal
        val token = Option(channelToToken.get(channel)).getOrElse(message.getOpenSessionInbound.getToken)
        val username = mbService.decodeToken(token)
        val outbound = mbService.openSession(token, Some(database), isLocal)
        logInfo(s"User($username), Token($token) open session completed: " + prettyError(outbound.error))
        if (outbound.sessionId.isDefined) {
          channelToSessionId.put(channel, outbound.sessionId.get)
        }
        val toResp = ProtoOutboundMessageBuilder.openSessionOutbound(outbound.sessionId.orNull, outbound.workerHost, outbound.workerPort, outbound.error.orNull)
        ProtoMessage.newBuilder().setMessageId(messageId).setOpenSessionOutbound(toResp).build()
      } else if (message.hasCloseSessionInbound) {
        val token = Option(channelToToken.get(channel)).getOrElse(message.getCloseSessionInbound.getToken)
        val sessionId = Option(channelToSessionId.get(channel)).getOrElse(message.getCloseSessionInbound.getSessionId)
        val outbound = mbService.closeSession(token, sessionId)
        logInfo(s"Token($token), SessionId($sessionId) close session completed: " + prettyError(outbound.error))
        val toResp = ProtoOutboundMessageBuilder.closeSessionOutbound(outbound.error.orNull)
        ProtoMessage.newBuilder().setMessageId(messageId).setCloseSessionOutbound(toResp).build()
      } else if (message.hasInteractiveQueryInbound) {
        val in = message.getInteractiveQueryInbound
        val token = Option(channelToToken.get(channel)).getOrElse(in.getToken)
        val sessionId = Option(channelToSessionId.get(channel)).getOrElse(in.getSessionId)
        val sqls = in.getSqlList.asScala.toList
        val outbound = if (in.hasFetchSize && in.hasMaxRows) {
          val fetchSize = in.getFetchSize.getValue
          val maxRows = in.getMaxRows.getValue
          mbService.interactiveQuery(token, sessionId, sqls, fetchSize, maxRows)
        } else if (in.hasFetchSize && !in.hasMaxRows) {
          val fetchSize = in.getFetchSize.getValue
          mbService.interactiveQuery(token, sessionId, sqls, fetchSize = fetchSize)
        } else if (!in.hasFetchSize && in.hasMaxRows) {
          val maxRows = in.getMaxRows.getValue
          mbService.interactiveQuery(token, sessionId, sqls, maxRows = maxRows)
        } else {
          mbService.interactiveQuery(token, sessionId, sqls)
        }
        val protoResultData = {
          outbound.data match {
            case None => null
            case Some(resultData) =>
              val protoData = ProtoOutboundMessageBuilder.protoData(resultData.data, resultData.schema)
              ProtoOutboundMessageBuilder.resultData(resultData.cursor, resultData.schema, protoData, resultData.hasNext)
          }
        }
        val toResp = ProtoOutboundMessageBuilder.interactiveQueryOutbound(outbound.error.orNull, protoResultData)
        ProtoMessage.newBuilder().setMessageId(message.getMessageId).setInteractiveQueryOutbound(toResp).build()
      }
      else if (message.hasInteractiveNextResultInbound) {
        val in = message.getInteractiveNextResultInbound
        val token = channelToToken.get(channel)
        val sessionId = Option(channelToSessionId.get(channel)).getOrElse(in.getSessionId)
        val outbound = mbService.interactiveNextResult(token, sessionId)
        logInfo(s"NextResult_query(SessionId=$sessionId) completed: " + prettyError(outbound.error))
        val protoResultData = {
          outbound.data match {
            case None => null
            case Some(resultData) =>
              val protoData = ProtoOutboundMessageBuilder.protoData(resultData.data, resultData.schema)
              ProtoOutboundMessageBuilder.resultData(resultData.cursor, resultData.schema, protoData, resultData.hasNext)
          }
        }
        val toResp = ProtoOutboundMessageBuilder.interactiveNextResultOutbound(outbound.error.orNull, protoResultData)
        ProtoMessage.newBuilder().setMessageId(message.getMessageId).setInteractiveNextResultOutbound(toResp).build()
      }
      else if (message.hasBatchQueryInbound) {
        val in = message.getBatchQueryInbound
        val token = Option(channelToToken.get(channel)).getOrElse(in.getToken)
        val username = mbService.decodeToken(token)
        val sqls = in.getSqlList
        val config = in.getConfig
        val outbound = mbService.batchQuery(token, sqls.asScala, config)
        logInfo(s"User($username, token=$token) batch query completed: " + prettyError(outbound.error))
        val toResp = ProtoOutboundMessageBuilder.batchQueryOutbound(outbound.jobId.orNull, outbound.error.orNull)
        ProtoMessage.newBuilder().setBatchQueryOutbound(toResp).build()
      } else if (message.hasBatchQueryProgressInbound){
        val in = message.getBatchQueryProgressInbound
        val token = Option(channelToToken.get(channel)).getOrElse(in.getToken)
        val username = mbService.decodeToken(token)
        val jobId = in.getJobId
        val outbound = mbService.batchQueryProgress(token, jobId)
        logInfo(s"User($username, jobId=$jobId, token=$token) batch query progress request completed: message=${outbound.message}, state=${outbound.state.orNull}")
        val toResp = ProtoOutboundMessageBuilder.batchQueryProgressOutbound(outbound.message, outbound.state.orNull)
        ProtoMessage.newBuilder().setBatchQueryProgressOutbound(toResp).build()
      } else if (message.hasCancelQueryInbound) {
        val in = message.getCancelQueryInbound
        val token = Option(channelToToken.get(channel)).getOrElse(in.getToken)
        val sessionId = Option(channelToSessionId.get(channel)).getOrElse(in.getSessionId)
        val username = mbService.decodeToken(token)
        val error = (in.getJobId, in.getSessionId) match {
          case (jId: String, _) if jId != "" => mbService.batchQueryCancel(token, jId).error
          case (_, sId: String) if sId != "" => mbService.interactiveQueryCancel(token, sId).error
          case (jId, sId) =>
            logWarning(s"Received invalid cancel query message: JobId=$jId, SessionId=$sId")
            Some("Invalid cancel query message, do nothing.")
        }
        logInfo(s"User($username, token=$token, sessionId=$sessionId) query cancel completed: " + prettyError(error))
        val toResp = ProtoOutboundMessageBuilder.cancelQueryOutbound(error.orNull)
        ProtoMessage.newBuilder().setMessageId(messageId).setCancelQueryOutbound(toResp).build()
      } else {
        logWarning(s"Received unsupported message type: $message, do noting!")
        ProtoMessage.newBuilder().setMessageId(messageId).build()
      }
    }
    ctx.writeAndFlush(result)
  }
}
