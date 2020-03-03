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

package moonbox.core.parser

import scala.collection.mutable.StringBuilder

object ParserUtils {

	def string(s: String): String = {
		unescapeSQLString(s)
	}

	def tripQuotes(string: String): String = {
		string.replaceAll("^\'|^\"|^`|\"$|\'$|`$", "")
	}

	def unescapeSQLString(b: String): String = {
		var enclosure: Character = null
		val sb = new StringBuilder(b.length())

		def appendEscapedChar(n: Char) {
			n match {
				case '0' => sb.append('\u0000')
				case '\'' => sb.append('\'')
				case '"' => sb.append('\"')
				case 'b' => sb.append('\b')
				case 'n' => sb.append('\n')
				case 'r' => sb.append('\r')
				case 't' => sb.append('\t')
				case 'Z' => sb.append('\u001A')
				case '\\' => sb.append('\\')
				// The following 2 lines are exactly what MySQL does TODO: why do we do this?
				case '%' => sb.append("\\%")
				case '_' => sb.append("\\_")
				case _ => sb.append(n)
			}
		}

		var i = 0
		val strLength = b.length
		while (i < strLength) {
			val currentChar = b.charAt(i)
			if (enclosure == null) {
				if (currentChar == '\'' || currentChar == '\"') {
					enclosure = currentChar
				}
			} else if (enclosure == currentChar) {
				enclosure = null
			} else if (currentChar == '\\') {

				if ((i + 6 < strLength) && b.charAt(i + 1) == 'u') {
					// \u0000 style character literals.

					val base = i + 2
					val code = (0 until 4).foldLeft(0) { (mid, j) =>
						val digit = Character.digit(b.charAt(j + base), 16)
						(mid << 4) + digit
					}
					sb.append(code.asInstanceOf[Char])
					i += 5
				} else if (i + 4 < strLength) {
					// \000 style character literals.

					val i1 = b.charAt(i + 1)
					val i2 = b.charAt(i + 2)
					val i3 = b.charAt(i + 3)

					if ((i1 >= '0' && i1 <= '1') && (i2 >= '0' && i2 <= '7') && (i3 >= '0' && i3 <= '7')) {
						val tmp = ((i3 - '0') + ((i2 - '0') << 3) + ((i1 - '0') << 6)).asInstanceOf[Char]
						sb.append(tmp)
						i += 3
					} else {
						appendEscapedChar(i1)
						i += 1
					}
				} else if (i + 2 < strLength) {
					// escaped character literals.
					val n = b.charAt(i + 1)
					appendEscapedChar(n)
					i += 1
				}
			} else {
				// non-escaped character literals.
				sb.append(currentChar)
			}
			i += 1
		}
		sb.toString()
	}
}
