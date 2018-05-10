package moonbox.core


import moonbox.common.{MbConf, MbLogging}
import moonbox.core.catalog.{CatalogSession, CatalogTable}
import moonbox.core.command._
import moonbox.core.config._
import org.apache.spark.sql.catalyst.TableIdentifier
import org.apache.spark.sql.catalyst.analysis.UnresolvedRelation
import org.apache.spark.sql.catalyst.expressions.{Exists, Expression, ListQuery, ScalarSubquery}
import org.apache.spark.sql.catalyst.plans.logical._
import org.apache.spark.sql.datasys.DataSystemFactory
import org.apache.spark.sql.{DataFrame, MixcalContext, Row, SaveMode}

import scala.collection.mutable

class MbSession(conf: MbConf) extends MbLogging {
	implicit var catalogSession: CatalogSession = _
	private val pushdown = conf.get(MIXCAL_PUSHDOWN_ENABLE.key, MIXCAL_PUSHDOWN_ENABLE.defaultValue.get)
	val columnPermission = conf.get(MIXCAL_COLUMN_PERMISSION_ENABLE.key, MIXCAL_COLUMN_PERMISSION_ENABLE.defaultValue.get)

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
							-1, "SYSTEM", -1, "SYSTEM")
					} else {
						val organization = catalog.getOrganization(catalogUser.organizationId)
						val database = catalog.getDatabase(catalogUser.organizationId, initializedDatabase.getOrElse("default"))
						new CatalogSession(
							catalogUser.id.get,
							catalogUser.name,
							database.id.get,
							database.name,
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
		this
	}

	def cancelJob(jobId: String): Unit = {
		mixcal.sparkSession.sparkContext.cancelJobGroup(jobId)
	}

	def execute(jobId: String, cmds: Seq[MbCommand]): Any = {
		cmds.map{c => execute(jobId, c)}.last
	}

	def execute(jobId: String, cmd: MbCommand): Any = {
		CmdPrivilegeChecker.intercept(cmd, catalog, catalogSession) match {
			case false => throw new Exception("Permission denied.")
			case true =>
				cmd match {
					case runnable: MbRunnableCommand => // direct
						executeRunnable(runnable)
					case createTempView: CreateTempView =>
						executeCreateTempView(createTempView)
					case mbQuery: MQLQuery => // cached
						executeQuery(mbQuery, jobId)
					case insert: InsertInto => // external
						executeInsertInto(insert)
					case _ => throw new Exception("Unsupported command.")
				}
		}
	}

	private def executeRunnable(runnable: MbRunnableCommand): Seq[Row] = {
		runnable.run(this)
	}

	private def executeCreateTempView(create: CreateTempView): Unit = {
		// pushdown optimize later
		val df = sql(create.query, pushdown = false)
		if (create.isCache) {
			df.cache()
		}
		if (create.replaceIfExists) {
			df.createOrReplaceTempView(create.name)
		} else {
			df.createTempView(create.name)
		}
	}

	private def executeQuery(query: MQLQuery, jobId: String): String = {
		try {
			sql(query.query).write
				.format("org.apache.spark.sql.execution.datasources.redis")
				.option("jobId", jobId)
				.options(conf.getAll.filter(_._1.startsWith("moonbox.cache.")))
				.save()
		} catch {
			case e: ColumnPrivilegeException =>
				throw e
			case e: Exception =>
				sql(query.query, pushdown = false).write
					.format("org.apache.spark.sql.execution.datasources.redis")
					.option("jobId", jobId)
					.options(conf.getAll.filter(_._1.startsWith("moonbox.cache.")))
					.save()
		}
		jobId
	}

	private def executeInsertInto(insert: InsertInto): Unit = {
		val options = getCatalogTable(insert.table.table, insert.table.database).properties
		val saveMode = if (insert.overwrite) SaveMode.Overwrite else SaveMode.Append
		try {
			sql(insert.query).write.format(options("type"))
				.options(options)
				.mode(saveMode)
				.save()
		} catch {
			case e: ColumnPrivilegeException =>
				throw e
			case e: Exception =>
				sql(insert.query, pushdown = false).write.format(options("type"))
					.options(options)
					.mode(saveMode)
					.save()
		}
	}

	def sql(sqlText: String, pushdown: Boolean = this.pushdown): DataFrame = {
		val parsedLogicalPlan = mixcal.parsedLogicalPlan(sqlText)
		val tableIdentifiers = collectDataSourceTable(parsedLogicalPlan)
		val tableIdentifierToCatalogTable = tableIdentifiers.map { table =>
			(table, getCatalogTable(table.table, table.database))
		}.toMap
		tableIdentifierToCatalogTable.foreach {
			case (table, catalogTable) => registerDataSourceTable(table, catalogTable)
		}
		val analyzedLogicalPlan = mixcal.analyzedLogicalPlan(parsedLogicalPlan)
		if (columnPermission) {
			ColumnPrivilegeChecker.intercept(analyzedLogicalPlan,
				tableIdentifierToCatalogTable, catalog, catalogSession)
		}
		val optimizedLogicalPlan = mixcal.optimizedLogicalPlan(analyzedLogicalPlan)



		val lastLogicalPlan = if (pushdown) {
			mixcal.furtherOptimizedLogicalPlan(optimizedLogicalPlan)
		} else optimizedLogicalPlan
		mixcal.treeToDF(lastLogicalPlan)
	}

	private def getCatalogTable(table: String, database: Option[String]): CatalogTable = {
		database match {
			case None =>
				catalog.getTable(catalogSession.databaseId, table)
			case Some(databaseName) =>
				val database = catalog.getDatabase(catalogSession.organizationId, databaseName)
				catalog.getTable(database.id.get, table)
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

	private def registerDataSourceTable(tableIdentifier: TableIdentifier, catalogTable: CatalogTable): Unit = {
		val props = catalogTable.properties.+("alias" -> tableIdentifier.table)
		val propsString = props.map { case (k, v) => s"$k '$v'" }.mkString(",")
		val typ = props("type")
		if (mixcal.sparkSession.sessionState.catalog.tableExists(tableIdentifier)) {
			mixcal.sparkSession.sessionState.catalog.dropTable(tableIdentifier, ignoreIfNotExists = true, purge = false)
		}
		val createTableSql = s"create table ${tableIdentifier.database.map(db => s"$db.${tableIdentifier.table}").getOrElse(tableIdentifier.table)} using ${DataSystemFactory.typeToSparkDatasource(typ)} options($propsString)"
		mixcal.sqlToDF(createTableSql)
	}
}

object MbSession extends MbLogging {
	def getMbSession(conf: MbConf): MbSession = new MbSession(conf)
}
