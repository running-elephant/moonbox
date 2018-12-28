package moonbox.core.datasys.clickhouse

import java.net.InetAddress
import java.sql.{Connection, DriverManager}
import java.util.Properties

import moonbox.common.MbLogging
import moonbox.core.datasys.{DataSystem, DataTable, Insertable, Pushdownable}
import moonbox.core.util.MbIterator
import org.apache.spark.sql.catalyst.expressions.aggregate._
import org.apache.spark.sql.catalyst.expressions.{Abs, Acos, Add, Alias, And, Asin, Atan, AttributeReference, BitwiseAnd, BitwiseNot, BitwiseOr, BitwiseXor, CaseWhen, CaseWhenBase, CaseWhenCodegen, Cast, Cbrt, Ceil, Concat, Cos, CurrentDatabase, CurrentDate, CurrentTimestamp, DayOfMonth, Divide, EqualTo, EulerNumber, Exp, Floor, GreaterThan, GreaterThanOrEqual, Hex, Hour, If, In, IsNotNull, IsNull, Length, LessThan, LessThanOrEqual, Literal, Log, Log10, Log2, Lower, Minute, Month, Multiply, Not, Or, Pi, Pow, RLike, Rand, RegExpExtract, RegExpReplace, Remainder, Round, Second, ShiftLeft, ShiftRight, Sin, SortOrder, Sqrt, StringLocate, StringReverse, Subtract, Tan, ToDate, UnaryMinus, Unhex, Upper, Year}
import org.apache.spark.sql.catalyst.plans._
import org.apache.spark.sql.catalyst.plans.logical._
import org.apache.spark.sql.execution.datasources.jdbc.JdbcUtils
import org.apache.spark.sql.rdd.MbJdbcRDD
import org.apache.spark.sql.sqlbuilder.{MbClickHouseDialect, MbSqlBuilder}
import org.apache.spark.sql.{DataFrame, Row, SaveMode, SparkSession}

import scala.collection.mutable.ArrayBuffer

class ClickHouseDataSystem(props: Map[String, String])
  extends DataSystem(props) with Pushdownable with Insertable with MbLogging {
  require(contains("type", "url", "user", "password"))

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

  private def getConnection: () => Connection = {
    val p = new Properties()
    props.foreach { case (k, v) => p.put(k, v) }
    ((url: String, props: Properties) => {
      () => {
        Class.forName("ru.yandex.clickhouse.ClickHouseDriver")
        DriverManager.getConnection(url, props)
      }
    }) (props("url"), p)
  }

  override def tableProperties(tableName: String): Map[String, String] = {
    props.+("dbtable" -> tableName)
  }


  override def tableName(): String = {
    props("dbtable")
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
    classOf[SubqueryAlias],
    classOf[Union]
  )

  override val supportedJoinTypes: Seq[JoinType] = Seq(
    Inner, LeftOuter, LeftSemi, LeftAnti
  )

  //todo: expression without override may have subquery table name, ck didn't support
  //todo: like expression
  //todo: md5/sha1/isNaN type cast optimize

  override val supportedExpressions: Seq[Class[_]] = Seq(
    classOf[Add], classOf[Subtract], classOf[Multiply], classOf[Divide],
    classOf[Remainder], classOf[Abs], classOf[UnaryMinus],
    classOf[BitwiseNot], classOf[BitwiseXor], classOf[BitwiseOr], classOf[BitwiseAnd], classOf[ShiftLeft], classOf[ShiftRight],
    classOf[LessThan], classOf[LessThanOrEqual], classOf[EqualTo], classOf[GreaterThanOrEqual], classOf[GreaterThan],
    classOf[Not], classOf[And], classOf[Or],
    classOf[ToDate], classOf[Year], classOf[Month], classOf[DayOfMonth], classOf[Hour], classOf[Minute], classOf[Second], classOf[CurrentTimestamp], classOf[CurrentDate],
    classOf[Length], classOf[IsNull], classOf[IsNotNull], classOf[Lower], classOf[Upper], classOf[StringReverse], classOf[StringLocate],
    classOf[Cast], classOf[Concat], classOf[RLike], classOf[RegExpReplace], classOf[RegExpExtract],
    classOf[If], classOf[CaseWhen], classOf[CaseWhenCodegen],
    classOf[EulerNumber], classOf[Pi], classOf[Exp], classOf[Log], classOf[Log2], classOf[Log10],
    classOf[Sqrt], classOf[Cbrt], classOf[Sin], classOf[Cos], classOf[Tan], classOf[Asin], classOf[Acos], classOf[Atan], classOf[Pow], classOf[Floor], classOf[Ceil], classOf[Round],
    //    classOf[ArrayContains], classOf[Size], classOf[CreateArray], classOf[SortArray],
    //    classOf[Md5], classOf[Sha1],classOf[Like], classOf[IsNaN],
    classOf[Rand], classOf[Hex], classOf[Unhex],
    classOf[CurrentDatabase],
    classOf[In], classOf[SortOrder],
    classOf[Sum], classOf[Count], classOf[Min], classOf[Max], classOf[Average], classOf[Last], classOf[Distinct],
    classOf[AggregateExpression], classOf[Literal], classOf[AttributeReference], classOf[Alias]
  )


  override val beGoodAtOperators: Seq[Class[_]] = supportedOperators

  override val supportedUDF: Seq[String] = Seq()

  override def isSupportAll: Boolean = false

  override def fastEquals(other: DataSystem): Boolean = {
    other match {
      case clickHouse: ClickHouseDataSystem =>
        socket == clickHouse.socket
      case _ => false
    }
  }

  private def socket: (String, Int) = {
    val url = props("url").toLowerCase
    val removeProtocol = url.stripPrefix("jdbc:clickhouse://")

    val hostPort = removeProtocol.substring(0, removeProtocol.indexOf('/')).split(":")
    val host = hostPort(0)
    val port = if (hostPort.length > 1) hostPort(1).toInt else 8123
    (InetAddress.getByName(host).getHostAddress, port)
  }

  override def buildScan(plan: LogicalPlan, sparkSession: SparkSession): DataFrame = {
    val sqlBuilder = new MbSqlBuilder(plan, new MbClickHouseDialect)
    val sql = sqlBuilder.toSQL
    logInfo(s"pushdown sql : $sql")
    val rdd = new MbJdbcRDD(
      sparkSession.sparkContext,
      getConnection,
      sql,
      rs => Row(MbJdbcRDD.resultSetToObjectArray(rs): _*)
    )
    val schema = sqlBuilder.finalLogicalPlan.schema
    sparkSession.createDataFrame(rdd, schema)
  }


  override def buildQuery(plan: LogicalPlan): DataTable = {
    val sqlBuilder = new MbSqlBuilder(plan, new MbClickHouseDialect)
    val sql = sqlBuilder.toSQL
    val schema = sqlBuilder.finalLogicalPlan.schema
    logInfo(s"query sql: $sql")
	  val conn = getConnection()
	  val statement = conn.createStatement()
	  val resultSet = statement.executeQuery(sql)

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
			  }
			  logInfo("closed connection")
		  } catch {
			  case e: Exception => logWarning("Exception closing connection", e)
		  }
	  }

	  val iter = JdbcUtils.resultSetToRows(resultSet, schema)
	  new DataTable(iter, schema, close _)
  }


  override def insert(table: DataTable, saveMode: SaveMode): Unit = {
	  throw new Exception("Unsupport operation: insert with datatalbe.")
  }

  override def test(): Boolean = {
    var connection: Connection = null
    try {
      connection = getConnection()
      if (connection != null) {
        true
      } else {
        false
      }
    } catch {
      case e: Exception =>
        logWarning(e.getMessage)
        logWarning(e.getStackTrace.mkString("\n"))
        false
    } finally {
      if (connection != null) {
        connection.close()
      }
    }
  }
}
