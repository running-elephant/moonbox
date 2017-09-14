/*-
 * <<
 * Moonbox
 * ==
 * Copyright (C) 2016 - 2017 EDP
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

package edp.moonbox.grid.rest

import akka.actor.{ActorRef, ActorSystem}
import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.ServerBinding
import akka.stream.ActorMaterializer
import akka.util.Timeout
import edp.moonbox.common.EdpLogging
import edp.moonbox.grid.rest.routing.Router
import edp.moonbox.grid.rest.services.{ComputeService, DataFetchService, ServiceImpl}

import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future}


class RestServer(host: String,
                 port: Int,
                 val computerRef: ActorRef,
                 val dataGetterRef: ActorRef,
                 actorSystem: ActorSystem) extends Router with ServiceImpl with EdpLogging {

    implicit val system: ActorSystem = actorSystem
	implicit val executionContext: ExecutionContext = system.dispatcher
	override val computer: ComputeService = new ComputeService(computerRef, executionContext, Timeout(FiniteDuration(30, SECONDS)))
	override val dataGetter: DataFetchService = new DataFetchService(dataGetterRef, executionContext, Timeout(FiniteDuration(120, SECONDS)))

    implicit val materializer = ActorMaterializer()
    var bindingFuture: Future[ServerBinding] = _

    def start(): Unit = {
        bindingFuture = Http().bindAndHandle(routes, host, port)
        logInfo(s"Waiting for requests at http://$host:$port/...")
    }

    def stop(): Unit = {
        if (bindingFuture != null) {
            bindingFuture.flatMap(_.unbind())
        }
    }

}

object RestServer {
    def apply(host: String, port: Int, grid: ActorRef, dataGetter: ActorRef, actorSystem: ActorSystem): RestServer =
	    new RestServer(host, port, grid, dataGetter, actorSystem)
}
