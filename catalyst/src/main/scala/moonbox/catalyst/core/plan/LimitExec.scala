package moonbox.catalyst.core.plan

import org.apache.spark.sql.catalyst.expressions.Attribute

abstract case class LimitExec(limit: Int, child: CatalystPlan) extends UnaryExecNode {
	override def output: Seq[Attribute] = child.output
}
