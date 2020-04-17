package moonbox.grid.deploy.app

import java.util.concurrent.CopyOnWriteArrayList

import moonbox.common.MbLogging

import scala.collection.mutable.ListBuffer

abstract class DriverMonitor extends MbLogging {

  def acceptsDeployMode(deployMode: DriverDeployMode): Boolean

  def registerDriver(driverInfo: DriverInfo): Unit

  def unRegisterDriver(driverInfo: DriverInfo): Unit

  def killDriver(driverInfo: DriverInfo): Unit

  def getDriver(id: String): Option[DriverInfo]

  def listDrivers(): Seq[DriverInfo]

  def reportDrivers(): Unit

  def monitorDrivers(): Unit
}

object DriverMonitor extends MbLogging {
  private val registeredDriverMonitors = new CopyOnWriteArrayList[DriverMonitor]

  def registerDriverMonitor(driverMonitor: DriverMonitor): Unit = {
    registeredDriverMonitors.addIfAbsent(driverMonitor)
    logInfo(s"register DriverMonitor: $driverMonitor.")
  }

  def getDriverMonitors(): Seq[DriverMonitor] = {
    val monitorListBuffer = new ListBuffer[DriverMonitor]
    val iter = registeredDriverMonitors.iterator()
    while (iter.hasNext) {
      monitorListBuffer.append(iter.next())
    }
    monitorListBuffer
  }
}


