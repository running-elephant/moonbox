// Generated from moonbox/catalyst/core/parser/udf/udfParser/UDF.g4 by ANTLR 4.5.3
package moonbox.catalyst.core.parser.udf.udfParser;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link UDFParser}.
 */
public interface UDFListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link UDFParser#udf}.
	 * @param ctx the parse tree
	 */
	void enterUdf(UDFParser.UdfContext ctx);
	/**
	 * Exit a parse tree produced by {@link UDFParser#udf}.
	 * @param ctx the parse tree
	 */
	void exitUdf(UDFParser.UdfContext ctx);
	/**
	 * Enter a parse tree produced by {@link UDFParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatement(UDFParser.StatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link UDFParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatement(UDFParser.StatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link UDFParser#prefix}.
	 * @param ctx the parse tree
	 */
	void enterPrefix(UDFParser.PrefixContext ctx);
	/**
	 * Exit a parse tree produced by {@link UDFParser#prefix}.
	 * @param ctx the parse tree
	 */
	void exitPrefix(UDFParser.PrefixContext ctx);
	/**
	 * Enter a parse tree produced by {@link UDFParser#whereStatement}.
	 * @param ctx the parse tree
	 */
	void enterWhereStatement(UDFParser.WhereStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link UDFParser#whereStatement}.
	 * @param ctx the parse tree
	 */
	void exitWhereStatement(UDFParser.WhereStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link UDFParser#udfunction}.
	 * @param ctx the parse tree
	 */
	void enterUdfunction(UDFParser.UdfunctionContext ctx);
	/**
	 * Exit a parse tree produced by {@link UDFParser#udfunction}.
	 * @param ctx the parse tree
	 */
	void exitUdfunction(UDFParser.UdfunctionContext ctx);
	/**
	 * Enter a parse tree produced by {@link UDFParser#arrayMap}.
	 * @param ctx the parse tree
	 */
	void enterArrayMap(UDFParser.ArrayMapContext ctx);
	/**
	 * Exit a parse tree produced by {@link UDFParser#arrayMap}.
	 * @param ctx the parse tree
	 */
	void exitArrayMap(UDFParser.ArrayMapContext ctx);
	/**
	 * Enter a parse tree produced by {@link UDFParser#arrayFilter}.
	 * @param ctx the parse tree
	 */
	void enterArrayFilter(UDFParser.ArrayFilterContext ctx);
	/**
	 * Exit a parse tree produced by {@link UDFParser#arrayFilter}.
	 * @param ctx the parse tree
	 */
	void exitArrayFilter(UDFParser.ArrayFilterContext ctx);
	/**
	 * Enter a parse tree produced by {@link UDFParser#arrayExists}.
	 * @param ctx the parse tree
	 */
	void enterArrayExists(UDFParser.ArrayExistsContext ctx);
	/**
	 * Exit a parse tree produced by {@link UDFParser#arrayExists}.
	 * @param ctx the parse tree
	 */
	void exitArrayExists(UDFParser.ArrayExistsContext ctx);
	/**
	 * Enter a parse tree produced by {@link UDFParser#arrowAndExpression}.
	 * @param ctx the parse tree
	 */
	void enterArrowAndExpression(UDFParser.ArrowAndExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link UDFParser#arrowAndExpression}.
	 * @param ctx the parse tree
	 */
	void exitArrowAndExpression(UDFParser.ArrowAndExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link UDFParser#variableAndArrow}.
	 * @param ctx the parse tree
	 */
	void enterVariableAndArrow(UDFParser.VariableAndArrowContext ctx);
	/**
	 * Exit a parse tree produced by {@link UDFParser#variableAndArrow}.
	 * @param ctx the parse tree
	 */
	void exitVariableAndArrow(UDFParser.VariableAndArrowContext ctx);
	/**
	 * Enter a parse tree produced by the {@code litExpr}
	 * labeled alternative in {@link UDFParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterLitExpr(UDFParser.LitExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code litExpr}
	 * labeled alternative in {@link UDFParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitLitExpr(UDFParser.LitExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code boolExpr}
	 * labeled alternative in {@link UDFParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterBoolExpr(UDFParser.BoolExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code boolExpr}
	 * labeled alternative in {@link UDFParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitBoolExpr(UDFParser.BoolExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code mapExpr}
	 * labeled alternative in {@link UDFParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterMapExpr(UDFParser.MapExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code mapExpr}
	 * labeled alternative in {@link UDFParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitMapExpr(UDFParser.MapExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link UDFParser#literal}.
	 * @param ctx the parse tree
	 */
	void enterLiteral(UDFParser.LiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link UDFParser#literal}.
	 * @param ctx the parse tree
	 */
	void exitLiteral(UDFParser.LiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link UDFParser#mapExpression}.
	 * @param ctx the parse tree
	 */
	void enterMapExpression(UDFParser.MapExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link UDFParser#mapExpression}.
	 * @param ctx the parse tree
	 */
	void exitMapExpression(UDFParser.MapExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link UDFParser#term}.
	 * @param ctx the parse tree
	 */
	void enterTerm(UDFParser.TermContext ctx);
	/**
	 * Exit a parse tree produced by {@link UDFParser#term}.
	 * @param ctx the parse tree
	 */
	void exitTerm(UDFParser.TermContext ctx);
	/**
	 * Enter a parse tree produced by {@link UDFParser#term1}.
	 * @param ctx the parse tree
	 */
	void enterTerm1(UDFParser.Term1Context ctx);
	/**
	 * Exit a parse tree produced by {@link UDFParser#term1}.
	 * @param ctx the parse tree
	 */
	void exitTerm1(UDFParser.Term1Context ctx);
	/**
	 * Enter a parse tree produced by {@link UDFParser#atom}.
	 * @param ctx the parse tree
	 */
	void enterAtom(UDFParser.AtomContext ctx);
	/**
	 * Exit a parse tree produced by {@link UDFParser#atom}.
	 * @param ctx the parse tree
	 */
	void exitAtom(UDFParser.AtomContext ctx);
	/**
	 * Enter a parse tree produced by {@link UDFParser#numberAndString}.
	 * @param ctx the parse tree
	 */
	void enterNumberAndString(UDFParser.NumberAndStringContext ctx);
	/**
	 * Exit a parse tree produced by {@link UDFParser#numberAndString}.
	 * @param ctx the parse tree
	 */
	void exitNumberAndString(UDFParser.NumberAndStringContext ctx);
	/**
	 * Enter a parse tree produced by {@link UDFParser#ideantifierOrLiteral}.
	 * @param ctx the parse tree
	 */
	void enterIdeantifierOrLiteral(UDFParser.IdeantifierOrLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link UDFParser#ideantifierOrLiteral}.
	 * @param ctx the parse tree
	 */
	void exitIdeantifierOrLiteral(UDFParser.IdeantifierOrLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link UDFParser#booleanExpression}.
	 * @param ctx the parse tree
	 */
	void enterBooleanExpression(UDFParser.BooleanExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link UDFParser#booleanExpression}.
	 * @param ctx the parse tree
	 */
	void exitBooleanExpression(UDFParser.BooleanExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link UDFParser#termf}.
	 * @param ctx the parse tree
	 */
	void enterTermf(UDFParser.TermfContext ctx);
	/**
	 * Exit a parse tree produced by {@link UDFParser#termf}.
	 * @param ctx the parse tree
	 */
	void exitTermf(UDFParser.TermfContext ctx);
	/**
	 * Enter a parse tree produced by {@link UDFParser#booleanValue}.
	 * @param ctx the parse tree
	 */
	void enterBooleanValue(UDFParser.BooleanValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link UDFParser#booleanValue}.
	 * @param ctx the parse tree
	 */
	void exitBooleanValue(UDFParser.BooleanValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link UDFParser#identifier}.
	 * @param ctx the parse tree
	 */
	void enterIdentifier(UDFParser.IdentifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link UDFParser#identifier}.
	 * @param ctx the parse tree
	 */
	void exitIdentifier(UDFParser.IdentifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link UDFParser#quotedIdentifier}.
	 * @param ctx the parse tree
	 */
	void enterQuotedIdentifier(UDFParser.QuotedIdentifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link UDFParser#quotedIdentifier}.
	 * @param ctx the parse tree
	 */
	void exitQuotedIdentifier(UDFParser.QuotedIdentifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link UDFParser#number}.
	 * @param ctx the parse tree
	 */
	void enterNumber(UDFParser.NumberContext ctx);
	/**
	 * Exit a parse tree produced by {@link UDFParser#number}.
	 * @param ctx the parse tree
	 */
	void exitNumber(UDFParser.NumberContext ctx);
}