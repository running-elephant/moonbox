package moonbox.localservice

import org.scalatest.{BeforeAndAfterAll, FunSuite}

class LocalJdbcSuite extends FunSuite with BeforeAndAfterAll {

	var jdbcServer: LocalJdbc = _

	override protected def beforeAll(): Unit = {
		jdbcServer = new LocalJdbc()
	}

	override protected def afterAll(): Unit = {
		if (jdbcServer.isRunning) jdbcServer.stop()
	}

	test("start server") {
		jdbcServer.start()
		assert(jdbcServer.isRunning)
	}

	test("stop server") {
		jdbcServer.stop()
		assert(jdbcServer.isStop)
	}


}
