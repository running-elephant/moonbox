package moonbox.grid.deploy.rest.service

import java.util.Date
import java.util.concurrent.TimeUnit

import akka.actor.ActorRef
import akka.pattern.ask
import akka.util.Timeout
import moonbox.catalog.AbstractCatalog.User
import moonbox.catalog.{CatalogApplication, JdbcCatalog}
import moonbox.common.MbLogging
import moonbox.grid.deploy.DeployMessages._
import moonbox.grid.deploy.app.AppMasterManager
import moonbox.grid.deploy.rest.entities.{ApplicationIn, ApplicationOut}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

/**
	*
	* @param catalog
	*/
class ApplicationService(catalog: JdbcCatalog, actorRef: ActorRef) extends MbLogging {

	private implicit val timeout = new Timeout(30, TimeUnit.SECONDS)

	def createApplication(app: ApplicationIn)(implicit user: User): Future[Either[Unit, Throwable]] = Future {
		try {
			catalog.createApplication(CatalogApplication(name = app.appName,
				org = app.org,
				appType = app.appType,
				cluster = app.cluster,
				config = app.config,
				startOnBoot = app.startOnBoot
			), ignoreIfExists = false)
			Left(Unit)
		} catch {
			case e: Throwable => Right(e)
		}
	}

	def updateApplication(app: ApplicationIn)(implicit user: User): Future[Either[Unit, Throwable]] = Future {
		try {
			catalog.alterApplication(CatalogApplication(name = app.appName,
				org = app.org,
				appType = app.appType,
				cluster = app.cluster,
				config = app.config,
				startOnBoot = app.startOnBoot))
			Left(Unit)
		} catch {
			case e: Throwable => Right(e)
		}
	}

	def getApplication(appName: String)(implicit user: User): Future[Either[ApplicationOut, Throwable]] =  {
		try {
			val app = catalog.getApplication(appName)
			actorRef.ask(RequestDriverStatus(user.org + "-" + app.name)).mapTo[DriverStatusResponse].map(status =>
				Left(
					ApplicationOut(
						org = app.org,
						appName = app.name,
						appType = app.appType,
						state = status.state.map(_.toString),
						config = app.config,
						createTime = app.createTime.map(new Date(_)),
						needReload = app.updateTime.map(_ > status.startTime.get),
						startTime = status.startTime.map(new Date(_)),
						worker = status.workerId,
						startOnBoot = app.startOnBoot
					)
				)
			)
		} catch {
			case e: Throwable => Future(Right(e))
		}
	}

	def deleteApplication(appName: String)(implicit user: User): Future[Either[Unit, Throwable]] = Future {
		try {
			catalog.dropApplication(appName, ignoreIfNotExists = false)
			Left(Unit)
		} catch {
			case e: Throwable => Right(e)
		}
	}

	def listApplications(implicit user: User): Future[Either[Seq[ApplicationOut], Throwable]] = {
		try {
			val (apps, pattern) =  if (user.org == "SYSTEM" && user.user.equalsIgnoreCase("ROOT")) {
				(catalog.listAllApplications(), None)
			} else {
				(catalog.listApplications(), Some(user.org))
			}
			actorRef.ask(RequestAllDriverStatus(pattern)).mapTo[AllDriverStatusResponse].map { response =>
				response.exception match {
					case Some(e) => Right(e)
					case None =>
						val idToDrivers = response.driverStatus.map(res => (res.driverId, res)).toMap
						val outs = apps.map { app =>
							idToDrivers.get(app.org + "-" + app.name) match {
								case Some(status) =>
									ApplicationOut(
										org = app.org,
										appName = app.name,
										appType = app.appType,
										state = status.state.map(_.toString),
										config = app.config,
										createTime = app.createTime.map(new Date(_)),
										needReload = app.updateTime.map(_ > status.startTime.get),
										startTime = status.startTime.map(new Date(_)),
										worker = status.workerId,
										startOnBoot = app.startOnBoot
									)
								case None =>
									ApplicationOut(
										org = app.org,
										appName = app.name,
										appType = app.appType,
										state = None,
										config = app.config,
										createTime = Some(new Date(app.createTime.get)),
										needReload = Some(false),
										startTime = None,
										worker = None,
										startOnBoot = app.startOnBoot
									)
							}
						}
						Left(outs)
				}
			}
		} catch {
			case e: Throwable => Future(Right(e))
		}
	}

	def startApplication(appName: String)(implicit user: User): Future[Either[String, Throwable]] =  {
		try {
			val app = catalog.getApplication(appName)
			val config = app.cluster match {
				case Some(cluster) =>
					catalog.getCluster(cluster).config ++ app.config
				case None =>
					app.config
			}
			AppMasterManager.getAppMaster(app.appType) match {
				case Some(appMaster) =>
					actorRef.ask(
						RequestSubmitDriver(app.org + "-" + app.name, appMaster.createDriverDesc(config))
					).mapTo[SubmitDriverResponse].map { res => Left(res.message) }
				case None =>
					Future(Right(new Exception(s"no suitable app master for app type ${app.appType}")))
			}
		} catch {
			case e: Throwable => Future(Right(e))
		}
	}

	def stopApplication(appName: String)(implicit user: User): Future[Either[String, Throwable]] = {
		try {
			actorRef.ask(RequestKillDriver(user.org + "-" + appName)).mapTo[KillDriverResponse].map(res => Left(res.message))
		} catch {
			case e: Throwable => Future(Right(e))
		}
	}

	def getApplicationTemplates(): Future[Either[Seq[String], Throwable]] = {
		Future(Left(Seq.empty))
	}

}
