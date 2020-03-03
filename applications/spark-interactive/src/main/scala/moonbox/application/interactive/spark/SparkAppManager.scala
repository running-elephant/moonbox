package moonbox.application.interactive.spark

import moonbox.grid.deploy.app._
import moonbox.grid.deploy.master.WorkerInfo

import scala.collection.mutable

class SparkAppManager extends AppManager {

	override def acceptsAppType(appType: AppType): Boolean = {
		appType == SparkAppType
	}

	override def updateApp(appInfo: AppInfo): Unit = {
		logInfo("updateApp")
	}

	override def registerApp(appInfo: AppInfo): Unit = {
		logInfo("registerApp")
	}

	override def listApps(): Seq[AppInfo] = {
		logInfo("listApps")
		Seq.empty
	}

	override def startApp(app: DriverDesc, workers: mutable.HashSet[WorkerInfo]): Unit = {
		logInfo("startApp")
	}

	override def unRegisterApp(appInfo: AppInfo): Unit = {
		logInfo("unregisterApp")
	}

	override def stopApp(appId: String): Unit = {
		logInfo("stopApp")
	}
}
