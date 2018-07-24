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

package moonbox.catalyst.adapter.elasticsearch5.client

import java.util
import java.util.LinkedList

import org.apache.spark.sql.types.StructType
import moonbox.catalyst.adapter.util.SparkUtil._
import moonbox.common.MbLogging

import scala.collection.mutable

class ActionResponse extends MbLogging{
    private var succeeded = false
    private var totalHits = 0L
    private val hits = new LinkedList[HitWrapper]
    private val aggregations = new LinkedList[AggWrapper]
    private var isAgg = false

    def clear(): Unit = {
        succeeded = false
        totalHits = 0L
        hits.clear()
        aggregations.clear()
        isAgg = false
    }

    def succeeded(succeeded: Boolean): ActionResponse = {
        this.succeeded = succeeded
        this
    }

    def isSucceeded: Boolean = succeeded

    def totalHits(totalHits: Long): ActionResponse = {
        this.totalHits = totalHits
        this
    }

    def getResult(schema: StructType, colId2ColName: Map[Int, String]): Seq[Seq[Any]] = {
        import scala.collection.JavaConversions._
        val resultSeq: mutable.Seq[Map[String, AnyRef]] = if(isAgg) {
            aggregations.map{_.getMap}
        }
        else {
            hits.map{_.getMap}
        }
        resultSeq.map{elem =>
            //logInfo("date= " + elem)
            resultListToObjectArray(schema, colId2ColName, elem, isAgg)
        }

    }

    def getTotalHits: Long = totalHits

    def getHits: util.List[HitWrapper] = hits

    def addHit(hit: HitWrapper): ActionResponse = {
        this.hits.add(hit)
        this
    }

    def getAggregations: util.List[AggWrapper] = aggregations

    def addAggregation(aggregation: AggWrapper): ActionResponse = {
        this.isAgg = true
        this.aggregations.add(aggregation)
        this
    }

    def hit(hit: HitWrapper): ActionResponse = {
        this.addHit(hit)
        this
    }

    def getHit: HitWrapper = this.hits.get(0)
}
