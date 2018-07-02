package org.apache.spark.sql.optimizer

import moonbox.core.datasys.DataSystem
import org.apache.spark.sql.catalyst.plans.logical.LogicalPlan

case class MbTreeNode(plan: LogicalPlan, dataSystem: DataSystem, dependencies: Seq[MbDependency]) {

}
