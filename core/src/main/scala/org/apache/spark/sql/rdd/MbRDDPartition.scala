package org.apache.spark.sql.rdd

import org.apache.spark.Partition

class MbRDDPartition(idx: Int) extends Partition {
	override def index: Int = idx
}
