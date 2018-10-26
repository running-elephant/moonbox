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

package moonbox.grid.deploy2

import akka.actor.ActorRef
import akka.pattern.ask
import moonbox.protocol.client._
import moonbox.common.{MbConf, MbLogging}
import moonbox.core.CatalogContext
import moonbox.grid.api.{ClosedSession, _}
import moonbox.grid.deploy2.authenticate.LoginManager
import moonbox.grid.deploy2.rest.TokenManager
import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import scala.reflect.ClassTag

class MbService(conf: MbConf, catalogContext: CatalogContext, proxy: ActorRef) extends MbLogging {

	private val SHORT_TIMEOUT = new FiniteDuration(10, SECONDS)
	private val LONG_TIMEOUT = new FiniteDuration(3600 * 24, SECONDS)
// TODO
	private val loginManager = new LoginManager(catalogContext, new TokenManager(conf), this)

	def getLoginManager(): LoginManager = loginManager

	def login(username: String, password: String): LoginOutbound = {
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

	def logout(token: String): LogoutOutbound = {
		loginManager.logout(token)
		LogoutOutbound(None)
	}


	def openSession(token: String, database: Option[String], isLocal: Boolean): OpenSessionOutbound = {
		isLogin(token) match {
			case Some(username) =>
				askForCompute[OpenSessionResponse](OpenSession(username, database, isLocal))(SHORT_TIMEOUT) match {
					case (Some(OpenedSession(sessionId)), None) =>
						loginManager.putSession(token, sessionId)
						OpenSessionOutbound(Some(sessionId), None)
					case (Some(OpenSessionFailed(error)), None) =>
						OpenSessionOutbound(None, Some(error))
					case (None, error) =>
						OpenSessionOutbound(error = error)
				}
			case None =>
				OpenSessionOutbound(error = Some("Please login first."))
		}
	}

	def closeSession(token: String, sessionId: String): CloseSessionOutbound = {
		isLogin(token) match {
			case Some(username) =>
				askForCompute[CloseSessionResponse](CloseSession(sessionId))(SHORT_TIMEOUT) match {
					case (Some(ClosedSession), None) =>
						loginManager.removeSession(token)
						CloseSessionOutbound(None)
					case (Some(CloseSessionFailed(error)), None) =>
						CloseSessionOutbound(Some(error))
					case (None, error) =>
						CloseSessionOutbound(error)
				}
			case None =>
				CloseSessionOutbound(Some("Please login first."))
		}
	}

	def interactiveQuery(token: String, sessionId: String,
		sqls: Seq[String], fetchSize: Int = 200, maxRows: Long = 10000): InteractiveQueryOutbound = {
		isLogin(token) match {
			case Some(username) =>
				askForCompute[JobResultResponse](JobQuery(sessionId, username, sqls))(LONG_TIMEOUT) match {
					case (Some(JobFailed(jobId, error)), None) =>
						InteractiveQueryOutbound(error = Some(error))
					case (Some(JobCompleteWithExternalData(jobId, message)), None) =>
						InteractiveQueryOutbound(hasResult = false)
					case (Some(JobCompleteWithDirectData(jobId, schema, data, hasNext)), None) =>
						InteractiveQueryOutbound(hasResult = true, data = Some(ResultData(jobId, schema, data, hasNext)))
					case (None, error) =>
						InteractiveQueryOutbound(error = error)
				}
			case None =>
				InteractiveQueryOutbound(error = Some("Please login first."))
		}
	}

	def interactiveNextResult(token: String, sessionId: String, cursor: String, fetchSize: Long): InteractiveNextResultOutbound = {
		isLogin(token) match {
			case Some(username) =>
				askForCompute[FetchDataResponse](FetchData(sessionId, cursor, fetchSize))(SHORT_TIMEOUT) match {
					case (Some(FetchDataSuccess(schema, data, hasNext)), None) =>
						InteractiveNextResultOutbound(data = Some(ResultData(cursor, schema, data, hasNext)))
					case (Some(FetchDataFailed(error)), None) =>
						InteractiveNextResultOutbound(error = Some(error))
					case (None, error) =>
						InteractiveNextResultOutbound(error = error)
				}
			case None =>
				InteractiveNextResultOutbound(error = Some("Please login first."))
		}
	}


	def batchQuery(token: String, sqls: Seq[String], config: Seq[String]): BatchQueryOutbound = {
		isLogin(token) match {
			case Some(username) =>
				askForCompute[JobHandleResponse](JobSubmit(username, sqls, async = true))(SHORT_TIMEOUT) match {
					case (Some(JobAccepted(jobId)), None) =>
						BatchQueryOutbound(jobId = Some(jobId))
					case (Some(JobRejected(error)), None) =>
						BatchQueryOutbound(error = Some(error))
					case (None, error) =>
						BatchQueryOutbound(error = error)
				}
			case None =>
				BatchQueryOutbound(error = Some("Please login first."))
		}
	}

	def cancelQuery(token: String, jobId: String): CancelQueryOutbound = {
		isLogin(token) match {
			case Some(username) =>
				askForCompute[JobCancelResponse](JobCancel(jobId))(SHORT_TIMEOUT) match {
					case (Some(JobCancelSuccess(id)), None) =>
						CancelQueryOutbound()
					case (Some(JobCancelFailed(id, error)), None) =>
						CancelQueryOutbound(error = Some(error))
					case (None, error) =>
						CancelQueryOutbound(error = error)
				}
			case None =>
				CancelQueryOutbound(error = Some("Please login first."))
		}
	}

	def batchQueryProgress(token: String, jobId: String): BatchQueryProgressOutbound = {
		isLogin(token) match {
			case Some(username) =>
				askForCompute[JobProgressResponse](JobProgress(jobId))(SHORT_TIMEOUT) match {
					case (Some(JobProgressState(id, jobInfo)), None) =>
						BatchQueryProgressOutbound(error = jobInfo.errorMessage, state = Some(s"${jobInfo.status}"))
					case (None, error) =>
						BatchQueryProgressOutbound(error = error)
				}
			case None =>
				BatchQueryProgressOutbound(error = Some("Please login first."))
		}
	}

	private def askForCompute[T: ClassTag](message: MbApi)(timeout: Duration): (Option[T], Option[String]) = {
		try {
			val result = Await.result(askFor(proxy)(message), timeout).asInstanceOf[T]
			(Some(result), None)
		} catch {
			case e: Exception =>
				(None, Some(e.getMessage))
		}
	}


	private def askFor(actorRef: ActorRef)(message: MbApi): Future[MbApi] = {
		(actorRef ask message).mapTo[MbApi]
	}


}
