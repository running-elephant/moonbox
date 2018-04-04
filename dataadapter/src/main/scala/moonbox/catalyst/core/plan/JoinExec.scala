package moonbox.catalyst.core.plan

import org.apache.spark.sql.catalyst.expressions.{Attribute, Expression}
import org.apache.spark.sql.catalyst.plans.{ExistenceJoin, LeftExistence, _}

abstract case class JoinExec(leftKeys: Seq[Expression],
							 rightKeys: Seq[Expression],
							 joinType: JoinType,
							 condition: Option[Expression],
							 left: CatalystPlan,
							 right: CatalystPlan) extends BinaryExecNode {
	override def output: Seq[Attribute] = {
		joinType match {
			case _: InnerLike =>
				left.output ++ right.output
			case LeftOuter =>
				left.output ++ right.output.map(_.withNullability(true))
			case RightOuter =>
				left.output.map(_.withNullability(true)) ++ right.output
			case FullOuter =>
				(left.output ++ right.output).map(_.withNullability(true))
			case j: ExistenceJoin =>
				left.output :+ j.exists
			case LeftExistence(_) =>
				left.output
			case x =>
				throw new IllegalArgumentException(
					s"${getClass.getSimpleName} should not take $x as the JoinType")
		}
	}
}
