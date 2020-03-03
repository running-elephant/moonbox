// Generated from moonbox/catalyst/core/parser/udf/udfParser/UDF.g4 by ANTLR 4.5.3
package moonbox.catalyst.core.parser.udf.udfParser;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class UDFLexer extends Lexer {
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
		UNRECOGNIZED=41;
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] ruleNames = {
		"T__0", "T__1", "T__2", "T__3", "T__4", "T__5", "T__6", "T__7", "T__8", 
		"T__9", "T__10", "AND", "OR", "MINUS", "TRUE", "FALSE", "NULL", "ARROW", 
		"LPAREN", "RPAREN", "SELECT", "FROM", "WHERE", "BinaryArithmeticOperator", 
		"BinaryComparator", "BACKQUOTED_IDENTIFIER", "STRING", "BIGINT_LITERAL", 
		"SMALLINT_LITERAL", "TINYINT_LITERAL", "BYTELENGTH_LITERAL", "DOUBLE_LITERAL", 
		"BIGDECIMAL_LITERAL", "INTEGER_VALUE", "DECIMAL_VALUE", "IDENTIFIER", 
		"DECIMAL_DIGITS", "EXPONENT", "DIGIT", "LETTER", "SIMPLE_COMMENT", "BRACKETED_EMPTY_COMMENT", 
		"BRACKETED_COMMENT", "WS", "UNRECOGNIZED"
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
		"BRACKETED_COMMENT", "WS", "UNRECOGNIZED"
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


	public UDFLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "UDF.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	@Override
	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 31:
			return DOUBLE_LITERAL_sempred((RuleContext)_localctx, predIndex);
		case 32:
			return BIGDECIMAL_LITERAL_sempred((RuleContext)_localctx, predIndex);
		case 34:
			return DECIMAL_VALUE_sempred((RuleContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean DOUBLE_LITERAL_sempred(RuleContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return isValidDecimal();
		}
		return true;
	}
	private boolean BIGDECIMAL_LITERAL_sempred(RuleContext _localctx, int predIndex) {
		switch (predIndex) {
		case 1:
			return isValidDecimal();
		}
		return true;
	}
	private boolean DECIMAL_VALUE_sempred(RuleContext _localctx, int predIndex) {
		switch (predIndex) {
		case 2:
			return isValidDecimal();
		}
		return true;
	}

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\2+\u01f3\b\1\4\2\t"+
		"\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t+\4"+
		",\t,\4-\t-\4.\t.\3\2\3\2\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\5\3\5\3\5\3\5\3\5\3\5\3\5"+
		"\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3"+
		"\6\3\6\3\6\3\6\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7"+
		"\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\t\3\t\3\n\3"+
		"\n\3\13\3\13\3\f\3\f\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\5\r\u00bf\n\r"+
		"\3\16\3\16\3\16\3\16\3\16\3\16\3\16\5\16\u00c8\n\16\3\17\3\17\3\20\3\20"+
		"\3\20\3\20\3\20\3\20\3\20\3\20\5\20\u00d4\n\20\3\21\3\21\3\21\3\21\3\21"+
		"\3\21\3\21\3\21\3\21\3\21\5\21\u00e0\n\21\3\22\3\22\3\22\3\22\3\22\3\23"+
		"\3\23\3\23\3\24\3\24\3\25\3\25\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26"+
		"\3\26\3\26\3\26\3\26\5\26\u00fa\n\26\3\27\3\27\3\27\3\27\3\27\3\27\3\27"+
		"\3\27\5\27\u0104\n\27\3\30\3\30\3\30\3\30\3\30\3\30\3\30\3\30\3\30\3\30"+
		"\5\30\u0110\n\30\3\31\3\31\3\32\3\32\3\32\3\32\3\32\3\32\3\32\3\32\3\32"+
		"\3\32\3\32\5\32\u011f\n\32\3\33\3\33\3\33\3\33\7\33\u0125\n\33\f\33\16"+
		"\33\u0128\13\33\3\33\3\33\3\34\3\34\3\34\3\34\7\34\u0130\n\34\f\34\16"+
		"\34\u0133\13\34\3\34\3\34\3\34\3\34\3\34\7\34\u013a\n\34\f\34\16\34\u013d"+
		"\13\34\3\34\5\34\u0140\n\34\3\35\6\35\u0143\n\35\r\35\16\35\u0144\3\35"+
		"\3\35\3\36\6\36\u014a\n\36\r\36\16\36\u014b\3\36\3\36\3\37\6\37\u0151"+
		"\n\37\r\37\16\37\u0152\3\37\3\37\3 \6 \u0158\n \r \16 \u0159\3 \3 \3!"+
		"\6!\u015f\n!\r!\16!\u0160\3!\5!\u0164\n!\3!\3!\3!\3!\5!\u016a\n!\3!\3"+
		"!\3!\5!\u016f\n!\3\"\6\"\u0172\n\"\r\"\16\"\u0173\3\"\5\"\u0177\n\"\3"+
		"\"\3\"\3\"\3\"\3\"\5\"\u017e\n\"\3\"\3\"\3\"\3\"\3\"\5\"\u0185\n\"\3#"+
		"\6#\u0188\n#\r#\16#\u0189\3$\6$\u018d\n$\r$\16$\u018e\3$\3$\3$\3$\5$\u0195"+
		"\n$\3$\3$\5$\u0199\n$\3%\3%\3%\6%\u019e\n%\r%\16%\u019f\3&\6&\u01a3\n"+
		"&\r&\16&\u01a4\3&\3&\7&\u01a9\n&\f&\16&\u01ac\13&\3&\3&\6&\u01b0\n&\r"+
		"&\16&\u01b1\5&\u01b4\n&\3\'\3\'\5\'\u01b8\n\'\3\'\6\'\u01bb\n\'\r\'\16"+
		"\'\u01bc\3(\3(\3)\5)\u01c2\n)\3*\3*\3*\3*\7*\u01c8\n*\f*\16*\u01cb\13"+
		"*\3*\5*\u01ce\n*\3*\5*\u01d1\n*\3*\3*\3+\3+\3+\3+\3+\3+\3+\3,\3,\3,\3"+
		",\3,\7,\u01e1\n,\f,\16,\u01e4\13,\3,\3,\3,\3,\3,\3-\6-\u01ec\n-\r-\16"+
		"-\u01ed\3-\3-\3.\3.\3\u01e2\2/\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13"+
		"\25\f\27\r\31\16\33\17\35\20\37\21!\22#\23%\24\'\25)\26+\27-\30/\31\61"+
		"\32\63\33\65\34\67\359\36;\37= ?!A\"C#E$G%I&K\2M\2O\2Q\2S\'U(W)Y*[+\3"+
		"\2\r\6\2\'\',-//\61\61\3\2bb\4\2))^^\4\2$$^^\6\2DDIIMMOO\4\2--//\3\2\62"+
		";\4\2C\\c|\4\2\f\f\17\17\3\2--\5\2\13\f\17\17\"\"\u0224\2\3\3\2\2\2\2"+
		"\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2"+
		"\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2"+
		"\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2"+
		"\2\'\3\2\2\2\2)\3\2\2\2\2+\3\2\2\2\2-\3\2\2\2\2/\3\2\2\2\2\61\3\2\2\2"+
		"\2\63\3\2\2\2\2\65\3\2\2\2\2\67\3\2\2\2\29\3\2\2\2\2;\3\2\2\2\2=\3\2\2"+
		"\2\2?\3\2\2\2\2A\3\2\2\2\2C\3\2\2\2\2E\3\2\2\2\2G\3\2\2\2\2I\3\2\2\2\2"+
		"S\3\2\2\2\2U\3\2\2\2\2W\3\2\2\2\2Y\3\2\2\2\2[\3\2\2\2\3]\3\2\2\2\5_\3"+
		"\2\2\2\7j\3\2\2\2\tu\3\2\2\2\13\u0083\3\2\2\2\r\u0091\3\2\2\2\17\u009f"+
		"\3\2\2\2\21\u00ad\3\2\2\2\23\u00af\3\2\2\2\25\u00b1\3\2\2\2\27\u00b3\3"+
		"\2\2\2\31\u00be\3\2\2\2\33\u00c7\3\2\2\2\35\u00c9\3\2\2\2\37\u00d3\3\2"+
		"\2\2!\u00df\3\2\2\2#\u00e1\3\2\2\2%\u00e6\3\2\2\2\'\u00e9\3\2\2\2)\u00eb"+
		"\3\2\2\2+\u00f9\3\2\2\2-\u0103\3\2\2\2/\u010f\3\2\2\2\61\u0111\3\2\2\2"+
		"\63\u011e\3\2\2\2\65\u0120\3\2\2\2\67\u013f\3\2\2\29\u0142\3\2\2\2;\u0149"+
		"\3\2\2\2=\u0150\3\2\2\2?\u0157\3\2\2\2A\u016e\3\2\2\2C\u0184\3\2\2\2E"+
		"\u0187\3\2\2\2G\u0198\3\2\2\2I\u019d\3\2\2\2K\u01b3\3\2\2\2M\u01b5\3\2"+
		"\2\2O\u01be\3\2\2\2Q\u01c1\3\2\2\2S\u01c3\3\2\2\2U\u01d4\3\2\2\2W\u01db"+
		"\3\2\2\2Y\u01eb\3\2\2\2[\u01f1\3\2\2\2]^\7.\2\2^\4\3\2\2\2_`\7c\2\2`a"+
		"\7t\2\2ab\7t\2\2bc\7c\2\2cd\7{\2\2de\7a\2\2ef\7o\2\2fg\7c\2\2gh\7r\2\2"+
		"hi\7*\2\2i\6\3\2\2\2jk\7C\2\2kl\7T\2\2lm\7T\2\2mn\7C\2\2no\7[\2\2op\7"+
		"a\2\2pq\7O\2\2qr\7C\2\2rs\7R\2\2st\7*\2\2t\b\3\2\2\2uv\7c\2\2vw\7t\2\2"+
		"wx\7t\2\2xy\7c\2\2yz\7{\2\2z{\7a\2\2{|\7h\2\2|}\7k\2\2}~\7n\2\2~\177\7"+
		"v\2\2\177\u0080\7g\2\2\u0080\u0081\7t\2\2\u0081\u0082\7*\2\2\u0082\n\3"+
		"\2\2\2\u0083\u0084\7C\2\2\u0084\u0085\7T\2\2\u0085\u0086\7T\2\2\u0086"+
		"\u0087\7C\2\2\u0087\u0088\7[\2\2\u0088\u0089\7a\2\2\u0089\u008a\7H\2\2"+
		"\u008a\u008b\7K\2\2\u008b\u008c\7N\2\2\u008c\u008d\7V\2\2\u008d\u008e"+
		"\7G\2\2\u008e\u008f\7T\2\2\u008f\u0090\7*\2\2\u0090\f\3\2\2\2\u0091\u0092"+
		"\7c\2\2\u0092\u0093\7t\2\2\u0093\u0094\7t\2\2\u0094\u0095\7c\2\2\u0095"+
		"\u0096\7{\2\2\u0096\u0097\7a\2\2\u0097\u0098\7g\2\2\u0098\u0099\7z\2\2"+
		"\u0099\u009a\7k\2\2\u009a\u009b\7u\2\2\u009b\u009c\7v\2\2\u009c\u009d"+
		"\7u\2\2\u009d\u009e\7*\2\2\u009e\16\3\2\2\2\u009f\u00a0\7C\2\2\u00a0\u00a1"+
		"\7T\2\2\u00a1\u00a2\7T\2\2\u00a2\u00a3\7C\2\2\u00a3\u00a4\7[\2\2\u00a4"+
		"\u00a5\7a\2\2\u00a5\u00a6\7G\2\2\u00a6\u00a7\7Z\2\2\u00a7\u00a8\7K\2\2"+
		"\u00a8\u00a9\7U\2\2\u00a9\u00aa\7V\2\2\u00aa\u00ab\7U\2\2\u00ab\u00ac"+
		"\7*\2\2\u00ac\20\3\2\2\2\u00ad\u00ae\7-\2\2\u00ae\22\3\2\2\2\u00af\u00b0"+
		"\7\'\2\2\u00b0\24\3\2\2\2\u00b1\u00b2\7,\2\2\u00b2\26\3\2\2\2\u00b3\u00b4"+
		"\7\61\2\2\u00b4\30\3\2\2\2\u00b5\u00b6\7C\2\2\u00b6\u00b7\7P\2\2\u00b7"+
		"\u00bf\7F\2\2\u00b8\u00b9\7c\2\2\u00b9\u00ba\7p\2\2\u00ba\u00bf\7f\2\2"+
		"\u00bb\u00bc\7(\2\2\u00bc\u00bf\7(\2\2\u00bd\u00bf\7(\2\2\u00be\u00b5"+
		"\3\2\2\2\u00be\u00b8\3\2\2\2\u00be\u00bb\3\2\2\2\u00be\u00bd\3\2\2\2\u00bf"+
		"\32\3\2\2\2\u00c0\u00c1\7Q\2\2\u00c1\u00c8\7T\2\2\u00c2\u00c3\7q\2\2\u00c3"+
		"\u00c8\7t\2\2\u00c4\u00c5\7~\2\2\u00c5\u00c8\7~\2\2\u00c6\u00c8\7~\2\2"+
		"\u00c7\u00c0\3\2\2\2\u00c7\u00c2\3\2\2\2\u00c7\u00c4\3\2\2\2\u00c7\u00c6"+
		"\3\2\2\2\u00c8\34\3\2\2\2\u00c9\u00ca\7/\2\2\u00ca\36\3\2\2\2\u00cb\u00cc"+
		"\7V\2\2\u00cc\u00cd\7T\2\2\u00cd\u00ce\7W\2\2\u00ce\u00d4\7G\2\2\u00cf"+
		"\u00d0\7v\2\2\u00d0\u00d1\7t\2\2\u00d1\u00d2\7w\2\2\u00d2\u00d4\7g\2\2"+
		"\u00d3\u00cb\3\2\2\2\u00d3\u00cf\3\2\2\2\u00d4 \3\2\2\2\u00d5\u00d6\7"+
		"H\2\2\u00d6\u00d7\7C\2\2\u00d7\u00d8\7N\2\2\u00d8\u00d9\7U\2\2\u00d9\u00e0"+
		"\7G\2\2\u00da\u00db\7h\2\2\u00db\u00dc\7c\2\2\u00dc\u00dd\7n\2\2\u00dd"+
		"\u00de\7u\2\2\u00de\u00e0\7g\2\2\u00df\u00d5\3\2\2\2\u00df\u00da\3\2\2"+
		"\2\u00e0\"\3\2\2\2\u00e1\u00e2\7P\2\2\u00e2\u00e3\7W\2\2\u00e3\u00e4\7"+
		"N\2\2\u00e4\u00e5\7N\2\2\u00e5$\3\2\2\2\u00e6\u00e7\7?\2\2\u00e7\u00e8"+
		"\7@\2\2\u00e8&\3\2\2\2\u00e9\u00ea\7*\2\2\u00ea(\3\2\2\2\u00eb\u00ec\7"+
		"+\2\2\u00ec*\3\2\2\2\u00ed\u00ee\7U\2\2\u00ee\u00ef\7G\2\2\u00ef\u00f0"+
		"\7N\2\2\u00f0\u00f1\7G\2\2\u00f1\u00f2\7E\2\2\u00f2\u00fa\7V\2\2\u00f3"+
		"\u00f4\7u\2\2\u00f4\u00f5\7g\2\2\u00f5\u00f6\7n\2\2\u00f6\u00f7\7g\2\2"+
		"\u00f7\u00f8\7e\2\2\u00f8\u00fa\7v\2\2\u00f9\u00ed\3\2\2\2\u00f9\u00f3"+
		"\3\2\2\2\u00fa,\3\2\2\2\u00fb\u00fc\7h\2\2\u00fc\u00fd\7t\2\2\u00fd\u00fe"+
		"\7q\2\2\u00fe\u0104\7o\2\2\u00ff\u0100\7H\2\2\u0100\u0101\7T\2\2\u0101"+
		"\u0102\7Q\2\2\u0102\u0104\7O\2\2\u0103\u00fb\3\2\2\2\u0103\u00ff\3\2\2"+
		"\2\u0104.\3\2\2\2\u0105\u0106\7y\2\2\u0106\u0107\7j\2\2\u0107\u0108\7"+
		"g\2\2\u0108\u0109\7t\2\2\u0109\u0110\7g\2\2\u010a\u010b\7Y\2\2\u010b\u010c"+
		"\7J\2\2\u010c\u010d\7G\2\2\u010d\u010e\7T\2\2\u010e\u0110\7G\2\2\u010f"+
		"\u0105\3\2\2\2\u010f\u010a\3\2\2\2\u0110\60\3\2\2\2\u0111\u0112\t\2\2"+
		"\2\u0112\62\3\2\2\2\u0113\u011f\7@\2\2\u0114\u0115\7@\2\2\u0115\u011f"+
		"\7?\2\2\u0116\u011f\7>\2\2\u0117\u0118\7>\2\2\u0118\u011f\7?\2\2\u0119"+
		"\u011a\7?\2\2\u011a\u011f\7?\2\2\u011b\u011c\7#\2\2\u011c\u011f\7?\2\2"+
		"\u011d\u011f\7?\2\2\u011e\u0113\3\2\2\2\u011e\u0114\3\2\2\2\u011e\u0116"+
		"\3\2\2\2\u011e\u0117\3\2\2\2\u011e\u0119\3\2\2\2\u011e\u011b\3\2\2\2\u011e"+
		"\u011d\3\2\2\2\u011f\64\3\2\2\2\u0120\u0126\7b\2\2\u0121\u0125\n\3\2\2"+
		"\u0122\u0123\7b\2\2\u0123\u0125\7b\2\2\u0124\u0121\3\2\2\2\u0124\u0122"+
		"\3\2\2\2\u0125\u0128\3\2\2\2\u0126\u0124\3\2\2\2\u0126\u0127\3\2\2\2\u0127"+
		"\u0129\3\2\2\2\u0128\u0126\3\2\2\2\u0129\u012a\7b\2\2\u012a\66\3\2\2\2"+
		"\u012b\u0131\7)\2\2\u012c\u0130\n\4\2\2\u012d\u012e\7^\2\2\u012e\u0130"+
		"\13\2\2\2\u012f\u012c\3\2\2\2\u012f\u012d\3\2\2\2\u0130\u0133\3\2\2\2"+
		"\u0131\u012f\3\2\2\2\u0131\u0132\3\2\2\2\u0132\u0134\3\2\2\2\u0133\u0131"+
		"\3\2\2\2\u0134\u0140\7)\2\2\u0135\u013b\7$\2\2\u0136\u013a\n\5\2\2\u0137"+
		"\u0138\7^\2\2\u0138\u013a\13\2\2\2\u0139\u0136\3\2\2\2\u0139\u0137\3\2"+
		"\2\2\u013a\u013d\3\2\2\2\u013b\u0139\3\2\2\2\u013b\u013c\3\2\2\2\u013c"+
		"\u013e\3\2\2\2\u013d\u013b\3\2\2\2\u013e\u0140\7$\2\2\u013f\u012b\3\2"+
		"\2\2\u013f\u0135\3\2\2\2\u01408\3\2\2\2\u0141\u0143\5O(\2\u0142\u0141"+
		"\3\2\2\2\u0143\u0144\3\2\2\2\u0144\u0142\3\2\2\2\u0144\u0145\3\2\2\2\u0145"+
		"\u0146\3\2\2\2\u0146\u0147\7N\2\2\u0147:\3\2\2\2\u0148\u014a\5O(\2\u0149"+
		"\u0148\3\2\2\2\u014a\u014b\3\2\2\2\u014b\u0149\3\2\2\2\u014b\u014c\3\2"+
		"\2\2\u014c\u014d\3\2\2\2\u014d\u014e\7U\2\2\u014e<\3\2\2\2\u014f\u0151"+
		"\5O(\2\u0150\u014f\3\2\2\2\u0151\u0152\3\2\2\2\u0152\u0150\3\2\2\2\u0152"+
		"\u0153\3\2\2\2\u0153\u0154\3\2\2\2\u0154\u0155\7[\2\2\u0155>\3\2\2\2\u0156"+
		"\u0158\5O(\2\u0157\u0156\3\2\2\2\u0158\u0159\3\2\2\2\u0159\u0157\3\2\2"+
		"\2\u0159\u015a\3\2\2\2\u015a\u015b\3\2\2\2\u015b\u015c\t\6\2\2\u015c@"+
		"\3\2\2\2\u015d\u015f\5O(\2\u015e\u015d\3\2\2\2\u015f\u0160\3\2\2\2\u0160"+
		"\u015e\3\2\2\2\u0160\u0161\3\2\2\2\u0161\u0163\3\2\2\2\u0162\u0164\5M"+
		"\'\2\u0163\u0162\3\2\2\2\u0163\u0164\3\2\2\2\u0164\u0165\3\2\2\2\u0165"+
		"\u0166\7F\2\2\u0166\u016f\3\2\2\2\u0167\u0169\5K&\2\u0168\u016a\5M\'\2"+
		"\u0169\u0168\3\2\2\2\u0169\u016a\3\2\2\2\u016a\u016b\3\2\2\2\u016b\u016c"+
		"\7F\2\2\u016c\u016d\6!\2\2\u016d\u016f\3\2\2\2\u016e\u015e\3\2\2\2\u016e"+
		"\u0167\3\2\2\2\u016fB\3\2\2\2\u0170\u0172\5O(\2\u0171\u0170\3\2\2\2\u0172"+
		"\u0173\3\2\2\2\u0173\u0171\3\2\2\2\u0173\u0174\3\2\2\2\u0174\u0176\3\2"+
		"\2\2\u0175\u0177\5M\'\2\u0176\u0175\3\2\2\2\u0176\u0177\3\2\2\2\u0177"+
		"\u0178\3\2\2\2\u0178\u0179\7D\2\2\u0179\u017a\7F\2\2\u017a\u0185\3\2\2"+
		"\2\u017b\u017d\5K&\2\u017c\u017e\5M\'\2\u017d\u017c\3\2\2\2\u017d\u017e"+
		"\3\2\2\2\u017e\u017f\3\2\2\2\u017f\u0180\7D\2\2\u0180\u0181\7F\2\2\u0181"+
		"\u0182\3\2\2\2\u0182\u0183\6\"\3\2\u0183\u0185\3\2\2\2\u0184\u0171\3\2"+
		"\2\2\u0184\u017b\3\2\2\2\u0185D\3\2\2\2\u0186\u0188\5O(\2\u0187\u0186"+
		"\3\2\2\2\u0188\u0189\3\2\2\2\u0189\u0187\3\2\2\2\u0189\u018a\3\2\2\2\u018a"+
		"F\3\2\2\2\u018b\u018d\5O(\2\u018c\u018b\3\2\2\2\u018d\u018e\3\2\2\2\u018e"+
		"\u018c\3\2\2\2\u018e\u018f\3\2\2\2\u018f\u0190\3\2\2\2\u0190\u0191\5M"+
		"\'\2\u0191\u0199\3\2\2\2\u0192\u0194\5K&\2\u0193\u0195\5M\'\2\u0194\u0193"+
		"\3\2\2\2\u0194\u0195\3\2\2\2\u0195\u0196\3\2\2\2\u0196\u0197\6$\4\2\u0197"+
		"\u0199\3\2\2\2\u0198\u018c\3\2\2\2\u0198\u0192\3\2\2\2\u0199H\3\2\2\2"+
		"\u019a\u019e\5Q)\2\u019b\u019e\5O(\2\u019c\u019e\7a\2\2\u019d\u019a\3"+
		"\2\2\2\u019d\u019b\3\2\2\2\u019d\u019c\3\2\2\2\u019e\u019f\3\2\2\2\u019f"+
		"\u019d\3\2\2\2\u019f\u01a0\3\2\2\2\u01a0J\3\2\2\2\u01a1\u01a3\5O(\2\u01a2"+
		"\u01a1\3\2\2\2\u01a3\u01a4\3\2\2\2\u01a4\u01a2\3\2\2\2\u01a4\u01a5\3\2"+
		"\2\2\u01a5\u01a6\3\2\2\2\u01a6\u01aa\7\60\2\2\u01a7\u01a9\5O(\2\u01a8"+
		"\u01a7\3\2\2\2\u01a9\u01ac\3\2\2\2\u01aa\u01a8\3\2\2\2\u01aa\u01ab\3\2"+
		"\2\2\u01ab\u01b4\3\2\2\2\u01ac\u01aa\3\2\2\2\u01ad\u01af\7\60\2\2\u01ae"+
		"\u01b0\5O(\2\u01af\u01ae\3\2\2\2\u01b0\u01b1\3\2\2\2\u01b1\u01af\3\2\2"+
		"\2\u01b1\u01b2\3\2\2\2\u01b2\u01b4\3\2\2\2\u01b3\u01a2\3\2\2\2\u01b3\u01ad"+
		"\3\2\2\2\u01b4L\3\2\2\2\u01b5\u01b7\7G\2\2\u01b6\u01b8\t\7\2\2\u01b7\u01b6"+
		"\3\2\2\2\u01b7\u01b8\3\2\2\2\u01b8\u01ba\3\2\2\2\u01b9\u01bb\5O(\2\u01ba"+
		"\u01b9\3\2\2\2\u01bb\u01bc\3\2\2\2\u01bc\u01ba\3\2\2\2\u01bc\u01bd\3\2"+
		"\2\2\u01bdN\3\2\2\2\u01be\u01bf\t\b\2\2\u01bfP\3\2\2\2\u01c0\u01c2\t\t"+
		"\2\2\u01c1\u01c0\3\2\2\2\u01c2R\3\2\2\2\u01c3\u01c4\7/\2\2\u01c4\u01c5"+
		"\7/\2\2\u01c5\u01c9\3\2\2\2\u01c6\u01c8\n\n\2\2\u01c7\u01c6\3\2\2\2\u01c8"+
		"\u01cb\3\2\2\2\u01c9\u01c7\3\2\2\2\u01c9\u01ca\3\2\2\2\u01ca\u01cd\3\2"+
		"\2\2\u01cb\u01c9\3\2\2\2\u01cc\u01ce\7\17\2\2\u01cd\u01cc\3\2\2\2\u01cd"+
		"\u01ce\3\2\2\2\u01ce\u01d0\3\2\2\2\u01cf\u01d1\7\f\2\2\u01d0\u01cf\3\2"+
		"\2\2\u01d0\u01d1\3\2\2\2\u01d1\u01d2\3\2\2\2\u01d2\u01d3\b*\2\2\u01d3"+
		"T\3\2\2\2\u01d4\u01d5\7\61\2\2\u01d5\u01d6\7,\2\2\u01d6\u01d7\7,\2\2\u01d7"+
		"\u01d8\7\61\2\2\u01d8\u01d9\3\2\2\2\u01d9\u01da\b+\2\2\u01daV\3\2\2\2"+
		"\u01db\u01dc\7\61\2\2\u01dc\u01dd\7,\2\2\u01dd\u01de\3\2\2\2\u01de\u01e2"+
		"\n\13\2\2\u01df\u01e1\13\2\2\2\u01e0\u01df\3\2\2\2\u01e1\u01e4\3\2\2\2"+
		"\u01e2\u01e3\3\2\2\2\u01e2\u01e0\3\2\2\2\u01e3\u01e5\3\2\2\2\u01e4\u01e2"+
		"\3\2\2\2\u01e5\u01e6\7,\2\2\u01e6\u01e7\7\61\2\2\u01e7\u01e8\3\2\2\2\u01e8"+
		"\u01e9\b,\2\2\u01e9X\3\2\2\2\u01ea\u01ec\t\f\2\2\u01eb\u01ea\3\2\2\2\u01ec"+
		"\u01ed\3\2\2\2\u01ed\u01eb\3\2\2\2\u01ed\u01ee\3\2\2\2\u01ee\u01ef\3\2"+
		"\2\2\u01ef\u01f0\b-\2\2\u01f0Z\3\2\2\2\u01f1\u01f2\13\2\2\2\u01f2\\\3"+
		"\2\2\2\60\2\u00be\u00c7\u00d3\u00df\u00f9\u0103\u010f\u011e\u0124\u0126"+
		"\u012f\u0131\u0139\u013b\u013f\u0144\u014b\u0152\u0159\u0160\u0163\u0169"+
		"\u016e\u0173\u0176\u017d\u0184\u0189\u018e\u0194\u0198\u019d\u019f\u01a4"+
		"\u01aa\u01b1\u01b3\u01b7\u01bc\u01c1\u01c9\u01cd\u01d0\u01e2\u01ed\3\2"+
		"\3\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}