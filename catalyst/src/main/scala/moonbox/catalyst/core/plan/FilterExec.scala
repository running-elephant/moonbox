package moonbox.catalyst.core.plan

import org.apache.spark.sql.catalyst.expressions.{Attribute, Expression}

abstract case class FilterExec(condition: Expression, child: CatalystPlan)
	extends UnaryExecNode {
	override def output: Seq[Attribute] = child.output
}
