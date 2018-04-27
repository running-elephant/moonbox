package moonbox.core

import moonbox.core.catalog.{CatalogSession, CatalogTable}
import org.apache.spark.sql.catalyst.TableIdentifier
import org.apache.spark.sql.catalyst.expressions.{Exists, Expression, ListQuery, ScalarSubquery}
import org.apache.spark.sql.catalyst.plans.logical.{Filter, LogicalPlan, SubqueryAlias, With}
import org.apache.spark.sql.execution.datasources.LogicalRelation

object ColumnPrivilegeChecker {
	def intercept(plan: LogicalPlan,
		tableIdentifierToCatalogTable: Map[TableIdentifier, CatalogTable],
		catalog: CatalogContext,
		catalogSession: CatalogSession): Unit = {
		if (tableIdentifierToCatalogTable.nonEmpty) {
			val availableColumns = collectLogicalRelation(plan).flatMap { logicalRelation =>
				logicalRelation.catalogTable match {
					case Some(catalogTable) =>
						val table = tableIdentifierToCatalogTable.get(catalogTable.identifier)
						require(table.isDefined)
						val columnNames = if (table.get.createBy == catalogSession.userId) {
							catalog.getColumns(table.get.id.get)
						} else {
							val columnIds = catalog.getUserTableRel(catalogSession.userId, table.get.id.get).map(_.columnId)
							catalog.getColumns(columnIds).map(_.name)
						}
						logicalRelation.references.filter(attr => columnNames.contains(attr.name))
					case None =>
						Seq()
				}
			}
			val unavailableColumns = plan.references -- availableColumns
			if (unavailableColumns.nonEmpty)
				throw new ColumnPrivilegeException(
					s""" SELECT command denied to user ${catalogSession.userName} for column ${unavailableColumns.map(attr =>s"'${attr.name}'").mkString(", ")}""".stripMargin)

		}
	}

	private def collectLogicalRelation(plan: LogicalPlan): Seq[LogicalRelation] = {

		def traverseExpression(expr: Expression): Seq[LogicalRelation] = {
			expr.flatMap {
				case ScalarSubquery(child, _, _) => collectLogicalRelation(child)
				case Exists(child, _, _) => collectLogicalRelation(child)
				case ListQuery(child, _, _) => collectLogicalRelation(child)
				case a => a.children.flatMap(traverseExpression)
			}
		}
		plan.collect {
			case l: LogicalRelation => Seq(l)
			case With(_, cteRelations) =>
				cteRelations.flatMap {
					case (_, SubqueryAlias(_, child)) => collectLogicalRelation(child)
				}
			case Filter(condition, _) =>
				traverseExpression(condition)
		}.flatten


	}
}
