// Generated from moonbox/catalyst/core/parser/udf/udfParser/UDF.g4 by ANTLR 4.5.3
package moonbox.catalyst.core.parser.udf.udfParser;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class UDFParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.5.3", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, T__10=11, AND=12, OR=13, MINUS=14, TRUE=15, FALSE=16, NULL=17, 
		ARROW=18, LPAREN=19, RPAREN=20, SELECT=21, FROM=22, WHERE=23, BinaryArithmeticOperator=24, 
		BinaryComparator=25, BACKQUOTED_IDENTIFIER=26, STRING=27, BIGINT_LITERAL=28, 
		SMALLINT_LITERAL=29, TINYINT_LITERAL=30, BYTELENGTH_LITERAL=31, DOUBLE_LITERAL=32, 
		BIGDECIMAL_LITERAL=33, INTEGER_VALUE=34, DECIMAL_VALUE=35, IDENTIFIER=36, 
		SIMPLE_COMMENT=37, BRACKETED_EMPTY_COMMENT=38, BRACKETED_COMMENT=39, WS=40, 
		UNRECOGNIZED=41, DELIMITER=42;
	public static final int
		RULE_udf = 0, RULE_statement = 1, RULE_prefix = 2, RULE_whereStatement = 3, 
		RULE_udfunction = 4, RULE_arrayMap = 5, RULE_arrayFilter = 6, RULE_arrayExists = 7, 
		RULE_arrowAndExpression = 8, RULE_variableAndArrow = 9, RULE_expression = 10, 
		RULE_literal = 11, RULE_mapExpression = 12, RULE_term = 13, RULE_term1 = 14, 
		RULE_atom = 15, RULE_numberAndString = 16, RULE_ideantifierOrLiteral = 17, 
		RULE_booleanExpression = 18, RULE_termf = 19, RULE_booleanValue = 20, 
		RULE_identifier = 21, RULE_quotedIdentifier = 22, RULE_number = 23;
	public static final String[] ruleNames = {
		"udf", "statement", "prefix", "whereStatement", "udfunction", "arrayMap", 
		"arrayFilter", "arrayExists", "arrowAndExpression", "variableAndArrow", 
		"expression", "literal", "mapExpression", "term", "term1", "atom", "numberAndString", 
		"ideantifierOrLiteral", "booleanExpression", "termf", "booleanValue", 
		"identifier", "quotedIdentifier", "number"
	};

	private static final String[] _LITERAL_NAMES = {
		null, "','", "'array_map('", "'ARRAY_MAP('", "'array_filter('", "'ARRAY_FILTER('", 
		"'array_exists('", "'ARRAY_EXISTS('", "'+'", "'%'", "'*'", "'/'", null, 
		null, "'-'", null, null, "'NULL'", "'=>'", "'('", "')'", null, null, null, 
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, null, "'/**/'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, null, null, null, null, null, null, null, null, null, null, null, 
		"AND", "OR", "MINUS", "TRUE", "FALSE", "NULL", "ARROW", "LPAREN", "RPAREN", 
		"SELECT", "FROM", "WHERE", "BinaryArithmeticOperator", "BinaryComparator", 
		"BACKQUOTED_IDENTIFIER", "STRING", "BIGINT_LITERAL", "SMALLINT_LITERAL", 
		"TINYINT_LITERAL", "BYTELENGTH_LITERAL", "DOUBLE_LITERAL", "BIGDECIMAL_LITERAL", 
		"INTEGER_VALUE", "DECIMAL_VALUE", "IDENTIFIER", "SIMPLE_COMMENT", "BRACKETED_EMPTY_COMMENT", 
		"BRACKETED_COMMENT", "WS", "UNRECOGNIZED", "DELIMITER"
	};
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "UDF.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }


	  /**
	   * Verify whether current token is a valid decimal token (which contains dot).
	   * Returns true if the character that follows the token is not a digit or letter or underscore.
	   *
	   * For example:
	   * For char stream "2.3", "2." is not a valid decimal token, because it is followed by digit '3'.
	   * For char stream "2.3_", "2.3" is not a valid decimal token, because it is followed by '_'.
	   * For char stream "2.3W", "2.3" is not a valid decimal token, because it is followed by 'W'.
	   * For char stream "12.0D 34.E2+0.12 "  12.0D is a valid decimal token because it is folllowed
	   * by a space. 34.E2 is a valid decimal token because it is followed by symbol '+'
	   * which is not a digit or letter or underscore.
	   */
	  public boolean isValidDecimal() {
	    int nextChar = _input.LA(1);
	    if (nextChar >= 'A' && nextChar <= 'Z' || nextChar >= '0' && nextChar <= '9' ||
	      nextChar == '_') {
	      return false;
	    } else {
	      return true;
	    }
	  }

	public UDFParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class UdfContext extends ParserRuleContext {
		public StatementContext sql;
		public TerminalNode EOF() { return getToken(UDFParser.EOF, 0); }
		public StatementContext statement() {
			return getRuleContext(StatementContext.class,0);
		}
		public UdfContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_udf; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof UDFListener ) ((UDFListener)listener).enterUdf(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof UDFListener ) ((UDFListener)listener).exitUdf(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof UDFVisitor ) return ((UDFVisitor<? extends T>)visitor).visitUdf(this);
			else return visitor.visitChildren(this);
		}
	}

	public final UdfContext udf() throws RecognitionException {
		UdfContext _localctx = new UdfContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_udf);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(48);
			((UdfContext)_localctx).sql = statement();
			setState(49);
			match(EOF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class StatementContext extends ParserRuleContext {
		public IdentifierContext table;
		public PrefixContext prefix() {
			return getRuleContext(PrefixContext.class,0);
		}
		public TerminalNode FROM() { return getToken(UDFParser.FROM, 0); }
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public WhereStatementContext whereStatement() {
			return getRuleContext(WhereStatementContext.class,0);
		}
		public StatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_statement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof UDFListener ) ((UDFListener)listener).enterStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof UDFListener ) ((UDFListener)listener).exitStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof UDFVisitor ) return ((UDFVisitor<? extends T>)visitor).visitStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StatementContext statement() throws RecognitionException {
		StatementContext _localctx = new StatementContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_statement);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(51);
			prefix();
			setState(52);
			match(FROM);
			setState(53);
			((StatementContext)_localctx).table = identifier();
			setState(55);
			_la = _input.LA(1);
			if (_la==WHERE) {
				{
				setState(54);
				whereStatement();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class PrefixContext extends ParserRuleContext {
		public TerminalNode SELECT() { return getToken(UDFParser.SELECT, 0); }
		public List<IdentifierContext> identifier() {
			return getRuleContexts(IdentifierContext.class);
		}
		public IdentifierContext identifier(int i) {
			return getRuleContext(IdentifierContext.class,i);
		}
		public List<UdfunctionContext> udfunction() {
			return getRuleContexts(UdfunctionContext.class);
		}
		public UdfunctionContext udfunction(int i) {
			return getRuleContext(UdfunctionContext.class,i);
		}
		public PrefixContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_prefix; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof UDFListener ) ((UDFListener)listener).enterPrefix(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof UDFListener ) ((UDFListener)listener).exitPrefix(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof UDFVisitor ) return ((UDFVisitor<? extends T>)visitor).visitPrefix(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PrefixContext prefix() throws RecognitionException {
		PrefixContext _localctx = new PrefixContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_prefix);
		int _la;
		try {
			int _alt;
			setState(79);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,5,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(57);
				match(SELECT);
				setState(60);
				switch (_input.LA(1)) {
				case BACKQUOTED_IDENTIFIER:
				case IDENTIFIER:
					{
					setState(58);
					identifier();
					}
					break;
				case T__1:
				case T__2:
				case T__3:
				case T__4:
				case T__5:
				case T__6:
					{
					setState(59);
					udfunction();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(69);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__0) {
					{
					{
					setState(62);
					match(T__0);
					setState(65);
					switch (_input.LA(1)) {
					case BACKQUOTED_IDENTIFIER:
					case IDENTIFIER:
						{
						setState(63);
						identifier();
						}
						break;
					case T__1:
					case T__2:
					case T__3:
					case T__4:
					case T__5:
					case T__6:
						{
						setState(64);
						udfunction();
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					}
					}
					setState(71);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(72);
				match(SELECT);
				setState(76);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,4,_ctx);
				while ( _alt!=1 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1+1 ) {
						{
						{
						setState(73);
						matchWildcard();
						}
						} 
					}
					setState(78);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,4,_ctx);
				}
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class WhereStatementContext extends ParserRuleContext {
		public TerminalNode WHERE() { return getToken(UDFParser.WHERE, 0); }
		public BooleanExpressionContext booleanExpression() {
			return getRuleContext(BooleanExpressionContext.class,0);
		}
		public WhereStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_whereStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof UDFListener ) ((UDFListener)listener).enterWhereStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof UDFListener ) ((UDFListener)listener).exitWhereStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof UDFVisitor ) return ((UDFVisitor<? extends T>)visitor).visitWhereStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final WhereStatementContext whereStatement() throws RecognitionException {
		WhereStatementContext _localctx = new WhereStatementContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_whereStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(81);
			match(WHERE);
			setState(82);
			booleanExpression();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class UdfunctionContext extends ParserRuleContext {
		public ArrayMapContext arrayMap() {
			return getRuleContext(ArrayMapContext.class,0);
		}
		public ArrayFilterContext arrayFilter() {
			return getRuleContext(ArrayFilterContext.class,0);
		}
		public ArrayExistsContext arrayExists() {
			return getRuleContext(ArrayExistsContext.class,0);
		}
		public UdfunctionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_udfunction; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof UDFListener ) ((UDFListener)listener).enterUdfunction(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof UDFListener ) ((UDFListener)listener).exitUdfunction(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof UDFVisitor ) return ((UDFVisitor<? extends T>)visitor).visitUdfunction(this);
			else return visitor.visitChildren(this);
		}
	}

	public final UdfunctionContext udfunction() throws RecognitionException {
		UdfunctionContext _localctx = new UdfunctionContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_udfunction);
		try {
			setState(87);
			switch (_input.LA(1)) {
			case T__1:
			case T__2:
				enterOuterAlt(_localctx, 1);
				{
				setState(84);
				arrayMap();
				}
				break;
			case T__3:
			case T__4:
				enterOuterAlt(_localctx, 2);
				{
				setState(85);
				arrayFilter();
				}
				break;
			case T__5:
			case T__6:
				enterOuterAlt(_localctx, 3);
				{
				setState(86);
				arrayExists();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ArrayMapContext extends ParserRuleContext {
		public ArrowAndExpressionContext aAnde;
		public ArrowAndExpressionContext arrowAndExpression() {
			return getRuleContext(ArrowAndExpressionContext.class,0);
		}
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public UdfunctionContext udfunction() {
			return getRuleContext(UdfunctionContext.class,0);
		}
		public ArrayMapContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_arrayMap; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof UDFListener ) ((UDFListener)listener).enterArrayMap(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof UDFListener ) ((UDFListener)listener).exitArrayMap(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof UDFVisitor ) return ((UDFVisitor<? extends T>)visitor).visitArrayMap(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArrayMapContext arrayMap() throws RecognitionException {
		ArrayMapContext _localctx = new ArrayMapContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_arrayMap);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(89);
			_la = _input.LA(1);
			if ( !(_la==T__1 || _la==T__2) ) {
			_errHandler.recoverInline(this);
			} else {
				consume();
			}
			{
			setState(92);
			switch (_input.LA(1)) {
			case BACKQUOTED_IDENTIFIER:
			case IDENTIFIER:
				{
				setState(90);
				identifier();
				}
				break;
			case T__1:
			case T__2:
			case T__3:
			case T__4:
			case T__5:
			case T__6:
				{
				setState(91);
				udfunction();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(94);
			match(T__0);
			setState(95);
			((ArrayMapContext)_localctx).aAnde = arrowAndExpression();
			}
			setState(97);
			match(RPAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ArrayFilterContext extends ParserRuleContext {
		public ArrowAndExpressionContext aAnde;
		public ArrowAndExpressionContext arrowAndExpression() {
			return getRuleContext(ArrowAndExpressionContext.class,0);
		}
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public UdfunctionContext udfunction() {
			return getRuleContext(UdfunctionContext.class,0);
		}
		public ArrayFilterContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_arrayFilter; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof UDFListener ) ((UDFListener)listener).enterArrayFilter(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof UDFListener ) ((UDFListener)listener).exitArrayFilter(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof UDFVisitor ) return ((UDFVisitor<? extends T>)visitor).visitArrayFilter(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArrayFilterContext arrayFilter() throws RecognitionException {
		ArrayFilterContext _localctx = new ArrayFilterContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_arrayFilter);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(99);
			_la = _input.LA(1);
			if ( !(_la==T__3 || _la==T__4) ) {
			_errHandler.recoverInline(this);
			} else {
				consume();
			}
			{
			setState(102);
			switch (_input.LA(1)) {
			case BACKQUOTED_IDENTIFIER:
			case IDENTIFIER:
				{
				setState(100);
				identifier();
				}
				break;
			case T__1:
			case T__2:
			case T__3:
			case T__4:
			case T__5:
			case T__6:
				{
				setState(101);
				udfunction();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(104);
			match(T__0);
			setState(105);
			((ArrayFilterContext)_localctx).aAnde = arrowAndExpression();
			}
			setState(107);
			match(RPAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ArrayExistsContext extends ParserRuleContext {
		public ArrowAndExpressionContext aAnde;
		public ArrowAndExpressionContext arrowAndExpression() {
			return getRuleContext(ArrowAndExpressionContext.class,0);
		}
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public UdfunctionContext udfunction() {
			return getRuleContext(UdfunctionContext.class,0);
		}
		public ArrayExistsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_arrayExists; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof UDFListener ) ((UDFListener)listener).enterArrayExists(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof UDFListener ) ((UDFListener)listener).exitArrayExists(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof UDFVisitor ) return ((UDFVisitor<? extends T>)visitor).visitArrayExists(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArrayExistsContext arrayExists() throws RecognitionException {
		ArrayExistsContext _localctx = new ArrayExistsContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_arrayExists);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(109);
			_la = _input.LA(1);
			if ( !(_la==T__5 || _la==T__6) ) {
			_errHandler.recoverInline(this);
			} else {
				consume();
			}
			{
			setState(112);
			switch (_input.LA(1)) {
			case BACKQUOTED_IDENTIFIER:
			case IDENTIFIER:
				{
				setState(110);
				identifier();
				}
				break;
			case T__1:
			case T__2:
			case T__3:
			case T__4:
			case T__5:
			case T__6:
				{
				setState(111);
				udfunction();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(114);
			match(T__0);
			setState(115);
			((ArrayExistsContext)_localctx).aAnde = arrowAndExpression();
			}
			setState(117);
			match(RPAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ArrowAndExpressionContext extends ParserRuleContext {
		public VariableAndArrowContext arrowPrefix;
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public VariableAndArrowContext variableAndArrow() {
			return getRuleContext(VariableAndArrowContext.class,0);
		}
		public ArrowAndExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_arrowAndExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof UDFListener ) ((UDFListener)listener).enterArrowAndExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof UDFListener ) ((UDFListener)listener).exitArrowAndExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof UDFVisitor ) return ((UDFVisitor<? extends T>)visitor).visitArrowAndExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArrowAndExpressionContext arrowAndExpression() throws RecognitionException {
		ArrowAndExpressionContext _localctx = new ArrowAndExpressionContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_arrowAndExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(120);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,10,_ctx) ) {
			case 1:
				{
				setState(119);
				((ArrowAndExpressionContext)_localctx).arrowPrefix = variableAndArrow();
				}
				break;
			}
			setState(122);
			expression();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class VariableAndArrowContext extends ParserRuleContext {
		public Token variable;
		public TerminalNode ARROW() { return getToken(UDFParser.ARROW, 0); }
		public TerminalNode IDENTIFIER() { return getToken(UDFParser.IDENTIFIER, 0); }
		public VariableAndArrowContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_variableAndArrow; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof UDFListener ) ((UDFListener)listener).enterVariableAndArrow(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof UDFListener ) ((UDFListener)listener).exitVariableAndArrow(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof UDFVisitor ) return ((UDFVisitor<? extends T>)visitor).visitVariableAndArrow(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VariableAndArrowContext variableAndArrow() throws RecognitionException {
		VariableAndArrowContext _localctx = new VariableAndArrowContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_variableAndArrow);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(124);
			((VariableAndArrowContext)_localctx).variable = match(IDENTIFIER);
			setState(125);
			match(ARROW);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ExpressionContext extends ParserRuleContext {
		public ExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expression; }
	 
		public ExpressionContext() { }
		public void copyFrom(ExpressionContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class MapExprContext extends ExpressionContext {
		public MapExpressionContext mapExpression() {
			return getRuleContext(MapExpressionContext.class,0);
		}
		public MapExprContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof UDFListener ) ((UDFListener)listener).enterMapExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof UDFListener ) ((UDFListener)listener).exitMapExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof UDFVisitor ) return ((UDFVisitor<? extends T>)visitor).visitMapExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class BoolExprContext extends ExpressionContext {
		public BooleanExpressionContext booleanExpression() {
			return getRuleContext(BooleanExpressionContext.class,0);
		}
		public BoolExprContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof UDFListener ) ((UDFListener)listener).enterBoolExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof UDFListener ) ((UDFListener)listener).exitBoolExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof UDFVisitor ) return ((UDFVisitor<? extends T>)visitor).visitBoolExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class LitExprContext extends ExpressionContext {
		public LiteralContext literal() {
			return getRuleContext(LiteralContext.class,0);
		}
		public LitExprContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof UDFListener ) ((UDFListener)listener).enterLitExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof UDFListener ) ((UDFListener)listener).exitLitExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof UDFVisitor ) return ((UDFVisitor<? extends T>)visitor).visitLitExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExpressionContext expression() throws RecognitionException {
		ExpressionContext _localctx = new ExpressionContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_expression);
		try {
			setState(130);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,11,_ctx) ) {
			case 1:
				_localctx = new LitExprContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(127);
				literal();
				}
				break;
			case 2:
				_localctx = new BoolExprContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(128);
				booleanExpression();
				}
				break;
			case 3:
				_localctx = new MapExprContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(129);
				mapExpression();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class LiteralContext extends ParserRuleContext {
		public TerminalNode NULL() { return getToken(UDFParser.NULL, 0); }
		public TerminalNode DELIMITER() { return getToken(UDFParser.DELIMITER, 0); }
		public NumberContext number() {
			return getRuleContext(NumberContext.class,0);
		}
		public BooleanValueContext booleanValue() {
			return getRuleContext(BooleanValueContext.class,0);
		}
		public TerminalNode STRING() { return getToken(UDFParser.STRING, 0); }
		public LiteralContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_literal; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof UDFListener ) ((UDFListener)listener).enterLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof UDFListener ) ((UDFListener)listener).exitLiteral(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof UDFVisitor ) return ((UDFVisitor<? extends T>)visitor).visitLiteral(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LiteralContext literal() throws RecognitionException {
		LiteralContext _localctx = new LiteralContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_literal);
		try {
			setState(137);
			switch (_input.LA(1)) {
			case NULL:
				enterOuterAlt(_localctx, 1);
				{
				setState(132);
				match(NULL);
				}
				break;
			case DELIMITER:
				enterOuterAlt(_localctx, 2);
				{
				setState(133);
				match(DELIMITER);
				}
				break;
			case MINUS:
			case BIGINT_LITERAL:
			case SMALLINT_LITERAL:
			case TINYINT_LITERAL:
			case DOUBLE_LITERAL:
			case BIGDECIMAL_LITERAL:
			case INTEGER_VALUE:
			case DECIMAL_VALUE:
				enterOuterAlt(_localctx, 3);
				{
				setState(134);
				number();
				}
				break;
			case TRUE:
			case FALSE:
				enterOuterAlt(_localctx, 4);
				{
				setState(135);
				booleanValue();
				}
				break;
			case STRING:
				enterOuterAlt(_localctx, 5);
				{
				setState(136);
				match(STRING);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class MapExpressionContext extends ParserRuleContext {
		public List<TermContext> term() {
			return getRuleContexts(TermContext.class);
		}
		public TermContext term(int i) {
			return getRuleContext(TermContext.class,i);
		}
		public MapExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_mapExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof UDFListener ) ((UDFListener)listener).enterMapExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof UDFListener ) ((UDFListener)listener).exitMapExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof UDFVisitor ) return ((UDFVisitor<? extends T>)visitor).visitMapExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MapExpressionContext mapExpression() throws RecognitionException {
		MapExpressionContext _localctx = new MapExpressionContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_mapExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(139);
			term();
			setState(144);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__7 || _la==MINUS) {
				{
				{
				setState(140);
				_la = _input.LA(1);
				if ( !(_la==T__7 || _la==MINUS) ) {
				_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(141);
				term();
				}
				}
				setState(146);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TermContext extends ParserRuleContext {
		public List<Term1Context> term1() {
			return getRuleContexts(Term1Context.class);
		}
		public Term1Context term1(int i) {
			return getRuleContext(Term1Context.class,i);
		}
		public TermContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_term; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof UDFListener ) ((UDFListener)listener).enterTerm(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof UDFListener ) ((UDFListener)listener).exitTerm(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof UDFVisitor ) return ((UDFVisitor<? extends T>)visitor).visitTerm(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TermContext term() throws RecognitionException {
		TermContext _localctx = new TermContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_term);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(147);
			term1();
			setState(152);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__8) {
				{
				{
				{
				setState(148);
				match(T__8);
				setState(149);
				term1();
				}
				}
				}
				setState(154);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Term1Context extends ParserRuleContext {
		public List<AtomContext> atom() {
			return getRuleContexts(AtomContext.class);
		}
		public AtomContext atom(int i) {
			return getRuleContext(AtomContext.class,i);
		}
		public Term1Context(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_term1; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof UDFListener ) ((UDFListener)listener).enterTerm1(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof UDFListener ) ((UDFListener)listener).exitTerm1(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof UDFVisitor ) return ((UDFVisitor<? extends T>)visitor).visitTerm1(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Term1Context term1() throws RecognitionException {
		Term1Context _localctx = new Term1Context(_ctx, getState());
		enterRule(_localctx, 28, RULE_term1);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(155);
			atom();
			setState(160);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__9 || _la==T__10) {
				{
				{
				setState(156);
				_la = _input.LA(1);
				if ( !(_la==T__9 || _la==T__10) ) {
				_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(157);
				atom();
				}
				}
				setState(162);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AtomContext extends ParserRuleContext {
		public TerminalNode IDENTIFIER() { return getToken(UDFParser.IDENTIFIER, 0); }
		public NumberAndStringContext numberAndString() {
			return getRuleContext(NumberAndStringContext.class,0);
		}
		public TerminalNode LPAREN() { return getToken(UDFParser.LPAREN, 0); }
		public MapExpressionContext mapExpression() {
			return getRuleContext(MapExpressionContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(UDFParser.RPAREN, 0); }
		public AtomContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_atom; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof UDFListener ) ((UDFListener)listener).enterAtom(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof UDFListener ) ((UDFListener)listener).exitAtom(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof UDFVisitor ) return ((UDFVisitor<? extends T>)visitor).visitAtom(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AtomContext atom() throws RecognitionException {
		AtomContext _localctx = new AtomContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_atom);
		try {
			setState(169);
			switch (_input.LA(1)) {
			case IDENTIFIER:
				enterOuterAlt(_localctx, 1);
				{
				setState(163);
				match(IDENTIFIER);
				}
				break;
			case MINUS:
			case NULL:
			case STRING:
			case BIGINT_LITERAL:
			case SMALLINT_LITERAL:
			case TINYINT_LITERAL:
			case DOUBLE_LITERAL:
			case BIGDECIMAL_LITERAL:
			case INTEGER_VALUE:
			case DECIMAL_VALUE:
				enterOuterAlt(_localctx, 2);
				{
				setState(164);
				numberAndString();
				}
				break;
			case LPAREN:
				enterOuterAlt(_localctx, 3);
				{
				setState(165);
				match(LPAREN);
				setState(166);
				mapExpression();
				setState(167);
				match(RPAREN);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class NumberAndStringContext extends ParserRuleContext {
		public NumberContext number() {
			return getRuleContext(NumberContext.class,0);
		}
		public TerminalNode STRING() { return getToken(UDFParser.STRING, 0); }
		public TerminalNode NULL() { return getToken(UDFParser.NULL, 0); }
		public NumberAndStringContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_numberAndString; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof UDFListener ) ((UDFListener)listener).enterNumberAndString(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof UDFListener ) ((UDFListener)listener).exitNumberAndString(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof UDFVisitor ) return ((UDFVisitor<? extends T>)visitor).visitNumberAndString(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NumberAndStringContext numberAndString() throws RecognitionException {
		NumberAndStringContext _localctx = new NumberAndStringContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_numberAndString);
		try {
			setState(174);
			switch (_input.LA(1)) {
			case MINUS:
			case BIGINT_LITERAL:
			case SMALLINT_LITERAL:
			case TINYINT_LITERAL:
			case DOUBLE_LITERAL:
			case BIGDECIMAL_LITERAL:
			case INTEGER_VALUE:
			case DECIMAL_VALUE:
				enterOuterAlt(_localctx, 1);
				{
				setState(171);
				number();
				}
				break;
			case STRING:
				enterOuterAlt(_localctx, 2);
				{
				setState(172);
				match(STRING);
				}
				break;
			case NULL:
				enterOuterAlt(_localctx, 3);
				{
				setState(173);
				match(NULL);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class IdeantifierOrLiteralContext extends ParserRuleContext {
		public TerminalNode IDENTIFIER() { return getToken(UDFParser.IDENTIFIER, 0); }
		public LiteralContext literal() {
			return getRuleContext(LiteralContext.class,0);
		}
		public IdeantifierOrLiteralContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ideantifierOrLiteral; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof UDFListener ) ((UDFListener)listener).enterIdeantifierOrLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof UDFListener ) ((UDFListener)listener).exitIdeantifierOrLiteral(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof UDFVisitor ) return ((UDFVisitor<? extends T>)visitor).visitIdeantifierOrLiteral(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IdeantifierOrLiteralContext ideantifierOrLiteral() throws RecognitionException {
		IdeantifierOrLiteralContext _localctx = new IdeantifierOrLiteralContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_ideantifierOrLiteral);
		try {
			setState(178);
			switch (_input.LA(1)) {
			case IDENTIFIER:
				enterOuterAlt(_localctx, 1);
				{
				setState(176);
				match(IDENTIFIER);
				}
				break;
			case MINUS:
			case TRUE:
			case FALSE:
			case NULL:
			case STRING:
			case BIGINT_LITERAL:
			case SMALLINT_LITERAL:
			case TINYINT_LITERAL:
			case DOUBLE_LITERAL:
			case BIGDECIMAL_LITERAL:
			case INTEGER_VALUE:
			case DECIMAL_VALUE:
			case DELIMITER:
				enterOuterAlt(_localctx, 2);
				{
				setState(177);
				literal();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class BooleanExpressionContext extends ParserRuleContext {
		public List<TermfContext> termf() {
			return getRuleContexts(TermfContext.class);
		}
		public TermfContext termf(int i) {
			return getRuleContext(TermfContext.class,i);
		}
		public List<TerminalNode> AND() { return getTokens(UDFParser.AND); }
		public TerminalNode AND(int i) {
			return getToken(UDFParser.AND, i);
		}
		public List<TerminalNode> OR() { return getTokens(UDFParser.OR); }
		public TerminalNode OR(int i) {
			return getToken(UDFParser.OR, i);
		}
		public BooleanExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_booleanExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof UDFListener ) ((UDFListener)listener).enterBooleanExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof UDFListener ) ((UDFListener)listener).exitBooleanExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof UDFVisitor ) return ((UDFVisitor<? extends T>)visitor).visitBooleanExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BooleanExpressionContext booleanExpression() throws RecognitionException {
		BooleanExpressionContext _localctx = new BooleanExpressionContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_booleanExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(180);
			termf();
			setState(185);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==AND || _la==OR) {
				{
				{
				setState(181);
				_la = _input.LA(1);
				if ( !(_la==AND || _la==OR) ) {
				_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(182);
				termf();
				}
				}
				setState(187);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TermfContext extends ParserRuleContext {
		public TerminalNode TRUE() { return getToken(UDFParser.TRUE, 0); }
		public TerminalNode FALSE() { return getToken(UDFParser.FALSE, 0); }
		public List<TerminalNode> IDENTIFIER() { return getTokens(UDFParser.IDENTIFIER); }
		public TerminalNode IDENTIFIER(int i) {
			return getToken(UDFParser.IDENTIFIER, i);
		}
		public TerminalNode BinaryComparator() { return getToken(UDFParser.BinaryComparator, 0); }
		public NumberAndStringContext numberAndString() {
			return getRuleContext(NumberAndStringContext.class,0);
		}
		public ArrayExistsContext arrayExists() {
			return getRuleContext(ArrayExistsContext.class,0);
		}
		public TerminalNode LPAREN() { return getToken(UDFParser.LPAREN, 0); }
		public BooleanExpressionContext booleanExpression() {
			return getRuleContext(BooleanExpressionContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(UDFParser.RPAREN, 0); }
		public TermfContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_termf; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof UDFListener ) ((UDFListener)listener).enterTermf(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof UDFListener ) ((UDFListener)listener).exitTermf(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof UDFVisitor ) return ((UDFVisitor<? extends T>)visitor).visitTermf(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TermfContext termf() throws RecognitionException {
		TermfContext _localctx = new TermfContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_termf);
		try {
			setState(201);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,20,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(188);
				match(TRUE);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(189);
				match(FALSE);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(190);
				match(IDENTIFIER);
				setState(191);
				match(BinaryComparator);
				setState(192);
				numberAndString();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(193);
				match(IDENTIFIER);
				setState(194);
				match(BinaryComparator);
				setState(195);
				match(IDENTIFIER);
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(196);
				arrayExists();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(197);
				match(LPAREN);
				setState(198);
				booleanExpression();
				setState(199);
				match(RPAREN);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class BooleanValueContext extends ParserRuleContext {
		public TerminalNode TRUE() { return getToken(UDFParser.TRUE, 0); }
		public TerminalNode FALSE() { return getToken(UDFParser.FALSE, 0); }
		public BooleanValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_booleanValue; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof UDFListener ) ((UDFListener)listener).enterBooleanValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof UDFListener ) ((UDFListener)listener).exitBooleanValue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof UDFVisitor ) return ((UDFVisitor<? extends T>)visitor).visitBooleanValue(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BooleanValueContext booleanValue() throws RecognitionException {
		BooleanValueContext _localctx = new BooleanValueContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_booleanValue);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(203);
			_la = _input.LA(1);
			if ( !(_la==TRUE || _la==FALSE) ) {
			_errHandler.recoverInline(this);
			} else {
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class IdentifierContext extends ParserRuleContext {
		public TerminalNode IDENTIFIER() { return getToken(UDFParser.IDENTIFIER, 0); }
		public List<IdentifierContext> identifier() {
			return getRuleContexts(IdentifierContext.class);
		}
		public IdentifierContext identifier(int i) {
			return getRuleContext(IdentifierContext.class,i);
		}
		public QuotedIdentifierContext quotedIdentifier() {
			return getRuleContext(QuotedIdentifierContext.class,0);
		}
		public IdentifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_identifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof UDFListener ) ((UDFListener)listener).enterIdentifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof UDFListener ) ((UDFListener)listener).exitIdentifier(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof UDFVisitor ) return ((UDFVisitor<? extends T>)visitor).visitIdentifier(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IdentifierContext identifier() throws RecognitionException {
		IdentifierContext _localctx = new IdentifierContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_identifier);
		try {
			int _alt;
			setState(214);
			switch (_input.LA(1)) {
			case IDENTIFIER:
				enterOuterAlt(_localctx, 1);
				{
				setState(205);
				match(IDENTIFIER);
				setState(210);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,21,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(206);
						matchWildcard();
						setState(207);
						identifier();
						}
						} 
					}
					setState(212);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,21,_ctx);
				}
				}
				break;
			case BACKQUOTED_IDENTIFIER:
				enterOuterAlt(_localctx, 2);
				{
				setState(213);
				quotedIdentifier();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class QuotedIdentifierContext extends ParserRuleContext {
		public TerminalNode BACKQUOTED_IDENTIFIER() { return getToken(UDFParser.BACKQUOTED_IDENTIFIER, 0); }
		public QuotedIdentifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_quotedIdentifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof UDFListener ) ((UDFListener)listener).enterQuotedIdentifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof UDFListener ) ((UDFListener)listener).exitQuotedIdentifier(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof UDFVisitor ) return ((UDFVisitor<? extends T>)visitor).visitQuotedIdentifier(this);
			else return visitor.visitChildren(this);
		}
	}

	public final QuotedIdentifierContext quotedIdentifier() throws RecognitionException {
		QuotedIdentifierContext _localctx = new QuotedIdentifierContext(_ctx, getState());
		enterRule(_localctx, 44, RULE_quotedIdentifier);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(216);
			match(BACKQUOTED_IDENTIFIER);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class NumberContext extends ParserRuleContext {
		public TerminalNode DECIMAL_VALUE() { return getToken(UDFParser.DECIMAL_VALUE, 0); }
		public TerminalNode MINUS() { return getToken(UDFParser.MINUS, 0); }
		public TerminalNode INTEGER_VALUE() { return getToken(UDFParser.INTEGER_VALUE, 0); }
		public TerminalNode BIGINT_LITERAL() { return getToken(UDFParser.BIGINT_LITERAL, 0); }
		public TerminalNode SMALLINT_LITERAL() { return getToken(UDFParser.SMALLINT_LITERAL, 0); }
		public TerminalNode TINYINT_LITERAL() { return getToken(UDFParser.TINYINT_LITERAL, 0); }
		public TerminalNode DOUBLE_LITERAL() { return getToken(UDFParser.DOUBLE_LITERAL, 0); }
		public TerminalNode BIGDECIMAL_LITERAL() { return getToken(UDFParser.BIGDECIMAL_LITERAL, 0); }
		public NumberContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_number; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof UDFListener ) ((UDFListener)listener).enterNumber(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof UDFListener ) ((UDFListener)listener).exitNumber(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof UDFVisitor ) return ((UDFVisitor<? extends T>)visitor).visitNumber(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NumberContext number() throws RecognitionException {
		NumberContext _localctx = new NumberContext(_ctx, getState());
		enterRule(_localctx, 46, RULE_number);
		int _la;
		try {
			setState(246);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,30,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(219);
				_la = _input.LA(1);
				if (_la==MINUS) {
					{
					setState(218);
					match(MINUS);
					}
				}

				setState(221);
				match(DECIMAL_VALUE);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(223);
				_la = _input.LA(1);
				if (_la==MINUS) {
					{
					setState(222);
					match(MINUS);
					}
				}

				setState(225);
				match(INTEGER_VALUE);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(227);
				_la = _input.LA(1);
				if (_la==MINUS) {
					{
					setState(226);
					match(MINUS);
					}
				}

				setState(229);
				match(BIGINT_LITERAL);
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(231);
				_la = _input.LA(1);
				if (_la==MINUS) {
					{
					setState(230);
					match(MINUS);
					}
				}

				setState(233);
				match(SMALLINT_LITERAL);
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(235);
				_la = _input.LA(1);
				if (_la==MINUS) {
					{
					setState(234);
					match(MINUS);
					}
				}

				setState(237);
				match(TINYINT_LITERAL);
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(239);
				_la = _input.LA(1);
				if (_la==MINUS) {
					{
					setState(238);
					match(MINUS);
					}
				}

				setState(241);
				match(DOUBLE_LITERAL);
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(243);
				_la = _input.LA(1);
				if (_la==MINUS) {
					{
					setState(242);
					match(MINUS);
					}
				}

				setState(245);
				match(BIGDECIMAL_LITERAL);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3,\u00fb\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\3\2\3\2\3\2\3\3\3\3\3\3\3\3\5\3:\n\3\3\4\3\4\3\4\5\4?\n\4\3\4\3\4\3\4"+
		"\5\4D\n\4\7\4F\n\4\f\4\16\4I\13\4\3\4\3\4\7\4M\n\4\f\4\16\4P\13\4\5\4"+
		"R\n\4\3\5\3\5\3\5\3\6\3\6\3\6\5\6Z\n\6\3\7\3\7\3\7\5\7_\n\7\3\7\3\7\3"+
		"\7\3\7\3\7\3\b\3\b\3\b\5\bi\n\b\3\b\3\b\3\b\3\b\3\b\3\t\3\t\3\t\5\ts\n"+
		"\t\3\t\3\t\3\t\3\t\3\t\3\n\5\n{\n\n\3\n\3\n\3\13\3\13\3\13\3\f\3\f\3\f"+
		"\5\f\u0085\n\f\3\r\3\r\3\r\3\r\3\r\5\r\u008c\n\r\3\16\3\16\3\16\7\16\u0091"+
		"\n\16\f\16\16\16\u0094\13\16\3\17\3\17\3\17\7\17\u0099\n\17\f\17\16\17"+
		"\u009c\13\17\3\20\3\20\3\20\7\20\u00a1\n\20\f\20\16\20\u00a4\13\20\3\21"+
		"\3\21\3\21\3\21\3\21\3\21\5\21\u00ac\n\21\3\22\3\22\3\22\5\22\u00b1\n"+
		"\22\3\23\3\23\5\23\u00b5\n\23\3\24\3\24\3\24\7\24\u00ba\n\24\f\24\16\24"+
		"\u00bd\13\24\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3"+
		"\25\3\25\5\25\u00cc\n\25\3\26\3\26\3\27\3\27\3\27\7\27\u00d3\n\27\f\27"+
		"\16\27\u00d6\13\27\3\27\5\27\u00d9\n\27\3\30\3\30\3\31\5\31\u00de\n\31"+
		"\3\31\3\31\5\31\u00e2\n\31\3\31\3\31\5\31\u00e6\n\31\3\31\3\31\5\31\u00ea"+
		"\n\31\3\31\3\31\5\31\u00ee\n\31\3\31\3\31\5\31\u00f2\n\31\3\31\3\31\5"+
		"\31\u00f6\n\31\3\31\5\31\u00f9\n\31\3\31\3N\2\32\2\4\6\b\n\f\16\20\22"+
		"\24\26\30\32\34\36 \"$&(*,.\60\2\t\3\2\4\5\3\2\6\7\3\2\b\t\4\2\n\n\20"+
		"\20\3\2\f\r\3\2\16\17\3\2\21\22\u0111\2\62\3\2\2\2\4\65\3\2\2\2\6Q\3\2"+
		"\2\2\bS\3\2\2\2\nY\3\2\2\2\f[\3\2\2\2\16e\3\2\2\2\20o\3\2\2\2\22z\3\2"+
		"\2\2\24~\3\2\2\2\26\u0084\3\2\2\2\30\u008b\3\2\2\2\32\u008d\3\2\2\2\34"+
		"\u0095\3\2\2\2\36\u009d\3\2\2\2 \u00ab\3\2\2\2\"\u00b0\3\2\2\2$\u00b4"+
		"\3\2\2\2&\u00b6\3\2\2\2(\u00cb\3\2\2\2*\u00cd\3\2\2\2,\u00d8\3\2\2\2."+
		"\u00da\3\2\2\2\60\u00f8\3\2\2\2\62\63\5\4\3\2\63\64\7\2\2\3\64\3\3\2\2"+
		"\2\65\66\5\6\4\2\66\67\7\30\2\2\679\5,\27\28:\5\b\5\298\3\2\2\29:\3\2"+
		"\2\2:\5\3\2\2\2;>\7\27\2\2<?\5,\27\2=?\5\n\6\2><\3\2\2\2>=\3\2\2\2?G\3"+
		"\2\2\2@C\7\3\2\2AD\5,\27\2BD\5\n\6\2CA\3\2\2\2CB\3\2\2\2DF\3\2\2\2E@\3"+
		"\2\2\2FI\3\2\2\2GE\3\2\2\2GH\3\2\2\2HR\3\2\2\2IG\3\2\2\2JN\7\27\2\2KM"+
		"\13\2\2\2LK\3\2\2\2MP\3\2\2\2NO\3\2\2\2NL\3\2\2\2OR\3\2\2\2PN\3\2\2\2"+
		"Q;\3\2\2\2QJ\3\2\2\2R\7\3\2\2\2ST\7\31\2\2TU\5&\24\2U\t\3\2\2\2VZ\5\f"+
		"\7\2WZ\5\16\b\2XZ\5\20\t\2YV\3\2\2\2YW\3\2\2\2YX\3\2\2\2Z\13\3\2\2\2["+
		"^\t\2\2\2\\_\5,\27\2]_\5\n\6\2^\\\3\2\2\2^]\3\2\2\2_`\3\2\2\2`a\7\3\2"+
		"\2ab\5\22\n\2bc\3\2\2\2cd\7\26\2\2d\r\3\2\2\2eh\t\3\2\2fi\5,\27\2gi\5"+
		"\n\6\2hf\3\2\2\2hg\3\2\2\2ij\3\2\2\2jk\7\3\2\2kl\5\22\n\2lm\3\2\2\2mn"+
		"\7\26\2\2n\17\3\2\2\2or\t\4\2\2ps\5,\27\2qs\5\n\6\2rp\3\2\2\2rq\3\2\2"+
		"\2st\3\2\2\2tu\7\3\2\2uv\5\22\n\2vw\3\2\2\2wx\7\26\2\2x\21\3\2\2\2y{\5"+
		"\24\13\2zy\3\2\2\2z{\3\2\2\2{|\3\2\2\2|}\5\26\f\2}\23\3\2\2\2~\177\7&"+
		"\2\2\177\u0080\7\24\2\2\u0080\25\3\2\2\2\u0081\u0085\5\30\r\2\u0082\u0085"+
		"\5&\24\2\u0083\u0085\5\32\16\2\u0084\u0081\3\2\2\2\u0084\u0082\3\2\2\2"+
		"\u0084\u0083\3\2\2\2\u0085\27\3\2\2\2\u0086\u008c\7\23\2\2\u0087\u008c"+
		"\7,\2\2\u0088\u008c\5\60\31\2\u0089\u008c\5*\26\2\u008a\u008c\7\35\2\2"+
		"\u008b\u0086\3\2\2\2\u008b\u0087\3\2\2\2\u008b\u0088\3\2\2\2\u008b\u0089"+
		"\3\2\2\2\u008b\u008a\3\2\2\2\u008c\31\3\2\2\2\u008d\u0092\5\34\17\2\u008e"+
		"\u008f\t\5\2\2\u008f\u0091\5\34\17\2\u0090\u008e\3\2\2\2\u0091\u0094\3"+
		"\2\2\2\u0092\u0090\3\2\2\2\u0092\u0093\3\2\2\2\u0093\33\3\2\2\2\u0094"+
		"\u0092\3\2\2\2\u0095\u009a\5\36\20\2\u0096\u0097\7\13\2\2\u0097\u0099"+
		"\5\36\20\2\u0098\u0096\3\2\2\2\u0099\u009c\3\2\2\2\u009a\u0098\3\2\2\2"+
		"\u009a\u009b\3\2\2\2\u009b\35\3\2\2\2\u009c\u009a\3\2\2\2\u009d\u00a2"+
		"\5 \21\2\u009e\u009f\t\6\2\2\u009f\u00a1\5 \21\2\u00a0\u009e\3\2\2\2\u00a1"+
		"\u00a4\3\2\2\2\u00a2\u00a0\3\2\2\2\u00a2\u00a3\3\2\2\2\u00a3\37\3\2\2"+
		"\2\u00a4\u00a2\3\2\2\2\u00a5\u00ac\7&\2\2\u00a6\u00ac\5\"\22\2\u00a7\u00a8"+
		"\7\25\2\2\u00a8\u00a9\5\32\16\2\u00a9\u00aa\7\26\2\2\u00aa\u00ac\3\2\2"+
		"\2\u00ab\u00a5\3\2\2\2\u00ab\u00a6\3\2\2\2\u00ab\u00a7\3\2\2\2\u00ac!"+
		"\3\2\2\2\u00ad\u00b1\5\60\31\2\u00ae\u00b1\7\35\2\2\u00af\u00b1\7\23\2"+
		"\2\u00b0\u00ad\3\2\2\2\u00b0\u00ae\3\2\2\2\u00b0\u00af\3\2\2\2\u00b1#"+
		"\3\2\2\2\u00b2\u00b5\7&\2\2\u00b3\u00b5\5\30\r\2\u00b4\u00b2\3\2\2\2\u00b4"+
		"\u00b3\3\2\2\2\u00b5%\3\2\2\2\u00b6\u00bb\5(\25\2\u00b7\u00b8\t\7\2\2"+
		"\u00b8\u00ba\5(\25\2\u00b9\u00b7\3\2\2\2\u00ba\u00bd\3\2\2\2\u00bb\u00b9"+
		"\3\2\2\2\u00bb\u00bc\3\2\2\2\u00bc\'\3\2\2\2\u00bd\u00bb\3\2\2\2\u00be"+
		"\u00cc\7\21\2\2\u00bf\u00cc\7\22\2\2\u00c0\u00c1\7&\2\2\u00c1\u00c2\7"+
		"\33\2\2\u00c2\u00cc\5\"\22\2\u00c3\u00c4\7&\2\2\u00c4\u00c5\7\33\2\2\u00c5"+
		"\u00cc\7&\2\2\u00c6\u00cc\5\20\t\2\u00c7\u00c8\7\25\2\2\u00c8\u00c9\5"+
		"&\24\2\u00c9\u00ca\7\26\2\2\u00ca\u00cc\3\2\2\2\u00cb\u00be\3\2\2\2\u00cb"+
		"\u00bf\3\2\2\2\u00cb\u00c0\3\2\2\2\u00cb\u00c3\3\2\2\2\u00cb\u00c6\3\2"+
		"\2\2\u00cb\u00c7\3\2\2\2\u00cc)\3\2\2\2\u00cd\u00ce\t\b\2\2\u00ce+\3\2"+
		"\2\2\u00cf\u00d4\7&\2\2\u00d0\u00d1\13\2\2\2\u00d1\u00d3\5,\27\2\u00d2"+
		"\u00d0\3\2\2\2\u00d3\u00d6\3\2\2\2\u00d4\u00d2\3\2\2\2\u00d4\u00d5\3\2"+
		"\2\2\u00d5\u00d9\3\2\2\2\u00d6\u00d4\3\2\2\2\u00d7\u00d9\5.\30\2\u00d8"+
		"\u00cf\3\2\2\2\u00d8\u00d7\3\2\2\2\u00d9-\3\2\2\2\u00da\u00db\7\34\2\2"+
		"\u00db/\3\2\2\2\u00dc\u00de\7\20\2\2\u00dd\u00dc\3\2\2\2\u00dd\u00de\3"+
		"\2\2\2\u00de\u00df\3\2\2\2\u00df\u00f9\7%\2\2\u00e0\u00e2\7\20\2\2\u00e1"+
		"\u00e0\3\2\2\2\u00e1\u00e2\3\2\2\2\u00e2\u00e3\3\2\2\2\u00e3\u00f9\7$"+
		"\2\2\u00e4\u00e6\7\20\2\2\u00e5\u00e4\3\2\2\2\u00e5\u00e6\3\2\2\2\u00e6"+
		"\u00e7\3\2\2\2\u00e7\u00f9\7\36\2\2\u00e8\u00ea\7\20\2\2\u00e9\u00e8\3"+
		"\2\2\2\u00e9\u00ea\3\2\2\2\u00ea\u00eb\3\2\2\2\u00eb\u00f9\7\37\2\2\u00ec"+
		"\u00ee\7\20\2\2\u00ed\u00ec\3\2\2\2\u00ed\u00ee\3\2\2\2\u00ee\u00ef\3"+
		"\2\2\2\u00ef\u00f9\7 \2\2\u00f0\u00f2\7\20\2\2\u00f1\u00f0\3\2\2\2\u00f1"+
		"\u00f2\3\2\2\2\u00f2\u00f3\3\2\2\2\u00f3\u00f9\7\"\2\2\u00f4\u00f6\7\20"+
		"\2\2\u00f5\u00f4\3\2\2\2\u00f5\u00f6\3\2\2\2\u00f6\u00f7\3\2\2\2\u00f7"+
		"\u00f9\7#\2\2\u00f8\u00dd\3\2\2\2\u00f8\u00e1\3\2\2\2\u00f8\u00e5\3\2"+
		"\2\2\u00f8\u00e9\3\2\2\2\u00f8\u00ed\3\2\2\2\u00f8\u00f1\3\2\2\2\u00f8"+
		"\u00f5\3\2\2\2\u00f9\61\3\2\2\2!9>CGNQY^hrz\u0084\u008b\u0092\u009a\u00a2"+
		"\u00ab\u00b0\u00b4\u00bb\u00cb\u00d4\u00d8\u00dd\u00e1\u00e5\u00e9\u00ed"+
		"\u00f1\u00f5\u00f8";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}