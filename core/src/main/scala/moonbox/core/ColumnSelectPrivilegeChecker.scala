package moonbox.core

import moonbox.core.catalog.{CatalogColumn, CatalogSession, CatalogTable}
import moonbox.core.command._
import org.apache.spark.sql.catalyst.TableIdentifier
import org.apache.spark.sql.catalyst.expressions.{AttributeSet, Exists, Expression, ListQuery, ScalarSubquery}
import org.apache.spark.sql.catalyst.plans.logical._
import org.apache.spark.sql.execution.datasources.LogicalRelation
import org.apache.spark.sql.types.StructType

import scala.collection.mutable.ArrayBuffer

class TablePrivilegeManager(mbSession: MbSession, catalogTable: CatalogTable) {

	val dbPrivileges = mbSession.catalog.getDatabasePrivilege(mbSession.catalogSession.userId, catalogTable.databaseId)
	val tablePrivileges= mbSession.catalog.getTablePrivilege(mbSession.catalogSession.userId, catalogTable.databaseId, catalogTable.name)

	def insertable(): Boolean = {
		tableLevelPrivilege(InsertPrivilege.NAME)
	}

	def deletable(): Boolean = {
		tableLevelPrivilege(DeletePrivilege.NAME)
	}

	def truncatable(): Boolean = {
		tableLevelPrivilege(TruncatePrivilege.NAME)
	}

	def updatable(): Seq[CatalogColumn] = {
		columnLevelPrivileges(UpdatePrivilege.NAME)
	}

	def selectable(): Seq[CatalogColumn] = {
		columnLevelPrivileges(SelectPrivilege.NAME)
	}

	private def tableLevelPrivilege(privilegeType: String): Boolean = {
		if (catalogTable.createBy == mbSession.catalogSession.userId || !mbSession.columnPermission) {
			true
		} else if (dbPrivileges.exists(dbPriv => dbPriv.databaseId == catalogTable.databaseId &&
			dbPriv.privilegeType == privilegeType)) true
		else if (tablePrivileges.exists(tablePriv => tablePriv.databaseId == catalogTable.databaseId &&
			tablePriv.table == catalogTable.name && tablePriv.privilegeType == privilegeType)) true
		else false
	}

	private def columnLevelPrivileges(privilegeType: String): Seq[CatalogColumn] = {
		val catalogColumns = mbSession.catalog.getColumns(catalogTable.databaseId, catalogTable.name)(mbSession)
		if (catalogTable.createBy == mbSession.catalogSession.userId || !mbSession.columnPermission) {
			catalogColumns
		} else {
			val tablePrivi = tableLevelPrivilege(privilegeType)
			if (tablePrivi) catalogColumns
			else {
				val visibleColumns = mbSession.catalog.getColumnPrivilege(mbSession.catalogSession.userId, catalogTable.databaseId, catalogTable.name, privilegeType).map(_.column)
				catalogColumns.filter(column => visibleColumns.contains(column.name))
			}
		}
	}
}

object ColumnSelectPrivilegeChecker {
	def intercept(plan: LogicalPlan,
		tableIdentifierToCatalogTable: Map[TableIdentifier, CatalogTable],
		mbSession: MbSession): Unit = {
		val catalogSession = mbSession.catalogSession
		if (tableIdentifierToCatalogTable.nonEmpty) {
			val physicalColumns = new ArrayBuffer[AttributeSet]()
			val availableColumns = collectLogicalRelation(plan).flatMap { logicalRelation =>
				logicalRelation.catalogTable match {
					case Some(table) =>
						val catalogTable = tableIdentifierToCatalogTable.get(table.identifier)
						require(catalogTable.isDefined)
						physicalColumns.append(logicalRelation.references)
						if (catalogTable.get.createBy == catalogSession.userId) {
							logicalRelation.references
						} else {
							val visibleColumns = new TablePrivilegeManager(mbSession, catalogTable.get).selectable().map(_.name)
							logicalRelation.references.filter(attr => visibleColumns.contains(attr.name))
						}
					case None =>
						Seq()
				}
			}
			val attributeSet = new ArrayBuffer[AttributeSet]()

			plan.transformAllExpressions {
				case expression =>
					attributeSet.append(expression.references)
					expression
			}
			val unavailableColumns = attributeSet.reduce(_ ++ _).filter(physicalColumns.reduce(_ ++ _).contains) -- availableColumns
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
