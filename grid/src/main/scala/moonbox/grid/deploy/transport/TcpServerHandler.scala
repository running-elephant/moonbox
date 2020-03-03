package moonbox.grid.deploy.transport

import java.util.concurrent.TimeUnit

import akka.actor.ActorRef
import akka.pattern.ask
import akka.util.Timeout
import io.netty.buffer.{ByteBuf, Unpooled}
import moonbox.catalog.JdbcCatalog
import moonbox.common.{MbConf, MbLogging}
import moonbox.grid.deploy.DeployMessages.{ApplicationAddressResponse, RequestApplicationAddress}
import moonbox.grid.deploy.security.{LoginManager, Session}
import moonbox.network.server.{RpcCallContext, RpcHandler}
import moonbox.network.util.JavaUtils
import moonbox.protocol.protobuf.{AccessRequestPB, AccessResponsePB, HostPortPB, SessionPB}

import scala.collection.JavaConverters._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

class TcpServerHandler(conf: MbConf, catalog: JdbcCatalog, master: ActorRef) extends RpcHandler with MbLogging {
	private val loginManager = new LoginManager(conf, catalog)
	private implicit val timeout = new Timeout(20, TimeUnit.SECONDS)

	override def receive(request: ByteBuf, context: RpcCallContext): Unit = {

		val accessRequestPB = AccessRequestPB.getDefaultInstance.getParserForType.parseFrom(JavaUtils.byteBufToByteArray(request))
		val username = accessRequestPB.getUsername
		val password = accessRequestPB.getPassword
		val appType = accessRequestPB.getAppType
		val appName = Option {
			val name = accessRequestPB.getAppName
			if (name != null && name.isEmpty) {
				null
			} else name
		}
		try {
			val session: Session = loginManager.login(username, password)
			logInfo(s"$username login successfully.")
			master.ask(RequestApplicationAddress(session, appType, appName)).mapTo[ApplicationAddressResponse].onComplete {
				case Success(addressResponse) =>
					if (addressResponse.found) {
						val host = addressResponse.host.get
						val port = addressResponse.port.get
						val hostPortPB = HostPortPB.newBuilder().setHost(host).setPort(port).build()

						val response = AccessResponsePB.newBuilder()
								.setHostPort(hostPortPB)
								.setSession(SessionPB.newBuilder().putAllSession(session.toMap.asJava)).build()
						context.reply(Unpooled.wrappedBuffer(response.toByteArray))
						logInfo(s"$username will connect to $host:$port")
					} else {
						val exception = addressResponse.exception.get
						context.sendFailure(exception)
						logWarning(exception.getMessage)
					}
				case Failure(e) =>
					context.sendFailure(e)
			}
		} catch {
			case e: Exception =>
				logWarning(s"$username request access failed ", e)
				context.sendFailure(e)
		}
	}
}
