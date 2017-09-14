/*-
 * <<
 * Moonbox
 * ==
 * Copyright (C) 2016 - 2017 EDP
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

package edp.moonbox.core.parser

import edp.moonbox.common.EdpLogging
import edp.moonbox.core.MbCommand
import org.antlr.v4.runtime.{ANTLRInputStream, CommonTokenStream, IntStream}


class MbParser extends EdpLogging {

	val cmdBuilder = new MbCmdBuilder

	def parse(sql: String): MbCommand = {
		val mbSqlLexer = new CMDLexer(new ANTLRNoCaseStringStream(sql))
		val tokenStream = new CommonTokenStream(mbSqlLexer)
		val mbSqlParser = new CMDParser(tokenStream)
		val cmd = cmdBuilder.visitSingleCmd(mbSqlParser.singleCmd())
		logInfo(s"parse $sql to cmd $cmd")
		cmd
	}

	class ANTLRNoCaseStringStream(input: String) extends ANTLRInputStream(input) {
		override def LA(i: Int): Int = {
			val la = super.LA(i)
			if (la == 0 || la == IntStream.EOF) la
			else Character.toUpperCase(la)
		}
	}
}
