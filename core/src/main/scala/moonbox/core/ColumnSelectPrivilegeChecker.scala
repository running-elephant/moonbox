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

import moonbox.catalog._
import moonbox.core.command._
import moonbox.core.datasys.DataTable
import org.apache.spark.sql.catalyst.expressions.{AttributeSet, Exists, Expression, ListQuery, ScalarSubquery}
import org.apache.spark.sql.catalyst.plans.logical._
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.catalyst.catalog.CatalogRelation
import org.apache.spark.sql.execution.datasources.LogicalRelation

import scala.collection.mutable.ArrayBuffer

class TablePrivilegeManager {
	private var mbSession: MbSession = _
	private var catalogView: CatalogView = _
	private var catalogTable: CatalogTable = _
	private var dbPrivileges: Seq[CatalogDatabasePrivilege] = _
	private var tablePrivileges: Seq[CatalogTablePrivilege] = _
	private var isView: Boolean = _

	def this(mbSession: MbSession, catalogView: CatalogView) = {
		this()
		this.mbSession = mbSession
		this.catalogView = catalogView
		this.isView = true
		this.dbPrivileges = mbSession.catalog.getDatabasePrivilege(
			mbSession.userContext.userId, catalogView.databaseId)
		this.tablePrivileges = mbSession.catalog.getTablePrivilege(
			mbSession.userContext.userId, catalogView.databaseId, catalogView.name)
	}

	def this(mbSession: MbSession, catalogTable: CatalogTable) = {
		this()
		this.mbSession = mbSession
		this.catalogTable = catalogTable
		this.isView = false
		this.dbPrivileges = mbSession.catalog.getDatabasePrivilege(
			mbSession.userContext.userId, catalogTable.databaseId)
		this.tablePrivileges = mbSession.catalog.getTablePrivilege(
			mbSession.userContext.userId, catalogTable.databaseId, catalogTable.name)
	}
	//val physicalTableName = DataSystem.lookupDataSystem(catalogTable.properties).tableName()

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
		if (isView) {
			if (catalogView.createBy == mbSession.userContext.userId || !mbSession.columnPermission) {
				true
			} else if (dbPrivileges.exists(dbPriv => dbPriv.databaseId == catalogView.databaseId &&
				dbPriv.privilegeType == privilegeType)) true
			else if (tablePrivileges.exists(tablePriv => tablePriv.databaseId == catalogView.databaseId &&
				tablePriv.table.equalsIgnoreCase(catalogView.name) && tablePriv.privilegeType == privilegeType)) true
			else false
		} else {
			if (catalogTable.createBy == mbSession.userContext.userId || !mbSession.columnPermission) {
				true
			} else if (dbPrivileges.exists(dbPriv => dbPriv.databaseId == catalogTable.databaseId &&
				dbPriv.privilegeType == privilegeType)) true
			else if (tablePrivileges.exists(tablePriv => tablePriv.databaseId == catalogTable.databaseId &&
				tablePriv.table.equalsIgnoreCase(catalogTable.name) && tablePriv.privilegeType == privilegeType)) true
			else false
		}
	}

	private def columnLevelPrivileges(privilegeType: String): Seq[CatalogColumn] = {
		if (isView) {
			val catalogColumns = mbSession.schema(catalogView.id.get, catalogView.name, catalogView.cmd)
			if (catalogView.createBy == mbSession.userContext.userId || !mbSession.columnPermission) {
				catalogColumns
			} else {
				val tablePrivi = tableLevelPrivilege(privilegeType)
				if (tablePrivi) catalogColumns
				else {
					val visibleColumns = mbSession.catalog.getColumnPrivilege(
						mbSession.userContext.userId, catalogView.databaseId, catalogView.name, privilegeType).map(_.column)
					catalogColumns.filter(column => visibleColumns.contains(column.name))
				}
			}
		} else {
			val catalogColumns = mbSession.schema(catalogTable.databaseId, catalogTable.name)
			if (catalogTable.createBy == mbSession.userContext.userId || !mbSession.columnPermission) {
				catalogColumns
			} else {
				val tablePrivi = tableLevelPrivilege(privilegeType)
				if (tablePrivi) catalogColumns
				else {
					val visibleColumns = mbSession.catalog.getColumnPrivilege(
						mbSession.userContext.userId, catalogTable.databaseId, catalogTable.name, privilegeType).map(_.column)
					catalogColumns.filter(column => visibleColumns.contains(column.name))
				}
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
	def intercept(plan: LogicalPlan, mbSession: MbSession): Unit = {
		val catalogSession = mbSession.userContext
		val physicalColumns = new ArrayBuffer[AttributeSet]()
		val availableColumns = collectRelationAndView(plan).flatMap { source =>
			val (catalog, isView) = source match {
				case LogicalRelation(_, _, catalogTable) =>
					(catalogTable, false)
				case CatalogRelation(tableMeta, _, _) =>
					(Some(tableMeta), false)
				case View(catalogTable, output, _) =>
					(Some(catalogTable), true)
				case _ => (None, false)
			}
			catalog match {
				case Some(table) =>
					val databaseId = table.identifier.database match {
						case None =>
							catalogSession.databaseId
						case Some(databaseName) =>
							val database = mbSession.catalog.getDatabase(catalogSession.organizationId, databaseName)
							database.id.get
					}
					if (isView) {
						val catalogView = mbSession.catalog.getView(databaseId, table.identifier.table)
						physicalColumns.append(source.references)
						if (catalogView.createBy == catalogSession.userId) {
							source.references
						} else {
							val visibleColumns = new TablePrivilegeManager(mbSession, catalogView).selectable().map(_.name)
							source.references.filter(attr => visibleColumns.contains(attr.name))
						}
					} else {
						val catalogTable = mbSession.getCatalogTable(table.identifier.table, table.identifier.database)//tableIdentifierToCatalogTable.get(table.identifier)
						physicalColumns.append(source.references)
						if (catalogTable.createBy == catalogSession.userId) {
							source.references
						} else {
							val visibleColumns = new TablePrivilegeManager(mbSession, catalogTable).selectable().map(_.name)
							source.references.filter(attr => visibleColumns.contains(attr.name))
						}
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
			case c: CatalogRelation => Seq(c)
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

	private def collectRelationAndView(plan: LogicalPlan): Seq[LogicalPlan] = {

		def traverseExpression(expr: Expression): Seq[LogicalPlan] = {
			expr.flatMap {
				case ScalarSubquery(child, _, _) => collectRelation(child)
				case Exists(child, _, _) => collectRelation(child)
				case ListQuery(child, _, _) => collectRelation(child)
				case a => a.children.flatMap(traverseExpression)
			}
		}

		def collectFirst(plan: LogicalPlan)(pf: PartialFunction[LogicalPlan, Seq[LogicalPlan]]): Seq[LogicalPlan] = {
			val res = pf.apply(plan)
			if (res.nonEmpty) {
				res
			} else {
				plan.children.foldLeft(Seq[LogicalPlan]()) { (l, r) =>
					l ++ collectFirst(r)(pf)
				}
			}
		}

		collectFirst(plan) {
			case l: LogicalRelation => Seq(l)
			case c: CatalogRelation => Seq(c)
			case v: View => Seq(v)
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
			case _ => Seq()
		}
	}
}
