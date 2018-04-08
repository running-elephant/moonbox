package moonbox.catalyst.adapter.mongo

import moonbox.catalyst.core.CatalystContext
import moonbox.catalyst.core.plan.{CatalystPlan, FilterExec, JoinExec}
import org.apache.spark.sql.catalyst.expressions.Expression
import org.apache.spark.sql.catalyst.plans.JoinType

class MongoJoinExec(leftKeys: Seq[Expression],
                         rightKeys: Seq[Expression],
                         joinType: JoinType,
                         condition: Option[Expression],
                         left: CatalystPlan,
                         right: CatalystPlan)
  extends JoinExec(leftKeys: Seq[Expression],
    rightKeys: Seq[Expression],
    joinType: JoinType,
    condition: Option[Expression],
    left: CatalystPlan,
    right: CatalystPlan) with MongoTranslateSupport {
  override def translate(context: CatalystContext) = {
    // TODO:
    Nil
  }
}
