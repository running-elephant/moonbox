package moonbox.catalyst.core.parser.udf.udfParser

import moonbox.catalyst.core.parser.udf.udfParser.ParserUtil._

class MbUDFVisitor extends UDFBaseVisitor[AnyRef] {


  var sql: String = _
  var expression: String = _

//  var arrowAndExpression: String = _
//  var variable: String = _
//  var mapExpression: String = _
//  var booleanExpression: String = _
//  var litInMap: String = _

  /*(a1,a2,a3) denotes: (a1: variable arrow expression, a2: variable, a3: expression)*/
  var arrowAndExpressionsInMap: Seq[(String, String, String)] = Nil
  var arrowAndExpressionsInFilter: Seq[(String, String, String)] = Nil
  var arrowAndExpressionsInExists: Seq[(String, String, String)] = Nil

  override def visitUdf(ctx: UDFParser.UdfContext) = {
    sql = ParserUtil.getTextPrettified(ctx.sql)
    super.visitUdf(ctx)
  }

  override def visitStatement(ctx: UDFParser.StatementContext) = super.visitStatement(ctx)

  override def visitPrefix(ctx: UDFParser.PrefixContext) = super.visitPrefix(ctx)

  override def visitWhereStatement(ctx: UDFParser.WhereStatementContext) = super.visitWhereStatement(ctx)

  override def visitUdfunction(ctx: UDFParser.UdfunctionContext) = super.visitUdfunction(ctx)

  override def visitArrayMap(ctx: UDFParser.ArrayMapContext) = {
    val aAndeCtx = ctx.aAnde
    val variableName = if (aAndeCtx.arrowPrefix != null) aAndeCtx.arrowPrefix.variable.getText else null
    val expressionCtx = aAndeCtx.expression()
    arrowAndExpressionsInMap :+= (getTextPrettified(aAndeCtx), variableName, getTextPrettified(expressionCtx))
    super.visitArrayMap(ctx)
  }

  override def visitArrayFilter(ctx: UDFParser.ArrayFilterContext) = {
    val aAndeCtx = ctx.aAnde
    val variableName = if (aAndeCtx.arrowPrefix != null) aAndeCtx.arrowPrefix.variable.getText else null
    val expressionCtx = aAndeCtx.expression()
    arrowAndExpressionsInFilter :+= (getTextPrettified(aAndeCtx), variableName, getTextPrettified(expressionCtx))
    super.visitArrayFilter(ctx)
  }

  override def visitArrayExists(ctx: UDFParser.ArrayExistsContext) = {
    val aAndeCtx = ctx.aAnde
    val variableName = if (aAndeCtx.arrowPrefix != null) aAndeCtx.arrowPrefix.variable.getText else null
    val expressionCtx = aAndeCtx.expression()
    arrowAndExpressionsInExists :+= (getTextPrettified(aAndeCtx), variableName, getTextPrettified(expressionCtx))
    super.visitArrayExists(ctx)
  }

  override def visitArrowAndExpression(ctx: UDFParser.ArrowAndExpressionContext) = {
//    arrowAndExpression = getTextPrettified(ctx)
//    if (ctx.arrowPrefix != null) variable = ctx.arrowPrefix.variable.getText
//    else variable = null
    expression = getTextPrettified(ctx.expression())
    super.visitArrowAndExpression(ctx)
  }

  override def visitVariableAndArrow(ctx: UDFParser.VariableAndArrowContext) = super.visitVariableAndArrow(ctx)

  override def visitLitExpr(ctx: UDFParser.LitExprContext) = {
//    litInMap = getTextPrettified(ctx)
    super.visitLitExpr(ctx)
  }

  override def visitBoolExpr(ctx: UDFParser.BoolExprContext) = {
//    booleanExpression = getTextPrettified(ctx)
    super.visitBoolExpr(ctx)
  }

  override def visitMapExpr(ctx: UDFParser.MapExprContext) = {
//    mapExpression = getTextPrettified(ctx)
    super.visitMapExpr(ctx)
  }

  override def visitMapExpression(ctx: UDFParser.MapExpressionContext) = super.visitMapExpression(ctx)

  override def visitTerm(ctx: UDFParser.TermContext) = super.visitTerm(ctx)

  override def visitBooleanExpression(ctx: UDFParser.BooleanExpressionContext) = super.visitBooleanExpression(ctx)

  override def visitTermf(ctx: UDFParser.TermfContext) = super.visitTermf(ctx)

  override def toString = s"visiting: ${sql}" // TODO:
}

