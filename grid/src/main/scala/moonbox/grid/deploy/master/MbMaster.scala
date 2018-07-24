/*-
 * <<
 * Moonbox
 * ==
 * Copyright (C) 2016 - 2018 EDP
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

import java.io.{PrintWriter, StringWriter}
import java.text.SimpleDateFormat
import java.util.{Date, Locale}

import akka.actor.{Actor, ActorRef, ActorSystem, Address, PoisonPill, Props, Terminated}
import akka.cluster.Cluster
import akka.cluster.ClusterEvent._
import akka.cluster.client.ClusterClientReceptionist
import akka.cluster.singleton.{ClusterSingletonManager, ClusterSingletonManagerSettings, ClusterSingletonProxy, ClusterSingletonProxySettings}
import akka.pattern._
import akka.util.Timeout
import com.typesafe.config.ConfigFactory
import moonbox.common.util.Utils
import moonbox.common.{MbConf, MbLogging}
import moonbox.core.CatalogContext
import moonbox.core.command.{ShowJobInfo, ShowRunningEventInfo, ShowSysInfo}
import moonbox.core.parser.MbParser
import moonbox.grid.api._
import moonbox.grid.config._
import moonbox.grid.deploy.DeployMessages._
import moonbox.grid.deploy.master.MbMasterMessages.ScheduleJob
import moonbox.grid.deploy.rest.RestServer
import moonbox.grid.deploy.transport.TransportServer
import moonbox.grid.deploy.worker.{MbWorker, WorkerInfo}
import moonbox.grid.deploy.{DeployMessage, MbService}
import moonbox.grid.timer.{TimedEventService, TimedEventServiceImpl}
import moonbox.grid.{CachedData, UnitData, _}
import org.apache.spark.sql.Row

import scala.collection.JavaConverters._
import scala.collection._
import scala.collection.mutable.ArrayBuffer
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration._
import scala.util.{Failure, Success}

class MbMaster(param: MbMasterParam, implicit val akkaSystem: ActorSystem) extends Actor with MbLogging {
	import MbMaster._
	// For job IDs
	private def createDateFormat = new SimpleDateFormat("yyyyMMddHHmmss", Locale.US)

	private val conf: MbConf = param.conf
	private val WORKER_TIMEOUT_MS = conf.get(WORKER_TIMEOUT.key, WORKER_TIMEOUT.defaultValue.get)
	private val PERSISTENCE_MODE = conf.get(PERSIST_IMPLEMENTATION.key, PERSIST_IMPLEMENTATION.defaultValue.get)
	private implicit val ASK_TIMEOUT = Timeout(FiniteDuration(60, SECONDS))
	private var nextJobNumber = 0
	private val workers = new mutable.HashMap[ActorRef, WorkerInfo]
    private val recoverWorkers = new mutable.HashMap[Address, (ActorRef, WorkerInfo, String)]

	/*private val jobIdToClient = new mutable.HashMap[String, ActorRef]()*/
	private val jobIdToWorker = new mutable.HashMap[String, ActorRef]()

	// only for batch
	private val waitingJobs = new mutable.Queue[JobInfo]

	// for batch and adhoc
	private val runningJobs = new mutable.HashMap[String, JobInfo]
	private val completeJobs = new mutable.HashMap[String, JobInfo]
	private val retainedCompleteJobs = new ArrayBuffer[String]()
	private val failedJobs = new mutable.HashMap[String, JobInfo]
	private val retainedFailedJobs = new ArrayBuffer[String]()


	private val workerToRunningJobIds = new mutable.HashMap[ActorRef, mutable.Set[String]]()

	// for context dependent
	private val sessionIdToWorker = new mutable.HashMap[String, ActorRef]()

	private val cluster: Cluster = Cluster(context.system)//Cluster.get(akkaSystem)
	cluster.subscribe(self, classOf[UnreachableMember], classOf[ReachableMember])

	private var catalogContext: CatalogContext = _
	private var persistenceEngine: PersistenceEngine = _
	private var singletonMaster: ActorRef = _
	private var resultGetter: ActorRef = _
	private var timedEventService: TimedEventService = _
	/*private var checkForWorkerTimeOutTask: Cancellable = _*/

	private val timerServiceEnabled = conf.get(TIMER_SERVICE_ENABLE.key, TIMER_SERVICE_ENABLE.defaultValue.get)

	private val restServerEnabled = conf.get(REST_SERVER_ENABLE.key, REST_SERVER_ENABLE.defaultValue.get)
	private var restServer: Option[RestServer] = None
	private var restServerBoundPort: Option[Int] = None

	private val tcpServerEnabled = conf.get(TCP_SERVER_ENABLE.key, TCP_SERVER_ENABLE.defaultValue.get)
	private var tcpServer: Option[TransportServer] = None
	private var tcpServerBoundPort: Option[Int] = None
	private var odbcServerBoundPort: Option[Int] = None

	private val mbParser = new MbParser


	@scala.throws[Exception](classOf[Exception])
	override def preStart(): Unit = {
		logInfo("PreStart master ...")
        cluster.subscribe(self, classOf[UnreachableMember], classOf[ReachableMember])

		catalogContext = new CatalogContext(conf)

		/*checkForWorkerTimeOutTask = akkaSystem.scheduler.schedule(
			FiniteDuration(0, MILLISECONDS),
			FiniteDuration(WORKER_TIMEOUT_MS, MILLISECONDS),
			self,
			CheckForWorkerTimeOut
		)*/

		singletonMaster = startMasterEndpoint(akkaSystem)
		MbMaster.singleton = singletonMaster
		resultGetter = akkaSystem.actorOf(Props(classOf[ResultGetter], conf), "result-getter")

		val serviceImpl = new MbService(conf, catalogContext, singletonMaster, resultGetter)

		if (timerServiceEnabled) {
			timedEventService = new TimedEventServiceImpl(conf)
			timedEventService.start()
		}

		if (restServerEnabled) {
			restServer = Some(new RestServer(param.host, param.restPort, conf, serviceImpl, akkaSystem))
		}
		if (tcpServerEnabled) {
			tcpServer = Some(new TransportServer(param.host, param.tcpPort, conf, serviceImpl))
		}

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

		recovery()

		val initialDelay = conf.get(SCHEDULER_INITIAL_WAIT.key, SCHEDULER_INITIAL_WAIT.defaultValue.get)
		val interval = conf.get(SCHEDULER_INTERVAL.key, SCHEDULER_INTERVAL.defaultValue.get)

		akkaSystem.scheduler.schedule(
			FiniteDuration(initialDelay, MILLISECONDS),
			FiniteDuration(interval, MILLISECONDS),
			self,
			ScheduleJob
		)

		odbcServerBoundPort = startODBCServer(serviceImpl)
		restServerBoundPort = restServer.map(_.start())
		tcpServerBoundPort = tcpServer.map(_.start())

    	self ! MasterChanged
		logInfo(s"MbMaster start successfully.")
	}

	@scala.throws[Exception](classOf[Exception])
	override def postStop(): Unit = {

		/*if (checkForWorkerTimeOutTask != null && !checkForWorkerTimeOutTask.isCancelled) {
			checkForWorkerTimeOutTask.cancel()
		}*/

		restServer.foreach(_.stop())
		tcpServer.foreach(_.stop())
		if (timedEventService != null) {
			timedEventService.stop()
		}
		persistenceEngine.close()
		cluster.leave(cluster.selfAddress)
	}


	override def receive: Receive = {
		case request: MbApi =>
			// TODO privilege check
			process.apply(request)
		case control =>
			internal.apply(control)
	}

	private def internal: Receive = {
		case ScheduleJob =>
			schedule()
			//logInfo(s"worker ${workers}")

		case RegisterWorker(id, endpoint, cores, memory) =>
			logInfo(s"Registering worker $endpoint with $cores cores, $memory RAM")
			if (workers.contains(endpoint)) {
				endpoint ! RegisterWorkerFailed(s"Duplicate worker $endpoint")
			} else {
				context.watch(sender())
				val workerInfo = new WorkerInfo(id, cores, memory, endpoint)
				workerInfo.coresFree = cores
				workerInfo.memoryFree = memory
				workers.put(sender(), workerInfo)
				endpoint ! RegisteredWorker(self)
				schedule()
			}

		case JobStateChanged(jobId, state, result) =>
			logInfo(s"Job $jobId state changed to $state $result")
			state match {
				case JobState.SUCCESS =>
					runningJobs.get(jobId) match {
						case Some(jobInfo) =>
							val response = result match {
								case CachedData =>
									JobCompleteWithCachedData(jobId)
								case DirectData(data) =>
									JobCompleteWithDirectData(jobId, data)
								case UnitData =>
									JobCompleteWithExternalData(jobId, None)
							}
							jobInfo.client ! response
							jobInfo.status = state //update status
                            jobInfo.updateTime = Utils.now
							completeJobs.put(jobId, jobInfo)
							runningJobs.remove(jobId)
						case None =>
							// do nothing
					}

				case JobState.FAILED | JobState.KILLED=>
					runningJobs.get(jobId) match {
						case Some(jobInfo) =>
							jobInfo.client ! JobFailed(jobId, result.asInstanceOf[Failed].message)
							jobInfo.status = state //update status
                            jobInfo.updateTime = Utils.now
							failedJobs.put(jobId, jobInfo)
							runningJobs.remove(jobId)
						case None =>
						// do nothing
					}
				case _ =>
			}

		case WorkerLatestState(workerInfo) =>
			logInfo(s"Worker ${workerInfo.id} update state $workerInfo")
			workers.update(workerInfo.endpoint, workerInfo)

		case Heartbeat =>
			workers.get(sender()) match {
				case Some(workerInfo) =>
					workerInfo.lastHeartbeat = Utils.now
				case None =>
					// do nothing
			}
		case Terminated(worker) =>
			logInfo(s"Terminated $worker")
			workerToRunningJobIds.get(worker) match {
				case Some(jobIds) =>
					jobIds.foreach { jobId =>
						runningJobs.get(jobId) match {
							case Some(jobInfo) =>
								if (jobInfo.sessionId.isDefined) { // context dependent
									runningJobs.remove(jobInfo.jobId)
									jobInfo.client ! JobFailed(jobInfo.jobId, s"Worker $worker shutdown.")
								} else {
									runningJobs.remove(jobInfo.jobId)
									val newJobInfo = jobInfo.copy(status = JobState.WAITING,
										updateTime = Utils.now)
									waitingJobs.enqueue(newJobInfo)
								}
							case None =>	// do nothing
						}
					}
				case None =>	// do nothing
			}
			workers.get(worker) match {
				case Some(workerInfo) =>
					workers.remove(worker)
					persistenceEngine.removeWorker(workerInfo)
				case None =>
			}
        case UnreachableMember(member) =>
            logInfo(s"UnreachableMember $member")
			val unreachableWorkers = workers.filterKeys(_.path.address == member.address)
			unreachableWorkers.foreach { case (actorRef, workerInfo) =>
				sessionIdToWorker.find(_._2 == actorRef).foreach { case (sessionId, _) =>
					recoverWorkers.put(member.address, (actorRef, workerInfo, sessionId))
					sessionIdToWorker.remove(sessionId)
				}
			}
			unreachableWorkers.foreach { case (ref, _) => workers.remove(ref) }

/*            workers.filter{elem => elem._1.path.address == member.address}
                   .foreach{elem =>
                       recoverWorkers.update(member.address, (elem._1, elem._2, ""))
                       workers.remove(elem._1)
                   }

            sessionIdToWorker.filter{elem => elem._2.path.address == member.address}
                    .foreach{elem =>
                        if(recoverWorkers.contains(member.address)){
                            val worker = recoverWorkers(member.address)
                            recoverWorkers.update(member.address, (worker._1, worker._2, elem._1))  //update session id
                        }
                        sessionIdToWorker.remove(elem._1)
                    }*/
            logInfo(s"new worker is $workers, new sessionId is $sessionIdToWorker")

        case ReachableMember(member) =>
            logInfo(s"ReachableMember $member")
            val worker = recoverWorkers.get(member.address)
			worker match {
				case Some((actorRef, workerInfo, sessionId)) =>
					if(!workers.contains(actorRef)) {
						workers.put(actorRef, workerInfo)
					}
					if(!sessionIdToWorker.contains(sessionId)) {
						sessionIdToWorker.put(sessionId, actorRef)
					}
					recoverWorkers.remove(member.address)
				case None =>
			}
            logInfo(s"new worker is $workers, new sessionId is $sessionIdToWorker")
        case _: CurrentClusterState =>

		case ch: MasterChanged.type =>
			logInfo("MasterChanged")
			informAliveWorkers(ch)

		case RegisterTimedEvent(event) =>
			val target = sender()
			Future {
				checkTimedServcieValid()
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
				checkTimedServcieValid()
				timedEventService.deleteTimedEvent(group, name)
			}.onComplete {
				case Success(_) =>
					target ! UnregisteredTimedEvent
				case Failure(e) =>
					target ! UnregisterTimedEventFailed(e.getMessage)
			}

		case a =>
			logWarning(s"Unknown Message: $a")

	}

	private def informAliveWorkers(msg: DeployMessage): Unit = {
		cluster.state.members.filter(_.hasRole(MbWorker.ROLE))
			.map { member =>
				val Some(host) = member.address.host
				val Some(port) = member.address.port
				val address = s"akka.tcp://${akkaSystem.name}@$host:$port${MbWorker.WORKER_PATH}"
				logInfo(s"informAliveWorkers  $address")
				context.actorSelection(address)
			}.foreach(_.forward(msg))
	}

	private def process: Receive = {
		case OpenSession(username, database) =>
			val client = sender()
			val candidate = selectWorker()
			candidate match {
				case Some(worker) =>
					val future = worker.ask(AllocateSession(username, database)).mapTo[AllocateSessionResponse]
					future.onComplete {
						case Success(response) =>
							response match {
								case AllocatedSession(sessionId) =>
									sessionIdToWorker.put(sessionId, worker)
									client ! OpenedSession(sessionId)
								case AllocateSessionFailed(error) =>
									client ! OpenSessionFailed(error)
							}
						case Failure(e) =>
							client ! OpenSessionFailed(e.getMessage)
					}
				case None =>
					client ! OpenSessionFailed("there is no available worker.")
			}
		case CloseSession(sessionId) =>
			val client = sender()
			sessionIdToWorker.get(sessionId) match {
				case Some(worker) =>
					val future = worker.ask(FreeSession(sessionId)).mapTo[FreeSessionResponse]
					future.onComplete {
						case Success(response) =>
							response match {
								case FreedSession(id) =>
									sessionIdToWorker.remove(id)
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
				val jobInfo = createJob(jobId, Some(username), Some(sessionId), sqls, now, client)
                if (jobInfo.cmds.isEmpty) {
                    client ! JobFailed(jobInfo.jobId, "no sql defined.")
                } else if ((jobInfo.cmds.contains(ShowSysInfo) || jobInfo.cmds.contains(ShowJobInfo) ||
                        jobInfo.cmds.contains(ShowRunningEventInfo)) && jobInfo.cmds.length > 1) {
                    client ! JobFailed(jobInfo.jobId, "System command must be used alone.")
                } else if ((jobInfo.cmds.contains(ShowSysInfo) || jobInfo.cmds.contains(ShowJobInfo) ||
                        jobInfo.cmds.contains(ShowRunningEventInfo)) && jobInfo.cmds.length == 1) {
                    doSystemCommand(jobInfo, client)
                } else {
                    sessionIdToWorker.get(sessionId) match {
                        case Some(worker) =>
                            jobInfo.status = JobState.RUNNING  //update status
                            runningJobs.put(jobInfo.jobId, jobInfo)
                            worker ! AssignJobToWorker(jobInfo)
                        case None =>
                            client ! JobFailed(jobInfo.jobId, "Session lost in master.")
                    }
                }
			} catch {
				case e: Exception =>
					client ! JobFailed(jobId, e.getMessage)
			}

		case JobSubmit(username, sqls, async) =>
			val client = sender()
			val now = Utils.now
			val date = new Date(now)
			val jobId = newJobId(date)
			try {
				val jobInfo = createJob(jobId, Some(username), None, sqls, now, client)
                if (jobInfo.cmds.isEmpty) {
                    client ! JobRejected("no sql defined.")
                } else if ((jobInfo.cmds.contains(ShowSysInfo) || jobInfo.cmds.contains(ShowJobInfo) ||
                        jobInfo.cmds.contains(ShowRunningEventInfo)) && jobInfo.cmds.length > 1) {
                    client ! JobFailed(jobInfo.jobId, "System command must be used alone.")
                } else if ((jobInfo.cmds.contains(ShowSysInfo) || jobInfo.cmds.contains(ShowJobInfo) ||
                        jobInfo.cmds.contains(ShowRunningEventInfo)) && jobInfo.cmds.length == 1) {
                    doSystemCommand(jobInfo, client)
                } else {
                    waitingJobs.enqueue(jobInfo)
                    persistenceEngine.addJob(jobInfo)
                    if (async) {
                        client ! JobAccepted(jobInfo.jobId)
                    }
                }
			} catch {
				case e: Exception =>
					client ! JobFailed(jobId, e.getMessage)
			}
		case JobProgress(jobId) =>
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

		case JobCancel(appId) =>
			val client = sender()
			waitingJobs.find(_.jobId == appId) match {
				case Some(jobInfo) =>
					waitingJobs.dequeueFirst(_.jobId == appId)
					persistenceEngine.removeJob(jobInfo)
                    client ! JobCancelSuccess (jobInfo.jobId)
				case None =>
                    if(sessionIdToWorker.contains(appId)){  //adhoc, sessionid --> jobId
                        runningJobs.filter(_._2.sessionId.isDefined).find(_._2.sessionId.get == appId).map(_._2) match {
                            case Some (jobInfo) =>
                                val worker = sessionIdToWorker(appId)
                                worker ! RemoveJobFromWorker (jobInfo)
                                client ! JobCancelSuccess (jobInfo.jobId)

                            case None =>
                                client ! JobCancelSuccess (appId)
                        }
                    }else {
                        runningJobs.get(appId) match {  //batch --> jobId
                            case Some(jobInfo) =>
                                jobIdToWorker.get(appId) match {
                                    case Some(worker) =>
                                        worker ! RemoveJobFromWorker(jobInfo)
                                        client ! JobCancelSuccess(appId)
                                    case None =>
                                        client ! JobCancelSuccess(appId)
                                }
                            case None =>
                                client ! JobCancelSuccess (appId)
                        }
                    }

			}
	}

    private def doSystemCommand(jobInfo: JobInfo, client: ActorRef): Unit = {
        val jobId= jobInfo.jobId
        if(jobInfo.cmds.last == ShowSysInfo){
            val result = getSysInfo()
            client ! JobCompleteWithDirectData(jobId, result)
        } else if(jobInfo.cmds.last == ShowJobInfo) {
            val result = getJobInfo()
            client ! JobCompleteWithDirectData(jobId, result)
        } else {
            val result = getEventInfo()
            client ! JobCompleteWithDirectData(jobId, result)
        }
    }

    private def getSysInfo(): Seq[Seq[String]] = {
        val aliveWorkerRow: Seq[Seq[String]] = workers.map{ elem =>
            val workerInfo = elem._2
            val actorAddress = elem._1.actorRef.path.address
			Row(workerInfo.id, "alive", actorAddress.host.getOrElse(""), actorAddress.port.getOrElse(0),
				workerInfo.cores, workerInfo.coresFree, workerInfo.memory, workerInfo.memoryFree)
        }.map(_.toSeq.map(_.toString)).toSeq

        val deadWorkerRow: Seq[Seq[String]] = recoverWorkers.map{ elem =>
            val workerInfo = elem._2._2
            val actorAddress = elem._1
			Row(workerInfo.id, "dead", actorAddress.host.getOrElse(""), actorAddress.port.getOrElse(0),
				workerInfo.cores, workerInfo.coresFree, workerInfo.memory, workerInfo.memoryFree)
        }.map(_.toSeq.map(_.toString)).toSeq


		Seq(Seq("worker id", "in cluster", "host", "port", "total cores", "free cores", "total memory", "free memory")) ++ aliveWorkerRow ++ deadWorkerRow
    }

    private def getEventInfo(): Seq[Seq[String]] = {
        val eventInfo = timedEventService.getTimedEvents().map{ event =>
            val startTime = event.startTime.map(Utils.formatDate).getOrElse("")
            val endTime  = event.endTime.map(Utils.formatDate).getOrElse("")
            val preTime  = event.preFireTime.map(Utils.formatDate).getOrElse("")
            val nextTime = event.nextFireTime.map(Utils.formatDate).getOrElse("")

            Row(event.group, event.name, event.cronDescription, event.status, startTime, endTime, preTime, nextTime)
        }.map(_.toSeq.map(_.toString))
        Seq(Seq("group", "name", "desc", "status", "start", "end", "prev", "next")) ++ eventInfo
    }

    private def getJobInfo(): Seq[Seq[String]] = {
        val noExist = "*"
        val waitingRows: Seq[Seq[String]] = waitingJobs.map{ jobInfo =>
            Row(jobInfo.jobId, "batch",jobInfo.cmds.mkString(";\n"), jobInfo.status, jobInfo.username.getOrElse(""), Utils.formatDate(jobInfo.submitTime), Utils.formatDate(jobInfo.updateTime), noExist)}.map(_.toSeq.map(_.toString))

        val runningRows: Seq[Seq[String]] = runningJobs.map{case (jobId, jobInfo) =>
            val jobType = if(jobInfo.sessionId.isDefined) {"adhoc"} else {"batch"}
            val workerId = if(jobInfo.sessionId.isDefined){
                val sessionId = jobInfo.sessionId.get
                if(sessionIdToWorker.contains(sessionId)){
                    val worker = sessionIdToWorker(sessionId)
                    if(workers.contains(worker)){ workers(worker).id } else {noExist}
                }else {noExist}

            }else {
                if (jobIdToWorker.contains(jobId)) {
                    val worker = jobIdToWorker(jobId)
                    if(workers.contains(worker)){ workers(worker).id } else {noExist}
                } else {noExist}
            }

            Row(jobId, jobType, jobInfo.cmds.mkString(";\n"), jobInfo.status, jobInfo.username.getOrElse(""), Utils.formatDate(jobInfo.submitTime), Utils.formatDate(jobInfo.updateTime), workerId)}.map(_.toSeq.map(_.toString)).toSeq

        val completeRows: Seq[Seq[String]] = completeJobs.map{case (jobId, jobInfo) =>
            val jobType = if(jobInfo.sessionId.isDefined) {"adhoc"} else {"batch"}
            Row(jobId, jobType, jobInfo.cmds.mkString(";\n"), jobInfo.status, jobInfo.username.getOrElse(""), Utils.formatDate(jobInfo.submitTime), Utils.formatDate(jobInfo.updateTime), noExist)}.map(_.toSeq.map(_.toString)).toSeq

        val failedRows: Seq[Seq[String]] = failedJobs.map{case (jobId, jobInfo) =>
            val jobType = if(jobInfo.sessionId.isDefined) {"adhoc"} else {"batch"}
            Row(jobId, jobType, jobInfo.cmds.mkString(";\n"), jobInfo.status, jobInfo.username.getOrElse(""), Utils.formatDate(jobInfo.submitTime), Utils.formatDate(jobInfo.updateTime), noExist)}.map(_.toSeq.map(_.toString)).toSeq


        Seq(Seq("job id", "type", "command", "status", "submit by", "submit time", "update time", "running worker")) ++ waitingRows ++ runningRows ++ completeRows ++ failedRows
    }

	/*private def timeOutDeadWorkers(): Unit = {
		val currentTime = Utils.now
		val toRemove = workers.filter {
			case (endpoint, workerInfo) =>
				workerInfo.lastHeartbeat < currentTime - WORKER_TIMEOUT_MS
		}
		workers.--(toRemove.keys)
		toRemove.foreach {case (key, value) => persistenceEngine.removeWorker(value)}
	}*/

	private def schedule(): Unit = {
		// jobId to worker
		if (waitingJobs.nonEmpty) {
			val jobInfo = waitingJobs.head
			val worker = selectWorker()
			worker match {
				case Some(w) =>
					jobIdToWorker.put(jobInfo.jobId, w)
					runningJobs.put(jobInfo.jobId, waitingJobs.dequeue().copy(status = JobState.RUNNING, updateTime = Utils.now))
					persistenceEngine.removeJob(jobInfo)
					w ! AssignJobToWorker(jobInfo)
				case None =>
			}
		}
	}

	private def selectWorker(): Option[ActorRef] = {
		// TODO
		val candidate = workers.filter { case (ref, workerInfo) =>
			(workerInfo.coresFree > 0 ) && (workerInfo.memoryFree > 0)
		}
		if (candidate.isEmpty) None
		else {
			val selected = candidate.toSeq.sortWith(_._2.coresFree > _._2.coresFree).head._1
			Some(selected)
		}
//		val selected = workers.toSeq.sortWith(_._2.coresFree > _._2.coresFree).map(_._1).headOption
//		selected
	}

	private def createJob(jobId: String, username: Option[String], sessionId: Option[String], sqls: Seq[String], createTime: Long, client: ActorRef): JobInfo = {
		val commands = sqls.map(mbParser.parsePlan)
		JobInfo(
			jobId = jobId,
			sessionId = sessionId,
			cmds = commands,
			status = JobState.WAITING,
			errorMessage = None,
			username = username,
			submitTime = createTime,
			updateTime = createTime,
			client = client
		)
	}

	private def newJobId(submitDate: Date): String = {
		val jobId = "job-%s-%05d".format(createDateFormat.format(submitDate), nextJobNumber)
		nextJobNumber += 1
		jobId
	}

	private def recovery(): Unit = {
		try {
			waitingJobs.enqueue(persistenceEngine.readJobs():_*)
			logInfo(s"Recovery from ${persistenceEngine.getClass.getSimpleName}")
		} catch {
			case e: Throwable =>
				logWarning(s"Recovery from ${persistenceEngine.getClass.getSimpleName} failed.")
		}
	}

	private def startMasterEndpoint(akkaSystem: ActorSystem): ActorRef = {
		val singletonProps = ClusterSingletonProxy.props(
			settings = ClusterSingletonProxySettings(akkaSystem)
				.withRole(ROLE).withBufferSize(5000)
				.withSingletonIdentificationInterval(new FiniteDuration(5, SECONDS)),
			singletonManagerPath = SINGLETON_MANAGER_PATH)

		val endpoint = akkaSystem.actorOf(singletonProps, SINGLETON_PROXY_NAME)
		ClusterClientReceptionist(akkaSystem).registerService(endpoint)
		logInfo(s"startMasterEndpoint $endpoint")
		endpoint
	}

	private def startODBCServer(service: MbService): Option[Int] = try {
		val className = conf.get(ODBC_SERVER_CLASS.key, ODBC_SERVER_CLASS.defaultValueString)
		val mbODBCServer = Class.forName(className)
		val constructor = mbODBCServer.getConstructor(classOf[MbConf], classOf[MbService])
		val method = mbODBCServer.getDeclaredMethod("start0")
		val instance = constructor.newInstance(conf, service)
		logInfo(s"Thrift server is started.")
		Some(method.invoke(instance).asInstanceOf[Int])
	} catch {
		case _: ClassNotFoundException =>
			logWarning(s"No thrift server implementation found.")
			None
		case e: Exception =>
			logError(e.getMessage)
			None
	}

	private def checkTimedServcieValid(): Unit = {
		if (!timerServiceEnabled || timedEventService == null) {
			throw new Exception("Timer Service is out of service.")
		}
	}
}

object MbMaster extends MbLogging {
	val ROLE = "master"
	val RECEPTIONIST_PATH = "/system/receptionist"
	val MASTER_NAME = "mbmaster"
	val SINGLETON_PROXY_NAME = "singleton-master-proxy"
	val SINGLETON_MANAGER_PATH = s"/user/$MASTER_NAME"
	var singleton: ActorRef = _

	def main(args: Array[String]) {
		val conf = new MbConf()
		val param = new MbMasterParam(args, conf)
		val akkaSystem = ActorSystem(param.clusterName, ConfigFactory.parseMap(param.akkaConfig.asJava))
		val masterProp = ClusterSingletonManager.props(
			singletonProps = Props(classOf[MbMaster], param, akkaSystem),
			PoisonPill,
			ClusterSingletonManagerSettings(akkaSystem).withRole(ROLE)
		)
		try {
			akkaSystem.actorOf(masterProp, MASTER_NAME)
		} catch {
			case e: Exception =>
				logError(e.getMessage)
				akkaSystem.terminate()
		}
	}

}
