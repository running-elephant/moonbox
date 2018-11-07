package moonbox.yarnapp

import java.util.UUID
import java.util.concurrent.CountDownLatch

import akka.actor.{Actor, ActorRef, ActorSystem, Cancellable, Props, Terminated}
import akka.util.Timeout
import moonbox.common.{MbConf, MbLogging}
import moonbox.core.MbSession
import moonbox.core.resource.ResourceMonitor
import moonbox.protocol.app._

import scala.collection.mutable
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration._
import scala.util.{Failure, Success}

class YarnAppActor(conf: MbConf, akkaSystem: ActorSystem, continue: CountDownLatch) extends Actor with MbLogging{
    private val nodeActorPath = conf.get("moonbox.mixcal.cluster.actor.path").get
    private val isOnYarn = conf.get("moonbox.grid.actor.yarn.on", "true").toBoolean
    private val id = conf.get("moonbox.mixcal.cluster.yarn.id").get
    private val batchIdOpt = conf.get("moonbox.mixcal.cluster.yarn.batchId")
    private var taskSeqNum: Int = -1
    private val nodeActorSelector = context.actorSelection(nodeActorPath)  //path ~ "akka.tcp://AkkaSystemName@host:port/user/ActorName"
    implicit val timeout: Timeout = Timeout(5 seconds)

    private val jobIdToJobRunner = new mutable.HashMap[String, ActorRef]      //batch id - runner, for kill session
    private val runnerToJobId = new mutable.HashMap[ActorRef, String]         //batch runner - id, for kill session
    private val sessionIdToJobRunner = new mutable.HashMap[String, ActorRef]

    private var resourceMonitor: ResourceMonitor = _

    var registerYarnAppScheduler: Cancellable = akkaSystem.scheduler.schedule(
        new FiniteDuration(10, SECONDS),
        new FiniteDuration(3, SECONDS)) {
        tryRegisteringToMoonbox()
    }

    def tryRegisteringToMoonbox(): Unit = {
        if(resourceMonitor != null) {
            nodeActorSelector ! RegisterAppRequest(id, batchIdOpt, taskSeqNum, resourceMonitor.clusterTotalCores, resourceMonitor.clusterTotalMemory, resourceMonitor.clusterFreeCores, resourceMonitor.clusterFreeMemory)
        }else{
            nodeActorSelector ! RegisterAppRequest(id, batchIdOpt, taskSeqNum, 0, 0, 0, 0)
        }
    }

    private def newSessionId(): String = UUID.randomUUID().toString


    override def preStart(): Unit = {
        MbSession.startMixcalEnv(conf, isOnYarn)  //yarn has started the sparkcontext, no need start sparkContext

        resourceMonitor = new ResourceMonitor

    }

    override def receive: Receive = {
        case RegisterAppResponse =>
            logInfo(s"recv RegisterAppResponse from ${sender().path}")

        case AllocateSession(username, database) =>
            val requester = sender()
            logInfo(s"MbWorker::AllocateSession $requester $username $database")
            Future {
                val mbSession = MbSession.getMbSession(conf).bindUser(username, database)
                val runner = context.actorOf(Props(classOf[Runner], conf, mbSession))
                val sessionId = newSessionId()
                sessionIdToJobRunner.put(sessionId, runner)
                sessionId
            }.onComplete {
                case Success(sessionId) =>
                    requester ! AllocatedSession(sessionId)
                case Failure(e) =>
                    requester ! AllocateSessionFailed(e.getMessage)
            }

        case FreeSession(sessionId) =>
            val requester = sender()
            logInfo(s"MbWorker::FreeSession $requester $sessionId")
            Future {
                if (sessionIdToJobRunner.get(sessionId).isDefined) {
                    val runner = sessionIdToJobRunner.get(sessionId).head
                    runner ! KillRunner //sessionId is not used
                    sessionIdToJobRunner.remove(sessionId)
                }
                sessionId
            }.onComplete {
                case Success(seid) =>
                    requester ! FreedSession(seid)
                case Failure(e) =>
                    requester ! FreeSessionFailed(e.getMessage)
            }

        case assign@AssignTaskToWorker(taskInfo) =>
            logInfo(s"MbWorker::AssignTaskToWorker $taskInfo")
            val requester = sender()
            taskSeqNum += 1

            taskInfo.sessionId match {
                case Some(sessionId) => // adhoc
                    sessionIdToJobRunner.get(sessionId) match {
                        case Some(runner) =>
                            runner forward RunJob(taskInfo)
                        case None =>
                            requester ! JobStateChanged(taskInfo.jobId, taskInfo.seq, JobState.FAILED, Failed("Session lost."))
                    }
                case None => // batch
                    jobIdToJobRunner.get(taskInfo.jobId) match {
                        case Some(runner) =>
                            runner forward RunJob(taskInfo)
                        case None =>
                            val session = MbSession.getMbSession(conf).bindUser(taskInfo.username.get)
                            val runner = context.actorOf(Props(classOf[Runner], conf, session))
                            jobIdToJobRunner.put(taskInfo.jobId, runner) //for batch cancel
                            runnerToJobId.put(runner, taskInfo.jobId)
                            context.watch(runner) // terminate
                            runner forward RunJob(taskInfo)
                    }
            }

        case kill@RemoveJobFromWorker(jobId) =>
            logInfo(s"MbWorker::RemoveJobFromWorker $jobId")
            if (sessionIdToJobRunner.contains(jobId) ) {  // adhoc
                val runner = sessionIdToJobRunner(jobId)
                runner forward CancelJob(jobId)
            }

            if (jobIdToJobRunner.contains(jobId)) {  //batch
                val runner = jobIdToJobRunner(jobId)
                runner forward CancelJob(jobId)
            }

        case m@FetchDataFromRunner(sessionId, jobId, fetchSize) =>
            logInfo(s"MbWorker::FetchDataFromRunner $m")
            val client = sender()
            sessionIdToJobRunner.get(sessionId) match {
                case Some(actor) => actor forward m
                case None => client ! FetchDataFromRunnerFailed(jobId, s"sessionId $sessionId does not exist or has been removed.")
            }

        case m@StopBatchAppByPeace(jobId) =>
            logInfo(s"MbWorker::StopBatchAppByPeace $jobId  ${jobIdToJobRunner.contains(jobId)}")
            if (jobIdToJobRunner.contains(jobId)) {
                stopAll
            }

        case Terminated(actor)  => context.unwatch(actor)  //only for batch
            logInfo(s"MbWorker::Terminated $actor}")
            val jobId: Option[String] = runnerToJobId.get(actor)
            if(jobId.isDefined && jobIdToJobRunner.contains(jobId.get) ) {
                jobIdToJobRunner.remove(jobId.get)
                stopAll  //TODO: batch kill it self app
            }
            runnerToJobId.remove(actor)
    }

    def stopAll: Unit = {
        logInfo("Yarn App Begin to EXIT")
        continue.countDown()
        logInfo("Yarn App Say: BYE!!!")
    }
}