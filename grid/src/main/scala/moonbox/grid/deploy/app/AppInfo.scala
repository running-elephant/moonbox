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

package moonbox.grid.deploy.app

import akka.actor.{ActorRef, Address}

class AppInfo(
	val startTime: Long,
	val id: String,
	val label: String,
	val host: String,
	val port: Int,
	val address: Address,
	val dataPort: Int,
	val endpoint: ActorRef,
	val appType: AppType
) extends Serializable {

	@transient var state: AppState.Value = _

	init()

	private def init(): Unit = {
		state = AppState.RUNNING
	}

	private def readObject(in: java.io.ObjectInputStream): Unit = {
		in.defaultReadObject()
		init()
	}

	override def toString: String = {
		s"""startTime: $startTime
		   |id: $id
		   |label: $label
		   |host: $host
		   |port: $port
		   |address: $address
		   |dataPort: $dataPort
		   |endpoint: $endpoint
		   |type: ${appType.getClass.getSimpleName.stripSuffix("$")}
		   |state: $state
		 """.stripMargin
	}
}








