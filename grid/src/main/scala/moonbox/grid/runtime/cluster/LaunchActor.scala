package moonbox.grid.runtime.cluster

import moonbox.common.MbLogging
import org.apache.spark.launcher.{SparkAppHandle, SparkLauncher}

import scala.concurrent.Future

class LaunchActor extends Actor with MbLogging {

	private var appDriver: ActorRef = _
	private var sparkAppHandle: Future[SparkAppHandle] = _
	private val appListener = new MbAppListener()

	override def preStart: Unit = {
		sparkAppHandle = Future(launchMbApp())
	}

	override def receive: Receive = {
		case RegisterApp(driver) =>
			appDriver = driver
			logInfo(s"Yarn Application ${appId.getOrElse("Not know yet")} registered.")
	}

	private def launchMbApp(): SparkAppHandle = {
		val launcher = new SparkLauncher()
			launcher.setAppName("")
		    .setMaster("yarn").setDeployMode("cluster")
		    .setSparkHome("").setAppResource("")
		    .setMainClass("")
		    .addJar("").addSparkArg("", "")
		launcher.startApplication(appListener)
	}

	private def appId: Option[String] = {
		Option(appListener.appId)
	}

	private def stopApp(): Unit = {
		sparkAppHandle.foreach(_.stop())
	}

	private def killApp(): Unit = {
		sparkAppHandle.foreach(_.kill())
	}

}
