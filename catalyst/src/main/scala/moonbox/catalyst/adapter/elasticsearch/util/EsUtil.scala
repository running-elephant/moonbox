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

package moonbox.catalyst.adapter.elasticsearch.util

import java.util.{Calendar, Date, Properties}
import javax.xml.bind.DatatypeConverter

import moonbox.catalyst.core.CatalystContext
import org.elasticsearch.hadoop.serialization.json.JacksonJsonGenerator
import org.elasticsearch.hadoop.util.FastByteArrayOutputStream
import org.elasticsearch.spark.serialization.ScalaValueWriter

import scala.collection.mutable.LinkedHashSet


object EsUtil {

    val DB_NAME = "database"
    val INDEX_NAME = "index"
    val TYPE_NAME = "table"

    def buildRequest(context: CatalystContext, element: Seq[String], info: Properties): String = {

        val fetchSize = if(info.containsKey("fetchsize")){  //use user define fetch size
            info.getProperty("fetchsize").toInt
        }else{
            10000  //size must be less than or equal to: [10000]
        }

        val jsonElement = if( -1 == context.limitSize ) {  //if no limit, use default fetch size here
            Seq(s""" "from":0, "size":$fetchSize """) ++ element
        } else { // 0 or other, use actual limit size
            element
        }

        "{ " + jsonElement.mkString(",").replace("@limit", s"$fetchSize") + " }"  //replace agg size
    }


    lazy val valueWriter = { new ScalaValueWriter }
    
    // required since type has a special meaning in Scala and thus the method cannot be called

    def isClass(obj: Any, className: String) = {
        className.equals(obj.getClass().getName())
    }

    def extract(value: Any):String = {
        extract(value, true, false)
    }

    def extractAsJsonArray(value: Any):String = {
        extract(value, true, true)
    }

    def extractMatchArray(attribute: String, ar: Array[Any], isEs5: Boolean):String = {
        // use a set to avoid duplicate values
        // especially since Spark conversion might turn each user param into null
        val numbers = LinkedHashSet.empty[AnyRef]
        val strings = LinkedHashSet.empty[AnyRef]

        // move numbers into a separate list for a terms query combined with a bool
        for (i <- ar) i.asInstanceOf[AnyRef] match {
            case null     => // ignore
            case n:Number => numbers += extract(i, false, false)
            case _        => strings += extract(i, false, false)
        }

        if (numbers.isEmpty) {
            if (strings.isEmpty) {
                ""
            } else {
                if (isEs5) {
                    s"""{"match":{"$attribute":${strings.mkString("\"", " ", "\"")}}}"""
                }
                else {
                    s"""{"query":{"match":{"$attribute":${strings.mkString("\"", " ", "\"")}}}}"""
                }
            }
        } else {
            // translate the numbers into a terms query
            val str = s"""{"terms":{"$attribute":${numbers.mkString("[", ",", "]")}}}"""
            if (strings.isEmpty){
                str
                // if needed, add the strings as a match query
            } else str + {
                if (isEs5) {
                    s""",{"match":{"$attribute":${strings.mkString("\"", " ", "\"")}}}"""
                }
                else {
                    s""",{"query":{"match":{"$attribute":${strings.mkString("\"", " ", "\"")}}}}"""
                }
            }
        }
    }

    private def extract(value: Any, inJsonFormat: Boolean, asJsonArray: Boolean):String = {
        // common-case implies primitives and String so try these before using the full-blown ValueWriter
        value match {
            case null           => "null"
            case u: Unit        => "null"
            case b: Boolean     => b.toString
            case by: Byte       => by.toString
            case s: Short       => s.toString
            case i: Int         => i.toString
            case l: Long        => l.toString
            case f: Float       => f.toString
            case d: Double      => d.toString
            case bd: BigDecimal  => bd.toString
            case _: Char        |
                 _: String      |
                 _: Array[Byte]  => if (inJsonFormat) org.elasticsearch.hadoop.util.StringUtils.toJsonString(value.toString) else value.toString()
            // handle Timestamp also
            case dt: Date        => {
                val cal = Calendar.getInstance()
                cal.setTime(dt)
                val str = DatatypeConverter.printDateTime(cal)
                if (inJsonFormat) org.elasticsearch.hadoop.util.StringUtils.toJsonString(str) else str
            }
            case ar: Array[Any] =>  // new in Spark 1.4
                if (asJsonArray) (for (i <- ar) yield extract(i, true, false)).distinct.mkString("[", ",", "]")
                else (for (i <- ar) yield extract(i, false, false)).distinct.mkString("\"", " ", "\"")
            
            case utf if (isClass(utf, "org.apache.spark.sql.types.UTF8String")
                    || isClass(utf, "org.apache.spark.unsafe.types.UTF8String"))  // new in Spark 1.5
                => if (inJsonFormat) org.elasticsearch.hadoop.util.StringUtils.toJsonString(utf.toString()) else utf.toString()
            case a: AnyRef      => {
                val storage = new FastByteArrayOutputStream()
                val generator = new JacksonJsonGenerator(storage)
                valueWriter.write(a, generator)
                generator.flush()
                generator.close()
                storage.toString()
            }
        }
    }

}
