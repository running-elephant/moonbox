package org.apache.spark.sql.sqlbuilder

import java.sql.Connection

import org.apache.spark.sql.execution.datasources.LogicalRelation
import org.apache.spark.sql.execution.datasources.mbjdbc.MbJDBCRelation


class MbMySQLDialect extends MbDialect {

	override def canHandle(url: String): Boolean = url.toLowerCase().startsWith("jdbc:mysql")

	override def quote(name: String): String = {
		"`" + name.replace("`", "``") + "`"
	}

	override def explainSQL(sql: String): String = s"EXPLAIN $sql"

	override def relation(relation: LogicalRelation): String = {
		relation.relation.asInstanceOf[MbJDBCRelation].jdbcOptions.table
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
