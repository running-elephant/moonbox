package moonbox.grid.deploy

import java.io.File

import akka.actor.ActorRef
import moonbox.common.util.Utils
import moonbox.common.{MbConf, MbLogging}
import moonbox.grid.deploy.DeployMessages.DriverStateChanged
import moonbox.grid.deploy.master.DriverState
import org.apache.spark.launcher.{SparkAppHandle, SparkLauncher}

private[deploy] class DriverRunner(
	conf: MbConf,
	val driverId: String,
	val desc: DriverDescription,
	val worker: ActorRef) extends MbLogging {

	private var sparkAppHandle: SparkAppHandle = _

	def start() = {
		new Thread("ClusterDriverRunner for " + driverId) {
			override def run(): Unit = {
				try {
					val launcher = new SparkLauncher()
					launcher.redirectOutput(new File(System.getProperty("user.dir") + File.separator + driverId + ".log"))
					launcher
						.setAppName(driverId)
						.setMaster(desc.master)
						if (desc.deployMode.isDefined) {
							launcher.setDeployMode(desc.deployMode.get)
						}
					launcher
						.setMainClass(desc.mainClass)
						.addAppArgs() //app args add here
						.setConf("spark.hadoop.yarn.resourcemanager.address", "172.16.231.133:8032")
						.setConf("spark.yarn.access.namenodes", "hdfs://172.16.231.133:8020")
						.setConf("spark.yarn.am.memory", "512m")
						/*.setConf(SparkLauncher.DRIVER_EXTRA_JAVA_OPTIONS, "-XX:+UseConcMarkSweepGC -XX:+PrintGCDetails -XX:-UseGCOverheadLimit -Dlog4j.configuration=sparkx.log4j.properties -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/tmp/moonbox/gc/,spark.executor.extraJavaOptions=-XX:+UseConcMarkSweepGC -XX:+PrintGCDetails -XX:-UseGCOverheadLimit -Dlog4j.configuration=sparkx.log4j.properties -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/tmp/moonbox/gc")*/
						.setVerbose(false)
					    .setSparkHome("/Users/wanghao/Downloads/spark-2.2.0-bin-hadoop2.7")

					launcher.setAppResource(Utils.getYarnAppJar().getOrElse(throw new Exception("no find yarn app in env")))

					Utils.getRuntimeJars().foreach{ launcher.addJar }
					sparkAppHandle = launcher.startApplication(new SparkAppHandle.Listener {
						override def infoChanged(handle: SparkAppHandle): Unit = {
						}

						override def stateChanged(handle: SparkAppHandle): Unit = {
							logInfo(handle.getState.toString)
							val state = handle.getState match {
								case SparkAppHandle.State.UNKNOWN | SparkAppHandle.State.LOST =>
									DriverState.UNKNOWN
								case SparkAppHandle.State.CONNECTED =>
									DriverState.CONNECTED
								case SparkAppHandle.State.SUBMITTED  =>
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
							worker ! DriverStateChanged(driverId, state, None)
						}
					})
				} catch {
					case e: Exception =>
						logError("Launch cluster driver failed.", e)
						worker ! DriverStateChanged(driverId, DriverState.ERROR, Some(e))
				}
			}
		}.start()
	}

	def kill() = {
		val appId = if (sparkAppHandle != null) {
			sparkAppHandle.getAppId
		} else "<unknown>"
		logInfo(s"Killing application with id: $appId.")
		sparkAppHandle.stop()
	}

}
