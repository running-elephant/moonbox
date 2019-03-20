package moonbox.application.batch.hive

import org.scalatest.FunSuite

class MainSuite extends FunSuite {
	test("main") {
		Main.main(Array(
			"username", "sally",
			"sqls", "select max(eid) from employee",
			"driverId", "aaaaaaa"))
	}
}
