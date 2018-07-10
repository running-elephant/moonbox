package moonbox.grid.deploy.worker

import java.text.SimpleDateFormat
import java.util.{Date, Locale, UUID}

import akka.actor.{Actor, ActorRef, ActorSystem, Props, Terminated}
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
	private val sessionIdToJobRunner = new mutable.HashMap[String, ActorRef]  //adhoc
	private val jobIdToJobRunner = new mutable.HashMap[String, ActorRef]      //batch id - runner
    private val runnerToJobId = new mutable.HashMap[ActorRef, String]         //batch runner - id

	private var cluster: Cluster = _

	@scala.throws[Exception](classOf[Exception])
	override def preStart(): Unit = {
		cluster = Cluster(context.system)
		cluster.subscribe(self, classOf[MemberUp])
		// TODO init sparkContext
		logDebug(s"MbWorker::preStart RegisterWorker")
		master ! RegisterWorker(workerId, self, 100, 1000)
		//heartbeat()
		workerLatestState()
		logInfo(s"MbWorker start successfully.")
	}

	override def postStop(): Unit = {
		cluster.unsubscribe(self)
	}

	override def receive: Receive = {

		case AllocateSession(username, database) =>
			val requester = sender()
			logInfo(s"MbWorker::AllocateSession $requester $username $database")
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
			logInfo(s"MbWorker::FreeSession $requester $sessionId")
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
			logInfo(s"MbWorker::AssignJobToWorker $jobInfo")
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
                    jobIdToJobRunner.put(jobInfo.jobId, runner)  //for cancel
                    runnerToJobId.put(runner, jobInfo.jobId)
                    context.watch(runner) // terminate
					runner forward RunJob(jobInfo)
			}

		case kill@RemoveJobFromWorker(jobInfo) =>
			logInfo(s"MbWorker::RemoveJobFromWorker $jobInfo")
            jobInfo.sessionId match {
                case Some(sessionId) => // adhoc
                    sessionIdToJobRunner.get(sessionId) match {
                        case Some(runner) => runner forward CancelJob(jobInfo.jobId)
                        case None => logWarning(s"JobRunner adhoc  $sessionId ${jobInfo.jobId} lost.")
                    }
                case None =>  //batch
                    jobIdToJobRunner.get(jobInfo.jobId) match {
                        case Some(runner) => runner forward CancelJob(jobInfo.jobId)
                        case None => logWarning(s"JobRunner batch ${jobInfo.jobId} lost.")
                    }
            }

		case r: RegisterWorkerFailed =>  logDebug(s"RegisterWorkerFailed ${r.message} ")
		case r: RegisteredWorker =>			logDebug(s"RegisteredWorker ${r.master}")
        case Terminated(actor)  => context.unwatch(actor)  //only for batch
            val jobId: Option[String] = runnerToJobId.get(actor)
            if(jobId.isDefined) {
                jobIdToJobRunner.remove(jobId.get)
            }
            runnerToJobId.remove(actor)
		case MasterChanged =>
			logDebug(s"MbWorker::MasterChanged RegisterWorker")
			context.system.scheduler.scheduleOnce(FiniteDuration(4, SECONDS),
				master, RegisterWorker(workerId, self, 100, 1000))

		case m@MemberUp(member) =>
			logDebug(s"MbWorker::MemberUp and register to $member")
			register(member)

		case state: CurrentClusterState =>
			logDebug(s"MbWorker::CurrentClusterState $state")
			state.members.filter(_.status == MemberStatus.Up).foreach(register)

		case a =>
			logWarning(s"MbWorker::receive UNKNOWN message $a")

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
					//TODO: get sparkContext and ResourceMonitor
					//val monitor = MbSession.getMbSession(conf).mixcal.getResourceMonitor
					//val cpuCores = monitor.clusterFreeCore
					//val memoryUsage = monitor.clusterFreeMemory
					//val workerInfo = WorkerInfo(workerId, cpuCores._1, memoryUsage._1, self)
					//workerInfo.coresFree = cpuCores._2
					//workerInfo.memoryFree = memoryUsage._2
					//master ! WorkerLatestState(workerInfo)
					val workerInfo = WorkerInfo(workerId, 100, 1000, self)
					workerInfo.coresFree = 100
					workerInfo.memoryFree = 1000
					master ! WorkerLatestState(workerInfo)
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
			akkaSystem.actorOf(
				Props(
					classOf[MbWorker],
					param,
					startMasterEndpoint(akkaSystem)),
				WORKER_NAME
			)
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
