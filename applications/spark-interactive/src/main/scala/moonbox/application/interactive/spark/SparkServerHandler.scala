package moonbox.application.interactive.spark

import io.netty.buffer.ByteBuf
import moonbox.network.server.{RpcCallContext, RpcHandler}
import moonbox.network.util.JavaUtils._
import moonbox.protocol.protobuf.AppRequestMessage

class SparkServerHandler extends RpcHandler {

	override def receive(request: ByteBuf, context: RpcCallContext): Unit = {

		val req = AppRequestMessage.getDefaultInstance.getParserForType.parseFrom(byteBufToByteArray(request))
		if (req.hasOpenSession) {
			handleOpenSession()
		} else if (req.hasExecute) {
			handleExecute()
		} else if (req.hasExecuteResult) {
			handleExecuteResult()
		} else if (req.hasExecteCancel) {
			handleExecuteCancel()
		} else if (req.hasCloseSession) {
			handleCloseSession()
		} else {
			context.sendFailure(new UnsupportedOperationException("Unsupport app request: " + req))
		}

	}

	private def handleOpenSession(): Unit = {

	}

	private def handleCloseSession(): Unit = {

	}

	private def handleExecute(): Unit = {

	}

	private def handleExecuteResult(): Unit = {

	}

	private def handleExecuteCancel(): Unit = {

	}
}
