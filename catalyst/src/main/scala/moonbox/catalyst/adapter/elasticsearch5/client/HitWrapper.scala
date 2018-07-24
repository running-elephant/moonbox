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

class HitWrapper(index: String, mtype: String, id: String, source: String, map: Map[String, AnyRef]) {

    def this(source: String) {
        this(null, null, null, source, Map.empty[String, AnyRef])
    }

    def getSourceAsString: String = source

    def getIndex: String = index

    def getType: String = mtype

    def getId: String = id

    def getMap: Map[String, AnyRef] = map

}
