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

package moonbox.core


import moonbox.common.util.ParseUtils
import moonbox.common.{MbConf, MbLogging}
import moonbox.core.catalog.{CatalogColumn, CatalogSession, CatalogTable}
import moonbox.core.command._
import moonbox.core.config._
import moonbox.core.execution.standalone.DataTable
import org.apache.spark.sql.catalyst.{FunctionIdentifier, TableIdentifier}
import org.apache.spark.sql.catalyst.analysis.{NoSuchPermanentFunctionException, UnresolvedFunction, UnresolvedRelation}
import org.apache.spark.sql.catalyst.expressions.{Exists, Expression, ListQuery, ScalarSubquery}
import org.apache.spark.sql.catalyst.plans.logical._
import moonbox.core.datasys.{DataSystem, Pushdownable}
import org.apache.spark.sql.{AnalysisException, DataFrame, MixcalContext}

import scala.collection.mutable

class MbSession(conf: MbConf) extends MbLogging {
	implicit var catalogSession: CatalogSession = _
	private val pushdown = conf.get(MIXCAL_PUSHDOWN_ENABLE.key, MIXCAL_PUSHDOWN_ENABLE.defaultValue.get)
	val columnPermission = conf.get(MIXCAL_COLUMN_PERMISSION_ENABLE.key, MIXCAL_COLUMN_PERMISSION_ENABLE.defaultValue.get)

	private val userVariable = new mutable.HashMap[String, String]()
	val catalog = new CatalogContext(conf)
	val mixcal = new MixcalContext(conf)

	def bindUser(username: String, initializedDatabase: Option[String] = None): this.type = {
		this.catalogSession = {
			catalog.getUserOption(username) match {
				case Some(catalogUser) =>
					if (catalogUser.name == "ROOT") {
						new CatalogSession(
							catalogUser.id.get,
							catalogUser.name,
							-1, "SYSTEM", true, -1, "SYSTEM")
					} else {
						val organization = catalog.getOrganization(catalogUser.organizationId)
						val database = catalog.getDatabase(catalogUser.organizationId, initializedDatabase.getOrElse("default"))
						new CatalogSession(
							catalogUser.id.get,
							catalogUser.name,
							database.id.get,
							database.name,
							database.isLogical,
							organization.id.get,
							organization.name
						)
					}
				case None =>
					throw new Exception(s"$username does not exist.")
			}
		}
		catalog.listDatabase(catalogSession.organizationId).map { catalogDatabase =>
			if (!mixcal.sparkSession.sessionState.catalog.databaseExists(catalogDatabase.name)) {
				mixcal.sqlToDF(s"create database if not exists ${catalogDatabase.name}")
			}
		}
		mixcal.sparkSession.catalog.setCurrentDatabase(initializedDatabase.getOrElse("default"))
		this
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
		mixcal.sparkSession.sparkContext.cancelJobGroup(jobId)
	}

	def analyzedPlan(sqlText: String): LogicalPlan = {
		val preparedSql = prepareSql(sqlText)
		val parsedLogicalPlan = mixcal.parsedLogicalPlan(preparedSql)
		val qualifiedLogicalPlan = qualifierFunctionName(parsedLogicalPlan)
		prepareAnalyze(qualifiedLogicalPlan)
		mixcal.analyzedLogicalPlan(qualifiedLogicalPlan)
	}

	def optimizedPlan(sqlText: String): LogicalPlan = {
		val analyzedLogicalPlan = analyzedPlan(sqlText)
		checkColumnPrivilege(analyzedLogicalPlan)
		mixcal.optimizedLogicalPlan(analyzedLogicalPlan)
	}

	def prepareAnalyze(plan: LogicalPlan): Unit = {
		val (tableIdentifiers, functionIdentifiers) = collectUnknownTablesAndFunctions(plan)
		registerTables(tableIdentifiers)
		registerFunctions(functionIdentifiers)
	}

	def checkColumnPrivilege(plan: LogicalPlan): Unit = {
		if (columnPermission) {
			ColumnSelectPrivilegeChecker.intercept(plan, this)
		}
	}

	def registerTables(tables: Seq[TableIdentifier]): Unit = {
		tables.foreach { table =>
			val isView = table.database.map(db => catalog.viewExists(catalogSession.organizationId, db, table.table))
				.getOrElse(catalog.viewExists(catalogSession.databaseId, table.table))
			if (isView) {
				val catalogView = table.database.map(db => catalog.getView(catalogSession.organizationId, db, table.table))
					.getOrElse(catalog.getView(catalogSession.databaseId, table.table))
				val parsedPlan = mixcal.parsedLogicalPlan(prepareSql(catalogView.cmd))
				prepareAnalyze(qualifierFunctionName(parsedPlan))
				mixcal.registerView(catalogView.name, parsedPlan)
			} else {// if table not exists, throws NoSuchTableException exception
				val catalogTable = table.database.map(db => catalog.getTable(catalogSession.organizationId, db, table.table))
					.getOrElse(catalog.getTable(catalogSession.databaseId, table.table))
				mixcal.registerTable(table, catalogTable.properties)
			}
		}
	}

	def registerFunctions(functions: Seq[FunctionIdentifier]): Unit = {
		functions.foreach { function =>
			val (databaseId, databaseName) = if (function.database.isEmpty) {
				(catalogSession.databaseId, catalogSession.databaseName)
			} else {
				val catalogDatabase = catalog.getDatabase(catalogSession.organizationId, function.database.get)
				(catalogDatabase.id.get, catalogDatabase.name)
			}
			val catalogFunction = catalog.getFunction(databaseId, function.funcName)
			mixcal.registerFunction(databaseName, catalogFunction)
		}
	}

	def pushdownPlan(plan: LogicalPlan, pushdown: Boolean = this.pushdown): LogicalPlan = {
		if (pushdown) {
			mixcal.furtherOptimizedLogicalPlan(plan)
		} else plan
	}

	def toDF(plan: LogicalPlan): DataFrame = {
		mixcal.treeToDF(plan)
	}

	def toDT(plan: LogicalPlan, datasys: Pushdownable): DataTable = {
		val qe = mixcal.sparkSession.sessionState.executePlan(plan)
		qe.assertAnalyzed()
		datasys.buildQuery(plan)
	}

	def withPrivilege[T](cmd: MbCommand)(f: => T): T = {
		CmdPrivilegeChecker.intercept(cmd, catalog, catalogSession) match {
			case false => throw new Exception("Permission denied.")
			case true => f
		}
	}

	def getCatalogTable(table: String, database: Option[String]): CatalogTable = {
		database match {
			case None =>
				catalog.getTable(catalogSession.databaseId, table)
			case Some(databaseName) =>
				val database = catalog.getDatabase(catalogSession.organizationId, databaseName)
				catalog.getTable(database.id.get, table)
		}
	}

	def schema(view: String, database: Option[String], sqlText: String): Seq[CatalogColumn] = {
		val db = database.getOrElse(catalogSession.databaseName)
		val catalogDatabase = catalog.getDatabase(catalogSession.organizationId, db)
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

	def schema(table: String, database: Option[String]): Seq[CatalogColumn] = {
		val db = database.getOrElse(catalogSession.databaseName)
		val catalogDatabase = catalog.getDatabase(catalogSession.organizationId, db)
		val tableIdentifier = TableIdentifier(table, Some(db))
		prepareAnalyze(UnresolvedRelation(tableIdentifier))
		mixcal.analyzedLogicalPlan(UnresolvedRelation(tableIdentifier)).schema.map { field =>
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
				if (mixcal.sparkSession.sessionState.catalog.functionExists(identifier)) {
					func
				} else {
					val database = identifier.database.orElse(Some(catalogSession.databaseName))
					func.copy(name = identifier.copy(database = database))
				}

			}
		}
	}

	private def collectUnknownTablesAndFunctions(plan: LogicalPlan): (Seq[TableIdentifier], Seq[FunctionIdentifier]) = {
		val tables = new mutable.HashSet[TableIdentifier]()
		val logicalTables = new mutable.HashSet[TableIdentifier]()
		val functions = new mutable.HashSet[UnresolvedFunction]()
		def traverseAll(plan: LogicalPlan): Unit = {
			plan.foreach {
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

		val needRegisterTables = tables.diff(logicalTables).filterNot { identifier =>
			mixcal.sparkSession.sessionState.catalog.isTemporaryTable(identifier) ||
				mixcal.sparkSession.sessionState.catalog.tableExists(identifier)
		}.map { identifier =>
			if (identifier.database.isDefined) identifier
			else identifier.copy(database = Some(catalogSession.databaseName))
		}.toSeq
		val needRegisterFunctions = {
			functions.filterNot { case UnresolvedFunction(identifier, children, _) =>
				mixcal.sparkSession.sessionState.catalog.functionExists(identifier)
			}
		}.map(_.name).toSeq
		(needRegisterTables, needRegisterFunctions)
	}
}

object MbSession extends MbLogging {

	def getMbSession(conf: MbConf): MbSession = new MbSession(conf)

	def startMixcalEnv(conf: MbConf): Unit = {
		MixcalContext.start(conf)
	}

}
