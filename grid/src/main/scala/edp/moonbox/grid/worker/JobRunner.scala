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

package edp.moonbox.grid.worker

import akka.actor.{Actor, ActorRef, PoisonPill}
import edp.moonbox.common.{EdpLogging, RedisDFHandler}
import edp.moonbox.core.MbConf
import edp.moonbox.grid.{Complete, Failed, JobState, JobType}
import edp.moonbox.grid.message.{CancelJob, JobStateReport, RunJob}
import org.apache.spark.sql.MbSession
import scala.concurrent.Future
import scala.util.{Failure, Success}


class JobRunner(conf: MbConf) extends Actor with EdpLogging {
	import context.dispatcher

	val mb = MbSession(conf)
	override def receive: Receive = {
		case RunJob(jobState) =>
			val sender_ = sender()
			run(jobState).onComplete {
				case Success(size) =>
					successCallback(jobState, size, sender_)
				case Failure(e) =>
					failureCallback(jobState, e, sender_)
			}
		case CancelJob(jobState) => {
			mb.cancelJobGroup(jobState.jobId)
		}
	}

	private def run(jobState: JobState): Future[Long] = {
		Future[Long] {
			mb.setJobGroup(jobState.jobId, jobState.jobDesc.getOrElse(""))
			val dataframe = mb.sql(jobState.sqlList)
			mb.manipulate(jobState.jobId, dataframe,
				Map("servers" -> conf.cacheServers),
				new RedisDFHandler).asInstanceOf[Long]
		}
	}

	private def successCallback(jobState: JobState, size: Long, sender: ActorRef): Unit = {
		logInfo(s"send job complete report to sender ${sender}")
		sender ! JobStateReport(jobState.copy(
			jobStatus = Complete(size),
			updateTime = System.currentTimeMillis())
		)
		if (jobState.jobType == JobType.BATCH) self ! PoisonPill
	}

	private def failureCallback(jobState: JobState, e: Throwable, sender: ActorRef): Unit = {
		logInfo(s"send job failed report to sender ${sender}")
		sender ! JobStateReport(jobState.copy(
			jobStatus = Failed(e.getMessage),
			updateTime = System.currentTimeMillis())
		)
		if (jobState.jobType == JobType.BATCH) self ! PoisonPill
	}
}
