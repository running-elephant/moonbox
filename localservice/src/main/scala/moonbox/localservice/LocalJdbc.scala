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

package moonbox.localservice

import moonbox.common.MbLogging
import org.h2.tools.Server


object LocalJdbc {
	val DEFAULT_JDBC_PORT: Int = 9092
}
class LocalJdbc(val port: Int) extends MbLogging {
	def this() = this(LocalJdbc.DEFAULT_JDBC_PORT)

	private val jdbcServer = Server.createTcpServer("-tcpPort", s"$port","-tcpAllowOthers")

	def start(): Unit = {
		try {
			jdbcServer.start()
			logInfo("Jdbc server is listening on port: " + port)
		} catch {
			case e: Exception => throw new RuntimeException(e)
		}
	}

	def stop(): Unit = {
		if (jdbcServer != null) {
			if (jdbcServer.isRunning(false)) {
				jdbcServer.stop()
				logInfo("Jdbc server is stop.")
			} else {
				logWarning("Jdbc server is not active.")
			}
		} else {
			logWarning("Jdbc server is not initialized.")
		}
	}

	def isRunning: Boolean = jdbcServer.isRunning(false)

	def isStop: Boolean = !isRunning


}
