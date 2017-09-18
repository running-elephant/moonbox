package edp.moonbox.common.kafka

import java.util.Properties

import edp.moonbox.common.zookeeper.LocalZookeeper
import org.scalatest.FunSuite

class LocalKafkaSuite extends FunSuite {
	test("LocalKafka Server") {
		val zkProperties = new Properties()
		zkProperties.put(LocalZookeeper.CLIENT_PORT, "2181")
		zkProperties.put(LocalZookeeper.TICKTIME, "2000")
		zkProperties.put(LocalZookeeper.NUM_CONNECTIONS, "5000")
		val zookeeper: LocalZookeeper = new LocalZookeeper(zkProperties)
		zookeeper.start()
		val kafka: LocalKafka = new LocalKafka(zookeeper.zkConnect)
		kafka.start()
		kafka.stop()
		zookeeper.stop()
	}
}
