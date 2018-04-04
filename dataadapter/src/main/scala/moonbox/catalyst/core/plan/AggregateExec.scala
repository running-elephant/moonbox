package moonbox.catalyst.core.plan

import org.apache.spark.sql.catalyst.expressions.{Attribute, Expression, NamedExpression}

abstract case class AggregateExec(groupingExpressions: Seq[Expression],
								  aggregateExpressions: Seq[NamedExpression],
								  child: CatalystPlan) extends UnaryExecNode {
	override def output: Seq[Attribute] = child.output
}
