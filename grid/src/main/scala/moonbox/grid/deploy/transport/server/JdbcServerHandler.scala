package moonbox.grid.deploy.transport.server

import io.netty.channel.{ChannelHandlerContext, ChannelInboundHandlerAdapter}
import io.netty.util.ReferenceCountUtil
import moonbox.common.MbLogging
import moonbox.grid.deploy.MbService
import moonbox.grid.deploy.transport.model._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

class JdbcServerHandler(mbService: MbService) extends ChannelInboundHandlerAdapter with MbLogging {

  val clientId2SessionId: collection.mutable.Map[String, String] = collection.mutable.Map.empty[String, String]

  override def channelRead(ctx: ChannelHandlerContext, msg: Any) = {
    try {
      handleMessage(ctx, msg)
    } finally
      ReferenceCountUtil.release(msg)
  }

  override def exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) = {
    cause.printStackTrace()
    ctx.close
  }

  def handleMessage(ctx: ChannelHandlerContext, msg: Any): Unit = {
    msg match {
      case echo: EchoInbound =>
        logInfo(s"Received message: $echo")
        ctx.writeAndFlush(EchoOutbound(echo.messageId, echo.content))
      case login: JdbcLoginInbound =>
        logInfo(s"Received message: ${login.copy(password = "***")}")
        mbService.login(login.user, login.password).onComplete {
          case Success(v) =>
            if (v) {
              logInfo(s"User: ${login.user} login succeed")
              //open session
              logInfo(s"Opening session for user: ${login.user}, clientId=${login.clientId}")
              mbService.openSession(login.user, Some(login.database)).onComplete {
                case Success(v) =>
                  if (v.error.isEmpty && v.sessionId.isDefined) {
                    clientId2SessionId += (login.clientId -> v.sessionId.get)
                    logInfo(s"Open session succeed, sessionId=${v.sessionId}")
                    ctx.writeAndFlush(JdbcLoginOutbound(login.messageId, login.clientId, None, Option("open session succeed")))
                  } else {
                    logInfo(s"Open session failed, error: ${v.error}")
                    ctx.writeAndFlush(JdbcLoginOutbound(login.messageId, login.clientId, v.error, Option("open session failed")))
                  }
                case Failure(e) =>
                  logInfo(e.getMessage)
                  ctx.writeAndFlush(JdbcLoginOutbound(login.messageId, login.clientId, Option(e.getMessage), Option("open session failed")))
              }
            } else {
              logInfo(s"Login failed user: ${login.user}, clientId=${login.clientId}")
              ctx.writeAndFlush(JdbcLoginOutbound(login.messageId, login.clientId, err = Option("Login failed"), None))
            }
          case Failure(e) => {
            logInfo(e.getMessage)
            ctx.writeAndFlush(JdbcLoginOutbound(login.messageId, login.clientId, Option(e.getMessage), None))
          }
        }
      case logout: JdbcLogoutInbound =>
        logInfo(s"Received message: $logout")
        val sessionId = clientId2SessionId(logout.clientId)
        mbService.closeSession(logout.user, sessionId).onComplete {
          case Success(v) =>
            if (v.error.isDefined) {
              logInfo(s"Close session error: ${v.error.get}")
              ctx.writeAndFlush(JdbcLogoutOutbound(logout.messageId, logout.clientId, v.error, Option("Close session error")))
            } else {
              logInfo("Session closed, logging out")
              mbService.logout(logout.user).onComplete {
                case Success(v) =>
                  if (v.error.isDefined) {
                    logInfo("Logout error")
                    ctx.writeAndFlush(JdbcLogoutOutbound(logout.messageId, logout.clientId, v.error, Option(s"User: ${logout.user} logout error")))
                  } else {
                    logInfo("Logout succeed")
                    clientId2SessionId.remove(logout.clientId)
                    ctx.writeAndFlush(JdbcLogoutOutbound(logout.messageId, logout.clientId, None, Option("Session closed")))
                  }
                case Failure(e) =>
                  logInfo("Logout failed")
                  ctx.writeAndFlush(JdbcLogoutOutbound(logout.messageId, logout.clientId, Option(e.getMessage), Option(s"User: ${logout.user} logout failed")))
              }
            }
          case Failure(e) =>
            logInfo(s"Close session error: $e")
            ctx.writeAndFlush(JdbcLogoutOutbound(logout.messageId, logout.clientId, Option(e.getMessage), Option("Close session error")))
        }
      case query: JdbcQueryInbound =>
        logInfo(s"Received message: $query")
        // default fetchSize is 200
        if (clientId2SessionId.contains(query.clientId)) {
          mbService.jobQuery(clientId2SessionId(query.clientId), Seq(query.sql), query.fetchSize).onComplete {
            case Success(v) =>
              if (v.error.isDefined) {
                logInfo(s"sql query error: ${v.error.get}")
                ctx.writeAndFlush(JdbcQueryOutbound(query.messageId, query.clientId, Option(v.error.get), null, null))
              } else {
                logInfo(s"sql query '$query' succeed")
                // 1. return JdbcQueryOutbound  2.return DataFetchOutbound,  according to the result data size
                if (v.data.get.size >= v.size.get)
                  ctx.writeAndFlush(JdbcQueryOutbound(query.messageId, query.clientId, None, v.data.orNull, v.schema.orNull))
                else {
                  val fetchState = DataFetchState(query.messageId, query.clientId, v.jobId, 0, v.data.get.size, v.size.get)
                  ctx.writeAndFlush(DataFetchOutbound(fetchState, None, v.data.get, v.schema.get))
                }
              }
            case Failure(e) => {
              logInfo(e.getMessage)
              ctx.writeAndFlush(JdbcQueryOutbound(query.messageId, query.clientId, Option(e.getMessage), null, null))
            }
          }
        } else {
          logInfo(s"User: ${query.user} have not login yet")
          ctx.writeAndFlush(JdbcQueryOutbound(query.messageId, query.clientId, Option("User did not login, login first"), null, null))
        }
      case dataFetch: DataFetchInbound =>
        logInfo(s"Received message: $dataFetch")
        doDataFetch(ctx, dataFetch)
      case oneWay: OneWayMessage =>
        logInfo(s"Received message: $oneWay")
      case _ => logInfo("Received unsupported message, do noting!")
    }
  }

  def doDataFetch(ctx: ChannelHandlerContext, inbound: DataFetchInbound): Unit = {
    mbService.jobResult(inbound.user, inbound.dataFetchState.jobId, inbound.dataFetchState.startRowIndex, inbound.dataFetchState.fetchSize).onComplete {
      case Success(v) =>
        if (v.error.isDefined) {
          logInfo(v.error.get)
          // Return error to client, or retry several times delivered to client
          ctx.writeAndFlush(DataFetchOutbound(inbound.dataFetchState, v.error, null, null))
        } else {
          val dataFetchState = inbound.dataFetchState.copy(fetchSize = v.data.get.size)
          ctx.writeAndFlush(DataFetchOutbound(dataFetchState, None, v.data.get, v.schema.get))
        }
      case Failure(e) => {
        logInfo(e.getMessage)
        ctx.writeAndFlush(DataFetchOutbound(inbound.dataFetchState, Option(e.getMessage), null, null))
      }
    }
  }

}
