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

package moonbox.catalyst.core.parser.udf.udfParser

import java.util.regex.Pattern

import org.antlr.v4.runtime.tree.ParseTree

object ParserUtil {
  val VARIABLE_NAME = "x"

  def sqlTransform(sql: String): String = {
    val udfParser = new MbUDFParser(sql)
    val visitor = udfParser.getVisitor
    visitor.visit(udfParser.getParser.udf())
    val map = visitor.arrowAndExpressionsInMap
    val filter = visitor.arrowAndExpressionsInFilter
    val exists = visitor.arrowAndExpressionsInExists
    var newSql: String = visitor.sql
    if (map.nonEmpty)
      map.foreach(m => if (m._2 != null && m._2.length > 0) newSql = newSql.replaceAll(Pattern.quote(m._1), withQuote(m._3.replaceAll(Pattern.quote(m._2), VARIABLE_NAME))))
    if (filter.nonEmpty)
      filter.foreach(f => if (f._2 != null && f._2.length > 0) newSql = newSql.replaceAll(Pattern.quote(f._1), withQuote(f._3.replaceAll(Pattern.quote(f._2), VARIABLE_NAME))))
    if (exists.nonEmpty)
      exists.foreach(e => if (e._2 != null && e._2.length > 0) newSql = newSql.replaceAll(Pattern.quote(e._1), withQuote(e._3.replaceAll(Pattern.quote(e._2), VARIABLE_NAME))))
    newSql
  }

  def getTextPrettified(r: ParseTree): String = {
    val sb = new StringBuilder
    if (r.getChildCount == 0)
      sb.append(" " + r.getText)
    else {
      val count = r.getChildCount
      for (i <- 0 until count) {
        sb.append(" " + getTextPrettified(r.getChild(i)))
      }
    }
    prettify(sb.toString()).trim
  }

  def prettify(string: String): String = {
    string.replaceAll(" \\. ", ".")
      .replaceAll("\\( ", "(")
      .replaceAll(" \\)", ")")
      .replaceAll(" ,", ",")
  }

  def withQuote(string: String): String = {
    s""""${string.replaceAll("\"", "\'")}""""
  }
}
