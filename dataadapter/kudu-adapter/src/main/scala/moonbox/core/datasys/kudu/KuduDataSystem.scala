/*-
 * <<
 * Moonbox
 * ==
 * Copyright (C) 2016 - 2018 EDP
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

package moonbox.core.datasys.kudu

import moonbox.common.MbLogging
import moonbox.core.datasys.{DataSystem, Insertable, Pushdownable}
import moonbox.core.execution.standalone.DataTable
import org.apache.kudu.Type
import org.apache.kudu.client.KuduClient.KuduClientBuilder
import org.apache.kudu.client.KuduScanner.KuduScannerBuilder
import org.apache.kudu.client.SessionConfiguration.FlushMode
import org.apache.kudu.client._
import org.apache.kudu.spark.kudu.KuduContext
import org.apache.spark.sql.catalyst.expressions._
import org.apache.spark.sql.catalyst.plans.JoinType
import org.apache.spark.sql.catalyst.plans.logical._
import org.apache.spark.sql.execution.datasources.LogicalRelation
import org.apache.spark.sql.types.{DataTypes, DecimalType, StructType}
import org.apache.spark.sql.{DataFrame, Row, SaveMode, SparkSession}

import scala.collection.JavaConversions._
import scala.collection.mutable.ArrayBuffer

class KuduDataSystem(props: Map[String, String]) extends DataSystem(props) with Pushdownable with Insertable with MbLogging {

  val TABLE_KEY = "kudu.table"
  val KUDU_MASTER = "kudu.master"
  val OPERATION = "kudu.operation"
  val FAULT_TOLERANT_SCANNER = "kudu.faultTolerantScan"
  val SCAN_LOCALITY = "kudu.scanLocality"
  val KUDU_SCANNER_LIMIT = "kudu.scanner.limit"

  require(contains(KUDU_MASTER))

  private def masterAddress(): String = {
    props(KUDU_MASTER)
  }

  private def getClient: KuduClient = {
    new KuduClientBuilder(masterAddress()).build()
  }

  override def tableNames(): Seq[String] = {
    val kuduClient = getClient
    val tables = kuduClient.getTablesList.getTablesList
    kuduClient.close()
    tables
  }

  override def tableName(): String = {
    props(TABLE_KEY)
  }

  override def tableProperties(tableName: String): Map[String, String] = {
    props.+(TABLE_KEY -> tableName)
  }

  override def test(): Boolean = {
    var client: KuduClient = null
    try {
      client = getClient
      if (client != null) {
        true
      } else {
        false
      }
    } catch {
      case e: Exception =>
        false
    } finally {
      if (client != null) {
        client.close()
      }
    }

  }

  override val supportedOperators: Seq[Class[_]] = Seq(classOf[Project], classOf[Filter], classOf[GlobalLimit], classOf[LocalLimit])
  override val supportedJoinTypes: Seq[JoinType] = Nil
  override val supportedExpressions: Seq[Class[_]] = Seq(
    classOf[Literal], classOf[AttributeReference],
    classOf[IsNull], classOf[IsNotNull],
    classOf[And], classOf[In], classOf[StartsWith],
    classOf[EqualTo], classOf[GreaterThan],
    classOf[GreaterThanOrEqual], classOf[LessThan], classOf[LessThanOrEqual]
  )
  override val beGoodAtOperators: Seq[Class[_]] = Seq(
    classOf[GlobalLimit],
    classOf[LocalLimit],
    classOf[Filter]
  )
  override val supportedUDF: Seq[String] = Nil
  private val SCAN_LIMIT: Long = props.get(KUDU_SCANNER_LIMIT).map(_.toLong).getOrElse(0)

  override def isSupportAll: Boolean = false

  override def fastEquals(other: DataSystem): Boolean = false

  private def collectFilters(plan: LogicalPlan): (Array[String], Array[org.apache.spark.sql.sources.Filter]) = {
    var columns: Array[String] = Array.empty
    val filters: ArrayBuffer[org.apache.spark.sql.sources.Filter] = new ArrayBuffer()
    plan.children.foreach { child =>
      val (cols, fs) = collectFilters(child)
      columns = cols
      filters ++ fs
    }
    plan match {
      case Filter(condition, child) =>
        ExpressionUtils.predicate2Filter(condition.asInstanceOf[Predicate])
      case LogicalRelation(_, output, _) => columns = output.map(_.name).toArray
      case _ => /* no-op */
    }
    (columns, filters.toArray)
  }

  private def getScanLocalityType(): ReplicaSelection = {
    props.getOrElse(SCAN_LOCALITY, "closest_replica").toLowerCase match {
      case "leader_only" => ReplicaSelection.LEADER_ONLY
      case "closest_replica" => ReplicaSelection.CLOSEST_REPLICA
      case other => throw new IllegalArgumentException(s"Unsupported replica selection type '$other'")
    }
  }

  override def buildScan(plan: LogicalPlan, sparkSession: SparkSession): DataFrame = {
    val faultTolerantScanner = props.getOrElse(FAULT_TOLERANT_SCANNER, "false").toBoolean
    val table = getClient.openTable(tableName())
    val (requiredColumns, predicates) = ExpressionUtils.findPredicates(plan, table.getSchema)
    val kuduRdd = new KuduRDD(new KuduContext(masterAddress(), sparkSession.sparkContext), 1024 * 1024 * 20, requiredColumns, predicates,
      table, faultTolerantScanner, getScanLocalityType(), sparkSession.sparkContext)
    sparkSession.createDataFrame(kuduRdd, plan.schema)
  }

  override def buildQuery(plan: LogicalPlan): DataTable = {
    val kuduClient = getClient
    val kuduTable = kuduClient.openTable(tableName())
    val scannerBuilder = kuduClient.newScannerBuilder(kuduTable)
    if (SCAN_LIMIT > 0) {
      scannerBuilder.limit(SCAN_LIMIT)
    }
    buildKuduScanner(plan, scannerBuilder, kuduTable)
    val kuduScanner = scannerBuilder.build()
    val iterator: Iterator[Row] = new Iterator[Row] {
      var limitValue: Long = kuduScanner.getLimit
      var iterOption: Option[RowResultIterator] = getIter

      override def hasNext = {
        iterOption match {
          case Some(iter) =>
            if (iter.hasNext && limitValue > 0) true
            else if (limitValue <= 0) {
              logWarning(s"To make limit($limitValue) take effect forcibly, stop the iteration.")
              false
            } else {
              iterOption = getIter
              hasNext
            }
          case None => false
        }
      }

      override def next() = {
        limitValue -= 1
        val rowResult = iterOption.get.next()
        val row = new ArrayBuffer[Any]()
        for (i <- 0 until rowResult.getSchema.getColumnCount) {
          val typ = rowResult.getColumnType(i)
          row += getColValue(rowResult, typ, i)
        }
        Row(row: _*)
      }

      private def getIter: Option[RowResultIterator] = {
        if (kuduScanner.hasMoreRows) {
          Some(kuduScanner.nextRows())
        } else None
      }
    }
    val schema: StructType = plan.schema
    val closeIfNeeded: () => Unit = () => {
      kuduScanner.close()
      kuduClient.close()
    }
    new DataTable(iterator, schema, closeIfNeeded)
  }

  private def getColValue(rowResult: RowResult, colType: Type, index: Int): Any = {
    if (rowResult.isNull(index)) {
      return null
    }
    colType match {
      case Type.INT8 => rowResult.getByte(index)
      case Type.INT16 => rowResult.getShort(index)
      case Type.INT32 => rowResult.getInt(index)
      case Type.INT64 => rowResult.getLong(index)
      case Type.BINARY => rowResult.getBinaryCopy(index)
      case Type.STRING => rowResult.getString(index)
      case Type.BOOL => rowResult.getBoolean(index)
      case Type.FLOAT => rowResult.getFloat(index)
      case Type.DOUBLE => rowResult.getDouble(index)
      case Type.UNIXTIME_MICROS => ExpressionUtils.microsToTimestamp(rowResult.getLong(index))
      case Type.DECIMAL => rowResult.getDecimal(index)
      case other => throw new Exception(s"Column type $other is unknown.")
    }
  }

  private def buildKuduScanner(plan: LogicalPlan, scannerBuilder: KuduScannerBuilder, kuduTable: KuduTable): Unit = {
    plan.children.foreach(child => buildKuduScanner(child, scannerBuilder, kuduTable))
    plan match {
      case GlobalLimit(limitExpr, child) =>
        val limitValue = limitExpr match {
          case IntegerLiteral(limit) => limit
          case other => throw new Exception(s"Invalid limit expression $other.")
        }
        scannerBuilder.limit(limitValue)
      case LocalLimit(limitExpr, child) =>
        val limitValue = limitExpr match {
          case IntegerLiteral(limit) => limit
          case other => throw new Exception(s"Invalid limit expression $other.")
        }
        scannerBuilder.limit(limitValue)
      case Filter(condition, child) =>
        val kuduPredicate = condition match {
          case predicate: Predicate => ExpressionUtils.toKuduPredicate(predicate, kuduTable.getSchema)
          case other => throw new Exception(s"Invalid predicate expression $other.")
        }
        kuduPredicate.foreach(scannerBuilder.addPredicate)
      case Project(projectList, child) =>
        val columns = projectList.map {
          case a: AttributeReference => a.name
          case other => throw new Exception(s"Invalid project expression: $other")
        }
        scannerBuilder.setProjectedColumnNames(columns)
      case LogicalRelation(baseRelation, output, catalogTable) =>
        scannerBuilder.setProjectedColumnNames(output.map(expr => expr.name))
      case _ => /* no-op */
    }
  }

  override def insert(table: DataTable, saveMode: SaveMode): Unit = {
    val kuduClient = getClient
    val schema = table.schema
    val lastPropagatedTimestamp = kuduClient.getLastPropagatedTimestamp
    kuduClient.updateLastPropagatedTimestamp(lastPropagatedTimestamp)
    val kuduTable = kuduClient.openTable(tableName())

    saveMode match {
      case SaveMode.Append =>
        val kuduSession = kuduClient.newSession()
        kuduSession.setFlushMode(FlushMode.AUTO_FLUSH_BACKGROUND)
        kuduSession.setIgnoreAllDuplicateRows(false)
        try {
          for (row <- table.iter) {
            val operation: Operation = kuduTable.newInsert()
            writeRow(row, schema, kuduTable, operation)
            kuduSession.apply(operation)
          }
        } finally {
          kuduSession.close()
        }
        val pendingErrors = kuduSession.getPendingErrors
        val errorCount = pendingErrors.getRowErrors.length
        if (errorCount > 0) {
          val errors = pendingErrors.getRowErrors.take(5).map(_.getErrorStatus).mkString
          throw new RuntimeException(
            s"failed to write $errorCount rows from DataFrame to Kudu; sample errors: $errors")
        }
      case SaveMode.ErrorIfExists =>
        throw new UnsupportedOperationException("ErrorIfExists is not yet supported")
      case SaveMode.Ignore => throw new UnsupportedOperationException("Ignore is not yet supported")
      case SaveMode.Overwrite => throw new UnsupportedOperationException("Overwrite is not yet supported")
    }
  }

  private def writeRow(row: Row, schema: StructType, kuduTable: KuduTable, operation: Operation): Unit = {
    val indices: Array[(Int, Int)] = schema.fields.zipWithIndex.map {
      case (field, oldIndex) => oldIndex -> kuduTable.getSchema.getColumnIndex(field.name)
    }
    for ((oldIndex, kuduIndex) <- indices) {
      if (row.isNullAt(oldIndex)) {
        operation.getRow.setNull(kuduIndex)
      } else {
        schema.fields(oldIndex).dataType match {
          case DataTypes.StringType => operation.getRow.addString(kuduIndex, row.getString(oldIndex))
          case DataTypes.BinaryType => operation.getRow.addBinary(kuduIndex, row.getAs[Array[Byte]](oldIndex))
          case DataTypes.BooleanType => operation.getRow.addBoolean(kuduIndex, row.getBoolean(oldIndex))
          case DataTypes.ByteType => operation.getRow.addByte(kuduIndex, row.getByte(oldIndex))
          case DataTypes.ShortType => operation.getRow.addShort(kuduIndex, row.getShort(oldIndex))
          case DataTypes.IntegerType => operation.getRow.addInt(kuduIndex, row.getInt(oldIndex))
          case DataTypes.LongType => operation.getRow.addLong(kuduIndex, row.getLong(oldIndex))
          case DataTypes.FloatType => operation.getRow.addFloat(kuduIndex, row.getFloat(oldIndex))
          case DataTypes.DoubleType => operation.getRow.addDouble(kuduIndex, row.getDouble(oldIndex))
          case DataTypes.TimestampType => operation.getRow.addLong(kuduIndex, ExpressionUtils.timestampToMicros(row.getTimestamp(oldIndex)))
          case DecimalType() => operation.getRow.addDecimal(kuduIndex, row.getDecimal(oldIndex))
          case t => throw new IllegalArgumentException(s"No support for Spark SQL type $t")
        }
      }
    }
  }
}
