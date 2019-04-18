/*-
 * <<
 * Moonbox
 * ==
 * Copyright (C) 2016 - 2019 EDP
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

import moonbox.catalyst.core.CatalystContext
import moonbox.catalyst.core.plan.{CatalystPlan, LimitExec}

import scala.collection.mutable


class EsLimitExec(limit: Int, child: CatalystPlan) extends LimitExec(limit, child) {

    override def translate(context: CatalystContext): Seq[String] = {
        val seq: Seq[String] = child.translate(context)
        context.limitSize = limit
        seq ++ Seq(toJson)
    }

    //---body-----
    def toJson: String = {  // es max req size is 10000
        if(limit <= 10000) {
            s""" "from": 0, "size": $limit """
        }else{
            s""" "from": 0, "size": 10000 """
        }
    }
}


object EsLimitExec{
    def apply(limit: Int, child: CatalystPlan): EsLimitExec = {
        new EsLimitExec(limit, child)
    }
}
