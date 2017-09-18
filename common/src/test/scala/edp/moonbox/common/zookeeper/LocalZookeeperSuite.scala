package edp.moonbox.common.zookeeper

import java.util.Properties

import org.scalatest.FunSuite

/**
  * Created by edp on 9/15/17.
  */
class LocalZookeeperSuite extends FunSuite {
	import LocalZookeeper._
	test("localZookeeper Server") {
		val zkProperties = new Properties()
		zkProperties.put(CLIENT_PORT, "2181")
		zkProperties.put(TICKTIME, "2000")
		zkProperties.put(NUM_CONNECTIONS, "5000")

		val zookeeper: LocalZookeeper = new LocalZookeeper(zkProperties)
		zookeeper.start()
		assert(zookeeper.isRunning)
		assert(zookeeper.zkPort == 2181)
		assert(zookeeper.zkConnect == "127.0.0.1:2181")
		zookeeper.stop()
		assert(zookeeper.isStop)
	}

}
