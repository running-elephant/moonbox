package moonbox.core

import moonbox.core.catalog.{CatalogSession, CatalogTable}
import org.apache.spark.sql.catalyst.TableIdentifier
import org.apache.spark.sql.catalyst.expressions.{AttributeSet, Exists, Expression, ListQuery, ScalarSubquery}
import org.apache.spark.sql.catalyst.plans.logical._
import org.apache.spark.sql.execution.datasources.LogicalRelation

import scala.collection.mutable.ArrayBuffer

object ColumnPrivilegeChecker {
	def intercept(plan: LogicalPlan,
		tableIdentifierToCatalogTable: Map[TableIdentifier, CatalogTable],
		mbSession: MbSession): Unit = {
		val catalog = mbSession.catalog
		val catalogSession = mbSession.catalogSession
		if (tableIdentifierToCatalogTable.nonEmpty) {
			val availableColumns = collectLogicalRelation(plan).flatMap { logicalRelation =>
				logicalRelation.catalogTable match {
					case Some(catalogTable) =>
						val table = tableIdentifierToCatalogTable.get(catalogTable.identifier)
						require(table.isDefined)
						val columnNames = if (table.get.createBy == catalogSession.userId) {
							catalog.getColumns(table.get.databaseId, table.get.name)(mbSession).map(_.name)
						} else {
							catalog.getUserTableRels(catalogSession.userId, table.get.databaseId, table.get.name).map(_.column)
						}
						logicalRelation.references.filter(attr => columnNames.contains(attr.name))
					case None =>
						Seq()
				}
			}
			val attributeSet = new ArrayBuffer[AttributeSet]()

			plan.transformAllExpressions {
				case expression =>
					attributeSet.append(expression.references)
					attributeSet.reduce(_ ++ _)
					expression
			}
			val unavailableColumns = attributeSet.reduce(_ ++ _) -- availableColumns
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
			case project: Project =>
				project.projectList.flatMap(traverseExpression)
			case aggregate: Aggregate =>
				aggregate.aggregateExpressions.flatMap(traverseExpression)
			case With(_, cteRelations) =>
				cteRelations.flatMap {
					case (_, SubqueryAlias(_, child)) => collectLogicalRelation(child)
				}
			case Filter(condition, _) =>
				traverseExpression(condition)
		}.flatten


	}
}
