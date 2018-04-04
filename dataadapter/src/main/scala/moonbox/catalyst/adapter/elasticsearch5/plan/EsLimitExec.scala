package moonbox.catalyst.adapter.elasticsearch5.plan

import moonbox.catalyst.core.CatalystContext
import moonbox.catalyst.core.plan.{CatalystPlan, LimitExec}

import scala.collection.mutable


class EsLimitExec(limit: Int, child: CatalystPlan) extends LimitExec(limit, child) {

    override def translate(context: CatalystContext): Seq[String] = {
        val seq: Seq[String] = child.translate(context)
        context.hasLimited = true
        seq ++ Seq(toJson)
    }

    //---body-----
    def toJson: String = {
        s""" "from": 0, "size": $limit """
    }
}


object EsLimitExec{
    def apply(limit: Int, child: CatalystPlan): EsLimitExec = {
        new EsLimitExec(limit, child)
    }
}
