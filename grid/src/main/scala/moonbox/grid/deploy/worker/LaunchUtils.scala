package moonbox.grid.deploy.worker

import java.io.File

import com.typesafe.config.ConfigException.Missing
import com.typesafe.config.{Config, ConfigValue}
import moonbox.common.{MbConf, MbLogging}

import scala.collection.JavaConversions._

object LaunchUtils extends MbLogging {

	def getDriverConfigs(conf: MbConf): (Seq[Map[String, String]], Seq[Map[String, String]]) = {
		val config = conf.getConfig("moonbox.mixcal")
		val commonEntrySet = config.entrySet().filterNot { entry =>
			entry.getKey.equalsIgnoreCase("local") ||
			entry.getKey.equalsIgnoreCase("cluster")
		}
		val commonConfigs = entrySetToMap(commonEntrySet)
		val localConfigs = getConfigs("local", config, commonConfigs)
		val clusterConfigs = getConfigs("cluster", config, commonConfigs)
		(localConfigs, clusterConfigs)
	}

	private def getConfigs(key: String, config: Config, common: Map[String, String]): Seq[Map[String, String]] = {
		try {
			config.getConfigList(key).map(obj => entrySetToMap(obj.entrySet()))
				.map(_.filterKeys(filterKeys)).map(_ ++ common)
		} catch {
			case e: Missing =>
				logInfo(s"mixcal type $key doesn't config.")
				Seq()
		}
	}

	private def entrySetToMap(entrySet: java.util.Set[java.util.Map.Entry[String, ConfigValue]]): Map[String, String] = {
		entrySet.map(entry => entry.getKey -> entry.getValue.unwrapped().toString)(collection.breakOut).toMap
	}

	private def filterKeys(key: String): Boolean = {
		!Seq("spark.master", "spark.submit.deployMode").contains(key)
	}

	private def getMoonboxHome(): String = {
		if (sys.env.contains("MOONBOX_DEV")) {
			val projectPath = System.getProperty("user.dir")
			val target = s"$projectPath${File.separator}assembly${File.separator}target"
			val file = new File(target)
			val home = if (file.exists()) {
				file.listFiles().find(f => f.isDirectory && f.getName.startsWith("moonbox-assembly")).flatMap { f =>
					f.listFiles().find(f => f.isDirectory && f.getName.startsWith("moonbox")).map(_.getAbsolutePath)
				}
			} else {
				None
			}
			home.getOrElse(throw new Exception("Project doesn't build yet"))
		} else {
			sys.env.getOrElse("MOONBOX_HOME", throw  new Exception("$MOONBOX_HOME does not exist"))
		}
	}

	def getRuntimeJars(env: Map[String, String] = sys.env): List[String] = {
		val path = getMoonboxHome() + File.separator + "runtime"
		val file = new File(path)
		if (file.exists()) {
			file.listFiles().filter(f => f.isFile && f.getName.endsWith(".jar")).map(_.getAbsolutePath).toList
		} else List()
	}

	def getAppResourceJar(appType: String, env: Map[String, String] = sys.env): Option[String] = {
		val path = getMoonboxHome() + File.separator + "apps"
		val file = new File(path)
		if (file.exists()) {
			file.listFiles().find(f => f.isFile && f.getName.contains(appType)).map(_.getAbsolutePath)
		} else None
	}

	def getDriverClasspath(env: Map[String, String] = sys.env): String = {
		val path = getMoonboxHome() + File.separator + "libs"
		val file = new File(path)
		if (file.exists()) {
			file.getAbsolutePath + File.separator + "*"
		} else {
			throw new Exception("Driver classpath dir libs is not found.")
		}
	}
}
