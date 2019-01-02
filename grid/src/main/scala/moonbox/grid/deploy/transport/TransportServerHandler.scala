/*-
 * <<
 * Moonbox
 * ==
 * Copyright (C) 2016 - 2018 EDP
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
import moonbox.grid.deploy.{ConnectionInfo, ConnectionType, MbService}
import moonbox.protocol.client._

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class TransportServerHandler(channelToToken: ConcurrentHashMap[Channel, String],
														 channelToSessionId: ConcurrentHashMap[Channel, String],
														 mbService: MbService)
	extends ChannelInboundHandlerAdapter with MbLogging {

	override def channelRead(ctx: ChannelHandlerContext, msg: Any) = {
		try {
			Future(handleMessage(ctx, msg))
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

	override def channelInactive(ctx: ChannelHandlerContext) = {
		val channel = ctx.channel()
		if (channelToToken.containsKey(channel)) {
			implicit val connection: ConnectionInfo = getConnectionInfo(ctx)
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

	private def handleMessage(ctx: ChannelHandlerContext, message: Any): Unit = {
		implicit val connection: ConnectionInfo = getConnectionInfo(ctx)
		val channel = ctx.channel()
		val result = message match {
			case login: LoginInbound =>
				val outbound = mbService.login(login.username, login.password)
				outbound.token.foreach(token => channelToToken.put(channel, token))
				outbound.setId(login.getId)
			case logout: LogoutInbound =>
				val token = Option(channelToToken.remove(channel)).getOrElse(logout.token)
				val outbound = mbService.logout(token)
				outbound.setId(logout.getId)
			case openSession@OpenSessionInbound(token, database, config) =>
				val _token = Option(channelToToken.get(channel)).getOrElse(token)
				val outbound = mbService.openSession(_token, database, config)
				outbound.sessionId.foreach(sId => channelToSessionId.put(channel, sId))
				outbound.setId(openSession.getId)
			case closeSession@CloseSessionInbound(token, sessionId) =>
				val _token = Option(channelToToken.get(channel)).getOrElse(token)
				val _sessionId = Option(channelToSessionId.remove(channel)).getOrElse(sessionId)
				val outbound = mbService.closeSession(_token, _sessionId)
				outbound.setId(closeSession.getId)
			case query@InteractiveQueryInbound(token, sessionId, sqls, fetchSize, maxRows) =>
				val _token = Option(channelToToken.get(channel)).getOrElse(token)
				val _sessionId = Option(channelToSessionId.remove(channel)).getOrElse(sessionId)
				val outbound = mbService.interactiveQuery(_token, _sessionId, sqls, fetchSize, maxRows)
				outbound.setId(query.getId)
			case next@InteractiveNextResultInbound(token, sessionId) =>
				val _token = Option(channelToToken.get(channel)).getOrElse(token.get)
				val _sessionId = Option(channelToSessionId.remove(channel)).getOrElse(sessionId)
				val outbound = mbService.interactiveNextResult(_token, _sessionId)
				outbound.setId(next.getId)
			case query@BatchQueryInbound(username, password, sqls, config) =>
				val outbound = mbService.batchQuery(username, password, sqls, config)
				outbound.setId(query.getId)
			case progress@BatchQueryProgressInbound(username, password, jobId) =>
				val outbound = mbService.batchQueryProgress(username, password, jobId)
				outbound.setId(progress.getId)
			case cancel@InteractiveQueryCancelInbound(token, sessionId) =>
				val _token = Option(channelToToken.get(channel)).getOrElse(token)
				val _sessionId = Option(channelToSessionId.get(channel)).getOrElse(sessionId)
				val outbound = mbService.interactiveQueryCancel(_token, sessionId=_sessionId)
				outbound.setId(cancel.getId)
			case cancel@BatchQueryCancelInbound(username, password, jobId) =>
				val outbound = mbService.batchQueryCancel(username, password, jobId)
				outbound.setId(cancel.getId)
			case _ => logWarning("Received unsupported message, do noting!")
		}
		ctx.writeAndFlush(result)
	}
}
