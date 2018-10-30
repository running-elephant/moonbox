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

package moonbox.grid.api

import moonbox.grid.ConnectionType.ConnectionType
import moonbox.grid.JobInfo
import moonbox.protocol.app.JobState.JobState
import moonbox.protocol.client.{DatabaseInfo, TableInfo}

sealed trait MbApi

case class RequestAccess(connectionType: ConnectionType) extends MbApi
sealed trait RequestAccessResponse
case class RequestedAccess(address: String) extends MbApi with RequestAccessResponse
case class RequestAccessFailed(error: String) extends MbApi with RequestAccessResponse

case class OpenSession(username: String, database: Option[String], isLocal: Boolean) extends MbApi

sealed trait OpenSessionResponse
case class OpenedSession(sessionId: String) extends MbApi with OpenSessionResponse
case class OpenSessionFailed(error: String) extends MbApi with OpenSessionResponse

case class CloseSession(sessionId: String) extends MbApi
sealed trait CloseSessionResponse
case object ClosedSession extends MbApi with CloseSessionResponse
case class CloseSessionFailed(error: String) extends MbApi with CloseSessionResponse

case class JobQuery(sessionId: String, username: String, sqls: Seq[String]) extends MbApi

case class JobSubmit(username: String, sqls: Seq[String], config: String, async: Boolean = true) extends MbApi

sealed trait JobHandleResponse
case class JobAccepted(jobId: String) extends MbApi with JobHandleResponse
case class JobRejected(error: String) extends MbApi with JobHandleResponse

sealed trait JobResultResponse
case class JobFailed(jobId: String, error: String) extends MbApi with JobResultResponse
//case class JobCompleteWithCachedData(jobId: String) extends MbApi with JobResultResponse
case class JobCompleteWithExternalData(jobId: String, message: Option[String]) extends MbApi with JobResultResponse
case class JobCompleteWithDirectData(jobId: String, schema: String, data: Seq[Seq[String]], hasNext: Boolean) extends MbApi with JobResultResponse

case class JobProgress(jobId: String) extends MbApi
sealed trait JobProgressResponse
case class JobProgressState(jobId: String, jobInfo: JobInfo) extends MbApi with JobProgressResponse

case class JobCancel(jobId: String) extends MbApi
sealed trait JobCancelResponse
case class JobCancelSuccess(jobId: String) extends MbApi with JobCancelResponse
case class JobCancelFailed(jobId: String, error: String) extends MbApi with JobCancelResponse

case class FetchData(sessionId: String, jobId: String, fetchSize: Long) extends MbApi
sealed trait FetchDataResponse
case class FetchDataSuccess(schema: String, date: Seq[Seq[Any]], hasNext: Boolean) extends MbApi with FetchDataResponse
case class FetchDataFailed(error: String) extends MbApi with FetchDataResponse

//---------- internal ------

case class JobSubmitInternal(jobInfo: JobInfo) extends MbApi
case class JobCancelInternal(jobId: String) extends MbApi
case class JobStateChangedInternal(jobId: String, state: JobState)

//----------- cluster (for status query) ---------------
case object GetNodesInfo extends MbApi
sealed trait NodesInfoResultResponse
case class GottenNodesInfo(schema: Seq[String], info: Seq[Seq[Any]]) extends NodesInfoResultResponse with MbApi


//----------- node (for yarn command) -----------
case object GetYarnAppsInfo extends MbApi
sealed trait AppShowResultResponse
case class GottenYarnAppsInfo(schema: Seq[String], info: Seq[Seq[Any]]) extends AppShowResultResponse with MbApi

sealed trait AppStopResultResponse
case class KillYarnApp(id: String) extends MbApi
case class KilledYarnApp(id: String) extends AppStopResultResponse with MbApi
case class KilledYarnAppFailed(id: String, error: String) extends AppStopResultResponse with MbApi

sealed trait AppStartResultResponse
case class StartYarnApp(config: String) extends MbApi //json config string
case class StartedYarnApp(id: String) extends AppStartResultResponse with MbApi
case class StartedYarnAppFailed(id: String, error: String) extends AppStartResultResponse  with MbApi

//---------- local (for catalog command) --------------
sealed trait MbMetaDataApi

case class ShowDatabasesInfo(username: String) extends MbMetaDataApi with MbApi
sealed trait ShowDatabasesResultResponse
case class ShowedDatabasesInfo(info: Seq[DatabaseInfo]) extends ShowDatabasesResultResponse  with MbApi
case class ShowedDatabasesInfoFailed(error: String) extends ShowDatabasesResultResponse  with MbApi

case class ShowTablesInfo(database: String, username: String) extends MbMetaDataApi with MbApi
sealed trait ShowTablesInfoResultResponse
case class ShowedTablesInfo(tables: Seq[String]) extends ShowTablesInfoResultResponse with MbApi
case class ShowedTablesInfoFailed(error: String) extends ShowTablesInfoResultResponse with MbApi


case class DescribeTableInfo(table: String, database: String, username: String) extends MbMetaDataApi with MbApi
sealed trait DescribeTableResultResponse
case class DescribedTableInfo(info: TableInfo) extends DescribeTableResultResponse with MbApi
case class DescribedTableInfoFailed(error: String) extends DescribeTableResultResponse with MbApi

