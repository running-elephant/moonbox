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

package moonbox.repl

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.stream.{ActorMaterializer, StreamTcpException}
import org.json4s.{DefaultFormats, jackson}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

/**
  *
  *
  */
class RequestSender(host: String, port: Int, mode: QueryMode, timeout: Duration,
                    implicit val akkaSystem: ActorSystem) extends JsonSerializer {

	implicit val materializer = ActorMaterializer()
	implicit val formats = DefaultFormats
	implicit val serialization = jackson.Serialization
	val http = Http(akkaSystem)
	val baseUrl = s"http://$host:$port"

	def send[B](api: String, sentData: => Future[RequestEntity], receivedData: HttpResponse => Future[B]): (Option[String], B) = {
		val uri = baseUrl + api
		val httpResponse: Future[HttpResponse] = sentData.flatMap { entity =>
			http.singleRequest(
				HttpRequest(
					method = HttpMethods.POST,
					uri = Uri(uri),
					entity = entity
				)
			)
		}
		val f = httpResponse.flatMap { response =>
			if (response.status.isSuccess()) {
				for(o <- receivedData(response)) yield (None, o)
			} else {
				Future((Some(response.status.value), null.asInstanceOf[B]))
			}
		}
		try {
			Await.result(f, timeout)
		} catch {
			case e: StreamTcpException =>
				(Some("can't connect to server."), null.asInstanceOf[B])
		}
	}
}
