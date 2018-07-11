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
import moonbox.core.udf.UdfUtils
import org.apache.spark.sql.resource.{ResourceMonitor, SparkResourceListener}
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

	def getResourceMonitor: ResourceMonitor = {
		resourceMonitor
	}

}

object MixcalContext extends MbLogging {
	// TODO SparkContext.getOrCreate()
	private var sparkContext: SparkContext = _
	private var resourceMonitor: ResourceMonitor = _

	private def getSparkContext(conf: MbConf): SparkContext = {
		synchronized {
			if (sparkContext == null || sparkContext.isStopped) {
				val sparkConf = new SparkConf().setAll(conf.getAll.filter {
					case (key, value) => key.startsWith("moonbox.mixcal")
				}.map{case (key, value) => (key.stripPrefix("moonbox.mixcal."), value)})

				val sparkListener = new SparkResourceListener(sparkConf)
				sparkContext = new SparkContext(sparkConf)
				sparkContext.addSparkListener(sparkListener)
				resourceMonitor = new ResourceMonitor(sparkContext, sparkListener)

				val toUpperCased = conf.get(MIXCAL_SPARK_LOGLEVEL.key, MIXCAL_SPARK_LOGLEVEL.defaultValueString).toUpperCase(Locale.ROOT)
				val loglevel = org.apache.log4j.Level.toLevel(toUpperCased)
				//org.apache.log4j.Logger.getRootLogger.setLevel(loglevel)
				logInfo("New a sparkContext instance.")
				Utils.getRuntimeJars().foreach(sparkContext.addJar)
			} else {
				logInfo("Using an exists sparkContext.")
			}
			sparkContext
		}
	}
}
