// Generated from moonbox/catalyst/core/parser/udf/udfParser/UDF.g4 by ANTLR 4.5.3
package moonbox.catalyst.core.parser.udf.udfParser;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link UDFParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface UDFVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link UDFParser#udf}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUdf(UDFParser.UdfContext ctx);
	/**
	 * Visit a parse tree produced by {@link UDFParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatement(UDFParser.StatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link UDFParser#prefix}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrefix(UDFParser.PrefixContext ctx);
	/**
	 * Visit a parse tree produced by {@link UDFParser#whereStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhereStatement(UDFParser.WhereStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link UDFParser#udfunction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUdfunction(UDFParser.UdfunctionContext ctx);
	/**
	 * Visit a parse tree produced by {@link UDFParser#arrayMap}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrayMap(UDFParser.ArrayMapContext ctx);
	/**
	 * Visit a parse tree produced by {@link UDFParser#arrayFilter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrayFilter(UDFParser.ArrayFilterContext ctx);
	/**
	 * Visit a parse tree produced by {@link UDFParser#arrayExists}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrayExists(UDFParser.ArrayExistsContext ctx);
	/**
	 * Visit a parse tree produced by {@link UDFParser#arrowAndExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrowAndExpression(UDFParser.ArrowAndExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link UDFParser#variableAndArrow}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariableAndArrow(UDFParser.VariableAndArrowContext ctx);
	/**
	 * Visit a parse tree produced by the {@code litExpr}
	 * labeled alternative in {@link UDFParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLitExpr(UDFParser.LitExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code boolExpr}
	 * labeled alternative in {@link UDFParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBoolExpr(UDFParser.BoolExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code mapExpr}
	 * labeled alternative in {@link UDFParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMapExpr(UDFParser.MapExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link UDFParser#literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLiteral(UDFParser.LiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link UDFParser#mapExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMapExpression(UDFParser.MapExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link UDFParser#term}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTerm(UDFParser.TermContext ctx);
	/**
	 * Visit a parse tree produced by {@link UDFParser#term1}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTerm1(UDFParser.Term1Context ctx);
	/**
	 * Visit a parse tree produced by {@link UDFParser#atom}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAtom(UDFParser.AtomContext ctx);
	/**
	 * Visit a parse tree produced by {@link UDFParser#numberAndString}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNumberAndString(UDFParser.NumberAndStringContext ctx);
	/**
	 * Visit a parse tree produced by {@link UDFParser#ideantifierOrLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdeantifierOrLiteral(UDFParser.IdeantifierOrLiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link UDFParser#booleanExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBooleanExpression(UDFParser.BooleanExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link UDFParser#termf}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTermf(UDFParser.TermfContext ctx);
	/**
	 * Visit a parse tree produced by {@link UDFParser#booleanValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBooleanValue(UDFParser.BooleanValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link UDFParser#identifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdentifier(UDFParser.IdentifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link UDFParser#quotedIdentifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQuotedIdentifier(UDFParser.QuotedIdentifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link UDFParser#number}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNumber(UDFParser.NumberContext ctx);
}