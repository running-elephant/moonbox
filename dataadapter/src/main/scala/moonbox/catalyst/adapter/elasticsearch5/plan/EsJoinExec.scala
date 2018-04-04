package moonbox.catalyst.adapter.elasticsearch5.plan

import moonbox.catalyst.core.CatalystContext
import moonbox.catalyst.core.plan.{CatalystPlan, JoinExec}
import org.apache.spark.sql.catalyst.expressions.Expression
import org.apache.spark.sql.catalyst.plans._

import scala.collection.mutable

class EsJoinExec(leftKeys: Seq[Expression],
                 rightKeys: Seq[Expression],
                 joinType: JoinType,
                 condition: Option[Expression],
                 left: CatalystPlan,
                 right: CatalystPlan) extends JoinExec(leftKeys, rightKeys, joinType, condition, left, right) {

    override def translate(context: CatalystContext): Seq[String] = {
        val seq1: Seq[String] = left.translate(context)
        val seq2: Seq[String] = right.translate(context)
        seq2 ++ seq1 ++ Seq.empty[String]
    }
}

object EsJoinExec {
    def apply(leftKeys: Seq[Expression],
              rightKeys: Seq[Expression],
              joinType: JoinType,
              condition: Option[Expression],
              left: CatalystPlan,
              right: CatalystPlan): EsJoinExec = {
        new EsJoinExec(leftKeys, rightKeys, joinType, condition, left, right)
    }
}
