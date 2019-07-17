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

package org.apache.spark.sql


import moonbox.common.{MbConf, MbLogging}
import moonbox.catalog._
import org.apache.spark.sql.optimizer.MbOptimizer
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.catalyst.TableIdentifier
import org.apache.spark.sql.catalyst.plans.logical.LogicalPlan
import moonbox.core.udf.UdfUtils
import moonbox.core.datasys.DataSystem
import org.apache.spark.sql.types.StructType
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.catalyst.catalog.{CatalogTableType, ArchiveResource => SparkArchiveResource, FileResource => SparkFileResource, FunctionResource => SparkFunctionResource, JarResource => SparkJarResource}
import org.apache.spark.sql.catalyst.rules.Rule
import org.apache.spark.sql.execution.datasources.{FindDataSourceTable, PreWriteCheck, ResolveSQLOnFile}
import org.apache.spark.sql.hive._


class SparkEngine(conf: MbConf) extends MbLogging {

	import SparkEngine._

	val sparkSession: SparkSession = createSparkSession()

	import sparkSession.sessionState

	private val mbOptimizer = new MbOptimizer(sparkSession)

	/**
	  * create sparkSession with inject hive rules and strategies for hive tables
	  * @return SparkSession
	  */
	private def createSparkSession(): SparkSession = {
		SparkSession.clearDefaultSession()
		SparkSession.clearActiveSession()
		val builder = SparkSession.builder().sparkContext(getSparkContext(conf))
		injectResolutionRule(builder)
		injectPostHocResolutionRule(builder)
		injectCheckRule(builder)
		injectPlannerStrategy(builder)
		builder.getOrCreate()
	}

	/**
	  * inject resolution rules
	  * @param builder SparkSession.Builder
	  */
	private def injectResolutionRule(builder: SparkSession.Builder): Unit = {
		hiveResolutionRule.foreach { rule =>
			builder.withExtensions(_.injectResolutionRule(rule))
		}
	}

	/**
	  * inject post hoc resolution rules
	  * @param builder SparkSession.Builder
	  */
	private def injectPostHocResolutionRule(builder: SparkSession.Builder): Unit = {
		hivePostHocResolutionRule.foreach { rule =>
			builder.withExtensions(_.injectPostHocResolutionRule(rule))
		}
	}

	/**
	  * inject check rules
	  * @param builder SparkSession.Builder
	  */
	private def injectCheckRule(builder: SparkSession.Builder): Unit = {
		hiveCheckRule.foreach { rule =>
			builder.withExtensions(_.injectCheckRule(rule))
		}
	}

	/**
	  * inject planner strategies
	  * @param builder SparkSession.Builder
	  */
	private def injectPlannerStrategy(builder: SparkSession.Builder): Unit = {
		hivePlannerStrategy.foreach { strategy =>
			builder.withExtensions(_.injectPlannerStrategy(strategy))
		}
	}

	/**
	  * parse sql text to unresolved logical plan
	  * @param sql origin sql
	  * @return unresolved logical plan
	  */
	def parsedLogicalPlan(sql: String): LogicalPlan = {
		sessionState.sqlParser.parsePlan(sql)
	}

	/**
	  * translate unresolved logical plan to resolved logical plan
	  * @param plan unresolved logical plan
	  * @return resolved logical plan
	  */
	def analyzedLogicalPlan(plan: LogicalPlan): LogicalPlan = {
		sessionState.analyzer.execute(plan)
	}

	/**
	  * translate resolved logical plan to optimized logical plan
	  * @param plan resolved logical plan
	  * @return optimized logical plan
	  */
	def optimizedLogicalPlan(plan: LogicalPlan): LogicalPlan = {
		sessionState.optimizer.execute(plan)
	}

	/**
	  * optimize logical plan using pushdown rule
	  * @param plan optimized logical plan
	  * @return optimized logical plan
	  */
	def furtherOptimizedLogicalPlan(plan: LogicalPlan): LogicalPlan = {
		mbOptimizer.execute(plan)
	}

	/**
	  * create empty dataFrame
	  * @return empty dataFrame
	  */
	def emptyDataFrame: DataFrame = sparkSession.emptyDataFrame

	/**
	  * clear catalog
	  */
	def reset(): Unit = {
		logInfo(s"Reset sparkSession.")
		sessionState.catalog.reset()
	}

	/**
	  * set job group for identifying user's job
	  * @param group group identifier, sessionId
	  */
	def setJobGroup(group: String, desc: String = ""): Unit = {
		logInfo(s"Set job group id as $group.")
		sparkSession.sparkContext.setJobGroup(group, desc)
	}

	/**
	  * clear job group identifier
	  */
	def clearJobGroup(): Unit = {
		logInfo("Clear job groups.")
		sparkSession.sparkContext.clearJobGroup()
	}

	/**
	  * cancel jobs
	  * @param group group identifier
	  */
	def cancelJobGroup(group: String): Unit = {
		logInfo(s"Cancel job group $group.")
		sparkSession.sparkContext.cancelJobGroup(group)
	}

	/**
	  * create dataFrame from sql
	  * @param sql sql text
	  * @return DataFrame
	  */
	def createDataFrame(sql: String): DataFrame = {
		sparkSession.sql(sql)
	}

	/**
	  * create dataFrame from logical plan
	  * @param tree logical plan
	  * @return DataFrame
	  */
	def createDataFrame(tree: LogicalPlan): DataFrame = {
		Dataset.ofRows(sparkSession, tree)
	}

	/**
	  * create dataFrame from RDD
	  * @param rdd RDD
	  * @param schema the schema describe the RDD
	  * @return DataFrame
	  */
	def createDataFrame(rdd: RDD[Row], schema: StructType): DataFrame = {
		sparkSession.createDataFrame(rdd, schema)
	}

	/**
	  * register database to catalog in sparkSession
	  * @param db database name
	  */
	def registerDatabase(db: String): Unit = {
		if (!sessionState.catalog.databaseExists(db)) {
			createDataFrame(s"create database if not exists ${db}")
		}
	}

	/**
	  * register table to catalog in sparkSession
	  * @param tableIdentifier table name and database
	  * @param props connection or other parameters
	  */
	def registerTable(tableIdentifier: TableIdentifier, props: Map[String, String]): Unit = {
		if (props("type") == "hive") {
			registerHiveTable(tableIdentifier, props)
		} else {
			registerDatasourceTable(tableIdentifier, props)
		}
	}

	/**
	  * register datasource table
	  * @param table table name and database
	  * @param props connection or other parameters
	  */
	private def registerDatasourceTable(table: TableIdentifier, props: Map[String, String]): Unit = {
		val options = props.map { case (k, v) => s"'$k' '$v'" }.mkString(",")
		val schema = props.get("schema").map(s => s"($s)").getOrElse("")
		val sql =
			s"""
			   |create table if not exists ${table.quotedString}$schema
			   |using ${DataSystem.lookupDataSource(props("type"))}
			   |options($options)
			 """.stripMargin
		createDataFrame(sql)
	}

	/**
	  * register hive table
	  * @param table table name and database
	  * @param props connection or other parameters
	  */
	private def registerHiveTable(table: TableIdentifier, props: Map[String, String]): Unit = {
		val hiveClient = HiveClientUtils.getHiveClient(props)
		val hiveCatalogTable = hiveClient.getTable(props("hivedb"), props("hivetable"))
		sessionState.catalog.createTable(hiveCatalogTable.copy(
			identifier = table,
			tableType = CatalogTableType.EXTERNAL,
			provider = Some("hive"),
			properties = hiveCatalogTable.properties ++ props
		), ignoreIfExists = true)
		sessionState.catalog.createPartitions(
			table,
			hiveClient.getPartitions(hiveCatalogTable),
			ignoreIfExists = true
		)
	}

	/**
	  * register view to catalog in sparkSession
	  * @param tableIdentifier table name and database
	  * @param sqlText sql
	  */
	def registerView(tableIdentifier: TableIdentifier, sqlText: String): Unit = {
		val createViewSql =
			s"""
			   |create or replace view ${tableIdentifier.quotedString} as
			   |$sqlText
			 """.stripMargin
		createDataFrame(createViewSql)
	}

	/**
	  * register function to catalog in sparkSession
	  * @param db
	  * @param func
	  */
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
		sessionState.catalog.loadFunctionResources(loadResources)
		if (sourceResources.nonEmpty) {
			sourceResources.foreach { source =>
				source.resourceType match {
					case ScalaResource =>
						sessionState.functionRegistry.registerFunction(
							funcName, UdfUtils.scalaSourceFunctionBuilder(funcName, source.uri, func.className, func.methodName))
					case _ =>
						sessionState.functionRegistry.registerFunction(
							funcName, UdfUtils.javaSourceFunctionBuilder(funcName, source.uri, func.className, func.methodName))
				}
			}
		} else {
			sessionState.functionRegistry.registerFunction(
				funcName, UdfUtils.nonSourceFunctionBuilder(funcName, func.className, func.methodName)
			)
		}
	}

}

object SparkEngine extends MbLogging {

	type RuleBuilder = SparkSession => Rule[LogicalPlan]
	type CheckRuleBuilder = SparkSession => LogicalPlan => Unit
	type StrategyBuilder = SparkSession => Strategy

	private val hiveResolutionRule = Seq[RuleBuilder](
		(session: SparkSession) => new ResolveHiveSerdeTable(session),
		(session: SparkSession) => new FindDataSourceTable(session),
		(session: SparkSession) => new ResolveSQLOnFile(session)
	)

	private val hivePostHocResolutionRule = Seq[RuleBuilder](
		(session: SparkSession) => new DetermineTableStats(session)
	)

	private val hiveCheckRule = Seq[CheckRuleBuilder](
		(session: SparkSession) => PreWriteCheck
	)

	private val hivePlannerStrategy = Seq[StrategyBuilder](
		(session: SparkSession) => new HiveTableScans(session),
		(session: SparkSession) => Scripts
	)

	private var sparkContext: SparkContext = _

	private def getSparkContext(conf: MbConf): SparkContext = {
		if (sparkContext == null) {
			start(conf)
			sparkContext
		} else sparkContext
	}

	def start(conf: MbConf): Unit = {
		synchronized {
			if (sparkContext == null || sparkContext.isStopped) {
				val sparkConf = new SparkConf().setAll(conf.getAll.filterKeys(_.startsWith("spark.")))

				sparkContext = SparkContext.getOrCreate(sparkConf)
				sparkConf.getOption("spark.loglevel").foreach(sparkContext.setLogLevel)
				// val toUpperCased =
				// val loglevel = org.apache.log4j.Level.toLevel(toUpperCased)
				// org.apache.log4j.Logger.getRootLogger.setLevel(loglevel)
				logInfo("New a sparkContext instance.")
			} else {
				logInfo("Using an exists sparkContext.")
			}
		}
	}

}
