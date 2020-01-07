package moonbox.grid.deploy.rest.service

import java.util.concurrent.TimeUnit

import akka.actor.ActorRef
import akka.pattern.ask
import akka.util.Timeout
import moonbox.catalog.AbstractCatalog.User
import moonbox.common.MbLogging
import moonbox.grid.deploy.DeployMessages.{ClusterStateResponse, RequestClusterState, RequestSubmitDriver, SubmitDriverResponse}
import moonbox.grid.deploy.app.AppMasterManager
import moonbox.grid.deploy.rest.entities.{BatchSubmit, Node}
import moonbox.grid.deploy.security.Session

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class GridService(actorRef: ActorRef) extends MbLogging {
	private implicit val timeout = new Timeout(30, TimeUnit.SECONDS)

	def clusterState(): Future[Seq[Node]] = {
		actorRef.ask(RequestClusterState).mapTo[ClusterStateResponse].map(_.nodes)
	}

	def submit(submit: BatchSubmit)(implicit user: User): Future[Either[String, Throwable]] = {

		AppMasterManager.getAppMaster(submit.typeName) match {
			case Some(appMaster) =>
				val conf = submit.config ++ Seq("org" -> user.org, "username" -> user.user, "script" -> submit.script.mkString(";"))
				val driverDesc = appMaster.createDriverDesc(conf)
				actorRef.ask(RequestSubmitDriver(submit.name, driverDesc)).mapTo[SubmitDriverResponse].map { response =>
					if (response.success) {
						Left(response.driverId.get)
					} else {
						Right(new RuntimeException(response.message))
					}
				}
			case None =>
				Future(Right(new RuntimeException(s"there is no suitable appmaster for ${submit.typeName}.")))
		}
	}
}
