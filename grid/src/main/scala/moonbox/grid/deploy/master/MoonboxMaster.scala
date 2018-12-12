package moonbox.grid.deploy.master

import java.text.SimpleDateFormat
import java.util.{Date, Locale}

import akka.actor.{ActorSystem, Address, Cancellable, Props}
import akka.pattern._
import akka.util.Timeout
import com.typesafe.config.ConfigFactory
import moonbox.common.{MbConf, MbLogging}
import moonbox.grid.{LogMessage, MbActor}
import moonbox.grid.config._
import moonbox.grid.deploy.audit.BlackHoleAuditLogger
import moonbox.grid.deploy.{ClusterDriverDescription, DriverDescription, MbService}
import moonbox.grid.deploy.DeployMessages._
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
import scala.util.{Failure, Random, Success}

class MoonboxMaster(
	val system: ActorSystem,
	val conf: MbConf) extends MbActor with LogMessage with LeaderElectable with MbLogging {

	// for batch application IDs
	private def createDateFormat = new SimpleDateFormat("yyyyMMddHHmmss", Locale.ROOT)

	private implicit val ASK_TIMEOUT = Timeout(FiniteDuration(60, SECONDS))
	private val WORKER_TIMEOUT_MS = conf.get(WORKER_TIMEOUT)

	private val recoveryMode = conf.get(RECOVERY_MODE)
	private val recoveryEnable = !recoveryMode.equalsIgnoreCase("NONE")

	private var state = RecoveryState.STANDBY

	private var mbService: MbService = _

	private val idToWorker = new mutable.HashMap[String, WorkerInfo]
	private val addressToWorker = new mutable.HashMap[Address, WorkerInfo]
	private val workers = new mutable.HashSet[WorkerInfo]

	// for batch and interactive
	private val drivers = new mutable.HashSet[DriverInfo]
	private val completedDrivers = new ArrayBuffer[DriverInfo]
	// for batch
	private val waitingDrivers = new ArrayBuffer[DriverInfo]

	// for batch driver id
	private var nextBatchDriverNumber = 0

	// for interactive application
	private val apps = new mutable.HashSet[ApplicationInfo]
	private val idToApp = new mutable.HashMap[String, ApplicationInfo]
	private val addressToApp = new mutable.HashMap[Address, ApplicationInfo]

	private val sessionIdToApp = new mutable.HashMap[String, ApplicationInfo]

	private var persistenceEngine: PersistenceEngine = _
	private var leaderElectionAgent: LeaderElectionAgent = _

	private var recoveryCompletionScheduler: Cancellable = _
	private var checkForWorkerTimeOutTask: Cancellable = _

	private var timedEventService: TimedEventService = _

	private var restServer: Option[RestServer] = None
	private var restServerBoundPort: Option[Int] = None

	private var tcpServer: Option[TransportServer] = None
	private var tcpServerBoundPort: Option[Int] = None

	private var odbcServer: Option[ThriftServer] = None
	private var odbcServerBoundPort: Option[Int] = None


	@scala.throws[Exception](classOf[Exception])
	override def preStart(): Unit = {

		// for check DEAD worker
		checkForWorkerTimeOutTask = system.scheduler.schedule(
			new FiniteDuration(0, SECONDS),
			new FiniteDuration(WORKER_TIMEOUT_MS, MILLISECONDS),
			self,
			CheckForWorkerTimeOut
		)

		// start persist engine and election agent
		try {
			val (persistenceEngine_, leaderElectionAgent_) = recoveryMode.toUpperCase match {
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
		 try {
			mbService = new MbService(conf, self, new BlackHoleAuditLogger)
		} catch {
			case e: Exception =>
				logError("Could not start catalog.", e)
				gracefullyShutdown()
		}

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
		context.system.scheduler.schedule(new FiniteDuration(2, SECONDS), new FiniteDuration(10, SECONDS)) {
			println("=========================================================================")
			println("idToWorker")
			println(idToWorker.map { case (_, v) => v}.mkString("\n"))
			println("--------------------------------------------------------------------------")
			println("addressToWorker")
			println(addressToWorker.map { case (_, v) => v}.mkString("\n"))
			println("--------------------------------------------------------------------------")
			println("workers")
			println(workers.map(_.toString).mkString("\n"))
			println("--------------------------------------------------------------------------")
			println("drivers")
			println(drivers.map(_.toString).mkString("\n"))
			println("--------------------------------------------------------------------------")
			println("idToApp")
			println(idToApp.map { case (_, v) => v}.mkString("\n") )
			println("--------------------------------------------------------------------------")
			println("addressToApp")
			println(addressToApp.map { case (_, v) => v}.mkString("\n") )
			println("--------------------------------------------------------------------------")
			println("apps")
			println(apps.map(_.toString).mkString("\n") )
		}
	}

	@scala.throws[Exception](classOf[Exception])
	override def postStop(): Unit = {
		restServer.foreach(_.stop())
		tcpServer.foreach(_.stop())
		odbcServer.foreach(_.stop())

		if (timedEventService != null) {
			timedEventService.stop()
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
			val (storedDrivers, storedWorkers, storedApps) = persistenceEngine.readPersistedData()
			state = if (storedDrivers.isEmpty && storedWorkers.isEmpty && storedApps.isEmpty) {
				logInfo("Nothing to recovery.")
				RecoveryState.ACTIVE
			} else {
				RecoveryState.RECOVERING
			}
			if (state == RecoveryState.RECOVERING) {
				beginRecovery(storedDrivers, storedWorkers, storedApps)
				recoveryCompletionScheduler = system.scheduler.scheduleOnce(
					new FiniteDuration(WORKER_TIMEOUT_MS, MILLISECONDS), self, CompleteRecovery)
			} else {
				logInfo(s"Now working as $state")
			}

		case RevokedLeadership =>
			logError("Leadership has been revoked, master shutting down.")
			gracefullyShutdown()

		case RegisterWorker(id, workerHost, workerPort, workerRef, workerAddress) =>
			logInfo(s"Worker try registering: $workerAddress")
			if (state == RecoveryState.STANDBY) {
				workerRef ! MasterInStandby
			} else if (idToWorker.contains(id)) {
				workerRef ! RegisterWorkerFailed("Duplicate worker ID")
			} else {
				val worker = new WorkerInfo(id, workerHost, workerPort, workerAddress, workerRef)
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

			// master changed
		case WorkerStateResponse(workerId, driverIdDesces) =>
			idToWorker.get(workerId) match {
				case Some(worker) =>
					logInfo(s"Worker has been re-registered: " + workerId)
					worker.state = WorkerState.ALIVE
					driverIdDesces.foreach { case (driverId, desc, date) =>
						drivers.find(_.id == driverId).foreach { driver =>
							logInfo(s"Driver $driverId exists, update it.")
							driver.worker = Some(worker)
							driver.state = DriverState.RUNNING
							worker.addDriver(driver)
						}
					}
				case None =>
					logWarning("Scheduler state from unknown worker: " + workerId)
			}

			if (canCompleteRecovery) { completeRecovery() }

			// registered
		case WorkerLatestState(workerId, driverIdDesces) =>
			idToWorker.get(workerId) match {
				case Some(worker) =>
					driverIdDesces.foreach { case (driverId, desc, date) =>
						val driverMatches = worker.drivers.exists { case (id, _) => id == driverId }
						if (!driverMatches) { // not exist
							if (recoveryEnable && desc.isInstanceOf[ClusterDriverDescription]) {
								logInfo(s"master doesn't recognize this driver: $driverId. So tell worker kill it.")
								worker.endpoint ! KillDriver(driverId)
							} else {
								logInfo(s"new driver registered $driverId")
								val driver = createDriver(desc, Some(driverId), Some(date))
								persistenceEngine.addDriver(driver)
								drivers.add(driver)
								driver.worker = Some(worker)
								driver.state = DriverState.UNKNOWN
								worker.addDriver(driver)
							}
						}
					}
				case None =>
					logWarning("Worker state from unknown worker: " + workerId)
			}

		case RegisterApplication(id, appHost, appPort, appRef, appAddress, dataPort, appType) =>
			logInfo(s"Application $id try registering: $appAddress")
			if (state == RecoveryState.STANDBY) {
				appRef ! MasterInStandby
			} else if (idToApp.contains(id)) {
				appRef ! RegisterApplicationFailed(s"Duplicate application ID $id")
			} else {
				val app = new ApplicationInfo(
					System.currentTimeMillis(), id, appHost, appPort, appAddress, dataPort, appRef, appType)
				if (registerApplication(app)) {
					persistenceEngine.addApplication(app)
					appRef ! RegisteredApplication(self)
					logInfo(s"Application $id registration success: $appAddress")
				} else {
					logWarning(s"Application $id registration failed. Attempted to re-register " +
						s"application at same address: $appAddress")
					appRef ! RegisterApplicationFailed(s"Application $id registration failed. " +
						s"Attempted to re-register application at same address: $appAddress")
				}
			}

		case ApplicationStateResponse(driverId) =>
			idToApp.get(driverId) match {
				case Some(app) =>
					logInfo(s"Application has been re-registered: " + driverId)
					app.state = ApplicationState.RUNNING
				case None =>
					logWarning("Scheduler state from unknown application: " + driverId)
			}
			if (canCompleteRecovery) { completeRecovery() }

		case CompleteRecovery => completeRecovery()

		case CheckForWorkerTimeOut =>
			timeOutDeadWorkers()

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
							self ! DriverStateChanged(driverId, DriverState.KILLED, None, None)
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

		case DriverStateChanged(driverId, driverState, appId, exception) =>
			driverState match {
				case DriverState.ERROR | DriverState.FINISHED | DriverState.KILLED | DriverState.FAILED =>
					removeDriver(driverId, driverState, appId, exception)
				case _ =>
					drivers.find(_.id == driverId).foreach { d =>
						d.state = driverState
						d.appId = appId
					}
			}

		case open @ OpenSession(_, _, centralized) =>
			val requester = sender()
			val candidate = selectApplication(centralized)
			candidate match {
				case Some(app) =>
					logInfo(s"Try asking application ${app.id} to open session.")
					val f = app.endpoint.ask(open).mapTo[OpenSessionResponse]
					f.onComplete {
						case Success(response) =>
							if (response.sessionId.isDefined) {
								sessionIdToApp.put(response.sessionId.get, app)
							}
							requester ! response
						case Failure(e) =>
							requester ! OpenSessionResponse(None, message = e.getMessage)
					}
				case None =>
					val appType = if (centralized) "centralized" else "distributed"
					val msg = s"There is no available application for $appType computation."
					logWarning(msg)
					sender() ! OpenSessionResponse(None, message = msg)
			}

		case close @ CloseSession(sessionId) =>
			val requester = sender()
			sessionIdToApp.get(sessionId) match {
				case Some(app) =>
					val f = app.endpoint.ask(close).mapTo[CloseSessionResponse]
					f.onComplete {
						case Success(response) =>
							if (response.success) {
								sessionIdToApp.remove(response.sessionId)
							}
							requester ! response
						case Failure(e) =>
							requester ! CloseSessionResponse(sessionId, success = false, e.getMessage)
					}
				case None =>
					requester ! CloseSessionResponse(sessionId, success = false, s"Session $sessionId lost in master.")
			}

		case query: JobQuery =>
			val requester = sender()
			sessionIdToApp.get(query.sessionId) match {
				case Some(app) =>
					app.endpoint forward query
				case None =>
					requester ! JobQueryResponse(success = false, "", Seq.empty, hasNext = false,
						s"Session ${query.sessionId} lost in master.")
			}

		case cancel @ InteractiveJobCancel(sessionId) =>
			val requester = sender()
			sessionIdToApp.get(sessionId) match {
				case Some(app) =>
					val f = app.endpoint.ask(cancel).mapTo[InteractiveJobCancelResponse]
					f.onComplete {
						case Success(response) =>
							requester ! response
						case Failure(e) =>
							requester ! InteractiveJobCancelResponse(success = false, e.getMessage)
					}
				case None =>
					requester ! InteractiveJobCancelResponse(success = false, s"Session $sessionId lost in master.")
			}

		case e => println(e)
	}

	override def onDisconnected(remoteAddress: Address): Unit = {
		logInfo(s"$remoteAddress got disassociated, removing it.")
		addressToWorker.get(remoteAddress).foreach(removeWorker)
		addressToApp.get(remoteAddress).foreach(removeApplication)
		if (state == RecoveryState.RECOVERING && canCompleteRecovery) { completeRecovery() }
	}

	private def selectApplication(centralized: Boolean): Option[ApplicationInfo] = {
		val activeApps = apps.filter(_.state == ApplicationState.RUNNING)
		val typedApps = if (centralized) {
			activeApps.filter(_.appType == ApplicationType.CENTRALIZED)
		} else {
			activeApps.filter(_.appType == ApplicationType.DISTRIBUTED)
		}
		Random.shuffle(typedApps).headOption
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

				curPos = (curPos + 1) % numWorkerAlive
			}
		}
	}

	private def beginRecovery(storedDrivers: Seq[DriverInfo], storedWorkers: Seq[WorkerInfo], storedApps: Seq[ApplicationInfo]): Unit = {
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

		for (app <- storedApps) {
			logInfo("Try to recovery application: " + app.id)
			try {
				registerApplication(app)
				app.state = ApplicationState.UNKNOWN
				app.endpoint ! MasterChanged(self)
			} catch {
				case e: Exception => logInfo("Application " + app.id + " had exception on reconnect")
			}
		}

		for (driver <- storedDrivers) {
			drivers += driver
		}

	}

	// TODO
	private def canCompleteRecovery: Boolean = {
		workers.count(_.state == WorkerState.UNKNOWN) == 0 &&
		apps.count(_.state == ApplicationState.UNKNOWN) == 0
	}

	private def completeRecovery(): Unit = {
		if (state != RecoveryState.RECOVERING) { return }
		state = RecoveryState.COMPLETING_RECOVERY
		// worker no response until waiting WORKER_TIMEOUT, then mark it as WorkerState.DEAD
		workers.filter(_.state == WorkerState.UNKNOWN).foreach(removeWorker)
		apps.filter(_.state == ApplicationState.UNKNOWN).foreach(removeApplication)

		// TODO drivers
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

		for (driver <- worker.drivers.values) {
			logInfo(s"Remove driver ${driver.id} because it's worker disconnected.")
			removeDriver(driver.id, DriverState.ERROR, driver.appId, None)
		}

		persistenceEngine.removeWorker(worker)
	}

	private def removeApplication(app: ApplicationInfo): Unit = {
		logInfo("Removing application " + app.id + " on " + app.endpoint)
		apps -= app
		idToApp -= app.id
		addressToApp -= app.address
		persistenceEngine.removeApplication(app)
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
				logWarning(s"Removing ${worker.id} because it got no heartbeat in ${WORKER_TIMEOUT_MS / 1000} seconds.")
				removeWorker(worker)
			} else {
				if (worker.lastHeartbeat < currentTime - (15 * WORKER_TIMEOUT_MS)) {
					workers -= worker
				}
			}
		}
	}

	private def registerApplication(app: ApplicationInfo): Boolean = {
		if (addressToApp.contains(app.address)) {
			logInfo("Attempted to re-register application at same address: " + app.address)
			return false
		}
		apps += app
		idToApp(app.id) = app
		addressToApp(app.address) = app
		true
	}


	private def newDriverId(submitDate: Date): String = {
		val appId = "batch-%s-%04d".format(createDateFormat.format(submitDate), nextBatchDriverNumber)
		nextBatchDriverNumber += 1
		appId
	}

	private def createDriver(desc: DriverDescription, driverId: Option[String] = None, submitDate: Option[Date] = None): DriverInfo = {
		val now = System.currentTimeMillis()
		val date = new Date(now)
		new DriverInfo(now, driverId.getOrElse(newDriverId(date)), desc, submitDate.getOrElse(date))
	}

	private def launchDriver(worker: WorkerInfo, driver: DriverInfo): Unit = {
		logInfo("Launching driver " + driver.id + " on worker " + worker.id)
		worker.addDriver(driver)
		driver.worker = Some(worker)
		worker.endpoint ! LaunchDriver(driver.id, driver.desc)
		driver.state = DriverState.SUBMITTING
	}

	private def removeDriver(driverId: String, state: DriverState, appId: Option[String], exception: Option[Exception]) {
		drivers.find(_.id == driverId) match {
			case Some(driver) =>
				logInfo(s"Removing driver: $driverId. Final state $state")
				drivers -= driver
				// TODO complete retain
				completedDrivers += driver
				persistenceEngine.removeDriver(driver)
				driver.state = state
				driver.exception = exception
				driver.appId = appId
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