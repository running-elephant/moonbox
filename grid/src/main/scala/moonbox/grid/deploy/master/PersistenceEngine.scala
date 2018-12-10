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

import scala.reflect.ClassTag

abstract class PersistenceEngine {
	protected def persist(name: String, obj: Object): Unit

	protected def unpersist(name: String): Unit

	protected def read[T: ClassTag](prefix: String): Seq[T]

	final def readPersistedData() = {
		(readDrivers(), readWorkers(), readApplication())
	}
	final def readDrivers(): Seq[DriverInfo] = {
		read[DriverInfo]("drivers")
	}

	final def readWorkers(): Seq[WorkerInfo] = {
		read[WorkerInfo]("workers")
	}

	final def readApplication(): Seq[ApplicationInfo] = {
		read[ApplicationInfo]("apps")
	}

	final def addDriver(driver: DriverInfo): Unit = {
		persist("drivers/" + driver.id, driver)
	}

	final def removeDriver(driver: DriverInfo): Unit = {
		unpersist("drivers/" + driver.id)
	}

	final def addWorker(node: WorkerInfo): Unit = {
		persist("workers/" + node.id, node)
	}

	final def removeWorker(node: WorkerInfo): Unit = {
		unpersist("workers/" + node.id)
	}

	final def addApplication(app: ApplicationInfo): Unit = {
		persist("apps/" + app.id, app)
	}

	final def removeApplication(app: ApplicationInfo): Unit = {
		unpersist("apps/" + app.id)
	}

	def exist(path: String): Boolean

	def close(): Unit = {}
}
