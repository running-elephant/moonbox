package moonbox.grid.deploy.transport.server

import java.util.concurrent.ConcurrentHashMap

import io.netty.channel.{Channel, ChannelHandlerContext, ChannelInboundHandlerAdapter}
import io.netty.util.ReferenceCountUtil
import moonbox.common.MbLogging
import moonbox.common.message._
import moonbox.grid.deploy.MbService

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

class JdbcServerHandler(channel2SessionIdAndToken: ConcurrentHashMap[Channel, (String, String)], mbService: MbService) extends ChannelInboundHandlerAdapter with MbLogging {

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
    if (channel2SessionIdAndToken.containsKey(ctx.channel())) {
      val sessionId = channel2SessionIdAndToken.get(ctx.channel())._1
      val token = channel2SessionIdAndToken.get(ctx.channel())._2
      logInfo(s"Closing session: sessionId=$sessionId ...")
      mbService.closeSession(token, sessionId).onComplete {
        case Success(v) =>
          if (v.error.isDefined) {
            logWarning(s"Close session success with error message: ${v.error.get}")
          } else {
            logInfo("Session closed, logging out ...")
            mbService.logout(token).onComplete {
              case Success(v) =>
                if (v.error.isDefined) {
                  logWarning(s"Token($token) logout succeed with error message: ${v.error.get}")
                } else {
                  logInfo(s"Logout succeed: Token($token)")
                }
              case Failure(e) =>
                logError(s"Logout failed: Token($token)")
                throw new Exception(s"Logout failed: Token($token)")
            }
          }
        case Failure(e) =>
          logError(s"Close session SessionId($sessionId) failed: ${e.getMessage}")
          throw new Exception(s"Close session SessionId($sessionId) failed: ${e.getMessage}")
      }
      channel2SessionIdAndToken.remove(ctx.channel())
    }
    super.channelInactive(ctx)
  }

  override def channelUnregistered(ctx: ChannelHandlerContext) = {
    super.channelUnregistered(ctx)
  }

  def handleMessage(ctx: ChannelHandlerContext, msg: Any): Unit = {
    msg match {
      case cancel: JdbcCancelInbound =>
      // TODO: 1. invoke job cancel interface from service;
      // TODO: 2. send the canceled message id back and set the corresponding promise to failure
      val sessionId = channel2SessionIdAndToken.get(ctx.channel())._1
      val token = channel2SessionIdAndToken.get(ctx.channel())._2
      mbService.jobCancel(token, sessionId).onComplete {
        case Success(CancelOutbound(_, error)) =>
          error match {
            case Some(err) =>
              logInfo(s"Query cancel failed, sessionId=$sessionId, since $err")
              ctx.writeAndFlush(JdbcCancelOutbound(cancel.messageId, error, Some(false)))
            case None =>
              logInfo(s"Query cancel successfully, sessionId=$sessionId.")
              ctx.writeAndFlush(JdbcCancelOutbound(cancel.messageId, None, Some(true)))
          }
        case Failure(e) => ctx.writeAndFlush(JdbcCancelOutbound(cancel.messageId, Some(e.getMessage), Some(false)))
      }
      case echo: EchoInbound =>
        logInfo(s"Received message: $echo")
        ctx.writeAndFlush(EchoOutbound(echo.messageId, Some(echo.content)))
      case login: JdbcLoginInbound =>
        logInfo(s"Received message: ${login.copy(password = "***")}")
        mbService.login(login.user, login.password).onComplete {
          case Success(Some(token)) =>
            logInfo(s"User: ${login.user} login succeed: Token=$token")
            //open session
            logInfo(s"Opening session for user: ${login.user}")
            mbService.openSession(token, Some(login.database)).onComplete {
              case Success(v) =>
                if (v.error.isEmpty && v.sessionId.isDefined) {
                  channel2SessionIdAndToken.put(ctx.channel(), (v.sessionId.get, token))
                  logInfo(s"Open session succeed, sessionId=${v.sessionId}")
                  ctx.writeAndFlush(JdbcLoginOutbound(login.messageId, None, Some(token)))
                } else {
                  logWarning(s"Open session succeed with error message: ${v.error}")
                  ctx.writeAndFlush(JdbcLoginOutbound(login.messageId, v.error, Some(token)))
                }
              case Failure(e) =>
                logError(s"Open session failed: ${e.getMessage}")
                ctx.writeAndFlush(JdbcLoginOutbound(login.messageId, Some(e.getMessage), Some("open session failed")))
            }
          case Success(None) =>
            logWarning(s"User(${login.user}) login success with Token($None)")
            ctx.writeAndFlush(JdbcLoginOutbound(login.messageId, err = Some("Login failed"), None))
          case Failure(e) =>
            logError(s"User(${login.user}) login failed: ${e.getMessage}")
            ctx.writeAndFlush(JdbcLoginOutbound(login.messageId, Some(e.getMessage), None))
        }
      case logout: JdbcLogoutInbound =>
        logInfo(s"Received message: $logout")
        val sessionId = channel2SessionIdAndToken.get(ctx.channel())._1
        val token = channel2SessionIdAndToken.get(ctx.channel())._2
        logInfo("Closing session ...")
        mbService.closeSession(token, sessionId).onComplete {
          case Success(v) =>
            if (v.error.isDefined) {
              logInfo(s"Close session error: ${v.error.get}")
              ctx.writeAndFlush(JdbcLogoutOutbound(logout.messageId, v.error, Some("Close session error")))
            } else {
              logInfo("Session closed, logging out ...")
              mbService.logout(token).onComplete {
                case Success(v) =>
                  if (v.error.isDefined) {
                    logInfo(s"Logout error: ${v.error.get}")
                    ctx.writeAndFlush(JdbcLogoutOutbound(logout.messageId, v.error, Some(s"Logout error: ${v.error.get}")))
                  } else {
                    logInfo("Logout succeed")
                    channel2SessionIdAndToken.remove(ctx.channel())
                    ctx.writeAndFlush(JdbcLogoutOutbound(logout.messageId, v.error, Some("Logout succeed")))
                  }
                case Failure(e) =>
                  logError("Logout failed")
                  ctx.writeAndFlush(JdbcLogoutOutbound(logout.messageId, Some(e.getMessage), Some(s"Logout error: ${v.error.get}")))
              }
            }
          case Failure(e) =>
            logError(s"Close session error: $e")
            ctx.writeAndFlush(JdbcLogoutOutbound(logout.messageId, Some(e.getMessage), Some("Close session error")))
        }
      case query: JdbcQueryInbound =>
        logInfo(s"Received message: $query")
        if (channel2SessionIdAndToken.containsKey(ctx.channel())) {
          val sessionId = channel2SessionIdAndToken.get(ctx.channel())._1
          val token = channel2SessionIdAndToken.get(ctx.channel())._2
          val sqls: Seq[String] = splitSql(query.sql, ';')
          // TODO: query can be canceled
          mbService.jobQuery(token, sessionId, sqls, query.fetchSize).onComplete {
            case Success(v) =>
              if (v.error.isEmpty) {
                logInfo(s"SQLs(${sqls.mkString("; ")}) query succeed")
                // 1. return JdbcQueryOutbound  2.return DataFetchOutbound,  according to the result data size
                if (v.size.isDefined && v.data.isDefined && v.data.get.size < v.size.get) {
                  val fetchState = DataFetchState(query.messageId, v.jobId.orNull, 0, v.data.get.size, v.size.get)
                  ctx.writeAndFlush(DataFetchOutbound(fetchState, v.error, v.data, v.schema))
                } else if (v.data.isDefined && v.schema.isEmpty) {
                  //for some queries which have data but have no schema: e.g. "show tables; show databases; desc table aaa; desc user sally"
                  val schema: Option[String] = {
                    val data1 = v.data.get
                    if (data1.nonEmpty) {
                      val firstRow = data1.head
                      if (firstRow != null && firstRow.nonEmpty) {
                        val buf = collection.mutable.ArrayBuffer.empty[String]
                        firstRow.indices.foreach { i =>
                          buf += s"{name: column_$i, type: string, nullable: true}"
                        }
                        Some(buf.mkString("{type: struct, fields: [", ", ", "]}"))
                      } else {
                        None
                      }
                    } else {
                      None
                    }
                  }
                  ctx.writeAndFlush(JdbcQueryOutbound(query.messageId, v.error, v.data, schema, v.size))
                } else {
                  ctx.writeAndFlush(JdbcQueryOutbound(query.messageId, v.error, v.data, v.schema, v.size))
                }
              } else {
                logInfo(s"SQLs(${sqls.mkString("; ")}) query succeed with error: ${v.error.orNull}")
                ctx.writeAndFlush(JdbcQueryOutbound(query.messageId, v.error, v.data, v.schema, v.size))
              }
            case Failure(e) =>
              logError(s"SQLs(${sqls.mkString("; ")}) query failed: ${e.getMessage}")
              ctx.writeAndFlush(JdbcQueryOutbound(query.messageId, Some(e.getMessage), None, None, None))
          }
        } else {
          logInfo(s"User have not login yet")
          ctx.writeAndFlush(JdbcQueryOutbound(query.messageId, Some("User did not login, login first"), None, None, None))
        }
      case dataFetch: DataFetchInbound =>
        logInfo(s"Received message: $dataFetch")
        doDataFetch(ctx, dataFetch)
      case oneWay: OneWayMessage =>
        logInfo(s"Received message: $oneWay")
      case _ => logInfo("Received unsupported message, do noting!")
    }
  }

  private def doDataFetch(ctx: ChannelHandlerContext, inbound: DataFetchInbound): Unit = {
    if (channel2SessionIdAndToken.containsKey(ctx.channel())) {
      val token = channel2SessionIdAndToken.get(ctx.channel())._2
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
      mbService.jobResult(token, inbound.dataFetchState.jobId, offset, requestSize).onComplete {
        case Success(v) =>
          if (v.error.isDefined) {
            logInfo(s"Do dataFetch error: ${v.error.get}")
            // Return error to client, or retry several times delivered to client
            ctx.writeAndFlush(DataFetchOutbound(inbound.dataFetchState, v.error, None, None))
          } else {
            val dataFetchState = inbound.dataFetchState.copy(fetchSize = v.data.get.size)
            ctx.writeAndFlush(DataFetchOutbound(dataFetchState, None, v.data, v.schema))
          }
        case Failure(e) =>
          logError(s"Do dataFetch failed: ${e.getMessage}")
          ctx.writeAndFlush(DataFetchOutbound(inbound.dataFetchState, Some(e.getMessage), None, None))
      }
    } else {
      logInfo("Do dataFetch error, login first")
      ctx.writeAndFlush(DataFetchOutbound(inbound.dataFetchState, Some("Do dataFetch error, login first"), None, None))
    }
  }

  private def splitSql(sql: String, splitter: Char): Seq[String] = {
    val stack = new mutable.Stack[Char]()
    val splitIndex = new ArrayBuffer[Int]()
    for ((char, idx) <- sql.toCharArray.zipWithIndex) {
      if (char == splitter) {
        if (stack.isEmpty) splitIndex += idx
      }
      if (char == '(') stack.push('(')
      if (char == ')') stack.pop()
    }
    splits(sql, splitIndex.toArray, 0).map(_.stripPrefix(splitter.toString).trim).filter(_.length > 0)
  }

  private def splits(sql: String, idxs: Array[Int], offset: Int): Seq[String] = {
    if (idxs.nonEmpty) {
      val head = idxs.head
      val (h, t) = sql.splitAt(head - offset)
      h +: splits(t, idxs.tail, head)
    } else sql :: Nil
  }

}
