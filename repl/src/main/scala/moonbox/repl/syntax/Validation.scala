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

/*
package moonbox.repl.syntax


import moonbox.core.parser.{ANTLRNoCaseStringStream, MqlBaseLexer, MqlBaseParser}
import org.antlr.v4.runtime.CommonTokenStream

import scala.util.Try

object Validation {
	def validate(sql: String): Try[Boolean] = {
		Try {
			val lexer: MqlBaseLexer = new MqlBaseLexer(new ANTLRNoCaseStringStream(sql))
			val tokenStream: CommonTokenStream = new CommonTokenStream(lexer)
			val mqlParser: MqlBaseParser = new MqlBaseParser(tokenStream)
			lexer.removeErrorListeners()
			lexer.addErrorListener(new SyntaxErrorListener)
			mqlParser.removeErrorListeners()
			mqlParser.addErrorListener(new SyntaxErrorListener)
			val ctx = mqlParser.single()
			if (ctx.exception != null) false
			else true
		}
	}
}
*/
