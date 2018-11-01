package moonbox.grid.runtime.local

import java.text.SimpleDateFormat
import java.util.{Date, Locale, UUID}

import akka.actor.{Actor, ActorRef, Props}
import moonbox.common.util.Utils
import moonbox.common.{MbConf, MbLogging}
import moonbox.core.resource.ResourceMonitor
import moonbox.core.{CatalogContext, MbSession}
import moonbox.grid.api._
import moonbox.grid.config._
import moonbox.grid.deploy2.node.DeployMessages.{AssignCommandToWorker, RunCommand}
import moonbox.protocol.app._
import moonbox.protocol.client.{DatabaseInfo, TableInfo}

import scala.collection.mutable
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

class MbLocalActor(conf: MbConf, catalogContext: CatalogContext) extends Actor with MbLogging {
    private val workerId = generateWorkerId()
    private val WORKER_TIMEOUT_MS = conf.get(WORKER_TIMEOUT.key, WORKER_TIMEOUT.defaultValue.get)
    private val STATEREPORT_INTERVAL = conf.get(WORKER_STATEREPORT_INTERVAL.key, WORKER_STATEREPORT_INTERVAL.defaultValue.get)
    private val sessionIdToJobRunner = new mutable.HashMap[String, ActorRef]  //adhoc
    private val jobIdToJobRunner = new mutable.HashMap[String, ActorRef]      //batch id - runner
    private val runnerToJobId = new mutable.HashMap[ActorRef, String]         //batch runner - id
    private var resourceMonitor: ResourceMonitor = _

    override def preStart(): Unit = {
        // TODO init sparkContext
        try {
            MbSession.startMixcalEnv(conf, false)
        } catch {
            case e: Exception =>
                logError(e.getMessage)
                System.exit(1)
        }
        resourceMonitor = new ResourceMonitor

        logInfo(s"MbWorker start successfully.")

    }

    override def receive: Receive = {
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
        case assign@AssignCommandToWorker(commandInfo) =>
            logInfo(s"MbWorker::AssignJobToWorker $commandInfo")
            val requester = sender()
            commandInfo.sessionId match {
                case Some(sessionId) => // adhoc
                    sessionIdToJobRunner.get(sessionId) match {
                        case Some(runner) =>
                            runner forward RunCommand(commandInfo)
                        case None =>
                            requester ! JobStateChanged(commandInfo.jobId, commandInfo.seq, JobState.FAILED, Failed("Session lost."))
                    }
                case None => // batch
                    val mb = MbSession.getMbSession(conf).bindUser(commandInfo.username.get)
                    val runner = context.actorOf(Props(classOf[Runner], conf, mb))
                    jobIdToJobRunner.put(commandInfo.jobId, runner)  //for cancel
                    runnerToJobId.put(runner, commandInfo.jobId)
                    context.watch(runner) // terminate
                    runner forward RunCommand(commandInfo)
            }

        case m@FetchDataFromRunner(sessionId, jobId, fetchSize) =>
            val client = sender()
            sessionIdToJobRunner.get(sessionId) match {
                case Some(actor) => actor forward m
                case None => client ! FetchDataFromRunnerFailed(jobId, s"sessionId $sessionId does not exist or has been removed.")
            }

        case ShowDatabasesInfo(username) =>
            val client = sender()
            //Future {
                catalogContext.getUserOption(username) match {
                    case Some(catalogUser) =>
                        val databases = catalogContext.listDatabase(catalogUser.organizationId)
                        val data = databases.map { d => DatabaseInfo(d.name, d.isLogical, d.properties.filter(!_._1.contains("password")), d.description.getOrElse("")) }
                        client ! ShowedDatabasesInfo(data)
                    case None =>
                        client ! ShowedDatabasesInfoFailed("Not Find User in db")
                }
            //}
        case ShowTablesInfo(database, username) =>
            val client = sender()
           // Future {
                catalogContext.getUserOption(username) match {
                    case Some(catalogUser) =>
                        val databaseId = catalogContext.getDatabase(catalogUser.organizationId, database).id.get
                        val tables = catalogContext.listTables(databaseId).map { d => d.name }
                        client ! ShowedTablesInfo(tables)
                    case None =>
                        client ! ShowedTablesInfoFailed("Not Find User in db")
                }
           // }
        case DescribeTableInfo(tablename, database, username) =>
            val client = sender()
            //Future {
                catalogContext.getUserOption(username) match {
                    case Some(catalogUser) =>
                        val databaseId = catalogContext.getDatabase(catalogUser.organizationId, database).id.get
                        val table = catalogContext.getTable(databaseId, tablename)

                        val mb = MbSession.getMbSession(conf).bindUser(username, Some(database), false)
                        if (!mb.mixcal.sparkSession.sessionState.catalog.databaseExists(database)) {
                            mb.mixcal.sqlToDF(s"create database if not exists $database")
                        }
                        mb.mixcal.sparkSession.catalog.setCurrentDatabase(database)

                        val optimized = mb.optimizedPlan(s"select * from $tablename")
                        //val optimized = mb.optimizedPlan(s"select * from $database.$tablename")
                        val columns = optimized.schema.fields.map{ field => (field.name, field.dataType.simpleString)}.toSeq

                        val tableInfo = TableInfo(
                            table.name,
                            table.properties.filter(!_._1.contains("password")),
                            columns,
                            table.description.getOrElse("")
                        )
                        client ! DescribedTableInfo(tableInfo)
                    case None =>
                        client ! DescribedTableInfoFailed("Not Find User in db")
                }
            //}
        case a => logInfo(s"recv unknown message: $a")

    }

    private def createDateFormat = new SimpleDateFormat("yyyyMMddHHmmss", Locale.US)

    private var nextAppNumber = 0

    private def generateWorkerId(): String = {
        val submitDate = new Date(Utils.now)
        val workerId = "worker-%s-%d".format(createDateFormat.format(submitDate), nextAppNumber)
        nextAppNumber += 1
        workerId
    }

    private def newSessionId(): String = {
        UUID.randomUUID().toString
    }
}


