/*-
 * <<
 * Moonbox
 * ==
 * Copyright (C) 2016 - 2017 EDP
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

package edp.moonbox.core

import java.util.concurrent.ConcurrentHashMap
import java.util.{Map => JMap}

import edp.moonbox.common.{EdpLogging, Util}
import org.apache.spark.SparkConf

import scala.collection.JavaConverters._


class MbConf(loadDefaults: Boolean) extends Cloneable with EdpLogging with Serializable {
	val path = Util.getDefaultPropertiesFile()
	val settings = new ConcurrentHashMap[String, String]()

	if (loadDefaults) {
		loadFromSystemProperties() // Set in java options, `moonbox.` has been striped
	}

	private val fileConfig: Map[String, String] = if(path != null) Util.getPropertiesFromFile(path)
		else Map[String, String]() // moonbox-default.conf
	def this() = this(true)

	mergeConfig(fileConfig)

	private def mergeConfig(config: Map[String, String]): Unit = {
		config.filter { case (k, _) => k.startsWith("moonbox.") }
			.map { case (k, v) => (k.stripPrefix("moonbox."), v) }
			.foreach { case (k, v) =>
				settings.putIfAbsent(k, v) // Set in java options has high priority
				sys.props.getOrElseUpdate(k, v)
			}
	}

	private def loadFromSystemProperties(): MbConf = {
		for ((key, value) <- Util.getSystemProperties if key.startsWith("moonbox.")) {
			set(key.stripPrefix("moonbox."), value)
		}
		this
	}

	def getOption(key: String): Option[String] = {
		Option(settings.get(key)).orElse(None)
	}

	def get(key: String): Option[String] = getOption(key)

	def get(key: String, defaultValue: String): String ={
		settings.getOrDefault(key, defaultValue)
	}

	def get(key: String, defaultValue: Int): Int = {
		get(key, defaultValue.toString).toInt
	}

	def get(key: String, defaultValue: Long): Long ={
		get(key, defaultValue.toString).toLong
	}

	def get(key: String, defaultValue: Boolean): Boolean ={
		get(key, defaultValue.toString).toBoolean
	}

	def get(key: String, defaultValue: Double): Double ={
		get(key, defaultValue.toString).toDouble
	}

	def set(key: String, value: String): this.type = {
		settings.put(key, value)
		this
	}

	def toSparkConf: SparkConf = {
		new SparkConf().setAll(sparkConf)
	}

	def setAll(keyValue: Seq[(String, String)]): this.type = {
		keyValue.foreach {
			case (k, v) => set(k, v)
		}
		this
	}

	def sparkConf: Map[String, String] = startsWith("core.spark.", "spark.").map {
		case (k, v) => (k.stripPrefix("core."), v)
	}

	def akka: JMap[String, String] = { startsWith("grid.akka.", "akka.").map {
			case (k, v) => (k.stripPrefix("grid."), v)
		}.asJava
	}

	def systemAddJars = Util.getPluginJars()

	def persistConnectString: String = persistServers

	def persistServers: String = {
		val impl = settings.get("grid.persist.impl")
		settings.get(s"grid.persist.$impl.servers")
	}

	def hostname: String = {
		settings.get("grid.akka.remote.netty.tcp.hostname")
	}

	def port: Int = {
		settings.get("grid.akka.remote.netty.tcp.port").toInt
	}


	def persisEnable: Boolean = false

	def cacheServers: String = {
		val impl = settings.get("grid.cache.impl")
		settings.get(s"grid.cache.$impl.servers")
	}

	private def startsWith(prefix: String*): Map[String, String] = {
		settings.asScala.filter { case (k, _) =>
			prefix.exists(str => k.startsWith(str))
		}.toMap
	}
}
