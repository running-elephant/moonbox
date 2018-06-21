package moonbox.grid.deploy.worker

import akka.actor.{Actor, ActorRef, PoisonPill}
import moonbox.common.{MbConf, MbLogging}
import moonbox.core.{ColumnPrivilegeException, MbSession}
import moonbox.core.command._
import moonbox.core.config.CACHE_IMPLEMENTATION
import moonbox.grid._
import moonbox.grid.deploy.DeployMessages._
import moonbox.grid.timer.EventEntity
import org.apache.spark.sql.SaveMode

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

class Runner(conf: MbConf, mbSession: MbSession) extends Actor with MbLogging {
	private implicit val catalogSession = mbSession.catalogSession
	private var currentJob: JobInfo = _

	override def receive: Receive = {
		case RunJob(jobInfo) =>
			logInfo(s"Runner::RunJob  $jobInfo")
			currentJob = jobInfo
			val target = sender()
			run(jobInfo, target).onComplete {
				case Success(data) =>
					successCallback(jobInfo.jobId, data, target, jobInfo.sessionId.isEmpty)
				case Failure(e) =>
					failureCallback(jobInfo.jobId, e, target, jobInfo.sessionId.isEmpty)
			}
		case CancelJob(jobId) =>
			mbSession.cancelJob(jobId)
		case KillRunner =>
			logInfo(s"Runner::KillRunner $currentJob")
			if(currentJob == null || currentJob.sessionId.isDefined) {  //if a runner have not a job OR it is an adhoc, release resources
				clean()
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
		val row = event.run(mbSession)
		val catalogApplication = mbSession.catalog.getApplication(catalogSession.organizationId, event.app)
		if (event.enable) {
			val eventEntity = EventEntity(
				group = catalogSession.organizationName,
				name = event.name,
				sqls = catalogApplication.cmds,
				cronExpr = event.schedule,
				definer = event.definer.getOrElse(catalogSession.userName),
				start = None,
				end = None,
				desc = event.description
			)
			target ! RegisterTimedEvent(eventEntity)
		}
		DirectData(row.map(_.toSeq.map(_.toString)))
	}

	def alterTimedEvent(event: AlterTimedEventSetEnable, target: ActorRef): JobResult = {
		val row = event.run(mbSession)
		if (event.enable) {
			val existsEvent = mbSession.catalog.getTimedEvent(catalogSession.organizationId, event.name)
			val catalogUser = mbSession.catalog.getUser(existsEvent.definer)
			val catalogApplication = mbSession.catalog.getApplication(existsEvent.application)
			val eventEntity = EventEntity(
				group = catalogSession.organizationName,
				name = event.name,
				sqls = catalogApplication.cmds,
				cronExpr = existsEvent.schedule,
				definer = catalogUser.name,
				start = None,
				end = None,
				desc = existsEvent.description
			)
			target ! RegisterTimedEvent(eventEntity)
		} else {
			target ! UnregisterTimedEvent(catalogSession.organizationName, event.name)
		}
		DirectData(row.map(_.toSeq.map(_.toString)))
	}

	def createTempView(tempView: CreateTempView): JobResult = {
		val optimized = mbSession.optimizedPlan(tempView.query)
		val (plan, _) = mbSession.pushdownPlan(optimized, pushdown = false)
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
		// TODO jobId
		val format = conf.get(CACHE_IMPLEMENTATION.key, CACHE_IMPLEMENTATION.defaultValueString)
		val options = conf.getAll.filterKeys(_.startsWith("moonbox.cache")).+("jobId" -> jobId)
		val optimized = mbSession.optimizedPlan(query.query)
		try {
			val (plan, wholePushdown) = mbSession.pushdownPlan(optimized)
			if (wholePushdown.isDefined) {
				mbSession.toDT(plan, wholePushdown.get).write().format(format).options(options).save()
			} else {
				mbSession.toDF(plan).write.format(format).options(options).save()
			}
		} catch {
			case e: ColumnPrivilegeException =>
				throw e
			case _: Throwable =>
				val (plan, _) = mbSession.pushdownPlan(optimized, pushdown = false)
				mbSession.toDF(plan).write.format(format).options(options).save()
		}
		CachedData
	}

	def insertInto(insert: InsertInto): JobResult = {
		val options = mbSession.getCatalogTable(insert.table.table, insert.table.database).properties
		val format = options("type")
		val saveMode = if (insert.overwrite) SaveMode.Overwrite else SaveMode.Append
		val optimized = mbSession.optimizedPlan(insert.query)
		try {
			val (plan, wholePushdown) = mbSession.pushdownPlan(optimized)
			if (wholePushdown.isDefined) {
				mbSession.toDT(plan, wholePushdown.get).write().format(format).options(options).mode(saveMode).save()
			} else {
				mbSession.toDF(plan).write.format(format).options(options).mode(saveMode).save()
			}
		} catch {
			case e: ColumnPrivilegeException =>
				throw e
			case _: Throwable =>
				val (plan, wholePushdown) = mbSession.pushdownPlan(optimized, pushdown = false)
				if (wholePushdown.isDefined) {
					mbSession.toDT(plan, wholePushdown.get).write().format(format).options(options).mode(saveMode).save()
				} else {
					mbSession.toDF(plan).write.format(format).options(options).mode(saveMode).save()
				}
		}
		UnitData
	}


	private def clean(): Unit = {
		Future {
			logInfo(s"Runner::clean $currentJob start")
			mbSession.cancelJob(currentJob.jobId)
			// session.mixcal.sparkSession.sessionState.catalog.reset()
			mbSession.catalog.stop()
			logInfo(s"Runner::clean $currentJob end")
		}
	}

	private def successCallback(jobId: String, result: JobResult, requester: ActorRef, shutdown: Boolean): Unit = {
		requester ! JobStateChanged(jobId, JobState.SUCCESS, result)
		if (shutdown) {
			clean()
			self ! PoisonPill
		}
	}

	private def failureCallback(jobId: String, e: Throwable, requester: ActorRef, shutdown: Boolean): Unit = {
		logError(e.getStackTrace.map(_.toString).mkString("\n"))
		requester ! JobStateChanged(jobId, JobState.FAILED, Failed(e.getMessage))
		if (shutdown) {
			clean()
			self ! PoisonPill
		}
	}
}
