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

package org.apache.spark.sql.execution.datasources.presto

import java.sql.{Connection, DriverManager, Date, Statement, Timestamp, ResultSet, ResultSetMetaData, SQLException}

import org.apache.commons.lang3.StringUtils
import org.apache.spark.annotation.DeveloperApi
import org.apache.spark.{InterruptibleIterator, Partition, SparkContext, TaskContext}
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.catalyst.InternalRow
import org.apache.spark.sql.execution.datasources.jdbc.JdbcUtils
import org.apache.spark.sql.sources._
import org.apache.spark.sql.types.{DoubleType, FloatType, IntegerType, TimestampType, _}
import org.apache.spark.util.CompletionIterator

import scala.util.control.NonFatal

class PrestoRDD(
	sc: SparkContext,
	getConnection: () => Connection,
	schema: StructType,
	columns: Array[String],
	filters: Array[Filter],
	partitions: Array[Partition],
	url: String,
	props: Map[String, String]) extends RDD[InternalRow](sc, Nil) {

	override protected def getPartitions: Array[Partition] = partitions

	private val columnList: String = {
		val sb = new StringBuilder()
		columns.foreach(x => sb.append(",").append(x))
		if (sb.isEmpty) "1" else sb.substring(1)
	}

	private val filterWhereClause: String =
		filters
			.flatMap(PrestoRDD.compileFilter)
			.map(p => s"($p)").mkString(" AND ")

	private def getWhereClause(part: PrestoPartition): String = {
		if (part.whereClause != null && filterWhereClause.length > 0) {
			"WHERE " + s"($filterWhereClause)" + " AND " + s"(${part.whereClause})"
		} else if (part.whereClause != null) {
			"WHERE " + part.whereClause
		} else if (filterWhereClause.length > 0) {
			"WHERE " + filterWhereClause
		} else {
			""
		}
	}

	@DeveloperApi
	override def compute(split: Partition, context: TaskContext): Iterator[InternalRow] = {
		var closed = false
		var rs: ResultSet = null
		var stmt: Statement = null
		var conn: Connection = null

		def close(): Unit = {
			if (closed) return
			try {
				if (null != rs) {
					rs.close()
				}
			} catch {
				case e: Exception => logWarning("Exception closing resultset", e)
			}
			try {
				if (null != stmt) {
					stmt.close()
				}
			} catch {
				case e: Exception => logWarning("Exception closing statement", e)
			}
			try {
				if (null != conn) {
					if (!conn.isClosed && !conn.getAutoCommit) {
						try {
							conn.commit()
						} catch {
							case NonFatal(e) => logWarning("Exception committing transaction", e)
						}
					}
					conn.close()
				}
				logInfo("closed connection")
			} catch {
				case e: Exception => logWarning("Exception closing connection", e)
			}
			closed = true
		}
		context.addTaskCompletionListener{ context => close() }

		val inputMetrics = context.taskMetrics().inputMetrics
		val part = split.asInstanceOf[PrestoPartition]
		conn = getConnection()

		// H2's JDBC driver does not support the setSchema() method.  We pass a
		// fully-qualified table name in the SELECT statement.  I don't know how to
		// talk about a table in a completely portable way.

		val myWhereClause = getWhereClause(part)

		val sqlText = s"SELECT $columnList FROM ${props("dbtable")} $myWhereClause"
		stmt = conn.createStatement()
		stmt.setFetchSize(props.getOrElse("fetchSize", "0").toInt)
		rs = stmt.executeQuery(sqlText)
		val rowsIterator = JdbcUtils.resultSetToSparkInternalRows(rs, schema, inputMetrics)

		CompletionIterator[InternalRow, Iterator[InternalRow]](
			new InterruptibleIterator(context, rowsIterator), close())
	}
}

object PrestoRDD {

	def getSchema(resultSet: ResultSet): StructType = {
		val rsmd = resultSet.getMetaData
		val ncols = rsmd.getColumnCount
		val fields = new Array[StructField](ncols)
		var i = 0
		while (i < ncols) {
			val columnName = rsmd.getColumnLabel(i + 1)
			val dataType = rsmd.getColumnType(i + 1)
			val typeName = rsmd.getColumnTypeName(i + 1)
			val fieldSize = rsmd.getPrecision(i + 1)
			val fieldScale = rsmd.getScale(i + 1)
			val isSigned = {
				try {
					rsmd.isSigned(i + 1)
				} catch {
					// Workaround for HIVE-14684:
					case e: SQLException if
					e.getMessage == "Method not supported" &&
						rsmd.getClass.getName == "org.apache.hive.jdbc.HiveResultSetMetaData" => true
				}
			}
			val nullable = rsmd.isNullable(i + 1) != ResultSetMetaData.columnNoNulls
			val metadata = new MetadataBuilder()
				.putString("name", columnName)
				.putLong("scale", fieldScale)
			val columnType = getCatalystType(dataType, fieldSize, fieldScale, isSigned)
			fields(i) = StructField(columnName, columnType, nullable, metadata.build())
			i = i + 1
		}
		new StructType(fields)
	}

	private def getCatalystType(
		sqlType: Int,
		precision: Int,
		scale: Int,
		signed: Boolean): DataType = {
		val answer = sqlType match {
			// scalastyle:off
			case java.sql.Types.ARRAY         => null
			case java.sql.Types.BIGINT        => if (signed) { LongType } else { DecimalType(20,0) }
			case java.sql.Types.BINARY        => BinaryType
			case java.sql.Types.BIT           => BooleanType // @see JdbcDialect for quirks
			case java.sql.Types.BLOB          => BinaryType
			case java.sql.Types.BOOLEAN       => BooleanType
			case java.sql.Types.CHAR          => StringType
			case java.sql.Types.CLOB          => StringType
			case java.sql.Types.DATALINK      => null
			case java.sql.Types.DATE          => DateType
			case java.sql.Types.DECIMAL
				if precision != 0 || scale != 0 => DecimalType.bounded(precision, scale)
			case java.sql.Types.DECIMAL       => DecimalType.SYSTEM_DEFAULT
			case java.sql.Types.DISTINCT      => null
			case java.sql.Types.DOUBLE        => DoubleType
			case java.sql.Types.FLOAT         => FloatType
			case java.sql.Types.INTEGER       => if (signed) { IntegerType } else { LongType }
			case java.sql.Types.JAVA_OBJECT   => null
			case java.sql.Types.LONGNVARCHAR  => StringType
			case java.sql.Types.LONGVARBINARY => BinaryType
			case java.sql.Types.LONGVARCHAR   => StringType
			case java.sql.Types.NCHAR         => StringType
			case java.sql.Types.NCLOB         => StringType
			case java.sql.Types.NULL          => null
			case java.sql.Types.NUMERIC
				if precision != 0 || scale != 0 => DecimalType.bounded(precision, scale)
			case java.sql.Types.NUMERIC       => DecimalType.SYSTEM_DEFAULT
			case java.sql.Types.NVARCHAR      => StringType
			case java.sql.Types.OTHER         => null
			case java.sql.Types.REAL          => DoubleType
			case java.sql.Types.REF           => StringType
			case java.sql.Types.ROWID         => LongType
			case java.sql.Types.SMALLINT      => IntegerType
			case java.sql.Types.SQLXML        => StringType
			case java.sql.Types.STRUCT        => StringType
			case java.sql.Types.TIME          => TimestampType
			case java.sql.Types.TIMESTAMP     => TimestampType
			case java.sql.Types.TINYINT       => IntegerType
			case java.sql.Types.VARBINARY     => BinaryType
			case java.sql.Types.VARCHAR       => StringType
			case _                            => null
			// scalastyle:on
		}

		if (answer == null) throw new SQLException("Unsupported type " + sqlType)
		answer
	}

	def resolveTable(props: Map[String, String]): StructType = {
		val table = props("dbtable")

		val conn: Connection = connectionFactory(props)()
		try {
			val statement = conn.createStatement()
			try {
				val rs = statement.executeQuery(s"SELECT * FROM $table WHERE 1=0")
				try {
					getSchema(rs)
				} finally {
					rs.close()
				}
			} finally {
				statement.close()
			}
		} finally {
			conn.close()
		}
	}

	def connectionFactory(props: Map[String, String]): () => Connection = {
		Class.forName("com.facebook.presto.jdbc.PrestoDriver")
		() => DriverManager.getConnection(props("url"), props("user"), props.get("password").orNull)
	}

	private def pruneSchema(schema: StructType, columns: Array[String]): StructType = {
		val fieldMap = Map(schema.fields.map(x => x.metadata.getString("name") -> x): _*)
		new StructType(columns.map(name => fieldMap(name)))
	}

	def scanTable(
		sc: SparkContext,
		schema: StructType,
		requiredColumns: Array[String],
		filters: Array[Filter],
		parts: Array[Partition],
		props: Map[String, String]): RDD[InternalRow] = {
		val url = props("url")
		val quotedColumns = requiredColumns.map(colName => s"$colName")
		new PrestoRDD(
			sc,
			connectionFactory(props),
			pruneSchema(schema, requiredColumns),
			quotedColumns,
			filters,
			parts,
			url,
			props
		)
	}

	private def compileValue(value: Any): Any = value match {
		case stringValue: String => s"'${escapeSql(stringValue)}'"
		case timestampValue: Timestamp => "'" + timestampValue + "'"
		case dateValue: Date => "'" + dateValue + "'"
		case arrayValue: Array[Any] => arrayValue.map(compileValue).mkString(", ")
		case _ => value
	}

	private def escapeSql(value: String): String =
		if (value == null) null else StringUtils.replace(value, "'", "''")


	def compileFilter(f: Filter): Option[String] = {
		def quote(colName: String): String = colName

		Option(f match {
			case EqualTo(attr, value) => s"${quote(attr)} = ${compileValue(value)}"
			case EqualNullSafe(attr, value) =>
				val col = quote(attr)
				s"(NOT ($col != ${compileValue(value)} OR $col IS NULL OR " +
					s"${compileValue(value)} IS NULL) OR ($col IS NULL AND ${compileValue(value)} IS NULL))"
			case LessThan(attr, value) => s"${quote(attr)} < ${compileValue(value)}"
			case GreaterThan(attr, value) => s"${quote(attr)} > ${compileValue(value)}"
			case LessThanOrEqual(attr, value) => s"${quote(attr)} <= ${compileValue(value)}"
			case GreaterThanOrEqual(attr, value) => s"${quote(attr)} >= ${compileValue(value)}"
			case IsNull(attr) => s"${quote(attr)} IS NULL"
			case IsNotNull(attr) => s"${quote(attr)} IS NOT NULL"
			case StringStartsWith(attr, value) => s"${quote(attr)} LIKE '${value}%'"
			case StringEndsWith(attr, value) => s"${quote(attr)} LIKE '%${value}'"
			case StringContains(attr, value) => s"${quote(attr)} LIKE '%${value}%'"
			case In(attr, value) if value.isEmpty =>
				s"CASE WHEN ${quote(attr)} IS NULL THEN NULL ELSE FALSE END"
			case In(attr, value) => s"${quote(attr)} IN (${compileValue(value)})"
			case Not(f) => compileFilter(f).map(p => s"(NOT ($p))").getOrElse(null)
			case Or(f1, f2) =>
				// We can't compile Or filter unless both sub-filters are compiled successfully.
				// It applies too for the following And filter.
				// If we can make sure compileFilter supports all filters, we can remove this check.
				val or = Seq(f1, f2).flatMap(compileFilter(_))
				if (or.size == 2) {
					or.map(p => s"($p)").mkString(" OR ")
				} else {
					null
				}
			case And(f1, f2) =>
				val and = Seq(f1, f2).flatMap(compileFilter(_))
				if (and.size == 2) {
					and.map(p => s"($p)").mkString(" AND ")
				} else {
					null
				}
			case _ => null
		})
	}
}
