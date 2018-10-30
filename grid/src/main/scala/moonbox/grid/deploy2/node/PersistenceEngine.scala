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

package moonbox.grid.deploy2.node

import moonbox.grid.JobInfo

import scala.reflect.ClassTag

abstract class PersistenceEngine {
	protected def persist(name: String, obj: Object): Unit

	protected def unpersist(name: String): Unit

	protected def read[T: ClassTag](prefix: String): Seq[T]

	final def readPersistedData() = {
		(readJobs(), readNodes())
	}
	final def readJobs(): Seq[JobInfo] = {
		read[JobInfo]("jobs")
	}

	final def readNodes(): Seq[NodeInfo] = {
		read[NodeInfo]("nodes")
	}

	final def addJob(job: JobInfo): Unit = {
		persist("jobs/" + job.jobId, job)
	}

	final def removeJob(job: JobInfo): Unit = {
		unpersist("jobs/" + job.jobId)
	}

	final def addNode(node: NodeInfo): Unit = {
		persist("nodes/" + node.id, node)
	}

	final def removeNode(node: NodeInfo): Unit = {
		unpersist("nodes/" + node.id)
	}

	final def saveMasterAddress(address: String): Unit = {
		if (exist("masters")) {
			unpersist("masters/master")
		}
		persist("masters/master", address)
	}

	final def readMasterAddress(): Option[String] = {
		read[String]("masters").headOption
	}

	def exist(path: String): Boolean

	def close(): Unit = {}
}
