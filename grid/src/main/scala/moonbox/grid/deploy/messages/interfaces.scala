/*
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

package moonbox.grid.deploy.messages

import moonbox.grid.deploy.ConnectionType.ConnectionType
import moonbox.grid.deploy.{ConnectionType, JobInfo}
import moonbox.protocol.app.JobState.JobState
import moonbox.protocol.client.{DatabaseInfo, TableInfo}

sealed trait MbApi

sealed trait MbJobApi extends MbApi

case class RequestAccess(connectionType: ConnectionType, isLocal: Boolean) extends MbJobApi
sealed trait RequestAccessResponse
case class RequestedAccess(address: String) extends MbJobApi with RequestAccessResponse
case class RequestAccessFailed(error: String) extends MbJobApi with RequestAccessResponse

case class OpenSession(username: String, database: Option[String], isLocal: Boolean) extends MbJobApi

sealed trait OpenSessionResponse
case class OpenedSession(sessionId: String) extends MbJobApi with OpenSessionResponse
case class OpenSessionFailed(error: String) extends MbJobApi with OpenSessionResponse

case class CloseSession(sessionId: String) extends MbJobApi
sealed trait CloseSessionResponse
case object ClosedSession extends MbJobApi with CloseSessionResponse
case class CloseSessionFailed(error: String) extends MbJobApi with CloseSessionResponse

case class JobQuery(sessionId: String, username: String, sqls: Seq[String]) extends MbJobApi

case class JobSubmit(username: String, sqls: Seq[String], config: String, async: Boolean = true) extends MbJobApi

sealed trait JobHandleResponse
case class JobAccepted(jobId: String) extends MbJobApi with JobHandleResponse
case class JobRejected(error: String) extends MbJobApi with JobHandleResponse

sealed trait JobResultResponse
case class JobFailed(jobId: String, error: String) extends MbJobApi with JobResultResponse
//case class JobCompleteWithCachedData(jobId: String) extends MbApi with JobResultResponse
case class JobCompleteWithExternalData(jobId: String, message: Option[String]) extends MbJobApi with JobResultResponse
case class JobCompleteWithDirectData(jobId: String, schema: String, data: Seq[Seq[String]], hasNext: Boolean) extends MbJobApi with JobResultResponse

case class JobProgress(jobId: String) extends MbJobApi
sealed trait JobProgressResponse
case class JobProgressState(jobId: String, jobInfo: JobInfo) extends MbJobApi with JobProgressResponse

case class JobCancel(jobId: Option[String], sessionId: Option[String]) extends MbJobApi
sealed trait JobCancelResponse
case class JobCancelSuccess(jobId: String) extends MbJobApi with JobCancelResponse
case class JobCancelFailed(jobId: String, error: String) extends MbJobApi with JobCancelResponse

case class FetchData(sessionId: String, jobId: String, fetchSize: Long) extends MbJobApi
sealed trait FetchDataResponse
case class FetchDataSuccess(schema: String, date: Seq[Seq[Any]], hasNext: Boolean) extends MbJobApi with FetchDataResponse
case class FetchDataFailed(error: String) extends MbJobApi with FetchDataResponse

//---------- internal ------
sealed trait MbNodeApi
case class JobSubmitInternal(jobInfo: JobInfo) extends MbNodeApi
case class JobCancelInternal(jobId: String) extends MbNodeApi
case class JobStateChangedInternal(jobId: String, state: JobState) extends MbNodeApi

//----------- management --------
sealed trait MbManagementApi extends MbApi
//cluster (for status query)
case object GetNodesInfo extends MbManagementApi
sealed trait NodesInfoResultResponse
case class GottenNodesInfo(schema: Seq[String], info: Seq[Seq[Any]]) extends NodesInfoResultResponse with MbManagementApi

case object GetRunningEvents extends MbManagementApi
sealed trait RunningEventsResponse
case class GottenRunningEvents(schema: Seq[String], info: Seq[Seq[Any]]) extends RunningEventsResponse with MbManagementApi

case object GetNodeJobInfo extends MbManagementApi
sealed trait NodeJobInfoResultResponse
case class GottenNodeJobInfo(schema: Seq[String], info: Seq[Seq[Any]]) extends NodeJobInfoResultResponse with MbManagementApi

case object GetClusterJobInfo extends MbManagementApi
sealed trait ClusterJobInfoResultResponse
case class GottenClusterJobInfo(schema: Seq[String], info: Seq[Seq[Any]]) extends ClusterJobInfoResultResponse with MbManagementApi



//node (for yarn command)
case object GetYarnAppsInfo extends MbManagementApi
sealed trait AppShowResultResponse
case class GottenYarnAppsInfo(schema: Seq[String], info: Seq[Seq[Any]]) extends AppShowResultResponse with MbManagementApi

sealed trait AppStopResultResponse
case class KillYarnApp(id: String) extends MbManagementApi
case class KilledYarnApp(id: String) extends AppStopResultResponse with MbManagementApi
case class KilledYarnAppFailed(id: String, error: String) extends AppStopResultResponse with MbManagementApi

sealed trait AppStartResultResponse
case class StartYarnApp(config: String) extends MbManagementApi //json config string
case class StartedYarnApp(id: String) extends AppStartResultResponse with MbManagementApi
case class StartedYarnAppFailed(id: String, error: String) extends AppStartResultResponse  with MbManagementApi

//---------- local (for catalog command) --------------
sealed trait MbMetaDataApi extends MbApi

case class ShowDatabasesInfo(username: String) extends MbMetaDataApi
sealed trait ShowDatabasesResultResponse extends MbMetaDataApi
case class ShowedDatabasesInfo(info: Seq[DatabaseInfo]) extends ShowDatabasesResultResponse
case class ShowedDatabasesInfoFailed(error: String) extends ShowDatabasesResultResponse

case class ShowTablesInfo(database: String, username: String) extends MbMetaDataApi
sealed trait ShowTablesInfoResultResponse extends MbMetaDataApi
case class ShowedTablesInfo(tables: Seq[String]) extends ShowTablesInfoResultResponse
case class ShowedTablesInfoFailed(error: String) extends ShowTablesInfoResultResponse


case class DescribeTableInfo(table: String, database: String, username: String) extends MbMetaDataApi
sealed trait DescribeTableResultResponse extends MbMetaDataApi
case class DescribedTableInfo(info: TableInfo) extends DescribeTableResultResponse
case class DescribedTableInfoFailed(error: String) extends DescribeTableResultResponse

*/
