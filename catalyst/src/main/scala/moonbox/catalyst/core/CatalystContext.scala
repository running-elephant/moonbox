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

package moonbox.catalyst.core

import org.apache.spark.sql.catalyst.expressions.Expression
import org.apache.spark.sql.types.StructType

class CatalystContext {
  //ES
  var nestFields = Set.empty[String] //es nest fields name
  var version = Seq.empty[Int] //es version
  def isES50: Boolean = version.headOption.getOrElse(5) == 5

  var limitSize: Int = 0  //es has default limit 0
  var filterFunctionSeq: Seq[Expression] = Seq.empty[Expression]
  var projectFunctionSeq: Seq[(Expression, Int)] = Seq.empty[(Expression, Int)]

  var projectElementSeq: Seq[(String, String)] = Seq.empty[(String, String)]
  var aggElementSeq: Seq[(String, String)] = Seq.empty[(String, String)]
  var hasAgg = false


  //Mongo
  var tableSchema: StructType = _
  val index2FieldName = collection.mutable.Map[Int, String]()
}


object CatalystContext {

}
