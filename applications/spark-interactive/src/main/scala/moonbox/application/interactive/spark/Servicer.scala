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
import moonbox.common.{MbConf, MbLogging}
import moonbox.core._
import moonbox.grid.deploy.messages.Message._
import org.apache.spark.sql.Row
import org.apache.spark.sql.catalyst.expressions.Literal
import org.apache.spark.sql.catalyst.plans.logical.{GlobalLimit, InsertIntoTable, LocalLimit, LogicalPlan}
import org.apache.spark.sql.execution.datasources.LogicalRelation
import org.apache.spark.sql.optimizer.WholePushdown
import org.apache.spark.sql.sqlbuilder.{MbDialect, MbSqlBuilder}
import org.apache.spark.sql.types.IntegerType

import scala.collection.mutable.ArrayBuffer

class Servicer(
	org: String,
	username: String,
	database: Option[String],
	conf: MbConf,
	manager: ActorRef
) extends MbLogging {

	private val mbSession = new MoonboxSession(conf, username, org, database)


	private def iteratorToSeq(iter: Iterator[Row]): Seq[Row] = {
		val buf = new ArrayBuffer[Row]()
		iter.foreach(row => buf.append(row))
		buf
	}

	def sample(sql: String): SampleResponse = {
		try {
			val result = mbSession.sql(sql, 20)
			SampleSuccessed(result._2.json, iteratorToSeq(result._1).map(_.toSeq))
		} catch {
			case e: Exception =>
				SampleFailed(e.getMessage)
		}
	}

	def translate(sql: String): TranslateResponse = {
		try {
			val parsed = mbSession.engine.parsePlan(sql)
			val analyzed = mbSession.engine.analyzePlan(parsed)
			val connectionUrl = analyzed.collectLeaves().toList match {
				case Nil => throw new Exception("leaves can not be empty.")
				case head :: Nil =>
					head.asInstanceOf[LogicalRelation].catalogTable.get.storage.properties("url")
				case head :: tails =>
					val url = head.asInstanceOf[LogicalRelation].catalogTable.get.storage.properties("url")
					val sameDb = tails.forall { relation =>
						relation.asInstanceOf[LogicalRelation].catalogTable.get.storage.properties("url") == url
					}
					if (sameDb) {
						url
					} else {
						throw new Exception("tables are cross multiple database instance.")
					}
			}
			val optimizedPlan = mbSession.engine.optimizePlan(analyzed)
			val resultSql = new MbSqlBuilder(optimizedPlan, MbDialect.get(connectionUrl)).toSQL
			TranslateResponse(success = true, sql = Some(resultSql))
		} catch {
			case e: Throwable =>
				logError("translate error", e)
				TranslateResponse(success = false, message = Some(e.getMessage))
		}
	}

	def verify(sqls: Seq[String]): VerifyResponse = {
		val result = sqls.map { sql =>
			try {
				val parsed = mbSession.engine.parsePlan(sql)
				mbSession.engine.analyzePlan(parsed)
				(true, None)
			} catch {
				case e: Exception =>
					(false, Some(e.getMessage))
			}
		}
		VerifyResponse(success = true, result = Some(result))
	}

	def resources(sqls: Seq[String]): TableResourcesResponses = {
		val result = sqls.map { sql =>
			try {
				val (output, inputs) = mbSession.engine.unresolvedTablesInSQL(sql)
				val functions = mbSession.engine.unresolvedFunctionsInSQL(sql)

				TableResourcesSuccessed(
					inputs.map(_.unquotedString),
					output.map(_.unquotedString),
					functions.map(_.funcName))
			} catch {
				case e: Exception =>
					TableResourcesFailed(e.getMessage)
			}
		}
		TableResourcesResponses(success = true, result = Some(result))
	}

	def schema(sql: String): SchemaResponse = {
		try {
			val schema = mbSession.sqlSchema(sql)
			SchemaSuccessed(schema.json)
		} catch {
			case e: Exception =>
				SchemaFailed(e.getMessage)
		}
	}

	def lineage(sql: String): LineageResponse = {
		LineageFailed("not support yet")
	}

}
