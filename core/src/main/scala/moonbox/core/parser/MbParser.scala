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

package moonbox.core.parser

import moonbox.common.MbLogging
import moonbox.core.command.MbCommand
import org.antlr.v4.runtime.CommonTokenStream
import org.apache.spark.sql.catalyst.parser.{ParseErrorListener, PostProcessor}

class MbParser extends MbLogging {
	private lazy val astBuilder = new MbAstBuilder

	private def parse[T](mql: String)(toResult: MqlBaseParser => T): T = {
		val lexer: MqlBaseLexer = new MqlBaseLexer(new ANTLRNoCaseStringStream(mql))
		lexer.removeErrorListeners()
		lexer.addErrorListener(ParseErrorListener)

		val tokenStream: CommonTokenStream = new CommonTokenStream(lexer)
		val parser: MqlBaseParser = new MqlBaseParser(tokenStream)

		parser.addParseListener(PostProcessor)
		parser.removeErrorListeners()
		parser.addErrorListener(ParseErrorListener)
		val result = toResult(parser)
		logInfo(s"Parsing MbSql '$mql' to command $result")
		result
	}

	def parsePlan(mql: String): MbCommand = parse(mql) { parser =>
		astBuilder.visitSingle(parser.single())
	}
}
