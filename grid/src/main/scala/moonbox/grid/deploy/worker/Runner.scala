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

package moonbox.grid.deploy.worker

import java.util.concurrent.Executors

import scala.concurrent.ExecutionContext
import akka.actor.{Actor, ActorRef, PoisonPill}
import akka.pattern._
import akka.util.Timeout
import moonbox.common.{MbConf, MbLogging}
import moonbox.core.{ColumnSelectPrivilegeException, MbSession, TableInsertPrivilegeChecker, TableInsertPrivilegeException}
import moonbox.core.command._
import moonbox.core.config.CACHE_IMPLEMENTATION
import moonbox.grid.JobState.JobState
import moonbox.grid._
import moonbox.grid.deploy.DeployMessages._
import moonbox.grid.timer.{EventCall, EventEntity}
import org.apache.spark.sql.SaveMode
import moonbox.core.datasys.{DataSystem, Insertable}
import org.apache.spark.sql.optimizer.WholePushdown

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import scala.util.{Failure, Success}

class Runner(conf: MbConf, mbSession: MbSession) extends Actor with MbLogging {
  implicit val askTimeout = Timeout(new FiniteDuration(20, SECONDS))
  private val awaitTimeout = new FiniteDuration(20, SECONDS)
  private implicit val catalogSession = mbSession.catalogSession
  private var currentJob: JobInfo = _
  private implicit val contextExecutor = {
    val executor = Executors.newFixedThreadPool(10) //poolsize is temporarily set 10
    ExecutionContext.fromExecutor(executor)
  }

  override def receive: Receive = {
    case RunJob(jobInfo) =>
      logInfo(s"Runner::RunJob  $jobInfo")
      currentJob = jobInfo
      val target = sender()
      run(jobInfo, target).onComplete {
        case Success(data) =>
          successCallback(jobInfo.jobId, data, target, jobInfo.sessionId.isEmpty)
        case Failure(e) =>
          if (e.getMessage.contains("cancelled job")) {
            cancelCallback(jobInfo.jobId, e, target, false) //TaskKilledException can not catch
          } else {
            failureCallback(jobInfo.jobId, e, target, jobInfo.sessionId.isEmpty)
          }
      }
    case CancelJob(jobId) =>
      logInfo(s"Runner::CancelJob [WARNING] !!! $jobId")
      mbSession.cancelJob(jobId)
    case KillRunner =>
      logInfo(s"Runner::KillRunner $currentJob")
      if (currentJob == null || currentJob.sessionId.isDefined) {
        //if a runner have not a job OR it is an adhoc, release resources
        clean(JobState.KILLED)
        self ! PoisonPill
      }
  }

  def run(jobInfo: JobInfo, target: ActorRef): Future[JobResult] = {
    Future {
      jobInfo.cmds.map { cmd =>
        mbSession.withPrivilege(cmd) {
          cmd match {
            case event: CreateTimedEvent =>
              createTimedEvent(event, target)
            case event: AlterTimedEventSetEnable =>
              alterTimedEvent(event, target)
            case runnable: MbRunnableCommand =>
              val row = runnable.run(mbSession)
              DirectData(row.map(_.toSeq.map(_.toString)))
            case tempView: CreateTempView =>
              createTempView(tempView)
            case query: MQLQuery =>
              mqlQuery(query, jobInfo.jobId)
            case insert: InsertInto =>
              insertInto(insert)
            case _ => throw new Exception("Unsupported command.")
          }
        }
      }.last
    }
  }

  def createTimedEvent(event: CreateTimedEvent, target: ActorRef): JobResult = {
    val result = if (event.enable) {
      val catalogProcedure = mbSession.catalog.getProcedure(catalogSession.organizationId, event.proc)
      val definer = event.definer.getOrElse(catalogSession.userName)
      val sqls = catalogProcedure.cmds
      val eventEntity = EventEntity(
        group = catalogSession.organizationName,
        name = event.name,
        sqls = sqls,
        cronExpr = event.schedule,
        definer = definer,
        start = None,
        end = None,
        desc = event.description,
        function = new EventCall(definer, sqls)
      )
      val response = target.ask(RegisterTimedEvent(eventEntity)).mapTo[RegisterTimedEventResponse].flatMap {
        case RegisteredTimedEvent =>
          Future(event.run(mbSession).map(_.toSeq.map(_.toString)))
        case RegisterTimedEventFailed(message) =>
          throw new Exception(message)
      }
      Await.result(response, awaitTimeout)
    } else {
      event.run(mbSession).map(_.toSeq.map(_.toString))
    }
    DirectData(result)
  }

  def alterTimedEvent(event: AlterTimedEventSetEnable, target: ActorRef): JobResult = {
    val result = if (event.enable) {
      val existsEvent = mbSession.catalog.getTimedEvent(catalogSession.organizationId, event.name)
      val catalogUser = mbSession.catalog.getUser(existsEvent.definer)
      val catalogProcedure = mbSession.catalog.getProcedure(existsEvent.procedure)
      val eventEntity = EventEntity(
        group = catalogSession.organizationName,
        name = event.name,
        sqls = catalogProcedure.cmds,
        cronExpr = existsEvent.schedule,
        definer = catalogUser.name,
        start = None,
        end = None,
        desc = existsEvent.description,
        function = new EventCall(catalogUser.name, catalogProcedure.cmds)
      )
      target.ask(RegisterTimedEvent(eventEntity)).mapTo[RegisterTimedEventResponse].flatMap {
        case RegisteredTimedEvent =>
          Future(event.run(mbSession).map(_.toSeq.map(_.toString)))
        case RegisterTimedEventFailed(message) =>
          throw new Exception(message)
      }
    } else {
      target.ask(UnregisterTimedEvent(catalogSession.organizationName, event.name))
        .mapTo[UnregisterTimedEventResponse].flatMap {
        case UnregisteredTimedEvent =>
          Future(event.run(mbSession).map(_.toSeq.map(_.toString)))
        case UnregisterTimedEventFailed(message) =>
          throw new Exception(message)
      }
    }
    DirectData(Await.result(result, awaitTimeout))
  }

  def createTempView(tempView: CreateTempView): JobResult = {
    val optimized = mbSession.optimizedPlan(tempView.query)
    val plan = mbSession.pushdownPlan(optimized, pushdown = false)
    val df = mbSession.toDF(plan)
    if (tempView.isCache) {
      df.cache()
    }
    if (tempView.replaceIfExists) {
      df.createOrReplaceTempView(tempView.name)
    } else {
      df.createTempView(tempView.name)
    }
    UnitData
  }

  def mqlQuery(query: MQLQuery, jobId: String): JobResult = {
    val format = conf.get(CACHE_IMPLEMENTATION.key, CACHE_IMPLEMENTATION.defaultValueString)
    val options = conf.getAll.filterKeys(_.startsWith("moonbox.cache")).+("jobId" -> jobId)
    val optimized = mbSession.optimizedPlan(query.query)
    try {
      mbSession.mixcal.setJobGroup(jobId)
      //cancel
      val plan = mbSession.pushdownPlan(optimized)
      plan match {
        case WholePushdown(child, queryable) =>
          logInfo(s"WholePushdown $query")
          mbSession.toDT(child, queryable).write().format(format).options(options).save()
        case _ =>
          mbSession.toDF(plan).write.format(format).options(options).save()
      }
    } catch {
      case e: ColumnSelectPrivilegeException =>
        throw e
      case e: Throwable =>
        if (e.getMessage.contains("cancelled job")) {
          throw e
        } else {
          e.printStackTrace()
          logWarning(s"Execute push failed with ${e.getMessage}. Retry without pushdown.")
          val plan = mbSession.pushdownPlan(optimized, pushdown = false)
          plan match {
            case WholePushdown(child, queryable) =>
              mbSession.toDF(child).write.format(format).options(options).save()
            case _ =>
              mbSession.toDF(plan).write.format(format).options(options).save()
          }
        }
    }
    CachedData
  }

  def insertInto(insert: InsertInto): JobResult = {
    // TODO write privilege
    val sinkCatalogTable = mbSession.getCatalogTable(insert.table.table, insert.table.database)
    val options = sinkCatalogTable.properties
    // val sinkDataSystem = DataSystem.lookupDataSystem(options)
    val format = DataSystem.lookupDataSource(options("type"))
    val saveMode = if (insert.overwrite) SaveMode.Overwrite else SaveMode.Append
    val optimized = mbSession.optimizedPlan(insert.query)
    try {
      val plan = mbSession.pushdownPlan(optimized)
      val dataFrame = plan match {
        case WholePushdown(child, queryable) =>
          queryable.buildScan(child, mbSession.mixcal.sparkSession)
        case _ =>
          mbSession.toDF(plan)
      }
      if (options.contains("partitionColumnNames"))
        TableInsertPrivilegeChecker.intercept(mbSession, sinkCatalogTable, dataFrame).write.format(format).options(options).partitionBy(options("partitionColumnNames").split(","): _*).mode(saveMode).save()
      else
        TableInsertPrivilegeChecker.intercept(mbSession, sinkCatalogTable, dataFrame).write.format(format).options(options).mode(saveMode).save()

    } catch {
      case e: ColumnSelectPrivilegeException =>
        throw e
      case e: TableInsertPrivilegeException =>
        throw e
      case e: Throwable =>
        logWarning(e.getMessage)
        val plan = mbSession.pushdownPlan(optimized, pushdown = false)
        plan match {
          case WholePushdown(child, queryable) =>
            mbSession.toDF(child).write.format(format).options(options).mode(saveMode).save()
          case _ =>
            mbSession.toDF(plan).write.format(format).options(options).mode(saveMode).save()
        }
    }
    UnitData
  }

  private def clean(jobState: JobState): Unit = {
    Future {
      logInfo(s"Runner::clean ${currentJob.copy(status = jobState)} start")
      mbSession.cancelJob(currentJob.jobId)
      // session.mixcal.sparkSession.sessionState.catalog.reset()
      mbSession.catalog.stop()
      logInfo(s"Runner::clean ${currentJob.copy(status = jobState)} end")
    }
  }

  private def successCallback(jobId: String, result: JobResult, requester: ActorRef, shutdown: Boolean): Unit = {
    requester ! JobStateChanged(jobId, JobState.SUCCESS, result)
    if (shutdown) {
      clean(JobState.SUCCESS)
      self ! PoisonPill
    }
  }

  private def failureCallback(jobId: String, e: Throwable, requester: ActorRef, shutdown: Boolean): Unit = {
    logError(e.getStackTrace.map(_.toString).mkString("\n"))
    requester ! JobStateChanged(jobId, JobState.FAILED, Failed(e.getMessage))
    if (shutdown) {
      clean(JobState.FAILED)
      self ! PoisonPill
    }
  }

  private def cancelCallback(jobId: String, e: Throwable, requester: ActorRef, shutdown: Boolean): Unit = {
    logWarning(e.getStackTrace.map(_.toString).mkString("\n"))
    requester ! JobStateChanged(jobId, JobState.KILLED, Failed(e.getMessage))
    if (shutdown) {
      clean(JobState.KILLED)
      self ! PoisonPill
    }
  }

}
