/*-
 * <<
 * Moonbox
 * ==
 * Copyright (C) 2016 - 2019 EDP
 * ==
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * >>
 */

package moonbox.grid.deploy.master

import java.text.SimpleDateFormat
import java.util.{Date, Locale}

import akka.actor.{ActorRef, ActorSystem, Address, Cancellable, Props}
import akka.pattern._
import akka.util.Timeout
import com.typesafe.config.ConfigFactory
import moonbox.catalog.JdbcCatalog
import moonbox.common.util.Utils
import moonbox.common.{MbConf, MbLogging}
import moonbox.grid.config._
import moonbox.grid.deploy.DeployMessages._
import moonbox.grid.deploy.app.DriverState.DriverState
import moonbox.grid.deploy.app._
import moonbox.grid.deploy.messages.Message._
import moonbox.grid.deploy.rest.RestServer
import moonbox.grid.deploy.rest.entities.Node
import moonbox.grid.deploy.transport.TcpServer
import moonbox.grid.deploy.worker.{LaunchUtils, WorkerState}
import moonbox.grid.timer.{EventHandler, TimedEventService, TimedEventServiceImpl}
import moonbox.grid.{LogMessage, MbActor}

import scala.collection.JavaConverters._
import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration._
import scala.util.{Failure, Random, Success}

class MoonboxMaster(
	val system: ActorSystem,
	val conf: MbConf) extends MbActor with LogMessage with LeaderElectable with MbLogging {

	private def createDateFormat = new SimpleDateFormat("yyyyMMddHHmmss", Locale.ROOT)

	private implicit val ASK_TIMEOUT = Timeout(FiniteDuration(60, SECONDS))
	private val WORKER_TIMEOUT_MS = conf.get(WORKER_TIMEOUT)

	private val recoveryMode = conf.get(RECOVERY_MODE)

	private var state = RecoveryState.STANDBY

	private var catalog: JdbcCatalog = _

	private val idToWorker = new mutable.HashMap[String, WorkerInfo]
	private val addressToWorker = new mutable.HashMap[Address, WorkerInfo]
	private val workers = new mutable.HashSet[WorkerInfo]

	private val drivers = new mutable.HashSet[DriverInfo]
	private val waitingDrivers = new ArrayBuffer[DriverInfo]
	private val completedDrivers = new ArrayBuffer[DriverInfo]

	private var nextDriverNumber = 0

	private val apps = new mutable.HashSet[AppInfo]
	private val idToApp = new mutable.HashMap[String, AppInfo]
	private val addressToApp = new mutable.HashMap[Address, AppInfo]

	private var persistenceEngine: PersistenceEngine = _
	private var leaderElectionAgent: LeaderElectionAgent = _

	private var recoveryCompletionScheduler: Cancellable = _
	private var checkForWorkerTimeOutTask: Cancellable = _

	private var timedEventService: TimedEventService = _

	private var restServer: Option[RestServer] = None
	private var restServerBoundPort: Option[Int] = None

	private var tcpServer: Option[TcpServer] = None
	private var tcpServerBoundPort: Option[Int] = None

	private def launchDrivers(): Unit = {
		try {
			catalog.listAllApplications(true).foreach { app =>
				val config = app.cluster match {
					case Some(cluster) =>
						catalog.getCluster(cluster).config ++ app.config
					case None =>
						app.config
				}
				AppMasterManager.getAppMaster(app.appType) match {
					case Some(appMaster) =>
						self ! RequestSubmitDriver(app.fullName(), appMaster.createDriverDesc(config))
					case None =>
						logWarning(s"no suitable app master for app type ${app.appType}")
				}
			}
		} catch {
			case e: Throwable =>
				logError("Launch Driver Error: ", e)
				gracefullyShutdown()
		}
	}

	@scala.throws[Exception](classOf[Exception])
	override def preStart(): Unit = {

		// for checking DEAD worker
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
					(new BlackHolePersistenceEngine, new MonarchyLeaderAgent(this))
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
			catalog = new JdbcCatalog(conf)
		} catch {
			case e: Exception =>
				logError("Could not start catalog.", e)
				gracefullyShutdown()
		}

		// start timer
		try {
			if (conf.get(TIMER_SERVICE_ENABLE)) {
				timedEventService = new TimedEventServiceImpl(conf, new EventHandler())
				timedEventService.start()
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
				restServer = Some(new RestServer(host, port, conf, catalog, self, system))
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
				tcpServer = Some(new TcpServer(host, port, conf, catalog, self))
				tcpServerBoundPort = tcpServer.map(_.start())
			}
		} catch {
			case e: Exception =>
				logError("Could not start tcp server.", e)
				gracefullyShutdown()
		}

		AppMasterManager.registerAppMaster(new SparkLocalAppMaster(catalog))
		AppMasterManager.registerAppMaster(new SparkClusterAppMaster(catalog))
		AppMasterManager.registerAppMaster(new SparkBatchAppMaster(catalog))

		logInfo(s"Running Moonbox version 0.4.0")
		logInfo(s"Starting MoonboxMaster at ${self.path.toSerializationFormatWithAddress(address)}")
	}

	@scala.throws[Exception](classOf[Exception])
	override def postStop(): Unit = {
		restServer.foreach(_.stop())
		tcpServer.foreach(_.stop())

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
				launchDrivers()
			}

		case RequestMasterAddress =>
			val masterAddress = s"$host:$port"
			val restServerAddress = restServerBoundPort.map(port => s"$host:$port")
			val tcpServerAddress = tcpServerBoundPort.map(port => s"$host:$port")
			sender ! MasterAddress(master = masterAddress, restServer = restServerAddress, tcpServer = tcpServerAddress)

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
		case WorkerSchedulerStateResponse(workerId, driverIds) =>
			idToWorker.get(workerId) match {
				case Some(worker) =>
					logInfo(s"Worker has been re-registered: " + workerId)
					worker.state = WorkerState.ALIVE
					driverIds.foreach { driverId =>
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

			if (canCompleteRecovery) {
				completeRecovery()
			}

		// registered
		case WorkerLatestState(workerId, driverIds) =>
			idToWorker.get(workerId) match {
				case Some(worker) =>
					driverIds.foreach { driverId =>
						val driverMatches = worker.drivers.exists { case (id, _) => id == driverId }
						if (!driverMatches) {
							// not exist
							logInfo(s"master doesn't recognize this driver: $driverId. So tell worker kill it.")
							worker.endpoint ! KillDriver(driverId)
						}
					}
				case None =>
					logWarning("Worker state from unknown worker: " + workerId)
			}


		case RequestClusterState =>
			val nodes = workers.map(w => Node(w.address.hostPort, "Worker", w.state.toString, "", new Date(w.lastHeartbeat).toString))
			sender() ! ClusterStateResponse(nodes.toSeq)

		case RegisterApplication(id, appHost, appPort, appRef, appAddress, dataPort, appType) =>
			logInfo(s"Application $id try registering: $appAddress")
			if (state == RecoveryState.STANDBY) {
				appRef ! MasterInStandby
			} else if (idToApp.contains(id)) {
				appRef ! RegisterApplicationFailed(s"Duplicate application ID $id")
			} else {
				val app = new AppInfo(
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
					app.state = AppState.RUNNING
				case None =>
					logWarning("Scheduler state from unknown application: " + driverId)
			}
			if (canCompleteRecovery) {
				completeRecovery()
			}

		case CompleteRecovery => completeRecovery()

		case CheckForWorkerTimeOut =>
			timeOutDeadWorkers()

		case DriverStateChanged(driverId, driverState, appId, exception, time) =>
			driverState match {
				case DriverState.ERROR | DriverState.FINISHED | DriverState.LOST | DriverState.KILLED | DriverState.FAILED =>
					removeDriver(driverId, driverState, appId, exception, time)
				case _ =>
					drivers.find(_.id == driverId).foreach { d =>
						d.state = driverState
						d.updateTime = time
						d.appId = appId
					}
			}

		case RegisterTimedEvent(event) =>
			val requester = sender()
			Future {
				if (checkTimedService()) {
					if (!timedEventService.timedEventExists(event.group, event.name)) {
						timedEventService.addTimedEvent(event)
						logInfo(s"Register time event: ${event.name}, ${event.cronExpr}, ${event.sqls}.")
						RegisteredTimedEvent(self)
					} else {
						val message = s"Timed event ${event.name} is running already."
						logWarning(message)
						RegisterTimedEventFailed(message)
					}
				} else {
					val message = s"Timer is out of service."
					logWarning(s"Try to register timed event ${event.name}," + message)
					RegisterTimedEventFailed(message)
				}
			}.onComplete {
				case Success(response) =>
					requester ! response
				case Failure(e) =>
					requester ! RegisterTimedEventFailed(e.getMessage)
			}

		case UnregisterTimedEvent(group, name) =>
			val requester = sender()
			Future {
				if (checkTimedService()) {
					timedEventService.deleteTimedEvent(group, name)
					logInfo(s"Unregistered timed event $name.")
					UnregisteredTimedEvent(self)
				} else {
					val message = s"Timer is out of service."
					logWarning(s"Try to disable timed event $name," + message)
					UnregisterTimedEventFailed(message)
				}
			}.onComplete {
				case Success(response) =>
					requester ! response
				case Failure(e) =>
					requester ! UnregisterTimedEventFailed(e.getMessage)
			}

		case service: ServiceMessage =>
			handleServiceMessage.apply(service)

		/*case management: ManagementMessage =>
			handleManagementMessage.apply(management)*/

		case RequestSubmitDriver(driverName, driverDesc) =>
			if (state != RecoveryState.ACTIVE) {
				val msg = s"Current state is not active: $state. " +
						"Can only accept driver submission in ALIVE state."
				sender() ! SubmitDriverResponse(self, false, None, msg)
			} else {
				logInfo(s"Driver submitted $driverName " + driverDesc.toString)
				val driver = createDriver(driverName, driverDesc)
				persistenceEngine.addDriver(driver)
				waitingDrivers += driver
				drivers.add(driver)
				schedule()

				sender() ! SubmitDriverResponse(self, true, Some(driver.id),
					s"Driver Successfully submitted as ${driver.id}")
			}

		case RequestKillDriver(driverId) =>
			if (state != RecoveryState.ACTIVE) {
				val msg = s"Current state is not active: $state. " +
						"Can only kill drivers status in ALIVE state."
				sender() ! KillDriverResponse(self, driverId, success = false, msg)
			} else {
				logInfo("Asked to kill driver " + driverId)
				val driver = drivers.find(_.id == driverId)
				driver match {
					case Some(d) =>
						if (waitingDrivers.contains(d)) {
							waitingDrivers -= d
							self ! DriverStateChanged(driverId, DriverState.KILLED, None, None)
						} else {
							d.worker.foreach { w =>
								w.endpoint ! KillDriver(driverId)
							}
						}
						val msg = s"Kill request for $driverId submitted."
						logInfo(msg)
						sender() ! KillDriverResponse(self, driverId, success = true, msg)
					case None =>
						val msg = s"Driver $driverId not found or it is competed."
						logInfo(msg)
						sender() ! KillDriverResponse(self, driverId, success = false, msg)
				}
			}

		case RequestDriverStatus(driverId) =>
			if (state != RecoveryState.ACTIVE) {
				val msg = s"Current state is not active: $state. " +
						"Can only request driver status in ALIVE state."
				sender() ! DriverStatusResponse(
					found = false,
					driverId = driverId,
					driverType = None,
					startTime = None,
					state = None,
					updateTime = None,
					workerId = None,
					workerHostPort = None,
					exception = Some(new Exception(msg)))
			} else {
				(drivers ++ completedDrivers).find(_.id == driverId) match {
					case Some(driver) =>
						sender() ! DriverStatusResponse(found = true, driverId, Some(driver.desc.name), Some(driver.startTime), Some(driver.state), Some(driver.updateTime),
							driver.worker.map(_.id), driver.worker.map(w => s"${w.host}:${w.port}"), driver.exception)
					case None =>
						sender() ! DriverStatusResponse(found = false, driverId, None, None, None, None, None, None, None)
				}
			}

		case RequestAllDriverStatus(pattern) =>
			if (state != RecoveryState.ACTIVE) {
				val msg = s"Current state is not active: $state. " +
						"Can only request driver status in ALIVE state."
				sender() ! AllDriverStatusResponse(Seq.empty, Some(new Exception(msg)))
			} else {
				val allDrivers = pattern match {
					case Some(p) =>
						(drivers ++ completedDrivers).filter(_.id.startsWith(p))
					case None =>
						drivers ++ completedDrivers
				}
				val response = allDrivers.map { driver =>
					DriverStatusResponse(found = true, driver.id, Some(driver.desc.name), Some(driver.startTime), Some(driver.state), Some(driver.updateTime),
						driver.worker.map(_.id), driver.worker.map(w => s"${w.host}:${w.port}"), driver.exception)
				}

				sender() ! AllDriverStatusResponse(response.toSeq, None)
			}

		case RequestApplicationAddress(session, appType, appName) =>

			val response = AppMasterManager.getAppMaster(appType) match {
				case Some(appMaster) =>
					appMaster.selectApp(apps, session, appName) match {
						case Some(appInfo) =>
							ApplicationAddressResponse(found = true, host = Some(appInfo.host), port = Some(appInfo.dataPort))
						case None =>
							ApplicationAddressResponse(found = false, exception = Some(new Exception(s"there is no available $appType application")))
					}
				case None =>
					ApplicationAddressResponse(found = false, exception = Some(new Exception(s"unknown application type $appType.")))
			}
			sender() ! response

		case s: SubmitDriverResponse => logInfo(s"${s.driverId} " + s.message)

		case e => logWarning("Unknown message: " + e.toString)

	}

	private def handleServiceMessage: Receive = {
		case sample: SampleRequest =>
			val requester = sender()
			val candidate = selectApplication(true)
			candidate match {
				case Some(app) =>
					logInfo(s"Asking application ${app.id} to sample data.")
					val f = app.endpoint.ask(sample).mapTo[SampleResponse]
					f.onComplete {
						case Success(response) =>
							requester ! response
						case Failure(e) =>
							requester ! SampleFailed(e.getMessage)
					}
				case None =>
					val msg = s"There is no available application for service."
					logWarning(msg)
					sender() ! SampleFailed(msg)
			}

		case verify: VerifyRequest =>
			val requester = sender()
			val candidate = selectApplication(true)
			candidate match {
				case Some(app) =>
					logInfo(s"Asking application ${app.id} to verify sql.")
					val f = app.endpoint.ask(verify).mapTo[VerifyResponse]
					f.onComplete {
						case Success(response) =>
							requester ! response
						case Failure(e) =>
							requester ! VerifyResponse(success = false, message = Some(e.getMessage))
					}
				case None =>
					val msg = s"There is no available application for service."
					logWarning(msg)
					sender() ! VerifyResponse(success = false, message = Some(msg))
			}

		case translate: TranslateRequest =>
			val requester = sender()
			val candidate = selectApplication(true)
			candidate match {
				case Some(app) =>
					logInfo(s"Asking application ${app.id} to translate sql.")
					val f = app.endpoint.ask(translate).mapTo[TranslateResponse]
					f.onComplete {
						case Success(response) =>
							requester ! response
						case Failure(e) =>
							requester ! TranslateResponse(success = false, message = Some(e.getMessage))
					}
				case None =>
					val msg = s"There is no available application for service."
					logWarning(msg)
					sender() ! TranslateResponse(success = false, message = Some(msg))
			}

		case resource: TableResourcesRequest =>
			val requester = sender()
			val candidate = selectApplication(true)
			candidate match {
				case Some(app) =>
					logInfo(s"Asking application ${app.id} to get tables and functions in sql.")
					val f = app.endpoint.ask(resource).mapTo[TableResourcesResponses]
					f.onComplete {
						case Success(response) =>
							requester ! response
						case Failure(e) =>
							requester ! TableResourcesResponses(success = false, Some(e.getMessage))
					}
				case None =>
					val msg = s"There is no available application for service."
					logWarning(msg)
					sender() ! TableResourcesResponses(success = false, Some(msg))
			}

		case schema: SchemaRequest =>
			val requester = sender()
			val candidate = selectApplication(true)
			candidate match {
				case Some(app) =>
					logInfo(s"Asking application ${app.id} to get schema for sql.")
					val f = app.endpoint.ask(schema).mapTo[SchemaResponse]
					f.onComplete {
						case Success(response) =>
							requester ! response
						case Failure(e) =>
							requester ! SchemaFailed(e.getMessage)
					}
				case None =>
					val msg = s"There is no available application for service."
					logWarning(msg)
					sender() ! SchemaFailed(msg)
			}

		case lineage: LineageRequest =>
			val requester = sender()
			val candidate = selectApplication(true)
			candidate match {
				case Some(app) =>
					logInfo(s"Asking application ${app.id} to get lineage for sql.")
					val f = app.endpoint.ask(lineage).mapTo[LineageResponse]
					f.onComplete {
						case Success(response) =>
							requester ! response
						case Failure(e) =>
							requester ! LineageFailed(e.getMessage)
					}
				case None =>
					val msg = s"There is no available application for service."
					logWarning(msg)
					sender() ! LineageFailed(msg)
			}
	}

	override def onDisconnected(remoteAddress: Address): Unit = {
		logInfo(s"$remoteAddress got disassociated, removing it.")
		addressToWorker.get(remoteAddress).foreach(removeWorker)
		addressToApp.get(remoteAddress).foreach(removeApplication)
		if (state == RecoveryState.RECOVERING && canCompleteRecovery) {
			completeRecovery()
		}
	}

	private def selectApplication(centralized: Boolean, label: String = "common"): Option[AppInfo] = {
		val activeApps = apps.filter(_.state == AppState.RUNNING).toSeq
		val typedApps = if (centralized) {
			activeApps.filter(app => app.appType == "sparklocal")
		} else {
			activeApps.filter(app => app.appType == "sparkcluster")
		}
		Random.shuffle(typedApps).headOption
	}

	private def schedule(): Unit = {
		if (state != RecoveryState.ACTIVE) {
			return
		}
		val shuffledAliveWorkers = Random.shuffle(workers.toSeq.filter(_.state == WorkerState.ALIVE))
		val numWorkerAlive = shuffledAliveWorkers.size
		if (numWorkerAlive > 0) {
			var curPos = 0
			for (driver <- waitingDrivers.toList) {
				val worker = shuffledAliveWorkers(curPos)
				launchDriver(worker, driver)
				waitingDrivers -= driver
				curPos = (curPos + 1) % numWorkerAlive
			}
		}
	}

	private def beginRecovery(storedDrivers: Seq[DriverInfo], storedWorkers: Seq[WorkerInfo], storedApps: Seq[AppInfo]): Unit = {
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
				app.state = AppState.UNKNOWN
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
				apps.count(_.state == AppState.UNKNOWN) == 0
	}

	private def completeRecovery(): Unit = {
		if (state != RecoveryState.RECOVERING) {
			return
		}
		state = RecoveryState.COMPLETING_RECOVERY
		// worker no response until waiting WORKER_TIMEOUT, then mark it as WorkerState.DEAD
		workers.filter(_.state == WorkerState.UNKNOWN).foreach(removeWorker)
		apps.filter(_.state == AppState.UNKNOWN).foreach(removeApplication)

		// TODO drivers
		state = RecoveryState.ACTIVE
		logInfo("Recovery complete.")
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
			removeDriver(driver.id, DriverState.ERROR, driver.appId, None, driver.updateTime)
		}

		persistenceEngine.removeWorker(worker)
	}

	private def removeApplication(app: AppInfo): Unit = {
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

	private def registerApplication(app: AppInfo): Boolean = {
		if (addressToApp.contains(app.address)) {
			logInfo("Attempted to re-register application at same address: " + app.address)
			return false
		}
		apps += app
		idToApp(app.id) = app
		addressToApp(app.address) = app
		true
	}

	/*private def newDriverId(tag: String, submitDate: Date): String = {
		val appId = s"$tag-%s-%04d".format(createDateFormat.format(submitDate), nextDriverNumber)
		nextDriverNumber += 1
		appId
	}

	private def createDriver(driverId: String, desc: DriverDesc): DriverInfo = {
		val now = System.currentTimeMillis()
		val date = new Date(now)
		new DriverInfo(now, newDriverId(driverId, date), desc, date)
	}*/

	private def createDriver(driverId: String, desc: DriverDesc): DriverInfo = {
		val now = System.currentTimeMillis()
		val date = new Date(now)
		new DriverInfo(now, driverId, desc, date)
	}

	private def launchDriver(worker: WorkerInfo, driver: DriverInfo): Unit = {
		logInfo("Launching driver " + driver.id + " on worker " + worker.id)
		worker.addDriver(driver)
		driver.worker = Some(worker)
		worker.endpoint ! LaunchDriver(driver.id, driver.desc)
		driver.state = DriverState.SUBMITTING
	}

	private def removeDriver(driverId: String, state: DriverState, appId: Option[String], exception: Option[Exception], time: Long) {
		drivers.find(_.id == driverId) match {
			case Some(driver) =>
				logInfo(s"Removing driver: $driverId. Final state $state")
				drivers -= driver
				// TODO complete retain
				completedDrivers += driver
				persistenceEngine.removeDriver(driver)
				driver.state = state
				driver.updateTime = time
				driver.exception = exception
				driver.appId = appId
				driver.worker.foreach(_.removeDriver(driver))
				schedule()
			case None =>
				logWarning(s"Asked to remove unknown driver: $driverId. State is $state")
		}
	}

	private def checkTimedService(): Boolean = {
		if (conf.get(TIMER_SERVICE_ENABLE) && timedEventService != null) {
			true
		} else false
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
	// for timed event call
	var MASTER_REF: ActorRef = _

	def main(args: Array[String]) {
		val conf = new MbConf()
		val param = new MoonboxMasterParam(args, conf)

		val actorSystem = ActorSystem(SYSTEM_NAME, ConfigFactory.parseMap(param.akkaConfig.asJava))

		try {
			MASTER_REF = actorSystem.actorOf(Props(classOf[MoonboxMaster], actorSystem, conf), MASTER_NAME)
		} catch {
			case e: Exception =>
				logError("Start MoonboxMaster failed with error: ", e)
				actorSystem.terminate()
				System.exit(1)
		}

	}


}
