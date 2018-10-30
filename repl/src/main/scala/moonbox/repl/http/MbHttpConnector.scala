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

package moonbox.repl.http

import moonbox.protocol.client._
import moonbox.repl.adapter.{Connector, Utils}
import org.json4s.jackson.Serialization.read

// timeout: XXX seconds
class MbHttpConnector(timeout: Int, val isLocal: Boolean) extends Connector {
  var client: MbHttpClient = _
  var sessionId: String = _
  var token: String = _
  var closed: Boolean = _
  var DEFAULT_FETCH_SIZE = 200

  Runtime.getRuntime.addShutdownHook(new Thread(new Runnable {
    override def run(): Unit = {
      if (!closed) close()
    }
  }))

  override def prepare(host: String, port: Int, user: String, pwd: String, db: String): Boolean = {
    try {
      client = new MbHttpClient(host, port, timeout * 1000)
      val requestAccessOutbound = requestAccess(login(user, pwd).token.get, isLocal)
      val hp = requestAccessOutbound.address.get.split(":").map(_.trim)
      try {
        logout(token)
      } catch {
        case e: Exception => Console.err.println(s"WARNING: Logout before requestAccess failed: ${e.getMessage}")
      }
      client = new MbHttpClient(hp(0), hp(1).toInt, timeout * 1000)
      token = login(user, pwd).token.get
      sessionId = openSession(token, db, isLocal).sessionId.get
      true
    } catch {
      case e: Exception =>
        println(e.getMessage)
        false
    }
  }

  override def process(sqls: Seq[String]) = {
    val _query = InteractiveQueryInbound(token, sessionId, sqls)
    val res = client.post(_query, "/query")
    val resObj = read[InteractiveQueryOutbound](res)
    resObj.error match {
      case None => showResult(resObj)
      case Some(errMsg) => System.err.println(errMsg)
    }
  }

  override def close() = {
    if (!closed && token != null) {
      if (sessionId != null) {
        // close session first
        val _closeSession = closeSession(token, sessionId)
        _closeSession.error match {
          case None =>
            println(s"Session closed")
            sessionId = null
          case Some(err) => println(s"Close session failed: error=$err")
        }
      }
      // do logout
      val _logout = logout(token)
      _logout.error match {
        case None =>
          closed = true
          println("Logout successfully")
        case Some(err) => println(s"Logout Failed: error=$err")
      }
    }
  }

  override def shutdown() = {
    // TODO:
    close()
  }

  private def requestAccess(token: String, _islocal: Boolean): RequestAccessOutbound = {
    val res = client.post(RequestAccessInbound(Some(token), _islocal), "/requestAccess")
    read[RequestAccessOutbound](res) match {
      case r@RequestAccessOutbound(Some(_), None) => r
      case other => throw new Exception(s"RequestAccess failed: address=${other.address}, error=${other.error}")
    }
  }

  private def login(username: String, password: String): LoginOutbound = {
    val _login = LoginInbound(username, password)
    val res = client.post(_login, "/login")
    read[LoginOutbound](res) match {
      case r@LoginOutbound(Some(_), None) => r
      case other => throw new Exception(s"Login failed: token=${other.token}, error=${other.error}")
    }
  }

  private def logout(token: String): LogoutOutbound = {
    val _logout = LogoutInbound(token)
    val res = client.post(_logout, "/logout")
    read[LogoutOutbound](res) match {
      case r@LogoutOutbound(None) => r
      case other => throw new Exception(s"Logout failed: error=${other.error}")
    }
  }

  private def openSession(token: String, database: String, _isLocal: Boolean): OpenSessionOutbound = {
    val db = if (database == null || database.length == 0) None else Some(database)
    val _openSession = OpenSessionInbound(token, db, _isLocal)
    val res = client.post(_openSession, "/openSession")
    read[OpenSessionOutbound](res) match {
      case r@OpenSessionOutbound(Some(_), None) => r
      case other => throw new Exception(s"Open session failed: sessionId=${other.sessionId}, error=${other.error}")
    }
  }

  private def closeSession(token: String, sessionId: String): CloseSessionOutbound = {
    val _closeSession = CloseSessionInbound(token, sessionId)
    val res = client.post(_closeSession, "/closeSession")
    read[CloseSessionOutbound](res) match {
      case r@CloseSessionOutbound(None) => r
      case other => throw new Exception(s"Close session failed: error=${other.error}")
    }
  }

  // TODO: api name
  private def fetchNextResult(token: String, sessionId: String, cursor: String, fetchSize: Long): InteractiveNextResultOutbound = {
    val res = client.post(InteractiveNextResultInbound(token, sessionId, cursor, fetchSize), "/nextResult")
    read[InteractiveNextResultOutbound](res) match {
      case r@InteractiveNextResultOutbound(None, Some(_)) => r
      case other => throw new Exception(s"Fetch next result failed: error=${other.error}")
    }
  }

  private def showResult(queryOutbound: InteractiveQueryOutbound): Unit = {
    queryOutbound match {
      case InteractiveQueryOutbound(None, true, Some(d)) =>
        val data = d.data
        var numShowed = data.size
        val parsedSchema: Seq[String] = Utils.parseJson(d.schema).map(s => s"${s._1}(${s._2})").toSeq
        /* print data */
        print(Utils.showString(data.take(numShowed), parsedSchema, max_count, truncate, showPromote = !d.hasNext))
        while (numShowed < max_count && d.hasNext) {
          val fetchSize = math.min(DEFAULT_FETCH_SIZE, max_count - numShowed)
          val outbound = fetchNextResult(token, sessionId, d.cursor, fetchSize)
          val dataToShow = outbound.data.get.data
          val promote = if (fetchSize == DEFAULT_FETCH_SIZE) false else true
          print(Utils.showString(dataToShow, parsedSchema, max_count, truncate, showPromote = promote, showSchema = false))
          numShowed += dataToShow.size
        }
      case InteractiveQueryOutbound(None, false, _) => /* no-op */
      case InteractiveQueryOutbound(error, _, _) => throw new Exception(s"SQL query failed: error=$error")
      case _ => throw new Exception(s"SQL query failed.")
    }
  }

  override def cancel(): Unit = {
    val _cancelInbound = CancelQueryInbound(token, sessionId)
    client.post(_cancelInbound, "/cancel")
  }
}
