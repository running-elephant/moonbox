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
import moonbox.core.MoonboxSession.DataResult
import moonbox.core._
import moonbox.core.command._
import moonbox.grid.deploy.DeployMessages._
import moonbox.grid.deploy.Interface.ResultData
import moonbox.grid.timer.EventEntity
import moonbox.protocol.util.SchemaUtil
import org.apache.spark.sql.Row
import scala.collection.mutable.ArrayBuffer
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

class Runner(
	sessionId: String,
	org: String,
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

	private var currentRowId: Long = _

	private val mbSession = new MoonboxSession(conf, org, username, database, sessionConfig)

	def query(sqls: Seq[String], fetchSize: Int, maxRows: Int): QueryResult = {
		this.fetchSize = fetchSize
		this.maxRows = if (maxRows == Int.MinValue) {
			100
		} else maxRows

		val commands = sqls.map(mbSession.parsedCommand)

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
			case other: Statement =>
				statement(other.sql)
			case other =>
				throw new Exception(s"Unsupport command $other")
		}.last
	}

	def cancel(): Unit = {
		logInfo(s"Cancel job group with job group id $sessionId")
		mbSession.engine.cancelJobGroup(sessionId)
	}

	private def createEventEntity(name: String, org: String, definer: String,
		expr: String, cmds: Seq[String], lang: String, desc: Option[String]): EventEntity = {
		EventEntity(
			group = mbSession.catalog.getCurrentOrg,
			name = name,
			lang = lang,
			sqls = cmds,
			config = Map(), // TODO
			cronExpr = expr,
			org = org,
			definer = definer,
			start = None,
			end = None,
			desc = desc
		)
	}

	private def alterTimedEvent(event: AlterTimedEventSetEnable, target: ActorRef): QueryResult = {
		val (schema, data) = if (event.enable) {
			val existsEvent = mbSession.catalog.getTimedEvent(event.name)
			val org = mbSession.catalog.getCurrentOrg
			val catalogUser = mbSession.catalog.getUser(org, existsEvent.definer)
			val procedure = mbSession.catalog.getProcedure(existsEvent.procedure)
			val sqls = procedure.sqls
			val lang = procedure.lang
			val eventEntity = createEventEntity(event.name, org, catalogUser.name, existsEvent.schedule, sqls, lang, existsEvent.description)

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
			val response = target.ask(UnregisterTimedEvent(mbSession.catalog.getCurrentOrg, event.name))
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
			val definer = event.definer.getOrElse(mbSession.catalog.getCurrentUser)
			val org = mbSession.catalog.getCurrentOrg
			val procedure = mbSession.catalog.getProcedure(event.proc)
			val cmds = procedure.sqls
			val language = procedure.lang
			val eventEntity = createEventEntity(event.name, org, definer, event.schedule, cmds, language, event.description)
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

	private def statement(sql: String): QueryResult = {
		val dataResult = mbSession.sql(sessionId, sql, maxRows)
		initCurrentData(dataResult)
	}


	private def createTempView(table: String, query: String, isCache: Boolean, replaceIfExists: Boolean): Unit = {
		val df = mbSession.engine.createDataFrame(query, prepared = false)
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
		logDebug(s"Fetching data from runner: row $currentRowId, fetchSize=$fetchSize")
		var rowCount = 0
		val resultData = new ArrayBuffer[Seq[Any]]()
		while (hasNext && rowCount < fetchSize) {
			resultData.append(currentData.next().toSeq)
			currentRowId += 1
			rowCount += 1
		}

		val continue = hasNext

		val data = ResultData(sessionId, currentSchema, resultData, continue)

		if (!continue) clear()

		data
	}

	private def initCurrentData(dataFrame: DataResult): QueryResult = {
		currentRowId = 0
		currentData = dataFrame._1
		currentSchema = dataFrame._2.json
		logInfo(s"Initialize current data: schema=$currentSchema")

		if (currentData.nonEmpty) {
			IndirectResult(currentSchema)
		} else {
			DirectResult(currentSchema, Seq.empty)
		}
	}

	private def hasNext: Boolean = currentData.hasNext && currentRowId < maxRows

	private def clear(): Unit = {
		currentData = null
		currentSchema = null
	}

}
