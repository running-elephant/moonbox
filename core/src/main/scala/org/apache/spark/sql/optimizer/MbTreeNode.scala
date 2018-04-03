package org.apache.spark.sql.optimizer

import org.apache.spark.sql.catalyst.plans.logical.LogicalPlan
import org.apache.spark.sql.datasys.DataSystem

case class MbTreeNode(plan: LogicalPlan, dataSystem: DataSystem, dependencies: Seq[MbDependency]) {

}
