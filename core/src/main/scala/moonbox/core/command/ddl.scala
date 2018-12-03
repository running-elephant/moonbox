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

package moonbox.core.command

import moonbox.common.util.Utils
import moonbox.core.catalog._
import moonbox.core.datasys.DataSystem
import moonbox.core.{MbFunctionIdentifier, MbSession, MbTableIdentifier}
import org.apache.spark.sql.Row
import org.apache.spark.sql.catalyst.TableIdentifier
import org.apache.spark.sql.types.StructType

sealed trait DDL

case class MountDatabase(
	name: String,
	props: Map[String, String],
	ignoreIfExists: Boolean) extends MbRunnableCommand with DDL {

	override def run(mbSession: MbSession)(implicit ctx: CatalogSession): Seq[Row] = {
		val validate = DataSystem.lookupDataSystem(props).test()
		if (!validate) {
			throw new Exception("Can't connect to the database. Please check your connecting parameters.")
		} else {
			val catalogDatabase = CatalogDatabase(
				name = name,
				properties = props,
				description = None,
				organizationId = ctx.organizationId,
				isLogical = false,
				createBy = ctx.userId,
				updateBy = ctx.userId
			)
			mbSession.catalog.createDatabase(catalogDatabase, ctx.organizationName, ignoreIfExists)
		}
		Seq.empty[Row]
	}
}

case class AlterDatabaseSetOptions(
	name: String,
	props: Map[String, String]) extends MbRunnableCommand with DDL {

	override def run(mbSession: MbSession)(implicit ctx: CatalogSession): Seq[Row] = {
		val existDatabase = mbSession.catalog.getDatabase(ctx.organizationId, name)
		if (existDatabase.isLogical) {
			throw new UnsupportedOperationException(s"Logical database $name can not be set properties")
		} else {
			mbSession.catalog.alterDatabase(
				existDatabase.copy(
					properties = existDatabase.properties ++ props,
					updateBy = ctx.userId,
					updateTime = Utils.now
				)
			)
		}
		Seq.empty[Row]
	}
}

case class UnmountDatabase(
	name: String,
	ignoreIfNotExists: Boolean) extends MbRunnableCommand with DDL {

	override def run(mbSession: MbSession)(implicit ctx: CatalogSession): Seq[Row] = {
		val existDatabase = mbSession.catalog.getDatabase(ctx.organizationId, name)
		if (existDatabase.isLogical) {
			throw new UnsupportedOperationException(s"Database $name is logical. Please use DROP DATABASE command.")
		} else {
			mbSession.catalog.dropDatabase(ctx.organizationId, ctx.organizationName, name, ignoreIfNotExists, cascade = false)
		}
		Seq.empty[Row]
	}
}

case class CreateDatabase(
	name: String,
	comment: Option[String],
	ignoreIfExists: Boolean) extends MbRunnableCommand with DDL {

	override def run(mbSession: MbSession)(implicit ctx: CatalogSession): Seq[Row] = {
		val catalogDatabase = CatalogDatabase(
			name = name,
			description = comment,
			organizationId = ctx.organizationId,
			properties = Map(),
			isLogical = true,
			createBy = ctx.userId,
			updateBy = ctx.userId
		)
		mbSession.catalog.createDatabase(catalogDatabase, ctx.organizationName, ignoreIfExists)
		Seq.empty[Row]
	}
}

case class AlterDatabaseSetName(
	name: String,
	newName: String) extends MbRunnableCommand with DDL {

	override def run(mbSession: MbSession)(implicit ctx: CatalogSession): Seq[Row] = {
		mbSession.catalog.renameDatabase(ctx.organizationId, ctx.organizationName, name, newName, ctx.userId)
		ctx.databaseName = newName
		Seq.empty[Row]
	}
}

case class AlterDatabaseSetComment(
	name: String,
	comment: String) extends MbRunnableCommand with DDL {

	override def run(mbSession: MbSession)(implicit ctx: CatalogSession): Seq[Row] = {
		val existDatabase: CatalogDatabase = mbSession.catalog.getDatabase(ctx.organizationId, name)
		mbSession.catalog.alterDatabase(
			existDatabase.copy(
				description = Some(comment),
				updateBy = ctx.userId,
				updateTime = Utils.now
			)
		)
		Seq.empty[Row]
	}
}

case class DropDatabase(
	name: String,
	ignoreIfNotExists: Boolean,
	cascade: Boolean) extends MbRunnableCommand with DDL {

	override def run(mbSession: MbSession)(implicit ctx: CatalogSession): Seq[Row] = {
		val existDatabase = mbSession.catalog.getDatabase(ctx.organizationId, name)
		if (!existDatabase.isLogical) {
			throw new UnsupportedOperationException(s"Database $name is physical. Please use UNMOUNT DATABASE command.")
		} else {
			mbSession.catalog.dropDatabase(ctx.organizationId, ctx.organizationName, name, ignoreIfNotExists, cascade)
		}
		Seq.empty[Row]
	}
}

case class MountTable(
	table: MbTableIdentifier,
	schema: Option[StructType],
	props: Map[String, String],
	isStream: Boolean,
	ignoreIfExists: Boolean) extends MbRunnableCommand with DDL {

	// TODO column privilege refactor
	override def run(mbSession: MbSession)(implicit ctx: CatalogSession): Seq[Row] = {
		val (databaseId, database, isLogical)= table.database match {
			case Some(db) =>
				val currentDatabase: CatalogDatabase = mbSession.catalog.getDatabase(ctx.organizationId, db)
				(currentDatabase.id.get, currentDatabase.name, currentDatabase.isLogical)
			case None =>
				(ctx.databaseId, ctx.databaseName, ctx.isLogical)
		}
		if (!isLogical) {
			throw new UnsupportedOperationException(s"Can't mount table in physical database $database")
		} else {
			// for verifying options
			val validate = DataSystem.lookupDataSystem(props).test()
			if (!validate) {
				throw new Exception("Can't connect to the database. Please check your connecting parameters.")
			} else {
//				mbSession.mixcal.registerTable(TableIdentifier(table.table, Some(database)), props)

				val catalogTable = CatalogTable(
					name = table.table,
					description = None,
					databaseId = databaseId,
					properties = props,
					isStream = isStream,
					createBy = ctx.userId,
					updateBy = ctx.userId
				)
				mbSession.catalog.createTable(catalogTable, ctx.organizationName, database, ignoreIfExists)
			}
		}
		Seq.empty[Row]
	}
}

case class AlterTableSetName(
	table: MbTableIdentifier,
	newTable: MbTableIdentifier) extends MbRunnableCommand with DDL {

	override def run(mbSession: MbSession)(implicit ctx: CatalogSession): Seq[Row] = {
		require(table.database == newTable.database, s"Rename table cant not rename database")
		val (databaseId, database, isLogical) = table.database match {
			case Some(db) =>
				val currentDatabase: CatalogDatabase = mbSession.catalog.getDatabase(ctx.organizationId, db)
				(currentDatabase.id.get, currentDatabase.name, currentDatabase.isLogical)
			case None =>
				(ctx.databaseId, ctx.databaseName, ctx.isLogical)
		}
		if (!isLogical) {
			throw new UnsupportedOperationException("Can't rename table in physical database")
		}
		mbSession.catalog.renameTable(databaseId, ctx.organizationName, database, table.table, newTable.table, ctx.userId)
		Seq.empty[Row]
	}
}

case class AlterTableSetOptions(
	table: MbTableIdentifier,
	props: Map[String, String]) extends MbRunnableCommand with DDL {
	override def run(mbSession: MbSession)(implicit ctx: CatalogSession): Seq[Row] = {
		val (databaseId, isLogical) = table.database match {
			case Some(db) =>
				val currentDatabase: CatalogDatabase = mbSession.catalog.getDatabase(ctx.organizationId, db)
				(currentDatabase.id.get, currentDatabase.isLogical)
			case None =>
				(ctx.databaseId, ctx.isLogical)
		}
		if (!isLogical) {
			throw new UnsupportedOperationException("Can't alter table options in physical database.")
		}
		val existTable: CatalogTable = mbSession.catalog.getTable(databaseId, table.table)
		mbSession.catalog.alterTable(
			existTable.copy(
				properties = existTable.properties ++ props,
				updateBy = ctx.userId,
				updateTime = Utils.now
			)
		)
		Seq.empty[Row]
	}
}

/*case class AlterTableAddColumns(
	table: MbTableIdentifier,
	columns: StructType) extends MbRunnableCommand with DDL {
	override def run(mbSession: MbSession)(implicit ctx: CatalogSession): Seq[Row] = {
		Seq.empty[Row]
	}
}

case class AlterTableChangeColumn(
	table: MbTableIdentifier,
	column: StructField) extends MbRunnableCommand with DDL {
	override def run(mbSession: MbSession)(implicit ctx: CatalogSession): Seq[Row] = {
		Seq.empty[Row]
	}
}

case class AlterTableDropColumn(
	table: MbTableIdentifier,
	column: String) extends MbRunnableCommand with DDL {
	override def run(mbSession: MbSession)(implicit ctx: CatalogSession): Seq[Row] = {
		Seq.empty[Row]
	}
}*/

case class UnmountTable(
	table: MbTableIdentifier,
	ignoreIfNotExists: Boolean) extends MbRunnableCommand with DDL {

	override def run(mbSession: MbSession)(implicit ctx: CatalogSession): Seq[Row] = {
		val (databaseId, database, isLogical) = table.database match {
			case Some(db) =>
				val currentDatabase: CatalogDatabase = mbSession.catalog.getDatabase(ctx.organizationId, db)
				(currentDatabase.id.get, currentDatabase.name, currentDatabase.isLogical)
			case None =>
				(ctx.databaseId, ctx.databaseName, ctx.isLogical)
		}
		if (!isLogical) {
			throw new UnsupportedOperationException("Can't unmount table in physical database.")
		}
		mbSession.catalog.dropTable(databaseId, ctx.organizationName, database, table.table, ignoreIfNotExists)
		Seq.empty[Row]
	}
}

case class CreateFunction(
	function: MbFunctionIdentifier,
	className: String,
	methodName: Option[String],
	resources: Seq[FunctionResource],
	ignoreIfExists: Boolean) extends MbRunnableCommand with DDL {

	override def run(mbSession: MbSession)(implicit ctx: CatalogSession): Seq[Row] = {
		val (databaseId, database) = function.database match {
			case Some(db) =>
				val currentDatabase: CatalogDatabase = mbSession.catalog.getDatabase(ctx.organizationId, db)
				(currentDatabase.id.get, currentDatabase.name)
			case None =>
				(ctx.databaseId, ctx.databaseName)
		}
		mbSession.catalog.createFunction(
			CatalogFunction(
				name = function.func,
				databaseId = databaseId,
				description = None,
				className = className,
				methodName = methodName,
				resources = resources,
				createBy = ctx.userId,
				updateBy = ctx.userId
			), ctx.organizationName, database, ignoreIfExists
		)
		Seq.empty[Row]
	}
}

/*case class AlterFunctionSetName(
	function: MbFunctionIdentifier,
	newFunction: MbFunctionIdentifier) extends MbRunnableCommand with DDL {
	override def run(mbSession: MbSession)(implicit ctx: CatalogSession): Seq[Row] = {
		require(function.database == function.database, s"Rename function cant not rename database")
		val (databaseId, database) = function.database match {
			case Some(db) =>
				val currentDatabase: CatalogDatabase = mbSession.catalog.getDatabase(ctx.organizationId, db)
				(currentDatabase.id.get, currentDatabase.name)
			case None =>
				(ctx.databaseId, ctx.databaseName)
		}
		mbSession.catalog.renameFunction(databaseId,
			ctx.organizationName, database, function.func, newFunction.func, ctx.userId)
		Seq.empty[Row]
	}
}

case class AlterFunctionSetOptions(
	function: MbFunctionIdentifier,
	props: Map[String, String]) extends MbRunnableCommand with DDL {
	override def run(mbSession: MbSession)(implicit ctx: CatalogSession): Seq[Row] = {
		// TODO
		val databaseId = function.database match {
			case Some(db) =>
				val currentDatabase: CatalogDatabase = mbSession.catalog.getDatabase(ctx.organizationId, db)
				currentDatabase.id.get
			case None =>
				ctx.databaseId
		}
		val existFunction = mbSession.catalog.getFunction(databaseId, function.func)
		mbSession.catalog.alterFunction(
			existFunction.copy(
				updateBy = ctx.userId,
				updateTime = Utils.now
			)
		)
		Seq.empty[Row]
	}
}*/

case class DropFunction(
	function: MbFunctionIdentifier,
	ignoreIfNotExists: Boolean) extends MbRunnableCommand with DDL {
	override def run(mbSession: MbSession)(implicit ctx: CatalogSession): Seq[Row] = {
		val (databaseId, database) = function.database match {
			case Some(db) =>
				val currentDatabase: CatalogDatabase = mbSession.catalog.getDatabase(ctx.organizationId, db)
				(currentDatabase.id.get, currentDatabase.name)
			case None =>
				(ctx.databaseId, ctx.databaseName)
		}
		mbSession.catalog.dropFunction(databaseId, ctx.organizationName, database, function.func, ignoreIfNotExists)
		Seq.empty[Row]
	}
}

case class CreateView(
	view: MbTableIdentifier,
	query: String,
	comment: Option[String],
	ignoreIfExists: Boolean) extends MbRunnableCommand with DDL {

	override def run(mbSession: MbSession)(implicit ctx: CatalogSession): Seq[Row] = {
		val (databaseId, database)= view.database match {
			case Some(db) =>
				val currentDatabase: CatalogDatabase = mbSession.catalog.getDatabase(ctx.organizationId, db)
				(currentDatabase.id.get, currentDatabase.name)
			case None =>
				(ctx.databaseId, ctx.databaseName)
		}
		val catalogView = CatalogView(
			name = view.table,
			databaseId = databaseId,
			description = comment,
			cmd = query,
			createBy = ctx.userId,
			updateBy = ctx.userId
		)
		mbSession.catalog.createView(catalogView, ctx.organizationName, database, ignoreIfExists)
		Seq.empty[Row]
	}
}

case class AlterViewSetName(
	view: MbTableIdentifier,
	newView: MbTableIdentifier) extends MbRunnableCommand with DDL {
	override def run(mbSession: MbSession)(implicit ctx: CatalogSession): Seq[Row] = {
		require(view.database == newView.database, s"Rename view cant not rename database")
		val (databaseId, database)= view.database match {
			case Some(db) =>
				val currentDatabase: CatalogDatabase = mbSession.catalog.getDatabase(ctx.organizationId, db)
				(currentDatabase.id.get, currentDatabase.name)
			case None =>
				(ctx.databaseId, ctx.databaseName)
		}
		mbSession.catalog.renameView(databaseId, ctx.organizationName, database, view.table, newView.table, ctx.userId)
		Seq.empty[Row]
	}
}

case class AlterViewSetComment(
	view: MbTableIdentifier,
	comment: String) extends MbRunnableCommand with DDL {
	override def run(mbSession: MbSession)(implicit ctx: CatalogSession): Seq[Row] = {
		val databaseId = view.database match {
			case Some(db) =>
				val currentDatabase: CatalogDatabase = mbSession.catalog.getDatabase(ctx.organizationId, db)
				currentDatabase.id.get
			case None =>
				ctx.databaseId
		}
		val existView: CatalogView = mbSession.catalog.getView(databaseId, view.table)
		mbSession.catalog.alterView(
			existView.copy(
				description = Some(comment),
				updateBy = ctx.userId,
				updateTime = Utils.now
			)
		)
		Seq.empty[Row]
	}
}

case class AlterViewSetQuery(
	view: MbTableIdentifier,
	query: String) extends MbRunnableCommand with DDL {
	override def run(mbSession: MbSession)(implicit ctx: CatalogSession): Seq[Row] = {
		val databaseId = view.database match {
			case Some(db) =>
				val currentDatabase: CatalogDatabase = mbSession.catalog.getDatabase(ctx.organizationId, db)
				currentDatabase.id.get
			case None =>
				ctx.databaseId
		}
		val existView: CatalogView = mbSession.catalog.getView(databaseId, view.table)
		mbSession.catalog.alterView(
			existView.copy(
				cmd = query,
				updateBy = ctx.userId,
				updateTime = Utils.now
			)
		)
		Seq.empty[Row]
	}
}

case class DropView(
	view: MbTableIdentifier,
	ignoreIfNotExists: Boolean) extends MbRunnableCommand with DDL {
	override def run(mbSession: MbSession)(implicit ctx: CatalogSession): Seq[Row] = {
		val (databaseId, database)= view.database match {
			case Some(db) =>
				val currentDatabase: CatalogDatabase = mbSession.catalog.getDatabase(ctx.organizationId, db)
				(currentDatabase.id.get, currentDatabase.name)
			case None =>
				(ctx.databaseId, ctx.databaseName)
		}
		mbSession.catalog.dropView(databaseId, ctx.organizationName, database, view.table, ignoreIfNotExists)
		Seq.empty[Row]
	}
}

case class CreateProcedure(
	name: String,
	queryList: Seq[String],
	ignoreIfExists: Boolean) extends MbRunnableCommand with DDL {

	override def run(mbSession: MbSession)(implicit ctx: CatalogSession): Seq[Row] = {
		val catalogProcedure = CatalogProcedure(
			name = name,
			cmds = queryList,
			organizationId = ctx.organizationId,
			description = None,
			createBy = ctx.userId,
			updateBy = ctx.userId
		)
		mbSession.catalog.createProcedure(catalogProcedure, ctx.organizationName, ignoreIfExists)
		Seq.empty[Row]
	}
}

case class AlterProcedureSetName(
	name: String,
	newName: String) extends MbRunnableCommand with DDL {

	override def run(mbSession: MbSession)(implicit ctx: CatalogSession): Seq[Row] = {
		mbSession.catalog.renameProcedure(ctx.organizationId, ctx.organizationName, name, newName, ctx.userId)
		Seq.empty[Row]
	}
}

case class AlterProcedureSetQuery(
	name: String,
	queryList: Seq[String]) extends MbRunnableCommand with DDL {
	override def run(mbSession: MbSession)(implicit ctx: CatalogSession): Seq[Row] = {
		val existProcedure: CatalogProcedure = mbSession.catalog.getProcedure(ctx.organizationId, name)
		mbSession.catalog.alterProcedure(
			existProcedure.copy(
				cmds = queryList,
				updateBy = ctx.userId,
				updateTime = Utils.now
			)
		)
		Seq.empty[Row]
	}
}

case class DropProcedure(
	name: String,
	ignoreIfNotExists: Boolean) extends MbRunnableCommand with DDL {
	override def run(mbSession: MbSession)(implicit ctx: CatalogSession): Seq[Row] = {
		mbSession.catalog.dropProcedure(ctx.organizationId, ctx.organizationName, name, ignoreIfNotExists)
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
	override def run(mbSession: MbSession)(implicit ctx: CatalogSession): Seq[Row] = {
		val definerId = definer.map(user => mbSession.catalog.getUser(ctx.organizationId, user).id.get).getOrElse(ctx.userId)
		val appId = mbSession.catalog.getProcedure(ctx.organizationId, proc).id.get
		mbSession.catalog.createTimedEvent(
			CatalogTimedEvent(
				name = name,
				organizationId = ctx.organizationId,
				definer = definerId,
				schedule = schedule,
				enable = enable,
				description = description,
				procedure = appId,
				createBy = ctx.userId,
				updateBy = ctx.userId
			), ctx.organizationName, ignoreIfExists
		)
		Seq.empty[Row]
	}
}

case class AlterTimedEventSetName(
	name: String, newName: String) extends MbRunnableCommand with DDL {
	override def run(mbSession: MbSession)(implicit ctx: CatalogSession): Seq[Row] = {
		val timedEvent = mbSession.catalog.getTimedEvent(ctx.organizationId, name)
		if (timedEvent.enable) {
			throw new Exception(s"Can't rename Event $name, while it is running.")
		}
		mbSession.catalog.renameTimedEvent(ctx.organizationId, ctx.organizationName, name, newName, ctx.userId)
		Seq.empty[Row]
	}
}

case class AlterTimedEventSetDefiner(
	name: String,
	definer: Option[String]) extends MbRunnableCommand with DDL {
	override def run(mbSession: MbSession)(implicit ctx: CatalogSession): Seq[Row] = {
		val timedEvent = mbSession.catalog.getTimedEvent(ctx.organizationId, name)
		if (timedEvent.enable) {
			throw new Exception(s"Can't alter definer of Event $name, while it is running.")
		}
		val newDefinerId = mbSession.catalog.getUser(ctx.organizationId, definer.getOrElse(ctx.userName)).id.get
		mbSession.catalog.alterTimedEvent(
			timedEvent.copy(definer = newDefinerId)
		)
		Seq.empty[Row]
	}
}

case class AlterTimedEventSetSchedule(
	name: String, schedule: String) extends MbRunnableCommand with DDL {
	override def run(mbSession: MbSession)(implicit ctx: CatalogSession): Seq[Row] = {
		val timedEvent = mbSession.catalog.getTimedEvent(ctx.organizationId, name)
		if (timedEvent.enable) {
			throw new Exception(s"Can't alter schedule of Event $name, while it is running.")
		}
		mbSession.catalog.alterTimedEvent(
			timedEvent.copy(schedule = schedule)
		)
		Seq.empty[Row]
	}
}

case class AlterTimedEventSetEnable(
	name: String, enable: Boolean) extends MbRunnableCommand with DDL {
	override def run(mbSession: MbSession)(implicit ctx: CatalogSession): Seq[Row] = {
		val timedEvent = mbSession.catalog.getTimedEvent(ctx.organizationId, name)
		mbSession.catalog.alterTimedEvent(
			timedEvent.copy(enable = enable)
		)
		Seq.empty[Row]
	}
}

case class DropTimedEvent(
	name: String,
	ignoreIfNotExists: Boolean) extends MbRunnableCommand with DDL {
	override def run(mbSession: MbSession)(implicit ctx: CatalogSession): Seq[Row] = {
		val timedEvent = mbSession.catalog.getTimedEvent(ctx.organizationId, name)
		if (timedEvent.enable) {
			throw new Exception(s"Can't delete schedule of Event $name, while it is running.")
		}
		mbSession.catalog.dropTimedEvent(ctx.organizationId, ctx.organizationName, name, ignoreIfNotExists)
		Seq.empty[Row]
	}
}

