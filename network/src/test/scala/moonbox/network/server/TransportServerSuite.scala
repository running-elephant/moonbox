package moonbox.network.server

import io.netty.buffer.ByteBuf
import moonbox.network.TransportContext
import org.scalatest.FunSuite

class TransportServerSuite extends FunSuite {

	test("transport server") {
		val context: TransportContext = new TransportContext(new RpcHandler() {
			override def receive(request: ByteBuf, context: RpcCallContext): Unit = {

			}
		}, true)
		val server: TransportServer = context.createServer("localhost", 10010)
		assert(server.start() == 10010)
		server.close()
	}

}
