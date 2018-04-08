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
