package moonbox.catalyst.adapter.elasticsearch5.plan

import moonbox.catalyst.core.CatalystContext
import moonbox.catalyst.core.plan.{CatalystPlan, LimitExec}

import scala.collection.mutable


class EsLimitExec(limit: Int, child: CatalystPlan) extends LimitExec(limit, child) {

    override def translate(context: CatalystContext): Seq[String] = {
        val seq: Seq[String] = child.translate(context)
        context.limitSize = limit
        seq ++ Seq(toJson)
    }

    //---body-----
    def toJson: String = {  // es max req size is 10000
        if(limit <= 10000) {
            s""" "from": 0, "size": $limit """
        }else{
            s""" "from": 0, "size": 10000 """
        }
    }
}


object EsLimitExec{
    def apply(limit: Int, child: CatalystPlan): EsLimitExec = {
        new EsLimitExec(limit, child)
    }
}
