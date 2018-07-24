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

import moonbox.catalyst.core.plan.{CatalystPlan, LeafExecNode}
import moonbox.common.MbLogging
import org.apache.spark.sql.catalyst.expressions.Attribute
import org.apache.spark.sql.catalyst.planning.{GenericStrategy, QueryPlanner}
import org.apache.spark.sql.catalyst.plans.logical.LogicalPlan


abstract class Strategy extends GenericStrategy[CatalystPlan] {
	override protected def planLater(plan: LogicalPlan): CatalystPlan = PlanLater(plan)
}

case class PlanLater(plan: LogicalPlan) extends LeafExecNode {

	override def output: Seq[Attribute] = plan.output

	override def translate(context: CatalystContext): Seq[String] = throw new UnsupportedOperationException
}

abstract class CatalystStrategies extends QueryPlanner[CatalystPlan] with MbLogging { self: CatalystPlanner =>
	override protected def collectPlaceholders(plan: CatalystPlan): Seq[(CatalystPlan, LogicalPlan)] = {
		plan.collect {
			case placeholder @ PlanLater(logicalPlan) => placeholder -> logicalPlan
		}
	}

	override protected def prunePlans(plans: Iterator[CatalystPlan]): Iterator[CatalystPlan] = plans

    override def plan(plan: LogicalPlan): Iterator[CatalystPlan] = {
        // Obviously a lot to do here still...

        logDebug("plan: " + plan)
        // Collect physical plan candidates.
        val candidatesPlan: Seq[CatalystPlan] = strategies.flatMap{sfunc =>
            val seq = sfunc(plan)
            seq}

        val candidates: Iterator[CatalystPlan] = candidatesPlan.iterator

        val plans: Iterator[CatalystPlan] = candidates.flatMap { candidate =>  //CatalystPlan
            val placeholders: Seq[(CatalystPlan, LogicalPlan)]  = collectPlaceholders(candidate)

            placeholders.map(e => logDebug(e._2.toString()))

            if (placeholders.isEmpty) {
                // Take the candidate as is because it does not contain placeholders.
                Iterator(candidate)  //CatalystPlan
            } else {
                placeholders.iterator.foldLeft(Iterator(candidate)) {     // Plan the logical plan marked as [[planLater]] and replace the placeholders.
                    case (candidatesWithPlaceholders, (placeholder, logicalPlan)) =>
                        // Plan the logical plan for the placeholder.
                        val childPlans = this.plan(logicalPlan)

                        candidatesWithPlaceholders.flatMap { candidateWithPlaceholders =>
                            childPlans.map { childPlan =>
                                // Replace the placeholder by the child plan
                                candidateWithPlaceholders.transformUp {
                                    case p if p == placeholder => childPlan
                                }
                            }
                        }
                }
            }
        }

        val pruned = prunePlans(plans)
        //assert(pruned.hasNext, s"No plan for $plan")
        pruned
    }

}
