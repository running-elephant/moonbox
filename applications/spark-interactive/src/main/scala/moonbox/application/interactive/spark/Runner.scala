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

package moonbox.application.interactive.spark

import akka.actor.ActorRef
import akka.pattern._
import akka.util.Timeout
import moonbox.common.{MbConf, MbLogging}
import moonbox.core._
import moonbox.core.command._
import moonbox.core.datasys.{DataSystem, DataTable}
import moonbox.grid.deploy.DeployMessages._
import moonbox.grid.deploy.Interface.ResultData
import moonbox.grid.timer.EventEntity
import moonbox.protocol.util.SchemaUtil
import org.apache.spark.sql.catalyst.expressions.Literal
import org.apache.spark.sql.catalyst.plans.logical.{GlobalLimit, LocalLimit}
import org.apache.spark.sql.optimizer.WholePushdown
import org.apache.spark.sql.types.IntegerType
import org.apache.spark.sql.{DataFrame, Row, SaveMode}

import scala.collection.mutable.ArrayBuffer
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

class Runner(
	sessionId: String,
	username: String,
	database: Option[String],
	conf: MbConf,
	sessionConfig: Map[String, String],
	manager: ActorRef
) extends MbLogging {

	private implicit val askTimeout = Timeout(new FiniteDuration(10, SECONDS))
	private val awaitTimeout = new FiniteDuration(10, SECONDS)

	private var fetchSize: Int = _
	private var maxRows: Int = _
	private var currentData: Iterator[Row] = _
	private var currentSchema: String = _
	private var currentCallback: Option[() => Unit] = _

	private var currentRowId: Long = _

	private val mbSession = new MoonboxSession(conf, username, database, sessionConfig = sessionConfig)

	private implicit var userContext: SessionEnv = mbSession.sessionEnv

	def query(sqls: Seq[String], fetchSize: Int, maxRows: Int): QueryResult = {
		this.fetchSize = fetchSize
		this.maxRows = if (maxRows == Int.MinValue) {
			10000
		} else maxRows

		val commands = sqls.map(mbSession.parsedCommand)
		if (commands.count(_.isInstanceOf[MQLQuery]) > 1) {
			throw new Exception(s"Can only write an SELECT SQL statement at a time。")
		}
		commands.map {
			case event: CreateTimedEvent =>
				createTimedEvent(event, manager)
			case event: AlterTimedEventSetEnable =>
				alterTimedEvent(event, manager)
			case runnable: MbRunnableCommand =>
				val result = runnable.run(mbSession)
				DirectResult(runnable.outputSchema, result.map(_.toSeq))
			case CreateTempView(table, query, isCache, replaceIfExists) =>
				createTempView(table, query, isCache, replaceIfExists)
				DirectResult(SchemaUtil.emptyJsonSchema, Seq.empty)
			case InsertInto(MbTableIdentifier(table, db), query, colNames, num, insertMode) =>
				insert(table, db, query, colNames, num, insertMode)
				DirectResult(SchemaUtil.emptyJsonSchema, Seq.empty)
			case MQLQuery(sql) =>
				query(sql)
			case other: Statement =>
				otherStatement(other.sql)
			case other =>
				throw new Exception(s"Unsupport command $other")
		}.last
	}

	def cancel(): Unit = {
		logInfo(s"Cancel job group with job group id $sessionId")
		mbSession.engine.cancelJobGroup(sessionId)
	}

	private def createEventEntity(name: String, definer: String,
		expr: String, cmds: Seq[String], lang: String, desc: Option[String]): EventEntity = {
		EventEntity(
			group = userContext.organizationName,
			name = name,
			lang = lang,
			sqls = cmds,
			config = Map(), // TODO
			cronExpr = expr,
			definer = definer,
			start = None,
			end = None,
			desc = desc
		)
	}

	def alterTimedEvent(event: AlterTimedEventSetEnable, target: ActorRef): QueryResult = {
		val (schema, data) = if (event.enable) {
			val existsEvent = mbSession.catalog.getTimedEvent(userContext.organizationId, event.name)
			val catalogUser = mbSession.catalog.getUser(existsEvent.definer)
			val procedure = mbSession.catalog.getProcedure(existsEvent.procedure)
			val cmds = procedure.cmds
			val lang = procedure.lang
			val eventEntity = createEventEntity(event.name, catalogUser.name, existsEvent.schedule, cmds, lang, existsEvent.description)

			val response = target.ask(RegisterTimedEvent(eventEntity)).mapTo[RegisterTimedEventResponse].flatMap {
				case RegisteredTimedEvent(_) =>
					Future {
						(event.outputSchema, event.run(mbSession).map(_.toSeq))
					}
				case RegisterTimedEventFailed(message) =>
					throw new Exception(message)
			}
			Await.result(response, awaitTimeout)
		} else {
			val response = target.ask(UnregisterTimedEvent(userContext.organizationName, event.name))
				.mapTo[UnregisterTimedEventResponse].flatMap {
				case UnregisteredTimedEvent(_) =>
					Future {
						(event.outputSchema, event.run(mbSession).map(_.toSeq))
					}
				case UnregisterTimedEventFailed(message) =>
					throw new Exception(message)
			}
			Await.result(response, awaitTimeout)
		}
		DirectResult(schema, data)
	}

	private def createTimedEvent(event: CreateTimedEvent, target: ActorRef): QueryResult = {
		val (schema, data) = if (event.enable) {
			val definer = event.definer.getOrElse(userContext.userName)
			val procedure = mbSession.catalog.getProcedure(userContext.organizationId, event.proc)
			val cmds = procedure.cmds
			val language = procedure.lang
			val eventEntity = createEventEntity(event.name, definer, event.schedule, cmds, language, event.description)
			val response = target.ask(RegisterTimedEvent(eventEntity)).mapTo[RegisterTimedEventResponse].flatMap {
				case RegisteredTimedEvent(_) =>
					Future {
						(event.outputSchema, event.run(mbSession).map(_.toSeq))
					}
				case RegisterTimedEventFailed(message) =>
					throw new Exception(message)
			}
			Await.result(response, awaitTimeout)
		} else {
			(event.outputSchema, event.run(mbSession).map(_.toSeq))
		}
		DirectResult(schema, data)
	}

	private def query(sql: String): QueryResult = {
		setGroup()
		val analyzedPlan = mbSession.analyzedPlan(sql)
		val limitedPlan = GlobalLimit(Literal(maxRows, IntegerType), LocalLimit(Literal(maxRows, IntegerType), analyzedPlan))
		val optimized = mbSession.optimizedPlan(limitedPlan)
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
				if (mbSession.pushdown) {
					logWarning(s"Execute push down failed: ${e.getMessage}. Retry with out pushdown.")
					val plan = mbSession.pushdownPlan(optimized, pushdown = false)
					val dataFrame = mbSession.toDF(plan)
					initCurrentData(dataFrame)
				} else {
					throw e
				}
		}
	}

	private def otherStatement(sql: String): QueryResult = {
		val dataFrame = mbSession.engine.sparkSession.sql(sql)
		initCurrentData(dataFrame)
	}

	private def insert(table: String, db: Option[String], query: String, colNames: Seq[String], num: Option[Int], insertMode: InsertMode.Value): Unit = {
		val sinkCatalogTable = mbSession.getCatalogTable(table, db)
		val options = sinkCatalogTable.properties
		val format = DataSystem.lookupDataSource(options("type"))
		val saveMode = if (insertMode == InsertMode.Overwrite) SaveMode.Overwrite else SaveMode.Append
		val optimized = mbSession.optimizedPlan(query)
		try {
			doInsert(pushdownEnable = true)
		} catch {
			case e: TableInsertPrivilegeException =>
				throw e
			case e: ColumnSelectPrivilegeException =>
				throw e
			case e: Throwable =>
				if (mbSession.pushdown) {
					doInsert(pushdownEnable = false)
				} else {
					throw e
				}
		}

		def doInsert(pushdownEnable: Boolean): Unit = {
			val pushdownPlan = {
				if (pushdownEnable) {
					mbSession.pushdownPlan(optimized)
				} else {
					mbSession.pushdownPlan(optimized, pushdown = pushdownEnable)
				}
			}
			val dataFrame = TableInsertPrivilegeChecker.intercept(
				mbSession,
				sinkCatalogTable,
				mbSession.toDF(pushdownPlan))

			val coalesceDataFrame = if (colNames.isEmpty && !options.contains("partitionColumnNames") && num.nonEmpty) {
				dataFrame.coalesce(num.get)
			} else dataFrame

			val dataFrameWriter = coalesceDataFrame
				.write
				.format(format)
				.options(options)
				.partitionBy(colNames: _*)
				.mode(saveMode)
			if (insertMode == InsertMode.Merge) {
				dataFrameWriter.option("update", "true")
			}
			// TODO remove
			if (options.contains("partitionColumnNames")) {
				dataFrameWriter.partitionBy(options("partitionColumnNames").split(","): _*)
			}
			dataFrameWriter.save()
			clearGroup()
		}
	}

	private def createTempView(table: String, query: String, isCache: Boolean, replaceIfExists: Boolean): Unit = {
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
		logDebug(s"Fetching data from runner: fetchSize=$fetchSize, maxRows=$maxRows")
		var rowCount = 0
		val resultData = new ArrayBuffer[Seq[Any]](fetchSize)
		while (hasNext && rowCount < fetchSize) {
			resultData.append(currentData.next().toSeq)
			currentRowId += 1
			rowCount += 1
		}

		if (!hasNext) {
			clearGroup()
			currentData = null
			currentCallback.foreach(close => close())
			logInfo("close client connection in datatable.")
		}

		ResultData(sessionId, currentSchema, resultData, hasNext)
	}

	private def initCurrentData(dataTable: DataTable): QueryResult = {
		currentRowId = 0
		currentData = dataTable.iterator
		currentSchema = dataTable.schema.json
		currentCallback = Some(dataTable.close _)
		logInfo(s"Initialize current data: schema=$currentSchema")

		if (hasNext) {
			IndirectResult(currentSchema)
		} else {
			DirectResult(currentSchema, Seq.empty)
		}
	}

	private def initCurrentData(dataFrame: DataFrame): QueryResult = {
		currentRowId = 0
		currentData = dataFrame.collect().toIterator
		currentSchema = dataFrame.schema.json
		currentCallback = None
		logInfo(s"Initialize current data: schema=$currentSchema")

		if (hasNext) {
			IndirectResult(currentSchema)
		} else {
			DirectResult(currentSchema, Seq.empty)
		}
	}

	private def hasNext: Boolean = currentData.hasNext && currentRowId < maxRows

	private def setGroup(): Unit = {
		mbSession.engine.setJobGroup(sessionId, username)
	}

	private def clearGroup(): Unit = {
		mbSession.engine.clearJobGroup()
	}
}
