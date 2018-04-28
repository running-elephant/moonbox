package org.apache.spark.sql.rdd

import java.util.Properties

import moonbox.catalyst.adapter.elasticsearch5.EsCatalystQueryExecutor
import moonbox.catalyst.adapter.jdbc.JdbcRow
import org.apache.spark.annotation.DeveloperApi
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.types._
import org.apache.spark.{Partition, SparkContext, TaskContext}

import scala.reflect.ClassTag


object MbElasticSearchRDD {

}


class MbElasticSearchRDD[T: ClassTag](@transient val sc: SparkContext,
                                      json: String,
                                      mapping: Seq[(String, String)],
                                      schema: StructType,
                                      numPartitions: Int = 1,
                                      prop: Properties,
                                      mapRow: (Option[StructType], Seq[Any]) => T)
        extends RDD[T](sc, Nil) {

    @DeveloperApi
    override def compute(split: Partition, context: TaskContext): Iterator[T] = {
        val executor = new EsCatalystQueryExecutor(prop)
        val rowIter = executor.execute(json, schema, mapping, mapRow)  //add ES ROW to structtype
        rowIter
    }

    override protected def getPartitions: Array[Partition] = {
        (0 until numPartitions).map { i =>
            new MbRDDPartition(i)
        }.toArray
    }

}