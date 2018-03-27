package moonbox.core.parser

import moonbox.common.MbLogging
import moonbox.core.command.MbCommand
import org.antlr.v4.runtime.CommonTokenStream

class MbParser extends MbLogging {
	private lazy val astBuilder = new MbAstBuilder

	def parse[T](mql: String)(toResult: MqlBaseParser => T): T = {
		val lexer: MqlBaseLexer = new MqlBaseLexer(new ANTLRNoCaseStringStream(mql))
		val tokenStream: CommonTokenStream = new CommonTokenStream(lexer)
		val parser: MqlBaseParser = new MqlBaseParser(tokenStream)
		val result = toResult(parser)
		logInfo(s"Parsing MbSql '$mql' to command $result")
		result
	}

	def parsePlan(mql: String): MbCommand = parse(mql) { parser =>
		astBuilder.visitSingle(parser.single())
	}
}
