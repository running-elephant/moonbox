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

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.spark.sql.execution.datasources.mbjdbc

import java.sql.Connection
import java.util

import moonbox.common.MbLogging
import moonbox.core.datasys.{DataSystem, Updatable}
import org.apache.spark.Partition
import org.apache.spark.internal.Logging
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.execution.datasources.jdbc.{JDBCOptions, JDBCPartition, JDBCRDD, JdbcUtils}
import org.apache.spark.sql.jdbc.JdbcDialects
import org.apache.spark.sql.sources._
import org.apache.spark.sql.types.StructType
import org.apache.spark.sql._
import org.apache.spark.sql.sqlbuilder.MbDialect

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

/**
  * Instructions on how to partition the table among workers.
  */
private[sql] case class JDBCPartitioningInfo(
	column: String,
	lowerBound: Long,
	upperBound: Long,
	numPartitions: Int)

private[sql] object MbJDBCRelation extends Logging {
	/**
	  * Given a partitioning schematic (a column of integral type, a number of
	  * partitions, and upper and lower bounds on the column's value), generate
	  * WHERE clauses for each partition so that each row in the table appears
	  * exactly once.  The parameters minValue and maxValue are advisory in that
	  * incorrect values may cause the partitioning to be poor, but no data
	  * will fail to be represented.
	  *
	  * Null value predicate is added to the first partition where clause to include
	  * the rows with null value for the partitions column.
	  *
	  * @param partitioning partition information to generate the where clause for each partition
	  * @return an array of partitions with where clause for each partition
	  */
	def columnPartition(partitioning: JDBCPartitioningInfo): Array[Partition] = {
		if (partitioning == null || partitioning.numPartitions <= 1 ||
			partitioning.lowerBound == partitioning.upperBound) {
			return Array[Partition](JDBCPartition(null, 0))
		}

		val lowerBound = partitioning.lowerBound
		val upperBound = partitioning.upperBound
		require(lowerBound <= upperBound,
			"Operation not allowed: the lower bound of partitioning column is larger than the upper " +
				s"bound. Lower bound: $lowerBound; Upper bound: $upperBound")

		val numPartitions =
			if ((upperBound - lowerBound) >= partitioning.numPartitions) {
				partitioning.numPartitions
			} else {
				logWarning("The number of partitions is reduced because the specified number of " +
					"partitions is less than the difference between upper bound and lower bound. " +
					s"Updated number of partitions: ${upperBound - lowerBound}; Input number of " +
					s"partitions: ${partitioning.numPartitions}; Lower bound: $lowerBound; " +
					s"Upper bound: $upperBound.")
				upperBound - lowerBound
			}
		// Overflow and silliness can happen if you subtract then divide.
		// Here we get a little roundoff, but that's (hopefully) OK.
		val stride: Long = upperBound / numPartitions - lowerBound / numPartitions
		val column = partitioning.column
		var i: Int = 0
		var currentValue: Long = lowerBound
		var ans = new ArrayBuffer[Partition]()
		while (i < numPartitions) {
			val lBound = if (i != 0) s"$column >= $currentValue" else null
			currentValue += stride
			val uBound = if (i != numPartitions - 1) s"$column < $currentValue" else null
			val whereClause =
				if (uBound == null) {
					lBound
				} else if (lBound == null) {
					s"$uBound or $column is null"
				} else {
					s"$lBound AND $uBound"
				}
			ans += JDBCPartition(whereClause, i)
			i = i + 1
		}
		ans.toArray
	}
}

case class MbJDBCRelation(parts: Array[Partition], jdbcOptions: JDBCOptions)(@transient val sparkSession: SparkSession)
	extends BaseRelation
		with PrunedFilteredScan
		with InsertableRelation with MbLogging {

	override def sqlContext: SQLContext = sparkSession.sqlContext

	override val needConversion: Boolean = false

	override val schema: StructType = {
		JDBCRDD.resolveTable(jdbcOptions)
	}

	override def sizeInBytes: Long = dataLength

	def rowCount: Option[BigInt] = tableRows

	def indexes: Set[String] = {
		var conn: Connection = null
		try {
			conn = JdbcUtils.createConnectionFactory(jdbcOptions)()
			val url = jdbcOptions.url
			val tbName = jdbcOptions.table
			MbDialect.get(url).getIndexes(conn, url, tbName)
		} catch {
			case e: Exception =>
				logWarning(e.getMessage)
				Set[String]()
		} finally {
			if (conn != null) conn.close()
		}
	}

	lazy val (tableRows, dataLength): (Option[BigInt], Long) = {
		var conn: Connection = null
		try {
			conn = JdbcUtils.createConnectionFactory(jdbcOptions)()
			val url = jdbcOptions.url
			val tbName = jdbcOptions.table
			MbDialect.get(url).getTableStat(conn, url, tbName) match {
				case (rowCount, Some(length)) => (rowCount, length)
				case (rowCount, None) => (rowCount, super.sizeInBytes)
			}
		} catch {
			case e: Exception =>
				logWarning(e.getMessage)
				(None, super.sizeInBytes)
		} finally {
			if (conn != null) conn.close()
		}
	}

	// Check if JDBCRDD.compileFilter can accept input filters
	override def unhandledFilters(filters: Array[Filter]): Array[Filter] = {
		filters.filter(JDBCRDD.compileFilter(_, JdbcDialects.get(jdbcOptions.url)).isEmpty)
	}

	override def buildScan(requiredColumns: Array[String], filters: Array[Filter]): RDD[Row] = {
		// Rely on a type erasure hack to pass RDD[InternalRow] back as RDD[Row]
		JDBCRDD.scanTable(
			sparkSession.sparkContext,
			schema,
			requiredColumns,
			filters,
			parts,
			jdbcOptions).asInstanceOf[RDD[Row]]
	}

	override def insert(data: DataFrame, overwrite: Boolean): Unit = {
		val url = jdbcOptions.url
		val table = jdbcOptions.table
		val properties = jdbcOptions.asProperties

		import scala.collection.JavaConversions._
		val parameters = properties.entrySet().map { entry =>
			(entry.getKey.toString, entry.getValue.toString)
		}.toMap
		val isUpdate = !overwrite && Option(properties.getProperty("update")).exists(_.equalsIgnoreCase("true"))
		val isCaseSensitive = sqlContext.conf.caseSensitiveAnalysis
		if (isUpdate) {
			val dataSystem = DataSystem.lookupDataSystem(parameters)
			dataSystem match {
				case ds: Updatable =>
					ds.update(data, Some(schema), isCaseSensitive, parameters)
				case _ => throw new Exception(s"${parameters.getOrElse("type", "Underlying data source")} doesn't support upsert operation.")
			}
		} else {
			data.write
				.mode(if (overwrite) SaveMode.Overwrite else SaveMode.Append)
				.jdbc(url, table, properties)
		}
	}

	override def toString: String = {
		val partitioningInfo = if (parts.nonEmpty) s" [numPartitions=${parts.length}]" else ""
		// credentials should not be included in the plan output, table information is sufficient.
		s"MbJDBCRelation(${jdbcOptions.table})" + partitioningInfo
	}
}
