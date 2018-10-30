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

import akka.actor.ActorRef

case class NodeInfo(
	id: String,
	host: String,
	port: Int,
	cores: Int,
	memory: Long,
	endpoint: ActorRef) {
	var coresUsed: Int = _
	var memoryUsed: Long = _
	var lastHeartbeat: Long = _

	@transient var state: NodeState.Value = _

	def coresFree: Int = cores - coresUsed

	def memoryFree: Long = memory - memoryUsed

	var yarnAdhocFreeCore: Int = 0

	var yarnAdhocFreeMemory: Long = 0L

	var yarnRunningBatchJob: Long = 0L

	var restPort: Int = 0

	var jdbcPort: Int = 0

	var odbcPort: Int = 0

}
