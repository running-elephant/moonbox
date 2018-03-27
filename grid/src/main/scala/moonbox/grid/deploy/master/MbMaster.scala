package moonbox.grid.deploy.master

import java.text.SimpleDateFormat
import java.util.{Date, Locale}

import akka.actor.{Actor, ActorRef, ActorSystem, Cancellable, PoisonPill, Props, Terminated}
import akka.cluster.Cluster
import akka.cluster.client.ClusterClientReceptionist
import akka.cluster.singleton.{ClusterSingletonManager, ClusterSingletonManagerSettings, ClusterSingletonProxy, ClusterSingletonProxySettings}
import com.typesafe.config.ConfigFactory
import moonbox.common.util.Utils
import moonbox.common.{MbConf, MbLogging}
import moonbox.core.CatalogContext
import moonbox.core.parser.MbParser
import moonbox.grid.{JobInfo, JobState}
import moonbox.grid.api._
import moonbox.grid.deploy.worker.WorkerInfo
import moonbox.grid.config._
import moonbox.grid.deploy.DeployMessages._
import moonbox.grid.deploy.MbService
import moonbox.grid.deploy.authenticate.LoginManager
import moonbox.grid.deploy.master.MbMasterMessages.{CheckForWorkerTimeOut, ScheduleJob}
import moonbox.grid.deploy.rest.RestServer
import moonbox.grid.deploy.transport.TransportServer

import scala.concurrent.ExecutionContext.Implicits.global
import scala.collection.JavaConverters._
import scala.concurrent.duration._
import scala.collection._
import scala.collection.mutable.ArrayBuffer
import scala.concurrent.Future

class MbMaster(param: MbMasterParam, implicit val akkaSystem: ActorSystem) extends Actor with MbLogging {
	import MbMaster._
	// For job IDs
	private def createDateFormat = new SimpleDateFormat("yyyyMMddHHmmss", Locale.US)

	private val conf: MbConf = param.conf
	private val WORKER_TIMEOUT_MS = conf.get(WORKER_TIMEOUT.key, WORKER_TIMEOUT.defaultValue.get)
	private val PERSISTENCE_MODE = conf.get(PERSIST_IMPLEMENTATION.key, PERSIST_IMPLEMENTATION.defaultValue.get)

	private val waitingJobs = new mutable.Queue[JobInfo]
	private val runningJobs = new mutable.HashMap[String, JobInfo]
	private val completeJobs = new mutable.HashMap[String, JobInfo]
	private val retainedCompleteJobs = new ArrayBuffer[String]()
	private val failedJobs = new mutable.HashMap[String, JobInfo]
	private val retainedFailedJobs = new ArrayBuffer[String]()
	private var nextJobNumber = 0
	private val workers = new mutable.HashMap[ActorRef, WorkerInfo]

	private val adhocUserToWorker = new mutable.HashMap[String, ActorRef]()
	private val jobIdToClient = new mutable.HashMap[String, ActorRef]()
	private val jobIdToWorker = new mutable.HashMap[String, ActorRef]()

	private var cluster: Cluster = _
	private var catalogContext: CatalogContext = _
	private var loginManager: LoginManager = _
	private var persistenceEngine: PersistenceEngine = _
	private var singletonMaster: ActorRef = _
	private var resultGetter: ActorRef = _
	private var checkForWorkerTimeOutTask: Cancellable = _

	private val restServerEnabled = conf.get(REST_SERVER_ENABLE.key, REST_SERVER_ENABLE.defaultValue.get)
	private var restServer: Option[RestServer] = None
	private var restServerBoundPort: Option[Int] = None

	private val tcpServerEnabled = conf.get(TCP_SERVER_ENABLE.key, TCP_SERVER_ENABLE.defaultValue.get)
	private var tcpServer: Option[TransportServer] = None
	private var tcpServerBoundPort: Option[Int] = None

	private val mbParser = new MbParser

	override def receive: Receive = {
		case request: MbRequest =>
			// TODO privilege check
			process.apply(request)
		case control =>
			internal.apply(control)
	}

	private def internal: Receive = {
		case ScheduleJob =>
			schedule()
		case CheckForWorkerTimeOut =>
			timeOutDeadWorkers()
		case RegisterWorker(id, endpoint, cores, memory) =>
			logInfo(s"Registering worker $endpoint with $cores cores, $memory RAM")
			if (workers.contains(endpoint)) {
				endpoint ! RegisterWorkerFailed(s"Duplicate worker $endpoint")
			} else {
				context.watch(sender())
				val workerInfo = new WorkerInfo(id, cores, memory, endpoint)
				workers.put(sender(), workerInfo)
				persistenceEngine.addWorker(workerInfo)
				endpoint ! RegisteredWorker(self)
				schedule()
			}
		case JobStateChanged(jobId, state, message) =>
		case WorkerLatestState =>
		case Heartbeat =>
		case Terminated =>
	}

	private def process: Receive = {
		// adhoc mode context dependent
		// schedule successfully or failed
		case MbRequest(username, query: JobQuery) =>
			val client = sender()
			Future {
				var job: JobInfo = null
				try {
					job= createJob(username, query.sqls, client)
					jobQuery(job)
				} catch {
					case e: Exception =>
						client ! MbResponse(username, JobFailed(job.jobId, e.getMessage))
				}
			}

		// batch mode context independent
		case MbRequest(username, JobSubmit(sqls, async)) =>
			val client = sender()
			Future {
				var job: JobInfo = null
				try {
					job = createJob(username, sqls, client)
					waitingJobs.enqueue(job)
					persistenceEngine.addJob(job)
					if (async) {
						client ! MbResponse(username, JobAccepted(job.jobId))
					}
				} catch {
					case e: Exception =>
						client ! MbResponse(username, JobFailed(job.jobId, e.getMessage))
				}
			}

		case MbRequest(username, JobProgress(jobId)) =>
			val client = sender()
			Future {
				val jobInfo = runningJobs.get(jobId).orElse(
					completeJobs.get(jobId).orElse(
						failedJobs.get(jobId).orElse(
							waitingJobs.find(_.jobId == jobId)
						)
					)
				)
				jobInfo match {
					case Some(job) =>
						client ! MbResponse(username, JobProgressResponse(jobId, job))
					case None =>
						client ! MbResponse(username, JobFailed(jobId, s"Job $jobId does not exist or has been removed."))
				}
			}

		case MbRequest(username, JobCancel(jobId)) =>
			val client = sender()
			Future {
				waitingJobs.find(_.jobId == jobId) match {
					case Some(jobInfo) =>
						waitingJobs.dequeueFirst(_.jobId == jobId)
						persistenceEngine.removeJob(jobInfo)
					case None =>
						runningJobs.get(jobId) match {
							case Some(jobInfo) =>
								jobIdToWorker.get(jobId) match {
									case Some(worker) =>
										worker ! RemoveJobFromWorker(jobId)
										client ! MbResponse(username, JobCancelSuccess(jobId))
									case None =>
										client ! MbResponse(username, JobCancelSuccess(jobId))
								}
							case None =>
								client ! MbResponse(username, JobCancelSuccess(jobId))
						}
				}
			}
	}

	private def jobQuery(job: JobInfo): Unit = {
		val worker = adhocUserToWorker.getOrElse(job.user, {
			// TODO scheduler algorithm
			val selected = null.asInstanceOf[ActorRef]
			adhocUserToWorker.put(job.user, selected)
			selected
		})
		worker ! AssignJobToWorker(job)
	}


	private def timeOutDeadWorkers(): Unit = {
		val currentTime = Utils.now
		val toRemove = workers.filter {
			case (endpoint, workerInfo) =>
				workerInfo.lastHeartbeat < currentTime - WORKER_TIMEOUT_MS
		}.keys
		workers.--(toRemove)
	}

	private def schedule(): Unit = {

	}

	private def createJob(username: String, sqls: Seq[String], client: ActorRef): JobInfo = {
		val commands = sqls.map(mbParser.parsePlan)
		val now = Utils.now
		val date = new Date(now)
		JobInfo(
			jobId = newJobId(date),
			cmds = commands,
			status = JobState.WAITING,
			errorMessage = None,
			user = username,
			submitTime = now,
			updateTime = now,
			client = client
		)
	}

	private def newJobId(submitDate: Date): String = {
		val jobId = "job-%s-%05d".format(createDateFormat.format(submitDate), nextJobNumber)
		nextJobNumber += 1
		jobId
	}

	@scala.throws[Exception](classOf[Exception])
	override def preStart(): Unit = {
		logInfo("PreStart master ...")
		cluster = Cluster.get(akkaSystem)

		catalogContext = new CatalogContext(conf)

		loginManager = new LoginManager(catalogContext)

		checkForWorkerTimeOutTask = akkaSystem.scheduler.schedule(
			FiniteDuration(0, MILLISECONDS),
			FiniteDuration(WORKER_TIMEOUT_MS, MILLISECONDS),
			self,
			CheckForWorkerTimeOut
		)

		singletonMaster = startMasterEndpoint(akkaSystem)
		resultGetter = akkaSystem.actorOf(Props(classOf[ResultGetter]), "result-getter")

		val serviceImpl = new MbService(loginManager, singletonMaster, resultGetter)

		if (restServerEnabled) {
			restServer = Some(new RestServer(param.host, param.restPort, conf, serviceImpl, akkaSystem))
		}
		restServerBoundPort = restServer.map(_.start())

		if (tcpServerEnabled) {
			tcpServer = Some(new TransportServer(serviceImpl))
		}
		tcpServerBoundPort = tcpServer.map(_.start())

		persistenceEngine = PERSISTENCE_MODE match {
			case "ZOOKEEPER" =>
				logInfo("Persisting state to Zookeeper")
				val zkFactory = new ZookeeperPersistModeFactory(conf, akkaSystem)
				zkFactory.createPersistEngine()
			case "HDFS" =>
				logInfo("Persisting state to Hdfs")
				val hdfsFactory = new HdfsPersistModeFactory(conf)
				hdfsFactory.createPersistEngine()
			case _ =>
				new BlackHolePersistenceEngine
		}

		val initialDelay = conf.get(SCHEDULER_INITIAL_WAIT.key, SCHEDULER_INITIAL_WAIT.defaultValue.get)
		val interval = conf.get(SCHEDULER_INTERVAL.key, SCHEDULER_INTERVAL.defaultValue.get)

		akkaSystem.scheduler.schedule(
			FiniteDuration(initialDelay, MILLISECONDS),
			FiniteDuration(interval, MILLISECONDS),
			self,
			ScheduleJob
		)
	}

	@scala.throws[Exception](classOf[Exception])
	override def postStop(): Unit = {

		if (checkForWorkerTimeOutTask != null && !checkForWorkerTimeOutTask.isCancelled) {
			checkForWorkerTimeOutTask.cancel()
		}

		restServer.foreach(_.stop())
		tcpServer.foreach(_.stop())

		persistenceEngine.close()
	}

	private def startMasterEndpoint(akkaSystem: ActorSystem): ActorRef = {
		val singletonProps = ClusterSingletonProxy.props(
			settings = ClusterSingletonProxySettings(akkaSystem)
				.withRole(ROLE).withBufferSize(5000)
				.withSingletonIdentificationInterval(new FiniteDuration(5, SECONDS)),
			singletonManagerPath = SINGLETON_MANAGER_PATH)

		val endpoint = akkaSystem.actorOf(singletonProps, SINGLETON_PROXY_NAME)
		ClusterClientReceptionist(akkaSystem).registerService(endpoint)
		endpoint
	}
}

object MbMaster {
	val ROLE = "master"
	val RECEPTIONIST_PATH = "/system/receptionist"
	val MASTER_NAME = "mbmaster"
	val SINGLETON_PROXY_NAME = "singleton-master-proxy"
	val SINGLETON_MANAGER_PATH = s"/user/$MASTER_NAME"

	def main(args: Array[String]) {
		val conf = new MbConf()
		val param = new MbMasterParam(args, conf)
		val akkaSystem = ActorSystem(param.clusterName, ConfigFactory.parseMap(param.akkaConfig.asJava))
		val masterProp = ClusterSingletonManager.props(
			singletonProps = Props(classOf[MbMaster], param, akkaSystem),
			PoisonPill,
			ClusterSingletonManagerSettings(akkaSystem).withRole(ROLE)
		)
		akkaSystem.actorOf(masterProp, MASTER_NAME)
	}

}
