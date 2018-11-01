package moonbox.grid.deploy2.node

import java.text.SimpleDateFormat
import java.util.{Date, Locale}

import akka.actor.{Actor, ActorRef, ActorSystem, Address, Cancellable, ExtendedActorSystem, Props}
import akka.cluster.Cluster
import akka.cluster.ClusterEvent._
import akka.pattern._
import akka.util.Timeout
import com.typesafe.config.ConfigFactory
import moonbox.common.util.Utils
import moonbox.common.{MbConf, MbLogging}
import moonbox.core.CatalogContext
import moonbox.core.command.{CreateTempView, InsertInto, MQLQuery, MbCommand, MbRunnableCommand}
import moonbox.core.parser.MbParser
import moonbox.grid.api._
import moonbox.grid.config._
import moonbox.grid.deploy2.MbService
import moonbox.grid.deploy2.node.DeployMessages.{ScheduleJob, _}
import moonbox.grid.deploy2.rest.RestServer
import moonbox.grid.deploy2.transport.TransportServer
import moonbox.grid.runtime.cluster.ClusterMessage.ReportYarnAppResource
import moonbox.grid.runtime.cluster.MbClusterActor
import moonbox.grid.runtime.local.MbLocalActor
import moonbox.grid.timer.{TimedEventService, TimedEventServiceImpl}
import moonbox.grid.{ConnectionType, JobInfo}
import moonbox.protocol.app._

import scala.collection.JavaConverters._
import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration.{FiniteDuration, MILLISECONDS, SECONDS}
import scala.util.{Failure, Success}

class Moonbox(akkaSystem: ActorSystem,
	host: String,
	port: Int,
	restPort: Int,
	jdbcPort: Int,
	val conf: MbConf) extends Actor with MbLogging with LeaderElectable {

	import Moonbox._

	private implicit val ASK_TIMEOUT = Timeout(FiniteDuration(60, SECONDS))

	private val NODE_ID = generateNodeId()

	private val RECOVERY_MODE = conf.get(PERSIST_IMPLEMENTATION)

	private val WORKER_TIMEOUT_MS = conf.get(RPC_AKKA_CLUSTER_FAILURE_DETECTOR_HEARTBEAT_PAUSE)

	private var state = RoleState.SLAVE

	private var nextJobNumber = 0

	private val nodes = new mutable.HashSet[NodeInfo]()
	private val addressToNode = new mutable.HashMap[Address, NodeInfo]()

	// for batch
	private val jobIdToNode = new mutable.HashMap[String, ActorRef]()
	private val waitingJobs = new mutable.Queue[JobInfo]()

	// for batch and interactive
	private val runningJobs = new mutable.HashMap[String, JobInfo]()
	private val completeJobs = new mutable.HashMap[String, JobInfo]()
	private val failedJobs = new mutable.HashMap[String, JobInfo]()

	// for interactive
	private val sessionIdToJobRunner = new mutable.HashMap[String, (ActorRef, Boolean)]()  // actorRef is local / remote

	// for cancel job
	private val jobIdToJobRunner = new mutable.HashMap[String, ActorRef]()      // actorRef is remote

	// for job history
	private val retainedCompleteJobs = new ArrayBuffer[String]()  //history job
	private val retainedFailedJobs = new ArrayBuffer[String]()
	private val retainedSuccessJobNum = conf.get(JOBS_SUCCESS_RETAINED.key, JOBS_SUCCESS_RETAINED.defaultValue.get)
	private val retainedFailedJobNum = conf.get(JOBS_FAILED_RETAINED.key, JOBS_FAILED_RETAINED.defaultValue.get)

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

	private var clusterProxyActorRef: ActorRef  = _

	private var localProxyActorRef: ActorRef  = _


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


		akkaSystem.scheduler.schedule(
			new FiniteDuration(1, SECONDS),
			new FiniteDuration(1, SECONDS),
			self,
			ScheduleJob)

		// for debug
		akkaSystem.scheduler.schedule(
			new FiniteDuration(10, SECONDS),
			new FiniteDuration(10, SECONDS)) {
			println(s"-----------$state---------")
			println(nodes.map(n => n.toString + ":" + n.state).mkString("\n"))
			println(addressToNode.map(_.toString()).mkString("\n"))
			if(state == RoleState.MASTER) {
				println(persistenceEngine.readNodes().mkString("\n"))
			}
			println
		}

		//TODO: all node should start persist engine ?
		clusterProxyActorRef = akkaSystem.actorOf(Props(classOf[MbClusterActor], conf, self), s"MbClusterActor")

		localProxyActorRef = akkaSystem.actorOf(Props(classOf[MbLocalActor], conf, catalogContext), s"MbLocalActor")

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
		case request: MbMetaDataApi =>
			metaData.apply(request)

		case response: JobStateChanged =>
			handle.apply(response)

		case request: MbApi =>
			process.apply(request)

		case ScheduleJob =>  //TODO:
			schedule()

		case StartedBatchAppResponse(jobId) =>
			logInfo(s"StartedBatchAppResponse $jobId")
			runningJobs.get(jobId) match {
				case Some(jobInfo) =>
					val expectedSeq = 0
					if (expectedSeq < jobInfo.cmds.length) {
						val command = jobInfo.cmds(expectedSeq)

						sendSqlToDestination(command, jobInfo)
					} else {
						jobInfo.client ! JobCompleteWithExternalData(jobId, None)
					}
				case None =>
			}

		case ReportYarnAppResource(adhocInfo, batchInfo) =>
			master ! ReportNodesResource(NODE_ID, adhocInfo, batchInfo)

		case ReportNodesResource(id, adhocInfo, jobs) =>
			val nodeOption = nodes.find(_.id == id)
			if (nodeOption.isDefined) {
				nodeOption.get.yarnAdhocFreeCore = adhocInfo.coresFree
				nodeOption.get.yarnAdhocFreeMemory = adhocInfo.memoryFree
				nodeOption.get.yarnRunningBatchJob = jobs
			}

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
				if(state == RoleState.MASTER) {
					println(persistenceEngine.readNodes().mkString("\n"))
				}
				println
			}

		case CompleteRecovery => completeRecovery()

		case RegisterNode(id, nodeHost, nodePort, endpoint, cores, memory, nodeJdbcPort, nodeRestPort) =>
			if (state == RoleState.SLAVE) {
				endpoint ! NotMasterNode
			} else {
				val node = new NodeInfo(id, nodeHost, nodePort, cores, memory, endpoint)
				node.jdbcPort = nodeJdbcPort
				node.restPort = nodeRestPort
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
			master ! RegisterNode(NODE_ID, host, port, self, 100, 100, jdbcPort, restPort)

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


		case RegisterTimedEvent(event) =>
			val target = sender()
			Future {
				if (!timedEventService.timedEventExists(event.group, event.name)) {
					timedEventService.addTimedEvent(event)}
			}.onComplete {
				case Success(_) =>
					target ! RegisteredTimedEvent
				case Failure(e) =>
					target ! RegisterTimedEventFailed(e.getMessage)
			}

		case UnregisterTimedEvent(group, name) =>
			val target = sender()
			Future {
				timedEventService.deleteTimedEvent(group, name)
			}.onComplete {
				case Success(_) =>
					target ! UnregisteredTimedEvent
				case Failure(e) =>
					target ! UnregisterTimedEventFailed(e.getMessage)
			}

		case e =>
			println(e)
	}



	private def handle: Receive = {
		case JobStateChangedInternal(jobId, jobState) =>
			logInfo(s"In Master: Job $jobId state changed to $jobState ")
			jobState match {
				case JobState.SUCCESS =>
					runningJobs.get(jobId) match {
						case Some(jobInfo) =>
							jobInfo.status = jobState //update status
							jobInfo.updateTime = Utils.now
							retainedCompleteJobs += jobId
							completeJobs.put(jobId, jobInfo)
							runningJobs.remove(jobId)
							jobIdToJobRunner.remove(jobId)  //batch clear mapping in master
							trimJobsIfNecessary(retainedCompleteJobs, completeJobs, retainedSuccessJobNum)
						case None =>
						// do nothing
					}

				case JobState.FAILED | JobState.KILLED =>
					runningJobs.get(jobId) match {
						case Some(jobInfo) => //batch no response, adhoc response in node proxy
							jobInfo.status = jobState //update status
							jobInfo.updateTime = Utils.now
							retainedFailedJobs += jobId
							failedJobs.put(jobId, jobInfo)
							runningJobs.remove(jobId)
							jobIdToJobRunner.remove(jobId)  //batch clear mapping in master
							trimJobsIfNecessary(retainedFailedJobs, failedJobs, retainedFailedJobNum)
						case None =>
						// do nothing
					}
				case _ =>
			}

		case m@JobStateChanged(jobId, taskSeq, jobState, result) =>
			logInfo(s"JobStateChanged send $jobId, $taskSeq, $jobState, $result")
			runningJobs.get(jobId) match {
				case Some(jobInfo) =>
					jobState match {
						case JobState.SUCCESS =>
							if (jobInfo.seq == taskSeq) {
								val expectedSeq = taskSeq + 1
								if (expectedSeq < jobInfo.cmds.length) {
									val nextCmd = jobInfo.cmds(expectedSeq)
									jobInfo.seq += 1
									sendSqlToDestination(nextCmd, jobInfo)
								}
								else {
									val response = result match {
										case DirectData(_, schema, data, hasNext) =>
											JobCompleteWithDirectData(jobId, schema, data, hasNext)
										case UnitData =>
											JobCompleteWithExternalData(jobId, None)
									}
									jobInfo.client ! response

									runningJobs.remove(jobId)
									if (jobInfo.sessionId.isEmpty) { //kill yarn batch
										clusterProxyActorRef ! StopBatchAppByPeace(jobId)
									}
									master ! JobStateChangedInternal(jobId, jobState)
								}
							}

						case JobState.FAILED | JobState.KILLED	=>
								if (jobState == JobState.FAILED) {
									jobInfo.client ! JobFailed(jobId, result.asInstanceOf[Failed].message)
								} else {
									jobInfo.client ! JobCancelSuccess(jobId)
								}
								runningJobs.remove(jobId)
								master ! JobStateChangedInternal(jobId, jobState) //TODO: this is master

						case _ =>
					}
				case None =>
			}

	}

	private def sendSqlToDestination(command: MbCommand,  jobInfo: JobInfo) = {
		logInfo(s"sendSqlToDestination send $command $jobInfo")

		if (jobInfo.isLocal) {
			localProxyActorRef ! AssignCommandToWorker(CommandInfo(jobInfo.jobId, jobInfo.sessionId, command, jobInfo.seq))
		}
		else {
			command match {
				case m: MQLQuery =>
					clusterProxyActorRef ! AssignTaskToWorker(TaskInfo(jobInfo.jobId, jobInfo.sessionId, QueryTask(m.query), jobInfo.seq, jobInfo.username))
				case m: CreateTempView =>
					val t = CreateTempViewTask(m.name, m.query, m.isCache, m.replaceIfExists)
					clusterProxyActorRef ! AssignTaskToWorker(TaskInfo(jobInfo.jobId, jobInfo.sessionId, t, jobInfo.seq, jobInfo.username))
				case m: InsertInto =>
					val t = InsertIntoTask(m.table.table, m.table.database, m.query, m.overwrite)
					clusterProxyActorRef ! AssignTaskToWorker(TaskInfo(jobInfo.jobId, jobInfo.sessionId, t, jobInfo.seq, jobInfo.username))
				case m: MbRunnableCommand => //TODO:
					localProxyActorRef ! AssignCommandToWorker(CommandInfo(jobInfo.jobId, jobInfo.sessionId, m, jobInfo.seq, jobInfo.username))
			}
		}
	}

	private def process: Receive = {
		case m@RequestAccess(connectionType, isLocal) =>
			if (state == RoleState.MASTER) {
				val requester = sender()
				val nodeOpt = selectAdhocNode(isLocal)
				nodeOpt match {
					case Some(node) =>
						val port = connectionType match {
							case ConnectionType.REST => node.restPort
							case ConnectionType.JDBC => node.jdbcPort
							case ConnectionType.ODBC => node.odbcPort
						}
						requester ! RequestedAccess(s"${node.host}:$port")
					case None => requester ! RequestAccessFailed("no available node in moonbox cluster")
				}
			} else {
				master forward m
			}

		case m@OpenSession(username, database, isLocal) =>
			val client = sender()
			val actorRef = if (isLocal) {
				localProxyActorRef
			} else {
				clusterProxyActorRef
			}

			actorRef.ask(AllocateSession(username, database)).mapTo[AllocateSessionResponse].onComplete {
				case Success(rsp) =>
					rsp match {
						case AllocatedSession(sessionId) =>
							sessionIdToJobRunner.put(sessionId, (actorRef, isLocal))
							client ! OpenedSession(sessionId)
						case AllocateSessionFailed(error) =>
							client ! OpenSessionFailed(error)
					}
				case Failure(e) =>
					client ! OpenSessionFailed(e.getMessage)
			}

		case CloseSession(sessionId) =>
			val client = sender()
			sessionIdToJobRunner.get(sessionId) match {
				case Some(Tuple2(worker, islocal)) =>
					val future = worker.ask(FreeSession(sessionId)).mapTo[FreeSessionResponse]
					future.onComplete {
						case Success(response) =>
							response match {
								case FreedSession(id) =>
									sessionIdToJobRunner.remove(id)
									client ! ClosedSession
								case FreeSessionFailed(error) =>
									client ! CloseSessionFailed(error)
							}
						case Failure(e) =>
							client ! CloseSessionFailed(e.getMessage)
					}
				case None =>
					client ! CloseSessionFailed(s"Session $sessionId does not exist.")
			}

		case JobQuery(sessionId, username, sqls) =>
			val client = sender()
			val now = Utils.now
			val date = new Date(now)
			val jobId = newJobId(date)
			try {
				sessionIdToJobRunner.get(sessionId) match {
					case Some(runner) =>
						val jobInfo = createJob(jobId, Some(username), Some(sessionId), sqls, None, runner._2, now, client)
						jobInfo.status = JobState.RUNNING //update status
						runningJobs.put(jobInfo.jobId, jobInfo)  //for response to client

						val nextSeq = 0
						if (nextSeq < jobInfo.cmds.length) {
							val command = jobInfo.cmds(nextSeq)
							//TODO: privilege check here

							sendSqlToDestination(command, jobInfo)
						}
					case None =>
						client ! JobFailed(jobId, "Session lost in master.")
				}
			} catch {
				case e: Exception =>
					client ! JobFailed(jobId, e.getMessage)
			}
		// Batch:
		//           ^------->   schedule      ^---f---->
		// client ---| node1 |---> master -----| node2  |----proxy------yarnAPP -----> Runner
		// client <--------------- master -------------------proxy------yarnAPP------- Runner
		//
		case job@JobSubmit(username, sqls, config, async) =>
			if ( state == RoleState.SLAVE ) {
				master forward job
			}
			else if ( state == RoleState.MASTER ) {
				val client = sender() //enqueue for schedule later
				val now = Utils.now
				val date = new Date(now)
				val jobId = newJobId(date)  //master generate jobId
				try {
					val jobInfo = createJob(jobId, Some(username), None, sqls, Some(config), false, now, client)
					waitingJobs.enqueue(jobInfo)
					persistenceEngine.addJob(jobInfo)
					if (async) {
						client ! JobAccepted(jobInfo.jobId)
					}
				} catch {
					case e: Exception =>
						client ! JobFailed(jobId, e.getMessage)
				}
			}

		// node --> clusterproxy
		case m@JobSubmitInternal(jobInfo) =>
			runningJobs.put(jobInfo.jobId, jobInfo)  //for response to client
			clusterProxyActorRef ! StartBatchAppByPeace(jobInfo.jobId, jobInfo.config.get)



		case m@JobProgress(jobId) =>
			if ( state == RoleState.SLAVE ) {
				master forward m
			}
			else {
				val client = sender()
				val jobInfo = runningJobs.get(jobId).orElse(
					completeJobs.get(jobId).orElse(
						failedJobs.get(jobId).orElse(
							waitingJobs.find(_.jobId == jobId)
						)
					)
				)
				jobInfo match {
					case Some(job) =>
						client ! JobProgressState(job.jobId, job)
					case None =>
						client ! JobFailed(jobId, s"Job $jobId does not exist or has been removed.")
				}
			}

		case m@FetchData(sessionId, jobId, fetchSize) =>
			val client = sender()
			sessionIdToJobRunner.get(sessionId) match {
				case Some(Tuple2(actor, isLocal)) => actor forward FetchDataFromRunner(sessionId, jobId, fetchSize)
				case None => client ! FetchDataFailed(s"sessionId $sessionId does not exist or has been removed.")
			}

		case m@JobCancel(jobId) =>
			if ( state == RoleState.SLAVE ) {
				master forward m
			}
			else {
				val client = sender()
				waitingJobs.find(_.jobId == jobId) match {
					case Some(jobInfo) =>
						waitingJobs.dequeueFirst(_.jobId == jobId)
						persistenceEngine.removeJob(jobInfo)
						client ! JobCancelSuccess(jobInfo.jobId)
					case None =>
						if (sessionIdToJobRunner.contains(jobId)) { //adhoc, sessionid --> jobId
							runningJobs.filter(_._2.sessionId.isDefined).find(_._2.sessionId.get == jobId).map(_._2) match {
								case Some(jobInfo) =>
									val worker = sessionIdToJobRunner(jobId)._1
									worker ! JobCancelInternal(jobId)
								case None =>
									client ! JobCancelSuccess(jobId)
							}
						} else {
							runningJobs.get(jobId) match { //batch --> jobId
								case Some(jobInfo) =>
									jobIdToJobRunner.get(jobId) match {
										case Some(worker) =>
											worker ! JobCancelInternal(jobId)
										case None =>
											client ! JobCancelSuccess(jobId)
									}
								case None =>
									client ! JobCancelSuccess(jobId)
							}
						}
				}
			}

		case m@JobCancelInternal =>
			clusterProxyActorRef forward m

		case m@GetYarnAppsInfo =>
			clusterProxyActorRef forward m

		case m@KillYarnApp =>
			clusterProxyActorRef forward m

		case m@StartYarnApp =>
			clusterProxyActorRef forward m

		case m@GetNodesInfo =>
			if (state == RoleState.SLAVE) {
				master forward m
			} else {
				val requester = sender()
				val schema = Seq("id", "host", "port", "jdbc", "rest", "local_core", "local_mem", "yarn_core", "yarn_mem" )
				val data = nodes.map{ elem=>
					Seq(elem.id, elem.host, elem.port, elem.jdbcPort, elem.restPort, elem.coresFree, elem.memoryFree, elem.yarnAdhocFreeCore, elem.yarnAdhocFreeMemory)}.toSeq
				requester ! GottenNodesInfo(schema, data)
			}

	}


	private def metaData: Receive = {
		case m: ShowDatabasesInfo =>
			localProxyActorRef forward m

		case m: ShowTablesInfo =>
			localProxyActorRef forward m

		case m: DescribeTableInfo =>
			localProxyActorRef forward m
	}

	private def schedule(): Unit = {  //only for batch schedule
		//TODO: jobId to cluster or local
		if (waitingJobs.nonEmpty) {
			val jobInfo = waitingJobs.head
			val nodeOpt = nodes.toSeq.sortWith(_.yarnRunningBatchJob < _.yarnRunningBatchJob).headOption

			nodeOpt match {
				case Some(node) =>
					val e = node.endpoint
					jobIdToJobRunner.put(jobInfo.jobId, e)
					runningJobs.put(jobInfo.jobId, waitingJobs.dequeue().copy(status = JobState.RUNNING, updateTime = Utils.now))
					persistenceEngine.removeJob(jobInfo)
					e ! JobSubmitInternal(jobInfo)
				case None =>
			}
		}
	}

	private def selectAdhocNode(isLocal: Boolean): Option[NodeInfo] = {  //only for adhoc apply
		val nodeOpt = if (isLocal) {
			nodes.toSeq.filter(_.coresFree > 0).sortWith(_.coresFree > _.coresFree).headOption
		} else {
			nodes.toSeq.filter(_.yarnAdhocFreeCore > 0).sortWith(_.yarnAdhocFreeCore > _.yarnAdhocFreeCore).headOption
		}
		nodeOpt
	}

	private def selectNode(): String = {
		null
	}

	private def tryRegisteringToMaster(): Unit = {
		persistenceEngine.readMasterAddress().foreach { address =>
			logInfo(s"Try registering to master $address")
			val core = Runtime.getRuntime.availableProcessors
			val memory = Runtime.getRuntime.freeMemory
			akkaSystem.actorSelection(address + NODE_PATH).tell(RegisterNode(NODE_ID, host, port, self, core, memory, jdbcPort, restPort), self)
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

	def newJobId(submitDate: Date): String = {
		val jobId = "job-%s-%05d".format(createDateFormat.format(submitDate), nextJobNumber)
		nextJobNumber += 1
		jobId
	}

	private def trimJobsIfNecessary(retainedJobs: ArrayBuffer[String], statusJobs: mutable.HashMap[String, JobInfo], retainedJobNum: Int) = {
		if (retainedJobs.size > retainedJobNum) {
			val removeSize = math.max(retainedJobs.size / 10, retainedJobNum - retainedJobs.size)  //delete at least 1/10 of jobs if it is full
			retainedJobs.take(removeSize).foreach { jobId =>
				statusJobs.remove(jobId)
			}
			retainedJobs.trimStart(removeSize)
		}
	}

	val mbParser = new MbParser
	private def createJob(jobId: String,
						  username: Option[String],
						  sessionId: Option[String],
						  sqls: Seq[String],
						  config: Option[String],
						  isLocal: Boolean,
						  createTime: Long,
						  client: ActorRef): JobInfo = {
		val commands = sqls.map(mbParser.parsePlan)
		JobInfo(
			jobId = jobId,
			sessionId = sessionId,
			cmds = commands,
			seq = 0,
			isLocal,
			config = config,
			status = JobState.WAITING,
			errorMessage = None,
			username = username,
			submitTime = createTime,
			updateTime = createTime,
			client = client
		)
	}
}

object Moonbox extends MbLogging {

	val NODE_NAME = "moonbox"
	val NODE_PATH = s"/user/$NODE_NAME"
	var master: ActorRef = _

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
