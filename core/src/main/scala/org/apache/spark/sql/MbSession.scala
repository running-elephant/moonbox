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

package org.apache.spark.sql

import java.util.concurrent.ConcurrentHashMap

import edp.moonbox.common.EdpLogging
import edp.moonbox.core.parser.{MbAnalyzer, MbParser}
import edp.moonbox.core.{MbCommand, MbConf}
import org.apache.spark.SparkContext

import scala.collection.JavaConverters._


object MbSession extends EdpLogging {

	private val resources = ConcurrentHashMap.newKeySet[String]()
	private var sc: SparkContext = _
	private def sparkContext(conf: MbConf): SparkContext = {
		synchronized {
			if (sc == null) {
				logInfo("create a new spark context")
				sc = new SparkContext(conf.toSparkConf)
				sc.conf.set("spark.sql.constraintPropagation.enabled", "false")
				logInfo(s"add jars ${resources.asScala.mkString(",")} to spark context")
				resources.asScala.foreach(sc.addJar)
				resources.clear()
			} else {
				logInfo("use existing spark context")
			}
		}
		sc
	}

	def addJar(path: String): Boolean = {
		resources.add(path)
	}

	def apply(conf: MbConf): MbSession = new MbSession(conf)
}

class MbSession(val mbConf: MbConf) extends EdpLogging {
	import MbSession._

	lazy val sc = sparkContext(mbConf)

	private implicit val spark = {
		SparkSession.clearDefaultSession()
		SparkSession.builder().sparkContext(sc).getOrCreate()
	}

	lazy val parser = new MbParser

	lazy val analyzer = new MbAnalyzer

	private def parse(sql: String) = parser.parse(sql)

	private def analyze(command: MbCommand) = analyzer.analyze(command)

	def reset(): Unit = {
		logInfo(s"reset spark session: $spark:")
		spark.sessionState.catalog.reset()
	}

	def setJobGroup(group: String, description: String): Unit = {
		logInfo(s"set job group id as $group, group description is $description")
		spark.sparkContext.setJobGroup(group, description, interruptOnCancel = true)
	}

	def clearJobGroup(): Unit = {
		spark.sparkContext.clearJobGroup()
	}

	def cancelJobGroup(group: String): Unit = {
		logInfo(s"cancel job group $group, all running and waiting jobs will be cancelled")
		spark.sparkContext.cancelJobGroup(group)
	}

	def sql(sqlList: Seq[String]): DataFrame = {
		sqlList.map(sql).last
	}

	def sql(sql: String): DataFrame = {
		analyze(parse(sql)).execute()
	}

	def manipulate(jobId: String, data: DataFrame, properties: Map[String, String], handler: (String, DataFrame, Map[String, String]) => Any): Any = {
		handler(jobId, data, properties)
	}


}
