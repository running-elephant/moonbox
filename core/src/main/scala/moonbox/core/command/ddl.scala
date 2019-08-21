/*-
 * <<
 * Moonbox
 * ==
 * Copyright (C) 2016 - 2019 EDP
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

package moonbox.core.command

import moonbox.catalog._
import moonbox.core.MoonboxSession
import moonbox.core.datasys.DataSystem
import org.apache.spark.sql.Row
import org.apache.spark.sql.catalyst.{FunctionIdentifier, TableIdentifier}
import org.apache.spark.sql.types.StructType

sealed trait DDL

case class MountDatabase(
	name: String,
	props: Map[String, String],
	ignoreIfExists: Boolean) extends MbRunnableCommand with DDL {

	override def run(mbSession: MoonboxSession): Seq[Row] = {
		import mbSession.catalog._

		val dataSystem = DataSystem.lookupDataSystem(props)
		// may throw test failed exception
		dataSystem.test()

		// create database
		createDatabase(
			CatalogDatabase(
			name = name,
			properties = props,
			description = None,
			isLogical = false), ignoreIfExists)

		// create table
		val tables = dataSystem.tableNames()
		tables.foreach { tbName =>
			createTable(
				CatalogTable(
					db = Some(name),
					name = tbName,
					tableType = CatalogTableType.TABLE,
					properties = dataSystem.tableProperties(tbName)
				), ignoreIfExists = true
			)
		}

		Seq.empty[Row]
	}
}

case class RefreshDatabase(name: String) extends MbRunnableCommand with DDL {

	override def run(mbSession: MoonboxSession): Seq[Row] = {
		import mbSession.catalog._

		val catalogDatabase = getDatabase(name)

		if (catalogDatabase.isLogical) {
			throw new UnsupportedOperationException("Can not refresh logical database.")
		}

		val dataSystem = DataSystem.lookupDataSystem(catalogDatabase.properties)
		// may throw test failed exception
		dataSystem.test()

		val exists = listTables(catalogDatabase.name).map(_.name)

		val dsTables = dataSystem.tableNames()

		val toRemove = exists.diff(dsTables)
		val toAdd = dsTables.diff(exists)

		toAdd.foreach {
			t => createTable(
				CatalogTable(
					name = t,
					db = Some(name),
					tableType =  CatalogTableType.TABLE,
					properties = dataSystem.tableProperties(t),
					owner = catalogDatabase.createBy
				), ignoreIfExists = true
			)
		}

		toRemove.foreach { t =>
			dropTable(name, t, ignoreIfNotExists = true)
		}

		Seq.empty[Row]
	}
}


case class AlterDatabaseSetOptions(
	name: String,
	props: Map[String, String]) extends MbRunnableCommand with DDL {

	override def run(mbSession: MoonboxSession): Seq[Row] = {
		import mbSession.catalog._
		val existDatabase = getDatabase(name)
		alterDatabase(
			existDatabase.copy(
				properties = existDatabase.properties ++ props
			)
		)
		
		Seq.empty[Row]
	}
}

case class UnmountDatabase(
	name: String,
	ignoreIfNotExists: Boolean,
	cascade: Boolean) extends MbRunnableCommand with DDL {

	override def run(mbSession: MoonboxSession): Seq[Row] = {
		import mbSession.catalog._
		val existDatabase = getDatabase(name)
		if (existDatabase.isLogical) {
			throw new UnsupportedOperationException(
				s"Database $name is logical. Please use DROP DATABASE command.")
		} else {
			mbSession.catalog.dropDatabase(
				name,
				ignoreIfNotExists,
				cascade)
			mbSession.engine.dropDatabase(
				name,
				ignoreIfNotExists = true,
				cascade)
		}

		Seq.empty[Row]
	}
}

case class CreateDatabase(
	name: String,
	comment: Option[String],
	ignoreIfExists: Boolean) extends MbRunnableCommand with DDL {

	override def run(mbSession: MoonboxSession): Seq[Row] = {

		mbSession.catalog.createDatabase(
			CatalogDatabase(
				name = name,
				description = comment,
				properties = Map(),
				isLogical = true
			), ignoreIfExists)

		Seq.empty[Row]
	}
}

case class AlterDatabaseSetName(
	name: String,
	newName: String) extends MbRunnableCommand with DDL {

	override def run(mbSession: MoonboxSession): Seq[Row] = {

		import mbSession.catalog._

		renameDatabase(
			name,
			newName)

		if (getCurrentDb.equalsIgnoreCase(name)) {
			setCurrentDb(newName)
		}

		Seq.empty[Row]
	}
}

case class AlterDatabaseSetComment(
	name: String,
	comment: String) extends MbRunnableCommand with DDL {

	override def run(mbSession: MoonboxSession): Seq[Row] = {

		val existDatabase = mbSession.catalog.getDatabase(name)
		mbSession.catalog.alterDatabase(
			existDatabase.copy(
				description = Some(comment)
			)
		)

		Seq.empty[Row]
	}
}

case class DropDatabase(
	name: String,
	ignoreIfNotExists: Boolean,
	cascade: Boolean) extends MbRunnableCommand with DDL {

	override def run(mbSession: MoonboxSession): Seq[Row] = {
		import mbSession.catalog._
		val existDatabase = getDatabase(name)
		if (!existDatabase.isLogical) {
			throw new UnsupportedOperationException(
				s"Database $name is physical. Please use UNMOUNT DATABASE command.")
		} else {
			mbSession.catalog.dropDatabase(
				name,
				ignoreIfNotExists,
				cascade)
			mbSession.engine.dropDatabase(
				name,
				ignoreIfNotExists = true,
				cascade)
		}

		// TODO if current db
		Seq.empty[Row]
	}
}

case class MountTable(
	table: TableIdentifier,
	schema: Option[StructType],
	props: Map[String, String],
	isStream: Boolean,
	ignoreIfExists: Boolean) extends MbRunnableCommand with DDL {

	override def run(mbSession: MoonboxSession): Seq[Row] = {

		import mbSession.catalog._

		val database = getDatabase(table.database.getOrElse(getCurrentDb))

		if (!database.isLogical) {
			throw new UnsupportedOperationException(
				s"Can't mount table in physical database ${database.name}")
		} else {
			// for verifying options, may throw failed exception
			DataSystem.lookupDataSystem(props).test()

			createTable(CatalogTable(
				db = Some(database.name),
				name = table.table,
				tableType = CatalogTableType.TABLE,
				description = None,
				properties = props,
				isStream = isStream
			), ignoreIfExists)
		}

		Seq.empty[Row]
	}
}

case class AlterTableSetName(
	table: TableIdentifier,
	newTable: TableIdentifier) extends MbRunnableCommand with DDL {

	override def run(mbSession: MoonboxSession): Seq[Row] = {

		import mbSession.catalog._

		require(table.database == newTable.database,
			s"Rename table cant not rename database")

		val database = mbSession.catalog.getDatabase(table.database.getOrElse(getCurrentDb))

		if (!database.isLogical) {
			throw new UnsupportedOperationException(
				"Can't rename table in physical database")
		}

		mbSession.catalog.renameTable(
			database.name,
			table.table,
			newTable.table)

		// update table in engine catalog
		if (mbSession.engine.catalog.databaseExists(database.name) && mbSession.engine.catalog.tableExists(table)) {
			mbSession.engine.catalog.getTableMetadataOption(table)
				.foreach { catalogTable =>
					// drop table first
					mbSession.engine.catalog.dropTable(
						table, ignoreIfNotExists = true, purge = true)
					// then create it
					mbSession.engine.catalog.createTable(
						catalogTable.copy(
							identifier = catalogTable.identifier.copy(table = newTable.table)
						), ignoreIfExists = true
					)
				}
		}

		Seq.empty[Row]
	}
}

case class AlterTableSetOptions(
	table: TableIdentifier,
	props: Map[String, String]) extends MbRunnableCommand with DDL {

	override def run(mbSession: MoonboxSession): Seq[Row] = {

		import mbSession.catalog._

		val dbName = table.database.getOrElse(getCurrentDb)

		val existTable = mbSession.catalog.getTable(dbName, table.table)

		if (existTable.tableType != CatalogTableType.TABLE) {
			throw new Exception(s"${existTable.name} is not a table.")
		}

		mbSession.catalog.alterTable(
			existTable.copy(
				properties = existTable.properties ++ props
			)
		)

		if (mbSession.engine.catalog.databaseExists(dbName) && mbSession.engine.catalog.tableExists(table)) {
			mbSession.engine.catalog.getTableMetadataOption(table).foreach { catalogTable =>
				mbSession.engine.catalog.alterTable(
					catalogTable.copy(
						storage = catalogTable.storage.copy(
							properties = catalogTable.storage.properties ++ props
						)
					)
				)
			}
		}

		Seq.empty[Row]
	}
}

case class UnmountTable(
	table: TableIdentifier,
	ignoreIfNotExists: Boolean) extends MbRunnableCommand with DDL {

	override def run(mbSession: MoonboxSession): Seq[Row] = {

		import mbSession.catalog._

		val dbName = table.database.getOrElse(getCurrentDb)
		val database = mbSession.catalog.getDatabase(dbName)

		if (!database.isLogical) {
			throw new UnsupportedOperationException(
				"Can't unmount table in physical database.")
		}
		mbSession.catalog.dropTable(
			dbName,
			table.table,
			ignoreIfNotExists)

		if (mbSession.engine.catalog.databaseExists(dbName)) {
			mbSession.engine.catalog
				.dropTable(table, ignoreIfNotExists = true, purge = true)
		}

		Seq.empty[Row]
	}
}

case class CreateFunction(
	function: FunctionIdentifier,
	className: String,
	methodName: Option[String],
	resources: Seq[FunctionResource],
	ignoreIfExists: Boolean) extends MbRunnableCommand with DDL {

	override def run(mbSession: MoonboxSession): Seq[Row] = {

		import mbSession.catalog._

		val database = mbSession.catalog.getDatabase(function.database.getOrElse(getCurrentDb))

		mbSession.catalog.createFunction(
			CatalogFunction(
				name = function.funcName,
				db = Some(database.name),
				description = None,
				className = className,
				methodName = methodName,
				resources = resources
			), ignoreIfExists)

		Seq.empty[Row]
	}
}

case class DropFunction(
	function: FunctionIdentifier,
	ignoreIfNotExists: Boolean) extends MbRunnableCommand with DDL {

	override def run(mbSession: MoonboxSession): Seq[Row] = {

		import mbSession.catalog._

		val database = mbSession.catalog.getDatabase(function.database.getOrElse(getCurrentDb))

		mbSession.catalog.dropFunction(
			database.name,
			function.funcName,
			ignoreIfNotExists)

		mbSession.engine.catalog.dropFunction(function, ignoreIfNotExists = true)

		Seq.empty[Row]
	}
}

case class CreateView(
	view: TableIdentifier,
	query: String,
	comment: Option[String],
	replaceIfExists: Boolean) extends MbRunnableCommand with DDL {

	override def run(mbSession: MoonboxSession): Seq[Row] = {

		import mbSession.catalog._

		val database = mbSession.catalog.getDatabase(view.database.getOrElse(getCurrentDb))

		if (!database.isLogical) {
			throw new UnsupportedOperationException(
				"Can't create view in physical database")
		}

		// TODO for checking sql syntax
		// mbSession.engine.sqlSchema(query)

		if (mbSession.catalog.tableExists(database.name, view.table) && replaceIfExists) {
			val exists = mbSession.catalog.getTable(database.name, view.table)

			if (exists.tableType != CatalogTableType.VIEW) {
				throw new UnsupportedOperationException(
					s"Can't replace table ${view.table} to view.")
			}

			mbSession.catalog.alterTable(
				exists.copy(
					viewText = Some(query),
					description = comment match {
						case Some(_) => comment
						case None => exists.description
					}
				)
			)
		} else {
			mbSession.catalog.createTable(CatalogTable(
				db = Some(database.name),
				name = view.table,
				tableType = CatalogTableType.VIEW,
				description = comment,
				viewText = Some(query)
			), ignoreIfExists = false)
		}

		Seq.empty[Row]
	}
}

case class AlterViewSetQuery(
	view: TableIdentifier,
	query: String) extends MbRunnableCommand with DDL {
	override def run(mbSession: MoonboxSession): Seq[Row] = {

		import mbSession.catalog._

		val database = mbSession.catalog.getDatabase(view.database.getOrElse(getCurrentDb))

		val existView = mbSession.catalog.getTable(database.name, view.table)

		if (existView.tableType != CatalogTableType.VIEW) {
			throw new Exception(s"${existView.name} is not a view.")
		}

		// TODO check query
		mbSession.catalog.alterTable(
			existView.copy(
				viewText = Some(query)
			)
		)

		Seq.empty[Row]
	}
}

case class DropView(
	view: TableIdentifier,
	ignoreIfNotExists: Boolean) extends MbRunnableCommand with DDL {

	override def run(mbSession: MoonboxSession): Seq[Row] = {

		import mbSession.catalog._

		val database = mbSession.catalog.getDatabase(view.database.getOrElse(getCurrentDb))

		val existView = mbSession.catalog.getTable(database.name, view.table)

		if (existView.tableType != CatalogTableType.VIEW) {
			throw new Exception(s"${existView.name} is not a view.")
		}

		mbSession.catalog.dropTable(
			database.name, view.table, ignoreIfNotExists)

		Seq.empty[Row]
	}
}

case class CreateProcedure(
	name: String,
	queryList: Seq[String],
	lang: String,
	ignoreIfExists: Boolean) extends MbRunnableCommand with DDL {

	override def run(mbSession: MoonboxSession): Seq[Row] = {

		val procedure = CatalogProcedure(
			name = name,
			sqls = queryList,
			lang = lang,
			description = None
		)
		mbSession.catalog.createProcedure(procedure, ignoreIfExists)

		Seq.empty[Row]
	}
}

case class AlterProcedureSetName(
	name: String,
	newName: String) extends MbRunnableCommand with DDL {

	override def run(mbSession: MoonboxSession): Seq[Row] = {

		mbSession.catalog.renameProcedure(name, newName)

		Seq.empty[Row]
	}
}

case class AlterProcedureSetQuery(
	name: String,
	queryList: Seq[String]) extends MbRunnableCommand with DDL {
	override def run(mbSession: MoonboxSession): Seq[Row] = {

		val procedure = mbSession.catalog.getProcedure(name)
		mbSession.catalog.alterProcedure(
			procedure.copy(
				sqls = queryList
			)
		)

		Seq.empty[Row]
	}
}

case class DropProcedure(
	name: String,
	ignoreIfNotExists: Boolean) extends MbRunnableCommand with DDL {

	override def run(mbSession: MoonboxSession): Seq[Row] = {

		mbSession.catalog.dropProcedure(name, ignoreIfNotExists)

		Seq.empty[Row]
	}
}


case class CreateTimedEvent(
	name: String,
	definer: Option[String],
	schedule: String,
	description: Option[String],
	proc: String,
	enable: Boolean,
	ignoreIfExists: Boolean) extends MbRunnableCommand with DDL {

	// TODO schedule validation
	override def run(mbSession: MoonboxSession): Seq[Row] = {

		import mbSession.catalog._

		mbSession.catalog.createTimedEvent(
			CatalogTimedEvent(
				name = name,
				definer = definer.getOrElse(getCurrentUser),
				schedule = schedule,
				enable = enable,
				description = description,
				procedure = proc
			), ignoreIfExists
		)

		Seq.empty[Row]
	}
}

case class AlterTimedEventSetName(
	name: String, newName: String) extends MbRunnableCommand with DDL {

	override def run(mbSession: MoonboxSession): Seq[Row] = {

		val timedEvent = mbSession.catalog.getTimedEvent(name)

		if (timedEvent.enable) {
			throw new Exception(s"Can't rename Event $name, while it is running.")
		}

		mbSession.catalog.renameTimedEvent(name, newName)

		Seq.empty[Row]
	}
}

case class AlterTimedEventSetDefiner(
	name: String,
	definer: Option[String]) extends MbRunnableCommand with DDL {

	override def run(mbSession: MoonboxSession): Seq[Row] = {

		import mbSession.catalog._

		val timedEvent = mbSession.catalog.getTimedEvent(name)

		if (timedEvent.enable) {
			throw new Exception(s"Can't alter definer of Event $name, while it is running.")
		}

		mbSession.catalog.alterTimedEvent(
			timedEvent.copy(definer = definer.getOrElse(getCurrentUser))
		)

		Seq.empty[Row]
	}
}

case class AlterTimedEventSetSchedule(
	name: String, schedule: String) extends MbRunnableCommand with DDL {

	override def run(mbSession: MoonboxSession): Seq[Row] = {

		val timedEvent = mbSession.catalog.getTimedEvent(name)

		if (timedEvent.enable) {
			throw new Exception(
				s"Can't alter schedule of Event $name, while it is running.")
		}

		mbSession.catalog.alterTimedEvent(
			timedEvent.copy(schedule = schedule)
		)

		Seq.empty[Row]
	}
}

case class AlterTimedEventSetEnable(
	name: String, enable: Boolean) extends MbRunnableCommand with DDL {

	override def run(mbSession: MoonboxSession): Seq[Row] = {

		val timedEvent = mbSession.catalog.getTimedEvent(name)

		mbSession.catalog.alterTimedEvent(
			timedEvent.copy(enable = enable)
		)

		Seq.empty[Row]
	}
}

case class DropTimedEvent(
	name: String,
	ignoreIfNotExists: Boolean) extends MbRunnableCommand with DDL {

	override def run(mbSession: MoonboxSession): Seq[Row] = {

		val timedEvent = mbSession.catalog.getTimedEvent(name)

		if (timedEvent.enable) {
			throw new Exception(
				s"Can't delete schedule of Event $name, while it is running.")
		}

		mbSession.catalog.dropTimedEvent(name, ignoreIfNotExists)

		Seq.empty[Row]
	}
}

