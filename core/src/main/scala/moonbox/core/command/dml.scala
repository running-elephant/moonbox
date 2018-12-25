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
import moonbox.catalog._
import moonbox.core._
import org.apache.spark.sql.Row
import org.apache.spark.sql.catalyst.errors.TreeNodeException
import org.apache.spark.sql.catalyst.expressions.{Attribute, AttributeReference}
import org.apache.spark.sql.optimizer.WholePushdown
import org.apache.spark.sql.types.StringType

import scala.collection.mutable.ArrayBuffer

sealed trait DML

case class UseDatabase(db: String) extends MbRunnableCommand with DML {
	override def run(mbSession: MbSession)(implicit ctx: UserContext): Seq[Row] = {
		val currentDb = mbSession.catalog.getDatabase(ctx.organizationId, db)
		ctx.databaseId = currentDb.id.get
		ctx.databaseName = currentDb.name
		ctx.isLogical = currentDb.isLogical
		if (!mbSession.mixcal.sparkSession.sessionState.catalog.databaseExists(currentDb.name)) {
			mbSession.mixcal.sqlToDF(s"create database if not exists ${currentDb.name}")
		}
		mbSession.mixcal.sparkSession.catalog.setCurrentDatabase(ctx.databaseName)
		Seq.empty[Row]
	}
}

case class SetVariable(name: String, value: String, isGlobal: Boolean)
	extends MbRunnableCommand with DML {
	override def run(mbSession: MbSession)(implicit ctx: UserContext): Seq[Row] = {
		// TODO
		if (isGlobal) {
			throw new UnsupportedOperationException("Set global configuration doesn't support now.")
		} else {
			mbSession.setVariable(name, value)
		}
		Seq.empty[Row]
	}
}

case class ShowVariables(pattern: Option[String]) extends MbRunnableCommand with DML {

	override def output: Seq[Attribute] = {
		AttributeReference("KEY", StringType, nullable = false)() ::
		AttributeReference("VALUE", StringType, nullable = false)() :: Nil
	}

	override def run(mbSession: MbSession)(implicit ctx: UserContext): Seq[Row] = {
		val variables = pattern.map { p =>
			mbSession.getVariables.filterKeys(key =>
				Utils.escapeLikeRegex(p).r.pattern.matcher(key).matches()).toSeq
		}.getOrElse {
			mbSession.getVariables.toSeq
		}
		val sortedVariables = variables.sortWith { case ((k1, _), (k2, _)) => k1 < k2}
		sortedVariables.map { case (k, v) =>
			Row(k, v)
		}.foldRight[List[Row]](Nil) { case (elem, res) =>
			elem :: res
		}
	}
}

case class ShowDatabases(
	pattern: Option[String]) extends MbRunnableCommand with DML {

	override def output: Seq[Attribute] = {
		AttributeReference("DATABASE_NAME", StringType, nullable = false)() :: Nil
	}

	override def run(mbSession: MbSession)(implicit ctx: UserContext): Seq[Row] = {
		val databases = pattern.map { p =>
			mbSession.catalog.listDatabase(ctx.organizationId, p)
		}.getOrElse(mbSession.catalog.listDatabase(ctx.organizationId))
		databases.map { d => Row(d.name)}
	}
}

case class ShowTables(
	database: Option[String],
	pattern: Option[String]) extends MbRunnableCommand with DML {

	override def output = {
		AttributeReference("TABLE_NAME", StringType, nullable = false)() :: Nil
	}

	override def run(mbSession: MbSession)(implicit ctx: UserContext): Seq[Row] = {
		val databaseId = database.map(db => mbSession.catalog.getDatabase(ctx.organizationId, db).id.get)
		    .getOrElse(ctx.databaseId)

		val tables = pattern.map { p =>
			mbSession.catalog.listTables(databaseId, p)
		}.getOrElse(mbSession.catalog.listTables(databaseId)).map(_.name)

		val views = pattern.map { p =>
			mbSession.catalog.listViews(databaseId, p)
		}.getOrElse(mbSession.catalog.listViews(databaseId)).map(_.name)
		(tables ++ views).map { t =>  Row(t)}
	}
}

case class ShowViews(
	database: Option[String],
	pattern: Option[String]) extends MbRunnableCommand with DML {

	override def output = {
		AttributeReference("VIEW_NAME", StringType, nullable = false)() :: Nil
	}

	override def run(mbSession: MbSession)(implicit ctx: UserContext): Seq[Row] = {
		val databaseId = database.map(db => mbSession.catalog.getDatabase(ctx.organizationId, db).id.get)
			.getOrElse(ctx.databaseId)
		val views = pattern.map { p =>
			mbSession.catalog.listViews(databaseId, p)
		}.getOrElse(mbSession.catalog.listViews(databaseId))
		views.map { v => Row(v.name) }
	}
}

case class ShowFunctions(
	database: Option[String],
	pattern: Option[String]) extends MbRunnableCommand with DML {

	override def output = {
		AttributeReference("FUNCTION_NAME", StringType, nullable = false)() :: Nil
	}

	override def run(mbSession: MbSession)(implicit ctx: UserContext): Seq[Row] = {
		val databaseId = database.map(db => mbSession.catalog.getDatabase(ctx.organizationId, db).id.get)
			.getOrElse(ctx.databaseId)
		val functions = pattern.map { p =>
			mbSession.catalog.listFunctions(databaseId, p)
		}.getOrElse(mbSession.catalog.listFunctions(databaseId))
		functions.map { f => Row(f.name) }
	}
}

case class ShowUsers(
	pattern: Option[String]) extends MbRunnableCommand with DML {

	override def output = {
		AttributeReference("USER_NAME", StringType, nullable = false)() :: Nil
	}

	override def run(mbSession: MbSession)(implicit ctx: UserContext): Seq[Row] = {
		val users = pattern.map { p =>
			mbSession.catalog.listUsers(ctx.organizationId, p)
		}.getOrElse(mbSession.catalog.listUsers(ctx.organizationId))
		users.map { u => Row(u.name) }
	}
}

case class ShowGroups(
	pattern: Option[String]) extends MbRunnableCommand with DML {

	override def output = {
		AttributeReference("GROUP_NAME", StringType, nullable = false)() :: Nil
	}
	override def run(mbSession: MbSession)(implicit ctx: UserContext): Seq[Row] = {
		val groups = pattern.map { p =>
			mbSession.catalog.listGroups(ctx.organizationId, p)
		}.getOrElse(mbSession.catalog.listGroups(ctx.organizationId))
		groups.map { g => Row(g.name) }
	}
}

case class ShowProcedures(
	pattern: Option[String]) extends MbRunnableCommand with DML {

	override def output = {
		AttributeReference("PROCEDURE_NAME", StringType, nullable = false)() :: Nil
	}

	override def run(mbSession: MbSession)(implicit ctx: UserContext): Seq[Row] = {
		val procedures = pattern.map { p =>
			mbSession.catalog.listProcedures(ctx.organizationId, p)
		}.getOrElse(mbSession.catalog.listProcedures(ctx.organizationId))
		procedures.map { a => Row(a.name) }
	}
}

case class ShowEvents(pattern: Option[String]) extends MbRunnableCommand with DML {

	override def output = {
		AttributeReference("EVENT_NAME", StringType, nullable = false)() :: Nil
	}

	override def run(mbSession: MbSession)(implicit ctx: UserContext): Seq[Row] = {
		val timedEvents = pattern.map { p =>
			mbSession.catalog.listTimedEvents(ctx.organizationId, p)
		}.getOrElse(mbSession.catalog.listTimedEvents(ctx.organizationId))
		timedEvents.map { e => Row(e.name) }
	}
}

case class ShowGrants(user: String) extends MbRunnableCommand with DML {

	override def output = {
		AttributeReference("PRIVILEGE_LEVEL", StringType)() ::
      AttributeReference("NAME", StringType)() ::
      AttributeReference("PRIVILEGE_TYPE", StringType)() ::
		  Nil
	}

	override def run(mbSession: MbSession)(implicit ctx: UserContext) = {
		val catalogUser = mbSession.catalog.getUser(ctx.organizationId, user)
		if (mbSession.catalog.isSa(ctx.userId) || user == ctx.userName) {
			val buffer = new ArrayBuffer[Row]()
			val databasePrivilege = mbSession.catalog.getDatabasePrivilege(catalogUser.id.get)
			val tablePrivilege = mbSession.catalog.getTablePrivilege(catalogUser.id.get)
			val columnPrivilege = mbSession.catalog.getColumnPrivilege(catalogUser.id.get)
			buffer.append( databasePrivilege.map { p =>
				val database = mbSession.catalog.getDatabase(p.databaseId)
				Row("Database" , database.name, p.privilegeType)}:_*)
			buffer.append( tablePrivilege.map { p =>
				val database = mbSession.catalog.getDatabase(p.databaseId)
				Row("Table" , s"${database.name}.${p.table}", p.privilegeType)}:_* )
			buffer.append( columnPrivilege.map { p =>
				val database = mbSession.catalog.getDatabase(p.databaseId)
				Row("Column" , s"${database.name}.${p.table}.${p.column}", p.privilegeType)}:_*)
			buffer
		} else {
			throw new Exception(s"Access denied for user '$user'")
		}
	}
}

case class ShowCreateTable(table: MbTableIdentifier) extends MbRunnableCommand with DML {

	override def output: Seq[Attribute] = {
		AttributeReference("Table", StringType, nullable = false)() ::
		AttributeReference("Create Table", StringType, nullable = false)() :: Nil
	}

	override def run(mbSession: MbSession)(implicit ctx: UserContext): Seq[Row] = {
		import mbSession.catalog._
		val databaseId = table.database.map(db => getDatabase(ctx.organizationId, db).id.get)
			.getOrElse(ctx.databaseId)
		val createTable = if (viewExists(databaseId, table.table)) {
			val catalogView = getView(databaseId, table.table)
			catalogView.cmd
		} else {
			val catalogTable = getTable(databaseId, table.table)
			catalogTable.properties.filterKeys(key => !key.contains("password")).map {
				case (key, value) => s"$key '$value'"
			}.mkString(", ")
		}
		Seq(Row(table.table, createTable))
	}
}

case class DescDatabase(name: String) extends MbRunnableCommand with DML {

  override def output = {
    AttributeReference("PROPERTY_NAME", StringType, nullable = false)() ::
      AttributeReference("VALUE", StringType, nullable = false)() ::
      Nil
  }

	override def run(mbSession: MbSession)(implicit ctx: UserContext): Seq[Row] = {
		val database = mbSession.catalog.getDatabase(ctx.organizationId, name)
		val isLogical = database.isLogical
		val properties = database.properties.filterNot { case (key, _) =>
			key.toLowerCase.contains("user") ||
					key.toLowerCase.contains("username") ||
					key.toLowerCase.contains("password")
		}.toSeq.mkString("(", ", ", ")")
		val result = Row("database_name", database.name) ::
				Row("islogical", isLogical) ::
				Row("properties", properties) ::
				Row("description", database.description.getOrElse("")) :: Nil
		result
	}
}

case class DescTable(table: MbTableIdentifier, extended: Boolean) extends MbRunnableCommand with DML {

  override def output = {
	  AttributeReference("Field", StringType, nullable = false)() ::
	  AttributeReference("Type", StringType, nullable = false)() ::
	  AttributeReference("Select", StringType, nullable = false)() ::
	  AttributeReference("Update", StringType, nullable = false)() ::
	  Nil
  }

	override def run(mbSession: MbSession)(implicit ctx: UserContext): Seq[Row] = {
		import mbSession.catalog._
		val result = new ArrayBuffer[Row]()

		val databaseId = table.database.map(db => getDatabase(ctx.organizationId, db).id.get)
			.getOrElse(ctx.databaseId)

		val (columns, manager) = if (viewExists(databaseId, table.table)) {
			val catalogView = getView(databaseId, table.table)
			val columns = mbSession.schema(databaseId, catalogView.name, catalogView.cmd)
			val privilegeManager = new TablePrivilegeManager(mbSession, catalogView)
			(columns, privilegeManager)
		} else {
			val catalogTable = getTable(databaseId, table.table)
			val columns = mbSession.schema(catalogTable.databaseId, catalogTable.name)
			val privilegeManager = new TablePrivilegeManager(mbSession, catalogTable)
			(columns, privilegeManager)
		}

		val select = manager.selectable()
		val update = manager.updatable()

		val rows = columns.map { col =>
			Row(col.name, col.dataType, select.contains(col), update.contains(col))
		}
		result.append(rows:_*)
		result
	}
}

case class DescView(view: MbTableIdentifier) extends MbRunnableCommand with DML {

  override def output = {
    AttributeReference("PROPERTY_NAME", StringType, nullable = false)() ::
      AttributeReference("VALUE", StringType, nullable = false)() ::
      Nil
  }

	override def run(mbSession: MbSession)(implicit ctx: UserContext): Seq[Row] = {
		val databaseId = view.database.map(db => mbSession.catalog.getDatabase(ctx.organizationId, db).id.get)
			.getOrElse(ctx.databaseId)
		val catalogView = mbSession.catalog.getView(databaseId, view.table)
		val result = Row("View Name", catalogView.name) ::
			Row("Description", catalogView.description.getOrElse("")) ::
			Row("SQL", catalogView.cmd) :: Nil
		result
	}

}

case class DescFunction(function: MbFunctionIdentifier, extended: Boolean)
	extends MbRunnableCommand with DML {

  override def output = {
    AttributeReference("PROPERTY_NAME", StringType, nullable = false)() ::
      AttributeReference("VALUE", StringType, nullable = false)() ::
      Nil
  }

	override def run(mbSession: MbSession)(implicit ctx: UserContext): Seq[Row] = {
		val result = new ArrayBuffer[Row]()

		val databaseId = function.database.map(db => mbSession.catalog.getDatabase(ctx.organizationId, db).id.get)
			.getOrElse(ctx.databaseId)
		val catalogFunction = mbSession.catalog.getFunction(databaseId, function.func)
		result.append(
			Row("Function Name", catalogFunction.name),
			Row("Description", catalogFunction.description.getOrElse(""))
		)
		if (extended) {
			// TODO
			result.append( Row("Class", catalogFunction.className))
		}
		result
	}
}

case class DescUser(user: String) extends MbRunnableCommand with DML {

  override def output = {
    AttributeReference("PROPERTY_NAME", StringType, nullable = false)() ::
      AttributeReference("VALUE", StringType, nullable = false)() ::
      Nil
  }

	override def run(mbSession: MbSession)(implicit ctx: UserContext): Seq[Row] = {
		val catalogUser: CatalogUser = mbSession.catalog.getUser(ctx.organizationId, user)
		val result = Row("User Name", catalogUser.name) ::
			Row("Account", catalogUser.account) ::
			Row("DDL", catalogUser.ddl) ::
			Row("DCL", catalogUser.dcl) ::
			Row("Grant Account", catalogUser.grantAccount) ::
			Row("Grant DDL", catalogUser.grantDdl) ::
			Row("Grant DCL", catalogUser.grantDcl) ::
			Row("IsSA", catalogUser.isSA) :: Nil
		result
	}
}

case class DescGroup(group: String) extends MbRunnableCommand with DML {

  override def output = {
    AttributeReference("PROPERTY_NAME", StringType, nullable = false)() ::
      AttributeReference("VALUE", StringType, nullable = false)() ::
      Nil
  }

	override def run(mbSession: MbSession)(implicit ctx: UserContext): Seq[Row] = {
		val catalogGroup: CatalogGroup = mbSession.catalog.getGroup(ctx.organizationId, group)
		val result = Row("Group Name", catalogGroup.name) ::
			Row("Description", catalogGroup.description.getOrElse("")) :: Nil
		result
	}
}

case class DescEvent(event: String) extends MbRunnableCommand with DML {

  override def output = {
    AttributeReference("PROPERTY_NAME", StringType, nullable = false)() ::
      AttributeReference("VALUE", StringType, nullable = false)() ::
      Nil
  }

	override def run(mbSession: MbSession)(implicit ctx: UserContext): Seq[Row] = {
		val catalogTimedEvent = mbSession.catalog.getTimedEvent(ctx.organizationId, event)
		val catalogUser = mbSession.catalog.getUser(catalogTimedEvent.definer)
		val proc = mbSession.catalog.getProcedure(catalogTimedEvent.procedure)
		val result = Row("Event Name", catalogTimedEvent.name) ::
			Row("Definer", catalogUser.name) ::
			Row("Schedule", catalogTimedEvent.schedule) ::
			Row("Enable", catalogTimedEvent.enable) ::
			Row("Procedure", proc.cmds) ::
			Row("Description", catalogTimedEvent.description.getOrElse("No Description.")) :: Nil
		result
	}
}

/*case class SetConfiguration(key: String, value: String) extends MbRunnableCommand with DML {
	override def run(mbSession: MbSession)(implicit ctx: CatalogSession): Seq[Row] = {
		val user = mbSession.catalog.getUser(ctx.userId)
		mbSession.catalog.alterUser(
			user.copy(
				configuration = user.configuration.+(key -> value),
				updateBy = ctx.userId,
				updateTime = Utils.now
			)
		)
		Seq.empty[Row]
	}
}*/

case class Explain(query: String, extended: Boolean = false) extends MbRunnableCommand with DML {

  override def output = {
    AttributeReference("EXPLAIN_RESULT", StringType, nullable = false)() :: Nil
  }

	// TODO
	override def run(mbSession: MbSession)(implicit ctx: UserContext): Seq[Row] = try {
		val logicalPlan = mbSession.pushdownPlan(mbSession.optimizedPlan(query))
		val outputString = logicalPlan match {
			case w@WholePushdown(child, _) =>
				val executedPlan = mbSession.toDF(child).queryExecution.executedPlan
				if (extended) {
					w.simpleString + "\n+-" +
					executedPlan.toString()
				} else {
					w.simpleString + "\n+-" +
					executedPlan.simpleString
				}
			case _ =>
				val executedPlan = mbSession.toDF(logicalPlan).queryExecution.executedPlan
				if (extended) {
					executedPlan.toString()
				} else {
					executedPlan.simpleString
				}
		}
		Seq(Row(outputString))
	} catch { case e: TreeNodeException[_] =>
		("Error occurred during query planning: \n" + e.getMessage).split("\n").map(Row(_))
	}
}

case class MQLQuery(query: String) extends MbCommand with DML

case class CreateTempView(
	name: String,
	query: String,
	isCache: Boolean,
	replaceIfExists: Boolean) extends MbCommand with DML


case class CreateTempFunction(
	function: MbFunctionIdentifier,
	className: String,
	methodName: Option[String],
	resources: Seq[FunctionResource],
	ignoreIfExists: Boolean) extends MbCommand with DML

case class DropTempFunction(
	function: MbFunctionIdentifier,
	ignoreIfNotExists: Boolean) extends MbCommand with DML

case class InsertInto(
	table: MbTableIdentifier,
	query: String,
	colNames: Seq[String],
	overwrite: Boolean) extends MbCommand with DML
