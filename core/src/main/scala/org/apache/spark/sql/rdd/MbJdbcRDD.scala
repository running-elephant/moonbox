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

package org.apache.spark.sql.rdd

import java.sql.{Connection, ResultSet}

import org.apache.spark.annotation.DeveloperApi
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.Row
import org.apache.spark.sql.execution.datasources.jdbc.JdbcUtils
import org.apache.spark.sql.types.StructType
import org.apache.spark.{Partition, SparkContext, TaskContext}

import scala.reflect.ClassTag


// so far, we don't support partial aggregate function pushdown.
// so that rdd contains only one partition.
// this class may be changed when we implement partial aggregate function pushdown
class MbJdbcRDD[T: ClassTag](sc: SparkContext, getConnection: () => Connection,
                             sql: String,
                             schema: StructType,
                             mapRow: (ResultSet, StructType) => Iterator[Row]) extends RDD[Row](sc, Nil) {
  @DeveloperApi
  override def compute(split: Partition, context: TaskContext): Iterator[Row] = {

    val conn = getConnection()
    val statement = conn.createStatement()
    val resultSet = statement.executeQuery(sql)

    context.addTaskCompletionListener{ context => close() }

     def close() {
      try {
        if (null != resultSet) {
          resultSet.close()
        }
      } catch {
        case e: Exception => logWarning("Exception closing resultset", e)
      }
      try {
        if (null != statement) {
          statement.isClosed
        }
      } catch {
        case e: Exception => logWarning("Exception closing statement", e)
      }
      try {
        if (null != conn) {
          conn.close()
        }
        logInfo("closed connection")
      } catch {
        case e: Exception => logWarning("Exception closing connection", e)
      }
    }

    mapRow(resultSet, schema)
  }

  override protected def getPartitions: Array[Partition] = {
    Array(new MbRDDPartition(0))
  }
}

object MbJdbcRDD {
  def resultSetToRows(rs: ResultSet, schema: StructType): Iterator[Row] = {
    JdbcUtils.resultSetToRows(rs, schema)
  }
}
