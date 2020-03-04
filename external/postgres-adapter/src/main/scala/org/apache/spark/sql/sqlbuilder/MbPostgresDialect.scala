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


class MbPostgresDialect extends MbDialect {

	override def canHandle(url: String): Boolean = url.toLowerCase().startsWith("jdbc:postgresql")

	override def quote(name: String): String = {
		"\"" + name.replace("`", "\"") + "\""
	}

	override def explainSQL(sql: String): String = s"EXPLAIN $sql"

	override def relation(relation: LogicalRelation): String = {
		relation.relation.asInstanceOf[MbJDBCRelation].jdbcOptions.table
	}

	override def maybeQuote(name: String): String = {
		name
	}

	override def getIndexes(conn: Connection, url: String, tableName: String): Set[String] = {
		Set[String]()
	}

	override def getTableStat(conn: Connection, url: String, tableName: String): (Option[BigInt], Option[Long]) = {
		(None, None)
	}

}
