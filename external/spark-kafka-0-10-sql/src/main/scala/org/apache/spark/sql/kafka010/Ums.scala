package org.apache.spark.sql.kafka010

import org.apache.spark.sql.kafka010.UmsFieldType.UmsFieldType
import org.apache.spark.sql.types._

case class Ums(protocol: UmsProtocol,
               schema: UmsSchema,
               payload: Seq[UmsTuple]) {
}

case class UmsProtocol(`type`: String,
                       version: Option[String] = Some("v1"),
                       msg_id: Option[Long] = Some(-1L),
                       msg_prev_id: Option[Long] = Some(-1L))


case class UmsSchema(namespace: String,
                     fields: Seq[UmsField])

object UmsSchema {

  def apply(namespace: String, schema: StructType): UmsSchema = {
    val umsFieldSeq = schema.map(structField => {
      val fieldType = structField.dataType match {
        case StringType => "string"
        case IntegerType => "int"
        case LongType => "long"
        case FloatType => "float"
        case DoubleType => "double"
        case BooleanType => "boolean"
        case DateType => "date"
        case TimestampType => "datetime"
        case DecimalType.SYSTEM_DEFAULT => "decimal"
        case BinaryType => "binary"
      }
      UmsField(structField.name, UmsFieldType.withName(fieldType), structField.nullable)
    })
    UmsSchema(namespace, umsFieldSeq)
  }
}


case class UmsField(name: String,
                    `type`: UmsFieldType,
                    nullable: Boolean)

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
  val DATA_FORMAT: String = "data.format"
  val UMS_DATA_FORMAT: String = "ums"
  val UMS_PROTOCOL: String = "ums.protocol"
  val UMS_NAMESPACE: String = "ums.namespace"
  val UMS_PAYLOAD_SIZE: String = "ums.payload.size"
  val UMS_SCHEMA: String = "ums.schema"
  val DEFAULT_UMS_PAYLOAD_SIZE: Int = 50

  def genKey(protocol: String, ns: String): String = protocol + "." + ns

  def validateNamespace(ns: String): Boolean = ns.split("\\.").length >= 7

  def validateProtocol(protocol: String): Boolean = !protocol.contains(".")

  def genSparkStructType(schema: String): StructType = {
    val fields = schema.split(",").map(field => {
      val fieldSplit = field.split("\\s+")
      val (fieldName, fieldType) = (fieldSplit.head, fieldSplit(1).trim.toLowerCase())
      val sparkDataType = fieldType match {
        case "string" => StringType
        case "int" => IntegerType
        case "long" => LongType
        case "float" => FloatType
        case "double" => DoubleType
        case "boolean" => BooleanType
        case "date" => DateType
        case "datetime" => TimestampType
        case "decimal" => DecimalType.SYSTEM_DEFAULT
        case "binary" => BinaryType
      }
      StructField(fieldName, sparkDataType, true)
    })
    StructType(fields)
  }
}