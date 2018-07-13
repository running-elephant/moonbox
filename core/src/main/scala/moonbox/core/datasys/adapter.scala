package moonbox.core.datasys

import moonbox.core.execution.standalone.DataTable
import org.apache.spark.sql.catalyst.expressions.{Expression, ScalaUDF}
import org.apache.spark.sql.catalyst.plans.JoinType
import org.apache.spark.sql.catalyst.plans.logical.{Join, LogicalPlan}
import org.apache.spark.sql.execution.aggregate.ScalaUDAF
import org.apache.spark.sql.{DataFrame, SaveMode, SparkSession}

trait Pushdownable { self: DataSystem =>
	val supportedOperators: Seq[Class[_]]
	val supportedJoinTypes: Seq[JoinType]
	val supportedExpressions: Seq[Class[_]]
	val beGoodAtOperators: Seq[Class[_]]
	val supportedUDF: Seq[String]

	def isGoodAt(operator: Class[_]): Boolean = {
		beGoodAtOperators.contains(operator)
	}

	def isSupportAll: Boolean

	def isSupport(plan: LogicalPlan): Boolean = {
		isSupportAll || {(plan match {
			case join: Join =>
				supportedOperators.contains(plan.getClass) && supportedJoinTypes.contains(join.joinType)
			case _ =>
				supportedOperators.contains(plan.getClass)
		}) && plan.expressions.forall {
			case udf: ScalaUDF =>
				udf.udfName match {
					case Some(udfName) =>
						supportedExpressions.contains(udf.getClass) && supportedUDF.contains(udfName)
					case None => false
				}
			case udaf: ScalaUDAF => false
			case expression => supportedExpressions.contains(expression.getClass)
		}
		}
	}

	def fastEquals(other: DataSystem): Boolean

	def buildScan(plan: LogicalPlan, sparkSession: SparkSession): DataFrame

	def buildQuery(plan: LogicalPlan): DataTable
}

trait Insertable {
	def insert(table: DataTable, saveMode: SaveMode): Unit
}

trait Truncatable {
    def truncate(): Unit
}

trait Deletable {
	def delete(key: Any, condition: Expression): Unit
}

trait Updatable {
	def update(key: Any, values: Seq[(String, String)]): Unit
}