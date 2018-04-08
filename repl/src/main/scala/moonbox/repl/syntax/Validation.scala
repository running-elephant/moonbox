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
