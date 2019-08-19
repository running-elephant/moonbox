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

import java.util.Locale

import moonbox.common.util.Utils
import moonbox.catalog._
import moonbox.core._
import org.apache.spark.sql.Row
import org.apache.spark.sql.catalyst.{FunctionIdentifier, TableIdentifier}
import org.apache.spark.sql.catalyst.errors.TreeNodeException
import org.apache.spark.sql.catalyst.expressions.{Attribute, AttributeReference}
import org.apache.spark.sql.optimizer.WholePushdown
import org.apache.spark.sql.types.StringType

import scala.collection.mutable.ArrayBuffer

sealed trait DML

case class UseDatabase(db: String) extends MbRunnableCommand with DML {

	override def run(mbSession: MoonboxSession): Seq[Row] = {

		mbSession.catalog.setCurrentDb(db)
		mbSession.engine.registerDatabase(db)
		mbSession.engine.setCurrentDatabase(db)
		Seq.empty[Row]

	}
}

case class ShowDatabases(
	pattern: Option[String]) extends MbRunnableCommand with DML {

	override def output: Seq[Attribute] = {
		AttributeReference("DATABASE_NAME", StringType, nullable = false)() ::
			AttributeReference("DATABASE_TYPE", StringType, nullable = false)() :: Nil
	}

	override def run(mbSession: MoonboxSession): Seq[Row] = {
		mbSession.catalog.listDatabase(pattern).map { d =>
			Row(d.name, if (d.isLogical) "logical" else "physical")
		}
	}

}

case class ShowTables(
	database: Option[String],
	pattern: Option[String]) extends MbRunnableCommand with DML {

	override def output = {
		AttributeReference("TABLE_NAME", StringType, nullable = false)() ::
			AttributeReference("TABLE_TYPE", StringType, nullable = false)() :: Nil
	}

	override def run(mbSession: MoonboxSession): Seq[Row] = {

		import mbSession.catalog._

		val result = new ArrayBuffer[Row]()

		val db = database.getOrElse(getCurrentDb)

		// tables
		val tables = mbSession.catalog.listTables(db, pattern)
		result.append(tables.map(t => Row(t.name, t.tableType.name.toLowerCase)): _*)

		// temp views
		val temp = pattern.map { p =>
			mbSession.engine.catalog.listTables("global_temp", p)
		}.getOrElse {
			mbSession.engine.catalog.listTables("global_temp")
		}
		result.append(temp.map(tv => Row(tv.table, "temp_view")): _*)

		result
	}
}

case class ShowFunctions(
	database: Option[String],
	pattern: Option[String],
	userFunc: Boolean,
	builtInFunc: Boolean
) extends MbRunnableCommand with DML {

	override def output = {
		AttributeReference("database", StringType, nullable = false)() ::
			AttributeReference("function", StringType, nullable = false)() :: Nil
	}

	override def run(mbSession: MoonboxSession): Seq[Row] = {

		import mbSession.catalog._

		val db = database.getOrElse(getCurrentDb)

		val result = new ArrayBuffer[Row]()
		// user defined
		if (userFunc) {
			mbSession.catalog.listFunctions(db, pattern.getOrElse("%")
			).foreach(f => result.append(Row(db, f.name)))
		}

		// built-in
		if (builtInFunc) {
			mbSession.engine.catalog
				.listFunctions(db, Utils.escapeLikeRegex(pattern.getOrElse("%")))
				.collect { case (f, "SYSTEM") => f.unquotedString }
				.foreach(f =>result.append(Row("built-in", f)))
		}

		result
	}
}



case class ShowProcedures(
	pattern: Option[String]) extends MbRunnableCommand with DML {

	override def output = {
		AttributeReference("PROCEDURE_NAME", StringType, nullable = false)() :: Nil
	}

	override def run(mbSession: MoonboxSession): Seq[Row] = {
		mbSession.catalog.listProcedures(pattern).map { a => Row(a.name) }
	}
}

case class ShowEvents(pattern: Option[String]) extends MbRunnableCommand with DML {

	override def output = {
		AttributeReference("EVENT_NAME", StringType, nullable = false)() ::
			AttributeReference("ENABLE", StringType, nullable = false)() :: Nil
	}

	override def run(mbSession: MoonboxSession): Seq[Row] = {

		mbSession.catalog.listTimedEvents(pattern).map { e =>
			Row(e.name, e.enable.toString)
		}
	}
}

case class ShowGrants(user: String) extends MbRunnableCommand with DML {

	override def output = {
		AttributeReference("PRIVILEGE_LEVEL", StringType)() ::
			AttributeReference("NAME", StringType)() ::
			AttributeReference("PRIVILEGE_TYPE", StringType)() ::
			Nil
	}

	override def run(mbSession: MoonboxSession): Seq[Row] = {

		// TODO
		/*val catalogUser = mbSession.catalog.getUser(user)

		if (catalogUser.isSA || mbSession.catalog.currentUser.equalsIgnoreCase(user)) {

		}

		if (mbSession.catalog.isSa(env.userId) || user == env.userName) {
			val buffer = new ArrayBuffer[Row]()
			val databasePrivilege = mbSession.catalog.getDatabasePrivilege(catalogUser.id.get)
			val tablePrivilege = mbSession.catalog.getTablePrivilege(catalogUser.id.get)
			val columnPrivilege = mbSession.catalog.getColumnPrivilege(catalogUser.id.get)
			buffer.append(databasePrivilege.map { p =>
				val database = mbSession.catalog.getDatabase(p.databaseId)
				Row("Database", database.name, p.privilegeType)
			}: _*)
			buffer.append(tablePrivilege.map { p =>
				val database = mbSession.catalog.getDatabase(p.databaseId)
				Row("Table", s"${database.name}.${p.table}", p.privilegeType)
			}: _*)
			buffer.append(columnPrivilege.map { p =>
				val database = mbSession.catalog.getDatabase(p.databaseId)
				Row("Column", s"${database.name}.${p.table}.${p.column}", p.privilegeType)
			}: _*)
			buffer
		} else {
			throw new Exception(s"Access denied for user '$user'")
		}*/
		Seq.empty[Row]
	}
}

case class ShowCreateTable(table: TableIdentifier) extends MbRunnableCommand with DML {

	override def output: Seq[Attribute] = {
		AttributeReference("Table", StringType, nullable = false)() ::
			AttributeReference("Create Table", StringType, nullable = false)() :: Nil
	}

	override def run(mbSession: MoonboxSession): Seq[Row] = {

		import mbSession.catalog._

		val db = table.database.getOrElse(getCurrentDb)

		val catalogTable = mbSession.catalog.getTable(db, table.table)

		val createTable = if (catalogTable.tableType == CatalogTableType.TABLE) {
			catalogTable.properties.filterKeys(!_.toLowerCase.contains("password")).map {
				case (key, value) => s"$key '$value'"
			}.mkString(", ")
		} else {
			catalogTable.viewText.get
		}

		Seq(Row(table.unquotedString, createTable))
	}
}

case class ShowSchema(sql: String) extends MbRunnableCommand with DML {

	override def output: Seq[Attribute] = {
		AttributeReference("name", StringType, nullable = false)() ::
			AttributeReference("dataType", StringType, nullable = false)() ::
			AttributeReference("nullable", StringType, nullable = false)() ::
			AttributeReference("metadata", StringType, nullable = false)() :: Nil
	}

	override def run(mbSession: MoonboxSession): Seq[Row] = {
		mbSession.sqlSchema(sql).map { field =>
			Row(field.name,
				field.dataType.simpleString, field.nullable.toString, field.metadata.json)
		}
	}
}

case class DescDatabase(name: String) extends MbRunnableCommand with DML {

	override def output = {
		AttributeReference("PROPERTY_NAME", StringType, nullable = false)() ::
			AttributeReference("VALUE", StringType, nullable = false)() ::
			Nil
	}

	override def run(mbSession: MoonboxSession): Seq[Row] = {

		val database = mbSession.catalog.getDatabase(name)

		val isLogical = database.isLogical
		val properties = database.properties.filterKeys(!_.toLowerCase.contains("password")).map {
			case (key, value) => s"$key '$value'"
		}.mkString(", ")
		val result = Row("database", database.name) ::
			Row("islogical", isLogical.toString) ::
			Row("properties", properties) ::
			Row("description", database.description.getOrElse("-")) :: Nil
		result
	}
}

case class DescTable(table: TableIdentifier, extended: Boolean) extends MbRunnableCommand with DML {

	override def output: Seq[Attribute] = {
		AttributeReference("name", StringType, nullable = false)() ::
			AttributeReference("dataType", StringType, nullable = false)() ::
			AttributeReference("nullable", StringType, nullable = false)() ::
			AttributeReference("metadata", StringType, nullable = false)() :: Nil
	}

	override def run(mbSession: MoonboxSession): Seq[Row] = {

		import mbSession.catalog._

		val db = table.database.getOrElse(getCurrentDb)

		val schema = mbSession.tableSchema(table.table, db)

		schema.map { field =>
			Row(field.name,
				field.dataType.simpleString,
				field.nullable.toString,
				field.metadata.json)
		}
	}
}

case class DescFunction(function: FunctionIdentifier, isExtended: Boolean)
	extends MbRunnableCommand with DML {

	override def output = {
		AttributeReference("PROPERTY_NAME", StringType, nullable = false)() ::
			AttributeReference("VALUE", StringType, nullable = false)() ::
			Nil
	}

	override def run(mbSession: MoonboxSession): Seq[Row] = {

		import mbSession.catalog._

		val db = function.database.getOrElse(getCurrentDb)

		mbSession.catalog.getFunctionOption(db, function.funcName) match {
			case Some(func) => // UDF
				val result =
					Row("Function", func.name) ::
						Row("Class", func.className) ::
						Row("Usage", func.description.getOrElse("-")) :: Nil
				if (isExtended) {
					result :+ Row("Extended Usage", "-")
				} else {
					result
				}
			case None =>
				function.funcName.toLowerCase(Locale.ROOT) match {
					case "<>" =>
						Row("Function", function.unquotedString) ::
							Row("Usage",
								" expr1 <> expr2 - Returns true if `expr1` is not equal to `expr2`.") :: Nil
					case "!=" =>
						Row(s"Function", function.unquotedString) ::
							Row("Usage",
								"expr1 != expr2 - Returns true if `expr1` is not equal to `expr2`.") :: Nil
					case "between" =>
						Row("Function", "between") ::
							Row("Usage",
								"expr1 [NOT] BETWEEN expr2 AND expr3 - evaluate if `expr1` is [not] in between `expr2` and `expr3`.") :: Nil
					case "case" =>
						Row("Function", "case") ::
							Row("Usage", "CASE expr1 WHEN expr2 THEN expr3 " +
								"[WHEN expr4 THEN expr5]* [ELSE expr6] END - " +
								"When `expr1` = `expr2`, returns `expr3`; " +
								"when `expr1` = `expr4`, return `expr5`; else return `expr6`.") :: Nil
					case _ =>
						val info = mbSession.engine.catalog.lookupFunctionInfo(function)
						val name = if (info.getDb != null) info.getDb + "." + info.getName else info.getName
						val result =
							Row(s"Function", name) ::
								Row(s"Class", info.getClassName) ::
								Row(s"Usage", replaceFunctionName(info.getUsage, info.getName)) :: Nil

						if (isExtended) {
							result :+
								Row(s"Extended Usage", replaceFunctionName(info.getExtended, info.getName))
						} else {
							result
						}

				}
		}
	}

	private def replaceFunctionName(usage: String, functionName: String): String = {
		if (usage == null) {
			"N/A."
		} else {
			usage.replaceAll("_FUNC_", functionName)
		}
	}
}



case class DescProcedure(proc: String) extends MbRunnableCommand with DML {

	override def output: Seq[Attribute] = {
		AttributeReference("PROPERTY_NAME", StringType, nullable = false)() ::
			AttributeReference("VALUE", StringType, nullable = false)() ::
			Nil
	}

	override def run(mbSession: MoonboxSession): Seq[Row] = {
		val procedure = mbSession.catalog.getProcedure(proc)

		Row("Procedure Name", procedure.name) ::
			Row("Language", procedure.lang) ::
			Row("SQL", procedure.sqls.mkString("; ")) :: Nil

	}
}

case class DescEvent(event: String) extends MbRunnableCommand with DML {

	override def output = {
		AttributeReference("PROPERTY_NAME", StringType, nullable = false)() ::
			AttributeReference("VALUE", StringType, nullable = false)() ::
			Nil
	}

	override def run(mbSession: MoonboxSession): Seq[Row] = {

		val catalogEvent = mbSession.catalog.getTimedEvent(event)

		 Row("Event Name", catalogEvent.name) ::
			Row("Definer", catalogEvent.definer) ::
			Row("Schedule", catalogEvent.schedule) ::
			Row("Enable", catalogEvent.enable.toString) ::
			Row("Procedure", catalogEvent.procedure) ::
			Row("Description", catalogEvent.description.getOrElse("-")) :: Nil

	}
}

case class Explain(query: String, extended: Boolean = false) extends MbRunnableCommand with DML {

	override def output = {
		AttributeReference("EXPLAIN_RESULT", StringType, nullable = false)() :: Nil
	}

	// TODO
	override def run(mbSession: MoonboxSession): Seq[Row] = try {
		import mbSession.engine._
		val parsedPlan = parsePlan(query)
		injectTableFunctions(parsedPlan)
		val logicalPlan = pushdownPlan(optimizePlan(analyzePlan(parsedPlan)))
		val outputString = logicalPlan match {
			case w@WholePushdown(child, _) =>
				val executedPlan = createDataFrame(child).queryExecution.executedPlan
				if (extended) {
					w.simpleString + "\n+-" +
						executedPlan.toString()
				} else {
					w.simpleString + "\n+-" +
						executedPlan.simpleString
				}
			case _ =>
				val executedPlan = createDataFrame(logicalPlan).queryExecution.executedPlan
				if (extended) {
					executedPlan.toString()
				} else {
					executedPlan.simpleString
				}
		}
		Seq(Row(outputString))
	} catch {
		case e: TreeNodeException[_] =>
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
	function: FunctionIdentifier,
	className: String,
	methodName: Option[String],
	resources: Seq[FunctionResource],
	ignoreIfExists: Boolean) extends MbCommand with DML

case class DropTempFunction(
	function: FunctionIdentifier,
	ignoreIfNotExists: Boolean) extends MbCommand with DML

case class InsertInto(
	table: TableIdentifier,
	query: String,
	partitionColumns: Seq[String],
	coalesce: Option[Int],
	insertMode: InsertMode.Value
) extends MbCommand with DML

object InsertMode extends Enumeration {
	val Append = Value
	val Overwrite = Value
	val Merge = Value
}

case class Statement(sql: String) extends MbCommand with DML
