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
import moonbox.common.util.Utils
import moonbox.common.{MbConf, MbLogging}
import moonbox.grid.config._
import moonbox.grid.deploy.DeployMessages._
import moonbox.grid.deploy.MoonboxService
import moonbox.grid.deploy.app.DriverState.DriverState
import moonbox.grid.deploy.app._
import moonbox.grid.deploy.audit.BlackHoleAuditLogger
import moonbox.grid.deploy.messages.Message._
import moonbox.grid.deploy.rest.RestServer
import moonbox.grid.deploy.transport.TransportServer
import moonbox.grid.deploy.worker.{LaunchUtils, WorkerState}
import moonbox.grid.timer.{EventEntity, EventHandler, TimedEventService, TimedEventServiceImpl}
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

  // for batch application IDs
  private def createDateFormat = new SimpleDateFormat("yyyyMMddHHmmss", Locale.ROOT)

  private implicit val ASK_TIMEOUT = Timeout(FiniteDuration(600, SECONDS))

  private val WORKER_TIMEOUT_MS = conf.get(WORKER_TIMEOUT)

  private def POOL_SCHEDULER_INTERVAL_MS: Long = conf.get(BATCH_DRIVER_POOL_SCHEDULER_INTERVAL)

  private val recoveryMode = conf.get(RECOVERY_MODE)

  private val recoveryEnable = !recoveryMode.equalsIgnoreCase("NONE")

  private var state = RecoveryState.STANDBY

  private var mbService: MoonboxService = _

  private val idToWorker = new mutable.HashMap[String, WorkerInfo]
  private val addressToWorker = new mutable.HashMap[Address, WorkerInfo]
  private val workers = new mutable.HashSet[WorkerInfo]

  // for batch and interactive
  private val drivers = new mutable.HashSet[DriverInfo]
  private val completedDrivers = new ArrayBuffer[DriverInfo]
  // for batch
  private val waitingDrivers = new ArrayBuffer[DriverInfo]

  // for batch pool
  private def driverPoolParallelism: Int = conf.get(BATCH_DRIVER_POOL_PARALLELISM)

  private var driverPoolOnScheduled: Boolean = false
  private val driverPool = new ArrayBuffer[DriverInfo]
  private var schedulingDrivers = new ArrayBuffer[String]

  // for monitor driver
  private var driverMonitors: Seq[DriverMonitor] = _

  // for batch driver id
  private var nextBatchDriverNumber = 0

  // for interactive application
  private val apps = new mutable.HashSet[AppInfo]
  private val idToApp = new mutable.HashMap[String, AppInfo]
  private val addressToApp = new mutable.HashMap[Address, AppInfo]

  private val sessionIdToApp = new mutable.HashMap[String, AppInfo]

  private var persistenceEngine: PersistenceEngine = _
  private var leaderElectionAgent: LeaderElectionAgent = _

  private var recoveryCompletionScheduler: Cancellable = _
  private var checkForWorkerTimeOutTask: Cancellable = _
  private var driverPollScheduler: Cancellable = _

  private var timedEventService: TimedEventService = _

  private var restServer: Option[RestServer] = None
  private var restServerBoundPort: Option[Int] = None

  private var tcpServer: Option[TransportServer] = None
  private var tcpServerBoundPort: Option[Int] = None

  @scala.throws[Exception](classOf[Exception])
  override def preStart(): Unit = {

    // for checking DEAD worker
    checkForWorkerTimeOutTask = system.scheduler.schedule(
      new FiniteDuration(0, SECONDS),
      new FiniteDuration(WORKER_TIMEOUT_MS, MILLISECONDS),
      self,
      CheckForWorkerTimeOut
    )

    // monitor drivers
    DriverMonitor.registerDriverMonitor(new SparkBatchDriverMonitor(system, self, conf))
    driverMonitors = DriverMonitor.getDriverMonitors()

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
      mbService = new MoonboxService(conf, self, new BlackHoleAuditLogger)
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
      val (storedDrivers, storedDriverPool, storedWorkers, storedApps) = persistenceEngine.readPersistedData()
      state = if (storedDrivers.isEmpty && storedDriverPool.isEmpty && storedWorkers.isEmpty && storedApps.isEmpty) {
        logInfo("Nothing to recovery.")
        RecoveryState.ACTIVE
      } else {
        RecoveryState.RECOVERING
      }
      if (state == RecoveryState.RECOVERING) {
        beginRecovery(storedDrivers, storedDriverPool, storedWorkers, storedApps)
        recoveryCompletionScheduler = system.scheduler.scheduleOnce(
          new FiniteDuration(WORKER_TIMEOUT_MS, MILLISECONDS), self, CompleteRecovery)
      } else {
        logInfo(s"Now working as $state")
        driverMonitors.foreach(driverMonitor => driverMonitor.monitorDrivers())
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

      if (canCompleteRecovery) {
        completeRecovery()
      }

    // registered
    case WorkerLatestState(workerId, driverIdDesces) =>
      idToWorker.get(workerId) match {
        case Some(worker) =>
          driverIdDesces.foreach { case (driverId, desc, date) =>
            val driverMatches = worker.drivers.exists { case (id, _) => id == driverId }
            if (!driverMatches) {
              // not exist
              if (recoveryEnable && desc.isInstanceOf[SparkBatchDriverDesc]) {
                logInfo(s"master doesn't recognize this driver: $driverId. So tell worker kill it.")
                worker.endpoint ! KillDriver(driverId)
              } else {
                logInfo(s"new driver registered $driverId")
                val driver = createDriver(desc, driverId, date)
                persistenceEngine.addDriver(driver)
                drivers.add(driver)
                driver.worker = Some(worker)
                driver.state = DriverState.UNKNOWN
                worker.addDriver(driver)
                driverMonitors.foreach(_.registerDriver(driver))
              }
            }
          }
        case None =>
          logWarning("Worker state from unknown worker: " + workerId)
      }

    case RegisterApplication(id, appLabel, appHost, appPort, appRef, appAddress, dataPort, appType) =>
      logInfo(s"Application $id try registering: $appAddress")
      if (state == RecoveryState.STANDBY) {
        appRef ! MasterInStandby
      } else if (idToApp.contains(id)) {
        appRef ! RegisterApplicationFailed(s"Duplicate application ID $id")
      } else {
        val app = new AppInfo(
          System.currentTimeMillis(), id, appLabel, appHost, appPort, appAddress, dataPort, appRef, appType)
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

    case driverStateChanged@DriverStateChanged(driverId, driverState, appId, exception) =>
      if (sender() == self && driverState != DriverState.SUBMITTED) {
        drivers.find(_.id == driverId)
          .foreach(driver => {
            driver.worker.get.endpoint ! driverStateChanged
          })
      }

      driverState match {
        case DriverState.ERROR | DriverState.FINISHED | DriverState.LOST | DriverState.KILLED | DriverState.FAILED =>
          removeDriver(driverId, driverState, appId, exception)
        case _ =>
          drivers.find(_.id == driverId).foreach { d =>
            d.state = driverState
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

    case job: JobMessage =>
      handleJobMessage.apply(job)

    case service: ServiceMessage =>
      handleServiceMessage.apply(service)

    case management: ManagementMessage =>
      handleManagementMessage.apply(management)

    case e => logWarning("Unknown message: " + e.toString)
  }

  private def handleJobMessage: Receive = {

    case JobSubmit(org, username, lang, sqls, userConfig) =>
      if (state != RecoveryState.ACTIVE) {
        val msg = s"Current master is not active: $state. Can only accept driver submissions in ALIVE state."
        logError(s"Batch job submitted failed: $msg")
        sender() ! JobSubmitResponse(None, msg)
      } else {
        logInfo("Batch job submitted: " + sqls.mkString("; "))
        val config = LaunchUtils.getBatchDriverConfigs(conf, userConfig)
        val submitDate = new Date()
        val driverId = newDriverId(submitDate) + userConfig.get(EventEntity.NAME).map("-" + _).getOrElse("")
        val driverDesc = if (lang == "hql") {
          HiveBatchDriverDesc(driverId, org, username, sqls, config)
        } else {
          SparkBatchDriverDesc(org, username, sqls, config)
        }
        val driver = createDriver(driverDesc, driverId, submitDate)
        persistenceEngine.addDriver(driver)
        waitingDrivers += driver
        drivers.add(driver)
        schedule()
        val msg = s"Batch job successfully submitted as ${driver.id}"
        logInfo(msg)
        sender() ! JobSubmitResponse(Some(driver.id), msg)
      }

    case JobPoolSubmit(org, username, lang, sqls, userConfig) =>
      if (state != RecoveryState.ACTIVE) {
        val msg = s"Current master is not active: $state. Can only accept driver pool submissions in ALIVE state."
        logError(s"Batch job pool submitted failed: $msg")
        sender() ! JobPoolSubmitResponse(None, msg)
      } else {
        logInfo("Batch job pool submitted.")
        val config = LaunchUtils.getBatchDriverConfigs(conf, userConfig)
        val submitDate = new Date()
        val submitDriverIds = new ArrayBuffer[String]
        val submitDrivers = sqls.zipWithIndex.map {
          case (sql, index) =>
            val driverId = newDriverId(submitDate) +
              userConfig.get(EventEntity.NAME).map("-" + _).getOrElse("") + s"-pool-${index + 1}"
            submitDriverIds.append(driverId)
            val driverDesc = if (lang == "hql") {
              HiveBatchDriverDesc(driverId, org, username, Seq(sql), config)
            } else {
              SparkBatchDriverDesc(org, username, Seq(sql), config)
            }
            createDriver(driverDesc, driverId, submitDate)
        }
        submitDrivers.foreach { driver =>
          persistenceEngine.addDriverPool(driver)
          driverPool += driver
        }

        if (!driverPoolOnScheduled) {
          scheduleDriverPool
        }
        val msg = s"Batch job pool successfully submitted, driver sequence size is ${submitDriverIds.size}. "
        logInfo(msg)
        sender() ! JobPoolSubmitResponse(Some(submitDriverIds), msg)
      }

    case JobProgress(driverId) =>
      if (state != RecoveryState.ACTIVE) {
        val msg = s"Current master is not active: $state.  Can only request driver state in ACTIVE state."
        sender() ! JobProgressState(driverId, None, -1, DriverState.UNKNOWN.toString, msg)
      } else {
        waitingDrivers.find(_.id == driverId) match {
          case Some(driver) =>
            val msg = s"Driver $driverId is waiting for submit."
            sender() ! JobProgressState(driverId, driver.appId, driver.submitTime, driver.state.toString, msg)
          case None =>
            (drivers ++ completedDrivers).find(_.id == driverId) match {
              case Some(driver) =>
                val msg = driver.exception.map(_.getMessage).getOrElse("")
                sender() ! JobProgressState(driverId, driver.appId, driver.submitTime, driver.state.toString, msg)
              case None =>
                val msg = s"Ask unknown job state: $driverId"
                logWarning(msg)
                sender() ! JobProgressState(driverId, None, -1, DriverState.UNKNOWN.toString, msg)
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
              logInfo(s"Remove driver $driverId from waitingDrivers.")
              self ! DriverStateChanged(driverId, DriverState.KILLED, None, None)
            } else {
              //todo driver has submitted to yarn, but state hasn't reported yet
              if (d.state == DriverState.SUBMITTING) {
                d.worker.foreach(_.endpoint ! KillDriver(driverId))
                logInfo(s"Asked worker ${d.worker.get.id} to kill driver $driverId.")
              } else {
                driverMonitors.find(_.isInstanceOf[SparkBatchDriverMonitor]).get.killDriver(d)
              }
            }
            val msg = s"Kill request for $driverId submitted."
            sender() ! BatchJobCancelResponse(driverId, success = true, msg)
          case None =>
            val msg = s"Driver $driverId has already finished or does not exist."
            logWarning(msg)
            sender() ! BatchJobCancelResponse(driverId, success = false, msg)
        }
      }

    case open@OpenSession(_, _, _, config) =>
      val requester = sender()
      val centralized = config.get("islocal").exists(_.equalsIgnoreCase("true"))
      val appLabel = config.getOrElse("spark.app.label", "common")
      val candidate = selectApplication(centralized, appLabel)
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
          val msg = s"There is no available application for $appType $appLabel computation."
          logWarning(msg)
          sender() ! OpenSessionResponse(None, message = msg)
      }

    case close@CloseSession(sessionId) =>
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

    case cancel@InteractiveJobCancel(sessionId) =>
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
              requester ! TableResourcesResponses(success = false, message = Some(e.getMessage))
          }
        case None =>
          val msg = s"There is no available application for service."
          logWarning(msg)
          sender() ! TableResourcesResponses(success = false, message = Some(msg))
      }

    case schema: SchemaRequest =>
      val requester = sender()
      val candidate = selectApplication(true)
      candidate match {
        case Some(app) =>
          logInfo(s"Asking application ${app.id} to get schema for sql.")
          val f = app.endpoint.ask(schema).mapTo[SchemaResponses]
          f.onComplete {
            case Success(response) =>
              requester ! response
            case Failure(e) =>
              requester ! SchemaResponses(success = false, message = Some(e.getMessage))
          }
        case None =>
          val msg = s"There is no available application for service."
          logWarning(msg)
          sender() ! SchemaResponses(success = false, message = Some(msg))
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

  private def handleManagementMessage: Receive = {
    case ClusterInfoRequest =>
      val clusterInfo = workers.toSeq
        .sortBy(worker => (worker.host, worker.id))
        .map { worker =>
          Seq(
            worker.host,
            worker.port.toString,
            worker.state.toString,
            s"${(Utils.now - worker.lastHeartbeat) / 1000}s"
          )
        }
      sender() ! ClusterInfoResponse(clusterInfo)
    case AppsInfoRequest =>
      val appsInfo = apps.toSeq
        .sortBy(app => (app.host, app.id))
        .map { app =>
          Seq(
            app.id,
            app.host,
            app.port.toString,
            app.state.toString,
            app.appType.toString
          )
        }
      sender() ! AppsInfoResponse(appsInfo)

    case DriversInfoRequest =>
      val driversInfo = (drivers ++ driverPool).toSeq
        .filter(driver => DriverDeployMode(driver.desc.deployMode.getOrElse("NONE")) == DriverDeployMode.CLUSTER)
        .sortBy(_.id)
        .map { driver =>
          Seq(
            driver.id,
            driver.appId.getOrElse("none"),
            if (driver.worker.isDefined) driver.worker.get.id else "none",
            driver.state.toString,
            driver.submitDate.toString
          )
        }
      sender() ! DriversInfoResponse(driversInfo)

    case ConfigRequest(config) =>
      //todo dynamic set grid config
      config.foreach {
        case (k, v) =>
          logInfo(s"ROOT set config $k=$v")
          conf.set(k, v)
      }
      sender() ! ConfigResponse("Config set success.")
  }

  override def onDisconnected(remoteAddress: Address): Unit = {
    addressToWorker.get(remoteAddress).foreach(worker => {
      logInfo(s"Worker ${worker.id} remoteAddress got disassociated, removing it.")
      removeWorker(worker)
    })
    addressToApp.get(remoteAddress).foreach(app => {
      logInfo(s"App ${app.id} remoteAddress got disassociated, removing it.")
      removeApplication(app)
    })
    if (state == RecoveryState.RECOVERING && canCompleteRecovery) {
      completeRecovery()
    }
  }

  private def selectApplication(centralized: Boolean, label: String = "common"): Option[AppInfo] = {
    val activeApps = apps.filter(_.state == AppState.RUNNING).toSeq
    val typedApps = if (centralized) {
      activeApps.filter(app => app.appType == AppType.CENTRALIZED && app.label.equals(label))
    } else {
      activeApps.filter(app => app.appType == AppType.DISTRIBUTED && app.label.equals(label))
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
        driverMonitors.foreach(_.registerDriver(driver))
        curPos = (curPos + 1) % numWorkerAlive
      }
    }
  }

  // for scheduler driver pool
  private def scheduleDriverPool() = {
    driverPoolOnScheduled = true
    driverPollScheduler = system.scheduler.schedule(
      new FiniteDuration(0, SECONDS),
      new FiniteDuration(POOL_SCHEDULER_INTERVAL_MS, MICROSECONDS)
    )(submitDriverPool())
    logInfo("Driver pool schedule started.")
  }

  private def submitDriverPool(): Unit = {
    while (schedulingDrivers.length < driverPoolParallelism && driverPool.nonEmpty) {
      val driver = driverPool.remove(0)
      persistenceEngine.removeDriverPool(driver)
      schedulingDrivers += driver.id
      drivers += driver
      waitingDrivers += driver
      logInfo(s"Submit driver ${driver.id} from driver pool.")
      schedule()
      if (schedulingDrivers.length == driverPoolParallelism) {
        logInfo(s"Driver pool parallelism is $driverPoolParallelism.")
        logInfo(s"There are still ${driverPool.size} drivers waiting to schedule.")
      }
    }
    if (driverPool.isEmpty) {
      driverPollScheduler.cancel()
      driverPoolOnScheduled = false
      logInfo("Driver pool schedule finished.")
    }
  }

  private def beginRecovery(storedDrivers: Seq[DriverInfo], storedDriverPool: Seq[DriverInfo], storedWorkers: Seq[WorkerInfo], storedApps: Seq[AppInfo]): Unit = {
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

    for (driver <- storedDriverPool) {
      driverPool += driver
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
      if (driver.desc.isInstanceOf[LongRunDriverDesc] || (driver.appId.isEmpty && (driver.state == DriverState.SUBMITTING))) {
        logInfo(s"Remove driver ${driver.id} because it's worker disconnected.")
        removeDriver(driver.id, DriverState.ERROR, driver.appId, None)
      }
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

  private def newDriverId(submitDate: Date): String = {
    val appId = "batch-%s-%04d".format(createDateFormat.format(submitDate), nextBatchDriverNumber)
    nextBatchDriverNumber += 1
    appId
  }

  private def createDriver(desc: DriverDesc, driverId: String, submitDate: Date): DriverInfo = {
    new DriverInfo(submitDate.getTime, driverId, desc, submitDate)
  }

  private def launchDriver(worker: WorkerInfo, driver: DriverInfo): Unit = {
    logInfo("Launching driver " + driver.id + " on worker " + worker.id)
    worker.addDriver(driver)
    worker.endpoint ! LaunchDriver(driver.id, driver.desc)
    drivers.find(_.id == driver.id)
      .foreach(driver => {
        driver.state == DriverState.SUBMITTING
        driver.worker = Some(worker)
      })
  }

  private def removeDriver(driverId: String, state: DriverState, appId: Option[String], exception: Option[Exception]) {
    drivers.find(_.id == driverId) match {
      case Some(driver) =>
        logInfo(s"Removing driver: $driverId. Final state $state.")
        drivers -= driver
        // TODO complete retain
        driver.setFinishDate(System.currentTimeMillis())
        completedDrivers += driver
        persistenceEngine.removeDriver(driver)
        driver.state = state
        driver.exception = exception
        driver.appId = appId
        driver.worker.foreach(_.removeDriver(driver))
        // remove driverId reference app
        apps.find(_.id == driverId) match {
          case Some(app) =>
            removeApplication(app)
          case None => // nothing
        }
        schedulingDrivers -= driverId
        driverMonitors.foreach(_.unRegisterDriver(driver))
        schedule()
      case None => // nothing
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
