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
import org.apache.spark.sql.types._
import scala.collection.JavaConversions._

object UmsUtils {

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
}
