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
