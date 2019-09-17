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


import java.io.File

import moonbox.catalog.{CatalogTableType => TableType, _}
import moonbox.common.util.Utils
import moonbox.common.{MbConf, MbLogging}
import moonbox.core.MoonboxCatalog
import moonbox.core.datasys.DataSystem
import moonbox.core.udf.UdfUtils
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.analyze.MbAnalyzer
import org.apache.spark.sql.catalyst.analysis.{UnresolvedFunction, UnresolvedRelation}
import org.apache.spark.sql.catalyst.catalog.{CatalogTableType, SessionCatalog, ArchiveResource => SparkArchiveResource, FileResource => SparkFileResource, FunctionResource => SparkFunctionResource, JarResource => SparkJarResource}
import org.apache.spark.sql.catalyst.expressions.{Literal, SubqueryExpression}
import org.apache.spark.sql.catalyst.plans.logical._
import org.apache.spark.sql.catalyst.rules.{Rule, RuleExecutor}
import org.apache.spark.sql.catalyst.{FunctionIdentifier, QualifiedTableName, TableIdentifier}
import org.apache.spark.sql.execution.command.RunnableCommand
import org.apache.spark.sql.execution.datasources._
import org.apache.spark.sql.hive._
import org.apache.spark.sql.hive.execution.InsertIntoHiveTable
import org.apache.spark.sql.internal.{SQLConf, StaticSQLConf}
import org.apache.spark.sql.optimizer.{MbOptimizer, WholePushdown}
import org.apache.spark.sql.rewrite.CTESubstitution
import org.apache.spark.sql.types.{IntegerType, StructType}
import org.apache.spark.{SparkConf, SparkContext}
import org.spark_project.guava.util.concurrent.Striped

import scala.collection.mutable.ArrayBuffer


class SparkEngine(conf: MbConf, mbCatalog: MoonboxCatalog) extends MbLogging {

  import SparkEngine._

  val sparkSession: SparkSession = createSparkSession()

  import sparkSession.sessionState

  private val mbAnalyzer = createColumnAnalyzer()
  private val mbOptimizer = new MbOptimizer(sparkSession)

  /**
    * create sparkSession with inject hive rules and strategies for hive tables
    *
    * @return SparkSession
    */
  private def createSparkSession(): SparkSession = {
    SparkSession.clearDefaultSession()
    SparkSession.clearActiveSession()

    val builder = SparkSession.builder().sparkContext(getSparkContext(conf))
    builder.config(StaticSQLConf.WAREHOUSE_PATH.key,
      "file://" + Utils.getMoonboxHomeOption.getOrElse("/tmp") + File.separator +
        "spark-warehouse" + File.separator + mbCatalog.getCurrentOrg)
    injectResolutionRule(builder)
    injectPostHocResolutionRule(builder)
    injectCheckRule(builder)
    injectPlannerStrategy(builder)
    builder.getOrCreate()
  }

  /**
    * inject resolution rules
    *
    * @param builder SparkSession.Builder
    */
  private def injectResolutionRule(builder: SparkSession.Builder): Unit = {
    hiveResolutionRule.foreach { rule =>
      builder.withExtensions(_.injectResolutionRule(rule))
    }
  }

  /**
    * inject post hoc resolution rules
    *
    * @param builder SparkSession.Builder
    */
  private def injectPostHocResolutionRule(builder: SparkSession.Builder): Unit = {
    hivePostHocResolutionRule.foreach { rule =>
      builder.withExtensions(_.injectPostHocResolutionRule(rule))
    }
  }

  /**
    * inject check rules
    *
    * @param builder SparkSession.Builder
    */
  private def injectCheckRule(builder: SparkSession.Builder): Unit = {
    hiveCheckRule.foreach { rule =>
      builder.withExtensions(_.injectCheckRule(rule))
    }
  }

  /**
    * inject planner strategies
    *
    * @param builder SparkSession.Builder
    */
  private def injectPlannerStrategy(builder: SparkSession.Builder): Unit = {
    hivePlannerStrategy.foreach { strategy =>
      builder.withExtensions(_.injectPlannerStrategy(strategy))
    }
  }

  private def createColumnAnalyzer(): Option[MbAnalyzer] = {
    mbCatalog.catalogOrg.config.get("spark.sql.permission") match {
      case Some(v) if v.equalsIgnoreCase("true") =>
        Some(new MbAnalyzer(mbCatalog))
      case _ => None
    }
  }

  /**
    * parse sql text to unresolved logical plan
    *
    * @param sql origin sql
    * @return unresolved logical plan
    */
  def parsePlan(sql: String): LogicalPlan = {
    sessionState.sqlParser.parsePlan(sql)
  }

  /**
    * translate unresolved logical plan to resolved logical plan
    *
    * @param plan unresolved logical plan
    * @return resolved logical plan
    */
  def analyzePlan(plan: LogicalPlan): LogicalPlan = {
    sessionState.analyzer.execute(plan)
  }

  /**
    * check all column is granted
    *
    * @param plan analyzed plan
    * @return
    */
  def checkColumns(plan: LogicalPlan): LogicalPlan = {
    mbAnalyzer.map(_.execute(plan)).getOrElse(plan)
  }

  /**
    * translate resolved logical plan to optimized logical plan
    *
    * @param plan resolved logical plan
    * @return optimized logical plan
    */
  def optimizePlan(plan: LogicalPlan): LogicalPlan = {
    sessionState.optimizer.execute(plan)
  }

  /**
    * optimize logical plan using pushdown rule
    *
    * @param plan optimized logical plan
    * @return optimized logical plan
    */
  def pushdownPlan(plan: LogicalPlan): LogicalPlan = {
    mbOptimizer.execute(plan)
  }

  /**
    * rewrite logical plan, to collect unresolved relation and function
    *
    * @param plan parsed logical plan
    * @return logical plan with CTE Substituted
    */
  private def rewrite(plan: LogicalPlan): LogicalPlan = {
    new RuleExecutor[LogicalPlan] {
      override def batches: Seq[Batch] = {
        Batch("CTESubstitution", Once,
          CTESubstitution
        ) :: Nil
      }
    }.execute(plan)
  }

  /**
    * get schema for table
    *
    * @param table    table name
    * @param database database
    * @return schema
    */
  def tableSchema(table: String, database: Option[String]): StructType = {
    val tb = UnresolvedRelation(TableIdentifier(table, database))
    injectTableFunctions(tb)
    analyzePlan(tb).schema
  }

  /**
    * get schema for view
    *
    * @param view     view name
    * @param database database
    * @param viewText view logical, sql text
    * @return schema
    */
  def viewSchema(view: String, database: Option[String], viewText: String): StructType = {
    val parsedPlan = parsePlan(viewText)
    injectTableFunctions(parsedPlan)
    analyzePlan(parsedPlan).schema
  }

  /**
    * get schema of sql result
    *
    * @param sql sql
    * @return schema
    */
  def sqlSchema(sql: String): StructType = {
    val parsedPlan = parsePlan(sql)
    injectTableFunctions(parsedPlan)
    analyzePlan(parsedPlan).schema
  }

  /**
    * execute spark sql
    *
    * @param sql     sql text
    * @param maxRows max rows return to client
    * @return data and schema
    */

  def sql(sql: String, maxRows: Int = 100): (Iterator[Row], StructType) = {
    val parsedPlan = parsePlan(sql)
    parsedPlan match {
      case runnable: RunnableCommand =>
        val dataFrame = createDataFrame(runnable)
        (dataFrame.collect().toIterator, dataFrame.schema)
      case other =>
        injectTableFunctions(parsedPlan)
        // may throw ColumnAnalysisException
        val analyzedPlan = checkColumns(analyzePlan(parsedPlan))
        val limitPlan = analyzedPlan match {
          case insert: InsertIntoDataSourceCommand =>
            insert
          case insert: InsertIntoHadoopFsRelationCommand =>
            insert
          case insert: InsertIntoHiveTable =>
            insert
					case limited@GlobalLimit(Literal(rows, _), LocalLimit(_, child)) =>
						if (rows.toString.toInt > maxRows) {
							GlobalLimit(
								Literal(maxRows, IntegerType),
								LocalLimit(Literal(maxRows, IntegerType),
									analyzedPlan))
						} else limited
          case _ =>
            GlobalLimit(
              Literal(10, IntegerType),
              LocalLimit(Literal(10, IntegerType),
                analyzedPlan))
        }

        val optimizedPlan = optimizePlan(limitPlan)

        if (pushdownEnable) {
          try {
            pushdownPlan(optimizedPlan) match {
              case WholePushdown(child, queryable) =>
                val qe = sparkSession.sessionState.executePlan(child)
                qe.assertAnalyzed()
                val dataTable = queryable.buildQuery(child, sparkSession)
                (dataTable.iterator, dataTable.schema)
              case plan =>
                val dataFrame = createDataFrame(plan)
                (dataFrame.collect().toIterator, dataFrame.schema)
            }
          } catch {
            case e: Exception if e.getMessage != null && e.getMessage.contains("cancelled") =>
              throw e
            case e: Exception if pushdownEnable =>
              logError("Execute pushdown failed, Retry without pushdown optimize.", e)
              // using sql instead of logical plan to create dataFrame.
              // because in some case will throw exception that spark.sql.execution.id is already set.
              val dataFrame = createDataFrame(optimizedPlan)
              (dataFrame.collect().toIterator, dataFrame.schema)
          }
        } else {
          val dataFrame = createDataFrame(optimizedPlan)
          (dataFrame.collect().toIterator, dataFrame.schema)
        }

    }
  }

  /**
    * if pushdown enable
    *
    * @return
    */
  private def pushdownEnable: Boolean = {
    !sparkSession.conf
      .getOption("spark.sql.optimize.pushdown")
      .exists(_.equalsIgnoreCase("false"))
  }

  /**
    * register tables and function to spark session catalog
    *
    * @param plan logical plan
    */
  def injectTableFunctions(plan: LogicalPlan): Unit = {
    val rewritePlan = rewrite(plan)
    val tables = unresolvedTables(rewritePlan)
    val functions = unresolvedFunctions(rewritePlan)

    (tables.map(_.database) ++ functions.map(_.database)).foreach {
      case Some(db) => registerDatabase(db) // create database
      case None => // do nothing
    }

    tables.filterNot { identifier =>
      identifier.database match {
        case Some(db) =>
          sparkSession.catalog.tableExists(db, identifier.table)
        case None =>
          sparkSession.catalog.tableExists(identifier.table)
      }
    }.foreach(registerTable)

    functions.filterNot { identifier =>
      identifier.database match {
        case Some(db) =>
          sparkSession.catalog.functionExists(db, identifier.funcName)
        case None =>
          sparkSession.catalog.functionExists(identifier.funcName)
      }
    }.foreach(registerFunction)
  }

  /**
    * find tables in sql
    *
    * @param sql sql
    * @return output table, input tables
    */
  def unresolvedTablesInSQL(sql: String): (Option[TableIdentifier], Seq[TableIdentifier]) = {
    rewrite(parsePlan(sql)) match {
      case insert: InsertIntoTable =>
        val table = insert.table.asInstanceOf[UnresolvedRelation].tableIdentifier
        (Some(table),
          unresolvedTablesInSelect(insert.query))
      case plan =>
        (None, unresolvedTablesInSelect(plan))
    }
  }

  def unresolvedFunctionsInSQL(sql: String): Seq[FunctionIdentifier] = {
    unresolvedFunctions(rewrite(parsePlan(sql)))
  }

  /**
    * find tables in sql
    *
    * @param plan unresolved logical plan
    * @return tables
    */
  private def unresolvedTables(plan: LogicalPlan): Seq[TableIdentifier] = {
    plan match {
      // TODO other statement
      case insert: InsertIntoTable =>
        val table = insert.table.asInstanceOf[UnresolvedRelation].tableIdentifier
        table +: unresolvedTablesInSelect(insert.query)
      case _ =>
        unresolvedTablesInSelect(plan)
    }
  }

  /**
    * find tables in select clause
    *
    * @param plan unresolved logical plan
    * @return tables
    */
  private def unresolvedTablesInSelect(plan: LogicalPlan): Seq[TableIdentifier] = {
    val tables = new ArrayBuffer[TableIdentifier]()

    def findUnresolvedTables(plan: LogicalPlan): Unit = {
      plan.foreach {
        case u: UnresolvedRelation =>
          tables.append(u.tableIdentifier)
        case u =>
          u.transformExpressions {
            case subExpr: SubqueryExpression =>
              findUnresolvedTables(subExpr.plan)
              subExpr
          }
      }
    }

    findUnresolvedTables(plan)
    tables.distinct
  }

  /**
    * find functions in sql
    *
    * @param plan unresolved logical plan
    * @return functions
    */
  private def unresolvedFunctions(plan: LogicalPlan): Seq[FunctionIdentifier] = {
    val functions = new ArrayBuffer[FunctionIdentifier]()

    def findUnresolvedFunctions(plan: LogicalPlan): Unit = {
      plan.foreach {
        case u =>
          u.transformAllExpressions {
            case func: UnresolvedFunction =>
              functions.append(func.name)
              func
            case subExpr: SubqueryExpression =>
              findUnresolvedFunctions(subExpr.plan)
              subExpr
          }
      }
    }

    findUnresolvedFunctions(plan)
    functions.distinct
  }

  /**
    * create empty dataFrame
    *
    * @return empty dataFrame
    */
  def emptyDataFrame: DataFrame = sparkSession.emptyDataFrame

  /**
    * set current database
    *
    * @param currentDb
    */
  def setCurrentDatabase(currentDb: String): Unit = {
    sessionState.catalog.setCurrentDatabase(currentDb)
  }

  /**
    * drop database from spark session
    *
    * @param db
    * @param ignoreIfNotExists
    * @param cascade
    */
  def dropDatabase(db: String, ignoreIfNotExists: Boolean, cascade: Boolean): Unit = {
    sessionState.catalog.dropDatabase(db, ignoreIfNotExists, cascade)
  }

  /**
    * spark session catalog
    *
    * @return
    */
  def catalog: SessionCatalog = {
    sessionState.catalog
  }

  /**
    * clear catalog
    */
  def reset(): Unit = {
    logInfo(s"Reset sparkSession.")
    sessionState.catalog.reset()
  }

  /**
    * set job group for identifying user's job
    *
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
    *
    * @param group group identifier
    */
  def cancelJobGroup(group: String): Unit = {
    logInfo(s"Cancel job group $group.")
    sparkSession.sparkContext.cancelJobGroup(group)
  }

  /**
    * create dataFrame from sql
    *
    * @param sql sql text
    * @return DataFrame
    */
  def createDataFrame(sql: String, prepared: Boolean = true): DataFrame = {
    if (!prepared) {
      injectTableFunctions(parsePlan(sql))
    }
    sparkSession.sql(sql)
  }

  /**
    * create dataFrame from logical plan
    *
    * @param tree logical plan
    * @return DataFrame
    */
  def createDataFrame(tree: LogicalPlan): DataFrame = {
    Dataset.ofRows(sparkSession, tree)
  }

  /**
    * create dataFrame from RDD
    *
    * @param rdd    RDD
    * @param schema the schema describe the RDD
    * @return DataFrame
    */
  def createDataFrame(rdd: RDD[Row], schema: StructType): DataFrame = {
    sparkSession.createDataFrame(rdd, schema)
  }

  /**
    * register table or view in spark session
    *
    * @param table table identifier
    */
  private def registerTable(table: TableIdentifier): Unit = {
    val currentDb = mbCatalog.getCurrentDb
    val db = table.database.getOrElse(currentDb)
    val catalogTable = mbCatalog.getTable(db, table.table)
    if (catalogTable.tableType == TableType.VIEW) {
      injectTableFunctions(parsePlan(catalogTable.viewText.get))
      registerView(table, catalogTable.viewText.get)
    } else {
      registerTable(table, catalogTable.properties)
    }
  }

  /**
    * register user defined function to spark session
    *
    * @param function
    */
  private def registerFunction(function: FunctionIdentifier): Unit = {
    val db = function.database.getOrElse(mbCatalog.getCurrentDb)
    val catalogFunction = mbCatalog.getFunction(db, function.funcName)
    registerFunction(catalogFunction)
  }

  /**
    * register database to catalog in sparkSession
    *
    * @param db database name
    */
  def registerDatabase(db: String): Unit = {
    if (!sessionState.catalog.databaseExists(db)) {
      createDataFrame(s"create database if not exists ${db}")
    }
  }

  /**
    * register table to catalog in sparkSession
    *
    * @param tableIdentifier table name and database
    * @param props           connection or other parameters
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
    *
    * @param table table name and database
    * @param props connection or other parameters
    */
  private def registerDatasourceTable(table: TableIdentifier, props: Map[String, String]): Unit = {
    val options = props.map { case (k, v) => s"'$k' '$v'" }.mkString(",")
    val schema = props.get("schema").map(s => s"($s)").getOrElse("")

    // for compatibility
    val partition = props.get("partitionColumns").orElse(
      props.get("partitionColumnNames")).map(s => s"PARTITIONED BY ($s)").getOrElse("")
    // val partition = props.get("partitionColumns").map(s => s"PARTITIONED BY ($s)").getOrElse("")
    val sql =
      s"""
         			   |create table if not exists ${table.quotedString}$schema
         			   |using ${DataSystem.lookupDataSource(props("type"))}
         			   |options($options)
         			   |$partition
			 """.stripMargin

    createDataFrame(sql)
  }

  /**
    * register hive table
    *
    * @param table table name and database
    * @param props connection or other parameters
    */
  private def registerHiveTable(table: TableIdentifier, props: Map[String, String]): Unit = {
    val hiveClient = HiveClientUtils.getHiveClient(props)
    val hiveCatalogTable = hiveClient.getTable(props("hivedb"), props("hivetable"))
    val hivePartitions = hiveClient.getPartitions(hiveCatalogTable)
    props.keys.foreach(key => {
      if (key.startsWith("spark.hadoop")) {
        sessionState.conf.setConfString(key.stripPrefix("spark.hadoop."), props(key))
      }
    })
    sessionState.catalog.createTable(hiveCatalogTable.copy(
      identifier = table,
      tableType = CatalogTableType.EXTERNAL,
      storage = hiveCatalogTable.storage.copy(properties = props ++ hiveCatalogTable.storage.properties),
      provider = Some("hive"),
      properties = hiveCatalogTable.properties ++ props
    ), ignoreIfExists = true)
    // TODO modify storage.uri
    sessionState.catalog.createPartitions(
      table,
      hivePartitions,
      ignoreIfExists = true
    )
  }

  /**
    * register view to catalog in sparkSession
    *
    * @param tableIdentifier table name and database
    * @param sqlText         sql
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
    *
    * @param func
    */
  def registerFunction(func: CatalogFunction): Unit = {
    val funcName = s"${func.database}.${func.name}"
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
    (session: SparkSession) => new DetermineTableStats(session),
    (session: SparkSession) => CatalogRelationConversions(sQLConf(session), session),
    (session: SparkSession) => PreprocessTableCreation(session),
    (session: SparkSession) => PreprocessTableInsertion(sQLConf(session)),
    (session: SparkSession) => HiveAnalysis
  )

  private def sQLConf(session: SparkSession): SQLConf = {
    val conf = new SQLConf()
    session.sparkContext.conf.getAll.foreach { case (k, v) =>
      conf.setConfString(k, v)
    }
    conf
  }

  private val hiveCheckRule = Seq[CheckRuleBuilder](
    (session: SparkSession) => PreWriteCheck
  )

  private val hivePlannerStrategy = Seq[StrategyBuilder](
    (session: SparkSession) => new HiveTableScans(session),
    (session: SparkSession) => Scripts
  )

  /** These locks guard against multiple attempts to instantiate a table, which wastes memory. */
  private val tableCreationLocks = Striped.lazyWeakLock(100)

  /** Acquires a lock on the table cache for the duration of `f`. */
  def withTableCreationLock[A](tableName: QualifiedTableName, f: => A): A = {
    val lock = tableCreationLocks.get(tableName)
    lock.lock()
    try f finally {
      lock.unlock()
    }
  }

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
        sparkConf.set(StaticSQLConf.CATALOG_IMPLEMENTATION.key, "in-memory")
        sparkConf.set(StaticSQLConf.WAREHOUSE_PATH.key,
          "file://" + Utils.getMoonboxHomeOption.getOrElse("/tmp") + File.separator + "spark-warehouse")
        sparkConf.set(SQLConf.CONSTRAINT_PROPAGATION_ENABLED.key, "false")
        sparkConf.set(SQLConf.HIVE_MANAGE_FILESOURCE_PARTITIONS.key, "false")
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
