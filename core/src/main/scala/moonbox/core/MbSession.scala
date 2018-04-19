package moonbox.core


import moonbox.common.{MbConf, MbLogging}
import moonbox.core.catalog.{CatalogSession, CatalogTable}
import moonbox.core.command._
import moonbox.core.config._
import org.apache.spark.sql.catalyst.TableIdentifier
import org.apache.spark.sql.catalyst.catalog.{CatalogTableType, CatalogTable => SparkCatalogTable}
import org.apache.spark.sql.catalyst.analysis.UnresolvedRelation
import org.apache.spark.sql.catalyst.expressions.{Exists, Expression, ListQuery, ScalarSubquery}
import org.apache.spark.sql.catalyst.plans.logical.{Filter, LogicalPlan, SubqueryAlias, With}
import org.apache.spark.sql.datasys.DataSystemFactory
import org.apache.spark.sql.execution.datasources.DataSource
import org.apache.spark.sql.pruner.MbPruner
import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.{DataFrame, MixcalContext, SaveMode}

import scala.collection.mutable

class MbSession(conf: MbConf) extends MbLogging {
	implicit var catalogSession: CatalogSession = _
	private val pushdown = conf.get(MIXCAL_PUSHDOWN_ENABLE.key, MIXCAL_PUSHDOWN_ENABLE.defaultValue.get)
	private val columnPermission = conf.get(MIXCAL_COLUMN_PERMISSION_ENABLE.key, MIXCAL_COLUMN_PERMISSION_ENABLE.defaultValue.get)

	val catalog = new CatalogContext(conf)
	val mixcal = new MixcalContext(conf)

	private lazy val mbPruner = new MbPruner(this)


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
		this
	}

	def cancelJob(jobId: String): Unit = {
		mixcal.sparkSession.sparkContext.cancelJobGroup(jobId)
	}

	def execute(jobId: String, cmds: Seq[MbCommand]): Any = {
		cmds.map{c => execute(jobId, c)}.last
	}

	def execute(jobId: String, cmd: MbCommand): Any = {
		PrivilegeChecker.intercept(cmd, catalog, catalogSession) match {
			case false => throw new Exception("Permission denied.")
			case true =>
				cmd match {
					case runnable: MbRunnableCommand => // direct
						runnable.run(this)
					case createTempView: CreateTempView =>
						val df = sql(createTempView.query)
						if (createTempView.isCache) {
							df.cache()
						}
						if (createTempView.replaceIfExists) {
							df.createOrReplaceTempView(createTempView.name)
						} else {
							df.createTempView(createTempView.name)
						}
					case mbQuery: MQLQuery => // cached
						try {
							sql(mbQuery.query).write
								.format("org.apache.spark.sql.execution.datasources.redis")
								.option("jobId", jobId)
								.options(conf.getAll.filter(_._1.startsWith("moonbox.cache.")))
								.save()
						} catch {
							// TODO fallback call sql() with pushdown parameter false
							case e: Exception =>
								mixcal.sqlToDF(mbQuery.query).write
									.format("org.apache.spark.sql.execution.datasources.redis")
									.option("jobId", jobId)
									.options(conf.getAll.filter(_._1.startsWith("moonbox.cache.")))
									.save()
						}
						jobId
					case insert: InsertInto => // external
						val options = getCatalogTable(insert.table.table, insert.table.database).properties
						try {
							sql(insert.query).write.format(options("type"))
								.options(options)
								.mode(SaveMode.Append)
								.save()
						} catch {
							case e: Exception =>
								mixcal.sqlToDF(insert.query).write.format(options("type"))
									.options(options)
									.mode(SaveMode.Append)
									.save()
						}
					case _ => throw new Exception("Unsupported command.")
				}
		}
	}

	def sql(sqlText: String): DataFrame = {
		val parsedLogicalPlan = mixcal.parsedLogicalPlan(sqlText)
		val prunedLogicalPlan = if (columnPermission) {
			mbPruner.execute(parsedLogicalPlan)
		} else parsedLogicalPlan
		registerTable(parsedLogicalPlan)
		val analyzedLogicalPlan = mixcal.analyzedLogicalPlan(prunedLogicalPlan)
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

	private def registerTable(plan: LogicalPlan): Unit = {
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
				case _ =>
			}
		}
		traverseAll(plan)

		tables.diff(logicalTables).foreach { tableIdentifier =>
			val catalogTable = getCatalogTable(tableIdentifier.table, tableIdentifier.database)
			val props = catalogTable.properties.+("alias" -> tableIdentifier.table)
			val typ = props("type")
			val storage = DataSource.buildStorageFormatFromOptions(props)
			val tableType = if (storage.locationUri.isDefined) {
				CatalogTableType.EXTERNAL
			} else {
				CatalogTableType.MANAGED
			}
			val tableDesc = SparkCatalogTable(
				identifier = TableIdentifier(tableIdentifier.table, tableIdentifier.database),
				tableType = tableType,
				storage = storage,
				schema = new StructType,
				provider = Some(DataSystemFactory.typeToSparkDatasource(typ))
			)
			if (mixcal.sparkSession.sessionState.catalog.tableExists(tableIdentifier)) {
				mixcal.sparkSession.sessionState.catalog.alterTable(tableDesc)
			} else {
				mixcal.sparkSession.sessionState.catalog.createTable(tableDesc, ignoreIfExists = true)
			}
		}
	}

}

object MbSession extends MbLogging {
	def getMbSession(conf: MbConf): MbSession = new MbSession(conf)
}
