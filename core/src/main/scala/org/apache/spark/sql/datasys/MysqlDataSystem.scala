package org.apache.spark.sql.datasys

import java.net.InetAddress
import java.sql.{Connection, DriverManager, ResultSet}
import java.util.Properties

import moonbox.common.MbLogging
import moonbox.core.execution.standalone.DataTable
import org.apache.spark.sql.catalyst.expressions._
import org.apache.spark.sql.catalyst.expressions.aggregate._
import org.apache.spark.sql.catalyst.plans._
import org.apache.spark.sql.catalyst.plans.logical._
import org.apache.spark.sql.rdd.MbJdbcRDD
import org.apache.spark.sql.sqlbuilder.{MbMySQLDialect, MbSqlBuilder}
import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.{DataFrame, Row, SaveMode, SparkSession}
import org.apache.spark.util.NextIterator

class MysqlDataSystem(props: Map[String, String])(@transient val sparkSession: SparkSession)
	extends DataSystem(props) with Queryable with Insertable with MbLogging {

	require(contains("type", "url", "user", "password", "dbtable"))

	override val name: String = "mysql"

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
		classOf[Literal], classOf[AttributeReference], classOf[Alias],
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

	override def buildScan(plan: LogicalPlan): DataFrame = {
		val sqlBuilder = new MbSqlBuilder(plan, MbMySQLDialect)
		val sql = sqlBuilder.toSQL
		logInfo(s"pushdown sql : $sql")
		val rdd = new MbJdbcRDD(
			sparkSession.sparkContext,
			getConnection,
			sql,
			rs => Row(MbJdbcRDD.resultSetToObjectArray(rs):_*)
		)
		val schema = StructType.fromAttributes(sqlBuilder.finalLogicalPlan.output)
		sparkSession.createDataFrame(rdd, schema)
	}

	override def buildQuery(plan: LogicalPlan): DataTable = {
		val sqlBuilder = new MbSqlBuilder(plan, MbMySQLDialect)
		val sql = sqlBuilder.toSQL
		val schema = StructType.fromAttributes(sqlBuilder.finalLogicalPlan.output)
		logInfo(s"query sql: $sql")
		val iter = new NextIterator[Row] {
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

    override def tableNames(): Seq[String] = Seq()

    override def tableOption(tableName: String): Map[String, String] = Map()

    override def insert(table: DataTable, saveMode: SaveMode): Unit = {}
}
