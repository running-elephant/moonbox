package moonbox.network.client

import io.netty.buffer.ByteBuf
import moonbox.network.TransportContext
import moonbox.network.server.{RpcCallContext, RpcHandler, TransportServer}
import moonbox.network.util.JavaUtils
import org.scalatest.{BeforeAndAfterAll, FunSuite}

class TransportClientSuite extends FunSuite with BeforeAndAfterAll {
	private var context: TransportContext = _
	private var server: TransportServer = _

	override protected def beforeAll(): Unit = {
		context = new TransportContext(new RpcHandler {
			override def receive(request: ByteBuf, context: RpcCallContext): Unit = {
				if (JavaUtils.bytesToString(request) == "Ping") {
					context.reply(JavaUtils.stringToBytes("Pong"))
				} else if (JavaUtils.bytesToString(request) == "Hello") {
					context.reply(JavaUtils.stringToBytes("World"))
				} else {
					context.sendFailure(new Exception("Unknown"))
				}
			}
		}, true)
		server = context.createServer("localhost", 10010)
		server.start()
	}

	test("transport client") {
		var client: TransportClient = null
		try {
			client = context.createClientFactory().createClient("localhost", 10010, 10)
			assert("Pong" == JavaUtils.bytesToString(client.sendSync(JavaUtils.stringToBytes("Ping"), 5000)))
			assert("World" == JavaUtils.bytesToString(client.sendSync(JavaUtils.stringToBytes("Hello"), 5000)))
			intercept[java.lang.Exception](client.sendSync(JavaUtils.stringToBytes("Failure"), 5000))
		} finally {
			client.close()
		}
	}

	override protected def afterAll(): Unit = {
		server.close()
	}
}
