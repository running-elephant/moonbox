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

package moonbox.grid.deploy.audit


import java.util.concurrent.{ConcurrentLinkedQueue, ScheduledThreadPoolExecutor, TimeUnit}

import moonbox.common.MbConf
import moonbox.grid.deploy.ConnectionInfo
import scala.collection.mutable.ArrayBuffer


trait AuditLogger {

	def log(user: String, action: String, param: Map[String, String] = Map())(implicit connectionInfo: ConnectionInfo): Unit

}
abstract class AbstractAuditLogger(conf: MbConf) extends AuditLogger {
	private val eventQueue = new ConcurrentLinkedQueue[AuditEvent]()
	private val flushScheduler = {
		val executor = new ScheduledThreadPoolExecutor(1)
		executor.setRemoveOnCancelPolicy(true)
		executor
	}

	init()

	flushScheduler.scheduleAtFixedRate(new Runnable {
		override def run(): Unit = {
			val buffer = new ArrayBuffer[AuditEvent]()
			while (!eventQueue.isEmpty) {
				buffer.append(eventQueue.poll())
			}
			eventQueue.clear()
			persist(buffer)
		}
	}, 0, 30, TimeUnit.SECONDS)

	override def log(user: String, action: String, param: Map[String, String] = Map())(implicit connectionInfo: ConnectionInfo): Unit = {
		log(AuditEvent(user, action, param, connectionInfo))
	}


	private def log(event: AuditEvent)(implicit connectionInfo: ConnectionInfo): Unit = {
		eventQueue.add(event)
	}

	protected def init(): Unit

	protected def persist(events: Seq[AuditEvent]): Unit

	def close(): Unit
}
