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

package moonbox.repl

import java.io.File

import com.typesafe.config.ConfigFactory
import scala.collection.JavaConverters._

object Utils {

	private def valueString(cell: Any): String = {
		cell match {
			case null => "null"
			case binary: Array[Byte] => binary.map("%02X".format(_)).mkString("[", " ", "]")
			case array: Array[_] => array.map(valueString).mkString("[", ", ", "]")
			case seq: Seq[_] => seq.map(valueString).mkString("[", ", ", "]")
			case map: java.util.Map[_, _] => map.asScala.map { case (k, v) =>
				s"${valueString(k)}:${valueString(v)}" }.mkString("{", ", ", "}")
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
		* @param _numRows default is 1000, denotes the max number of rows to show
		* @param truncate 0 denotes not truncating
		* @return
		*/
	def stringToShow(_data: Seq[Seq[Any]],
		schema: Seq[String],
		_numRows: Int = 1000,
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
				val str = valueString(cell)
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
		if (showPromote) {
			val message = if (data.size < numRows) {
				s"${data.size} row(s) in set\n"
			} else {
				s"showing at most $numRows row(s)\n"
			}
			sb.append(message)
		}
		sb.toString()
	}

	def showDataResult(schema: Option[Seq[String]], data: Option[Seq[Seq[Any]]], error: Option[String]): Unit = {
		if (schema.isDefined && data.isDefined) {
			println(stringToShow(data.get, schema.get))
		}
		if (error.isDefined) {
			println(s"ERROR: ${error.get}")
		}
	}

	def secondToMs(timeout: Int): Int = {
		timeout * 1000
	}

	def getJdbcDefaultPort(): Int = {
		val moonboxHome = sys.env.get("MOONBOX_HOME")
		moonboxHome.map { home =>
			val configFilePath = s"$home${File.separator}conf${File.separator}moonbox-defaults.conf"
			val file = new File(configFilePath)
			if (!file.exists()) {
				10010
			} else {
				val config = ConfigFactory.parseFile(file)
				val restPort = if (config.hasPath("moonbox.deploy.tcp.port")) {
					config.getInt("moonbox.deploy.tcp.port")
				} else 10010
				restPort
			}
		}.getOrElse(10010)
	}

}
