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

package org.apache.spark.sql.sqlbuilder

import java.sql.Connection
import java.util.ServiceLoader

import org.apache.spark.sql.execution.datasources.LogicalRelation
import scala.collection.JavaConverters._

trait MbDialect {
	import MbDialect._

	registerDialect(this)

	def relation(relation: LogicalRelation): String

	def canHandle(url: String): Boolean

	def explainSQL(sql: String): String

	def quote(name: String): String

	def maybeQuote(name: String): String

	def getIndexes(conn: Connection, url: String, tableName: String): Set[String]

	def getTableStat(conn: Connection, url: String, tableName: String): ((Option[BigInt], Option[Long]))

	def limitSQL(sql: String, limit: String): String = {
		s"$sql LIMIT $limit"
	}
}

object MbDialect {
	private[this] var dialects = List[MbDialect]()

	{
		for (x <- ServiceLoader.load(classOf[MbDialect]).asScala) {}
	}

	def registerDialect(dialect: MbDialect) : Unit = synchronized {
		dialects = dialect :: dialects.filterNot(_ == dialect)
	}

	def unregisterDialect(dialect : MbDialect) : Unit = synchronized {
		dialects = dialects.filterNot(_ == dialect)
	}

	def get(url: String): MbDialect = {
		val matchingDialects = dialects.filter(_.canHandle(url))
		matchingDialects.headOption match {
			case None => throw new NoSuchElementException(s"no suitable MbDialect from $url")
			case Some(d) => d
		}
	}
}



/*object MbOracleDialect extends MbDialect {

	override def canHandle(name: String): Boolean = name.equalsIgnoreCase("oracle")

	override def quote(name: String): String = name

	override def explainSQL(sql: String): String = "EXPLAIN PLAN FOR"

	override def relation(relation: LogicalRelation): String = {
		relation.relation.asInstanceOf[MbJDBCRelation].jdbcOptions.table
	}

	override def maybeQuote(name: String): String = name
}*/







