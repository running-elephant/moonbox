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
import moonbox.core.CatalogContext
import moonbox.grid.deploy.audit.{AuditEvent, AuditLogger}
import moonbox.grid.deploy.messages.Message
import moonbox.grid.deploy.messages.Message._
import moonbox.grid.deploy.security.LoginManager
import moonbox.grid.deploy.rest.TokenManager
import moonbox.protocol.client._

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import scala.reflect.ClassTag

private[deploy] class MbService(
	conf: MbConf,
	catalogContext: CatalogContext,
	masterRef: ActorRef,
	auditLogger: AuditLogger) extends MbLogging {

	private val SHORT_TIMEOUT = new FiniteDuration(10, SECONDS)
	private val LONG_TIMEOUT = new FiniteDuration(3600 * 24, SECONDS)
	private val loginManager = new LoginManager(catalogContext, new TokenManager(conf), this)

	/*def requestAccess(token: String, isLocal: Boolean, connectionType: ConnectionType)(implicit connection: ConnectionInfo): RequestAccessOutbound = {
		isLogin(token) match {
			case Some(username) =>
				//auditLogger.log(AuditInfo("access", username, connection))
				askSync[RequestAccessResponse](RequestAccess(connectionType, isLocal))(SHORT_TIMEOUT) match {
					case (Some(RequestedAccess(address)), _) =>
						RequestAccessOutbound(Some(address), None)
					case (Some(RequestAccessFailed(error)), _) =>
						RequestAccessOutbound(error = Some(error))
					case (None, error) =>
						RequestAccessOutbound(None, error = error)
				}
			case None =>
				RequestAccessOutbound(None, error = Some("Please login first."))
		}
	}*/

	def decodeToken(token: String): Option[String] = {
		loginManager.tokenManager.decode(token)
	}

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

	def openSession(token: String, database: Option[String], isLocal: Boolean)(implicit connection: ConnectionInfo): OpenSessionOutbound = {
		auditLogger.log(decodeToken(token), "openSession")
		isLogin(token) match {
			case Some(username) =>
				askSync[OpenSessionResponse](OpenSession(username, database, isLocal))(SHORT_TIMEOUT) match {
					case Left(OpenSessionResponse(Some(sessionId), message)) =>
						loginManager.putSession(token, sessionId)
						OpenSessionOutbound(Some(sessionId), None)
					case Left(OpenSessionResponse(None, message)) =>
						OpenSessionOutbound(error = Some(message))
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

	def interactiveQuery(token: String, sessionId: String, sqls: Seq[String], fetchSize: Int = 200, maxRows: Long = 10000)(implicit connection: ConnectionInfo): InteractiveQueryOutbound = {
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

	def batchQuery(token: String, sqls: Seq[String], config: String)(implicit connection: ConnectionInfo): BatchQueryOutbound = {
		auditLogger.log(decodeToken(token), "batchQuery", Map("sqls" -> sqls.mkString(";"), "config" -> config))
		isLogin(token) match {
			case Some(username) =>
				askSync[JobSubmitResponse](JobSubmit(username, sqls, config))(SHORT_TIMEOUT) match {
					case Left(JobSubmitResponse(Some(jobId), _)) =>
						BatchQueryOutbound(jobId = Some(jobId))
					case Left(JobSubmitResponse(None, message)) =>
						BatchQueryOutbound(error = Some(message))
					case Right(message) =>
						BatchQueryOutbound(error = Some(message))
				}
			case None =>
				BatchQueryOutbound(error = Some("Please login first."))
		}
	}

	def batchQueryCancel(token: String, jobId: String)(implicit connection: ConnectionInfo): CancelQueryOutbound = {
		auditLogger.log(decodeToken(token), "batchQueryCancel", Map("jobId" -> jobId))
		isLogin(token) match {
			case Some(username) =>
				askSync[BatchJobCancelResponse](BatchJobCancel(jobId))(SHORT_TIMEOUT) match {
					case Left(BatchJobCancelResponse(jobId, true, _)) =>
						CancelQueryOutbound()
					case Left(BatchJobCancelResponse(jobId, false, message)) =>
						CancelQueryOutbound(error = Some(message))
					case Right(message) =>
						CancelQueryOutbound(error = Some(message))
				}
			case None =>
				CancelQueryOutbound(error = Some("Please login first."))
		}
	}

	def batchQueryProgress(token: String, jobId: String)(implicit connection: ConnectionInfo): BatchQueryProgressOutbound = {
		auditLogger.log(decodeToken(token), "batchQueryProgress", Map("jobId" -> jobId))
		isLogin(token) match {
			case Some(username) =>
				askSync[JobProgressState](JobProgress(jobId))(SHORT_TIMEOUT) match {
					case Left(JobProgressState(id, submitTime, state, message)) =>
						BatchQueryProgressOutbound(message, Some(state))
					case Right(message) =>
						BatchQueryProgressOutbound(message, None)
				}
			case None =>
				BatchQueryProgressOutbound("Please login first.", None)
		}
	}


	/*def yarnAppAdd(username: String, config: String): AddAppOutbound = {
		askSync[AppStartResultResponse](StartYarnApp(config))(SHORT_TIMEOUT) match {  //TODO: user name need log
			case (Some(StartedYarnApp(id)), _) =>
				AddAppOutbound(Some(id))
			case (Some(StartedYarnAppFailed(id, error)), _) =>
				AddAppOutbound(None, Some(error))
			case (None, error) =>
				AddAppOutbound(None, error)
		}
	}

	def yarnAppRemove(username: String, appId: String): RemoveAppOutbound = {
		askSync[AppStopResultResponse](KillYarnApp(appId))(SHORT_TIMEOUT) match {
			case (Some(KilledYarnApp(id)), _) =>
				RemoveAppOutbound(Some(id))
			case
			case (None, error) =>
				RemoveAppOutbound(None, error)
		}
	}

	def yarnAppShow(username: String): ShowAppOutbound = {
		askSync[AppShowResultResponse](GetYarnAppsInfo)(SHORT_TIMEOUT) match {
			case (Some(GottenYarnAppsInfo(schema, info)), _) =>
				ShowAppOutbound(None, Some(schema), Some(info))
			case (None, error) =>
				ShowAppOutbound(error, None, None)
		}
	}

	def nodesInfoShow(username: String): ShowNodesInfoOutbound = {
		askSync[NodesInfoResultResponse](GetNodesInfo)(SHORT_TIMEOUT) match {
			case (Some(GottenNodesInfo(schema, info)), _) =>
				ShowNodesInfoOutbound(None, Some(schema), Some(info))
			case (None, error) =>
				ShowNodesInfoOutbound(error, None, None)
		}
	}

	def runningEventsShow(username: String): ShowRunningEventsOutbound = {
		askSync[RunningEventsResponse](GetRunningEvents)(SHORT_TIMEOUT) match {
			case (Some(GottenRunningEvents(schema, info)), _) =>
				ShowRunningEventsOutbound(None, Some(schema), Some(info))
			case (None, error) =>
				ShowRunningEventsOutbound(error, None, None)
		}
	}

	def nodeJobsShow(username: String): ShowNodeJobsOutbound = {
		askSync[NodeJobInfoResultResponse](GetNodeJobInfo)(SHORT_TIMEOUT) match {
			case (Some(GottenNodeJobInfo(schema, info)), _) =>
				ShowNodeJobsOutbound(None, Some(schema), Some(info))
			case (None, error) =>
				ShowNodeJobsOutbound(error, None, None)
		}
	}

	def clusterJobsShow(username: String): ShowClusterJobsOutbound = {
		askSync[ClusterJobInfoResultResponse](GetClusterJobInfo)(SHORT_TIMEOUT) match {
			case (Some(GottenClusterJobInfo(schema, info)), _) =>
				ShowClusterJobsOutbound(None, Some(schema), Some(info))
			case (None, error) =>
				ShowClusterJobsOutbound(error, None, None)
		}
	}


	def databasesShow(token: String): ShowDatabasesOutbound = {
		isLogin(token) match {
			case Some(username) =>  //TODO: user name need ???
				askSync[ShowDatabasesResultResponse](ShowDatabasesInfo(username))(SHORT_TIMEOUT) match {
					case (Some(ShowedDatabasesInfo(info)), _) =>
						ShowDatabasesOutbound(None, Some(info))
					case (None, error) =>
						ShowDatabasesOutbound(error, None)
				}
			case None =>
				ShowDatabasesOutbound(error = Some("Please login first."))
		}
	}

	def tablesShow(token: String, database: String): ShowTablesOutbound = {
		isLogin(token) match {
			case Some(username) =>  //TODO: user name need ???
				askSync[ShowTablesInfoResultResponse](ShowTablesInfo(database, username))(SHORT_TIMEOUT) match {
					case (Some(ShowedTablesInfo(info)), _) =>
						ShowTablesOutbound(None, Some(info))
					case (None, error) =>
						ShowTablesOutbound(error, None)
				}
			case None =>
				ShowTablesOutbound(error = Some("Please login first."))
		}
	}

	def tableDescribe(token: String, table: String, database: String): DescribeTablesOutbound = {
		isLogin(token) match {
			case Some(username) =>  //TODO: user name need ???
				askSync[DescribeTableResultResponse](DescribeTableInfo(table, database, username))(SHORT_TIMEOUT) match {
					case (Some(DescribedTableInfo(info)), _) =>
						DescribeTablesOutbound(None, Some(info))
					case (None, error) =>
						DescribeTablesOutbound(error, None)
				}
			case None =>
				DescribeTablesOutbound(error = Some("Please login first."))
		}
	}*/

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
