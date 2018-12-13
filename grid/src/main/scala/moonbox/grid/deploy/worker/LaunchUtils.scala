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
			config.getObjectList(key).map(obj => entrySetToMap(obj.entrySet()))
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

	def getRuntimeJars(env: Map[String, String] = sys.env): List[String] = {
		if (env.contains("MOONBOX_DEV")) {
			val projectPath = System.getProperty("user.dir")
			val target = s"$projectPath${File.separator}assembly${File.separator}target"
			val file = new File(target)
			if (file.exists()) {
				file.listFiles()
					.find(f => f.isDirectory && f.getName.startsWith("moonbox-assembly")).map { f =>
					new File(f.getPath + File.separator + "moonbox" + File.separator + "runtime").listFiles()
						.filter(f => f.isFile && f.getName.endsWith(".jar")).map(_.getAbsolutePath).toList
				}.getOrElse(List())
			} else {
				List()
			}
		} else {
			val runtimeDirectory: Option[String] = env.get("MOONBOX_CONF_DIR").orElse(env.get("MOONBOX_HOME").map { t => s"$t${File.separator}runtime" })
			if (runtimeDirectory.isEmpty) {
				throw new Exception("$MOONBOX_HOME does not exist")
			} else {
				val lib = new File(runtimeDirectory.get)
				if (lib.exists()) {
					val jars = lib.listFiles().filter { f => f.isFile && f.getName.endsWith(".jar")}
						.map(_.getAbsolutePath).toList
					jars
				} else {
					List()
				}
			}
		}
	}

	def getAppResourceJar(appType: String, env: Map[String, String] = sys.env): Option[String] = {
		if (env.contains("MOONBOX_DEV" )) { // for dev
		val projectPath = System.getProperty("user.dir")
			val target = s"$projectPath${File.separator}assembly${File.separator}target"
			val file = new File(target)
			if (file.exists()) {
				file.listFiles()
					.find(f => f.isDirectory && f.getName.startsWith("moonbox-assembly")).flatMap { f =>
					new File(f.getPath + File.separator + "moonbox" + File.separator + "apps").listFiles()
						.find(f => f.isFile && f.getName.contains(appType)).map(_.getAbsolutePath)
				}
			} else {
				None
			}
		} else {
			val appsDirectory = env.get("MOONBOX_APP_DIR").orElse(env.get("MOONBOX_HOME").map { t => s"$t${File.separator}apps" })
			if (appsDirectory.isEmpty) {
				throw new Exception("$MOONBOX_HOME does not exist")
			} else {
				val file = new File(appsDirectory.get)
				if (file.exists()) {
					file.listFiles().find(f => f.isFile && f.getName.contains(appType)).map(_.getAbsolutePath)
				} else {
					None
				}
			}
		}
	}

	def getDriverClasspath(env: Map[String, String] = sys.env): String = {
		if (env.contains("MOONBOX_DEV")) {
			val projectPath = System.getProperty("user.dir")
			val target = s"$projectPath${File.separator}assembly${File.separator}target"
			val file = new File(target)
			if (file.exists()) {
				file.listFiles().find(f => f.isDirectory && f.getName.startsWith("moonbox-assembly")) match {
					case Some(f) =>
						val path = new File(f.getPath + File.separator + "moonbox" + File.separator + "libs")
						if (path.exists()) {
							path.getAbsolutePath + File.separator + "*"
						} else {
							throw new Exception("Driver classpath dir libs is not found.")
						}
					case None =>
						throw new Exception("Driver classpath dir libs is not found.")
				}
			} else {
				throw new Exception()
			}
		} else {
			env.get("MOONBOX_HOME") match {
				case Some(t) =>
					val file = new File(s"$t${File.separator}libs")
					if (file.exists()) {
						file.getAbsolutePath + File.separator + "*"
					} else {
						throw new Exception("Driver classpath dir libs is not found.")
					}
				case None =>
					throw new Exception("Driver classpath dir libs is not found.")
				}
		}
	}
}
