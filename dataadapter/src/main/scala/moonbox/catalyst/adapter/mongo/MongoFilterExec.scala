package moonbox.catalyst.adapter.mongo

import moonbox.catalyst.core.CatalystContext
import moonbox.catalyst.core.plan.{CatalystPlan, FilterExec}
import org.apache.spark.sql.catalyst.expressions.Expression

class MongoFilterExec(condition: Expression, child: CatalystPlan) extends FilterExec(condition: Expression, child: CatalystPlan) with MongoTranslateSupport {
  override def translate(context: CatalystContext) = {
    val filterBson = expressionToBson(condition)
    val matchBson = "{$match: " + filterBson + "}"
    child.translate(context) :+ matchBson
  }
}
