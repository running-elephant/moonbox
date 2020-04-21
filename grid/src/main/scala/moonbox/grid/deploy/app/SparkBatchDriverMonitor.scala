package moonbox.grid.deploy.app

import java.util.concurrent.ConcurrentHashMap

import akka.actor.{ActorRef, ActorSystem}
import moonbox.common.MbConf
import moonbox.common.util.Utils
import moonbox.grid.config.DRIVER_STATEMONITOR_INTERVAL
import moonbox.grid.deploy.DeployMessages.DriverStateChanged
import moonbox.grid.deploy.app.DriverState.DriverState
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.Path
import org.apache.hadoop.security.UserGroupInformation
import org.apache.hadoop.yarn.api.records.{ApplicationReport, YarnApplicationState}
import org.apache.hadoop.yarn.client.api.YarnClient
import org.apache.hadoop.yarn.conf.YarnConfiguration

import scala.collection.JavaConversions._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

class SparkBatchDriverMonitor(system: ActorSystem, master: ActorRef, conf: MbConf) extends DriverMonitor {

  private var principal: String = _
  private var keytab: String = _
  private var yarnConf: Configuration = _
  private var yarnClient: YarnClient = _

  private val STATEREPORT_INTERVAL_MS = conf.get(DRIVER_STATEMONITOR_INTERVAL)

  private implicit val sender: ActorRef = master

  val initLock = new Object()

  import SparkBatchDriverMonitor._

  private def init(): Unit = {
    val config = new YarnConfiguration()
    conf.getAll.foreach { kv =>
      if (YARN_PRINCIPAL.contains(kv._1)) principal = conf.get(kv._1).get
      if (YARN_KEYTAB.contains(kv._1)) keytab = conf.get(kv._1).get
      if (kv._1.startsWith("moonbox.mixcal.spark.yarn.")) config.set(kv._1.drop(21), kv._2)
      if (kv._1.startsWith("moonbox.mixcal.batch.spark.yarn.")) config.set(kv._1.drop(29), kv._2)
    }
    Utils.getDefaultYarnPropertyFiles().foreach(file => config.addResource(new Path(file)))
    yarnConf = config
    yarnClient = YarnClient.createYarnClient()
    if (principal != null) {
      UserGroupInformation.setConfiguration(config)
      UserGroupInformation.loginUserFromKeytab(principal, keytab)
    }
    yarnClient.init(yarnConf)
    yarnClient.start()
    monitorDrivers()
  }

  override def acceptsDeployMode(deployMode: DriverDeployMode): Boolean = {
    deployMode == DriverDeployMode.CLUSTER
  }

  override def registerDriver(driverInfo: DriverInfo): Unit = {
    if (acceptsDeployMode(DriverDeployMode(driverInfo.desc.deployMode.getOrElse("None"))) &&
      !drivers.containsKey(driverInfo.id)) {
      drivers.put(driverInfo.id, driverInfo)
    }
    if (!initialized && drivers.nonEmpty) {
      initLock.synchronized {
        initialized = true
        init()
      }
    }
  }

  override def unRegisterDriver(driverInfo: DriverInfo): Unit = {
    drivers.remove(driverInfo.id)
  }

  override def getDriver(id: String): Option[DriverInfo] = {
    if (drivers.contains(id)) Some(drivers(id))
    else None
  }

  override def killDriver(driverInfo: DriverInfo): Unit = {
    if (driverInfo.appId.isDefined) {
      yarnClient.killApplication(driverInfo.appIdInfo.get)
      unRegisterDriver(driverInfo)
      logInfo(s"Driver ${driverInfo.id} kill success.")
    }
  }

  override def listDrivers(): Seq[DriverInfo] = {
    drivers.values().toSeq
  }

  override def reportDrivers(): Unit = {
    val yarnAppReport = yarnClient.getApplications(yarnApplicationTypes)

    def reportDriver(driverInfo: DriverInfo, appReport: ApplicationReport): Boolean = {
      val appIdInfo = appReport.getApplicationId
      val state = convertToDriverState(appReport.getYarnApplicationState)
      val startTime = appReport.getStartTime
      val finishTime = appReport.getFinishTime
      val exception = appReport.getDiagnostics
      if (driverInfo.appId.isEmpty) {
        driverInfo.appId = Some(appIdInfo.toString)
        driverInfo.appIdInfo = Some(appIdInfo)
        driverInfo.state = state
        driverInfo.setStartDate(startTime)
        driverInfo.setFinishDate(finishTime)
        driverInfo.setException(exception)
        drivers.update(driverInfo.id, driverInfo)
        logInfo(s"Driver ${driverInfo.id} has submitted to yarn, current state is $state.")
        true
      } else if (driverInfo.appId.get == appReport.getApplicationId.toString && driverInfo.state != state) {
        driverInfo.state = state
        driverInfo.setFinishDate(finishTime)
        driverInfo.setException(exception)
        drivers.update(driverInfo.id, driverInfo)
        logInfo(s"Driver ${driverInfo.id} current state is $state.")
        true
      } else {
        false
      }
    }

    val appReportIter = yarnAppReport.iterator()
    while (appReportIter.hasNext) {
      val appReport = appReportIter.next()
      if (drivers.containsKey(appReport.getName)) {
        val driverInfo = drivers.get(appReport.getName)
        val isReported = reportDriver(driverInfo, appReport)
        if (isReported) {
          master ! DriverStateChanged(driverInfo.id, driverInfo.state, driverInfo.appId, driverInfo.exception)
          if (DriverState.isFinished(driverInfo.state)) unRegisterDriver(driverInfo)
        }
      }
    }
  }

  private def monitorDrivers(): Unit = {
    system.scheduler.schedule(0.seconds, STATEREPORT_INTERVAL_MS.milliseconds)(reportDrivers)
  }

}

object SparkBatchDriverMonitor {

  private val YARN_PRINCIPAL = Seq("moonbox.mixcal.spark.yarn.principal", "moonbox.mixcal.batch.spark.yarn.principal")
  private val YARN_KEYTAB = Seq("moonbox.mixcal.spark.yarn.keytab", "moonbox.mixcal.batch.spark.yarn.keytab")
  private val yarnApplicationTypes = Set("SPARK")

  private var initialized: Boolean = false
  private val drivers = new ConcurrentHashMap[String, DriverInfo]

  private def convertToDriverState(yarnApplicationState: YarnApplicationState): DriverState = {
    yarnApplicationState match {
      case YarnApplicationState.NEW | YarnApplicationState.NEW_SAVING | YarnApplicationState.SUBMITTED =>
        DriverState.SUBMITTING
      case YarnApplicationState.ACCEPTED => DriverState.SUBMITTED
      case YarnApplicationState.RUNNING => DriverState.RUNNING
      case YarnApplicationState.FINISHED => DriverState.FINISHED
      case YarnApplicationState.FAILED => DriverState.FAILED
      case YarnApplicationState.KILLED => DriverState.KILLED
    }
  }
}