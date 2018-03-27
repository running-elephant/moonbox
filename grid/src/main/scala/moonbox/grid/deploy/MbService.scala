package moonbox.grid.deploy

import akka.actor.ActorRef
import akka.pattern.ask
import akka.util.Timeout
import moonbox.common.MbLogging
import moonbox.grid.api._
import moonbox.grid.deploy.authenticate.LoginManager
import moonbox.grid.deploy.rest._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration._

class MbService(loginManager: LoginManager, master: ActorRef, resultGetter: ActorRef) extends MbLogging {

	implicit val timeout = Timeout(new FiniteDuration(3600 * 24, SECONDS))

	def login(username: String, password: String): Future[Boolean] = {
		Future(loginManager.login(username, password))
	}

	def isLogin(username: String): Future[Boolean] = {
		Future(loginManager.isLogin(username))
	}

	def logout(username: String): Future[LogoutOutbound] = {
		Future(loginManager.logout(username)).map { res =>
			LogoutOutbound(message = Some("Logout successfully."))
		}
	}

	def jobQuery(username: String, sqls: Seq[String], fetchSize: Int = 200): Future[QueryOutbound] = {
		isLogin(username).flatMap {
			case true =>
				askForCompute(MbRequest(username, JobQuery(sqls))).flatMap {
					case MbResponse(_, JobFailed(jobId, error)) =>
						Future(QueryOutbound(error = Some(error)))
					case MbResponse(_, JobCompleteWithCachedData(jobId)) =>
						askForResult(MbRequest(username, FetchData(jobId, offset = 0, fetchSize))).map {
							case MbResponse(_, FetchDataSuccess(id, schema, data)) =>
								QueryOutbound(schema = Some(schema), data = Some(data))
							case MbResponse(_, FetchDataFailed(id, error)) =>
								QueryOutbound(error = Some(error))
						}
					case MbResponse(_, JobCompleteWithExternalData(jobId, message)) =>
						Future(QueryOutbound())
					case MbResponse(_, JobCompleteWithDirectData(jobId, data)) =>
						Future(QueryOutbound(data = Some(data)))
				}
			case false =>
				Future(QueryOutbound(error = Some("Please login first.")))
		}
	}

	def jobSubmitSync(username: String, sqls: Seq[String]): Future[SubmitOutbound] = {
		isLogin(username).flatMap {
			case true =>
				askForCompute(MbRequest(username, JobSubmit(sqls, async = false))).flatMap {
					case MbResponse(_, JobFailed(jobId, error)) =>
						Future(SubmitOutbound(jobId = Some(jobId), error = Some(error)))
					case MbResponse(_, JobCompleteWithCachedData(jobId)) =>
						askForResult(MbRequest(username, FetchData(jobId, offset = 0, size = 200))).map {
							case MbResponse(_, FetchDataSuccess(id, schema, data)) =>
								SubmitOutbound(jobId = Some(id), schema = Some(schema), data = Some(data))
							case MbResponse(_, FetchDataFailed(id, error)) =>
								SubmitOutbound(jobId = Some(id), error = Some(error))
						}
					case MbResponse(_, JobCompleteWithExternalData(jobId, message)) =>
						Future(SubmitOutbound(jobId = Some(jobId), message = message))
					case MbResponse(_, JobCompleteWithDirectData(jobId, data)) =>
						Future(SubmitOutbound(jobId = Some(jobId), data = Some(data)))
				}
			case false =>
				Future(SubmitOutbound(error = Some("Please login first.")))
		}
	}

	def jobSubmitAsync(username: String, sqls: Seq[String]): Future[SubmitOutbound] = {
		isLogin(username).flatMap {
			case true =>
				askForCompute(MbRequest(username, JobSubmit(sqls, async = true))).map {
					case MbResponse(_, JobAccepted(jobId)) =>
						SubmitOutbound(jobId = Some(jobId))
					case MbResponse(_, JobRejected(error)) =>
						SubmitOutbound(error = Some(error))
				}
			case false =>
				Future(SubmitOutbound(error = Some("Please login first.")))
		}
	}

	def jobCancel(username: String, jobId: String): Future[CancelOutbound] = {
		isLogin(username).flatMap {
			case true =>
				askForCompute(MbRequest(username, JobCancel(jobId))).map {
					case MbResponse(_, JobCancelSuccess(id)) =>
						CancelOutbound(jobId = jobId)
					case MbResponse(_, JobCancelFailed(id, error)) =>
						CancelOutbound(jobId = jobId, error = Some(error))
				}
			case false =>
				Future(CancelOutbound(jobId = jobId, error = Some("Please login first.")))
		}
	}

	def jobProgress(username: String, jobId: String): Future[ProgressOutbound] = {
		isLogin(username).flatMap {
			case true =>
				askForCompute(MbRequest(username, JobProgress(jobId))).map {
					case MbResponse(_, JobProgressResponse(id, jobInfo)) =>
						ProgressOutbound(id, error = jobInfo.errorMessage, status = Some(s"${jobInfo.status}"))
				}
			case false =>
				Future(ProgressOutbound(jobId = jobId, error = Some("Please login first.")))
		}
	}

	def jobResult(username: String, jobId: String, offset: Long, size: Long): Future[ResultOutbound] = {
		isLogin(username).flatMap {
			case true =>
				askForResult(MbRequest(username, FetchData(jobId, offset, size))).map {
					case MbResponse(_, FetchDataSuccess(id, schema, data)) =>
						ResultOutbound(jobId = id, schema = Some(schema), data = Some(data))
					case MbResponse(_, FetchDataFailed(id, error)) =>
						ResultOutbound(jobId = id, error = Some(error))
				}
			case false =>
				Future(ResultOutbound(jobId = jobId, error = Some("Please login first.")))
		}
	}


	private def askForCompute(message: MbRequest): Future[MbResponse] = askFor(master)(message)

	private def askForResult(message: MbRequest): Future[MbResponse] = askFor(resultGetter)(message)

	private def askFor(actorRef: ActorRef)(message: MbRequest): Future[MbResponse] = {
		(actorRef ask message).mapTo[MbResponse]
	}


}
