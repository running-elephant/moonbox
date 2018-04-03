package org.apache.spark.sql.optimizer

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.catalyst.plans.logical.LogicalPlan
import org.apache.spark.sql.catalyst.rules.RuleExecutor

class MbOptimizer(sparkSession: SparkSession) extends RuleExecutor[LogicalPlan] {
	override protected def batches: Seq[Batch] = {
		Batch("pushdown", Once,
			Pushdown(sparkSession)
		) :: Nil
	}
}
