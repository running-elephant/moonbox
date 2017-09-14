/*-
 * <<
 * Moonbox
 * ==
 * Copyright (C) 2016 - 2017 EDP
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

package edp.moonbox.grid.message

import edp.moonbox.grid.worker.WorkerState
import edp.moonbox.grid.{JobInfo, JobState}


sealed trait MbMessage

case object ActiveMasterHasChanged extends MbMessage

case class WorkerRegister(state: WorkerState) extends MbMessage

case class JobSubmit(job: JobInfo) extends MbMessage
case class JobAccepted(jobId: String, sessionId: Option[String] = None) extends MbMessage

case class JobProgress(jobId: String, sessionId: Option[String] = None) extends MbMessage
case class JobProgressResponse(jobState: JobState) extends MbMessage

case class JobCancel(jobId: String, sessionId: Option[String] = None) extends MbMessage
case class JobCancelResponse(message: String) extends MbMessage

case object OpenSession extends MbMessage
case class SessionOpened(sessionId: String) extends MbMessage
case class SessionOpenFailed(message: String) extends MbMessage

case class CloseSession(sessionId: String) extends MbMessage
case class SessionClosed(sessionId: String) extends MbMessage
case class SessionCloseFailed(sessionId: String, message: String) extends MbMessage

case class WorkerStateReport(workerState: WorkerState) extends MbMessage
case class JobStateReport(jobState: JobState) extends MbMessage

case class RunJob(jobState: JobState) extends MbMessage
case class CancelJob(jobState: JobState) extends MbMessage

case class FetchData(jobId: String, offset: Long, size: Long) extends MbMessage
case class FetchDataResponse(jobId: String, data: Option[Seq[String]], message: String) extends MbMessage
