/*-
 * <<
 * Moonbox
 * ==
 * Copyright (C) 2016 - 2019 EDP
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

import akka.actor.{ActorRef, Address}
import moonbox.grid.deploy.app.DriverInfo
import moonbox.grid.deploy.worker.WorkerState

import scala.collection.mutable


class WorkerInfo(
	val id: String,
	val host: String,
	val port: Int,
	val address: Address,
	val endpoint: ActorRef
) extends Serializable {

	@transient var state: WorkerState.Value = _
	@transient var drivers: mutable.HashMap[String, DriverInfo] = _
	@transient var lastHeartbeat: Long = _

	init()

	def setState(state: WorkerState.Value): Unit = {
		this.state = state
	}

	def isAlive: Boolean = {
		this.state == WorkerState.ALIVE
	}

	private def init(): Unit = {
		state = WorkerState.ALIVE
		drivers = new mutable.HashMap[String, DriverInfo]()
		lastHeartbeat = System.currentTimeMillis()
	}

	private def readObject(in: java.io.ObjectInputStream): Unit = {
		in.defaultReadObject()
		init()
	}

	def addDriver(driver: DriverInfo): Unit = {
		drivers(driver.id) = driver
	}

	def removeDriver(driver: DriverInfo): Unit = {
		drivers -= driver.id
	}

	override def toString: String = {
		s"""id: $id
		   |host: $host
		   |port: $port
		   |address: $address
		   |endpoint: $endpoint
		   |state: $state
		 """.stripMargin
	}
}
