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

package moonbox.grid.deploy.transport

import java.io.{PrintWriter, StringWriter}
import java.net.InetSocketAddress
import java.util.concurrent.ConcurrentHashMap

import io.netty.channel.{Channel, ChannelHandlerContext, ChannelInboundHandlerAdapter}
import io.netty.util.ReferenceCountUtil
import moonbox.common.MbLogging
import moonbox.grid.deploy.{ConnectionInfo, ConnectionType, MoonboxService}
import moonbox.grid.deploy.Interface._
import moonbox.message.protobuf
import moonbox.message.protobuf.ProtoMessage
import moonbox.protocol.util.ProtoOutboundMessageBuilder

import scala.collection.JavaConverters._
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

class TransportServerProtoHandler(channelToToken: ConcurrentHashMap[Channel, String],
	channelToSessionId: ConcurrentHashMap[Channel, String],
	mbService: MoonboxService)
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
				case m: protobuf.ProtoMessage => handleProtoMessage(ctx, m)
				case other => logWarning(s"Unknown message type $other")
			}
		} finally {
			ReferenceCountUtil.release(msg)
		}
	}

	override def channelInactive(ctx: ChannelHandlerContext) = {
		implicit val connection: ConnectionInfo = getConnectionInfo(ctx)
		val channel = ctx.channel()
		if (channelToToken.containsKey(channel)) {
			val token = channelToToken.remove(channel)
			try {
				if (channelToSessionId.containsKey(channel)) {
					val sessionId = channelToSessionId.remove(channel)
					mbService.closeSession(token, sessionId)
				}
			} finally {
				mbService.logout(token)
			}
		}
		super.channelInactive(ctx)
	}

	private def getConnectionInfo(ctx: ChannelHandlerContext): ConnectionInfo = {
		val remote = ctx.channel().remoteAddress() match {
			case i: InetSocketAddress => i.toString
			case _ => "Unknown"
		}
		val local = ctx.channel().localAddress() match {
			case i: InetSocketAddress => i.toString
			case _ => "Unknown"
		}
		ConnectionInfo(local, remote, ConnectionType.CLIENT)
	}

	private def handleProtoMessage(ctx: ChannelHandlerContext, message: protobuf.ProtoMessage): Unit = {
		val messageId = message.getMessageId
		if (message.hasLoginInbound) {
			handleLogin(ctx, message.getLoginInbound, messageId)
		} else if (message.hasLogoutInbound) {
			handleLogout(ctx, message.getLogoutInbound, messageId)
		} else if (message.hasOpenSessionInbound) {
			handleOpenSession(ctx, message.getOpenSessionInbound, messageId)
		} else if (message.hasCloseSessionInbound) {
			handleCloseSession(ctx, message.getCloseSessionInbound, messageId)
		} else if (message.hasInteractiveQueryInbound) {
			handleInteractiveQuery(ctx, message.getInteractiveQueryInbound, messageId)
		} else if (message.hasInteractiveNextResultInbound) {
			handleNextResult(ctx, message.getInteractiveNextResultInbound, messageId)
		} else if (message.hasBatchQueryInbound) {
			handleBatchQuery(ctx, message.getBatchQueryInbound, messageId)
		} else if (message.hasBatchQueryProgressInbound) {
			handleBatchProgress(ctx, message.getBatchQueryProgressInbound, messageId)
		} else if (message.hasInteractiveQueryCancelInbound) {
			handleInteractiveCancel(ctx, message.getInteractiveQueryCancelInbound, messageId)
		} else if (message.hasBatchQueryCancelInbound) {
			handleBatchCancel(ctx, message.getBatchQueryCancelInbound, messageId)
		} else {
			val errorMessage = s"Received unsupported message: $message, drop it!"
			logWarning(errorMessage)
			val message1: ProtoMessage =
				protobuf.ProtoMessage.newBuilder()
					.setMessageId(messageId)
					.setInternalError(protobuf.InternalError.newBuilder().setError(errorMessage))
					.build()
			ctx.writeAndFlush(message1)
		}
	}

	private def handleLogin(ctx: ChannelHandlerContext, inbound: protobuf.LoginInbound, messageId: Long): Unit = {
		implicit val connection: ConnectionInfo = getConnectionInfo(ctx)
		val username = inbound.getUsername
		val password = inbound.getPassword

		Future(mbService.login(username, password, () => {
			ctx.close()
		})) onComplete {
			case Success(LoginOutbound(token, error)) => loginResponse(token, error)
			case Failure(e) => loginResponse(None, Some(e.getMessage))
		}

		def loginResponse(token: Option[String], error: Option[String]): Unit = {
			token.foreach(t => channelToToken.put(ctx.channel(), t))
			val toResp = ProtoOutboundMessageBuilder.loginOutbound(token.orNull, error.orNull)
			val resp = protobuf.ProtoMessage.newBuilder().setMessageId(messageId).setLoginOutbound(toResp).build()
			ctx.writeAndFlush(resp)
		}
	}

	private def handleLogout(ctx: ChannelHandlerContext, inbound: protobuf.LogoutInbound, messageId: Long): Unit = {
		implicit val connection: ConnectionInfo = getConnectionInfo(ctx)
		val token = Option(channelToToken.get(ctx.channel())).getOrElse(inbound.getToken)

		Future(mbService.logout(token)) onComplete {
			case Success(LogoutOutbound(error)) => logoutResponse(error)
			case Failure(e) => logoutResponse(Some(e.getMessage))
		}

		def logoutResponse(error: Option[String]): Unit = {
			val toResp = ProtoOutboundMessageBuilder.logoutOutbound(error.orNull)
			val resp = protobuf.ProtoMessage.newBuilder().setMessageId(messageId).setLogoutOutbound(toResp).build()
			ctx.writeAndFlush(resp)
		}
	}

	private def handleOpenSession(ctx: ChannelHandlerContext, inbound: protobuf.OpenSessionInbound, messageId: Long): Unit = {
		implicit val connection: ConnectionInfo = getConnectionInfo(ctx)
		val database = inbound.getDatabase
		val token = Option(channelToToken.get(ctx.channel())).getOrElse(inbound.getToken)
		val config = inbound.getConfigMap

		Future(mbService.openSession(token, Some(database), config.asScala.toMap)) onComplete {
			case Success(OpenSessionOutbound(sessionId, workerHost, workerPort, error)) => openSessionResponse(sessionId, workerHost, workerPort, error)
			case Failure(e) => openSessionResponse(None, None, None, Some(e.getMessage))
		}

		def openSessionResponse(sessionId: Option[String], workerHost: Option[String], workerPort: Option[Int], error: Option[String]): Unit = {
			sessionId.foreach(s => channelToSessionId.put(ctx.channel(), s))
			val toResp = ProtoOutboundMessageBuilder.openSessionOutbound(sessionId.orNull, workerHost, workerPort, error.orNull)
			val resp: ProtoMessage = protobuf.ProtoMessage.newBuilder().setMessageId(messageId).setOpenSessionOutbound(toResp).build()
			ctx.writeAndFlush(resp)
		}
	}

	private def handleCloseSession(ctx: ChannelHandlerContext, inbound: protobuf.CloseSessionInbound, messageId: Long): Unit = {
		implicit val connection: ConnectionInfo = getConnectionInfo(ctx)
		val token = Option(channelToToken.get(ctx.channel())).getOrElse(inbound.getToken)
		val sessionId = Option(channelToSessionId.get(ctx.channel())).getOrElse(inbound.getSessionId)

		Future(mbService.closeSession(token, sessionId)) onComplete {
			case Success(CloseSessionOutbound(error)) => closeSessionResponse(error)
			case Failure(e) => closeSessionResponse(Some(e.getMessage))
		}

		def closeSessionResponse(error: Option[String]): Unit = {
			val toResp = ProtoOutboundMessageBuilder.closeSessionOutbound(error.orNull)
			val message: ProtoMessage = protobuf.ProtoMessage.newBuilder().setMessageId(messageId).setCloseSessionOutbound(toResp).build()
			ctx.writeAndFlush(message)
		}
	}

	private def handleInteractiveQuery(ctx: ChannelHandlerContext, in: protobuf.InteractiveQueryInbound, messageId: Long): Unit = {
		implicit val connection: ConnectionInfo = getConnectionInfo(ctx)
		val token = Option(channelToToken.get(ctx.channel())).getOrElse(in.getToken)
		val sessionId = Option(channelToSessionId.get(ctx.channel())).getOrElse(in.getSessionId)
		val sqls = in.getSqlList.asScala.toList

		Future(
			if (in.hasFetchSize && in.hasMaxRows) {
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
			}) onComplete {
			case Success(InteractiveQueryOutbound(error, data)) => interactiveResponse(error, data)
			case Failure(e) => interactiveResponse(Some(e.getMessage), None)
		}

		def interactiveResponse(error: Option[String], data: Option[ResultData]): Unit = {
			val protoResultData: Option[protobuf.ResultData] = data.map { resultData =>
				val protoData = ProtoOutboundMessageBuilder.protoData(resultData.data, resultData.schema)
				ProtoOutboundMessageBuilder.resultData(resultData.cursor, resultData.schema, protoData, resultData.hasNext)
			}
			val toResp = ProtoOutboundMessageBuilder.interactiveQueryOutbound(error.orNull, protoResultData.orNull)
			val message = protobuf.ProtoMessage.newBuilder().setMessageId(messageId).setInteractiveQueryOutbound(toResp).build()
			ctx.writeAndFlush(message)
		}
	}

	private def handleNextResult(ctx: ChannelHandlerContext, in: protobuf.InteractiveNextResultInbound, messageId: Long): Unit = {
		implicit val connection: ConnectionInfo = getConnectionInfo(ctx)
		val token = channelToToken.get(ctx.channel())
		val sessionId = Option(channelToSessionId.get(ctx.channel())).getOrElse(in.getSessionId)

		Future(mbService.interactiveNextResult(token, sessionId)) onComplete {
			case Success(InteractiveNextResultOutbound(error, data)) => nextResultResponse(error, data)
			case Failure(e) => nextResultResponse(Some(e.getMessage), None)
		}

		def nextResultResponse(error: Option[String], data: Option[ResultData]): Unit = {
			val protoResultData: Option[protobuf.ResultData] = data.map { resultData =>
				val protoData = ProtoOutboundMessageBuilder.protoData(resultData.data, resultData.schema)
				ProtoOutboundMessageBuilder.resultData(resultData.cursor, resultData.schema, protoData, resultData.hasNext)
			}
			val toResp = ProtoOutboundMessageBuilder.interactiveNextResultOutbound(error.orNull, protoResultData.orNull)
			val message = protobuf.ProtoMessage.newBuilder().setMessageId(messageId).setInteractiveNextResultOutbound(toResp).build()
			ctx.writeAndFlush(message)
		}
	}

	private def handleBatchQuery(ctx: ChannelHandlerContext, in: protobuf.BatchQueryInbound, messageId: Long): Unit = {
		implicit val connection: ConnectionInfo = getConnectionInfo(ctx)
		val username = in.getUsername
		val password = in.getPassword
		val lang = in.getLang
		val sqls = in.getSqlList
		val config = in.getConfigMap

		Future(mbService.batchQuery(username, password, lang, sqls.asScala.toList, config.asScala.toMap)) onComplete {
			case Success(BatchQueryOutbound(jobId, error)) => batchQueryResponse(jobId, error)
			case Failure(e) => batchQueryResponse(None, Some(e.getMessage))
		}

		def batchQueryResponse(jobId: Option[String], error: Option[String]): Unit = {
			val toResp = ProtoOutboundMessageBuilder.batchQueryOutbound(jobId.orNull, error.orNull)
			val message = protobuf.ProtoMessage.newBuilder().setMessageId(messageId).setBatchQueryOutbound(toResp).build()
			ctx.writeAndFlush(message)
		}
	}

	private def handleBatchProgress(ctx: ChannelHandlerContext, in: protobuf.BatchQueryProgressInbound, messageId: Long): Unit = {
		implicit val connection: ConnectionInfo = getConnectionInfo(ctx)
		val username = in.getUsername
		val password = in.getPassword
		val jobId = in.getJobId

		Future(mbService.batchQueryProgress(username, password, jobId)) onComplete {
			case Success(BatchQueryProgressOutbound(message, state)) => batchProgressResponse(message, state)
			case Failure(exception) => batchProgressResponse(exception.getMessage, None)
		}

		def batchProgressResponse(message: String, state: Option[String]): Unit = {
			val toResp = ProtoOutboundMessageBuilder.batchQueryProgressOutbound(message, state.orNull)
			val message1: ProtoMessage = protobuf.ProtoMessage.newBuilder().setMessageId(messageId).setBatchQueryProgressOutbound(toResp).build()
			ctx.writeAndFlush(message1)
		}
	}

	private def handleInteractiveCancel(ctx: ChannelHandlerContext, in: protobuf.InteractiveQueryCancelInbound, messageId: Long): Unit = {
		implicit val connection: ConnectionInfo = getConnectionInfo(ctx)
		val token = Option(channelToToken.get(ctx.channel())).getOrElse(in.getToken)
		val sessionId = Option(channelToSessionId.get(ctx.channel())).getOrElse(in.getSessionId)

		Future(mbService.interactiveQueryCancel(token, sessionId)) onComplete {
			case Success(CancelQueryOutbound(error)) => interactiveCancelResponse(error)
			case Failure(exception) => interactiveCancelResponse(Some(exception.getMessage))
		}

		def interactiveCancelResponse(error: Option[String]): Unit = {
			val toResp = ProtoOutboundMessageBuilder.interactiveQueryCancelOutbound(error.orNull)
			val message: ProtoMessage = protobuf.ProtoMessage.newBuilder().setMessageId(messageId).setInteractiveQueryCancelOutbound(toResp).build()
			ctx.writeAndFlush(message)
		}
	}

	private def handleBatchCancel(ctx: ChannelHandlerContext, in: protobuf.BatchQueryCancelInbound, messageId: Long): Unit = {
		implicit val connection: ConnectionInfo = getConnectionInfo(ctx)
		val username = in.getUsername
		val password = in.getPassword
		val jobId = Option(channelToSessionId.get(ctx.channel())).getOrElse(in.getJobId)

		Future(mbService.batchQueryCancel(username, password, jobId)) onComplete {
			case Success(CancelQueryOutbound(error)) => batchCancelResponse(error)
			case Failure(exception) => batchCancelResponse(Some(exception.getMessage))
		}

		def batchCancelResponse(error: Option[String]): Unit = {
			val toResp = ProtoOutboundMessageBuilder.batchQueryCancelOutbound(error.orNull)
			val message: ProtoMessage = protobuf.ProtoMessage.newBuilder().setMessageId(messageId).setBatchQueryCancelOutbound(toResp).build()
			ctx.writeAndFlush(message)
		}
	}
}
