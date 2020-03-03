package org.apache.spark.sql.analyze

import moonbox.core.MoonboxCatalog
import org.apache.spark.sql.catalyst.plans.logical.LogicalPlan
import org.apache.spark.sql.catalyst.rules.RuleExecutor

class MbAnalyzer(catalog: MoonboxCatalog) extends RuleExecutor[LogicalPlan]{

	lazy val batches: Seq[Batch] = Seq(
		Batch("Post-Hoc Resolution", Once, new PrivilegeAnalysis(catalog))
	)

}
