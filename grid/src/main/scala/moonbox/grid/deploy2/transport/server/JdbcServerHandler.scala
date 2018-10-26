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

package moonbox.grid.deploy2.transport.server
import java.util.concurrent.ConcurrentHashMap
import io.netty.channel.{Channel, ChannelHandlerContext, ChannelInboundHandlerAdapter}
import io.netty.util.ReferenceCountUtil
import moonbox.common.MbLogging
import moonbox.grid.deploy2.MbService
import moonbox.protocol.client._

class JdbcServerHandler(
	channelToIdentifier: ConcurrentHashMap[Channel, (String, String)],
	mbService: MbService) extends ChannelInboundHandlerAdapter with MbLogging {

	override def channelRead(ctx: ChannelHandlerContext, msg: Any) = {
		try {
		  	handleMessage(ctx, msg)
		} finally {
			ReferenceCountUtil.release(msg)
		}
	}

	override def exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) = {
		cause.printStackTrace()
		ctx.close
	}

	override def channelInactive(ctx: ChannelHandlerContext) = {
		if (channelToIdentifier.containsKey(ctx.channel())) {
			val (sessionId, token) = channelToIdentifier.get(ctx.channel())
		    logInfo(s"Closing session with sessionId: $sessionId")
			mbService.closeSession(token, sessionId)
		  	channelToIdentifier.remove(ctx.channel())
		}
		super.channelInactive(ctx)
	}

	private def handleMessage(ctx: ChannelHandlerContext, message: Any): Unit = {
		val result = message match {
			case login: LoginInbound =>
				logInfo(s"User ${login.username} try login.")
				mbService.login(login.username, login.password).setId(login.getId)
			case logout: LogoutInbound =>
				val username = mbService.getLoginManager().tokenManager.decode(logout.token)
				logInfo(s"User ${username.getOrElse(logout.token)}")
				mbService.logout(logout.token).setId(logout.getId)
			case openSession@OpenSessionInbound(token, database, isLocal) =>
				mbService.openSession(token, database, isLocal)
					.setId(openSession.getId)
			case closeSession@CloseSessionInbound(token, sessionId) =>
				mbService.closeSession(token, sessionId).setId(closeSession.getId)
			case query@InteractiveQueryInbound(token, sessionId, sqls, fetchSize, maxRows) =>
				mbService.interactiveQuery(token, sessionId, sqls, fetchSize, maxRows).setId(query.getId)
			case next@InteractiveNextResultInbound(token, sessionId, cursor, fetchSize) =>
				mbService.interactiveNextResult(token, sessionId, cursor, fetchSize).setId(next.getId)
			case cancel: CancelQueryInbound =>
				val (sessionId, token) = channelToIdentifier.get(ctx.channel())
				mbService.cancelQuery(token, sessionId).setId(cancel.getId)
		  	case _ => logInfo("Received unsupported message, do noting!")
		}
		ctx.writeAndFlush(result)
	}

}
