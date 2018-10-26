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

import moonbox.grid.JobInfo

sealed trait MbApi

case object RequestAccess extends MbApi
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

case class JobSubmit(username: String, sqls: Seq[String], async: Boolean = true) extends MbApi

sealed trait JobHandleResponse
case class JobAccepted(jobId: String) extends MbApi with JobHandleResponse
case class JobRejected(error: String) extends MbApi with JobHandleResponse

sealed trait JobResultResponse
case class JobFailed(jobId: String, error: String) extends MbApi with JobResultResponse
case class JobCompleteWithCachedData(jobId: String) extends MbApi with JobResultResponse
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
