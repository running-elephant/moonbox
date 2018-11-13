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

package moonbox.repl.connector.rest

import moonbox.protocol.client._
import moonbox.repl.Utils
import moonbox.repl.connector.{ConnectionState, ConnectionType, Connector, RuntimeMode}
import org.json4s.jackson.Serialization.read

import scala.collection.mutable.ArrayBuffer

// timeout: XXX seconds
class HttpConnector(_timeout: Int, val isLocal: Boolean, nextFetchSize: Int) extends Connector {
  var _client: HttpClient = _
  var _sessionId: String = _
  var _token: String = _
  var _connectionState: ConnectionState = _
  @volatile var _closed: Boolean = _

  Runtime.getRuntime.addShutdownHook(new Thread(new Runnable {
    override def run(): Unit = {
      if (!_closed) close()
    }
  }))

  override def prepare(host: String, port: Int, user: String, pwd: String, db: String): Boolean = {
    try {
      initClient(host, port, Utils.secondToMs(_timeout))
      val requestAccessOutbound = requestAccess(login(user, pwd).token.get, isLocal)
      val hp = requestAccessOutbound.address.get.split(":").map(_.trim)
      try {
        logout(_token)
      } catch {
        case e: Exception => Console.err.println(s"WARNING: Logout before requestAccess failed: ${e.getMessage}")
      }
      initClient(hp(0), hp(1).toInt, Utils.secondToMs(_timeout))
      login(user, pwd)
      openSession(_token, db, isLocal)
      _connectionState =
        ConnectionState(host = hp(0),
          port = hp(1).toInt,
          connectionType = ConnectionType.REST,
          runtimeMode = if (isLocal) RuntimeMode.LOCAL else RuntimeMode.CLUSTER,
          username = user,
          token = _token,
          sessionId = _sessionId,
          timeout = Utils.secondToMs(_timeout),
          fetchSize = nextFetchSize,
          maxColumnLength = this.truncate,
          maxRowsShow = this.max_count
        )
      true
    } catch {
      case e: Exception =>
        println(e.getMessage)
        false
    }
  }

  override def process(sqls: Seq[String]) = {
    val res = _client.post(InteractiveQueryInbound(_token, _sessionId, sqls, fetchSize = 1), "/query")
    val resObj = read[InteractiveQueryOutbound](res)
    resObj.error match {
      case None => showResult(resObj)
      case Some(errMsg) => System.err.println(errMsg)
    }
  }

  override def close() = {
    if (!_closed && _token != null) {
      if (_sessionId != null) {
        // close session first
        closeSession(_token, _sessionId)
      }
      // do logout
      logout(_token)
    }
    _closed = true
  }

  override def shutdown() = {
    // TODO:
    close()
  }

  /* timeout: XXX ms */
  private def initClient(host: String, port: Int, timeout: Int): Unit = {
    _client = new HttpClient(host, port, timeout)
  }

  private def requestAccess(token: String, _islocal: Boolean): RequestAccessOutbound = {
    val res = _client.post(RequestAccessInbound(Some(token), _islocal), "/requestAccess")
    read[RequestAccessOutbound](res) match {
      case r@RequestAccessOutbound(Some(_), None) => r
      case other => throw new Exception(s"RequestAccess failed: address=${other.address}, error=${other.error}")
    }
  }

  private def login(username: String, password: String): LoginOutbound = {
    val res = _client.post(LoginInbound(username, password), "/login")
    read[LoginOutbound](res) match {
      case r@LoginOutbound(Some(token), None) =>
        _token = token
        r
      case other => throw new Exception(s"Login failed: token=${other.token}, error=${other.error}")
    }
  }

  private def logout(token: String): LogoutOutbound = {
    val res = _client.post(LogoutInbound(token), "/logout")
    read[LogoutOutbound](res) match {
      case r@LogoutOutbound(None) => r
      case other => throw new Exception(s"Logout failed: error=${other.error}")
    }
  }

  private def openSession(token: String, database: String, _isLocal: Boolean): OpenSessionOutbound = {
    val db = if (database == null || database.length == 0) None else Some(database)
    val res = _client.post(OpenSessionInbound(token, db, _isLocal), "/openSession")
    read[OpenSessionOutbound](res) match {
      case r@OpenSessionOutbound(Some(sessionId), None) =>
        _sessionId = sessionId
        r
      case other => throw new Exception(s"Open session failed: sessionId=${other.sessionId}, error=${other.error}")
    }
  }

  private def closeSession(token: String, sessionId: String): CloseSessionOutbound = {
    val res = _client.post(CloseSessionInbound(token, sessionId), "/closeSession")
    read[CloseSessionOutbound](res) match {
      case r@CloseSessionOutbound(None) => r
      case other => throw new Exception(s"Close session failed: error=${other.error}")
    }
  }

  // TODO: api name
  private def fetchNextResult(token: String, sessionId: String, cursor: String, fetchSize: Long): InteractiveNextResultOutbound = {
    val res = _client.post(InteractiveNextResultInbound(token, sessionId, cursor, fetchSize), "/nextResult")
    read[InteractiveNextResultOutbound](res) match {
      case r@InteractiveNextResultOutbound(None, Some(_)) => r
      case other => throw new Exception(s"Fetch next result failed: error=${other.error}")
    }
  }

  private def showResult(queryOutbound: InteractiveQueryOutbound): Unit = {
    queryOutbound match {
      case InteractiveQueryOutbound(None, true, Some(d)) =>
        val dataBuf = ArrayBuffer.empty[Seq[Any]]
        val data = d.data
        var numShowed = data.size
        var hasNext = d.hasNext
        val parsedSchema: Seq[String] = Utils.parseJson(d.schema).map(s => s"${s._1}(${s._2})").toSeq
        /* print data */
        dataBuf ++= data
//        print(Utils.showString(data.take(numShowed), parsedSchema, _connectionState.maxRowsShow, _connectionState.maxColumnLength, showPromote = !d.hasNext || numShowed >= _connectionState.maxRowsShow))
        while (numShowed < _connectionState.maxRowsShow && hasNext) {
          val fetchSize = math.min(_connectionState.fetchSize, _connectionState.maxRowsShow - numShowed)
          val outbound = fetchNextResult(_token, _sessionId, d.cursor, fetchSize)
          val dataToShow = outbound.data.get.data
          hasNext = outbound.data.get.hasNext
          numShowed += dataToShow.size
          dataBuf ++= dataToShow
        }
//        val promote = if (numShowed < _connectionState.maxRowsShow && hasNext) false else true
        print(Utils.showString(dataBuf, parsedSchema, _connectionState.maxRowsShow, _connectionState.maxColumnLength))
      case InteractiveQueryOutbound(None, false, _) => /* no-op */
      case InteractiveQueryOutbound(error, _, _) => throw new Exception(s"SQL query failed: error=$error")
      case _ => throw new Exception(s"SQL query failed.")
    }
  }

  override def cancel(): Unit = {
    val cancelInbound = CancelQueryInbound(_token, jobId = None, sessionId = Some(_sessionId))
    _client.post(cancelInbound, "/cancel")
  }

  override def connectionState: ConnectionState = _connectionState
}
