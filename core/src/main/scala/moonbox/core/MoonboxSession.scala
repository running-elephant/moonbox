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

package moonbox.core


import moonbox.common.util.ParseUtils
import moonbox.common.{MbConf, MbLogging}
import moonbox.catalog.{CatalogColumn, CatalogTable}
import moonbox.core.command._
import moonbox.core.config._
import moonbox.core.datasys.{DataTable, Pushdownable}
import moonbox.core.parser.MoonboxParser
import org.apache.spark.sql.catalyst.{FunctionIdentifier, TableIdentifier}
import org.apache.spark.sql.catalyst.analysis.{UnresolvedFunction, UnresolvedRelation}
import org.apache.spark.sql.catalyst.expressions.{Exists, Expression, ListQuery, ScalarSubquery}
import org.apache.spark.sql.catalyst.plans.logical._
import org.apache.spark.sql.execution.command.{AnalyzeColumnCommand, AnalyzeTableCommand}
import org.apache.spark.sql.{DataFrame, SparkEngine}

import scala.collection.mutable

class MoonboxSession(
	conf: MbConf,
	username: String,
	defaultDb: Option[String] = None,
	autoLoadDatabases: Boolean = true,
	sessionConfig: Map[String, String] = Map()) extends MbLogging {

	val pushdown = sessionConfig.get(MoonboxSession.PUSHDOWN).map(_.toBoolean).getOrElse(conf.get(MIXCAL_PUSHDOWN_ENABLE))
	val columnPermission = conf.get(MIXCAL_COLUMN_PERMISSION_ENABLE)

	private val parser = new MoonboxParser()

	val catalog = new CatalogContext(conf)
	val engine = new SparkEngine(conf)

	implicit val sessionEnv: SessionEnv = createSessionEnv()

	initDatabase()

	private val userVariable = new mutable.HashMap[String, String]()

	private def createSessionEnv(): SessionEnv = {
		val userOption = catalog.getUserOption(username)
		if (userOption.isEmpty) {
			throw new Exception(s"$username does not exist.")
		} else {
			val user = userOption.get
			if (user.name.equalsIgnoreCase("ROOT") ) {
				SessionEnv(
					user.id.get,
					user.name,
					isSa = false,
					-1, "SYSTEM", true, -1, "SYSTEM")
			} else {
				val organization = catalog.getOrganization(user.organizationId)
				val database = catalog.getDatabase(user.organizationId, defaultDb.getOrElse("default"))
				SessionEnv(
					user.id.get,
					user.name,
					isSa = user.isSA,
					database.id.get,
					database.name,
					database.isLogical,
					organization.id.get,
					organization.name
				)
			}
		}
	}

	private def initDatabase(): Unit = {
		val currentDb = defaultDb.getOrElse("default")
		if (autoLoadDatabases) {
			catalog.listDatabase(sessionEnv.organizationId).foreach { db =>
				engine.registerDatabase(db.name)
			}
		} else {
			engine.registerDatabase(currentDb)
		}
		engine.sparkSession.catalog.setCurrentDatabase(currentDb)
	}

	def setVariable(key: String, value: String): Unit = {
		userVariable.put(key, value)
	}

	def getVariable(key: String): String = {
		userVariable.getOrElse(key, """$""" + key)
	}

	def getVariables: Map[String, String] = {
		userVariable.toMap
	}

	def cancelJob(jobId: String): Unit = {
		engine.cancelJobGroup(jobId)
	}

	def parsedCommand(sql: String): MbCommand = {
		parser.parsePlan(sql)
	}

	def parsedPlan(sql: String): LogicalPlan = {
		val preparedSql = prepareSql(sql)
		engine.parsedLogicalPlan(preparedSql)
	}

	def analyzedPlan(sqlText: String): LogicalPlan = {
		val preparedSql = prepareSql(sqlText)
		val parsedLogicalPlan = engine.parsedLogicalPlan(preparedSql)
		val qualifiedLogicalPlan = qualifierFunctionName(parsedLogicalPlan)
		prepareAnalyze(qualifiedLogicalPlan)
		engine.analyzedLogicalPlan(qualifiedLogicalPlan)
	}

	def optimizedPlan(plan: LogicalPlan): LogicalPlan = {
		checkColumnPrivilege(plan)
		engine.optimizedLogicalPlan(plan)
	}

	def optimizedPlan(sqlText: String): LogicalPlan = {
		val analyzedLogicalPlan = analyzedPlan(sqlText)
		checkColumnPrivilege(analyzedLogicalPlan)
		engine.optimizedLogicalPlan(analyzedLogicalPlan)
	}

	private def prepareAnalyze(plan: LogicalPlan): Unit = {
		val (tables, functions) = collectUnknownTablesAndFunctions(plan)
		tables.foreach(registerTable)
		functions.foreach(registerFunction)
	}

	def checkColumnPrivilege(plan: LogicalPlan): Unit = {
		if (columnPermission) {
			ColumnSelectPrivilegeChecker.intercept(plan, this)
		}
	}

	private def registerTable(table: TableIdentifier): Unit = {
		val isView = table.database.map(db => catalog.viewExists(sessionEnv.organizationId, db, table.table))
			.getOrElse(catalog.viewExists(sessionEnv.databaseId, table.table))
		if (isView) {
			val catalogView = table.database.map(db => catalog.getView(sessionEnv.organizationId, db, table.table))
				.getOrElse(catalog.getView(sessionEnv.databaseId, table.table))
			val preparedSql = prepareSql(catalogView.cmd)
			val parsedPlan = engine.parsedLogicalPlan(preparedSql)
			prepareAnalyze(qualifierFunctionName(parsedPlan))
			engine.registerView(table, preparedSql)
		} else {
			// if table not exists, throws NoSuchTableException exception
			val catalogTable = table.database.map(db => catalog.getTable(sessionEnv.organizationId, db, table.table))
				.getOrElse(catalog.getTable(sessionEnv.databaseId, table.table))
			engine.registerTable(table, catalogTable.properties)
		}
	}

	private def registerFunction(function: FunctionIdentifier): Unit = {
		val (databaseId, databaseName) = if (function.database.isEmpty) {
			(sessionEnv.databaseId, sessionEnv.databaseName)
		} else {
			val catalogDatabase = catalog.getDatabase(sessionEnv.organizationId, function.database.get)
			(catalogDatabase.id.get, catalogDatabase.name)
		}
		val catalogFunction = catalog.getFunction(databaseId, function.funcName)
		engine.registerFunction(databaseName, catalogFunction)
	}

	def pushdownPlan(plan: LogicalPlan, pushdown: Boolean = this.pushdown): LogicalPlan = {
		if (pushdown) {
			engine.furtherOptimizedLogicalPlan(plan)
		} else plan
	}

	def toDF(plan: LogicalPlan): DataFrame = {
		engine.createDataFrame(plan)
	}

	def toDT(plan: LogicalPlan, datasys: Pushdownable): DataTable = {
		val qe = engine.sparkSession.sessionState.executePlan(plan)
		qe.assertAnalyzed()
		datasys.buildQuery(plan, engine.sparkSession)
	}

	def withPrivilege[T](cmd: MbCommand)(f: => T): T = {
		CmdPrivilegeChecker.intercept(cmd, catalog, sessionEnv) match {
			case false => throw new Exception("Permission denied.")
			case true => f
		}
	}

	def getCatalogTable(table: String, database: Option[String]): CatalogTable = {
		database match {
			case None =>
				catalog.getTable(sessionEnv.databaseId, table)
			case Some(databaseName) =>
				val database = catalog.getDatabase(sessionEnv.organizationId, databaseName)
				catalog.getTable(database.id.get, table)
		}
	}

	def schema(databaseId: Long, view: String, sqlText: String): Seq[CatalogColumn] = {
		val catalogDatabase = catalog.getDatabase(databaseId)
		analyzedPlan(sqlText).schema.map { field =>
			CatalogColumn(
				name = field.name,
				dataType = field.dataType.simpleString,
				databaseId = catalogDatabase.id.get,
				table = view,
				createBy = catalogDatabase.createBy,
				createTime = catalogDatabase.createTime,
				updateBy = catalogDatabase.updateBy,
				updateTime = catalogDatabase.updateTime
			)
		}
	}

	def schema(databaseId: Long, table: String): Seq[CatalogColumn] = {
		val catalogDatabase = catalog.getDatabase(databaseId)
		schema(table, Some(catalogDatabase.name))
	}

	def schema(table: String, database: Option[String]): Seq[CatalogColumn] = {
		val db = database.getOrElse(sessionEnv.databaseName)
		val catalogDatabase = catalog.getDatabase(sessionEnv.organizationId, db)
		val tableIdentifier = TableIdentifier(table, Some(db))
		prepareAnalyze(UnresolvedRelation(tableIdentifier))
		engine.analyzedLogicalPlan(UnresolvedRelation(tableIdentifier)).schema.map { field =>
			CatalogColumn(
				name = field.name,
				dataType = field.dataType.simpleString,
				databaseId = catalogDatabase.id.get,
				table = table,
				createBy = catalogDatabase.createBy,
				createTime = catalogDatabase.createTime,
				updateBy = catalogDatabase.updateBy,
				updateTime = catalogDatabase.updateTime
			)
		}
	}

	private def prepareSql(sqlText: String): String = {
		ParseUtils.parseVariable(sqlText).foldLeft[String](sqlText) { case (res, elem) =>
			res.replaceAll(s"""\\$elem""", getVariable(elem.substring(1)))
		}
	}

	private def qualifierFunctionName(plan: LogicalPlan): LogicalPlan = {
		plan.transformAllExpressions {
			case func@UnresolvedFunction(identifier, children, _) => {
				if (engine.sparkSession.sessionState.catalog.functionExists(identifier)) {
					func
				} else {
					val database = identifier.database.orElse(Some(sessionEnv.databaseName))
					func.copy(name = identifier.copy(database = database))
				}

			}
		}
	}

	def collectUnknownTablesAndFunctions(plan: LogicalPlan): (Seq[TableIdentifier], Seq[FunctionIdentifier]) = {
		val tables = new mutable.HashSet[TableIdentifier]()
		val logicalTables = new mutable.HashSet[TableIdentifier]()
		val functions = new mutable.HashSet[UnresolvedFunction]()
		def traverseAll(plan: LogicalPlan): Unit = {
			plan.foreach {
				case AnalyzeTableCommand(tableIdent, _) =>
					tables.add(tableIdent)
				case AnalyzeColumnCommand(tableIdent, _) =>
					tables.add(tableIdent)
				case InsertIntoTable(UnresolvedRelation(tableIdentifier), _, query ,_ , _) =>
					tables.add(tableIdentifier)
					traverseAll(query)
				case With(_, cteRelations) =>
					cteRelations.foreach { case (sql, SubqueryAlias(alias, child)) =>
						logicalTables.add(TableIdentifier(alias))
						traverseAll(child)
					}
				case SubqueryAlias(alias, _) =>
					logicalTables.add(TableIdentifier(alias))
				case UnresolvedRelation(indent) =>
					tables.add(indent)
				case project: Project =>
					project.projectList.foreach(traverseExpression)
				case aggregate: Aggregate =>
					aggregate.aggregateExpressions.foreach(traverseExpression)
				case Filter(condition, child) =>
					traverseExpression(condition)
				case _ => // do nothing
			}
		}

		def traverseExpression(expr: Expression): Unit = {
			expr.foreach {
				case func@UnresolvedFunction(identifier, children, _) => {
					functions.add(func)
					children.foreach(traverseExpression)
				}
				case ScalarSubquery(child, _, _) => traverseAll(child)
				case Exists(child, _, _) => traverseAll(child)
				case ListQuery(child, _, _) => traverseAll(child)
				case a => a.children.foreach(traverseExpression)
			}
		}
		traverseAll(plan)

		if (!autoLoadDatabases) {
			val catalogDatabases = catalog.listDatabase(sessionEnv.organizationId).map(_.name)
			(tables.map(_.database) ++ functions.map(_.name.database)).toSeq.distinct.foreach {
				case Some(db) =>
					if (catalogDatabases.contains(db)) {
						engine.registerDatabase(db)
					} else {
						throw new Exception(s"Database $db does not exist.")
					}
				case None =>
				// do nothing
			}
		}

		val needRegisterTables = tables.diff(logicalTables).filterNot { identifier =>
			engine.sparkSession.sessionState.catalog.isTemporaryTable(identifier) ||
				engine.sparkSession.sessionState.catalog.tableExists(identifier)
		}.map { identifier =>
			if (identifier.database.isDefined) identifier
			else identifier.copy(database = Some(sessionEnv.databaseName))
		}.toSeq
		val needRegisterFunctions = {
			functions.filterNot { case UnresolvedFunction(identifier, children, _) =>
				engine.sparkSession.sessionState.catalog.functionExists(identifier)
			}
		}.map(_.name).toSeq
		(needRegisterTables, needRegisterFunctions)
	}
}

object MoonboxSession extends MbLogging {
	val PUSHDOWN = "pushdown"
}
