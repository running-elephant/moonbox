/*-
 * <<
 * Moonbox
 * ==
 * Copyright (C) 2016 - 2018 EDP
 * ==
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * >>
 */

package moonbox.core

import moonbox.core.catalog.{CatalogColumn, CatalogTable}
import moonbox.core.command._
import moonbox.core.datasys.DataSystem
import moonbox.core.execution.standalone.DataTable
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.catalyst.catalog.HiveTableRelation
import org.apache.spark.sql.catalyst.expressions.{AttributeSet, Exists, Expression, ListQuery, ScalarSubquery}
import org.apache.spark.sql.catalyst.plans.logical._
import org.apache.spark.sql.execution.datasources.LogicalRelation

import scala.collection.mutable.ArrayBuffer

class TablePrivilegeManager(mbSession: MbSession, catalogTable: CatalogTable) {
	val physicalTableName = DataSystem.lookupDataSystem(catalogTable.properties).tableName()
	val dbPrivileges = mbSession.catalog.getDatabasePrivilege(mbSession.catalogSession.userId, catalogTable.databaseId)
	val tablePrivileges = mbSession.catalog.getTablePrivilege(mbSession.catalogSession.userId, catalogTable.databaseId, physicalTableName)

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
			tablePriv.table == physicalTableName && tablePriv.privilegeType == privilegeType)) true
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
				val visibleColumns = mbSession.catalog.getColumnPrivilege(mbSession.catalogSession.userId, catalogTable.databaseId, physicalTableName, privilegeType).map(_.column)
				catalogColumns.filter(column => visibleColumns.contains(column.name))
			}
		}
	}
}

object TableInsertPrivilegeChecker {
	def intercept(mbSession: MbSession, catalogTable: CatalogTable, dataTable: DataTable): DataTable = {
		if (mbSession.columnPermission) {
			val manager = new TablePrivilegeManager(mbSession, catalogTable)
			if (manager.insertable()) {
				dataTable
			} else {
				throw new TableInsertPrivilegeException(s"Table ${catalogTable.name} is not writable.")
			}
		} else {
			dataTable
		}
	}
	def intercept(mbSession: MbSession, catalogTable: CatalogTable, dataFrame: DataFrame): DataFrame = {
		if (mbSession.columnPermission) {
			val manager = new TablePrivilegeManager(mbSession, catalogTable)
			if (manager.insertable()) {
				dataFrame
			} else {
				throw new TableInsertPrivilegeException(s"Table ${catalogTable.name} is not writable.")
			}
		} else {
			dataFrame
		}
	}
}

object ColumnSelectPrivilegeChecker {
	def intercept(plan: LogicalPlan,
		mbSession: MbSession): Unit = {
		val catalogSession = mbSession.catalogSession
		// TODO
		val physicalColumns = new ArrayBuffer[AttributeSet]()
		val availableColumns = collectRelation(plan).flatMap { relation =>
			val tableMate = if (relation.isInstanceOf[LogicalRelation]) {
				relation.asInstanceOf[LogicalRelation].catalogTable
			} else {
				Some(relation.asInstanceOf[HiveTableRelation].tableMeta)
			}
			tableMate match {
				case Some(table) =>
					val catalogTable = mbSession.getCatalogTable(table.identifier.table, table.identifier.database)//tableIdentifierToCatalogTable.get(table.identifier)
					physicalColumns.append(relation.references)
					if (catalogTable.createBy == catalogSession.userId) {
						relation.references
					} else {
						val visibleColumns = new TablePrivilegeManager(mbSession, catalogTable).selectable().map(_.name)
						relation.references.filter(attr => visibleColumns.contains(attr.name))
					}
				case None =>
					Seq()
			}
		}
		val attributeSet = new ArrayBuffer[AttributeSet]()

		plan.foreach {
			case project: Project =>
				attributeSet.append(project.projectList.map(_.references):_*)
			case aggregate: Aggregate =>
				attributeSet.append(aggregate.aggregateExpressions.map(_.references):_*)
				attributeSet.append(aggregate.groupingExpressions.map(_.references):_*)
			case other =>
		}

		val unavailableColumns: AttributeSet = if (physicalColumns.isEmpty) { // for `select literal`
			attributeSet.reduce(_ ++ _) -- availableColumns
		} else {
			attributeSet.reduce(_ ++ _).filter(physicalColumns.reduce(_ ++ _).contains) -- availableColumns
		}

		if (unavailableColumns.nonEmpty) {
			throw new ColumnSelectPrivilegeException(
				s""" SELECT command denied to user ${catalogSession.userName} for column ${unavailableColumns.map(attr => s"'${attr.name}'").mkString(", ")}""".stripMargin)
		}
	}

	private def collectRelation(plan: LogicalPlan): Seq[LogicalPlan] = {

		def traverseExpression(expr: Expression): Seq[LogicalPlan] = {
			expr.flatMap {
				case ScalarSubquery(child, _, _) => collectRelation(child)
				case Exists(child, _, _) => collectRelation(child)
				case ListQuery(child, _, _) => collectRelation(child)
				case a => a.children.flatMap(traverseExpression)
			}
		}
		plan.collect {
			case l: LogicalRelation => Seq(l)
			case c: HiveTableRelation => Seq(c)
			case project: Project =>
				project.projectList.flatMap(traverseExpression)
			case aggregate: Aggregate =>
				aggregate.aggregateExpressions.flatMap(traverseExpression)
			case With(_, cteRelations) =>
				cteRelations.flatMap {
					case (_, SubqueryAlias(_, child)) => collectRelation(child)
				}
			case Filter(condition, _) =>
				traverseExpression(condition)
		}.flatten
	}
}
