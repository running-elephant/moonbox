package moonbox.catalyst.core.plan

import moonbox.catalyst.core.CatalystContext
import org.apache.spark.sql.catalyst.expressions.AttributeSet
import org.apache.spark.sql.catalyst.plans.QueryPlan



abstract class CatalystPlan extends QueryPlan[CatalystPlan]{
	//def translate(): String
	//def translate(): Seq[String] = Nil
	def translate(context: CatalystContext): Seq[String]
}

trait LeafExecNode extends CatalystPlan {
	override final def children: Seq[CatalystPlan] = Nil
	override def producedAttributes: AttributeSet = {
        println("output" + output)
        outputSet
    }
}

trait UnaryExecNode extends CatalystPlan {
	def child: CatalystPlan

	override final def children: Seq[CatalystPlan] = child :: Nil
}

trait BinaryExecNode extends CatalystPlan {
	def left: CatalystPlan
	def right: CatalystPlan

	override final def children: Seq[CatalystPlan] = Seq(left, right)
}
