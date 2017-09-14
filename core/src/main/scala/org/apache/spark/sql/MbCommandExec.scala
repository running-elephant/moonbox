/*-
 * <<
 * Moonbox
 * ==
 * Copyright (C) 2016 - 2017 EDP
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

package org.apache.spark.sql

import edp.moonbox.common.{EdpLogging, Util}
import edp.moonbox.core.split.MbSpliter
import org.apache.spark.sql.catalyst.TableIdentifier

sealed trait MbCommandExec extends EdpLogging {

	def execute()(implicit spark: SparkSession): DataFrame

	protected def tableExists(table: String)(implicit spark: SparkSession): Boolean = {
		spark.sessionState.catalog.tableExists(TableIdentifier(table))
	}

	protected def dropTable(table: String)(implicit spark: SparkSession) = {
		spark.sessionState.catalog.dropTable(TableIdentifier(table), true, true)
	}

}

case class MountTableExec(table: String, options: Map[String, String]) extends MbCommandExec {
	override def execute()(implicit spark: SparkSession): DataFrame = {
		if (tableExists(table)) dropTable(table)
		//spark.sessionState.catalog.createTable()
		val `type` = Util.getOrException(options, "type")
		val ddl =
			s"""create table $table
			   |using ${ Util.datasourceRelationProvider(`type`) }
			   |options (${ options.map{ case (key, value) => s"$key '$value'" }.mkString(",") })
			 """.stripMargin
		logInfo(ddl)
		spark.sql(ddl)
	}
}

case class UnmountTableExec(table: String) extends MbCommandExec {
	override def execute()(implicit spark: SparkSession): DataFrame = {
		dropTable(table)
		logInfo(s"dropped table $table")
		spark.emptyDataFrame
	}
}

case class CreateViewAsSelectExec(view: String, ignore: Boolean, select: SelectExec) extends MbCommandExec {
	override def execute()(implicit spark: SparkSession): DataFrame = {
		if (ignore) {
			select.execute().createOrReplaceTempView(view)
		} else {
			select.execute().createTempView(view)
		}
		spark.emptyDataFrame
	}
}

case class SelectExec(sql: String) extends MbCommandExec {
	override def execute()(implicit spark: SparkSession): DataFrame = {
		try {
			logInfo(s"try to run sql $sql in pushdown mode")
			val (resultTable, pushdownTables) = new MbSpliter(spark).split(sql)
			logInfo(s"prepare pushdown:\n")
			logInfo(s"has ResultTable: ${resultTable.isDefined}")
			logInfo(s"PushdownTables' size: ${pushdownTables.size}")
			if (pushdownTables.isEmpty) spark.sql(sql) // no pushdown, run origin sql via spark
			else {
				val namedDF = pushdownTables.map(table => (table.name, table.execute))
				namedDF.foreach { case (name, df) => df.createTempView(name) }
				resultTable.map(table => table.execute).getOrElse(namedDF.head._2)
			}
		} catch {
			case e: Exception =>
				logWarning(s"pushdown error, using spark")
				logError(e.getMessage)
				spark.sql(sql)
		}
	}
}

