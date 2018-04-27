package moonbox.catalyst.adapter.elasticsearch5.rdd

import java.util.Properties

import moonbox.catalyst.adapter.elasticsearch5.EsCatalystQueryExecutor
import org.apache.spark.annotation.DeveloperApi
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.catalyst.plans.logical.LogicalPlan
import org.apache.spark.sql.types._
import org.apache.spark.{Partition, SparkContext, TaskContext}

import scala.reflect.ClassTag


object MbElasticSearchRDD {

}


class MbElasticSearchRDD[T: ClassTag](sc: SparkContext,
                                      plan: LogicalPlan,
                                      numPartitions: Int = 1,
                                      info: Map[String, String],
                                      mapRow: (Option[StructType], Seq[Any]) => T)
        extends RDD[T](sc, Nil) {

    @DeveloperApi
    override def compute(split: Partition, context: TaskContext): Iterator[T] = {
        val prop = new Properties()
        info.foreach{elem => prop.setProperty(elem._1, elem._2)}
        val executor = new EsCatalystQueryExecutor(prop)
        val rowIter = executor.execute(plan, mapRow)  //add ES ROW to structtype
        rowIter
    }

    override protected def getPartitions: Array[Partition] = {
        (0 until numPartitions).map { i =>
            new MbRDDPartition(i)
        }.toArray
    }

}

import org.apache.spark.Partition

class MbRDDPartition(idx: Int) extends Partition {
    override def index: Int = idx
}
