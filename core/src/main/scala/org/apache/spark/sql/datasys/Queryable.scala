package org.apache.spark.sql.datasys

import moonbox.core.execution.standalone.DataTable
import org.apache.spark.sql.catalyst.plans.logical.LogicalPlan

trait Queryable {
	def buildQuery(plan: LogicalPlan): DataTable
}

trait Insertable {
	def insert(table: DataTable): Unit
}
