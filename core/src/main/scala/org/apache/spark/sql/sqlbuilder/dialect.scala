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







