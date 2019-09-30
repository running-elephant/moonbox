package moonbox.grid.deploy.app

import java.util.ServiceLoader
import java.util.concurrent.CopyOnWriteArrayList

import moonbox.common.MbLogging
import moonbox.grid.deploy.master.WorkerInfo

import scala.collection.mutable

abstract class AppManager extends MbLogging {
	import AppManager._

	registerAppManager(this)

	def acceptsAppType(appType: AppType): Boolean

	def registerApp(appInfo: AppInfo): Unit

	def unRegisterApp(appInfo: AppInfo): Unit

	def updateApp(appInfo: AppInfo): Unit

	def startApp(app: DriverDesc, workers: mutable.HashSet[WorkerInfo]): Unit

	def stopApp(appId: String): Unit

	def listApps(): Seq[AppInfo]

}

object AppManager extends MbLogging {
	private val registeredDrivers = new CopyOnWriteArrayList[AppManager]

	loadInitialAppManagers()

	private def loadInitialAppManagers(): Unit = {
		val loadedAppManagers = ServiceLoader.load(classOf[AppManager])
		val appManagerIterator = loadedAppManagers.iterator()
		try {
			while(appManagerIterator.hasNext) {
				appManagerIterator.next()
			}
		} catch {
			case e: Throwable =>
		}
		logInfo("Initial load appManagers complete.")
	}

	def registerAppManager(appManager: AppManager): Unit = {
		registeredDrivers.addIfAbsent(appManager)
		logInfo(s"register AppManager: $appManager.")
	}

}
