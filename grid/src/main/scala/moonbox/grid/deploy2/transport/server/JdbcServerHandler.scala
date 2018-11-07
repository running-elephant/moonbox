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

import java.io.{PrintWriter, StringWriter}
import java.net.InetSocketAddress
import java.util.concurrent.ConcurrentHashMap

import io.netty.channel.{Channel, ChannelHandlerContext, ChannelInboundHandlerAdapter}
import io.netty.util.ReferenceCountUtil
import moonbox.common.MbLogging
import moonbox.grid.{ConnectionInfo, ConnectionType}
import moonbox.grid.deploy2.MbService
import moonbox.protocol.client._

class JdbcServerHandler(
                         channelToToken: ConcurrentHashMap[Channel, String],
                         channelToSessionId: ConcurrentHashMap[Channel, String],
                         mbService: MbService) extends ChannelInboundHandlerAdapter with MbLogging {

  override def channelRead(ctx: ChannelHandlerContext, msg: Any) = {
    try {
      handleMessage(ctx, msg)
    } finally {
      ReferenceCountUtil.release(msg)
    }
  }

  override def exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) = {
    val sw = new StringWriter()
    cause.printStackTrace(new PrintWriter(sw))
    logInfo(sw.toString)
    ctx.close
  }

  override def channelInactive(ctx: ChannelHandlerContext) = {
    val channel = ctx.channel()
    if (channelToToken.containsKey(channel)) {
      implicit val connection = getConnectionInfo(ctx)
      val token = channelToToken.remove(channel)
      if (channelToSessionId.containsKey(channel)) {
        val sessionId = channelToSessionId.remove(channel)
        logInfo(s"Closing session with sessionId: $sessionId")
        mbService.closeSession(token, sessionId)
      }
      mbService.logout(token)
    }
    super.channelInactive(ctx)
  }

  private def prettyError(error: Option[String]): String = {
    error match {
      case Some(_) => s"ERROR=$error"
      case None => "SUCCESSFULLY"
    }
  }

  private def getConnectionInfo(ctx: ChannelHandlerContext) : ConnectionInfo = {
    val remote = ctx.channel().remoteAddress() match {
      case i:InetSocketAddress => i
      case _ => throw new Exception("unknown socket address ")
    }
    val local = ctx.channel().localAddress() match {
      case i:InetSocketAddress => i
      case _ => throw new Exception("unknown socket address ")
    }

    ConnectionInfo(remote, local, ConnectionType.JDBC)
  }

  private def handleMessage(ctx: ChannelHandlerContext, message: Any): Unit = {
    implicit val connection:ConnectionInfo = getConnectionInfo(ctx)
    val channel = ctx.channel()
    val result = message match {
      case login: LoginInbound =>
        logInfo(s"User ${login.username} try login.")
        val outbound = mbService.login(login.username, login.password)
        logInfo(s"User(${login.username}) login completed: " + prettyError(outbound.error))
        if (outbound.token.isDefined) {
          channelToToken.put(channel, outbound.token.get)
        }
        outbound.setId(login.getId)
      case logout: LogoutInbound =>
        val token = channelToToken.remove(channel)
        val username = mbService.getLoginManager().tokenManager.decode(token)
        val outbound = mbService.logout(token)
        logInfo(s"User($username), Token($token) logout completed: " + prettyError(outbound.error))
        outbound.setId(logout.getId)
      case r: RequestAccessInbound =>
        val token = if (channelToToken.containsKey(channel)){
          channelToToken.get(channel)
        } else null
        val outbound = mbService.requestAccess(token, r.isLocal, ConnectionType.JDBC)
        outbound.setId(r.getId)
      case openSession@OpenSessionInbound(_, database, isLocal) =>
        val token = channelToToken.get(channel)
        val username = mbService.getLoginManager().tokenManager.decode(token)
        val outbound = mbService.openSession(token, database, isLocal)
        logInfo(s"User($username), Token($token) open session completed: " + prettyError(outbound.error))
        if (outbound.sessionId.isDefined) {
          channelToSessionId.put(channel, outbound.sessionId.get)
        }
        outbound.setId(openSession.getId)
      case closeSession@CloseSessionInbound(_, _) =>
        val token = channelToToken.get(channel)
        val sessionId = channelToSessionId.remove(channel)
        val outbound = mbService.closeSession(token, sessionId)
        logInfo(s"Token($token), SessionId($sessionId) close session completed: " + prettyError(outbound.error))
        outbound.setId(closeSession.getId)
      case query@InteractiveQueryInbound(_, _, sqls, fetchSize, maxRows) =>
        val token = channelToToken.get(channel)
        val sessionId = channelToSessionId.get(channel)
        val outbound = mbService.interactiveQuery(token, sessionId, sqls, fetchSize, maxRows)
        logInfo(s"Query(sqls=$sqls, fetchSize=$fetchSize, maxRows=$maxRows) completed: " + prettyError(outbound.error))
        outbound.setId(query.getId)
      case next@InteractiveNextResultInbound(_, _, cursor, fetchSize) =>
        val token = channelToToken.get(channel)
        val sessionId = channelToSessionId.get(channel)
        val outbound = mbService.interactiveNextResult(token, sessionId, cursor, fetchSize)
        logInfo(s"NextResult_query(cursor=$cursor, fetchSize=$fetchSize) completed: " + prettyError(outbound.error))
        outbound.setId(next.getId)
      case cancel: CancelQueryInbound =>
        val token = channelToToken.get(channel)
        val username = mbService.getLoginManager().tokenManager.decode(token)
        val sessionId = channelToSessionId.get(channel)
        val outbound = mbService.cancelQuery(token, jobId = None, sessionId=Some(sessionId))
        logInfo(s"User($username, token=$token, sessionId=$sessionId) query cancel completed: " + prettyError(outbound.error))
        outbound.setId(cancel.getId)
      case _ => logInfo("Received unsupported message, do noting!")
    }
    ctx.writeAndFlush(result)
  }

}
