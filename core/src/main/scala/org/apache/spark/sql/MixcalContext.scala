package org.apache.spark.sql

import java.util.Locale
import java.util.concurrent.ConcurrentHashMap

import moonbox.common.{MbConf, MbLogging}
import moonbox.core.MbSession._
import moonbox.core.config._
import org.apache.spark.sql.optimizer.MbOptimizer
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.catalyst.plans.logical.LogicalPlan
import org.apache.spark.sql.types.StructType
import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.JavaConversions._


class MixcalContext(conf: MbConf) extends MbLogging {
	import MixcalContext._
	lazy val sparkSession = {
		SparkSession.clearDefaultSession()
		SparkSession.builder().sparkContext(getSparkContext(conf)).getOrCreate()
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

}

object MixcalContext {
	private val resources = ConcurrentHashMap.newKeySet[String]()
	private var sparkContext: SparkContext = _

	private def getSparkContext(conf: MbConf): SparkContext = {
		synchronized {
			if (sparkContext == null || sparkContext.isStopped) {
				val sparkConf = new SparkConf().setAll(conf.getAll.filter {
					case (key, value) => key.startsWith("moonbox.mixcal")
				}.map{case (key, value) => (key.stripPrefix("moonbox.mixcal."), value)})
				sparkContext = new SparkContext(sparkConf)
				val toUpperCased = conf.get(MIXCAL_SPARK_LOGLEVEL.key, MIXCAL_SPARK_LOGLEVEL.defaultValueString).toUpperCase(Locale.ROOT)
				val loglevel = org.apache.log4j.Level.toLevel(toUpperCased)
				//org.apache.log4j.Logger.getRootLogger.setLevel(loglevel)
				logInfo("New a sparkContext instance.")
				resources.foreach(sparkContext.addJar)
				resources.clear()
			} else {
				logInfo("Using an exists sparkContext.")
			}
			sparkContext
		}
	}

	def addJar(path: String): Unit = resources.add(path)
}
