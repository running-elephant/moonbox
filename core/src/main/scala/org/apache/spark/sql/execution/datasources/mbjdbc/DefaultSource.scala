package org.apache.spark.sql.execution.datasources.mbjdbc

import org.apache.spark.sql.execution.datasources.jdbc.JdbcUtils._
import org.apache.spark.sql.execution.datasources.jdbc.{JDBCOptions, JdbcUtils}
import org.apache.spark.sql.sources.{CreatableRelationProvider, RelationProvider, _}
import org.apache.spark.sql.{SQLContext, SaveMode, _}

class DefaultSource extends CreatableRelationProvider
	with RelationProvider {

  override def createRelation(
								 sqlContext: SQLContext,
								 parameters: Map[String, String]): BaseRelation = {
	val jdbcOptions = new JDBCOptions(parameters)
	val partitionColumn = jdbcOptions.partitionColumn
	val lowerBound = jdbcOptions.lowerBound
	val upperBound = jdbcOptions.upperBound
	val numPartitions = jdbcOptions.numPartitions

	val partitionInfo = if (partitionColumn.isEmpty) {
	  assert(lowerBound.isEmpty && upperBound.isEmpty)
	  null
	} else {
	  assert(lowerBound.nonEmpty && upperBound.nonEmpty && numPartitions.nonEmpty)
	  JDBCPartitioningInfo(
		partitionColumn.get, lowerBound.get, upperBound.get, numPartitions.get)
	}
	val parts = MbJDBCRelation.columnPartition(partitionInfo)
	MbJDBCRelation(parts, jdbcOptions)(sqlContext.sparkSession)
  }

  override def createRelation(
								 sqlContext: SQLContext,
								 mode: SaveMode,
								 parameters: Map[String, String],
								 df: DataFrame): BaseRelation = {
	val options = new JDBCOptions(parameters)
	val isCaseSensitive = sqlContext.conf.caseSensitiveAnalysis

	val conn = JdbcUtils.createConnectionFactory(options)()
	try {
	  val tableExists = JdbcUtils.tableExists(conn, options)
	  if (tableExists) {
		mode match {
		  case SaveMode.Overwrite =>
			if (options.isTruncate && isCascadingTruncateTable(options.url) == Some(false)) {
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
	  } else {
		createTable(conn, df, options)
		saveTable(df, Some(df.schema), isCaseSensitive, options)
	  }
	} finally {
	  conn.close()
	}

	createRelation(sqlContext, parameters)
  }
}
