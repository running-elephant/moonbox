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

package moonbox.repl

import java.io.File

import com.typesafe.config.ConfigFactory
import org.json.JSONObject

import scala.annotation.tailrec
import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer


object Utils {
  // name, type, nullable
  def parseJson(json: String): Array[(String, String, Boolean)] = {
    import scala.collection.JavaConversions._
    val schemaObject = new JSONObject(json)
    schemaObject.getJSONArray("fields").map {
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

  def splitSql(sql: String, splitter: Char): Seq[String] = {
    val stack = new mutable.Stack[Char]()
    val splitIndex = new ArrayBuffer[Int]()
    for ((char, idx) <- sql.toCharArray.zipWithIndex) {
      if (char == splitter) {
        if (stack.isEmpty) splitIndex += idx
      }
      if (char == '(') stack.push('(')
      if (char == ')') stack.pop()
    }
    splits(sql, splitIndex.toArray, 0, Nil).map(_.stripPrefix(splitter.toString).trim).filter(_.length > 0)
  }

  @tailrec
  private def splits(sql: String, idxs: Array[Int], offset: Int, res: Seq[String]): Seq[String] = {
    if (idxs.nonEmpty) {
      val head = idxs.head
      val (h, t) = sql.splitAt(head - offset)
      splits(t, idxs.tail, head, h :: res.toList)
    } else res
  }

  def parseJson2(json: String): Unit = {
    val jsonObject = new JSONObject(json)
    jsonObject.getJSONArray("fields")
  }

  private def cell2String(cell: Any): String = {
    cell match {
      case null => "null"
      case binary: Array[Byte] => binary.map("%02X".format(_)).mkString("[", " ", "]")
      case array: Array[_] => array.map(cell2String).mkString("[", ", ", "]")
      case seq: Seq[_] => seq.map(cell2String).mkString("[", ", ", "]")
      /*case d: java.sql.Date =>
        new ThreadLocal[DateFormat]() {
          override def initialValue() = {
            new SimpleDateFormat("yyyy-MM-dd", Locale.US)
          }
        }.get().format(d)
      case ts: Timestamp =>
        val formatted = new ThreadLocal[DateFormat]() {
          override def initialValue() = {
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
          }
        }.get().format(ts, Locale.US)
        if (ts.toString.length > 19 && ts.toString.substring(19) != ".0") {
          formatted + ts.toString.substring(19)
        } else formatted*/
      case _ => cell.toString
    }
  }

  private def padWs(cell: String, len: Int, truncate: Int): String = {
    var j = 0
    var paddedCell: String = cell
    while (j < len - cell.getBytes("GB2312").length) {
      if (truncate > 0) {
        paddedCell = " " + paddedCell
      } else {
        paddedCell = paddedCell + " "
      }
      j += 1
    }
    paddedCell
  }

  /**
    *
    * @param _data    rows of table
    * @param schema   a sequence of column names
    * @param _numRows default is 500, denotes the max number of rows to show
    * @param truncate 0 denotes not truncating
    * @return
    */
  def showString(_data: Seq[Seq[Any]],
                 schema: Seq[String],
                 _numRows: Int = 500,
                 truncate: Int = 0,
                 showPromote: Boolean = true,
                 showSchema: Boolean = true): String = {
    val numRows = _numRows.max(0)
    val data = _data.take(numRows)
    // For array values, replace Seq and Array with square brackets
    // For cells that are beyond `truncate` characters, replace it with the
    // first `truncate-3` and "..."
    var rows: Seq[Seq[String]] = data.map { row =>
      row.map { cell =>
        val str = cell2String(cell)
        if (truncate > 0 && str.length > truncate) {
          // do not show ellipses for strings shorter than 4 characters.
          if (truncate < 4) str.substring(0, truncate)
          else str.substring(0, truncate - 3) + "..."
        } else str
      }: Seq[String]
    }
    if (schema.nonEmpty && showSchema) {
      rows = schema +: rows
    }
    if (rows.isEmpty) {
      return ""
    }
    val sb = new StringBuilder
    val numCols = {
      if (data.nonEmpty) {
        math.max(schema.length, data.head.length)
      } else {
        schema.length
      }
    }
    // Initialise the width of each column to a minimum value of '3'
    val colWidths = Array.fill(numCols)(3)
    // Compute the width of each column
    for (row <- rows) {
      for ((cell, i) <- row.zipWithIndex) {
        colWidths(i) = math.max(colWidths(i), cell.getBytes("GB2312").length)
      }
    }
    // Create SeparateLine
    val sep: String = colWidths.map("-" * _).addString(sb, "+", "+", "+\n").toString()
    // column names
    rows.head.zipWithIndex.map { case (cell, i) =>
      padWs(cell, colWidths(i), truncate)
    }.addString(sb, "|", "|", "|\n")
    // escape empty schema
    if (schema.nonEmpty && showSchema) {
      sb.append(sep)
    }
    // data
    rows.tail.foreach {
      _.zipWithIndex.map { case (cell, i) =>
        padWs(cell, colWidths(i), truncate)
      }.addString(sb, "|", "|", "|\n")
    }
    if (data.nonEmpty) {
      sb.append(sep)
    }
    // For Data that has more than "numRows" records
    val rowsString = if (numRows == 1) "row" else "rows"
    if (showPromote){
      sb.append(s"Showing at most top $numRows $rowsString\n")
    }
    sb.toString()
  }

  def getHttpHostAndPort: (String, Int) = {
    val defaultFile = new File(moonbox.common.util.Utils.getDefaultPropertiesFile().get)
    val defaultConfig = ConfigFactory.parseFile(defaultFile)

    val restPortPath = "moonbox.rest.server.port"
    val port = if ( defaultConfig.hasPathOrNull(restPortPath) ) {
      if ( defaultConfig.getIsNull(restPortPath) ) { 8080 }
      else {
        defaultConfig.getInt(restPortPath)
      }
    } else {
      8080
    }

    val restHostPath = "moonbox.host"
    val host = if ( defaultConfig.hasPathOrNull(restHostPath) ) {
      if ( defaultConfig.getIsNull(restHostPath) ) { "localhost" }
      else {
        defaultConfig.getString(restHostPath)
      }
    } else {
      "localhost"
    }

    (host, port)

  }

  def showDataResult(schema: Option[Seq[String]], data: Option[Seq[Seq[Any]]], error: Option[String]): Unit = {
    if(schema.isDefined && data.isDefined){
      println(showString(data.get, schema.get))
    }
    if(error.isDefined) {
      println(s"ERROR: ${error.get}")
    }
  }

  def showDataResult2(data: Any): Unit = {
    println("data:" + data)
  }

}
