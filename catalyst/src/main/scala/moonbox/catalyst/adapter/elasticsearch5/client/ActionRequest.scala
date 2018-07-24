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

import org.apache.spark.sql.types.DataType

import scala.collection.mutable

class ActionRequest {

    var hasLimit: Boolean = false
    var element: scala.collection.mutable.ArrayBuffer[String] = scala.collection.mutable.ArrayBuffer.empty[String]
    //val projectSchemaMap: mutable.LinkedHashMap[String, DataType] = mutable.LinkedHashMap.empty[String, DataType]
    val aggSchemaMap: mutable.LinkedHashMap[String, DataType] = mutable.LinkedHashMap.empty[String, DataType]

    def buildRequest(): String = {
        if(!hasLimit) {  //if no limit, add default size here
            element.insert(0, """ "from":0, "size":200 """)
        }
        "{ " + element.mkString(",") + " }"
    }

}
