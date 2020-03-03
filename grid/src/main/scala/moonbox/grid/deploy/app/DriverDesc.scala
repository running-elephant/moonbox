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

import moonbox.grid.deploy.worker.LaunchUtils
import org.apache.spark.launcher.SparkLauncher

trait DriverDesc {
	def master: Option[String]
	def deployMode: Option[String]
	def mainClass: String
	def appResource: String
	def toAppArgs: Seq[String]
	def toConf: Map[String, String]
}

trait LongRunDriverDesc extends DriverDesc

trait OnceRunDriverDesc extends DriverDesc

case class SparkLocalDriverDesc(
	driverId: String,
	label: String,
	masters: Array[String],
	config: Map[String, String]) extends LongRunDriverDesc {

	override def master = {
		val cores = Runtime.getRuntime.availableProcessors()
		val specialized = config.get("spark.driver.cores")
		val configured = specialized.map(_.toInt).getOrElse(cores / 2)
		Some(s"local[$configured]")
	}
	override def deployMode = None
	override def mainClass = "moonbox.application.interactive.spark.Main"

	override def toString: String = {
		s"DriverDescription ($master)"
	}

	override def toAppArgs: Seq[String] = {
		(config.filterKeys(key => !key.startsWith("spark."))
			++ Map(
			"driverId" -> driverId,
			"appLabel" -> label,
			"masters" -> masters.mkString(";"),
			"applicationType" -> "CENTRALIZED"
		)).toSeq.flatMap { case (k, v) => Seq(k, v)}
	}

	override def toConf: Map[String, String] = {
		config.filterKeys(_.startsWith("spark.")) ++ Map(
			SparkLauncher.DRIVER_EXTRA_CLASSPATH -> LaunchUtils.getDriverClasspath()
		)
	}

	override def appResource: String = {
		LaunchUtils.getAppResourceJar("spark-interactive").getOrElse(
			throw new Exception("Interactive app jar does not found in env.")
		)
	}
}

case class SparkClusterDriverDesc(
	driverId: String,
	label: String,
	masters: Array[String],
	config: Map[String, String]) extends LongRunDriverDesc {

	override def master = Some("yarn")
	override def deployMode = Some("client")
	override def mainClass = "moonbox.application.interactive.spark.Main"

	override def toString: String = {
		s"DriverDescription ($master ${deployMode.get})"
	}

	override def toAppArgs: Seq[String] = {
		(config.filterKeys(key => !key.startsWith("spark."))
			++ Map(
			"driverId" -> driverId,
			"appLabel" -> label,
			"masters" -> masters.mkString(";"),
			"applicationType" -> "DISTRIBUTED"
		)).toSeq.flatMap { case (k, v) => Seq(k, v)}
	}

	override def toConf: Map[String, String] = {
		config.filterKeys(_.startsWith("spark.")) ++ Map(
			SparkLauncher.DRIVER_EXTRA_CLASSPATH -> LaunchUtils.getDriverClasspath()
		)
	}

	override def appResource: String = {
		LaunchUtils.getAppResourceJar("spark-interactive").getOrElse(
			throw new Exception("Interactive app jar does not found in env.")
		)
	}
}

case class SparkBatchDriverDesc(
	org: String,
	username: String,
	sqls: Seq[String],
	config: Map[String, String]
) extends OnceRunDriverDesc {

	override def master = Some("yarn")
	override def deployMode = Some("cluster")
	override def mainClass = "moonbox.application.batch.spark.Main"

	override def toString: String = {
		s"DriverDescription ($master ${deployMode.get} $username ${sqls.mkString(";")})"
	}

	override def toAppArgs: Seq[String] = {
		(config.filterKeys(key => !key.startsWith("spark."))
			++ Map(
			"org" -> org,
			"username" -> username,
			"sqls" -> sqls.mkString(";")
		)).toSeq.flatMap { case (k, v) => Seq(k, v) }
	}

	override def toConf: Map[String, String] = {
		config.filterKeys(_.startsWith("spark."))
	}

	override def appResource: String = {
		LaunchUtils.getAppResourceJar("spark-batch").getOrElse(
			throw new Exception("batch app jar does not found in env.")
		)
	}
}

case class HiveBatchDriverDesc(
	driverId: String,
	org: String,
	username: String,
	sqls: Seq[String],
	config: Map[String, String]
) extends OnceRunDriverDesc {

	override def master = None
	override def deployMode = None
	override def mainClass = "moonbox.application.batch.hive.Main"

	override def toString: String = {
		s"DriverDescription (hive $username ${sqls.mkString(";")})"
	}

	override def toAppArgs: Seq[String] = {
		Map(
			"driverId" -> driverId,
			"org" -> org,
			"username" -> username,
			"sqls" -> sqls.mkString(";")
		).toSeq.flatMap { case (k, v) => Seq(k, v) }
	}

	override def toConf: Map[String, String] = {
		config.filterKeys(_.startsWith("hive.")) ++ Map(
			SparkLauncher.DRIVER_EXTRA_CLASSPATH -> LaunchUtils.getMoonboxLibs()
		)
	}

	override def appResource: String = {
		LaunchUtils.getAppResourceJar("hive-batch").getOrElse(
			throw new Exception("hive app jar does not found in env.")
		)
	}
}

