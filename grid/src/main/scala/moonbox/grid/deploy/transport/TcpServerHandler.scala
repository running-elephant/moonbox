package moonbox.grid.deploy.transport

import io.netty.buffer.{ByteBuf, Unpooled}
import moonbox.catalog.JdbcCatalog
import moonbox.common.{MbConf, MbLogging}
import moonbox.grid.deploy.app.AppMasterManager
import moonbox.grid.deploy.security.{LoginManager, Session}
import moonbox.network.server.{RpcCallContext, RpcHandler}
import moonbox.network.util.JavaUtils
import moonbox.protocol.protobuf.{AccessRequestPB, AccessResponsePB, HostPortPB}

class TcpServerHandler(conf: MbConf, catalog: JdbcCatalog) extends RpcHandler with MbLogging {
	private val loginManager = new LoginManager(conf, catalog)


	override def receive(request: ByteBuf, context: RpcCallContext): Unit = {

		val accessRequestPB = AccessRequestPB.getDefaultInstance.getParserForType.parseFrom(JavaUtils.byteBufToByteArray(request))
		val username = accessRequestPB.getUsername
		val password = accessRequestPB.getPassword
		val appType = accessRequestPB.getAppType

		try {
			val login: Session = loginManager.login(username, password)
			AppMasterManager.getAppMaster(appType)

			val response = Unpooled.wrappedBuffer(AccessResponsePB.newBuilder().setHostPort(HostPortPB.newBuilder().setHost("").setPort(10020).build()).build().toByteArray)
			context.reply(response)
		} catch {
			case e: Exception =>
				context.sendFailure(e)
		}

	}


}
