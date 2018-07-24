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

import moonbox.catalyst.adapter.elasticsearch5.client.AggWrapper.AggregationType.AggregationType


class AggWrapper(mtype: AggregationType, result: String, map: Map[String, AnyRef]= Map.empty[String, AnyRef]) {

    def getType: AggregationType = mtype
    def getResult: String = result

    def getMap: Map[String, AnyRef] = map

}

object AggWrapper {
    /** Type of an aggregation (to know if there are buckets or not) */
    object AggregationType extends Enumeration {
        type AggregationType = Value
        val SIMPLE, MULTI_BUCKETS = Value
    }
}
