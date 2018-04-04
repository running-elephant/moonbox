package moonbox.catalyst.adapter.mongo

import moonbox.catalyst.core.CatalystContext
import moonbox.catalyst.core.plan.{CatalystPlan, LimitExec}

class MongoLimitExec(limit: Int, child: CatalystPlan) extends LimitExec(limit: Int, child: CatalystPlan) with MongoTranslateSupport {
  override def translate(context: CatalystContext) = {
    child.translate(context) :+ s"{${symbolToBson("limit")}: ${limit}}"
  }
}
