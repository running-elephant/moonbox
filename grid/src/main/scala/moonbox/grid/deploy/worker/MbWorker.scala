package moonbox.grid.deploy.worker

import java.text.SimpleDateFormat
import java.util.{Date, Locale, UUID}

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.cluster.ClusterEvent.{CurrentClusterState, MemberUp}
import akka.cluster.client.ClusterClientReceptionist
import akka.cluster.singleton.{ClusterSingletonProxy, ClusterSingletonProxySettings}
import akka.cluster.{Cluster, Member, MemberStatus}
import com.typesafe.config.ConfigFactory
import moonbox.common.{MbConf, MbLogging}
import moonbox.core.MbSession
import moonbox.grid.{Failed, JobState}
import moonbox.grid.config._
import moonbox.grid.deploy.DeployMessages._
import moonbox.grid.deploy.master.MbMaster

import scala.collection.JavaConverters._
import scala.collection.mutable
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration._
import scala.util.{Failure, Success}

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
		master ! RegisterWorker(workerId, self, 100, 1000)
		//heartbeat()
		workerLatestState()
	}

	override def postStop(): Unit = {
		cluster.unsubscribe(self)
	}

	override def receive: Receive = {

		case AllocateSession(username, database) =>
			val requester = sender()
			Future {
				val mbSession = MbSession.getMbSession(conf).bindUser(username, database)
				val runner = context.actorOf(Props(classOf[Runner], conf, mbSession))
				val sessionId = newSessionId()
				sessionIdToJobRunner.put(sessionId, runner)
				sessionId
			}.onComplete {
				case Success(sessionId) =>
					requester ! AllocatedSession(sessionId)
				case Failure(e) =>
					requester ! AllocateSessionFailed(e.getMessage)
			}
		case FreeSession(sessionId) =>
			val requester = sender()
			Future {
				if (sessionIdToJobRunner.get(sessionId).isDefined) {
					val runner = sessionIdToJobRunner.get(sessionId).head
					runner ! KillRunner //sessionId is not used
					sessionIdToJobRunner.remove(sessionId)
				}
				sessionId
			}.onComplete {
				case Success(sessionId) =>
					requester ! FreedSession(sessionId)
				case Failure(e) =>
					requester ! FreeSessionFailed(e.getMessage)
			}
		case assign@AssignJobToWorker(jobInfo) =>
			logInfo(s"AssignJobToWorker ${jobInfo}")
			val requester = sender()
			jobInfo.sessionId match {
				case Some(sessionId) => // adhoc
					sessionIdToJobRunner.get(sessionId) match {
						case Some(runner) =>
							runner forward RunJob(jobInfo)
						case None =>
							requester ! JobStateChanged(jobInfo.jobId, JobState.FAILED, Failed("Session lost."))
					}
				case None => // batch
					val mb = MbSession.getMbSession(conf).bindUser(jobInfo.username.get)
					val runner = context.actorOf(Props(classOf[Runner], conf, mb))
					runner forward RunJob(jobInfo)
			}

		case kill@RemoveJobFromWorker(jobId) =>
			logDebug(s"RemoveJobFromWorker ${jobId}")
			sessionIdToJobRunner.get(jobId) match {
				case Some(runner) => runner forward CancelJob(jobId)
				case None => logWarning(s"JobRunner $jobId lost.")
			}

		case r: RegisterWorkerFailed =>  logDebug(s"RegisterWorkerFailed ${r.message} ")
		case r: RegisteredWorker =>			logDebug(s"RegisteredWorker ${r.master}")

		case MasterChanged =>
			logDebug(s"MasterChanged ")
			context.system.scheduler.scheduleOnce(FiniteDuration(10, SECONDS),
				master, RegisterWorker(workerId, self, 100, 1000))

		case m@MemberUp(member) =>
			logDebug(s"MemberUp and register to $member")
			register(member)

		case state: CurrentClusterState =>
			logDebug(s"CurrentClusterState ${state}")
			state.members.filter(_.status == MemberStatus.Up).foreach(register)

		case a =>
			logWarning(s"receive UNKNOWN message ${a}")

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
					println(s"workerLatestState $master")
					master ! WorkerLatestState(WorkerInfo(workerId, 100, 1000, self))
				}
			}
		)
	}

	private def createDateFormat = new SimpleDateFormat("yyyyMMddHHmmss", Locale.US)

	private def generateWorkerId(): String = {
		"worker-%s-%s-%d".format(createDateFormat.format(new Date), param.host, param.port)
	}

	private def newSessionId(): String = {
		UUID.randomUUID().toString
	}

	private def register(m: Member): Unit = {
		logDebug(s"register to $master, ${m.hasRole(MbMaster.ROLE)}")
		if (m.hasRole(MbMaster.ROLE)) {
			master ! RegisterWorker(workerId, self, 100, 1000)
		}
	}

}

object MbWorker extends MbLogging {
	val ROLE = "worker"
	val WORKER_NAME = "mbworker"
	val WORKER_PATH = s"/user/$WORKER_NAME"

	def main(args: Array[String]) {
		val conf = new MbConf(true)
		val param = new MbWorkerParam(args, conf)

		val akkaSystem = ActorSystem(param.clusterName, ConfigFactory.parseMap(param.akkaConfig.asJava))

		try {
			val worker = akkaSystem.actorOf(
				Props(
					classOf[MbWorker],
					param,
					startMasterEndpoint(akkaSystem)),
				WORKER_NAME
			)
			logInfo(s"MbWorker $worker start successfully.")
		} catch {
			case e: Exception =>
				logError(e.getMessage)
				akkaSystem.terminate()
		}
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
