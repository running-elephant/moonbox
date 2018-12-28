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

package moonbox.core.datasys.sqlserver

import java.net.InetAddress
import java.sql.{Connection, DriverManager}
import java.util.Properties

import moonbox.common.MbLogging
import moonbox.core.util.MbIterator
import moonbox.core.datasys.{DataTable, _}
import org.apache.spark.sql.catalyst.expressions._
import org.apache.spark.sql.catalyst.expressions.aggregate._
import org.apache.spark.sql.catalyst.plans._
import org.apache.spark.sql.catalyst.plans.logical._
import org.apache.spark.sql.rdd.MbJdbcRDD
import org.apache.spark.sql.sqlbuilder.{MbSqlBuilder, MbSqlServerDialect}
import org.apache.spark.sql.{DataFrame, Row, SaveMode, SparkSession}

import scala.collection.mutable.ArrayBuffer

class SqlServerDataSystem(props: Map[String, String])
	extends DataSystem(props) with Pushdownable with Insertable with MbLogging {

	require(contains("type", "url", "user", "password"))

	override def tableNames(): Seq[String] = {
		val tables = new ArrayBuffer[String]()
		val connection = getConnection()
		val resultSet = connection.createStatement().executeQuery("select name from SysObjects where xtype = 'u' or xtype = 'v'")
		while (resultSet.next()) {
			tables.+=:(resultSet.getString(1))
		}
		connection.close()
		tables
	}

	override def tableName(): String = {
		props("dbtable")
	}

	override def tableProperties(tableName: String): Map[String, String] = {
		props.+("dbtable" -> tableName)
	}

	override def buildQuery(plan: LogicalPlan): DataTable = {
		val sqlBuilder = new MbSqlBuilder(plan, new MbSqlServerDialect)
		val sql = sqlBuilder.toSQL
		val schema = sqlBuilder.finalLogicalPlan.schema
		logInfo(s"query sql: $sql")
		val iter = new MbIterator[Row] {
			val conn = getConnection()
			val statement = conn.createStatement()
			val resultSet = statement.executeQuery(sql)

			override def close(): Unit = {
				try {
					if (null != resultSet) {
						resultSet.close()
					}
				} catch {
					case e: Exception => logWarning("Exception closing resultset", e)
				}
				try {
					if (null != statement) {
						statement.isClosed
					}
				} catch {
					case e: Exception => logWarning("Exception closing statement", e)
				}
				try {
					if (null != conn) {
						conn.close()
					}
					logInfo("closed connection")
				} catch {
					case e: Exception => logWarning("Exception closing connection", e)
				}
			}

			override def getNext(): Row = {
				if (resultSet != null && resultSet.next()) {
					Row(MbJdbcRDD.resultSetToObjectArray(resultSet):_*)
				} else {
					finished = true
					null.asInstanceOf[Row]
				}
			}
		}
		new DataTable(iter, schema, () => iter.closeIfNeeded())
	}

	override def isSupportAll: Boolean = false

	override def fastEquals(other: DataSystem): Boolean = {
		other match {
			case sqlserver: SqlServerDataSystem =>
				socket == sqlserver.socket
			case _ => false
		}
	}

	override def buildScan(plan: LogicalPlan, sparkSession: SparkSession): DataFrame = {
		val sqlBuilder = new MbSqlBuilder(plan, new MbSqlServerDialect)
		val sql = sqlBuilder.toSQL
		logInfo(s"pushdown sql : $sql")
		val rdd = new MbJdbcRDD(
			sparkSession.sparkContext,
			getConnection,
			sql,
			rs => Row(MbJdbcRDD.resultSetToObjectArray(rs):_*)
		)
		val schema = sqlBuilder.finalLogicalPlan.schema
		sparkSession.createDataFrame(rdd, schema)
	}

	override val supportedOperators: Seq[Class[_]] = Seq(
		classOf[Project],
		classOf[Filter],
		classOf[Aggregate],
		classOf[Sort],
		classOf[Join],
		classOf[GlobalLimit],
		classOf[LocalLimit],
		classOf[Subquery],
		classOf[SubqueryAlias]
	)
	override val supportedUDF: Seq[String] = Seq()

	// approximate function:
	//IF - IIF,  BIN - BINARY_CHECKSUM ,  ceil - CEILING,  pow - POWER
	//std  - STDEV , encode  - CERTENCODED, split  - STRING_SPLIT, hash  -  HASHBYTES
	//MSSQL Function: https://docs.microsoft.com/en-us/sql/t-sql/functions/functions?view=sql-server-2017
	override val supportedExpressions: Seq[Class[_]] = Seq(
		classOf[Literal], classOf[AttributeReference], classOf[Alias], classOf[AggregateExpression],
		classOf[Abs], classOf[Coalesce], classOf[IsNull], classOf[IsNotNull],
		classOf[Rand], classOf[Acos], classOf[Asin], classOf[Atan], classOf[Ceil],
		classOf[Cos], classOf[Cosh], classOf[CaseWhen], classOf[ToDegrees],
		classOf[Exp], classOf[Floor], classOf[Floor], classOf[Logarithm], classOf[Log10],
		classOf[Pi], classOf[Pow], classOf[ToRadians], classOf[Round], classOf[Signum],
		classOf[Sin], classOf[Sqrt], classOf[Tan], classOf[Average], classOf[Count],
		classOf[First], classOf[Last], classOf[Max], classOf[Min], classOf[Sum],
		classOf[Ascii], classOf[Concat], classOf[ConcatWs], classOf[Like], classOf[Lower],
		classOf[StringTrimLeft], classOf[StringReverse], classOf[StringTrimRight],
		classOf[SoundEx], classOf[StringSpace], classOf[Substring], classOf[StringTranslate],
		classOf[StringTrim], classOf[Upper], classOf[CurrentTimestamp], classOf[DateDiff],
		classOf[DayOfMonth], classOf[Month], classOf[Year], classOf[Grouping], classOf[Grouping],
		classOf[GroupingID], classOf[Lead], classOf[Lag], classOf[RowNumber], classOf[CumeDist],
		classOf[NTile], classOf[Rank], classOf[DenseRank], classOf[PercentRank], classOf[And],
		classOf[In], classOf[Not], classOf[Or], classOf[EqualTo], classOf[GreaterThan],
		classOf[GreaterThanOrEqual], classOf[BitwiseAnd], classOf[BitwiseNot], classOf[BitwiseOr],
		classOf[BitwiseXor], classOf[Cast], classOf[Add], classOf[Subtract], classOf[Multiply],
		classOf[Divide], classOf[Remainder], classOf[If], classOf[Bin], classOf[Ceil], classOf[Pow],
		classOf[StddevSamp], classOf[Encode], classOf[StringSplit], classOf[Murmur3Hash]
	)

	override val beGoodAtOperators: Seq[Class[_]] = Seq(
		classOf[Join],
		classOf[GlobalLimit],
		classOf[LocalLimit],
		classOf[Aggregate]
	)

	override val supportedJoinTypes: Seq[JoinType] = Seq(
		Inner, Cross, LeftOuter, RightOuter
	)

	override def insert(table: DataTable, saveMode: SaveMode): Unit = {
		throw new Exception("Unsupport operation: insert with datatalbe.")
	}

	private def socket: (String, Int) = {
		val url = props("url").toLowerCase
		val removeProtocol = url.stripPrefix("jdbc:sqlserver://")
		val hostPort = removeProtocol.substring(0, removeProtocol.indexOf(';')).split(":")
		val host = hostPort(0)
		val port = if (hostPort.length > 1) hostPort(1).toInt else 1433
		(InetAddress.getByName(host).getHostAddress, port)
	}

	private def getConnection: () => Connection = {
		val p = new Properties()
		props.foreach { case (k, v) => p.put(k, v) }
		((url: String, props: Properties) => {
			() => {
				Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver")
				DriverManager.getConnection(url, props)
			}
		})(props("url"), p)
	}
}
