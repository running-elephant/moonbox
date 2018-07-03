package moonbox.core


import moonbox.common.util.ParseUtils
import moonbox.common.{MbConf, MbLogging}
import moonbox.core.catalog.{CatalogSession, CatalogTable}
import moonbox.core.command._
import moonbox.core.config._
import moonbox.core.execution.standalone.DataTable
import org.apache.spark.sql.catalyst.TableIdentifier
import org.apache.spark.sql.catalyst.analysis.UnresolvedRelation
import org.apache.spark.sql.catalyst.expressions.{Exists, Expression, ListQuery, ScalarSubquery}
import org.apache.spark.sql.catalyst.plans.logical._
import moonbox.core.datasys.Pushdownable
import org.apache.spark.sql.{DataFrame, MixcalContext}

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

	def optimizedPlan(sqlText: String): LogicalPlan = {
		val preparedSql = prepareSql(sqlText)
		val parsedLogicalPlan = mixcal.parsedLogicalPlan(preparedSql)
		val tableIdentifiers = collectDataSourceTable(parsedLogicalPlan)
		val tableIdentifierToCatalogTable = tableIdentifiers.map { table =>
			(table, getCatalogTable(table.table, table.database))
		}
		tableIdentifierToCatalogTable.foreach {
			case (table, catalogTable) => mixcal.registerTable(table, catalogTable.properties)
		}
		val analyzedLogicalPlan = mixcal.analyzedLogicalPlan(parsedLogicalPlan)
		if (columnPermission) {
			ColumnSelectPrivilegeChecker.intercept(analyzedLogicalPlan, tableIdentifierToCatalogTable.toMap, this)
		}
		mixcal.optimizedLogicalPlan(analyzedLogicalPlan)
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

	private def prepareSql(sqlText: String): String = {
		ParseUtils.parseVariable(sqlText).foldLeft[String](sqlText) { case (res, elem) =>
			res.replaceAll(s"""\\$elem""", getVariable(elem.substring(1)))
		}
	}

	private def collectDataSourceTable(plan: LogicalPlan): Seq[TableIdentifier] = {
		val tables = new mutable.HashSet[TableIdentifier]()
		val logicalTables = new mutable.HashSet[TableIdentifier]()
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
				case ScalarSubquery(child, _, _) => traverseAll(child)
				case Exists(child, _, _) => traverseAll(child)
				case ListQuery(child, _, _) => traverseAll(child)
				case a => a.children.foreach(traverseExpression)
			}
		}
		traverseAll(plan)

		tables.diff(logicalTables).filterNot { identifier =>
			mixcal.sparkSession.sessionState.catalog.isTemporaryTable(identifier)
		}.map { identifier =>
			if (identifier.database.isDefined) identifier
			else identifier.copy(database = Some(catalogSession.databaseName))
		}.toSeq
	}
}

object MbSession extends MbLogging {

	def getMbSession(conf: MbConf): MbSession = new MbSession(conf)

}
