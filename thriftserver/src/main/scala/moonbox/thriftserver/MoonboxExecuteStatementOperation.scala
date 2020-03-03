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

package moonbox.thriftserver

import java.sql.{Connection, JDBCType, ResultSet, Statement}
import java.util.{Locale, Map => JMap}

import moonbox.common.MbLogging
import org.apache.hadoop.hive.metastore.api.FieldSchema
import org.apache.hive.service.cli._
import org.apache.hive.service.cli.operation.ExecuteStatementOperation
import org.apache.hive.service.cli.session.HiveSession

import scala.collection.JavaConversions._

class MoonboxExecuteStatementOperation(var connection: Connection,
                                       parentSession: HiveSession,
                                       statement: String,
                                       confOverlay: JMap[String, String],
                                       runInBackground: Boolean = false)
  extends ExecuteStatementOperation(parentSession, statement, confOverlay, runInBackground) with MbLogging {

  private var moonboxResultSet: ResultSet = _
  private var database: String = _
  private var jdbcStatement: Statement = _
  private val ignoreHiveSqls = Seq()
  private val ignorePrefixes = Seq(
    "SET"
  )

  private var resultSchema: TableSchema = _

  override def close(): Unit = {
    logDebug(s"Closing statement operation: statement=[$statement]")
    cleanup(OperationState.CLOSED)
  }

  override def getNextRowSet(order: FetchOrientation, maxRowsL: Long): RowSet = {
    validateDefaultFetchOrientation(order)
    assertState(OperationState.FINISHED)
    val resultRowSet: RowSet = RowSetFactory.create(getResultSetSchema, getProtocolVersion)
    var count: Long = 0
    while (count < maxRowsL && moonboxResultSet != null && moonboxResultSet.next()) {
      val row = moonboxResultSet.next()
      resultRowSet.addRow(row.asInstanceOf[Array[AnyRef]])
      count += 1
    }
    resultRowSet
  }

  override def getResultSetSchema: TableSchema = resultSchema

  private def mbDescStatement(statement: String): String = {
    val unChange = Seq(
      "DESC TABLE",
      "DESCRIBE TABLE",
      "DESC DATABASE",
      "DESCRIBE DATABASE",
      "DESC FUNCTION",
      "DESCRIBE FUNCTION"
    )
    val descSchema = Seq(
      "DESC SCHEMA",
      "DESCRIBE SCHEMA"
    )
    val mbDescStatement = if (unChange.exists(statement.startsWith)) {
      statement
    } else if (descSchema.exists(statement.startsWith)) {
      schemaToDatabase(statement)
    } else {
      if (statement.startsWith("DESC")) {
        "DESC TABLE " + statement.stripPrefix("DESC").trim
      } else {
        "DESCRIBE TABLE " + statement.stripPrefix("DESCRIBE").trim
      }
    }
    stripPrime(mbDescStatement)
  }

  private def schemaToDatabase(statement: String) = {
    val splitter = "DESCRIBE SCHEMA".length
    val (front, later) = statement.splitAt(splitter)
    front.replace("SCHEMA", "DATABASE") + later
  }

  private def stripPrime(statement: String): String = {
    statement.replaceAll("`", "")
  }

  override def runInternal(): Unit = {
    logInfo(s"Received statement: '$statement'")
    setState(OperationState.RUNNING)
    setHasResultSet(true)
    val upperStatement = statement.trim.toUpperCase(Locale.ROOT)
    if (!ignoreHiveSqls.contains(upperStatement) && !ignorePrefixes.exists(upperStatement.startsWith)) {
      if (upperStatement.startsWith("DESC ")) {
        handleDescStatement(mbDescStatement(upperStatement))
      } else if (upperStatement.startsWith("SHOW SCHEMAS") || upperStatement.startsWith("SHOW DATABASES")) {
        /* show databases */
        logInfo("Convert 'SHOW SCHEMAS' to 'SHOW DATABASES'")
        handleShowStatement("SHOW DATABASES", isDatabases = true)
      } else if (upperStatement.startsWith("SHOW TABLES")) {
        /* show tables */
        val mbStatement = stripPrime(statement)
        database = mbStatement.toUpperCase(Locale.ROOT).stripPrefix("SHOW TABLES IN").trim
        handleShowStatement(mbStatement, isDatabases = false)
      } else {
        /* handle query sql */
        moonboxResultSet = doMoonboxQuery(statement)
        resultSchema = convert2HiveSchema(moonboxResultSet)
      }
    } else {
      /* Ignore unsupported commands. */
      logInfo(s"Ignored statement: '$statement'.")
    }
    setState(OperationState.FINISHED)
  }

  private def handleShowStatement(mbStatement: String, isDatabases: Boolean): Unit = {
    val result = doMoonboxQuery(mbStatement)
    if (!isDatabases) {
      val schema =
        """
          |{
          |  "type": "struct",
          |  "fields": [
          |     {
          |       "name": "database",
          |       "type": "string",
          |       "nullable": "false"
          |     },
          |     {
          |       "name": "tableName",
          |       "type": "string",
          |       "nullable": "false"
          |     },
          |     {
          |       "name": "isTemporary",
          |       "type": "boolean",
          |       "nullable": "false"
          |     }
          |  ]
          |}
        """.stripMargin

      val tableSchema = new TableSchema()
      tableSchema.addStringColumn("database", "database name")
      tableSchema.addStringColumn("tableName", "table name")
      tableSchema.addPrimitiveColumn("isTemporary", Type.BOOLEAN_TYPE, "temporary")

      resultSchema = tableSchema
    } else {
      resultSchema = convert2HiveSchema(result)
    }
    moonboxResultSet = result
  }

  private def handleDescStatement(mbStatement: String): Unit = {
    val result = doMoonboxQuery(mbStatement)
    val schema =
      """
        |{
        |  "type": "struct",
        |  "fields": [
        |     {
        |       "name": "col_name",
        |       "type": "string",
        |       "nullable": "false"
        |     },
        |     {
        |       "name": "data_type",
        |       "type": "string",
        |       "nullable": "false"
        |     },
        |     {
        |       "name": "comment",
        |       "type": "string",
        |       "nullable": "true"
        |     }
        |  ]
        |}
      """.stripMargin
    val tableSchema = new TableSchema()
    tableSchema.addStringColumn("col_name", "column name")
    tableSchema.addStringColumn("data_type", "datatype")
    tableSchema.addStringColumn("comment", "comment")

    resultSchema = tableSchema
    moonboxResultSet = result
  }


  /* do moonbox query */
  private def doMoonboxQuery(mbStatement: String = statement): ResultSet = {
    try {
      jdbcStatement = connection.createStatement()
      jdbcStatement.executeQuery(mbStatement)
    } catch {
      case e: Exception =>
        throw new HiveSQLException(e)
    }
  }

  private def convert2HiveSchema(moonboxResultSet: ResultSet): TableSchema = {
    val metaData = moonboxResultSet.getMetaData
    if (moonboxResultSet == null || metaData.getColumnCount == 0) {
      new TableSchema(Seq(new FieldSchema("Result", "string", "")))
    } else {
      val fieldSchemas = (1 to metaData.getColumnCount).map {
        index =>
          val name = metaData.getColumnName(index)
          val hiveType = MoonboxExecuteStatementOperation.toHiveType(JDBCType.valueOf(metaData.getColumnType(index)))
          new FieldSchema(name, hiveType, "")
      }
      new TableSchema(fieldSchemas)
    }
  }

  override def cancel(): Unit = {
    try {
      jdbcStatement.cancel()
      logInfo("Cancel query successfully.")
    } catch {
      case ex: Exception =>
        logError("Cancel query failed", ex)
    }
    cleanup(OperationState.CANCELED)
  }

  private def cleanup(state: OperationState) {
    setState(state)
    if (runInBackground) {
      val backgroundHandle = getBackgroundHandle
      if (backgroundHandle != null) {
        backgroundHandle.cancel(true)
      }
    }
  }
}

object MoonboxExecuteStatementOperation {
  def toHiveType(dataType: JDBCType): String = {
    dataType match {
      case JDBCType.NULL => "void"
      case JDBCType.INTEGER => "int"
      case JDBCType.JAVA_OBJECT => "map"
      case JDBCType.DECIMAL => "decimal(38,18)"
      case _ => dataType.getName
    }
  }
}
