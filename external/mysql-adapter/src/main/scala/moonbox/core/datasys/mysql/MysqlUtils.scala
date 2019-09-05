package moonbox.core.datasys.mysql

import java.sql.{Connection, PreparedStatement, SQLException}
import java.util.Locale

import org.apache.spark.internal.Logging
import org.apache.spark.sql._
import org.apache.spark.sql.execution.datasources.jdbc.{JDBCOptions, JdbcUtils}
import org.apache.spark.sql.jdbc.{JdbcDialect, JdbcDialects, JdbcType}
import org.apache.spark.sql.types._

import scala.util.control.NonFatal

object MysqlUtils extends Logging {

	def updateTable(data: DataFrame, tableSchema: Option[StructType],
		isCaseSensitive: Boolean,parameter: Map[String, String]) {
		val options = new JDBCOptions(parameter)

		val url = options.url
		val table = options.table
		val dialect = JdbcDialects.get(url)
		val rddSchema = data.schema
		val getConnection: () => Connection = JdbcUtils.createConnectionFactory(options)
		val batchSize = options.batchSize
		val isolationLevel = options.isolationLevel

		val insertStmt = getUpsertStatement(table, rddSchema, tableSchema, isCaseSensitive, dialect)
		val repartitionedDF = options.numPartitions match {
			case Some(n) if n <= 0 => throw new IllegalArgumentException(
				s"Invalid value `$n` for parameter `${JDBCOptions.JDBC_NUM_PARTITIONS}` in table writing " +
					"via JDBC. The minimum value is 1.")
			case Some(n) if n < data.rdd.getNumPartitions => data.coalesce(n)
			case _ => data
		}
		repartitionedDF.foreachPartition(iterator => savePartition(
			getConnection, table, iterator, rddSchema, insertStmt, isCaseSensitive, batchSize, dialect, isolationLevel)
		)
	}

	private def getUpsertStatement(table: String,
		rddSchema: StructType,
		tableSchema: Option[StructType],
		isCaseSensitive: Boolean,
		dialect: JdbcDialect): String = {
		val placeholders = rddSchema.fields.map(_ => "?").mkString(",")
		val columnNameEquality = if (isCaseSensitive) {
			org.apache.spark.sql.catalyst.analysis.caseSensitiveResolution
		} else {
			org.apache.spark.sql.catalyst.analysis.caseInsensitiveResolution
		}
		val columns = if (tableSchema.isEmpty) {
			val fields = rddSchema.fields.map(_.name)
			fields.map(x => dialect.quoteIdentifier(x))
		} else {
			// The generated insert statement needs to follow rddSchema's column sequence and
			// tableSchema's column names. When appending data into some case-sensitive DBMSs like
			// PostgreSQL/Oracle, we need to respect the existing case-sensitive column names instead of
			// RDD column names for user convenience.
			val tableColumnNames = tableSchema.get.fieldNames
			rddSchema.fields.map { col =>
				val normalizedName = tableColumnNames.find(f => columnNameEquality(f, col.name)).getOrElse {
					throw new Exception(s"""Column "${col.name}" not found in schema $tableSchema""")
				}
				dialect.quoteIdentifier(normalizedName)
			}
		}
		val update = columns.map(s => s"$s = ?").mkString(",")
		s"INSERT INTO $table (${columns.mkString(",")}) VALUES ($placeholders) ON DUPLICATE KEY UPDATE $update"
	}

	def savePartition(
		getConnection: () => Connection,
		table: String,
		iterator: Iterator[Row],
		rddSchema: StructType,
		insertStmt: String,
		isCaseSensitive: Boolean,
		batchSize: Int,
		dialect: JdbcDialect,
		isolationLevel: Int): Iterator[Byte] = {
		val conn = getConnection()
		var committed = false

		var finalIsolationLevel = Connection.TRANSACTION_NONE
		if (isolationLevel != Connection.TRANSACTION_NONE) {
			try {
				val metadata = conn.getMetaData
				if (metadata.supportsTransactions()) {
					// Update to at least use the default isolation, if any transaction level
					// has been chosen and transactions are supported
					val defaultIsolation = metadata.getDefaultTransactionIsolation
					finalIsolationLevel = defaultIsolation
					if (metadata.supportsTransactionIsolationLevel(isolationLevel))  {
						// Finally update to actually requested level if possible
						finalIsolationLevel = isolationLevel
					} else {
						logWarning(s"Requested isolation level $isolationLevel is not supported; " +
							s"falling back to default isolation level $defaultIsolation")
					}
				} else {
					logWarning(s"Requested isolation level $isolationLevel, but transactions are unsupported")
				}
			} catch {
				case NonFatal(e) => logWarning("Exception while detecting transaction support", e)
			}
		}
		val supportsTransactions = finalIsolationLevel != Connection.TRANSACTION_NONE

		try {
			if (supportsTransactions) {
				conn.setAutoCommit(false) // Everything in the same db transaction.
				conn.setTransactionIsolation(finalIsolationLevel)
			}

			val stmt = conn.prepareStatement(insertStmt)
			val setters = rddSchema.fields.map(f => makeSetter(conn, dialect, f.dataType))
			val nullTypes = rddSchema.fields.map(f => getJdbcType(f.dataType, dialect).jdbcNullType)
			val numFields = rddSchema.fields.length

			try {
				var rowCount = 0
				while (iterator.hasNext) {
					val row = iterator.next()
					var i = 0
					while (i < numFields) {
						if (row.isNullAt(i)) {
							stmt.setNull(i + 1, nullTypes(i))
							stmt.setNull(i + 1 + numFields, nullTypes(i))
						} else {
							setters(i).apply(stmt, row, i, i)
							setters(i).apply(stmt, row, i + numFields, i)
						}
						i = i + 1
					}

					stmt.addBatch()
					rowCount += 1
					if (rowCount % batchSize == 0) {
						stmt.executeBatch()
						rowCount = 0
					}
				}
				if (rowCount > 0) {
					stmt.executeBatch()
				}
			} finally {
				stmt.close()
			}
			if (supportsTransactions) {
				conn.commit()
			}
			committed = true
			Iterator.empty
		} catch {
			case e: SQLException =>
				val cause = e.getNextException
				if (cause != null && e.getCause != cause) {
					// If there is no cause already, set 'next exception' as cause. If cause is null,
					// it *may* be because no cause was set yet
					if (e.getCause == null) {
						try {
							e.initCause(cause)
						} catch {
							// Or it may be null because the cause *was* explicitly initialized, to *null*,
							// in which case this fails. There is no other way to detect it.
							// addSuppressed in this case as well.
							case _: IllegalStateException => e.addSuppressed(cause)
						}
					} else {
						e.addSuppressed(cause)
					}
				}
				throw e
		} finally {
			if (!committed) {
				// The stage must fail.  We got here through an exception path, so
				// let the exception through unless rollback() or close() want to
				// tell the user about another problem.
				if (supportsTransactions) {
					conn.rollback()
				}
				conn.close()
			} else {
				// The stage must succeed.  We cannot propagate any exception close() might throw.
				try {
					conn.close()
				} catch {
					case e: Exception => logWarning("Transaction succeeded, but closing failed", e)
				}
			}
		}
	}

	private def getJdbcType(dt: DataType, dialect: JdbcDialect): JdbcType = {
		dialect.getJDBCType(dt).orElse(JdbcUtils.getCommonJDBCType(dt)).getOrElse(
			throw new IllegalArgumentException(s"Can't get JDBC type for ${dt.simpleString}"))
	}

	private def makeSetter(
		conn: Connection,
		dialect: JdbcDialect,
		dataType: DataType): (PreparedStatement, Row, Int, Int) => Unit = dataType match {
		case IntegerType =>
			(stmt: PreparedStatement, row: Row, pos: Int, rowPos: Int) =>
				stmt.setInt(pos + 1, row.getInt(rowPos))

		case LongType =>
			(stmt: PreparedStatement, row: Row, pos: Int, rowPos: Int) =>
				stmt.setLong(pos + 1, row.getLong(rowPos))

		case DoubleType =>
			(stmt: PreparedStatement, row: Row, pos: Int, rowPos: Int) =>
				stmt.setDouble(pos + 1, row.getDouble(rowPos))

		case FloatType =>
			(stmt: PreparedStatement, row: Row, pos: Int, rowPos: Int) =>
				stmt.setFloat(pos + 1, row.getFloat(rowPos))

		case ShortType =>
			(stmt: PreparedStatement, row: Row, pos: Int, rowPos: Int) =>
				stmt.setInt(pos + 1, row.getShort(rowPos))

		case ByteType =>
			(stmt: PreparedStatement, row: Row, pos: Int, rowPos: Int) =>
				stmt.setInt(pos + 1, row.getByte(rowPos))

		case BooleanType =>
			(stmt: PreparedStatement, row: Row, pos: Int, rowPos: Int) =>
				stmt.setBoolean(pos + 1, row.getBoolean(rowPos))

		case StringType =>
			(stmt: PreparedStatement, row: Row, pos: Int, rowPos: Int) =>
				stmt.setString(pos + 1, row.getString(rowPos))

		case BinaryType =>
			(stmt: PreparedStatement, row: Row, pos: Int, rowPos: Int) =>
				stmt.setBytes(pos + 1, row.getAs[Array[Byte]](rowPos))

		case TimestampType =>
			(stmt: PreparedStatement, row: Row, pos: Int, rowPos: Int) =>
				stmt.setTimestamp(pos + 1, row.getAs[java.sql.Timestamp](rowPos))

		case DateType =>
			(stmt: PreparedStatement, row: Row, pos: Int, rowPos: Int) =>
				stmt.setDate(pos + 1, row.getAs[java.sql.Date](rowPos))

		case t: DecimalType =>
			(stmt: PreparedStatement, row: Row, pos: Int, rowPos: Int) =>
				stmt.setBigDecimal(pos + 1, row.getDecimal(rowPos))

		case ArrayType(et, _) =>
			// remove type length parameters from end of type name
			val typeName = getJdbcType(et, dialect).databaseTypeDefinition
				.toLowerCase(Locale.ROOT).split("\\(")(0)
			(stmt: PreparedStatement, row: Row, pos: Int, rowPos: Int) =>
				val array = conn.createArrayOf(
					typeName,
					row.getSeq[AnyRef](rowPos).toArray)
				stmt.setArray(pos + 1, array)

		case _ =>
			(_: PreparedStatement, _: Row, pos: Int, rowPos: Int) =>
				throw new IllegalArgumentException(
					s"Can't translate non-null value for field $rowPos")
	}
}
