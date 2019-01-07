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

package moonbox.grid.deploy


import akka.actor.ActorRef
import akka.pattern.ask
import akka.util.Timeout
import moonbox.common.{MbConf, MbLogging}
import moonbox.grid.deploy.audit.AuditLogger
import moonbox.grid.deploy.messages.Message
import moonbox.grid.deploy.messages.Message._
import moonbox.grid.deploy.security.LoginManager
import moonbox.protocol.client._
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import scala.reflect.ClassTag

private[deploy] class MbService(
	conf: MbConf,
	masterRef: ActorRef,
	auditLogger: AuditLogger) extends MbLogging {

	private val SHORT_TIMEOUT = new FiniteDuration(10, SECONDS)
	private val LONG_TIMEOUT = new FiniteDuration(3600 * 24, SECONDS)

	private val loginManager = new LoginManager(conf, this)

	def login(username: String, password: String)(implicit connection: ConnectionInfo): LoginOutbound = {
		auditLogger.log(username, "login")
		loginManager.login(username, password) match {
			case Some(token) =>
				LoginOutbound(Some(token), None)
			case None =>
				LoginOutbound(None, Some(s"User '$username' does not exist or password is incorrect."))
		}
	}

	def isLogin(token: String): Option[String] = {
		loginManager.isLogin(token)
	}

	def logout(token: String)(implicit connection: ConnectionInfo): LogoutOutbound = {
		auditLogger.log(decodeToken(token), "logout")
		loginManager.logout(token)
		LogoutOutbound(None)
	}

	def openSession(token: String, database: Option[String], config: Map[String, String])(implicit connection: ConnectionInfo): OpenSessionOutbound = {
		auditLogger.log(decodeToken(token), "openSession")
		isLogin(token) match {
			case Some(username) =>
				askSync[OpenSessionResponse](OpenSession(username, database, config))(SHORT_TIMEOUT) match {
					case Left(OpenSessionResponse(Some(sessionId), workerHost, workerPort, message)) =>
						loginManager.putSession(token, sessionId)
						OpenSessionOutbound(Some(sessionId), workerHost, workerPort, None)
					case Left(OpenSessionResponse(None, workerHost, workerPort, message)) =>
						OpenSessionOutbound(workerHost = workerHost, workerPort = workerPort, error = Some(message))
					case Right(message) =>
						OpenSessionOutbound(error = Some(message))
				}
			case None =>
				OpenSessionOutbound(error = Some("Please login first."))
		}
	}

	def closeSession(token: String, sessionId: String)(implicit connection: ConnectionInfo): CloseSessionOutbound = {
		auditLogger.log(decodeToken(token), "closeSession", Map("sessionId" -> sessionId))
		isLogin(token) match {
			case Some(username) =>
				askSync[CloseSessionResponse](CloseSession(sessionId))(SHORT_TIMEOUT) match {
					case Left(CloseSessionResponse(_, true, _)) =>
						loginManager.removeSession(token)
						CloseSessionOutbound(None)
					case Left(CloseSessionResponse(_, false, message)) =>
						CloseSessionOutbound(Some(message))
					case Right(message) =>
						CloseSessionOutbound(error = Some(message))
				}
			case None =>
				CloseSessionOutbound(Some("Please login first."))
		}
	}

	def interactiveQuery(token: String, sessionId: String, sqls: Seq[String], fetchSize: Int = 200, maxRows: Int = 10000)(implicit connection: ConnectionInfo): InteractiveQueryOutbound = {
		auditLogger.log(decodeToken(token), "interactiveQuery", Map("sessionId" -> sessionId, "sqls" -> sqls.mkString(";")))
		isLogin(token) match {
			case Some(username) =>
				askSync[JobQueryResponse](JobQuery(sessionId, sqls, fetchSize, maxRows))(LONG_TIMEOUT) match {
					case Left(JobQueryResponse(false, _, _, _, message)) =>
						InteractiveQueryOutbound(error = Some(message))
					case Left(JobQueryResponse(true, schema, data, hasNext, _)) =>
						InteractiveQueryOutbound(data = Some(ResultData(sessionId, schema, data, hasNext)))
					case Right(message) =>
						InteractiveQueryOutbound(error = Some(message))
				}
			case None =>
				InteractiveQueryOutbound(error = Some("Please login first."))
		}
	}

	def interactiveQueryCancel(token: String, sessionId: String)(implicit connection: ConnectionInfo): CancelQueryOutbound = {
		auditLogger.log(decodeToken(token), "interactiveQueryCancel", Map("sessionId" -> sessionId))
		isLogin(token) match {
			case Some(username) =>
				askSync[InteractiveJobCancelResponse](InteractiveJobCancel(sessionId))(SHORT_TIMEOUT) match {
					case Left(InteractiveJobCancelResponse(true, _)) =>
						CancelQueryOutbound()
					case Left(InteractiveJobCancelResponse(false, message)) =>
						CancelQueryOutbound(error = Some(message))
					case Right(message) =>
						CancelQueryOutbound(error = Some(message))
				}
			case None =>
				CancelQueryOutbound(error = Some("Please login first."))
		}
	}

	def interactiveNextResult(token: String, sessionId: String)(implicit connection: ConnectionInfo): InteractiveNextResultOutbound = {
		auditLogger.log(decodeToken(token), "interactiveNextResult", Map("sessionId" -> sessionId))
		isLogin(token) match {
			case Some(username) =>
				askSync[JobQueryNextResultResponse](JobQueryNextResult(sessionId))(SHORT_TIMEOUT) match {
					case Left(JobQueryNextResultResponse(true, schema, data, hasNext, _)) =>
						InteractiveNextResultOutbound(data = Some(ResultData(sessionId, schema, data, hasNext)))
					case Left(JobQueryNextResultResponse(false, _, _, _, message)) =>
						InteractiveNextResultOutbound(error = Some(message))
					case Right(message) =>
						InteractiveNextResultOutbound(error = Some(message))
				}
			case None =>
				InteractiveNextResultOutbound(error = Some("Please login first."))
		}
	}

	def batchQuery(username: String, password: String, sqls: Seq[String], config: Map[String, String])(implicit connection: ConnectionInfo): BatchQueryOutbound = {
		auditLogger.log(username, "batchQuery", Map("sqls" -> sqls.mkString(";"), "config" -> config.mkString(", ")))
		loginManager.login(username, password) match {
			case Some(_) =>
				askSync[JobSubmitResponse](JobSubmit(username, sqls, config))(SHORT_TIMEOUT) match {
					case Left(JobSubmitResponse(Some(jobId), _)) =>
						BatchQueryOutbound(jobId = Some(jobId))
					case Left(JobSubmitResponse(None, message)) =>
						BatchQueryOutbound(error = Some(message))
					case Right(message) =>
						BatchQueryOutbound(error = Some(message))
				}
			case None =>
				BatchQueryOutbound(error = Some("Login failed. Please check your username and password."))
		}
	}

	def batchQueryCancel(username: String, password: String, jobId: String)(implicit connection: ConnectionInfo): CancelQueryOutbound = {
		auditLogger.log(username, "batchQueryCancel", Map("jobId" -> jobId))
		loginManager.login(username, password) match {
			case Some(_) =>
				askSync[BatchJobCancelResponse](BatchJobCancel(jobId))(SHORT_TIMEOUT) match {
					case Left(BatchJobCancelResponse(id, true, _)) =>
						CancelQueryOutbound()
					case Left(BatchJobCancelResponse(id, false, message)) =>
						CancelQueryOutbound(error = Some(message))
					case Right(message) =>
						CancelQueryOutbound(error = Some(message))
				}
			case None =>
				CancelQueryOutbound(error = Some("Login failed. Please check your username and password."))
		}
	}

	def batchQueryProgress(username: String, password: String, jobId: String)(implicit connection: ConnectionInfo): BatchQueryProgressOutbound = {
		auditLogger.log(username, "batchQueryProgress", Map("jobId" -> jobId))
		loginManager.login(username, password) match {
			case Some(_) =>
				askSync[JobProgressState](JobProgress(jobId))(SHORT_TIMEOUT) match {
					case Left(JobProgressState(id, submitTime, state, message)) =>
						BatchQueryProgressOutbound(message, Some(state))
					case Right(message) =>
						BatchQueryProgressOutbound(message, None)
				}
			case None =>
				BatchQueryProgressOutbound("Please check your username and password.", None)
		}
	}

	def decodeToken(token: String): Option[String] = {
		loginManager.decode(token)
	}

	private def askSync[T: ClassTag](message: Message)(timeout: FiniteDuration): Either[T, String] = {
		try {
			val future = askAsync(masterRef, message)(Timeout(timeout))
			val result = Await.result(future, timeout).asInstanceOf[T]
			Left(result)
		} catch {
			case e: Exception =>
				logWarning(e.getMessage)
				Right(e.getMessage)
		}
	}

	private def askAsync(actorRef: ActorRef, message: Message)(implicit timeout: Timeout): Future[Message] = {
		(actorRef ask message).mapTo[Message]
	}


}
