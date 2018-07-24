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

package moonbox.catalyst.adapter.mongo.function

object UDFunctions {
  /** new in version 2.4 */
  def geoNear(x: Double,
              y: Double,
              spherical: Boolean = false,
              limit: Int = 100,
              num: Int = 100,
              maxDistance: Number = null,
              query: String = null,
              distanceMultiplier: Number = null,
              uniqueDocs: Boolean = false,
              distanceField: String,
              includeLocs: String,
              minDistance: Number
             ): Boolean = true

  /** new in version 3.2 */
  def indexStats(): Boolean = true

}
