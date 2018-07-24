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

package moonbox.catalyst.core.plan

import moonbox.catalyst.core.CatalystContext
import org.apache.spark.sql.catalyst.expressions.AttributeSet
import org.apache.spark.sql.catalyst.plans.QueryPlan



abstract class CatalystPlan extends QueryPlan[CatalystPlan]{
	//def translate(): String
	//def translate(): Seq[String] = Nil
	def translate(context: CatalystContext): Seq[String]
}

trait LeafExecNode extends CatalystPlan {
	override final def children: Seq[CatalystPlan] = Nil
	override def producedAttributes: AttributeSet = {
        println("output" + output)
        outputSet
    }
}

trait UnaryExecNode extends CatalystPlan {
	def child: CatalystPlan

	override final def children: Seq[CatalystPlan] = child :: Nil
}

trait BinaryExecNode extends CatalystPlan {
	def left: CatalystPlan
	def right: CatalystPlan

	override final def children: Seq[CatalystPlan] = Seq(left, right)
}
