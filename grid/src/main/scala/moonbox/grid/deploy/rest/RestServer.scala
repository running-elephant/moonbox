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

package moonbox.grid.deploy.rest

import java.math.BigInteger
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.ServerBinding
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import de.heikoseeberger.akkahttpjson4s.Json4sSupport.ShouldWritePretty
import moonbox.common.{MbConf, MbLogging}
import moonbox.grid.config._
import moonbox.grid.deploy.{ConnectionInfo, ConnectionType, MbService}
import moonbox.protocol.client._
import org.json4s.jackson.Serialization
import org.json4s.{CustomSerializer, DefaultFormats, JInt, JString, Serializer}

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

class RestServer(host: String, port: Int, conf: MbConf, service: MbService,
	implicit val akkaSystem: ActorSystem) extends JsonSerializer with MbLogging {

	private val maxRetries: Int = conf.get(PORT_MAX_RETRIES)
	private var bindingFuture: Future[ServerBinding] = _
	private implicit val materializer = ActorMaterializer()
	private implicit val formats = DefaultFormats ++ customFormats
	private implicit val serialization = Serialization
	private implicit val shouldWritePretty = ShouldWritePretty.True

	private def customFormats: Traversable[Serializer[_]] = {
		Seq(
			new CustomSerializer[java.sql.Date](_ => (
					{ case JInt(s) => new java.sql.Date(s.longValue()) },
					{ case x: java.sql.Date => JString(x.toString) }
				)
			),
			new CustomSerializer[java.math.BigDecimal](_ => (
					{ case JString(s) => new java.math.BigDecimal(s) },
					{ case b: java.math.BigDecimal => JString(b.toString) }
				)
			),
			new CustomSerializer[java.math.BigInteger](_ => (
					{ case JString(s) => new BigInteger(s) },
					{ case b: java.math.BigInteger => JString(b.toString) }
				)
			)
		)
	}

	private def createRoutes(localAddress: String) = {
		extractClientIP { clientIP =>
			implicit val connectionInfo = ConnectionInfo(localAddress, clientIP.value, ConnectionType.REST)
			path("login") {
				post {
					entity(as[LoginInbound]) { in =>
						complete {
							service.login(in.username, in.password)
						}
					}
				}
			} ~
			path("logout") {
				post {
					entity(as[LogoutInbound]) { in =>
						complete {
							service.logout(in.token)
						}
					}
				}
			} ~
			path("openSession") {
				post {
					entity(as[OpenSessionInbound]) { in =>
						complete {
							service.openSession(in.token, in.database, in.config)
						}
					}
				}
			} ~
			path("closeSession") {
				post {
					entity(as[CloseSessionInbound]) { in =>
						complete {
							service.closeSession(in.token, in.sessionId)
						}
					}
				}
			} ~
			path("query") {
				post {
					entity(as[InteractiveQueryInbound]) { in =>
						complete {
							service.interactiveQuery(in.token, in.sessionId, in.sqls, in.fetchSize, in.maxRows)
						}
					}
				}
			} ~
			path("nextResult") {
				post {
					entity(as[InteractiveNextResultInbound]) { in =>
						complete {
							service.interactiveNextResult(in.token.get, in.sessionId)
						}
					}
				}
			} ~
			path("submit") {
				post {
					entity(as[BatchQueryInbound]) { in =>
						complete {
							service.batchQuery(in.token, in.sqls, in.config)
						}
					}
				}
			} ~
			path("progress") {
				post {
					entity(as[BatchQueryProgressInbound]) { in =>
						complete {
							service.batchQueryProgress(in.token, in.jobId)
						}
					}
				}
			} ~
			path("cancel") {
				post {
					entity(as[CancelQueryInbound]) { in =>
						complete {
							if (in.sessionId.isDefined) {
								service.interactiveQueryCancel(in.token, in.sessionId.get)
							} else {
								service.batchQueryCancel(in.token, jobId = in.jobId.get)
							}

						}
					}
				}
			} /*~
			path("requestAccess") {
				post {
					entity(as[RequestAccessInbound]) { in =>
						complete {
							service.requestAccess(in.token.getOrElse(""), in.isLocal, ConnectionType.REST)
						}
					}
				}
			} ~
			path("addYarnApp") {
				post {
					entity(as[AddAppInbound]) { in =>
						complete {
							service.yarnAppAdd(in.username, in.config)
						}
					}
				}
			} ~
			path("removeYarnApp") {
				post {
					entity(as[RemoveAppInbound]) { in =>
						complete {
							service.yarnAppRemove(in.username, in.appId)
						}
					}
				}
			} ~
			path("showYarnApp") {
				post {
					entity(as[ShowAppInbound]) { in =>
						complete {
							service.yarnAppShow(in.username)
						}
					}
				}
			} ~
			path("showNodesInfo") {
				post {
					entity(as[ShowNodesInfoInbound]) { in =>
						complete {
							service.nodesInfoShow(in.username)
						}
					}
				}
			} ~
			path("showRunningEvents") {
				post {
					entity(as[ShowRunningEventsInbound]) { in =>
						complete {
							service.runningEventsShow(in.username)
						}
					}
				}
			} ~
			path("showNodeJobs") {
				post {
					entity(as[ShowNodeJobsInbound]) { in =>
						complete {
							service.nodeJobsShow(in.username)
						}
					}
				}
			} ~
			path("showClusterJobs") {
				post {
					entity(as[ShowClusterJobsInbound]) { in =>
						complete {
							service.clusterJobsShow(in.username)
						}
					}
				}
			} ~
			path("metadata" / "showDatabases") {
				//第一次请求，moonbox将所有的数据库名 数据库连接信息 和 该标签，一起返回，比如（tag，（databasename， URL））
				post {
					entity(as[ShowDatabasesInbound]) { in =>
						complete {
							service.databasesShow(in.token)
						}
					}
				}
			} ~
			path("metadata" / "showTables") {
				//第二次请求根据数据库去获取该库下所有表名
				post {
					entity(as[ShowTablesInbound]) { in =>
						complete {
							service.tablesShow(in.token, in.database)
						}
					}
				}
			} ~
			path("metadata" / "describeTable") {
				//第三次请求根据表名获取所有字段信息
				post {
					entity(as[DescribeTablesInbound]) { in =>
						complete {
							service.tableDescribe(in.token, in.table, in.database)
						}
					}
				}
			}*/
		}


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
