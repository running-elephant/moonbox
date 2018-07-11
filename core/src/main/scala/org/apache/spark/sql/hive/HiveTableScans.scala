package org.apache.spark.sql.hive

import org.apache.spark.sql.{SparkSession, Strategy}
import org.apache.spark.sql.catalyst.catalog.CatalogRelation
import org.apache.spark.sql.catalyst.expressions.{AttributeSet, Expression}
import org.apache.spark.sql.catalyst.planning.PhysicalOperation
import org.apache.spark.sql.catalyst.plans.logical.LogicalPlan
import org.apache.spark.sql.execution.SparkPlan
import org.apache.spark.sql.hive.execution.HiveTableScanExec

case class HiveTableScans(sparkSession: SparkSession) extends Strategy {
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
