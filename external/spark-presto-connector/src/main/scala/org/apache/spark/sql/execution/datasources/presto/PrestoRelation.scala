package org.apache.spark.sql.execution.datasources.presto

import org.apache.spark.Partition
import org.apache.spark.internal.Logging
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{Row, SQLContext, SparkSession}
import org.apache.spark.sql.sources.{BaseRelation, Filter, PrunedFilteredScan}
import org.apache.spark.sql.types.StructType

import scala.collection.mutable.ArrayBuffer

case class PrestoPartitioningInfo(
	column: String,
	lowerBound: Long,
	upperBound: Long,
	numPartitions: Int)


case class PrestoRelation(parts: Array[Partition], props: Map[String, String])(@transient val sparkSession: SparkSession) extends BaseRelation with PrunedFilteredScan {

	override def sqlContext: SQLContext = sparkSession.sqlContext

	override def schema: StructType = PrestoRDD.resolveTable(props)

	override def buildScan(requiredColumns: Array[String], filters: Array[Filter]): RDD[Row] = {
		PrestoRDD.scanTable(
			sparkSession.sparkContext,
			schema,
			requiredColumns,
			filters,
			parts,
			props
		)
	}.asInstanceOf[RDD[Row]]

}

object PrestoRelation extends Logging {
	def columnPartition(partitioning: PrestoPartitioningInfo): Array[Partition] = {
		if (partitioning == null || partitioning.numPartitions <= 1 ||
			partitioning.lowerBound == partitioning.upperBound) {
			return Array[Partition](PrestoPartition(null, 0))
		}

		val lowerBound = partitioning.lowerBound
		val upperBound = partitioning.upperBound
		require (lowerBound <= upperBound,
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
			ans += PrestoPartition(whereClause, i)
			i = i + 1
		}
		ans.toArray
	}
}
