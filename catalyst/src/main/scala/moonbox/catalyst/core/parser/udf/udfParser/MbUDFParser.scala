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

package moonbox.catalyst.core.parser.udf.udfParser

import org.antlr.v4.runtime
import org.antlr.v4.runtime.{ANTLRInputStream, CommonTokenStream}

class MbUDFParser(input: ANTLRInputStream) {
  private var visited = false
  private val lexer: UDFLexer = new UDFLexer(input)
  private val parser: UDFParser = new UDFParser(new CommonTokenStream(lexer))
  private val visitor: MbUDFVisitor = new MbUDFVisitor

  //  init

  def isVisited = visited

  def getParser = parser

  def getVisitor = visitor

  def parseSqlInit: Unit = {
    if (!visited) {
      synchronized {
        if (!visited) {
          visitor.visit(parser.udf())
          visited = true
        }
      }
    }
  }

  def this(expression: String) = this(new runtime.ANTLRInputStream(expression))
}
