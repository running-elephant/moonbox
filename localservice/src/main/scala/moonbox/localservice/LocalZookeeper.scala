package moonbox.localservice

import java.io.File
import java.net.InetSocketAddress
import java.util.Properties

import moonbox.common.MbLogging
import moonbox.common.util.Utils
import org.apache.zookeeper.server.{NIOServerCnxnFactory, ZooKeeperServer}

object LocalZookeeper {
	val DEFAULT_ZK_PORT = 2181
}

class LocalZookeeper(properties: Properties) extends MbLogging {
	def this() = this(new Properties())

	import LocalZookeeper._
	private var zkServer: ZooKeeperServer = _
	private lazy val file = new File(System.getProperty("java.io.tmpdir"), "zookeeper").getAbsoluteFile
	private val tickTime = properties.getProperty("tickTime", "2000").toInt
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
