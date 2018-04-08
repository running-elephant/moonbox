package moonbox.catalyst.core.plan

import org.apache.spark.sql.catalyst.expressions.{Attribute, SortOrder}

abstract case class SortExec(sortOrder: Seq[SortOrder],
							 global: Boolean,
							 child: CatalystPlan) extends UnaryExecNode {
	override def output: Seq[Attribute] = child.output
}
