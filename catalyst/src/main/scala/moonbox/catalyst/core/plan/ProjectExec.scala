package moonbox.catalyst.core.plan

import org.apache.spark.sql.catalyst.expressions.{Attribute, NamedExpression}

abstract case class ProjectExec(projectList: Seq[NamedExpression], child: CatalystPlan) extends UnaryExecNode {
	override def output: Seq[Attribute] = projectList.map(_.toAttribute)
}
