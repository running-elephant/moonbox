package org.apache.spark.sql.pruner

import moonbox.core.MbSession
import org.apache.spark.sql.catalyst.analysis.{UnresolvedAttribute, UnresolvedRelation}
import org.apache.spark.sql.catalyst.plans.logical.{LeafNode, LogicalPlan, Project, UnaryNode}
import org.apache.spark.sql.catalyst.rules.Rule

class ColumnPrune(mbSession: MbSession) extends Rule[LogicalPlan] {
	private val session = mbSession.catalogSession
	override def apply(plan: LogicalPlan): LogicalPlan = {
		plan.transformUp {
			case relation@UnresolvedRelation(tableIdentifier) =>
				val databaseId = tableIdentifier.database.map(db => mbSession.catalog.getDatabase(session.organizationId, db).id.get)
					.getOrElse(session.databaseId)
				val table = mbSession.catalog.getTable(databaseId, tableIdentifier.table)
				val userTableRel = mbSession.catalog.getUserTableRel(session.userId, table.id.get)
				val columns = mbSession.catalog.getColumns(userTableRel.columns)
				if (columns.length == 1 && columns.head.name == "*") {
					relation
				} else {
					Project(columns.map(col =>UnresolvedAttribute(col.name)), relation)
				}

		}
	}
}

object ColumnPrune {
	def apply(mbSession: MbSession): ColumnPrune = new ColumnPrune(mbSession)
}