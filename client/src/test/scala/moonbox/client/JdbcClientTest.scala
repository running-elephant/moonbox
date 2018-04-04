package moonbox.client

import java.util.UUID

import moonbox.common.message.{EchoInbound, EchoOutbound, JdbcLoginInbound, JdbcLoginOutbound}
import org.scalatest.{BeforeAndAfterAll, FunSuite}

class JdbcClientTest extends FunSuite with BeforeAndAfterAll {
  val CONNECT_TIMEOUT = 5000
  val RESULT_RESPONSE_TIMEOUT = 5000
  val host = "localhost"
  val port = 8080
  var client: JdbcClient = _

  override def beforeAll() {
//    new Thread() {
//      override def run() = {
//        new JdbcServer(host, port, null, null).start()
//      }
//    }.start()
//    Thread.sleep(1000)
    client = new JdbcClient(host, port)
  }

  override def afterAll() {

  }

  test("testSendAndReceive") {
    var nullCounter: Long = 0
    var count = 0
    var seq = Seq.empty[(String, Int)]
    while (count < 10) {
      val sin = UUID.randomUUID().toString
      val msgId = client.getMessageId()
      val message = EchoInbound(msgId, sin)
      val resp = client.sendAndReceive(message, RESULT_RESPONSE_TIMEOUT)
      if (resp == null) {
        seq +:= ("null id: ", count)
        nullCounter += 1
      }
      assert(resp.isInstanceOf[EchoOutbound])
      val respp = resp.asInstanceOf[EchoOutbound]
      assert(respp.messageId == message.messageId && respp.content == message.content)
      count += 1
    }
    println(s"nullCounter = $nullCounter")
    seq.foreach(println)
  }

  test("testSendOneWayMessage") {
    client.sendOneWayMessage(OneWayMessageTest("Hello server!"))

  }

  test("testSendWithCallback") {
    client.sendWithCallback(EchoInbound(client.getMessageId(), "testSendWithCallback"), resp => println("Callback run: " + resp))
  }

  test("test login") {
    var count = 0
    while (count < 1000) {
      val recv = client.sendAndReceive(JdbcLoginInbound(client.getMessageId(), client.clientId, "ROOT", "123456", "default"), 5000)
      assert(recv.isInstanceOf[JdbcLoginOutbound])
      count += 1
    }
  }

}
