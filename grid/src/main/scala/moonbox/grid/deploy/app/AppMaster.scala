package moonbox.grid.deploy.app

import java.util.ServiceLoader
import java.util.concurrent.ConcurrentHashMap

import moonbox.common.MbLogging

import scala.collection.mutable
import scala.util.Random

abstract class AppMaster extends MbLogging {

	// AppMasterManager.registerAppMaster(this)

	def selectApp(apps: mutable.HashSet[AppInfo]): Option[AppInfo] = {
		Random.shuffle(apps.filter(_.appType == typeName)).headOption
	}

	def launchApp(): Unit = {

	}

	def stopApp(appName: String): Unit = {

	}

	def createDriverDesc(config: Map[String, String]): DriverDesc

	def typeName: String

	def configTemplate: Map[String, String]

	def resourceTemplate: Map[String, String]

}

object AppMasterManager extends MbLogging {
	private val registeredDrivers = new ConcurrentHashMap[String, AppMaster]

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
		registeredDrivers.putIfAbsent(appManager.typeName, appManager)
		logInfo(s"register AppManager: $appManager.")
	}

	def getAppMaster(typeName: String): Option[AppMaster] = {
		Option(registeredDrivers.get(typeName))
	}

}
