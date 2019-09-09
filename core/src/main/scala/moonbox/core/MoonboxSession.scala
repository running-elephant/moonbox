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

package moonbox.core


import moonbox.catalog.CatalogTableType
import moonbox.common.{MbConf, MbLogging}
import moonbox.core.command._
import moonbox.core.parser.MoonboxParser
import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.{Row, SparkEngine}

class MoonboxSession(
	conf: MbConf,
	org: String,
	username: String,
	defaultDb: Option[String] = None,
	sessionConfig: Map[String, String] = Map()) extends MbLogging {

	import MoonboxSession._

	private val parser = new MoonboxParser()

	val catalog = new MoonboxCatalog(conf).setCurrentUser(org, username)

	val engine = new SparkEngine(conf, catalog)

	initializeDatabase()

	def cancelJob(jobLabel: String): Unit = {
		engine.cancelJobGroup(jobLabel)
	}

	def parsedCommand(sql: String): MbCommand = {
		val command = parser.parsePlan(sql)
		CommandChecker.check(command, catalog)
		command
	}

	def sql(sqlText: String, maxRows: Int): DataResult = {
		sql("", sqlText, maxRows)
	}

	def sql(jobLabel: String, sql: String, maxRows: Int): DataResult = {
		engine.setJobGroup(jobLabel,
			s"""| $org@$username<br/>
			    | ${sql.take(50)} ${if (sql.length > 50) " ..." else ""}
			 """.stripMargin)
		try {
			engine.sql(sql, maxRows)
		} finally {
			engine.clearJobGroup()
		}
	}

	def sqlSchema(sql: String): StructType = {
		engine.sqlSchema(sql)
	}

	def tableSchema(table: String, database: String): StructType = {
		val catalogTable = catalog.getTable(database, table)
		if (catalogTable.tableType == CatalogTableType.TABLE) {
			engine.tableSchema(table, Some(database))
		} else {
			engine.viewSchema(table, Some(database), catalogTable.viewText.get)
		}
	}

	private def initializeDatabase(): Unit = {
		defaultDb.foreach { db =>
			catalog.setCurrentDb(db)
			engine.registerDatabase(db)
			engine.setCurrentDatabase(db)
		}
	}
}

object MoonboxSession extends MbLogging {
	type  DataResult = (Iterator[Row], StructType)
}
