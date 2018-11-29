package moonbox.application.client

import java.net.InetAddress
import java.util.concurrent.CountDownLatch

import akka.actor.{ActorSystem, Props}
import com.typesafe.config.{Config, ConfigFactory}
import moonbox.common.{MbConf, MbLogging}

import scala.collection.JavaConverters._

object Main extends MbLogging{

	def runApp(args: Array[String]) = {

		val sparkConf = (0 until args.length / 2).map { index =>
			val key = index * 2
			val value = index * 2 + 1
			(args(key), args(value))
		}
		val mbConf = new MbConf()
		mbConf.set(sparkConf)

		sparkConf.foreach{println(_)}

		val akkaMap = Map("akka.actor.provider" ->"akka.remote.RemoteActorRefProvider",
			"akka.remote.enabled-transports.0" ->"akka.remote.netty.tcp",
			"akka.remote.netty.tcp.hostname" -> InetAddress.getLocalHost.getHostName,
			"akka.remote.netty.tcp.port" -> 0
		)
		val akkaConf: Config = ConfigFactory.parseMap(akkaMap.asJava)
		val system = ActorSystem("YarnAppSystem", akkaConf)
		val latch = new CountDownLatch(1)

		system.actorOf(Props(classOf[YarnAppActor], mbConf, system, latch), name = "YarnAppActor")

		latch.await()

	}

	def main(args: Array[String]) {
		runApp(args)
	}
}
