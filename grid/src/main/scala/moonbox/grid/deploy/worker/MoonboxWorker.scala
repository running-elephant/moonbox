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

package moonbox.grid.deploy.worker

import java.net.URI
import java.text.SimpleDateFormat
import java.util.{Date, Locale}

import akka.actor.{ActorRef, ActorSystem, Address, Cancellable, Props}
import com.typesafe.config.ConfigFactory
import moonbox.common.{MbConf, MbLogging}
import moonbox.grid.config.WORKER_TIMEOUT
import moonbox.grid.deploy.DeployMessages.{LaunchDriver, _}
import moonbox.grid.deploy._
import moonbox.grid.deploy.app._
import moonbox.grid.deploy.master.MoonboxMaster._
import moonbox.grid.deploy.master.MoonboxMaster
import moonbox.grid.{LogMessage, MbActor}

import scala.collection.JavaConverters._
import scala.collection.mutable
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._


class MoonboxWorker(
                     system: ActorSystem,
                     masterAddresses: Array[String],
                     val conf: MbConf) extends MbActor with LogMessage with MbLogging {

  private val HEARTBEAT_MILLIS = conf.get(WORKER_TIMEOUT) / 4

  private val workerId = generateWorkerId()
  private var master: Option[ActorRef] = None
  private var activeMasterUrl: String = ""
  private var masterAddressToConnect: Option[Address] = None
  private var registered = false
  private var connected = false
  private var connectionAttemptCount = 0
  private var nextInteractiveDriverNumber = 0

  private val drivers = new mutable.HashMap[String, DriverRunner]
  private val finishedDrivers = new mutable.LinkedHashMap[String, DriverRunner]

  private val driverIdToDriverDesc = new mutable.HashMap[String, DriverDesc]()
  private val killMarker = new mutable.HashSet[String]()

  private var registerToMasterScheduler: Option[Cancellable] = None


  @scala.throws[Exception](classOf[Exception])
  override def preStart(): Unit = {
    assert(!registered)
    logInfo(s"Running Moonbox version 0.3.0") // TODO
    logInfo(s"Starting MoonboxWorker at ${self.path.toSerializationFormatWithAddress(address)}")

    Runtime.getRuntime.addShutdownHook(new Thread(new Runnable {
      override def run(): Unit = {
        drivers.values.foreach { driver =>
          if (driver.desc.isInstanceOf[LongRunDriverDesc]) {
            driver.kill()
          }
        }
      }
    }))

    // launch interactive drivers
    launchDrivers()
    registerWithMaster()
  }

  @scala.throws[Exception](classOf[Exception])
  override def postStop(): Unit = {

  }

  override def handleMessage: Receive = {
    case msg: RegisterWorkerResponse =>
      handleRegisterResponse(msg)

    case SendHeartbeat =>
      if (connected) {
        sendToMaster(Heartbeat(workerId, self))
      }

    case MasterChanged(masterRef) =>
      logInfo(s"Master has changed, new master is at ${masterRef.path.address}")
      changeMaster(masterRef, masterRef.path.address)
      val driverDesces = drivers.map { case (k, v) => v }.toSeq.map(r => (r.driverId, r.desc, r.submitDate))
      masterRef ! WorkerStateResponse(workerId, driverDesces)

    case ReconnectWorker(masterRef) =>
      logInfo(s"Master with ${masterRef.path.address} requested this worker to reconnect.")
      registerWithMaster()

    case LaunchDriver(driverId, driverDesc) =>
      logInfo(s"Ask to launch driver $driverId")
      val driver = new DriverRunner(conf, driverId, driverDesc, self, new Date())
      drivers(driverId) = driver
      driver.start()

    case KillDriver(driverId) =>
      logInfo(s"Asked to kill driver $driverId")
      driverIdToDriverDesc.remove(driverId)
      drivers.get(driverId) match {
        case Some(runner) =>
          killMarker.add(driverId)
          runner.kill()
        case None =>
          logError(s"Asked to kill unknown driver $driverId")
      }

    case driverStateChanged@DriverStateChanged(driverId, state, appId, exception) =>
      handleDriverStateChanged(driverStateChanged)

    case e => println(e)
  }

  private def handleDriverStateChanged(driverStateChanged: DriverStateChanged): Unit = {
    val state = driverStateChanged.state
    val driverId = driverStateChanged.driverId
    val exception = driverStateChanged.exception
    state match {
      case DriverState.ERROR =>
        logWarning(s"Driver $driverId failed with exception: ${exception.get}")
      case DriverState.FAILED =>
        logWarning(s"Driver $driverId exited with failure.")
      case DriverState.FINISHED =>
        logInfo(s"Driver $driverId exited with successfully.")
      case DriverState.KILLED =>
        logInfo(s"Driver $driverId was killed by user.")
      case DriverState.SUBMITTED =>
        logInfo(s"Driver $driverId has submitted to yarn.")
      case DriverState.RUNNING =>
        logInfo(s"Driver $driverId begin running.")
      case _ =>
        logDebug(s"Driver $driverId changed state to $state")
    }
    if (DriverState.isFinished(state)) {
      finishDriver(driverId)
    }

    sendToMaster(driverStateChanged)

    if (state == DriverState.FINISHED) {
      if (killMarker.contains(driverId)) {
        killMarker.remove(driverId)
      } else {
        driverIdToDriverDesc.get(driverId).foreach { desc =>
          if (desc.isInstanceOf[LongRunDriverDesc]) {
            system.scheduler.scheduleOnce(new FiniteDuration(3, SECONDS)) {
              logInfo(s"Relaunch driver $driverId")
              self ! LaunchDriver(driverId, desc)
            }
          }
        }
      }
    }
  }

  private def finishDriver(driverId: String): Unit = {
    drivers.remove(driverId).foreach { driver =>
      finishedDrivers(driverId) = driver
    }
  }

  override def onDisconnected(remoteAddress: Address): Unit = {
    if (master.exists(_.path.address == remoteAddress) ||
      masterAddressToConnect.contains(remoteAddress)) {
      logInfo(s"$remoteAddress Disassociated!")
      masterDisconnected()
    }
  }

  private def masterDisconnected(): Unit = {
    logWarning("Connection to master failed! Waiting for master to reconnect...")
    connected = false
    registerWithMaster()
  }

  private def changeMaster(masterRef: ActorRef, masterAddress: Address): Unit = {
    activeMasterUrl = "" // TODO
    masterAddressToConnect = Some(masterAddress) // TODO
    master = Some(masterRef)
    connected = true
    cancelRegistrationScheduler()
  }

  private def cancelRegistrationScheduler(): Unit = {
    registerToMasterScheduler.foreach(_.cancel())
    registerToMasterScheduler = None
  }

  private def registerWithMaster(): Unit = {
    registerToMasterScheduler match {
      case None =>
        registered = false
        connectionAttemptCount = 0
        registerToMasterScheduler = Some {
          system.scheduler.schedule(
            new FiniteDuration(1, SECONDS),
            new FiniteDuration(5, SECONDS))(
            tryRegisterAllMasters()
          )
        }
      case Some(_) =>
        logInfo("Don't attempt to register with the master, since there is an attempt scheduled already.")
    }
  }

  private def tryRegisterAllMasters(): Unit = {
    connectionAttemptCount += 1
    if (registered) {
      cancelRegistrationScheduler()
    } else if (connectionAttemptCount <= 15) {
      masterAddresses.par.foreach(sendRegisterMessageToMaster)
    } else {
      logError("All masters are unresponsive! Giving up.")
      gracefullyShutdown()
    }
  }

  private def sendRegisterMessageToMaster(masterRpcAddress: String): Unit = {
    logInfo(s"Try registering with master $masterRpcAddress.")
    system.actorSelection(masterRpcAddress).tell(RegisterWorker(
      workerId,
      host,
      port,
      self,
      address
    ), self)
  }

  private def handleRegisterResponse(msg: RegisterWorkerResponse): Unit = {
    msg match {
      case RegisteredWorker(masterRef) =>
        logInfo("Successfully registered with master " + masterRef.path.address)
        registered = true
        changeMaster(masterRef, masterRef.path.address)

        system.scheduler.schedule(new FiniteDuration(0, SECONDS),
          new FiniteDuration(HEARTBEAT_MILLIS, MILLISECONDS), self, SendHeartbeat)

        val driverDesces = drivers.map { case (k, v) => v }.toSeq.map(r => (r.driverId, r.desc, r.submitDate))
        masterRef ! WorkerLatestState(workerId, driverDesces)

      case RegisterWorkerFailed(message) =>
        if (!registered) {
          logError("Worker registration failed: " + message)
          gracefullyShutdown()
        }
      case MasterInStandby =>
      // do nothing
    }
  }

  private def sendToMaster(message: Any): Unit = {
    master match {
      case Some(masterRef) =>
        masterRef ! message
      case None =>
        logWarning(s"Dropping $message because master is unknown now.")
    }
  }

  private def createDateFormat = new SimpleDateFormat("yyyyMMddHHmmss", Locale.US)

  private def generateWorkerId(): String = {
    "worker-%s-%s".format(createDateFormat.format(new Date), address.hostPort)
  }

  private def newDriverId(driverType: String, label: String): String = {
    val now = System.currentTimeMillis()
    val submitDate = new Date(now)
    val driverId = "%s-%s-%s-%s-%04d".format(driverType, label, host, createDateFormat.format(submitDate), nextInteractiveDriverNumber)
    nextInteractiveDriverNumber += 1
    driverId
  }

  private def launchDrivers(): Unit = {
    try {
      val local = LaunchUtils.getLocalDriverConfigs(conf)
      local.foreach { config =>
        val label = config.getOrElse("spark.app.label", "common")
        val driverId = newDriverId("local", label)
        val driverDesc = new SparkLocalDriverDesc(driverId, label, masterAddresses, config)
        driverIdToDriverDesc.put(driverId, driverDesc)
        self ! LaunchDriver(driverId, driverDesc)
      }
      val cluster = LaunchUtils.getClusterDriverConfigs(conf)
      cluster.foreach { config =>
        val label = config.getOrElse("spark.app.label", "common")
        val driverId = newDriverId("cluster", label)
        val driverDesc = new SparkClusterDriverDesc(driverId, label, masterAddresses, config)
        driverIdToDriverDesc.put(driverId, driverDesc)
        self ! LaunchDriver(driverId, driverDesc)
      }
    } catch {
      case e: Throwable =>
        logError("Launch Driver Error: ", e)
        gracefullyShutdown()
    }
  }
}

object MoonboxWorker extends MbLogging {
  val WORKER_NAME = "MoonboxWorker"
  val WORKER_PATH = s"/user/$WORKER_NAME"

  def main(args: Array[String]) {
    val conf = new MbConf()
    val param = new MoonboxWorkerParam(args, conf)
    val actorSystem = ActorSystem(SYSTEM_NAME, ConfigFactory.parseMap(param.akkaConfig.asJava))

    try {
      actorSystem.actorOf(Props(classOf[MoonboxWorker], actorSystem, param.masters.map(fromMoonboxURL), conf), WORKER_NAME)
    } catch {
      case e: Exception =>
        logError("Start MoonboxWorker failed with error: ", e)
        actorSystem.terminate()
        System.exit(1)
    }
  }

  private def fromMoonboxURL(url: String): String = {
    try {
      val uri = new URI(url)
      val host = uri.getHost
      val port = uri.getPort
      if (uri.getScheme != "moonbox" ||
        host == null ||
        port < 0 ||
        (uri.getPath != null && !uri.getPath.isEmpty) || // uri.getPath returns "" instead of null
        uri.getFragment != null ||
        uri.getQuery != null ||
        uri.getUserInfo != null) {
        throw new Exception("Invalid master URL: " + url)
      }
      s"akka.tcp://${MoonboxMaster.SYSTEM_NAME}@$host:$port${MoonboxMaster.MASTER_PATH}"
    } catch {
      case e: java.net.URISyntaxException =>
        throw new Exception("Invalid master URL: " + url)
    }
  }
}



