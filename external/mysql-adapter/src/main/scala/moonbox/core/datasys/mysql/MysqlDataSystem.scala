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

package moonbox.core.datasys.mysql

import java.net.InetAddress
import java.sql.{Connection, DriverManager}
import java.util.Properties

import moonbox.common.MbLogging
import moonbox.core.datasys.{DataTable, _}
import org.apache.spark.sql._
import org.apache.spark.sql.catalyst.expressions._
import org.apache.spark.sql.catalyst.expressions.aggregate._
import org.apache.spark.sql.catalyst.plans._
import org.apache.spark.sql.catalyst.plans.logical._
import org.apache.spark.sql.execution.datasources.jdbc.JdbcUtils
import org.apache.spark.sql.rdd.MbJdbcRDD
import org.apache.spark.sql.sqlbuilder.{MbMySQLDialect, MbSqlBuilder}
import org.apache.spark.sql.types._

import scala.collection.mutable.ArrayBuffer

class MysqlDataSystem(props: Map[String, String])
	extends DataSystem(props) with Pushdownable with Insertable with Updatable with MbLogging {

	checkOptions("type", "url", "user", "password")

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

	override val supportedExpressions: Seq[Class[_]] = Seq(
		classOf[Literal], classOf[AttributeReference], classOf[Alias], classOf[AggregateExpression],
		classOf[Abs], classOf[Coalesce], classOf[Greatest], classOf[If], classOf[IfNull],
		classOf[IsNull], classOf[IsNotNull], classOf[Least], classOf[NullIf],
		classOf[Rand], classOf[Acos], classOf[Asin], classOf[Atan],
		classOf[Atan2], classOf[Bin], classOf[Ceil], classOf[Cos], classOf[ToDegrees], classOf[Exp],
		classOf[Floor], classOf[Hex], classOf[Logarithm], classOf[Log10], classOf[Log2], classOf[Log],
		classOf[Pi], classOf[Pow], classOf[ToRadians], classOf[Round], classOf[Signum], classOf[Sin],
		classOf[Sqrt], classOf[Tan], classOf[Add], classOf[Subtract], classOf[Multiply], classOf[Divide],
		classOf[Remainder], classOf[Average], classOf[Count], classOf[Max], classOf[Min],
		classOf[StddevSamp], classOf[StddevPop], classOf[Sum], classOf[VarianceSamp], classOf[VariancePop],
		classOf[Ascii], classOf[Base64], classOf[Concat], classOf[ConcatWs],
		classOf[Decode], classOf[Elt], classOf[Encode], classOf[FindInSet], classOf[StringInstr],
		classOf[Lower], classOf[Length], classOf[Like], classOf[Lower], classOf[StringLocate],
		classOf[StringLPad], classOf[StringTrimLeft], classOf[StringRepeat], classOf[StringReverse], classOf[RLike],
		classOf[StringRPad], classOf[StringTrimRight], classOf[SoundEx], classOf[StringSpace],
		classOf[Substring], classOf[SubstringIndex], classOf[StringTrim], classOf[Upper], classOf[UnBase64],
		classOf[Unhex], classOf[Upper], classOf[CurrentDate], classOf[CurrentTimestamp], classOf[DateDiff],
		classOf[DateAdd], classOf[DateFormatClass], classOf[DateSub], classOf[DayOfMonth],
		classOf[DayOfYear], classOf[FromUnixTime], classOf[Hour], classOf[LastDay], classOf[Minute],
		classOf[Month], classOf[Quarter], classOf[Second], classOf[ParseToDate], classOf[UnixTimestamp],
		classOf[WeekOfYear], classOf[Year], classOf[Crc32], classOf[Md5], classOf[Sha1], classOf[Sha2],
		classOf[And], classOf[In], classOf[Not],
		classOf[Or], classOf[EqualNullSafe], classOf[EqualTo], classOf[GreaterThan],
		classOf[GreaterThanOrEqual], classOf[LessThan], classOf[LessThanOrEqual], classOf[Not], classOf[BitwiseAnd],
		classOf[BitwiseNot], classOf[BitwiseOr], classOf[BitwiseXor], classOf[Cast], classOf[CaseWhen]
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

	override def isSupportAll: Boolean = false

	override def fastEquals(other: DataSystem): Boolean = {
		other match {
			case mysql: MysqlDataSystem =>
				socket == mysql.socket
			case _ => false
		}
	}

	private def socket: (String, Int) = {
		val url = props("url").toLowerCase
		val removeProtocol = url.stripPrefix("jdbc:mysql://")
		val hostPort = removeProtocol.substring(0, removeProtocol.indexOf('/')).split(":")
		val host = hostPort(0)
		val port = if (hostPort.length > 1) hostPort(1).toInt else 3306
		(InetAddress.getByName(host).getHostAddress, port)
	}

	private def getConnection: () => Connection = {
		val p = new Properties()
		props.foreach { case (k, v) => p.put(k, v) }
		((url: String, props: Properties) => {
			() => {
				Class.forName("com.mysql.jdbc.Driver")
				DriverManager.getConnection(url, props)
			}
		})(props("url"), p)
	}

	override def buildScan(plan: LogicalPlan, sparkSession: SparkSession): DataFrame = {
		val sqlBuilder = new MbSqlBuilder(plan, new MbMySQLDialect)
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

	override def buildQuery(plan: LogicalPlan, sparkSession: SparkSession): DataTable = {
		val sqlBuilder = new MbSqlBuilder(plan, new MbMySQLDialect)
		val sql = sqlBuilder.toSQL
		val schema = sqlBuilder.finalLogicalPlan.schema
		logInfo(s"translate pushdown query sql: $sql")
		val conn = getConnection()
		val statement = conn.createStatement()
		val resultSet = try {
			statement.executeQuery(sql)
		} catch {
			case e: Exception =>
				conn.close()
				logWarning("mysql pushdown execute failed.", e)
				throw e
		}

		def close(): Unit = {
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
					logInfo("closed connection")
				}
			} catch {
				case e: Exception => logWarning("Exception closing connection", e)
			}
		}

		val iter = JdbcUtils.resultSetToRows(resultSet, schema)
		new DataTable(iter, schema, close _)
	}

    override def tableNames(): Seq[String] = {
		val tables = new ArrayBuffer[String]()
		val connection = getConnection()
		val resultSet = connection.createStatement().executeQuery("show tables")
		while (resultSet.next()) {
			tables.+=:(resultSet.getString(1))
		}
		connection.close()
		tables
	}

    override def tableProperties(tableName: String): Map[String, String] = {
		props.+("dbtable" -> tableName)
	}
	// TODO
    override def insert(table: DataTable, saveMode: SaveMode): Unit = {
		throw new Exception("Unsupport operation: insert with datatalbe.")
	}

	override def tableName(): String = {
		props("dbtable")
	}

	override def test(): Unit = {
		var connection: Connection = null
		try  {
			connection = getConnection()
		} catch {
			case e: Exception =>
				logError("mysql test failed.", e)
				throw e
		} finally {
			if (connection != null) {
				connection.close()
			}
		}
	}

	override def update(data: DataFrame, tableSchema: Option[StructType],
		isCaseSensitive: Boolean,parameter: Map[String, String]): Unit = {
		MysqlUtils.updateTable(data, tableSchema, isCaseSensitive, parameter)
	}

}
