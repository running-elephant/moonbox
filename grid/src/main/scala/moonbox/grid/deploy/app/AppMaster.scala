package moonbox.grid.deploy.app

import java.text.SimpleDateFormat
import java.util.{Date, Locale, ServiceLoader}
import java.util.concurrent.{ConcurrentHashMap, CopyOnWriteArrayList}

import akka.actor.Address
import moonbox.common.MbLogging
import moonbox.grid.deploy.master.WorkerInfo

import scala.collection.mutable

abstract class AppMaster extends MbLogging {

	AppMasterManager.registerAppMaster(this)

	private var nextDriverNumber = 0

	private def createDateFormat = new SimpleDateFormat("yyyyMMddHHmmss", Locale.ROOT)

	private def newDriverId(): String = {
		val submitDate = new Date()
		val appId = s"$typeName-%s-%04d".format(createDateFormat.format(submitDate), nextDriverNumber)
		nextDriverNumber += 1
		appId
	}

	def createDriverDesc: DriverDesc

	def typeName: String

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

	def getAppMaster(typeName: String): AppMaster = {
		registeredDrivers.get(typeName)
	}

}
