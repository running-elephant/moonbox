package org.apache.spark.sql.datasys

import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.catalyst.expressions.ScalaUDF
import org.apache.spark.sql.catalyst.plans.JoinType
import org.apache.spark.sql.catalyst.plans.logical._
import org.apache.spark.sql.execution.aggregate.ScalaUDAF

abstract class DataSystem(props: Map[String, String]) {
	val name: String

	protected val supportedOperators: Seq[Class[_]]
	protected val supportedJoinTypes: Seq[JoinType]
	protected val supportedExpressions: Seq[Class[_]]
	protected val beGoodAtOperators: Seq[Class[_]]
	protected val supportedUDF: Seq[String]

	def isGoodAt(operator: Class[_]): Boolean = {
		beGoodAtOperators.contains(operator)
	}

	protected def isSupportAll: Boolean

	def isSupport(plan: LogicalPlan): Boolean = {
		isSupportAll || {(plan match {
				case join: Join =>
					supportedOperators.contains(plan.getClass) && supportedJoinTypes.contains(join.joinType)
				case _ =>
					supportedOperators.contains(plan.getClass)
		}) && plan.expressions.forall {
			case udf: ScalaUDF =>
				udf.udfName match {
					case Some(name) =>
						supportedExpressions.contains(udf.getClass) && supportedUDF.contains(name)
					case None => false
				}
			case udaf: ScalaUDAF => false
			case expression => supportedExpressions.contains(expression.getClass)
		}
		}
	}

	def fastEquals(other: DataSystem): Boolean

	protected def contains(params: String*): Boolean = params.forall(props.contains)

	def buildScan(plan: LogicalPlan): DataFrame


}
