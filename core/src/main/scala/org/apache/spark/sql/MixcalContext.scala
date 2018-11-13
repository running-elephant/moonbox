/*-
 * <<
 * Moonbox
 * ==
 * Copyright (C) 2016 - 2018 EDP
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

import java.util.Locale

import moonbox.common.util.Utils
import moonbox.common.{MbConf, MbLogging}
import moonbox.core.catalog._
import moonbox.core.config._
import org.apache.spark.sql.optimizer.MbOptimizer
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.catalyst.TableIdentifier
import org.apache.spark.sql.catalyst.plans.logical.LogicalPlan
import moonbox.core.datasys.DataSystem
import moonbox.core.resource.ResourceMonitor
import moonbox.core.udf.UdfUtils
import org.apache.spark.sql.resource.{SparkResourceListener, SparkResourceMonitor}
import org.apache.spark.sql.types.StructType
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.catalyst.catalog.{CatalogTableType, ArchiveResource => SparkArchiveResource, FileResource => SparkFileResource, FunctionResource => SparkFunctionResource, JarResource => SparkJarResource}
import org.apache.spark.sql.hive.{HiveClientUtils, HiveTableScans}


class MixcalContext(conf: MbConf) extends MbLogging {
	import MixcalContext._
	val sparkSession = {
		SparkSession.clearDefaultSession()
		SparkSession.builder().sparkContext(getSparkContext(conf))
			.withExtensions(_.injectPlannerStrategy(sparkSession => HiveTableScans(sparkSession)))
			.getOrCreate()
	}
	lazy val mbOptimizer = new MbOptimizer(sparkSession)

	import sparkSession.sessionState

	def parsedLogicalPlan(sql: String): LogicalPlan = sessionState.sqlParser.parsePlan(sql)

	def analyzedLogicalPlan(plan: LogicalPlan): LogicalPlan = sessionState.analyzer.execute(plan)

	def optimizedLogicalPlan(plan: LogicalPlan): LogicalPlan = sessionState.optimizer.execute(plan)

	def furtherOptimizedLogicalPlan(plan: LogicalPlan): LogicalPlan = mbOptimizer.execute(plan)

	def emptyDataFrame: DataFrame = sparkSession.emptyDataFrame

	def reset(): Unit = {
		logInfo(s"Reset sparkSession.")
		sparkSession.sessionState.catalog.reset()
	}

	def setJobGroup(group: String, desc: String = ""): Unit = {
		logInfo(s"Set job group id as $group.")
		sparkSession.sparkContext.setJobGroup(group, desc)
	}

	def clearJobGroup(): Unit = {
		logInfo("Clear job groups.")
		sparkSession.sparkContext.clearJobGroup()
	}

	def cancelJobGroup(group: String): Unit = {
		logInfo(s"Cancel job group $group.")
		sparkSession.sparkContext.cancelJobGroup(group)
	}

	def sqlToDF(sql: String): DataFrame = {
		sparkSession.sql(sql)
	}

	def treeToDF(tree: LogicalPlan): DataFrame = {
		Dataset.ofRows(sparkSession, tree)
	}

	def rddToDF(rdd: RDD[Row], schema: StructType): DataFrame = {
		sparkSession.createDataFrame(rdd, schema)
	}

	def registerTable(tableIdentifier: TableIdentifier, props: Map[String, String]): Unit = {
		val propsString = props.map { case (k, v) => s"$k '$v'" }.mkString(",")
		val typ = props("type")
		val catalog = sparkSession.sessionState.catalog
		if (catalog.tableExists(tableIdentifier)) {
			catalog.dropTable(tableIdentifier, ignoreIfNotExists = true, purge = false)
		}
		if (typ == "hive") {
			val catalogTable = HiveClientUtils.getHiveClient(props).getTable(props("hivedb"), props("hivetable"))
			catalog.createTable(catalogTable.copy(identifier = tableIdentifier, tableType = CatalogTableType.EXTERNAL), ignoreIfExists = true)
		} else {
			val createTableSql =
				s"""
				   |create table ${tableIdentifier.database.map(db => s"$db.${tableIdentifier.table}").getOrElse(tableIdentifier.table)}
				   |using ${DataSystem.lookupDataSource(typ)}
				   |options($propsString)
			 """.stripMargin
			sqlToDF(createTableSql)
		}
	}

	def registerView(tableIdentifier: TableIdentifier, sqlText: String): Unit = {
		val createViewSql =
			s"""
			   |create or replace view ${tableIdentifier.database.map(db => s"$db.${tableIdentifier.table}").getOrElse(tableIdentifier.table)} as
			   |$sqlText
			 """.stripMargin
		sqlToDF(createViewSql)
		println()
	}

	def registerFunction(db: String, func: CatalogFunction): Unit = {
		val funcName = s"$db.${func.name}"
		val (nonSourceResources, sourceResources) = func.resources.partition { resource =>
			resource.resourceType match {
				case _: NonSourceResource => true
				case _: SourceResource => false
			}
		}
		val loadResources = nonSourceResources.map { nonSource =>
			nonSource.resourceType match {
				case JarResource => SparkFunctionResource(SparkJarResource, nonSource.uri)
				case FileResource => SparkFunctionResource(SparkFileResource, nonSource.uri)
				case ArchiveResource => SparkFunctionResource(SparkArchiveResource, nonSource.uri)
			}
		}
		sparkSession.sessionState.catalog.loadFunctionResources(loadResources)
		if (sourceResources.nonEmpty) {
			sourceResources.foreach { source =>
				source.resourceType match {
					case ScalaResource =>
						sparkSession.sessionState.functionRegistry.registerFunction(
							funcName, UdfUtils.scalaSourceFunctionBuilder(funcName, source.uri, func.className, func.methodName))
					case _ =>
						sparkSession.sessionState.functionRegistry.registerFunction(
							funcName, UdfUtils.javaSourceFunctionBuilder(funcName, source.uri, func.className, func.methodName))
				}
			}
		} else {
			sparkSession.sessionState.functionRegistry.registerFunction(
				funcName, UdfUtils.nonSourceFunctionBuilder(funcName, func.className, func.methodName)
			)
		}
	}

}

object MixcalContext extends MbLogging {
	// TODO SparkContext.getOrCreate()
	private var sparkContext: SparkContext = _
	private var resourceMonitor: SparkResourceMonitor = _

	private def getSparkContext(conf: MbConf): SparkContext = {
		sparkContext
	}

	def getMixcalResourceMonitor: SparkResourceMonitor = {
		resourceMonitor
	}

	def start(conf: MbConf, isOnYarn: Boolean): Unit = {
		synchronized {
			if (sparkContext == null || sparkContext.isStopped) {
				val sparkConf = new SparkConf().setAll(conf.getAll.filter {
					case (key, value) => key.startsWith("moonbox.mixcal.")
				}.map{ case (key, value) => (key.stripPrefix("moonbox.mixcal."), value)})

				sparkContext = SparkContext.getOrCreate(sparkConf)

				val sparkListener = new SparkResourceListener(sparkContext.getConf)
				sparkContext.addSparkListener(sparkListener)
				resourceMonitor = new SparkResourceMonitor(sparkContext, sparkListener)

				if (!isOnYarn) {
					//val toUpperCased = conf.get(MIXCAL_SPARK_LOGLEVEL.key, MIXCAL_SPARK_LOGLEVEL.defaultValueString).toUpperCase(Locale.ROOT)
					//val loglevel = org.apache.log4j.Level.toLevel(toUpperCased)
					//org.apache.log4j.Logger.getRootLogger.setLevel(loglevel)
					Utils.getRuntimeJars().foreach(sparkContext.addJar)
				}
				logInfo("New a sparkContext instance.")
			} else {
				logInfo("Using an exists sparkContext.")
			}
		}
	}

}
