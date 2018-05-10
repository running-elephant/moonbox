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
    //--------------------
    /* classOf[Least], classOf[NullIf],
    classOf[Rand], classOf[Acos], classOf[Asin], classOf[Atan],
    classOf[Atan2], classOf[Bin], classOf[Ceil], classOf[Cos], classOf[ToDegrees], classOf[Exp],
    classOf[Floor], classOf[Hex], classOf[Logarithm], classOf[Log10], classOf[Log2], classOf[Log],
    classOf[Pi], classOf[Pow], classOf[ToRadians], classOf[Round], classOf[Signum], classOf[Sin],
    classOf[Sqrt], classOf[Tan],
    classOf[Remainder],
    classOf[StddevSamp], classOf[StddevPop], classOf[VarianceSamp], classOf[VariancePop],
    classOf[Ascii], classOf[Base64],  classOf[ConcatWs],
    classOf[Decode], classOf[Elt], classOf[Encode], classOf[FindInSet], classOf[StringInstr],
    classOf[Length], classOf[Like], classOf[StringLocate],
    classOf[StringLPad], classOf[StringTrimLeft], classOf[StringRepeat], classOf[StringReverse], classOf[RLike],
    classOf[StringRPad], classOf[StringTrimRight], classOf[SoundEx], classOf[StringSpace],
     classOf[SubstringIndex], classOf[StringTrim], classOf[UnBase64],
    classOf[Unhex], classOf[CurrentDate], classOf[CurrentTimestamp], classOf[DateDiff],
    classOf[DateAdd], classOf[DateFormatClass], classOf[DateSub],
    classOf[FromUnixTime], classOf[LastDay],
     classOf[Quarter],  classOf[ParseToDate], classOf[UnixTimestamp],
     classOf[Crc32], classOf[Md5], classOf[Sha1], classOf[Sha2],
    classOf[In],
    classOf[EqualNullSafe], classOf[BitwiseAnd],
    classOf[BitwiseNot], classOf[BitwiseOr], classOf[BitwiseXor], classOf[Cast],
    // to be considered
    classOf[Greatest], classOf[If], classOf[IfNull],
    // unsupported
    classOf[Coalesce]*/
  )
  override protected val beGoodAtOperators: Seq[Class[_]] = Seq(classOf[Project], classOf[Filter], classOf[Aggregate], classOf[Sort], classOf[GlobalLimit], classOf[LocalLimit])
  override protected val supportedUDF: Seq[String] = Seq()

  override protected def isSupportAll: Boolean = false

  override def fastEquals(other: DataSystem): Boolean = {
    // same host and port
    other match {
      case m: MongoDataSystem =>
        this.hostAndPort == m.hostAndPort
      case _ => false
    }
  }

  private def hostAndPort: (String, Int) = {
    val tailUrl = props("url").toLowerCase.stripPrefix("jdbc:mongo://")
    val h = tailUrl.split("/")
    h.length match {
      case 2 =>
        val hp = h(0).split(":")
        hp.length match {
          case 2 => (InetAddress.getByName(hp(0)).getHostAddress, hp(1).toInt)
          case 1 => (InetAddress.getByName(hp(0)).getHostAddress, 27017)
          case _ => throw new Exception("invalid host or port")
        }
      case _ => throw new Exception("invalid host, port or database")
    }
  }

  //  private def converter: (Option[StructType], Seq[Any]) => Row = {
  //    (_: Option[StructType], rows: Seq[Any]) => {
  //      Row(rows: _*)
  //    }
  //  }

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
