package org.apache.spark.sql.rewrite

import org.apache.spark.sql.catalyst.analysis.UnresolvedRelation
import org.apache.spark.sql.catalyst.expressions.SubqueryExpression
import org.apache.spark.sql.catalyst.plans.logical.{LogicalPlan, With}
import org.apache.spark.sql.catalyst.rules.Rule

object CTESubstitution extends Rule[LogicalPlan] {
	def apply(plan: LogicalPlan): LogicalPlan = plan.transformUp {
		case With(child, relations) =>
			substituteCTE(child, relations.foldLeft(Seq.empty[(String, LogicalPlan)]) {
				case (resolved, (name, relation)) =>
					resolved :+ name -> substituteCTE(relation, resolved)
			})
		case other => other
	}

	def substituteCTE(plan: LogicalPlan, cteRelations: Seq[(String, LogicalPlan)]): LogicalPlan = {
		plan transformDown {
			case u : UnresolvedRelation =>
				cteRelations.find(x => x._1.equalsIgnoreCase(u.tableIdentifier.table))
					.map(_._2).getOrElse(u)
			case other =>
				// This cannot be done in ResolveSubquery because ResolveSubquery does not know the CTE.
				other transformExpressions {
					case e: SubqueryExpression =>
						e.withNewPlan(substituteCTE(e.plan, cteRelations))
				}
		}
	}
}
