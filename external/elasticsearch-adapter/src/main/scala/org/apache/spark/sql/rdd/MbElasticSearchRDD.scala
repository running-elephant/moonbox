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

package org.apache.spark.sql.rdd

import java.util.Properties

import moonbox.catalyst.adapter.elasticsearch5.EsCatalystQueryExecutor
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
                                      limitSize: Int,
                                      mapRow: (Option[StructType], Seq[Any]) => T)
        extends RDD[T](sc, Nil) {

    @DeveloperApi
    override def compute(split: Partition, context: TaskContext): Iterator[T] = {
        val executor = new EsCatalystQueryExecutor(prop)
        context.addTaskCompletionListener( context => executor.client.close())
        val rowIter = executor.execute(json, schema, mapping, limitSize, mapRow)  //add ES ROW to structtype
        rowIter
    }

    override protected def getPartitions: Array[Partition] = {
        (0 until numPartitions).map { i =>
            new MbRDDPartition(i)
        }.toArray
    }

}
