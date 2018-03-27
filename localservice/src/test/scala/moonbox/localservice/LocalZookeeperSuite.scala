package moonbox.localservice

import java.util.Properties

import org.scalatest.{BeforeAndAfterAll, FunSuite}

class LocalZookeeperSuite extends FunSuite with BeforeAndAfterAll {
	var zookeeper: LocalZookeeper = _

	override protected def beforeAll(): Unit = {
		zookeeper = new LocalZookeeper(new Properties())
	}

	override protected def afterAll(): Unit = {
		if (zookeeper.isRunning) zookeeper.stop()
	}

	test("start server") {
		zookeeper.start()
		assert(zookeeper.isRunning)
	}

	test("stop server") {
		zookeeper.stop()
		assert(zookeeper.isStop)
	}

}
