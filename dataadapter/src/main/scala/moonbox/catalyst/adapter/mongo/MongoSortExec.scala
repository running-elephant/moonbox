package moonbox.catalyst.adapter.mongo

import moonbox.catalyst.core.CatalystContext
import moonbox.catalyst.core.plan.{CatalystPlan, SortExec}
import org.apache.spark.sql.catalyst.expressions.SortOrder

class MongoSortExec(sortOrder: Seq[SortOrder],
                    global: Boolean,
                    child: CatalystPlan) extends SortExec(sortOrder: Seq[SortOrder], global: Boolean, child: CatalystPlan) with MongoTranslateSupport {
  override def translate(context: CatalystContext) = {
    val fieldAndOrders = sortOrder.map(unaryExpressionToBson).mkString("{", ", ", "}")
    child.translate(context) :+ s"{${symbolToBson("sort")}: ${fieldAndOrders}}"
  }
}
