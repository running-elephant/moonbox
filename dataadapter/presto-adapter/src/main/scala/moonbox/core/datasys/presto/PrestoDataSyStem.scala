package moonbox.core.datasys.presto

import java.net.InetAddress
import java.sql.{Connection, DriverManager}
import java.util.Properties

import moonbox.common.MbLogging
import moonbox.core.datasys.{DataSystem, DataSystemRegister, Pushdownable}
import moonbox.core.execution.standalone.DataTable
import moonbox.core.util.MbIterator
import org.apache.spark.sql.catalyst.expressions._
import org.apache.spark.sql.catalyst.expressions.aggregate._
import org.apache.spark.sql.catalyst.plans._
import org.apache.spark.sql.catalyst.plans.logical._
import org.apache.spark.sql.rdd.MbJdbcRDD
import org.apache.spark.sql.sqlbuilder.{MbPrestoDialect, MbSqlBuilder}
import org.apache.spark.sql.{DataFrame, Row, SparkSession}

class PrestoDataSystem(props: Map[String, String]) extends DataSystem(props)
	with Pushdownable with MbLogging {

	override val supportedOperators: Seq[Class[_]] = Seq(
		classOf[Project],
		classOf[Filter],
		classOf[Aggregate],
		classOf[Sort],
		classOf[Join],
		classOf[Union],
		classOf[GlobalLimit],
		classOf[LocalLimit],
		classOf[Subquery],
		classOf[SubqueryAlias]
	)
	override val supportedUDF: Seq[String] = Seq()

	override val supportedExpressions: Seq[Class[_]] = Seq(
		classOf[AttributeReference], classOf[Alias],
		classOf[Abs], classOf[Coalesce], classOf[Greatest], classOf[IsNaN], classOf[NullIf],
		classOf[Least], classOf[Rand], classOf[Acos], classOf[Asin], classOf[Atan],
		classOf[Atan2], classOf[Cbrt], classOf[Ceil], classOf[Cos], classOf[Cosh],
		classOf[ToDegrees], classOf[EulerNumber], classOf[Exp], classOf[Floor], classOf[Hex],
		classOf[Logarithm], classOf[Log10], classOf[Log2], classOf[Log], classOf[Pi], classOf[Pow],
		classOf[ToRadians], classOf[Round], classOf[Signum], classOf[Sin], classOf[Sqrt], classOf[Tan],
		classOf[Tanh], classOf[Add], classOf[Subtract], classOf[Multiply], classOf[Divide],
		classOf[Remainder], classOf[Average], classOf[Corr], classOf[Count], classOf[CovPopulation],
		classOf[CovSample], classOf[Kurtosis], classOf[Last], classOf[Max], classOf[Min],
		classOf[ApproximatePercentile], classOf[StddevSamp], classOf[StddevPop], classOf[Sum],
		classOf[VarianceSamp], classOf[VariancePop], classOf[Base64], classOf[Skewness],
		classOf[Concat], classOf[Length], classOf[Like], classOf[Lower], classOf[StringLPad],
		classOf[StringTrimLeft], classOf[RegExpExtract], classOf[RegExpReplace], classOf[StringRepeat],
		classOf[StringReverse], classOf[StringRPad], classOf[StringTrimRight], classOf[StringSplit],
		classOf[Substring], classOf[StringTrim], classOf[UnBase64], classOf[Unhex], classOf[Upper],
		classOf[CurrentDate], classOf[CurrentTimestamp], classOf[DateDiff], classOf[DateAdd],
		classOf[DateFormatClass], classOf[DayOfMonth], classOf[DayOfYear], classOf[FromUnixTime],
		classOf[Hour], classOf[Minute], classOf[Month], classOf[CurrentTimestamp], classOf[Quarter],
		classOf[Second], classOf[ParseToTimestamp], classOf[ParseToDate], classOf[TruncDate],
		classOf[WeekOfYear], classOf[Year], classOf[CreateMap], classOf[MapKeys], classOf[MapValues],
		classOf[SortArray], classOf[Crc32], classOf[Md5], classOf[Sha1], classOf[Lead], classOf[Lag],
		classOf[RowNumber], classOf[CumeDist], classOf[NTile], classOf[Rank], classOf[DenseRank],
		classOf[PercentRank], classOf[And], classOf[In], classOf[Not], classOf[Or],
		classOf[EqualTo], classOf[GreaterThan], classOf[GreaterThanOrEqual], classOf[LessThan],
		classOf[LessThanOrEqual], classOf[Not], classOf[BitwiseAnd], classOf[BitwiseNot],
		classOf[BitwiseOr], classOf[BitwiseXor], classOf[Cast], classOf[CaseWhen], classOf[If]
	)

	override val beGoodAtOperators: Seq[Class[_]] = supportedOperators

	override val supportedJoinTypes: Seq[JoinType] = Seq(
		Inner, Cross, LeftOuter, RightOuter
	)

	override def isSupportAll: Boolean = false

	override def fastEquals(other: DataSystem): Boolean = {
		other match {
			case presto: PrestoDataSystem =>
				socket == presto.socket
			case _ => false
		}
	}

	override def buildScan(plan: LogicalPlan, sparkSession: SparkSession): DataFrame = {
		val sqlBuilder = new MbSqlBuilder(plan, MbPrestoDialect)
		val rdd = new MbJdbcRDD(
			sparkSession.sparkContext,
			getConnection,
			sqlBuilder.toSQL,
			rs => Row(MbJdbcRDD.resultSetToObjectArray(rs):_*)
		)
		val schema = sqlBuilder.finalLogicalPlan.schema
		sparkSession.createDataFrame(rdd, schema)
	}

	override def buildQuery(plan: LogicalPlan): DataTable = {
		val sqlBuilder = new MbSqlBuilder(plan, MbPrestoDialect)
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

	private def socket: (String, Int) = {
		val url = props("url").toLowerCase
		val removeProtocol = url.stripPrefix("jdbc:presto://")
		val hostPort = removeProtocol.substring(0, removeProtocol.indexOf('/')).split(":")
		val host = hostPort(0)
		val port = if (hostPort.length > 1) hostPort(1).toInt else 8080
		(InetAddress.getByName(host).getHostAddress, port)
	}

	private def getConnection: () => Connection = {
		val p = new Properties()
		p.put("user", props("user"))
		if (props.contains("password")) {
			p.put("password", props.get("password"))
		}
		((url: String, props: Properties) => {
			() => {
				Class.forName("com.facebook.presto.jdbc.PrestoDriver")
				DriverManager.getConnection(url, props)
			}
		})(props("url"), p)
	}

	// TODO
	override def tableNames(): Seq[String] = Seq()

	override def tableProperties(tableName: String): Map[String, String] = Map()

	override def tableName(): String = { "" }


}
