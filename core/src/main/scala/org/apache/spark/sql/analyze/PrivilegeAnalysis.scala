package org.apache.spark.sql.analyze

import moonbox.core.MoonboxCatalog
import moonbox.core.command.{ColumnSelectPrivilege, InsertPrivilege, SelectPrivilege}
import org.apache.spark.sql.catalyst.TableIdentifier
import org.apache.spark.sql.catalyst.catalog.{CatalogRelation, CatalogTable}
import org.apache.spark.sql.catalyst.expressions.AttributeSet
import org.apache.spark.sql.catalyst.plans.logical.{LogicalPlan, View}
import org.apache.spark.sql.catalyst.rules.Rule
import org.apache.spark.sql.execution.datasources.{InsertIntoDataSourceCommand, InsertIntoHadoopFsRelationCommand, LogicalRelation}
import org.apache.spark.sql.hive.execution.InsertIntoHiveTable

import scala.collection.mutable.ArrayBuffer
import scala.collection.mutable

class PrivilegeAnalysis(catalog: MoonboxCatalog) extends Rule[LogicalPlan] {

	override def apply(plan: LogicalPlan): LogicalPlan = {

		if (catalog.catalogUser.isSA) {
			return plan
		}

		val (queryPlan, insertTable) = plan match {
			case ds: InsertIntoDataSourceCommand =>
				(ds.query, ds.logicalRelation.catalogTable.map(_.identifier))
			case fs: InsertIntoHadoopFsRelationCommand =>
				(fs.query, fs.catalogTable.map(_.identifier))
			case hive: InsertIntoHiveTable =>
				(hive.query, Some(hive.table.identifier))
			case _ =>
				(plan, None)
		}

		// insert
		insertTable.foreach(checkInsert)
		// select
		checkSelect(queryPlan)

		plan
	}

	private def checkInsert(identifier: TableIdentifier) = {
		val db = identifier.database.getOrElse(catalog.getCurrentDb)
		val tb = identifier.table
		val canInsert = isOwner(db, tb) || isDatabaseGranted(db, InsertPrivilege.NAME) || isTableGranted(db, tb, InsertPrivilege.NAME)
		if (!canInsert) {
			val message = s"""INSERT denied to user ${catalog.getCurrentUser} for table $db.$tb"""
			throw new PrivilegeAnalysisException(message)
		}
	}

	private def checkSelect(queryPlan: LogicalPlan) = {
		// database level
		val tableOrView = new mutable.HashSet[(CatalogTable, AttributeSet)]

		val references = new ArrayBuffer[AttributeSet]()

		val availables = new ArrayBuffer[AttributeSet]()

		foreach(queryPlan) {
			// do not change case order
			case view: View =>
				tableOrView.add((normalizeIdentifier(view.desc), view.references))

			// view never match this case
			case p if p.children.nonEmpty =>
				references.append(p.references)

			case lr: LogicalRelation =>
				lr.catalogTable.foreach(t =>
					tableOrView.add((normalizeIdentifier(t), lr.references)))

			case cr: CatalogRelation =>
				tableOrView.add((normalizeIdentifier(cr.tableMeta), cr.references))

			case _ =>
		}

		tableOrView.groupBy(_._1.database).foreach { case (db, tables) =>
			val dbGranted = isDatabaseGranted(db, SelectPrivilege.NAME)

			tables.foreach { case (table, output) =>
				val tableName = table.identifier.table
				if (dbGranted || isOwner(db, tableName) || isTableGranted(db, tableName, SelectPrivilege.NAME)) {
					availables.append(output)
				} else {
					val columnPrivilege = catalog.getColumnPrivilege(catalog.getCurrentUser, db, tableName)
					availables.append(
						output.filter(attr =>
							columnPrivilege.privilege.get(attr.name).exists(_.contains(SelectPrivilege.NAME))
						)
					)
				}
			}
		}

		val nonProduced = tableOrView.map(_._2).foldLeft(AttributeSet.empty)(_ ++ _)

		val unavailables = references.foldLeft(AttributeSet.empty)(_ ++ _).filter(nonProduced.contains) -- availables.foldLeft(AttributeSet.empty)(_ ++ _)
		if (unavailables.nonEmpty) {
			val columns = unavailables.map(attr => attr.name).mkString(", ")
			val message = s"""SELECT denied to user ${catalog.getCurrentUser} for columns $columns"""
			throw new PrivilegeAnalysisException(message)
		}
	}

	private def isTableGranted(db:String, table: String, privilege: String): Boolean = {
		val tbPrivilege = catalog.getTablePrivilege(catalog.getCurrentUser, db, table)
		tbPrivilege.privileges.contains(privilege)
	}

	private def isDatabaseGranted(db: String, privilege: String): Boolean = {
		val dbPrivilege = catalog.getDatabasePrivilege(catalog.getCurrentUser, db)
		dbPrivilege.privileges.contains(privilege)
	}

	private def isOwner(db: String, table: String): Boolean = {
		val owner = catalog.getTable(db, table).owner
		owner.map(_.toLowerCase).contains(catalog.getCurrentUser.toLowerCase)
	}

	private def normalizeIdentifier(catalogTable: CatalogTable): CatalogTable = {
		val ident = catalogTable.identifier
		val normalizedIdent = ident.copy(database = Some(ident.database.getOrElse(catalog.getCurrentDb)))
		catalogTable.copy(identifier = normalizedIdent)
	}

	private def foreach(plan: LogicalPlan)(f: LogicalPlan => Unit): Unit = {
		f(plan)
		plan match {
			case view: View =>
			case p => plan.children.foreach(foreach(_)(f))
		}
	}
}
