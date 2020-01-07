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
import moonbox.grid.deploy.app.{AppMasterManager, DriverState}
import moonbox.grid.deploy.rest.entities.{ApplicationIn, ApplicationInfo, ApplicationOut, ApplicationTemplate}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import DateFormatUtils.formatDate

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

	def getApplication(appName: String)(implicit user: User): Future[Either[ApplicationOut, Throwable]] =  Future {
		try {
			val app = catalog.getApplication(appName)
			Left(
				ApplicationOut(
					org = app.org,
					appName = app.name,
					appType = app.appType,
					config = app.config,
					cluster = app.cluster,
					createTime = app.createTime.map(formatDate),
					updateTime = app.updateTime.map(formatDate),
					startOnBoot = app.startOnBoot
				)
			)
		} catch {
			case e: Throwable => Right(e)
		}
	}

	def deleteApplication(appName: String)(implicit user: User): Future[Either[Unit, Throwable]] = {
		try {
			val app = catalog.getApplication(appName)
			actorRef.ask(RequestDriverStatus(app.fullName())).mapTo[DriverStatusResponse].map { status =>
				if (!status.found || (status.found && status.state.isDefined && DriverState.isFinished(status.state.get))) {
					Left (catalog.dropApplication(appName, ignoreIfNotExists = false))
				} else {
					Right(new Exception(s"Application $appName has been started already. "))
				}
			}
		} catch {
			case e: Throwable => Future(Right(e))
		}
	}

	def listApplications(implicit user: User): Future[Either[Seq[ApplicationOut], Throwable]] = Future {
		try {
			val apps =  if (user.org == "SYSTEM" && user.user.equalsIgnoreCase("ROOT")) {
				catalog.listAllApplications()
			} else {
				catalog.listApplications()
			}
			Left(
				apps.map { app =>
					ApplicationOut(
						org = app.org,
						appName = app.name,
						appType = app.appType,
						config = app.config,
						cluster = app.cluster,
						createTime = app.createTime.map(formatDate),
						updateTime = app.updateTime.map(formatDate),
						startOnBoot = app.startOnBoot
					)
				}
			)
		} catch {
			case e: Throwable => Right(e)
		}
	}

	def startApplication(appName: String)(implicit user: User): Future[Either[String, Throwable]] =  {
		try {
			val app = catalog.getApplication(appName)
			actorRef.ask(RequestDriverStatus(app.fullName())).mapTo[DriverStatusResponse].flatMap { status =>
				if (!status.found || (status.found && status.state.isDefined && DriverState.isFinished(status.state.get))) {
					val config = app.cluster match {
						case Some(cluster) =>
							catalog.getCluster(cluster).config ++ app.config
						case None =>
							app.config
					}
					AppMasterManager.getAppMaster(app.appType) match {
						case Some(appMaster) =>
							actorRef.ask(
								RequestSubmitDriver(app.fullName(), appMaster.createDriverDesc(config))
							).mapTo[SubmitDriverResponse].map { res => Left(res.message) }
						case None =>
							Future(Right(new Exception(s"No suitable app master for app type ${app.appType}")))
					}
				} else {
					Future(Right(new Exception(s"Application $appName has been started already. ")))
				}
			}
		} catch {
			case e: Throwable => Future(Right(e))
		}
	}

	def stopApplication(appName: String)(implicit user: User): Future[Either[String, Throwable]] = {
		try {
			val app = catalog.getApplication(appName)
			actorRef.ask(RequestKillDriver(app.fullName())).mapTo[KillDriverResponse].map(res => Left(res.message))
		} catch {
			case e: Throwable => Future(Right(e))
		}
	}

	def getApplicationInfos(implicit user: User): Future[Either[Seq[ApplicationInfo], Throwable]] = {
		val pattern =  if (user.org == "SYSTEM" && user.user.equalsIgnoreCase("ROOT")) {
			None
		} else {
			Some(user.org)
		}
		actorRef.ask(RequestAllDriverStatus(pattern)).mapTo[AllDriverStatusResponse].map(response => {
			response.exception match {
				case Some(e) => Right(e)
				case None =>
					val appInfos = response.driverStatus.map(res =>
						ApplicationInfo(
							name = res.driverId,
							appType = res.driverType.get,
							startTime = res.startTime.map(d => formatDate(new Date(d))),
							state = res.state.map(_.toString),
							updateTime = res.updateTime.map(t => formatDate(new Date(t))),
							worker = res.workerHostPort,
							exception = res.exception.map(_.getMessage)
						)
					)
					Left(appInfos)
			}
		})
	}

	def getApplicationTemplates(implicit user: User): Future[Either[Seq[ApplicationTemplate], Throwable]] = Future {
		Left(AppMasterManager.getAppMaters().map(am => ApplicationTemplate(am.typeName, am.configTemplate)))
	}
}
