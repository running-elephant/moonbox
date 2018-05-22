package moonbox.repl.http

import moonbox.common.message._
import moonbox.repl.adapter.{Connector, Utils}

class MbHttpConnector extends Connector {
  var client: MbHttpClient = _
  var sessionId: String = _
  var token: String = _
  var isLogin: Boolean = _
  var closed: Boolean = _

  Runtime.getRuntime.addShutdownHook(new Thread(new Runnable {
    override def run(): Unit = {
      if (isLogin) close()
    }
  }))

  override def prepare(host: String, port: Int, user: String, pwd: String, db: String): Boolean = {
    var flag: Boolean = false
    try {
      client = new MbHttpClient(host, port)
      val loginRes = login(user, pwd)
      if (loginRes.error.isEmpty && loginRes.token.isDefined) {
        isLogin = true
        token = loginRes.token.get
        val sessionRes = openSession(token, db)
        if (sessionRes.error.isEmpty && sessionRes.sessionId.isDefined) {
          sessionId = sessionRes.sessionId.get
          flag = true
        } else {
          throw new Exception("Open session error ...")
        }
      } else {
        throw new Exception("Login error ...")
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
    val resObj = SerializationUtils.fromJson[QueryOutbound](res, classOf[QueryOutbound])
    resObj.error match {
      case None => showResult(resObj)
      case Some(errMsg) => System.err.println(errMsg)
    }
  }

  override def close() = {
    if (isLogin && token != null) {
      if (sessionId != null){
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
    SerializationUtils.fromJson[LoginOutbound](res, classOf[LoginOutbound])
  }

  private def logout(token: String): LogoutOutbound = {
    val _logout = LogoutInbound(token)
    val res = client.post(_logout, "/logout")
    SerializationUtils.fromJson[LogoutOutbound](res, classOf[LogoutOutbound])
  }

  private def openSession(token: String, database: String): OpenSessionOutbound = {
    val db = if (database == null || database.length == 0) None else Some(database)
    val _openSession = OpenSessionInbound(token, db)
    val res = client.post(_openSession, "/openSession")
    SerializationUtils.fromJson[OpenSessionOutbound](res, classOf[OpenSessionOutbound])
  }

  private def closeSession(token: String, sessionId: String): CloseSessionOutbound ={
    val _closeSession = CloseSessionInbound(token, sessionId)
    val res = client.post(_closeSession, "/closeSession")
    SerializationUtils.fromJson[CloseSessionOutbound](res, classOf[CloseSessionOutbound])
  }

  private def showResult(queryOutbound: QueryOutbound): Unit = {
    queryOutbound match {
      case QueryOutbound(_, None, schema, Some(data), _) =>
        if (schema.isDefined) {
          Utils.parseJson(schema.get).map(s => s"${s._1}(${s._2})").mkString(" | ")
        }
        data.foreach(row => println(row.mkString(" | ")))
      case _ => System.err.println("QueryOutbound mismatch")
    }
  }
}
