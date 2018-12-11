package moonbox.application.interactive

import moonbox.common.MbLogging
import moonbox.core._
import moonbox.core.command.{CreateTempView, InsertInto, MQLQuery, MbRunnableCommand}
import moonbox.core.datasys.DataSystem
import org.apache.spark.sql.{Row, SaveMode}
import org.apache.spark.sql.optimizer.WholePushdown

class Runner(
	sessionId: String,
	username: String,
	database: Option[String],
	mbSession: MbSession) extends MbLogging {

	private var fetchSize: Long = _
	private var maxRows: Long = _
	private var currentData: Iterator[Row] = _

	init()

	def query(sqls: Seq[String], fetchSize: Long, maxRows: Long): Unit = {
		this.fetchSize = fetchSize
		this.maxRows = maxRows

		sqls.map(mbSession.parsedPlan).map {
			case runnable: MbRunnableCommand =>
				runnable.run(mbSession)(mbSession.userContext)

			case createTempView @ CreateTempView(table, query, isCache, replaceIfExists) =>
				doCreateTempView(table, query, isCache, replaceIfExists)

			case insert @ InsertInto(MbTableIdentifier(table, db), query, overwrite) =>
				doInsert(table, db, query, overwrite)

			case query @ MQLQuery(sql) =>
				doMqlQuery(sql)
			case other =>
				throw new Exception(s"Unsupport command $other")

		}
	}

	def cancel(): Unit = {
		logInfo(s"Cancel job group with job group id $sessionId")
		mbSession.mixcal.cancelJobGroup(sessionId)
	}

	private def doMqlQuery(sql: String): Unit = {
		val optimized = mbSession.optimizedPlan(sql)
		try {
			mbSession.pushdownPlan(optimized) match {
				case WholePushdown(child, queryable) =>
					mbSession.toDT(child, queryable)
				case plan =>
					mbSession.toDF(plan)
			}
		} catch {
			case e: ColumnSelectPrivilegeException =>
				throw e
			case e: Throwable if e.getMessage.contains("cancelled job") =>
				throw e
			case e: Throwable =>
				logWarning(s"Execute push down failed: ${e.getMessage}. Retry with out pushdown.")
				val plan = mbSession.pushdownPlan(optimized, pushdown = false)
				mbSession.toDF(plan)
		}
	}

	private def doInsert(table: String, db: Option[String], query: String, overwrite: Boolean): Unit = {
		val sinkCatalogTable = mbSession.getCatalogTable(table, db)
		val options = sinkCatalogTable.properties
		val format = DataSystem.lookupDataSource(options("type"))
		val saveMode = if (overwrite) SaveMode.Overwrite else SaveMode.Append
		val optimized = mbSession.optimizedPlan(query)
		try {
			run(pushdownEnable = true)
		} catch {
			case e: TableInsertPrivilegeException =>
				throw e
			case e: ColumnSelectPrivilegeException =>
				throw e
			case e: Throwable =>
				run(pushdownEnable = false)
		}

		def run(pushdownEnable: Boolean): Unit = {
			val pushdownPlan = {
				if (pushdownEnable) {
					mbSession.pushdownPlan(optimized)
				} else {
					mbSession.pushdownPlan(optimized, pushdown = pushdownEnable)
				}
			}
			val dataFrame = mbSession.toDF(pushdownPlan)
			val dataFrameWriter = TableInsertPrivilegeChecker
				.intercept(mbSession, sinkCatalogTable, dataFrame)
				.write
				.format(format)
				.options(options)
				.mode(saveMode)
			if (options.contains("partitionColumnNames")) {
				dataFrameWriter.partitionBy(options("partitionColumnNames").split(","): _*)
			}
			dataFrameWriter.save()
		}
	}

	private def doCreateTempView(table: String, query: String, isCache: Boolean, replaceIfExists: Boolean): Unit = {
		val optimized = mbSession.optimizedPlan(query)
		val df = mbSession.toDF(optimized)
		if (isCache) {
			df.cache()
		}
		if (replaceIfExists) {
			df.createOrReplaceTempView(table)
		} else {
			df.createTempView(table)
		}
	}




	private def init(): Unit = {
		mbSession.bindUser(username, database)
		mbSession.mixcal.setJobGroup(sessionId)
	}
}
