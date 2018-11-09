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

import java.net.InetSocketAddress

import akka.actor.ActorRef
import akka.pattern.ask
import akka.util.Timeout
import moonbox.protocol.client._
import moonbox.common.{MbConf, MbLogging}
import moonbox.core.CatalogContext
import moonbox.grid.ConnectionInfo
import moonbox.grid.ConnectionType.ConnectionType
import moonbox.grid.api._
import moonbox.grid.deploy2.audit.{AuditInfo, AuditLogger}
import moonbox.grid.deploy2.authenticate.LoginManager
import moonbox.grid.deploy2.rest.TokenManager

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import scala.reflect.ClassTag

class MbService(conf: MbConf, catalogContext: CatalogContext, proxy: ActorRef) extends MbLogging {

	private val SHORT_TIMEOUT = new FiniteDuration(10, SECONDS)
	private val LONG_TIMEOUT = new FiniteDuration(3600 * 24, SECONDS)
// TODO
	private val auditLogger = AuditLogger(conf.getAll)

	private val loginManager = new LoginManager(catalogContext, new TokenManager(conf), this)

	def getLoginManager(): LoginManager = loginManager

	def requestAccess(token: String, isLocal: Boolean, connectionType: ConnectionType)(implicit connection: ConnectionInfo): RequestAccessOutbound = {
		isLogin(token) match {
			case Some(username) =>
				auditLogger.log(AuditInfo("access", username, connection))
				askForCompute[RequestAccessResponse](RequestAccess(connectionType, isLocal))(SHORT_TIMEOUT) match {
					case (Some(RequestedAccess(address)), None) =>
						RequestAccessOutbound(Some(address), None)
					case (Some(RequestAccessFailed(error)), None) =>
						RequestAccessOutbound(error = Some(error))
					case (None, error) =>
						RequestAccessOutbound(None, error = error)
				}
			case None =>
				RequestAccessOutbound(None, error = Some("Please login first."))
		}
	}

	def login(username: String, password: String)(implicit connection: ConnectionInfo): LoginOutbound = {
		auditLogger.log(AuditInfo("login", username, connection))
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
		auditLogger.log(AuditInfo("logout", loginManager.isLogin(token).getOrElse("unknown"), connection))
		loginManager.logout(token)
		LogoutOutbound(None)
	}

	def openSession(token: String, database: Option[String], isLocal: Boolean)(implicit connection: ConnectionInfo): OpenSessionOutbound = {
		isLogin(token) match {
			case Some(username) =>
				auditLogger.log(AuditInfo("openSession", username, connection))
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

	def closeSession(token: String, sessionId: String)(implicit connection: ConnectionInfo =
														ConnectionInfo(new InetSocketAddress(0), new InetSocketAddress(0))): CloseSessionOutbound = {
		isLogin(token) match {
			case Some(username) =>
				auditLogger.log(AuditInfo("closeSession", username, connection))
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

	def interactiveQuery(token: String, sessionId: String, sqls: Seq[String], fetchSize: Int = 200, maxRows: Long = 10000)(implicit connection: ConnectionInfo): InteractiveQueryOutbound = {
		isLogin(token) match {
			case Some(username) =>
				auditLogger.log(AuditInfo("interactiveQuery", username, connection, sql=Some(sqls.mkString(";"))))
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


	def batchQuery(token: String, sqls: Seq[String], config: String)(implicit connection: ConnectionInfo): BatchQueryOutbound = {
		isLogin(token) match {
			case Some(username) =>
				auditLogger.log(AuditInfo("batchQuery", username, connection, sql=Some(sqls.mkString(";"))))
				askForCompute[JobHandleResponse](JobSubmit(username, sqls, config, async = true))(SHORT_TIMEOUT) match {
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

	def cancelQuery(token: String, jobId: Option[String], sessionId: Option[String])(implicit connection: ConnectionInfo): CancelQueryOutbound = {
		isLogin(token) match {
			case Some(username) =>
				auditLogger.log(AuditInfo("cancelQuery", username, connection, sql=None, detail = Some(s"jobId=${jobId.orNull}, sessionId=${sessionId.orNull}")))
				askForCompute[JobCancelResponse](JobCancel(jobId, sessionId))(SHORT_TIMEOUT) match {
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


	def yarnAppAdd(username: String, config: String): AddAppOutbound = {
		askForCompute[AppStartResultResponse](StartYarnApp(config))(SHORT_TIMEOUT) match {  //TODO: user name need log
			case (Some(StartedYarnApp(id)), None) =>
				AddAppOutbound(Some(id))
			case (None, error) =>
				AddAppOutbound(None, error)
		}
	}

	def yarnAppRemove(username: String, appId: String): RemoveAppOutbound = {
		askForCompute[AppStopResultResponse](KillYarnApp(appId))(SHORT_TIMEOUT) match {
			case (Some(KilledYarnApp(id)), None) =>
				RemoveAppOutbound(Some(id))
			case (None, error) =>
				RemoveAppOutbound(None, error)
		}
	}

	def yarnAppShow(username: String): ShowAppOutbound = {
		askForCompute[AppShowResultResponse](GetYarnAppsInfo)(SHORT_TIMEOUT) match {
			case (Some(GottenYarnAppsInfo(schema, info)), None) =>
				ShowAppOutbound(None, Some(schema), Some(info))
			case (None, error) =>
				ShowAppOutbound(error, None, None)
		}
	}

	def nodesInfoShow(username: String): ShowNodesInfoOutbound = {
		askForCompute[NodesInfoResultResponse](GetNodesInfo)(SHORT_TIMEOUT) match {
			case (Some(GottenNodesInfo(schema, info)), None) =>
				ShowNodesInfoOutbound(None, Some(schema), Some(info))
			case (None, error) =>
				ShowNodesInfoOutbound(error, None, None)
		}
	}

	def runningEventsShow(username: String): ShowRunningEventsOutbound = {
		askForCompute[RunningEventsResponse](GetRunningEvents)(SHORT_TIMEOUT) match {
			case (Some(GottenRunningEvents(schema, info)), None) =>
				ShowRunningEventsOutbound(None, Some(schema), Some(info))
			case (None, error) =>
				ShowRunningEventsOutbound(error, None, None)
		}
	}

	def nodeJobsShow(username: String): ShowNodeJobsOutbound = {
		askForCompute[NodeJobInfoResultResponse](GetNodeJobInfo)(SHORT_TIMEOUT) match {
			case (Some(GottenNodeJobInfo(schema, info)), None) =>
				ShowNodeJobsOutbound(None, Some(schema), Some(info))
			case (None, error) =>
				ShowNodeJobsOutbound(error, None, None)
		}
	}

	def clusterJobsShow(username: String): ShowClusterJobsOutbound = {
		askForCompute[ClusterJobInfoResultResponse](GetClusterJobInfo)(SHORT_TIMEOUT) match {
			case (Some(GottenClusterJobInfo(schema, info)), None) =>
				ShowClusterJobsOutbound(None, Some(schema), Some(info))
			case (None, error) =>
				ShowClusterJobsOutbound(error, None, None)
		}
	}


	def databasesShow(token: String): ShowDatabasesOutbound = {
		isLogin(token) match {
			case Some(username) =>  //TODO: user name need ???
				askForCompute[ShowDatabasesResultResponse](ShowDatabasesInfo(username))(SHORT_TIMEOUT) match {
					case (Some(ShowedDatabasesInfo(info)), None) =>
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
				askForCompute[ShowTablesInfoResultResponse](ShowTablesInfo(database, username))(SHORT_TIMEOUT) match {
					case (Some(ShowedTablesInfo(info)), None) =>
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
				askForCompute[DescribeTableResultResponse](DescribeTableInfo(table, database, username))(SHORT_TIMEOUT) match {
					case (Some(DescribedTableInfo(info)), None) =>
						DescribeTablesOutbound(None, Some(info))
					case (None, error) =>
						DescribeTablesOutbound(error, None)
				}
			case None =>
				DescribeTablesOutbound(error = Some("Please login first."))
		}
	}



	private def askForCompute[T: ClassTag](message: MbApi)(timeout: FiniteDuration): (Option[T], Option[String]) = {
		try {
			val result = Await.result(askFor(proxy)(message)(Timeout(timeout)), timeout).asInstanceOf[T]
			(Some(result), None)
		} catch {
			case e: Exception =>
				(None, Some(e.getMessage))
		}
	}


	private def askFor(actorRef: ActorRef)(message: MbApi)(implicit timeout: Timeout): Future[MbApi] = {
		(actorRef ask message).mapTo[MbApi]
	}


}
