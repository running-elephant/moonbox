package edp.moonbox.common.zookeeper

import java.io.File
import java.net.InetSocketAddress
import java.util.Properties

import edp.moonbox.common.{EdpLogging, Util}
import org.apache.zookeeper.server.{NIOServerCnxnFactory, ZooKeeperServer}


class LocalZookeeper(zkProperties: Properties) extends EdpLogging {
	import LocalZookeeper._

	var zkServer: ZooKeeperServer = _
	val dataDirectory = System.getProperty("java.io.tmpdir")
	lazy val file = new File(dataDirectory, "zookeeper").getAbsoluteFile

	private def parse(zkProperties: Properties): (Int, Int, Int) = {
		val clientPort = zkProperties.getProperty(CLIENT_PORT, "2181").toInt
		val tickTime = zkProperties.getProperty(TICKTIME, "2000").toInt
		val numConnections = zkProperties.getProperty(NUM_CONNECTIONS, "5000").toInt
		(clientPort, tickTime, numConnections)
	}

	def zkPort: Int = zkServer.getClientPort

	def zkConnect: String = s"127.0.0.1:$zkPort"

	def start(): Unit = {
		val (clientPort, tickTime, numConnections) = parse(zkProperties)
		if (zkServer == null) {
			zkServer = new ZooKeeperServer(file, file, tickTime)
			val factory: NIOServerCnxnFactory = new NIOServerCnxnFactory
			factory.configure(new InetSocketAddress(clientPort), numConnections)
			try {
				factory.startup(zkServer)
			} catch {
				case e: Exception => throw new RuntimeException(e)
			}
			logInfo("Zookeeper Server is Started.")
		} else {
			logWarning("Zookeeper Server Started already.")
		}
	}

	def stop(): Unit = {
		if (zkServer.isRunning) {
			zkServer.shutdown()
			Util.delete(file)
			logInfo("Zookeeper Server Shutdown.")
		}
	}

	def isRunning: Boolean = {
		zkServer.isRunning
	}

	def isStop: Boolean = !isRunning
}

object LocalZookeeper {
	val CLIENT_PORT = "clientPort"
	val TICKTIME = "tickTime"
	val NUM_CONNECTIONS = "numConnections"
}
