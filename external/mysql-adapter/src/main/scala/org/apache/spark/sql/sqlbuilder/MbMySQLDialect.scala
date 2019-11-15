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

package org.apache.spark.sql.sqlbuilder

import java.sql.Connection

import org.apache.spark.sql.execution.datasources.LogicalRelation
import org.apache.spark.sql.execution.datasources.mbjdbc.MbJDBCRelation
import org.apache.spark.sql.types.{DataType, StringType}


class MbMySQLDialect extends MbDialect {

	override def canHandle(url: String): Boolean = url.toLowerCase().startsWith("jdbc:mysql")

	override def quote(name: String): String = {
		"`" + name.replace("`", "``") + "`"
	}

	override def explainSQL(sql: String): String = s"EXPLAIN $sql"

	override def relation(relation: LogicalRelation): String = {
		relation.relation.asInstanceOf[MbJDBCRelation].jdbcOptions.table
	}

	override def dataTypeToSQL(dataType: DataType): String = {
		dataType match {
			case StringType => "CHAR"
			case _ => dataType.sql
		}
	}

	override def maybeQuote(name: String): String = {
		name
	}

	override def getIndexes(conn: Connection, url: String, tableName: String): Set[String] = {
		val dbName = url.split("\\?").head.split("/").last
		val sql =
			s"""
			   |SHOW index FROM $dbName.$tableName
		 """.stripMargin
		val rs = conn.createStatement().executeQuery(sql)
		val index = new scala.collection.mutable.HashSet[String]
		while (rs.next()) {
			index.add(rs.getString("Column_name"))
		}
		index.toSet
	}

	override def getTableStat(conn: Connection, url: String, tableName: String): (Option[BigInt], Option[Long]) = {
		val dbName = url.split("\\?").head.split("/").last
		val sql = s"""
					 |SELECT TABLE_ROWS, DATA_LENGTH FROM information_schema.tables
					 |WHERE TABLE_SCHEMA = '$dbName' AND TABLE_NAME = '$tableName'""".stripMargin
		val rs = conn.createStatement().executeQuery(sql)
		if (rs.next())
			(Some(BigInt(rs.getLong(1))), Some(rs.getLong(2)))
		else {
			(None, None)
		}
	}
}
