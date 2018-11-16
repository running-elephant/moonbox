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

package org.apache.spark.sql.hive

import org.apache.spark.sql.{SparkSession, Strategy}
import org.apache.spark.sql.catalyst.catalog.CatalogRelation
import org.apache.spark.sql.catalyst.expressions.{AttributeSet, Expression}
import org.apache.spark.sql.catalyst.planning.PhysicalOperation
import org.apache.spark.sql.catalyst.plans.logical.LogicalPlan
import org.apache.spark.sql.execution.SparkPlan

case class HiveTableScan(sparkSession: SparkSession) extends Strategy {
	def apply(plan: LogicalPlan): Seq[SparkPlan] = plan match {
		case PhysicalOperation(projectList, predicates, relation: CatalogRelation) =>
			// Filter out all predicates that only deal with partition keys, these are given to the
			// hive table scan operator to be used for partition pruning.
			val partitionKeyIds = AttributeSet(relation.partitionCols)
			val (pruningPredicates, otherPredicates) = predicates.partition { predicate =>
				!predicate.references.isEmpty &&
					predicate.references.subsetOf(partitionKeyIds)
			}

			sparkSession.sessionState.planner.pruneFilterProject(
				projectList,
				otherPredicates,
				identity[Seq[Expression]],
				HiveTableScanExec(_, relation, pruningPredicates)(sparkSession)) :: Nil
		case _ =>
			Nil
	}
}
