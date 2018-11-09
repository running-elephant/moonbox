package moonbox.grid.deploy2.node

import java.text.SimpleDateFormat
import java.util.{Date, Locale, Random}

import akka.actor.{Actor, ActorRef, ActorSystem, Address, Cancellable, ExtendedActorSystem, Props}
import akka.cluster.{Cluster, ClusterEvent}
import akka.cluster.ClusterEvent._
import akka.pattern._
import akka.util.Timeout
import com.typesafe.config.ConfigFactory
import moonbox.common.util.Utils
import moonbox.common.{MbConf, MbLogging}
import moonbox.core.CatalogContext
import moonbox.core.command.{CreateTempView, InsertInto, MQLQuery, MbCommand, MbRunnableCommand, UseDatabase}
import moonbox.core.parser.MbParser
import moonbox.grid.api._
import moonbox.grid.config._
import moonbox.grid.deploy2.MbService
import moonbox.grid.deploy2.node.DeployMessages._
import moonbox.grid.deploy2.node.ScheduleMessage._
import moonbox.grid.deploy2.rest.RestServer
import moonbox.grid.deploy2.transport.TransportServer
import moonbox.grid.runtime.cluster.MbClusterActor
import moonbox.grid.runtime.local.MbLocalActor
import moonbox.grid.timer.{TimedEventService, TimedEventServiceImpl}
import moonbox.grid.{ConnectionType, JobInfo}
import moonbox.protocol.app.{FreeSessionResponse, _}

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

	private val registeredNodes = new mutable.HashSet[NodeInfo]()
	private val addressToNode = new mutable.HashMap[Address, NodeInfo]()

	// for batch and interactive
	private val runningJobs = new mutable.HashMap[String, JobInfo]() //in every node

	// for batch in master
	private val jobIdToNode = new mutable.HashMap[String, ActorRef]()
	private val waitingJobs = new mutable.Queue[JobInfo]()
	private val allocatedBatchJobs = new mutable.HashMap[String, JobInfo]()  //only in master
	private val completeJobs = new mutable.HashMap[String, JobInfo]()
	private val failedJobs = new mutable.HashMap[String, JobInfo]()

	// for interactive
	private val sessionIdToJobRunner = new mutable.HashMap[String, ActorRef]()  // actorRef is local / remote

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
			println(registeredNodes.map(n => n.toString + ":" + n.state).mkString("\n"))
			println(addressToNode.map(_.toString()).mkString("\n"))
			if(state == RoleState.MASTER) {
				println(persistenceEngine.readNodes().mkString("\n"))
			}
			println
		}

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

		case request: MbManagementApi =>
			management.apply(request)

		case response: JobStateChanged =>
			jobResult.apply(response)

		case request: MbNodeApi =>
			node.apply(request)

		case request: DeployMessage  =>
			deployment.apply(request)

		case request: ClusterEvent.ReachabilityEvent =>
			deployment.apply(request)

		case request: ScheduleMessage =>
			schedule.apply(request)

		case request: MbJobApi =>
			interface.apply(request)

		case request: AppApi =>
			application.apply(request)

		case e =>
			logInfo(s"moonbox receiving unknown message $e")
	}

	private def application: Receive = {
		case StartedBatchAppResponse(jobId) =>
			logInfo(s"StartedBatchAppResponse $jobId")
			runningJobs.get(jobId) match {
				case Some(jobInfo) =>
					val expectedSeq = 0
					if (expectedSeq < jobInfo.cmds.length) {
						val command = jobInfo.cmds(expectedSeq)

						sendSqlToDest(command, jobInfo)
					} else {
						jobInfo.client ! JobCompleteWithExternalData(jobId, None)
					}
				case None =>
			}
	}

	private def schedule: Receive = {
		case ScheduleJob =>  //TODO:
			scheduleJob()

		case ReportYarnAppResource(adhocInfo, batchNum) =>
			if(master != null) {
				master ! ReportNodesResource(NODE_ID, adhocInfo, batchNum)
			}

		case ReportNodesResource(id, adhocInfo, jobs) =>
			val nodeOption = registeredNodes.find(_.id == id)
			if (nodeOption.isDefined) {
				nodeOption.get.yarnAdhocFreeCore = adhocInfo.coresFree
				nodeOption.get.yarnAdhocFreeMemory = adhocInfo.memoryFree
				nodeOption.get.yarnRunningBatchJob = jobs
			}

		case m@RegisterTimedEvent(event) =>
			if (state == RoleState.SLAVE) {
				master forward m
			} else {
				val target = sender()
				Future {
					if (!timedEventService.timedEventExists(event.group, event.name)) {
						timedEventService.addTimedEvent(event)
					}
				}.onComplete {
					case Success(_) =>
						target ! RegisteredTimedEvent
					case Failure(e) =>
						target ! RegisterTimedEventFailed(e.getMessage)
				}
			}

		case m@UnregisterTimedEvent(group, name) =>
			if (state == RoleState.SLAVE) {
				master forward m
			} else {
				val target = sender()
				Future {
					timedEventService.deleteTimedEvent(group, name)
				}.onComplete {
					case Success(_) =>
						target ! UnregisteredTimedEvent
					case Failure(e) =>
						target ! UnregisterTimedEventFailed(e.getMessage)
				}
			}
	}


	private def deployment: Receive = {
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
				println(registeredNodes.map(n => n.toString + ":" + n.state).mkString("\n"))
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
					scheduleJob()
				} else {
					val nodeAddress = endpoint.path.address
					logWarning("Node registration failed. Attempted to re-register node at same address: " + nodeAddress)
					endpoint ! RegisterNodeFailed("Attempted to re-register node at same address: " + nodeAddress)
				}
			}
		case RegisterNodeFailed(message) =>
			logError(s"RegisterNodeFailed $message")

		case RevokedLeadership =>
			if (state == RoleState.MASTER) {
				cluster.unsubscribe(self)
				state = RoleState.SLAVE
			}
			if (timedEventService != null) {
				timedEventService.stop()
			}
			registeredNodes.clear()
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
			registeredNodes.filter(_.endpoint.path.address == member.address).foreach { node =>
				node.state = NodeState.ALIVE
				addressToNode(member.address) = node
				persistenceEngine.addNode(node)
			}
			logInfo(s"Node reconnected: " + member.address)
		case n@NotMasterNode => logInfo(n.toString)
		case other => logInfo(s"moonbox receiving unknown deploy message: $other")

	}

	private def node: Receive = {
		case JobStateChangedInternal(jobId, jobState) =>
			logInfo(s"In Master: Job $jobId state changed to $jobState ")
			jobState match {
				case JobState.SUCCESS =>
					allocatedBatchJobs.get(jobId) match {
						case Some(jobInfo) =>
							jobInfo.status = jobState //update status
							jobInfo.updateTime = Utils.now
							retainedCompleteJobs += jobId
							completeJobs.put(jobId, jobInfo)
							runningJobs.remove(jobId)
							jobIdToNode.remove(jobId)  //batch clear mapping in master
							trimJobsIfNecessary(retainedCompleteJobs, completeJobs, retainedSuccessJobNum)
						case None =>
						// do nothing
					}

				case JobState.FAILED | JobState.KILLED =>
					allocatedBatchJobs.get(jobId) match {
						case Some(jobInfo) => //batch no response, adhoc response in node proxy
							jobInfo.status = jobState //update status
							jobInfo.updateTime = Utils.now
							retainedFailedJobs += jobId
							failedJobs.put(jobId, jobInfo)
							runningJobs.remove(jobId)
							jobIdToNode.remove(jobId)  //batch clear mapping in master
							trimJobsIfNecessary(retainedFailedJobs, failedJobs, retainedFailedJobNum)
						case None =>
						// do nothing
					}
				case _ =>
			}

		case m@JobCancelInternal(jobId) =>
			state match {
				case RoleState.MASTER =>
					waitingJobs.find(_.jobId == jobId) match {
						case Some(jobInfo) =>
							logInfo(s"Handling job cancel: jobInfo=$jobInfo, RoleState=MASTER")
							waitingJobs.dequeueFirst(_.jobId == jobId)
							persistenceEngine.removeJob(jobInfo)
							/* for audit */
							jobInfo.status = JobState.KILLED //update status
							jobInfo.updateTime = Utils.now
							retainedFailedJobs += jobId
							failedJobs.put(jobId, jobInfo)
							trimJobsIfNecessary(retainedFailedJobs, failedJobs, retainedFailedJobNum)
						case None =>
							jobIdToNode.get(jobId) match {
								case Some(node) =>
									logInfo(s"Transferring job cancel to slave node(node=$node): jobId=$jobId")
									node ! JobCancelInternal(jobId)
								case None => /* no-op */
									logWarning(s"Invalid jobId in jobCancel: jobId=$jobId")
							}
					}
				case RoleState.SLAVE =>
					clusterProxyActorRef forward m
				case RoleState.RECOVERING =>
				case RoleState.COMPLETING_RECOVERY =>
				case other => logError(s"Unknown role state: $other")
			}
		// node --> clusterproxy
		case m@JobSubmitInternal(jobInfo) =>
			runningJobs.put(jobInfo.jobId, jobInfo)  //for response to client
			clusterProxyActorRef ! StartBatchAppByPeace(jobInfo.jobId, jobInfo.config.get)

	}


	private def jobResult: Receive = {
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
									sendSqlToDest(nextCmd, jobInfo)
								}
								else { //the last sql
									val response = result match {
										case DirectData(_, schema, data, hasNext) =>
											JobCompleteWithDirectData(jobId, schema, data, hasNext)
										case UnitData =>
											JobCompleteWithExternalData(jobId, None)
									}
									jobInfo.client ! response

									runningJobs.remove(jobId)
									if (jobInfo.localSessionId.isEmpty && jobInfo.clusterSessionId.isEmpty) { //kill yarn batch
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

	private def sendSqlToDest(command: MbCommand, jobInfo: JobInfo) = {
		logInfo(s"sendSqlToDestination send $command $jobInfo")

		if (jobInfo.isLocal) {
			localProxyActorRef ! AssignCommandToWorker(CommandInfo(jobInfo.jobId, jobInfo.localSessionId, command, jobInfo.seq))
		}
		else {
			command match {
				case m: MQLQuery =>
					clusterProxyActorRef ! AssignTaskToWorker(TaskInfo(jobInfo.jobId, jobInfo.clusterSessionId, QueryTask(m.query), jobInfo.seq, jobInfo.username))
				case m: CreateTempView =>
					val t = CreateTempViewTask(m.name, m.query, m.isCache, m.replaceIfExists)
					clusterProxyActorRef ! AssignTaskToWorker(TaskInfo(jobInfo.jobId, jobInfo.clusterSessionId, t, jobInfo.seq, jobInfo.username))
				case m: InsertInto =>
					val t = InsertIntoTask(m.table.table, m.table.database, m.query, m.overwrite)
					clusterProxyActorRef ! AssignTaskToWorker(TaskInfo(jobInfo.jobId, jobInfo.clusterSessionId, t, jobInfo.seq, jobInfo.username))
				case m: UseDatabase =>
					clusterProxyActorRef ! AssignTaskToWorker(TaskInfo(jobInfo.jobId, jobInfo.clusterSessionId, UseDatabaseTask(m.db), jobInfo.seq, jobInfo.username))
					localProxyActorRef ! AssignCommandToWorker(CommandInfo(jobInfo.jobId, jobInfo.localSessionId, m, jobInfo.seq, jobInfo.username))
				case m: MbRunnableCommand => //TODO:
					localProxyActorRef ! AssignCommandToWorker(CommandInfo(jobInfo.jobId, jobInfo.localSessionId, m, jobInfo.seq, jobInfo.username))
			}
		}
	}

	private def interface: Receive = {
		case m@RequestAccess(connectionType, isLocal) =>
			logInfo(s"RequestAccess $connectionType, $isLocal")
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
			logInfo(s"OpenSession $username, $database, $isLocal")
			val client = sender()
			if (isLocal) {
				localProxyActorRef.ask(AllocateSession(username, database)).mapTo[AllocateSessionResponse].onComplete {
					case Success(rsp) =>
						rsp match {
							case AllocatedSession(sessionId) =>
								sessionIdToJobRunner.put(sessionId, localProxyActorRef)
								client ! OpenedSession(sessionId)
							case AllocateSessionFailed(error) =>
								client ! OpenSessionFailed(error)
						}
					case Failure(e) =>
						client ! OpenSessionFailed(e.getMessage)
				}
			} else {
				def handle(response1: AllocateSessionResponse, actor1: ActorRef, response2: AllocateSessionResponse, actor2: ActorRef) = {
					if (response1.isInstanceOf[AllocatedSession] && response2.isInstanceOf[AllocatedSession]) {
						val rsp1 = response1.asInstanceOf[AllocatedSession]
						val rsp2 = response2.asInstanceOf[AllocatedSession]
						sessionIdToJobRunner.put(rsp1.sessionId, actor1)
						sessionIdToJobRunner.put(rsp2.sessionId, actor2)
						client ! OpenedSession(rsp2.sessionId + "#" + rsp1.sessionId) //  local sessionId # cluster sessionId
					} else if (response1.isInstanceOf[AllocateSessionFailed] && response2.isInstanceOf[AllocateSessionFailed]) {
						val rsp1 = response1.asInstanceOf[AllocateSessionFailed]
						val rsp2 = response2.asInstanceOf[AllocateSessionFailed]
						client ! OpenSessionFailed(rsp1.error + "#" + rsp2.error)
					} else if (response1.isInstanceOf[AllocatedSession] && response1.isInstanceOf[AllocateSessionFailed]) {
						val rsp1 = response1.asInstanceOf[AllocatedSession]
						val rsp2 = response2.asInstanceOf[AllocateSessionFailed]
						actor1 ! FreeSession(rsp1.sessionId)
						client ! OpenSessionFailed("#" + rsp2.error)
					} else if (response1.isInstanceOf[AllocateSessionFailed] && response2.isInstanceOf[AllocatedSession]) {
						val rsp1 = response1.asInstanceOf[AllocateSessionFailed]
						val rsp2 = response2.asInstanceOf[AllocatedSession]
						actor2 ! FreeSession(rsp2.sessionId)
						client ! OpenSessionFailed(rsp1.error + "#")
					}
				}

				Future {
					for {
						a <- clusterProxyActorRef.ask(AllocateSession(username, database)).mapTo[AllocateSessionResponse]
						b <- localProxyActorRef.ask(AllocateSession(username, database)).mapTo[AllocateSessionResponse]
					} yield handle(a, clusterProxyActorRef, b, localProxyActorRef)
				}
			}

		case CloseSession(sessionId) =>
			val client = sender()
			logInfo(s"CloseSession $sessionId")
			if(sessionId.split("#").length == 1) {
				val localSessionId = sessionId.split("#")(0)
				sessionIdToJobRunner.get(localSessionId) match {
					case Some(worker) =>
						val future = worker.ask(FreeSession(localSessionId)).mapTo[FreeSessionResponse]
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
			} else {
				def handle(response1: FreeSessionResponse, response2: FreeSessionResponse) = {
					if (response1.isInstanceOf[FreedSession] && response2.isInstanceOf[FreedSession]) {
						val rsp1 = response1.asInstanceOf[FreedSession]
						val rsp2 = response2.asInstanceOf[FreedSession]
						sessionIdToJobRunner.remove(rsp1.sessionId)
						sessionIdToJobRunner.remove(rsp2.sessionId)
						client ! ClosedSession
					} else if (response1.isInstanceOf[FreeSessionFailed] && response2.isInstanceOf[FreeSessionFailed]) {
						val rsp1 = response1.asInstanceOf[FreeSessionFailed]
						val rsp2 = response2.asInstanceOf[FreeSessionFailed]
						client ! CloseSessionFailed(rsp1.error + "#" + rsp2.error)
					} else if (response1.isInstanceOf[FreedSession] && response1.isInstanceOf[FreeSessionFailed]) {
						val rsp2 = response2.asInstanceOf[FreeSessionFailed]
						client ! CloseSessionFailed("#" + rsp2.error)
					} else if (response1.isInstanceOf[FreeSessionFailed] && response2.isInstanceOf[FreedSession]) {
						val rsp1 = response1.asInstanceOf[FreeSessionFailed]
						client ! CloseSessionFailed(rsp1.error + "#")
					}
				}

				Future {
					val localSessionId  = sessionId.split("#")(0)
					val remoteSessionId = sessionId.split("#")(1)
					for {
						a <- clusterProxyActorRef.ask(FreeSession(remoteSessionId)).mapTo[FreeSessionResponse]
						b <- localProxyActorRef.ask(FreeSession(localSessionId)).mapTo[FreeSessionResponse]
					} yield handle(a, b)
				}
			}

		case JobQuery(sessionId, username, sqls) =>
			logInfo(s"JobQuery $sessionId, $username $sqls")
			val client = sender()
			val now = Utils.now
			val date = new Date(now)
			val jobId = newJobId(date)
			try {
				if (sessionId.split("#").forall(id => sessionIdToJobRunner.contains(id))) {
					val jobInfo =  if ( sessionId.split("#").length == 1) {
						createJob(jobId, Some(username), Some(sessionId.split("#")(0)),  None, sqls, None, !sessionId.contains("#"), now, client)
					} else  {  // equal to 2
						createJob(jobId, Some(username), Some(sessionId.split("#")(0)),  Some(sessionId.split("#")(1)), sqls, None, !sessionId.contains("#"), now, client)
					}

					jobInfo.status = JobState.RUNNING //update status
					runningJobs.put(jobInfo.jobId, jobInfo) //for response to client

					val nextSeq = 0
					if (nextSeq < jobInfo.cmds.length) {
						val command = jobInfo.cmds(nextSeq)
						sendSqlToDest(command, jobInfo)
					}

				} else {
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
			logInfo(s"JobSubmit $username, $sqls, $config, $async")
			if ( state == RoleState.SLAVE ) {
				master forward job
			}
			else if ( state == RoleState.MASTER ) {
				val client = sender() //enqueue for schedule later
				val now = Utils.now
				val date = new Date(now)
				val jobId = newJobId(date)  //master generate jobId
				try {
					val jobInfo = createJob(jobId, Some(username), None, None, sqls, Some(config), false, now, client)
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
			val validSessionId = if (sessionId.indexOf("#") != -1) {
									sessionId.split('#')(1)
								 } else { sessionId }
			sessionIdToJobRunner.get(validSessionId) match {
				case Some(actor) =>
					val future = actor.ask(FetchDataFromRunner(validSessionId, jobId, fetchSize)).mapTo[FetchDataFromRunnerResponse]
					future.onComplete {
						case Success(response) =>
							response match {
								case FetchedDataFromRunner(jobId, schema, date, hasNext) =>
									client ! FetchDataSuccess(schema, date, hasNext)
								case FetchDataFromRunnerFailed(jobId, error) =>
									client ! FetchDataFailed(error)
							}
						case Failure(e) =>
							client ! FetchDataFailed(e.getMessage)
					}
				case None =>
					client ! FetchDataFailed(s"sessionId $sessionId does not exist or has been removed.")
			}


		case m@JobCancel(jobId, sessionId) =>
			logInfo(s"JobCancel $jobId, $sessionId")
			val requester = sender()
			(jobId, sessionId) match {
				case (_, Some(id)) =>
					/* for interactive mode */
					logInfo(s"Handling interactive job cancel: sessionId=$id")
					if (id.contains('#')) {
						/* cluster */
						id.split("#").foreach(sId => sessionIdToJobRunner.get(sId).foreach(_ ! JobCancelInternal(sId)))
					} else {
						/* local */
						sessionIdToJobRunner.get(id).foreach(_ ! JobCancelInternal(id))
					}
					requester ! JobCancelSuccess(id)
				case (Some(id), _) =>
					/* for batch mode */
					logInfo(s"Transferring batch job cancel to master: jobId=$id")
					master forward JobCancelInternal(id)
					requester ! JobCancelSuccess(id)
				case _ => /* no-op */
					val message = s"Invalid job cancel: jobId=${jobId.orNull}, sessionId={${sessionId.orNull}}"
					logWarning(message)
					requester ! JobCancelFailed(null, message)
			}
	}

	private def management: Receive = {
		case m@GetYarnAppsInfo =>
			clusterProxyActorRef forward m

		case m:KillYarnApp =>
			clusterProxyActorRef forward m

		case m:StartYarnApp =>
			clusterProxyActorRef forward m

		case m@GetNodesInfo =>
			if (state == RoleState.SLAVE) {
				master forward m
			} else {
				val requester = sender()
				val schema = Seq("id", "host", "port", "jdbc", "rest", "local_core", "local_mem", "yarn_core", "yarn_mem" )
				val data = registeredNodes.map{ elem=>
					Seq(elem.id, elem.host, elem.port, elem.jdbcPort, elem.restPort, elem.coresFree, elem.memoryFree, elem.yarnAdhocFreeCore, elem.yarnAdhocFreeMemory)}.toSeq
				requester ! GottenNodesInfo(schema, data)
			}

		case m@GetRunningEvents =>
			if (state == RoleState.SLAVE) {
				master forward m
			} else {
				val requester = sender()
				val schema = Seq("group", "name", "desc", "status", "start", "end", "prev", "next")
				val eventInfo = timedEventService.getTimedEvents().map{ event =>
					val startTime = event.startTime.map(Utils.formatDate).getOrElse("")
					val endTime  = event.endTime.map(Utils.formatDate).getOrElse("")
					val preTime  = event.preFireTime.map(Utils.formatDate).getOrElse("")
					val nextTime = event.nextFireTime.map(Utils.formatDate).getOrElse("")

					Seq(event.group, event.name, event.cronDescription, event.status, startTime, endTime, preTime, nextTime)
				}
				requester ! GottenRunningEvents(schema, eventInfo)
			}

		case m@GetNodeJobInfo =>
			val schema = Seq("jobid", "type", "command", "seq", "status", "submit by", "submit time", "update time")
			val requester = sender()
			val runningRows: Seq[Seq[Any]] = runningJobs.map { case (jobId, jobInfo) =>
				val jobType: String = if (jobInfo.localSessionId.isDefined && jobInfo.clusterSessionId.isDefined) { "adhoc-local" }
								else if (jobInfo.localSessionId.isDefined) { "adhoc-yarn" }
								else { "batch" }
				Seq(jobInfo.jobId, jobType, jobInfo.cmds.mkString(";\n"), jobInfo.seq, jobInfo.status, jobInfo.username.getOrElse(""),  Utils.formatDate(jobInfo.submitTime), Utils.formatDate(jobInfo.updateTime))
			}.toSeq
			requester ! GottenNodeJobInfo(schema, runningRows)

		case m@GetClusterJobInfo =>
			if (state == RoleState.SLAVE) {
				master forward m
			} else {
				val jobType = "batch"
				val requester = sender()
				val schema = Seq("jobid", "type", "command", "status", "submit by", "submit time", "update time", "running worker")
				val noExist = "*"
				val waitingRows = waitingJobs.map { jobInfo =>
					Seq(jobInfo.jobId, "batch", jobInfo.cmds.mkString(";\n"), jobInfo.status, jobInfo.username.getOrElse(""),
						Utils.formatDate(jobInfo.submitTime), Utils.formatDate(jobInfo.updateTime), noExist)
				}
				val runningRows: Seq[Seq[Any]] = allocatedBatchJobs.map { case (jobId, jobInfo) =>
					val nodeInfo = registeredNodes.find(_.endpoint == node)
					val workerId = if (nodeInfo.isDefined) { nodeInfo.get.id }
				    				else { noExist }
						Seq(jobId, jobType, jobInfo.cmds.mkString(";\n"), jobInfo.status, jobInfo.username.getOrElse(""), Utils.formatDate(jobInfo.submitTime), Utils.formatDate(jobInfo.updateTime), workerId)
					}.toSeq

				val completeRows: Seq[Seq[Any]] = completeJobs.map { case (jobId, jobInfo) =>
						Seq(jobId, jobType, jobInfo.cmds.mkString(";\n"), jobInfo.status, jobInfo.username.getOrElse(""), Utils.formatDate(jobInfo.submitTime), Utils.formatDate(jobInfo.updateTime), noExist)
					}.toSeq

				val failedRows: Seq[Seq[Any]] = failedJobs.map{case (jobId, jobInfo) =>
						Seq(jobId, jobType, jobInfo.cmds.mkString(";\n"), jobInfo.status, jobInfo.username.getOrElse(""), Utils.formatDate(jobInfo.submitTime), Utils.formatDate(jobInfo.updateTime), noExist)
					}.toSeq

				requester ! GottenClusterJobInfo(schema, waitingRows ++ runningRows ++ completeRows ++ failedRows)
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

	private def scheduleJob(): Unit = {  //only for batch schedule
		if (waitingJobs.nonEmpty) {
			val jobInfo = waitingJobs.head //TODO: how to schedule
			val nodeOpt = registeredNodes.toSeq.sortWith(_.yarnRunningBatchJob < _.yarnRunningBatchJob).headOption

			nodeOpt match {
				case Some(node) =>
					val e = node.endpoint
					jobIdToNode.put(jobInfo.jobId, e)
					allocatedBatchJobs.put(jobInfo.jobId, waitingJobs.dequeue().copy(status = JobState.RUNNING, updateTime = Utils.now))
					persistenceEngine.removeJob(jobInfo)
					e ! JobSubmitInternal(jobInfo)
				case None =>
			}
		}
	}

	private def selectAdhocNode(isLocal: Boolean): Option[NodeInfo] = {  //only for adhoc apply
		val nodes = if (isLocal) {
			registeredNodes.toSeq.filter(_.coresFree > 0).sortWith(_.coresFree > _.coresFree)
		} else {
			registeredNodes.toSeq.filter(_.yarnAdhocFreeCore > 0).sortWith(_.yarnAdhocFreeCore > _.yarnAdhocFreeCore)
		}
		/**  core:  4 3 2 1 -> interval:  4 7 9 10 -> [0-4), [4-7), [7-9), [9-10), find random number in which interval  */
		var initNum = 0
		val nodeWithInterval = nodes.map { node =>
			initNum += node.coresFree
			(initNum, node)
		}
		val randIndex = new Random().nextInt(initNum)
		nodeWithInterval.find(randIndex < _._1 ).map(_._2)

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
		registeredNodes.count(_.state == NodeState.UNKNOWN) == 0
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
		registeredNodes.filter(_.state == NodeState.UNKNOWN).foreach(removeNode)
		state = RoleState.MASTER
		logInfo("Recovery complete. New State: " + state)
		scheduleJob()
	}

	private def registerNode(node: NodeInfo): Boolean = {
		registeredNodes.filter { n =>
			(n.host == node.host && n.port == node.port) && (n.state == NodeState.DEAD)
		}.foreach(n => registeredNodes -= n)

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

		registeredNodes += node
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
						  localSessionId: Option[String],
						  clusterSessionId: Option[String],
						  sqls: Seq[String],
						  config: Option[String],
						  isLocal: Boolean,
						  createTime: Long,
						  client: ActorRef): JobInfo = {
		val commands = sqls.map(mbParser.parsePlan)
		JobInfo(
			jobId = jobId,
			localSessionId = localSessionId,
			clusterSessionId = clusterSessionId,
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
