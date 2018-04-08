package org.apache.spark.sql.execution.datasources.presto

import org.apache.spark.Partition

case class PrestoPartition(whereClause: String, idx: Int) extends Partition {
	override def index: Int = idx
}
