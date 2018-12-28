package moonbox.thriftserver

import java.util.{Locale, Map => JMap}

import moonbox.client.MoonboxClient
import moonbox.client.entity.{MoonboxRow, MoonboxRowSet}
import moonbox.client.exception.BackendException
import moonbox.common.MbLogging
import moonbox.protocol.DataType
import org.apache.hadoop.hive.metastore.api.FieldSchema
import org.apache.hive.service.cli._
import org.apache.hive.service.cli.operation.ExecuteStatementOperation
import org.apache.hive.service.cli.session.HiveSession

import scala.collection.JavaConverters._

class MoonboxExecuteStatementOperation(var client: MoonboxClient,
                                       parentSession: HiveSession,
                                       statement: String,
                                       confOverlay: JMap[String, String],
                                       runInBackground: Boolean = false)
  extends ExecuteStatementOperation(parentSession, statement, confOverlay, runInBackground) with MbLogging {

  private var moonboxRowSet: MoonboxRowSet = _
  private var database: String = _
  private val ignoreHiveSqls = Seq()
  private val ignorePrefixes = Seq(
    "SET"
  )

  private lazy val resultSchema: TableSchema = {
    if (moonboxRowSet == null || moonboxRowSet.isEmptySchema) {
      new TableSchema(Seq(new FieldSchema("Result", "string", "")).asJava)
    } else {
      val fieldSchemas = (0 until moonboxRowSet.columnCount).map { index =>
        val name = moonboxRowSet.columnName(index)
        val hiveType = MoonboxExecuteStatementOperation.toHiveType(moonboxRowSet.columnDataType(index))
        new FieldSchema(name, hiveType, "")
      }
      new TableSchema(fieldSchemas.asJava)
    }
  }

  override def close(): Unit = {
    logDebug(s"Closing statement operation: statement=[$statement]")
    cleanup(OperationState.CLOSED)
  }

  override def getNextRowSet(order: FetchOrientation, maxRowsL: Long): RowSet = {
    validateDefaultFetchOrientation(order)
    assertState(OperationState.FINISHED)
    val resultRowSet: RowSet = RowSetFactory.create(getResultSetSchema, getProtocolVersion)
    var count: Long = 0
    while (count < maxRowsL && moonboxRowSet != null && moonboxRowSet.hasNext) {
      val row = moonboxRowSet.next()
      resultRowSet.addRow(row.toArray.asInstanceOf[Array[AnyRef]])
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
        moonboxRowSet = doMoonboxQuery(statement)
      }
    } else {
      /* Ignore unsupported commands. */
      logInfo(s"Ignored statement: '$statement'.")
    }
    setState(OperationState.FINISHED)
  }

  private def handleShowStatement(mbStatement: String, isDatabases: Boolean): Unit = {
    val result = doMoonboxQuery(mbStatement)
    if (!isDatabases)  {
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
      val array =  new Array[Any](3)
      val iter = result.map { row =>
        array(0) = database
        array(1) = row.getString(0)
        array(2) = false
        new MoonboxRow(array)
      }
      moonboxRowSet = new MoonboxRowSet(iter.asJava, schema)
    } else {
      moonboxRowSet = result
    }
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
    val iter = result.map { row =>
      val arr = new Array[Any](3)
        arr(0) = row.getString(0)
        arr(1) = row.getString(1)
        arr(2) = ""
      new MoonboxRow(arr)
    }
    moonboxRowSet = new MoonboxRowSet(iter.asJava, schema)
  }

  /* do moonbox query */
  private def doMoonboxQuery(mbStatement: String = statement): MoonboxRowSet = {
    try {
      client.interactiveQuery(mbStatement :: Nil)
    } catch {
      case e: BackendException =>
        if (e.getMessage.contains("login first")) {
          client = client.newClient
          client.interactiveQuery(mbStatement :: Nil)
        } else throw new HiveSQLException(e)
    }
  }
  override def cancel(): Unit = {
    logInfo(s"Canceling query, SessionId=${client.sessionId}, Token=${client.token}")
    if (client.cancelInteractiveQuery()){
      logInfo("Cancel query successfully.")
    } else {
      logInfo("Cancel query failed.")
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
  def toHiveType(dataType: DataType): String = {
    import DataType._
    dataType match {
      case BYTE => "tinyint"
      case SHORT => "smallint"
      case INTEGER => "int"
      case LONG => "bigint"
      case NULL => "void"
      case ARRAY | BINARY | BOOLEAN | DATE | CHAR | VARCHAR | DOUBLE | FLOAT | STRING | TIMESTAMP | STRUCT | MAP | OBJECT | _ => dataType.getName
    }
  }
}
