// Generated from moonbox/core/parser/MqlBase.g4 by ANTLR 4.5.3
package moonbox.core.parser;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class MqlBaseParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.5.3", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, T__10=11, T__11=12, T__12=13, ACCOUNT=14, ADD=15, ALTER=16, APPLICATION=17, 
		APPLICATIONS=18, ARRAY=19, AT=20, MAP=21, STRUCT=22, AS=23, BY=24, CACHE=25, 
		CALL=26, CASCADE=27, COLUMN=28, COLUMNS=29, COMMENT=30, CHANGE=31, CREATE=32, 
		CURRENT_USER=33, DATABASE=34, DATABASES=35, DATASOURCE=36, DATASOURCES=37, 
		DDL=38, DEFINER=39, DESC=40, DESCRIBE=41, DISABLE=42, DO=43, DML=44, DMLON=45, 
		DROP=46, ENABLE=47, EQ=48, NEQ=49, EVENT=50, EXISTS=51, EXPLAIN=52, EXTENDED=53, 
		FROM=54, FUNCTION=55, FUNCTIONS=56, GRANT=57, GROUP=58, GROUPS=59, IDENTIFIED=60, 
		IF=61, IN=62, INSERT=63, INTO=64, LIKE=65, MOUNT=66, NOT=67, ON=68, OPTION=69, 
		OPTIONS=70, OR=71, ORG=72, ORGANIZATION=73, OVERWRITE=74, PLAN=75, REMOVE=76, 
		RENAME=77, REPLACE=78, REVOKE=79, SA=80, SCHEDULE=81, SELECT=82, SET=83, 
		SHOW=84, STAR=85, STREAM=86, SYSINFO=87, TABLE=88, TABLES=89, TEMP=90, 
		TEMPORARY=91, TO=92, TYPE=93, UNMOUNT=94, USE=95, USER=96, USERS=97, VIEW=98, 
		VIEWS=99, WITH=100, STRING=101, INTEGER_VALUE=102, IDENTIFIER=103, SIMPLE_COMMENT=104, 
		BRACKETED_COMMENT=105, WS=106, UNRECOGNIZED=107, DELIMITER=108;
	public static final int
		RULE_single = 0, RULE_mql = 1, RULE_definer = 2, RULE_schedule = 3, RULE_starOrInteger = 4, 
		RULE_appCmds = 5, RULE_nonLastCmdList = 6, RULE_nonLastCmd = 7, RULE_lastCmd = 8, 
		RULE_insertIntoCmd = 9, RULE_insertOverwriteCmd = 10, RULE_createTemporaryViewCmd = 11, 
		RULE_createTemporaryFunctionCmd = 12, RULE_query = 13, RULE_ctes = 14, 
		RULE_dataType = 15, RULE_colTypeList = 16, RULE_colType = 17, RULE_complexColTypeList = 18, 
		RULE_complexColType = 19, RULE_namedQuery = 20, RULE_mountTableList = 21, 
		RULE_mountTableOptions = 22, RULE_privilegeList = 23, RULE_privilege = 24, 
		RULE_qualifiedColumnList = 25, RULE_columnIdentifier = 26, RULE_identifierStarList = 27, 
		RULE_addUser = 28, RULE_removeUser = 29, RULE_identifierList = 30, RULE_funcIdentifier = 31, 
		RULE_tableIdentifier = 32, RULE_propertyList = 33, RULE_property = 34, 
		RULE_propertyKey = 35, RULE_password = 36, RULE_identifier = 37, RULE_generalIdentifier = 38, 
		RULE_nonReserved = 39;
	public static final String[] ruleNames = {
		"single", "mql", "definer", "schedule", "starOrInteger", "appCmds", "nonLastCmdList", 
		"nonLastCmd", "lastCmd", "insertIntoCmd", "insertOverwriteCmd", "createTemporaryViewCmd", 
		"createTemporaryFunctionCmd", "query", "ctes", "dataType", "colTypeList", 
		"colType", "complexColTypeList", "complexColType", "namedQuery", "mountTableList", 
		"mountTableOptions", "privilegeList", "privilege", "qualifiedColumnList", 
		"columnIdentifier", "identifierStarList", "addUser", "removeUser", "identifierList", 
		"funcIdentifier", "tableIdentifier", "propertyList", "property", "propertyKey", 
		"password", "identifier", "generalIdentifier", "nonReserved"
	};

	private static final String[] _LITERAL_NAMES = {
		null, "'('", "')'", "','", "';'", "'<'", "'>'", "':'", "'.'", "'['", "']'", 
		"'{'", "'}'", "'/'", "'ACCOUNT'", "'ADD'", "'ALTER'", "'APPLICATION'", 
		"'APPLICATIONS'", "'ARRAY'", "'AT'", "'MAP'", "'STRUCT'", "'AS'", "'BY'", 
		"'CACHE'", "'CALL'", "'CASCADE'", "'COLUMN'", "'COLUMNS'", "'COMMENT'", 
		"'CHANGE'", "'CREATE'", "'CURRENT_USER'", "'DATABASE'", "'DATABASES'", 
		"'DATASOURCE'", "'DATASOURCES'", "'DDL'", "'DEFINER'", "'DESC'", "'DESCRIBE'", 
		"'DISABLE'", "'DO'", "'DML'", "'DMLON'", "'DROP'", "'ENABLE'", null, "'<>'", 
		"'EVENT'", "'EXISTS'", "'EXPLAIN'", "'EXTENDED'", "'FROM'", "'FUNCTION'", 
		"'FUNCTIONS'", "'GRANT'", "'GROUP'", "'GROUPS'", "'IDENTIFIED '", "'IF'", 
		"'IN'", "'INSERT'", "'INTO'", "'LIKE'", "'MOUNT'", "'NOT'", "'ON'", "'OPTION'", 
		"'OPTIONS'", "'OR'", "'ORG'", "'ORGANIZATION'", "'OVERWRITE'", "'PLAN'", 
		"'REMOVE'", "'RENAME'", "'REPLACE'", "'REVOKE'", "'SA'", "'SCHEDULE'", 
		"'SELECT'", "'SET'", "'SHOW'", "'*'", "'STREAM'", "'SYSINFO'", "'TABLE'", 
		"'TABLES'", "'TEMP'", "'TEMPORARY'", "'TO'", "'TYPE'", "'UNMOUNT'", "'USE'", 
		"'USER'", "'USERS'", "'VIEW'", "'VIEWS'", "'WITH'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, null, "ACCOUNT", "ADD", "ALTER", "APPLICATION", "APPLICATIONS", 
		"ARRAY", "AT", "MAP", "STRUCT", "AS", "BY", "CACHE", "CALL", "CASCADE", 
		"COLUMN", "COLUMNS", "COMMENT", "CHANGE", "CREATE", "CURRENT_USER", "DATABASE", 
		"DATABASES", "DATASOURCE", "DATASOURCES", "DDL", "DEFINER", "DESC", "DESCRIBE", 
		"DISABLE", "DO", "DML", "DMLON", "DROP", "ENABLE", "EQ", "NEQ", "EVENT", 
		"EXISTS", "EXPLAIN", "EXTENDED", "FROM", "FUNCTION", "FUNCTIONS", "GRANT", 
		"GROUP", "GROUPS", "IDENTIFIED", "IF", "IN", "INSERT", "INTO", "LIKE", 
		"MOUNT", "NOT", "ON", "OPTION", "OPTIONS", "OR", "ORG", "ORGANIZATION", 
		"OVERWRITE", "PLAN", "REMOVE", "RENAME", "REPLACE", "REVOKE", "SA", "SCHEDULE", 
		"SELECT", "SET", "SHOW", "STAR", "STREAM", "SYSINFO", "TABLE", "TABLES", 
		"TEMP", "TEMPORARY", "TO", "TYPE", "UNMOUNT", "USE", "USER", "USERS", 
		"VIEW", "VIEWS", "WITH", "STRING", "INTEGER_VALUE", "IDENTIFIER", "SIMPLE_COMMENT", 
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
	public String getGrammarFileName() { return "MqlBase.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public MqlBaseParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class SingleContext extends ParserRuleContext {
		public MqlContext mql() {
			return getRuleContext(MqlContext.class,0);
		}
		public TerminalNode EOF() { return getToken(MqlBaseParser.EOF, 0); }
		public SingleContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_single; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterSingle(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitSingle(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitSingle(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SingleContext single() throws RecognitionException {
		SingleContext _localctx = new SingleContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_single);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(80);
			mql();
			setState(81);
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

	public static class MqlContext extends ParserRuleContext {
		public MqlContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_mql; }
	 
		public MqlContext() { }
		public void copyFrom(MqlContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class SetTableNameContext extends MqlContext {
		public TableIdentifierContext name;
		public TableIdentifierContext newName;
		public TerminalNode ALTER() { return getToken(MqlBaseParser.ALTER, 0); }
		public TerminalNode TABLE() { return getToken(MqlBaseParser.TABLE, 0); }
		public TerminalNode RENAME() { return getToken(MqlBaseParser.RENAME, 0); }
		public TerminalNode TO() { return getToken(MqlBaseParser.TO, 0); }
		public List<TableIdentifierContext> tableIdentifier() {
			return getRuleContexts(TableIdentifierContext.class);
		}
		public TableIdentifierContext tableIdentifier(int i) {
			return getRuleContext(TableIdentifierContext.class,i);
		}
		public SetTableNameContext(MqlContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterSetTableName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitSetTableName(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitSetTableName(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class SetDefinerContext extends MqlContext {
		public IdentifierContext name;
		public TerminalNode ALTER() { return getToken(MqlBaseParser.ALTER, 0); }
		public TerminalNode DEFINER() { return getToken(MqlBaseParser.DEFINER, 0); }
		public DefinerContext definer() {
			return getRuleContext(DefinerContext.class,0);
		}
		public TerminalNode EVENT() { return getToken(MqlBaseParser.EVENT, 0); }
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public SetDefinerContext(MqlContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterSetDefiner(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitSetDefiner(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitSetDefiner(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ExplainContext extends MqlContext {
		public TerminalNode EXPLAIN() { return getToken(MqlBaseParser.EXPLAIN, 0); }
		public QueryContext query() {
			return getRuleContext(QueryContext.class,0);
		}
		public TerminalNode EXTENDED() { return getToken(MqlBaseParser.EXTENDED, 0); }
		public TerminalNode PLAN() { return getToken(MqlBaseParser.PLAN, 0); }
		public ExplainContext(MqlContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterExplain(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitExplain(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitExplain(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class RevokeGrantFromGroupContext extends MqlContext {
		public IdentifierListContext groups;
		public TerminalNode REVOKE() { return getToken(MqlBaseParser.REVOKE, 0); }
		public TerminalNode GRANT() { return getToken(MqlBaseParser.GRANT, 0); }
		public TerminalNode OPTION() { return getToken(MqlBaseParser.OPTION, 0); }
		public PrivilegeListContext privilegeList() {
			return getRuleContext(PrivilegeListContext.class,0);
		}
		public TerminalNode FROM() { return getToken(MqlBaseParser.FROM, 0); }
		public TerminalNode GROUP() { return getToken(MqlBaseParser.GROUP, 0); }
		public IdentifierListContext identifierList() {
			return getRuleContext(IdentifierListContext.class,0);
		}
		public RevokeGrantFromGroupContext(MqlContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterRevokeGrantFromGroup(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitRevokeGrantFromGroup(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitRevokeGrantFromGroup(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class DropDatabaseContext extends MqlContext {
		public IdentifierContext name;
		public TerminalNode DROP() { return getToken(MqlBaseParser.DROP, 0); }
		public TerminalNode DATABASE() { return getToken(MqlBaseParser.DATABASE, 0); }
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public TerminalNode IF() { return getToken(MqlBaseParser.IF, 0); }
		public TerminalNode EXISTS() { return getToken(MqlBaseParser.EXISTS, 0); }
		public TerminalNode CASCADE() { return getToken(MqlBaseParser.CASCADE, 0); }
		public DropDatabaseContext(MqlContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterDropDatabase(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitDropDatabase(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitDropDatabase(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class DescTableContext extends MqlContext {
		public TerminalNode TABLE() { return getToken(MqlBaseParser.TABLE, 0); }
		public TableIdentifierContext tableIdentifier() {
			return getRuleContext(TableIdentifierContext.class,0);
		}
		public TerminalNode DESC() { return getToken(MqlBaseParser.DESC, 0); }
		public TerminalNode DESCRIBE() { return getToken(MqlBaseParser.DESCRIBE, 0); }
		public TerminalNode EXTENDED() { return getToken(MqlBaseParser.EXTENDED, 0); }
		public DescTableContext(MqlContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterDescTable(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitDescTable(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitDescTable(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ShowUsersContext extends MqlContext {
		public Token pattern;
		public TerminalNode SHOW() { return getToken(MqlBaseParser.SHOW, 0); }
		public TerminalNode USERS() { return getToken(MqlBaseParser.USERS, 0); }
		public TerminalNode LIKE() { return getToken(MqlBaseParser.LIKE, 0); }
		public TerminalNode STRING() { return getToken(MqlBaseParser.STRING, 0); }
		public ShowUsersContext(MqlContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterShowUsers(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitShowUsers(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitShowUsers(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class DropViewContext extends MqlContext {
		public TableIdentifierContext name;
		public TerminalNode DROP() { return getToken(MqlBaseParser.DROP, 0); }
		public TerminalNode VIEW() { return getToken(MqlBaseParser.VIEW, 0); }
		public TableIdentifierContext tableIdentifier() {
			return getRuleContext(TableIdentifierContext.class,0);
		}
		public TerminalNode IF() { return getToken(MqlBaseParser.IF, 0); }
		public TerminalNode EXISTS() { return getToken(MqlBaseParser.EXISTS, 0); }
		public DropViewContext(MqlContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterDropView(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitDropView(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitDropView(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class RenameEventContext extends MqlContext {
		public IdentifierContext name;
		public IdentifierContext newName;
		public TerminalNode RENAME() { return getToken(MqlBaseParser.RENAME, 0); }
		public TerminalNode EVENT() { return getToken(MqlBaseParser.EVENT, 0); }
		public TerminalNode TO() { return getToken(MqlBaseParser.TO, 0); }
		public List<IdentifierContext> identifier() {
			return getRuleContexts(IdentifierContext.class);
		}
		public IdentifierContext identifier(int i) {
			return getRuleContext(IdentifierContext.class,i);
		}
		public RenameEventContext(MqlContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterRenameEvent(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitRenameEvent(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitRenameEvent(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class MqlQueryContext extends MqlContext {
		public QueryContext query() {
			return getRuleContext(QueryContext.class,0);
		}
		public MqlQueryContext(MqlContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterMqlQuery(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitMqlQuery(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitMqlQuery(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class DropEventContext extends MqlContext {
		public IdentifierContext name;
		public TerminalNode DROP() { return getToken(MqlBaseParser.DROP, 0); }
		public TerminalNode EVENT() { return getToken(MqlBaseParser.EVENT, 0); }
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public TerminalNode IF() { return getToken(MqlBaseParser.IF, 0); }
		public TerminalNode EXISTS() { return getToken(MqlBaseParser.EXISTS, 0); }
		public DropEventContext(MqlContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterDropEvent(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitDropEvent(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitDropEvent(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class CreateTemporaryFunctionContext extends MqlContext {
		public CreateTemporaryFunctionCmdContext createTemporaryFunctionCmd() {
			return getRuleContext(CreateTemporaryFunctionCmdContext.class,0);
		}
		public CreateTemporaryFunctionContext(MqlContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterCreateTemporaryFunction(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitCreateTemporaryFunction(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitCreateTemporaryFunction(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class SetOrganizationCommentContext extends MqlContext {
		public IdentifierContext name;
		public Token comment;
		public TerminalNode ALTER() { return getToken(MqlBaseParser.ALTER, 0); }
		public TerminalNode SET() { return getToken(MqlBaseParser.SET, 0); }
		public TerminalNode COMMENT() { return getToken(MqlBaseParser.COMMENT, 0); }
		public TerminalNode ORG() { return getToken(MqlBaseParser.ORG, 0); }
		public TerminalNode ORGANIZATION() { return getToken(MqlBaseParser.ORGANIZATION, 0); }
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public TerminalNode STRING() { return getToken(MqlBaseParser.STRING, 0); }
		public SetOrganizationCommentContext(MqlContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterSetOrganizationComment(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitSetOrganizationComment(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitSetOrganizationComment(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class UseDatabaseContext extends MqlContext {
		public IdentifierContext db;
		public TerminalNode USE() { return getToken(MqlBaseParser.USE, 0); }
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public UseDatabaseContext(MqlContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterUseDatabase(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitUseDatabase(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitUseDatabase(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ShowGroupsContext extends MqlContext {
		public Token pattern;
		public TerminalNode SHOW() { return getToken(MqlBaseParser.SHOW, 0); }
		public TerminalNode GROUPS() { return getToken(MqlBaseParser.GROUPS, 0); }
		public TerminalNode LIKE() { return getToken(MqlBaseParser.LIKE, 0); }
		public TerminalNode STRING() { return getToken(MqlBaseParser.STRING, 0); }
		public ShowGroupsContext(MqlContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterShowGroups(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitShowGroups(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitShowGroups(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class InsertIntoContext extends MqlContext {
		public InsertIntoCmdContext insertIntoCmd() {
			return getRuleContext(InsertIntoCmdContext.class,0);
		}
		public InsertIntoContext(MqlContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterInsertInto(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitInsertInto(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitInsertInto(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class SetOrganizationNameContext extends MqlContext {
		public IdentifierContext name;
		public IdentifierContext newName;
		public TerminalNode ALTER() { return getToken(MqlBaseParser.ALTER, 0); }
		public TerminalNode RENAME() { return getToken(MqlBaseParser.RENAME, 0); }
		public TerminalNode TO() { return getToken(MqlBaseParser.TO, 0); }
		public TerminalNode ORG() { return getToken(MqlBaseParser.ORG, 0); }
		public TerminalNode ORGANIZATION() { return getToken(MqlBaseParser.ORGANIZATION, 0); }
		public List<IdentifierContext> identifier() {
			return getRuleContexts(IdentifierContext.class);
		}
		public IdentifierContext identifier(int i) {
			return getRuleContext(IdentifierContext.class,i);
		}
		public SetOrganizationNameContext(MqlContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterSetOrganizationName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitSetOrganizationName(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitSetOrganizationName(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class GrantAccountToUsersContext extends MqlContext {
		public IdentifierListContext users;
		public TerminalNode GRANT() { return getToken(MqlBaseParser.GRANT, 0); }
		public TerminalNode ACCOUNT() { return getToken(MqlBaseParser.ACCOUNT, 0); }
		public TerminalNode TO() { return getToken(MqlBaseParser.TO, 0); }
		public TerminalNode USER() { return getToken(MqlBaseParser.USER, 0); }
		public IdentifierListContext identifierList() {
			return getRuleContext(IdentifierListContext.class,0);
		}
		public GrantAccountToUsersContext(MqlContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterGrantAccountToUsers(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitGrantAccountToUsers(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitGrantAccountToUsers(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class SetGroupNameContext extends MqlContext {
		public IdentifierContext name;
		public IdentifierContext newName;
		public TerminalNode ALTER() { return getToken(MqlBaseParser.ALTER, 0); }
		public TerminalNode GROUP() { return getToken(MqlBaseParser.GROUP, 0); }
		public TerminalNode RENAME() { return getToken(MqlBaseParser.RENAME, 0); }
		public TerminalNode TO() { return getToken(MqlBaseParser.TO, 0); }
		public List<IdentifierContext> identifier() {
			return getRuleContexts(IdentifierContext.class);
		}
		public IdentifierContext identifier(int i) {
			return getRuleContext(IdentifierContext.class,i);
		}
		public SetGroupNameContext(MqlContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterSetGroupName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitSetGroupName(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitSetGroupName(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class CreateDatabaseContext extends MqlContext {
		public IdentifierContext name;
		public Token comment;
		public TerminalNode CREATE() { return getToken(MqlBaseParser.CREATE, 0); }
		public TerminalNode DATABASE() { return getToken(MqlBaseParser.DATABASE, 0); }
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public TerminalNode IF() { return getToken(MqlBaseParser.IF, 0); }
		public TerminalNode NOT() { return getToken(MqlBaseParser.NOT, 0); }
		public TerminalNode EXISTS() { return getToken(MqlBaseParser.EXISTS, 0); }
		public TerminalNode COMMENT() { return getToken(MqlBaseParser.COMMENT, 0); }
		public TerminalNode STRING() { return getToken(MqlBaseParser.STRING, 0); }
		public CreateDatabaseContext(MqlContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterCreateDatabase(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitCreateDatabase(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitCreateDatabase(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class RevokeAccountFromUsersContext extends MqlContext {
		public IdentifierListContext users;
		public TerminalNode REVOKE() { return getToken(MqlBaseParser.REVOKE, 0); }
		public TerminalNode ACCOUNT() { return getToken(MqlBaseParser.ACCOUNT, 0); }
		public TerminalNode FROM() { return getToken(MqlBaseParser.FROM, 0); }
		public TerminalNode USER() { return getToken(MqlBaseParser.USER, 0); }
		public IdentifierListContext identifierList() {
			return getRuleContext(IdentifierListContext.class,0);
		}
		public RevokeAccountFromUsersContext(MqlContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterRevokeAccountFromUsers(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitRevokeAccountFromUsers(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitRevokeAccountFromUsers(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class MountTableWithDatasourceContext extends MqlContext {
		public IdentifierContext ds;
		public TerminalNode WITH() { return getToken(MqlBaseParser.WITH, 0); }
		public TerminalNode DATASOURCE() { return getToken(MqlBaseParser.DATASOURCE, 0); }
		public TerminalNode MOUNT() { return getToken(MqlBaseParser.MOUNT, 0); }
		public TerminalNode TABLE() { return getToken(MqlBaseParser.TABLE, 0); }
		public MountTableListContext mountTableList() {
			return getRuleContext(MountTableListContext.class,0);
		}
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public TerminalNode STREAM() { return getToken(MqlBaseParser.STREAM, 0); }
		public TerminalNode IF() { return getToken(MqlBaseParser.IF, 0); }
		public TerminalNode NOT() { return getToken(MqlBaseParser.NOT, 0); }
		public TerminalNode EXISTS() { return getToken(MqlBaseParser.EXISTS, 0); }
		public MountTableWithDatasourceContext(MqlContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterMountTableWithDatasource(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitMountTableWithDatasource(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitMountTableWithDatasource(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class GrantAccountToGroupsContext extends MqlContext {
		public IdentifierListContext groups;
		public TerminalNode GRANT() { return getToken(MqlBaseParser.GRANT, 0); }
		public TerminalNode ACCOUNT() { return getToken(MqlBaseParser.ACCOUNT, 0); }
		public TerminalNode TO() { return getToken(MqlBaseParser.TO, 0); }
		public TerminalNode GROUP() { return getToken(MqlBaseParser.GROUP, 0); }
		public IdentifierListContext identifierList() {
			return getRuleContext(IdentifierListContext.class,0);
		}
		public GrantAccountToGroupsContext(MqlContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterGrantAccountToGroups(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitGrantAccountToGroups(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitGrantAccountToGroups(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class RenameUserContext extends MqlContext {
		public IdentifierContext name;
		public IdentifierContext newName;
		public TerminalNode RENAME() { return getToken(MqlBaseParser.RENAME, 0); }
		public TerminalNode USER() { return getToken(MqlBaseParser.USER, 0); }
		public TerminalNode TO() { return getToken(MqlBaseParser.TO, 0); }
		public List<IdentifierContext> identifier() {
			return getRuleContexts(IdentifierContext.class);
		}
		public IdentifierContext identifier(int i) {
			return getRuleContext(IdentifierContext.class,i);
		}
		public RenameUserContext(MqlContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterRenameUser(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitRenameUser(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitRenameUser(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class GrantDdlToUsersContext extends MqlContext {
		public IdentifierListContext users;
		public TerminalNode GRANT() { return getToken(MqlBaseParser.GRANT, 0); }
		public TerminalNode DDL() { return getToken(MqlBaseParser.DDL, 0); }
		public TerminalNode TO() { return getToken(MqlBaseParser.TO, 0); }
		public TerminalNode USER() { return getToken(MqlBaseParser.USER, 0); }
		public IdentifierListContext identifierList() {
			return getRuleContext(IdentifierListContext.class,0);
		}
		public GrantDdlToUsersContext(MqlContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterGrantDdlToUsers(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitGrantDdlToUsers(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitGrantDdlToUsers(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class RenameApplicationContext extends MqlContext {
		public IdentifierContext name;
		public IdentifierContext newName;
		public TerminalNode RENAME() { return getToken(MqlBaseParser.RENAME, 0); }
		public TerminalNode APPLICATION() { return getToken(MqlBaseParser.APPLICATION, 0); }
		public TerminalNode TO() { return getToken(MqlBaseParser.TO, 0); }
		public List<IdentifierContext> identifier() {
			return getRuleContexts(IdentifierContext.class);
		}
		public IdentifierContext identifier(int i) {
			return getRuleContext(IdentifierContext.class,i);
		}
		public RenameApplicationContext(MqlContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterRenameApplication(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitRenameApplication(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitRenameApplication(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class RemoveUsersFromGroupContext extends MqlContext {
		public IdentifierContext name;
		public TerminalNode ALTER() { return getToken(MqlBaseParser.ALTER, 0); }
		public TerminalNode GROUP() { return getToken(MqlBaseParser.GROUP, 0); }
		public RemoveUserContext removeUser() {
			return getRuleContext(RemoveUserContext.class,0);
		}
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public AddUserContext addUser() {
			return getRuleContext(AddUserContext.class,0);
		}
		public RemoveUsersFromGroupContext(MqlContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterRemoveUsersFromGroup(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitRemoveUsersFromGroup(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitRemoveUsersFromGroup(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ChangeTableColumnContext extends MqlContext {
		public TableIdentifierContext name;
		public TerminalNode ALTER() { return getToken(MqlBaseParser.ALTER, 0); }
		public TerminalNode TABLE() { return getToken(MqlBaseParser.TABLE, 0); }
		public TerminalNode CHANGE() { return getToken(MqlBaseParser.CHANGE, 0); }
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public ColTypeContext colType() {
			return getRuleContext(ColTypeContext.class,0);
		}
		public TableIdentifierContext tableIdentifier() {
			return getRuleContext(TableIdentifierContext.class,0);
		}
		public TerminalNode COLUMN() { return getToken(MqlBaseParser.COLUMN, 0); }
		public ChangeTableColumnContext(MqlContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterChangeTableColumn(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitChangeTableColumn(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitChangeTableColumn(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class CreateOrganizationContext extends MqlContext {
		public IdentifierContext name;
		public Token comment;
		public TerminalNode CREATE() { return getToken(MqlBaseParser.CREATE, 0); }
		public TerminalNode ORG() { return getToken(MqlBaseParser.ORG, 0); }
		public TerminalNode ORGANIZATION() { return getToken(MqlBaseParser.ORGANIZATION, 0); }
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public TerminalNode IF() { return getToken(MqlBaseParser.IF, 0); }
		public TerminalNode NOT() { return getToken(MqlBaseParser.NOT, 0); }
		public TerminalNode EXISTS() { return getToken(MqlBaseParser.EXISTS, 0); }
		public TerminalNode COMMENT() { return getToken(MqlBaseParser.COMMENT, 0); }
		public TerminalNode STRING() { return getToken(MqlBaseParser.STRING, 0); }
		public CreateOrganizationContext(MqlContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterCreateOrganization(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitCreateOrganization(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitCreateOrganization(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class CreateViewContext extends MqlContext {
		public TableIdentifierContext name;
		public Token comment;
		public TerminalNode CREATE() { return getToken(MqlBaseParser.CREATE, 0); }
		public TerminalNode VIEW() { return getToken(MqlBaseParser.VIEW, 0); }
		public TerminalNode AS() { return getToken(MqlBaseParser.AS, 0); }
		public QueryContext query() {
			return getRuleContext(QueryContext.class,0);
		}
		public TableIdentifierContext tableIdentifier() {
			return getRuleContext(TableIdentifierContext.class,0);
		}
		public TerminalNode IF() { return getToken(MqlBaseParser.IF, 0); }
		public TerminalNode NOT() { return getToken(MqlBaseParser.NOT, 0); }
		public TerminalNode EXISTS() { return getToken(MqlBaseParser.EXISTS, 0); }
		public TerminalNode COMMENT() { return getToken(MqlBaseParser.COMMENT, 0); }
		public TerminalNode STRING() { return getToken(MqlBaseParser.STRING, 0); }
		public CreateViewContext(MqlContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterCreateView(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitCreateView(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitCreateView(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class DescUserContext extends MqlContext {
		public IdentifierContext name;
		public TerminalNode USER() { return getToken(MqlBaseParser.USER, 0); }
		public TerminalNode DESC() { return getToken(MqlBaseParser.DESC, 0); }
		public TerminalNode DESCRIBE() { return getToken(MqlBaseParser.DESCRIBE, 0); }
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public DescUserContext(MqlContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterDescUser(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitDescUser(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitDescUser(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class DropUserContext extends MqlContext {
		public IdentifierContext name;
		public TerminalNode DROP() { return getToken(MqlBaseParser.DROP, 0); }
		public TerminalNode USER() { return getToken(MqlBaseParser.USER, 0); }
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public TerminalNode IF() { return getToken(MqlBaseParser.IF, 0); }
		public TerminalNode EXISTS() { return getToken(MqlBaseParser.EXISTS, 0); }
		public DropUserContext(MqlContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterDropUser(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitDropUser(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitDropUser(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class DescGroupContext extends MqlContext {
		public IdentifierContext name;
		public TerminalNode GROUP() { return getToken(MqlBaseParser.GROUP, 0); }
		public TerminalNode DESC() { return getToken(MqlBaseParser.DESC, 0); }
		public TerminalNode DESCRIBE() { return getToken(MqlBaseParser.DESCRIBE, 0); }
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public DescGroupContext(MqlContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterDescGroup(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitDescGroup(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitDescGroup(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class RenameViewContext extends MqlContext {
		public TableIdentifierContext name;
		public TableIdentifierContext newName;
		public TerminalNode RENAME() { return getToken(MqlBaseParser.RENAME, 0); }
		public TerminalNode VIEW() { return getToken(MqlBaseParser.VIEW, 0); }
		public TerminalNode TO() { return getToken(MqlBaseParser.TO, 0); }
		public List<TableIdentifierContext> tableIdentifier() {
			return getRuleContexts(TableIdentifierContext.class);
		}
		public TableIdentifierContext tableIdentifier(int i) {
			return getRuleContext(TableIdentifierContext.class,i);
		}
		public RenameViewContext(MqlContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterRenameView(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitRenameView(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitRenameView(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class RevokeDdlFromUsersContext extends MqlContext {
		public IdentifierListContext users;
		public TerminalNode REVOKE() { return getToken(MqlBaseParser.REVOKE, 0); }
		public TerminalNode DDL() { return getToken(MqlBaseParser.DDL, 0); }
		public TerminalNode FROM() { return getToken(MqlBaseParser.FROM, 0); }
		public TerminalNode USER() { return getToken(MqlBaseParser.USER, 0); }
		public IdentifierListContext identifierList() {
			return getRuleContext(IdentifierListContext.class,0);
		}
		public RevokeDdlFromUsersContext(MqlContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterRevokeDdlFromUsers(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitRevokeDdlFromUsers(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitRevokeDdlFromUsers(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class RenameOrganizationContext extends MqlContext {
		public IdentifierContext name;
		public IdentifierContext newName;
		public TerminalNode RENAME() { return getToken(MqlBaseParser.RENAME, 0); }
		public TerminalNode TO() { return getToken(MqlBaseParser.TO, 0); }
		public TerminalNode ORG() { return getToken(MqlBaseParser.ORG, 0); }
		public TerminalNode ORGANIZATION() { return getToken(MqlBaseParser.ORGANIZATION, 0); }
		public List<IdentifierContext> identifier() {
			return getRuleContexts(IdentifierContext.class);
		}
		public IdentifierContext identifier(int i) {
			return getRuleContext(IdentifierContext.class,i);
		}
		public RenameOrganizationContext(MqlContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterRenameOrganization(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitRenameOrganization(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitRenameOrganization(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class RevokeGrantFromUserContext extends MqlContext {
		public IdentifierListContext users;
		public TerminalNode REVOKE() { return getToken(MqlBaseParser.REVOKE, 0); }
		public TerminalNode GRANT() { return getToken(MqlBaseParser.GRANT, 0); }
		public TerminalNode OPTION() { return getToken(MqlBaseParser.OPTION, 0); }
		public PrivilegeListContext privilegeList() {
			return getRuleContext(PrivilegeListContext.class,0);
		}
		public TerminalNode FROM() { return getToken(MqlBaseParser.FROM, 0); }
		public TerminalNode USER() { return getToken(MqlBaseParser.USER, 0); }
		public IdentifierListContext identifierList() {
			return getRuleContext(IdentifierListContext.class,0);
		}
		public RevokeGrantFromUserContext(MqlContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterRevokeGrantFromUser(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitRevokeGrantFromUser(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitRevokeGrantFromUser(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class DescDatasourceContext extends MqlContext {
		public IdentifierContext name;
		public TerminalNode DATASOURCE() { return getToken(MqlBaseParser.DATASOURCE, 0); }
		public TerminalNode DESC() { return getToken(MqlBaseParser.DESC, 0); }
		public TerminalNode DESCRIBE() { return getToken(MqlBaseParser.DESCRIBE, 0); }
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public TerminalNode EXTENDED() { return getToken(MqlBaseParser.EXTENDED, 0); }
		public DescDatasourceContext(MqlContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterDescDatasource(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitDescDatasource(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitDescDatasource(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class GrantDmlOnToGroupsContext extends MqlContext {
		public IdentifierListContext groups;
		public TerminalNode GRANT() { return getToken(MqlBaseParser.GRANT, 0); }
		public TerminalNode DML() { return getToken(MqlBaseParser.DML, 0); }
		public TerminalNode ON() { return getToken(MqlBaseParser.ON, 0); }
		public QualifiedColumnListContext qualifiedColumnList() {
			return getRuleContext(QualifiedColumnListContext.class,0);
		}
		public TerminalNode TO() { return getToken(MqlBaseParser.TO, 0); }
		public TerminalNode GROUP() { return getToken(MqlBaseParser.GROUP, 0); }
		public IdentifierListContext identifierList() {
			return getRuleContext(IdentifierListContext.class,0);
		}
		public GrantDmlOnToGroupsContext(MqlContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterGrantDmlOnToGroups(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitGrantDmlOnToGroups(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitGrantDmlOnToGroups(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class SetGroupCommentContext extends MqlContext {
		public IdentifierContext name;
		public Token comment;
		public TerminalNode ALTER() { return getToken(MqlBaseParser.ALTER, 0); }
		public TerminalNode GROUP() { return getToken(MqlBaseParser.GROUP, 0); }
		public TerminalNode SET() { return getToken(MqlBaseParser.SET, 0); }
		public TerminalNode COMMENT() { return getToken(MqlBaseParser.COMMENT, 0); }
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public TerminalNode STRING() { return getToken(MqlBaseParser.STRING, 0); }
		public SetGroupCommentContext(MqlContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterSetGroupComment(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitSetGroupComment(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitSetGroupComment(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class UnmountTableContext extends MqlContext {
		public TableIdentifierContext name;
		public TerminalNode UNMOUNT() { return getToken(MqlBaseParser.UNMOUNT, 0); }
		public TerminalNode TABLE() { return getToken(MqlBaseParser.TABLE, 0); }
		public TableIdentifierContext tableIdentifier() {
			return getRuleContext(TableIdentifierContext.class,0);
		}
		public TerminalNode IF() { return getToken(MqlBaseParser.IF, 0); }
		public TerminalNode EXISTS() { return getToken(MqlBaseParser.EXISTS, 0); }
		public UnmountTableContext(MqlContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterUnmountTable(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitUnmountTable(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitUnmountTable(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class DescViewContext extends MqlContext {
		public TerminalNode VIEW() { return getToken(MqlBaseParser.VIEW, 0); }
		public TableIdentifierContext tableIdentifier() {
			return getRuleContext(TableIdentifierContext.class,0);
		}
		public TerminalNode DESC() { return getToken(MqlBaseParser.DESC, 0); }
		public TerminalNode DESCRIBE() { return getToken(MqlBaseParser.DESCRIBE, 0); }
		public DescViewContext(MqlContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterDescView(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitDescView(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitDescView(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class RenameSaContext extends MqlContext {
		public IdentifierContext name;
		public IdentifierContext newName;
		public IdentifierContext org;
		public TerminalNode RENAME() { return getToken(MqlBaseParser.RENAME, 0); }
		public TerminalNode SA() { return getToken(MqlBaseParser.SA, 0); }
		public TerminalNode TO() { return getToken(MqlBaseParser.TO, 0); }
		public TerminalNode IN() { return getToken(MqlBaseParser.IN, 0); }
		public List<IdentifierContext> identifier() {
			return getRuleContexts(IdentifierContext.class);
		}
		public IdentifierContext identifier(int i) {
			return getRuleContext(IdentifierContext.class,i);
		}
		public TerminalNode ORG() { return getToken(MqlBaseParser.ORG, 0); }
		public TerminalNode ORGANIZATION() { return getToken(MqlBaseParser.ORGANIZATION, 0); }
		public RenameSaContext(MqlContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterRenameSa(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitRenameSa(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitRenameSa(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class CreateApplicationContext extends MqlContext {
		public IdentifierContext name;
		public TerminalNode CREATE() { return getToken(MqlBaseParser.CREATE, 0); }
		public TerminalNode APPLICATION() { return getToken(MqlBaseParser.APPLICATION, 0); }
		public TerminalNode AS() { return getToken(MqlBaseParser.AS, 0); }
		public AppCmdsContext appCmds() {
			return getRuleContext(AppCmdsContext.class,0);
		}
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public TerminalNode IF() { return getToken(MqlBaseParser.IF, 0); }
		public TerminalNode NOT() { return getToken(MqlBaseParser.NOT, 0); }
		public TerminalNode EXISTS() { return getToken(MqlBaseParser.EXISTS, 0); }
		public CreateApplicationContext(MqlContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterCreateApplication(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitCreateApplication(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitCreateApplication(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class SetViewCommentContext extends MqlContext {
		public TableIdentifierContext name;
		public Token comment;
		public TerminalNode ALTER() { return getToken(MqlBaseParser.ALTER, 0); }
		public TerminalNode VIEW() { return getToken(MqlBaseParser.VIEW, 0); }
		public TerminalNode SET() { return getToken(MqlBaseParser.SET, 0); }
		public TerminalNode COMMENT() { return getToken(MqlBaseParser.COMMENT, 0); }
		public TableIdentifierContext tableIdentifier() {
			return getRuleContext(TableIdentifierContext.class,0);
		}
		public TerminalNode STRING() { return getToken(MqlBaseParser.STRING, 0); }
		public SetViewCommentContext(MqlContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterSetViewComment(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitSetViewComment(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitSetViewComment(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ShowViewsContext extends MqlContext {
		public IdentifierContext db;
		public Token pattern;
		public TerminalNode SHOW() { return getToken(MqlBaseParser.SHOW, 0); }
		public TerminalNode VIEWS() { return getToken(MqlBaseParser.VIEWS, 0); }
		public TerminalNode LIKE() { return getToken(MqlBaseParser.LIKE, 0); }
		public TerminalNode FROM() { return getToken(MqlBaseParser.FROM, 0); }
		public TerminalNode IN() { return getToken(MqlBaseParser.IN, 0); }
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public TerminalNode STRING() { return getToken(MqlBaseParser.STRING, 0); }
		public ShowViewsContext(MqlContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterShowViews(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitShowViews(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitShowViews(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ShowDatabaseContext extends MqlContext {
		public Token pattern;
		public TerminalNode SHOW() { return getToken(MqlBaseParser.SHOW, 0); }
		public TerminalNode DATABASES() { return getToken(MqlBaseParser.DATABASES, 0); }
		public TerminalNode LIKE() { return getToken(MqlBaseParser.LIKE, 0); }
		public TerminalNode STRING() { return getToken(MqlBaseParser.STRING, 0); }
		public ShowDatabaseContext(MqlContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterShowDatabase(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitShowDatabase(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitShowDatabase(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class CreateUserContext extends MqlContext {
		public IdentifierContext name;
		public PasswordContext pwd;
		public TerminalNode CREATE() { return getToken(MqlBaseParser.CREATE, 0); }
		public TerminalNode USER() { return getToken(MqlBaseParser.USER, 0); }
		public TerminalNode IDENTIFIED() { return getToken(MqlBaseParser.IDENTIFIED, 0); }
		public TerminalNode BY() { return getToken(MqlBaseParser.BY, 0); }
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public PasswordContext password() {
			return getRuleContext(PasswordContext.class,0);
		}
		public TerminalNode IF() { return getToken(MqlBaseParser.IF, 0); }
		public TerminalNode NOT() { return getToken(MqlBaseParser.NOT, 0); }
		public TerminalNode EXISTS() { return getToken(MqlBaseParser.EXISTS, 0); }
		public CreateUserContext(MqlContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterCreateUser(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitCreateUser(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitCreateUser(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ShowFunctionsContext extends MqlContext {
		public IdentifierContext db;
		public Token pattern;
		public TerminalNode SHOW() { return getToken(MqlBaseParser.SHOW, 0); }
		public TerminalNode FUNCTIONS() { return getToken(MqlBaseParser.FUNCTIONS, 0); }
		public TerminalNode LIKE() { return getToken(MqlBaseParser.LIKE, 0); }
		public TerminalNode FROM() { return getToken(MqlBaseParser.FROM, 0); }
		public TerminalNode IN() { return getToken(MqlBaseParser.IN, 0); }
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public TerminalNode STRING() { return getToken(MqlBaseParser.STRING, 0); }
		public ShowFunctionsContext(MqlContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterShowFunctions(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitShowFunctions(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitShowFunctions(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class SetTablePropertiesContext extends MqlContext {
		public TableIdentifierContext name;
		public TerminalNode ALTER() { return getToken(MqlBaseParser.ALTER, 0); }
		public TerminalNode TABLE() { return getToken(MqlBaseParser.TABLE, 0); }
		public TerminalNode SET() { return getToken(MqlBaseParser.SET, 0); }
		public TerminalNode OPTIONS() { return getToken(MqlBaseParser.OPTIONS, 0); }
		public PropertyListContext propertyList() {
			return getRuleContext(PropertyListContext.class,0);
		}
		public TableIdentifierContext tableIdentifier() {
			return getRuleContext(TableIdentifierContext.class,0);
		}
		public SetTablePropertiesContext(MqlContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterSetTableProperties(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitSetTableProperties(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitSetTableProperties(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class DropSaContext extends MqlContext {
		public IdentifierContext name;
		public IdentifierContext org;
		public TerminalNode DROP() { return getToken(MqlBaseParser.DROP, 0); }
		public TerminalNode SA() { return getToken(MqlBaseParser.SA, 0); }
		public TerminalNode IN() { return getToken(MqlBaseParser.IN, 0); }
		public List<IdentifierContext> identifier() {
			return getRuleContexts(IdentifierContext.class);
		}
		public IdentifierContext identifier(int i) {
			return getRuleContext(IdentifierContext.class,i);
		}
		public TerminalNode ORG() { return getToken(MqlBaseParser.ORG, 0); }
		public TerminalNode ORGANIZATION() { return getToken(MqlBaseParser.ORGANIZATION, 0); }
		public TerminalNode IF() { return getToken(MqlBaseParser.IF, 0); }
		public TerminalNode EXISTS() { return getToken(MqlBaseParser.EXISTS, 0); }
		public DropSaContext(MqlContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterDropSa(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitDropSa(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitDropSa(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class SetViewNameContext extends MqlContext {
		public TableIdentifierContext name;
		public TableIdentifierContext newName;
		public TerminalNode ALTER() { return getToken(MqlBaseParser.ALTER, 0); }
		public TerminalNode VIEW() { return getToken(MqlBaseParser.VIEW, 0); }
		public TerminalNode RENAME() { return getToken(MqlBaseParser.RENAME, 0); }
		public TerminalNode TO() { return getToken(MqlBaseParser.TO, 0); }
		public List<TableIdentifierContext> tableIdentifier() {
			return getRuleContexts(TableIdentifierContext.class);
		}
		public TableIdentifierContext tableIdentifier(int i) {
			return getRuleContext(TableIdentifierContext.class,i);
		}
		public SetViewNameContext(MqlContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterSetViewName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitSetViewName(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitSetViewName(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class UnmountDatasourceContext extends MqlContext {
		public IdentifierContext name;
		public TerminalNode UNMOUNT() { return getToken(MqlBaseParser.UNMOUNT, 0); }
		public TerminalNode DATASOURCE() { return getToken(MqlBaseParser.DATASOURCE, 0); }
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public TerminalNode IF() { return getToken(MqlBaseParser.IF, 0); }
		public TerminalNode EXISTS() { return getToken(MqlBaseParser.EXISTS, 0); }
		public UnmountDatasourceContext(MqlContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterUnmountDatasource(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitUnmountDatasource(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitUnmountDatasource(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class GrantDmlOnToUsersContext extends MqlContext {
		public IdentifierListContext users;
		public TerminalNode GRANT() { return getToken(MqlBaseParser.GRANT, 0); }
		public TerminalNode DML() { return getToken(MqlBaseParser.DML, 0); }
		public TerminalNode ON() { return getToken(MqlBaseParser.ON, 0); }
		public QualifiedColumnListContext qualifiedColumnList() {
			return getRuleContext(QualifiedColumnListContext.class,0);
		}
		public TerminalNode TO() { return getToken(MqlBaseParser.TO, 0); }
		public TerminalNode USER() { return getToken(MqlBaseParser.USER, 0); }
		public IdentifierListContext identifierList() {
			return getRuleContext(IdentifierListContext.class,0);
		}
		public GrantDmlOnToUsersContext(MqlContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterGrantDmlOnToUsers(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitGrantDmlOnToUsers(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitGrantDmlOnToUsers(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class AddUsersToGroupContext extends MqlContext {
		public IdentifierContext name;
		public TerminalNode ALTER() { return getToken(MqlBaseParser.ALTER, 0); }
		public TerminalNode GROUP() { return getToken(MqlBaseParser.GROUP, 0); }
		public AddUserContext addUser() {
			return getRuleContext(AddUserContext.class,0);
		}
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public RemoveUserContext removeUser() {
			return getRuleContext(RemoveUserContext.class,0);
		}
		public AddUsersToGroupContext(MqlContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterAddUsersToGroup(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitAddUsersToGroup(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitAddUsersToGroup(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class RenameTableContext extends MqlContext {
		public TableIdentifierContext name;
		public TableIdentifierContext newName;
		public TerminalNode RENAME() { return getToken(MqlBaseParser.RENAME, 0); }
		public TerminalNode TABLE() { return getToken(MqlBaseParser.TABLE, 0); }
		public TerminalNode TO() { return getToken(MqlBaseParser.TO, 0); }
		public List<TableIdentifierContext> tableIdentifier() {
			return getRuleContexts(TableIdentifierContext.class);
		}
		public TableIdentifierContext tableIdentifier(int i) {
			return getRuleContext(TableIdentifierContext.class,i);
		}
		public RenameTableContext(MqlContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterRenameTable(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitRenameTable(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitRenameTable(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class MountDatasourceContext extends MqlContext {
		public IdentifierContext name;
		public TerminalNode MOUNT() { return getToken(MqlBaseParser.MOUNT, 0); }
		public TerminalNode DATASOURCE() { return getToken(MqlBaseParser.DATASOURCE, 0); }
		public TerminalNode OPTIONS() { return getToken(MqlBaseParser.OPTIONS, 0); }
		public PropertyListContext propertyList() {
			return getRuleContext(PropertyListContext.class,0);
		}
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public TerminalNode IF() { return getToken(MqlBaseParser.IF, 0); }
		public TerminalNode NOT() { return getToken(MqlBaseParser.NOT, 0); }
		public TerminalNode EXISTS() { return getToken(MqlBaseParser.EXISTS, 0); }
		public MountDatasourceContext(MqlContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterMountDatasource(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitMountDatasource(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitMountDatasource(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ShowTablesContext extends MqlContext {
		public IdentifierContext db;
		public Token pattern;
		public TerminalNode SHOW() { return getToken(MqlBaseParser.SHOW, 0); }
		public TerminalNode TABLES() { return getToken(MqlBaseParser.TABLES, 0); }
		public TerminalNode LIKE() { return getToken(MqlBaseParser.LIKE, 0); }
		public TerminalNode FROM() { return getToken(MqlBaseParser.FROM, 0); }
		public TerminalNode IN() { return getToken(MqlBaseParser.IN, 0); }
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public TerminalNode STRING() { return getToken(MqlBaseParser.STRING, 0); }
		public ShowTablesContext(MqlContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterShowTables(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitShowTables(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitShowTables(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class SetDatasourcePropertiesContext extends MqlContext {
		public IdentifierContext name;
		public TerminalNode ALTER() { return getToken(MqlBaseParser.ALTER, 0); }
		public TerminalNode DATASOURCE() { return getToken(MqlBaseParser.DATASOURCE, 0); }
		public TerminalNode SET() { return getToken(MqlBaseParser.SET, 0); }
		public TerminalNode OPTIONS() { return getToken(MqlBaseParser.OPTIONS, 0); }
		public PropertyListContext propertyList() {
			return getRuleContext(PropertyListContext.class,0);
		}
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public SetDatasourcePropertiesContext(MqlContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterSetDatasourceProperties(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitSetDatasourceProperties(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitSetDatasourceProperties(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class GrantGrantToUserContext extends MqlContext {
		public IdentifierListContext users;
		public List<TerminalNode> GRANT() { return getTokens(MqlBaseParser.GRANT); }
		public TerminalNode GRANT(int i) {
			return getToken(MqlBaseParser.GRANT, i);
		}
		public TerminalNode OPTION() { return getToken(MqlBaseParser.OPTION, 0); }
		public PrivilegeListContext privilegeList() {
			return getRuleContext(PrivilegeListContext.class,0);
		}
		public TerminalNode TO() { return getToken(MqlBaseParser.TO, 0); }
		public TerminalNode USER() { return getToken(MqlBaseParser.USER, 0); }
		public IdentifierListContext identifierList() {
			return getRuleContext(IdentifierListContext.class,0);
		}
		public GrantGrantToUserContext(MqlContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterGrantGrantToUser(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitGrantGrantToUser(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitGrantGrantToUser(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class GrantDdlToGroupsContext extends MqlContext {
		public IdentifierListContext groups;
		public TerminalNode GRANT() { return getToken(MqlBaseParser.GRANT, 0); }
		public TerminalNode DDL() { return getToken(MqlBaseParser.DDL, 0); }
		public TerminalNode TO() { return getToken(MqlBaseParser.TO, 0); }
		public TerminalNode GROUP() { return getToken(MqlBaseParser.GROUP, 0); }
		public IdentifierListContext identifierList() {
			return getRuleContext(IdentifierListContext.class,0);
		}
		public GrantDdlToGroupsContext(MqlContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterGrantDdlToGroups(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitGrantDdlToGroups(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitGrantDdlToGroups(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class SetEventNameContext extends MqlContext {
		public IdentifierContext name;
		public IdentifierContext newName;
		public TerminalNode ALTER() { return getToken(MqlBaseParser.ALTER, 0); }
		public TerminalNode EVENT() { return getToken(MqlBaseParser.EVENT, 0); }
		public TerminalNode RENAME() { return getToken(MqlBaseParser.RENAME, 0); }
		public TerminalNode TO() { return getToken(MqlBaseParser.TO, 0); }
		public List<IdentifierContext> identifier() {
			return getRuleContexts(IdentifierContext.class);
		}
		public IdentifierContext identifier(int i) {
			return getRuleContext(IdentifierContext.class,i);
		}
		public SetEventNameContext(MqlContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterSetEventName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitSetEventName(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitSetEventName(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class CreateGroupContext extends MqlContext {
		public IdentifierContext name;
		public Token comment;
		public TerminalNode CREATE() { return getToken(MqlBaseParser.CREATE, 0); }
		public TerminalNode GROUP() { return getToken(MqlBaseParser.GROUP, 0); }
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public TerminalNode IF() { return getToken(MqlBaseParser.IF, 0); }
		public TerminalNode NOT() { return getToken(MqlBaseParser.NOT, 0); }
		public TerminalNode EXISTS() { return getToken(MqlBaseParser.EXISTS, 0); }
		public TerminalNode COMMENT() { return getToken(MqlBaseParser.COMMENT, 0); }
		public TerminalNode STRING() { return getToken(MqlBaseParser.STRING, 0); }
		public CreateGroupContext(MqlContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterCreateGroup(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitCreateGroup(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitCreateGroup(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ShowApplicationsContext extends MqlContext {
		public Token pattern;
		public TerminalNode SHOW() { return getToken(MqlBaseParser.SHOW, 0); }
		public TerminalNode APPLICATIONS() { return getToken(MqlBaseParser.APPLICATIONS, 0); }
		public TerminalNode LIKE() { return getToken(MqlBaseParser.LIKE, 0); }
		public TerminalNode STRING() { return getToken(MqlBaseParser.STRING, 0); }
		public ShowApplicationsContext(MqlContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterShowApplications(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitShowApplications(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitShowApplications(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class SetEventEnableContext extends MqlContext {
		public IdentifierContext name;
		public TerminalNode ALTER() { return getToken(MqlBaseParser.ALTER, 0); }
		public TerminalNode EVENT() { return getToken(MqlBaseParser.EVENT, 0); }
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public TerminalNode ENABLE() { return getToken(MqlBaseParser.ENABLE, 0); }
		public TerminalNode DISABLE() { return getToken(MqlBaseParser.DISABLE, 0); }
		public SetEventEnableContext(MqlContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterSetEventEnable(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitSetEventEnable(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitSetEventEnable(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class RevokeDmlOnFromGroupsContext extends MqlContext {
		public IdentifierListContext groups;
		public TerminalNode REVOKE() { return getToken(MqlBaseParser.REVOKE, 0); }
		public TerminalNode DML() { return getToken(MqlBaseParser.DML, 0); }
		public TerminalNode ON() { return getToken(MqlBaseParser.ON, 0); }
		public QualifiedColumnListContext qualifiedColumnList() {
			return getRuleContext(QualifiedColumnListContext.class,0);
		}
		public TerminalNode FROM() { return getToken(MqlBaseParser.FROM, 0); }
		public TerminalNode GROUP() { return getToken(MqlBaseParser.GROUP, 0); }
		public IdentifierListContext identifierList() {
			return getRuleContext(IdentifierListContext.class,0);
		}
		public RevokeDmlOnFromGroupsContext(MqlContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterRevokeDmlOnFromGroups(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitRevokeDmlOnFromGroups(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitRevokeDmlOnFromGroups(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class SetFunctionNameContext extends MqlContext {
		public FuncIdentifierContext name;
		public FuncIdentifierContext newName;
		public TerminalNode ALTER() { return getToken(MqlBaseParser.ALTER, 0); }
		public TerminalNode FUNCTION() { return getToken(MqlBaseParser.FUNCTION, 0); }
		public TerminalNode RENAME() { return getToken(MqlBaseParser.RENAME, 0); }
		public TerminalNode TO() { return getToken(MqlBaseParser.TO, 0); }
		public List<FuncIdentifierContext> funcIdentifier() {
			return getRuleContexts(FuncIdentifierContext.class);
		}
		public FuncIdentifierContext funcIdentifier(int i) {
			return getRuleContext(FuncIdentifierContext.class,i);
		}
		public SetFunctionNameContext(MqlContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterSetFunctionName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitSetFunctionName(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitSetFunctionName(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class RevokeDmlOnFromUsersContext extends MqlContext {
		public IdentifierListContext users;
		public TerminalNode REVOKE() { return getToken(MqlBaseParser.REVOKE, 0); }
		public TerminalNode DML() { return getToken(MqlBaseParser.DML, 0); }
		public TerminalNode ON() { return getToken(MqlBaseParser.ON, 0); }
		public QualifiedColumnListContext qualifiedColumnList() {
			return getRuleContext(QualifiedColumnListContext.class,0);
		}
		public TerminalNode FROM() { return getToken(MqlBaseParser.FROM, 0); }
		public TerminalNode USER() { return getToken(MqlBaseParser.USER, 0); }
		public IdentifierListContext identifierList() {
			return getRuleContext(IdentifierListContext.class,0);
		}
		public RevokeDmlOnFromUsersContext(MqlContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterRevokeDmlOnFromUsers(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitRevokeDmlOnFromUsers(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitRevokeDmlOnFromUsers(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class MountTableContext extends MqlContext {
		public ColTypeListContext columns;
		public TerminalNode MOUNT() { return getToken(MqlBaseParser.MOUNT, 0); }
		public TerminalNode TABLE() { return getToken(MqlBaseParser.TABLE, 0); }
		public TableIdentifierContext tableIdentifier() {
			return getRuleContext(TableIdentifierContext.class,0);
		}
		public TerminalNode OPTIONS() { return getToken(MqlBaseParser.OPTIONS, 0); }
		public PropertyListContext propertyList() {
			return getRuleContext(PropertyListContext.class,0);
		}
		public TerminalNode STREAM() { return getToken(MqlBaseParser.STREAM, 0); }
		public TerminalNode IF() { return getToken(MqlBaseParser.IF, 0); }
		public TerminalNode NOT() { return getToken(MqlBaseParser.NOT, 0); }
		public TerminalNode EXISTS() { return getToken(MqlBaseParser.EXISTS, 0); }
		public ColTypeListContext colTypeList() {
			return getRuleContext(ColTypeListContext.class,0);
		}
		public MountTableContext(MqlContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterMountTable(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitMountTable(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitMountTable(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class DescFunctionContext extends MqlContext {
		public TerminalNode FUNCTION() { return getToken(MqlBaseParser.FUNCTION, 0); }
		public FuncIdentifierContext funcIdentifier() {
			return getRuleContext(FuncIdentifierContext.class,0);
		}
		public TerminalNode DESC() { return getToken(MqlBaseParser.DESC, 0); }
		public TerminalNode DESCRIBE() { return getToken(MqlBaseParser.DESCRIBE, 0); }
		public TerminalNode EXTENDED() { return getToken(MqlBaseParser.EXTENDED, 0); }
		public DescFunctionContext(MqlContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterDescFunction(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitDescFunction(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitDescFunction(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class InsertOverwriteContext extends MqlContext {
		public InsertOverwriteCmdContext insertOverwriteCmd() {
			return getRuleContext(InsertOverwriteCmdContext.class,0);
		}
		public InsertOverwriteContext(MqlContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterInsertOverwrite(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitInsertOverwrite(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitInsertOverwrite(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class RenameDatasourceContext extends MqlContext {
		public IdentifierContext name;
		public IdentifierContext newName;
		public TerminalNode RENAME() { return getToken(MqlBaseParser.RENAME, 0); }
		public TerminalNode DATASOURCE() { return getToken(MqlBaseParser.DATASOURCE, 0); }
		public TerminalNode TO() { return getToken(MqlBaseParser.TO, 0); }
		public List<IdentifierContext> identifier() {
			return getRuleContexts(IdentifierContext.class);
		}
		public IdentifierContext identifier(int i) {
			return getRuleContext(IdentifierContext.class,i);
		}
		public RenameDatasourceContext(MqlContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterRenameDatasource(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitRenameDatasource(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitRenameDatasource(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class CreateFunctionContext extends MqlContext {
		public FuncIdentifierContext name;
		public TerminalNode CREATE() { return getToken(MqlBaseParser.CREATE, 0); }
		public TerminalNode FUNCTION() { return getToken(MqlBaseParser.FUNCTION, 0); }
		public TerminalNode OPTIONS() { return getToken(MqlBaseParser.OPTIONS, 0); }
		public PropertyListContext propertyList() {
			return getRuleContext(PropertyListContext.class,0);
		}
		public FuncIdentifierContext funcIdentifier() {
			return getRuleContext(FuncIdentifierContext.class,0);
		}
		public TerminalNode IF() { return getToken(MqlBaseParser.IF, 0); }
		public TerminalNode NOT() { return getToken(MqlBaseParser.NOT, 0); }
		public TerminalNode EXISTS() { return getToken(MqlBaseParser.EXISTS, 0); }
		public CreateFunctionContext(MqlContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterCreateFunction(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitCreateFunction(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitCreateFunction(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class SetApplicationQuerysContext extends MqlContext {
		public IdentifierContext name;
		public TerminalNode ALTER() { return getToken(MqlBaseParser.ALTER, 0); }
		public TerminalNode APPLICATION() { return getToken(MqlBaseParser.APPLICATION, 0); }
		public TerminalNode AS() { return getToken(MqlBaseParser.AS, 0); }
		public AppCmdsContext appCmds() {
			return getRuleContext(AppCmdsContext.class,0);
		}
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public SetApplicationQuerysContext(MqlContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterSetApplicationQuerys(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitSetApplicationQuerys(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitSetApplicationQuerys(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class DescDatabaseContext extends MqlContext {
		public IdentifierContext name;
		public TerminalNode DATABASE() { return getToken(MqlBaseParser.DATABASE, 0); }
		public TerminalNode DESC() { return getToken(MqlBaseParser.DESC, 0); }
		public TerminalNode DESCRIBE() { return getToken(MqlBaseParser.DESCRIBE, 0); }
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public DescDatabaseContext(MqlContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterDescDatabase(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitDescDatabase(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitDescDatabase(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class SetSaPasswordContext extends MqlContext {
		public IdentifierContext name;
		public PasswordContext pwd;
		public IdentifierContext org;
		public TerminalNode ALTER() { return getToken(MqlBaseParser.ALTER, 0); }
		public TerminalNode SA() { return getToken(MqlBaseParser.SA, 0); }
		public TerminalNode IDENTIFIED() { return getToken(MqlBaseParser.IDENTIFIED, 0); }
		public TerminalNode BY() { return getToken(MqlBaseParser.BY, 0); }
		public TerminalNode IN() { return getToken(MqlBaseParser.IN, 0); }
		public List<IdentifierContext> identifier() {
			return getRuleContexts(IdentifierContext.class);
		}
		public IdentifierContext identifier(int i) {
			return getRuleContext(IdentifierContext.class,i);
		}
		public PasswordContext password() {
			return getRuleContext(PasswordContext.class,0);
		}
		public TerminalNode ORG() { return getToken(MqlBaseParser.ORG, 0); }
		public TerminalNode ORGANIZATION() { return getToken(MqlBaseParser.ORGANIZATION, 0); }
		public SetSaPasswordContext(MqlContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterSetSaPassword(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitSetSaPassword(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitSetSaPassword(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class DropGroupContext extends MqlContext {
		public IdentifierContext name;
		public TerminalNode DROP() { return getToken(MqlBaseParser.DROP, 0); }
		public TerminalNode GROUP() { return getToken(MqlBaseParser.GROUP, 0); }
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public TerminalNode IF() { return getToken(MqlBaseParser.IF, 0); }
		public TerminalNode EXISTS() { return getToken(MqlBaseParser.EXISTS, 0); }
		public TerminalNode CASCADE() { return getToken(MqlBaseParser.CASCADE, 0); }
		public DropGroupContext(MqlContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterDropGroup(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitDropGroup(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitDropGroup(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class SetDatasourceNameContext extends MqlContext {
		public IdentifierContext name;
		public IdentifierContext newName;
		public TerminalNode ALTER() { return getToken(MqlBaseParser.ALTER, 0); }
		public TerminalNode DATASOURCE() { return getToken(MqlBaseParser.DATASOURCE, 0); }
		public TerminalNode RENAME() { return getToken(MqlBaseParser.RENAME, 0); }
		public TerminalNode TO() { return getToken(MqlBaseParser.TO, 0); }
		public List<IdentifierContext> identifier() {
			return getRuleContexts(IdentifierContext.class);
		}
		public IdentifierContext identifier(int i) {
			return getRuleContext(IdentifierContext.class,i);
		}
		public SetDatasourceNameContext(MqlContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterSetDatasourceName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitSetDatasourceName(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitSetDatasourceName(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class RevokeAccountFromGroupsContext extends MqlContext {
		public IdentifierListContext groups;
		public TerminalNode REVOKE() { return getToken(MqlBaseParser.REVOKE, 0); }
		public TerminalNode ACCOUNT() { return getToken(MqlBaseParser.ACCOUNT, 0); }
		public TerminalNode FROM() { return getToken(MqlBaseParser.FROM, 0); }
		public TerminalNode GROUP() { return getToken(MqlBaseParser.GROUP, 0); }
		public IdentifierListContext identifierList() {
			return getRuleContext(IdentifierListContext.class,0);
		}
		public RevokeAccountFromGroupsContext(MqlContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterRevokeAccountFromGroups(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitRevokeAccountFromGroups(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitRevokeAccountFromGroups(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class DropFunctionContext extends MqlContext {
		public FuncIdentifierContext name;
		public TerminalNode DROP() { return getToken(MqlBaseParser.DROP, 0); }
		public TerminalNode FUNCTION() { return getToken(MqlBaseParser.FUNCTION, 0); }
		public FuncIdentifierContext funcIdentifier() {
			return getRuleContext(FuncIdentifierContext.class,0);
		}
		public TerminalNode IF() { return getToken(MqlBaseParser.IF, 0); }
		public TerminalNode EXISTS() { return getToken(MqlBaseParser.EXISTS, 0); }
		public DropFunctionContext(MqlContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterDropFunction(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitDropFunction(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitDropFunction(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class GrantGrantToGroupContext extends MqlContext {
		public IdentifierListContext groups;
		public List<TerminalNode> GRANT() { return getTokens(MqlBaseParser.GRANT); }
		public TerminalNode GRANT(int i) {
			return getToken(MqlBaseParser.GRANT, i);
		}
		public TerminalNode OPTION() { return getToken(MqlBaseParser.OPTION, 0); }
		public PrivilegeListContext privilegeList() {
			return getRuleContext(PrivilegeListContext.class,0);
		}
		public TerminalNode TO() { return getToken(MqlBaseParser.TO, 0); }
		public TerminalNode GROUP() { return getToken(MqlBaseParser.GROUP, 0); }
		public IdentifierListContext identifierList() {
			return getRuleContext(IdentifierListContext.class,0);
		}
		public GrantGrantToGroupContext(MqlContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterGrantGrantToGroup(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitGrantGrantToGroup(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitGrantGrantToGroup(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class SetFunctionPropertiesContext extends MqlContext {
		public FuncIdentifierContext name;
		public TerminalNode ALTER() { return getToken(MqlBaseParser.ALTER, 0); }
		public TerminalNode FUNCTION() { return getToken(MqlBaseParser.FUNCTION, 0); }
		public TerminalNode SET() { return getToken(MqlBaseParser.SET, 0); }
		public TerminalNode OPTIONS() { return getToken(MqlBaseParser.OPTIONS, 0); }
		public PropertyListContext propertyList() {
			return getRuleContext(PropertyListContext.class,0);
		}
		public FuncIdentifierContext funcIdentifier() {
			return getRuleContext(FuncIdentifierContext.class,0);
		}
		public SetFunctionPropertiesContext(MqlContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterSetFunctionProperties(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitSetFunctionProperties(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitSetFunctionProperties(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class SetUserNameContext extends MqlContext {
		public IdentifierContext name;
		public IdentifierContext newName;
		public TerminalNode ALTER() { return getToken(MqlBaseParser.ALTER, 0); }
		public TerminalNode USER() { return getToken(MqlBaseParser.USER, 0); }
		public TerminalNode RENAME() { return getToken(MqlBaseParser.RENAME, 0); }
		public TerminalNode TO() { return getToken(MqlBaseParser.TO, 0); }
		public List<IdentifierContext> identifier() {
			return getRuleContexts(IdentifierContext.class);
		}
		public IdentifierContext identifier(int i) {
			return getRuleContext(IdentifierContext.class,i);
		}
		public SetUserNameContext(MqlContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterSetUserName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitSetUserName(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitSetUserName(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class CreateTemporaryViewContext extends MqlContext {
		public CreateTemporaryViewCmdContext createTemporaryViewCmd() {
			return getRuleContext(CreateTemporaryViewCmdContext.class,0);
		}
		public CreateTemporaryViewContext(MqlContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterCreateTemporaryView(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitCreateTemporaryView(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitCreateTemporaryView(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class RenameFunctionContext extends MqlContext {
		public FuncIdentifierContext name;
		public FuncIdentifierContext newName;
		public TerminalNode RENAME() { return getToken(MqlBaseParser.RENAME, 0); }
		public TerminalNode FUNCTION() { return getToken(MqlBaseParser.FUNCTION, 0); }
		public TerminalNode TO() { return getToken(MqlBaseParser.TO, 0); }
		public List<FuncIdentifierContext> funcIdentifier() {
			return getRuleContexts(FuncIdentifierContext.class);
		}
		public FuncIdentifierContext funcIdentifier(int i) {
			return getRuleContext(FuncIdentifierContext.class,i);
		}
		public RenameFunctionContext(MqlContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterRenameFunction(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitRenameFunction(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitRenameFunction(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class SetViewQueryContext extends MqlContext {
		public TableIdentifierContext name;
		public TerminalNode ALTER() { return getToken(MqlBaseParser.ALTER, 0); }
		public TerminalNode VIEW() { return getToken(MqlBaseParser.VIEW, 0); }
		public TerminalNode AS() { return getToken(MqlBaseParser.AS, 0); }
		public QueryContext query() {
			return getRuleContext(QueryContext.class,0);
		}
		public TableIdentifierContext tableIdentifier() {
			return getRuleContext(TableIdentifierContext.class,0);
		}
		public SetViewQueryContext(MqlContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterSetViewQuery(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitSetViewQuery(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitSetViewQuery(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class CreateEventContext extends MqlContext {
		public IdentifierContext name;
		public Token comment;
		public IdentifierContext app;
		public TerminalNode CREATE() { return getToken(MqlBaseParser.CREATE, 0); }
		public TerminalNode EVENT() { return getToken(MqlBaseParser.EVENT, 0); }
		public TerminalNode ON() { return getToken(MqlBaseParser.ON, 0); }
		public TerminalNode SCHEDULE() { return getToken(MqlBaseParser.SCHEDULE, 0); }
		public TerminalNode AT() { return getToken(MqlBaseParser.AT, 0); }
		public ScheduleContext schedule() {
			return getRuleContext(ScheduleContext.class,0);
		}
		public TerminalNode DO() { return getToken(MqlBaseParser.DO, 0); }
		public TerminalNode CALL() { return getToken(MqlBaseParser.CALL, 0); }
		public List<IdentifierContext> identifier() {
			return getRuleContexts(IdentifierContext.class);
		}
		public IdentifierContext identifier(int i) {
			return getRuleContext(IdentifierContext.class,i);
		}
		public TerminalNode DEFINER() { return getToken(MqlBaseParser.DEFINER, 0); }
		public DefinerContext definer() {
			return getRuleContext(DefinerContext.class,0);
		}
		public TerminalNode IF() { return getToken(MqlBaseParser.IF, 0); }
		public TerminalNode NOT() { return getToken(MqlBaseParser.NOT, 0); }
		public TerminalNode EXISTS() { return getToken(MqlBaseParser.EXISTS, 0); }
		public TerminalNode COMMENT() { return getToken(MqlBaseParser.COMMENT, 0); }
		public TerminalNode ENABLE() { return getToken(MqlBaseParser.ENABLE, 0); }
		public TerminalNode DISABLE() { return getToken(MqlBaseParser.DISABLE, 0); }
		public TerminalNode STRING() { return getToken(MqlBaseParser.STRING, 0); }
		public CreateEventContext(MqlContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterCreateEvent(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitCreateEvent(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitCreateEvent(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class SetEventScheduleContext extends MqlContext {
		public IdentifierContext name;
		public TerminalNode ALTER() { return getToken(MqlBaseParser.ALTER, 0); }
		public TerminalNode EVENT() { return getToken(MqlBaseParser.EVENT, 0); }
		public TerminalNode ON() { return getToken(MqlBaseParser.ON, 0); }
		public TerminalNode SCHEDULE() { return getToken(MqlBaseParser.SCHEDULE, 0); }
		public TerminalNode AT() { return getToken(MqlBaseParser.AT, 0); }
		public ScheduleContext schedule() {
			return getRuleContext(ScheduleContext.class,0);
		}
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public SetEventScheduleContext(MqlContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterSetEventSchedule(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitSetEventSchedule(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitSetEventSchedule(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class SetDatabaseCommentContext extends MqlContext {
		public IdentifierContext name;
		public Token comment;
		public TerminalNode ALTER() { return getToken(MqlBaseParser.ALTER, 0); }
		public TerminalNode DATABASE() { return getToken(MqlBaseParser.DATABASE, 0); }
		public TerminalNode SET() { return getToken(MqlBaseParser.SET, 0); }
		public TerminalNode COMMENT() { return getToken(MqlBaseParser.COMMENT, 0); }
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public TerminalNode STRING() { return getToken(MqlBaseParser.STRING, 0); }
		public SetDatabaseCommentContext(MqlContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterSetDatabaseComment(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitSetDatabaseComment(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitSetDatabaseComment(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class SetConfigurationContext extends MqlContext {
		public TerminalNode SET() { return getToken(MqlBaseParser.SET, 0); }
		public PropertyContext property() {
			return getRuleContext(PropertyContext.class,0);
		}
		public SetConfigurationContext(MqlContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterSetConfiguration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitSetConfiguration(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitSetConfiguration(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class DropTableColumnContext extends MqlContext {
		public TableIdentifierContext name;
		public TerminalNode ALTER() { return getToken(MqlBaseParser.ALTER, 0); }
		public TerminalNode TABLE() { return getToken(MqlBaseParser.TABLE, 0); }
		public TerminalNode DROP() { return getToken(MqlBaseParser.DROP, 0); }
		public TerminalNode COLUMN() { return getToken(MqlBaseParser.COLUMN, 0); }
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public TableIdentifierContext tableIdentifier() {
			return getRuleContext(TableIdentifierContext.class,0);
		}
		public DropTableColumnContext(MqlContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterDropTableColumn(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitDropTableColumn(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitDropTableColumn(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ShowSysInfoContext extends MqlContext {
		public TerminalNode SHOW() { return getToken(MqlBaseParser.SHOW, 0); }
		public TerminalNode SYSINFO() { return getToken(MqlBaseParser.SYSINFO, 0); }
		public ShowSysInfoContext(MqlContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterShowSysInfo(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitShowSysInfo(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitShowSysInfo(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class RenameDatabaseContext extends MqlContext {
		public IdentifierContext name;
		public IdentifierContext newName;
		public TerminalNode RENAME() { return getToken(MqlBaseParser.RENAME, 0); }
		public TerminalNode DATABASE() { return getToken(MqlBaseParser.DATABASE, 0); }
		public TerminalNode TO() { return getToken(MqlBaseParser.TO, 0); }
		public List<IdentifierContext> identifier() {
			return getRuleContexts(IdentifierContext.class);
		}
		public IdentifierContext identifier(int i) {
			return getRuleContext(IdentifierContext.class,i);
		}
		public RenameDatabaseContext(MqlContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterRenameDatabase(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitRenameDatabase(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitRenameDatabase(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ShowDatasourcesContext extends MqlContext {
		public Token pattern;
		public TerminalNode SHOW() { return getToken(MqlBaseParser.SHOW, 0); }
		public TerminalNode DATASOURCES() { return getToken(MqlBaseParser.DATASOURCES, 0); }
		public TerminalNode LIKE() { return getToken(MqlBaseParser.LIKE, 0); }
		public TerminalNode STRING() { return getToken(MqlBaseParser.STRING, 0); }
		public ShowDatasourcesContext(MqlContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterShowDatasources(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitShowDatasources(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitShowDatasources(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class SetApplicationNameContext extends MqlContext {
		public IdentifierContext name;
		public IdentifierContext newName;
		public TerminalNode ALTER() { return getToken(MqlBaseParser.ALTER, 0); }
		public TerminalNode APPLICATION() { return getToken(MqlBaseParser.APPLICATION, 0); }
		public TerminalNode RENAME() { return getToken(MqlBaseParser.RENAME, 0); }
		public TerminalNode TO() { return getToken(MqlBaseParser.TO, 0); }
		public List<IdentifierContext> identifier() {
			return getRuleContexts(IdentifierContext.class);
		}
		public IdentifierContext identifier(int i) {
			return getRuleContext(IdentifierContext.class,i);
		}
		public SetApplicationNameContext(MqlContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterSetApplicationName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitSetApplicationName(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitSetApplicationName(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class RenameGroupContext extends MqlContext {
		public IdentifierContext name;
		public IdentifierContext newName;
		public TerminalNode RENAME() { return getToken(MqlBaseParser.RENAME, 0); }
		public TerminalNode GROUP() { return getToken(MqlBaseParser.GROUP, 0); }
		public TerminalNode TO() { return getToken(MqlBaseParser.TO, 0); }
		public List<IdentifierContext> identifier() {
			return getRuleContexts(IdentifierContext.class);
		}
		public IdentifierContext identifier(int i) {
			return getRuleContext(IdentifierContext.class,i);
		}
		public RenameGroupContext(MqlContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterRenameGroup(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitRenameGroup(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitRenameGroup(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class CreateSaContext extends MqlContext {
		public IdentifierContext name;
		public IdentifierContext org;
		public PasswordContext pwd;
		public TerminalNode CREATE() { return getToken(MqlBaseParser.CREATE, 0); }
		public TerminalNode SA() { return getToken(MqlBaseParser.SA, 0); }
		public TerminalNode IN() { return getToken(MqlBaseParser.IN, 0); }
		public TerminalNode IDENTIFIED() { return getToken(MqlBaseParser.IDENTIFIED, 0); }
		public TerminalNode BY() { return getToken(MqlBaseParser.BY, 0); }
		public List<IdentifierContext> identifier() {
			return getRuleContexts(IdentifierContext.class);
		}
		public IdentifierContext identifier(int i) {
			return getRuleContext(IdentifierContext.class,i);
		}
		public TerminalNode ORG() { return getToken(MqlBaseParser.ORG, 0); }
		public TerminalNode ORGANIZATION() { return getToken(MqlBaseParser.ORGANIZATION, 0); }
		public PasswordContext password() {
			return getRuleContext(PasswordContext.class,0);
		}
		public TerminalNode IF() { return getToken(MqlBaseParser.IF, 0); }
		public TerminalNode NOT() { return getToken(MqlBaseParser.NOT, 0); }
		public TerminalNode EXISTS() { return getToken(MqlBaseParser.EXISTS, 0); }
		public CreateSaContext(MqlContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterCreateSa(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitCreateSa(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitCreateSa(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class DropApplicationContext extends MqlContext {
		public IdentifierContext name;
		public TerminalNode DROP() { return getToken(MqlBaseParser.DROP, 0); }
		public TerminalNode APPLICATION() { return getToken(MqlBaseParser.APPLICATION, 0); }
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public TerminalNode IF() { return getToken(MqlBaseParser.IF, 0); }
		public TerminalNode EXISTS() { return getToken(MqlBaseParser.EXISTS, 0); }
		public DropApplicationContext(MqlContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterDropApplication(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitDropApplication(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitDropApplication(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class RevokeDdlFromGroupsContext extends MqlContext {
		public IdentifierListContext groups;
		public TerminalNode REVOKE() { return getToken(MqlBaseParser.REVOKE, 0); }
		public TerminalNode DDL() { return getToken(MqlBaseParser.DDL, 0); }
		public TerminalNode FROM() { return getToken(MqlBaseParser.FROM, 0); }
		public TerminalNode GROUP() { return getToken(MqlBaseParser.GROUP, 0); }
		public IdentifierListContext identifierList() {
			return getRuleContext(IdentifierListContext.class,0);
		}
		public RevokeDdlFromGroupsContext(MqlContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterRevokeDdlFromGroups(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitRevokeDdlFromGroups(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitRevokeDdlFromGroups(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class SetDatabaseNameContext extends MqlContext {
		public IdentifierContext name;
		public IdentifierContext newName;
		public TerminalNode ALTER() { return getToken(MqlBaseParser.ALTER, 0); }
		public TerminalNode DATABASE() { return getToken(MqlBaseParser.DATABASE, 0); }
		public TerminalNode RENAME() { return getToken(MqlBaseParser.RENAME, 0); }
		public TerminalNode TO() { return getToken(MqlBaseParser.TO, 0); }
		public List<IdentifierContext> identifier() {
			return getRuleContexts(IdentifierContext.class);
		}
		public IdentifierContext identifier(int i) {
			return getRuleContext(IdentifierContext.class,i);
		}
		public SetDatabaseNameContext(MqlContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterSetDatabaseName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitSetDatabaseName(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitSetDatabaseName(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class SetUserPasswordContext extends MqlContext {
		public IdentifierContext name;
		public PasswordContext pwd;
		public TerminalNode ALTER() { return getToken(MqlBaseParser.ALTER, 0); }
		public TerminalNode USER() { return getToken(MqlBaseParser.USER, 0); }
		public TerminalNode IDENTIFIED() { return getToken(MqlBaseParser.IDENTIFIED, 0); }
		public TerminalNode BY() { return getToken(MqlBaseParser.BY, 0); }
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public PasswordContext password() {
			return getRuleContext(PasswordContext.class,0);
		}
		public SetUserPasswordContext(MqlContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterSetUserPassword(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitSetUserPassword(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitSetUserPassword(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class AddTableColumnsContext extends MqlContext {
		public TableIdentifierContext name;
		public ColTypeListContext columns;
		public TerminalNode ALTER() { return getToken(MqlBaseParser.ALTER, 0); }
		public TerminalNode TABLE() { return getToken(MqlBaseParser.TABLE, 0); }
		public TerminalNode ADD() { return getToken(MqlBaseParser.ADD, 0); }
		public TerminalNode COLUMNS() { return getToken(MqlBaseParser.COLUMNS, 0); }
		public TableIdentifierContext tableIdentifier() {
			return getRuleContext(TableIdentifierContext.class,0);
		}
		public ColTypeListContext colTypeList() {
			return getRuleContext(ColTypeListContext.class,0);
		}
		public AddTableColumnsContext(MqlContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterAddTableColumns(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitAddTableColumns(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitAddTableColumns(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class SetSaNameContext extends MqlContext {
		public IdentifierContext name;
		public IdentifierContext newName;
		public IdentifierContext org;
		public TerminalNode ALTER() { return getToken(MqlBaseParser.ALTER, 0); }
		public TerminalNode SA() { return getToken(MqlBaseParser.SA, 0); }
		public TerminalNode RENAME() { return getToken(MqlBaseParser.RENAME, 0); }
		public TerminalNode TO() { return getToken(MqlBaseParser.TO, 0); }
		public TerminalNode IN() { return getToken(MqlBaseParser.IN, 0); }
		public List<IdentifierContext> identifier() {
			return getRuleContexts(IdentifierContext.class);
		}
		public IdentifierContext identifier(int i) {
			return getRuleContext(IdentifierContext.class,i);
		}
		public TerminalNode ORG() { return getToken(MqlBaseParser.ORG, 0); }
		public TerminalNode ORGANIZATION() { return getToken(MqlBaseParser.ORGANIZATION, 0); }
		public SetSaNameContext(MqlContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterSetSaName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitSetSaName(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitSetSaName(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class DropOrganizationContext extends MqlContext {
		public IdentifierContext name;
		public TerminalNode DROP() { return getToken(MqlBaseParser.DROP, 0); }
		public TerminalNode ORG() { return getToken(MqlBaseParser.ORG, 0); }
		public TerminalNode ORGANIZATION() { return getToken(MqlBaseParser.ORGANIZATION, 0); }
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public TerminalNode IF() { return getToken(MqlBaseParser.IF, 0); }
		public TerminalNode EXISTS() { return getToken(MqlBaseParser.EXISTS, 0); }
		public TerminalNode CASCADE() { return getToken(MqlBaseParser.CASCADE, 0); }
		public DropOrganizationContext(MqlContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterDropOrganization(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitDropOrganization(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitDropOrganization(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MqlContext mql() throws RecognitionException {
		MqlContext _localctx = new MqlContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_mql);
		int _la;
		try {
			setState(845);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,55,_ctx) ) {
			case 1:
				_localctx = new CreateOrganizationContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(83);
				match(CREATE);
				setState(84);
				_la = _input.LA(1);
				if ( !(_la==ORG || _la==ORGANIZATION) ) {
				_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(88);
				_la = _input.LA(1);
				if (_la==IF) {
					{
					setState(85);
					match(IF);
					setState(86);
					match(NOT);
					setState(87);
					match(EXISTS);
					}
				}

				setState(90);
				((CreateOrganizationContext)_localctx).name = identifier();
				setState(93);
				_la = _input.LA(1);
				if (_la==COMMENT) {
					{
					setState(91);
					match(COMMENT);
					setState(92);
					((CreateOrganizationContext)_localctx).comment = match(STRING);
					}
				}

				}
				break;
			case 2:
				_localctx = new RenameOrganizationContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(95);
				match(RENAME);
				setState(96);
				_la = _input.LA(1);
				if ( !(_la==ORG || _la==ORGANIZATION) ) {
				_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(97);
				((RenameOrganizationContext)_localctx).name = identifier();
				setState(98);
				match(TO);
				setState(99);
				((RenameOrganizationContext)_localctx).newName = identifier();
				}
				break;
			case 3:
				_localctx = new SetOrganizationNameContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(101);
				match(ALTER);
				setState(102);
				_la = _input.LA(1);
				if ( !(_la==ORG || _la==ORGANIZATION) ) {
				_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(103);
				((SetOrganizationNameContext)_localctx).name = identifier();
				setState(104);
				match(RENAME);
				setState(105);
				match(TO);
				setState(106);
				((SetOrganizationNameContext)_localctx).newName = identifier();
				}
				break;
			case 4:
				_localctx = new SetOrganizationCommentContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(108);
				match(ALTER);
				setState(109);
				_la = _input.LA(1);
				if ( !(_la==ORG || _la==ORGANIZATION) ) {
				_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(110);
				((SetOrganizationCommentContext)_localctx).name = identifier();
				setState(111);
				match(SET);
				setState(112);
				match(COMMENT);
				setState(113);
				((SetOrganizationCommentContext)_localctx).comment = match(STRING);
				}
				break;
			case 5:
				_localctx = new DropOrganizationContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(115);
				match(DROP);
				setState(116);
				_la = _input.LA(1);
				if ( !(_la==ORG || _la==ORGANIZATION) ) {
				_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(119);
				_la = _input.LA(1);
				if (_la==IF) {
					{
					setState(117);
					match(IF);
					setState(118);
					match(EXISTS);
					}
				}

				setState(121);
				((DropOrganizationContext)_localctx).name = identifier();
				setState(123);
				_la = _input.LA(1);
				if (_la==CASCADE) {
					{
					setState(122);
					match(CASCADE);
					}
				}

				}
				break;
			case 6:
				_localctx = new CreateSaContext(_localctx);
				enterOuterAlt(_localctx, 6);
				{
				setState(125);
				match(CREATE);
				setState(126);
				match(SA);
				setState(130);
				_la = _input.LA(1);
				if (_la==IF) {
					{
					setState(127);
					match(IF);
					setState(128);
					match(NOT);
					setState(129);
					match(EXISTS);
					}
				}

				setState(132);
				((CreateSaContext)_localctx).name = identifier();
				setState(133);
				match(IN);
				setState(134);
				_la = _input.LA(1);
				if ( !(_la==ORG || _la==ORGANIZATION) ) {
				_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(135);
				((CreateSaContext)_localctx).org = identifier();
				setState(136);
				match(IDENTIFIED);
				setState(137);
				match(BY);
				setState(138);
				((CreateSaContext)_localctx).pwd = password();
				}
				break;
			case 7:
				_localctx = new RenameSaContext(_localctx);
				enterOuterAlt(_localctx, 7);
				{
				setState(140);
				match(RENAME);
				setState(141);
				match(SA);
				setState(142);
				((RenameSaContext)_localctx).name = identifier();
				setState(143);
				match(TO);
				setState(144);
				((RenameSaContext)_localctx).newName = identifier();
				setState(145);
				match(IN);
				setState(146);
				_la = _input.LA(1);
				if ( !(_la==ORG || _la==ORGANIZATION) ) {
				_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(147);
				((RenameSaContext)_localctx).org = identifier();
				}
				break;
			case 8:
				_localctx = new SetSaNameContext(_localctx);
				enterOuterAlt(_localctx, 8);
				{
				setState(149);
				match(ALTER);
				setState(150);
				match(SA);
				setState(151);
				((SetSaNameContext)_localctx).name = identifier();
				setState(152);
				match(RENAME);
				setState(153);
				match(TO);
				setState(154);
				((SetSaNameContext)_localctx).newName = identifier();
				setState(155);
				match(IN);
				setState(156);
				_la = _input.LA(1);
				if ( !(_la==ORG || _la==ORGANIZATION) ) {
				_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(157);
				((SetSaNameContext)_localctx).org = identifier();
				}
				break;
			case 9:
				_localctx = new SetSaPasswordContext(_localctx);
				enterOuterAlt(_localctx, 9);
				{
				setState(159);
				match(ALTER);
				setState(160);
				match(SA);
				setState(161);
				((SetSaPasswordContext)_localctx).name = identifier();
				setState(162);
				match(IDENTIFIED);
				setState(163);
				match(BY);
				setState(164);
				((SetSaPasswordContext)_localctx).pwd = password();
				setState(165);
				match(IN);
				setState(166);
				_la = _input.LA(1);
				if ( !(_la==ORG || _la==ORGANIZATION) ) {
				_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(167);
				((SetSaPasswordContext)_localctx).org = identifier();
				}
				break;
			case 10:
				_localctx = new DropSaContext(_localctx);
				enterOuterAlt(_localctx, 10);
				{
				setState(169);
				match(DROP);
				setState(170);
				match(SA);
				setState(173);
				_la = _input.LA(1);
				if (_la==IF) {
					{
					setState(171);
					match(IF);
					setState(172);
					match(EXISTS);
					}
				}

				setState(175);
				((DropSaContext)_localctx).name = identifier();
				setState(176);
				match(IN);
				setState(177);
				_la = _input.LA(1);
				if ( !(_la==ORG || _la==ORGANIZATION) ) {
				_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(178);
				((DropSaContext)_localctx).org = identifier();
				}
				break;
			case 11:
				_localctx = new GrantGrantToUserContext(_localctx);
				enterOuterAlt(_localctx, 11);
				{
				setState(180);
				match(GRANT);
				setState(181);
				match(GRANT);
				setState(182);
				match(OPTION);
				setState(183);
				privilegeList();
				setState(184);
				match(TO);
				setState(185);
				match(USER);
				setState(186);
				((GrantGrantToUserContext)_localctx).users = identifierList();
				}
				break;
			case 12:
				_localctx = new GrantGrantToGroupContext(_localctx);
				enterOuterAlt(_localctx, 12);
				{
				setState(188);
				match(GRANT);
				setState(189);
				match(GRANT);
				setState(190);
				match(OPTION);
				setState(191);
				privilegeList();
				setState(192);
				match(TO);
				setState(193);
				match(GROUP);
				setState(194);
				((GrantGrantToGroupContext)_localctx).groups = identifierList();
				}
				break;
			case 13:
				_localctx = new RevokeGrantFromUserContext(_localctx);
				enterOuterAlt(_localctx, 13);
				{
				setState(196);
				match(REVOKE);
				setState(197);
				match(GRANT);
				setState(198);
				match(OPTION);
				setState(199);
				privilegeList();
				setState(200);
				match(FROM);
				setState(201);
				match(USER);
				setState(202);
				((RevokeGrantFromUserContext)_localctx).users = identifierList();
				}
				break;
			case 14:
				_localctx = new RevokeGrantFromGroupContext(_localctx);
				enterOuterAlt(_localctx, 14);
				{
				setState(204);
				match(REVOKE);
				setState(205);
				match(GRANT);
				setState(206);
				match(OPTION);
				setState(207);
				privilegeList();
				setState(208);
				match(FROM);
				setState(209);
				match(GROUP);
				setState(210);
				((RevokeGrantFromGroupContext)_localctx).groups = identifierList();
				}
				break;
			case 15:
				_localctx = new GrantAccountToUsersContext(_localctx);
				enterOuterAlt(_localctx, 15);
				{
				setState(212);
				match(GRANT);
				setState(213);
				match(ACCOUNT);
				setState(214);
				match(TO);
				setState(215);
				match(USER);
				setState(216);
				((GrantAccountToUsersContext)_localctx).users = identifierList();
				}
				break;
			case 16:
				_localctx = new GrantAccountToGroupsContext(_localctx);
				enterOuterAlt(_localctx, 16);
				{
				setState(217);
				match(GRANT);
				setState(218);
				match(ACCOUNT);
				setState(219);
				match(TO);
				setState(220);
				match(GROUP);
				setState(221);
				((GrantAccountToGroupsContext)_localctx).groups = identifierList();
				}
				break;
			case 17:
				_localctx = new RevokeAccountFromUsersContext(_localctx);
				enterOuterAlt(_localctx, 17);
				{
				setState(222);
				match(REVOKE);
				setState(223);
				match(ACCOUNT);
				setState(224);
				match(FROM);
				setState(225);
				match(USER);
				setState(226);
				((RevokeAccountFromUsersContext)_localctx).users = identifierList();
				}
				break;
			case 18:
				_localctx = new RevokeAccountFromGroupsContext(_localctx);
				enterOuterAlt(_localctx, 18);
				{
				setState(227);
				match(REVOKE);
				setState(228);
				match(ACCOUNT);
				setState(229);
				match(FROM);
				setState(230);
				match(GROUP);
				setState(231);
				((RevokeAccountFromGroupsContext)_localctx).groups = identifierList();
				}
				break;
			case 19:
				_localctx = new GrantDdlToUsersContext(_localctx);
				enterOuterAlt(_localctx, 19);
				{
				setState(232);
				match(GRANT);
				setState(233);
				match(DDL);
				setState(234);
				match(TO);
				setState(235);
				match(USER);
				setState(236);
				((GrantDdlToUsersContext)_localctx).users = identifierList();
				}
				break;
			case 20:
				_localctx = new GrantDdlToGroupsContext(_localctx);
				enterOuterAlt(_localctx, 20);
				{
				setState(237);
				match(GRANT);
				setState(238);
				match(DDL);
				setState(239);
				match(TO);
				setState(240);
				match(GROUP);
				setState(241);
				((GrantDdlToGroupsContext)_localctx).groups = identifierList();
				}
				break;
			case 21:
				_localctx = new RevokeDdlFromUsersContext(_localctx);
				enterOuterAlt(_localctx, 21);
				{
				setState(242);
				match(REVOKE);
				setState(243);
				match(DDL);
				setState(244);
				match(FROM);
				setState(245);
				match(USER);
				setState(246);
				((RevokeDdlFromUsersContext)_localctx).users = identifierList();
				}
				break;
			case 22:
				_localctx = new RevokeDdlFromGroupsContext(_localctx);
				enterOuterAlt(_localctx, 22);
				{
				setState(247);
				match(REVOKE);
				setState(248);
				match(DDL);
				setState(249);
				match(FROM);
				setState(250);
				match(GROUP);
				setState(251);
				((RevokeDdlFromGroupsContext)_localctx).groups = identifierList();
				}
				break;
			case 23:
				_localctx = new GrantDmlOnToUsersContext(_localctx);
				enterOuterAlt(_localctx, 23);
				{
				setState(252);
				match(GRANT);
				setState(253);
				match(DML);
				setState(254);
				match(ON);
				setState(255);
				qualifiedColumnList();
				setState(256);
				match(TO);
				setState(257);
				match(USER);
				setState(258);
				((GrantDmlOnToUsersContext)_localctx).users = identifierList();
				}
				break;
			case 24:
				_localctx = new GrantDmlOnToGroupsContext(_localctx);
				enterOuterAlt(_localctx, 24);
				{
				setState(260);
				match(GRANT);
				setState(261);
				match(DML);
				setState(262);
				match(ON);
				setState(263);
				qualifiedColumnList();
				setState(264);
				match(TO);
				setState(265);
				match(GROUP);
				setState(266);
				((GrantDmlOnToGroupsContext)_localctx).groups = identifierList();
				}
				break;
			case 25:
				_localctx = new RevokeDmlOnFromUsersContext(_localctx);
				enterOuterAlt(_localctx, 25);
				{
				setState(268);
				match(REVOKE);
				setState(269);
				match(DML);
				setState(270);
				match(ON);
				setState(271);
				qualifiedColumnList();
				setState(272);
				match(FROM);
				setState(273);
				match(USER);
				setState(274);
				((RevokeDmlOnFromUsersContext)_localctx).users = identifierList();
				}
				break;
			case 26:
				_localctx = new RevokeDmlOnFromGroupsContext(_localctx);
				enterOuterAlt(_localctx, 26);
				{
				setState(276);
				match(REVOKE);
				setState(277);
				match(DML);
				setState(278);
				match(ON);
				setState(279);
				qualifiedColumnList();
				setState(280);
				match(FROM);
				setState(281);
				match(GROUP);
				setState(282);
				((RevokeDmlOnFromGroupsContext)_localctx).groups = identifierList();
				}
				break;
			case 27:
				_localctx = new CreateUserContext(_localctx);
				enterOuterAlt(_localctx, 27);
				{
				setState(284);
				match(CREATE);
				setState(285);
				match(USER);
				setState(289);
				_la = _input.LA(1);
				if (_la==IF) {
					{
					setState(286);
					match(IF);
					setState(287);
					match(NOT);
					setState(288);
					match(EXISTS);
					}
				}

				setState(291);
				((CreateUserContext)_localctx).name = identifier();
				setState(292);
				match(IDENTIFIED);
				setState(293);
				match(BY);
				setState(294);
				((CreateUserContext)_localctx).pwd = password();
				}
				break;
			case 28:
				_localctx = new RenameUserContext(_localctx);
				enterOuterAlt(_localctx, 28);
				{
				setState(296);
				match(RENAME);
				setState(297);
				match(USER);
				setState(298);
				((RenameUserContext)_localctx).name = identifier();
				setState(299);
				match(TO);
				setState(300);
				((RenameUserContext)_localctx).newName = identifier();
				}
				break;
			case 29:
				_localctx = new SetUserNameContext(_localctx);
				enterOuterAlt(_localctx, 29);
				{
				setState(302);
				match(ALTER);
				setState(303);
				match(USER);
				setState(304);
				((SetUserNameContext)_localctx).name = identifier();
				setState(305);
				match(RENAME);
				setState(306);
				match(TO);
				setState(307);
				((SetUserNameContext)_localctx).newName = identifier();
				}
				break;
			case 30:
				_localctx = new SetUserPasswordContext(_localctx);
				enterOuterAlt(_localctx, 30);
				{
				setState(309);
				match(ALTER);
				setState(310);
				match(USER);
				setState(311);
				((SetUserPasswordContext)_localctx).name = identifier();
				setState(312);
				match(IDENTIFIED);
				setState(313);
				match(BY);
				setState(314);
				((SetUserPasswordContext)_localctx).pwd = password();
				}
				break;
			case 31:
				_localctx = new DropUserContext(_localctx);
				enterOuterAlt(_localctx, 31);
				{
				setState(316);
				match(DROP);
				setState(317);
				match(USER);
				setState(320);
				_la = _input.LA(1);
				if (_la==IF) {
					{
					setState(318);
					match(IF);
					setState(319);
					match(EXISTS);
					}
				}

				setState(322);
				((DropUserContext)_localctx).name = identifier();
				}
				break;
			case 32:
				_localctx = new CreateGroupContext(_localctx);
				enterOuterAlt(_localctx, 32);
				{
				setState(323);
				match(CREATE);
				setState(324);
				match(GROUP);
				setState(328);
				_la = _input.LA(1);
				if (_la==IF) {
					{
					setState(325);
					match(IF);
					setState(326);
					match(NOT);
					setState(327);
					match(EXISTS);
					}
				}

				setState(330);
				((CreateGroupContext)_localctx).name = identifier();
				setState(333);
				_la = _input.LA(1);
				if (_la==COMMENT) {
					{
					setState(331);
					match(COMMENT);
					setState(332);
					((CreateGroupContext)_localctx).comment = match(STRING);
					}
				}

				}
				break;
			case 33:
				_localctx = new RenameGroupContext(_localctx);
				enterOuterAlt(_localctx, 33);
				{
				setState(335);
				match(RENAME);
				setState(336);
				match(GROUP);
				setState(337);
				((RenameGroupContext)_localctx).name = identifier();
				setState(338);
				match(TO);
				setState(339);
				((RenameGroupContext)_localctx).newName = identifier();
				}
				break;
			case 34:
				_localctx = new SetGroupNameContext(_localctx);
				enterOuterAlt(_localctx, 34);
				{
				setState(341);
				match(ALTER);
				setState(342);
				match(GROUP);
				setState(343);
				((SetGroupNameContext)_localctx).name = identifier();
				setState(344);
				match(RENAME);
				setState(345);
				match(TO);
				setState(346);
				((SetGroupNameContext)_localctx).newName = identifier();
				}
				break;
			case 35:
				_localctx = new SetGroupCommentContext(_localctx);
				enterOuterAlt(_localctx, 35);
				{
				setState(348);
				match(ALTER);
				setState(349);
				match(GROUP);
				setState(350);
				((SetGroupCommentContext)_localctx).name = identifier();
				setState(351);
				match(SET);
				setState(352);
				match(COMMENT);
				setState(353);
				((SetGroupCommentContext)_localctx).comment = match(STRING);
				}
				break;
			case 36:
				_localctx = new AddUsersToGroupContext(_localctx);
				enterOuterAlt(_localctx, 36);
				{
				setState(355);
				match(ALTER);
				setState(356);
				match(GROUP);
				setState(357);
				((AddUsersToGroupContext)_localctx).name = identifier();
				setState(358);
				addUser();
				setState(360);
				_la = _input.LA(1);
				if (_la==REMOVE) {
					{
					setState(359);
					removeUser();
					}
				}

				}
				break;
			case 37:
				_localctx = new RemoveUsersFromGroupContext(_localctx);
				enterOuterAlt(_localctx, 37);
				{
				setState(362);
				match(ALTER);
				setState(363);
				match(GROUP);
				setState(364);
				((RemoveUsersFromGroupContext)_localctx).name = identifier();
				setState(365);
				removeUser();
				setState(367);
				_la = _input.LA(1);
				if (_la==ADD) {
					{
					setState(366);
					addUser();
					}
				}

				}
				break;
			case 38:
				_localctx = new DropGroupContext(_localctx);
				enterOuterAlt(_localctx, 38);
				{
				setState(369);
				match(DROP);
				setState(370);
				match(GROUP);
				setState(373);
				_la = _input.LA(1);
				if (_la==IF) {
					{
					setState(371);
					match(IF);
					setState(372);
					match(EXISTS);
					}
				}

				setState(375);
				((DropGroupContext)_localctx).name = identifier();
				setState(377);
				_la = _input.LA(1);
				if (_la==CASCADE) {
					{
					setState(376);
					match(CASCADE);
					}
				}

				}
				break;
			case 39:
				_localctx = new MountDatasourceContext(_localctx);
				enterOuterAlt(_localctx, 39);
				{
				setState(379);
				match(MOUNT);
				setState(380);
				match(DATASOURCE);
				setState(384);
				_la = _input.LA(1);
				if (_la==IF) {
					{
					setState(381);
					match(IF);
					setState(382);
					match(NOT);
					setState(383);
					match(EXISTS);
					}
				}

				setState(386);
				((MountDatasourceContext)_localctx).name = identifier();
				setState(387);
				match(OPTIONS);
				setState(388);
				propertyList();
				}
				break;
			case 40:
				_localctx = new RenameDatasourceContext(_localctx);
				enterOuterAlt(_localctx, 40);
				{
				setState(390);
				match(RENAME);
				setState(391);
				match(DATASOURCE);
				setState(392);
				((RenameDatasourceContext)_localctx).name = identifier();
				setState(393);
				match(TO);
				setState(394);
				((RenameDatasourceContext)_localctx).newName = identifier();
				}
				break;
			case 41:
				_localctx = new SetDatasourceNameContext(_localctx);
				enterOuterAlt(_localctx, 41);
				{
				setState(396);
				match(ALTER);
				setState(397);
				match(DATASOURCE);
				setState(398);
				((SetDatasourceNameContext)_localctx).name = identifier();
				setState(399);
				match(RENAME);
				setState(400);
				match(TO);
				setState(401);
				((SetDatasourceNameContext)_localctx).newName = identifier();
				}
				break;
			case 42:
				_localctx = new SetDatasourcePropertiesContext(_localctx);
				enterOuterAlt(_localctx, 42);
				{
				setState(403);
				match(ALTER);
				setState(404);
				match(DATASOURCE);
				setState(405);
				((SetDatasourcePropertiesContext)_localctx).name = identifier();
				setState(406);
				match(SET);
				setState(407);
				match(OPTIONS);
				setState(408);
				propertyList();
				}
				break;
			case 43:
				_localctx = new UnmountDatasourceContext(_localctx);
				enterOuterAlt(_localctx, 43);
				{
				setState(410);
				match(UNMOUNT);
				setState(411);
				match(DATASOURCE);
				setState(414);
				_la = _input.LA(1);
				if (_la==IF) {
					{
					setState(412);
					match(IF);
					setState(413);
					match(EXISTS);
					}
				}

				setState(416);
				((UnmountDatasourceContext)_localctx).name = identifier();
				}
				break;
			case 44:
				_localctx = new MountTableContext(_localctx);
				enterOuterAlt(_localctx, 44);
				{
				setState(417);
				match(MOUNT);
				setState(419);
				_la = _input.LA(1);
				if (_la==STREAM) {
					{
					setState(418);
					match(STREAM);
					}
				}

				setState(421);
				match(TABLE);
				setState(425);
				_la = _input.LA(1);
				if (_la==IF) {
					{
					setState(422);
					match(IF);
					setState(423);
					match(NOT);
					setState(424);
					match(EXISTS);
					}
				}

				setState(427);
				tableIdentifier();
				setState(432);
				_la = _input.LA(1);
				if (_la==T__0) {
					{
					setState(428);
					match(T__0);
					setState(429);
					((MountTableContext)_localctx).columns = colTypeList();
					setState(430);
					match(T__1);
					}
				}

				setState(434);
				match(OPTIONS);
				setState(435);
				propertyList();
				}
				break;
			case 45:
				_localctx = new MountTableWithDatasourceContext(_localctx);
				enterOuterAlt(_localctx, 45);
				{
				setState(437);
				match(WITH);
				setState(438);
				match(DATASOURCE);
				setState(439);
				((MountTableWithDatasourceContext)_localctx).ds = identifier();
				setState(440);
				match(MOUNT);
				setState(442);
				_la = _input.LA(1);
				if (_la==STREAM) {
					{
					setState(441);
					match(STREAM);
					}
				}

				setState(444);
				match(TABLE);
				setState(448);
				_la = _input.LA(1);
				if (_la==IF) {
					{
					setState(445);
					match(IF);
					setState(446);
					match(NOT);
					setState(447);
					match(EXISTS);
					}
				}

				setState(450);
				mountTableList();
				}
				break;
			case 46:
				_localctx = new RenameTableContext(_localctx);
				enterOuterAlt(_localctx, 46);
				{
				setState(452);
				match(RENAME);
				setState(453);
				match(TABLE);
				setState(454);
				((RenameTableContext)_localctx).name = tableIdentifier();
				setState(455);
				match(TO);
				setState(456);
				((RenameTableContext)_localctx).newName = tableIdentifier();
				}
				break;
			case 47:
				_localctx = new SetTableNameContext(_localctx);
				enterOuterAlt(_localctx, 47);
				{
				setState(458);
				match(ALTER);
				setState(459);
				match(TABLE);
				setState(460);
				((SetTableNameContext)_localctx).name = tableIdentifier();
				setState(461);
				match(RENAME);
				setState(462);
				match(TO);
				setState(463);
				((SetTableNameContext)_localctx).newName = tableIdentifier();
				}
				break;
			case 48:
				_localctx = new SetTablePropertiesContext(_localctx);
				enterOuterAlt(_localctx, 48);
				{
				setState(465);
				match(ALTER);
				setState(466);
				match(TABLE);
				setState(467);
				((SetTablePropertiesContext)_localctx).name = tableIdentifier();
				setState(468);
				match(SET);
				setState(469);
				match(OPTIONS);
				setState(470);
				propertyList();
				}
				break;
			case 49:
				_localctx = new AddTableColumnsContext(_localctx);
				enterOuterAlt(_localctx, 49);
				{
				setState(472);
				match(ALTER);
				setState(473);
				match(TABLE);
				setState(474);
				((AddTableColumnsContext)_localctx).name = tableIdentifier();
				setState(475);
				match(ADD);
				setState(476);
				match(COLUMNS);
				{
				setState(477);
				match(T__0);
				setState(478);
				((AddTableColumnsContext)_localctx).columns = colTypeList();
				setState(479);
				match(T__1);
				}
				}
				break;
			case 50:
				_localctx = new ChangeTableColumnContext(_localctx);
				enterOuterAlt(_localctx, 50);
				{
				setState(481);
				match(ALTER);
				setState(482);
				match(TABLE);
				setState(483);
				((ChangeTableColumnContext)_localctx).name = tableIdentifier();
				setState(484);
				match(CHANGE);
				setState(486);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,21,_ctx) ) {
				case 1:
					{
					setState(485);
					match(COLUMN);
					}
					break;
				}
				setState(488);
				identifier();
				setState(489);
				colType();
				}
				break;
			case 51:
				_localctx = new DropTableColumnContext(_localctx);
				enterOuterAlt(_localctx, 51);
				{
				setState(491);
				match(ALTER);
				setState(492);
				match(TABLE);
				setState(493);
				((DropTableColumnContext)_localctx).name = tableIdentifier();
				setState(494);
				match(DROP);
				setState(495);
				match(COLUMN);
				setState(496);
				identifier();
				}
				break;
			case 52:
				_localctx = new UnmountTableContext(_localctx);
				enterOuterAlt(_localctx, 52);
				{
				setState(498);
				match(UNMOUNT);
				setState(499);
				match(TABLE);
				setState(502);
				_la = _input.LA(1);
				if (_la==IF) {
					{
					setState(500);
					match(IF);
					setState(501);
					match(EXISTS);
					}
				}

				setState(504);
				((UnmountTableContext)_localctx).name = tableIdentifier();
				}
				break;
			case 53:
				_localctx = new CreateDatabaseContext(_localctx);
				enterOuterAlt(_localctx, 53);
				{
				setState(505);
				match(CREATE);
				setState(506);
				match(DATABASE);
				setState(510);
				_la = _input.LA(1);
				if (_la==IF) {
					{
					setState(507);
					match(IF);
					setState(508);
					match(NOT);
					setState(509);
					match(EXISTS);
					}
				}

				setState(512);
				((CreateDatabaseContext)_localctx).name = identifier();
				setState(515);
				_la = _input.LA(1);
				if (_la==COMMENT) {
					{
					setState(513);
					match(COMMENT);
					setState(514);
					((CreateDatabaseContext)_localctx).comment = match(STRING);
					}
				}

				}
				break;
			case 54:
				_localctx = new RenameDatabaseContext(_localctx);
				enterOuterAlt(_localctx, 54);
				{
				setState(517);
				match(RENAME);
				setState(518);
				match(DATABASE);
				setState(519);
				((RenameDatabaseContext)_localctx).name = identifier();
				setState(520);
				match(TO);
				setState(521);
				((RenameDatabaseContext)_localctx).newName = identifier();
				}
				break;
			case 55:
				_localctx = new SetDatabaseNameContext(_localctx);
				enterOuterAlt(_localctx, 55);
				{
				setState(523);
				match(ALTER);
				setState(524);
				match(DATABASE);
				setState(525);
				((SetDatabaseNameContext)_localctx).name = identifier();
				setState(526);
				match(RENAME);
				setState(527);
				match(TO);
				setState(528);
				((SetDatabaseNameContext)_localctx).newName = identifier();
				}
				break;
			case 56:
				_localctx = new SetDatabaseCommentContext(_localctx);
				enterOuterAlt(_localctx, 56);
				{
				setState(530);
				match(ALTER);
				setState(531);
				match(DATABASE);
				setState(532);
				((SetDatabaseCommentContext)_localctx).name = identifier();
				setState(533);
				match(SET);
				setState(534);
				match(COMMENT);
				setState(535);
				((SetDatabaseCommentContext)_localctx).comment = match(STRING);
				}
				break;
			case 57:
				_localctx = new DropDatabaseContext(_localctx);
				enterOuterAlt(_localctx, 57);
				{
				setState(537);
				match(DROP);
				setState(538);
				match(DATABASE);
				setState(541);
				_la = _input.LA(1);
				if (_la==IF) {
					{
					setState(539);
					match(IF);
					setState(540);
					match(EXISTS);
					}
				}

				setState(543);
				((DropDatabaseContext)_localctx).name = identifier();
				setState(545);
				_la = _input.LA(1);
				if (_la==CASCADE) {
					{
					setState(544);
					match(CASCADE);
					}
				}

				}
				break;
			case 58:
				_localctx = new UseDatabaseContext(_localctx);
				enterOuterAlt(_localctx, 58);
				{
				setState(547);
				match(USE);
				setState(548);
				((UseDatabaseContext)_localctx).db = identifier();
				}
				break;
			case 59:
				_localctx = new CreateFunctionContext(_localctx);
				enterOuterAlt(_localctx, 59);
				{
				setState(549);
				match(CREATE);
				setState(550);
				match(FUNCTION);
				setState(554);
				_la = _input.LA(1);
				if (_la==IF) {
					{
					setState(551);
					match(IF);
					setState(552);
					match(NOT);
					setState(553);
					match(EXISTS);
					}
				}

				setState(556);
				((CreateFunctionContext)_localctx).name = funcIdentifier();
				setState(557);
				match(OPTIONS);
				setState(558);
				propertyList();
				}
				break;
			case 60:
				_localctx = new RenameFunctionContext(_localctx);
				enterOuterAlt(_localctx, 60);
				{
				setState(560);
				match(RENAME);
				setState(561);
				match(FUNCTION);
				setState(562);
				((RenameFunctionContext)_localctx).name = funcIdentifier();
				setState(563);
				match(TO);
				setState(564);
				((RenameFunctionContext)_localctx).newName = funcIdentifier();
				}
				break;
			case 61:
				_localctx = new SetFunctionNameContext(_localctx);
				enterOuterAlt(_localctx, 61);
				{
				setState(566);
				match(ALTER);
				setState(567);
				match(FUNCTION);
				setState(568);
				((SetFunctionNameContext)_localctx).name = funcIdentifier();
				setState(569);
				match(RENAME);
				setState(570);
				match(TO);
				setState(571);
				((SetFunctionNameContext)_localctx).newName = funcIdentifier();
				}
				break;
			case 62:
				_localctx = new SetFunctionPropertiesContext(_localctx);
				enterOuterAlt(_localctx, 62);
				{
				setState(573);
				match(ALTER);
				setState(574);
				match(FUNCTION);
				setState(575);
				((SetFunctionPropertiesContext)_localctx).name = funcIdentifier();
				setState(576);
				match(SET);
				setState(577);
				match(OPTIONS);
				setState(578);
				propertyList();
				}
				break;
			case 63:
				_localctx = new DropFunctionContext(_localctx);
				enterOuterAlt(_localctx, 63);
				{
				setState(580);
				match(DROP);
				setState(581);
				match(FUNCTION);
				setState(584);
				_la = _input.LA(1);
				if (_la==IF) {
					{
					setState(582);
					match(IF);
					setState(583);
					match(EXISTS);
					}
				}

				setState(586);
				((DropFunctionContext)_localctx).name = funcIdentifier();
				}
				break;
			case 64:
				_localctx = new CreateViewContext(_localctx);
				enterOuterAlt(_localctx, 64);
				{
				setState(587);
				match(CREATE);
				setState(588);
				match(VIEW);
				setState(592);
				_la = _input.LA(1);
				if (_la==IF) {
					{
					setState(589);
					match(IF);
					setState(590);
					match(NOT);
					setState(591);
					match(EXISTS);
					}
				}

				setState(594);
				((CreateViewContext)_localctx).name = tableIdentifier();
				setState(597);
				_la = _input.LA(1);
				if (_la==COMMENT) {
					{
					setState(595);
					match(COMMENT);
					setState(596);
					((CreateViewContext)_localctx).comment = match(STRING);
					}
				}

				setState(599);
				match(AS);
				setState(600);
				query();
				}
				break;
			case 65:
				_localctx = new RenameViewContext(_localctx);
				enterOuterAlt(_localctx, 65);
				{
				setState(602);
				match(RENAME);
				setState(603);
				match(VIEW);
				setState(604);
				((RenameViewContext)_localctx).name = tableIdentifier();
				setState(605);
				match(TO);
				setState(606);
				((RenameViewContext)_localctx).newName = tableIdentifier();
				}
				break;
			case 66:
				_localctx = new SetViewNameContext(_localctx);
				enterOuterAlt(_localctx, 66);
				{
				setState(608);
				match(ALTER);
				setState(609);
				match(VIEW);
				setState(610);
				((SetViewNameContext)_localctx).name = tableIdentifier();
				setState(611);
				match(RENAME);
				setState(612);
				match(TO);
				setState(613);
				((SetViewNameContext)_localctx).newName = tableIdentifier();
				}
				break;
			case 67:
				_localctx = new SetViewCommentContext(_localctx);
				enterOuterAlt(_localctx, 67);
				{
				setState(615);
				match(ALTER);
				setState(616);
				match(VIEW);
				setState(617);
				((SetViewCommentContext)_localctx).name = tableIdentifier();
				setState(618);
				match(SET);
				setState(619);
				match(COMMENT);
				setState(620);
				((SetViewCommentContext)_localctx).comment = match(STRING);
				}
				break;
			case 68:
				_localctx = new SetViewQueryContext(_localctx);
				enterOuterAlt(_localctx, 68);
				{
				setState(622);
				match(ALTER);
				setState(623);
				match(VIEW);
				setState(624);
				((SetViewQueryContext)_localctx).name = tableIdentifier();
				setState(625);
				match(AS);
				setState(626);
				query();
				}
				break;
			case 69:
				_localctx = new DropViewContext(_localctx);
				enterOuterAlt(_localctx, 69);
				{
				setState(628);
				match(DROP);
				setState(629);
				match(VIEW);
				setState(632);
				_la = _input.LA(1);
				if (_la==IF) {
					{
					setState(630);
					match(IF);
					setState(631);
					match(EXISTS);
					}
				}

				setState(634);
				((DropViewContext)_localctx).name = tableIdentifier();
				}
				break;
			case 70:
				_localctx = new CreateApplicationContext(_localctx);
				enterOuterAlt(_localctx, 70);
				{
				setState(635);
				match(CREATE);
				setState(636);
				match(APPLICATION);
				setState(640);
				_la = _input.LA(1);
				if (_la==IF) {
					{
					setState(637);
					match(IF);
					setState(638);
					match(NOT);
					setState(639);
					match(EXISTS);
					}
				}

				setState(642);
				((CreateApplicationContext)_localctx).name = identifier();
				setState(643);
				match(AS);
				setState(644);
				appCmds();
				}
				break;
			case 71:
				_localctx = new RenameApplicationContext(_localctx);
				enterOuterAlt(_localctx, 71);
				{
				setState(646);
				match(RENAME);
				setState(647);
				match(APPLICATION);
				setState(648);
				((RenameApplicationContext)_localctx).name = identifier();
				setState(649);
				match(TO);
				setState(650);
				((RenameApplicationContext)_localctx).newName = identifier();
				}
				break;
			case 72:
				_localctx = new SetApplicationNameContext(_localctx);
				enterOuterAlt(_localctx, 72);
				{
				setState(652);
				match(ALTER);
				setState(653);
				match(APPLICATION);
				setState(654);
				((SetApplicationNameContext)_localctx).name = identifier();
				setState(655);
				match(RENAME);
				setState(656);
				match(TO);
				setState(657);
				((SetApplicationNameContext)_localctx).newName = identifier();
				}
				break;
			case 73:
				_localctx = new SetApplicationQuerysContext(_localctx);
				enterOuterAlt(_localctx, 73);
				{
				setState(659);
				match(ALTER);
				setState(660);
				match(APPLICATION);
				setState(661);
				((SetApplicationQuerysContext)_localctx).name = identifier();
				setState(662);
				match(AS);
				setState(663);
				appCmds();
				}
				break;
			case 74:
				_localctx = new DropApplicationContext(_localctx);
				enterOuterAlt(_localctx, 74);
				{
				setState(665);
				match(DROP);
				setState(666);
				match(APPLICATION);
				setState(669);
				_la = _input.LA(1);
				if (_la==IF) {
					{
					setState(667);
					match(IF);
					setState(668);
					match(EXISTS);
					}
				}

				setState(671);
				((DropApplicationContext)_localctx).name = identifier();
				}
				break;
			case 75:
				_localctx = new CreateEventContext(_localctx);
				enterOuterAlt(_localctx, 75);
				{
				setState(672);
				match(CREATE);
				setState(675);
				_la = _input.LA(1);
				if (_la==DEFINER) {
					{
					setState(673);
					match(DEFINER);
					setState(674);
					definer();
					}
				}

				setState(677);
				match(EVENT);
				setState(681);
				_la = _input.LA(1);
				if (_la==IF) {
					{
					setState(678);
					match(IF);
					setState(679);
					match(NOT);
					setState(680);
					match(EXISTS);
					}
				}

				setState(683);
				((CreateEventContext)_localctx).name = identifier();
				setState(684);
				match(ON);
				setState(685);
				match(SCHEDULE);
				setState(686);
				match(AT);
				setState(687);
				schedule();
				setState(689);
				_la = _input.LA(1);
				if (_la==DISABLE || _la==ENABLE) {
					{
					setState(688);
					_la = _input.LA(1);
					if ( !(_la==DISABLE || _la==ENABLE) ) {
					_errHandler.recoverInline(this);
					} else {
						consume();
					}
					}
				}

				setState(693);
				_la = _input.LA(1);
				if (_la==COMMENT) {
					{
					setState(691);
					match(COMMENT);
					setState(692);
					((CreateEventContext)_localctx).comment = match(STRING);
					}
				}

				setState(695);
				match(DO);
				setState(696);
				match(CALL);
				setState(697);
				((CreateEventContext)_localctx).app = identifier();
				}
				break;
			case 76:
				_localctx = new RenameEventContext(_localctx);
				enterOuterAlt(_localctx, 76);
				{
				setState(699);
				match(RENAME);
				setState(700);
				match(EVENT);
				setState(701);
				((RenameEventContext)_localctx).name = identifier();
				setState(702);
				match(TO);
				setState(703);
				((RenameEventContext)_localctx).newName = identifier();
				}
				break;
			case 77:
				_localctx = new SetDefinerContext(_localctx);
				enterOuterAlt(_localctx, 77);
				{
				setState(705);
				match(ALTER);
				setState(706);
				match(DEFINER);
				setState(707);
				definer();
				setState(708);
				match(EVENT);
				setState(709);
				((SetDefinerContext)_localctx).name = identifier();
				}
				break;
			case 78:
				_localctx = new SetEventNameContext(_localctx);
				enterOuterAlt(_localctx, 78);
				{
				setState(711);
				match(ALTER);
				setState(712);
				match(EVENT);
				setState(713);
				((SetEventNameContext)_localctx).name = identifier();
				setState(714);
				match(RENAME);
				setState(715);
				match(TO);
				setState(716);
				((SetEventNameContext)_localctx).newName = identifier();
				}
				break;
			case 79:
				_localctx = new SetEventScheduleContext(_localctx);
				enterOuterAlt(_localctx, 79);
				{
				setState(718);
				match(ALTER);
				setState(719);
				match(EVENT);
				setState(720);
				((SetEventScheduleContext)_localctx).name = identifier();
				setState(721);
				match(ON);
				setState(722);
				match(SCHEDULE);
				setState(723);
				match(AT);
				setState(724);
				schedule();
				}
				break;
			case 80:
				_localctx = new SetEventEnableContext(_localctx);
				enterOuterAlt(_localctx, 80);
				{
				setState(726);
				match(ALTER);
				setState(727);
				match(EVENT);
				setState(728);
				((SetEventEnableContext)_localctx).name = identifier();
				setState(729);
				_la = _input.LA(1);
				if ( !(_la==DISABLE || _la==ENABLE) ) {
				_errHandler.recoverInline(this);
				} else {
					consume();
				}
				}
				break;
			case 81:
				_localctx = new DropEventContext(_localctx);
				enterOuterAlt(_localctx, 81);
				{
				setState(731);
				match(DROP);
				setState(732);
				match(EVENT);
				setState(735);
				_la = _input.LA(1);
				if (_la==IF) {
					{
					setState(733);
					match(IF);
					setState(734);
					match(EXISTS);
					}
				}

				setState(737);
				((DropEventContext)_localctx).name = identifier();
				}
				break;
			case 82:
				_localctx = new ShowSysInfoContext(_localctx);
				enterOuterAlt(_localctx, 82);
				{
				setState(738);
				match(SHOW);
				setState(739);
				match(SYSINFO);
				}
				break;
			case 83:
				_localctx = new ShowDatasourcesContext(_localctx);
				enterOuterAlt(_localctx, 83);
				{
				setState(740);
				match(SHOW);
				setState(741);
				match(DATASOURCES);
				setState(744);
				_la = _input.LA(1);
				if (_la==LIKE) {
					{
					setState(742);
					match(LIKE);
					setState(743);
					((ShowDatasourcesContext)_localctx).pattern = match(STRING);
					}
				}

				}
				break;
			case 84:
				_localctx = new ShowDatabaseContext(_localctx);
				enterOuterAlt(_localctx, 84);
				{
				setState(746);
				match(SHOW);
				setState(747);
				match(DATABASES);
				setState(750);
				_la = _input.LA(1);
				if (_la==LIKE) {
					{
					setState(748);
					match(LIKE);
					setState(749);
					((ShowDatabaseContext)_localctx).pattern = match(STRING);
					}
				}

				}
				break;
			case 85:
				_localctx = new ShowTablesContext(_localctx);
				enterOuterAlt(_localctx, 85);
				{
				setState(752);
				match(SHOW);
				setState(753);
				match(TABLES);
				setState(756);
				_la = _input.LA(1);
				if (_la==FROM || _la==IN) {
					{
					setState(754);
					_la = _input.LA(1);
					if ( !(_la==FROM || _la==IN) ) {
					_errHandler.recoverInline(this);
					} else {
						consume();
					}
					setState(755);
					((ShowTablesContext)_localctx).db = identifier();
					}
				}

				setState(760);
				_la = _input.LA(1);
				if (_la==LIKE) {
					{
					setState(758);
					match(LIKE);
					setState(759);
					((ShowTablesContext)_localctx).pattern = match(STRING);
					}
				}

				}
				break;
			case 86:
				_localctx = new ShowViewsContext(_localctx);
				enterOuterAlt(_localctx, 86);
				{
				setState(762);
				match(SHOW);
				setState(763);
				match(VIEWS);
				setState(766);
				_la = _input.LA(1);
				if (_la==FROM || _la==IN) {
					{
					setState(764);
					_la = _input.LA(1);
					if ( !(_la==FROM || _la==IN) ) {
					_errHandler.recoverInline(this);
					} else {
						consume();
					}
					setState(765);
					((ShowViewsContext)_localctx).db = identifier();
					}
				}

				setState(770);
				_la = _input.LA(1);
				if (_la==LIKE) {
					{
					setState(768);
					match(LIKE);
					setState(769);
					((ShowViewsContext)_localctx).pattern = match(STRING);
					}
				}

				}
				break;
			case 87:
				_localctx = new ShowFunctionsContext(_localctx);
				enterOuterAlt(_localctx, 87);
				{
				setState(772);
				match(SHOW);
				setState(773);
				match(FUNCTIONS);
				setState(776);
				_la = _input.LA(1);
				if (_la==FROM || _la==IN) {
					{
					setState(774);
					_la = _input.LA(1);
					if ( !(_la==FROM || _la==IN) ) {
					_errHandler.recoverInline(this);
					} else {
						consume();
					}
					setState(775);
					((ShowFunctionsContext)_localctx).db = identifier();
					}
				}

				setState(780);
				_la = _input.LA(1);
				if (_la==LIKE) {
					{
					setState(778);
					match(LIKE);
					setState(779);
					((ShowFunctionsContext)_localctx).pattern = match(STRING);
					}
				}

				}
				break;
			case 88:
				_localctx = new ShowUsersContext(_localctx);
				enterOuterAlt(_localctx, 88);
				{
				setState(782);
				match(SHOW);
				setState(783);
				match(USERS);
				setState(786);
				_la = _input.LA(1);
				if (_la==LIKE) {
					{
					setState(784);
					match(LIKE);
					setState(785);
					((ShowUsersContext)_localctx).pattern = match(STRING);
					}
				}

				}
				break;
			case 89:
				_localctx = new ShowGroupsContext(_localctx);
				enterOuterAlt(_localctx, 89);
				{
				setState(788);
				match(SHOW);
				setState(789);
				match(GROUPS);
				setState(792);
				_la = _input.LA(1);
				if (_la==LIKE) {
					{
					setState(790);
					match(LIKE);
					setState(791);
					((ShowGroupsContext)_localctx).pattern = match(STRING);
					}
				}

				}
				break;
			case 90:
				_localctx = new ShowApplicationsContext(_localctx);
				enterOuterAlt(_localctx, 90);
				{
				setState(794);
				match(SHOW);
				setState(795);
				match(APPLICATIONS);
				setState(798);
				_la = _input.LA(1);
				if (_la==LIKE) {
					{
					setState(796);
					match(LIKE);
					setState(797);
					((ShowApplicationsContext)_localctx).pattern = match(STRING);
					}
				}

				}
				break;
			case 91:
				_localctx = new DescDatasourceContext(_localctx);
				enterOuterAlt(_localctx, 91);
				{
				setState(800);
				_la = _input.LA(1);
				if ( !(_la==DESC || _la==DESCRIBE) ) {
				_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(801);
				match(DATASOURCE);
				setState(803);
				_la = _input.LA(1);
				if (_la==EXTENDED) {
					{
					setState(802);
					match(EXTENDED);
					}
				}

				setState(805);
				((DescDatasourceContext)_localctx).name = identifier();
				}
				break;
			case 92:
				_localctx = new DescDatabaseContext(_localctx);
				enterOuterAlt(_localctx, 92);
				{
				setState(806);
				_la = _input.LA(1);
				if ( !(_la==DESC || _la==DESCRIBE) ) {
				_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(807);
				match(DATABASE);
				setState(808);
				((DescDatabaseContext)_localctx).name = identifier();
				}
				break;
			case 93:
				_localctx = new DescTableContext(_localctx);
				enterOuterAlt(_localctx, 93);
				{
				setState(809);
				_la = _input.LA(1);
				if ( !(_la==DESC || _la==DESCRIBE) ) {
				_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(810);
				match(TABLE);
				setState(812);
				_la = _input.LA(1);
				if (_la==EXTENDED) {
					{
					setState(811);
					match(EXTENDED);
					}
				}

				setState(814);
				tableIdentifier();
				}
				break;
			case 94:
				_localctx = new DescViewContext(_localctx);
				enterOuterAlt(_localctx, 94);
				{
				setState(815);
				_la = _input.LA(1);
				if ( !(_la==DESC || _la==DESCRIBE) ) {
				_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(816);
				match(VIEW);
				setState(817);
				tableIdentifier();
				}
				break;
			case 95:
				_localctx = new DescFunctionContext(_localctx);
				enterOuterAlt(_localctx, 95);
				{
				setState(818);
				_la = _input.LA(1);
				if ( !(_la==DESC || _la==DESCRIBE) ) {
				_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(819);
				match(FUNCTION);
				setState(821);
				_la = _input.LA(1);
				if (_la==EXTENDED) {
					{
					setState(820);
					match(EXTENDED);
					}
				}

				setState(823);
				funcIdentifier();
				}
				break;
			case 96:
				_localctx = new DescUserContext(_localctx);
				enterOuterAlt(_localctx, 96);
				{
				setState(824);
				_la = _input.LA(1);
				if ( !(_la==DESC || _la==DESCRIBE) ) {
				_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(825);
				match(USER);
				setState(826);
				((DescUserContext)_localctx).name = identifier();
				}
				break;
			case 97:
				_localctx = new DescGroupContext(_localctx);
				enterOuterAlt(_localctx, 97);
				{
				setState(827);
				_la = _input.LA(1);
				if ( !(_la==DESC || _la==DESCRIBE) ) {
				_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(828);
				match(GROUP);
				setState(829);
				((DescGroupContext)_localctx).name = identifier();
				}
				break;
			case 98:
				_localctx = new ExplainContext(_localctx);
				enterOuterAlt(_localctx, 98);
				{
				setState(830);
				match(EXPLAIN);
				setState(832);
				_la = _input.LA(1);
				if (_la==EXTENDED) {
					{
					setState(831);
					match(EXTENDED);
					}
				}

				setState(835);
				_la = _input.LA(1);
				if (_la==PLAN) {
					{
					setState(834);
					match(PLAN);
					}
				}

				setState(837);
				query();
				}
				break;
			case 99:
				_localctx = new SetConfigurationContext(_localctx);
				enterOuterAlt(_localctx, 99);
				{
				setState(838);
				match(SET);
				setState(839);
				property();
				}
				break;
			case 100:
				_localctx = new MqlQueryContext(_localctx);
				enterOuterAlt(_localctx, 100);
				{
				setState(840);
				query();
				}
				break;
			case 101:
				_localctx = new InsertIntoContext(_localctx);
				enterOuterAlt(_localctx, 101);
				{
				setState(841);
				insertIntoCmd();
				}
				break;
			case 102:
				_localctx = new InsertOverwriteContext(_localctx);
				enterOuterAlt(_localctx, 102);
				{
				setState(842);
				insertOverwriteCmd();
				}
				break;
			case 103:
				_localctx = new CreateTemporaryViewContext(_localctx);
				enterOuterAlt(_localctx, 103);
				{
				setState(843);
				createTemporaryViewCmd();
				}
				break;
			case 104:
				_localctx = new CreateTemporaryFunctionContext(_localctx);
				enterOuterAlt(_localctx, 104);
				{
				setState(844);
				createTemporaryFunctionCmd();
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

	public static class DefinerContext extends ParserRuleContext {
		public IdentifierContext user;
		public TerminalNode EQ() { return getToken(MqlBaseParser.EQ, 0); }
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public TerminalNode CURRENT_USER() { return getToken(MqlBaseParser.CURRENT_USER, 0); }
		public DefinerContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_definer; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterDefiner(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitDefiner(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitDefiner(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DefinerContext definer() throws RecognitionException {
		DefinerContext _localctx = new DefinerContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_definer);
		try {
			setState(850);
			switch (_input.LA(1)) {
			case EQ:
				enterOuterAlt(_localctx, 1);
				{
				setState(847);
				match(EQ);
				setState(848);
				((DefinerContext)_localctx).user = identifier();
				}
				break;
			case CURRENT_USER:
				enterOuterAlt(_localctx, 2);
				{
				setState(849);
				match(CURRENT_USER);
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

	public static class ScheduleContext extends ParserRuleContext {
		public List<StarOrIntegerContext> starOrInteger() {
			return getRuleContexts(StarOrIntegerContext.class);
		}
		public StarOrIntegerContext starOrInteger(int i) {
			return getRuleContext(StarOrIntegerContext.class,i);
		}
		public ScheduleContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_schedule; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterSchedule(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitSchedule(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitSchedule(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ScheduleContext schedule() throws RecognitionException {
		ScheduleContext _localctx = new ScheduleContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_schedule);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(852);
			starOrInteger();
			setState(853);
			starOrInteger();
			setState(854);
			starOrInteger();
			setState(855);
			starOrInteger();
			setState(856);
			starOrInteger();
			setState(857);
			starOrInteger();
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

	public static class StarOrIntegerContext extends ParserRuleContext {
		public TerminalNode STAR() { return getToken(MqlBaseParser.STAR, 0); }
		public TerminalNode INTEGER_VALUE() { return getToken(MqlBaseParser.INTEGER_VALUE, 0); }
		public StarOrIntegerContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_starOrInteger; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterStarOrInteger(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitStarOrInteger(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitStarOrInteger(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StarOrIntegerContext starOrInteger() throws RecognitionException {
		StarOrIntegerContext _localctx = new StarOrIntegerContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_starOrInteger);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(859);
			_la = _input.LA(1);
			if ( !(_la==STAR || _la==INTEGER_VALUE) ) {
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

	public static class AppCmdsContext extends ParserRuleContext {
		public LastCmdContext lastCmd() {
			return getRuleContext(LastCmdContext.class,0);
		}
		public NonLastCmdListContext nonLastCmdList() {
			return getRuleContext(NonLastCmdListContext.class,0);
		}
		public AppCmdsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_appCmds; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterAppCmds(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitAppCmds(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitAppCmds(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AppCmdsContext appCmds() throws RecognitionException {
		AppCmdsContext _localctx = new AppCmdsContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_appCmds);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(864);
			_la = _input.LA(1);
			if (_la==CREATE) {
				{
				setState(861);
				nonLastCmdList();
				setState(862);
				match(T__2);
				}
			}

			setState(866);
			lastCmd();
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

	public static class NonLastCmdListContext extends ParserRuleContext {
		public List<NonLastCmdContext> nonLastCmd() {
			return getRuleContexts(NonLastCmdContext.class);
		}
		public NonLastCmdContext nonLastCmd(int i) {
			return getRuleContext(NonLastCmdContext.class,i);
		}
		public NonLastCmdListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_nonLastCmdList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterNonLastCmdList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitNonLastCmdList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitNonLastCmdList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NonLastCmdListContext nonLastCmdList() throws RecognitionException {
		NonLastCmdListContext _localctx = new NonLastCmdListContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_nonLastCmdList);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(868);
			nonLastCmd();
			setState(873);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,58,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(869);
					match(T__2);
					setState(870);
					nonLastCmd();
					}
					} 
				}
				setState(875);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,58,_ctx);
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

	public static class NonLastCmdContext extends ParserRuleContext {
		public CreateTemporaryViewCmdContext createTemporaryViewCmd() {
			return getRuleContext(CreateTemporaryViewCmdContext.class,0);
		}
		public CreateTemporaryFunctionCmdContext createTemporaryFunctionCmd() {
			return getRuleContext(CreateTemporaryFunctionCmdContext.class,0);
		}
		public NonLastCmdContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_nonLastCmd; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterNonLastCmd(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitNonLastCmd(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitNonLastCmd(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NonLastCmdContext nonLastCmd() throws RecognitionException {
		NonLastCmdContext _localctx = new NonLastCmdContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_nonLastCmd);
		try {
			setState(878);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,59,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(876);
				createTemporaryViewCmd();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(877);
				createTemporaryFunctionCmd();
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

	public static class LastCmdContext extends ParserRuleContext {
		public InsertIntoCmdContext insertIntoCmd() {
			return getRuleContext(InsertIntoCmdContext.class,0);
		}
		public InsertOverwriteCmdContext insertOverwriteCmd() {
			return getRuleContext(InsertOverwriteCmdContext.class,0);
		}
		public QueryContext query() {
			return getRuleContext(QueryContext.class,0);
		}
		public LastCmdContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_lastCmd; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterLastCmd(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitLastCmd(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitLastCmd(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LastCmdContext lastCmd() throws RecognitionException {
		LastCmdContext _localctx = new LastCmdContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_lastCmd);
		try {
			setState(883);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,60,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(880);
				insertIntoCmd();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(881);
				insertOverwriteCmd();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(882);
				query();
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

	public static class InsertIntoCmdContext extends ParserRuleContext {
		public TerminalNode INSERT() { return getToken(MqlBaseParser.INSERT, 0); }
		public TerminalNode INTO() { return getToken(MqlBaseParser.INTO, 0); }
		public TableIdentifierContext tableIdentifier() {
			return getRuleContext(TableIdentifierContext.class,0);
		}
		public QueryContext query() {
			return getRuleContext(QueryContext.class,0);
		}
		public TerminalNode TABLE() { return getToken(MqlBaseParser.TABLE, 0); }
		public InsertIntoCmdContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_insertIntoCmd; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterInsertIntoCmd(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitInsertIntoCmd(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitInsertIntoCmd(this);
			else return visitor.visitChildren(this);
		}
	}

	public final InsertIntoCmdContext insertIntoCmd() throws RecognitionException {
		InsertIntoCmdContext _localctx = new InsertIntoCmdContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_insertIntoCmd);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(885);
			match(INSERT);
			setState(886);
			match(INTO);
			setState(888);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,61,_ctx) ) {
			case 1:
				{
				setState(887);
				match(TABLE);
				}
				break;
			}
			setState(890);
			tableIdentifier();
			setState(891);
			query();
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

	public static class InsertOverwriteCmdContext extends ParserRuleContext {
		public TerminalNode INSERT() { return getToken(MqlBaseParser.INSERT, 0); }
		public TerminalNode OVERWRITE() { return getToken(MqlBaseParser.OVERWRITE, 0); }
		public TerminalNode TABLE() { return getToken(MqlBaseParser.TABLE, 0); }
		public TableIdentifierContext tableIdentifier() {
			return getRuleContext(TableIdentifierContext.class,0);
		}
		public QueryContext query() {
			return getRuleContext(QueryContext.class,0);
		}
		public InsertOverwriteCmdContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_insertOverwriteCmd; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterInsertOverwriteCmd(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitInsertOverwriteCmd(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitInsertOverwriteCmd(this);
			else return visitor.visitChildren(this);
		}
	}

	public final InsertOverwriteCmdContext insertOverwriteCmd() throws RecognitionException {
		InsertOverwriteCmdContext _localctx = new InsertOverwriteCmdContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_insertOverwriteCmd);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(893);
			match(INSERT);
			setState(894);
			match(OVERWRITE);
			setState(895);
			match(TABLE);
			setState(896);
			tableIdentifier();
			setState(897);
			query();
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

	public static class CreateTemporaryViewCmdContext extends ParserRuleContext {
		public IdentifierContext name;
		public TerminalNode CREATE() { return getToken(MqlBaseParser.CREATE, 0); }
		public TerminalNode VIEW() { return getToken(MqlBaseParser.VIEW, 0); }
		public TerminalNode AS() { return getToken(MqlBaseParser.AS, 0); }
		public QueryContext query() {
			return getRuleContext(QueryContext.class,0);
		}
		public TerminalNode TEMP() { return getToken(MqlBaseParser.TEMP, 0); }
		public TerminalNode TEMPORARY() { return getToken(MqlBaseParser.TEMPORARY, 0); }
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public TerminalNode OR() { return getToken(MqlBaseParser.OR, 0); }
		public TerminalNode REPLACE() { return getToken(MqlBaseParser.REPLACE, 0); }
		public TerminalNode CACHE() { return getToken(MqlBaseParser.CACHE, 0); }
		public CreateTemporaryViewCmdContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_createTemporaryViewCmd; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterCreateTemporaryViewCmd(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitCreateTemporaryViewCmd(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitCreateTemporaryViewCmd(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CreateTemporaryViewCmdContext createTemporaryViewCmd() throws RecognitionException {
		CreateTemporaryViewCmdContext _localctx = new CreateTemporaryViewCmdContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_createTemporaryViewCmd);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(899);
			match(CREATE);
			setState(902);
			_la = _input.LA(1);
			if (_la==OR) {
				{
				setState(900);
				match(OR);
				setState(901);
				match(REPLACE);
				}
			}

			setState(905);
			_la = _input.LA(1);
			if (_la==CACHE) {
				{
				setState(904);
				match(CACHE);
				}
			}

			setState(907);
			_la = _input.LA(1);
			if ( !(_la==TEMP || _la==TEMPORARY) ) {
			_errHandler.recoverInline(this);
			} else {
				consume();
			}
			setState(908);
			match(VIEW);
			setState(909);
			((CreateTemporaryViewCmdContext)_localctx).name = identifier();
			setState(910);
			match(AS);
			setState(911);
			query();
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

	public static class CreateTemporaryFunctionCmdContext extends ParserRuleContext {
		public IdentifierContext name;
		public TerminalNode CREATE() { return getToken(MqlBaseParser.CREATE, 0); }
		public TerminalNode FUNCTION() { return getToken(MqlBaseParser.FUNCTION, 0); }
		public TerminalNode OPTIONS() { return getToken(MqlBaseParser.OPTIONS, 0); }
		public PropertyListContext propertyList() {
			return getRuleContext(PropertyListContext.class,0);
		}
		public TerminalNode TEMP() { return getToken(MqlBaseParser.TEMP, 0); }
		public TerminalNode TEMPORARY() { return getToken(MqlBaseParser.TEMPORARY, 0); }
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public TerminalNode OR() { return getToken(MqlBaseParser.OR, 0); }
		public TerminalNode REPLACE() { return getToken(MqlBaseParser.REPLACE, 0); }
		public CreateTemporaryFunctionCmdContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_createTemporaryFunctionCmd; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterCreateTemporaryFunctionCmd(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitCreateTemporaryFunctionCmd(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitCreateTemporaryFunctionCmd(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CreateTemporaryFunctionCmdContext createTemporaryFunctionCmd() throws RecognitionException {
		CreateTemporaryFunctionCmdContext _localctx = new CreateTemporaryFunctionCmdContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_createTemporaryFunctionCmd);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(913);
			match(CREATE);
			setState(916);
			_la = _input.LA(1);
			if (_la==OR) {
				{
				setState(914);
				match(OR);
				setState(915);
				match(REPLACE);
				}
			}

			setState(918);
			_la = _input.LA(1);
			if ( !(_la==TEMP || _la==TEMPORARY) ) {
			_errHandler.recoverInline(this);
			} else {
				consume();
			}
			setState(919);
			match(FUNCTION);
			setState(920);
			((CreateTemporaryFunctionCmdContext)_localctx).name = identifier();
			setState(921);
			match(OPTIONS);
			setState(922);
			propertyList();
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

	public static class QueryContext extends ParserRuleContext {
		public TerminalNode SELECT() { return getToken(MqlBaseParser.SELECT, 0); }
		public CtesContext ctes() {
			return getRuleContext(CtesContext.class,0);
		}
		public QueryContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_query; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterQuery(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitQuery(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitQuery(this);
			else return visitor.visitChildren(this);
		}
	}

	public final QueryContext query() throws RecognitionException {
		QueryContext _localctx = new QueryContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_query);
		int _la;
		try {
			int _alt;
			setState(939);
			switch (_input.LA(1)) {
			case SELECT:
				enterOuterAlt(_localctx, 1);
				{
				setState(924);
				match(SELECT);
				setState(928);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,65,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(925);
						_la = _input.LA(1);
						if ( _la <= 0 || (_la==T__3) ) {
						_errHandler.recoverInline(this);
						} else {
							consume();
						}
						}
						} 
					}
					setState(930);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,65,_ctx);
				}
				}
				break;
			case WITH:
				enterOuterAlt(_localctx, 2);
				{
				setState(931);
				ctes();
				setState(932);
				match(SELECT);
				setState(936);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,66,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(933);
						_la = _input.LA(1);
						if ( _la <= 0 || (_la==T__3) ) {
						_errHandler.recoverInline(this);
						} else {
							consume();
						}
						}
						} 
					}
					setState(938);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,66,_ctx);
				}
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

	public static class CtesContext extends ParserRuleContext {
		public TerminalNode WITH() { return getToken(MqlBaseParser.WITH, 0); }
		public List<NamedQueryContext> namedQuery() {
			return getRuleContexts(NamedQueryContext.class);
		}
		public NamedQueryContext namedQuery(int i) {
			return getRuleContext(NamedQueryContext.class,i);
		}
		public CtesContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ctes; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterCtes(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitCtes(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitCtes(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CtesContext ctes() throws RecognitionException {
		CtesContext _localctx = new CtesContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_ctes);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(941);
			match(WITH);
			setState(942);
			namedQuery();
			setState(947);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__2) {
				{
				{
				setState(943);
				match(T__2);
				setState(944);
				namedQuery();
				}
				}
				setState(949);
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

	public static class DataTypeContext extends ParserRuleContext {
		public DataTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_dataType; }
	 
		public DataTypeContext() { }
		public void copyFrom(DataTypeContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class ComplexDataTypeContext extends DataTypeContext {
		public Token complex;
		public List<DataTypeContext> dataType() {
			return getRuleContexts(DataTypeContext.class);
		}
		public DataTypeContext dataType(int i) {
			return getRuleContext(DataTypeContext.class,i);
		}
		public TerminalNode ARRAY() { return getToken(MqlBaseParser.ARRAY, 0); }
		public TerminalNode MAP() { return getToken(MqlBaseParser.MAP, 0); }
		public TerminalNode STRUCT() { return getToken(MqlBaseParser.STRUCT, 0); }
		public TerminalNode NEQ() { return getToken(MqlBaseParser.NEQ, 0); }
		public ComplexColTypeListContext complexColTypeList() {
			return getRuleContext(ComplexColTypeListContext.class,0);
		}
		public ComplexDataTypeContext(DataTypeContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterComplexDataType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitComplexDataType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitComplexDataType(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class PrimitiveDataTypeContext extends DataTypeContext {
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public List<TerminalNode> INTEGER_VALUE() { return getTokens(MqlBaseParser.INTEGER_VALUE); }
		public TerminalNode INTEGER_VALUE(int i) {
			return getToken(MqlBaseParser.INTEGER_VALUE, i);
		}
		public PrimitiveDataTypeContext(DataTypeContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterPrimitiveDataType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitPrimitiveDataType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitPrimitiveDataType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DataTypeContext dataType() throws RecognitionException {
		DataTypeContext _localctx = new DataTypeContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_dataType);
		int _la;
		try {
			setState(984);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,73,_ctx) ) {
			case 1:
				_localctx = new ComplexDataTypeContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(950);
				((ComplexDataTypeContext)_localctx).complex = match(ARRAY);
				setState(951);
				match(T__4);
				setState(952);
				dataType();
				setState(953);
				match(T__5);
				}
				break;
			case 2:
				_localctx = new ComplexDataTypeContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(955);
				((ComplexDataTypeContext)_localctx).complex = match(MAP);
				setState(956);
				match(T__4);
				setState(957);
				dataType();
				setState(958);
				match(T__2);
				setState(959);
				dataType();
				setState(960);
				match(T__5);
				}
				break;
			case 3:
				_localctx = new ComplexDataTypeContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(962);
				((ComplexDataTypeContext)_localctx).complex = match(STRUCT);
				setState(969);
				switch (_input.LA(1)) {
				case T__4:
					{
					setState(963);
					match(T__4);
					setState(965);
					_la = _input.LA(1);
					if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ACCOUNT) | (1L << ADD) | (1L << ALTER) | (1L << APPLICATION) | (1L << APPLICATIONS) | (1L << ARRAY) | (1L << AS) | (1L << CACHE) | (1L << CASCADE) | (1L << COLUMN) | (1L << COLUMNS) | (1L << DATABASE) | (1L << DATABASES) | (1L << DATASOURCE) | (1L << DATASOURCES) | (1L << FUNCTION) | (1L << FUNCTIONS) | (1L << GRANT) | (1L << GROUP) | (1L << GROUPS))) != 0) || ((((_la - 72)) & ~0x3f) == 0 && ((1L << (_la - 72)) & ((1L << (ORG - 72)) | (1L << (REMOVE - 72)) | (1L << (RENAME - 72)) | (1L << (REVOKE - 72)) | (1L << (SA - 72)) | (1L << (SET - 72)) | (1L << (SHOW - 72)) | (1L << (TABLE - 72)) | (1L << (TABLES - 72)) | (1L << (TO - 72)) | (1L << (TYPE - 72)) | (1L << (USER - 72)) | (1L << (VIEW - 72)) | (1L << (VIEWS - 72)) | (1L << (WITH - 72)) | (1L << (IDENTIFIER - 72)))) != 0)) {
						{
						setState(964);
						complexColTypeList();
						}
					}

					setState(967);
					match(T__5);
					}
					break;
				case NEQ:
					{
					setState(968);
					match(NEQ);
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				break;
			case 4:
				_localctx = new PrimitiveDataTypeContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(971);
				identifier();
				setState(982);
				_la = _input.LA(1);
				if (_la==T__0) {
					{
					setState(972);
					match(T__0);
					setState(973);
					match(INTEGER_VALUE);
					setState(978);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==T__2) {
						{
						{
						setState(974);
						match(T__2);
						setState(975);
						match(INTEGER_VALUE);
						}
						}
						setState(980);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					setState(981);
					match(T__1);
					}
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

	public static class ColTypeListContext extends ParserRuleContext {
		public List<ColTypeContext> colType() {
			return getRuleContexts(ColTypeContext.class);
		}
		public ColTypeContext colType(int i) {
			return getRuleContext(ColTypeContext.class,i);
		}
		public ColTypeListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_colTypeList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterColTypeList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitColTypeList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitColTypeList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ColTypeListContext colTypeList() throws RecognitionException {
		ColTypeListContext _localctx = new ColTypeListContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_colTypeList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(986);
			colType();
			setState(991);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__2) {
				{
				{
				setState(987);
				match(T__2);
				setState(988);
				colType();
				}
				}
				setState(993);
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

	public static class ColTypeContext extends ParserRuleContext {
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public DataTypeContext dataType() {
			return getRuleContext(DataTypeContext.class,0);
		}
		public ColTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_colType; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterColType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitColType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitColType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ColTypeContext colType() throws RecognitionException {
		ColTypeContext _localctx = new ColTypeContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_colType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(994);
			identifier();
			setState(995);
			dataType();
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

	public static class ComplexColTypeListContext extends ParserRuleContext {
		public List<ComplexColTypeContext> complexColType() {
			return getRuleContexts(ComplexColTypeContext.class);
		}
		public ComplexColTypeContext complexColType(int i) {
			return getRuleContext(ComplexColTypeContext.class,i);
		}
		public ComplexColTypeListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_complexColTypeList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterComplexColTypeList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitComplexColTypeList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitComplexColTypeList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ComplexColTypeListContext complexColTypeList() throws RecognitionException {
		ComplexColTypeListContext _localctx = new ComplexColTypeListContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_complexColTypeList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(997);
			complexColType();
			setState(1002);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__2) {
				{
				{
				setState(998);
				match(T__2);
				setState(999);
				complexColType();
				}
				}
				setState(1004);
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

	public static class ComplexColTypeContext extends ParserRuleContext {
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public DataTypeContext dataType() {
			return getRuleContext(DataTypeContext.class,0);
		}
		public ComplexColTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_complexColType; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterComplexColType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitComplexColType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitComplexColType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ComplexColTypeContext complexColType() throws RecognitionException {
		ComplexColTypeContext _localctx = new ComplexColTypeContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_complexColType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1005);
			identifier();
			setState(1006);
			match(T__6);
			setState(1007);
			dataType();
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

	public static class NamedQueryContext extends ParserRuleContext {
		public IdentifierContext name;
		public QueryContext query() {
			return getRuleContext(QueryContext.class,0);
		}
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public TerminalNode AS() { return getToken(MqlBaseParser.AS, 0); }
		public NamedQueryContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_namedQuery; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterNamedQuery(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitNamedQuery(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitNamedQuery(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NamedQueryContext namedQuery() throws RecognitionException {
		NamedQueryContext _localctx = new NamedQueryContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_namedQuery);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1009);
			((NamedQueryContext)_localctx).name = identifier();
			setState(1011);
			_la = _input.LA(1);
			if (_la==AS) {
				{
				setState(1010);
				match(AS);
				}
			}

			setState(1013);
			match(T__0);
			setState(1014);
			query();
			setState(1015);
			match(T__1);
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

	public static class MountTableListContext extends ParserRuleContext {
		public List<MountTableOptionsContext> mountTableOptions() {
			return getRuleContexts(MountTableOptionsContext.class);
		}
		public MountTableOptionsContext mountTableOptions(int i) {
			return getRuleContext(MountTableOptionsContext.class,i);
		}
		public MountTableListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_mountTableList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterMountTableList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitMountTableList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitMountTableList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MountTableListContext mountTableList() throws RecognitionException {
		MountTableListContext _localctx = new MountTableListContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_mountTableList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1017);
			mountTableOptions();
			setState(1022);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__2) {
				{
				{
				setState(1018);
				match(T__2);
				setState(1019);
				mountTableOptions();
				}
				}
				setState(1024);
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

	public static class MountTableOptionsContext extends ParserRuleContext {
		public ColTypeListContext columns;
		public TableIdentifierContext tableIdentifier() {
			return getRuleContext(TableIdentifierContext.class,0);
		}
		public TerminalNode OPTIONS() { return getToken(MqlBaseParser.OPTIONS, 0); }
		public PropertyListContext propertyList() {
			return getRuleContext(PropertyListContext.class,0);
		}
		public ColTypeListContext colTypeList() {
			return getRuleContext(ColTypeListContext.class,0);
		}
		public MountTableOptionsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_mountTableOptions; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterMountTableOptions(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitMountTableOptions(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitMountTableOptions(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MountTableOptionsContext mountTableOptions() throws RecognitionException {
		MountTableOptionsContext _localctx = new MountTableOptionsContext(_ctx, getState());
		enterRule(_localctx, 44, RULE_mountTableOptions);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1025);
			tableIdentifier();
			setState(1030);
			_la = _input.LA(1);
			if (_la==T__0) {
				{
				setState(1026);
				match(T__0);
				setState(1027);
				((MountTableOptionsContext)_localctx).columns = colTypeList();
				setState(1028);
				match(T__1);
				}
			}

			setState(1032);
			match(OPTIONS);
			setState(1033);
			propertyList();
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

	public static class PrivilegeListContext extends ParserRuleContext {
		public List<PrivilegeContext> privilege() {
			return getRuleContexts(PrivilegeContext.class);
		}
		public PrivilegeContext privilege(int i) {
			return getRuleContext(PrivilegeContext.class,i);
		}
		public PrivilegeListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_privilegeList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterPrivilegeList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitPrivilegeList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitPrivilegeList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PrivilegeListContext privilegeList() throws RecognitionException {
		PrivilegeListContext _localctx = new PrivilegeListContext(_ctx, getState());
		enterRule(_localctx, 46, RULE_privilegeList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1035);
			privilege();
			setState(1040);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__2) {
				{
				{
				setState(1036);
				match(T__2);
				setState(1037);
				privilege();
				}
				}
				setState(1042);
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

	public static class PrivilegeContext extends ParserRuleContext {
		public TerminalNode ACCOUNT() { return getToken(MqlBaseParser.ACCOUNT, 0); }
		public TerminalNode DDL() { return getToken(MqlBaseParser.DDL, 0); }
		public TerminalNode DMLON() { return getToken(MqlBaseParser.DMLON, 0); }
		public PrivilegeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_privilege; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterPrivilege(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitPrivilege(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitPrivilege(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PrivilegeContext privilege() throws RecognitionException {
		PrivilegeContext _localctx = new PrivilegeContext(_ctx, getState());
		enterRule(_localctx, 48, RULE_privilege);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1043);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ACCOUNT) | (1L << DDL) | (1L << DMLON))) != 0)) ) {
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

	public static class QualifiedColumnListContext extends ParserRuleContext {
		public List<ColumnIdentifierContext> columnIdentifier() {
			return getRuleContexts(ColumnIdentifierContext.class);
		}
		public ColumnIdentifierContext columnIdentifier(int i) {
			return getRuleContext(ColumnIdentifierContext.class,i);
		}
		public QualifiedColumnListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_qualifiedColumnList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterQualifiedColumnList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitQualifiedColumnList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitQualifiedColumnList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final QualifiedColumnListContext qualifiedColumnList() throws RecognitionException {
		QualifiedColumnListContext _localctx = new QualifiedColumnListContext(_ctx, getState());
		enterRule(_localctx, 50, RULE_qualifiedColumnList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1045);
			columnIdentifier();
			setState(1050);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__2) {
				{
				{
				setState(1046);
				match(T__2);
				setState(1047);
				columnIdentifier();
				}
				}
				setState(1052);
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

	public static class ColumnIdentifierContext extends ParserRuleContext {
		public IdentifierContext db;
		public IdentifierStarListContext table;
		public IdentifierStarListContext colunm;
		public List<IdentifierStarListContext> identifierStarList() {
			return getRuleContexts(IdentifierStarListContext.class);
		}
		public IdentifierStarListContext identifierStarList(int i) {
			return getRuleContext(IdentifierStarListContext.class,i);
		}
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public ColumnIdentifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_columnIdentifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterColumnIdentifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitColumnIdentifier(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitColumnIdentifier(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ColumnIdentifierContext columnIdentifier() throws RecognitionException {
		ColumnIdentifierContext _localctx = new ColumnIdentifierContext(_ctx, getState());
		enterRule(_localctx, 52, RULE_columnIdentifier);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1056);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,81,_ctx) ) {
			case 1:
				{
				setState(1053);
				((ColumnIdentifierContext)_localctx).db = identifier();
				setState(1054);
				match(T__7);
				}
				break;
			}
			setState(1058);
			((ColumnIdentifierContext)_localctx).table = identifierStarList();
			setState(1059);
			match(T__7);
			setState(1060);
			((ColumnIdentifierContext)_localctx).colunm = identifierStarList();
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

	public static class IdentifierStarListContext extends ParserRuleContext {
		public List<IdentifierContext> identifier() {
			return getRuleContexts(IdentifierContext.class);
		}
		public IdentifierContext identifier(int i) {
			return getRuleContext(IdentifierContext.class,i);
		}
		public TerminalNode STAR() { return getToken(MqlBaseParser.STAR, 0); }
		public IdentifierStarListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_identifierStarList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterIdentifierStarList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitIdentifierStarList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitIdentifierStarList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IdentifierStarListContext identifierStarList() throws RecognitionException {
		IdentifierStarListContext _localctx = new IdentifierStarListContext(_ctx, getState());
		enterRule(_localctx, 54, RULE_identifierStarList);
		int _la;
		try {
			setState(1086);
			switch (_input.LA(1)) {
			case T__8:
				enterOuterAlt(_localctx, 1);
				{
				setState(1062);
				match(T__8);
				setState(1063);
				identifier();
				setState(1068);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__2) {
					{
					{
					setState(1064);
					match(T__2);
					setState(1065);
					identifier();
					}
					}
					setState(1070);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(1071);
				match(T__9);
				}
				break;
			case T__10:
				enterOuterAlt(_localctx, 2);
				{
				setState(1073);
				match(T__10);
				setState(1074);
				identifier();
				setState(1079);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__2) {
					{
					{
					setState(1075);
					match(T__2);
					setState(1076);
					identifier();
					}
					}
					setState(1081);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(1082);
				match(T__11);
				}
				break;
			case ACCOUNT:
			case ADD:
			case ALTER:
			case APPLICATION:
			case APPLICATIONS:
			case ARRAY:
			case AS:
			case CACHE:
			case CASCADE:
			case COLUMN:
			case COLUMNS:
			case DATABASE:
			case DATABASES:
			case DATASOURCE:
			case DATASOURCES:
			case FUNCTION:
			case FUNCTIONS:
			case GRANT:
			case GROUP:
			case GROUPS:
			case ORG:
			case REMOVE:
			case RENAME:
			case REVOKE:
			case SA:
			case SET:
			case SHOW:
			case TABLE:
			case TABLES:
			case TO:
			case TYPE:
			case USER:
			case VIEW:
			case VIEWS:
			case WITH:
			case IDENTIFIER:
				enterOuterAlt(_localctx, 3);
				{
				setState(1084);
				identifier();
				}
				break;
			case STAR:
				enterOuterAlt(_localctx, 4);
				{
				setState(1085);
				match(STAR);
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

	public static class AddUserContext extends ParserRuleContext {
		public TerminalNode ADD() { return getToken(MqlBaseParser.ADD, 0); }
		public TerminalNode USER() { return getToken(MqlBaseParser.USER, 0); }
		public IdentifierListContext identifierList() {
			return getRuleContext(IdentifierListContext.class,0);
		}
		public AddUserContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_addUser; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterAddUser(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitAddUser(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitAddUser(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AddUserContext addUser() throws RecognitionException {
		AddUserContext _localctx = new AddUserContext(_ctx, getState());
		enterRule(_localctx, 56, RULE_addUser);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1088);
			match(ADD);
			setState(1089);
			match(USER);
			setState(1090);
			identifierList();
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

	public static class RemoveUserContext extends ParserRuleContext {
		public TerminalNode REMOVE() { return getToken(MqlBaseParser.REMOVE, 0); }
		public TerminalNode USER() { return getToken(MqlBaseParser.USER, 0); }
		public IdentifierListContext identifierList() {
			return getRuleContext(IdentifierListContext.class,0);
		}
		public RemoveUserContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_removeUser; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterRemoveUser(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitRemoveUser(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitRemoveUser(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RemoveUserContext removeUser() throws RecognitionException {
		RemoveUserContext _localctx = new RemoveUserContext(_ctx, getState());
		enterRule(_localctx, 58, RULE_removeUser);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1092);
			match(REMOVE);
			setState(1093);
			match(USER);
			setState(1094);
			identifierList();
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

	public static class IdentifierListContext extends ParserRuleContext {
		public List<IdentifierContext> identifier() {
			return getRuleContexts(IdentifierContext.class);
		}
		public IdentifierContext identifier(int i) {
			return getRuleContext(IdentifierContext.class,i);
		}
		public IdentifierListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_identifierList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterIdentifierList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitIdentifierList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitIdentifierList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IdentifierListContext identifierList() throws RecognitionException {
		IdentifierListContext _localctx = new IdentifierListContext(_ctx, getState());
		enterRule(_localctx, 60, RULE_identifierList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1096);
			identifier();
			setState(1101);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__2) {
				{
				{
				setState(1097);
				match(T__2);
				setState(1098);
				identifier();
				}
				}
				setState(1103);
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

	public static class FuncIdentifierContext extends ParserRuleContext {
		public IdentifierContext db;
		public IdentifierContext func;
		public List<IdentifierContext> identifier() {
			return getRuleContexts(IdentifierContext.class);
		}
		public IdentifierContext identifier(int i) {
			return getRuleContext(IdentifierContext.class,i);
		}
		public FuncIdentifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_funcIdentifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterFuncIdentifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitFuncIdentifier(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitFuncIdentifier(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FuncIdentifierContext funcIdentifier() throws RecognitionException {
		FuncIdentifierContext _localctx = new FuncIdentifierContext(_ctx, getState());
		enterRule(_localctx, 62, RULE_funcIdentifier);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1107);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,86,_ctx) ) {
			case 1:
				{
				setState(1104);
				((FuncIdentifierContext)_localctx).db = identifier();
				setState(1105);
				match(T__7);
				}
				break;
			}
			setState(1109);
			((FuncIdentifierContext)_localctx).func = identifier();
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

	public static class TableIdentifierContext extends ParserRuleContext {
		public IdentifierContext db;
		public IdentifierContext table;
		public List<IdentifierContext> identifier() {
			return getRuleContexts(IdentifierContext.class);
		}
		public IdentifierContext identifier(int i) {
			return getRuleContext(IdentifierContext.class,i);
		}
		public TableIdentifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_tableIdentifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterTableIdentifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitTableIdentifier(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitTableIdentifier(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TableIdentifierContext tableIdentifier() throws RecognitionException {
		TableIdentifierContext _localctx = new TableIdentifierContext(_ctx, getState());
		enterRule(_localctx, 64, RULE_tableIdentifier);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1114);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,87,_ctx) ) {
			case 1:
				{
				setState(1111);
				((TableIdentifierContext)_localctx).db = identifier();
				setState(1112);
				match(T__7);
				}
				break;
			}
			setState(1116);
			((TableIdentifierContext)_localctx).table = identifier();
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

	public static class PropertyListContext extends ParserRuleContext {
		public List<PropertyContext> property() {
			return getRuleContexts(PropertyContext.class);
		}
		public PropertyContext property(int i) {
			return getRuleContext(PropertyContext.class,i);
		}
		public PropertyListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_propertyList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterPropertyList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitPropertyList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitPropertyList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PropertyListContext propertyList() throws RecognitionException {
		PropertyListContext _localctx = new PropertyListContext(_ctx, getState());
		enterRule(_localctx, 66, RULE_propertyList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1118);
			match(T__0);
			setState(1119);
			property();
			setState(1124);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__2) {
				{
				{
				setState(1120);
				match(T__2);
				setState(1121);
				property();
				}
				}
				setState(1126);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(1127);
			match(T__1);
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

	public static class PropertyContext extends ParserRuleContext {
		public PropertyKeyContext key;
		public Token value;
		public PropertyKeyContext propertyKey() {
			return getRuleContext(PropertyKeyContext.class,0);
		}
		public TerminalNode STRING() { return getToken(MqlBaseParser.STRING, 0); }
		public TerminalNode EQ() { return getToken(MqlBaseParser.EQ, 0); }
		public PropertyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_property; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterProperty(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitProperty(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitProperty(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PropertyContext property() throws RecognitionException {
		PropertyContext _localctx = new PropertyContext(_ctx, getState());
		enterRule(_localctx, 68, RULE_property);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1129);
			((PropertyContext)_localctx).key = propertyKey();
			setState(1134);
			_la = _input.LA(1);
			if (_la==EQ || _la==STRING) {
				{
				setState(1131);
				_la = _input.LA(1);
				if (_la==EQ) {
					{
					setState(1130);
					match(EQ);
					}
				}

				setState(1133);
				((PropertyContext)_localctx).value = match(STRING);
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

	public static class PropertyKeyContext extends ParserRuleContext {
		public List<IdentifierContext> identifier() {
			return getRuleContexts(IdentifierContext.class);
		}
		public IdentifierContext identifier(int i) {
			return getRuleContext(IdentifierContext.class,i);
		}
		public TerminalNode STRING() { return getToken(MqlBaseParser.STRING, 0); }
		public PropertyKeyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_propertyKey; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterPropertyKey(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitPropertyKey(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitPropertyKey(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PropertyKeyContext propertyKey() throws RecognitionException {
		PropertyKeyContext _localctx = new PropertyKeyContext(_ctx, getState());
		enterRule(_localctx, 70, RULE_propertyKey);
		int _la;
		try {
			setState(1145);
			switch (_input.LA(1)) {
			case ACCOUNT:
			case ADD:
			case ALTER:
			case APPLICATION:
			case APPLICATIONS:
			case ARRAY:
			case AS:
			case CACHE:
			case CASCADE:
			case COLUMN:
			case COLUMNS:
			case DATABASE:
			case DATABASES:
			case DATASOURCE:
			case DATASOURCES:
			case FUNCTION:
			case FUNCTIONS:
			case GRANT:
			case GROUP:
			case GROUPS:
			case ORG:
			case REMOVE:
			case RENAME:
			case REVOKE:
			case SA:
			case SET:
			case SHOW:
			case TABLE:
			case TABLES:
			case TO:
			case TYPE:
			case USER:
			case VIEW:
			case VIEWS:
			case WITH:
			case IDENTIFIER:
				enterOuterAlt(_localctx, 1);
				{
				setState(1136);
				identifier();
				setState(1141);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__7) {
					{
					{
					setState(1137);
					match(T__7);
					setState(1138);
					identifier();
					}
					}
					setState(1143);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
				break;
			case STRING:
				enterOuterAlt(_localctx, 2);
				{
				setState(1144);
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

	public static class PasswordContext extends ParserRuleContext {
		public PasswordContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_password; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterPassword(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitPassword(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitPassword(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PasswordContext password() throws RecognitionException {
		PasswordContext _localctx = new PasswordContext(_ctx, getState());
		enterRule(_localctx, 72, RULE_password);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1150);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,93,_ctx);
			while ( _alt!=1 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1+1 ) {
					{
					{
					setState(1147);
					matchWildcard();
					}
					} 
				}
				setState(1152);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,93,_ctx);
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
		public TerminalNode IDENTIFIER() { return getToken(MqlBaseParser.IDENTIFIER, 0); }
		public NonReservedContext nonReserved() {
			return getRuleContext(NonReservedContext.class,0);
		}
		public IdentifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_identifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterIdentifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitIdentifier(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitIdentifier(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IdentifierContext identifier() throws RecognitionException {
		IdentifierContext _localctx = new IdentifierContext(_ctx, getState());
		enterRule(_localctx, 74, RULE_identifier);
		try {
			setState(1155);
			switch (_input.LA(1)) {
			case IDENTIFIER:
				enterOuterAlt(_localctx, 1);
				{
				setState(1153);
				match(IDENTIFIER);
				}
				break;
			case ACCOUNT:
			case ADD:
			case ALTER:
			case APPLICATION:
			case APPLICATIONS:
			case ARRAY:
			case AS:
			case CACHE:
			case CASCADE:
			case COLUMN:
			case COLUMNS:
			case DATABASE:
			case DATABASES:
			case DATASOURCE:
			case DATASOURCES:
			case FUNCTION:
			case FUNCTIONS:
			case GRANT:
			case GROUP:
			case GROUPS:
			case ORG:
			case REMOVE:
			case RENAME:
			case REVOKE:
			case SA:
			case SET:
			case SHOW:
			case TABLE:
			case TABLES:
			case TO:
			case TYPE:
			case USER:
			case VIEW:
			case VIEWS:
			case WITH:
				enterOuterAlt(_localctx, 2);
				{
				setState(1154);
				nonReserved();
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

	public static class GeneralIdentifierContext extends ParserRuleContext {
		public List<IdentifierContext> identifier() {
			return getRuleContexts(IdentifierContext.class);
		}
		public IdentifierContext identifier(int i) {
			return getRuleContext(IdentifierContext.class,i);
		}
		public GeneralIdentifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_generalIdentifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterGeneralIdentifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitGeneralIdentifier(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitGeneralIdentifier(this);
			else return visitor.visitChildren(this);
		}
	}

	public final GeneralIdentifierContext generalIdentifier() throws RecognitionException {
		GeneralIdentifierContext _localctx = new GeneralIdentifierContext(_ctx, getState());
		enterRule(_localctx, 76, RULE_generalIdentifier);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1158);
			_la = _input.LA(1);
			if (_la==T__12) {
				{
				setState(1157);
				match(T__12);
				}
			}

			setState(1160);
			identifier();
			setState(1165);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__7 || _la==T__12) {
				{
				{
				setState(1161);
				_la = _input.LA(1);
				if ( !(_la==T__7 || _la==T__12) ) {
				_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(1162);
				identifier();
				}
				}
				setState(1167);
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

	public static class NonReservedContext extends ParserRuleContext {
		public TerminalNode ARRAY() { return getToken(MqlBaseParser.ARRAY, 0); }
		public TerminalNode SHOW() { return getToken(MqlBaseParser.SHOW, 0); }
		public TerminalNode TABLES() { return getToken(MqlBaseParser.TABLES, 0); }
		public TerminalNode TABLE() { return getToken(MqlBaseParser.TABLE, 0); }
		public TerminalNode COLUMNS() { return getToken(MqlBaseParser.COLUMNS, 0); }
		public TerminalNode COLUMN() { return getToken(MqlBaseParser.COLUMN, 0); }
		public TerminalNode FUNCTIONS() { return getToken(MqlBaseParser.FUNCTIONS, 0); }
		public TerminalNode DATABASES() { return getToken(MqlBaseParser.DATABASES, 0); }
		public TerminalNode FUNCTION() { return getToken(MqlBaseParser.FUNCTION, 0); }
		public List<TerminalNode> DATABASE() { return getTokens(MqlBaseParser.DATABASE); }
		public TerminalNode DATABASE(int i) {
			return getToken(MqlBaseParser.DATABASE, i);
		}
		public TerminalNode DATASOURCES() { return getToken(MqlBaseParser.DATASOURCES, 0); }
		public List<TerminalNode> DATASOURCE() { return getTokens(MqlBaseParser.DATASOURCE); }
		public TerminalNode DATASOURCE(int i) {
			return getToken(MqlBaseParser.DATASOURCE, i);
		}
		public TerminalNode ADD() { return getToken(MqlBaseParser.ADD, 0); }
		public TerminalNode REMOVE() { return getToken(MqlBaseParser.REMOVE, 0); }
		public TerminalNode GRANT() { return getToken(MqlBaseParser.GRANT, 0); }
		public TerminalNode REVOKE() { return getToken(MqlBaseParser.REVOKE, 0); }
		public TerminalNode ALTER() { return getToken(MqlBaseParser.ALTER, 0); }
		public TerminalNode RENAME() { return getToken(MqlBaseParser.RENAME, 0); }
		public TerminalNode TO() { return getToken(MqlBaseParser.TO, 0); }
		public TerminalNode SET() { return getToken(MqlBaseParser.SET, 0); }
		public TerminalNode USER() { return getToken(MqlBaseParser.USER, 0); }
		public TerminalNode TYPE() { return getToken(MqlBaseParser.TYPE, 0); }
		public TerminalNode AS() { return getToken(MqlBaseParser.AS, 0); }
		public TerminalNode SA() { return getToken(MqlBaseParser.SA, 0); }
		public TerminalNode ORG() { return getToken(MqlBaseParser.ORG, 0); }
		public TerminalNode GROUP() { return getToken(MqlBaseParser.GROUP, 0); }
		public TerminalNode GROUPS() { return getToken(MqlBaseParser.GROUPS, 0); }
		public TerminalNode VIEW() { return getToken(MqlBaseParser.VIEW, 0); }
		public TerminalNode VIEWS() { return getToken(MqlBaseParser.VIEWS, 0); }
		public TerminalNode ACCOUNT() { return getToken(MqlBaseParser.ACCOUNT, 0); }
		public TerminalNode APPLICATION() { return getToken(MqlBaseParser.APPLICATION, 0); }
		public TerminalNode APPLICATIONS() { return getToken(MqlBaseParser.APPLICATIONS, 0); }
		public TerminalNode CASCADE() { return getToken(MqlBaseParser.CASCADE, 0); }
		public TerminalNode CACHE() { return getToken(MqlBaseParser.CACHE, 0); }
		public TerminalNode WITH() { return getToken(MqlBaseParser.WITH, 0); }
		public NonReservedContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_nonReserved; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterNonReserved(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitNonReserved(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitNonReserved(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NonReservedContext nonReserved() throws RecognitionException {
		NonReservedContext _localctx = new NonReservedContext(_ctx, getState());
		enterRule(_localctx, 78, RULE_nonReserved);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1168);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ACCOUNT) | (1L << ADD) | (1L << ALTER) | (1L << APPLICATION) | (1L << APPLICATIONS) | (1L << ARRAY) | (1L << AS) | (1L << CACHE) | (1L << CASCADE) | (1L << COLUMN) | (1L << COLUMNS) | (1L << DATABASE) | (1L << DATABASES) | (1L << DATASOURCE) | (1L << DATASOURCES) | (1L << FUNCTION) | (1L << FUNCTIONS) | (1L << GRANT) | (1L << GROUP) | (1L << GROUPS))) != 0) || ((((_la - 72)) & ~0x3f) == 0 && ((1L << (_la - 72)) & ((1L << (ORG - 72)) | (1L << (REMOVE - 72)) | (1L << (RENAME - 72)) | (1L << (REVOKE - 72)) | (1L << (SA - 72)) | (1L << (SET - 72)) | (1L << (SHOW - 72)) | (1L << (TABLE - 72)) | (1L << (TABLES - 72)) | (1L << (TO - 72)) | (1L << (TYPE - 72)) | (1L << (USER - 72)) | (1L << (VIEW - 72)) | (1L << (VIEWS - 72)) | (1L << (WITH - 72)))) != 0)) ) {
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

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3n\u0495\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\3\2\3\2\3\2\3"+
		"\3\3\3\3\3\3\3\3\3\5\3[\n\3\3\3\3\3\3\3\5\3`\n\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\5\3z\n\3\3\3\3\3\5\3~\n\3\3\3\3\3\3\3\3\3\3\3\5\3\u0085\n\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\5\3\u00b0\n\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\5"+
		"\3\u0124\n\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3\u0143\n"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3\u014b\n\3\3\3\3\3\3\3\5\3\u0150\n\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3\u016b\n\3\3\3\3\3\3\3\3\3\3\3\5\3\u0172"+
		"\n\3\3\3\3\3\3\3\3\3\5\3\u0178\n\3\3\3\3\3\5\3\u017c\n\3\3\3\3\3\3\3\3"+
		"\3\3\3\5\3\u0183\n\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3\u01a1"+
		"\n\3\3\3\3\3\3\3\5\3\u01a6\n\3\3\3\3\3\3\3\3\3\5\3\u01ac\n\3\3\3\3\3\3"+
		"\3\3\3\3\3\5\3\u01b3\n\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3\u01bd\n\3"+
		"\3\3\3\3\3\3\3\3\5\3\u01c3\n\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3\u01e9\n\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3\u01f9\n\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\5\3\u0201\n\3\3\3\3\3\3\3\5\3\u0206\n\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\5"+
		"\3\u0220\n\3\3\3\3\3\5\3\u0224\n\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3\u022d"+
		"\n\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3\u024b\n\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\5\3\u0253\n\3\3\3\3\3\3\3\5\3\u0258\n\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3\u027b\n\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\5\3\u0283\n\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3"+
		"\u02a0\n\3\3\3\3\3\3\3\3\3\5\3\u02a6\n\3\3\3\3\3\3\3\3\3\5\3\u02ac\n\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\5\3\u02b4\n\3\3\3\3\3\5\3\u02b8\n\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\5\3\u02e2\n\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3\u02eb\n\3\3\3"+
		"\3\3\3\3\3\3\5\3\u02f1\n\3\3\3\3\3\3\3\3\3\5\3\u02f7\n\3\3\3\3\3\5\3\u02fb"+
		"\n\3\3\3\3\3\3\3\3\3\5\3\u0301\n\3\3\3\3\3\5\3\u0305\n\3\3\3\3\3\3\3\3"+
		"\3\5\3\u030b\n\3\3\3\3\3\5\3\u030f\n\3\3\3\3\3\3\3\3\3\5\3\u0315\n\3\3"+
		"\3\3\3\3\3\3\3\5\3\u031b\n\3\3\3\3\3\3\3\3\3\5\3\u0321\n\3\3\3\3\3\3\3"+
		"\5\3\u0326\n\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3\u032f\n\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\5\3\u0338\n\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3\u0343"+
		"\n\3\3\3\5\3\u0346\n\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3\u0350\n\3\3"+
		"\4\3\4\3\4\5\4\u0355\n\4\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\6\3\6\3\7\3\7\3"+
		"\7\5\7\u0363\n\7\3\7\3\7\3\b\3\b\3\b\7\b\u036a\n\b\f\b\16\b\u036d\13\b"+
		"\3\t\3\t\5\t\u0371\n\t\3\n\3\n\3\n\5\n\u0376\n\n\3\13\3\13\3\13\5\13\u037b"+
		"\n\13\3\13\3\13\3\13\3\f\3\f\3\f\3\f\3\f\3\f\3\r\3\r\3\r\5\r\u0389\n\r"+
		"\3\r\5\r\u038c\n\r\3\r\3\r\3\r\3\r\3\r\3\r\3\16\3\16\3\16\5\16\u0397\n"+
		"\16\3\16\3\16\3\16\3\16\3\16\3\16\3\17\3\17\7\17\u03a1\n\17\f\17\16\17"+
		"\u03a4\13\17\3\17\3\17\3\17\7\17\u03a9\n\17\f\17\16\17\u03ac\13\17\5\17"+
		"\u03ae\n\17\3\20\3\20\3\20\3\20\7\20\u03b4\n\20\f\20\16\20\u03b7\13\20"+
		"\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21"+
		"\3\21\5\21\u03c8\n\21\3\21\3\21\5\21\u03cc\n\21\3\21\3\21\3\21\3\21\3"+
		"\21\7\21\u03d3\n\21\f\21\16\21\u03d6\13\21\3\21\5\21\u03d9\n\21\5\21\u03db"+
		"\n\21\3\22\3\22\3\22\7\22\u03e0\n\22\f\22\16\22\u03e3\13\22\3\23\3\23"+
		"\3\23\3\24\3\24\3\24\7\24\u03eb\n\24\f\24\16\24\u03ee\13\24\3\25\3\25"+
		"\3\25\3\25\3\26\3\26\5\26\u03f6\n\26\3\26\3\26\3\26\3\26\3\27\3\27\3\27"+
		"\7\27\u03ff\n\27\f\27\16\27\u0402\13\27\3\30\3\30\3\30\3\30\3\30\5\30"+
		"\u0409\n\30\3\30\3\30\3\30\3\31\3\31\3\31\7\31\u0411\n\31\f\31\16\31\u0414"+
		"\13\31\3\32\3\32\3\33\3\33\3\33\7\33\u041b\n\33\f\33\16\33\u041e\13\33"+
		"\3\34\3\34\3\34\5\34\u0423\n\34\3\34\3\34\3\34\3\34\3\35\3\35\3\35\3\35"+
		"\7\35\u042d\n\35\f\35\16\35\u0430\13\35\3\35\3\35\3\35\3\35\3\35\3\35"+
		"\7\35\u0438\n\35\f\35\16\35\u043b\13\35\3\35\3\35\3\35\3\35\5\35\u0441"+
		"\n\35\3\36\3\36\3\36\3\36\3\37\3\37\3\37\3\37\3 \3 \3 \7 \u044e\n \f "+
		"\16 \u0451\13 \3!\3!\3!\5!\u0456\n!\3!\3!\3\"\3\"\3\"\5\"\u045d\n\"\3"+
		"\"\3\"\3#\3#\3#\3#\7#\u0465\n#\f#\16#\u0468\13#\3#\3#\3$\3$\5$\u046e\n"+
		"$\3$\5$\u0471\n$\3%\3%\3%\7%\u0476\n%\f%\16%\u0479\13%\3%\5%\u047c\n%"+
		"\3&\7&\u047f\n&\f&\16&\u0482\13&\3\'\3\'\5\'\u0486\n\'\3(\5(\u0489\n("+
		"\3(\3(\3(\7(\u048e\n(\f(\16(\u0491\13(\3)\3)\3)\3\u0480\2*\2\4\6\b\n\f"+
		"\16\20\22\24\26\30\32\34\36 \"$&(*,.\60\62\64\668:<>@BDFHJLNP\2\f\3\2"+
		"JK\4\2,,\61\61\4\288@@\3\2*+\4\2WWhh\3\2\\]\3\2\6\6\5\2\20\20((//\4\2"+
		"\n\n\17\17\20\2\20\25\31\31\33\33\35\37$\'9=JJNOQRUVZ[^_bbdf\u0538\2R"+
		"\3\2\2\2\4\u034f\3\2\2\2\6\u0354\3\2\2\2\b\u0356\3\2\2\2\n\u035d\3\2\2"+
		"\2\f\u0362\3\2\2\2\16\u0366\3\2\2\2\20\u0370\3\2\2\2\22\u0375\3\2\2\2"+
		"\24\u0377\3\2\2\2\26\u037f\3\2\2\2\30\u0385\3\2\2\2\32\u0393\3\2\2\2\34"+
		"\u03ad\3\2\2\2\36\u03af\3\2\2\2 \u03da\3\2\2\2\"\u03dc\3\2\2\2$\u03e4"+
		"\3\2\2\2&\u03e7\3\2\2\2(\u03ef\3\2\2\2*\u03f3\3\2\2\2,\u03fb\3\2\2\2."+
		"\u0403\3\2\2\2\60\u040d\3\2\2\2\62\u0415\3\2\2\2\64\u0417\3\2\2\2\66\u0422"+
		"\3\2\2\28\u0440\3\2\2\2:\u0442\3\2\2\2<\u0446\3\2\2\2>\u044a\3\2\2\2@"+
		"\u0455\3\2\2\2B\u045c\3\2\2\2D\u0460\3\2\2\2F\u046b\3\2\2\2H\u047b\3\2"+
		"\2\2J\u0480\3\2\2\2L\u0485\3\2\2\2N\u0488\3\2\2\2P\u0492\3\2\2\2RS\5\4"+
		"\3\2ST\7\2\2\3T\3\3\2\2\2UV\7\"\2\2VZ\t\2\2\2WX\7?\2\2XY\7E\2\2Y[\7\65"+
		"\2\2ZW\3\2\2\2Z[\3\2\2\2[\\\3\2\2\2\\_\5L\'\2]^\7 \2\2^`\7g\2\2_]\3\2"+
		"\2\2_`\3\2\2\2`\u0350\3\2\2\2ab\7O\2\2bc\t\2\2\2cd\5L\'\2de\7^\2\2ef\5"+
		"L\'\2f\u0350\3\2\2\2gh\7\22\2\2hi\t\2\2\2ij\5L\'\2jk\7O\2\2kl\7^\2\2l"+
		"m\5L\'\2m\u0350\3\2\2\2no\7\22\2\2op\t\2\2\2pq\5L\'\2qr\7U\2\2rs\7 \2"+
		"\2st\7g\2\2t\u0350\3\2\2\2uv\7\60\2\2vy\t\2\2\2wx\7?\2\2xz\7\65\2\2yw"+
		"\3\2\2\2yz\3\2\2\2z{\3\2\2\2{}\5L\'\2|~\7\35\2\2}|\3\2\2\2}~\3\2\2\2~"+
		"\u0350\3\2\2\2\177\u0080\7\"\2\2\u0080\u0084\7R\2\2\u0081\u0082\7?\2\2"+
		"\u0082\u0083\7E\2\2\u0083\u0085\7\65\2\2\u0084\u0081\3\2\2\2\u0084\u0085"+
		"\3\2\2\2\u0085\u0086\3\2\2\2\u0086\u0087\5L\'\2\u0087\u0088\7@\2\2\u0088"+
		"\u0089\t\2\2\2\u0089\u008a\5L\'\2\u008a\u008b\7>\2\2\u008b\u008c\7\32"+
		"\2\2\u008c\u008d\5J&\2\u008d\u0350\3\2\2\2\u008e\u008f\7O\2\2\u008f\u0090"+
		"\7R\2\2\u0090\u0091\5L\'\2\u0091\u0092\7^\2\2\u0092\u0093\5L\'\2\u0093"+
		"\u0094\7@\2\2\u0094\u0095\t\2\2\2\u0095\u0096\5L\'\2\u0096\u0350\3\2\2"+
		"\2\u0097\u0098\7\22\2\2\u0098\u0099\7R\2\2\u0099\u009a\5L\'\2\u009a\u009b"+
		"\7O\2\2\u009b\u009c\7^\2\2\u009c\u009d\5L\'\2\u009d\u009e\7@\2\2\u009e"+
		"\u009f\t\2\2\2\u009f\u00a0\5L\'\2\u00a0\u0350\3\2\2\2\u00a1\u00a2\7\22"+
		"\2\2\u00a2\u00a3\7R\2\2\u00a3\u00a4\5L\'\2\u00a4\u00a5\7>\2\2\u00a5\u00a6"+
		"\7\32\2\2\u00a6\u00a7\5J&\2\u00a7\u00a8\7@\2\2\u00a8\u00a9\t\2\2\2\u00a9"+
		"\u00aa\5L\'\2\u00aa\u0350\3\2\2\2\u00ab\u00ac\7\60\2\2\u00ac\u00af\7R"+
		"\2\2\u00ad\u00ae\7?\2\2\u00ae\u00b0\7\65\2\2\u00af\u00ad\3\2\2\2\u00af"+
		"\u00b0\3\2\2\2\u00b0\u00b1\3\2\2\2\u00b1\u00b2\5L\'\2\u00b2\u00b3\7@\2"+
		"\2\u00b3\u00b4\t\2\2\2\u00b4\u00b5\5L\'\2\u00b5\u0350\3\2\2\2\u00b6\u00b7"+
		"\7;\2\2\u00b7\u00b8\7;\2\2\u00b8\u00b9\7G\2\2\u00b9\u00ba\5\60\31\2\u00ba"+
		"\u00bb\7^\2\2\u00bb\u00bc\7b\2\2\u00bc\u00bd\5> \2\u00bd\u0350\3\2\2\2"+
		"\u00be\u00bf\7;\2\2\u00bf\u00c0\7;\2\2\u00c0\u00c1\7G\2\2\u00c1\u00c2"+
		"\5\60\31\2\u00c2\u00c3\7^\2\2\u00c3\u00c4\7<\2\2\u00c4\u00c5\5> \2\u00c5"+
		"\u0350\3\2\2\2\u00c6\u00c7\7Q\2\2\u00c7\u00c8\7;\2\2\u00c8\u00c9\7G\2"+
		"\2\u00c9\u00ca\5\60\31\2\u00ca\u00cb\78\2\2\u00cb\u00cc\7b\2\2\u00cc\u00cd"+
		"\5> \2\u00cd\u0350\3\2\2\2\u00ce\u00cf\7Q\2\2\u00cf\u00d0\7;\2\2\u00d0"+
		"\u00d1\7G\2\2\u00d1\u00d2\5\60\31\2\u00d2\u00d3\78\2\2\u00d3\u00d4\7<"+
		"\2\2\u00d4\u00d5\5> \2\u00d5\u0350\3\2\2\2\u00d6\u00d7\7;\2\2\u00d7\u00d8"+
		"\7\20\2\2\u00d8\u00d9\7^\2\2\u00d9\u00da\7b\2\2\u00da\u0350\5> \2\u00db"+
		"\u00dc\7;\2\2\u00dc\u00dd\7\20\2\2\u00dd\u00de\7^\2\2\u00de\u00df\7<\2"+
		"\2\u00df\u0350\5> \2\u00e0\u00e1\7Q\2\2\u00e1\u00e2\7\20\2\2\u00e2\u00e3"+
		"\78\2\2\u00e3\u00e4\7b\2\2\u00e4\u0350\5> \2\u00e5\u00e6\7Q\2\2\u00e6"+
		"\u00e7\7\20\2\2\u00e7\u00e8\78\2\2\u00e8\u00e9\7<\2\2\u00e9\u0350\5> "+
		"\2\u00ea\u00eb\7;\2\2\u00eb\u00ec\7(\2\2\u00ec\u00ed\7^\2\2\u00ed\u00ee"+
		"\7b\2\2\u00ee\u0350\5> \2\u00ef\u00f0\7;\2\2\u00f0\u00f1\7(\2\2\u00f1"+
		"\u00f2\7^\2\2\u00f2\u00f3\7<\2\2\u00f3\u0350\5> \2\u00f4\u00f5\7Q\2\2"+
		"\u00f5\u00f6\7(\2\2\u00f6\u00f7\78\2\2\u00f7\u00f8\7b\2\2\u00f8\u0350"+
		"\5> \2\u00f9\u00fa\7Q\2\2\u00fa\u00fb\7(\2\2\u00fb\u00fc\78\2\2\u00fc"+
		"\u00fd\7<\2\2\u00fd\u0350\5> \2\u00fe\u00ff\7;\2\2\u00ff\u0100\7.\2\2"+
		"\u0100\u0101\7F\2\2\u0101\u0102\5\64\33\2\u0102\u0103\7^\2\2\u0103\u0104"+
		"\7b\2\2\u0104\u0105\5> \2\u0105\u0350\3\2\2\2\u0106\u0107\7;\2\2\u0107"+
		"\u0108\7.\2\2\u0108\u0109\7F\2\2\u0109\u010a\5\64\33\2\u010a\u010b\7^"+
		"\2\2\u010b\u010c\7<\2\2\u010c\u010d\5> \2\u010d\u0350\3\2\2\2\u010e\u010f"+
		"\7Q\2\2\u010f\u0110\7.\2\2\u0110\u0111\7F\2\2\u0111\u0112\5\64\33\2\u0112"+
		"\u0113\78\2\2\u0113\u0114\7b\2\2\u0114\u0115\5> \2\u0115\u0350\3\2\2\2"+
		"\u0116\u0117\7Q\2\2\u0117\u0118\7.\2\2\u0118\u0119\7F\2\2\u0119\u011a"+
		"\5\64\33\2\u011a\u011b\78\2\2\u011b\u011c\7<\2\2\u011c\u011d\5> \2\u011d"+
		"\u0350\3\2\2\2\u011e\u011f\7\"\2\2\u011f\u0123\7b\2\2\u0120\u0121\7?\2"+
		"\2\u0121\u0122\7E\2\2\u0122\u0124\7\65\2\2\u0123\u0120\3\2\2\2\u0123\u0124"+
		"\3\2\2\2\u0124\u0125\3\2\2\2\u0125\u0126\5L\'\2\u0126\u0127\7>\2\2\u0127"+
		"\u0128\7\32\2\2\u0128\u0129\5J&\2\u0129\u0350\3\2\2\2\u012a\u012b\7O\2"+
		"\2\u012b\u012c\7b\2\2\u012c\u012d\5L\'\2\u012d\u012e\7^\2\2\u012e\u012f"+
		"\5L\'\2\u012f\u0350\3\2\2\2\u0130\u0131\7\22\2\2\u0131\u0132\7b\2\2\u0132"+
		"\u0133\5L\'\2\u0133\u0134\7O\2\2\u0134\u0135\7^\2\2\u0135\u0136\5L\'\2"+
		"\u0136\u0350\3\2\2\2\u0137\u0138\7\22\2\2\u0138\u0139\7b\2\2\u0139\u013a"+
		"\5L\'\2\u013a\u013b\7>\2\2\u013b\u013c\7\32\2\2\u013c\u013d\5J&\2\u013d"+
		"\u0350\3\2\2\2\u013e\u013f\7\60\2\2\u013f\u0142\7b\2\2\u0140\u0141\7?"+
		"\2\2\u0141\u0143\7\65\2\2\u0142\u0140\3\2\2\2\u0142\u0143\3\2\2\2\u0143"+
		"\u0144\3\2\2\2\u0144\u0350\5L\'\2\u0145\u0146\7\"\2\2\u0146\u014a\7<\2"+
		"\2\u0147\u0148\7?\2\2\u0148\u0149\7E\2\2\u0149\u014b\7\65\2\2\u014a\u0147"+
		"\3\2\2\2\u014a\u014b\3\2\2\2\u014b\u014c\3\2\2\2\u014c\u014f\5L\'\2\u014d"+
		"\u014e\7 \2\2\u014e\u0150\7g\2\2\u014f\u014d\3\2\2\2\u014f\u0150\3\2\2"+
		"\2\u0150\u0350\3\2\2\2\u0151\u0152\7O\2\2\u0152\u0153\7<\2\2\u0153\u0154"+
		"\5L\'\2\u0154\u0155\7^\2\2\u0155\u0156\5L\'\2\u0156\u0350\3\2\2\2\u0157"+
		"\u0158\7\22\2\2\u0158\u0159\7<\2\2\u0159\u015a\5L\'\2\u015a\u015b\7O\2"+
		"\2\u015b\u015c\7^\2\2\u015c\u015d\5L\'\2\u015d\u0350\3\2\2\2\u015e\u015f"+
		"\7\22\2\2\u015f\u0160\7<\2\2\u0160\u0161\5L\'\2\u0161\u0162\7U\2\2\u0162"+
		"\u0163\7 \2\2\u0163\u0164\7g\2\2\u0164\u0350\3\2\2\2\u0165\u0166\7\22"+
		"\2\2\u0166\u0167\7<\2\2\u0167\u0168\5L\'\2\u0168\u016a\5:\36\2\u0169\u016b"+
		"\5<\37\2\u016a\u0169\3\2\2\2\u016a\u016b\3\2\2\2\u016b\u0350\3\2\2\2\u016c"+
		"\u016d\7\22\2\2\u016d\u016e\7<\2\2\u016e\u016f\5L\'\2\u016f\u0171\5<\37"+
		"\2\u0170\u0172\5:\36\2\u0171\u0170\3\2\2\2\u0171\u0172\3\2\2\2\u0172\u0350"+
		"\3\2\2\2\u0173\u0174\7\60\2\2\u0174\u0177\7<\2\2\u0175\u0176\7?\2\2\u0176"+
		"\u0178\7\65\2\2\u0177\u0175\3\2\2\2\u0177\u0178\3\2\2\2\u0178\u0179\3"+
		"\2\2\2\u0179\u017b\5L\'\2\u017a\u017c\7\35\2\2\u017b\u017a\3\2\2\2\u017b"+
		"\u017c\3\2\2\2\u017c\u0350\3\2\2\2\u017d\u017e\7D\2\2\u017e\u0182\7&\2"+
		"\2\u017f\u0180\7?\2\2\u0180\u0181\7E\2\2\u0181\u0183\7\65\2\2\u0182\u017f"+
		"\3\2\2\2\u0182\u0183\3\2\2\2\u0183\u0184\3\2\2\2\u0184\u0185\5L\'\2\u0185"+
		"\u0186\7H\2\2\u0186\u0187\5D#\2\u0187\u0350\3\2\2\2\u0188\u0189\7O\2\2"+
		"\u0189\u018a\7&\2\2\u018a\u018b\5L\'\2\u018b\u018c\7^\2\2\u018c\u018d"+
		"\5L\'\2\u018d\u0350\3\2\2\2\u018e\u018f\7\22\2\2\u018f\u0190\7&\2\2\u0190"+
		"\u0191\5L\'\2\u0191\u0192\7O\2\2\u0192\u0193\7^\2\2\u0193\u0194\5L\'\2"+
		"\u0194\u0350\3\2\2\2\u0195\u0196\7\22\2\2\u0196\u0197\7&\2\2\u0197\u0198"+
		"\5L\'\2\u0198\u0199\7U\2\2\u0199\u019a\7H\2\2\u019a\u019b\5D#\2\u019b"+
		"\u0350\3\2\2\2\u019c\u019d\7`\2\2\u019d\u01a0\7&\2\2\u019e\u019f\7?\2"+
		"\2\u019f\u01a1\7\65\2\2\u01a0\u019e\3\2\2\2\u01a0\u01a1\3\2\2\2\u01a1"+
		"\u01a2\3\2\2\2\u01a2\u0350\5L\'\2\u01a3\u01a5\7D\2\2\u01a4\u01a6\7X\2"+
		"\2\u01a5\u01a4\3\2\2\2\u01a5\u01a6\3\2\2\2\u01a6\u01a7\3\2\2\2\u01a7\u01ab"+
		"\7Z\2\2\u01a8\u01a9\7?\2\2\u01a9\u01aa\7E\2\2\u01aa\u01ac\7\65\2\2\u01ab"+
		"\u01a8\3\2\2\2\u01ab\u01ac\3\2\2\2\u01ac\u01ad\3\2\2\2\u01ad\u01b2\5B"+
		"\"\2\u01ae\u01af\7\3\2\2\u01af\u01b0\5\"\22\2\u01b0\u01b1\7\4\2\2\u01b1"+
		"\u01b3\3\2\2\2\u01b2\u01ae\3\2\2\2\u01b2\u01b3\3\2\2\2\u01b3\u01b4\3\2"+
		"\2\2\u01b4\u01b5\7H\2\2\u01b5\u01b6\5D#\2\u01b6\u0350\3\2\2\2\u01b7\u01b8"+
		"\7f\2\2\u01b8\u01b9\7&\2\2\u01b9\u01ba\5L\'\2\u01ba\u01bc\7D\2\2\u01bb"+
		"\u01bd\7X\2\2\u01bc\u01bb\3\2\2\2\u01bc\u01bd\3\2\2\2\u01bd\u01be\3\2"+
		"\2\2\u01be\u01c2\7Z\2\2\u01bf\u01c0\7?\2\2\u01c0\u01c1\7E\2\2\u01c1\u01c3"+
		"\7\65\2\2\u01c2\u01bf\3\2\2\2\u01c2\u01c3\3\2\2\2\u01c3\u01c4\3\2\2\2"+
		"\u01c4\u01c5\5,\27\2\u01c5\u0350\3\2\2\2\u01c6\u01c7\7O\2\2\u01c7\u01c8"+
		"\7Z\2\2\u01c8\u01c9\5B\"\2\u01c9\u01ca\7^\2\2\u01ca\u01cb\5B\"\2\u01cb"+
		"\u0350\3\2\2\2\u01cc\u01cd\7\22\2\2\u01cd\u01ce\7Z\2\2\u01ce\u01cf\5B"+
		"\"\2\u01cf\u01d0\7O\2\2\u01d0\u01d1\7^\2\2\u01d1\u01d2\5B\"\2\u01d2\u0350"+
		"\3\2\2\2\u01d3\u01d4\7\22\2\2\u01d4\u01d5\7Z\2\2\u01d5\u01d6\5B\"\2\u01d6"+
		"\u01d7\7U\2\2\u01d7\u01d8\7H\2\2\u01d8\u01d9\5D#\2\u01d9\u0350\3\2\2\2"+
		"\u01da\u01db\7\22\2\2\u01db\u01dc\7Z\2\2\u01dc\u01dd\5B\"\2\u01dd\u01de"+
		"\7\21\2\2\u01de\u01df\7\37\2\2\u01df\u01e0\7\3\2\2\u01e0\u01e1\5\"\22"+
		"\2\u01e1\u01e2\7\4\2\2\u01e2\u0350\3\2\2\2\u01e3\u01e4\7\22\2\2\u01e4"+
		"\u01e5\7Z\2\2\u01e5\u01e6\5B\"\2\u01e6\u01e8\7!\2\2\u01e7\u01e9\7\36\2"+
		"\2\u01e8\u01e7\3\2\2\2\u01e8\u01e9\3\2\2\2\u01e9\u01ea\3\2\2\2\u01ea\u01eb"+
		"\5L\'\2\u01eb\u01ec\5$\23\2\u01ec\u0350\3\2\2\2\u01ed\u01ee\7\22\2\2\u01ee"+
		"\u01ef\7Z\2\2\u01ef\u01f0\5B\"\2\u01f0\u01f1\7\60\2\2\u01f1\u01f2\7\36"+
		"\2\2\u01f2\u01f3\5L\'\2\u01f3\u0350\3\2\2\2\u01f4\u01f5\7`\2\2\u01f5\u01f8"+
		"\7Z\2\2\u01f6\u01f7\7?\2\2\u01f7\u01f9\7\65\2\2\u01f8\u01f6\3\2\2\2\u01f8"+
		"\u01f9\3\2\2\2\u01f9\u01fa\3\2\2\2\u01fa\u0350\5B\"\2\u01fb\u01fc\7\""+
		"\2\2\u01fc\u0200\7$\2\2\u01fd\u01fe\7?\2\2\u01fe\u01ff\7E\2\2\u01ff\u0201"+
		"\7\65\2\2\u0200\u01fd\3\2\2\2\u0200\u0201\3\2\2\2\u0201\u0202\3\2\2\2"+
		"\u0202\u0205\5L\'\2\u0203\u0204\7 \2\2\u0204\u0206\7g\2\2\u0205\u0203"+
		"\3\2\2\2\u0205\u0206\3\2\2\2\u0206\u0350\3\2\2\2\u0207\u0208\7O\2\2\u0208"+
		"\u0209\7$\2\2\u0209\u020a\5L\'\2\u020a\u020b\7^\2\2\u020b\u020c\5L\'\2"+
		"\u020c\u0350\3\2\2\2\u020d\u020e\7\22\2\2\u020e\u020f\7$\2\2\u020f\u0210"+
		"\5L\'\2\u0210\u0211\7O\2\2\u0211\u0212\7^\2\2\u0212\u0213\5L\'\2\u0213"+
		"\u0350\3\2\2\2\u0214\u0215\7\22\2\2\u0215\u0216\7$\2\2\u0216\u0217\5L"+
		"\'\2\u0217\u0218\7U\2\2\u0218\u0219\7 \2\2\u0219\u021a\7g\2\2\u021a\u0350"+
		"\3\2\2\2\u021b\u021c\7\60\2\2\u021c\u021f\7$\2\2\u021d\u021e\7?\2\2\u021e"+
		"\u0220\7\65\2\2\u021f\u021d\3\2\2\2\u021f\u0220\3\2\2\2\u0220\u0221\3"+
		"\2\2\2\u0221\u0223\5L\'\2\u0222\u0224\7\35\2\2\u0223\u0222\3\2\2\2\u0223"+
		"\u0224\3\2\2\2\u0224\u0350\3\2\2\2\u0225\u0226\7a\2\2\u0226\u0350\5L\'"+
		"\2\u0227\u0228\7\"\2\2\u0228\u022c\79\2\2\u0229\u022a\7?\2\2\u022a\u022b"+
		"\7E\2\2\u022b\u022d\7\65\2\2\u022c\u0229\3\2\2\2\u022c\u022d\3\2\2\2\u022d"+
		"\u022e\3\2\2\2\u022e\u022f\5@!\2\u022f\u0230\7H\2\2\u0230\u0231\5D#\2"+
		"\u0231\u0350\3\2\2\2\u0232\u0233\7O\2\2\u0233\u0234\79\2\2\u0234\u0235"+
		"\5@!\2\u0235\u0236\7^\2\2\u0236\u0237\5@!\2\u0237\u0350\3\2\2\2\u0238"+
		"\u0239\7\22\2\2\u0239\u023a\79\2\2\u023a\u023b\5@!\2\u023b\u023c\7O\2"+
		"\2\u023c\u023d\7^\2\2\u023d\u023e\5@!\2\u023e\u0350\3\2\2\2\u023f\u0240"+
		"\7\22\2\2\u0240\u0241\79\2\2\u0241\u0242\5@!\2\u0242\u0243\7U\2\2\u0243"+
		"\u0244\7H\2\2\u0244\u0245\5D#\2\u0245\u0350\3\2\2\2\u0246\u0247\7\60\2"+
		"\2\u0247\u024a\79\2\2\u0248\u0249\7?\2\2\u0249\u024b\7\65\2\2\u024a\u0248"+
		"\3\2\2\2\u024a\u024b\3\2\2\2\u024b\u024c\3\2\2\2\u024c\u0350\5@!\2\u024d"+
		"\u024e\7\"\2\2\u024e\u0252\7d\2\2\u024f\u0250\7?\2\2\u0250\u0251\7E\2"+
		"\2\u0251\u0253\7\65\2\2\u0252\u024f\3\2\2\2\u0252\u0253\3\2\2\2\u0253"+
		"\u0254\3\2\2\2\u0254\u0257\5B\"\2\u0255\u0256\7 \2\2\u0256\u0258\7g\2"+
		"\2\u0257\u0255\3\2\2\2\u0257\u0258\3\2\2\2\u0258\u0259\3\2\2\2\u0259\u025a"+
		"\7\31\2\2\u025a\u025b\5\34\17\2\u025b\u0350\3\2\2\2\u025c\u025d\7O\2\2"+
		"\u025d\u025e\7d\2\2\u025e\u025f\5B\"\2\u025f\u0260\7^\2\2\u0260\u0261"+
		"\5B\"\2\u0261\u0350\3\2\2\2\u0262\u0263\7\22\2\2\u0263\u0264\7d\2\2\u0264"+
		"\u0265\5B\"\2\u0265\u0266\7O\2\2\u0266\u0267\7^\2\2\u0267\u0268\5B\"\2"+
		"\u0268\u0350\3\2\2\2\u0269\u026a\7\22\2\2\u026a\u026b\7d\2\2\u026b\u026c"+
		"\5B\"\2\u026c\u026d\7U\2\2\u026d\u026e\7 \2\2\u026e\u026f\7g\2\2\u026f"+
		"\u0350\3\2\2\2\u0270\u0271\7\22\2\2\u0271\u0272\7d\2\2\u0272\u0273\5B"+
		"\"\2\u0273\u0274\7\31\2\2\u0274\u0275\5\34\17\2\u0275\u0350\3\2\2\2\u0276"+
		"\u0277\7\60\2\2\u0277\u027a\7d\2\2\u0278\u0279\7?\2\2\u0279\u027b\7\65"+
		"\2\2\u027a\u0278\3\2\2\2\u027a\u027b\3\2\2\2\u027b\u027c\3\2\2\2\u027c"+
		"\u0350\5B\"\2\u027d\u027e\7\"\2\2\u027e\u0282\7\23\2\2\u027f\u0280\7?"+
		"\2\2\u0280\u0281\7E\2\2\u0281\u0283\7\65\2\2\u0282\u027f\3\2\2\2\u0282"+
		"\u0283\3\2\2\2\u0283\u0284\3\2\2\2\u0284\u0285\5L\'\2\u0285\u0286\7\31"+
		"\2\2\u0286\u0287\5\f\7\2\u0287\u0350\3\2\2\2\u0288\u0289\7O\2\2\u0289"+
		"\u028a\7\23\2\2\u028a\u028b\5L\'\2\u028b\u028c\7^\2\2\u028c\u028d\5L\'"+
		"\2\u028d\u0350\3\2\2\2\u028e\u028f\7\22\2\2\u028f\u0290\7\23\2\2\u0290"+
		"\u0291\5L\'\2\u0291\u0292\7O\2\2\u0292\u0293\7^\2\2\u0293\u0294\5L\'\2"+
		"\u0294\u0350\3\2\2\2\u0295\u0296\7\22\2\2\u0296\u0297\7\23\2\2\u0297\u0298"+
		"\5L\'\2\u0298\u0299\7\31\2\2\u0299\u029a\5\f\7\2\u029a\u0350\3\2\2\2\u029b"+
		"\u029c\7\60\2\2\u029c\u029f\7\23\2\2\u029d\u029e\7?\2\2\u029e\u02a0\7"+
		"\65\2\2\u029f\u029d\3\2\2\2\u029f\u02a0\3\2\2\2\u02a0\u02a1\3\2\2\2\u02a1"+
		"\u0350\5L\'\2\u02a2\u02a5\7\"\2\2\u02a3\u02a4\7)\2\2\u02a4\u02a6\5\6\4"+
		"\2\u02a5\u02a3\3\2\2\2\u02a5\u02a6\3\2\2\2\u02a6\u02a7\3\2\2\2\u02a7\u02ab"+
		"\7\64\2\2\u02a8\u02a9\7?\2\2\u02a9\u02aa\7E\2\2\u02aa\u02ac\7\65\2\2\u02ab"+
		"\u02a8\3\2\2\2\u02ab\u02ac\3\2\2\2\u02ac\u02ad\3\2\2\2\u02ad\u02ae\5L"+
		"\'\2\u02ae\u02af\7F\2\2\u02af\u02b0\7S\2\2\u02b0\u02b1\7\26\2\2\u02b1"+
		"\u02b3\5\b\5\2\u02b2\u02b4\t\3\2\2\u02b3\u02b2\3\2\2\2\u02b3\u02b4\3\2"+
		"\2\2\u02b4\u02b7\3\2\2\2\u02b5\u02b6\7 \2\2\u02b6\u02b8\7g\2\2\u02b7\u02b5"+
		"\3\2\2\2\u02b7\u02b8\3\2\2\2\u02b8\u02b9\3\2\2\2\u02b9\u02ba\7-\2\2\u02ba"+
		"\u02bb\7\34\2\2\u02bb\u02bc\5L\'\2\u02bc\u0350\3\2\2\2\u02bd\u02be\7O"+
		"\2\2\u02be\u02bf\7\64\2\2\u02bf\u02c0\5L\'\2\u02c0\u02c1\7^\2\2\u02c1"+
		"\u02c2\5L\'\2\u02c2\u0350\3\2\2\2\u02c3\u02c4\7\22\2\2\u02c4\u02c5\7)"+
		"\2\2\u02c5\u02c6\5\6\4\2\u02c6\u02c7\7\64\2\2\u02c7\u02c8\5L\'\2\u02c8"+
		"\u0350\3\2\2\2\u02c9\u02ca\7\22\2\2\u02ca\u02cb\7\64\2\2\u02cb\u02cc\5"+
		"L\'\2\u02cc\u02cd\7O\2\2\u02cd\u02ce\7^\2\2\u02ce\u02cf\5L\'\2\u02cf\u0350"+
		"\3\2\2\2\u02d0\u02d1\7\22\2\2\u02d1\u02d2\7\64\2\2\u02d2\u02d3\5L\'\2"+
		"\u02d3\u02d4\7F\2\2\u02d4\u02d5\7S\2\2\u02d5\u02d6\7\26\2\2\u02d6\u02d7"+
		"\5\b\5\2\u02d7\u0350\3\2\2\2\u02d8\u02d9\7\22\2\2\u02d9\u02da\7\64\2\2"+
		"\u02da\u02db\5L\'\2\u02db\u02dc\t\3\2\2\u02dc\u0350\3\2\2\2\u02dd\u02de"+
		"\7\60\2\2\u02de\u02e1\7\64\2\2\u02df\u02e0\7?\2\2\u02e0\u02e2\7\65\2\2"+
		"\u02e1\u02df\3\2\2\2\u02e1\u02e2\3\2\2\2\u02e2\u02e3\3\2\2\2\u02e3\u0350"+
		"\5L\'\2\u02e4\u02e5\7V\2\2\u02e5\u0350\7Y\2\2\u02e6\u02e7\7V\2\2\u02e7"+
		"\u02ea\7\'\2\2\u02e8\u02e9\7C\2\2\u02e9\u02eb\7g\2\2\u02ea\u02e8\3\2\2"+
		"\2\u02ea\u02eb\3\2\2\2\u02eb\u0350\3\2\2\2\u02ec\u02ed\7V\2\2\u02ed\u02f0"+
		"\7%\2\2\u02ee\u02ef\7C\2\2\u02ef\u02f1\7g\2\2\u02f0\u02ee\3\2\2\2\u02f0"+
		"\u02f1\3\2\2\2\u02f1\u0350\3\2\2\2\u02f2\u02f3\7V\2\2\u02f3\u02f6\7[\2"+
		"\2\u02f4\u02f5\t\4\2\2\u02f5\u02f7\5L\'\2\u02f6\u02f4\3\2\2\2\u02f6\u02f7"+
		"\3\2\2\2\u02f7\u02fa\3\2\2\2\u02f8\u02f9\7C\2\2\u02f9\u02fb\7g\2\2\u02fa"+
		"\u02f8\3\2\2\2\u02fa\u02fb\3\2\2\2\u02fb\u0350\3\2\2\2\u02fc\u02fd\7V"+
		"\2\2\u02fd\u0300\7e\2\2\u02fe\u02ff\t\4\2\2\u02ff\u0301\5L\'\2\u0300\u02fe"+
		"\3\2\2\2\u0300\u0301\3\2\2\2\u0301\u0304\3\2\2\2\u0302\u0303\7C\2\2\u0303"+
		"\u0305\7g\2\2\u0304\u0302\3\2\2\2\u0304\u0305\3\2\2\2\u0305\u0350\3\2"+
		"\2\2\u0306\u0307\7V\2\2\u0307\u030a\7:\2\2\u0308\u0309\t\4\2\2\u0309\u030b"+
		"\5L\'\2\u030a\u0308\3\2\2\2\u030a\u030b\3\2\2\2\u030b\u030e\3\2\2\2\u030c"+
		"\u030d\7C\2\2\u030d\u030f\7g\2\2\u030e\u030c\3\2\2\2\u030e\u030f\3\2\2"+
		"\2\u030f\u0350\3\2\2\2\u0310\u0311\7V\2\2\u0311\u0314\7c\2\2\u0312\u0313"+
		"\7C\2\2\u0313\u0315\7g\2\2\u0314\u0312\3\2\2\2\u0314\u0315\3\2\2\2\u0315"+
		"\u0350\3\2\2\2\u0316\u0317\7V\2\2\u0317\u031a\7=\2\2\u0318\u0319\7C\2"+
		"\2\u0319\u031b\7g\2\2\u031a\u0318\3\2\2\2\u031a\u031b\3\2\2\2\u031b\u0350"+
		"\3\2\2\2\u031c\u031d\7V\2\2\u031d\u0320\7\24\2\2\u031e\u031f\7C\2\2\u031f"+
		"\u0321\7g\2\2\u0320\u031e\3\2\2\2\u0320\u0321\3\2\2\2\u0321\u0350\3\2"+
		"\2\2\u0322\u0323\t\5\2\2\u0323\u0325\7&\2\2\u0324\u0326\7\67\2\2\u0325"+
		"\u0324\3\2\2\2\u0325\u0326\3\2\2\2\u0326\u0327\3\2\2\2\u0327\u0350\5L"+
		"\'\2\u0328\u0329\t\5\2\2\u0329\u032a\7$\2\2\u032a\u0350\5L\'\2\u032b\u032c"+
		"\t\5\2\2\u032c\u032e\7Z\2\2\u032d\u032f\7\67\2\2\u032e\u032d\3\2\2\2\u032e"+
		"\u032f\3\2\2\2\u032f\u0330\3\2\2\2\u0330\u0350\5B\"\2\u0331\u0332\t\5"+
		"\2\2\u0332\u0333\7d\2\2\u0333\u0350\5B\"\2\u0334\u0335\t\5\2\2\u0335\u0337"+
		"\79\2\2\u0336\u0338\7\67\2\2\u0337\u0336\3\2\2\2\u0337\u0338\3\2\2\2\u0338"+
		"\u0339\3\2\2\2\u0339\u0350\5@!\2\u033a\u033b\t\5\2\2\u033b\u033c\7b\2"+
		"\2\u033c\u0350\5L\'\2\u033d\u033e\t\5\2\2\u033e\u033f\7<\2\2\u033f\u0350"+
		"\5L\'\2\u0340\u0342\7\66\2\2\u0341\u0343\7\67\2\2\u0342\u0341\3\2\2\2"+
		"\u0342\u0343\3\2\2\2\u0343\u0345\3\2\2\2\u0344\u0346\7M\2\2\u0345\u0344"+
		"\3\2\2\2\u0345\u0346\3\2\2\2\u0346\u0347\3\2\2\2\u0347\u0350\5\34\17\2"+
		"\u0348\u0349\7U\2\2\u0349\u0350\5F$\2\u034a\u0350\5\34\17\2\u034b\u0350"+
		"\5\24\13\2\u034c\u0350\5\26\f\2\u034d\u0350\5\30\r\2\u034e\u0350\5\32"+
		"\16\2\u034fU\3\2\2\2\u034fa\3\2\2\2\u034fg\3\2\2\2\u034fn\3\2\2\2\u034f"+
		"u\3\2\2\2\u034f\177\3\2\2\2\u034f\u008e\3\2\2\2\u034f\u0097\3\2\2\2\u034f"+
		"\u00a1\3\2\2\2\u034f\u00ab\3\2\2\2\u034f\u00b6\3\2\2\2\u034f\u00be\3\2"+
		"\2\2\u034f\u00c6\3\2\2\2\u034f\u00ce\3\2\2\2\u034f\u00d6\3\2\2\2\u034f"+
		"\u00db\3\2\2\2\u034f\u00e0\3\2\2\2\u034f\u00e5\3\2\2\2\u034f\u00ea\3\2"+
		"\2\2\u034f\u00ef\3\2\2\2\u034f\u00f4\3\2\2\2\u034f\u00f9\3\2\2\2\u034f"+
		"\u00fe\3\2\2\2\u034f\u0106\3\2\2\2\u034f\u010e\3\2\2\2\u034f\u0116\3\2"+
		"\2\2\u034f\u011e\3\2\2\2\u034f\u012a\3\2\2\2\u034f\u0130\3\2\2\2\u034f"+
		"\u0137\3\2\2\2\u034f\u013e\3\2\2\2\u034f\u0145\3\2\2\2\u034f\u0151\3\2"+
		"\2\2\u034f\u0157\3\2\2\2\u034f\u015e\3\2\2\2\u034f\u0165\3\2\2\2\u034f"+
		"\u016c\3\2\2\2\u034f\u0173\3\2\2\2\u034f\u017d\3\2\2\2\u034f\u0188\3\2"+
		"\2\2\u034f\u018e\3\2\2\2\u034f\u0195\3\2\2\2\u034f\u019c\3\2\2\2\u034f"+
		"\u01a3\3\2\2\2\u034f\u01b7\3\2\2\2\u034f\u01c6\3\2\2\2\u034f\u01cc\3\2"+
		"\2\2\u034f\u01d3\3\2\2\2\u034f\u01da\3\2\2\2\u034f\u01e3\3\2\2\2\u034f"+
		"\u01ed\3\2\2\2\u034f\u01f4\3\2\2\2\u034f\u01fb\3\2\2\2\u034f\u0207\3\2"+
		"\2\2\u034f\u020d\3\2\2\2\u034f\u0214\3\2\2\2\u034f\u021b\3\2\2\2\u034f"+
		"\u0225\3\2\2\2\u034f\u0227\3\2\2\2\u034f\u0232\3\2\2\2\u034f\u0238\3\2"+
		"\2\2\u034f\u023f\3\2\2\2\u034f\u0246\3\2\2\2\u034f\u024d\3\2\2\2\u034f"+
		"\u025c\3\2\2\2\u034f\u0262\3\2\2\2\u034f\u0269\3\2\2\2\u034f\u0270\3\2"+
		"\2\2\u034f\u0276\3\2\2\2\u034f\u027d\3\2\2\2\u034f\u0288\3\2\2\2\u034f"+
		"\u028e\3\2\2\2\u034f\u0295\3\2\2\2\u034f\u029b\3\2\2\2\u034f\u02a2\3\2"+
		"\2\2\u034f\u02bd\3\2\2\2\u034f\u02c3\3\2\2\2\u034f\u02c9\3\2\2\2\u034f"+
		"\u02d0\3\2\2\2\u034f\u02d8\3\2\2\2\u034f\u02dd\3\2\2\2\u034f\u02e4\3\2"+
		"\2\2\u034f\u02e6\3\2\2\2\u034f\u02ec\3\2\2\2\u034f\u02f2\3\2\2\2\u034f"+
		"\u02fc\3\2\2\2\u034f\u0306\3\2\2\2\u034f\u0310\3\2\2\2\u034f\u0316\3\2"+
		"\2\2\u034f\u031c\3\2\2\2\u034f\u0322\3\2\2\2\u034f\u0328\3\2\2\2\u034f"+
		"\u032b\3\2\2\2\u034f\u0331\3\2\2\2\u034f\u0334\3\2\2\2\u034f\u033a\3\2"+
		"\2\2\u034f\u033d\3\2\2\2\u034f\u0340\3\2\2\2\u034f\u0348\3\2\2\2\u034f"+
		"\u034a\3\2\2\2\u034f\u034b\3\2\2\2\u034f\u034c\3\2\2\2\u034f\u034d\3\2"+
		"\2\2\u034f\u034e\3\2\2\2\u0350\5\3\2\2\2\u0351\u0352\7\62\2\2\u0352\u0355"+
		"\5L\'\2\u0353\u0355\7#\2\2\u0354\u0351\3\2\2\2\u0354\u0353\3\2\2\2\u0355"+
		"\7\3\2\2\2\u0356\u0357\5\n\6\2\u0357\u0358\5\n\6\2\u0358\u0359\5\n\6\2"+
		"\u0359\u035a\5\n\6\2\u035a\u035b\5\n\6\2\u035b\u035c\5\n\6\2\u035c\t\3"+
		"\2\2\2\u035d\u035e\t\6\2\2\u035e\13\3\2\2\2\u035f\u0360\5\16\b\2\u0360"+
		"\u0361\7\5\2\2\u0361\u0363\3\2\2\2\u0362\u035f\3\2\2\2\u0362\u0363\3\2"+
		"\2\2\u0363\u0364\3\2\2\2\u0364\u0365\5\22\n\2\u0365\r\3\2\2\2\u0366\u036b"+
		"\5\20\t\2\u0367\u0368\7\5\2\2\u0368\u036a\5\20\t\2\u0369\u0367\3\2\2\2"+
		"\u036a\u036d\3\2\2\2\u036b\u0369\3\2\2\2\u036b\u036c\3\2\2\2\u036c\17"+
		"\3\2\2\2\u036d\u036b\3\2\2\2\u036e\u0371\5\30\r\2\u036f\u0371\5\32\16"+
		"\2\u0370\u036e\3\2\2\2\u0370\u036f\3\2\2\2\u0371\21\3\2\2\2\u0372\u0376"+
		"\5\24\13\2\u0373\u0376\5\26\f\2\u0374\u0376\5\34\17\2\u0375\u0372\3\2"+
		"\2\2\u0375\u0373\3\2\2\2\u0375\u0374\3\2\2\2\u0376\23\3\2\2\2\u0377\u0378"+
		"\7A\2\2\u0378\u037a\7B\2\2\u0379\u037b\7Z\2\2\u037a\u0379\3\2\2\2\u037a"+
		"\u037b\3\2\2\2\u037b\u037c\3\2\2\2\u037c\u037d\5B\"\2\u037d\u037e\5\34"+
		"\17\2\u037e\25\3\2\2\2\u037f\u0380\7A\2\2\u0380\u0381\7L\2\2\u0381\u0382"+
		"\7Z\2\2\u0382\u0383\5B\"\2\u0383\u0384\5\34\17\2\u0384\27\3\2\2\2\u0385"+
		"\u0388\7\"\2\2\u0386\u0387\7I\2\2\u0387\u0389\7P\2\2\u0388\u0386\3\2\2"+
		"\2\u0388\u0389\3\2\2\2\u0389\u038b\3\2\2\2\u038a\u038c\7\33\2\2\u038b"+
		"\u038a\3\2\2\2\u038b\u038c\3\2\2\2\u038c\u038d\3\2\2\2\u038d\u038e\t\7"+
		"\2\2\u038e\u038f\7d\2\2\u038f\u0390\5L\'\2\u0390\u0391\7\31\2\2\u0391"+
		"\u0392\5\34\17\2\u0392\31\3\2\2\2\u0393\u0396\7\"\2\2\u0394\u0395\7I\2"+
		"\2\u0395\u0397\7P\2\2\u0396\u0394\3\2\2\2\u0396\u0397\3\2\2\2\u0397\u0398"+
		"\3\2\2\2\u0398\u0399\t\7\2\2\u0399\u039a\79\2\2\u039a\u039b\5L\'\2\u039b"+
		"\u039c\7H\2\2\u039c\u039d\5D#\2\u039d\33\3\2\2\2\u039e\u03a2\7T\2\2\u039f"+
		"\u03a1\n\b\2\2\u03a0\u039f\3\2\2\2\u03a1\u03a4\3\2\2\2\u03a2\u03a0\3\2"+
		"\2\2\u03a2\u03a3\3\2\2\2\u03a3\u03ae\3\2\2\2\u03a4\u03a2\3\2\2\2\u03a5"+
		"\u03a6\5\36\20\2\u03a6\u03aa\7T\2\2\u03a7\u03a9\n\b\2\2\u03a8\u03a7\3"+
		"\2\2\2\u03a9\u03ac\3\2\2\2\u03aa\u03a8\3\2\2\2\u03aa\u03ab\3\2\2\2\u03ab"+
		"\u03ae\3\2\2\2\u03ac\u03aa\3\2\2\2\u03ad\u039e\3\2\2\2\u03ad\u03a5\3\2"+
		"\2\2\u03ae\35\3\2\2\2\u03af\u03b0\7f\2\2\u03b0\u03b5\5*\26\2\u03b1\u03b2"+
		"\7\5\2\2\u03b2\u03b4\5*\26\2\u03b3\u03b1\3\2\2\2\u03b4\u03b7\3\2\2\2\u03b5"+
		"\u03b3\3\2\2\2\u03b5\u03b6\3\2\2\2\u03b6\37\3\2\2\2\u03b7\u03b5\3\2\2"+
		"\2\u03b8\u03b9\7\25\2\2\u03b9\u03ba\7\7\2\2\u03ba\u03bb\5 \21\2\u03bb"+
		"\u03bc\7\b\2\2\u03bc\u03db\3\2\2\2\u03bd\u03be\7\27\2\2\u03be\u03bf\7"+
		"\7\2\2\u03bf\u03c0\5 \21\2\u03c0\u03c1\7\5\2\2\u03c1\u03c2\5 \21\2\u03c2"+
		"\u03c3\7\b\2\2\u03c3\u03db\3\2\2\2\u03c4\u03cb\7\30\2\2\u03c5\u03c7\7"+
		"\7\2\2\u03c6\u03c8\5&\24\2\u03c7\u03c6\3\2\2\2\u03c7\u03c8\3\2\2\2\u03c8"+
		"\u03c9\3\2\2\2\u03c9\u03cc\7\b\2\2\u03ca\u03cc\7\63\2\2\u03cb\u03c5\3"+
		"\2\2\2\u03cb\u03ca\3\2\2\2\u03cc\u03db\3\2\2\2\u03cd\u03d8\5L\'\2\u03ce"+
		"\u03cf\7\3\2\2\u03cf\u03d4\7h\2\2\u03d0\u03d1\7\5\2\2\u03d1\u03d3\7h\2"+
		"\2\u03d2\u03d0\3\2\2\2\u03d3\u03d6\3\2\2\2\u03d4\u03d2\3\2\2\2\u03d4\u03d5"+
		"\3\2\2\2\u03d5\u03d7\3\2\2\2\u03d6\u03d4\3\2\2\2\u03d7\u03d9\7\4\2\2\u03d8"+
		"\u03ce\3\2\2\2\u03d8\u03d9\3\2\2\2\u03d9\u03db\3\2\2\2\u03da\u03b8\3\2"+
		"\2\2\u03da\u03bd\3\2\2\2\u03da\u03c4\3\2\2\2\u03da\u03cd\3\2\2\2\u03db"+
		"!\3\2\2\2\u03dc\u03e1\5$\23\2\u03dd\u03de\7\5\2\2\u03de\u03e0\5$\23\2"+
		"\u03df\u03dd\3\2\2\2\u03e0\u03e3\3\2\2\2\u03e1\u03df\3\2\2\2\u03e1\u03e2"+
		"\3\2\2\2\u03e2#\3\2\2\2\u03e3\u03e1\3\2\2\2\u03e4\u03e5\5L\'\2\u03e5\u03e6"+
		"\5 \21\2\u03e6%\3\2\2\2\u03e7\u03ec\5(\25\2\u03e8\u03e9\7\5\2\2\u03e9"+
		"\u03eb\5(\25\2\u03ea\u03e8\3\2\2\2\u03eb\u03ee\3\2\2\2\u03ec\u03ea\3\2"+
		"\2\2\u03ec\u03ed\3\2\2\2\u03ed\'\3\2\2\2\u03ee\u03ec\3\2\2\2\u03ef\u03f0"+
		"\5L\'\2\u03f0\u03f1\7\t\2\2\u03f1\u03f2\5 \21\2\u03f2)\3\2\2\2\u03f3\u03f5"+
		"\5L\'\2\u03f4\u03f6\7\31\2\2\u03f5\u03f4\3\2\2\2\u03f5\u03f6\3\2\2\2\u03f6"+
		"\u03f7\3\2\2\2\u03f7\u03f8\7\3\2\2\u03f8\u03f9\5\34\17\2\u03f9\u03fa\7"+
		"\4\2\2\u03fa+\3\2\2\2\u03fb\u0400\5.\30\2\u03fc\u03fd\7\5\2\2\u03fd\u03ff"+
		"\5.\30\2\u03fe\u03fc\3\2\2\2\u03ff\u0402\3\2\2\2\u0400\u03fe\3\2\2\2\u0400"+
		"\u0401\3\2\2\2\u0401-\3\2\2\2\u0402\u0400\3\2\2\2\u0403\u0408\5B\"\2\u0404"+
		"\u0405\7\3\2\2\u0405\u0406\5\"\22\2\u0406\u0407\7\4\2\2\u0407\u0409\3"+
		"\2\2\2\u0408\u0404\3\2\2\2\u0408\u0409\3\2\2\2\u0409\u040a\3\2\2\2\u040a"+
		"\u040b\7H\2\2\u040b\u040c\5D#\2\u040c/\3\2\2\2\u040d\u0412\5\62\32\2\u040e"+
		"\u040f\7\5\2\2\u040f\u0411\5\62\32\2\u0410\u040e\3\2\2\2\u0411\u0414\3"+
		"\2\2\2\u0412\u0410\3\2\2\2\u0412\u0413\3\2\2\2\u0413\61\3\2\2\2\u0414"+
		"\u0412\3\2\2\2\u0415\u0416\t\t\2\2\u0416\63\3\2\2\2\u0417\u041c\5\66\34"+
		"\2\u0418\u0419\7\5\2\2\u0419\u041b\5\66\34\2\u041a\u0418\3\2\2\2\u041b"+
		"\u041e\3\2\2\2\u041c\u041a\3\2\2\2\u041c\u041d\3\2\2\2\u041d\65\3\2\2"+
		"\2\u041e\u041c\3\2\2\2\u041f\u0420\5L\'\2\u0420\u0421\7\n\2\2\u0421\u0423"+
		"\3\2\2\2\u0422\u041f\3\2\2\2\u0422\u0423\3\2\2\2\u0423\u0424\3\2\2\2\u0424"+
		"\u0425\58\35\2\u0425\u0426\7\n\2\2\u0426\u0427\58\35\2\u0427\67\3\2\2"+
		"\2\u0428\u0429\7\13\2\2\u0429\u042e\5L\'\2\u042a\u042b\7\5\2\2\u042b\u042d"+
		"\5L\'\2\u042c\u042a\3\2\2\2\u042d\u0430\3\2\2\2\u042e\u042c\3\2\2\2\u042e"+
		"\u042f\3\2\2\2\u042f\u0431\3\2\2\2\u0430\u042e\3\2\2\2\u0431\u0432\7\f"+
		"\2\2\u0432\u0441\3\2\2\2\u0433\u0434\7\r\2\2\u0434\u0439\5L\'\2\u0435"+
		"\u0436\7\5\2\2\u0436\u0438\5L\'\2\u0437\u0435\3\2\2\2\u0438\u043b\3\2"+
		"\2\2\u0439\u0437\3\2\2\2\u0439\u043a\3\2\2\2\u043a\u043c\3\2\2\2\u043b"+
		"\u0439\3\2\2\2\u043c\u043d\7\16\2\2\u043d\u0441\3\2\2\2\u043e\u0441\5"+
		"L\'\2\u043f\u0441\7W\2\2\u0440\u0428\3\2\2\2\u0440\u0433\3\2\2\2\u0440"+
		"\u043e\3\2\2\2\u0440\u043f\3\2\2\2\u04419\3\2\2\2\u0442\u0443\7\21\2\2"+
		"\u0443\u0444\7b\2\2\u0444\u0445\5> \2\u0445;\3\2\2\2\u0446\u0447\7N\2"+
		"\2\u0447\u0448\7b\2\2\u0448\u0449\5> \2\u0449=\3\2\2\2\u044a\u044f\5L"+
		"\'\2\u044b\u044c\7\5\2\2\u044c\u044e\5L\'\2\u044d\u044b\3\2\2\2\u044e"+
		"\u0451\3\2\2\2\u044f\u044d\3\2\2\2\u044f\u0450\3\2\2\2\u0450?\3\2\2\2"+
		"\u0451\u044f\3\2\2\2\u0452\u0453\5L\'\2\u0453\u0454\7\n\2\2\u0454\u0456"+
		"\3\2\2\2\u0455\u0452\3\2\2\2\u0455\u0456\3\2\2\2\u0456\u0457\3\2\2\2\u0457"+
		"\u0458\5L\'\2\u0458A\3\2\2\2\u0459\u045a\5L\'\2\u045a\u045b\7\n\2\2\u045b"+
		"\u045d\3\2\2\2\u045c\u0459\3\2\2\2\u045c\u045d\3\2\2\2\u045d\u045e\3\2"+
		"\2\2\u045e\u045f\5L\'\2\u045fC\3\2\2\2\u0460\u0461\7\3\2\2\u0461\u0466"+
		"\5F$\2\u0462\u0463\7\5\2\2\u0463\u0465\5F$\2\u0464\u0462\3\2\2\2\u0465"+
		"\u0468\3\2\2\2\u0466\u0464\3\2\2\2\u0466\u0467\3\2\2\2\u0467\u0469\3\2"+
		"\2\2\u0468\u0466\3\2\2\2\u0469\u046a\7\4\2\2\u046aE\3\2\2\2\u046b\u0470"+
		"\5H%\2\u046c\u046e\7\62\2\2\u046d\u046c\3\2\2\2\u046d\u046e\3\2\2\2\u046e"+
		"\u046f\3\2\2\2\u046f\u0471\7g\2\2\u0470\u046d\3\2\2\2\u0470\u0471\3\2"+
		"\2\2\u0471G\3\2\2\2\u0472\u0477\5L\'\2\u0473\u0474\7\n\2\2\u0474\u0476"+
		"\5L\'\2\u0475\u0473\3\2\2\2\u0476\u0479\3\2\2\2\u0477\u0475\3\2\2\2\u0477"+
		"\u0478\3\2\2\2\u0478\u047c\3\2\2\2\u0479\u0477\3\2\2\2\u047a\u047c\7g"+
		"\2\2\u047b\u0472\3\2\2\2\u047b\u047a\3\2\2\2\u047cI\3\2\2\2\u047d\u047f"+
		"\13\2\2\2\u047e\u047d\3\2\2\2\u047f\u0482\3\2\2\2\u0480\u0481\3\2\2\2"+
		"\u0480\u047e\3\2\2\2\u0481K\3\2\2\2\u0482\u0480\3\2\2\2\u0483\u0486\7"+
		"i\2\2\u0484\u0486\5P)\2\u0485\u0483\3\2\2\2\u0485\u0484\3\2\2\2\u0486"+
		"M\3\2\2\2\u0487\u0489\7\17\2\2\u0488\u0487\3\2\2\2\u0488\u0489\3\2\2\2"+
		"\u0489\u048a\3\2\2\2\u048a\u048f\5L\'\2\u048b\u048c\t\n\2\2\u048c\u048e"+
		"\5L\'\2\u048d\u048b\3\2\2\2\u048e\u0491\3\2\2\2\u048f\u048d\3\2\2\2\u048f"+
		"\u0490\3\2\2\2\u0490O\3\2\2\2\u0491\u048f\3\2\2\2\u0492\u0493\t\13\2\2"+
		"\u0493Q\3\2\2\2cZ_y}\u0084\u00af\u0123\u0142\u014a\u014f\u016a\u0171\u0177"+
		"\u017b\u0182\u01a0\u01a5\u01ab\u01b2\u01bc\u01c2\u01e8\u01f8\u0200\u0205"+
		"\u021f\u0223\u022c\u024a\u0252\u0257\u027a\u0282\u029f\u02a5\u02ab\u02b3"+
		"\u02b7\u02e1\u02ea\u02f0\u02f6\u02fa\u0300\u0304\u030a\u030e\u0314\u031a"+
		"\u0320\u0325\u032e\u0337\u0342\u0345\u034f\u0354\u0362\u036b\u0370\u0375"+
		"\u037a\u0388\u038b\u0396\u03a2\u03aa\u03ad\u03b5\u03c7\u03cb\u03d4\u03d8"+
		"\u03da\u03e1\u03ec\u03f5\u0400\u0408\u0412\u041c\u0422\u042e\u0439\u0440"+
		"\u044f\u0455\u045c\u0466\u046d\u0470\u0477\u047b\u0480\u0485\u0488\u048f";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}