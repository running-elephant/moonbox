/*-
 * <<
 * Moonbox
 * ==
 * Copyright (C) 2016 - 2019 EDP
 * ==
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * >>
 */

package moonbox.grid.deploy.app

import java.io.File
import java.util.Date

import akka.actor.ActorRef
import moonbox.common.{MbConf, MbLogging}
import moonbox.grid.deploy.DeployMessages.DriverStateChanged
import moonbox.grid.deploy.app.DriverState.DriverState
import moonbox.grid.deploy.worker.LaunchUtils
import moonbox.launcher.AppLauncher
import org.apache.spark.launcher.SparkAppHandle

private[deploy] class DriverRunner(
                                    conf: MbConf,
                                    val driverId: String,
                                    val desc: DriverDesc,
                                    val worker: ActorRef,
                                    val submitDate: Date) extends Serializable with MbLogging {

  @transient private var sparkAppHandle: SparkAppHandle = _
  @transient private var process: Process = _
  @transient private var state: Option[DriverState] = _

  private val CHECK_PROCESS_INTERVAL_MS = 3000

  private implicit val sender: ActorRef = worker

  def start() = {
    new Thread("DriverRunner for " + driverId) {
      override def run(): Unit = {
        try {
          val launcher = new AppLauncher()
          // redirect log
          LaunchUtils.getLogsDirectory.foreach { dir =>
            launcher.redirectOutput(
              ProcessBuilder.Redirect.appendTo(new File(dir + File.separator + driverId + ".log")))
          }
          launcher.setAppName(driverId)

          desc.master.foreach(launcher.setMaster)
          desc.deployMode.foreach(launcher.setDeployMode)
          sys.env.get("SPARK_HOME").foreach(launcher.setSparkHome)

          launcher
            .setMainClass(desc.mainClass)
            .addAppArgs(desc.toAppArgs: _*)
            .setVerbose(false)
            .setAppResource(desc.appResource)
          desc.toConf.foreach { case (k, v) =>
            launcher.setConf(k, v)
          }

          LaunchUtils.getRuntimeJars().foreach {
            launcher.addJar
          }

          def startApplication(): SparkAppHandle = {
            if (desc.deployMode.isDefined && DriverDeployMode(desc.deployMode.get) == DriverDeployMode.CLUSTER) {
              launcher.startApplication()
            } else {
              launcher.startApplication(new SparkAppHandle.Listener {

                override def infoChanged(handle: SparkAppHandle): Unit = {
                }

                override def stateChanged(handle: SparkAppHandle): Unit = {
                  logInfo(handle.getState.toString)
                  val reportState = handle.getState match {
                    case SparkAppHandle.State.UNKNOWN =>
                      DriverState.UNKNOWN
                    case SparkAppHandle.State.LOST =>
                      DriverState.LOST
                    case SparkAppHandle.State.CONNECTED =>
                      DriverState.CONNECTED
                    case SparkAppHandle.State.SUBMITTED =>
                      DriverState.SUBMITTED
                    case SparkAppHandle.State.RUNNING =>
                      DriverState.RUNNING
                    case SparkAppHandle.State.FAILED =>
                      DriverState.FAILED
                    case SparkAppHandle.State.KILLED =>
                      DriverState.KILLED
                    case SparkAppHandle.State.FINISHED =>
                      DriverState.FINISHED
                  }
                  state = Some(reportState)
                  worker ! DriverStateChanged(driverId, reportState, Option(handle.getAppId), None)
                }
              })
            }
          }

          sparkAppHandle = startApplication()
          process = launcher.process

          if (desc.deployMode.isDefined && DriverDeployMode(desc.deployMode.get) == DriverDeployMode.CLUSTER) {
            process.waitFor()
            if (process.exitValue() == 0) {
              worker ! DriverStateChanged(driverId, DriverState.SUBMITTED, None, None)
            } else {
              val message = s"Launch driver $driverId failed."
              logError(message)
              worker ! DriverStateChanged(driverId, DriverState.ERROR, None, Some(new Exception(message)))
            }
          }
        } catch {
          case e: Exception =>
            logError(s"Launch driver $driverId failed.", e)
            worker ! DriverStateChanged(driverId, DriverState.ERROR, None, Some(e))
        }
      }
    }.start()
  }

  def kill() = {
    val appId = if (sparkAppHandle != null && sparkAppHandle.getAppId != null) {
      sparkAppHandle.getAppId
    } else "<unknown>"
    logInfo(s"Killing application with id: $appId.")
    if (sparkAppHandle != null) {
      try {
        sparkAppHandle.stop()
      } catch {
        case e: Exception =>
          logWarning(s"Kill application with id: $appId failed." + e.getMessage)
      }
    } else {
      logWarning(s"SparkAppHandle is null, driver id is $driverId")
    }
    try {
      if (process != null) {
        sparkAppHandle.kill()
      }
    } catch {
      case e: Exception =>
        logWarning(s"Exit application $driverId main process failed, " + e.getMessage)
    }

  }

}
