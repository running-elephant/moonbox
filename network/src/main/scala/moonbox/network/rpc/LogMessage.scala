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

package moonbox.network.rpc

import akka.actor.Actor
import moonbox.common.MbLogging

trait LogMessage extends Actor with MbLogging {
	abstract override def receive: Receive = {
		val _receive = super.receive
		new Receive {
			override def isDefinedAt(x: Any): Boolean = {
				_receive.isDefinedAt(x)
			}

			override def apply(x: Any): Unit = {
				if (!log.isTraceEnabled) {
					_receive(x)
				} else {
					val start = System.nanoTime()

					_receive(x)

					val duration = (System.nanoTime() - start) / 1000000
					log.trace(s"Handled message $x in $duration ms from ${context.sender()}.")
				}
			}
		}
	}
}
