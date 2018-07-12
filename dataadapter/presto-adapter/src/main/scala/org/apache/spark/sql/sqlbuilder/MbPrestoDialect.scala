package org.apache.spark.sql.sqlbuilder

import java.sql.Connection

import org.apache.spark.sql.execution.datasources.LogicalRelation
import org.apache.spark.sql.execution.datasources.presto.PrestoRelation

object MbPrestoDialect extends MbDialect {

	override def relation(relation: LogicalRelation): String = {
		relation.relation.asInstanceOf[PrestoRelation].props("dbtable")
	}

	override def canHandle(url: String): Boolean = url.toLowerCase().startsWith("jdbc:presto")

	override def quote(name: String): String = name

	override def explainSQL(sql: String): String = s"EXPLAIN $sql"

	override def maybeQuote(name: String): String = name

	override def getIndexes(conn: Connection, url: String, tableName: String): Set[String] = {
		Set[String]()
	}

	override def getTableStat(conn: Connection, url: String, tableName: String): (Option[BigInt], Option[Long]) = {
		(None, None)
	}
}
