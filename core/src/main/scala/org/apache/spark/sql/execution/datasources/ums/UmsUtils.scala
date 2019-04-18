/*-
 * <<
 * Moonbox
 * ==
 * Copyright (C) 2016 - 2019 EDP
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

package org.apache.spark.sql.execution.datasources.ums

import com.fasterxml.jackson.databind.{JsonNode, ObjectMapper}
import org.apache.hadoop.io.Text
import org.apache.spark.sql.types._

import scala.collection.JavaConversions._
import scala.collection.mutable.ArrayBuffer

object UmsUtils {
    private val base64decoder = new sun.misc.BASE64Decoder
    private def base64s2byte(s: String, default: Array[Byte] = null): Array[Byte]  = {
        base64decoder.decodeBuffer(s.trim)
    }

    def getPayloadSchema(sampleJson: String): StructType = {
        val mapper = new ObjectMapper()
        val node = mapper.readTree(sampleJson)
        val jsonNode: JsonNode = node.get("schema").get("fields")

        val fields = jsonNode.toSeq.map { elem =>
            val name = elem.get("name").asText
            val utype = elem.get("type").asText
            val nullable = elem.get("nullable").asBoolean
            val sparkType = utype.toLowerCase match {
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
            StructField(name, sparkType, nullable)
        }
        val schema = StructType(Array(fields: _*))
        schema
    }

    private def toTypedValue(value: JsonNode, dataType: DataType):Any = {
        if (value == null) { null }
        else if(value.isNull) { null }
        else {
            val realValue: String = value.toString.stripPrefix("\"").stripSuffix("\"")
            dataType match {
                case StringType => "\"" + realValue + "\""
                case IntegerType => realValue.toInt
                case LongType => realValue.toLong
                case FloatType => realValue.toFloat
                case DoubleType => realValue.toDouble
                case BooleanType => realValue.toBoolean
                case DateType => DateUtils.dt2sqlDate(realValue)  //TODO:
                case BinaryType => base64s2byte(realValue)
                case DecimalType() => new java.math.BigDecimal(realValue)
                case TimestampType => {
                    //Notice: See org.apache.spark.sql.catalyst.json.JacksonParser:172, timestamp * 1000 may be wrong, but we satisfy it
                    val time: Long = DateUtils.dt2timestamp(DateUtils.dt2date(realValue)).getTime
                    if (time != 0) { time / 1000L}
                    else { 0L }
                }
                case _ => throw new UnsupportedOperationException(s"Unknown DataType: $dataType")
            }
        }
    }


    def getPayloadData(text: Text, requiredSchema: StructType, dataSchema: StructType): String = {
        val mapper = new ObjectMapper()
        val node = mapper.readTree(text.toString)
        val payload = node.get("payload")
        val iter = payload.iterator
        val array = new ArrayBuffer[String]

        val schemaWithIndex: Seq[(StructField, Int)] = dataSchema.zipWithIndex.filter{case (elem, _) => requiredSchema.getFieldIndex(elem.name).isDefined}

        while (iter.hasNext) {
            val next = iter.next
            val tupleSeq: Seq[JsonNode] = next.get("tuple").toSeq
            val payloadSeq =schemaWithIndex.map {
                case (schema, index) =>
                    val jsonName = schema.name
                    val jsonType = schema.dataType
                    val jsonValue = if (index < tupleSeq.length) {
                        toTypedValue(tupleSeq(index), jsonType)
                    }else{
                        throw new Exception(s"index $index is larger than tupleSeq length ${tupleSeq.length}, $jsonName, $jsonType, $text")
                    }
                    s""""$jsonName": $jsonValue"""
            }
            array.append(payloadSeq.mkString("{", ",", "}"))
        }
        array.mkString("[", ",", "]")
    }

}
