package moonbox.grid.deploy

import akka.actor.ActorRef
import akka.pattern.ask
import akka.util.Timeout
import moonbox.common.MbLogging
import moonbox.common.message._
import moonbox.grid.api.{ClosedSession, _}
import moonbox.grid.deploy.authenticate.LoginManager

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

	def openSession(username: String, database: Option[String]): Future[OpenSessionOutbound] = {
		isLogin(username).flatMap {
			case true =>
				askForCompute(OpenSession(username, database)).flatMap {
					case OpenedSession(sessionId) =>
						Future(OpenSessionOutbound(Some(sessionId), None))
					case OpenSessionFailed(error) =>
						Future(OpenSessionOutbound(None, Some(error)))
				}
			case false =>
				Future(OpenSessionOutbound(None, Some("Please login first.")))
		}
	}

	def closeSession(username: String, sessionId: String): Future[CloseSessionOutbound] = {
		isLogin(username).flatMap {
			case true =>
				askForCompute(CloseSession(sessionId)).flatMap {
					case ClosedSession => Future(CloseSessionOutbound(None))
					case CloseSessionFailed(error) => Future(CloseSessionOutbound(Some(error)))
				}
			case false =>
				Future(CloseSessionOutbound(Some("Please login first.")))
		}
	}

	def jobQuery(sessionId: String, sqls: Seq[String], fetchSize: Int = 200): Future[QueryOutbound] = {
		askForCompute(JobQuery(sessionId, sqls)).flatMap {
			case JobFailed(jobId, error) =>
				Future(QueryOutbound(jobId, error = Some(error)))
			case JobCompleteWithCachedData(jobId) =>
				askForResult(FetchData(jobId, offset = 0, fetchSize)).map {
					case FetchDataSuccess(id, schema, data, size) =>
						QueryOutbound(id, schema = Some(schema), data = Some(data), size = Some(size))  //total size
					case FetchDataFailed(id, error) =>
						QueryOutbound(id, error = Some(error))
				}
			case JobCompleteWithExternalData(jobId, message) =>
				Future(QueryOutbound(jobId))
			case JobCompleteWithDirectData(jobId, data) =>
				Future(QueryOutbound(jobId, data = Some(data)))
		}
	}

	def jobSubmitSync(username: String, sqls: Seq[String]): Future[SubmitOutbound] = {
		isLogin(username).flatMap {
			case true =>
				askForCompute(JobSubmit(username, sqls, async = false)).flatMap {
					case JobFailed(jobId, error) =>
						Future(SubmitOutbound(jobId = Some(jobId), error = Some(error)))
					case JobCompleteWithCachedData(jobId) =>
						askForResult(FetchData(jobId, offset = 0, size = 200)).map {
							case FetchDataSuccess(id, schema, data, size) =>  //TODO: 200 ???, size maybe be used if the data is part of data
								SubmitOutbound(jobId = Some(id), schema = Some(schema), data = Some(data))
							case FetchDataFailed(id, error) =>
								SubmitOutbound(jobId = Some(id), error = Some(error))
						}
					case JobCompleteWithExternalData(jobId, message) =>
						Future(SubmitOutbound(jobId = Some(jobId), message = message))
					case JobCompleteWithDirectData(jobId, data) =>
						Future(SubmitOutbound(jobId = Some(jobId), data = Some(data)))
				}
			case false =>
				Future(SubmitOutbound(error = Some("Please login first.")))
		}
	}

	def jobSubmitAsync(username: String, sqls: Seq[String]): Future[SubmitOutbound] = {
		isLogin(username).flatMap {
			case true =>
				askForCompute(JobSubmit(username, sqls, async = true)).map {
					case JobAccepted(jobId) =>
						SubmitOutbound(jobId = Some(jobId))
					case JobRejected(error) =>
						SubmitOutbound(error = Some(error))
				}
			case false =>
				Future(SubmitOutbound(error = Some("Please login first.")))
		}
	}

	def jobCancel(username: String, jobId: String): Future[CancelOutbound] = {
		isLogin(username).flatMap {
			case true =>
				askForCompute(JobCancel(jobId)).map {
					case JobCancelSuccess(id) =>
						CancelOutbound(jobId = jobId)
					case JobCancelFailed(id, error) =>
						CancelOutbound(jobId = jobId, error = Some(error))
				}
			case false =>
				Future(CancelOutbound(jobId = jobId, error = Some("Please login first.")))
		}
	}

	def jobProgress(username: String, jobId: String): Future[ProgressOutbound] = {
		isLogin(username).flatMap {
			case true =>
				askForCompute(JobProgress(jobId)).map {
					case JobProgressResponse(id, jobInfo) =>
						ProgressOutbound(id, error = jobInfo.errorMessage, status = Some(s"${jobInfo.status}"))
				}
			case false =>
				Future(ProgressOutbound(jobId = jobId, error = Some("Please login first.")))
		}
	}

	def jobResult(username: String, jobId: String, offset: Long, size: Long): Future[ResultOutbound] = {
		isLogin(username).flatMap {
			case true =>
				askForResult(FetchData(jobId, offset, size)).map {
					case FetchDataSuccess(id, schema, data, size) =>  //TODO: size maybe be used if the data is part of data
						ResultOutbound(jobId = id, schema = Some(schema), data = Some(data))
					case FetchDataFailed(id, error) =>
						ResultOutbound(jobId = id, error = Some(error))
				}
			case false =>
				Future(ResultOutbound(jobId = jobId, error = Some("Please login first.")))
		}
	}

	private def askForCompute(message: MbApi): Future[MbApi] = askFor(master)(message)

	private def askForResult(message: MbApi): Future[MbApi] = askFor(resultGetter)(message)

	private def askFor(actorRef: ActorRef)(message: MbApi): Future[MbApi] = {
		(actorRef ask message).mapTo[MbApi]
	}


}
