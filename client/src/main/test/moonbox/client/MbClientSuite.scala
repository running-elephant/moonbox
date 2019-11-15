package moonbox.client

import org.scalatest.FunSuite

class MbClientSuite extends FunSuite {
	test("client") {
		val client: MbClient = new MbClient("localhost", 10010, "common@sally", "123456", "SPARK")
		client.isActive
	}

}
