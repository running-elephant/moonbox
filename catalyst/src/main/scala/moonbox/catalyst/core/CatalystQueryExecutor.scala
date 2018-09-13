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

import moonbox.catalyst.jdbc.JdbcRow
import org.apache.spark.sql.UDFRegistration
import org.apache.spark.sql.catalyst.plans.logical.LogicalPlan
import org.apache.spark.sql.types.StructType

trait CatalystQueryExecutor {

  val planner = new CatalystPlanner(getPlannerRule)

  val provider: String

  def getPlannerRule(): Seq[Strategy]

  def execute4Jdbc(plan: LogicalPlan): (Iterator[JdbcRow], Map[Int, Int], Map[String, Int])

  def getTableSchema: StructType

  def execute[T](plan: LogicalPlan, convert: (Option[StructType], Seq[Any]) => T): Iterator[T] = { ??? }

  def adaptorFunctionRegister(udf: UDFRegistration)

  def translate(plan: LogicalPlan): Seq[String] = Seq()
}
