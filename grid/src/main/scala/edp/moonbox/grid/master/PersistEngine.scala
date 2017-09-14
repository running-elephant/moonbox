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

import akka.actor.{ActorRef, ActorSystem}
import edp.moonbox.common.{EdpLogging, Util}
import edp.moonbox.core.MbConf
import edp.moonbox.grid.JobState
import org.apache.curator.framework.{CuratorFramework, CuratorFrameworkFactory}
import org.apache.curator.retry.RetryNTimes
import org.apache.zookeeper.CreateMode

import scala.collection.mutable
import scala.collection.JavaConverters._
import scala.reflect.ClassTag


object PersistEngine {
	def newInstance(conf: MbConf)(implicit system: ActorSystem): PersistInterface = {
		// TODO
		new ZkPersist(conf)
	}
}

trait PersistInterface {

	val ROOT = "/grid"
	val MASTERS_DIR = s"$ROOT/masters"
	val MASTER_KEY = s"url"

	val CACHE_DIR = s"$ROOT/cache"
	val CACHE_KEY = s"url"

	val JOBS_DIR = s"$ROOT/jobs"
	val WAITING_DIR = s"$JOBS_DIR/waiting"
	val RUNNING_DIR = s"$JOBS_DIR/running"
	val COMPLETE_DIR = s"$JOBS_DIR/complete"
	val FAILED_DIR = s"$JOBS_DIR/failed"
	val WORKER_TO_RUNNING_JOBS = s"$ROOT/work2jobs"
	val KEY_UNDER_WORKER = s"jobs"

	def addWaitingJob(job: JobState): Boolean = {
		add[JobState](WAITING_DIR, job.jobId, job, CreateMode.PERSISTENT)
	}
	def removeWaitingJob(jobId: String): Boolean = {
		remove(WAITING_DIR, jobId)
	}
	def updateWaitingJob(job: JobState): Boolean = {
		update[JobState](WAITING_DIR, job.jobId, job)
	}
	def getWaitingJobs: Seq[JobState] = {
		getAll[JobState](WAITING_DIR)
	}

	def addRunningJob(job: JobState): Boolean = {
		add[JobState](RUNNING_DIR, job.jobId, job, CreateMode.PERSISTENT)
	}
	def removeRunningJob(jobId: String): Boolean = {
		remove(RUNNING_DIR, jobId)
	}
	def updateRunningJob(job: JobState): Boolean = {
		update[JobState](RUNNING_DIR, job.jobId, job)
	}
	def getRunningJobs: Seq[(String, JobState)] = {
		getAll[JobState](RUNNING_DIR).map(job => (job.jobId, job))
	}

	def addCompleteJob(job: JobState): Boolean = {
		add[JobState](COMPLETE_DIR, job.jobId, job, CreateMode.PERSISTENT)
	}
	def removeCompleteJob(jobId: String): Boolean = {
		remove(COMPLETE_DIR, jobId)
	}
	def updateCompleteJob(job: JobState): Boolean = {
		update[JobState](COMPLETE_DIR, job.jobId, job)
	}
	def getCompleteJobs: Seq[(String, JobState)] = {
		getAll[JobState](COMPLETE_DIR).map(job => (job.jobId, job))
	}

	def addFailedJob(job: JobState): Boolean = {
		add[JobState](FAILED_DIR, job.jobId, job, CreateMode.PERSISTENT)
	}
	def removeFailedJob(jobId: String): Boolean = {
		remove(FAILED_DIR, jobId)
	}
	def updateFailedJob(job: JobState): Boolean = {
		update[JobState](COMPLETE_DIR, job.jobId, job)
	}
	def getFailedJobs: Seq[(String, JobState)] = {
		getAll[JobState](COMPLETE_DIR).map(job => (job.jobId, job))
	}

	def addWorkerJob(worker: ActorRef, job: String): Boolean = {
		val workerKey = Util.actorRef2SocketString(worker)
		add[ActorRef](WORKER_TO_RUNNING_JOBS, workerKey, worker, CreateMode.PERSISTENT)
		add[String](s"$WORKER_TO_RUNNING_JOBS/$workerKey", KEY_UNDER_WORKER, job, CreateMode.PERSISTENT)
	}

	def removeWorker(worker: ActorRef): Boolean = {
		remove(WORKER_TO_RUNNING_JOBS, Util.actorRef2SocketString(worker))
	}
	def removeJobFromWorker(worker: ActorRef, jobId: String): Boolean = {
		remove(s"$WORKER_TO_RUNNING_JOBS/${Util.actorRef2SocketString(worker)}", jobId)
	}

	def getWorkerToRunningJobs: Seq[(ActorRef, mutable.Set[String])] = {
		val workers = getAll[ActorRef](WORKER_TO_RUNNING_JOBS)
		workers.map { worker =>
			val workerKey = Util.actorRef2SocketString(worker)
			val jobsPath = s"$WORKER_TO_RUNNING_JOBS/$workerKey/$KEY_UNDER_WORKER"
			worker -> (mutable.Set[String]() ++ getAll[String](jobsPath))
		}
	}

	def clear: Boolean = {
		remove(ROOT, "")
	}

	def cacheURL: Option[String] = {
		getAll[String](CACHE_DIR).headOption
	}

	def setCacheURL(url: String): Boolean = {
		add[String](CACHE_DIR, CACHE_KEY, url, CreateMode.EPHEMERAL_SEQUENTIAL)
	}

	def masterURL: Seq[String] = {
		getAll[String](MASTERS_DIR)
	}

	def setMasterURL(url: String): Boolean = {
		add[String](MASTERS_DIR, MASTER_KEY, url, CreateMode.EPHEMERAL_SEQUENTIAL)
	}

	def add[T: ClassTag](path: String, key: String, data: T, mode: CreateMode): Boolean

	def remove(path: String, key: String): Boolean

	def update[T: ClassTag](path: String, key: String, data: T): Boolean

	def getAll[T: ClassTag](path: String): Seq[T]

	def exist(path: String): Boolean

}

class ZkPersist(conf: MbConf)(implicit val system: ActorSystem)
	extends PersistInterface with EdpLogging {

	val persistEnable = conf.persisEnable

	private val zk: CuratorFramework = {
		val client = CuratorFrameworkFactory.newClient(conf.persistConnectString,
			new RetryNTimes(3, 5000))
		client.start()
		client
	}

	override def add[T: ClassTag](path: String, key: String, job: T, mode: CreateMode): Boolean = {
		zk.create().creatingParentsIfNeeded()
			.withMode(mode)
			.forPath(s"$path/$key", Util.serialize[T](job))
		true
	}

	override def remove(path: String, key: String): Boolean = {
		try {
			zk.delete().deletingChildrenIfNeeded().forPath(s"$path/$key")
			true
		} catch {
			case e: Exception => false
		}
	}

	override def update[T: ClassTag](path: String, key: String, job: T): Boolean = {
		zk.setData().forPath(s"$path/$key", Util.serialize[T](job))
		true
	}

	override def getAll[T: ClassTag](path: String): Seq[T] = {
		if (exist(path)) {
			val children: List[String] = zk.getChildren.forPath(path).asScala.toList
			val all = children.map { jobId =>
				val key = s"$path/$jobId"
				val time = zk.checkExists().forPath(key).getCtime
				val data = zk.getData.forPath(key)
				time -> Util.deserialize[T](data)
			}.sortBy(it => it._1).map(_._2)
			all
		} else Seq()

	}

	override def exist(path: String): Boolean = {
		null != zk.checkExists().forPath(path)
	}
}
