package moonbox.grid.runtime.cluster

import java.io.File
import java.text.SimpleDateFormat
import java.util.concurrent.ConcurrentHashMap
import java.util.{Date, Locale}

import akka.actor.{Actor, ActorRef}
import akka.pattern._
import akka.util.Timeout
import com.typesafe.config.ConfigFactory
import moonbox.common.util.Utils
import moonbox.common.{MbConf, MbLogging}
import moonbox.grid.JobInfo
import moonbox.grid.api._
import moonbox.grid.config._
import moonbox.grid.deploy2.node.ScheduleMessage.ReportYarnAppResource
import moonbox.protocol.app.{StopBatchAppByPeace, _}
import org.apache.spark.launcher.SparkAppHandle
import org.json4s.DefaultFormats

import scala.collection.JavaConverters._
import scala.collection.mutable
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration.{FiniteDuration, MILLISECONDS, SECONDS}
import scala.util.{Failure, Success}

class MbClusterActor(conf: MbConf, nodeActor: ActorRef) extends Actor with MbLogging {

    private implicit val ASK_TIMEOUT = Timeout(FiniteDuration(60, SECONDS))

    private val sparkAppHandleMap: ConcurrentHashMap[String, Future[SparkAppHandle]] = new ConcurrentHashMap[String, Future[SparkAppHandle]]
    private val appListenerMap: ConcurrentHashMap[String, MbAppListener] = new ConcurrentHashMap[String, MbAppListener]()

    private val appResourceMap = new mutable.HashMap[String, AppResourceInfo]   //id -- yarn resource
    private val appActorRefMap = new mutable.HashMap[String, ActorRef]      //id -- actor ref
    private val appConfigurationMap = new mutable.HashMap[String, Map[String, String]] //id --- BATCH / ADHOC

    // for interactive
    private val sessionIdToJobRunner = new mutable.HashMap[String, ActorRef]()
    private val jobIdToJobRunner = new mutable.HashMap[String, ActorRef]()
    private val yarnIdToJobInfo = new mutable.HashMap[String, JobInfo]()

    private val isOnYarn = conf.get("moonbox.grid.actor.yarn.on", "true").toBoolean

    logInfo("path->" + self.path)

    private def getActorSelectorPath: String = {
        val akkaPort = context.system.settings.config.getString("akka.remote.netty.tcp.port")
        val akkaHost = context.system.settings.config.getString("akka.remote.netty.tcp.hostname")
        val akkaPath = MbClusterActor.PROXY_PATH
        val systemName = conf.get(CLUSTER_NAME)

        s"akka.tcp://$systemName@$akkaHost:$akkaPort$akkaPath"
    }

    private def startAppByConfig(launchConf: Map[String, String], batchJobId: Option[String] = None): String = {
        val jobType = if (batchJobId.isEmpty) { "adhoc" }
                      else { "batch" }
        val yarnId = newAppId + "_" + launchConf.getOrElse("name", "yarnapp") + "_" + jobType

        val yarnAppMainConf = mutable.Map.empty[String, String]
        yarnAppMainConf += ("moonbox.mixcal.cluster.actor.path" -> getActorSelectorPath)
        yarnAppMainConf += ("moonbox.mixcal.cluster.yarn.id" -> yarnId)
        batchJobId.map{ jobId => yarnAppMainConf += ("moonbox.mixcal.cluster.yarn.batchId" -> jobId) }

        val yarnAppConfig = launchConf.filter{elem => elem._1.indexOf("spark.hadoop.") != -1}
                .map{elem => (elem._1.replace("spark.hadoop.", ""), elem._2)}

        appConfigurationMap.put(yarnId, yarnAppConfig + ("job.mode" -> jobType))

        val appListener = new MbAppListener(yarnId, self)
        appListenerMap.put(yarnId, appListener)

        yarnAppMainConf  ++= conf.getAll.filter(_._1.toLowerCase.startsWith("moonbox"))
                                        .filter(_._1.toLowerCase.indexOf("moonbox.mixcal.local") == -1)  //must have this line
        val handler = Future {
            MbAppLauncher.launch(yarnAppMainConf.toMap, launchConf, appListener)
        }
        appResourceMap.put(yarnId, AppResourceInfo(-1, -1, -1, -1, -1, System.currentTimeMillis()))
        sparkAppHandleMap.put(yarnId, handler)
        yarnId
    }

    private def startLaunchAll(): Unit = {
        val defaultFile = new File(Utils.getDefaultPropertiesFile().get)
        val defaultConfig = ConfigFactory.parseFile(defaultFile)

        //start application for adhoc
        defaultConfig.getConfigList("moonbox.mixcal.cluster").asScala.foreach { elem =>
            val applicationConf = elem.entrySet().asScala.map{ c => (c.getKey, c.getValue.unwrapped().toString)}.toMap
            startAppByConfig(applicationConf)
        }
    }


    override def preStart: Unit = {
        if (isOnYarn) {
            startLaunchAll()
        }

        val initialDelay = conf.get(SCHEDULER_INITIAL_WAIT.key, SCHEDULER_INITIAL_WAIT.defaultValue.get)
        val interval = conf.get(SCHEDULER_INTERVAL.key, SCHEDULER_INTERVAL.defaultValue.get)

        context.system.scheduler.schedule(
            FiniteDuration(initialDelay, MILLISECONDS),
            FiniteDuration(interval, MILLISECONDS),
            new Runnable {
                override def run(): Unit = {
                    val allAvailableYarn: mutable.Map[AppResourceInfo, String] = appListenerMap.asScala.filter{_._2.state == SparkAppHandle.State.RUNNING}
                            .filter {elem => appResourceMap.contains(elem._1) && appConfigurationMap.contains(elem._1)}
                            .map {elem => (appResourceMap(elem._1), appConfigurationMap(elem._1).getOrElse("job.mode", "adhoc")) }

                    val adhocInfo = allAvailableYarn.filter(_._2 == "adhoc").keys.toSeq
                                                    .sortWith(_.coresFree > _.coresFree).headOption.getOrElse(AppResourceInfo(0, 0, 0, 0, 0, 0))

                    val batchInfo = jobIdToJobRunner.size

                    nodeActor ! ReportYarnAppResource(adhocInfo, batchInfo)
                }
            }
        )
    }



    override def receive: Receive = {
        case request: MbManagementApi =>
            management.apply(request)

        case request: AppApi =>
            log.info(request.toString)
            application.apply(request)  // TODO privilege check

        case request: MbNodeApi =>
            node.apply(request)

        case result: JobStateChanged =>
            jobResult.apply(result)

        case m =>
            logInfo(s"RECEIVE UNKNOWN MESG $m")

    }


    private def node: Receive = {
        case JobSubmitInternal(jobInfo) =>
            // Batch:
            //           ^------->   schedule      ^---f---->
            // client ---| node1 |---> master -----| node2  |----proxy------yarnAPP -----> Runner
            // client <--------------- master -------------------proxy------yarnAPP------- Runner
            //
            // 1. if config is None, use a old yarn application
            // 2. if config has string, start a new yarn application
            if (jobInfo.config.isDefined) {
                val typeConfig = ConfigFactory.parseString(jobInfo.config.get)
                val typeMap = typeConfig.entrySet().asScala.map{ c => (c.getKey, c.getValue.unwrapped().toString)}.toMap

                val yarnid = startAppByConfig(typeMap, Some(jobInfo.jobId))
                yarnIdToJobInfo.put(yarnid, jobInfo)
            } else { //TODO: how to do if no job config

            }

        case m@JobCancelInternal(jobId) =>
            if (sessionIdToJobRunner.contains(jobId)) { //adhoc, sessionid --> jobId
                sessionIdToJobRunner(jobId) ! RemoveJobFromWorker(jobId)
            }
            if (jobIdToJobRunner.contains(jobId)) {
                jobIdToJobRunner(jobId) ! RemoveJobFromWorker(jobId)
            }
    }


    private def application: Receive = {
        case m@RegisterAppRequest(id, batchJobId, seq, totalCores, totalMemory, freeCores, freeMemory) =>
            val appDriver = sender()
            appDriver ! RegisterAppResponse
            if (!appActorRefMap.contains(id)) {
                appActorRefMap.put(id, appDriver)

                if (batchJobId.isDefined) {  //batch first message trigger, only send once
                    logInfo(s"RegisterAppRequest batch jobId is started ${batchJobId.get}")
                    jobIdToJobRunner.put(batchJobId.get, appDriver)
                    nodeActor ! StartedBatchAppResponse(batchJobId.get)
                } else {
                    Future {
                        import org.json4s.jackson.Serialization.write
                        implicit val formats = DefaultFormats
                        val status = appListenerMap.get(id).state
                        val appId =  appListenerMap.get(id).appId
                        if (status == SparkAppHandle.State.SUBMITTED || status == SparkAppHandle.State.RUNNING) { //add
                            val cfg = if(appConfigurationMap.contains(id)) {
                                val map = appConfigurationMap(id)
                                Some(write(map))
                            } else { None }
                            Utils.updateYarnAppInfo2File(appId, cfg, true)
                        } else if (status == SparkAppHandle.State.FINISHED || status == SparkAppHandle.State.LOST) { //del
                            val cfg = if(appConfigurationMap.contains(id)) {
                                val map = appConfigurationMap(id)
                                Some(write(map))
                            } else { None }
                            Utils.updateYarnAppInfo2File(appId, cfg, false)
                        }
                    }
                }
            }

            if (appResourceMap.contains(id)){  //adhoc use this map
                appResourceMap.update(id, AppResourceInfo(totalCores, totalMemory, freeCores, freeMemory, System.currentTimeMillis(),  appResourceMap(id).submit))
            } else {
                appResourceMap.put(id, AppResourceInfo(totalCores, totalMemory, freeCores, freeMemory, System.currentTimeMillis(), System.currentTimeMillis()))
            }

            logInfo(s"RegisterAppRequest $totalCores, $totalMemory, $freeCores, $freeMemory")
            val appId = appListenerMap.get(id)
            if (appId == null) {
                logInfo(s"Yarn Application appId Not know yet registered. ref is $appDriver")
            }else {
                logInfo(s"Yarn Application $appId registered. ref is $appDriver")
            }

        case m@FetchDataFromRunner(sessionId, jobId, fetchSize) =>
            val client = sender()
            sessionIdToJobRunner.get(sessionId) match {
                case Some(actor) => actor forward FetchDataFromRunner(sessionId, jobId, fetchSize)
                case None => client ! FetchDataFromRunnerFailed(jobId, s"sessionId $sessionId $jobId does not exist or has been removed.")
            }

        case request@AllocateSession(username, database) =>
            val client = sender()
            val yarnApp = selectYarnAppForAdhoc()
            yarnApp match {
                case Some(yarnActorRef) =>
                    yarnActorRef.ask(request).mapTo[AllocateSessionResponse].onComplete {
                        case Success(rsp) =>
                            rsp match {
                                case m@AllocatedSession(sessionId) =>
                                    sessionIdToJobRunner.put(sessionId, yarnActorRef)
                                    client ! m
                                case m@AllocateSessionFailed(error) =>
                                    client ! m
                            }
                        case Failure(e) =>
                            client ! AllocateSessionFailed(e.getMessage)
                    }
                case None =>
                    client ! AllocateSessionFailed("there is no available worker.")
            }

        case request@FreeSession(sessionId) =>
            val client = sender()
            sessionIdToJobRunner.get(sessionId) match {
                case Some(worker) =>
                    val future = worker.ask(request).mapTo[FreeSessionResponse]
                    future.onComplete {
                        case Success(response) =>
                            response match {
                                case m@FreedSession(id) =>
                                    sessionIdToJobRunner.remove(id)
                                    client ! m
                                case m@FreeSessionFailed(error) =>
                                    client ! m
                            }
                        case Failure(e) =>
                            client ! FreeSessionFailed(e.getMessage)
                    }
                case None =>
                    client ! FreeSessionFailed(s"Session $sessionId does not exist.")
            }

        case request@AssignTaskToWorker(taskInfo) =>
            val client = sender()
            taskInfo.sessionId match {
                case Some(sessionId) =>
                    sessionIdToJobRunner.get(taskInfo.sessionId.get) match {  //TODO: for adhoc
                        case Some(worker) =>
                            logInfo(s"AssignTaskToWorker $taskInfo for adhoc $worker")
                            worker ! request
                        case None =>
                            client ! JobFailed(taskInfo.jobId, "Session lost in master.")
                    }
                case None =>
                    jobIdToJobRunner.get(taskInfo.jobId) match {
                        case Some(worker) =>
                            logInfo(s"AssignTaskToWorker $taskInfo for batch $worker")
                            worker ! request
                        case None =>
                            client ! JobFailed(taskInfo.jobId, "Session lost in master.")
                    }
            }

        case m@StopBatchAppByPeace(jobId) =>  //stop all finished batch
            logInfo(s"StopBatchAppByPeace $jobId")
            if (jobIdToJobRunner.contains(jobId)) {
                jobIdToJobRunner(jobId) ! m
                jobIdToJobRunner.remove(jobId)
            }

        case m@StartBatchAppByPeace(jobId, config) =>
            logInfo(s"StartBatchAppByPeace $jobId")
            val typeConfig = ConfigFactory.parseString(config)
            val typeMap = typeConfig.entrySet().asScala.map{ c => (c.getKey, c.getValue.unwrapped().toString)}.toMap
            startAppByConfig(typeMap, Some(jobId))

    }


    private def jobResult: Receive = {
        case m@JobStateChanged(jobId, seq, jobState, result) =>
            if (jobIdToJobRunner.contains(jobId)) { //batch clear mapping
                nodeActor ! m
            } else {
                logInfo(s"In node adhoc, Job $jobId state changed to $jobState $result")
                nodeActor ! m
            }
    }


    private def management: Receive = {
        case GetYarnAppsInfo =>
            val requester = sender()
            val info = appListenerMap.asScala.map { case (id, listener) =>
                val res = if (appResourceMap.contains(id)) {
                    val app = appResourceMap(id)
                    (app.coresTotal, app.memoryTotal, app.coresFree, app.memoryFree, Utils.formatDate(app.lastHeartbeat), Utils.formatDate(app.submit))
                } else {
                    (-1, -1L, -1, -1L, "", "")
                }
                val path = if (appActorRefMap.contains(id)) {
                    appActorRefMap(id).path.toString
                } else {
                    ""
                }
                Seq(id, listener.appId, listener.state.toString, path, res._1, res._2, res._3, res._4, res._5, res._6)
            }.toSeq
            val schema = Seq("id", "yarnId", "state", "path", "core", "memory", "freeCore", "freeMemory", "heartBeat", "submitted")
            requester ! GottenYarnAppsInfo(schema, info)

        case KillYarnApp(id) =>
            val requester = sender()
            Future{
                if (appListenerMap.containsKey(id)){
                    var currentState = appListenerMap.get(id).state
                    var tryTime = 0
                    while ( !currentState.isFinal && tryTime < 5 ) {
                        stopApp(id)
                        tryTime += 1
                        Thread.sleep(1000)
                        currentState = appListenerMap.get(id).state
                    }
                    if( !currentState.isFinal ){
                        killApp(id)
                        Thread.sleep(2000)
                        currentState = appListenerMap.get(id).state
                    }
                    if(currentState.isFinal) {
                        requester ! KilledYarnApp(id)
                    }else{
                        requester ! KilledYarnAppFailed(id, s"Yarn App Id $id can not stopped")
                    }
                } else {
                    requester ! KilledYarnAppFailed(id, s"No Yarn App Id $id is found in AppProxyActor")
                }
                killApp(id)
            }

        case StartYarnApp(config) =>
            val requester = sender()

            val typeConfig = ConfigFactory.parseString(config)
            val typeMap = typeConfig.entrySet().asScala.map{ c => (c.getKey, c.getValue.unwrapped().toString)}.toMap
            val id = startAppByConfig(typeMap)
            requester ! StartedYarnApp(id)
    }


    private def appId: Seq[String] = {
        appListenerMap.values().asScala.map{listener => listener.appId}.toSeq
    }

    private def stopApp(id: String): Unit = {
        if(sparkAppHandleMap.containsKey(id)) {
            sparkAppHandleMap.get(id).foreach(_.stop())
        }
    }

    private def killApp(id: String): Unit = {
        if(sparkAppHandleMap.containsKey(id)) {
            sparkAppHandleMap.get(id).foreach(_.kill())
        }
    }

    private def stopAllApp(): Unit = {
        sparkAppHandleMap.values().asScala.foreach(_.foreach(_.stop()))
    }

    private def killAllApp(): Unit = {
        sparkAppHandleMap.values().asScala.foreach(_.foreach(_.kill()))
    }

    private var nextAppNumber = 0
    def newAppId: String = {
        val submitDate = new Date(Utils.now)
        val appId = "app-%s-%05d".format(createDateFormat.format(submitDate), nextAppNumber)
        nextAppNumber += 1
        appId
    }

    private var nextJobNumber = 0
    private def createDateFormat = new SimpleDateFormat("yyyyMMddHHmmss", Locale.US)
    def newJobId(submitDate: Date): String = {
        val jobId = "job-%s-%05d".format(createDateFormat.format(submitDate), nextJobNumber)
        nextJobNumber += 1
        jobId
    }

    private def selectYarnAppForAdhoc(): Option[ActorRef] = {
        val now = System.currentTimeMillis()
        val availableNode = appListenerMap.asScala.filter(_._2.state == SparkAppHandle.State.RUNNING)
                .filter { elem => appResourceMap.contains(elem._1) && appActorRefMap.contains(elem._1) && appConfigurationMap.contains(elem._1) }
        val selected =  availableNode.filter(elem => appConfigurationMap(elem._1).getOrElse("job.mode", "adhoc") == "adhoc").filter(elem => appResourceMap(elem._1).coresFree > 0 && now - appResourceMap(elem._1).lastHeartbeat < 60000).keys
                    .toSeq.sortWith(appResourceMap(_).coresFree > appResourceMap(_).coresFree).map {
                appActorRefMap(_)
            }.headOption

        selected
    }


}

object MbClusterActor {
    val PROXY_PATH = "/user/MbClusterActor"

}
