package moonbox.grid.deploy.worker

import akka.actor.{Actor, ActorRef, PoisonPill}
import moonbox.common.{MbConf, MbLogging}
import moonbox.core.MbSession
import moonbox.grid._
import moonbox.grid.deploy.DeployMessages._
import org.apache.spark.sql.Row

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
			case seq: Seq[_] =>
				DirectData(seq.asInstanceOf[Seq[Row]].map(_.toSeq.map(_.toString)))
			case str: String =>
				CachedData
		}
		requester ! JobStateChanged(jobId, JobState.SUCCESS, result)
		if (shutdown) self ! PoisonPill
	}

	private def failureCallback(jobId: String, e: Throwable, requester: ActorRef, shutdown: Boolean): Unit = {
		logError(e.getStackTrace.map(_.toString).mkString("\n"))
		requester ! JobStateChanged(jobId, JobState.FAILED, Failed(e.getMessage))
		if (shutdown) self ! PoisonPill
	}
}
