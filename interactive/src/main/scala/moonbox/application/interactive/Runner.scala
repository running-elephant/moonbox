package moonbox.application.interactive

import moonbox.common.MbLogging
import moonbox.core._
import moonbox.core.command.{CreateTempView, InsertInto, MbRunnableCommand}
import moonbox.core.datasys.DataSystem
import org.apache.spark.sql.SaveMode

class Runner(
	sessionId: String,
	username: String,
	database: Option[String],
	mbSession: MbSession) extends MbLogging {

	init()

	def query(sqls: Seq[String], fetchSize: Long, maxRows: Long): Unit = {
		sqls.map(mbSession.parsedPlan).map {
			case runnable: MbRunnableCommand =>
				runnable.run(mbSession)(mbSession.userContext)

			case createTempView @ CreateTempView(table, query, isCache, replaceIfExists) =>
				doCreateTempView(table, query, isCache, replaceIfExists)

			case insert @ InsertInto(MbTableIdentifier(table, db), query, overwrite) =>
				doInsert(table, db, query, overwrite)

			case other =>
				throw new Exception(s"Unsupport command $other")

		}
	}

	def cancel(): Unit = {
		logInfo(s"Cancel job group with job group id $sessionId")
		mbSession.mixcal.cancelJobGroup(sessionId)
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

	private def doInsert(table: String, db: Option[String], query: String, overwrite: Boolean): Unit = {
		val sinkCatalogTable = mbSession.getCatalogTable(table, db)
		val options = sinkCatalogTable.properties
		val format = DataSystem.lookupDataSource(options("type"))
		val saveMode = if (overwrite) SaveMode.Overwrite else SaveMode.Append
		val optimized = mbSession.optimizedPlan(query)
		try {
			run(pushdown = true)
		} catch {
			case e: TableInsertPrivilegeException =>
				throw e
			case e: ColumnSelectPrivilegeException =>
				throw e
			case e: Throwable =>
				run(pushdown = false)
		}

		def run(pushdown: Boolean): Unit = {
			val pushdownPlan = {
				if (pushdown) {
					mbSession.pushdownPlan(optimized)
				} else {
					mbSession.pushdownPlan(optimized, pushdown = false)
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


	private def init(): Unit = {
		mbSession.bindUser(username, database)
		mbSession.mixcal.setJobGroup(sessionId)
	}
}
