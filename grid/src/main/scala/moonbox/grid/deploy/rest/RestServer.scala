/*-
 * <<
 * Moonbox
 * ==
 * Copyright (C) 2016 - 2019 EDP
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

package moonbox.grid.deploy.rest

import akka.actor.{ActorRef, ActorSystem}
import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.ServerBinding
import akka.stream.ActorMaterializer
import moonbox.catalog.JdbcCatalog
import moonbox.common.{MbConf, MbLogging}
import moonbox.grid.config._
import moonbox.grid.deploy.rest.routes.AssembleRoutes
import moonbox.grid.deploy.rest.service.workbench.MoonboxConnectionCache

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

class RestServer(host: String, port: Int, conf: MbConf, catalog: JdbcCatalog, masterRef: ActorRef,
                 implicit val akkaSystem: ActorSystem) extends MbLogging {

  private val maxRetries: Int = conf.get(PORT_MAX_RETRIES)
  private val clearConnectionCacheInterval = 10.seconds
  private var bindingFuture: Future[ServerBinding] = _
  private implicit val materializer = ActorMaterializer()

  private def createRoutes(localAddress: String) = {
    new AssembleRoutes(conf, catalog, masterRef).routes // ~
    /*extractClientIP { clientIP =>
      implicit val connectionInfo = ConnectionInfo(localAddress, clientIP.value, ConnectionType.REST)

      pathPrefix("management") {
        path("cluster-info") {
          get {
            complete {
              mbService.clusterInfo()
            }
          }
        } ~
        path("apps-info") {
          get {
            complete {
              mbService.appsInfo()
            }
          }
        }
      } ~
      pathPrefix("service") {
        path("query") {
          post {
            entity(as[SampleInbound]) { in =>
              complete {
                mbService.sample(in.username, in.password, in.sql, in.database)
              }
            }
          }
        } ~
        path("translation") {
          post {
            entity(as[TranslationInbound]) { in =>
              complete {
                mbService.translate(in.username, in.password, in.sql, in.database)
              }
            }
          }
        } ~
        path("verify") {
          post {
            entity(as[VerifyInbound]) { in =>
              complete {
                mbService.verify(in.username, in.password, in.sqls, in.database)
              }
            }
          }
        } ~
        path("tableresources") {
          post {
            entity(as[TableResourceInbound]) { in =>
              complete {
                mbService.resources(in.username, in.password, in.sqls, in.database)
              }
            }
          }
        } ~
        path("schema") {
          post {
            entity(as[SchemaInbound]) { in =>
              complete {
                mbService.schema(in.username, in.password, in.sql, in.database)
              }
            }
          }
        } ~
        path("lineage") {
          post {
            entity(as[LineageInbound]) { in =>
              complete {
                mbService.lineage(in.username, in.password, in.sqls, in.database)
              }
            }
          }
        }
      } ~
      pathPrefix("batch") {
        path("submit") {
          post {
            entity(as[BatchQueryInbound]) { in =>
              complete {
                mbService.batchQuery(in.username, in.password, in.lang, in.sqls, in.config)
              }
            }
          }
        } ~
        path("progress") {
          post {
            entity(as[BatchQueryProgressInbound]) { in =>
              complete {
                mbService.batchQueryProgress(in.username, in.password, in.jobId)
              }
            }
          }
        } ~
        path("cancel") {
          post {
            entity(as[BatchQueryCancelInbound]) { in =>
              complete {
                mbService.batchQueryCancel(in.username, in.password, in.jobId)
              }
            }
          }
        }
      }
    }*/


  }

  def start(): Int = {
    require(port == 0 || (port >= 1024 && port < 65535),
      "rest port should be between 1024 (inclusive) and 65535 (exclusive), or 0 for a random free port.")
    logInfo("Starting rest server ...")
    for (offset <- 0 to maxRetries) {
      val tryPort = if (port == 0) port else port + offset
      try {
        val routes = createRoutes(s"$host:$tryPort")
        bindingFuture = Http().bindAndHandle(routes, host, tryPort)
        val serverBinding: ServerBinding = Await.result(bindingFuture, new FiniteDuration(10, SECONDS))
        logInfo(s"RestServer is listening on ${serverBinding.localAddress.toString}.")

        // schedule to clear workbench console moonbox jdbc connection
        akkaSystem.scheduler.schedule(initialDelay = 0.seconds, interval = clearConnectionCacheInterval)(MoonboxConnectionCache.clearConnection)
        return serverBinding.localAddress.getPort
      } catch {
        case e: Exception =>
          if (offset >= maxRetries) {
            throw e
          }
          if (port == 0) {
            logWarning(s"RestServer could not bind on a random free pot. Attempting again.")
          } else {
            logWarning(s"RestServer could not bind on port $tryPort. " + "" +
              s"Attempting port ${tryPort + 1}")
          }
      }
    }
    throw new Exception(s"Failed to start RestServer on port $port")
  }

  def stop(): Unit = {
    if (bindingFuture != null) {
      bindingFuture.flatMap(_.unbind())
    }
  }

}
