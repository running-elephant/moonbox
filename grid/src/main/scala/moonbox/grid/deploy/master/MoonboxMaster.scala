package moonbox.grid.deploy.master

import java.text.SimpleDateFormat
import java.util.{Date, Locale}

import akka.actor.{ActorSystem, Address, Cancellable, Props}
import akka.util.Timeout
import com.typesafe.config.ConfigFactory
import moonbox.common.{MbConf, MbLogging}
import moonbox.core.CatalogContext
import moonbox.grid.{LogMessage, MbActor}
import moonbox.grid.config._
import moonbox.grid.deploy.audit.BlackHoleAuditLogger
import moonbox.grid.deploy.{ClusterDriverDescription, DeployMessages, MbService}
import DeployMessages._
import moonbox.grid.deploy.master.DriverState.DriverState
import moonbox.grid.deploy.worker.WorkerState
import moonbox.grid.deploy.messages.Message._
import moonbox.grid.deploy.thrift.ThriftServer
import moonbox.grid.deploy.rest.RestServer
import moonbox.grid.deploy.transport.TransportServer
import moonbox.grid.timer.{TimedEventService, TimedEventServiceImpl}

import scala.collection.JavaConverters._
import scala.concurrent.duration._
import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Random

class MoonboxMaster(
	val system: ActorSystem,
	val conf: MbConf) extends MbActor with LogMessage with LeaderElectable with MbLogging {

	// for batch application IDs
	private def createDateFormat = new SimpleDateFormat("yyyyMMddHHmmss", Locale.ROOT)

	private implicit val ASK_TIMEOUT = Timeout(FiniteDuration(60, SECONDS))
	private val WORKER_TIMEOUT_MS = conf.get(WORKER_TIMEOUT)
	private val host = address.host.orNull
	checkHost()

	private var state = RecoveryState.STANDBY

	private val idToWorker = new mutable.HashMap[String, WorkerInfo]
	private val addressToWorker = new mutable.HashMap[Address, WorkerInfo]
	private val workers = new mutable.HashSet[WorkerInfo]

	// for batch
	private val drivers = new mutable.HashSet[DriverInfo]
	private val waitingDrivers = new ArrayBuffer[DriverInfo]
	private val completedDrivers = new ArrayBuffer[DriverInfo]
	private var nextBatchDriverNumber = 0

	private var persistenceEngine: PersistenceEngine = _
	private var leaderElectionAgent: LeaderElectionAgent = _

	private var recoveryCompletionScheduler: Cancellable = _
	private var checkForWorkerTimeOutTask: Cancellable = _

	private var catalogContext: CatalogContext = _
	private var timedEventService: TimedEventService = _

	private var restServer: Option[RestServer] = None
	private var restServerBoundPort: Option[Int] = None

	private var tcpServer: Option[TransportServer] = None
	private var tcpServerBoundPort: Option[Int] = None

	private var odbcServer: Option[ThriftServer] = None
	private var odbcServerBoundPort: Option[Int] = None


	@scala.throws[Exception](classOf[Exception])
	override def preStart(): Unit = {

		// start catalog
		try {
			catalogContext = new CatalogContext(conf)
		} catch {
			case e: Exception =>
				logError("Could not start the CatalogContext.", e)
				gracefullyShutdown()
		}

		// for check DEAD worker
		checkForWorkerTimeOutTask = system.scheduler.schedule(
			new FiniteDuration(0, SECONDS),
			new FiniteDuration(WORKER_TIMEOUT_MS, MILLISECONDS),
			self,
			CheckForWorkerTimeOut
		)

		// start persist engine and election agent
		try {
			val (persistenceEngine_, leaderElectionAgent_) = conf.get(RECOVERY_MODE).toUpperCase match {
				case "ZOOKEEPER" =>
					logInfo("Persisting recovery state to Zookeeper.")
					val zkFactory = new ZookeeperRecoveryModeFactory(conf, system)
					(zkFactory.createPersistEngine(), zkFactory.createLeaderElectionAgent(this))
				case _ =>
					(new BlackHolePersistenceEngine,  new MonarchyLeaderAgent(this))
			}
			persistenceEngine = persistenceEngine_
			leaderElectionAgent = leaderElectionAgent_
		} catch {
			case e: Exception =>
				logError("Could not start the recovery service.", e)
				gracefullyShutdown()
		}

		// TODO
		val mbService = new MbService(conf, catalogContext, self, new BlackHoleAuditLogger)

		// start timer
		try {
			if (conf.get(TIMER_SERVICE_ENABLE)) {
				timedEventService = new TimedEventServiceImpl(conf)
			}
		} catch {
			case e: Exception =>
				logError("Could not start timer event scheduler.", e)
				gracefullyShutdown()
		}

		// start rest server if it is enabled
		try {
			if (conf.get(REST_SERVER_ENABLE)) {
				val port = conf.get(REST_SERVER_PORT)
				restServer = Some(new RestServer(host, port, conf, mbService, system))
				restServerBoundPort = restServer.map(_.start())
			}
		} catch {
			case e: Exception =>
				logError("Could not start rest server.", e)
				gracefullyShutdown()
		}

		// start tcp server if it is enabled
		try {
			if (conf.get(TCP_SERVER_ENABLE)) {
				val port = conf.get(TCP_SERVER_PORT)
				tcpServer = Some(new TransportServer(host, port, conf, mbService))
				tcpServerBoundPort = tcpServer.map(_.start())
			}
		} catch {
			case e: Exception =>
				logError("Could not start tcp server.", e)
				gracefullyShutdown()
		}

		// start odbc server if it is enabled
		try {
			if (conf.get(ODBC_SERVER_ENABLE)) {
				val port = conf.get(ODBC_SERVER_PORT)
				odbcServer = Some(
					// TODO
					Class.forName(conf.get(ODBC_SERVER_CLASS))
						.getDeclaredConstructor(classOf[String], classOf[Int], classOf[MbConf], classOf[MbService])
						.newInstance(host, new Integer(port), conf, mbService).asInstanceOf[ThriftServer]
				)
				odbcServerBoundPort = odbcServer.map(_.start())
			}
		} catch {
			case e: Exception =>
				logError("Could not start odbc server.", e)
				gracefullyShutdown()
		}

		logInfo(s"Starting MoonboxMaster at ${self.path.toSerializationFormatWithAddress(address)}")
		// for debug
		/*context.system.scheduler.schedule(new FiniteDuration(2, SECONDS), new FiniteDuration(10, SECONDS)) {
			println("=========================================================================")
			println("idToWorker")
			println(idToWorker.map { case (_, v) => v}.mkString("\n"))
			println("--------------------------------------------------------------------------")
			println("addressToWorker")
			println(addressToWorker.map { case (_, v) => v}.mkString("\n"))
			println("--------------------------------------------------------------------------")
			println("workers")
			println(workers.map(_.toString).mkString("\n"))
			println
		}*/
	}

	@scala.throws[Exception](classOf[Exception])
	override def postStop(): Unit = {
		restServer.foreach(_.stop())
		tcpServer.foreach(_.stop())
		odbcServer.foreach(_.stop())

		if (timedEventService != null) {
			timedEventService.stop()
		}
		if (catalogContext != null) {
			catalogContext.stop()
		}
		if (persistenceEngine != null) {
			persistenceEngine.close()
		}
		if (leaderElectionAgent != null) {
			leaderElectionAgent.stop()
		}
	}

	override def handleMessage: Receive = {
		case ElectedLeader =>
			logInfo("I have been elected leader!")
			val (storedJobs, storedWorkers) = persistenceEngine.readPersistedData()
			state = if (storedJobs.isEmpty && storedWorkers.isEmpty) {
				logInfo("Nothing to recovery.")
				RecoveryState.ACTIVE
			} else {
				RecoveryState.RECOVERING
			}
			if (state == RecoveryState.RECOVERING) {
				beginRecovery(storedJobs, storedWorkers)
				recoveryCompletionScheduler = system.scheduler.scheduleOnce(
					new FiniteDuration(WORKER_TIMEOUT_MS, MILLISECONDS), self, CompleteRecovery)
			} else {
				logInfo(s"Now working as $state")
			}

		case RevokedLeadership =>
			logError("Leadership has been revoked, master shutting down.")
			gracefullyShutdown()

		case RegisterWorker(id, workerHost, workerPort, workerRef, workerAddress, internalPort) =>
			logInfo(s"Worker try registering: $workerAddress")
			if (state == RecoveryState.STANDBY) {
				workerRef ! MasterInStandby
			} else if (idToWorker.contains(id)) {
				workerRef ! RegisterWorkerFailed("Duplicate worker ID")
			} else {
				val worker = new WorkerInfo(id, workerHost, workerPort, workerAddress, workerRef, internalPort)
				if (registerWorker(worker)) {
					persistenceEngine.addWorker(worker)
					workerRef ! RegisteredWorker(self)
					logInfo(s"Worker registration success: $workerAddress")
					schedule()
				} else {
					logWarning(s"Worker registration failed. Attempted to re-register " +
						s"worker at same address: $workerAddress")
					workerRef ! RegisterWorkerFailed(s"Worker registration failed. " +
						s"Attempted to re-register worker at same address: $workerAddress")
				}
			}

		case Heartbeat(workerId, worker) =>
			idToWorker.get(workerId) match {
				case Some(workerInfo) =>
					workerInfo.lastHeartbeat = System.currentTimeMillis()
				case None =>
					if (workers.map(_.id).contains(workerId)) {
						logWarning(s"Got heartbeat from unregistered worker $workerId." +
							" Asking it to re-register.")
						worker ! ReconnectWorker(self)
					} else {
						logWarning(s"Got heartbeat from unregistered worker $workerId." +
							" This worker was never registered, so ignoring the heartbeat.")
					}
			}

		case WorkerStateResponse(workerId) =>
			idToWorker.get(workerId) match {
				case Some(worker) =>
					logInfo(s"Worker has been re-registered: " + workerId)
					worker.state = WorkerState.ALIVE
				case None =>
					logWarning("Scheduler state from unknown worker: " + workerId)
			}

			if (canCompleteRecovery) { completeRecovery() }

		case CompleteRecovery => completeRecovery()

		case CheckForWorkerTimeOut =>
			timeOutDeadWorkers()

		case WorkerLatestState(workerId) =>

		case JobSubmit(username, sqls, config) =>
			if(state != RecoveryState.ACTIVE) {
				val msg = s"Current master is not active: $state. Can only accept driver submissions in ALIVE state."
				sender() ! JobSubmitResponse(None, msg)
			} else {
				logInfo("Batch job submitted: " + sqls.mkString("; "))
				val driver = createDriver(ClusterDriverDescription(username, sqls, config))
				persistenceEngine.addDriver(driver)
				waitingDrivers += driver
				drivers.add(driver)
				schedule()
				sender() ! JobSubmitResponse(Some(driver.id),
					s"Batch job successfully submitted as ${driver.id}")
			}

		case JobProgress(driverId) =>
			if (state != RecoveryState.ACTIVE) {
				val msg = s"Current master is not active: $state.  Can only request driver state in ACTIVE state."
				sender() ! JobProgressState(driverId, -1, "unknown", msg)
			} else {
				waitingDrivers.find(_.id == driverId) match {
					case Some(driver) =>
						val msg = s"Driver $driverId is waiting for submit."
						sender() ! JobProgressState(driverId, driver.startTime, driver.state.toString, msg)
					case None =>
						(drivers ++ completedDrivers).find(_.id == driverId) match {
							case Some(driver) =>
								val msg = driver.exception.map(_.getMessage).getOrElse("")
								sender() ! JobProgressState(driverId, driver.startTime, driver.state.toString, msg)
							case None =>
								val msg = s"Ask unknown job state: $driverId"
								logWarning(msg)
								sender() ! JobProgressState(driverId, -1, "unknown", msg)
						}
				}
			}

		case BatchJobCancel(driverId) =>
			if (state != RecoveryState.ACTIVE) {
				val msg = s"Current master is not active: $state. Can only kill drivers in ACTIVE state."
				sender() ! BatchJobCancelResponse(driverId, success = false, msg)
			} else {
				logInfo(s"Asked to kill driver " + driverId)
				val driver = drivers.find(_.id == driverId)
				driver match {
					case Some(d) =>
						if (waitingDrivers.contains(d)) {
							waitingDrivers -= d
							self ! DriverStateChanged(driverId, DriverState.KILLED, None)
						} else {
							d.worker.foreach(_.endpoint ! KillDriver(driverId))
						}
						val msg = s"Kill request for $driverId submitted."
						logInfo(msg)
						sender() ! BatchJobCancelResponse(driverId, success = true, msg)
					case None =>
						val msg = s"Driver $driverId has already finished or does not exist."
						logWarning(msg)
						sender() ! BatchJobCancelResponse(driverId, success = false, msg)
				}
			}

		case DriverStateChanged(driverId, driverState, exception) =>
			driverState match {
				case DriverState.ERROR | DriverState.FINISHED | DriverState.KILLED | DriverState.FAILED =>
					removeDriver(driverId, driverState, exception)
				case _ =>
					drivers.find(_.id == driverId).foreach(_.state = driverState)

			}
		case e => println(e)
	}

	override def onDisconnected(remoteAddress: Address): Unit = {
		logInfo(s"$remoteAddress got disassociated, removing it.")
		addressToWorker.get(remoteAddress).foreach(removeWorker)
		if (state == RecoveryState.RECOVERING && canCompleteRecovery) { completeRecovery() }
	}

	private def schedule(): Unit = {
		if (state != RecoveryState.ACTIVE) {
			return
		}
		val shuffledAliveWorkers = Random.shuffle(workers.toSeq.filter(_.state == WorkerState.ALIVE))
		val numWorkerAlive = shuffledAliveWorkers.size
		var curPos = 0
		for (driver <- waitingDrivers.toList) {
			var numWorkersVisited = 0
			while (numWorkersVisited < numWorkerAlive) {
				val worker = shuffledAliveWorkers(curPos)
				numWorkersVisited += 1
				launchDriver(worker, driver)
				waitingDrivers -= driver
			}
			curPos = (curPos + 1) % numWorkerAlive
		}
	}

	private def beginRecovery(storedJobs: Seq[DriverInfo], storedWorkers: Seq[WorkerInfo]): Unit = {
		for (worker <- storedWorkers) {
			logInfo("Try to recovery worker: " + worker.id)
			try {
				registerWorker(worker)
				worker.state = WorkerState.UNKNOWN
				worker.endpoint ! MasterChanged(self)
			} catch {
				case e: Exception => logInfo("Worker " + worker.id + " had exception on reconnect")
			}
		}

		for (job <- storedJobs) {
			drivers += job
		}
		// TODO
	}

	private def canCompleteRecovery = workers.count(_.state == WorkerState.UNKNOWN) == 0

	private def completeRecovery(): Unit = {
		if (state != RecoveryState.RECOVERING) { return }
		state = RecoveryState.COMPLETING_RECOVERY
		// worker no response until waiting WORKER_TIMEOUT, then mark it as WorkerState.DEAD
		workers.filter(_.state == WorkerState.UNKNOWN).foreach(removeWorker)
		// TODO jobs

		state = RecoveryState.ACTIVE
		logInfo("Recovery complete." )
		logInfo("Now working as " + RecoveryState.ACTIVE)
		schedule()
	}

	private def removeWorker(worker: WorkerInfo): Unit = {
		// TODO
		logInfo("Removing worker " + worker.id + " on " + worker.endpoint)
		worker.setState(WorkerState.DEAD)
		idToWorker -= worker.id
		addressToWorker -= worker.address
		persistenceEngine.removeWorker(worker)
	}

	private def registerWorker(worker: WorkerInfo): Boolean = {
		workers.filter { w =>
			(w.host == worker.host && w.port == worker.port) && (w.state == WorkerState.DEAD)
		}.foreach(workers -= _)
		val workerAddress = worker.address
		if (addressToWorker.contains(workerAddress)) {
			val oldWorker = addressToWorker(workerAddress)
			if (oldWorker.state == WorkerState.UNKNOWN) {
				removeWorker(oldWorker)
			} else {
				logInfo("Attempted to re-register worker at same address: " + workerAddress)
				return false
			}
		}

		workers += worker
		idToWorker(worker.id) = worker
		addressToWorker(workerAddress) = worker
		true
	}

	private def timeOutDeadWorkers(): Unit = {
		val currentTime = System.currentTimeMillis()
		val toRemove = workers.filter(_.lastHeartbeat < currentTime - WORKER_TIMEOUT_MS).toArray
		for (worker <- toRemove) {
			if (worker.state != WorkerState.DEAD) {
				logWarning(s"Removing ${worker.id} because wt got no heartbeat in ${WORKER_TIMEOUT_MS / 1000} seconds.")
				removeWorker(worker)
			} else {
				if (worker.lastHeartbeat < currentTime - (15 * WORKER_TIMEOUT_MS)) {
					workers -= worker
				}
			}
		}
	}

	private def newDriverId(submitDate: Date): String = {
		val appId = "batch-%s-%04d".format(createDateFormat.format(submitDate), nextBatchDriverNumber)
		nextBatchDriverNumber += 1
		appId
	}

	private def createDriver(desc: ClusterDriverDescription): DriverInfo = {
		val now = System.currentTimeMillis()
		val date = new Date(now)
		new DriverInfo(now, newDriverId(date), desc, date)
	}

	private def launchDriver(worker: WorkerInfo, driver: DriverInfo): Unit = {
		logInfo("Launching driver " + driver.id + " on worker " + worker.id)
		worker.addDriver(driver)
		driver.worker = Some(worker)
		worker.endpoint ! LaunchDriver(driver.id, driver.desc)
		driver.state = DriverState.SUBMITTING
	}

	private def removeDriver(driverId: String, state: DriverState, exception: Option[Exception]) {
		drivers.find(_.id == driverId) match {
			case Some(driver) =>
				logInfo(s"Removing driver: $driverId")
				drivers -= driver
				// TODO complete retain
				completedDrivers += driver
				persistenceEngine.removeDriver(driver)
				driver.state = state
				driver.exception = exception
				driver.worker.foreach(_.removeDriver(driver))
				schedule()
			case None =>
				logWarning(s"Asked to remove unknown driver: $driverId")
		}
	}

	override def electedLeader(): Unit = {
		self ! ElectedLeader
	}

	override def revokedLeadership(): Unit = {
		self ! RevokedLeadership
	}

	private def gracefullyShutdown(): Unit = {
		system.terminate()
		System.exit(1)
	}

	private def checkHost(): Unit = {
		if (host == null) {
			logError("Host is null.")
			gracefullyShutdown()
		}
	}
}


object MoonboxMaster extends MbLogging {
	val SYSTEM_NAME = "Moonbox"
	val MASTER_NAME = "MoonboxMaster"
	val MASTER_PATH = s"/user/$MASTER_NAME"

	def main(args: Array[String]) {
		val conf = new MbConf()
		val param = new MoonboxMasterParam(args, conf)

		val actorSystem = ActorSystem(SYSTEM_NAME, ConfigFactory.parseMap(param.akkaConfig.asJava))

		try {
			actorSystem.actorOf(Props(classOf[MoonboxMaster], actorSystem, conf), MASTER_NAME)
		} catch {
			case e: Exception =>
				logError("Start MoonboxMaster failed with error: ", e)
				actorSystem.terminate()
				System.exit(1)
		}

	}


}