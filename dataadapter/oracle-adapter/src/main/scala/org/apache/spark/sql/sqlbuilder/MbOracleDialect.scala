package org.apache.spark.sql.sqlbuilder

import org.apache.spark.sql.execution.datasources.LogicalRelation
import org.apache.spark.sql.execution.datasources.mbjdbc.MbJDBCRelation


object MbOracleDialect extends MbDialect {

	override def canHandle(name: String): Boolean = name.equalsIgnoreCase("mysql")

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
}
