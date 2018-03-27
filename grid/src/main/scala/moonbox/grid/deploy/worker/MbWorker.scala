package moonbox.grid.deploy.worker

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.cluster.client.ClusterClientReceptionist
import akka.cluster.singleton.{ClusterSingletonProxy, ClusterSingletonProxySettings}
import com.typesafe.config.ConfigFactory
import moonbox.common.{MbConf, MbLogging}
import moonbox.grid.deploy.master.MbMaster

import scala.collection.JavaConverters._
import scala.concurrent.duration._

class MbWorker(conf: MbConf, master: ActorRef) extends Actor with MbLogging {


	@scala.throws[Exception](classOf[Exception])
	override def preStart(): Unit = {

	}

	override def receive: Receive = {
		case a => println(a)
	}
}

object MbWorker {
	val ROLE = "worker"
	val WORKER_NAME = "mbworker"
	val WORKER_PATH = s"/user/$WORKER_NAME"

	def main(args: Array[String]) {
		val conf = new MbConf(true)
		val param = new MbWorkerParam(args, conf)

		val akkaSystem = ActorSystem(param.clusterName, ConfigFactory.parseMap(param.akkaConfig.asJava))

		akkaSystem.actorOf(
			Props(
				classOf[MbWorker],
				param.conf,
				startMasterEndpoint(akkaSystem)),
			WORKER_NAME
		)
	}

	private def startMasterEndpoint(akkaSystem: ActorSystem): ActorRef = {
		val singletonProps = ClusterSingletonProxy.props(
			settings = ClusterSingletonProxySettings(akkaSystem)
				.withRole(MbMaster.ROLE).withBufferSize(5000)
				.withSingletonIdentificationInterval(new FiniteDuration(5, SECONDS)),
			singletonManagerPath = MbMaster.SINGLETON_MANAGER_PATH)

		val endpoint = akkaSystem.actorOf(singletonProps, MbMaster.SINGLETON_PROXY_NAME)
		ClusterClientReceptionist(akkaSystem).registerService(endpoint)
		endpoint
	}
}
