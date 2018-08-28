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

package moonbox.catalyst.adapter.mongo.util

import java.sql.{Timestamp, Types}

import org.apache.spark.sql.types._
import org.bson.BsonValue

import scala.collection.JavaConverters._

object MongoJDBCUtils {

  val URL_PREFIX: String = "jdbc:mongo://"
  val HOST_KEY: String = "host"
  val PORT_KEY: String = "port"
  val AUTH_SOURCE: String = "authsource"
  val DB_NAME: String = "database"
  val COLLECTION_KEY = "table"
  val USER_KEY = "user"
  val PASSWORD_KEY = "password"
  val HOSTS_AND_PORTS = "nodes"
  val SPLITTER_KEY: String = ","

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

  def numericValueConverter(value: Any, dataType: DataType): Any = {
    import java.math.BigDecimal
    value match {
      case v: BigDecimal => v
      case v: Double => v
      case v: Float => v
      case v: Byte => v
      case v: Int =>
        dataType match {
          case LongType => v.toLong
          case _ => v
        }
      case v: Long =>
        dataType match {
          case IntegerType => v.toInt
          case _ => v
        }
      case v: Short => v
      case _ => value
    }

  }

  def bsonValue2Value(bsonValue: BsonValue, fieldName: Seq[String], dataType: DataType): Any = {
    if (bsonValue.isArray) {
      if (fieldName.isEmpty) {
        bsonValue.asArray().getValues.asScala.map(v => bsonValue2Value(v, fieldName, dataType)).toArray
      } else {
        bsonValue.asArray().getValues.asScala.filter(doc =>
          doc.isDocument && doc.asDocument().containsKey(fieldName.head)
        ).map(doc =>
          bsonValue2Value(doc.asDocument().get(fieldName.head), fieldName.tail, dataType)
        ).toArray
      }
    } else if (bsonValue.isBinary) {
      bsonValue.asBinary().getData
    } else if (bsonValue.isBoolean) {
      bsonValue.asBoolean().getValue
    } else if (bsonValue.isDateTime) {
      new Timestamp(bsonValue.asDateTime().getValue)
    } else if (bsonValue.isDBPointer) {
      /** For DBPointer mongo type, return the namespace */
      /** deprecated */
      bsonValue.asDBPointer().getNamespace
    } else if (bsonValue.isDecimal128) {
      /** new in version 3.4 */
      bsonValue.asDecimal128.getValue.bigDecimalValue()
    } else if (bsonValue.isDocument) {
      if (fieldName.isEmpty)
        bsonValue.asDocument().toString
      else
        bsonValue2Value(bsonValue.asDocument().get(fieldName.head), fieldName.tail, dataType)
    } else if (bsonValue.isDouble) {
      val value = bsonValue.asDouble.getValue
      numericValueConverter(value, dataType)
    } else if (bsonValue.isInt32) {
      val value = bsonValue.asInt32.getValue
      numericValueConverter(value, dataType)
    } else if (bsonValue.isInt64) {
      val value = bsonValue.asInt64().getValue
      numericValueConverter(value, dataType)
    } else if (bsonValue.isJavaScript) {
      /**javaScript*/
      bsonValue.asJavaScript().getCode
    } else if (bsonValue.isJavaScriptWithScope) {
      /**javaScriptWithScope*/
      bsonValue.asJavaScriptWithScope.getCode
    } else if (bsonValue.isNull) {
      null
    } else if (bsonValue.isNumber) {
      /** Actually this condition is inaccessible */
      bsonValue.asNumber()
    } else if (bsonValue.isObjectId) {
      bsonValue.asObjectId().getValue.toString
    } else if (bsonValue.isRegularExpression) {
      bsonValue.asRegularExpression().toString
    } else if (bsonValue.isString) {
      bsonValue.asString().getValue
    } else if (bsonValue.isSymbol) {
      /** deprecated */
      bsonValue.asSymbol().getSymbol
    } else if (bsonValue.isTimestamp) {
      new Timestamp(bsonValue.asTimestamp().getValue)
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
