package moonbox.core.parser

import org.antlr.v4.runtime.{ANTLRInputStream, IntStream}

class ANTLRNoCaseStringStream(input: String) extends ANTLRInputStream(input) {
	override def LA(i: Int): Int = {
		val la = super.LA(i)
		if (la == 0 || la == IntStream.EOF) la
		else Character.toUpperCase(la)
	}
}
