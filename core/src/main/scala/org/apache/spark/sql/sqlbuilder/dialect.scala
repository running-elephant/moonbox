package org.apache.spark.sql.sqlbuilder

import org.apache.spark.sql.execution.datasources.LogicalRelation


trait MbDialect {
	import MbDialect._

	registerDialect(this)

	def relation(relation: LogicalRelation): String

	def canHandle(name: String): Boolean

	def explainSQL(sql: String): String

	def quote(name: String): String

	def maybeQuote(name: String): String

}

object MbDialect {

	def registerDialect(dialect: MbDialect) : Unit = {
		dialects = dialect :: dialects.filterNot(_ == dialect)
	}

	def unregisterDialect(dialect : MbDialect) : Unit = {
		dialects = dialects.filterNot(_ == dialect)
	}

	private[this] var dialects = List[MbDialect]()

	def get(name: String): MbDialect = {
		val matchingDialects = dialects.filter(_.canHandle(name))
		matchingDialects.headOption match {
			case None => throw new NoSuchElementException(s"no suitable MbDialect from $name")
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







