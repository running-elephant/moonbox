package moonbox.grid.deploy.rest

import java.util.concurrent.ConcurrentHashMap

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.ServerBinding
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._
import akka.stream.ActorMaterializer
import de.heikoseeberger.akkahttpjson4s.Json4sSupport.ShouldWritePretty
import moonbox.common.{MbConf, MbLogging}
import moonbox.grid.deploy.MbService
import moonbox.grid.config._
import org.json4s.DefaultFormats
import org.json4s.jackson.Serialization

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

class RestServer(host: String, port: Int, conf: MbConf, service: MbService,
	implicit val akkaSystem: ActorSystem) extends JsonSerializer with MbLogging {

	private val maxRetries: Int = conf.get(PORT_MAX_RETRIES.key, PORT_MAX_RETRIES.defaultValue.get)

	private var bindingFuture: Future[ServerBinding] = _
	private val tokenManager = new TokenManager(conf)

	implicit val materializer = ActorMaterializer()
	implicit val formats = DefaultFormats
	implicit val serialization = Serialization
	implicit val shouldWritePretty = ShouldWritePretty.True


	val routes: Route = {
		path("login") {
			post {
				entity(as[LoginInbound]) { in =>
					complete {
						service.isLogin(in.username).flatMap {
							case true =>
								Future(LoginOutbound(None, Some(s"User '${in.username}' has already logged in.")))
							case false =>
								service.login(in.username, in.password).map {
									case true =>
										val token = tokenManager.encode(in.username)
										LoginOutbound(Some(token), None)
									case false =>
										LoginOutbound(None, Some(s"User '${in.username}' does not exist or password is incorrect."))
								}
						}
					}
				}
			}
		} ~
		path("logout") {
			post {
				entity(as[LogoutInbound]) { in =>
					complete {
						tokenManager.decode(in.token) match {
							case None =>
								LogoutOutbound(error = Some("Token is incorrect or expired."))
							case Some(username) =>
								service.logout(username)
						}
					}
				}
			}
		} ~
		path("openSession") {
			post {
				entity(as[OpenSessionInbound]) { in =>
					complete {
						tokenManager.decode(in.token) match {
							case None =>
								LogoutOutbound(error = Some("Token is incorrect or expired."))
							case Some(username) =>
								service.openSession(username)
						}
					}
				}
			}
		} ~
		path("closeSession") {
			post {
				entity(as[CloseSessionInbound]) { in =>
					complete {
						tokenManager.decode(in.token) match {
							case None =>
								LogoutOutbound(error = Some("Token is incorrect or expired."))
							case Some(username) =>
								service.closeSession(username, in.sessionId)
						}
					}
				}
			}
		} ~
		path("query") {
			post {
				entity(as[QueryInbound]) { in =>
					complete {
						tokenManager.decode(in.token) match {
							case None =>
								LogoutOutbound(error = Some("Token is incorrect or expired."))
							case Some(username) =>
								service.jobQuery(in.sessionId, in.sqls)
						}
					}
				}
			}
		} ~
		path("submit") {
			post {
				entity(as[SubmitInbound]) { in =>
					complete {
						tokenManager.decode(in.token) match {
							case None => SubmitOutbound(error = Some("Token is incorrect or expired."))
							case Some(username) =>
								if (in.mode == "sync") {
									service.jobSubmitSync(username, in.sqls)
								} else {
									service.jobSubmitAsync(username, in.sqls)
								}
						}
					}
				}
			}
		} ~
		path("progress") {
			post {
				entity(as[ProgressInbound]) { in =>
					complete {
						tokenManager.decode(in.token) match {
							case None =>
								ProgressOutbound(jobId = in.jobId, error = Some("Token is incorrect or expired."))
							case Some(username) =>
								service.jobProgress(username, in.jobId)
						}
					}
				}
			}
		} ~
		path("result") {
			post {
				entity(as[ResultInbound]) { in =>
					complete {
						tokenManager.decode(in.token) match {
							case None =>
								ResultOutbound(jobId = in.jobId, error = Some("Token is incorrect or expired."))
							case Some(username) =>
								service.jobResult(username, in.jobId, in.offset, in.size)
						}
					}
				}
			}
		} ~
		path("cancel") {
			post {
				entity(as[CancelInbound]) { in =>
					complete {

						tokenManager.decode(in.token) match {
							case None =>
								CancelOutbound(jobId = in.jobId, error = Some("Token is incorrect or expired."))
							case Some(username) =>
								service.jobCancel(username, in.jobId)
						}
					}
				}
			}
		}


	}

	def start(): Int = {
		require(port == 0 || (port >= 1024 && port < 65535),
			"rest port should be between 1024 (inclusive) and 65535 (exclusive), or 0 for a random free port.")
		logInfo("Starting RestServer ...")
		for (offset <- 0 to maxRetries) {
			val tryPort = if (port == 0) port else port + offset
			try {
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
