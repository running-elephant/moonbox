package moonbox.grid.deploy2.node

import java.text.SimpleDateFormat
import java.util.{Date, Locale}

import akka.actor.{Actor, ActorRef, ActorSystem, Address, Cancellable, ExtendedActorSystem, Props, Terminated}
import akka.cluster.Cluster
import akka.cluster.ClusterEvent._
import akka.pattern._
import com.typesafe.config.ConfigFactory
import moonbox.common.{MbConf, MbLogging}
import moonbox.core.CatalogContext
import moonbox.grid.JobInfo
import moonbox.grid.api.{MbApi, OpenSession, RequestAccess}
import moonbox.grid.config._
import moonbox.grid.deploy2.node.DeployMessages._
import moonbox.grid.deploy2.rest.RestServer
import moonbox.grid.deploy2.transport.TransportServer
import moonbox.grid.deploy2.MbService
import moonbox.grid.timer.{TimedEventService, TimedEventServiceImpl}

import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.collection.JavaConverters._
import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer


class Moonbox(akkaSystem: ActorSystem,
	host: String,
	port: Int,
	restPort: Int,
	jdbcPort: Int,
	val conf: MbConf) extends Actor with MbLogging with LeaderElectable {

	import Moonbox._

	private val NODE_ID = generateNodeId()

	private val RECOVERY_MODE = conf.get("moonbox.deploy.recoveryMode", "ZOOKEEPER")

	private val WORKER_TIMEOUT_MS = conf.get(RPC_AKKA_CLUSTER_FAILURE_DETECTOR_HEARTBEAT_PAUSE)

	private var state = RoleState.SLAVE

	private var master: ActorRef = _

	private var nextJobNumber = 0

	private val nodes = new mutable.HashSet[NodeInfo]()
	private val addressToNode = new mutable.HashMap[Address, NodeInfo]()

	// for batch
	private val jobIdToNode = new mutable.HashMap[String, ActorRef]()
	private val waitingJobs = new mutable.Queue[JobInfo]()

	// for batch and interactive
	private val runningJobs = new mutable.HashMap[String, JobInfo]()
	private val completeJobs = new ArrayBuffer[JobInfo]()

	// for interactive
	private val sessionIdToJobRunner = new mutable.HashMap[String, ActorRef]()

	// for cancel job
	private val jobIdToJobRunner = new mutable.HashMap[String, ActorRef]()

	private val cluster: Cluster = Cluster(akkaSystem)

	private var persistenceEngine: PersistenceEngine = _

	private var leaderElectionAgent: LeaderElectionAgent = _

	private var registerToMasterScheduler: Cancellable = _

	private var recoveryCompletionScheduler: Cancellable = _

	private var catalogContext: CatalogContext = _

	private var timedEventService: TimedEventService = _

	private var restServer: Option[RestServer] = None

	private var restServerBoundPort: Option[Int] = None

	private var tcpServer: Option[TransportServer] = None

	private var tcpServerBoundPort: Option[Int] = None


	@scala.throws[Exception](classOf[Exception])
	override def preStart(): Unit = {

		// TODO try
		catalogContext = new CatalogContext(conf)

		val serviceImpl = new MbService(conf, catalogContext, self)

		if (conf.get(REST_SERVER_ENABLE)) {
			restServer = Some(new RestServer(host, restPort, conf, serviceImpl, akkaSystem))
		}

		if (conf.get(TCP_SERVER_ENABLE)) {
			tcpServer = Some(new TransportServer(host, jdbcPort, conf, serviceImpl))
		}

		restServerBoundPort = restServer.map(_.start())
		tcpServerBoundPort = tcpServer.map(_.start())

		val (persistenceEngine_, leaderElectionAgent_) = RECOVERY_MODE.toUpperCase match {
			case "ZOOKEEPER" =>
				logInfo("Persisting recovery state to Zookeeper")
				val zkFactory = new ZookeeperRecoveryModeFactory(conf, akkaSystem)
				(zkFactory.createPersistEngine(), zkFactory.createLeaderElectionAgent(this))
			case _ =>
				(new BlackHolePersistenceEngine,  new MonarchyLeaderAgent(this))
		}
		persistenceEngine = persistenceEngine_
		leaderElectionAgent = leaderElectionAgent_

		registerToMasterScheduler = akkaSystem.scheduler.schedule(
			new FiniteDuration(1, SECONDS),
			new FiniteDuration(1, SECONDS)) {
			tryRegisteringToMaster()
		}

	}

	@scala.throws[Exception](classOf[Exception])
	override def postStop(): Unit = {
		cluster.unsubscribe(self)
		restServer.foreach(_.stop())
		tcpServer.foreach(_.stop())
		if (timedEventService != null) {
			timedEventService.stop()
		}
		if (persistenceEngine != null) {
			persistenceEngine.close()
		}
		leaderElectionAgent.stop()
		if (recoveryCompletionScheduler != null) {
			recoveryCompletionScheduler.cancel()
		}
	}

	override def receive: Receive = {
		case request: MbApi =>
			process.apply(request)
		case ElectedLeader =>
			cluster.subscribe(self, classOf[UnreachableMember], classOf[ReachableMember])
			master = self
			persistenceEngine.saveMasterAddress(masterAddressString)
			val (storedJobs, storedWorkers) = persistenceEngine.readPersistedData()
			state = if (storedJobs.isEmpty && storedWorkers.isEmpty) {
				RoleState.MASTER
			} else {
				RoleState.RECOVERING
			}
			if (state == RoleState.RECOVERING) {
				beginRecovery(storedJobs, storedWorkers)
				recoveryCompletionScheduler = akkaSystem.scheduler.scheduleOnce(new FiniteDuration(WORKER_TIMEOUT_MS, MILLISECONDS), self, CompleteRecovery)
			}
			if (conf.get(TIMER_SERVICE_ENABLE)) {
				timedEventService = new TimedEventServiceImpl(conf)
				timedEventService.start()
			}
			logInfo("I am running as Master. New state: " + state)

			// for debug
			akkaSystem.scheduler.schedule(
				new FiniteDuration(10, SECONDS),
				new FiniteDuration(10, SECONDS)) {
				println
				println(nodes.map(n => n.toString + ":" + n.state).mkString("\n"))
				println(addressToNode.map(_.toString()).mkString("\n"))
				println(persistenceEngine.readNodes().mkString("\n"))
				println
			}

		case CompleteRecovery => completeRecovery()

		case RegisterNode(id, nodeHost, nodePort, endpoint, cores, memory) =>
			if (state == RoleState.SLAVE) {
				endpoint ! NotMasterNode
			} else {
				val node = new NodeInfo(id, nodeHost, nodePort, cores, memory, endpoint)
				if (registerNode(node)) {
					node.state = NodeState.ALIVE
					persistenceEngine.addNode(node)
					endpoint ! RegisteredNode(self)
					schedule()
				} else {
					val nodeAddress = endpoint.path.address
					logWarning("Node registration failed. Attempted to re-register node at same address: " +
						nodeAddress
					)
					endpoint ! RegisterNodeFailed("Attempted to re-register node at same address: " + nodeAddress)
				}
			}

		case RevokedLeadership =>
			if (state == RoleState.MASTER) {
				cluster.unsubscribe(self)
				state = RoleState.SLAVE
			}
			if (timedEventService != null) {
				timedEventService.stop()
			}
			nodes.clear()
			addressToNode.clear()
			waitingJobs.clear()
			logInfo("Have not been elected leader! New state: " + state)
		case RegisteredNode(masterAddress) =>
			master = masterAddress
			registerToMasterScheduler.cancel()
			if (state == RoleState.SLAVE) {
				persistenceEngine.close()
			}
			logInfo(s"Registered to master ${master.path.address}")

		case MasterChanged(address) =>
			logInfo("Master has changed, new master is at " + address)
			master = address
			master ! RegisterNode(generateNodeId(), host, port, self, 100, 100)
		case UnreachableMember(member) =>
			addressToNode.get(member.address).foreach(removeNode)
			if (state == RoleState.RECOVERING && canCompleteRecovery) {
				completeRecovery()
			}
			logInfo(s"Node ${member.address} leaved.")
		case ReachableMember(member) =>
			nodes.filter(_.endpoint.path.address == member.address).foreach { node =>
				node.state = NodeState.ALIVE
				addressToNode(member.address) = node
				persistenceEngine.addNode(node)
			}
			logInfo(s"Node reconnected: " + member.address)

		case e =>
			println(e)
	}

	private def process: Receive = {
		case RequestAccess =>
			if (state == RoleState.MASTER) {
				selectNode()
			} else {
				master.ask(RequestAccess)
			}
		case OpenSession(username, database, isLocal) =>
			// cluster
			// local
	}

	private def schedule(): Unit = {

	}

	private def selectNode(): String = {
		null
	}

	private def tryRegisteringToMaster(): Unit = {
		persistenceEngine.readMasterAddress().foreach { address =>
			logDebug(s"Try registering to master $address")
			akkaSystem.actorSelection(address + NODE_PATH).tell(RegisterNode(generateNodeId(), host, port, self, 100,100), self)
		}
	}

	/*private def isSelf(address: String): Boolean = {
		val selfAddress = akkaSystem.asInstanceOf[ExtendedActorSystem].provider.getDefaultAddress.toString
		address == selfAddress
	}*/

	private def masterAddressString: String = {
		akkaSystem.asInstanceOf[ExtendedActorSystem].provider.getDefaultAddress.toString
	}

	private def canCompleteRecovery = {
		nodes.count(_.state == NodeState.UNKNOWN) == 0
	}

	private def beginRecovery(storedJobs: Seq[JobInfo], storedNodes: Seq[NodeInfo]) = {
		for (job <- storedJobs) {
			waitingJobs.enqueue(job)
		}

		for (node <- storedNodes) {
			if (node.host == host && node.port == port) {
				// if recovery node is myself, don't recovery, trust registration
				persistenceEngine.removeNode(node)
			} else {
				logInfo(s"Try to recovery node: " + node.id)
				registerNode(node)
				node.state = NodeState.UNKNOWN
			}
			try {
				node.endpoint ! MasterChanged(self)
			} catch {
				case e: Exception =>
					logInfo(s"Node ${node.id} ${node.endpoint.path.address} had exception on reconnect")
			}
		}
	}

	private def completeRecovery() : Unit = {
		if (state != RoleState.RECOVERING) { return }
		state = RoleState.COMPLETING_RECOVERY
		nodes.filter(_.state == NodeState.UNKNOWN).foreach(removeNode)
		state = RoleState.MASTER
		logInfo("Recovery complete. New State: " + state)
		schedule()
	}

	private def registerNode(node: NodeInfo): Boolean = {
		nodes.filter { n =>
			(n.host == node.host && n.port == node.port) && (n.state == NodeState.DEAD)
		}.foreach(n => nodes -= n)

		val nodeAddress = node.endpoint.path.address
		if (addressToNode.contains(nodeAddress)) {
			val oldNode = addressToNode(nodeAddress)
			if (oldNode.state == NodeState.UNKNOWN) {
				removeNode(oldNode)
			} else {
				logInfo("Attempted to re-register node at same address: " + nodeAddress)
				return false
			}
		}

		nodes += node
		addressToNode(nodeAddress) = node
		logInfo(s"Registering node $nodeAddress with ${node.cores} cores, ${node.memory} RAM")
		true
	}

	private def removeNode(node: NodeInfo): Unit = {
		logInfo("Removing node " + node.id + "on " + node.host + ":" + node.port)
		node.state = NodeState.DEAD
		addressToNode -= node.endpoint.path.address

		persistenceEngine.removeNode(node)
	}

	override def electedLeader(): Unit = {
		self ! ElectedLeader
	}

	override def revokedLeadership(): Unit = {
		self ! RevokedLeadership
	}

	// For job IDs
	private def createDateFormat = new SimpleDateFormat("yyyyMMddHHmmss", Locale.US)

	private def generateNodeId(): String = {
		"node-%s-%s-%d".format(createDateFormat.format(new Date), host, port)
	}
}

object Moonbox extends MbLogging {

	val NODE_NAME = "moonbox"
	val NODE_PATH = s"/user/$NODE_NAME"

	def main(args: Array[String]) {

		val conf = new MbConf()
		val param = new MoonboxParam(args, conf)
		val akkaSystem = ActorSystem(param.clusterName, ConfigFactory.parseMap(param.akkaConfig.asJava))

		try {
			akkaSystem.actorOf(Props(
				classOf[Moonbox], akkaSystem, param.host, param.port, param.restPort, param.tcpPort, conf
			), NODE_NAME)
		} catch {
			case e: Throwable =>
				logError("Start moonbox error", e)
				akkaSystem.terminate()
				System.exit(1)
		}

	}
}
