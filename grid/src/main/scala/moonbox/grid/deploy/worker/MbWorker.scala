package moonbox.grid.deploy.worker

import java.text.SimpleDateFormat
import java.util.{Date, Locale}

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.cluster.Cluster
import akka.cluster.ClusterEvent.{CurrentClusterState, MemberUp}
import akka.cluster.client.ClusterClientReceptionist
import akka.cluster.singleton.{ClusterSingletonProxy, ClusterSingletonProxySettings}
import com.typesafe.config.ConfigFactory
import moonbox.common.{MbConf, MbLogging}
import moonbox.grid.deploy.DeployMessages._
import moonbox.grid.deploy.master.MbMaster
import moonbox.grid.config._

import scala.collection.mutable
import scala.collection.JavaConverters._
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

class MbWorker(param: MbWorkerParam, master: ActorRef) extends Actor with MbLogging {
	private val conf = param.conf
	private val workerId = generateWorkerId()
	private val WORKER_TIMEOUT_MS = conf.get(WORKER_TIMEOUT.key, WORKER_TIMEOUT.defaultValue.get)
	private val STATEREPORT_INTERVAL = conf.get(WORKER_STATEREPORT_INTERVAL.key, WORKER_STATEREPORT_INTERVAL.defaultValue.get)
	private val sessionIdToJobRunner = new mutable.HashMap[String, ActorRef]

	private var cluster: Cluster = _

	@scala.throws[Exception](classOf[Exception])
	override def preStart(): Unit = {
		cluster = Cluster(context.system)
		cluster.subscribe(self, classOf[MemberUp])
		// TODO init sparkContext
		master ! RegisterWorker(workerId,self, 100, 1000)
		heartbeat()
		workerLatestState()
	}

	override def receive: Receive = {

		case AllocateSession(username) =>

		case FreeSession(sessionId) =>

		case AssignJobToWorker(jobInfo) =>

		case RemoveJobFromWorker(jobId) =>

		case RegisterWorkerFailed =>

		case RegisteredWorker =>

	}

	def heartbeat(): Unit = {
		context.system.scheduler.schedule(
			FiniteDuration(0, MILLISECONDS),
			FiniteDuration(WORKER_TIMEOUT_MS, MILLISECONDS),
			master,
			Heartbeat
		)
	}

	def workerLatestState(): Unit = {
		context.system.scheduler.schedule(
			FiniteDuration(STATEREPORT_INTERVAL, MILLISECONDS),
			FiniteDuration(STATEREPORT_INTERVAL, MILLISECONDS),
			new Runnable {
				override def run(): Unit = {
					master ! WorkerLatestState(WorkerInfo(workerId, 100, 1000, self))
				}
			}
		)
	}

	private def createDateFormat = new SimpleDateFormat("yyyyMMddHHmmss", Locale.US)

	private def generateWorkerId(): String = {
		"worker-%s-%s-%d".format(createDateFormat.format(new Date), param.host, param.port)
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
				param,
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
