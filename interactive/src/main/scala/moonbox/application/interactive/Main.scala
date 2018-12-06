package moonbox.application.interactive


import akka.actor.{Actor, ActorSystem, Address, Props}
import com.typesafe.config.{Config, ConfigFactory}
import moonbox.common.util.Utils
import moonbox.common.{MbConf, MbLogging}
import moonbox.core.MbSession
import moonbox.grid.{LogMessage, MbActor}
import moonbox.grid.deploy.DeployMessages.MasterChanged

import scala.collection.JavaConverters._

object Main extends MbLogging {

	def main(args: Array[String]) {
		val conf = new MbConf()
		val keyValues = for (i <- 0 until(args.length, 2)) yield (args(i), args(i+1))
		keyValues.foreach { case (k, v) => conf.set(k, v) }

		val akkaConfig = Map("akka.actor.provider" ->"akka.remote.RemoteActorRefProvider",
			"akka.remote.enabled-transports.0" ->"akka.remote.netty.tcp",
			"akka.remote.netty.tcp.hostname" -> Utils.localHostName(),
			"akka.remote.netty.tcp.port" -> "0"
		)

		val akkaConf: Config = ConfigFactory.parseMap(akkaConfig.asJava)
		val system = ActorSystem("Moonbox", akkaConf)

		system.actorOf(Props(classOf[Main], conf), name = "interactive")
		Thread.currentThread().join()
	}
}

class Main(conf: MbConf) extends MbActor with LogMessage with MbLogging {

	@scala.throws[Exception](classOf[Exception])
	override def preStart(): Unit = {
		self ! MasterChanged(self)
	}

	override def handleMessage: Receive = {
		case MasterChanged(masterRef) =>
	}

	override def onDisconnected(remoteAddress: Address): Unit = {

	}
}
