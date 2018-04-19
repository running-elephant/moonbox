package org.apache.spark.sql.pruner

import moonbox.core.MbSession
import org.apache.spark.sql.catalyst.plans.logical.LogicalPlan
import org.apache.spark.sql.catalyst.rules.RuleExecutor

class MbPruner(mbSession: MbSession) extends RuleExecutor[LogicalPlan] {
	override protected def batches: Seq[Batch] = {
		Batch("column permission", Once,
			ColumnPrune(mbSession)
		) :: Nil
	}
}

