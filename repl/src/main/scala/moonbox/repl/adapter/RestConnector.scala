///*-
// * <<
// * Moonbox
// * ==
// * Copyright (C) 2016 - 2018 EDP
// * ==
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *      http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// * >>
// */
//
//package moonbox.repl.adapter
//
//import akka.actor.ActorSystem
//import akka.http.scaladsl.marshalling.Marshal
//import akka.http.scaladsl.model.RequestEntity
//import akka.http.scaladsl.unmarshalling.Unmarshal
//import akka.stream.ActorMaterializer
//import com.typesafe.config.ConfigFactory
//import moonbox.common.message._
//import moonbox.repl.adapter.Utils._
//import moonbox.repl.{Batch, JsonSerializer, QueryMode, RequestSender}
//import org.json4s.{DefaultFormats, jackson}
//
//import scala.collection.JavaConversions._
//import scala.concurrent.ExecutionContext.Implicits.global
//import scala.concurrent.duration.{FiniteDuration, SECONDS}
//import scala.language.implicitConversions
//
//class RestConnector(timeout: Int) extends Connector with JsonSerializer  {
//
//  var mode: QueryMode = Batch
//  var isLogin = false
//  implicit val formats = DefaultFormats
//  implicit val serialization = jackson.Serialization
//
//  implicit val akkaSystem: ActorSystem = ActorSystem("Client",
//    ConfigFactory.load(ConfigFactory.parseMap(Map[String, String](
//      "akka.http.client.idle-timeout" -> s"$timeout s",
//      "akka.http.host-connection-pool.client.idle-timeout" -> s"$timeout s"
//    )).withFallback(ConfigFactory.load())))
//  implicit val materializer:ActorMaterializer = ActorMaterializer()
//
//  Runtime.getRuntime.addShutdownHook(new Thread(new Runnable {
//    override def run(): Unit = {
//      if (isLogin) doLogout()
//      akkaSystem.terminate()
//    }
//  }))
//  var sender: RequestSender = _
//  var token: String = _
//  var sessionId: String = _
//
//  override def prepare(host: String, port:Int, user: String, pwd: String, db: String): Boolean = {
//    sender = new RequestSender(host, port, mode, new FiniteDuration(timeout + 3, SECONDS), akkaSystem)
//    try {
//      if (doLogin(user, pwd, db)) {
//        registerSession(db)
//      }
//      true
//    }catch{
//      case e: Exception => e.printStackTrace
//        false
//    }
//  }
//
//  override def process(sqls: Seq[String]): Unit = {
//    val (err, res) = sender.send("/query", {
//      Marshal(QueryInbound(sessionId, token, sqls)).to[RequestEntity]
//    }, {
//      response => Unmarshal(response).to[QueryOutbound]
//    })
//    err match {
//      case Some(message) => System.err.println(message)
//      case None => res match {
//        case QueryOutbound(_, Some(message), _, _, _) => System.err.println(message)
//        case QueryOutbound(_, None, schema, data, _) =>
//          if (schema.isDefined) {
//            val array: Array[(String, String, Boolean)] = parseJson(schema.get)
//            println(array.map(elem=> s"${elem._1}(${elem._2})").mkString(" | "))
//          }
//
//          if (data.isDefined) {
//            data.get.foreach(row =>
//              println(row.mkString(" | "))
//            )
//          }
//
//      }
//    }
//  }
//
//
//  override def close(): Unit = {
//    doLogout()
//  }
//
//
//  override def shutdown(): Unit = {
//    doLogout()
//    akkaSystem.terminate()
//  }
//
//  private def doLogin(user: String, pwd: String, db: String): Boolean = {
//    val (err, res) = sender.send("/login", {
//      Marshal(LoginInbound(user, pwd))
//        .to[RequestEntity]
//    }, {
//      response => Unmarshal(response).to[LoginOutbound]
//    })
//    if (err.isDefined) { // system error, retry
//      System.out.println(err.get)
//      false
//    } else {
//      if (res.error.isDefined) { // user and password error
//        System.out.println(res.error.get)
//        false
//      } else { // login success
//        token = res.token.get
//        isLogin = true
//        true
//      }
//    }
//  }
//
//
//  private def doLogout(): Unit = {
//    sender.send("/logout", {
//      Marshal(LogoutInbound(token)).to[RequestEntity]
//    }, {
//      response => Unmarshal(response).to[LogoutOutbound]})
//    isLogin = false
//    System.out.println("logout successfully.")
//  }
//
//
//  private def registerSession(db: String): Unit = {
//    val (err, sess) = sender.send("/openSession", {
//      Marshal(OpenSessionInbound(token, Some(db))).to[RequestEntity]
//    }, {
//      response => Unmarshal(response).to[OpenSessionOutbound]
//    })
//    if (err.isDefined) {
//      System.err.println(err.get)
//    } else {
//      if (sess.error.isDefined) {
//        System.err.println(sess.error.get)
//      } else {
//        this.sessionId = sess.sessionId.get
//      }
//
//    }
//  }
//
//  override def cancel(): Unit = {
//      val (err, cancel) = sender.send("/cancel", {
//          Marshal(CancelInbound(token, sessionId)).to[RequestEntity]
//      }, {
//          response => Unmarshal(response).to[CancelOutbound]
//      })
//      if (err.isDefined) {
//          System.err.println(err.get)
//      } else {
//          if (cancel.error.isDefined) {
//              System.err.println(cancel.error.get)
//          }
//      }
//  }
//}
