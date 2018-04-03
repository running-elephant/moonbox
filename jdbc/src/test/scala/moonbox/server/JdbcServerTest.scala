package moonbox.server

import moonbox.grid.deploy.transport.server.JdbcServer
import org.scalatest.{BeforeAndAfterAll, FunSuite}

class JdbcServerTest extends FunSuite with BeforeAndAfterAll {

  val host = "localhost"
  val port = 8080

  override def beforeAll() {

  }

  override def afterAll() {

  }

  test("testStart") {
    new JdbcServer(host, port, null, null).start()
  }

}
