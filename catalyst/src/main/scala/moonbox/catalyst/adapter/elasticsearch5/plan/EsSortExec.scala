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

package moonbox.catalyst.adapter.elasticsearch5.plan

import moonbox.catalyst.adapter.util.SparkUtil
import moonbox.catalyst.core.CatalystContext
import moonbox.catalyst.core.plan.{CatalystPlan, SortExec}
import org.apache.spark.sql.catalyst.expressions.{Ascending, Descending, SortDirection, SortOrder}
import org.apache.spark.sql.catalyst.plans.logical.Sort


class EsSortExec(sortOrder: Seq[SortOrder],
                      global: Boolean,
                      child: CatalystPlan) extends SortExec(sortOrder, global, child ) {

    override def translate(context: CatalystContext): Seq[String] = {
        val seq: Seq[String] = child.translate(context)

        seq ++ Seq(toJson)
    }

    //-------body-----------

    def toJson: String = {
        val orderSeq = sortOrder.map{e =>
            val order: String = e.direction.sql
            val name: String = SparkUtil.parseLeafExpression(e.child).name
            s""" {"${name}":{"order":"${order}"}} """   //for es2 or es5 all passed, note: es5 also support  {"${name}":"${order}"}
        }

        s""" "sort": [${orderSeq.mkString(",")}] """
    }
}


object EsSortExec{
    def apply(sortOrder: Seq[SortOrder],
              global: Boolean,
              child: CatalystPlan): EsSortExec = {
        new EsSortExec(sortOrder, global, child)
    }
}
