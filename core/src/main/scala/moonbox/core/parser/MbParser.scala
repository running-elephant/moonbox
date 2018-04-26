package moonbox.core.parser

import moonbox.common.MbLogging
import moonbox.core.command.MbCommand
import org.antlr.v4.runtime.CommonTokenStream
import org.apache.spark.sql.catalyst.parser.{ParseErrorListener, PostProcessor}

class MbParser extends MbLogging {
	private lazy val astBuilder = new MbAstBuilder

	def parse[T](mql: String)(toResult: MqlBaseParser => T): T = {
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
