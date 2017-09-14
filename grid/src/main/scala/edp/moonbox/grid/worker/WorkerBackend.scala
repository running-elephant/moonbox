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

import akka.actor.{Actor, ActorRef, Props}
import akka.actor.Actor.Receive
import akka.cluster.{Cluster, Member}
import akka.cluster.ClusterEvent.MemberUp
import edp.moonbox.common.EdpLogging
import edp.moonbox.grid.{JobState, Running}
import edp.moonbox.grid.master.MbMaster
import edp.moonbox.grid.message._

import scala.collection.mutable._
import scala.concurrent.duration._


class WorkerBackend(endpoint: ActorRef, param: WorkerParam) extends Actor with EdpLogging {
	import context.dispatcher

	private val runningJobs = new HashMap[String, JobState]()
	private val cluster = Cluster(context.system)

	override def preStart(): Unit = {
		cluster.subscribe(self, classOf[MemberUp])
		workerStateReport()
	}
	override def postStop(): Unit = {
		cluster.unsubscribe(self)
	}

	override def receive: Receive = {
		case r@RunJob(jobState) => {
			handleRunJob(r)
		}

		case ActiveMasterHasChanged => {
			registerDelay()
		}

		case MemberUp(member) => {
			register(member)
		}

	}

	private def handleRunJob(message: RunJob): Unit = {
		val jobState = message.jobState
		runningJobs.put(jobState.jobId, jobState.copy(jobStatus = Running(0)))
		val jobRunner = context.actorOf(Props(classOf[JobRunner], param.conf))
		// TODO
		jobRunner forward message
	}

	private def register(member: Member) = {
		if (member.hasRole(MbMaster.ROLE))
			endpoint ! WorkerRegister(WorkerState(self, runningJobs.size, 10, 5))
	}

	private def workerStateReport(): Unit = {
		context.system.scheduler.schedule(FiniteDuration(10, SECONDS), FiniteDuration(30, SECONDS),
			endpoint, WorkerStateReport(WorkerState(self, runningJobs.size, 10, 5))
		)
	}

	private def registerDelay(): Unit = {
		context.system.scheduler.scheduleOnce(FiniteDuration(10, SECONDS),
			endpoint, WorkerRegister(WorkerState(self, runningJobs.size, 10 , 5)))
	}


}

object WorkerBackend {
	val NAME = "workerbackend"
}
