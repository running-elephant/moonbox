package moonbox.grid.runtime.cluster

import moonbox.common.MbConf
import moonbox.common.util.Utils
import moonbox.grid.config.{AKKA_DEFAULT_CONFIG, AKKA_HTTP_DEFAULT_CONFIG}
import org.apache.spark.launcher.{SparkAppHandle, SparkLauncher}

import scala.collection.mutable.ArrayBuffer


object MbAppLauncher {

    def akkaConfig(conf: MbConf): Map[String, String] = {
        for { (key, value) <- AKKA_DEFAULT_CONFIG ++ AKKA_HTTP_DEFAULT_CONFIG ++ conf.getAll
              if key.startsWith("moonbox.rpc.akka") || key.startsWith("moonbox.rest.akka")
        } yield {
            if (key.startsWith("moonbox.rpc.akka"))
                (key.stripPrefix("moonbox.rpc."), value)
            else
                (key.stripPrefix("moonbox.rest."), value)
        }
    }

    def launch(yarnAppMainConf: Map[String, String], launchConf: Map[String, String], listener: SparkAppHandle.Listener): SparkAppHandle = {

        val appName = yarnAppMainConf.getOrElse("moonbox.mixcal.cluster.yarn.id", "yarnapp")

        val appArgs = ArrayBuffer.empty[String]
        yarnAppMainConf.foreach { elem => appArgs += elem._1; appArgs += elem._2 }
        launchConf.map{ elem => (s"moonbox.mixcal.${elem._1}", elem._2)}.foreach { elem =>
            appArgs += elem._1; appArgs += elem._2  //mixCallConf, change moonbox.mixcal.cluster to moonbox.mixcal, mbsession config need
        }

        val launcher = new SparkLauncher()
        launcher.setAppName(appName)
                .setMaster("yarn")
                .setDeployMode("cluster")
                .setMainClass("moonbox.yarnapp.MbApp")
                .addAppArgs(appArgs: _*) //app args add here
                .setVerbose(true)

        launchConf.filter(elem => elem._1.startsWith("spark.")).foreach { elem =>
            launcher.setConf(elem._1, elem._2)
        }
        launcher.setSparkHome(sys.env("SPARK_HOME"))
        launcher.setAppResource(Utils.getYarnAppJar().getOrElse(throw new Exception("no find yarn app in env")))

        Utils.getRuntimeJars().foreach{ launcher.addJar }

        launcher.startApplication(listener)
    }
}
