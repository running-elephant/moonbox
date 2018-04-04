package org.apache.spark.sql.datasys

import java.net.InetAddress
import java.sql.{Connection, DriverManager}
import java.util.Properties

import org.apache.spark.sql.{DataFrame, Row, SparkSession}
import org.apache.spark.sql.catalyst.plans.JoinType
import org.apache.spark.sql.catalyst.plans.logical.LogicalPlan
import org.apache.spark.sql.rdd.MbJdbcRDD
import org.apache.spark.sql.sqlbuilder.{MbMySQLDialect, MbSqlBuilder}
import org.apache.spark.sql.types.StructType

class PrestoDataSystem(props: Map[String, String])(@transient val sparkSession: SparkSession) extends DataSystem(props) {
	override val name: String = "presto"

	override protected val supportedOperators: Seq[Class[_]] = _
	override protected val supportedUDF: Seq[String] = _
	override protected val supportedExpressions: Seq[Class[_]] = _
	override protected val beGoodAtOperators: Seq[Class[_]] = _
	override protected val supportedJoinTypes: Seq[JoinType] = _

	override protected def isSupportAll: Boolean = false

	override def fastEquals(other: DataSystem): Boolean = {
		other match {
			case mysql: PrestoDataSystem =>
				socket == mysql.socket
			case _ => false
		}
	}

	override def buildScan(plan: LogicalPlan): DataFrame = {
		val sqlBuilder = new MbSqlBuilder(plan, MbMySQLDialect)
		val rdd = new MbJdbcRDD(
			sparkSession.sparkContext,
			getConnection,
			sqlBuilder.toSQL,
			rs => Row(MbJdbcRDD.resultSetToObjectArray(rs):_*)
		)
		val schema = StructType.fromAttributes(sqlBuilder.finalLogicalPlan.output)
		sparkSession.createDataFrame(rdd, schema)
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
		props.foreach { case (k, v) => p.put(k, v) }
		((url: String, props: Properties) => {
			() => {
				Class.forName("com.facebook.presto.jdbc.PrestoDriver")
				DriverManager.getConnection(url, props)
			}
		})(props("url"), p)
	}
}
