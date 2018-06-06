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
	private var currentJob: JobInfo = _

	override def receive: Receive = {
		case RunJob(jobInfo) =>
			logInfo(s"Runner::RunJob  $jobInfo")
			currentJob = jobInfo
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
			logInfo(s"Runner::KillRunner $currentJob")
			if(currentJob == null || currentJob.sessionId.isDefined) {  //if a runner have not a job OR it is an adhoc, release resources
				clean()
				self ! PoisonPill
			}
	}

	def run(jobInfo: JobInfo): Future[Any] = {
		Future(session.execute(jobInfo.jobId, jobInfo.cmds))
	}

	private def clean(): Unit = {
		Future {
			logInfo(s"Runner::clean $currentJob start")
			session.cancelJob(currentJob.jobId)
			// session.mixcal.sparkSession.sessionState.catalog.reset()
			session.catalog.stop()
			logInfo(s"Runner::clean $currentJob end")
		}
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
