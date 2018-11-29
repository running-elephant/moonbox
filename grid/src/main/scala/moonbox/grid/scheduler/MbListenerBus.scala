/*
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

package moonbox.grid.scheduler

import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.{LinkedBlockingDeque, Semaphore}

import moonbox.common.MbConf
import moonbox.common.util.{ListenerBus, ListenerEvent}
import moonbox.grid.config._

import scala.util.DynamicVariable

class MbListenerBus(conf: MbConf) extends ListenerBus[MbListenerInterface, ListenerEvent] {
	self =>
	import MbListenerBus._
	private lazy val EVENT_QUEUE_CAPACITY: Int = {
		val queueSize: Int = conf.get(LISTENER_BUS_EVENT_QUEUE_SIZE)
		if (queueSize < 0) {
			throw new Exception("moonbox.scheduler.listenerbus.eventqueue.size must be > 0!")
		}
		queueSize
	}

	private lazy val eventQueue = new LinkedBlockingDeque[ListenerEvent](EVENT_QUEUE_CAPACITY)

	private val started = new AtomicBoolean(false)
	private val stopped = new AtomicBoolean(false)
	private var processingEvent = false
	private val eventLock = new Semaphore(0)

	private val listenerThread = new Thread(name) {
		setDaemon(true)
		override def run(): Unit = {
			MbListenerBus.withinListenerThread.withValue(true) {
				while (true) {
					eventLock.acquire()
					self.synchronized {
						processingEvent = true
					}
					try {
						val event = eventQueue.poll
						if (event == null) {
							if (!stopped.get) {
								throw new IllegalStateException("Polling `null` from eventQueue. So `stopped` must be true")
							}
							return
						}
						postToAll(event)
					} finally {
						self.synchronized {
							processingEvent = false
						}
					}
				}
			}
		}
	}

	def start(): Unit = {
		if (started.compareAndSet(false, true)) {
			listenerThread.start()
		} else {
			throw new IllegalStateException(s"name already started!")
		}
	}

	def stop(): Unit = {
		if (!started.get()) {
			throw new IllegalStateException(s"try to stop $name that has not started yet!")
		}
		if (stopped.compareAndSet(false, true)) {
			eventLock.release()
			listenerThread.join()
		} else {
		}
	}

	def post(event: ListenerEvent):Unit = {
		if (stopped.get) {
			logError(s"name has already stopped! Dropping event $event")
			return
		}
		val eventAdded = eventQueue.offer(event)
		if (eventAdded) {
			eventLock.release()
		}
	}

	override protected def doPostEvent(
		listener: MbListenerInterface,
		event: ListenerEvent): Unit = {
		event match {
			case jobSubmitted: JobSubmitted =>
				listener.onJobSubmitted(jobSubmitted)
			case jobAccepted: JobAccepted =>
				listener.onJobAccepted(jobAccepted)
			case jobReject: JobReject =>
				listener.onJobReject(jobReject)
			case jobStart: JobStart =>
				listener.onJobStart(jobStart)
			case jobEnd: JobEnd =>
				listener.onJobEnd(jobEnd)
			case jobGettingResult: JobGettingResult =>
				listener.onJobGettingResult(jobGettingResult)
			case _ => listener.onOtherEvent(event)
		}
	}
}

object MbListenerBus {
	val withinListenerThread = new DynamicVariable[Boolean](false)
	val name = "MbListenerBus"
}
*/
