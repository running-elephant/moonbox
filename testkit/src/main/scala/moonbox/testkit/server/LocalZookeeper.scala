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

package moonbox.testkit.server

import java.io.File
import java.net.InetSocketAddress
import java.util.Properties

import moonbox.common.MbLogging
import moonbox.common.util.Utils
import org.apache.zookeeper.server.{NIOServerCnxnFactory, ZooKeeperServer}

object LocalZookeeper {
	val DEFAULT_ZK_PORT = 2181

	def main(args: Array[String]) {
		val zk = new LocalZookeeper()
		zk.start()
		Runtime.getRuntime.addShutdownHook(new Thread(new Runnable {
			override def run(): Unit = {
				zk.stop()
			}
		}))
	}
}

class LocalZookeeper(properties: Properties) extends MbLogging {
	def this() = this(new Properties())

	import LocalZookeeper._
	private var zkServer: ZooKeeperServer = _
	private lazy val file = new File(System.getProperty("java.io.tmpdir"), "zookeeper").getAbsoluteFile
	private val tickTime = properties.getProperty("tickTime", "5000").toInt
	val clientPort = properties.getProperty("clientPort", s"$DEFAULT_ZK_PORT").toInt
	val numConnections = properties.getProperty("numConnections", "5000").toInt
	def start(): Unit = {
		if (zkServer == null) {
			zkServer = new ZooKeeperServer(file, file, tickTime)
			val factory = new NIOServerCnxnFactory
			factory.configure(new InetSocketAddress(clientPort), numConnections)
			try {
				factory.startup(zkServer)
				logInfo(s"Zookeeper server is started. Listening on port $clientPort")
			} catch {
				case e: Exception => throw new RuntimeException(e)
			}
		} else {
			logWarning("Zookeeper server has been started already.")
		}
	}

	def stop(): Unit = {
		if (zkServer.isRunning) {
			zkServer.shutdown()
			Utils.delete(file)
			logInfo("Zookeeper server shutdown.")
		}
	}

	def isRunning: Boolean = zkServer.isRunning

	def isStop: Boolean = !zkServer.isRunning
}
