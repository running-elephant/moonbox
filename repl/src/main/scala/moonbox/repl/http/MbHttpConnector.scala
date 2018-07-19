package moonbox.repl.http

import moonbox.common.message._
import moonbox.repl.adapter.{Connector, Utils}
import org.json4s.jackson.Serialization.read

// timeout: XXX seconds
class MbHttpConnector(timeout: Int) extends Connector {
  var client: MbHttpClient = _
  var sessionId: String = _
  var token: String = _
  var isLogin: Boolean = _
  var closed: Boolean = _
  var DEFAULT_FETCH_SIZE = 200

  Runtime.getRuntime.addShutdownHook(new Thread(new Runnable {
    override def run(): Unit = {
      if (isLogin) close()
    }
  }))

  override def prepare(host: String, port: Int, user: String, pwd: String, db: String): Boolean = {
    var flag: Boolean = false
    try {
      client = new MbHttpClient(host, port, timeout * 1000)
      val loginRes = login(user, pwd)
      if (loginRes.error.isEmpty && loginRes.token.isDefined) {
        isLogin = true
        token = loginRes.token.get
        val sessionRes = openSession(token, db)
        if (sessionRes.error.isEmpty && sessionRes.sessionId.isDefined) {
          sessionId = sessionRes.sessionId.get
          flag = true
        } else {
          throw new Exception(sessionRes.error.getOrElse("Open session error ..."))
        }
      } else {
        throw new Exception(loginRes.error.getOrElse("Login error ..."))
      }
      flag
    } catch {
      case e: Exception =>
        println(e.getMessage)
        flag
    }
  }

  override def process(sqls: Seq[String]) = {
    val _query = QueryInbound(sessionId, token, sqls)
    val res = client.post(_query, "/query")
    val resObj = read[QueryOutbound](res)
    resObj.error match {
      case None => showResult(resObj)
      case Some(errMsg) => System.err.println(errMsg)
    }
  }

  override def close() = {
    if (isLogin && token != null) {
      if (sessionId != null) {
        // close session first
        val _closeSession = closeSession(token, sessionId)
        _closeSession.error match {
          case None =>
            println(s"Session closed")
            sessionId = null
          case Some(msg) => println(s"Session closing error: $msg")
        }
      }
      // do logout
      val _logout = logout(token)
      _logout.error match {
        case None =>
          isLogin = false
          println(_logout.message.getOrElse("Logout successfully"))
        case Some(msg) => println(s"Logout error: $msg")
      }
    }
  }

  override def shutdown() = {
    // TODO:
    close()
  }

  private def login(username: String, password: String): LoginOutbound = {
    val _login = LoginInbound(username, password)
    val res = client.post(_login, "/login")
    read[LoginOutbound](res)
  }

  private def logout(token: String): LogoutOutbound = {
    val _logout = LogoutInbound(token)
    val res = client.post(_logout, "/logout")
    read[LogoutOutbound](res)
  }

  private def openSession(token: String, database: String): OpenSessionOutbound = {
    val db = if (database == null || database.length == 0) None else Some(database)
    val _openSession = OpenSessionInbound(token, db)
    val res = client.post(_openSession, "/openSession")
    read[OpenSessionOutbound](res)
  }

  private def closeSession(token: String, sessionId: String): CloseSessionOutbound = {
    val _closeSession = CloseSessionInbound(token, sessionId)
    val res = client.post(_closeSession, "/closeSession")
    read[CloseSessionOutbound](res)
  }

  private def dataFetch(jobId: String, offset: Long, fetchSize: Long): ResultOutbound = {
    val _dataFetch = ResultInbound(token, jobId, offset, fetchSize)
    val res = client.post(_dataFetch, "/result")
    read[ResultOutbound](res)
  }

  private def showResult(queryOutbound: QueryOutbound): Unit = {
    queryOutbound match {
      case QueryOutbound(jobId, None, schema, Some(data), size) =>
        val numShow = max_count
        var dataBuf: Seq[Seq[Any]] = Nil
        val parsedSchema: Seq[String] = if (schema.isDefined) {
          Utils.parseJson(schema.get).map(s => s"${s._1}(${s._2})").toSeq
        } else Nil
        if (numShow <= data.size) {
          dataBuf = data.take(numShow)
        } else {
          var currentSize: Long = data.size
          if (jobId.isDefined && size.isDefined && data.size < size.get) {
            val remainRowNums = if (numShow >= size.get) size.get else numShow - data.size
            while (currentSize < remainRowNums) {
              val fetchSize = math.min(DEFAULT_FETCH_SIZE, remainRowNums - currentSize)
              val fetchResult = dataFetch(jobId.get, currentSize, fetchSize)
              fetchResult match {
                case ResultOutbound(_, None, _, Some(data)) =>
                  dataBuf = dataBuf ++ data
                  currentSize += data.size
                case ResultOutbound(_, Some(errMsg), _, _) =>
                  System.err.println(s"QueryOutbound error: $errMsg")
                  currentSize = size.get // break the while cycle
                case _ =>
                  System.err.println("QueryOutbound mismatch")
                  currentSize = size.get // break the while cycle
              }
            }
          } else {
            dataBuf = dataBuf ++ data
          }
        }
        print(Utils.showString(dataBuf, parsedSchema, numShow, truncate))
      case QueryOutbound(_, None, _, _, _) =>
      case _ => System.err.println("QueryOutbound mismatch")
    }
  }

  override def cancel(): Unit = {
      val _cancelInbound = CancelInbound(token, sessionId)
      client.post(_cancelInbound, "/cancel")
  }
}
