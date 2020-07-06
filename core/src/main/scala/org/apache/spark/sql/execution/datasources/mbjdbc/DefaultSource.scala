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

package org.apache.spark.sql.execution.datasources.mbjdbc

import java.sql.Connection
import java.util.ServiceLoader

import moonbox.core.datasys.{DataSystem, Updatable}
import org.apache.spark.sql.execution.datasources.jdbc.{JDBCOptions, JDBCRDD}
import org.apache.spark.sql.execution.datasources.mbjdbc.JdbcUtils._
import org.apache.spark.sql.jdbc.{JdbcDialect, JdbcDialects}
import org.apache.spark.sql.sources.{CreatableRelationProvider, RelationProvider, _}
import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.{SQLContext, SaveMode, _}

import scala.collection.JavaConverters._
import scala.collection.mutable

class DefaultSource extends CreatableRelationProvider
  with SchemaRelationProvider
  with RelationProvider {

  import DefaultSource._

  //TODO: in yarn cluster, if not driver name, app throws no suitable driver exception. It should config driver or code add automatically
  private def addDriverIfNecessary(parameters: Map[String, String]): Map[String, String] = {
    val newParameters = if (parameters.get("type").isDefined && parameters.get("driver").isEmpty) {
      val driver = parameters("type").toLowerCase match {
        case "clickhouse" => Some("ru.yandex.clickhouse.ClickHouseDriver")
        case "mysql" => Some("com.mysql.jdbc.Driver")
        case "oracle" => Some("oracle.jdbc.driver.OracleDriver")
        case "sqlserver" => Some("com.microsoft.sqlserver.jdbc.SQLServerDriver")
        case "presto" => Some("com.facebook.presto.jdbc.PrestoDriver")
        case "impala" => Some("com.cloudera.impala.jdbc41.Driver")
        case "postgres" => Some("org.postgresql.Driver")
        case _ => None
      }
      if (driver.isDefined) {
        parameters.updated("driver", driver.get)
      } else {
        parameters
      }
    } else {
      parameters
    }
    newParameters
  }

  private def computePartitionBound(jdbcOptions: JDBCOptions, partitionColumn: String): (String, String) = {
    var conn: Connection = null
    try {
      conn = JdbcUtils.createConnectionFactory(jdbcOptions)()
      val statement = conn.createStatement()
      val table = jdbcOptions.table
      val sql = s"select min($partitionColumn) as min, max($partitionColumn) as max from $table"
      val result = statement.executeQuery(sql)
      if (result.next()) {
        val min = result.getObject(1).toString
        val max = result.getObject(2).toString
        (min, max)
      } else {
        throw new Exception(s"execute $sql ResultSet is empty, auto calculate partition column bounds failed.")
      }
    } catch {
      case ex: Exception =>
        throw new Exception(s"auto calculate partition column bounds failed", ex)
    } finally {
      if (conn != null) conn.close()
    }
  }

  override def createRelation(
                               sqlContext: SQLContext,
                               parameters: Map[String, String]): BaseRelation = {
    createRelation(sqlContext, parameters, null)
  }

  override def createRelation(sqlContext: SQLContext, parameters: Map[String, String], schema: StructType): BaseRelation = {
    val newParameters = addDriverIfNecessary(parameters) ++ addDefaultProps(parameters)
    val mbJdbcOptions = new MbJDBCOptions(newParameters)

    val removedPartitionBoundParameters =
      newParameters
        .filterNot(kv =>
          List(JDBCOptions.JDBC_LOWER_BOUND.toLowerCase,
            JDBCOptions.JDBC_UPPER_BOUND.toLowerCase,
            JDBCOptions.JDBC_PARTITION_COLUMN.toLowerCase,
            JDBCOptions.JDBC_NUM_PARTITIONS).contains(kv._1))

    val jdbcOptions = new JDBCOptions(removedPartitionBoundParameters)
    val autoCalculateBound = newParameters.get(AUTO_COMPUTE_PARTITION_BOUND).map(_.toBoolean)
    if (autoCalculateBound.nonEmpty && autoCalculateBound.get) {
      val (lowerBound, upperBound) = computePartitionBound(jdbcOptions, mbJdbcOptions.partitionColumn.get)
      mbJdbcOptions.setLowerBound(lowerBound)
      mbJdbcOptions.setUpperBound(upperBound)
      newParameters ++ Map(JDBCOptions.JDBC_LOWER_BOUND -> lowerBound, JDBCOptions.JDBC_UPPER_BOUND -> upperBound)
    }

    val tableSchema = if (schema == null) JDBCRDD.resolveTable(jdbcOptions) else schema
    val resolver = sqlContext.conf.resolver
    val timeZoneId = sqlContext.conf.sessionLocalTimeZone
    val parts = MbJDBCRelation.columnPartition(tableSchema, resolver, timeZoneId, mbJdbcOptions)
    MbJDBCRelation(parts, Option(schema), newParameters)(sqlContext.sparkSession)
  }

  override def createRelation(
                               sqlContext: SQLContext,
                               mode: SaveMode,
                               parameters: Map[String, String],
                               df: DataFrame): BaseRelation = {
    val newParameters = addDriverIfNecessary(parameters) ++ addDefaultProps(parameters)
    val options = new JDBCOptions(newParameters)
    val isCaseSensitive = sqlContext.conf.caseSensitiveAnalysis

    val conn = JdbcUtils.createConnectionFactory(options)()
    try {
      val tableExists = JdbcUtils.tableExists(conn, options)
      if (tableExists) {
        parameters.get("update") match {
          case Some("true") if mode != SaveMode.Overwrite =>
            val dataSystem = DataSystem.lookupDataSystem(parameters)
            dataSystem match {
              case ds: Updatable =>
                val tableSchema = JdbcUtils.getSchemaOption(conn, options)
                ds.update(df, tableSchema, isCaseSensitive, parameters)
              case _ => throw new Exception(s"${parameters.getOrElse("type", "Underlying data source")} doesn't support upsert operation.")
            }
          case _ =>
            mode match {
              case SaveMode.Overwrite =>
                if (options.isTruncate && isCascadingTruncateTable(options.url).contains(false)) {
                  // In this case, we should truncate table and then load.
                  truncateTable(conn, options.table)
                  val tableSchema = JdbcUtils.getSchemaOption(conn, options)
                  saveTable(df, tableSchema, isCaseSensitive, options)
                } else {
                  // Otherwise, do not truncate the table, instead drop and recreate it
                  dropTable(conn, options.table)
                  createTable(conn, df, options)
                  saveTable(df, Some(df.schema), isCaseSensitive, options)
                }
              case SaveMode.Append =>
                val tableSchema = JdbcUtils.getSchemaOption(conn, options)
                saveTable(df, tableSchema, isCaseSensitive, options)

              case SaveMode.ErrorIfExists =>
                throw new AnalysisException(
                  s"Table or view '${options.table}' already exists. SaveMode: ErrorIfExists.")

              case SaveMode.Ignore =>
              // With `SaveMode.Ignore` mode, if table already exists, the save operation is expected
              // to not save the contents of the DataFrame and to not change the existing data.
              // Therefore, it is okay to do nothing here and then just return the relation below.
            }
        }
      } else {
        createTable(conn, df, options)

        saveTable(df, Some(df.schema), isCaseSensitive, options)
      }
    } finally {
      conn.close()
    }

    createRelation(sqlContext, newParameters)
  }

  private def addDefaultProps(props: Map[String, String]): Map[String, String] = {
    val map = mutable.HashMap.empty[String, String]
    if (!props.contains(JDBCOptions.JDBC_TRUNCATE))
      map.put(JDBCOptions.JDBC_TRUNCATE, "true")
    if (!props.contains(JDBCOptions.JDBC_TXN_ISOLATION_LEVEL))
      map.put(JDBCOptions.JDBC_TXN_ISOLATION_LEVEL, "NONE")
    if (!props.contains(JDBCOptions.JDBC_BATCH_FETCH_SIZE))
      map.put(JDBCOptions.JDBC_BATCH_FETCH_SIZE, "20000")
    if (props("type") == "mysql") {
      var url = props(JDBCOptions.JDBC_URL)
      if (!url.contains("rewriteBatchedStatements")) {
        if (url.contains("&")) url += "&rewriteBatchedStatements=true"
        else url += "?rewriteBatchedStatements=true"
      }
      map.put(JDBCOptions.JDBC_URL, url)
    }
    map.toMap
  }
}

object DefaultSource {

  val AUTO_COMPUTE_PARTITION_BOUND = "autoComputePartitionBound"

  for (x <- ServiceLoader.load(classOf[JdbcDialect]).asScala) {
    JdbcDialects.registerDialect(x)
  }
}
