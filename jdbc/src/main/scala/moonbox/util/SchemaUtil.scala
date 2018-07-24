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

package moonbox.util

import java.sql.Types._

import org.json.JSONObject

import collection.JavaConverters._

object SchemaUtil {

  // name, type, nullable
  def parse(json: String): Array[(String, String, Boolean)] = {
    val schemaObject = new JSONObject(json.toLowerCase)
    schemaObject.getJSONArray("fields").asScala.map {
      case elem: JSONObject =>
        val columnName = elem.getString("name")
        val nullable = elem.getBoolean("nullable")
        val columnType = elem.get("type") match {
          case v: JSONObject => v.getString("type")
          case s => s.toString
        }
        (columnName, columnType, nullable)
      case _ => null
    }.filter(_ != null).toArray
  }

  def schema2SqlType(schema: Array[(String, String, Boolean)]) = {
    schema.map { t =>
      val sqlType =
        if (t._2.startsWith("decimal")) DECIMAL // decimal(10,2)
        else t._2 match {
          case "array" => ARRAY
          case "binary" => BINARY
          case "boolean" => BIT
          case "date" => DATE
          case "char" => VARCHAR
          case "varchar" => VARCHAR
          case "double" => DOUBLE
          case "float" => FLOAT
          case "byte" => TINYINT
          case "integer" => INTEGER
          case "long" => BIGINT
          case "short" => SMALLINT
          case "string" => VARCHAR
          case "timestamp" => TIMESTAMP
          case "null" => NULL
          case "object" => JAVA_OBJECT
          case "struct" => STRUCT
          case "calendarinterval" => JAVA_OBJECT // CalendarIntervalType in spark
          case "map" => JAVA_OBJECT // MapType in spark
          case _ => JAVA_OBJECT // user defined type in spark
        }
      (t._1, sqlType, t._3)
    }
  }
}
