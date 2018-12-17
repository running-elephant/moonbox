package moonbox.application.interactive

import moonbox.common.MbLogging
import moonbox.core._
import moonbox.core.command.{CreateTempView, InsertInto, MQLQuery, MbRunnableCommand}
import moonbox.core.datasys.{DataSystem, DataTable}
import moonbox.protocol.client.ResultData
import moonbox.protocol.util.SchemaUtil
import org.apache.spark.sql.optimizer.WholePushdown
import org.apache.spark.sql.{DataFrame, Row, SaveMode}

import scala.collection.mutable.ArrayBuffer

class Runner(
	sessionId: String,
	username: String,
	database: Option[String],
	mbSession: MbSession) extends MbLogging {

	private var fetchSize: Long = _
	private var maxRows: Long = _
	private var currentData: Iterator[Row] = _
	private var currentSchema: String = _
	private var currentRowId: Long = _

	init()

	def query(sqls: Seq[String], fetchSize: Long, maxRows: Long): QueryResult = {
		this.fetchSize = fetchSize
		this.maxRows = maxRows
		sqls.map(mbSession.parsedPlan).map {
			case runnable: MbRunnableCommand =>
				val result = runnable.run(mbSession)(mbSession.userContext)
				DirectResult(runnable.outputSchema, result.map(_.toSeq))
			case createTempView@CreateTempView(table, query, isCache, replaceIfExists) =>
				doCreateTempView(table, query, isCache, replaceIfExists)
				DirectResult(SchemaUtil.emptyJsonSchema, Seq.empty)
			case insert@InsertInto(MbTableIdentifier(table, db), query, colNames, overwrite) =>
				doInsert(table, db, query, colNames, overwrite)
				DirectResult(SchemaUtil.emptyJsonSchema, Seq.empty)
			case query@MQLQuery(sql) =>
				doMqlQuery(sql)
			case other =>
				throw new Exception(s"Unsupport command $other")
		}.last
	}

	def cancel(): Unit = {
		logInfo(s"Cancel job group with job group id $sessionId")
		mbSession.mixcal.cancelJobGroup(sessionId)
	}

	private def doMqlQuery(sql: String): QueryResult = {
		val optimized = mbSession.optimizedPlan(sql)
		try {
			mbSession.pushdownPlan(optimized) match {
				case WholePushdown(child, queryable) =>
					val dataTable = mbSession.toDT(child, queryable)
					initCurrentData(dataTable)
				case plan =>
					val dataFrame = mbSession.toDF(plan)
					initCurrentData(dataFrame)
			}
		} catch {
			case e: ColumnSelectPrivilegeException =>
				throw e
			case e: Throwable if e.getMessage.contains("cancelled job") =>
				throw e
			case e: Throwable =>
				logWarning(s"Execute push down failed: ${e.getMessage}. Retry with out pushdown.")
				val plan = mbSession.pushdownPlan(optimized, pushdown = false)
				val dataFrame = mbSession.toDF(plan)
				initCurrentData(dataFrame)
		}
	}

	private def doInsert(table: String, db: Option[String], query: String, colNames: Seq[String], overwrite: Boolean): Unit = {
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
				.partitionBy(colNames:_*)
				.mode(saveMode)
			// TODO remove
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

	def fetchResultData(): ResultData = {
		logInfo(s"Fetching data from runner: fetchSize=$fetchSize, maxRows=$maxRows")
		var data: ArrayBuffer[Seq[Any]] = new ArrayBuffer[Seq[Any]]
		var rowCount = 0
		while (currentData.hasNext && currentRowId < maxRows && rowCount < fetchSize) {
			data += currentData.next().toSeq
			currentRowId += 1
			rowCount += 1
		}
		ResultData(sessionId, currentSchema, data, hasNext)
	}

	private def initCurrentData(dataTable: DataTable): QueryResult = {
		currentRowId = 0
		currentData = dataTable.iter
		currentSchema = dataTable.schema.json
		logInfo(s"Initialize current data: schema=$currentSchema")

		if (hasNext) {
			// TODO:
			IndirectResult(currentSchema)
		} else {
			DirectResult(currentSchema, Seq.empty)
		}
	}

	private def initCurrentData(dataFrame: DataFrame): QueryResult = {
		currentRowId = 0
		currentData = dataFrame.collect().toIterator
		currentSchema = dataFrame.schema.json
		logInfo(s"Initialize current data: schema=$currentSchema")

		if (hasNext) {
			IndirectResult(currentSchema)
		} else {
			DirectResult(currentSchema, Seq.empty)
		}
	}

	private def hasNext: Boolean = currentData.hasNext && currentRowId < maxRows

	private def init(): Unit = {
		mbSession.bindUser(username, database)
		mbSession.mixcal.setJobGroup(sessionId)
	}
}
