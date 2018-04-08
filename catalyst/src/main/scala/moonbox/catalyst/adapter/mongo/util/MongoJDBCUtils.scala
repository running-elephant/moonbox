package moonbox.catalyst.adapter.mongo.util

import java.sql.Types

import org.apache.spark.sql.types._
import org.bson.BsonValue

import scala.collection.JavaConverters._

object MongoJDBCUtils {

  val URL_PREFIX: String = "jdbc:mongo://"
  val HOST_KEY: String = "host"
  val PORT_KEY: String = "port"
  val DB_NAME: String = "database"
  val COLLECTION_NAME = "collection"
  val USER_KEY = "user"
  val PASSWORD_KEY = "password"
  val HOSTS_AND_PORTS = "nodes"
  val SPLITER_KEY: String = ","

  def parseHostsAndPorts(hostsAndPorts: String): Seq[(String, String)] = {
    if (hostsAndPorts != null && hostsAndPorts.length > 0)
      hostsAndPorts.split(",").map { hp =>
        val h_p = hp.split(":")
        if (h_p.length == 2) {
          (h_p(0).trim, h_p(1).trim)
        } else null
      }.filter(_ != null).toSeq
    else Seq(("127.0.0.1", "27017"))
  }

  def bsonValue2Value(bsonValue: BsonValue, fieldName: Seq[String]): Any = {
    if (bsonValue.isArray) {
      if (fieldName.isEmpty) {
        bsonValue.asArray().getValues.asScala.map(v => bsonValue2Value(v, fieldName)).toArray
      } else {
        bsonValue.asArray().getValues.asScala.filter(doc =>
          doc.isDocument && doc.asDocument().containsKey(fieldName.head)
        ).map(doc =>
          bsonValue2Value(doc.asDocument().get(fieldName.head), fieldName.tail)
        ).toArray
      }
    } else if (bsonValue.isBinary) {
      bsonValue.asBinary().getData
    } else if (bsonValue.isBoolean) {
      bsonValue.asBoolean().getValue
    } else if (bsonValue.isDateTime) {
      bsonValue.asDateTime().getValue
    } else if (bsonValue.isDBPointer) {
      /** For DBPointer mongo type, return the namespace */
      /** deprecated */
      bsonValue.asDBPointer().getNamespace
    } else if (bsonValue.isDecimal128) {
      /** new in version 3.4 */
      bsonValue.asDecimal128.getValue.bigDecimalValue()
    } else if (bsonValue.isDocument) {
      if (fieldName.isEmpty)
        bsonValue.asDocument()
      else
        bsonValue2Value(bsonValue.asDocument().get(fieldName.head), fieldName.tail)
    } else if (bsonValue.isDouble) {
      bsonValue.asDouble.getValue
    } else if (bsonValue.isInt32) {
      bsonValue.asInt32.getValue
    } else if (bsonValue.isInt64) {
      bsonValue.asInt64().getValue
    } else if (bsonValue.isJavaScript) {
      /**javaScript*/
      bsonValue.asJavaScript().getCode
    } else if (bsonValue.isJavaScriptWithScope) {
      /**javaScriptWithScope*/
      bsonValue.asJavaScriptWithScope
    } else if (bsonValue.isNull) {
      null
    } else if (bsonValue.isNumber) {
      /** Actually this condition is inaccessible */
      bsonValue2Value(bsonValue.asNumber(), fieldName)
    } else if (bsonValue.isObjectId) {
      // TODO: handle ObjectId
      // return the ObjectId object
      bsonValue.asObjectId().getValue
    } else if (bsonValue.isRegularExpression) {
      // TODO: handle regx
      // return the RegularExpression object
      bsonValue.asRegularExpression()
    } else if (bsonValue.isString) {
      bsonValue.asString().getValue
    } else if (bsonValue.isSymbol) {
      /** deprecated */
      bsonValue.asSymbol().getSymbol
    } else if (bsonValue.isTimestamp) {
      bsonValue.asTimestamp()
    }
  }

  def dataType2SqlType(dataType: DataType): Int = {
    dataType match {
      /** basic type */
      case _: ArrayType => Types.ARRAY
      case _: BinaryType => Types.BINARY
      case _: BooleanType => Types.BOOLEAN
      case _: DoubleType => Types.DOUBLE
      case _: IntegerType => Types.INTEGER
      case _: LongType => Types.BIGINT
      case _: StringType => Types.LONGVARCHAR // java.sql.type: LONGVERCHAR
      case _: NullType => Types.NULL

      /** mongo type: DateTime */
      case _: TimestampType => Types.TIMESTAMP

      /** mongo type: decimal128 */
      case _: DecimalType => Types.DECIMAL

      /** mongo type: timestamp, objectId, Document */
      case s: StructType =>
        // objectId
        if (s.fields.length == 1 && s.fields.forall(o => o.name == "oid" && dataType2SqlType(o.dataType) == Types.LONGVARCHAR))
          Types.ROWID // java.sql.type: ROWID
        // timestamp
        else if (s.fields.length == 2) {
          val head = s.fields.head
          val last = s.fields.last
          if (head.name == "time" && dataType2SqlType(head.dataType) == Types.INTEGER && last.name == "inc" && dataType2SqlType(last.dataType) == Types.INTEGER)
            Types.STRUCT
          else
            Types.STRUCT
        } else {
          // document
          Types.STRUCT
        }

      /** mongo type: RegularExpression, JavaScript, JavaScriptWithScope, Symbol, DBPointer */
      case _ => throw new Exception("unsupported data type")
    }
  }

  def index2SqlType(outputSchema: StructType): Map[Int, Int] = {
    outputSchema.fields.zipWithIndex.map {
      case (field, index) =>
        (index + 1, dataType2SqlType(field.dataType))
    }.toMap
  }

}
