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

package moonbox.grid.deploy.master

import moonbox.grid.JobInfo
import moonbox.grid.deploy.worker.WorkerInfo

import scala.reflect.ClassTag

abstract class PersistenceEngine {
	protected def persist(name: String, obj: Object): Unit

	protected def unpersist(name: String): Unit

	protected def read[T: ClassTag](prefix: String): Seq[T]

	final def readJobs(): Seq[JobInfo] = {
		read[JobInfo]("jobs")
	}

	final def addJob(job: JobInfo): Unit = {
		persist("jobs/" + job.jobId, job)
	}

	final def removeJob(job: JobInfo): Unit = {
		unpersist("jobs/" + job.jobId)
	}

	final def addWorker(worker: WorkerInfo): Unit = {
		persist("workers/" + worker.id, worker)
	}

	final def removeWorker(worker: WorkerInfo): Unit = {
		unpersist("workers/" + worker.id)
	}

	def exist(path: String): Boolean

	def close(): Unit = {}
}
