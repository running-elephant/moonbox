package org.apache.spark.sql.datasys

import java.net.InetAddress
import java.util.Properties

import com.mongodb.spark.MongoSpark
import moonbox.catalyst.adapter.mongo.MongoCatalystQueryExecutor
import moonbox.catalyst.core.parser.udf.{ArrayFilter, ArrayMap}
import moonbox.common.MbLogging
import org.apache.spark.sql.catalyst.expressions._
import org.apache.spark.sql.catalyst.expressions.aggregate._
import org.apache.spark.sql.catalyst.plans.JoinType
import org.apache.spark.sql.catalyst.plans.logical._
import org.apache.spark.sql.{DataFrame, SparkSession}
import org.bson.BsonDocument

class MongoDataSystem(props: Map[String, String])(@transient val sparkSession: SparkSession) extends DataSystem(props) with MbLogging {

  override val name: String = "Mongo"
  override protected val supportedOperators: Seq[Class[_]] = Seq(
    classOf[Project],
    classOf[Filter],
    classOf[Aggregate],
    classOf[Sort],
    classOf[GlobalLimit],
    classOf[LocalLimit]
  )
  override protected val supportedJoinTypes: Seq[JoinType] = Seq()
  override protected val supportedExpressions: Seq[Class[_]] = Seq(
    classOf[AttributeReference], classOf[Alias], classOf[Literal],
    classOf[Abs], classOf[Not], classOf[And], classOf[Or], classOf[EqualTo], classOf[Max], classOf[Min], classOf[Average], classOf[Count], classOf[Add], classOf[Subtract], classOf[Multiply], classOf[Divide],
    classOf[Sum], classOf[GreaterThan], classOf[GreaterThanOrEqual], classOf[LessThan], classOf[LessThanOrEqual], classOf[Not],
    classOf[ArrayMap], classOf[ArrayFilter], classOf[IsNull], classOf[IsNotNull], classOf[Lower], classOf[Upper], classOf[Substring], classOf[Hour], classOf[Second], classOf[Month], classOf[Minute],
    classOf[Year], classOf[WeekOfYear], classOf[CaseWhen], classOf[DayOfYear], classOf[Concat], classOf[DayOfMonth], classOf[CaseWhenCodegen]
  )
  override protected val beGoodAtOperators: Seq[Class[_]] = Seq(classOf[Project], classOf[Filter], classOf[Aggregate], classOf[Sort], classOf[GlobalLimit], classOf[LocalLimit])
  override protected val supportedUDF: Seq[String] = Seq()

  override protected def isSupportAll: Boolean = false

  override def fastEquals(other: DataSystem): Boolean = false

  override def buildScan(plan: LogicalPlan): DataFrame = {
    val builder = MongoSpark.builder().sparkSession(sparkSession)
    val newProps = new Properties()
    props.foreach {
      case (k, v) =>
        newProps.setProperty(k, v)
        builder.option(k, v)
    }
    val pipeline = new MongoCatalystQueryExecutor(newProps).translate(plan).map(BsonDocument.parse)
    logInfo(pipeline.map(_.toJson).mkString("\n"))
    val schema = plan.schema
    builder.pipeline(pipeline).build().toDF(schema)
  }
}
