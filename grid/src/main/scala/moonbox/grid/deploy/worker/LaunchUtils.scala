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

package moonbox.grid.deploy.worker

import java.io.File

import com.typesafe.config.ConfigException.Missing
import com.typesafe.config.{Config, ConfigValue}
import moonbox.common.{MbConf, MbLogging}

import scala.collection.JavaConversions._

object LaunchUtils extends MbLogging {

	private def getCommonDriverConfigs(config: Config): Map[String, String] = {
		val commonEntrySet = config.entrySet().filterNot { entry =>
			entry.getKey.equalsIgnoreCase("local") ||
				entry.getKey.equalsIgnoreCase("cluster") ||
			entry.getKey.equalsIgnoreCase("batch")
		}
		entrySetToMap(commonEntrySet).map { case (key, value) =>
			if (!key.startsWith("spark")) {
				"moonbox.mixcal." + key -> value
			} else key -> value
		}
	}

	def getLocalDriverConfigs(conf: MbConf): Seq[Map[String, String]] = {
		val config = conf.getConfig("moonbox.mixcal")
		val commonConfigs = getCommonDriverConfigs(config)
		val localConfigs = getConfigs("local", config, commonConfigs)
		localConfigs
	}

	def getClusterDriverConfigs(conf: MbConf): Seq[Map[String, String]] = {
		val config = conf.getConfig("moonbox.mixcal")
		val commonConfigs = getCommonDriverConfigs(config)
		val clusterConfigs = getConfigs("cluster", config, commonConfigs)
		clusterConfigs
	}

	def getBatchDriverConfigs(conf: MbConf, userConfig: Map[String, String]): Map[String, String] = {
		val systemConfig = conf.getAll.filter { case (k, _) => k.startsWith("moonbox.deploy.catalog") }
		val config = conf.getConfig("moonbox.mixcal")
		val commonConfig = getCommonDriverConfigs(config)
		val batchConfigs = getConfigs("batch", config, commonConfig)

		systemConfig ++ batchConfigs.headOption.getOrElse(Map()) ++ userConfig
	}

	private def getConfigs(key: String, config: Config, common: Map[String, String]): Seq[Map[String, String]] = {
		try {
			config.getConfigList(key).map(obj => entrySetToMap(obj.entrySet()))
				.map(_.filterKeys(filterKeys)).map(common ++ _)
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

	private def getSparkHome(): String = {
		sys.env.getOrElse("SPARK_HOME", throw  new Exception("$SPARK_HOME does not exist"))
	}

	def getLogsDirectory: Option[String] = {
		val path = getMoonboxHome() + File.separator + "logs"
		val file = new File(path)
		if (file.exists()) {
			Some(file.getAbsolutePath)
		} else {
			None
		}
	}

	def getRuntimeJars(env: Map[String, String] = sys.env): List[String] = {
		val file = new File(getRuntimeDir())
		file.listFiles().filter(f => f.isFile && f.getName.endsWith(".jar")).map(_.getAbsolutePath).toList
	}

	private def getRuntimeDir(env: Map[String, String] = sys.env): String = {
		val path = getMoonboxHome() + File.separator + "runtime"
		val file = new File(path)
		if (file.exists()) {
			path
		} else throw new Exception("runtime dir is not found")
	}

	def getAppResourceJar(appType: String, env: Map[String, String] = sys.env): Option[String] = {
		val path = getMoonboxHome() + File.separator + "apps"
		val file = new File(path)
		if (file.exists()) {
			file.listFiles().find(f => f.isFile && f.getName.contains(appType) && f.getName.endsWith(".jar")).map(_.getAbsolutePath)
		} else None
	}

	def getMoonboxLibs() : String = {
		val path = getMoonboxHome() + File.separator + "libs"
		val file = new File(path)
		if (file.exists()) {
			file.getAbsolutePath + File.separator + "*"
		} else {
			throw new Exception("Driver classpath moonbox libs is not found.")
		}
	}

	def getSparkJars() : String = {
		val path = getSparkHome() + File.separator + "jars"
		val file = new File(path)
		if (file.exists()) {
			file.getAbsolutePath + File.separator + "*"
		} else {
			throw new Exception("Driver classpath spark jars is not found.")
		}
	}

	def getDriverClasspath(env: Map[String, String] = sys.env): String = {
		getMoonboxLibs()
	}
}