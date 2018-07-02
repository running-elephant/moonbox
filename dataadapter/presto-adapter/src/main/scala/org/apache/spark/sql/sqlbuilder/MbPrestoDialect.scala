package org.apache.spark.sql.sqlbuilder

import org.apache.spark.sql.execution.datasources.LogicalRelation
import org.apache.spark.sql.execution.datasources.presto.PrestoRelation

object MbPrestoDialect extends MbDialect {

	override def relation(relation: LogicalRelation): String = {
		relation.relation.asInstanceOf[PrestoRelation].props("dbtable")
	}

	override def canHandle(name: String): Boolean = name.equalsIgnoreCase("presto")

	override def quote(name: String): String = name

	override def explainSQL(sql: String): String = s"EXPLAIN $sql"

	override def maybeQuote(name: String): String = name
}
