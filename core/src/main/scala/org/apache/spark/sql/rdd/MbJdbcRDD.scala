package org.apache.spark.sql.rdd

import java.sql.{Connection, ResultSet}

import org.apache.spark.annotation.DeveloperApi
import org.apache.spark.{Partition, SparkContext, TaskContext}
import org.apache.spark.rdd.RDD

import scala.reflect.ClassTag


// so far, we don't support partial aggregate function pushdown.
// so that rdd contains only one partition.
// this class may be changed when we implement partial aggregate function pushdown
class MbJdbcRDD[T: ClassTag](sc: SparkContext, getConnection: () => Connection,
	sql: String,
	mapRow: ResultSet => T) extends RDD[T](sc, Nil) {
	@DeveloperApi
	override def compute(split: Partition, context: TaskContext): Iterator[T] = {
null
	}

	override protected def getPartitions: Array[Partition] = {
		Array(new MbRDDPartition(0))
	}
}

object MbJdbcRDD {
	def resultSetToObjectArray(rs: ResultSet): Array[Object] = {
		Array.tabulate[Object](rs.getMetaData.getColumnCount)(i => rs.getObject(i + 1))
	}
}
