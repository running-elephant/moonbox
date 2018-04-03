package moonbox.grid.deploy.worker

import akka.actor.{Actor, ActorRef, PoisonPill}
import moonbox.common.{MbConf, MbLogging}
import moonbox.core.cache.RedisCache
import moonbox.core.command.{InsertInto, MQLQuery, MbCommand}
import moonbox.core.config.CACHE_SERVERS
import moonbox.core.{MbSession, MbTableIdentifier}
import moonbox.grid._
import moonbox.grid.deploy.DeployMessages._
import org.apache.spark.sql.{DataFrame, Row, SparkSession}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

class Runner(conf: MbConf, session: MbSession) extends Actor with MbLogging {
	override def receive: Receive = {
		case RunJob(jobInfo) =>
			val target = sender()
			run(jobInfo).onComplete {
				case Success(data) =>
					successCallback(jobInfo.jobId, data, target, jobInfo.sessionId.isEmpty)
				case Failure(e) =>
					failureCallback(jobInfo.jobId, e, target, jobInfo.sessionId.isEmpty)
			}
		case CancelJob(jobId) =>
			session.cancelJob(jobId)
		case KillRunner =>
	}

	def run(jobInfo: JobInfo): Future[Any] = {
		Future(session.execute(jobInfo.jobId, jobInfo.cmds))
	}

	private def successCallback(jobId: String, data: Any, requester: ActorRef, shutdown: Boolean): Unit = {
		val result = data match {
			case u: Unit =>
				UnitData
			case seq :: Nil =>
				DirectData(seq.asInstanceOf[Seq[Row]].map(_.toSeq.map(_.toString)))
			case str: String =>
				CachedData
		}
		requester ! JobStateChanged(jobId, JobState.SUCCESS, result)
		if (shutdown) self ! PoisonPill
	}

	private def failureCallback(jobId: String, e: Throwable, requester: ActorRef, shutdown: Boolean): Unit = {
		requester ! JobStateChanged(jobId, JobState.FAILED, Failed(jobId))
		if (shutdown) self ! PoisonPill
	}
}
