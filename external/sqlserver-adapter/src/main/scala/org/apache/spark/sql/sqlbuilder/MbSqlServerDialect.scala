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


class MbSqlServerDialect extends MbDialect {

	val prefix: String = "jdbc:sqlserver"
	override def canHandle(url: String): Boolean = url.toLowerCase.startsWith(prefix)

	override def quote(name: String): String = {
		"'" + name.replace("`", "") + "'"
	}

	override def explainSQL(sql: String): String = s"EXPLAIN $sql"

	override def relation(relation: LogicalRelation): String = {
		relation.relation.asInstanceOf[MbJDBCRelation].jdbcOptions.table
	}

	override def maybeQuote(name: String): String = {
		name
	}

	override def getIndexes(conn: Connection, url: String, tableName: String): Set[String] = {
		val splitName = url.toLowerCase.stripPrefix(prefix).split("databasename=")
		val dbName = if(splitName.length == 2){
			splitName(1)
		}else{
			throw new Exception("sql server url format error, jdbc:sqlserver://host:port;DatabaseName=dbname")
		}

		val sql =
    	s"""|USE $dbName;
			|SELECT	t.name as table_name,
			|     ind.name as index_name,
			|     ind.index_id as index_id,
			|     ic.index_column_id as col_id,
			|     col.name as col_name
			|FROM sys.indexes ind
			|INNER JOIN  sys.index_columns ic ON ind.object_id = ic.object_id and ind.index_id = ic.index_id
			|INNER JOIN  sys.columns col ON ic.object_id = col.object_id and ic.column_id = col.column_id
			|INNER JOIN  sys.tables t ON ind.object_id = t.object_id
			|where t.name = '$tableName';
    	""".stripMargin

		val rs = conn.createStatement().executeQuery(sql)
		val index = new scala.collection.mutable.HashSet[String]
		while (rs.next()) {
			index.add(rs.getString("col_name"))
		}
		index.toSet
	}

	override def getTableStat(conn: Connection, url: String, tableName: String): (Option[BigInt], Option[Long]) = {  //rows , size
		val splitName = url.toLowerCase.stripPrefix(prefix).split("databasename=")
		val dbName = if(splitName.length == 2){
			splitName(1)
		}else{
			throw new Exception("sql server url format error, jdbc:sqlserver://host:port;DatabaseName=dbname")
		}

		val sql =
			s"""|USE $dbName;
				|SELECT s.Name AS schema_name, t.Name AS table_name, p.rows AS table_rows,
				|		SUM(a.data_pages)*8*1024 AS data_size,  SUM(a.used_pages)*8*1024 AS used_size, SUM(a.total_pages)*8*1024 AS total_size
				|FROM sys.tables t
				|INNER JOIN sys.indexes i ON t.OBJECT_ID = i.object_id
				|INNER JOIN sys.partitions p ON i.object_id = p.OBJECT_ID AND i.index_id = p.index_id
				|INNER JOIN sys.allocation_units a ON p.partition_id = a.container_id
				|INNER JOIN sys.schemas s ON t.schema_id = s.schema_id
				|GROUP BY t.Name, s.Name, p.Rows
				|having t.Name = '$tableName';
		""".stripMargin

		val rs = conn.createStatement().executeQuery(sql)
		if (rs.next())
			(Some(BigInt(rs.getLong(3))), Some(rs.getLong(4)))
		else {
			(None, None)
		}

	}

}
