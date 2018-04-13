package moonbox.grid.deploy.transport.server

import io.netty.channel.{Channel, ChannelHandlerContext, ChannelInboundHandlerAdapter}
import io.netty.util.ReferenceCountUtil
import moonbox.common.MbLogging
import moonbox.common.message._
import moonbox.grid.deploy.MbService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}
import scala.collection.JavaConverters._

import java.util.concurrent.ConcurrentHashMap

class JdbcServerHandler(channel2SessionIdAndUser: ConcurrentHashMap[Channel, (String, String)], mbService: MbService) extends ChannelInboundHandlerAdapter with MbLogging {

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

  override def channelInactive(ctx: ChannelHandlerContext) = {
    if (channel2SessionIdAndUser.containsKey(ctx.channel())) {
      val sessionId = channel2SessionIdAndUser.get(ctx.channel())._1
      val username = channel2SessionIdAndUser.get(ctx.channel())._2
      logDebug(s"Cleaning session and logging out for user: $username ...")
      logInfo(s"Closing session: sessionId=$sessionId ...")
      mbService.closeSession(username, sessionId).onComplete {
        case Success(v) =>
          if (v.error.isDefined) {
            logInfo(s"Close session error: ${v.error.get}")
          } else {
            logInfo("Session closed, logging out ...")
            mbService.logout(username).onComplete {
              case Success(v) =>
                if (v.error.isDefined) {
                  logInfo(s"User($username) logout error")
                } else {
                  logInfo(s"User($username) logout succeed")
                }
              case Failure(e) =>
                logInfo(s"User($username) logout failed")
            }
          }
        case Failure(e) =>
          logInfo(s"Close session error: $e")
      }
      channel2SessionIdAndUser.remove(ctx.channel())
    }
    logInfo("Remain active (sessionId, user)s: " + channel2SessionIdAndUser.asScala.values.mkString(" | "))
    super.channelInactive(ctx)
  }

  override def channelUnregistered(ctx: ChannelHandlerContext) = {
    super.channelUnregistered(ctx)
  }

  def handleMessage(ctx: ChannelHandlerContext, msg: Any): Unit = {
    msg match {
      case echo: EchoInbound =>
        logInfo(s"Received message: $echo")
        //        channel2SessionIdAndUser.put(ctx.channel(), (echo.messageId.toString, "root"))
        ctx.writeAndFlush(EchoOutbound(echo.messageId, echo.content))
      case login: JdbcLoginInbound =>
        logInfo(s"Received message: ${login.copy(password = "***")}")
        mbService.login(login.user, login.password).onComplete {
          case Success(v) =>
            if (v) {
              logInfo(s"User: ${login.user} login succeed")
              //open session
              logInfo(s"Opening session for user: ${login.user}")
              mbService.openSession(login.user, Some(login.database)).onComplete {
                case Success(v) =>
                  if (v.error.isEmpty && v.sessionId.isDefined) {
                    channel2SessionIdAndUser.put(ctx.channel(), (v.sessionId.get, login.user))
                    logInfo(s"Open session succeed, sessionId=${v.sessionId}")
                    logInfo("Remain active (sessionId, user)s: " + channel2SessionIdAndUser.asScala.values.mkString(" | "))
                    ctx.writeAndFlush(JdbcLoginOutbound(login.messageId, None, Option("open session succeed")))
                  } else {
                    logInfo(s"Open session failed, error: ${v.error}")
                    ctx.writeAndFlush(JdbcLoginOutbound(login.messageId, v.error, Option("open session failed")))
                  }
                case Failure(e) =>
                  logInfo(e.getMessage)
                  ctx.writeAndFlush(JdbcLoginOutbound(login.messageId, Option(e.getMessage), Option("open session failed")))
              }
            } else {
              logInfo(s"Login failed user: ${login.user}")
              ctx.writeAndFlush(JdbcLoginOutbound(login.messageId, err = Option("Login failed"), None))
            }
          case Failure(e) =>
            logInfo(e.getMessage)
            ctx.writeAndFlush(JdbcLoginOutbound(login.messageId, Option(e.getMessage), None))
        }
      case logout: JdbcLogoutInbound =>
        logInfo(s"Received message: $logout")
        val sessionId = channel2SessionIdAndUser.get(ctx.channel())._1
        val username = channel2SessionIdAndUser.get(ctx.channel())._2
        if (username == logout.user) {
          logInfo("Closing session ...")
          mbService.closeSession(logout.user, sessionId).onComplete {
            case Success(v) =>
              if (v.error.isDefined) {
                logInfo(s"Close session error: ${v.error.get}")
                ctx.writeAndFlush(JdbcLogoutOutbound(logout.messageId, v.error, Option("Close session error")))
              } else {
                logInfo("Session closed, logging out ...")
                mbService.logout(logout.user).onComplete {
                  case Success(v) =>
                    if (v.error.isDefined) {
                      logInfo("Logout error")
                      ctx.writeAndFlush(JdbcLogoutOutbound(logout.messageId, v.error, Option(s"User: ${logout.user} logout error")))
                    } else {
                      logInfo("Logout succeed")
                      channel2SessionIdAndUser.remove(ctx.channel())
                      ctx.writeAndFlush(JdbcLogoutOutbound(logout.messageId, None, Option("Session closed")))
                    }
                  case Failure(e) =>
                    logInfo("Logout failed")
                    ctx.writeAndFlush(JdbcLogoutOutbound(logout.messageId, Option(e.getMessage), Option(s"User: ${logout.user} logout failed")))
                }
              }
            case Failure(e) =>
              logInfo(s"Close session error: $e")
              ctx.writeAndFlush(JdbcLogoutOutbound(logout.messageId, Option(e.getMessage), Option("Close session error")))
          }
        }
      case query: JdbcQueryInbound =>
        logInfo(s"Received message: $query")
        if (channel2SessionIdAndUser.containsKey(ctx.channel())) {
          val sqls = query.sql.split(";").toSeq
          // default fetchSize is 200
          mbService.jobQuery(channel2SessionIdAndUser.get(ctx.channel())._1, sqls, query.fetchSize).onComplete {
            case Success(v) =>
              if (v.error.isEmpty) {
                logInfo(s"SQLs(${sqls.mkString("; ")}) query succeed")
                // 1. return JdbcQueryOutbound  2.return DataFetchOutbound,  according to the result data size
                if (v.size.isDefined && v.data.isDefined && v.data.get.size < v.size.get) {
                  val fetchState = DataFetchState(query.messageId, v.jobId, 0, v.data.get.size, v.size.get)
                  ctx.writeAndFlush(DataFetchOutbound(fetchState, v.error, v.data.get, v.schema.get))
                } else {
                  ctx.writeAndFlush(JdbcQueryOutbound(query.messageId, v.error, v.data.orNull, v.schema.orNull))
                }
              } else {
                logInfo(s"SQLs(${sqls.mkString("; ")}) query succeed with error: ${v.error.orNull}")
                ctx.writeAndFlush(JdbcQueryOutbound(query.messageId, v.error, v.data.orNull, v.schema.orNull))
              }
            case Failure(e) =>
              logInfo(e.getMessage)
              ctx.writeAndFlush(JdbcQueryOutbound(query.messageId, Option(e.getMessage), null, null))
          }
        } else {
          logInfo(s"User: ${query.user} have not login yet")
          ctx.writeAndFlush(JdbcQueryOutbound(query.messageId, Option("User did not login, login first"), null, null))
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
    val dataFetch = inbound.dataFetchState
    val offset = dataFetch.startRowIndex
    val requestSize = {
      val remainSize = dataFetch.totalRows - dataFetch.startRowIndex
      if (remainSize <= 0) {
        throw new Exception("No data to fetch")
      }
      if (remainSize > dataFetch.fetchSize) {
        dataFetch.fetchSize
      } else {
        remainSize
      }
    }
    mbService.jobResult(inbound.user, inbound.dataFetchState.jobId, offset, requestSize).onComplete {
      case Success(v) =>
        if (v.error.isDefined) {
          logInfo(v.error.get)
          // Return error to client, or retry several times delivered to client
          ctx.writeAndFlush(DataFetchOutbound(inbound.dataFetchState, v.error, null, null))
        } else {
          val dataFetchState = inbound.dataFetchState.copy(fetchSize = v.data.get.size)
          ctx.writeAndFlush(DataFetchOutbound(dataFetchState, None, v.data.get, v.schema.get))
        }
      case Failure(e) =>
        logInfo(e.getMessage)
        ctx.writeAndFlush(DataFetchOutbound(inbound.dataFetchState, Option(e.getMessage), null, null))
    }
  }

}
