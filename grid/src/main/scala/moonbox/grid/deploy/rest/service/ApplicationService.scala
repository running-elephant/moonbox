package moonbox.grid.deploy.rest.service

import moonbox.catalog.AbstractCatalog.User
import moonbox.catalog.{CatalogApplication, JdbcCatalog}
import moonbox.common.MbLogging
import moonbox.grid.deploy.rest.entities.Application
import moonbox.grid.deploy.security.Session
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
	*
	* @param catalog
	*/
class ApplicationService(catalog: JdbcCatalog) extends MbLogging {

	/** create application in catalog
		*
		* @param app application definition
		* @param session session
		* @return
		*/
	def createApplication(app: Application, session: Session): Future[Unit] = {
		implicit val user = sessionConverter(session)
		Future {
			catalog.createApplication(CatalogApplication(name= app.appName,
				labels = Seq(),
				appType = app.appType,
				state = "",
				config = app.config))
		}
	}

	/**
		*
		* @param app
		* @param session
		* @return
		*/
	def updateApplication(app: Application, session: Session): Future[Unit] = {
		implicit val user = sessionConverter(session)
		Future {
			catalog.alterApplication(CatalogApplication(name= app.appName,
				labels = Seq(),
				appType = app.appType,
				state = "",
				config = app.config))
		}
	}

	/**
		*
		* @param appName
		* @param session
		* @return
		*/
	def getApplication(appName: String, session: Session): Future[Option[Application]] = {
		implicit val user = sessionConverter(session)
		Future {
			catalog.getApplicationOption(appName).map(app =>
				Application(appName = app.name, appType = app.appType, state = Some(app.state), config = app.config))
		}
	}

	/**
		*
		* @param session
		* @return
		*/
	def listApplication(session: Session): Future[Seq[Application]] = {
		Future {
			catalog.listApplications().map { app =>
				Application(appName = app.name, appType = app.appType, state = Some(app.state), config = app.config)
			}
		}
	}

	private def sessionConverter(session: Session): User = {
		User(session("orgId").toLong, session("org"), session("userId").toLong, session("user"))
	}
}
