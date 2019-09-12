package org.apache.spark.sql.analyze

import moonbox.core.MoonboxCatalog
import org.apache.spark.sql.catalyst.catalog.{CatalogRelation, CatalogTable}
import org.apache.spark.sql.catalyst.expressions.AttributeSet
import org.apache.spark.sql.catalyst.plans.logical.{LogicalPlan, View}
import org.apache.spark.sql.catalyst.rules.Rule
import org.apache.spark.sql.execution.datasources.{InsertIntoDataSourceCommand, InsertIntoHadoopFsRelationCommand, LogicalRelation}
import org.apache.spark.sql.hive.execution.InsertIntoHiveTable

import scala.collection.mutable.ArrayBuffer
import scala.collection.mutable

class ColumnAnalysis(catalog: MoonboxCatalog) extends Rule[LogicalPlan] {

	override def apply(plan: LogicalPlan): LogicalPlan = {

		if (catalog.catalogUser.isSA) {
			return plan
		}

		// TODO INSERT
		val queryPlan = plan match {
			case ds: InsertIntoDataSourceCommand =>
				ds.query
			case fs: InsertIntoHadoopFsRelationCommand =>
				fs.query
			case hive: InsertIntoHiveTable =>
				hive.query
			case _ => plan
		}

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
			val dbGranted = isDatabaseGranted(db)

			tables.foreach { case (table, output) =>
				val tableName = table.identifier.table
				if (dbGranted || isOwner(db, tableName) || isTableGranted(db, tableName)) {
					availables.append(output)
				} else {
					val columnPrivilege = catalog.getColumnPrivilege(catalog.getCurrentUser, db, tableName)
					availables.append(
						output.filter(attr =>
							columnPrivilege.privilege.get(attr.name).exists(_.contains("SELECT"))
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
			throw new ColumnAnalysisException(message)
		}
		plan
	}

	private def isTableGranted(db:String, table: String): Boolean = {
		val tbPrivilege = catalog.getTablePrivilege(catalog.getCurrentUser, db, table)
		tbPrivilege.privileges.contains("SELECT")
	}

	private def isDatabaseGranted(db: String): Boolean = {
		val dbPrivilege = catalog.getDatabasePrivilege(catalog.getCurrentUser, db)
		dbPrivilege.privileges.contains("SELECT")
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
