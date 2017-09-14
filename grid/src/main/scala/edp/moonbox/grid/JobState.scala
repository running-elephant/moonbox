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

package edp.moonbox.grid

import edp.moonbox.grid.JobType.JobType


object JobType extends Enumeration {
	type JobType = Value
	val BATCH, ADHOC, STREAMING, UNKNOWN = Value
}

sealed trait JobStatus {
	def message: String
}

case class Running(progress: Int) extends JobStatus {
	override def message: String = {
		s"job is running, progress is $progress/100"
	}
}
case class Waiting(submitTime: Long) extends JobStatus {
	def duration: Long = System.currentTimeMillis() - submitTime

	override def message: String = {
		s"job is waiting for running, has been wait ${duration/1000} sec"
	}
}
case class Complete(resultSize: Long) extends JobStatus {
	override def message: String = {
		s"job is completed, result size is $resultSize"
	}
}
case class Failed(message: String) extends JobStatus
case class NotFound(message: String = "not found") extends JobStatus

case class JobInfo(jobType: JobType,
                   sessionId: Option[String],
                   jobId: String,
                   sqlList: Seq[String],
                   jobDesc: Option[String] = None)

// TODO add previous JobStatus
case class JobState(jobType: JobType,
                    sessionId: Option[String],
                    jobId: String,
                    sqlList: Seq[String],
                    jobDesc: Option[String] = None,
                    jobStatus: JobStatus,
                    submitTime: Long,
                    updateTime: Long)
