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

package moonbox.grid.timer

trait TimedEventService {
	def start(): Unit
	def stop(): Unit
	def clear(): Unit
	def addTimedEvent(event: EventEntity): Unit
	def deleteTimedEvent(group: String, name: String): Unit
	def pauseTimedEvent(group: String, name: String): Unit
	def resumeTimedEvent(group: String, name: String): Unit
	def getTimedEvent(group: String, name: String): EventRuntime
	def getTimedEvents(group: String): Seq[EventRuntime]
	def getTimedEvents(): Seq[EventRuntime]
	def timedEventExists(group: String, name: String): Boolean
}
