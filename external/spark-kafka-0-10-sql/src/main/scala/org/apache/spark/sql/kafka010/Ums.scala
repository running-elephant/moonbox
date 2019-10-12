package org.apache.spark.sql.kafka010

import org.apache.spark.sql.catalyst.InternalRow
import org.apache.spark.sql.catalyst.util.DateTimeUtils
import org.apache.spark.sql.types._

import scala.collection.mutable.ArrayBuffer

case class Ums(protocol: UmsProtocol,
               schema: UmsSchema,
               payload: Seq[UmsTuple])


case class UmsProtocol(`type`: String,
                       version: Option[String] = Some("v1")
                      )


case class UmsSchema(namespace: String,
                     fields: Seq[UmsField])

object UmsSchema {

  def apply(namespace: String, schema: StructType): UmsSchema = {
    val umsFieldSeq = schema.map(structField => {
      val fieldType = structField.dataType match {
        case StringType => UmsFieldType.STRING
        case IntegerType => UmsFieldType.INT
        case LongType => UmsFieldType.LONG
        case FloatType => UmsFieldType.FLOAT
        case DoubleType => UmsFieldType.DOUBLE
        case BooleanType => UmsFieldType.BOOLEAN
        case DateType => UmsFieldType.DATE
        case TimestampType => UmsFieldType.DATETIME
        case DecimalType.Fixed(p, s) => UmsFieldType.DECIMAL
        case BinaryType => UmsFieldType.BINARY
      }
      UmsField(structField.name, fieldType.toString, structField.nullable)
    })
    UmsSchema(namespace, umsFieldSeq)
  }
}


case class UmsField(name: String,
                    `type`: String,
                    nullable: Boolean,
                    encoded: Boolean = false)

case class UmsTuple(tuple: Seq[String])


object UmsFieldType extends Enumeration {
  type UmsFieldType = Value

  val STRING = Value("string")
  val INT = Value("int")
  val LONG = Value("long")
  val FLOAT = Value("float")
  val DOUBLE = Value("double")
  val BOOLEAN = Value("boolean")
  val DATE = Value("date")
  val DATETIME = Value("datetime")
  val DECIMAL = Value("decimal")
  val BINARY = Value("binary")

  def umsFieldType(s: String) = UmsFieldType.withName(s.toLowerCase)
}

object UmsCommon {
  val UMS_FORMAT: String = "ums"
  val UMS_PROTOCOL: String = "ums.protocol"
  val UMS_NAMESPACE: String = "ums.namespace"
  val UMS_PAYLOAD_SIZE: String = "ums.payload.size"
  val DEFAULT_UMS_PAYLOAD_SIZE: Int = 50

  def genKey(protocol: String, ns: String): String = protocol + "." + ns

  def validateNamespace(ns: String): Boolean = ns.split("\\.").length >= 7

  def validateProtocol(protocol: String): Boolean = !protocol.contains(".")

  def genUmsTuple(sparkSchema: StructType, sparkRow: InternalRow): UmsTuple = {
    val seq = new ArrayBuffer[String]
    for (i <- sparkSchema.indices) {
      val umsValue = sparkSchema(i).dataType match {
        case StringType => sparkRow.getUTF8String(i)
        case IntegerType => sparkRow.getInt(i)
        case LongType => sparkRow.getLong(i)
        case FloatType => sparkRow.getFloat(i)
        case DoubleType => sparkRow.getDouble(i)
        case BooleanType => sparkRow.getBoolean(i)
        case DateType =>
          if(sparkRow.isNullAt(i)) null
          else DateTimeUtils.toJavaDate(sparkRow.getInt(i))
        case TimestampType =>
          if(sparkRow.isNullAt(i)) null
          else DateTimeUtils.toJavaTimestamp(sparkRow.getLong(i))
        case d@DecimalType.Fixed(p, s) =>
          if (sparkRow.isNullAt(i)) "0.0"
          else sparkRow.getDecimal(i, d.precision, d.scale).toJavaBigDecimal
        case BinaryType => sparkRow.getBinary(i)
      }
      if (umsValue == null) seq.append("null")
      else seq.append(umsValue.toString)
    }
    UmsTuple(seq)
  }



  //  def genSparkStructType(schema: String): StructType = {
  //    val fields = schema.split(",").map(field => {
  //      val fieldSplit = field.trim.split("\\s+")
  //      val (fieldName, fieldType) = (fieldSplit.head, fieldSplit(1).trim.toLowerCase())
  //      val sparkDataType = fieldType match {
  //        case "string" => StringType
  //        case "int" => IntegerType
  //        case "long" => LongType
  //        case "float" => FloatType
  //        case "double" => DoubleType
  //        case "boolean" => BooleanType
  //        case "date" => DateType
  //        case "datetime" => TimestampType
  //        case "decimal" => DecimalType.SYSTEM_DEFAULT
  //        case "binary" => BinaryType
  //      }
  //      StructField(fieldName, sparkDataType, true)
  //    })
  //    StructType(fields)
  //  }
}