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

package edp.moonbox.grid.master

import java.util.UUID

import akka.actor.{Actor, ActorRef, ActorSystem}
import akka.actor.Actor.Receive
import akka.cluster.Cluster
import akka.actor.Terminated
import edp.moonbox.common.{EdpLogging, Util}
import edp.moonbox.core.MbConf
import edp.moonbox.grid._
import edp.moonbox.grid.message.{RunJob, _}
import edp.moonbox.grid.worker.{MbWorker, WorkerState}

import scala.collection.mutable._
import scala.collection.Seq
import scala.concurrent.Future
import scala.concurrent.duration.FiniteDuration
import scala.concurrent.duration._


trait Scheduler {
	def schedule(): Future[Unit]
}

case object SchedulerJob
case object HandleWorkerLeaveDuringRecovery

class MasterBackend(conf: MbConf,
                    akkaSystem: ActorSystem,
                    persistEngine: PersistInterface) extends Actor with Scheduler with EdpLogging {
	import context.dispatcher
	private val akkaCluster = Cluster.get(akkaSystem)

	private val waitingJobs = new Queue[JobState]()
	private val runningJobs = new HashMap[String, JobState]()
	private val completeJobs = new HashMap[String, JobState]()
	private val failedJobs = new HashMap[String, JobState]()

	private val workerToRunningJobs = new HashMap[ActorRef, Set[String]]()
	private val sessionIdToWorker = new HashMap[String, ActorRef]()

	private val workers = new HashMap[ActorRef, WorkerState]()

	@scala.throws[Exception](classOf[Exception])
	override def preStart(): Unit = {
		recover()
		akkaSystem.scheduler.schedule(
			FiniteDuration(10, SECONDS),
			FiniteDuration(5, SECONDS),
			self,
			SchedulerJob
		)
		akkaSystem.scheduler.scheduleOnce(
			FiniteDuration(60, SECONDS),
			self,
			HandleWorkerLeaveDuringRecovery
		)
		self ! ActiveMasterHasChanged
	}

	private def recover(): Unit = {
		waitingJobs.enqueue(persistEngine.getWaitingJobs:_*)
		runningJobs ++= persistEngine.getRunningJobs
		completeJobs ++= persistEngine.getCompleteJobs
		failedJobs ++= persistEngine.getFailedJobs
		workerToRunningJobs ++= persistEngine.getWorkerToRunningJobs
	}

	override def receive: Receive = {
		case a: ActiveMasterHasChanged.type => informAliveWorkers(a)
		case WorkerRegister(state) => {
			context.watch(sender())
			workers.put(sender(), state)
			schedule()
		}
		case WorkerStateReport(state) => {
			workers.update(sender(), state)
		}
		case SchedulerJob => {
			schedule()
		}
		case HandleWorkerLeaveDuringRecovery => {
			handleWorkerLeaveDuringRecovery()
		}
		case Terminated(worker) => {
			handleWorkerLeave(worker)
		}
		case JobSubmit(jobInfo) =>  {
			logDebug(s"receive jobinfo $jobInfo")
			val jobId = handleJobSubmit(jobInfo)
			reply(JobAccepted(jobId), sender())
			schedule()
		}
		case JobProgress(jobId, sessionId) => {
			val jobState = findJobProgress(jobId, sessionId)
			reply(JobProgressResponse(jobState), sender())
		}
		case JobCancel(jobId, session) => {
			handleJobCancel(jobId, session, sender())
		}
		case JobStateReport(jobState) => {
			logInfo(s"receive job state from worker ${sender()}: ${jobState}")
			handJobStateReport(jobState)
		}
		case OpenSession => {
			reply(handleOpenSession(), sender())
		}
		case CloseSession(sessionId) => {
			reply(handleCloseSession(sessionId), sender())
		}
	}

	def handleJobCancel(jobId: String, sessionId: Option[String], target: ActorRef): Unit = {
		if (!runningJobs.contains(jobId)) {
			reply(JobCancelResponse(s"there is no job $jobId running"), target)
		} else {
			val workerJobs = workerToRunningJobs.find(_._2.contains(jobId))
			if (workerJobs.isDefined) {
				workerJobs.get._1 ! CancelJob(runningJobs.get(jobId).get)
				reply(JobCancelResponse(s"job $jobId canceled"), target)
			} else {
				reply(JobCancelResponse(s"there is no job $jobId running"), target)
			}
		}
	}

	def handJobStateReport(jobState: JobState): Unit = {
		jobState.jobStatus match {
			case r: Running => updateRunningJobState(jobState)
			case c: Complete => moveJobFromRunningToComplete(jobState)
			case f: Failed => moveJobFromRunningToFailed(jobState)
			case _ =>
		}
	}

	private def moveJobFromWaitingToRunning(jobState: JobState): Unit = {
		withPersist {
			persistEngine.removeWaitingJob {
				jobState.jobId
			}
		}
		withPersist {
			persistEngine.addRunningJob {
				val newJobState = jobState.copy(jobStatus = Running(0), updateTime = currentTimeMillis)
				runningJobs.put(newJobState.jobId, newJobState)
				newJobState
			}
		}
	}

	private def moveJobFromRunningToComplete(jobState: JobState): Unit = {
		withPersist {
			persistEngine.addCompleteJob {
				completeJobs.put(jobState.jobId, jobState)
				jobState
			}
		}
		withPersist {
			persistEngine.removeRunningJob {
				runningJobs.remove(jobState.jobId)
				jobState.jobId
			}
		}
	}

	private def moveJobFromRunningToFailed(jobState: JobState): Unit = {
		withPersist {
			persistEngine.addFailedJob {
				failedJobs.put(jobState.jobId, jobState)
				jobState
			}

		}
		withPersist {
			persistEngine.removeRunningJob {
				runningJobs.remove(jobState.jobId)
				jobState.jobId
			}
		}
	}

	private def updateRunningJobState(jobState: JobState): Unit = {
		withPersist {
			persistEngine.updateRunningJob {
				runningJobs.update(jobState.jobId, jobState)
				jobState
			}
		}
	}

	private def handleCloseSession(sessionId: String): MbMessage = {
		val worker = sessionIdToWorker.remove(sessionId)
		if (worker.isDefined) {
			SessionClosed(sessionId)
		} else {
			SessionCloseFailed(sessionId, s"no session $sessionId in scope")
		}
	}

	private def handleOpenSession(): MbMessage = {
		val worker = selectWorker()
		if (worker.isDefined) {
			val sessionId = UUID.randomUUID().toString
			sessionIdToWorker.put(sessionId, worker.get)
			SessionOpened(sessionId)
		} else {
			SessionOpenFailed("there is no available worker, please check your resource")
		}
	}

	private def selectWorker(job: JobState): Option[ActorRef] = {
		job.jobType match {
			case JobType.ADHOC => {
				sessionIdToWorker.get(job.sessionId.get)
			}
			case _ => workers.find(_._2.freeCores>0).map(_._1)
		}
	}

	private def selectWorker(): Option[ActorRef] = {
		workers.find(_._2.freeCores>0).map(_._1)
	}

	private def findJobProgress(jobId: String, sessionId: Option[String]): JobState = {
		waitingJobs.find(job => jobId.equals(job.jobId)).getOrElse(
			runningJobs.getOrElse(jobId,
				completeJobs.getOrElse(jobId,
					failedJobs.getOrElse(jobId, JobState(
						JobType.UNKNOWN, sessionId, jobId, Seq[String](),
						None, NotFound(), 0L, currentTimeMillis
					))
				)
			)
		)
	}

	private def reply(message: MbMessage, target: ActorRef) = {
		target ! message
	}

	private def withPersist(f: => Any): Unit = {
		f
	}


	implicit private def jobInfo2jobState(job: JobInfo): JobState = {
		JobState(job.jobType,
			job.sessionId,
			job.jobId,
			job.sqlList,
			job.jobDesc,
			Waiting(currentTimeMillis),
			currentTimeMillis,
			currentTimeMillis)
	}

	private def handleJobSubmit(job: JobInfo): String = {
		val id = newJobId
		withPersist {
			persistEngine.addWaitingJob {
				val jobWithId = job.copy(jobId = id)
				waitingJobs.enqueue(jobWithId)
				jobWithId
			}
		}
		id
	}

	private def moveJobsFromRunningToWaiting(missingJobIds: Seq[String]): Unit = {
		implicit val missingJobStates = missingJobIds.filter(runningJobs.contains)
			.map { jobId =>
				val oldJobState = runningJobs.get(jobId).get
				val newJobState = oldJobState.copy(jobStatus = Waiting(oldJobState.submitTime),
					updateTime = currentTimeMillis)
				newJobState
			}
		withPersist(
			waitingJobs.enqueue(missingJobStates:_*)
		)
		withPersist {
			missingJobIds.foreach(runningJobs.remove)
		}
	}
	private def handleWorkerLeave(worker: ActorRef): Unit = {
		workers.remove(worker)
		val jobIds = workerToRunningJobs.get(worker)
		if (jobIds.isDefined) {
			moveJobsFromRunningToWaiting(jobIds.get.toSeq)
		}
	}

	private def handleWorkerLeaveDuringRecovery(): Unit = {
		val missingWorkerJobs = workerToRunningJobs.filter { case (worker, jobSet) => !workers.contains(worker) }
		implicit val missingJobIds = missingWorkerJobs.values.flatten.toSeq
		moveJobsFromRunningToWaiting(missingJobIds)
	}

	private def newJobId: String = UUID.randomUUID().toString
	private def currentTimeMillis: Long = System.currentTimeMillis()

	override def schedule(): Future[Unit] = {
		Future {
			logDebug(s"workers: ${workers.size}, " +
				s"waiting: ${waitingJobs.size}, " +
				s"running: ${runningJobs.size}, " +
				s"complete: ${completeJobs.size}, " +
				s"failed: ${failedJobs.size}")

			if (waitingJobs.nonEmpty) {
				val job = waitingJobs.head
				val worker = selectWorker(job)
				worker match {
					case Some(w) =>
						moveJobFromWaitingToRunning(waitingJobs.dequeue())
						logInfo(s"schedule job ${job.jobId} to worker ${worker}")
						w ! RunJob(job)
					case None =>
				}
			}
		}
	}

	private def informAliveWorkers(message: MbMessage): Unit = {
		akkaCluster.state.members.filter(_.hasRole(MbWorker.ROLE))
		    .map { member =>
			    val Some(host) = member.address.host
			    val Some(port) = member.address.port
			    context.actorSelection(Util.akkaAddress(MbMaster.CLUSTER_NAME, host, port, MbWorker.PATH))
		    }.foreach(_ forward message)
	}
}

object MasterBackend {

}
