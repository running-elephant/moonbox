package moonbox.grid.deploy.rest.service

import akka.actor.ActorRef
import moonbox.catalog.AbstractCatalog.User
import moonbox.catalog.{CatalogApplication, JdbcCatalog}
import moonbox.common.MbLogging
import moonbox.grid.deploy.rest.entities.Application

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
	*
	* @param catalog
	*/
class ApplicationService(catalog: JdbcCatalog, actorRef: ActorRef) extends MbLogging {

	/** create application in catalog
		*
		* @param app  application definition
		* @param user User
		* @return
		*/
	def createApplication(app: Application)(implicit user: User): Future[Unit] = {
		Future {
			catalog.createApplication(CatalogApplication(name = app.appName,
				org = user.org,
				appType = app.appType,
				state = "",
				config = app.config))
		}
	}

	/**
		*
		* @param app
		* @param user
		* @return
		*/
	def updateApplication(app: Application)(implicit user: User): Future[Unit] = {
		Future {
			catalog.alterApplication(CatalogApplication(name = app.appName,
				org = user.org,
				appType = app.appType,
				state = "",
				config = app.config))
		}
	}

	/**
		*
		* @param appName
		* @param user
		* @return
		*/
	def getApplication(appName: String)(implicit user: User): Future[Option[Application]] = {
		Future {
			catalog.getApplicationOption(appName).map(app =>
				Application(appName = app.name, appType = app.appType, state = Some(app.state), config = app.config))
		}
	}

	def deleteApplication(appName: String): Future[Unit] = {
		Future()
	}

	def startApplication(appName: String): Future[Unit] = {
		Future()
	}

	def stopApplication(appName: String): Future[Unit] = {
		Future()
	}

	/**
		*
		* @param user
		* @return
		*/
	def listApplication(implicit user: User): Future[Seq[Application]] = {
		Future {
			catalog.listApplications().map { app =>
				Application(appName = app.name, appType = app.appType, state = Some(app.state), config = app.config)
			}
		}
	}

}
