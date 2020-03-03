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
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, ACCOUNT=9, 
		ADD=10, ALL=11, ALTER=12, ANALYZE=13, ARRAY=14, AT=15, MAP=16, STRUCT=17, 
		AS=18, BY=19, CACHE=20, CALL=21, CASCADE=22, COLUMN=23, COLUMNS=24, COMMENT=25, 
		CHANGE=26, CREATE=27, COALESCE=28, CURRENT_USER=29, DATABASE=30, DATABASES=31, 
		DATASOURCE=32, DATASOURCES=33, DDL=34, DEFINER=35, DELETE=36, DESC=37, 
		DESCRIBE=38, DISABLE=39, DO=40, DCL=41, DROP=42, ENABLE=43, EQ=44, NEQ=45, 
		EVENT=46, EVENTS=47, EXISTS=48, EXPLAIN=49, EXTENDED=50, FOR=51, FROM=52, 
		FUNCTION=53, FUNCTIONS=54, GLOBAL=55, GRANT=56, GRANTS=57, GROUP=58, GROUPS=59, 
		HQL=60, IDENTIFIED=61, IF=62, IN=63, INSERT=64, INTO=65, LIKE=66, MERGE=67, 
		JOBS=68, MOUNT=69, MQL=70, NOT=71, ON=72, OPTION=73, OPTIONS=74, OR=75, 
		ORG=76, ORGS=77, ORGANIZATION=78, ORGANIZATIONS=79, OVERWRITE=80, PLAN=81, 
		PARTITION=82, PROC=83, PROCS=84, PROCEDURE=85, PROCEDURES=86, REMOVE=87, 
		RENAME=88, REFRESH=89, REPLACE=90, REVOKE=91, RUNNING=92, SA=93, SAS=94, 
		SCHEMA=95, SCHEDULE=96, SELECT=97, SESSION=98, SET=99, SHOW=100, STAR=101, 
		STREAM=102, SYSINFO=103, TABLE=104, TABLES=105, TEMP=106, TEMPORARY=107, 
		TO=108, TYPE=109, TRUNCATE=110, UNMOUNT=111, UPDATE=112, USE=113, USING=114, 
		USER=115, USERS=116, VARIABLES=117, VIEW=118, VIEWS=119, WITH=120, STRING=121, 
		INTEGER_VALUE=122, IDENTIFIER=123, BACKQUOTED_IDENTIFIER=124, SIMPLE_COMMENT=125, 
		BRACKETED_COMMENT=126, WS=127, UNRECOGNIZED=128, DELIMITER=129;
	public static final int
		RULE_single = 0, RULE_mql = 1, RULE_procCmds = 2, RULE_definer = 3, RULE_query = 4, 
		RULE_ctes = 5, RULE_partitionSpec = 6, RULE_coalesceSpec = 7, RULE_dataType = 8, 
		RULE_colTypeList = 9, RULE_colType = 10, RULE_complexColTypeList = 11, 
		RULE_complexColType = 12, RULE_namedQuery = 13, RULE_grantPrivilegeList = 14, 
		RULE_grantPrivilege = 15, RULE_privileges = 16, RULE_privilege = 17, RULE_columnIdentifiers = 18, 
		RULE_tableCollections = 19, RULE_identifierOrStar = 20, RULE_addUser = 21, 
		RULE_removeUser = 22, RULE_identifierList = 23, RULE_funcIdentifier = 24, 
		RULE_tableIdentifier = 25, RULE_propertyList = 26, RULE_property = 27, 
		RULE_propertyKey = 28, RULE_password = 29, RULE_identifier = 30, RULE_resource = 31, 
		RULE_nonReserved = 32;
	public static final String[] ruleNames = {
		"single", "mql", "procCmds", "definer", "query", "ctes", "partitionSpec", 
		"coalesceSpec", "dataType", "colTypeList", "colType", "complexColTypeList", 
		"complexColType", "namedQuery", "grantPrivilegeList", "grantPrivilege", 
		"privileges", "privilege", "columnIdentifiers", "tableCollections", "identifierOrStar", 
		"addUser", "removeUser", "identifierList", "funcIdentifier", "tableIdentifier", 
		"propertyList", "property", "propertyKey", "password", "identifier", "resource", 
		"nonReserved"
	};

	private static final String[] _LITERAL_NAMES = {
		null, "'('", "')'", "','", "';'", "'<'", "'>'", "':'", "'.'", "'ACCOUNT'", 
		"'ADD'", "'ALL'", "'ALTER'", "'ANALYZE'", "'ARRAY'", "'AT'", "'MAP'", 
		"'STRUCT'", "'AS'", "'BY'", "'CACHE'", "'CALL'", "'CASCADE'", "'COLUMN'", 
		"'COLUMNS'", "'COMMENT'", "'CHANGE'", "'CREATE'", "'COALESCE'", "'CURRENT_USER'", 
		"'DATABASE'", "'DATABASES'", "'DATASOURCE'", "'DATASOURCES'", "'DDL'", 
		"'DEFINER'", "'DELETE'", "'DESC'", "'DESCRIBE'", "'DISABLE'", "'DO'", 
		"'DCL'", "'DROP'", "'ENABLE'", null, "'<>'", "'EVENT'", "'EVENTS'", "'EXISTS'", 
		"'EXPLAIN'", "'EXTENDED'", "'FOR'", "'FROM'", "'FUNCTION'", "'FUNCTIONS'", 
		"'GLOBAL'", "'GRANT'", "'GRANTS'", "'GROUP'", "'GROUPS'", "'HQL'", "'IDENTIFIED '", 
		"'IF'", "'IN'", "'INSERT'", "'INTO'", "'LIKE'", "'MERGE'", "'JOBS'", "'MOUNT'", 
		"'MQL'", "'NOT'", "'ON'", "'OPTION'", "'OPTIONS'", "'OR'", "'ORG'", "'ORGS'", 
		"'ORGANIZATION'", "'ORGANIZATIONS'", "'OVERWRITE'", "'PLAN'", "'PARTITION'", 
		"'PROC'", "'PROCS'", "'PROCEDURE'", "'PROCEDURES'", "'REMOVE'", "'RENAME'", 
		"'REFRESH'", "'REPLACE'", "'REVOKE'", "'RUNNING'", "'SA'", "'SAS'", "'SCHEMA'", 
		"'SCHEDULE'", "'SELECT'", "'SESSION'", "'SET'", "'SHOW'", "'*'", "'STREAM'", 
		"'SYSINFO'", "'TABLE'", "'TABLES'", "'TEMP'", "'TEMPORARY'", "'TO'", "'TYPE'", 
		"'TRUNCATE'", "'UNMOUNT'", "'UPDATE'", "'USE'", "'USING'", "'USER'", "'USERS'", 
		"'VARIABLES'", "'VIEW'", "'VIEWS'", "'WITH'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, null, null, null, null, null, null, null, null, "ACCOUNT", "ADD", 
		"ALL", "ALTER", "ANALYZE", "ARRAY", "AT", "MAP", "STRUCT", "AS", "BY", 
		"CACHE", "CALL", "CASCADE", "COLUMN", "COLUMNS", "COMMENT", "CHANGE", 
		"CREATE", "COALESCE", "CURRENT_USER", "DATABASE", "DATABASES", "DATASOURCE", 
		"DATASOURCES", "DDL", "DEFINER", "DELETE", "DESC", "DESCRIBE", "DISABLE", 
		"DO", "DCL", "DROP", "ENABLE", "EQ", "NEQ", "EVENT", "EVENTS", "EXISTS", 
		"EXPLAIN", "EXTENDED", "FOR", "FROM", "FUNCTION", "FUNCTIONS", "GLOBAL", 
		"GRANT", "GRANTS", "GROUP", "GROUPS", "HQL", "IDENTIFIED", "IF", "IN", 
		"INSERT", "INTO", "LIKE", "MERGE", "JOBS", "MOUNT", "MQL", "NOT", "ON", 
		"OPTION", "OPTIONS", "OR", "ORG", "ORGS", "ORGANIZATION", "ORGANIZATIONS", 
		"OVERWRITE", "PLAN", "PARTITION", "PROC", "PROCS", "PROCEDURE", "PROCEDURES", 
		"REMOVE", "RENAME", "REFRESH", "REPLACE", "REVOKE", "RUNNING", "SA", "SAS", 
		"SCHEMA", "SCHEDULE", "SELECT", "SESSION", "SET", "SHOW", "STAR", "STREAM", 
		"SYSINFO", "TABLE", "TABLES", "TEMP", "TEMPORARY", "TO", "TYPE", "TRUNCATE", 
		"UNMOUNT", "UPDATE", "USE", "USING", "USER", "USERS", "VARIABLES", "VIEW", 
		"VIEWS", "WITH", "STRING", "INTEGER_VALUE", "IDENTIFIER", "BACKQUOTED_IDENTIFIER", 
		"SIMPLE_COMMENT", "BRACKETED_COMMENT", "WS", "UNRECOGNIZED", "DELIMITER"
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
			setState(66);
			mql();
			setState(67);
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
		public TerminalNode IF() { return getToken(MqlBaseParser.IF, 0); }
		public TerminalNode EXISTS() { return getToken(MqlBaseParser.EXISTS, 0); }
		public TerminalNode ORG() { return getToken(MqlBaseParser.ORG, 0); }
		public TerminalNode ORGANIZATION() { return getToken(MqlBaseParser.ORGANIZATION, 0); }
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
	public static class RevokeGrantFromGroupContext extends MqlContext {
		public IdentifierContext group;
		public TerminalNode REVOKE() { return getToken(MqlBaseParser.REVOKE, 0); }
		public TerminalNode GRANT() { return getToken(MqlBaseParser.GRANT, 0); }
		public TerminalNode OPTION() { return getToken(MqlBaseParser.OPTION, 0); }
		public GrantPrivilegeListContext grantPrivilegeList() {
			return getRuleContext(GrantPrivilegeListContext.class,0);
		}
		public TerminalNode FROM() { return getToken(MqlBaseParser.FROM, 0); }
		public TerminalNode GROUP() { return getToken(MqlBaseParser.GROUP, 0); }
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
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
	public static class ShowProceduresContext extends MqlContext {
		public Token pattern;
		public TerminalNode SHOW() { return getToken(MqlBaseParser.SHOW, 0); }
		public TerminalNode PROCS() { return getToken(MqlBaseParser.PROCS, 0); }
		public TerminalNode PROCEDURES() { return getToken(MqlBaseParser.PROCEDURES, 0); }
		public TerminalNode LIKE() { return getToken(MqlBaseParser.LIKE, 0); }
		public TerminalNode STRING() { return getToken(MqlBaseParser.STRING, 0); }
		public ShowProceduresContext(MqlContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterShowProcedures(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitShowProcedures(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitShowProcedures(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class DescTableContext extends MqlContext {
		public TableIdentifierContext tableIdentifier() {
			return getRuleContext(TableIdentifierContext.class,0);
		}
		public TerminalNode DESC() { return getToken(MqlBaseParser.DESC, 0); }
		public TerminalNode DESCRIBE() { return getToken(MqlBaseParser.DESCRIBE, 0); }
		public TerminalNode TABLE() { return getToken(MqlBaseParser.TABLE, 0); }
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
	public static class ShowSasContext extends MqlContext {
		public Token pattern;
		public TerminalNode SHOW() { return getToken(MqlBaseParser.SHOW, 0); }
		public TerminalNode SAS() { return getToken(MqlBaseParser.SAS, 0); }
		public TerminalNode LIKE() { return getToken(MqlBaseParser.LIKE, 0); }
		public TerminalNode STRING() { return getToken(MqlBaseParser.STRING, 0); }
		public ShowSasContext(MqlContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterShowSas(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitShowSas(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitShowSas(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ShowRunningEventsContext extends MqlContext {
		public TerminalNode SHOW() { return getToken(MqlBaseParser.SHOW, 0); }
		public TerminalNode RUNNING() { return getToken(MqlBaseParser.RUNNING, 0); }
		public TerminalNode EVENTS() { return getToken(MqlBaseParser.EVENTS, 0); }
		public ShowRunningEventsContext(MqlContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterShowRunningEvents(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitShowRunningEvents(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitShowRunningEvents(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class DropProcedureContext extends MqlContext {
		public IdentifierContext name;
		public TerminalNode DROP() { return getToken(MqlBaseParser.DROP, 0); }
		public TerminalNode PROC() { return getToken(MqlBaseParser.PROC, 0); }
		public TerminalNode PROCEDURE() { return getToken(MqlBaseParser.PROCEDURE, 0); }
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public TerminalNode IF() { return getToken(MqlBaseParser.IF, 0); }
		public TerminalNode EXISTS() { return getToken(MqlBaseParser.EXISTS, 0); }
		public DropProcedureContext(MqlContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterDropProcedure(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitDropProcedure(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitDropProcedure(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class GrantGrantToUserContext extends MqlContext {
		public IdentifierContext user;
		public List<TerminalNode> GRANT() { return getTokens(MqlBaseParser.GRANT); }
		public TerminalNode GRANT(int i) {
			return getToken(MqlBaseParser.GRANT, i);
		}
		public TerminalNode OPTION() { return getToken(MqlBaseParser.OPTION, 0); }
		public GrantPrivilegeListContext grantPrivilegeList() {
			return getRuleContext(GrantPrivilegeListContext.class,0);
		}
		public TerminalNode TO() { return getToken(MqlBaseParser.TO, 0); }
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public TerminalNode USER() { return getToken(MqlBaseParser.USER, 0); }
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
	public static class GrantPrivilegeToUsersContext extends MqlContext {
		public IdentifierContext user;
		public TerminalNode GRANT() { return getToken(MqlBaseParser.GRANT, 0); }
		public GrantPrivilegeListContext grantPrivilegeList() {
			return getRuleContext(GrantPrivilegeListContext.class,0);
		}
		public TerminalNode TO() { return getToken(MqlBaseParser.TO, 0); }
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public TerminalNode USER() { return getToken(MqlBaseParser.USER, 0); }
		public GrantPrivilegeToUsersContext(MqlContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterGrantPrivilegeToUsers(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitGrantPrivilegeToUsers(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitGrantPrivilegeToUsers(this);
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
	public static class ShowCreateTableContext extends MqlContext {
		public TableIdentifierContext name;
		public TerminalNode SHOW() { return getToken(MqlBaseParser.SHOW, 0); }
		public TerminalNode CREATE() { return getToken(MqlBaseParser.CREATE, 0); }
		public TerminalNode TABLE() { return getToken(MqlBaseParser.TABLE, 0); }
		public TableIdentifierContext tableIdentifier() {
			return getRuleContext(TableIdentifierContext.class,0);
		}
		public ShowCreateTableContext(MqlContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterShowCreateTable(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitShowCreateTable(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitShowCreateTable(this);
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
	public static class ShowVariableContext extends MqlContext {
		public Token pattern;
		public TerminalNode SHOW() { return getToken(MqlBaseParser.SHOW, 0); }
		public TerminalNode VARIABLES() { return getToken(MqlBaseParser.VARIABLES, 0); }
		public TerminalNode LIKE() { return getToken(MqlBaseParser.LIKE, 0); }
		public TerminalNode STRING() { return getToken(MqlBaseParser.STRING, 0); }
		public ShowVariableContext(MqlContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterShowVariable(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitShowVariable(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitShowVariable(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ShowEventsContext extends MqlContext {
		public Token pattern;
		public TerminalNode SHOW() { return getToken(MqlBaseParser.SHOW, 0); }
		public TerminalNode EVENTS() { return getToken(MqlBaseParser.EVENTS, 0); }
		public TerminalNode LIKE() { return getToken(MqlBaseParser.LIKE, 0); }
		public TerminalNode STRING() { return getToken(MqlBaseParser.STRING, 0); }
		public ShowEventsContext(MqlContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterShowEvents(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitShowEvents(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitShowEvents(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class AddGroupUserContext extends MqlContext {
		public IdentifierContext name;
		public TerminalNode ALTER() { return getToken(MqlBaseParser.ALTER, 0); }
		public TerminalNode GROUP() { return getToken(MqlBaseParser.GROUP, 0); }
		public AddUserContext addUser() {
			return getRuleContext(AddUserContext.class,0);
		}
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public AddGroupUserContext(MqlContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterAddGroupUser(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitAddGroupUser(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitAddGroupUser(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class RevokeResourcePrivilegeFromUsersContext extends MqlContext {
		public IdentifierContext user;
		public TerminalNode REVOKE() { return getToken(MqlBaseParser.REVOKE, 0); }
		public PrivilegesContext privileges() {
			return getRuleContext(PrivilegesContext.class,0);
		}
		public TerminalNode ON() { return getToken(MqlBaseParser.ON, 0); }
		public TableCollectionsContext tableCollections() {
			return getRuleContext(TableCollectionsContext.class,0);
		}
		public TerminalNode FROM() { return getToken(MqlBaseParser.FROM, 0); }
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public TerminalNode USER() { return getToken(MqlBaseParser.USER, 0); }
		public RevokeResourcePrivilegeFromUsersContext(MqlContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterRevokeResourcePrivilegeFromUsers(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitRevokeResourcePrivilegeFromUsers(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitRevokeResourcePrivilegeFromUsers(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class GrantPrivilegeToGroupContext extends MqlContext {
		public IdentifierContext group;
		public TerminalNode GRANT() { return getToken(MqlBaseParser.GRANT, 0); }
		public GrantPrivilegeListContext grantPrivilegeList() {
			return getRuleContext(GrantPrivilegeListContext.class,0);
		}
		public TerminalNode TO() { return getToken(MqlBaseParser.TO, 0); }
		public TerminalNode GROUP() { return getToken(MqlBaseParser.GROUP, 0); }
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public GrantPrivilegeToGroupContext(MqlContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterGrantPrivilegeToGroup(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitGrantPrivilegeToGroup(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitGrantPrivilegeToGroup(this);
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
	public static class CreateFunctionContext extends MqlContext {
		public FuncIdentifierContext name;
		public Token className;
		public Token methodName;
		public TerminalNode CREATE() { return getToken(MqlBaseParser.CREATE, 0); }
		public TerminalNode FUNCTION() { return getToken(MqlBaseParser.FUNCTION, 0); }
		public TerminalNode AS() { return getToken(MqlBaseParser.AS, 0); }
		public FuncIdentifierContext funcIdentifier() {
			return getRuleContext(FuncIdentifierContext.class,0);
		}
		public List<TerminalNode> STRING() { return getTokens(MqlBaseParser.STRING); }
		public TerminalNode STRING(int i) {
			return getToken(MqlBaseParser.STRING, i);
		}
		public TerminalNode IF() { return getToken(MqlBaseParser.IF, 0); }
		public TerminalNode NOT() { return getToken(MqlBaseParser.NOT, 0); }
		public TerminalNode EXISTS() { return getToken(MqlBaseParser.EXISTS, 0); }
		public TerminalNode USING() { return getToken(MqlBaseParser.USING, 0); }
		public List<ResourceContext> resource() {
			return getRuleContexts(ResourceContext.class);
		}
		public ResourceContext resource(int i) {
			return getRuleContext(ResourceContext.class,i);
		}
		public TerminalNode TEMP() { return getToken(MqlBaseParser.TEMP, 0); }
		public TerminalNode TEMPORARY() { return getToken(MqlBaseParser.TEMPORARY, 0); }
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
	public static class ShowJobsContext extends MqlContext {
		public TerminalNode SHOW() { return getToken(MqlBaseParser.SHOW, 0); }
		public TerminalNode JOBS() { return getToken(MqlBaseParser.JOBS, 0); }
		public ShowJobsContext(MqlContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterShowJobs(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitShowJobs(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitShowJobs(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ShowGrantsContext extends MqlContext {
		public IdentifierContext user;
		public TerminalNode SHOW() { return getToken(MqlBaseParser.SHOW, 0); }
		public TerminalNode GRANTS() { return getToken(MqlBaseParser.GRANTS, 0); }
		public TerminalNode FOR() { return getToken(MqlBaseParser.FOR, 0); }
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public ShowGrantsContext(MqlContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterShowGrants(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitShowGrants(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitShowGrants(this);
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
	public static class RevokePrivilegeFromUsersContext extends MqlContext {
		public IdentifierContext user;
		public TerminalNode REVOKE() { return getToken(MqlBaseParser.REVOKE, 0); }
		public GrantPrivilegeListContext grantPrivilegeList() {
			return getRuleContext(GrantPrivilegeListContext.class,0);
		}
		public TerminalNode FROM() { return getToken(MqlBaseParser.FROM, 0); }
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public TerminalNode USER() { return getToken(MqlBaseParser.USER, 0); }
		public RevokePrivilegeFromUsersContext(MqlContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterRevokePrivilegeFromUsers(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitRevokePrivilegeFromUsers(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitRevokePrivilegeFromUsers(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class SetSaPasswordContext extends MqlContext {
		public IdentifierContext name;
		public IdentifierContext org;
		public PasswordContext pwd;
		public TerminalNode ALTER() { return getToken(MqlBaseParser.ALTER, 0); }
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
	public static class SetSaOptionsContext extends MqlContext {
		public IdentifierContext name;
		public IdentifierContext org;
		public TerminalNode ALTER() { return getToken(MqlBaseParser.ALTER, 0); }
		public TerminalNode SA() { return getToken(MqlBaseParser.SA, 0); }
		public TerminalNode IN() { return getToken(MqlBaseParser.IN, 0); }
		public TerminalNode SET() { return getToken(MqlBaseParser.SET, 0); }
		public TerminalNode OPTIONS() { return getToken(MqlBaseParser.OPTIONS, 0); }
		public PropertyListContext propertyList() {
			return getRuleContext(PropertyListContext.class,0);
		}
		public List<IdentifierContext> identifier() {
			return getRuleContexts(IdentifierContext.class);
		}
		public IdentifierContext identifier(int i) {
			return getRuleContext(IdentifierContext.class,i);
		}
		public TerminalNode ORG() { return getToken(MqlBaseParser.ORG, 0); }
		public TerminalNode ORGANIZATION() { return getToken(MqlBaseParser.ORGANIZATION, 0); }
		public SetSaOptionsContext(MqlContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterSetSaOptions(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitSetSaOptions(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitSetSaOptions(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class RemoveGroupUserContext extends MqlContext {
		public IdentifierContext name;
		public TerminalNode ALTER() { return getToken(MqlBaseParser.ALTER, 0); }
		public TerminalNode GROUP() { return getToken(MqlBaseParser.GROUP, 0); }
		public RemoveUserContext removeUser() {
			return getRuleContext(RemoveUserContext.class,0);
		}
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public RemoveGroupUserContext(MqlContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterRemoveGroupUser(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitRemoveGroupUser(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitRemoveGroupUser(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class SetProcedureQuerysContext extends MqlContext {
		public IdentifierContext name;
		public TerminalNode ALTER() { return getToken(MqlBaseParser.ALTER, 0); }
		public TerminalNode AS() { return getToken(MqlBaseParser.AS, 0); }
		public ProcCmdsContext procCmds() {
			return getRuleContext(ProcCmdsContext.class,0);
		}
		public TerminalNode PROC() { return getToken(MqlBaseParser.PROC, 0); }
		public TerminalNode PROCEDURE() { return getToken(MqlBaseParser.PROCEDURE, 0); }
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public SetProcedureQuerysContext(MqlContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterSetProcedureQuerys(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitSetProcedureQuerys(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitSetProcedureQuerys(this);
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
	public static class SetDatabasePropertiesContext extends MqlContext {
		public IdentifierContext name;
		public TerminalNode ALTER() { return getToken(MqlBaseParser.ALTER, 0); }
		public TerminalNode DATABASE() { return getToken(MqlBaseParser.DATABASE, 0); }
		public TerminalNode SET() { return getToken(MqlBaseParser.SET, 0); }
		public TerminalNode OPTIONS() { return getToken(MqlBaseParser.OPTIONS, 0); }
		public PropertyListContext propertyList() {
			return getRuleContext(PropertyListContext.class,0);
		}
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public SetDatabasePropertiesContext(MqlContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterSetDatabaseProperties(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitSetDatabaseProperties(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitSetDatabaseProperties(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class UnmountDatabaseContext extends MqlContext {
		public IdentifierContext name;
		public TerminalNode UNMOUNT() { return getToken(MqlBaseParser.UNMOUNT, 0); }
		public TerminalNode DATABASE() { return getToken(MqlBaseParser.DATABASE, 0); }
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public TerminalNode IF() { return getToken(MqlBaseParser.IF, 0); }
		public TerminalNode EXISTS() { return getToken(MqlBaseParser.EXISTS, 0); }
		public TerminalNode CASCADE() { return getToken(MqlBaseParser.CASCADE, 0); }
		public UnmountDatabaseContext(MqlContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterUnmountDatabase(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitUnmountDatabase(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitUnmountDatabase(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ShowUsersInGroupContext extends MqlContext {
		public IdentifierContext name;
		public Token pattern;
		public TerminalNode SHOW() { return getToken(MqlBaseParser.SHOW, 0); }
		public TerminalNode USERS() { return getToken(MqlBaseParser.USERS, 0); }
		public TerminalNode IN() { return getToken(MqlBaseParser.IN, 0); }
		public TerminalNode GROUP() { return getToken(MqlBaseParser.GROUP, 0); }
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public TerminalNode LIKE() { return getToken(MqlBaseParser.LIKE, 0); }
		public TerminalNode STRING() { return getToken(MqlBaseParser.STRING, 0); }
		public ShowUsersInGroupContext(MqlContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterShowUsersInGroup(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitShowUsersInGroup(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitShowUsersInGroup(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ShowSchemaContext extends MqlContext {
		public TerminalNode SHOW() { return getToken(MqlBaseParser.SHOW, 0); }
		public TerminalNode SCHEMA() { return getToken(MqlBaseParser.SCHEMA, 0); }
		public TerminalNode FOR() { return getToken(MqlBaseParser.FOR, 0); }
		public QueryContext query() {
			return getRuleContext(QueryContext.class,0);
		}
		public ShowSchemaContext(MqlContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterShowSchema(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitShowSchema(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitShowSchema(this);
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
		public TerminalNode TEMP() { return getToken(MqlBaseParser.TEMP, 0); }
		public TerminalNode TEMPORARY() { return getToken(MqlBaseParser.TEMPORARY, 0); }
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
		public IdentifierContext group;
		public List<TerminalNode> GRANT() { return getTokens(MqlBaseParser.GRANT); }
		public TerminalNode GRANT(int i) {
			return getToken(MqlBaseParser.GRANT, i);
		}
		public TerminalNode OPTION() { return getToken(MqlBaseParser.OPTION, 0); }
		public GrantPrivilegeListContext grantPrivilegeList() {
			return getRuleContext(GrantPrivilegeListContext.class,0);
		}
		public TerminalNode TO() { return getToken(MqlBaseParser.TO, 0); }
		public TerminalNode GROUP() { return getToken(MqlBaseParser.GROUP, 0); }
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
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
		public TerminalNode OPTIONS() { return getToken(MqlBaseParser.OPTIONS, 0); }
		public PropertyListContext propertyList() {
			return getRuleContext(PropertyListContext.class,0);
		}
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
	public static class DescProcedureContext extends MqlContext {
		public IdentifierContext name;
		public TerminalNode DESC() { return getToken(MqlBaseParser.DESC, 0); }
		public TerminalNode DESCRIBE() { return getToken(MqlBaseParser.DESCRIBE, 0); }
		public TerminalNode PROC() { return getToken(MqlBaseParser.PROC, 0); }
		public TerminalNode PROCEDURE() { return getToken(MqlBaseParser.PROCEDURE, 0); }
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public DescProcedureContext(MqlContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterDescProcedure(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitDescProcedure(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitDescProcedure(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class CreateProcedureContext extends MqlContext {
		public IdentifierContext name;
		public TerminalNode CREATE() { return getToken(MqlBaseParser.CREATE, 0); }
		public TerminalNode USING() { return getToken(MqlBaseParser.USING, 0); }
		public TerminalNode AS() { return getToken(MqlBaseParser.AS, 0); }
		public ProcCmdsContext procCmds() {
			return getRuleContext(ProcCmdsContext.class,0);
		}
		public TerminalNode PROC() { return getToken(MqlBaseParser.PROC, 0); }
		public TerminalNode PROCEDURE() { return getToken(MqlBaseParser.PROCEDURE, 0); }
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public TerminalNode MQL() { return getToken(MqlBaseParser.MQL, 0); }
		public TerminalNode HQL() { return getToken(MqlBaseParser.HQL, 0); }
		public TerminalNode IF() { return getToken(MqlBaseParser.IF, 0); }
		public TerminalNode NOT() { return getToken(MqlBaseParser.NOT, 0); }
		public TerminalNode EXISTS() { return getToken(MqlBaseParser.EXISTS, 0); }
		public CreateProcedureContext(MqlContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterCreateProcedure(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitCreateProcedure(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitCreateProcedure(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class SetUserOptionsContext extends MqlContext {
		public IdentifierContext name;
		public TerminalNode ALTER() { return getToken(MqlBaseParser.ALTER, 0); }
		public TerminalNode USER() { return getToken(MqlBaseParser.USER, 0); }
		public TerminalNode SET() { return getToken(MqlBaseParser.SET, 0); }
		public TerminalNode OPTIONS() { return getToken(MqlBaseParser.OPTIONS, 0); }
		public PropertyListContext propertyList() {
			return getRuleContext(PropertyListContext.class,0);
		}
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public SetUserOptionsContext(MqlContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterSetUserOptions(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitSetUserOptions(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitSetUserOptions(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class CreateTemporaryViewContext extends MqlContext {
		public IdentifierContext name;
		public TerminalNode CREATE() { return getToken(MqlBaseParser.CREATE, 0); }
		public TerminalNode VIEW() { return getToken(MqlBaseParser.VIEW, 0); }
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
		public TerminalNode AS() { return getToken(MqlBaseParser.AS, 0); }
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
	public static class DescOrgContext extends MqlContext {
		public IdentifierContext name;
		public TerminalNode ORG() { return getToken(MqlBaseParser.ORG, 0); }
		public TerminalNode DESC() { return getToken(MqlBaseParser.DESC, 0); }
		public TerminalNode DESCRIBE() { return getToken(MqlBaseParser.DESCRIBE, 0); }
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public DescOrgContext(MqlContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterDescOrg(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitDescOrg(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitDescOrg(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class StatementContext extends MqlContext {
		public Token statement;
		public TerminalNode SELECT() { return getToken(MqlBaseParser.SELECT, 0); }
		public TerminalNode WITH() { return getToken(MqlBaseParser.WITH, 0); }
		public TerminalNode INSERT() { return getToken(MqlBaseParser.INSERT, 0); }
		public TerminalNode SET() { return getToken(MqlBaseParser.SET, 0); }
		public TerminalNode ANALYZE() { return getToken(MqlBaseParser.ANALYZE, 0); }
		public TerminalNode REFRESH() { return getToken(MqlBaseParser.REFRESH, 0); }
		public StatementContext(MqlContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitStatement(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class CreateEventContext extends MqlContext {
		public IdentifierContext name;
		public Token cronExpression;
		public Token comment;
		public IdentifierContext proc;
		public TerminalNode CREATE() { return getToken(MqlBaseParser.CREATE, 0); }
		public TerminalNode EVENT() { return getToken(MqlBaseParser.EVENT, 0); }
		public TerminalNode ON() { return getToken(MqlBaseParser.ON, 0); }
		public TerminalNode SCHEDULE() { return getToken(MqlBaseParser.SCHEDULE, 0); }
		public TerminalNode AT() { return getToken(MqlBaseParser.AT, 0); }
		public TerminalNode DO() { return getToken(MqlBaseParser.DO, 0); }
		public TerminalNode CALL() { return getToken(MqlBaseParser.CALL, 0); }
		public List<IdentifierContext> identifier() {
			return getRuleContexts(IdentifierContext.class);
		}
		public IdentifierContext identifier(int i) {
			return getRuleContext(IdentifierContext.class,i);
		}
		public List<TerminalNode> STRING() { return getTokens(MqlBaseParser.STRING); }
		public TerminalNode STRING(int i) {
			return getToken(MqlBaseParser.STRING, i);
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
		public Token cronExpression;
		public TerminalNode ALTER() { return getToken(MqlBaseParser.ALTER, 0); }
		public TerminalNode EVENT() { return getToken(MqlBaseParser.EVENT, 0); }
		public TerminalNode ON() { return getToken(MqlBaseParser.ON, 0); }
		public TerminalNode SCHEDULE() { return getToken(MqlBaseParser.SCHEDULE, 0); }
		public TerminalNode AT() { return getToken(MqlBaseParser.AT, 0); }
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public TerminalNode STRING() { return getToken(MqlBaseParser.STRING, 0); }
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
		public TerminalNode OR() { return getToken(MqlBaseParser.OR, 0); }
		public TerminalNode REPLACE() { return getToken(MqlBaseParser.REPLACE, 0); }
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
	public static class RevokeResourcePrivilegeFromGroupContext extends MqlContext {
		public IdentifierContext group;
		public TerminalNode REVOKE() { return getToken(MqlBaseParser.REVOKE, 0); }
		public PrivilegesContext privileges() {
			return getRuleContext(PrivilegesContext.class,0);
		}
		public TerminalNode ON() { return getToken(MqlBaseParser.ON, 0); }
		public TableCollectionsContext tableCollections() {
			return getRuleContext(TableCollectionsContext.class,0);
		}
		public TerminalNode FROM() { return getToken(MqlBaseParser.FROM, 0); }
		public TerminalNode GROUP() { return getToken(MqlBaseParser.GROUP, 0); }
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public RevokeResourcePrivilegeFromGroupContext(MqlContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterRevokeResourcePrivilegeFromGroup(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitRevokeResourcePrivilegeFromGroup(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitRevokeResourcePrivilegeFromGroup(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class MountDatabaseContext extends MqlContext {
		public IdentifierContext name;
		public TerminalNode MOUNT() { return getToken(MqlBaseParser.MOUNT, 0); }
		public TerminalNode DATABASE() { return getToken(MqlBaseParser.DATABASE, 0); }
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
		public MountDatabaseContext(MqlContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterMountDatabase(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitMountDatabase(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitMountDatabase(this);
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
	public static class RevokePrivilegeFromGroupContext extends MqlContext {
		public IdentifierContext group;
		public TerminalNode REVOKE() { return getToken(MqlBaseParser.REVOKE, 0); }
		public GrantPrivilegeListContext grantPrivilegeList() {
			return getRuleContext(GrantPrivilegeListContext.class,0);
		}
		public TerminalNode FROM() { return getToken(MqlBaseParser.FROM, 0); }
		public TerminalNode GROUP() { return getToken(MqlBaseParser.GROUP, 0); }
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public RevokePrivilegeFromGroupContext(MqlContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterRevokePrivilegeFromGroup(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitRevokePrivilegeFromGroup(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitRevokePrivilegeFromGroup(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class GrantResourcePrivilegeToGroupContext extends MqlContext {
		public IdentifierContext group;
		public TerminalNode GRANT() { return getToken(MqlBaseParser.GRANT, 0); }
		public PrivilegesContext privileges() {
			return getRuleContext(PrivilegesContext.class,0);
		}
		public TerminalNode ON() { return getToken(MqlBaseParser.ON, 0); }
		public TableCollectionsContext tableCollections() {
			return getRuleContext(TableCollectionsContext.class,0);
		}
		public TerminalNode TO() { return getToken(MqlBaseParser.TO, 0); }
		public TerminalNode GROUP() { return getToken(MqlBaseParser.GROUP, 0); }
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public GrantResourcePrivilegeToGroupContext(MqlContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterGrantResourcePrivilegeToGroup(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitGrantResourcePrivilegeToGroup(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitGrantResourcePrivilegeToGroup(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class GrantResourcePrivilegeToUsersContext extends MqlContext {
		public IdentifierContext user;
		public TerminalNode GRANT() { return getToken(MqlBaseParser.GRANT, 0); }
		public PrivilegesContext privileges() {
			return getRuleContext(PrivilegesContext.class,0);
		}
		public TerminalNode ON() { return getToken(MqlBaseParser.ON, 0); }
		public TableCollectionsContext tableCollections() {
			return getRuleContext(TableCollectionsContext.class,0);
		}
		public TerminalNode TO() { return getToken(MqlBaseParser.TO, 0); }
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public TerminalNode USER() { return getToken(MqlBaseParser.USER, 0); }
		public GrantResourcePrivilegeToUsersContext(MqlContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterGrantResourcePrivilegeToUsers(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitGrantResourcePrivilegeToUsers(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitGrantResourcePrivilegeToUsers(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ShowOrgsContext extends MqlContext {
		public Token pattern;
		public TerminalNode SHOW() { return getToken(MqlBaseParser.SHOW, 0); }
		public TerminalNode ORGS() { return getToken(MqlBaseParser.ORGS, 0); }
		public TerminalNode ORGANIZATIONS() { return getToken(MqlBaseParser.ORGANIZATIONS, 0); }
		public TerminalNode LIKE() { return getToken(MqlBaseParser.LIKE, 0); }
		public TerminalNode STRING() { return getToken(MqlBaseParser.STRING, 0); }
		public ShowOrgsContext(MqlContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterShowOrgs(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitShowOrgs(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitShowOrgs(this);
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
		public PasswordContext password() {
			return getRuleContext(PasswordContext.class,0);
		}
		public TerminalNode IF() { return getToken(MqlBaseParser.IF, 0); }
		public TerminalNode NOT() { return getToken(MqlBaseParser.NOT, 0); }
		public TerminalNode EXISTS() { return getToken(MqlBaseParser.EXISTS, 0); }
		public TerminalNode OPTIONS() { return getToken(MqlBaseParser.OPTIONS, 0); }
		public PropertyListContext propertyList() {
			return getRuleContext(PropertyListContext.class,0);
		}
		public TerminalNode ORG() { return getToken(MqlBaseParser.ORG, 0); }
		public TerminalNode ORGANIZATION() { return getToken(MqlBaseParser.ORGANIZATION, 0); }
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
	public static class RevokeGrantFromUserContext extends MqlContext {
		public IdentifierContext user;
		public TerminalNode REVOKE() { return getToken(MqlBaseParser.REVOKE, 0); }
		public TerminalNode GRANT() { return getToken(MqlBaseParser.GRANT, 0); }
		public TerminalNode OPTION() { return getToken(MqlBaseParser.OPTION, 0); }
		public GrantPrivilegeListContext grantPrivilegeList() {
			return getRuleContext(GrantPrivilegeListContext.class,0);
		}
		public TerminalNode FROM() { return getToken(MqlBaseParser.FROM, 0); }
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public TerminalNode USER() { return getToken(MqlBaseParser.USER, 0); }
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
	public static class RefreshDatabaseContext extends MqlContext {
		public IdentifierContext name;
		public TerminalNode REFRESH() { return getToken(MqlBaseParser.REFRESH, 0); }
		public TerminalNode DATABASE() { return getToken(MqlBaseParser.DATABASE, 0); }
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public RefreshDatabaseContext(MqlContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterRefreshDatabase(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitRefreshDatabase(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitRefreshDatabase(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class DescEventContext extends MqlContext {
		public IdentifierContext name;
		public TerminalNode EVENT() { return getToken(MqlBaseParser.EVENT, 0); }
		public TerminalNode DESC() { return getToken(MqlBaseParser.DESC, 0); }
		public TerminalNode DESCRIBE() { return getToken(MqlBaseParser.DESCRIBE, 0); }
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public DescEventContext(MqlContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterDescEvent(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitDescEvent(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitDescEvent(this);
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
		public TerminalNode OPTIONS() { return getToken(MqlBaseParser.OPTIONS, 0); }
		public PropertyListContext propertyList() {
			return getRuleContext(PropertyListContext.class,0);
		}
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
		public IdentifierContext scope;
		public IdentifierContext db;
		public Token pattern;
		public TerminalNode SHOW() { return getToken(MqlBaseParser.SHOW, 0); }
		public TerminalNode FUNCTIONS() { return getToken(MqlBaseParser.FUNCTIONS, 0); }
		public TerminalNode LIKE() { return getToken(MqlBaseParser.LIKE, 0); }
		public List<IdentifierContext> identifier() {
			return getRuleContexts(IdentifierContext.class);
		}
		public IdentifierContext identifier(int i) {
			return getRuleContext(IdentifierContext.class,i);
		}
		public TerminalNode FROM() { return getToken(MqlBaseParser.FROM, 0); }
		public TerminalNode IN() { return getToken(MqlBaseParser.IN, 0); }
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
	public static class SetOrganizationOptionsContext extends MqlContext {
		public IdentifierContext name;
		public TerminalNode ALTER() { return getToken(MqlBaseParser.ALTER, 0); }
		public TerminalNode SET() { return getToken(MqlBaseParser.SET, 0); }
		public TerminalNode OPTIONS() { return getToken(MqlBaseParser.OPTIONS, 0); }
		public PropertyListContext propertyList() {
			return getRuleContext(PropertyListContext.class,0);
		}
		public TerminalNode ORG() { return getToken(MqlBaseParser.ORG, 0); }
		public TerminalNode ORGANIZATION() { return getToken(MqlBaseParser.ORGANIZATION, 0); }
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public SetOrganizationOptionsContext(MqlContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterSetOrganizationOptions(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitSetOrganizationOptions(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitSetOrganizationOptions(this);
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
			int _alt;
			setState(739);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,76,_ctx) ) {
			case 1:
				_localctx = new CreateOrganizationContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(69);
				match(CREATE);
				setState(70);
				_la = _input.LA(1);
				if ( !(_la==ORG || _la==ORGANIZATION) ) {
				_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(74);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,0,_ctx) ) {
				case 1:
					{
					setState(71);
					match(IF);
					setState(72);
					match(NOT);
					setState(73);
					match(EXISTS);
					}
					break;
				}
				setState(76);
				((CreateOrganizationContext)_localctx).name = identifier();
				setState(79);
				_la = _input.LA(1);
				if (_la==COMMENT) {
					{
					setState(77);
					match(COMMENT);
					setState(78);
					((CreateOrganizationContext)_localctx).comment = match(STRING);
					}
				}

				setState(83);
				_la = _input.LA(1);
				if (_la==OPTIONS) {
					{
					setState(81);
					match(OPTIONS);
					setState(82);
					propertyList();
					}
				}

				}
				break;
			case 2:
				_localctx = new SetOrganizationOptionsContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(85);
				match(ALTER);
				setState(86);
				_la = _input.LA(1);
				if ( !(_la==ORG || _la==ORGANIZATION) ) {
				_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(87);
				((SetOrganizationOptionsContext)_localctx).name = identifier();
				setState(88);
				match(SET);
				setState(89);
				match(OPTIONS);
				setState(90);
				propertyList();
				}
				break;
			case 3:
				_localctx = new SetOrganizationCommentContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(92);
				match(ALTER);
				setState(93);
				_la = _input.LA(1);
				if ( !(_la==ORG || _la==ORGANIZATION) ) {
				_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(94);
				((SetOrganizationCommentContext)_localctx).name = identifier();
				setState(95);
				match(SET);
				setState(96);
				match(COMMENT);
				setState(97);
				((SetOrganizationCommentContext)_localctx).comment = match(STRING);
				}
				break;
			case 4:
				_localctx = new DropOrganizationContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(99);
				match(DROP);
				setState(100);
				_la = _input.LA(1);
				if ( !(_la==ORG || _la==ORGANIZATION) ) {
				_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(103);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,3,_ctx) ) {
				case 1:
					{
					setState(101);
					match(IF);
					setState(102);
					match(EXISTS);
					}
					break;
				}
				setState(105);
				((DropOrganizationContext)_localctx).name = identifier();
				setState(107);
				_la = _input.LA(1);
				if (_la==CASCADE) {
					{
					setState(106);
					match(CASCADE);
					}
				}

				}
				break;
			case 5:
				_localctx = new CreateSaContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(109);
				match(CREATE);
				setState(110);
				match(SA);
				setState(114);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,5,_ctx) ) {
				case 1:
					{
					setState(111);
					match(IF);
					setState(112);
					match(NOT);
					setState(113);
					match(EXISTS);
					}
					break;
				}
				setState(116);
				((CreateSaContext)_localctx).name = identifier();
				setState(117);
				match(IN);
				setState(119);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,6,_ctx) ) {
				case 1:
					{
					setState(118);
					_la = _input.LA(1);
					if ( !(_la==ORG || _la==ORGANIZATION) ) {
					_errHandler.recoverInline(this);
					} else {
						consume();
					}
					}
					break;
				}
				setState(121);
				((CreateSaContext)_localctx).org = identifier();
				setState(122);
				match(IDENTIFIED);
				setState(123);
				match(BY);
				setState(124);
				((CreateSaContext)_localctx).pwd = password();
				setState(127);
				_la = _input.LA(1);
				if (_la==OPTIONS) {
					{
					setState(125);
					match(OPTIONS);
					setState(126);
					propertyList();
					}
				}

				}
				break;
			case 6:
				_localctx = new SetSaPasswordContext(_localctx);
				enterOuterAlt(_localctx, 6);
				{
				setState(129);
				match(ALTER);
				setState(130);
				match(SA);
				setState(131);
				((SetSaPasswordContext)_localctx).name = identifier();
				setState(132);
				match(IN);
				setState(134);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,8,_ctx) ) {
				case 1:
					{
					setState(133);
					_la = _input.LA(1);
					if ( !(_la==ORG || _la==ORGANIZATION) ) {
					_errHandler.recoverInline(this);
					} else {
						consume();
					}
					}
					break;
				}
				setState(136);
				((SetSaPasswordContext)_localctx).org = identifier();
				setState(137);
				match(IDENTIFIED);
				setState(138);
				match(BY);
				setState(139);
				((SetSaPasswordContext)_localctx).pwd = password();
				}
				break;
			case 7:
				_localctx = new SetSaOptionsContext(_localctx);
				enterOuterAlt(_localctx, 7);
				{
				setState(141);
				match(ALTER);
				setState(142);
				match(SA);
				setState(143);
				((SetSaOptionsContext)_localctx).name = identifier();
				setState(144);
				match(IN);
				setState(146);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,9,_ctx) ) {
				case 1:
					{
					setState(145);
					_la = _input.LA(1);
					if ( !(_la==ORG || _la==ORGANIZATION) ) {
					_errHandler.recoverInline(this);
					} else {
						consume();
					}
					}
					break;
				}
				setState(148);
				((SetSaOptionsContext)_localctx).org = identifier();
				setState(149);
				match(SET);
				setState(150);
				match(OPTIONS);
				setState(151);
				propertyList();
				}
				break;
			case 8:
				_localctx = new DropSaContext(_localctx);
				enterOuterAlt(_localctx, 8);
				{
				setState(153);
				match(DROP);
				setState(154);
				match(SA);
				setState(157);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,10,_ctx) ) {
				case 1:
					{
					setState(155);
					match(IF);
					setState(156);
					match(EXISTS);
					}
					break;
				}
				setState(159);
				((DropSaContext)_localctx).name = identifier();
				setState(160);
				match(IN);
				setState(162);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,11,_ctx) ) {
				case 1:
					{
					setState(161);
					_la = _input.LA(1);
					if ( !(_la==ORG || _la==ORGANIZATION) ) {
					_errHandler.recoverInline(this);
					} else {
						consume();
					}
					}
					break;
				}
				setState(164);
				((DropSaContext)_localctx).org = identifier();
				}
				break;
			case 9:
				_localctx = new GrantGrantToUserContext(_localctx);
				enterOuterAlt(_localctx, 9);
				{
				setState(166);
				match(GRANT);
				setState(167);
				match(GRANT);
				setState(168);
				match(OPTION);
				setState(169);
				grantPrivilegeList();
				setState(170);
				match(TO);
				setState(172);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,12,_ctx) ) {
				case 1:
					{
					setState(171);
					match(USER);
					}
					break;
				}
				setState(174);
				((GrantGrantToUserContext)_localctx).user = identifier();
				}
				break;
			case 10:
				_localctx = new RevokeGrantFromUserContext(_localctx);
				enterOuterAlt(_localctx, 10);
				{
				setState(176);
				match(REVOKE);
				setState(177);
				match(GRANT);
				setState(178);
				match(OPTION);
				setState(179);
				grantPrivilegeList();
				setState(180);
				match(FROM);
				setState(182);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,13,_ctx) ) {
				case 1:
					{
					setState(181);
					match(USER);
					}
					break;
				}
				setState(184);
				((RevokeGrantFromUserContext)_localctx).user = identifier();
				}
				break;
			case 11:
				_localctx = new GrantGrantToGroupContext(_localctx);
				enterOuterAlt(_localctx, 11);
				{
				setState(186);
				match(GRANT);
				setState(187);
				match(GRANT);
				setState(188);
				match(OPTION);
				setState(189);
				grantPrivilegeList();
				setState(190);
				match(TO);
				setState(191);
				match(GROUP);
				setState(192);
				((GrantGrantToGroupContext)_localctx).group = identifier();
				}
				break;
			case 12:
				_localctx = new RevokeGrantFromGroupContext(_localctx);
				enterOuterAlt(_localctx, 12);
				{
				setState(194);
				match(REVOKE);
				setState(195);
				match(GRANT);
				setState(196);
				match(OPTION);
				setState(197);
				grantPrivilegeList();
				setState(198);
				match(FROM);
				setState(199);
				match(GROUP);
				setState(200);
				((RevokeGrantFromGroupContext)_localctx).group = identifier();
				}
				break;
			case 13:
				_localctx = new GrantPrivilegeToUsersContext(_localctx);
				enterOuterAlt(_localctx, 13);
				{
				setState(202);
				match(GRANT);
				setState(203);
				grantPrivilegeList();
				setState(204);
				match(TO);
				setState(206);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,14,_ctx) ) {
				case 1:
					{
					setState(205);
					match(USER);
					}
					break;
				}
				setState(208);
				((GrantPrivilegeToUsersContext)_localctx).user = identifier();
				}
				break;
			case 14:
				_localctx = new RevokePrivilegeFromUsersContext(_localctx);
				enterOuterAlt(_localctx, 14);
				{
				setState(210);
				match(REVOKE);
				setState(211);
				grantPrivilegeList();
				setState(212);
				match(FROM);
				setState(214);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,15,_ctx) ) {
				case 1:
					{
					setState(213);
					match(USER);
					}
					break;
				}
				setState(216);
				((RevokePrivilegeFromUsersContext)_localctx).user = identifier();
				}
				break;
			case 15:
				_localctx = new GrantPrivilegeToGroupContext(_localctx);
				enterOuterAlt(_localctx, 15);
				{
				setState(218);
				match(GRANT);
				setState(219);
				grantPrivilegeList();
				setState(220);
				match(TO);
				setState(221);
				match(GROUP);
				setState(222);
				((GrantPrivilegeToGroupContext)_localctx).group = identifier();
				}
				break;
			case 16:
				_localctx = new RevokePrivilegeFromGroupContext(_localctx);
				enterOuterAlt(_localctx, 16);
				{
				setState(224);
				match(REVOKE);
				setState(225);
				grantPrivilegeList();
				setState(226);
				match(FROM);
				setState(227);
				match(GROUP);
				setState(228);
				((RevokePrivilegeFromGroupContext)_localctx).group = identifier();
				}
				break;
			case 17:
				_localctx = new GrantResourcePrivilegeToUsersContext(_localctx);
				enterOuterAlt(_localctx, 17);
				{
				setState(230);
				match(GRANT);
				setState(231);
				privileges();
				setState(232);
				match(ON);
				setState(233);
				tableCollections();
				setState(234);
				match(TO);
				setState(236);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,16,_ctx) ) {
				case 1:
					{
					setState(235);
					match(USER);
					}
					break;
				}
				setState(238);
				((GrantResourcePrivilegeToUsersContext)_localctx).user = identifier();
				}
				break;
			case 18:
				_localctx = new RevokeResourcePrivilegeFromUsersContext(_localctx);
				enterOuterAlt(_localctx, 18);
				{
				setState(240);
				match(REVOKE);
				setState(241);
				privileges();
				setState(242);
				match(ON);
				setState(243);
				tableCollections();
				setState(244);
				match(FROM);
				setState(246);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,17,_ctx) ) {
				case 1:
					{
					setState(245);
					match(USER);
					}
					break;
				}
				setState(248);
				((RevokeResourcePrivilegeFromUsersContext)_localctx).user = identifier();
				}
				break;
			case 19:
				_localctx = new GrantResourcePrivilegeToGroupContext(_localctx);
				enterOuterAlt(_localctx, 19);
				{
				setState(250);
				match(GRANT);
				setState(251);
				privileges();
				setState(252);
				match(ON);
				setState(253);
				tableCollections();
				setState(254);
				match(TO);
				setState(255);
				match(GROUP);
				setState(256);
				((GrantResourcePrivilegeToGroupContext)_localctx).group = identifier();
				}
				break;
			case 20:
				_localctx = new RevokeResourcePrivilegeFromGroupContext(_localctx);
				enterOuterAlt(_localctx, 20);
				{
				setState(258);
				match(REVOKE);
				setState(259);
				privileges();
				setState(260);
				match(ON);
				setState(261);
				tableCollections();
				setState(262);
				match(FROM);
				setState(263);
				match(GROUP);
				setState(264);
				((RevokeResourcePrivilegeFromGroupContext)_localctx).group = identifier();
				}
				break;
			case 21:
				_localctx = new CreateUserContext(_localctx);
				enterOuterAlt(_localctx, 21);
				{
				setState(266);
				match(CREATE);
				setState(267);
				match(USER);
				setState(271);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,18,_ctx) ) {
				case 1:
					{
					setState(268);
					match(IF);
					setState(269);
					match(NOT);
					setState(270);
					match(EXISTS);
					}
					break;
				}
				setState(273);
				((CreateUserContext)_localctx).name = identifier();
				setState(274);
				match(IDENTIFIED);
				setState(275);
				match(BY);
				setState(276);
				((CreateUserContext)_localctx).pwd = password();
				setState(279);
				_la = _input.LA(1);
				if (_la==OPTIONS) {
					{
					setState(277);
					match(OPTIONS);
					setState(278);
					propertyList();
					}
				}

				}
				break;
			case 22:
				_localctx = new SetUserPasswordContext(_localctx);
				enterOuterAlt(_localctx, 22);
				{
				setState(281);
				match(ALTER);
				setState(282);
				match(USER);
				setState(283);
				((SetUserPasswordContext)_localctx).name = identifier();
				setState(284);
				match(IDENTIFIED);
				setState(285);
				match(BY);
				setState(286);
				((SetUserPasswordContext)_localctx).pwd = password();
				}
				break;
			case 23:
				_localctx = new SetUserOptionsContext(_localctx);
				enterOuterAlt(_localctx, 23);
				{
				setState(288);
				match(ALTER);
				setState(289);
				match(USER);
				setState(290);
				((SetUserOptionsContext)_localctx).name = identifier();
				setState(291);
				match(SET);
				setState(292);
				match(OPTIONS);
				setState(293);
				propertyList();
				}
				break;
			case 24:
				_localctx = new DropUserContext(_localctx);
				enterOuterAlt(_localctx, 24);
				{
				setState(295);
				match(DROP);
				setState(296);
				match(USER);
				setState(299);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,20,_ctx) ) {
				case 1:
					{
					setState(297);
					match(IF);
					setState(298);
					match(EXISTS);
					}
					break;
				}
				setState(301);
				((DropUserContext)_localctx).name = identifier();
				}
				break;
			case 25:
				_localctx = new CreateGroupContext(_localctx);
				enterOuterAlt(_localctx, 25);
				{
				setState(302);
				match(CREATE);
				setState(303);
				match(GROUP);
				setState(307);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,21,_ctx) ) {
				case 1:
					{
					setState(304);
					match(IF);
					setState(305);
					match(NOT);
					setState(306);
					match(EXISTS);
					}
					break;
				}
				setState(309);
				((CreateGroupContext)_localctx).name = identifier();
				setState(312);
				_la = _input.LA(1);
				if (_la==COMMENT) {
					{
					setState(310);
					match(COMMENT);
					setState(311);
					((CreateGroupContext)_localctx).comment = match(STRING);
					}
				}

				}
				break;
			case 26:
				_localctx = new SetGroupCommentContext(_localctx);
				enterOuterAlt(_localctx, 26);
				{
				setState(314);
				match(ALTER);
				setState(315);
				match(GROUP);
				setState(316);
				((SetGroupCommentContext)_localctx).name = identifier();
				setState(317);
				match(SET);
				setState(318);
				match(COMMENT);
				setState(319);
				((SetGroupCommentContext)_localctx).comment = match(STRING);
				}
				break;
			case 27:
				_localctx = new AddGroupUserContext(_localctx);
				enterOuterAlt(_localctx, 27);
				{
				setState(321);
				match(ALTER);
				setState(322);
				match(GROUP);
				setState(323);
				((AddGroupUserContext)_localctx).name = identifier();
				setState(324);
				addUser();
				}
				break;
			case 28:
				_localctx = new RemoveGroupUserContext(_localctx);
				enterOuterAlt(_localctx, 28);
				{
				setState(326);
				match(ALTER);
				setState(327);
				match(GROUP);
				setState(328);
				((RemoveGroupUserContext)_localctx).name = identifier();
				setState(329);
				removeUser();
				}
				break;
			case 29:
				_localctx = new DropGroupContext(_localctx);
				enterOuterAlt(_localctx, 29);
				{
				setState(331);
				match(DROP);
				setState(332);
				match(GROUP);
				setState(335);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,23,_ctx) ) {
				case 1:
					{
					setState(333);
					match(IF);
					setState(334);
					match(EXISTS);
					}
					break;
				}
				setState(337);
				((DropGroupContext)_localctx).name = identifier();
				setState(339);
				_la = _input.LA(1);
				if (_la==CASCADE) {
					{
					setState(338);
					match(CASCADE);
					}
				}

				}
				break;
			case 30:
				_localctx = new MountTableContext(_localctx);
				enterOuterAlt(_localctx, 30);
				{
				setState(341);
				match(MOUNT);
				setState(343);
				_la = _input.LA(1);
				if (_la==STREAM) {
					{
					setState(342);
					match(STREAM);
					}
				}

				setState(345);
				match(TABLE);
				setState(349);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,26,_ctx) ) {
				case 1:
					{
					setState(346);
					match(IF);
					setState(347);
					match(NOT);
					setState(348);
					match(EXISTS);
					}
					break;
				}
				setState(351);
				tableIdentifier();
				setState(356);
				_la = _input.LA(1);
				if (_la==T__0) {
					{
					setState(352);
					match(T__0);
					setState(353);
					((MountTableContext)_localctx).columns = colTypeList();
					setState(354);
					match(T__1);
					}
				}

				setState(358);
				match(OPTIONS);
				setState(359);
				propertyList();
				}
				break;
			case 31:
				_localctx = new SetTablePropertiesContext(_localctx);
				enterOuterAlt(_localctx, 31);
				{
				setState(361);
				match(ALTER);
				setState(362);
				match(TABLE);
				setState(363);
				((SetTablePropertiesContext)_localctx).name = tableIdentifier();
				setState(364);
				match(SET);
				setState(365);
				match(OPTIONS);
				setState(366);
				propertyList();
				}
				break;
			case 32:
				_localctx = new UnmountTableContext(_localctx);
				enterOuterAlt(_localctx, 32);
				{
				setState(368);
				match(UNMOUNT);
				setState(369);
				match(TABLE);
				setState(372);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,28,_ctx) ) {
				case 1:
					{
					setState(370);
					match(IF);
					setState(371);
					match(EXISTS);
					}
					break;
				}
				setState(374);
				((UnmountTableContext)_localctx).name = tableIdentifier();
				}
				break;
			case 33:
				_localctx = new MountDatabaseContext(_localctx);
				enterOuterAlt(_localctx, 33);
				{
				setState(375);
				match(MOUNT);
				setState(376);
				match(DATABASE);
				setState(380);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,29,_ctx) ) {
				case 1:
					{
					setState(377);
					match(IF);
					setState(378);
					match(NOT);
					setState(379);
					match(EXISTS);
					}
					break;
				}
				setState(382);
				((MountDatabaseContext)_localctx).name = identifier();
				setState(383);
				match(OPTIONS);
				setState(384);
				propertyList();
				}
				break;
			case 34:
				_localctx = new UnmountDatabaseContext(_localctx);
				enterOuterAlt(_localctx, 34);
				{
				setState(386);
				match(UNMOUNT);
				setState(387);
				match(DATABASE);
				setState(390);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,30,_ctx) ) {
				case 1:
					{
					setState(388);
					match(IF);
					setState(389);
					match(EXISTS);
					}
					break;
				}
				setState(392);
				((UnmountDatabaseContext)_localctx).name = identifier();
				setState(394);
				_la = _input.LA(1);
				if (_la==CASCADE) {
					{
					setState(393);
					match(CASCADE);
					}
				}

				}
				break;
			case 35:
				_localctx = new SetDatabasePropertiesContext(_localctx);
				enterOuterAlt(_localctx, 35);
				{
				setState(396);
				match(ALTER);
				setState(397);
				match(DATABASE);
				setState(398);
				((SetDatabasePropertiesContext)_localctx).name = identifier();
				setState(399);
				match(SET);
				setState(400);
				match(OPTIONS);
				setState(401);
				propertyList();
				}
				break;
			case 36:
				_localctx = new RefreshDatabaseContext(_localctx);
				enterOuterAlt(_localctx, 36);
				{
				setState(403);
				match(REFRESH);
				setState(404);
				match(DATABASE);
				setState(405);
				((RefreshDatabaseContext)_localctx).name = identifier();
				}
				break;
			case 37:
				_localctx = new CreateDatabaseContext(_localctx);
				enterOuterAlt(_localctx, 37);
				{
				setState(406);
				match(CREATE);
				setState(407);
				match(DATABASE);
				setState(411);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,32,_ctx) ) {
				case 1:
					{
					setState(408);
					match(IF);
					setState(409);
					match(NOT);
					setState(410);
					match(EXISTS);
					}
					break;
				}
				setState(413);
				((CreateDatabaseContext)_localctx).name = identifier();
				setState(416);
				_la = _input.LA(1);
				if (_la==COMMENT) {
					{
					setState(414);
					match(COMMENT);
					setState(415);
					((CreateDatabaseContext)_localctx).comment = match(STRING);
					}
				}

				}
				break;
			case 38:
				_localctx = new SetDatabaseCommentContext(_localctx);
				enterOuterAlt(_localctx, 38);
				{
				setState(418);
				match(ALTER);
				setState(419);
				match(DATABASE);
				setState(420);
				((SetDatabaseCommentContext)_localctx).name = identifier();
				setState(421);
				match(SET);
				setState(422);
				match(COMMENT);
				setState(423);
				((SetDatabaseCommentContext)_localctx).comment = match(STRING);
				}
				break;
			case 39:
				_localctx = new DropDatabaseContext(_localctx);
				enterOuterAlt(_localctx, 39);
				{
				setState(425);
				match(DROP);
				setState(426);
				match(DATABASE);
				setState(429);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,34,_ctx) ) {
				case 1:
					{
					setState(427);
					match(IF);
					setState(428);
					match(EXISTS);
					}
					break;
				}
				setState(431);
				((DropDatabaseContext)_localctx).name = identifier();
				setState(433);
				_la = _input.LA(1);
				if (_la==CASCADE) {
					{
					setState(432);
					match(CASCADE);
					}
				}

				}
				break;
			case 40:
				_localctx = new UseDatabaseContext(_localctx);
				enterOuterAlt(_localctx, 40);
				{
				setState(435);
				match(USE);
				setState(436);
				((UseDatabaseContext)_localctx).db = identifier();
				}
				break;
			case 41:
				_localctx = new CreateFunctionContext(_localctx);
				enterOuterAlt(_localctx, 41);
				{
				setState(437);
				match(CREATE);
				setState(439);
				_la = _input.LA(1);
				if (_la==TEMP || _la==TEMPORARY) {
					{
					setState(438);
					_la = _input.LA(1);
					if ( !(_la==TEMP || _la==TEMPORARY) ) {
					_errHandler.recoverInline(this);
					} else {
						consume();
					}
					}
				}

				setState(441);
				match(FUNCTION);
				setState(445);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,37,_ctx) ) {
				case 1:
					{
					setState(442);
					match(IF);
					setState(443);
					match(NOT);
					setState(444);
					match(EXISTS);
					}
					break;
				}
				setState(447);
				((CreateFunctionContext)_localctx).name = funcIdentifier();
				setState(448);
				match(AS);
				setState(449);
				((CreateFunctionContext)_localctx).className = match(STRING);
				setState(451);
				_la = _input.LA(1);
				if (_la==STRING) {
					{
					setState(450);
					((CreateFunctionContext)_localctx).methodName = match(STRING);
					}
				}

				setState(462);
				_la = _input.LA(1);
				if (_la==USING) {
					{
					setState(453);
					match(USING);
					setState(454);
					resource();
					setState(459);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==T__2) {
						{
						{
						setState(455);
						match(T__2);
						setState(456);
						resource();
						}
						}
						setState(461);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					}
				}

				}
				break;
			case 42:
				_localctx = new DropFunctionContext(_localctx);
				enterOuterAlt(_localctx, 42);
				{
				setState(464);
				match(DROP);
				setState(466);
				_la = _input.LA(1);
				if (_la==TEMP || _la==TEMPORARY) {
					{
					setState(465);
					_la = _input.LA(1);
					if ( !(_la==TEMP || _la==TEMPORARY) ) {
					_errHandler.recoverInline(this);
					} else {
						consume();
					}
					}
				}

				setState(468);
				match(FUNCTION);
				setState(471);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,42,_ctx) ) {
				case 1:
					{
					setState(469);
					match(IF);
					setState(470);
					match(EXISTS);
					}
					break;
				}
				setState(473);
				((DropFunctionContext)_localctx).name = funcIdentifier();
				}
				break;
			case 43:
				_localctx = new CreateViewContext(_localctx);
				enterOuterAlt(_localctx, 43);
				{
				setState(474);
				match(CREATE);
				setState(477);
				_la = _input.LA(1);
				if (_la==OR) {
					{
					setState(475);
					match(OR);
					setState(476);
					match(REPLACE);
					}
				}

				setState(479);
				match(VIEW);
				setState(480);
				((CreateViewContext)_localctx).name = tableIdentifier();
				setState(483);
				_la = _input.LA(1);
				if (_la==COMMENT) {
					{
					setState(481);
					match(COMMENT);
					setState(482);
					((CreateViewContext)_localctx).comment = match(STRING);
					}
				}

				setState(485);
				match(AS);
				setState(486);
				query();
				}
				break;
			case 44:
				_localctx = new SetViewQueryContext(_localctx);
				enterOuterAlt(_localctx, 44);
				{
				setState(488);
				match(ALTER);
				setState(489);
				match(VIEW);
				setState(490);
				((SetViewQueryContext)_localctx).name = tableIdentifier();
				setState(491);
				match(AS);
				setState(492);
				query();
				}
				break;
			case 45:
				_localctx = new DropViewContext(_localctx);
				enterOuterAlt(_localctx, 45);
				{
				setState(494);
				match(DROP);
				setState(495);
				match(VIEW);
				setState(498);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,45,_ctx) ) {
				case 1:
					{
					setState(496);
					match(IF);
					setState(497);
					match(EXISTS);
					}
					break;
				}
				setState(500);
				((DropViewContext)_localctx).name = tableIdentifier();
				}
				break;
			case 46:
				_localctx = new CreateProcedureContext(_localctx);
				enterOuterAlt(_localctx, 46);
				{
				setState(501);
				match(CREATE);
				setState(502);
				_la = _input.LA(1);
				if ( !(_la==PROC || _la==PROCEDURE) ) {
				_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(506);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,46,_ctx) ) {
				case 1:
					{
					setState(503);
					match(IF);
					setState(504);
					match(NOT);
					setState(505);
					match(EXISTS);
					}
					break;
				}
				setState(508);
				((CreateProcedureContext)_localctx).name = identifier();
				setState(509);
				match(USING);
				setState(510);
				_la = _input.LA(1);
				if ( !(_la==HQL || _la==MQL) ) {
				_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(511);
				match(AS);
				setState(512);
				procCmds();
				}
				break;
			case 47:
				_localctx = new SetProcedureQuerysContext(_localctx);
				enterOuterAlt(_localctx, 47);
				{
				setState(514);
				match(ALTER);
				setState(515);
				_la = _input.LA(1);
				if ( !(_la==PROC || _la==PROCEDURE) ) {
				_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(516);
				((SetProcedureQuerysContext)_localctx).name = identifier();
				setState(517);
				match(AS);
				setState(518);
				procCmds();
				}
				break;
			case 48:
				_localctx = new DropProcedureContext(_localctx);
				enterOuterAlt(_localctx, 48);
				{
				setState(520);
				match(DROP);
				setState(521);
				_la = _input.LA(1);
				if ( !(_la==PROC || _la==PROCEDURE) ) {
				_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(524);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,47,_ctx) ) {
				case 1:
					{
					setState(522);
					match(IF);
					setState(523);
					match(EXISTS);
					}
					break;
				}
				setState(526);
				((DropProcedureContext)_localctx).name = identifier();
				}
				break;
			case 49:
				_localctx = new CreateEventContext(_localctx);
				enterOuterAlt(_localctx, 49);
				{
				setState(527);
				match(CREATE);
				setState(530);
				_la = _input.LA(1);
				if (_la==DEFINER) {
					{
					setState(528);
					match(DEFINER);
					setState(529);
					definer();
					}
				}

				setState(532);
				match(EVENT);
				setState(536);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,49,_ctx) ) {
				case 1:
					{
					setState(533);
					match(IF);
					setState(534);
					match(NOT);
					setState(535);
					match(EXISTS);
					}
					break;
				}
				setState(538);
				((CreateEventContext)_localctx).name = identifier();
				setState(539);
				match(ON);
				setState(540);
				match(SCHEDULE);
				setState(541);
				match(AT);
				setState(542);
				((CreateEventContext)_localctx).cronExpression = match(STRING);
				setState(544);
				_la = _input.LA(1);
				if (_la==DISABLE || _la==ENABLE) {
					{
					setState(543);
					_la = _input.LA(1);
					if ( !(_la==DISABLE || _la==ENABLE) ) {
					_errHandler.recoverInline(this);
					} else {
						consume();
					}
					}
				}

				setState(548);
				_la = _input.LA(1);
				if (_la==COMMENT) {
					{
					setState(546);
					match(COMMENT);
					setState(547);
					((CreateEventContext)_localctx).comment = match(STRING);
					}
				}

				setState(550);
				match(DO);
				setState(551);
				match(CALL);
				setState(552);
				((CreateEventContext)_localctx).proc = identifier();
				}
				break;
			case 50:
				_localctx = new SetDefinerContext(_localctx);
				enterOuterAlt(_localctx, 50);
				{
				setState(554);
				match(ALTER);
				setState(555);
				match(DEFINER);
				setState(556);
				definer();
				setState(557);
				match(EVENT);
				setState(558);
				((SetDefinerContext)_localctx).name = identifier();
				}
				break;
			case 51:
				_localctx = new SetEventScheduleContext(_localctx);
				enterOuterAlt(_localctx, 51);
				{
				setState(560);
				match(ALTER);
				setState(561);
				match(EVENT);
				setState(562);
				((SetEventScheduleContext)_localctx).name = identifier();
				setState(563);
				match(ON);
				setState(564);
				match(SCHEDULE);
				setState(565);
				match(AT);
				setState(566);
				((SetEventScheduleContext)_localctx).cronExpression = match(STRING);
				}
				break;
			case 52:
				_localctx = new SetEventEnableContext(_localctx);
				enterOuterAlt(_localctx, 52);
				{
				setState(568);
				match(ALTER);
				setState(569);
				match(EVENT);
				setState(570);
				((SetEventEnableContext)_localctx).name = identifier();
				setState(571);
				_la = _input.LA(1);
				if ( !(_la==DISABLE || _la==ENABLE) ) {
				_errHandler.recoverInline(this);
				} else {
					consume();
				}
				}
				break;
			case 53:
				_localctx = new DropEventContext(_localctx);
				enterOuterAlt(_localctx, 53);
				{
				setState(573);
				match(DROP);
				setState(574);
				match(EVENT);
				setState(577);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,52,_ctx) ) {
				case 1:
					{
					setState(575);
					match(IF);
					setState(576);
					match(EXISTS);
					}
					break;
				}
				setState(579);
				((DropEventContext)_localctx).name = identifier();
				}
				break;
			case 54:
				_localctx = new ShowSysInfoContext(_localctx);
				enterOuterAlt(_localctx, 54);
				{
				setState(580);
				match(SHOW);
				setState(581);
				match(SYSINFO);
				}
				break;
			case 55:
				_localctx = new ShowJobsContext(_localctx);
				enterOuterAlt(_localctx, 55);
				{
				setState(582);
				match(SHOW);
				setState(583);
				match(JOBS);
				}
				break;
			case 56:
				_localctx = new ShowRunningEventsContext(_localctx);
				enterOuterAlt(_localctx, 56);
				{
				setState(584);
				match(SHOW);
				setState(585);
				match(RUNNING);
				setState(586);
				match(EVENTS);
				}
				break;
			case 57:
				_localctx = new ShowEventsContext(_localctx);
				enterOuterAlt(_localctx, 57);
				{
				setState(587);
				match(SHOW);
				setState(588);
				match(EVENTS);
				setState(591);
				_la = _input.LA(1);
				if (_la==LIKE) {
					{
					setState(589);
					match(LIKE);
					setState(590);
					((ShowEventsContext)_localctx).pattern = match(STRING);
					}
				}

				}
				break;
			case 58:
				_localctx = new ShowDatabaseContext(_localctx);
				enterOuterAlt(_localctx, 58);
				{
				setState(593);
				match(SHOW);
				setState(594);
				match(DATABASES);
				setState(597);
				_la = _input.LA(1);
				if (_la==LIKE) {
					{
					setState(595);
					match(LIKE);
					setState(596);
					((ShowDatabaseContext)_localctx).pattern = match(STRING);
					}
				}

				}
				break;
			case 59:
				_localctx = new ShowTablesContext(_localctx);
				enterOuterAlt(_localctx, 59);
				{
				setState(599);
				match(SHOW);
				setState(600);
				match(TABLES);
				setState(603);
				_la = _input.LA(1);
				if (_la==FROM || _la==IN) {
					{
					setState(601);
					_la = _input.LA(1);
					if ( !(_la==FROM || _la==IN) ) {
					_errHandler.recoverInline(this);
					} else {
						consume();
					}
					setState(602);
					((ShowTablesContext)_localctx).db = identifier();
					}
				}

				setState(607);
				_la = _input.LA(1);
				if (_la==LIKE) {
					{
					setState(605);
					match(LIKE);
					setState(606);
					((ShowTablesContext)_localctx).pattern = match(STRING);
					}
				}

				}
				break;
			case 60:
				_localctx = new ShowFunctionsContext(_localctx);
				enterOuterAlt(_localctx, 60);
				{
				setState(609);
				match(SHOW);
				setState(611);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,57,_ctx) ) {
				case 1:
					{
					setState(610);
					((ShowFunctionsContext)_localctx).scope = identifier();
					}
					break;
				}
				setState(613);
				match(FUNCTIONS);
				setState(616);
				_la = _input.LA(1);
				if (_la==FROM || _la==IN) {
					{
					setState(614);
					_la = _input.LA(1);
					if ( !(_la==FROM || _la==IN) ) {
					_errHandler.recoverInline(this);
					} else {
						consume();
					}
					setState(615);
					((ShowFunctionsContext)_localctx).db = identifier();
					}
				}

				setState(620);
				_la = _input.LA(1);
				if (_la==LIKE) {
					{
					setState(618);
					match(LIKE);
					setState(619);
					((ShowFunctionsContext)_localctx).pattern = match(STRING);
					}
				}

				}
				break;
			case 61:
				_localctx = new ShowOrgsContext(_localctx);
				enterOuterAlt(_localctx, 61);
				{
				setState(622);
				match(SHOW);
				setState(623);
				_la = _input.LA(1);
				if ( !(_la==ORGS || _la==ORGANIZATIONS) ) {
				_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(626);
				_la = _input.LA(1);
				if (_la==LIKE) {
					{
					setState(624);
					match(LIKE);
					setState(625);
					((ShowOrgsContext)_localctx).pattern = match(STRING);
					}
				}

				}
				break;
			case 62:
				_localctx = new ShowSasContext(_localctx);
				enterOuterAlt(_localctx, 62);
				{
				setState(628);
				match(SHOW);
				setState(629);
				match(SAS);
				setState(632);
				_la = _input.LA(1);
				if (_la==LIKE) {
					{
					setState(630);
					match(LIKE);
					setState(631);
					((ShowSasContext)_localctx).pattern = match(STRING);
					}
				}

				}
				break;
			case 63:
				_localctx = new ShowUsersContext(_localctx);
				enterOuterAlt(_localctx, 63);
				{
				setState(634);
				match(SHOW);
				setState(635);
				match(USERS);
				setState(638);
				_la = _input.LA(1);
				if (_la==LIKE) {
					{
					setState(636);
					match(LIKE);
					setState(637);
					((ShowUsersContext)_localctx).pattern = match(STRING);
					}
				}

				}
				break;
			case 64:
				_localctx = new ShowGroupsContext(_localctx);
				enterOuterAlt(_localctx, 64);
				{
				setState(640);
				match(SHOW);
				setState(641);
				match(GROUPS);
				setState(644);
				_la = _input.LA(1);
				if (_la==LIKE) {
					{
					setState(642);
					match(LIKE);
					setState(643);
					((ShowGroupsContext)_localctx).pattern = match(STRING);
					}
				}

				}
				break;
			case 65:
				_localctx = new ShowUsersInGroupContext(_localctx);
				enterOuterAlt(_localctx, 65);
				{
				setState(646);
				match(SHOW);
				setState(647);
				match(USERS);
				setState(648);
				match(IN);
				setState(649);
				match(GROUP);
				setState(650);
				((ShowUsersInGroupContext)_localctx).name = identifier();
				setState(653);
				_la = _input.LA(1);
				if (_la==LIKE) {
					{
					setState(651);
					match(LIKE);
					setState(652);
					((ShowUsersInGroupContext)_localctx).pattern = match(STRING);
					}
				}

				}
				break;
			case 66:
				_localctx = new ShowProceduresContext(_localctx);
				enterOuterAlt(_localctx, 66);
				{
				setState(655);
				match(SHOW);
				setState(656);
				_la = _input.LA(1);
				if ( !(_la==PROCS || _la==PROCEDURES) ) {
				_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(659);
				_la = _input.LA(1);
				if (_la==LIKE) {
					{
					setState(657);
					match(LIKE);
					setState(658);
					((ShowProceduresContext)_localctx).pattern = match(STRING);
					}
				}

				}
				break;
			case 67:
				_localctx = new ShowVariableContext(_localctx);
				enterOuterAlt(_localctx, 67);
				{
				setState(661);
				match(SHOW);
				setState(662);
				match(VARIABLES);
				setState(665);
				_la = _input.LA(1);
				if (_la==LIKE) {
					{
					setState(663);
					match(LIKE);
					setState(664);
					((ShowVariableContext)_localctx).pattern = match(STRING);
					}
				}

				}
				break;
			case 68:
				_localctx = new ShowGrantsContext(_localctx);
				enterOuterAlt(_localctx, 68);
				{
				setState(667);
				match(SHOW);
				setState(668);
				match(GRANTS);
				setState(669);
				match(FOR);
				setState(670);
				((ShowGrantsContext)_localctx).user = identifier();
				}
				break;
			case 69:
				_localctx = new ShowCreateTableContext(_localctx);
				enterOuterAlt(_localctx, 69);
				{
				setState(671);
				match(SHOW);
				setState(672);
				match(CREATE);
				setState(673);
				match(TABLE);
				setState(674);
				((ShowCreateTableContext)_localctx).name = tableIdentifier();
				}
				break;
			case 70:
				_localctx = new ShowSchemaContext(_localctx);
				enterOuterAlt(_localctx, 70);
				{
				setState(675);
				match(SHOW);
				setState(676);
				match(SCHEMA);
				setState(677);
				match(FOR);
				setState(678);
				query();
				}
				break;
			case 71:
				_localctx = new DescEventContext(_localctx);
				enterOuterAlt(_localctx, 71);
				{
				setState(679);
				_la = _input.LA(1);
				if ( !(_la==DESC || _la==DESCRIBE) ) {
				_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(680);
				match(EVENT);
				setState(681);
				((DescEventContext)_localctx).name = identifier();
				}
				break;
			case 72:
				_localctx = new DescProcedureContext(_localctx);
				enterOuterAlt(_localctx, 72);
				{
				setState(682);
				_la = _input.LA(1);
				if ( !(_la==DESC || _la==DESCRIBE) ) {
				_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(683);
				_la = _input.LA(1);
				if ( !(_la==PROC || _la==PROCEDURE) ) {
				_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(684);
				((DescProcedureContext)_localctx).name = identifier();
				}
				break;
			case 73:
				_localctx = new DescDatabaseContext(_localctx);
				enterOuterAlt(_localctx, 73);
				{
				setState(685);
				_la = _input.LA(1);
				if ( !(_la==DESC || _la==DESCRIBE) ) {
				_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(686);
				match(DATABASE);
				setState(687);
				((DescDatabaseContext)_localctx).name = identifier();
				}
				break;
			case 74:
				_localctx = new DescTableContext(_localctx);
				enterOuterAlt(_localctx, 74);
				{
				setState(688);
				_la = _input.LA(1);
				if ( !(_la==DESC || _la==DESCRIBE) ) {
				_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(690);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,67,_ctx) ) {
				case 1:
					{
					setState(689);
					match(TABLE);
					}
					break;
				}
				setState(693);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,68,_ctx) ) {
				case 1:
					{
					setState(692);
					match(EXTENDED);
					}
					break;
				}
				setState(695);
				tableIdentifier();
				}
				break;
			case 75:
				_localctx = new DescFunctionContext(_localctx);
				enterOuterAlt(_localctx, 75);
				{
				setState(696);
				_la = _input.LA(1);
				if ( !(_la==DESC || _la==DESCRIBE) ) {
				_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(697);
				match(FUNCTION);
				setState(699);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,69,_ctx) ) {
				case 1:
					{
					setState(698);
					match(EXTENDED);
					}
					break;
				}
				setState(701);
				funcIdentifier();
				}
				break;
			case 76:
				_localctx = new DescUserContext(_localctx);
				enterOuterAlt(_localctx, 76);
				{
				setState(702);
				_la = _input.LA(1);
				if ( !(_la==DESC || _la==DESCRIBE) ) {
				_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(703);
				match(USER);
				setState(704);
				((DescUserContext)_localctx).name = identifier();
				}
				break;
			case 77:
				_localctx = new DescOrgContext(_localctx);
				enterOuterAlt(_localctx, 77);
				{
				setState(705);
				_la = _input.LA(1);
				if ( !(_la==DESC || _la==DESCRIBE) ) {
				_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(706);
				match(ORG);
				setState(707);
				((DescOrgContext)_localctx).name = identifier();
				}
				break;
			case 78:
				_localctx = new ExplainContext(_localctx);
				enterOuterAlt(_localctx, 78);
				{
				setState(708);
				match(EXPLAIN);
				setState(710);
				_la = _input.LA(1);
				if (_la==EXTENDED) {
					{
					setState(709);
					match(EXTENDED);
					}
				}

				setState(713);
				_la = _input.LA(1);
				if (_la==PLAN) {
					{
					setState(712);
					match(PLAN);
					}
				}

				setState(715);
				query();
				}
				break;
			case 79:
				_localctx = new CreateTemporaryViewContext(_localctx);
				enterOuterAlt(_localctx, 79);
				{
				setState(716);
				match(CREATE);
				setState(719);
				_la = _input.LA(1);
				if (_la==OR) {
					{
					setState(717);
					match(OR);
					setState(718);
					match(REPLACE);
					}
				}

				setState(722);
				_la = _input.LA(1);
				if (_la==CACHE) {
					{
					setState(721);
					match(CACHE);
					}
				}

				setState(724);
				_la = _input.LA(1);
				if ( !(_la==TEMP || _la==TEMPORARY) ) {
				_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(725);
				match(VIEW);
				setState(726);
				((CreateTemporaryViewContext)_localctx).name = identifier();
				setState(728);
				_la = _input.LA(1);
				if (_la==AS) {
					{
					setState(727);
					match(AS);
					}
				}

				setState(730);
				query();
				}
				break;
			case 80:
				_localctx = new StatementContext(_localctx);
				enterOuterAlt(_localctx, 80);
				{
				setState(732);
				((StatementContext)_localctx).statement = _input.LT(1);
				_la = _input.LA(1);
				if ( !(_la==ANALYZE || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (INSERT - 64)) | (1L << (REFRESH - 64)) | (1L << (SELECT - 64)) | (1L << (SET - 64)) | (1L << (WITH - 64)))) != 0)) ) {
					((StatementContext)_localctx).statement = (Token)_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(736);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,75,_ctx);
				while ( _alt!=1 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1+1 ) {
						{
						{
						setState(733);
						matchWildcard();
						}
						} 
					}
					setState(738);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,75,_ctx);
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

	public static class ProcCmdsContext extends ParserRuleContext {
		public List<MqlContext> mql() {
			return getRuleContexts(MqlContext.class);
		}
		public MqlContext mql(int i) {
			return getRuleContext(MqlContext.class,i);
		}
		public ProcCmdsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_procCmds; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterProcCmds(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitProcCmds(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitProcCmds(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ProcCmdsContext procCmds() throws RecognitionException {
		ProcCmdsContext _localctx = new ProcCmdsContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_procCmds);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(741);
			match(T__0);
			setState(742);
			mql();
			setState(747);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__3) {
				{
				{
				setState(743);
				match(T__3);
				setState(744);
				mql();
				}
				}
				setState(749);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(750);
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

	public static class DefinerContext extends ParserRuleContext {
		public IdentifierContext user;
		public TerminalNode CURRENT_USER() { return getToken(MqlBaseParser.CURRENT_USER, 0); }
		public TerminalNode EQ() { return getToken(MqlBaseParser.EQ, 0); }
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
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
		enterRule(_localctx, 6, RULE_definer);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(753);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,78,_ctx) ) {
			case 1:
				{
				setState(752);
				match(EQ);
				}
				break;
			}
			setState(757);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,79,_ctx) ) {
			case 1:
				{
				setState(755);
				((DefinerContext)_localctx).user = identifier();
				}
				break;
			case 2:
				{
				setState(756);
				match(CURRENT_USER);
				}
				break;
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
		enterRule(_localctx, 8, RULE_query);
		int _la;
		try {
			int _alt;
			setState(774);
			switch (_input.LA(1)) {
			case SELECT:
				enterOuterAlt(_localctx, 1);
				{
				setState(759);
				match(SELECT);
				setState(763);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,80,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(760);
						_la = _input.LA(1);
						if ( _la <= 0 || (_la==T__3) ) {
						_errHandler.recoverInline(this);
						} else {
							consume();
						}
						}
						} 
					}
					setState(765);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,80,_ctx);
				}
				}
				break;
			case WITH:
				enterOuterAlt(_localctx, 2);
				{
				setState(766);
				ctes();
				setState(767);
				match(SELECT);
				setState(771);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,81,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(768);
						_la = _input.LA(1);
						if ( _la <= 0 || (_la==T__3) ) {
						_errHandler.recoverInline(this);
						} else {
							consume();
						}
						}
						} 
					}
					setState(773);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,81,_ctx);
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
		enterRule(_localctx, 10, RULE_ctes);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(776);
			match(WITH);
			setState(777);
			namedQuery();
			setState(782);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__2) {
				{
				{
				setState(778);
				match(T__2);
				setState(779);
				namedQuery();
				}
				}
				setState(784);
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

	public static class PartitionSpecContext extends ParserRuleContext {
		public TerminalNode PARTITION() { return getToken(MqlBaseParser.PARTITION, 0); }
		public List<IdentifierContext> identifier() {
			return getRuleContexts(IdentifierContext.class);
		}
		public IdentifierContext identifier(int i) {
			return getRuleContext(IdentifierContext.class,i);
		}
		public PartitionSpecContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_partitionSpec; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterPartitionSpec(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitPartitionSpec(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitPartitionSpec(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PartitionSpecContext partitionSpec() throws RecognitionException {
		PartitionSpecContext _localctx = new PartitionSpecContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_partitionSpec);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(785);
			match(PARTITION);
			setState(786);
			match(T__0);
			setState(787);
			identifier();
			setState(792);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__2) {
				{
				{
				setState(788);
				match(T__2);
				setState(789);
				identifier();
				}
				}
				setState(794);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(795);
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

	public static class CoalesceSpecContext extends ParserRuleContext {
		public Token num;
		public TerminalNode COALESCE() { return getToken(MqlBaseParser.COALESCE, 0); }
		public TerminalNode INTEGER_VALUE() { return getToken(MqlBaseParser.INTEGER_VALUE, 0); }
		public CoalesceSpecContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_coalesceSpec; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterCoalesceSpec(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitCoalesceSpec(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitCoalesceSpec(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CoalesceSpecContext coalesceSpec() throws RecognitionException {
		CoalesceSpecContext _localctx = new CoalesceSpecContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_coalesceSpec);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(797);
			match(COALESCE);
			setState(798);
			((CoalesceSpecContext)_localctx).num = match(INTEGER_VALUE);
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
		enterRule(_localctx, 16, RULE_dataType);
		int _la;
		try {
			setState(834);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,89,_ctx) ) {
			case 1:
				_localctx = new ComplexDataTypeContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(800);
				((ComplexDataTypeContext)_localctx).complex = match(ARRAY);
				setState(801);
				match(T__4);
				setState(802);
				dataType();
				setState(803);
				match(T__5);
				}
				break;
			case 2:
				_localctx = new ComplexDataTypeContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(805);
				((ComplexDataTypeContext)_localctx).complex = match(MAP);
				setState(806);
				match(T__4);
				setState(807);
				dataType();
				setState(808);
				match(T__2);
				setState(809);
				dataType();
				setState(810);
				match(T__5);
				}
				break;
			case 3:
				_localctx = new ComplexDataTypeContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(812);
				((ComplexDataTypeContext)_localctx).complex = match(STRUCT);
				setState(819);
				switch (_input.LA(1)) {
				case T__4:
					{
					setState(813);
					match(T__4);
					setState(815);
					_la = _input.LA(1);
					if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ACCOUNT) | (1L << ADD) | (1L << ALL) | (1L << ALTER) | (1L << ARRAY) | (1L << AT) | (1L << MAP) | (1L << STRUCT) | (1L << AS) | (1L << BY) | (1L << CACHE) | (1L << CALL) | (1L << CASCADE) | (1L << COLUMN) | (1L << COLUMNS) | (1L << COMMENT) | (1L << CHANGE) | (1L << CREATE) | (1L << CURRENT_USER) | (1L << DATABASE) | (1L << DATABASES) | (1L << DATASOURCE) | (1L << DATASOURCES) | (1L << DDL) | (1L << DEFINER) | (1L << DELETE) | (1L << DESC) | (1L << DESCRIBE) | (1L << DISABLE) | (1L << DO) | (1L << DCL) | (1L << DROP) | (1L << ENABLE) | (1L << EQ) | (1L << NEQ) | (1L << EVENT) | (1L << EVENTS) | (1L << EXISTS) | (1L << EXPLAIN) | (1L << EXTENDED) | (1L << FOR) | (1L << FROM) | (1L << FUNCTION) | (1L << FUNCTIONS) | (1L << GLOBAL) | (1L << GRANT) | (1L << GRANTS) | (1L << GROUP) | (1L << GROUPS) | (1L << HQL) | (1L << IDENTIFIED) | (1L << IF) | (1L << IN))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (INSERT - 64)) | (1L << (INTO - 64)) | (1L << (LIKE - 64)) | (1L << (JOBS - 64)) | (1L << (MOUNT - 64)) | (1L << (MQL - 64)) | (1L << (NOT - 64)) | (1L << (ON - 64)) | (1L << (OPTION - 64)) | (1L << (OPTIONS - 64)) | (1L << (OR - 64)) | (1L << (ORG - 64)) | (1L << (ORGANIZATION - 64)) | (1L << (OVERWRITE - 64)) | (1L << (PLAN - 64)) | (1L << (PARTITION - 64)) | (1L << (PROC - 64)) | (1L << (PROCS - 64)) | (1L << (PROCEDURE - 64)) | (1L << (PROCEDURES - 64)) | (1L << (REMOVE - 64)) | (1L << (RENAME - 64)) | (1L << (REFRESH - 64)) | (1L << (REPLACE - 64)) | (1L << (REVOKE - 64)) | (1L << (RUNNING - 64)) | (1L << (SA - 64)) | (1L << (SCHEMA - 64)) | (1L << (SCHEDULE - 64)) | (1L << (SELECT - 64)) | (1L << (SESSION - 64)) | (1L << (SET - 64)) | (1L << (SHOW - 64)) | (1L << (STAR - 64)) | (1L << (STREAM - 64)) | (1L << (SYSINFO - 64)) | (1L << (TABLE - 64)) | (1L << (TABLES - 64)) | (1L << (TEMP - 64)) | (1L << (TEMPORARY - 64)) | (1L << (TO - 64)) | (1L << (TYPE - 64)) | (1L << (TRUNCATE - 64)) | (1L << (UNMOUNT - 64)) | (1L << (UPDATE - 64)) | (1L << (USE - 64)) | (1L << (USING - 64)) | (1L << (USER - 64)) | (1L << (USERS - 64)) | (1L << (VARIABLES - 64)) | (1L << (VIEW - 64)) | (1L << (VIEWS - 64)) | (1L << (WITH - 64)) | (1L << (IDENTIFIER - 64)) | (1L << (BACKQUOTED_IDENTIFIER - 64)))) != 0)) {
						{
						setState(814);
						complexColTypeList();
						}
					}

					setState(817);
					match(T__5);
					}
					break;
				case NEQ:
					{
					setState(818);
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
				setState(821);
				identifier();
				setState(832);
				_la = _input.LA(1);
				if (_la==T__0) {
					{
					setState(822);
					match(T__0);
					setState(823);
					match(INTEGER_VALUE);
					setState(828);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==T__2) {
						{
						{
						setState(824);
						match(T__2);
						setState(825);
						match(INTEGER_VALUE);
						}
						}
						setState(830);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					setState(831);
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
		enterRule(_localctx, 18, RULE_colTypeList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(836);
			colType();
			setState(841);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__2) {
				{
				{
				setState(837);
				match(T__2);
				setState(838);
				colType();
				}
				}
				setState(843);
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
		enterRule(_localctx, 20, RULE_colType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(844);
			identifier();
			setState(845);
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
		enterRule(_localctx, 22, RULE_complexColTypeList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(847);
			complexColType();
			setState(852);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__2) {
				{
				{
				setState(848);
				match(T__2);
				setState(849);
				complexColType();
				}
				}
				setState(854);
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
		enterRule(_localctx, 24, RULE_complexColType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(855);
			identifier();
			setState(856);
			match(T__6);
			setState(857);
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
		enterRule(_localctx, 26, RULE_namedQuery);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(859);
			((NamedQueryContext)_localctx).name = identifier();
			setState(861);
			_la = _input.LA(1);
			if (_la==AS) {
				{
				setState(860);
				match(AS);
				}
			}

			setState(863);
			match(T__0);
			setState(864);
			query();
			setState(865);
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

	public static class GrantPrivilegeListContext extends ParserRuleContext {
		public List<GrantPrivilegeContext> grantPrivilege() {
			return getRuleContexts(GrantPrivilegeContext.class);
		}
		public GrantPrivilegeContext grantPrivilege(int i) {
			return getRuleContext(GrantPrivilegeContext.class,i);
		}
		public GrantPrivilegeListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_grantPrivilegeList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterGrantPrivilegeList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitGrantPrivilegeList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitGrantPrivilegeList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final GrantPrivilegeListContext grantPrivilegeList() throws RecognitionException {
		GrantPrivilegeListContext _localctx = new GrantPrivilegeListContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_grantPrivilegeList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(867);
			grantPrivilege();
			setState(872);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__2) {
				{
				{
				setState(868);
				match(T__2);
				setState(869);
				grantPrivilege();
				}
				}
				setState(874);
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

	public static class GrantPrivilegeContext extends ParserRuleContext {
		public TerminalNode ACCOUNT() { return getToken(MqlBaseParser.ACCOUNT, 0); }
		public TerminalNode DDL() { return getToken(MqlBaseParser.DDL, 0); }
		public TerminalNode DCL() { return getToken(MqlBaseParser.DCL, 0); }
		public GrantPrivilegeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_grantPrivilege; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterGrantPrivilege(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitGrantPrivilege(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitGrantPrivilege(this);
			else return visitor.visitChildren(this);
		}
	}

	public final GrantPrivilegeContext grantPrivilege() throws RecognitionException {
		GrantPrivilegeContext _localctx = new GrantPrivilegeContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_grantPrivilege);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(875);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ACCOUNT) | (1L << DDL) | (1L << DCL))) != 0)) ) {
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

	public static class PrivilegesContext extends ParserRuleContext {
		public List<PrivilegeContext> privilege() {
			return getRuleContexts(PrivilegeContext.class);
		}
		public PrivilegeContext privilege(int i) {
			return getRuleContext(PrivilegeContext.class,i);
		}
		public PrivilegesContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_privileges; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterPrivileges(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitPrivileges(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitPrivileges(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PrivilegesContext privileges() throws RecognitionException {
		PrivilegesContext _localctx = new PrivilegesContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_privileges);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(877);
			privilege();
			setState(882);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__2) {
				{
				{
				setState(878);
				match(T__2);
				setState(879);
				privilege();
				}
				}
				setState(884);
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
		public TerminalNode SELECT() { return getToken(MqlBaseParser.SELECT, 0); }
		public ColumnIdentifiersContext columnIdentifiers() {
			return getRuleContext(ColumnIdentifiersContext.class,0);
		}
		public TerminalNode UPDATE() { return getToken(MqlBaseParser.UPDATE, 0); }
		public TerminalNode INSERT() { return getToken(MqlBaseParser.INSERT, 0); }
		public TerminalNode DELETE() { return getToken(MqlBaseParser.DELETE, 0); }
		public TerminalNode TRUNCATE() { return getToken(MqlBaseParser.TRUNCATE, 0); }
		public TerminalNode ALL() { return getToken(MqlBaseParser.ALL, 0); }
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
		enterRule(_localctx, 34, RULE_privilege);
		int _la;
		try {
			setState(897);
			switch (_input.LA(1)) {
			case SELECT:
				enterOuterAlt(_localctx, 1);
				{
				setState(885);
				match(SELECT);
				setState(887);
				_la = _input.LA(1);
				if (_la==T__0) {
					{
					setState(886);
					columnIdentifiers();
					}
				}

				}
				break;
			case UPDATE:
				enterOuterAlt(_localctx, 2);
				{
				setState(889);
				match(UPDATE);
				setState(891);
				_la = _input.LA(1);
				if (_la==T__0) {
					{
					setState(890);
					columnIdentifiers();
					}
				}

				}
				break;
			case INSERT:
				enterOuterAlt(_localctx, 3);
				{
				setState(893);
				match(INSERT);
				}
				break;
			case DELETE:
				enterOuterAlt(_localctx, 4);
				{
				setState(894);
				match(DELETE);
				}
				break;
			case TRUNCATE:
				enterOuterAlt(_localctx, 5);
				{
				setState(895);
				match(TRUNCATE);
				}
				break;
			case ALL:
				enterOuterAlt(_localctx, 6);
				{
				setState(896);
				match(ALL);
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

	public static class ColumnIdentifiersContext extends ParserRuleContext {
		public List<IdentifierContext> identifier() {
			return getRuleContexts(IdentifierContext.class);
		}
		public IdentifierContext identifier(int i) {
			return getRuleContext(IdentifierContext.class,i);
		}
		public ColumnIdentifiersContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_columnIdentifiers; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterColumnIdentifiers(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitColumnIdentifiers(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitColumnIdentifiers(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ColumnIdentifiersContext columnIdentifiers() throws RecognitionException {
		ColumnIdentifiersContext _localctx = new ColumnIdentifiersContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_columnIdentifiers);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(899);
			match(T__0);
			setState(900);
			identifier();
			setState(905);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__2) {
				{
				{
				setState(901);
				match(T__2);
				setState(902);
				identifier();
				}
				}
				setState(907);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(908);
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

	public static class TableCollectionsContext extends ParserRuleContext {
		public IdentifierOrStarContext db;
		public IdentifierOrStarContext table;
		public List<IdentifierOrStarContext> identifierOrStar() {
			return getRuleContexts(IdentifierOrStarContext.class);
		}
		public IdentifierOrStarContext identifierOrStar(int i) {
			return getRuleContext(IdentifierOrStarContext.class,i);
		}
		public TableCollectionsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_tableCollections; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterTableCollections(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitTableCollections(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitTableCollections(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TableCollectionsContext tableCollections() throws RecognitionException {
		TableCollectionsContext _localctx = new TableCollectionsContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_tableCollections);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(913);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,99,_ctx) ) {
			case 1:
				{
				setState(910);
				((TableCollectionsContext)_localctx).db = identifierOrStar();
				setState(911);
				match(T__7);
				}
				break;
			}
			setState(915);
			((TableCollectionsContext)_localctx).table = identifierOrStar();
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

	public static class IdentifierOrStarContext extends ParserRuleContext {
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public TerminalNode STAR() { return getToken(MqlBaseParser.STAR, 0); }
		public IdentifierOrStarContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_identifierOrStar; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterIdentifierOrStar(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitIdentifierOrStar(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitIdentifierOrStar(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IdentifierOrStarContext identifierOrStar() throws RecognitionException {
		IdentifierOrStarContext _localctx = new IdentifierOrStarContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_identifierOrStar);
		try {
			setState(919);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,100,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(917);
				identifier();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(918);
				match(STAR);
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
		enterRule(_localctx, 42, RULE_addUser);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(921);
			match(ADD);
			setState(922);
			match(USER);
			setState(923);
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
		enterRule(_localctx, 44, RULE_removeUser);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(925);
			match(REMOVE);
			setState(926);
			match(USER);
			setState(927);
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
		enterRule(_localctx, 46, RULE_identifierList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(929);
			identifier();
			setState(934);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__2) {
				{
				{
				setState(930);
				match(T__2);
				setState(931);
				identifier();
				}
				}
				setState(936);
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
		enterRule(_localctx, 48, RULE_funcIdentifier);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(940);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,102,_ctx) ) {
			case 1:
				{
				setState(937);
				((FuncIdentifierContext)_localctx).db = identifier();
				setState(938);
				match(T__7);
				}
				break;
			}
			setState(942);
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
		enterRule(_localctx, 50, RULE_tableIdentifier);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(947);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,103,_ctx) ) {
			case 1:
				{
				setState(944);
				((TableIdentifierContext)_localctx).db = identifier();
				setState(945);
				match(T__7);
				}
				break;
			}
			setState(949);
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
		enterRule(_localctx, 52, RULE_propertyList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(951);
			match(T__0);
			setState(952);
			property();
			setState(957);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__2) {
				{
				{
				setState(953);
				match(T__2);
				setState(954);
				property();
				}
				}
				setState(959);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(960);
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
		enterRule(_localctx, 54, RULE_property);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(962);
			((PropertyContext)_localctx).key = propertyKey();
			setState(964);
			_la = _input.LA(1);
			if (_la==EQ) {
				{
				setState(963);
				match(EQ);
				}
			}

			setState(966);
			((PropertyContext)_localctx).value = match(STRING);
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
		enterRule(_localctx, 56, RULE_propertyKey);
		int _la;
		try {
			setState(977);
			switch (_input.LA(1)) {
			case ACCOUNT:
			case ADD:
			case ALL:
			case ALTER:
			case ARRAY:
			case AT:
			case MAP:
			case STRUCT:
			case AS:
			case BY:
			case CACHE:
			case CALL:
			case CASCADE:
			case COLUMN:
			case COLUMNS:
			case COMMENT:
			case CHANGE:
			case CREATE:
			case CURRENT_USER:
			case DATABASE:
			case DATABASES:
			case DATASOURCE:
			case DATASOURCES:
			case DDL:
			case DEFINER:
			case DELETE:
			case DESC:
			case DESCRIBE:
			case DISABLE:
			case DO:
			case DCL:
			case DROP:
			case ENABLE:
			case EQ:
			case NEQ:
			case EVENT:
			case EVENTS:
			case EXISTS:
			case EXPLAIN:
			case EXTENDED:
			case FOR:
			case FROM:
			case FUNCTION:
			case FUNCTIONS:
			case GLOBAL:
			case GRANT:
			case GRANTS:
			case GROUP:
			case GROUPS:
			case HQL:
			case IDENTIFIED:
			case IF:
			case IN:
			case INSERT:
			case INTO:
			case LIKE:
			case JOBS:
			case MOUNT:
			case MQL:
			case NOT:
			case ON:
			case OPTION:
			case OPTIONS:
			case OR:
			case ORG:
			case ORGANIZATION:
			case OVERWRITE:
			case PLAN:
			case PARTITION:
			case PROC:
			case PROCS:
			case PROCEDURE:
			case PROCEDURES:
			case REMOVE:
			case RENAME:
			case REFRESH:
			case REPLACE:
			case REVOKE:
			case RUNNING:
			case SA:
			case SCHEMA:
			case SCHEDULE:
			case SELECT:
			case SESSION:
			case SET:
			case SHOW:
			case STAR:
			case STREAM:
			case SYSINFO:
			case TABLE:
			case TABLES:
			case TEMP:
			case TEMPORARY:
			case TO:
			case TYPE:
			case TRUNCATE:
			case UNMOUNT:
			case UPDATE:
			case USE:
			case USING:
			case USER:
			case USERS:
			case VARIABLES:
			case VIEW:
			case VIEWS:
			case WITH:
			case IDENTIFIER:
			case BACKQUOTED_IDENTIFIER:
				enterOuterAlt(_localctx, 1);
				{
				setState(968);
				identifier();
				setState(973);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__7) {
					{
					{
					setState(969);
					match(T__7);
					setState(970);
					identifier();
					}
					}
					setState(975);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
				break;
			case STRING:
				enterOuterAlt(_localctx, 2);
				{
				setState(976);
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
		enterRule(_localctx, 58, RULE_password);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(982);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,108,_ctx);
			while ( _alt!=1 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1+1 ) {
					{
					{
					setState(979);
					matchWildcard();
					}
					} 
				}
				setState(984);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,108,_ctx);
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
		public TerminalNode BACKQUOTED_IDENTIFIER() { return getToken(MqlBaseParser.BACKQUOTED_IDENTIFIER, 0); }
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
		enterRule(_localctx, 60, RULE_identifier);
		try {
			setState(988);
			switch (_input.LA(1)) {
			case IDENTIFIER:
				enterOuterAlt(_localctx, 1);
				{
				setState(985);
				match(IDENTIFIER);
				}
				break;
			case BACKQUOTED_IDENTIFIER:
				enterOuterAlt(_localctx, 2);
				{
				setState(986);
				match(BACKQUOTED_IDENTIFIER);
				}
				break;
			case ACCOUNT:
			case ADD:
			case ALL:
			case ALTER:
			case ARRAY:
			case AT:
			case MAP:
			case STRUCT:
			case AS:
			case BY:
			case CACHE:
			case CALL:
			case CASCADE:
			case COLUMN:
			case COLUMNS:
			case COMMENT:
			case CHANGE:
			case CREATE:
			case CURRENT_USER:
			case DATABASE:
			case DATABASES:
			case DATASOURCE:
			case DATASOURCES:
			case DDL:
			case DEFINER:
			case DELETE:
			case DESC:
			case DESCRIBE:
			case DISABLE:
			case DO:
			case DCL:
			case DROP:
			case ENABLE:
			case EQ:
			case NEQ:
			case EVENT:
			case EVENTS:
			case EXISTS:
			case EXPLAIN:
			case EXTENDED:
			case FOR:
			case FROM:
			case FUNCTION:
			case FUNCTIONS:
			case GLOBAL:
			case GRANT:
			case GRANTS:
			case GROUP:
			case GROUPS:
			case HQL:
			case IDENTIFIED:
			case IF:
			case IN:
			case INSERT:
			case INTO:
			case LIKE:
			case JOBS:
			case MOUNT:
			case MQL:
			case NOT:
			case ON:
			case OPTION:
			case OPTIONS:
			case OR:
			case ORG:
			case ORGANIZATION:
			case OVERWRITE:
			case PLAN:
			case PARTITION:
			case PROC:
			case PROCS:
			case PROCEDURE:
			case PROCEDURES:
			case REMOVE:
			case RENAME:
			case REFRESH:
			case REPLACE:
			case REVOKE:
			case RUNNING:
			case SA:
			case SCHEMA:
			case SCHEDULE:
			case SELECT:
			case SESSION:
			case SET:
			case SHOW:
			case STAR:
			case STREAM:
			case SYSINFO:
			case TABLE:
			case TABLES:
			case TEMP:
			case TEMPORARY:
			case TO:
			case TYPE:
			case TRUNCATE:
			case UNMOUNT:
			case UPDATE:
			case USE:
			case USING:
			case USER:
			case USERS:
			case VARIABLES:
			case VIEW:
			case VIEWS:
			case WITH:
				enterOuterAlt(_localctx, 3);
				{
				setState(987);
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

	public static class ResourceContext extends ParserRuleContext {
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public TerminalNode STRING() { return getToken(MqlBaseParser.STRING, 0); }
		public ResourceContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_resource; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterResource(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitResource(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitResource(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ResourceContext resource() throws RecognitionException {
		ResourceContext _localctx = new ResourceContext(_ctx, getState());
		enterRule(_localctx, 62, RULE_resource);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(990);
			identifier();
			setState(991);
			match(STRING);
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
		public TerminalNode ACCOUNT() { return getToken(MqlBaseParser.ACCOUNT, 0); }
		public TerminalNode ADD() { return getToken(MqlBaseParser.ADD, 0); }
		public TerminalNode ALL() { return getToken(MqlBaseParser.ALL, 0); }
		public TerminalNode ALTER() { return getToken(MqlBaseParser.ALTER, 0); }
		public TerminalNode ARRAY() { return getToken(MqlBaseParser.ARRAY, 0); }
		public TerminalNode AT() { return getToken(MqlBaseParser.AT, 0); }
		public TerminalNode MAP() { return getToken(MqlBaseParser.MAP, 0); }
		public TerminalNode STRUCT() { return getToken(MqlBaseParser.STRUCT, 0); }
		public TerminalNode AS() { return getToken(MqlBaseParser.AS, 0); }
		public TerminalNode BY() { return getToken(MqlBaseParser.BY, 0); }
		public TerminalNode CACHE() { return getToken(MqlBaseParser.CACHE, 0); }
		public TerminalNode CALL() { return getToken(MqlBaseParser.CALL, 0); }
		public TerminalNode CASCADE() { return getToken(MqlBaseParser.CASCADE, 0); }
		public TerminalNode COLUMN() { return getToken(MqlBaseParser.COLUMN, 0); }
		public TerminalNode COLUMNS() { return getToken(MqlBaseParser.COLUMNS, 0); }
		public TerminalNode COMMENT() { return getToken(MqlBaseParser.COMMENT, 0); }
		public TerminalNode CHANGE() { return getToken(MqlBaseParser.CHANGE, 0); }
		public TerminalNode CREATE() { return getToken(MqlBaseParser.CREATE, 0); }
		public TerminalNode CURRENT_USER() { return getToken(MqlBaseParser.CURRENT_USER, 0); }
		public TerminalNode DATABASE() { return getToken(MqlBaseParser.DATABASE, 0); }
		public TerminalNode DATABASES() { return getToken(MqlBaseParser.DATABASES, 0); }
		public TerminalNode DATASOURCE() { return getToken(MqlBaseParser.DATASOURCE, 0); }
		public TerminalNode DATASOURCES() { return getToken(MqlBaseParser.DATASOURCES, 0); }
		public TerminalNode DDL() { return getToken(MqlBaseParser.DDL, 0); }
		public TerminalNode DEFINER() { return getToken(MqlBaseParser.DEFINER, 0); }
		public TerminalNode DELETE() { return getToken(MqlBaseParser.DELETE, 0); }
		public TerminalNode DESC() { return getToken(MqlBaseParser.DESC, 0); }
		public TerminalNode DESCRIBE() { return getToken(MqlBaseParser.DESCRIBE, 0); }
		public TerminalNode DISABLE() { return getToken(MqlBaseParser.DISABLE, 0); }
		public TerminalNode DO() { return getToken(MqlBaseParser.DO, 0); }
		public TerminalNode DCL() { return getToken(MqlBaseParser.DCL, 0); }
		public TerminalNode DROP() { return getToken(MqlBaseParser.DROP, 0); }
		public TerminalNode ENABLE() { return getToken(MqlBaseParser.ENABLE, 0); }
		public TerminalNode EQ() { return getToken(MqlBaseParser.EQ, 0); }
		public TerminalNode NEQ() { return getToken(MqlBaseParser.NEQ, 0); }
		public TerminalNode EVENT() { return getToken(MqlBaseParser.EVENT, 0); }
		public TerminalNode EVENTS() { return getToken(MqlBaseParser.EVENTS, 0); }
		public TerminalNode EXISTS() { return getToken(MqlBaseParser.EXISTS, 0); }
		public TerminalNode EXPLAIN() { return getToken(MqlBaseParser.EXPLAIN, 0); }
		public TerminalNode EXTENDED() { return getToken(MqlBaseParser.EXTENDED, 0); }
		public TerminalNode FOR() { return getToken(MqlBaseParser.FOR, 0); }
		public TerminalNode FROM() { return getToken(MqlBaseParser.FROM, 0); }
		public TerminalNode FUNCTION() { return getToken(MqlBaseParser.FUNCTION, 0); }
		public TerminalNode FUNCTIONS() { return getToken(MqlBaseParser.FUNCTIONS, 0); }
		public TerminalNode GLOBAL() { return getToken(MqlBaseParser.GLOBAL, 0); }
		public TerminalNode GRANT() { return getToken(MqlBaseParser.GRANT, 0); }
		public TerminalNode GRANTS() { return getToken(MqlBaseParser.GRANTS, 0); }
		public TerminalNode GROUP() { return getToken(MqlBaseParser.GROUP, 0); }
		public TerminalNode GROUPS() { return getToken(MqlBaseParser.GROUPS, 0); }
		public TerminalNode HQL() { return getToken(MqlBaseParser.HQL, 0); }
		public TerminalNode IDENTIFIED() { return getToken(MqlBaseParser.IDENTIFIED, 0); }
		public TerminalNode IF() { return getToken(MqlBaseParser.IF, 0); }
		public TerminalNode IN() { return getToken(MqlBaseParser.IN, 0); }
		public TerminalNode INSERT() { return getToken(MqlBaseParser.INSERT, 0); }
		public TerminalNode INTO() { return getToken(MqlBaseParser.INTO, 0); }
		public TerminalNode LIKE() { return getToken(MqlBaseParser.LIKE, 0); }
		public TerminalNode JOBS() { return getToken(MqlBaseParser.JOBS, 0); }
		public TerminalNode MOUNT() { return getToken(MqlBaseParser.MOUNT, 0); }
		public TerminalNode MQL() { return getToken(MqlBaseParser.MQL, 0); }
		public TerminalNode NOT() { return getToken(MqlBaseParser.NOT, 0); }
		public TerminalNode ON() { return getToken(MqlBaseParser.ON, 0); }
		public TerminalNode OPTION() { return getToken(MqlBaseParser.OPTION, 0); }
		public TerminalNode OPTIONS() { return getToken(MqlBaseParser.OPTIONS, 0); }
		public TerminalNode OR() { return getToken(MqlBaseParser.OR, 0); }
		public TerminalNode ORG() { return getToken(MqlBaseParser.ORG, 0); }
		public TerminalNode ORGANIZATION() { return getToken(MqlBaseParser.ORGANIZATION, 0); }
		public TerminalNode OVERWRITE() { return getToken(MqlBaseParser.OVERWRITE, 0); }
		public TerminalNode PLAN() { return getToken(MqlBaseParser.PLAN, 0); }
		public TerminalNode PARTITION() { return getToken(MqlBaseParser.PARTITION, 0); }
		public TerminalNode PROC() { return getToken(MqlBaseParser.PROC, 0); }
		public TerminalNode PROCS() { return getToken(MqlBaseParser.PROCS, 0); }
		public TerminalNode PROCEDURE() { return getToken(MqlBaseParser.PROCEDURE, 0); }
		public TerminalNode PROCEDURES() { return getToken(MqlBaseParser.PROCEDURES, 0); }
		public TerminalNode REMOVE() { return getToken(MqlBaseParser.REMOVE, 0); }
		public TerminalNode RENAME() { return getToken(MqlBaseParser.RENAME, 0); }
		public TerminalNode REFRESH() { return getToken(MqlBaseParser.REFRESH, 0); }
		public TerminalNode REPLACE() { return getToken(MqlBaseParser.REPLACE, 0); }
		public TerminalNode REVOKE() { return getToken(MqlBaseParser.REVOKE, 0); }
		public TerminalNode RUNNING() { return getToken(MqlBaseParser.RUNNING, 0); }
		public TerminalNode SA() { return getToken(MqlBaseParser.SA, 0); }
		public TerminalNode SCHEMA() { return getToken(MqlBaseParser.SCHEMA, 0); }
		public TerminalNode SCHEDULE() { return getToken(MqlBaseParser.SCHEDULE, 0); }
		public TerminalNode SELECT() { return getToken(MqlBaseParser.SELECT, 0); }
		public TerminalNode SESSION() { return getToken(MqlBaseParser.SESSION, 0); }
		public TerminalNode SET() { return getToken(MqlBaseParser.SET, 0); }
		public TerminalNode SHOW() { return getToken(MqlBaseParser.SHOW, 0); }
		public TerminalNode STAR() { return getToken(MqlBaseParser.STAR, 0); }
		public TerminalNode STREAM() { return getToken(MqlBaseParser.STREAM, 0); }
		public TerminalNode SYSINFO() { return getToken(MqlBaseParser.SYSINFO, 0); }
		public TerminalNode TABLE() { return getToken(MqlBaseParser.TABLE, 0); }
		public TerminalNode TABLES() { return getToken(MqlBaseParser.TABLES, 0); }
		public TerminalNode TEMP() { return getToken(MqlBaseParser.TEMP, 0); }
		public TerminalNode TEMPORARY() { return getToken(MqlBaseParser.TEMPORARY, 0); }
		public TerminalNode TO() { return getToken(MqlBaseParser.TO, 0); }
		public TerminalNode TYPE() { return getToken(MqlBaseParser.TYPE, 0); }
		public TerminalNode TRUNCATE() { return getToken(MqlBaseParser.TRUNCATE, 0); }
		public TerminalNode UNMOUNT() { return getToken(MqlBaseParser.UNMOUNT, 0); }
		public TerminalNode UPDATE() { return getToken(MqlBaseParser.UPDATE, 0); }
		public TerminalNode USE() { return getToken(MqlBaseParser.USE, 0); }
		public TerminalNode USING() { return getToken(MqlBaseParser.USING, 0); }
		public TerminalNode USER() { return getToken(MqlBaseParser.USER, 0); }
		public TerminalNode USERS() { return getToken(MqlBaseParser.USERS, 0); }
		public TerminalNode VARIABLES() { return getToken(MqlBaseParser.VARIABLES, 0); }
		public TerminalNode VIEW() { return getToken(MqlBaseParser.VIEW, 0); }
		public TerminalNode VIEWS() { return getToken(MqlBaseParser.VIEWS, 0); }
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
		enterRule(_localctx, 64, RULE_nonReserved);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(993);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ACCOUNT) | (1L << ADD) | (1L << ALL) | (1L << ALTER) | (1L << ARRAY) | (1L << AT) | (1L << MAP) | (1L << STRUCT) | (1L << AS) | (1L << BY) | (1L << CACHE) | (1L << CALL) | (1L << CASCADE) | (1L << COLUMN) | (1L << COLUMNS) | (1L << COMMENT) | (1L << CHANGE) | (1L << CREATE) | (1L << CURRENT_USER) | (1L << DATABASE) | (1L << DATABASES) | (1L << DATASOURCE) | (1L << DATASOURCES) | (1L << DDL) | (1L << DEFINER) | (1L << DELETE) | (1L << DESC) | (1L << DESCRIBE) | (1L << DISABLE) | (1L << DO) | (1L << DCL) | (1L << DROP) | (1L << ENABLE) | (1L << EQ) | (1L << NEQ) | (1L << EVENT) | (1L << EVENTS) | (1L << EXISTS) | (1L << EXPLAIN) | (1L << EXTENDED) | (1L << FOR) | (1L << FROM) | (1L << FUNCTION) | (1L << FUNCTIONS) | (1L << GLOBAL) | (1L << GRANT) | (1L << GRANTS) | (1L << GROUP) | (1L << GROUPS) | (1L << HQL) | (1L << IDENTIFIED) | (1L << IF) | (1L << IN))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (INSERT - 64)) | (1L << (INTO - 64)) | (1L << (LIKE - 64)) | (1L << (JOBS - 64)) | (1L << (MOUNT - 64)) | (1L << (MQL - 64)) | (1L << (NOT - 64)) | (1L << (ON - 64)) | (1L << (OPTION - 64)) | (1L << (OPTIONS - 64)) | (1L << (OR - 64)) | (1L << (ORG - 64)) | (1L << (ORGANIZATION - 64)) | (1L << (OVERWRITE - 64)) | (1L << (PLAN - 64)) | (1L << (PARTITION - 64)) | (1L << (PROC - 64)) | (1L << (PROCS - 64)) | (1L << (PROCEDURE - 64)) | (1L << (PROCEDURES - 64)) | (1L << (REMOVE - 64)) | (1L << (RENAME - 64)) | (1L << (REFRESH - 64)) | (1L << (REPLACE - 64)) | (1L << (REVOKE - 64)) | (1L << (RUNNING - 64)) | (1L << (SA - 64)) | (1L << (SCHEMA - 64)) | (1L << (SCHEDULE - 64)) | (1L << (SELECT - 64)) | (1L << (SESSION - 64)) | (1L << (SET - 64)) | (1L << (SHOW - 64)) | (1L << (STAR - 64)) | (1L << (STREAM - 64)) | (1L << (SYSINFO - 64)) | (1L << (TABLE - 64)) | (1L << (TABLES - 64)) | (1L << (TEMP - 64)) | (1L << (TEMPORARY - 64)) | (1L << (TO - 64)) | (1L << (TYPE - 64)) | (1L << (TRUNCATE - 64)) | (1L << (UNMOUNT - 64)) | (1L << (UPDATE - 64)) | (1L << (USE - 64)) | (1L << (USING - 64)) | (1L << (USER - 64)) | (1L << (USERS - 64)) | (1L << (VARIABLES - 64)) | (1L << (VIEW - 64)) | (1L << (VIEWS - 64)) | (1L << (WITH - 64)))) != 0)) ) {
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
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3\u0083\u03e6\4\2\t"+
		"\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\3\2\3\2\3\2\3\3\3\3\3\3\3\3\3\3\5\3M\n\3\3\3\3\3\3\3\5\3R"+
		"\n\3\3\3\3\3\5\3V\n\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\5\3j\n\3\3\3\3\3\5\3n\n\3\3\3\3\3\3\3\3\3\3\3"+
		"\5\3u\n\3\3\3\3\3\3\3\5\3z\n\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3\u0082\n\3\3"+
		"\3\3\3\3\3\3\3\3\3\5\3\u0089\n\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\5\3\u0095\n\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3\u00a0\n\3\3\3"+
		"\3\3\3\3\5\3\u00a5\n\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3\u00af\n\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3\u00b9\n\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3\u00d1"+
		"\n\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3\u00d9\n\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3\u00ef\n\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3\u00f9\n\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3\u0112"+
		"\n\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3\u011a\n\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3\u012e\n\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\5\3\u0136\n\3\3\3\3\3\3\3\5\3\u013b\n\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3"+
		"\u0152\n\3\3\3\3\3\5\3\u0156\n\3\3\3\3\3\5\3\u015a\n\3\3\3\3\3\3\3\3\3"+
		"\5\3\u0160\n\3\3\3\3\3\3\3\3\3\3\3\5\3\u0167\n\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3\u0177\n\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\5\3\u017f\n\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3\u0189\n\3\3\3\3\3"+
		"\5\3\u018d\n\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\5\3\u019e\n\3\3\3\3\3\3\3\5\3\u01a3\n\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\5\3\u01b0\n\3\3\3\3\3\5\3\u01b4\n\3\3\3\3\3\3\3\3\3"+
		"\5\3\u01ba\n\3\3\3\3\3\3\3\3\3\5\3\u01c0\n\3\3\3\3\3\3\3\3\3\5\3\u01c6"+
		"\n\3\3\3\3\3\3\3\3\3\7\3\u01cc\n\3\f\3\16\3\u01cf\13\3\5\3\u01d1\n\3\3"+
		"\3\3\3\5\3\u01d5\n\3\3\3\3\3\3\3\5\3\u01da\n\3\3\3\3\3\3\3\3\3\5\3\u01e0"+
		"\n\3\3\3\3\3\3\3\3\3\5\3\u01e6\n\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\5\3\u01f5\n\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3\u01fd\n\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3\u020f"+
		"\n\3\3\3\3\3\3\3\3\3\5\3\u0215\n\3\3\3\3\3\3\3\3\3\5\3\u021b\n\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\5\3\u0223\n\3\3\3\3\3\5\3\u0227\n\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\5\3\u0244\n\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\5\3\u0252\n\3\3\3\3\3\3\3\3\3\5\3\u0258\n\3\3\3\3\3\3\3"+
		"\3\3\5\3\u025e\n\3\3\3\3\3\5\3\u0262\n\3\3\3\3\3\5\3\u0266\n\3\3\3\3\3"+
		"\3\3\5\3\u026b\n\3\3\3\3\3\5\3\u026f\n\3\3\3\3\3\3\3\3\3\5\3\u0275\n\3"+
		"\3\3\3\3\3\3\3\3\5\3\u027b\n\3\3\3\3\3\3\3\3\3\5\3\u0281\n\3\3\3\3\3\3"+
		"\3\3\3\5\3\u0287\n\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3\u0290\n\3\3\3\3\3"+
		"\3\3\3\3\5\3\u0296\n\3\3\3\3\3\3\3\3\3\5\3\u029c\n\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\5\3\u02b5\n\3\3\3\5\3\u02b8\n\3\3\3\3\3\3\3\3\3\5\3\u02be\n\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3\u02c9\n\3\3\3\5\3\u02cc\n\3\3\3\3"+
		"\3\3\3\3\3\5\3\u02d2\n\3\3\3\5\3\u02d5\n\3\3\3\3\3\3\3\3\3\5\3\u02db\n"+
		"\3\3\3\3\3\3\3\3\3\7\3\u02e1\n\3\f\3\16\3\u02e4\13\3\5\3\u02e6\n\3\3\4"+
		"\3\4\3\4\3\4\7\4\u02ec\n\4\f\4\16\4\u02ef\13\4\3\4\3\4\3\5\5\5\u02f4\n"+
		"\5\3\5\3\5\5\5\u02f8\n\5\3\6\3\6\7\6\u02fc\n\6\f\6\16\6\u02ff\13\6\3\6"+
		"\3\6\3\6\7\6\u0304\n\6\f\6\16\6\u0307\13\6\5\6\u0309\n\6\3\7\3\7\3\7\3"+
		"\7\7\7\u030f\n\7\f\7\16\7\u0312\13\7\3\b\3\b\3\b\3\b\3\b\7\b\u0319\n\b"+
		"\f\b\16\b\u031c\13\b\3\b\3\b\3\t\3\t\3\t\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3"+
		"\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\5\n\u0332\n\n\3\n\3\n\5\n\u0336\n\n\3\n"+
		"\3\n\3\n\3\n\3\n\7\n\u033d\n\n\f\n\16\n\u0340\13\n\3\n\5\n\u0343\n\n\5"+
		"\n\u0345\n\n\3\13\3\13\3\13\7\13\u034a\n\13\f\13\16\13\u034d\13\13\3\f"+
		"\3\f\3\f\3\r\3\r\3\r\7\r\u0355\n\r\f\r\16\r\u0358\13\r\3\16\3\16\3\16"+
		"\3\16\3\17\3\17\5\17\u0360\n\17\3\17\3\17\3\17\3\17\3\20\3\20\3\20\7\20"+
		"\u0369\n\20\f\20\16\20\u036c\13\20\3\21\3\21\3\22\3\22\3\22\7\22\u0373"+
		"\n\22\f\22\16\22\u0376\13\22\3\23\3\23\5\23\u037a\n\23\3\23\3\23\5\23"+
		"\u037e\n\23\3\23\3\23\3\23\3\23\5\23\u0384\n\23\3\24\3\24\3\24\3\24\7"+
		"\24\u038a\n\24\f\24\16\24\u038d\13\24\3\24\3\24\3\25\3\25\3\25\5\25\u0394"+
		"\n\25\3\25\3\25\3\26\3\26\5\26\u039a\n\26\3\27\3\27\3\27\3\27\3\30\3\30"+
		"\3\30\3\30\3\31\3\31\3\31\7\31\u03a7\n\31\f\31\16\31\u03aa\13\31\3\32"+
		"\3\32\3\32\5\32\u03af\n\32\3\32\3\32\3\33\3\33\3\33\5\33\u03b6\n\33\3"+
		"\33\3\33\3\34\3\34\3\34\3\34\7\34\u03be\n\34\f\34\16\34\u03c1\13\34\3"+
		"\34\3\34\3\35\3\35\5\35\u03c7\n\35\3\35\3\35\3\36\3\36\3\36\7\36\u03ce"+
		"\n\36\f\36\16\36\u03d1\13\36\3\36\5\36\u03d4\n\36\3\37\7\37\u03d7\n\37"+
		"\f\37\16\37\u03da\13\37\3 \3 \3 \5 \u03df\n \3!\3!\3!\3\"\3\"\3\"\4\u02e2"+
		"\u03d8\2#\2\4\6\b\n\f\16\20\22\24\26\30\32\34\36 \"$&(*,.\60\62\64\66"+
		"8:<>@B\2\17\4\2NNPP\3\2lm\4\2UUWW\4\2>>HH\4\2))--\4\2\66\66AA\4\2OOQQ"+
		"\4\2VVXX\3\2\'(\b\2\17\17BB[[cceezz\3\2\6\6\5\2\13\13$$++\t\2\13\16\20"+
		"\35\37DFNPPR_az\u0487\2D\3\2\2\2\4\u02e5\3\2\2\2\6\u02e7\3\2\2\2\b\u02f3"+
		"\3\2\2\2\n\u0308\3\2\2\2\f\u030a\3\2\2\2\16\u0313\3\2\2\2\20\u031f\3\2"+
		"\2\2\22\u0344\3\2\2\2\24\u0346\3\2\2\2\26\u034e\3\2\2\2\30\u0351\3\2\2"+
		"\2\32\u0359\3\2\2\2\34\u035d\3\2\2\2\36\u0365\3\2\2\2 \u036d\3\2\2\2\""+
		"\u036f\3\2\2\2$\u0383\3\2\2\2&\u0385\3\2\2\2(\u0393\3\2\2\2*\u0399\3\2"+
		"\2\2,\u039b\3\2\2\2.\u039f\3\2\2\2\60\u03a3\3\2\2\2\62\u03ae\3\2\2\2\64"+
		"\u03b5\3\2\2\2\66\u03b9\3\2\2\28\u03c4\3\2\2\2:\u03d3\3\2\2\2<\u03d8\3"+
		"\2\2\2>\u03de\3\2\2\2@\u03e0\3\2\2\2B\u03e3\3\2\2\2DE\5\4\3\2EF\7\2\2"+
		"\3F\3\3\2\2\2GH\7\35\2\2HL\t\2\2\2IJ\7@\2\2JK\7I\2\2KM\7\62\2\2LI\3\2"+
		"\2\2LM\3\2\2\2MN\3\2\2\2NQ\5> \2OP\7\33\2\2PR\7{\2\2QO\3\2\2\2QR\3\2\2"+
		"\2RU\3\2\2\2ST\7L\2\2TV\5\66\34\2US\3\2\2\2UV\3\2\2\2V\u02e6\3\2\2\2W"+
		"X\7\16\2\2XY\t\2\2\2YZ\5> \2Z[\7e\2\2[\\\7L\2\2\\]\5\66\34\2]\u02e6\3"+
		"\2\2\2^_\7\16\2\2_`\t\2\2\2`a\5> \2ab\7e\2\2bc\7\33\2\2cd\7{\2\2d\u02e6"+
		"\3\2\2\2ef\7,\2\2fi\t\2\2\2gh\7@\2\2hj\7\62\2\2ig\3\2\2\2ij\3\2\2\2jk"+
		"\3\2\2\2km\5> \2ln\7\30\2\2ml\3\2\2\2mn\3\2\2\2n\u02e6\3\2\2\2op\7\35"+
		"\2\2pt\7_\2\2qr\7@\2\2rs\7I\2\2su\7\62\2\2tq\3\2\2\2tu\3\2\2\2uv\3\2\2"+
		"\2vw\5> \2wy\7A\2\2xz\t\2\2\2yx\3\2\2\2yz\3\2\2\2z{\3\2\2\2{|\5> \2|}"+
		"\7?\2\2}~\7\25\2\2~\u0081\5<\37\2\177\u0080\7L\2\2\u0080\u0082\5\66\34"+
		"\2\u0081\177\3\2\2\2\u0081\u0082\3\2\2\2\u0082\u02e6\3\2\2\2\u0083\u0084"+
		"\7\16\2\2\u0084\u0085\7_\2\2\u0085\u0086\5> \2\u0086\u0088\7A\2\2\u0087"+
		"\u0089\t\2\2\2\u0088\u0087\3\2\2\2\u0088\u0089\3\2\2\2\u0089\u008a\3\2"+
		"\2\2\u008a\u008b\5> \2\u008b\u008c\7?\2\2\u008c\u008d\7\25\2\2\u008d\u008e"+
		"\5<\37\2\u008e\u02e6\3\2\2\2\u008f\u0090\7\16\2\2\u0090\u0091\7_\2\2\u0091"+
		"\u0092\5> \2\u0092\u0094\7A\2\2\u0093\u0095\t\2\2\2\u0094\u0093\3\2\2"+
		"\2\u0094\u0095\3\2\2\2\u0095\u0096\3\2\2\2\u0096\u0097\5> \2\u0097\u0098"+
		"\7e\2\2\u0098\u0099\7L\2\2\u0099\u009a\5\66\34\2\u009a\u02e6\3\2\2\2\u009b"+
		"\u009c\7,\2\2\u009c\u009f\7_\2\2\u009d\u009e\7@\2\2\u009e\u00a0\7\62\2"+
		"\2\u009f\u009d\3\2\2\2\u009f\u00a0\3\2\2\2\u00a0\u00a1\3\2\2\2\u00a1\u00a2"+
		"\5> \2\u00a2\u00a4\7A\2\2\u00a3\u00a5\t\2\2\2\u00a4\u00a3\3\2\2\2\u00a4"+
		"\u00a5\3\2\2\2\u00a5\u00a6\3\2\2\2\u00a6\u00a7\5> \2\u00a7\u02e6\3\2\2"+
		"\2\u00a8\u00a9\7:\2\2\u00a9\u00aa\7:\2\2\u00aa\u00ab\7K\2\2\u00ab\u00ac"+
		"\5\36\20\2\u00ac\u00ae\7n\2\2\u00ad\u00af\7u\2\2\u00ae\u00ad\3\2\2\2\u00ae"+
		"\u00af\3\2\2\2\u00af\u00b0\3\2\2\2\u00b0\u00b1\5> \2\u00b1\u02e6\3\2\2"+
		"\2\u00b2\u00b3\7]\2\2\u00b3\u00b4\7:\2\2\u00b4\u00b5\7K\2\2\u00b5\u00b6"+
		"\5\36\20\2\u00b6\u00b8\7\66\2\2\u00b7\u00b9\7u\2\2\u00b8\u00b7\3\2\2\2"+
		"\u00b8\u00b9\3\2\2\2\u00b9\u00ba\3\2\2\2\u00ba\u00bb\5> \2\u00bb\u02e6"+
		"\3\2\2\2\u00bc\u00bd\7:\2\2\u00bd\u00be\7:\2\2\u00be\u00bf\7K\2\2\u00bf"+
		"\u00c0\5\36\20\2\u00c0\u00c1\7n\2\2\u00c1\u00c2\7<\2\2\u00c2\u00c3\5>"+
		" \2\u00c3\u02e6\3\2\2\2\u00c4\u00c5\7]\2\2\u00c5\u00c6\7:\2\2\u00c6\u00c7"+
		"\7K\2\2\u00c7\u00c8\5\36\20\2\u00c8\u00c9\7\66\2\2\u00c9\u00ca\7<\2\2"+
		"\u00ca\u00cb\5> \2\u00cb\u02e6\3\2\2\2\u00cc\u00cd\7:\2\2\u00cd\u00ce"+
		"\5\36\20\2\u00ce\u00d0\7n\2\2\u00cf\u00d1\7u\2\2\u00d0\u00cf\3\2\2\2\u00d0"+
		"\u00d1\3\2\2\2\u00d1\u00d2\3\2\2\2\u00d2\u00d3\5> \2\u00d3\u02e6\3\2\2"+
		"\2\u00d4\u00d5\7]\2\2\u00d5\u00d6\5\36\20\2\u00d6\u00d8\7\66\2\2\u00d7"+
		"\u00d9\7u\2\2\u00d8\u00d7\3\2\2\2\u00d8\u00d9\3\2\2\2\u00d9\u00da\3\2"+
		"\2\2\u00da\u00db\5> \2\u00db\u02e6\3\2\2\2\u00dc\u00dd\7:\2\2\u00dd\u00de"+
		"\5\36\20\2\u00de\u00df\7n\2\2\u00df\u00e0\7<\2\2\u00e0\u00e1\5> \2\u00e1"+
		"\u02e6\3\2\2\2\u00e2\u00e3\7]\2\2\u00e3\u00e4\5\36\20\2\u00e4\u00e5\7"+
		"\66\2\2\u00e5\u00e6\7<\2\2\u00e6\u00e7\5> \2\u00e7\u02e6\3\2\2\2\u00e8"+
		"\u00e9\7:\2\2\u00e9\u00ea\5\"\22\2\u00ea\u00eb\7J\2\2\u00eb\u00ec\5(\25"+
		"\2\u00ec\u00ee\7n\2\2\u00ed\u00ef\7u\2\2\u00ee\u00ed\3\2\2\2\u00ee\u00ef"+
		"\3\2\2\2\u00ef\u00f0\3\2\2\2\u00f0\u00f1\5> \2\u00f1\u02e6\3\2\2\2\u00f2"+
		"\u00f3\7]\2\2\u00f3\u00f4\5\"\22\2\u00f4\u00f5\7J\2\2\u00f5\u00f6\5(\25"+
		"\2\u00f6\u00f8\7\66\2\2\u00f7\u00f9\7u\2\2\u00f8\u00f7\3\2\2\2\u00f8\u00f9"+
		"\3\2\2\2\u00f9\u00fa\3\2\2\2\u00fa\u00fb\5> \2\u00fb\u02e6\3\2\2\2\u00fc"+
		"\u00fd\7:\2\2\u00fd\u00fe\5\"\22\2\u00fe\u00ff\7J\2\2\u00ff\u0100\5(\25"+
		"\2\u0100\u0101\7n\2\2\u0101\u0102\7<\2\2\u0102\u0103\5> \2\u0103\u02e6"+
		"\3\2\2\2\u0104\u0105\7]\2\2\u0105\u0106\5\"\22\2\u0106\u0107\7J\2\2\u0107"+
		"\u0108\5(\25\2\u0108\u0109\7\66\2\2\u0109\u010a\7<\2\2\u010a\u010b\5>"+
		" \2\u010b\u02e6\3\2\2\2\u010c\u010d\7\35\2\2\u010d\u0111\7u\2\2\u010e"+
		"\u010f\7@\2\2\u010f\u0110\7I\2\2\u0110\u0112\7\62\2\2\u0111\u010e\3\2"+
		"\2\2\u0111\u0112\3\2\2\2\u0112\u0113\3\2\2\2\u0113\u0114\5> \2\u0114\u0115"+
		"\7?\2\2\u0115\u0116\7\25\2\2\u0116\u0119\5<\37\2\u0117\u0118\7L\2\2\u0118"+
		"\u011a\5\66\34\2\u0119\u0117\3\2\2\2\u0119\u011a\3\2\2\2\u011a\u02e6\3"+
		"\2\2\2\u011b\u011c\7\16\2\2\u011c\u011d\7u\2\2\u011d\u011e\5> \2\u011e"+
		"\u011f\7?\2\2\u011f\u0120\7\25\2\2\u0120\u0121\5<\37\2\u0121\u02e6\3\2"+
		"\2\2\u0122\u0123\7\16\2\2\u0123\u0124\7u\2\2\u0124\u0125\5> \2\u0125\u0126"+
		"\7e\2\2\u0126\u0127\7L\2\2\u0127\u0128\5\66\34\2\u0128\u02e6\3\2\2\2\u0129"+
		"\u012a\7,\2\2\u012a\u012d\7u\2\2\u012b\u012c\7@\2\2\u012c\u012e\7\62\2"+
		"\2\u012d\u012b\3\2\2\2\u012d\u012e\3\2\2\2\u012e\u012f\3\2\2\2\u012f\u02e6"+
		"\5> \2\u0130\u0131\7\35\2\2\u0131\u0135\7<\2\2\u0132\u0133\7@\2\2\u0133"+
		"\u0134\7I\2\2\u0134\u0136\7\62\2\2\u0135\u0132\3\2\2\2\u0135\u0136\3\2"+
		"\2\2\u0136\u0137\3\2\2\2\u0137\u013a\5> \2\u0138\u0139\7\33\2\2\u0139"+
		"\u013b\7{\2\2\u013a\u0138\3\2\2\2\u013a\u013b\3\2\2\2\u013b\u02e6\3\2"+
		"\2\2\u013c\u013d\7\16\2\2\u013d\u013e\7<\2\2\u013e\u013f\5> \2\u013f\u0140"+
		"\7e\2\2\u0140\u0141\7\33\2\2\u0141\u0142\7{\2\2\u0142\u02e6\3\2\2\2\u0143"+
		"\u0144\7\16\2\2\u0144\u0145\7<\2\2\u0145\u0146\5> \2\u0146\u0147\5,\27"+
		"\2\u0147\u02e6\3\2\2\2\u0148\u0149\7\16\2\2\u0149\u014a\7<\2\2\u014a\u014b"+
		"\5> \2\u014b\u014c\5.\30\2\u014c\u02e6\3\2\2\2\u014d\u014e\7,\2\2\u014e"+
		"\u0151\7<\2\2\u014f\u0150\7@\2\2\u0150\u0152\7\62\2\2\u0151\u014f\3\2"+
		"\2\2\u0151\u0152\3\2\2\2\u0152\u0153\3\2\2\2\u0153\u0155\5> \2\u0154\u0156"+
		"\7\30\2\2\u0155\u0154\3\2\2\2\u0155\u0156\3\2\2\2\u0156\u02e6\3\2\2\2"+
		"\u0157\u0159\7G\2\2\u0158\u015a\7h\2\2\u0159\u0158\3\2\2\2\u0159\u015a"+
		"\3\2\2\2\u015a\u015b\3\2\2\2\u015b\u015f\7j\2\2\u015c\u015d\7@\2\2\u015d"+
		"\u015e\7I\2\2\u015e\u0160\7\62\2\2\u015f\u015c\3\2\2\2\u015f\u0160\3\2"+
		"\2\2\u0160\u0161\3\2\2\2\u0161\u0166\5\64\33\2\u0162\u0163\7\3\2\2\u0163"+
		"\u0164\5\24\13\2\u0164\u0165\7\4\2\2\u0165\u0167\3\2\2\2\u0166\u0162\3"+
		"\2\2\2\u0166\u0167\3\2\2\2\u0167\u0168\3\2\2\2\u0168\u0169\7L\2\2\u0169"+
		"\u016a\5\66\34\2\u016a\u02e6\3\2\2\2\u016b\u016c\7\16\2\2\u016c\u016d"+
		"\7j\2\2\u016d\u016e\5\64\33\2\u016e\u016f\7e\2\2\u016f\u0170\7L\2\2\u0170"+
		"\u0171\5\66\34\2\u0171\u02e6\3\2\2\2\u0172\u0173\7q\2\2\u0173\u0176\7"+
		"j\2\2\u0174\u0175\7@\2\2\u0175\u0177\7\62\2\2\u0176\u0174\3\2\2\2\u0176"+
		"\u0177\3\2\2\2\u0177\u0178\3\2\2\2\u0178\u02e6\5\64\33\2\u0179\u017a\7"+
		"G\2\2\u017a\u017e\7 \2\2\u017b\u017c\7@\2\2\u017c\u017d\7I\2\2\u017d\u017f"+
		"\7\62\2\2\u017e\u017b\3\2\2\2\u017e\u017f\3\2\2\2\u017f\u0180\3\2\2\2"+
		"\u0180\u0181\5> \2\u0181\u0182\7L\2\2\u0182\u0183\5\66\34\2\u0183\u02e6"+
		"\3\2\2\2\u0184\u0185\7q\2\2\u0185\u0188\7 \2\2\u0186\u0187\7@\2\2\u0187"+
		"\u0189\7\62\2\2\u0188\u0186\3\2\2\2\u0188\u0189\3\2\2\2\u0189\u018a\3"+
		"\2\2\2\u018a\u018c\5> \2\u018b\u018d\7\30\2\2\u018c\u018b\3\2\2\2\u018c"+
		"\u018d\3\2\2\2\u018d\u02e6\3\2\2\2\u018e\u018f\7\16\2\2\u018f\u0190\7"+
		" \2\2\u0190\u0191\5> \2\u0191\u0192\7e\2\2\u0192\u0193\7L\2\2\u0193\u0194"+
		"\5\66\34\2\u0194\u02e6\3\2\2\2\u0195\u0196\7[\2\2\u0196\u0197\7 \2\2\u0197"+
		"\u02e6\5> \2\u0198\u0199\7\35\2\2\u0199\u019d\7 \2\2\u019a\u019b\7@\2"+
		"\2\u019b\u019c\7I\2\2\u019c\u019e\7\62\2\2\u019d\u019a\3\2\2\2\u019d\u019e"+
		"\3\2\2\2\u019e\u019f\3\2\2\2\u019f\u01a2\5> \2\u01a0\u01a1\7\33\2\2\u01a1"+
		"\u01a3\7{\2\2\u01a2\u01a0\3\2\2\2\u01a2\u01a3\3\2\2\2\u01a3\u02e6\3\2"+
		"\2\2\u01a4\u01a5\7\16\2\2\u01a5\u01a6\7 \2\2\u01a6\u01a7\5> \2\u01a7\u01a8"+
		"\7e\2\2\u01a8\u01a9\7\33\2\2\u01a9\u01aa\7{\2\2\u01aa\u02e6\3\2\2\2\u01ab"+
		"\u01ac\7,\2\2\u01ac\u01af\7 \2\2\u01ad\u01ae\7@\2\2\u01ae\u01b0\7\62\2"+
		"\2\u01af\u01ad\3\2\2\2\u01af\u01b0\3\2\2\2\u01b0\u01b1\3\2\2\2\u01b1\u01b3"+
		"\5> \2\u01b2\u01b4\7\30\2\2\u01b3\u01b2\3\2\2\2\u01b3\u01b4\3\2\2\2\u01b4"+
		"\u02e6\3\2\2\2\u01b5\u01b6\7s\2\2\u01b6\u02e6\5> \2\u01b7\u01b9\7\35\2"+
		"\2\u01b8\u01ba\t\3\2\2\u01b9\u01b8\3\2\2\2\u01b9\u01ba\3\2\2\2\u01ba\u01bb"+
		"\3\2\2\2\u01bb\u01bf\7\67\2\2\u01bc\u01bd\7@\2\2\u01bd\u01be\7I\2\2\u01be"+
		"\u01c0\7\62\2\2\u01bf\u01bc\3\2\2\2\u01bf\u01c0\3\2\2\2\u01c0\u01c1\3"+
		"\2\2\2\u01c1\u01c2\5\62\32\2\u01c2\u01c3\7\24\2\2\u01c3\u01c5\7{\2\2\u01c4"+
		"\u01c6\7{\2\2\u01c5\u01c4\3\2\2\2\u01c5\u01c6\3\2\2\2\u01c6\u01d0\3\2"+
		"\2\2\u01c7\u01c8\7t\2\2\u01c8\u01cd\5@!\2\u01c9\u01ca\7\5\2\2\u01ca\u01cc"+
		"\5@!\2\u01cb\u01c9\3\2\2\2\u01cc\u01cf\3\2\2\2\u01cd\u01cb\3\2\2\2\u01cd"+
		"\u01ce\3\2\2\2\u01ce\u01d1\3\2\2\2\u01cf\u01cd\3\2\2\2\u01d0\u01c7\3\2"+
		"\2\2\u01d0\u01d1\3\2\2\2\u01d1\u02e6\3\2\2\2\u01d2\u01d4\7,\2\2\u01d3"+
		"\u01d5\t\3\2\2\u01d4\u01d3\3\2\2\2\u01d4\u01d5\3\2\2\2\u01d5\u01d6\3\2"+
		"\2\2\u01d6\u01d9\7\67\2\2\u01d7\u01d8\7@\2\2\u01d8\u01da\7\62\2\2\u01d9"+
		"\u01d7\3\2\2\2\u01d9\u01da\3\2\2\2\u01da\u01db\3\2\2\2\u01db\u02e6\5\62"+
		"\32\2\u01dc\u01df\7\35\2\2\u01dd\u01de\7M\2\2\u01de\u01e0\7\\\2\2\u01df"+
		"\u01dd\3\2\2\2\u01df\u01e0\3\2\2\2\u01e0\u01e1\3\2\2\2\u01e1\u01e2\7x"+
		"\2\2\u01e2\u01e5\5\64\33\2\u01e3\u01e4\7\33\2\2\u01e4\u01e6\7{\2\2\u01e5"+
		"\u01e3\3\2\2\2\u01e5\u01e6\3\2\2\2\u01e6\u01e7\3\2\2\2\u01e7\u01e8\7\24"+
		"\2\2\u01e8\u01e9\5\n\6\2\u01e9\u02e6\3\2\2\2\u01ea\u01eb\7\16\2\2\u01eb"+
		"\u01ec\7x\2\2\u01ec\u01ed\5\64\33\2\u01ed\u01ee\7\24\2\2\u01ee\u01ef\5"+
		"\n\6\2\u01ef\u02e6\3\2\2\2\u01f0\u01f1\7,\2\2\u01f1\u01f4\7x\2\2\u01f2"+
		"\u01f3\7@\2\2\u01f3\u01f5\7\62\2\2\u01f4\u01f2\3\2\2\2\u01f4\u01f5\3\2"+
		"\2\2\u01f5\u01f6\3\2\2\2\u01f6\u02e6\5\64\33\2\u01f7\u01f8\7\35\2\2\u01f8"+
		"\u01fc\t\4\2\2\u01f9\u01fa\7@\2\2\u01fa\u01fb\7I\2\2\u01fb\u01fd\7\62"+
		"\2\2\u01fc\u01f9\3\2\2\2\u01fc\u01fd\3\2\2\2\u01fd\u01fe\3\2\2\2\u01fe"+
		"\u01ff\5> \2\u01ff\u0200\7t\2\2\u0200\u0201\t\5\2\2\u0201\u0202\7\24\2"+
		"\2\u0202\u0203\5\6\4\2\u0203\u02e6\3\2\2\2\u0204\u0205\7\16\2\2\u0205"+
		"\u0206\t\4\2\2\u0206\u0207\5> \2\u0207\u0208\7\24\2\2\u0208\u0209\5\6"+
		"\4\2\u0209\u02e6\3\2\2\2\u020a\u020b\7,\2\2\u020b\u020e\t\4\2\2\u020c"+
		"\u020d\7@\2\2\u020d\u020f\7\62\2\2\u020e\u020c\3\2\2\2\u020e\u020f\3\2"+
		"\2\2\u020f\u0210\3\2\2\2\u0210\u02e6\5> \2\u0211\u0214\7\35\2\2\u0212"+
		"\u0213\7%\2\2\u0213\u0215\5\b\5\2\u0214\u0212\3\2\2\2\u0214\u0215\3\2"+
		"\2\2\u0215\u0216\3\2\2\2\u0216\u021a\7\60\2\2\u0217\u0218\7@\2\2\u0218"+
		"\u0219\7I\2\2\u0219\u021b\7\62\2\2\u021a\u0217\3\2\2\2\u021a\u021b\3\2"+
		"\2\2\u021b\u021c\3\2\2\2\u021c\u021d\5> \2\u021d\u021e\7J\2\2\u021e\u021f"+
		"\7b\2\2\u021f\u0220\7\21\2\2\u0220\u0222\7{\2\2\u0221\u0223\t\6\2\2\u0222"+
		"\u0221\3\2\2\2\u0222\u0223\3\2\2\2\u0223\u0226\3\2\2\2\u0224\u0225\7\33"+
		"\2\2\u0225\u0227\7{\2\2\u0226\u0224\3\2\2\2\u0226\u0227\3\2\2\2\u0227"+
		"\u0228\3\2\2\2\u0228\u0229\7*\2\2\u0229\u022a\7\27\2\2\u022a\u022b\5>"+
		" \2\u022b\u02e6\3\2\2\2\u022c\u022d\7\16\2\2\u022d\u022e\7%\2\2\u022e"+
		"\u022f\5\b\5\2\u022f\u0230\7\60\2\2\u0230\u0231\5> \2\u0231\u02e6\3\2"+
		"\2\2\u0232\u0233\7\16\2\2\u0233\u0234\7\60\2\2\u0234\u0235\5> \2\u0235"+
		"\u0236\7J\2\2\u0236\u0237\7b\2\2\u0237\u0238\7\21\2\2\u0238\u0239\7{\2"+
		"\2\u0239\u02e6\3\2\2\2\u023a\u023b\7\16\2\2\u023b\u023c\7\60\2\2\u023c"+
		"\u023d\5> \2\u023d\u023e\t\6\2\2\u023e\u02e6\3\2\2\2\u023f\u0240\7,\2"+
		"\2\u0240\u0243\7\60\2\2\u0241\u0242\7@\2\2\u0242\u0244\7\62\2\2\u0243"+
		"\u0241\3\2\2\2\u0243\u0244\3\2\2\2\u0244\u0245\3\2\2\2\u0245\u02e6\5>"+
		" \2\u0246\u0247\7f\2\2\u0247\u02e6\7i\2\2\u0248\u0249\7f\2\2\u0249\u02e6"+
		"\7F\2\2\u024a\u024b\7f\2\2\u024b\u024c\7^\2\2\u024c\u02e6\7\61\2\2\u024d"+
		"\u024e\7f\2\2\u024e\u0251\7\61\2\2\u024f\u0250\7D\2\2\u0250\u0252\7{\2"+
		"\2\u0251\u024f\3\2\2\2\u0251\u0252\3\2\2\2\u0252\u02e6\3\2\2\2\u0253\u0254"+
		"\7f\2\2\u0254\u0257\7!\2\2\u0255\u0256\7D\2\2\u0256\u0258\7{\2\2\u0257"+
		"\u0255\3\2\2\2\u0257\u0258\3\2\2\2\u0258\u02e6\3\2\2\2\u0259\u025a\7f"+
		"\2\2\u025a\u025d\7k\2\2\u025b\u025c\t\7\2\2\u025c\u025e\5> \2\u025d\u025b"+
		"\3\2\2\2\u025d\u025e\3\2\2\2\u025e\u0261\3\2\2\2\u025f\u0260\7D\2\2\u0260"+
		"\u0262\7{\2\2\u0261\u025f\3\2\2\2\u0261\u0262\3\2\2\2\u0262\u02e6\3\2"+
		"\2\2\u0263\u0265\7f\2\2\u0264\u0266\5> \2\u0265\u0264\3\2\2\2\u0265\u0266"+
		"\3\2\2\2\u0266\u0267\3\2\2\2\u0267\u026a\78\2\2\u0268\u0269\t\7\2\2\u0269"+
		"\u026b\5> \2\u026a\u0268\3\2\2\2\u026a\u026b\3\2\2\2\u026b\u026e\3\2\2"+
		"\2\u026c\u026d\7D\2\2\u026d\u026f\7{\2\2\u026e\u026c\3\2\2\2\u026e\u026f"+
		"\3\2\2\2\u026f\u02e6\3\2\2\2\u0270\u0271\7f\2\2\u0271\u0274\t\b\2\2\u0272"+
		"\u0273\7D\2\2\u0273\u0275\7{\2\2\u0274\u0272\3\2\2\2\u0274\u0275\3\2\2"+
		"\2\u0275\u02e6\3\2\2\2\u0276\u0277\7f\2\2\u0277\u027a\7`\2\2\u0278\u0279"+
		"\7D\2\2\u0279\u027b\7{\2\2\u027a\u0278\3\2\2\2\u027a\u027b\3\2\2\2\u027b"+
		"\u02e6\3\2\2\2\u027c\u027d\7f\2\2\u027d\u0280\7v\2\2\u027e\u027f\7D\2"+
		"\2\u027f\u0281\7{\2\2\u0280\u027e\3\2\2\2\u0280\u0281\3\2\2\2\u0281\u02e6"+
		"\3\2\2\2\u0282\u0283\7f\2\2\u0283\u0286\7=\2\2\u0284\u0285\7D\2\2\u0285"+
		"\u0287\7{\2\2\u0286\u0284\3\2\2\2\u0286\u0287\3\2\2\2\u0287\u02e6\3\2"+
		"\2\2\u0288\u0289\7f\2\2\u0289\u028a\7v\2\2\u028a\u028b\7A\2\2\u028b\u028c"+
		"\7<\2\2\u028c\u028f\5> \2\u028d\u028e\7D\2\2\u028e\u0290\7{\2\2\u028f"+
		"\u028d\3\2\2\2\u028f\u0290\3\2\2\2\u0290\u02e6\3\2\2\2\u0291\u0292\7f"+
		"\2\2\u0292\u0295\t\t\2\2\u0293\u0294\7D\2\2\u0294\u0296\7{\2\2\u0295\u0293"+
		"\3\2\2\2\u0295\u0296\3\2\2\2\u0296\u02e6\3\2\2\2\u0297\u0298\7f\2\2\u0298"+
		"\u029b\7w\2\2\u0299\u029a\7D\2\2\u029a\u029c\7{\2\2\u029b\u0299\3\2\2"+
		"\2\u029b\u029c\3\2\2\2\u029c\u02e6\3\2\2\2\u029d\u029e\7f\2\2\u029e\u029f"+
		"\7;\2\2\u029f\u02a0\7\65\2\2\u02a0\u02e6\5> \2\u02a1\u02a2\7f\2\2\u02a2"+
		"\u02a3\7\35\2\2\u02a3\u02a4\7j\2\2\u02a4\u02e6\5\64\33\2\u02a5\u02a6\7"+
		"f\2\2\u02a6\u02a7\7a\2\2\u02a7\u02a8\7\65\2\2\u02a8\u02e6\5\n\6\2\u02a9"+
		"\u02aa\t\n\2\2\u02aa\u02ab\7\60\2\2\u02ab\u02e6\5> \2\u02ac\u02ad\t\n"+
		"\2\2\u02ad\u02ae\t\4\2\2\u02ae\u02e6\5> \2\u02af\u02b0\t\n\2\2\u02b0\u02b1"+
		"\7 \2\2\u02b1\u02e6\5> \2\u02b2\u02b4\t\n\2\2\u02b3\u02b5\7j\2\2\u02b4"+
		"\u02b3\3\2\2\2\u02b4\u02b5\3\2\2\2\u02b5\u02b7\3\2\2\2\u02b6\u02b8\7\64"+
		"\2\2\u02b7\u02b6\3\2\2\2\u02b7\u02b8\3\2\2\2\u02b8\u02b9\3\2\2\2\u02b9"+
		"\u02e6\5\64\33\2\u02ba\u02bb\t\n\2\2\u02bb\u02bd\7\67\2\2\u02bc\u02be"+
		"\7\64\2\2\u02bd\u02bc\3\2\2\2\u02bd\u02be\3\2\2\2\u02be\u02bf\3\2\2\2"+
		"\u02bf\u02e6\5\62\32\2\u02c0\u02c1\t\n\2\2\u02c1\u02c2\7u\2\2\u02c2\u02e6"+
		"\5> \2\u02c3\u02c4\t\n\2\2\u02c4\u02c5\7N\2\2\u02c5\u02e6\5> \2\u02c6"+
		"\u02c8\7\63\2\2\u02c7\u02c9\7\64\2\2\u02c8\u02c7\3\2\2\2\u02c8\u02c9\3"+
		"\2\2\2\u02c9\u02cb\3\2\2\2\u02ca\u02cc\7S\2\2\u02cb\u02ca\3\2\2\2\u02cb"+
		"\u02cc\3\2\2\2\u02cc\u02cd\3\2\2\2\u02cd\u02e6\5\n\6\2\u02ce\u02d1\7\35"+
		"\2\2\u02cf\u02d0\7M\2\2\u02d0\u02d2\7\\\2\2\u02d1\u02cf\3\2\2\2\u02d1"+
		"\u02d2\3\2\2\2\u02d2\u02d4\3\2\2\2\u02d3\u02d5\7\26\2\2\u02d4\u02d3\3"+
		"\2\2\2\u02d4\u02d5\3\2\2\2\u02d5\u02d6\3\2\2\2\u02d6\u02d7\t\3\2\2\u02d7"+
		"\u02d8\7x\2\2\u02d8\u02da\5> \2\u02d9\u02db\7\24\2\2\u02da\u02d9\3\2\2"+
		"\2\u02da\u02db\3\2\2\2\u02db\u02dc\3\2\2\2\u02dc\u02dd\5\n\6\2\u02dd\u02e6"+
		"\3\2\2\2\u02de\u02e2\t\13\2\2\u02df\u02e1\13\2\2\2\u02e0\u02df\3\2\2\2"+
		"\u02e1\u02e4\3\2\2\2\u02e2\u02e3\3\2\2\2\u02e2\u02e0\3\2\2\2\u02e3\u02e6"+
		"\3\2\2\2\u02e4\u02e2\3\2\2\2\u02e5G\3\2\2\2\u02e5W\3\2\2\2\u02e5^\3\2"+
		"\2\2\u02e5e\3\2\2\2\u02e5o\3\2\2\2\u02e5\u0083\3\2\2\2\u02e5\u008f\3\2"+
		"\2\2\u02e5\u009b\3\2\2\2\u02e5\u00a8\3\2\2\2\u02e5\u00b2\3\2\2\2\u02e5"+
		"\u00bc\3\2\2\2\u02e5\u00c4\3\2\2\2\u02e5\u00cc\3\2\2\2\u02e5\u00d4\3\2"+
		"\2\2\u02e5\u00dc\3\2\2\2\u02e5\u00e2\3\2\2\2\u02e5\u00e8\3\2\2\2\u02e5"+
		"\u00f2\3\2\2\2\u02e5\u00fc\3\2\2\2\u02e5\u0104\3\2\2\2\u02e5\u010c\3\2"+
		"\2\2\u02e5\u011b\3\2\2\2\u02e5\u0122\3\2\2\2\u02e5\u0129\3\2\2\2\u02e5"+
		"\u0130\3\2\2\2\u02e5\u013c\3\2\2\2\u02e5\u0143\3\2\2\2\u02e5\u0148\3\2"+
		"\2\2\u02e5\u014d\3\2\2\2\u02e5\u0157\3\2\2\2\u02e5\u016b\3\2\2\2\u02e5"+
		"\u0172\3\2\2\2\u02e5\u0179\3\2\2\2\u02e5\u0184\3\2\2\2\u02e5\u018e\3\2"+
		"\2\2\u02e5\u0195\3\2\2\2\u02e5\u0198\3\2\2\2\u02e5\u01a4\3\2\2\2\u02e5"+
		"\u01ab\3\2\2\2\u02e5\u01b5\3\2\2\2\u02e5\u01b7\3\2\2\2\u02e5\u01d2\3\2"+
		"\2\2\u02e5\u01dc\3\2\2\2\u02e5\u01ea\3\2\2\2\u02e5\u01f0\3\2\2\2\u02e5"+
		"\u01f7\3\2\2\2\u02e5\u0204\3\2\2\2\u02e5\u020a\3\2\2\2\u02e5\u0211\3\2"+
		"\2\2\u02e5\u022c\3\2\2\2\u02e5\u0232\3\2\2\2\u02e5\u023a\3\2\2\2\u02e5"+
		"\u023f\3\2\2\2\u02e5\u0246\3\2\2\2\u02e5\u0248\3\2\2\2\u02e5\u024a\3\2"+
		"\2\2\u02e5\u024d\3\2\2\2\u02e5\u0253\3\2\2\2\u02e5\u0259\3\2\2\2\u02e5"+
		"\u0263\3\2\2\2\u02e5\u0270\3\2\2\2\u02e5\u0276\3\2\2\2\u02e5\u027c\3\2"+
		"\2\2\u02e5\u0282\3\2\2\2\u02e5\u0288\3\2\2\2\u02e5\u0291\3\2\2\2\u02e5"+
		"\u0297\3\2\2\2\u02e5\u029d\3\2\2\2\u02e5\u02a1\3\2\2\2\u02e5\u02a5\3\2"+
		"\2\2\u02e5\u02a9\3\2\2\2\u02e5\u02ac\3\2\2\2\u02e5\u02af\3\2\2\2\u02e5"+
		"\u02b2\3\2\2\2\u02e5\u02ba\3\2\2\2\u02e5\u02c0\3\2\2\2\u02e5\u02c3\3\2"+
		"\2\2\u02e5\u02c6\3\2\2\2\u02e5\u02ce\3\2\2\2\u02e5\u02de\3\2\2\2\u02e6"+
		"\5\3\2\2\2\u02e7\u02e8\7\3\2\2\u02e8\u02ed\5\4\3\2\u02e9\u02ea\7\6\2\2"+
		"\u02ea\u02ec\5\4\3\2\u02eb\u02e9\3\2\2\2\u02ec\u02ef\3\2\2\2\u02ed\u02eb"+
		"\3\2\2\2\u02ed\u02ee\3\2\2\2\u02ee\u02f0\3\2\2\2\u02ef\u02ed\3\2\2\2\u02f0"+
		"\u02f1\7\4\2\2\u02f1\7\3\2\2\2\u02f2\u02f4\7.\2\2\u02f3\u02f2\3\2\2\2"+
		"\u02f3\u02f4\3\2\2\2\u02f4\u02f7\3\2\2\2\u02f5\u02f8\5> \2\u02f6\u02f8"+
		"\7\37\2\2\u02f7\u02f5\3\2\2\2\u02f7\u02f6\3\2\2\2\u02f8\t\3\2\2\2\u02f9"+
		"\u02fd\7c\2\2\u02fa\u02fc\n\f\2\2\u02fb\u02fa\3\2\2\2\u02fc\u02ff\3\2"+
		"\2\2\u02fd\u02fb\3\2\2\2\u02fd\u02fe\3\2\2\2\u02fe\u0309\3\2\2\2\u02ff"+
		"\u02fd\3\2\2\2\u0300\u0301\5\f\7\2\u0301\u0305\7c\2\2\u0302\u0304\n\f"+
		"\2\2\u0303\u0302\3\2\2\2\u0304\u0307\3\2\2\2\u0305\u0303\3\2\2\2\u0305"+
		"\u0306\3\2\2\2\u0306\u0309\3\2\2\2\u0307\u0305\3\2\2\2\u0308\u02f9\3\2"+
		"\2\2\u0308\u0300\3\2\2\2\u0309\13\3\2\2\2\u030a\u030b\7z\2\2\u030b\u0310"+
		"\5\34\17\2\u030c\u030d\7\5\2\2\u030d\u030f\5\34\17\2\u030e\u030c\3\2\2"+
		"\2\u030f\u0312\3\2\2\2\u0310\u030e\3\2\2\2\u0310\u0311\3\2\2\2\u0311\r"+
		"\3\2\2\2\u0312\u0310\3\2\2\2\u0313\u0314\7T\2\2\u0314\u0315\7\3\2\2\u0315"+
		"\u031a\5> \2\u0316\u0317\7\5\2\2\u0317\u0319\5> \2\u0318\u0316\3\2\2\2"+
		"\u0319\u031c\3\2\2\2\u031a\u0318\3\2\2\2\u031a\u031b\3\2\2\2\u031b\u031d"+
		"\3\2\2\2\u031c\u031a\3\2\2\2\u031d\u031e\7\4\2\2\u031e\17\3\2\2\2\u031f"+
		"\u0320\7\36\2\2\u0320\u0321\7|\2\2\u0321\21\3\2\2\2\u0322\u0323\7\20\2"+
		"\2\u0323\u0324\7\7\2\2\u0324\u0325\5\22\n\2\u0325\u0326\7\b\2\2\u0326"+
		"\u0345\3\2\2\2\u0327\u0328\7\22\2\2\u0328\u0329\7\7\2\2\u0329\u032a\5"+
		"\22\n\2\u032a\u032b\7\5\2\2\u032b\u032c\5\22\n\2\u032c\u032d\7\b\2\2\u032d"+
		"\u0345\3\2\2\2\u032e\u0335\7\23\2\2\u032f\u0331\7\7\2\2\u0330\u0332\5"+
		"\30\r\2\u0331\u0330\3\2\2\2\u0331\u0332\3\2\2\2\u0332\u0333\3\2\2\2\u0333"+
		"\u0336\7\b\2\2\u0334\u0336\7/\2\2\u0335\u032f\3\2\2\2\u0335\u0334\3\2"+
		"\2\2\u0336\u0345\3\2\2\2\u0337\u0342\5> \2\u0338\u0339\7\3\2\2\u0339\u033e"+
		"\7|\2\2\u033a\u033b\7\5\2\2\u033b\u033d\7|\2\2\u033c\u033a\3\2\2\2\u033d"+
		"\u0340\3\2\2\2\u033e\u033c\3\2\2\2\u033e\u033f\3\2\2\2\u033f\u0341\3\2"+
		"\2\2\u0340\u033e\3\2\2\2\u0341\u0343\7\4\2\2\u0342\u0338\3\2\2\2\u0342"+
		"\u0343\3\2\2\2\u0343\u0345\3\2\2\2\u0344\u0322\3\2\2\2\u0344\u0327\3\2"+
		"\2\2\u0344\u032e\3\2\2\2\u0344\u0337\3\2\2\2\u0345\23\3\2\2\2\u0346\u034b"+
		"\5\26\f\2\u0347\u0348\7\5\2\2\u0348\u034a\5\26\f\2\u0349\u0347\3\2\2\2"+
		"\u034a\u034d\3\2\2\2\u034b\u0349\3\2\2\2\u034b\u034c\3\2\2\2\u034c\25"+
		"\3\2\2\2\u034d\u034b\3\2\2\2\u034e\u034f\5> \2\u034f\u0350\5\22\n\2\u0350"+
		"\27\3\2\2\2\u0351\u0356\5\32\16\2\u0352\u0353\7\5\2\2\u0353\u0355\5\32"+
		"\16\2\u0354\u0352\3\2\2\2\u0355\u0358\3\2\2\2\u0356\u0354\3\2\2\2\u0356"+
		"\u0357\3\2\2\2\u0357\31\3\2\2\2\u0358\u0356\3\2\2\2\u0359\u035a\5> \2"+
		"\u035a\u035b\7\t\2\2\u035b\u035c\5\22\n\2\u035c\33\3\2\2\2\u035d\u035f"+
		"\5> \2\u035e\u0360\7\24\2\2\u035f\u035e\3\2\2\2\u035f\u0360\3\2\2\2\u0360"+
		"\u0361\3\2\2\2\u0361\u0362\7\3\2\2\u0362\u0363\5\n\6\2\u0363\u0364\7\4"+
		"\2\2\u0364\35\3\2\2\2\u0365\u036a\5 \21\2\u0366\u0367\7\5\2\2\u0367\u0369"+
		"\5 \21\2\u0368\u0366\3\2\2\2\u0369\u036c\3\2\2\2\u036a\u0368\3\2\2\2\u036a"+
		"\u036b\3\2\2\2\u036b\37\3\2\2\2\u036c\u036a\3\2\2\2\u036d\u036e\t\r\2"+
		"\2\u036e!\3\2\2\2\u036f\u0374\5$\23\2\u0370\u0371\7\5\2\2\u0371\u0373"+
		"\5$\23\2\u0372\u0370\3\2\2\2\u0373\u0376\3\2\2\2\u0374\u0372\3\2\2\2\u0374"+
		"\u0375\3\2\2\2\u0375#\3\2\2\2\u0376\u0374\3\2\2\2\u0377\u0379\7c\2\2\u0378"+
		"\u037a\5&\24\2\u0379\u0378\3\2\2\2\u0379\u037a\3\2\2\2\u037a\u0384\3\2"+
		"\2\2\u037b\u037d\7r\2\2\u037c\u037e\5&\24\2\u037d\u037c\3\2\2\2\u037d"+
		"\u037e\3\2\2\2\u037e\u0384\3\2\2\2\u037f\u0384\7B\2\2\u0380\u0384\7&\2"+
		"\2\u0381\u0384\7p\2\2\u0382\u0384\7\r\2\2\u0383\u0377\3\2\2\2\u0383\u037b"+
		"\3\2\2\2\u0383\u037f\3\2\2\2\u0383\u0380\3\2\2\2\u0383\u0381\3\2\2\2\u0383"+
		"\u0382\3\2\2\2\u0384%\3\2\2\2\u0385\u0386\7\3\2\2\u0386\u038b\5> \2\u0387"+
		"\u0388\7\5\2\2\u0388\u038a\5> \2\u0389\u0387\3\2\2\2\u038a\u038d\3\2\2"+
		"\2\u038b\u0389\3\2\2\2\u038b\u038c\3\2\2\2\u038c\u038e\3\2\2\2\u038d\u038b"+
		"\3\2\2\2\u038e\u038f\7\4\2\2\u038f\'\3\2\2\2\u0390\u0391\5*\26\2\u0391"+
		"\u0392\7\n\2\2\u0392\u0394\3\2\2\2\u0393\u0390\3\2\2\2\u0393\u0394\3\2"+
		"\2\2\u0394\u0395\3\2\2\2\u0395\u0396\5*\26\2\u0396)\3\2\2\2\u0397\u039a"+
		"\5> \2\u0398\u039a\7g\2\2\u0399\u0397\3\2\2\2\u0399\u0398\3\2\2\2\u039a"+
		"+\3\2\2\2\u039b\u039c\7\f\2\2\u039c\u039d\7u\2\2\u039d\u039e\5\60\31\2"+
		"\u039e-\3\2\2\2\u039f\u03a0\7Y\2\2\u03a0\u03a1\7u\2\2\u03a1\u03a2\5\60"+
		"\31\2\u03a2/\3\2\2\2\u03a3\u03a8\5> \2\u03a4\u03a5\7\5\2\2\u03a5\u03a7"+
		"\5> \2\u03a6\u03a4\3\2\2\2\u03a7\u03aa\3\2\2\2\u03a8\u03a6\3\2\2\2\u03a8"+
		"\u03a9\3\2\2\2\u03a9\61\3\2\2\2\u03aa\u03a8\3\2\2\2\u03ab\u03ac\5> \2"+
		"\u03ac\u03ad\7\n\2\2\u03ad\u03af\3\2\2\2\u03ae\u03ab\3\2\2\2\u03ae\u03af"+
		"\3\2\2\2\u03af\u03b0\3\2\2\2\u03b0\u03b1\5> \2\u03b1\63\3\2\2\2\u03b2"+
		"\u03b3\5> \2\u03b3\u03b4\7\n\2\2\u03b4\u03b6\3\2\2\2\u03b5\u03b2\3\2\2"+
		"\2\u03b5\u03b6\3\2\2\2\u03b6\u03b7\3\2\2\2\u03b7\u03b8\5> \2\u03b8\65"+
		"\3\2\2\2\u03b9\u03ba\7\3\2\2\u03ba\u03bf\58\35\2\u03bb\u03bc\7\5\2\2\u03bc"+
		"\u03be\58\35\2\u03bd\u03bb\3\2\2\2\u03be\u03c1\3\2\2\2\u03bf\u03bd\3\2"+
		"\2\2\u03bf\u03c0\3\2\2\2\u03c0\u03c2\3\2\2\2\u03c1\u03bf\3\2\2\2\u03c2"+
		"\u03c3\7\4\2\2\u03c3\67\3\2\2\2\u03c4\u03c6\5:\36\2\u03c5\u03c7\7.\2\2"+
		"\u03c6\u03c5\3\2\2\2\u03c6\u03c7\3\2\2\2\u03c7\u03c8\3\2\2\2\u03c8\u03c9"+
		"\7{\2\2\u03c99\3\2\2\2\u03ca\u03cf\5> \2\u03cb\u03cc\7\n\2\2\u03cc\u03ce"+
		"\5> \2\u03cd\u03cb\3\2\2\2\u03ce\u03d1\3\2\2\2\u03cf\u03cd\3\2\2\2\u03cf"+
		"\u03d0\3\2\2\2\u03d0\u03d4\3\2\2\2\u03d1\u03cf\3\2\2\2\u03d2\u03d4\7{"+
		"\2\2\u03d3\u03ca\3\2\2\2\u03d3\u03d2\3\2\2\2\u03d4;\3\2\2\2\u03d5\u03d7"+
		"\13\2\2\2\u03d6\u03d5\3\2\2\2\u03d7\u03da\3\2\2\2\u03d8\u03d9\3\2\2\2"+
		"\u03d8\u03d6\3\2\2\2\u03d9=\3\2\2\2\u03da\u03d8\3\2\2\2\u03db\u03df\7"+
		"}\2\2\u03dc\u03df\7~\2\2\u03dd\u03df\5B\"\2\u03de\u03db\3\2\2\2\u03de"+
		"\u03dc\3\2\2\2\u03de\u03dd\3\2\2\2\u03df?\3\2\2\2\u03e0\u03e1\5> \2\u03e1"+
		"\u03e2\7{\2\2\u03e2A\3\2\2\2\u03e3\u03e4\t\16\2\2\u03e4C\3\2\2\2pLQUi"+
		"mty\u0081\u0088\u0094\u009f\u00a4\u00ae\u00b8\u00d0\u00d8\u00ee\u00f8"+
		"\u0111\u0119\u012d\u0135\u013a\u0151\u0155\u0159\u015f\u0166\u0176\u017e"+
		"\u0188\u018c\u019d\u01a2\u01af\u01b3\u01b9\u01bf\u01c5\u01cd\u01d0\u01d4"+
		"\u01d9\u01df\u01e5\u01f4\u01fc\u020e\u0214\u021a\u0222\u0226\u0243\u0251"+
		"\u0257\u025d\u0261\u0265\u026a\u026e\u0274\u027a\u0280\u0286\u028f\u0295"+
		"\u029b\u02b4\u02b7\u02bd\u02c8\u02cb\u02d1\u02d4\u02da\u02e2\u02e5\u02ed"+
		"\u02f3\u02f7\u02fd\u0305\u0308\u0310\u031a\u0331\u0335\u033e\u0342\u0344"+
		"\u034b\u0356\u035f\u036a\u0374\u0379\u037d\u0383\u038b\u0393\u0399\u03a8"+
		"\u03ae\u03b5\u03bf\u03c6\u03cf\u03d3\u03d8\u03de";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}