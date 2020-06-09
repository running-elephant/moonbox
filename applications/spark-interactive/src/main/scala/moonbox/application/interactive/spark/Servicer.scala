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
import moonbox.grid.deploy.Interface.Dag
import moonbox.grid.deploy.messages.Message._
import org.apache.spark.sql.Row
import org.apache.spark.sql.catalyst.analysis.UnresolvedRelation
import org.apache.spark.sql.catalyst.plans.logical._
import org.apache.spark.sql.execution.datasources.{InsertIntoDataSourceCommand, InsertIntoHadoopFsRelationCommand, LogicalRelation}
import org.apache.spark.sql.hive.InsertIntoHiveTable
import org.apache.spark.sql.sqlbuilder.{MbDialect, MbSqlBuilder}
import org.json4s.DefaultFormats
import org.json4s.native.Serialization.write

import scala.collection.mutable.ArrayBuffer

class Servicer(
                org: String,
                username: String,
                database: Option[String],
                conf: MbConf,
                manager: ActorRef
              ) extends MbLogging {

  private val mbSession = new MoonboxSession(conf, org, username, database)

  private implicit val formats = DefaultFormats

  private def iteratorToSeq(iter: Iterator[Row]): Seq[Row] = {
    val buf = new ArrayBuffer[Row]()
    iter.foreach(row => buf.append(row))
    buf
  }

  def sample(sql: String): SampleResponse = {
    try {
      val result = mbSession.sql(sql.stripSuffix(";"), 20)
      SampleSuccessed(result._2.json, iteratorToSeq(result._1).map(_.toSeq))
    } catch {
      case e: Exception =>
        SampleFailed(e.getMessage)
    }
  }

  def translate(sql: String): TranslateResponse = {
    try {
      val parsed = mbSession.engine.parsePlan(sql.stripSuffix(";"))
      mbSession.engine.injectTableFunctions(parsed)
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
        val parsed = mbSession.engine.parsePlan(sql.stripSuffix(";"))
        mbSession.engine.injectTableFunctions(parsed)
        val analyzed = mbSession.engine.analyzePlan(parsed)
        mbSession.engine.checkAnalysis(analyzed)
        mbSession.engine.checkColumns(analyzed)
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
        val (output, inputs) = mbSession.engine.unresolvedTablesInSQL(sql.stripSuffix(";"))
        val functions = mbSession.engine.unresolvedFunctionsInSQL(sql.stripSuffix(";"))

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

  def schema(sqls: Seq[String], analyzable: Option[Boolean]): SchemaResponses = {
    val result = sqls.map {
      sql => {
        try {
          val schema = analyzable.getOrElse(true) match {
            case true =>
              mbSession.sqlSchema(sql.stripSuffix(";"))
            case false =>
              mbSession.unresolvedSqlSchema(sql.stripSuffix(";"))
          }
          val fields = schema.map(structField => SchemaField(structField.name, structField.dataType.simpleString, structField.nullable))
          SchemaSuccessed(schema.typeName, fields)
        } catch {
          case e: Exception =>
            SchemaFailed(e.getMessage)
        }
      }
    }
    SchemaResponses(success = true, result = Some(result))
  }

  def lineage(sqls: Seq[String], analyzable: Option[Boolean]): LineageResponse = {
    val lineage = analyzable.getOrElse(true) match {
      case true => sqls.map(sql => {
        val dag = sqlDag(sql.trim.stripSuffix(";"))
        Dag(write(dag.dag_table), write(dag.dag_col))
      })
      case false =>
        sqls.map(sql => {
          val dag = unresolvedSqlDag(sql.trim.stripSuffix(";"))
          Dag(write(dag.dag_table), write(dag.dag_col))
        })
    }
    LineageSuccessed(lineage)
  }

  /**
    * get resolved dag of sql
    *
    * @param sql sql
    * @return dag
    */
  private def sqlDag(sql: String): SqlDag = {
    val lineageBuilder = new LineageBuilder
    val parsedPlan = mbSession.engine.parsePlan(sql)
    var targetTableNodeId: Int = 0
    mbSession.engine.injectTableFunctions(parsedPlan)
    val analyzedPlan = mbSession.engine.analyzePlan(parsedPlan)
    val query = analyzedPlan match {
      case datasource: InsertIntoDataSourceCommand =>
        targetTableNodeId = lineageBuilder.genTableNode(datasource.logicalRelation.catalogTable.get)
        datasource.query
      case hadoopFs: InsertIntoHadoopFsRelationCommand =>
        targetTableNodeId = lineageBuilder.genTableNode(hadoopFs.catalogTable.get)
        hadoopFs.query
      case hive: InsertIntoHiveTable =>
	      targetTableNodeId = lineageBuilder.genTableNode(hive.table)
	      hive.query
      case _ =>
        throw new Exception("Lineage analysis is not supported for this sql")
    }
    queryDag(lineageBuilder, targetTableNodeId, query)
    SqlDag(dag_table = lineageBuilder.buildTableDag,
      dag_col = DagEntity(Nil, Nil))
  }

  /**
    * get unresolved dag of sql
    *
    * @param sql sql
    * @return dag
    */
  private def unresolvedSqlDag(sql: String): SqlDag = {
    val lineageBuilder = new LineageBuilder
    val parsedPlan = mbSession.engine.parsePlan(sql)
    var targetTableNodeId: Int = 0
    val query = parsedPlan match {
      case _@InsertIntoTable(plan, _, query, _, _) =>
        val relation = plan.asInstanceOf[UnresolvedRelation]
        targetTableNodeId = lineageBuilder.genTableNode(relation.tableIdentifier)
        query
      case _ =>
        throw new Exception("Lineage analysis is not supported for this sql")
    }
    unresolvedQueryDag(lineageBuilder, targetTableNodeId, query)
    SqlDag(dag_table = lineageBuilder.buildTableDag,
      dag_col = DagEntity(Nil, Nil))
  }

  def queryDag(lineageBuilder: LineageBuilder, targetTableNodeId: Int, logicalPlan: LogicalPlan): Unit = {

    def genDag(targetTableNodeId: Int, logicalPlan: LogicalPlan): Unit = {
      logicalPlan match {
        // can't adjust sequence
        case relation: LogicalRelation =>
          val sourceTableNodeId = lineageBuilder.genTableNode(relation.catalogTable.get)
          lineageBuilder.putTableNodeEdge(sourceTableNodeId, targetTableNodeId)
        case view: View =>
          val viewTableNodeId = lineageBuilder.genTableNode(view.desc)
          lineageBuilder.putTableNodeEdge(viewTableNodeId, targetTableNodeId)
          queryDag(lineageBuilder, viewTableNodeId, view.child)
        case unary: UnaryNode =>
          genDag(targetTableNodeId, unary.child)
        case binary: BinaryNode =>
          genDag(targetTableNodeId, binary.left)
          genDag(targetTableNodeId, binary.right)
        case _: LogicalPlan =>
      }
    }

    genDag(targetTableNodeId, logicalPlan)
  }

  def unresolvedQueryDag(lineageBuilder: LineageBuilder, targetTableNodeId: Int, logicalPlan: LogicalPlan): Unit = {

    def genDag(targetTableNodeId: Int, logicalPlan: LogicalPlan): Unit = {
      logicalPlan match {
        // can't adjust sequence
        case relation: UnresolvedRelation =>
          val sourceTableNodeId = lineageBuilder.genTableNode(relation.tableIdentifier)
          lineageBuilder.putTableNodeEdge(sourceTableNodeId, targetTableNodeId)
        //        case view: View =>
        //          val viewTableNodeId = lineageBuilder.genTableNode(view.desc)
        //          lineageBuilder.putTableNodeEdge(viewTableNodeId, targetTableNodeId)
        //          queryDag(lineageBuilder, viewTableNodeId, view.child)
        case unary: UnaryNode =>
          genDag(targetTableNodeId, unary.child)
        case binary: BinaryNode =>
          genDag(targetTableNodeId, binary.left)
          genDag(targetTableNodeId, binary.right)
        case _: LogicalPlan =>
      }
    }

    genDag(targetTableNodeId, logicalPlan)
  }
}
