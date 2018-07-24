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

package org.apache.spark.sql.execution.datasources.presto

import org.apache.spark.Partition
import org.apache.spark.sql.SQLContext
import org.apache.spark.sql.sources.{BaseRelation, RelationProvider}

class DefaultSource extends RelationProvider {
	override def createRelation(sqlContext: SQLContext, parameters: Map[String, String]): BaseRelation = {

		val partitionColumn = parameters.get("partitionColumn")
		val lowerBound = parameters.get("lowerBound").map(_.toLong)
		val upperBound = parameters.get("upperBound").map(_.toLong)
		val numPartitions = parameters.get("numPartitions").map(_.toInt)

		val partitionInfo = if (partitionColumn.isEmpty) {
			assert(lowerBound.isEmpty && upperBound.isEmpty)
			null
		} else {
			assert(lowerBound.nonEmpty && upperBound.nonEmpty && numPartitions.nonEmpty)
			PrestoPartitioningInfo(partitionColumn.get,
				lowerBound.get,
				upperBound.get,
				numPartitions.get
			)
		}
		val partitions: Array[Partition] = PrestoRelation.columnPartition(partitionInfo)
		PrestoRelation(partitions, parameters)(sqlContext.sparkSession)
	}
}
