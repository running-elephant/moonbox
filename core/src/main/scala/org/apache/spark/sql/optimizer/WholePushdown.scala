package org.apache.spark.sql.optimizer

import org.apache.spark.sql.catalyst.expressions.Attribute
import org.apache.spark.sql.catalyst.plans.logical.{LogicalPlan, UnaryNode}
import moonbox.core.datasys.Pushdownable

case class WholePushdown(child: LogicalPlan, queryable: Pushdownable) extends UnaryNode {
	override def output: Seq[Attribute] = child.output
}
