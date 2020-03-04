package moonbox.grid.deploy.app

import java.util.ServiceLoader
import java.util.concurrent.ConcurrentHashMap

import moonbox.catalog.JdbcCatalog
import moonbox.common.MbLogging
import moonbox.grid.deploy.security.Session

import scala.collection.mutable
import scala.util.Random
import scala.collection.JavaConverters._

abstract class AppMaster(jdbcCatalog: JdbcCatalog) extends MbLogging {

	// AppMasterManager.registerAppMaster(this)

	def selectApp(apps: mutable.HashSet[AppInfo], session: Session, appName: Option[String]): Option[AppInfo] = {
		val org = session("org")
		val appsInOrg = apps.filter(app => app.id.startsWith(org) && app.appType == typeName)
		appName match {
			case Some(name) =>
				Random.shuffle(appsInOrg).find(_.id == name)
			case None =>
				Random.shuffle(appsInOrg).headOption
		}
	}

	def launchApp(): Unit = {

	}

	def stopApp(appName: String): Unit = {

	}

	def onWorkerExit(driverRunner: DriverRunner): Unit = {}

	def createDriverDesc(config: Map[String, String]): DriverDesc

	def typeName: String

	def configTemplate: Map[String, String]

	def resourceTemplate: Map[String, String]

}

object AppMasterManager extends MbLogging {
	private val registeredAppMasters = new ConcurrentHashMap[String, AppMaster]

	loadInitialAppMasters()

	private def loadInitialAppMasters(): Unit = {
		val loadedAppMasters = ServiceLoader.load(classOf[AppMaster])
		val appMasterIterator = loadedAppMasters.iterator()
		try {
			while(appMasterIterator.hasNext) {
				appMasterIterator.next()
			}
		} catch {
			case e: Throwable =>
		}
		logInfo("Initial load appManagers complete.")
	}

	def registerAppMaster(appManager: AppMaster): Unit = {
		if (!registeredAppMasters.contains(appManager.typeName)) {
			registeredAppMasters.put(appManager.typeName, appManager)
			logInfo(s"register AppManager: ${appManager.getClass.getSimpleName}.")
		}
	}

	def getAppMaster(typeName: String): Option[AppMaster] = {
		Option(registeredAppMasters.get(typeName))
	}

	def getAppMaters(): Seq[AppMaster] = {
		registeredAppMasters.values().asScala.toSeq
	}

}
