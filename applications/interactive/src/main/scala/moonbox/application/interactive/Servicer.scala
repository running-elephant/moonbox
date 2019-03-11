package moonbox.application.interactive

import akka.actor.ActorRef
import akka.pattern._
import akka.util.Timeout
import moonbox.common.MbLogging
import moonbox.core._
import moonbox.core.command._
import moonbox.core.datasys.{DataSystem, DataTable}
import moonbox.grid.deploy.DeployMessages._
import moonbox.grid.deploy.Interface.ResultData
import moonbox.grid.deploy.messages.Message._
import moonbox.grid.timer.EventEntity
import moonbox.protocol.util.SchemaUtil
import org.apache.spark.sql.catalyst.{FunctionIdentifier, TableIdentifier}
import org.apache.spark.sql.catalyst.expressions.Literal
import org.apache.spark.sql.catalyst.plans.logical.{GlobalLimit, LocalLimit, LogicalPlan}
import org.apache.spark.sql.optimizer.WholePushdown
import org.apache.spark.sql.sqlbuilder.{MbDialect, MbSqlBuilder}
import org.apache.spark.sql.types.IntegerType
import org.apache.spark.sql.{DataFrame, Row, SaveMode}

import scala.collection.mutable.ArrayBuffer
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

class Servicer(
	username: String,
	database: Option[String],
	mbSession: MbSession,
	manager: ActorRef
) extends MbLogging {

	private implicit var userContext: UserContext = _

	init()

	private def init(): Unit = {
		mbSession.bindUser(username, database, autoLoadDatabases = false)
		userContext = mbSession.userContext
	}

	private def iteratorToSeq(iter: Iterator[Row]): Seq[Row] = {
		val buf = new ArrayBuffer[Row]()
		iter.foreach(row => buf.append(row))
		buf
	}

	def sample(sql: String): SampleResponse = {
		var optimized: LogicalPlan = null
		try {
			val analyzedPlan = mbSession.analyzedPlan(sql)
			val limitedPlan = GlobalLimit(Literal(20, IntegerType), LocalLimit(Literal(20, IntegerType), analyzedPlan))
			optimized = mbSession.optimizedPlan(limitedPlan)
			mbSession.pushdownPlan(optimized) match {
				case WholePushdown(child, queryable) =>
					val dataTable = mbSession.toDT(child, queryable)
					SampleSuccessed(dataTable.schema.json, iteratorToSeq(dataTable.iter).map(_.toSeq))
				case plan =>
					val dataFrame = mbSession.toDF(plan)
					SampleSuccessed(dataFrame.schema.json, iteratorToSeq(dataFrame.collect().iterator).map(_.toSeq))
			}
		} catch {
			case e: ColumnSelectPrivilegeException =>
				SampleFailed(e.getMessage)
			case e: Throwable if e.getMessage.contains("cancelled job") =>
				SampleFailed(e.getMessage)
			case e: Throwable =>
				if (mbSession.pushdown) {
					logWarning(s"Execute push down failed: ${e.getMessage}. Retry with out pushdown.")
					val plan = mbSession.pushdownPlan(optimized, pushdown = false)
					val dataFrame = mbSession.toDF(plan)
					SampleSuccessed(dataFrame.schema.json, iteratorToSeq(dataFrame.collect().iterator).map(_.toSeq))
				} else {
					SampleFailed(e.getMessage)
				}
		}
	}

	def translate(sql: String, dialect: String): TranslateResponse = {
		try {
			val analyzedPlan = mbSession.analyzedPlan(sql)
			val optimizedPlan = mbSession.optimizedPlan(analyzedPlan)
			val resultSql = new MbSqlBuilder(optimizedPlan, MbDialect.get(dialect)).toSQL
			TranslateResponse(success = true, sql = Some(resultSql))
		} catch {
			case e: Throwable =>
				TranslateResponse(success = false, message = Some(e.getMessage))
		}
	}

	def verify(sqls: Seq[String]): VerifyResponse = {
		val result = sqls.map { sql =>
			try {
				val analyzedPlan = mbSession.analyzedPlan(sql)
				mbSession.checkColumnPrivilege(analyzedPlan)
				(true, None)
			} catch {
				case e: ColumnSelectPrivilegeException =>
					(false, Some(e.getMessage))
				case e: Exception =>
					(false, Some(e.getMessage))
			}
		}
		VerifyResponse(success = true, result = Some(result))
	}

	def resources(sql: String): TableResourcesResponse = {
		try {
			val parsedPlan: LogicalPlan = mbSession.parsedPlan(sql)
			val (tables, functions) = mbSession.collectUnknownTablesAndFunctions(parsedPlan)
			TableResourcesSuccessed(tables.map(_.table), functions.map(_.funcName))
		} catch {
			case e: Exception =>
				TableResourcesFailed(e.getMessage)
		}
	}

	def schema(sql: String): SchemaResponse = {
		try {
			val schema = mbSession.analyzedPlan(sql).schema.json
			SchemaSuccessed(schema)
		} catch {
			case e: Exception =>
				SchemaFailed(e.getMessage)
		}
	}

	def lineage(sql: String): LineageResponse = {
		LineageFailed("not support yet")
	}
}
