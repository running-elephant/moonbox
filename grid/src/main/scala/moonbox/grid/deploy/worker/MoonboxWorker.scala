package moonbox.grid.deploy.worker

import java.net.URI
import java.text.SimpleDateFormat
import java.util.{Date, Locale}

import akka.actor.{ActorRef, ActorSystem, Address, Cancellable, Props}
import com.typesafe.config.ConfigFactory
import moonbox.common.{MbConf, MbLogging}
import moonbox.grid.{LogMessage, MbActor}
import moonbox.grid.deploy.master.{DriverState, MoonboxMaster}
import moonbox.grid.deploy.master.MoonboxMaster._
import moonbox.grid.config.WORKER_TIMEOUT
import moonbox.grid.deploy._
import moonbox.grid.deploy.DeployMessages.{LaunchDriver, _}

import scala.collection.JavaConverters._
import scala.collection.mutable
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global


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

	private var registerToMasterScheduler: Option[Cancellable] = None


	@scala.throws[Exception](classOf[Exception])
	override def preStart(): Unit = {
		assert(!registered)
		logInfo(s"Running Moonbox version 0.3.0")// TODO
		logInfo(s"Starting MoonboxWorker at ${self.path.toSerializationFormatWithAddress(address)}")
		// TODO
		val driverId = newDriverId
		self ! LaunchDriver(driverId, new LocalDriverDescription(driverId, masterAddresses, conf))
//		val driverId2 = newDriverId
//		self ! LaunchDriver(driverId2, new ClientDriverDescription(driverId2, masterAddresses, conf))
		registerWithMaster()
	}

	@scala.throws[Exception](classOf[Exception])
	override def postStop(): Unit = {
		drivers.values.foreach { driver =>
			if (driver.desc.isInstanceOf[LocalDriverDescription] ||
				driver.desc.isInstanceOf[ClientDriverDescription]
			) {
				driver.kill()
			}
		}
	}

	override def handleMessage: Receive = {
		case msg: RegisterWorkerResponse =>
			handleRegisterResponse(msg)

		case SendHeartbeat =>
			if (connected) { sendToMaster(Heartbeat(workerId, self)) }

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
			drivers.get(driverId) match {
				case Some(runner) =>
					runner.kill()
				case None =>
					logError(s"Asked to kill unknown driver $driverId")
			}

		case driverStateChanged @ DriverStateChanged(driverId, state, appId, exception) =>
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
				finishDriver()
			case DriverState.FAILED =>
				logWarning(s"Driver $driverId exited with failure.")
				finishDriver()
			case DriverState.FINISHED =>
				logInfo(s"Driver $driverId exited with successfully.")
				finishDriver()
			case DriverState.KILLED =>
				logInfo(s"Driver $driverId was killed by user.")
				finishDriver()
			case DriverState.SUBMITTED =>
				logInfo(s"Driver $driverId has submitted to yarn.")
			case DriverState.RUNNING =>
				logInfo(s"Driver $driverId begin running.")
			case _ =>
				logDebug(s"Driver $driverId changed state to $state")
		}
		sendToMaster(driverStateChanged)

		def finishDriver(): Unit = {
			val driver = drivers.remove(driverId).get
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
		logError("Connection to master failed! Waiting for master to reconnect...")
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

	private def newDriverId: String = {
		val now = System.currentTimeMillis()
		val submitDate = new Date(now)
		val driverId = "interacive-%s-%04d".format(createDateFormat.format(submitDate), nextInteractiveDriverNumber)
		nextInteractiveDriverNumber += 1
		driverId
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



