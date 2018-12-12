package moonbox.application.interactive

import moonbox.common.MbLogging
import moonbox.core._
import moonbox.core.command.{CreateTempView, InsertInto, MQLQuery, MbRunnableCommand}
import moonbox.core.datasys.{DataSystem, DataTable}
import moonbox.protocol.client.ResultData
import org.apache.spark.sql.optimizer.WholePushdown
import org.apache.spark.sql.{DataFrame, Row, SaveMode}

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
				IndirectResult()
			case insert@InsertInto(MbTableIdentifier(table, db), query, overwrite) =>
				doInsert(table, db, query, overwrite)
				IndirectResult()
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
					DirectResult(dataTable.schema.json, Seq.empty, hasMore = true)
				case plan =>
					val dataFrame = mbSession.toDF(plan)
					initCurrentData(dataFrame)
					DirectResult(dataFrame.schema.json, Seq.empty, hasMore = true)
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
				DirectResult(dataFrame.schema.json, Seq.empty, hasMore = true)
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

	def fetchData(): Seq[Seq[Any]] = {
		// TODO: useless
		Seq.empty[Row].map(_.toSeq)
	}

	def fetchResultData(): ResultData = {
		logInfo(s"Fetching data from runner: fetchSize=$fetchSize, maxRows=$maxRows")
		var data: Seq[Seq[Any]] = Seq.empty[Seq[Any]]
		var rowCount = 0
		while (currentData.hasNext && currentRowId < maxRows && rowCount < fetchSize) {
			data :+= currentData.next().toSeq
			currentRowId += 1
			rowCount += 1
		}
		ResultData(sessionId, currentSchema, data, hasNext)
	}

	private def initCurrentData(dataTable: DataTable): Unit = {
		currentRowId = 0
		currentData = dataTable.iter
		currentSchema = dataTable.schema.json
		logInfo(s"Initialize current data: schema=$currentSchema")
	}

	private def initCurrentData(dataFrame: DataFrame): Unit = {
		currentRowId = 0
		currentData = dataFrame.collect().toIterator
		currentSchema = dataFrame.schema.json
		logInfo(s"Initialize current data: schema=$currentSchema")
	}

	private def hasNext: Boolean = currentData.hasNext && currentRowId < maxRows

	private def init(): Unit = {
		mbSession.bindUser(username, database)
		mbSession.mixcal.setJobGroup(sessionId)
	}
}
