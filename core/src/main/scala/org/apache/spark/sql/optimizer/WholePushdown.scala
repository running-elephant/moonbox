package org.apache.spark.sql.optimizer

import org.apache.spark.sql.catalyst.expressions.Attribute
import org.apache.spark.sql.catalyst.plans.logical.{LogicalPlan, UnaryNode}
import org.apache.spark.sql.datasys.Queryable

case class WholePushdown(child: LogicalPlan, queryable: Queryable) extends UnaryNode {
	override def output: Seq[Attribute] = child.output
}
