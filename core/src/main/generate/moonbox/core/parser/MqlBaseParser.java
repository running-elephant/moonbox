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
		RULE_propertyKeyList = 28, RULE_propertyKey = 29, RULE_password = 30, 
		RULE_identifier = 31, RULE_resource = 32, RULE_nonReserved = 33;
	public static final String[] ruleNames = {
		"single", "mql", "procCmds", "definer", "query", "ctes", "partitionSpec", 
		"coalesceSpec", "dataType", "colTypeList", "colType", "complexColTypeList", 
		"complexColType", "namedQuery", "grantPrivilegeList", "grantPrivilege", 
		"privileges", "privilege", "columnIdentifiers", "tableCollections", "identifierOrStar", 
		"addUser", "removeUser", "identifierList", "funcIdentifier", "tableIdentifier", 
		"propertyList", "property", "propertyKeyList", "propertyKey", "password", 
		"identifier", "resource", "nonReserved"
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
			setState(68);
			mql();
			setState(69);
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
	public static class RemoveUserOptionsContext extends MqlContext {
		public IdentifierContext name;
		public TerminalNode ALTER() { return getToken(MqlBaseParser.ALTER, 0); }
		public TerminalNode USER() { return getToken(MqlBaseParser.USER, 0); }
		public TerminalNode REMOVE() { return getToken(MqlBaseParser.REMOVE, 0); }
		public TerminalNode OPTIONS() { return getToken(MqlBaseParser.OPTIONS, 0); }
		public PropertyKeyListContext propertyKeyList() {
			return getRuleContext(PropertyKeyListContext.class,0);
		}
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public RemoveUserOptionsContext(MqlContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterRemoveUserOptions(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitRemoveUserOptions(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitRemoveUserOptions(this);
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
	public static class SetProcedureNameContext extends MqlContext {
		public IdentifierContext name;
		public IdentifierContext newName;
		public TerminalNode ALTER() { return getToken(MqlBaseParser.ALTER, 0); }
		public TerminalNode RENAME() { return getToken(MqlBaseParser.RENAME, 0); }
		public TerminalNode TO() { return getToken(MqlBaseParser.TO, 0); }
		public TerminalNode PROC() { return getToken(MqlBaseParser.PROC, 0); }
		public TerminalNode PROCEDURE() { return getToken(MqlBaseParser.PROCEDURE, 0); }
		public List<IdentifierContext> identifier() {
			return getRuleContexts(IdentifierContext.class);
		}
		public IdentifierContext identifier(int i) {
			return getRuleContext(IdentifierContext.class,i);
		}
		public SetProcedureNameContext(MqlContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterSetProcedureName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitSetProcedureName(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitSetProcedureName(this);
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
	public static class RemoveDatabasePropertiesContext extends MqlContext {
		public IdentifierContext name;
		public TerminalNode ALTER() { return getToken(MqlBaseParser.ALTER, 0); }
		public TerminalNode DATABASE() { return getToken(MqlBaseParser.DATABASE, 0); }
		public TerminalNode REMOVE() { return getToken(MqlBaseParser.REMOVE, 0); }
		public TerminalNode OPTIONS() { return getToken(MqlBaseParser.OPTIONS, 0); }
		public PropertyKeyListContext propertyKeyList() {
			return getRuleContext(PropertyKeyListContext.class,0);
		}
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public RemoveDatabasePropertiesContext(MqlContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterRemoveDatabaseProperties(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitRemoveDatabaseProperties(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitRemoveDatabaseProperties(this);
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
	public static class RenameProcedureContext extends MqlContext {
		public IdentifierContext name;
		public IdentifierContext newName;
		public TerminalNode RENAME() { return getToken(MqlBaseParser.RENAME, 0); }
		public TerminalNode TO() { return getToken(MqlBaseParser.TO, 0); }
		public TerminalNode PROC() { return getToken(MqlBaseParser.PROC, 0); }
		public TerminalNode PROCEDURE() { return getToken(MqlBaseParser.PROCEDURE, 0); }
		public List<IdentifierContext> identifier() {
			return getRuleContexts(IdentifierContext.class);
		}
		public IdentifierContext identifier(int i) {
			return getRuleContext(IdentifierContext.class,i);
		}
		public RenameProcedureContext(MqlContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterRenameProcedure(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitRenameProcedure(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitRenameProcedure(this);
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
	public static class RemoveOrganizationOptionsContext extends MqlContext {
		public IdentifierContext name;
		public TerminalNode ALTER() { return getToken(MqlBaseParser.ALTER, 0); }
		public TerminalNode REMOVE() { return getToken(MqlBaseParser.REMOVE, 0); }
		public TerminalNode OPTIONS() { return getToken(MqlBaseParser.OPTIONS, 0); }
		public PropertyKeyListContext propertyKeyList() {
			return getRuleContext(PropertyKeyListContext.class,0);
		}
		public TerminalNode ORG() { return getToken(MqlBaseParser.ORG, 0); }
		public TerminalNode ORGANIZATION() { return getToken(MqlBaseParser.ORGANIZATION, 0); }
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public RemoveOrganizationOptionsContext(MqlContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterRemoveOrganizationOptions(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitRemoveOrganizationOptions(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitRemoveOrganizationOptions(this);
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
	public static class RenameSaContext extends MqlContext {
		public IdentifierContext name;
		public IdentifierContext org;
		public IdentifierContext newName;
		public TerminalNode RENAME() { return getToken(MqlBaseParser.RENAME, 0); }
		public TerminalNode SA() { return getToken(MqlBaseParser.SA, 0); }
		public TerminalNode IN() { return getToken(MqlBaseParser.IN, 0); }
		public TerminalNode TO() { return getToken(MqlBaseParser.TO, 0); }
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
	public static class StatementContext extends MqlContext {
		public Token statement;
		public TerminalNode SELECT() { return getToken(MqlBaseParser.SELECT, 0); }
		public TerminalNode WITH() { return getToken(MqlBaseParser.WITH, 0); }
		public TerminalNode INSERT() { return getToken(MqlBaseParser.INSERT, 0); }
		public TerminalNode SET() { return getToken(MqlBaseParser.SET, 0); }
		public TerminalNode ANALYZE() { return getToken(MqlBaseParser.ANALYZE, 0); }
		public TerminalNode REFRESH() { return getToken(MqlBaseParser.REFRESH, 0); }
		public TerminalNode SHOW() { return getToken(MqlBaseParser.SHOW, 0); }
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
	public static class RemoveTablePropertiesContext extends MqlContext {
		public TableIdentifierContext name;
		public TerminalNode ALTER() { return getToken(MqlBaseParser.ALTER, 0); }
		public TerminalNode TABLE() { return getToken(MqlBaseParser.TABLE, 0); }
		public TerminalNode REMOVE() { return getToken(MqlBaseParser.REMOVE, 0); }
		public TerminalNode OPTIONS() { return getToken(MqlBaseParser.OPTIONS, 0); }
		public PropertyKeyListContext propertyKeyList() {
			return getRuleContext(PropertyKeyListContext.class,0);
		}
		public TableIdentifierContext tableIdentifier() {
			return getRuleContext(TableIdentifierContext.class,0);
		}
		public RemoveTablePropertiesContext(MqlContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterRemoveTableProperties(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitRemoveTableProperties(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitRemoveTableProperties(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class RemoveSaOptionsContext extends MqlContext {
		public IdentifierContext name;
		public IdentifierContext org;
		public TerminalNode ALTER() { return getToken(MqlBaseParser.ALTER, 0); }
		public TerminalNode SA() { return getToken(MqlBaseParser.SA, 0); }
		public TerminalNode IN() { return getToken(MqlBaseParser.IN, 0); }
		public TerminalNode REMOVE() { return getToken(MqlBaseParser.REMOVE, 0); }
		public TerminalNode OPTIONS() { return getToken(MqlBaseParser.OPTIONS, 0); }
		public PropertyKeyListContext propertyKeyList() {
			return getRuleContext(PropertyKeyListContext.class,0);
		}
		public List<IdentifierContext> identifier() {
			return getRuleContexts(IdentifierContext.class);
		}
		public IdentifierContext identifier(int i) {
			return getRuleContext(IdentifierContext.class,i);
		}
		public TerminalNode ORG() { return getToken(MqlBaseParser.ORG, 0); }
		public TerminalNode ORGANIZATION() { return getToken(MqlBaseParser.ORGANIZATION, 0); }
		public RemoveSaOptionsContext(MqlContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterRemoveSaOptions(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitRemoveSaOptions(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitRemoveSaOptions(this);
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
	public static class SetSaNameContext extends MqlContext {
		public IdentifierContext name;
		public IdentifierContext org;
		public IdentifierContext newName;
		public TerminalNode ALTER() { return getToken(MqlBaseParser.ALTER, 0); }
		public TerminalNode SA() { return getToken(MqlBaseParser.SA, 0); }
		public TerminalNode IN() { return getToken(MqlBaseParser.IN, 0); }
		public TerminalNode RENAME() { return getToken(MqlBaseParser.RENAME, 0); }
		public TerminalNode TO() { return getToken(MqlBaseParser.TO, 0); }
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
			setState(895);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,79,_ctx) ) {
			case 1:
				_localctx = new CreateOrganizationContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(71);
				match(CREATE);
				setState(72);
				_la = _input.LA(1);
				if ( !(_la==ORG || _la==ORGANIZATION) ) {
				_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(76);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,0,_ctx) ) {
				case 1:
					{
					setState(73);
					match(IF);
					setState(74);
					match(NOT);
					setState(75);
					match(EXISTS);
					}
					break;
				}
				setState(78);
				((CreateOrganizationContext)_localctx).name = identifier();
				setState(81);
				_la = _input.LA(1);
				if (_la==COMMENT) {
					{
					setState(79);
					match(COMMENT);
					setState(80);
					((CreateOrganizationContext)_localctx).comment = match(STRING);
					}
				}

				setState(85);
				_la = _input.LA(1);
				if (_la==OPTIONS) {
					{
					setState(83);
					match(OPTIONS);
					setState(84);
					propertyList();
					}
				}

				}
				break;
			case 2:
				_localctx = new RenameOrganizationContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(87);
				match(RENAME);
				setState(88);
				_la = _input.LA(1);
				if ( !(_la==ORG || _la==ORGANIZATION) ) {
				_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(89);
				((RenameOrganizationContext)_localctx).name = identifier();
				setState(90);
				match(TO);
				setState(91);
				((RenameOrganizationContext)_localctx).newName = identifier();
				}
				break;
			case 3:
				_localctx = new SetOrganizationNameContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(93);
				match(ALTER);
				setState(94);
				_la = _input.LA(1);
				if ( !(_la==ORG || _la==ORGANIZATION) ) {
				_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(95);
				((SetOrganizationNameContext)_localctx).name = identifier();
				setState(96);
				match(RENAME);
				setState(97);
				match(TO);
				setState(98);
				((SetOrganizationNameContext)_localctx).newName = identifier();
				}
				break;
			case 4:
				_localctx = new SetOrganizationOptionsContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(100);
				match(ALTER);
				setState(101);
				_la = _input.LA(1);
				if ( !(_la==ORG || _la==ORGANIZATION) ) {
				_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(102);
				((SetOrganizationOptionsContext)_localctx).name = identifier();
				setState(103);
				match(SET);
				setState(104);
				match(OPTIONS);
				setState(105);
				propertyList();
				}
				break;
			case 5:
				_localctx = new RemoveOrganizationOptionsContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(107);
				match(ALTER);
				setState(108);
				_la = _input.LA(1);
				if ( !(_la==ORG || _la==ORGANIZATION) ) {
				_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(109);
				((RemoveOrganizationOptionsContext)_localctx).name = identifier();
				setState(110);
				match(REMOVE);
				setState(111);
				match(OPTIONS);
				setState(112);
				propertyKeyList();
				}
				break;
			case 6:
				_localctx = new SetOrganizationCommentContext(_localctx);
				enterOuterAlt(_localctx, 6);
				{
				setState(114);
				match(ALTER);
				setState(115);
				_la = _input.LA(1);
				if ( !(_la==ORG || _la==ORGANIZATION) ) {
				_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(116);
				((SetOrganizationCommentContext)_localctx).name = identifier();
				setState(117);
				match(SET);
				setState(118);
				match(COMMENT);
				setState(119);
				((SetOrganizationCommentContext)_localctx).comment = match(STRING);
				}
				break;
			case 7:
				_localctx = new DropOrganizationContext(_localctx);
				enterOuterAlt(_localctx, 7);
				{
				setState(121);
				match(DROP);
				setState(122);
				_la = _input.LA(1);
				if ( !(_la==ORG || _la==ORGANIZATION) ) {
				_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(125);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,3,_ctx) ) {
				case 1:
					{
					setState(123);
					match(IF);
					setState(124);
					match(EXISTS);
					}
					break;
				}
				setState(127);
				((DropOrganizationContext)_localctx).name = identifier();
				setState(129);
				_la = _input.LA(1);
				if (_la==CASCADE) {
					{
					setState(128);
					match(CASCADE);
					}
				}

				}
				break;
			case 8:
				_localctx = new CreateSaContext(_localctx);
				enterOuterAlt(_localctx, 8);
				{
				setState(131);
				match(CREATE);
				setState(132);
				match(SA);
				setState(136);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,5,_ctx) ) {
				case 1:
					{
					setState(133);
					match(IF);
					setState(134);
					match(NOT);
					setState(135);
					match(EXISTS);
					}
					break;
				}
				setState(138);
				((CreateSaContext)_localctx).name = identifier();
				setState(139);
				match(IN);
				setState(141);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,6,_ctx) ) {
				case 1:
					{
					setState(140);
					_la = _input.LA(1);
					if ( !(_la==ORG || _la==ORGANIZATION) ) {
					_errHandler.recoverInline(this);
					} else {
						consume();
					}
					}
					break;
				}
				setState(143);
				((CreateSaContext)_localctx).org = identifier();
				setState(144);
				match(IDENTIFIED);
				setState(145);
				match(BY);
				setState(146);
				((CreateSaContext)_localctx).pwd = password();
				setState(149);
				_la = _input.LA(1);
				if (_la==OPTIONS) {
					{
					setState(147);
					match(OPTIONS);
					setState(148);
					propertyList();
					}
				}

				}
				break;
			case 9:
				_localctx = new RenameSaContext(_localctx);
				enterOuterAlt(_localctx, 9);
				{
				setState(151);
				match(RENAME);
				setState(152);
				match(SA);
				setState(153);
				((RenameSaContext)_localctx).name = identifier();
				setState(154);
				match(IN);
				setState(156);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,8,_ctx) ) {
				case 1:
					{
					setState(155);
					_la = _input.LA(1);
					if ( !(_la==ORG || _la==ORGANIZATION) ) {
					_errHandler.recoverInline(this);
					} else {
						consume();
					}
					}
					break;
				}
				setState(158);
				((RenameSaContext)_localctx).org = identifier();
				setState(159);
				match(TO);
				setState(160);
				((RenameSaContext)_localctx).newName = identifier();
				}
				break;
			case 10:
				_localctx = new SetSaNameContext(_localctx);
				enterOuterAlt(_localctx, 10);
				{
				setState(162);
				match(ALTER);
				setState(163);
				match(SA);
				setState(164);
				((SetSaNameContext)_localctx).name = identifier();
				setState(165);
				match(IN);
				setState(167);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,9,_ctx) ) {
				case 1:
					{
					setState(166);
					_la = _input.LA(1);
					if ( !(_la==ORG || _la==ORGANIZATION) ) {
					_errHandler.recoverInline(this);
					} else {
						consume();
					}
					}
					break;
				}
				setState(169);
				((SetSaNameContext)_localctx).org = identifier();
				setState(170);
				match(RENAME);
				setState(171);
				match(TO);
				setState(172);
				((SetSaNameContext)_localctx).newName = identifier();
				}
				break;
			case 11:
				_localctx = new SetSaPasswordContext(_localctx);
				enterOuterAlt(_localctx, 11);
				{
				setState(174);
				match(ALTER);
				setState(175);
				match(SA);
				setState(176);
				((SetSaPasswordContext)_localctx).name = identifier();
				setState(177);
				match(IN);
				setState(179);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,10,_ctx) ) {
				case 1:
					{
					setState(178);
					_la = _input.LA(1);
					if ( !(_la==ORG || _la==ORGANIZATION) ) {
					_errHandler.recoverInline(this);
					} else {
						consume();
					}
					}
					break;
				}
				setState(181);
				((SetSaPasswordContext)_localctx).org = identifier();
				setState(182);
				match(IDENTIFIED);
				setState(183);
				match(BY);
				setState(184);
				((SetSaPasswordContext)_localctx).pwd = password();
				}
				break;
			case 12:
				_localctx = new SetSaOptionsContext(_localctx);
				enterOuterAlt(_localctx, 12);
				{
				setState(186);
				match(ALTER);
				setState(187);
				match(SA);
				setState(188);
				((SetSaOptionsContext)_localctx).name = identifier();
				setState(189);
				match(IN);
				setState(191);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,11,_ctx) ) {
				case 1:
					{
					setState(190);
					_la = _input.LA(1);
					if ( !(_la==ORG || _la==ORGANIZATION) ) {
					_errHandler.recoverInline(this);
					} else {
						consume();
					}
					}
					break;
				}
				setState(193);
				((SetSaOptionsContext)_localctx).org = identifier();
				setState(194);
				match(SET);
				setState(195);
				match(OPTIONS);
				setState(196);
				propertyList();
				}
				break;
			case 13:
				_localctx = new RemoveSaOptionsContext(_localctx);
				enterOuterAlt(_localctx, 13);
				{
				setState(198);
				match(ALTER);
				setState(199);
				match(SA);
				setState(200);
				((RemoveSaOptionsContext)_localctx).name = identifier();
				setState(201);
				match(IN);
				setState(203);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,12,_ctx) ) {
				case 1:
					{
					setState(202);
					_la = _input.LA(1);
					if ( !(_la==ORG || _la==ORGANIZATION) ) {
					_errHandler.recoverInline(this);
					} else {
						consume();
					}
					}
					break;
				}
				setState(205);
				((RemoveSaOptionsContext)_localctx).org = identifier();
				setState(206);
				match(REMOVE);
				setState(207);
				match(OPTIONS);
				setState(208);
				propertyKeyList();
				}
				break;
			case 14:
				_localctx = new DropSaContext(_localctx);
				enterOuterAlt(_localctx, 14);
				{
				setState(210);
				match(DROP);
				setState(211);
				match(SA);
				setState(214);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,13,_ctx) ) {
				case 1:
					{
					setState(212);
					match(IF);
					setState(213);
					match(EXISTS);
					}
					break;
				}
				setState(216);
				((DropSaContext)_localctx).name = identifier();
				setState(217);
				match(IN);
				setState(219);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,14,_ctx) ) {
				case 1:
					{
					setState(218);
					_la = _input.LA(1);
					if ( !(_la==ORG || _la==ORGANIZATION) ) {
					_errHandler.recoverInline(this);
					} else {
						consume();
					}
					}
					break;
				}
				setState(221);
				((DropSaContext)_localctx).org = identifier();
				}
				break;
			case 15:
				_localctx = new GrantGrantToUserContext(_localctx);
				enterOuterAlt(_localctx, 15);
				{
				setState(223);
				match(GRANT);
				setState(224);
				match(GRANT);
				setState(225);
				match(OPTION);
				setState(226);
				grantPrivilegeList();
				setState(227);
				match(TO);
				setState(229);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,15,_ctx) ) {
				case 1:
					{
					setState(228);
					match(USER);
					}
					break;
				}
				setState(231);
				((GrantGrantToUserContext)_localctx).user = identifier();
				}
				break;
			case 16:
				_localctx = new RevokeGrantFromUserContext(_localctx);
				enterOuterAlt(_localctx, 16);
				{
				setState(233);
				match(REVOKE);
				setState(234);
				match(GRANT);
				setState(235);
				match(OPTION);
				setState(236);
				grantPrivilegeList();
				setState(237);
				match(FROM);
				setState(239);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,16,_ctx) ) {
				case 1:
					{
					setState(238);
					match(USER);
					}
					break;
				}
				setState(241);
				((RevokeGrantFromUserContext)_localctx).user = identifier();
				}
				break;
			case 17:
				_localctx = new GrantGrantToGroupContext(_localctx);
				enterOuterAlt(_localctx, 17);
				{
				setState(243);
				match(GRANT);
				setState(244);
				match(GRANT);
				setState(245);
				match(OPTION);
				setState(246);
				grantPrivilegeList();
				setState(247);
				match(TO);
				setState(248);
				match(GROUP);
				setState(249);
				((GrantGrantToGroupContext)_localctx).group = identifier();
				}
				break;
			case 18:
				_localctx = new RevokeGrantFromGroupContext(_localctx);
				enterOuterAlt(_localctx, 18);
				{
				setState(251);
				match(REVOKE);
				setState(252);
				match(GRANT);
				setState(253);
				match(OPTION);
				setState(254);
				grantPrivilegeList();
				setState(255);
				match(FROM);
				setState(256);
				match(GROUP);
				setState(257);
				((RevokeGrantFromGroupContext)_localctx).group = identifier();
				}
				break;
			case 19:
				_localctx = new GrantPrivilegeToUsersContext(_localctx);
				enterOuterAlt(_localctx, 19);
				{
				setState(259);
				match(GRANT);
				setState(260);
				grantPrivilegeList();
				setState(261);
				match(TO);
				setState(263);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,17,_ctx) ) {
				case 1:
					{
					setState(262);
					match(USER);
					}
					break;
				}
				setState(265);
				((GrantPrivilegeToUsersContext)_localctx).user = identifier();
				}
				break;
			case 20:
				_localctx = new RevokePrivilegeFromUsersContext(_localctx);
				enterOuterAlt(_localctx, 20);
				{
				setState(267);
				match(REVOKE);
				setState(268);
				grantPrivilegeList();
				setState(269);
				match(FROM);
				setState(271);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,18,_ctx) ) {
				case 1:
					{
					setState(270);
					match(USER);
					}
					break;
				}
				setState(273);
				((RevokePrivilegeFromUsersContext)_localctx).user = identifier();
				}
				break;
			case 21:
				_localctx = new GrantPrivilegeToGroupContext(_localctx);
				enterOuterAlt(_localctx, 21);
				{
				setState(275);
				match(GRANT);
				setState(276);
				grantPrivilegeList();
				setState(277);
				match(TO);
				setState(278);
				match(GROUP);
				setState(279);
				((GrantPrivilegeToGroupContext)_localctx).group = identifier();
				}
				break;
			case 22:
				_localctx = new RevokePrivilegeFromGroupContext(_localctx);
				enterOuterAlt(_localctx, 22);
				{
				setState(281);
				match(REVOKE);
				setState(282);
				grantPrivilegeList();
				setState(283);
				match(FROM);
				setState(284);
				match(GROUP);
				setState(285);
				((RevokePrivilegeFromGroupContext)_localctx).group = identifier();
				}
				break;
			case 23:
				_localctx = new GrantResourcePrivilegeToUsersContext(_localctx);
				enterOuterAlt(_localctx, 23);
				{
				setState(287);
				match(GRANT);
				setState(288);
				privileges();
				setState(289);
				match(ON);
				setState(290);
				tableCollections();
				setState(291);
				match(TO);
				setState(293);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,19,_ctx) ) {
				case 1:
					{
					setState(292);
					match(USER);
					}
					break;
				}
				setState(295);
				((GrantResourcePrivilegeToUsersContext)_localctx).user = identifier();
				}
				break;
			case 24:
				_localctx = new RevokeResourcePrivilegeFromUsersContext(_localctx);
				enterOuterAlt(_localctx, 24);
				{
				setState(297);
				match(REVOKE);
				setState(298);
				privileges();
				setState(299);
				match(ON);
				setState(300);
				tableCollections();
				setState(301);
				match(FROM);
				setState(303);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,20,_ctx) ) {
				case 1:
					{
					setState(302);
					match(USER);
					}
					break;
				}
				setState(305);
				((RevokeResourcePrivilegeFromUsersContext)_localctx).user = identifier();
				}
				break;
			case 25:
				_localctx = new GrantResourcePrivilegeToGroupContext(_localctx);
				enterOuterAlt(_localctx, 25);
				{
				setState(307);
				match(GRANT);
				setState(308);
				privileges();
				setState(309);
				match(ON);
				setState(310);
				tableCollections();
				setState(311);
				match(TO);
				setState(312);
				match(GROUP);
				setState(313);
				((GrantResourcePrivilegeToGroupContext)_localctx).group = identifier();
				}
				break;
			case 26:
				_localctx = new RevokeResourcePrivilegeFromGroupContext(_localctx);
				enterOuterAlt(_localctx, 26);
				{
				setState(315);
				match(REVOKE);
				setState(316);
				privileges();
				setState(317);
				match(ON);
				setState(318);
				tableCollections();
				setState(319);
				match(FROM);
				setState(320);
				match(GROUP);
				setState(321);
				((RevokeResourcePrivilegeFromGroupContext)_localctx).group = identifier();
				}
				break;
			case 27:
				_localctx = new CreateUserContext(_localctx);
				enterOuterAlt(_localctx, 27);
				{
				setState(323);
				match(CREATE);
				setState(324);
				match(USER);
				setState(328);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,21,_ctx) ) {
				case 1:
					{
					setState(325);
					match(IF);
					setState(326);
					match(NOT);
					setState(327);
					match(EXISTS);
					}
					break;
				}
				setState(330);
				((CreateUserContext)_localctx).name = identifier();
				setState(331);
				match(IDENTIFIED);
				setState(332);
				match(BY);
				setState(333);
				((CreateUserContext)_localctx).pwd = password();
				setState(336);
				_la = _input.LA(1);
				if (_la==OPTIONS) {
					{
					setState(334);
					match(OPTIONS);
					setState(335);
					propertyList();
					}
				}

				}
				break;
			case 28:
				_localctx = new RenameUserContext(_localctx);
				enterOuterAlt(_localctx, 28);
				{
				setState(338);
				match(RENAME);
				setState(339);
				match(USER);
				setState(340);
				((RenameUserContext)_localctx).name = identifier();
				setState(341);
				match(TO);
				setState(342);
				((RenameUserContext)_localctx).newName = identifier();
				}
				break;
			case 29:
				_localctx = new SetUserNameContext(_localctx);
				enterOuterAlt(_localctx, 29);
				{
				setState(344);
				match(ALTER);
				setState(345);
				match(USER);
				setState(346);
				((SetUserNameContext)_localctx).name = identifier();
				setState(347);
				match(RENAME);
				setState(348);
				match(TO);
				setState(349);
				((SetUserNameContext)_localctx).newName = identifier();
				}
				break;
			case 30:
				_localctx = new SetUserPasswordContext(_localctx);
				enterOuterAlt(_localctx, 30);
				{
				setState(351);
				match(ALTER);
				setState(352);
				match(USER);
				setState(353);
				((SetUserPasswordContext)_localctx).name = identifier();
				setState(354);
				match(IDENTIFIED);
				setState(355);
				match(BY);
				setState(356);
				((SetUserPasswordContext)_localctx).pwd = password();
				}
				break;
			case 31:
				_localctx = new SetUserOptionsContext(_localctx);
				enterOuterAlt(_localctx, 31);
				{
				setState(358);
				match(ALTER);
				setState(359);
				match(USER);
				setState(360);
				((SetUserOptionsContext)_localctx).name = identifier();
				setState(361);
				match(SET);
				setState(362);
				match(OPTIONS);
				setState(363);
				propertyList();
				}
				break;
			case 32:
				_localctx = new RemoveUserOptionsContext(_localctx);
				enterOuterAlt(_localctx, 32);
				{
				setState(365);
				match(ALTER);
				setState(366);
				match(USER);
				setState(367);
				((RemoveUserOptionsContext)_localctx).name = identifier();
				setState(368);
				match(REMOVE);
				setState(369);
				match(OPTIONS);
				setState(370);
				propertyKeyList();
				}
				break;
			case 33:
				_localctx = new DropUserContext(_localctx);
				enterOuterAlt(_localctx, 33);
				{
				setState(372);
				match(DROP);
				setState(373);
				match(USER);
				setState(376);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,23,_ctx) ) {
				case 1:
					{
					setState(374);
					match(IF);
					setState(375);
					match(EXISTS);
					}
					break;
				}
				setState(378);
				((DropUserContext)_localctx).name = identifier();
				}
				break;
			case 34:
				_localctx = new CreateGroupContext(_localctx);
				enterOuterAlt(_localctx, 34);
				{
				setState(379);
				match(CREATE);
				setState(380);
				match(GROUP);
				setState(384);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,24,_ctx) ) {
				case 1:
					{
					setState(381);
					match(IF);
					setState(382);
					match(NOT);
					setState(383);
					match(EXISTS);
					}
					break;
				}
				setState(386);
				((CreateGroupContext)_localctx).name = identifier();
				setState(389);
				_la = _input.LA(1);
				if (_la==COMMENT) {
					{
					setState(387);
					match(COMMENT);
					setState(388);
					((CreateGroupContext)_localctx).comment = match(STRING);
					}
				}

				}
				break;
			case 35:
				_localctx = new RenameGroupContext(_localctx);
				enterOuterAlt(_localctx, 35);
				{
				setState(391);
				match(RENAME);
				setState(392);
				match(GROUP);
				setState(393);
				((RenameGroupContext)_localctx).name = identifier();
				setState(394);
				match(TO);
				setState(395);
				((RenameGroupContext)_localctx).newName = identifier();
				}
				break;
			case 36:
				_localctx = new SetGroupNameContext(_localctx);
				enterOuterAlt(_localctx, 36);
				{
				setState(397);
				match(ALTER);
				setState(398);
				match(GROUP);
				setState(399);
				((SetGroupNameContext)_localctx).name = identifier();
				setState(400);
				match(RENAME);
				setState(401);
				match(TO);
				setState(402);
				((SetGroupNameContext)_localctx).newName = identifier();
				}
				break;
			case 37:
				_localctx = new SetGroupCommentContext(_localctx);
				enterOuterAlt(_localctx, 37);
				{
				setState(404);
				match(ALTER);
				setState(405);
				match(GROUP);
				setState(406);
				((SetGroupCommentContext)_localctx).name = identifier();
				setState(407);
				match(SET);
				setState(408);
				match(COMMENT);
				setState(409);
				((SetGroupCommentContext)_localctx).comment = match(STRING);
				}
				break;
			case 38:
				_localctx = new AddGroupUserContext(_localctx);
				enterOuterAlt(_localctx, 38);
				{
				setState(411);
				match(ALTER);
				setState(412);
				match(GROUP);
				setState(413);
				((AddGroupUserContext)_localctx).name = identifier();
				setState(414);
				addUser();
				}
				break;
			case 39:
				_localctx = new RemoveGroupUserContext(_localctx);
				enterOuterAlt(_localctx, 39);
				{
				setState(416);
				match(ALTER);
				setState(417);
				match(GROUP);
				setState(418);
				((RemoveGroupUserContext)_localctx).name = identifier();
				setState(419);
				removeUser();
				}
				break;
			case 40:
				_localctx = new DropGroupContext(_localctx);
				enterOuterAlt(_localctx, 40);
				{
				setState(421);
				match(DROP);
				setState(422);
				match(GROUP);
				setState(425);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,26,_ctx) ) {
				case 1:
					{
					setState(423);
					match(IF);
					setState(424);
					match(EXISTS);
					}
					break;
				}
				setState(427);
				((DropGroupContext)_localctx).name = identifier();
				setState(429);
				_la = _input.LA(1);
				if (_la==CASCADE) {
					{
					setState(428);
					match(CASCADE);
					}
				}

				}
				break;
			case 41:
				_localctx = new MountTableContext(_localctx);
				enterOuterAlt(_localctx, 41);
				{
				setState(431);
				match(MOUNT);
				setState(433);
				_la = _input.LA(1);
				if (_la==STREAM) {
					{
					setState(432);
					match(STREAM);
					}
				}

				setState(435);
				match(TABLE);
				setState(439);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,29,_ctx) ) {
				case 1:
					{
					setState(436);
					match(IF);
					setState(437);
					match(NOT);
					setState(438);
					match(EXISTS);
					}
					break;
				}
				setState(441);
				tableIdentifier();
				setState(446);
				_la = _input.LA(1);
				if (_la==T__0) {
					{
					setState(442);
					match(T__0);
					setState(443);
					((MountTableContext)_localctx).columns = colTypeList();
					setState(444);
					match(T__1);
					}
				}

				setState(448);
				match(OPTIONS);
				setState(449);
				propertyList();
				}
				break;
			case 42:
				_localctx = new RenameTableContext(_localctx);
				enterOuterAlt(_localctx, 42);
				{
				setState(451);
				match(RENAME);
				setState(452);
				match(TABLE);
				setState(453);
				((RenameTableContext)_localctx).name = tableIdentifier();
				setState(454);
				match(TO);
				setState(455);
				((RenameTableContext)_localctx).newName = tableIdentifier();
				}
				break;
			case 43:
				_localctx = new SetTableNameContext(_localctx);
				enterOuterAlt(_localctx, 43);
				{
				setState(457);
				match(ALTER);
				setState(458);
				match(TABLE);
				setState(459);
				((SetTableNameContext)_localctx).name = tableIdentifier();
				setState(460);
				match(RENAME);
				setState(461);
				match(TO);
				setState(462);
				((SetTableNameContext)_localctx).newName = tableIdentifier();
				}
				break;
			case 44:
				_localctx = new SetTablePropertiesContext(_localctx);
				enterOuterAlt(_localctx, 44);
				{
				setState(464);
				match(ALTER);
				setState(465);
				match(TABLE);
				setState(466);
				((SetTablePropertiesContext)_localctx).name = tableIdentifier();
				setState(467);
				match(SET);
				setState(468);
				match(OPTIONS);
				setState(469);
				propertyList();
				}
				break;
			case 45:
				_localctx = new RemoveTablePropertiesContext(_localctx);
				enterOuterAlt(_localctx, 45);
				{
				setState(471);
				match(ALTER);
				setState(472);
				match(TABLE);
				setState(473);
				((RemoveTablePropertiesContext)_localctx).name = tableIdentifier();
				setState(474);
				match(REMOVE);
				setState(475);
				match(OPTIONS);
				setState(476);
				propertyKeyList();
				}
				break;
			case 46:
				_localctx = new UnmountTableContext(_localctx);
				enterOuterAlt(_localctx, 46);
				{
				setState(478);
				match(UNMOUNT);
				setState(479);
				match(TABLE);
				setState(482);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,31,_ctx) ) {
				case 1:
					{
					setState(480);
					match(IF);
					setState(481);
					match(EXISTS);
					}
					break;
				}
				setState(484);
				((UnmountTableContext)_localctx).name = tableIdentifier();
				}
				break;
			case 47:
				_localctx = new MountDatabaseContext(_localctx);
				enterOuterAlt(_localctx, 47);
				{
				setState(485);
				match(MOUNT);
				setState(486);
				match(DATABASE);
				setState(490);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,32,_ctx) ) {
				case 1:
					{
					setState(487);
					match(IF);
					setState(488);
					match(NOT);
					setState(489);
					match(EXISTS);
					}
					break;
				}
				setState(492);
				((MountDatabaseContext)_localctx).name = identifier();
				setState(493);
				match(OPTIONS);
				setState(494);
				propertyList();
				}
				break;
			case 48:
				_localctx = new UnmountDatabaseContext(_localctx);
				enterOuterAlt(_localctx, 48);
				{
				setState(496);
				match(UNMOUNT);
				setState(497);
				match(DATABASE);
				setState(500);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,33,_ctx) ) {
				case 1:
					{
					setState(498);
					match(IF);
					setState(499);
					match(EXISTS);
					}
					break;
				}
				setState(502);
				((UnmountDatabaseContext)_localctx).name = identifier();
				setState(504);
				_la = _input.LA(1);
				if (_la==CASCADE) {
					{
					setState(503);
					match(CASCADE);
					}
				}

				}
				break;
			case 49:
				_localctx = new SetDatabasePropertiesContext(_localctx);
				enterOuterAlt(_localctx, 49);
				{
				setState(506);
				match(ALTER);
				setState(507);
				match(DATABASE);
				setState(508);
				((SetDatabasePropertiesContext)_localctx).name = identifier();
				setState(509);
				match(SET);
				setState(510);
				match(OPTIONS);
				setState(511);
				propertyList();
				}
				break;
			case 50:
				_localctx = new RemoveDatabasePropertiesContext(_localctx);
				enterOuterAlt(_localctx, 50);
				{
				setState(513);
				match(ALTER);
				setState(514);
				match(DATABASE);
				setState(515);
				((RemoveDatabasePropertiesContext)_localctx).name = identifier();
				setState(516);
				match(REMOVE);
				setState(517);
				match(OPTIONS);
				setState(518);
				propertyKeyList();
				}
				break;
			case 51:
				_localctx = new RefreshDatabaseContext(_localctx);
				enterOuterAlt(_localctx, 51);
				{
				setState(520);
				match(REFRESH);
				setState(521);
				match(DATABASE);
				setState(522);
				((RefreshDatabaseContext)_localctx).name = identifier();
				}
				break;
			case 52:
				_localctx = new CreateDatabaseContext(_localctx);
				enterOuterAlt(_localctx, 52);
				{
				setState(523);
				match(CREATE);
				setState(524);
				match(DATABASE);
				setState(528);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,35,_ctx) ) {
				case 1:
					{
					setState(525);
					match(IF);
					setState(526);
					match(NOT);
					setState(527);
					match(EXISTS);
					}
					break;
				}
				setState(530);
				((CreateDatabaseContext)_localctx).name = identifier();
				setState(533);
				_la = _input.LA(1);
				if (_la==COMMENT) {
					{
					setState(531);
					match(COMMENT);
					setState(532);
					((CreateDatabaseContext)_localctx).comment = match(STRING);
					}
				}

				}
				break;
			case 53:
				_localctx = new RenameDatabaseContext(_localctx);
				enterOuterAlt(_localctx, 53);
				{
				setState(535);
				match(RENAME);
				setState(536);
				match(DATABASE);
				setState(537);
				((RenameDatabaseContext)_localctx).name = identifier();
				setState(538);
				match(TO);
				setState(539);
				((RenameDatabaseContext)_localctx).newName = identifier();
				}
				break;
			case 54:
				_localctx = new SetDatabaseNameContext(_localctx);
				enterOuterAlt(_localctx, 54);
				{
				setState(541);
				match(ALTER);
				setState(542);
				match(DATABASE);
				setState(543);
				((SetDatabaseNameContext)_localctx).name = identifier();
				setState(544);
				match(RENAME);
				setState(545);
				match(TO);
				setState(546);
				((SetDatabaseNameContext)_localctx).newName = identifier();
				}
				break;
			case 55:
				_localctx = new SetDatabaseCommentContext(_localctx);
				enterOuterAlt(_localctx, 55);
				{
				setState(548);
				match(ALTER);
				setState(549);
				match(DATABASE);
				setState(550);
				((SetDatabaseCommentContext)_localctx).name = identifier();
				setState(551);
				match(SET);
				setState(552);
				match(COMMENT);
				setState(553);
				((SetDatabaseCommentContext)_localctx).comment = match(STRING);
				}
				break;
			case 56:
				_localctx = new DropDatabaseContext(_localctx);
				enterOuterAlt(_localctx, 56);
				{
				setState(555);
				match(DROP);
				setState(556);
				match(DATABASE);
				setState(559);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,37,_ctx) ) {
				case 1:
					{
					setState(557);
					match(IF);
					setState(558);
					match(EXISTS);
					}
					break;
				}
				setState(561);
				((DropDatabaseContext)_localctx).name = identifier();
				setState(563);
				_la = _input.LA(1);
				if (_la==CASCADE) {
					{
					setState(562);
					match(CASCADE);
					}
				}

				}
				break;
			case 57:
				_localctx = new UseDatabaseContext(_localctx);
				enterOuterAlt(_localctx, 57);
				{
				setState(565);
				match(USE);
				setState(566);
				((UseDatabaseContext)_localctx).db = identifier();
				}
				break;
			case 58:
				_localctx = new CreateFunctionContext(_localctx);
				enterOuterAlt(_localctx, 58);
				{
				setState(567);
				match(CREATE);
				setState(569);
				_la = _input.LA(1);
				if (_la==TEMP || _la==TEMPORARY) {
					{
					setState(568);
					_la = _input.LA(1);
					if ( !(_la==TEMP || _la==TEMPORARY) ) {
					_errHandler.recoverInline(this);
					} else {
						consume();
					}
					}
				}

				setState(571);
				match(FUNCTION);
				setState(575);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,40,_ctx) ) {
				case 1:
					{
					setState(572);
					match(IF);
					setState(573);
					match(NOT);
					setState(574);
					match(EXISTS);
					}
					break;
				}
				setState(577);
				((CreateFunctionContext)_localctx).name = funcIdentifier();
				setState(578);
				match(AS);
				setState(579);
				((CreateFunctionContext)_localctx).className = match(STRING);
				setState(581);
				_la = _input.LA(1);
				if (_la==STRING) {
					{
					setState(580);
					((CreateFunctionContext)_localctx).methodName = match(STRING);
					}
				}

				setState(592);
				_la = _input.LA(1);
				if (_la==USING) {
					{
					setState(583);
					match(USING);
					setState(584);
					resource();
					setState(589);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==T__2) {
						{
						{
						setState(585);
						match(T__2);
						setState(586);
						resource();
						}
						}
						setState(591);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					}
				}

				}
				break;
			case 59:
				_localctx = new DropFunctionContext(_localctx);
				enterOuterAlt(_localctx, 59);
				{
				setState(594);
				match(DROP);
				setState(596);
				_la = _input.LA(1);
				if (_la==TEMP || _la==TEMPORARY) {
					{
					setState(595);
					_la = _input.LA(1);
					if ( !(_la==TEMP || _la==TEMPORARY) ) {
					_errHandler.recoverInline(this);
					} else {
						consume();
					}
					}
				}

				setState(598);
				match(FUNCTION);
				setState(601);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,45,_ctx) ) {
				case 1:
					{
					setState(599);
					match(IF);
					setState(600);
					match(EXISTS);
					}
					break;
				}
				setState(603);
				((DropFunctionContext)_localctx).name = funcIdentifier();
				}
				break;
			case 60:
				_localctx = new CreateViewContext(_localctx);
				enterOuterAlt(_localctx, 60);
				{
				setState(604);
				match(CREATE);
				setState(607);
				_la = _input.LA(1);
				if (_la==OR) {
					{
					setState(605);
					match(OR);
					setState(606);
					match(REPLACE);
					}
				}

				setState(609);
				match(VIEW);
				setState(610);
				((CreateViewContext)_localctx).name = tableIdentifier();
				setState(613);
				_la = _input.LA(1);
				if (_la==COMMENT) {
					{
					setState(611);
					match(COMMENT);
					setState(612);
					((CreateViewContext)_localctx).comment = match(STRING);
					}
				}

				setState(615);
				match(AS);
				setState(616);
				query();
				}
				break;
			case 61:
				_localctx = new SetViewQueryContext(_localctx);
				enterOuterAlt(_localctx, 61);
				{
				setState(618);
				match(ALTER);
				setState(619);
				match(VIEW);
				setState(620);
				((SetViewQueryContext)_localctx).name = tableIdentifier();
				setState(621);
				match(AS);
				setState(622);
				query();
				}
				break;
			case 62:
				_localctx = new DropViewContext(_localctx);
				enterOuterAlt(_localctx, 62);
				{
				setState(624);
				match(DROP);
				setState(625);
				match(VIEW);
				setState(628);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,48,_ctx) ) {
				case 1:
					{
					setState(626);
					match(IF);
					setState(627);
					match(EXISTS);
					}
					break;
				}
				setState(630);
				((DropViewContext)_localctx).name = tableIdentifier();
				}
				break;
			case 63:
				_localctx = new CreateProcedureContext(_localctx);
				enterOuterAlt(_localctx, 63);
				{
				setState(631);
				match(CREATE);
				setState(632);
				_la = _input.LA(1);
				if ( !(_la==PROC || _la==PROCEDURE) ) {
				_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(636);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,49,_ctx) ) {
				case 1:
					{
					setState(633);
					match(IF);
					setState(634);
					match(NOT);
					setState(635);
					match(EXISTS);
					}
					break;
				}
				setState(638);
				((CreateProcedureContext)_localctx).name = identifier();
				setState(639);
				match(USING);
				setState(640);
				_la = _input.LA(1);
				if ( !(_la==HQL || _la==MQL) ) {
				_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(641);
				match(AS);
				setState(642);
				procCmds();
				}
				break;
			case 64:
				_localctx = new RenameProcedureContext(_localctx);
				enterOuterAlt(_localctx, 64);
				{
				setState(644);
				match(RENAME);
				setState(645);
				_la = _input.LA(1);
				if ( !(_la==PROC || _la==PROCEDURE) ) {
				_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(646);
				((RenameProcedureContext)_localctx).name = identifier();
				setState(647);
				match(TO);
				setState(648);
				((RenameProcedureContext)_localctx).newName = identifier();
				}
				break;
			case 65:
				_localctx = new SetProcedureNameContext(_localctx);
				enterOuterAlt(_localctx, 65);
				{
				setState(650);
				match(ALTER);
				setState(651);
				_la = _input.LA(1);
				if ( !(_la==PROC || _la==PROCEDURE) ) {
				_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(652);
				((SetProcedureNameContext)_localctx).name = identifier();
				setState(653);
				match(RENAME);
				setState(654);
				match(TO);
				setState(655);
				((SetProcedureNameContext)_localctx).newName = identifier();
				}
				break;
			case 66:
				_localctx = new SetProcedureQuerysContext(_localctx);
				enterOuterAlt(_localctx, 66);
				{
				setState(657);
				match(ALTER);
				setState(658);
				_la = _input.LA(1);
				if ( !(_la==PROC || _la==PROCEDURE) ) {
				_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(659);
				((SetProcedureQuerysContext)_localctx).name = identifier();
				setState(660);
				match(AS);
				setState(661);
				procCmds();
				}
				break;
			case 67:
				_localctx = new DropProcedureContext(_localctx);
				enterOuterAlt(_localctx, 67);
				{
				setState(663);
				match(DROP);
				setState(664);
				_la = _input.LA(1);
				if ( !(_la==PROC || _la==PROCEDURE) ) {
				_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(667);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,50,_ctx) ) {
				case 1:
					{
					setState(665);
					match(IF);
					setState(666);
					match(EXISTS);
					}
					break;
				}
				setState(669);
				((DropProcedureContext)_localctx).name = identifier();
				}
				break;
			case 68:
				_localctx = new CreateEventContext(_localctx);
				enterOuterAlt(_localctx, 68);
				{
				setState(670);
				match(CREATE);
				setState(673);
				_la = _input.LA(1);
				if (_la==DEFINER) {
					{
					setState(671);
					match(DEFINER);
					setState(672);
					definer();
					}
				}

				setState(675);
				match(EVENT);
				setState(679);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,52,_ctx) ) {
				case 1:
					{
					setState(676);
					match(IF);
					setState(677);
					match(NOT);
					setState(678);
					match(EXISTS);
					}
					break;
				}
				setState(681);
				((CreateEventContext)_localctx).name = identifier();
				setState(682);
				match(ON);
				setState(683);
				match(SCHEDULE);
				setState(684);
				match(AT);
				setState(685);
				((CreateEventContext)_localctx).cronExpression = match(STRING);
				setState(687);
				_la = _input.LA(1);
				if (_la==DISABLE || _la==ENABLE) {
					{
					setState(686);
					_la = _input.LA(1);
					if ( !(_la==DISABLE || _la==ENABLE) ) {
					_errHandler.recoverInline(this);
					} else {
						consume();
					}
					}
				}

				setState(691);
				_la = _input.LA(1);
				if (_la==COMMENT) {
					{
					setState(689);
					match(COMMENT);
					setState(690);
					((CreateEventContext)_localctx).comment = match(STRING);
					}
				}

				setState(693);
				match(DO);
				setState(694);
				match(CALL);
				setState(695);
				((CreateEventContext)_localctx).proc = identifier();
				}
				break;
			case 69:
				_localctx = new RenameEventContext(_localctx);
				enterOuterAlt(_localctx, 69);
				{
				setState(697);
				match(RENAME);
				setState(698);
				match(EVENT);
				setState(699);
				((RenameEventContext)_localctx).name = identifier();
				setState(700);
				match(TO);
				setState(701);
				((RenameEventContext)_localctx).newName = identifier();
				}
				break;
			case 70:
				_localctx = new SetDefinerContext(_localctx);
				enterOuterAlt(_localctx, 70);
				{
				setState(703);
				match(ALTER);
				setState(704);
				match(DEFINER);
				setState(705);
				definer();
				setState(706);
				match(EVENT);
				setState(707);
				((SetDefinerContext)_localctx).name = identifier();
				}
				break;
			case 71:
				_localctx = new SetEventNameContext(_localctx);
				enterOuterAlt(_localctx, 71);
				{
				setState(709);
				match(ALTER);
				setState(710);
				match(EVENT);
				setState(711);
				((SetEventNameContext)_localctx).name = identifier();
				setState(712);
				match(RENAME);
				setState(713);
				match(TO);
				setState(714);
				((SetEventNameContext)_localctx).newName = identifier();
				}
				break;
			case 72:
				_localctx = new SetEventScheduleContext(_localctx);
				enterOuterAlt(_localctx, 72);
				{
				setState(716);
				match(ALTER);
				setState(717);
				match(EVENT);
				setState(718);
				((SetEventScheduleContext)_localctx).name = identifier();
				setState(719);
				match(ON);
				setState(720);
				match(SCHEDULE);
				setState(721);
				match(AT);
				setState(722);
				((SetEventScheduleContext)_localctx).cronExpression = match(STRING);
				}
				break;
			case 73:
				_localctx = new SetEventEnableContext(_localctx);
				enterOuterAlt(_localctx, 73);
				{
				setState(724);
				match(ALTER);
				setState(725);
				match(EVENT);
				setState(726);
				((SetEventEnableContext)_localctx).name = identifier();
				setState(727);
				_la = _input.LA(1);
				if ( !(_la==DISABLE || _la==ENABLE) ) {
				_errHandler.recoverInline(this);
				} else {
					consume();
				}
				}
				break;
			case 74:
				_localctx = new DropEventContext(_localctx);
				enterOuterAlt(_localctx, 74);
				{
				setState(729);
				match(DROP);
				setState(730);
				match(EVENT);
				setState(733);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,55,_ctx) ) {
				case 1:
					{
					setState(731);
					match(IF);
					setState(732);
					match(EXISTS);
					}
					break;
				}
				setState(735);
				((DropEventContext)_localctx).name = identifier();
				}
				break;
			case 75:
				_localctx = new ShowSysInfoContext(_localctx);
				enterOuterAlt(_localctx, 75);
				{
				setState(736);
				match(SHOW);
				setState(737);
				match(SYSINFO);
				}
				break;
			case 76:
				_localctx = new ShowJobsContext(_localctx);
				enterOuterAlt(_localctx, 76);
				{
				setState(738);
				match(SHOW);
				setState(739);
				match(JOBS);
				}
				break;
			case 77:
				_localctx = new ShowRunningEventsContext(_localctx);
				enterOuterAlt(_localctx, 77);
				{
				setState(740);
				match(SHOW);
				setState(741);
				match(RUNNING);
				setState(742);
				match(EVENTS);
				}
				break;
			case 78:
				_localctx = new ShowEventsContext(_localctx);
				enterOuterAlt(_localctx, 78);
				{
				setState(743);
				match(SHOW);
				setState(744);
				match(EVENTS);
				setState(747);
				_la = _input.LA(1);
				if (_la==LIKE) {
					{
					setState(745);
					match(LIKE);
					setState(746);
					((ShowEventsContext)_localctx).pattern = match(STRING);
					}
				}

				}
				break;
			case 79:
				_localctx = new ShowDatabaseContext(_localctx);
				enterOuterAlt(_localctx, 79);
				{
				setState(749);
				match(SHOW);
				setState(750);
				match(DATABASES);
				setState(753);
				_la = _input.LA(1);
				if (_la==LIKE) {
					{
					setState(751);
					match(LIKE);
					setState(752);
					((ShowDatabaseContext)_localctx).pattern = match(STRING);
					}
				}

				}
				break;
			case 80:
				_localctx = new ShowTablesContext(_localctx);
				enterOuterAlt(_localctx, 80);
				{
				setState(755);
				match(SHOW);
				setState(756);
				match(TABLES);
				setState(759);
				_la = _input.LA(1);
				if (_la==FROM || _la==IN) {
					{
					setState(757);
					_la = _input.LA(1);
					if ( !(_la==FROM || _la==IN) ) {
					_errHandler.recoverInline(this);
					} else {
						consume();
					}
					setState(758);
					((ShowTablesContext)_localctx).db = identifier();
					}
				}

				setState(763);
				_la = _input.LA(1);
				if (_la==LIKE) {
					{
					setState(761);
					match(LIKE);
					setState(762);
					((ShowTablesContext)_localctx).pattern = match(STRING);
					}
				}

				}
				break;
			case 81:
				_localctx = new ShowFunctionsContext(_localctx);
				enterOuterAlt(_localctx, 81);
				{
				setState(765);
				match(SHOW);
				setState(767);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,60,_ctx) ) {
				case 1:
					{
					setState(766);
					((ShowFunctionsContext)_localctx).scope = identifier();
					}
					break;
				}
				setState(769);
				match(FUNCTIONS);
				setState(772);
				_la = _input.LA(1);
				if (_la==FROM || _la==IN) {
					{
					setState(770);
					_la = _input.LA(1);
					if ( !(_la==FROM || _la==IN) ) {
					_errHandler.recoverInline(this);
					} else {
						consume();
					}
					setState(771);
					((ShowFunctionsContext)_localctx).db = identifier();
					}
				}

				setState(776);
				_la = _input.LA(1);
				if (_la==LIKE) {
					{
					setState(774);
					match(LIKE);
					setState(775);
					((ShowFunctionsContext)_localctx).pattern = match(STRING);
					}
				}

				}
				break;
			case 82:
				_localctx = new ShowOrgsContext(_localctx);
				enterOuterAlt(_localctx, 82);
				{
				setState(778);
				match(SHOW);
				setState(779);
				_la = _input.LA(1);
				if ( !(_la==ORGS || _la==ORGANIZATIONS) ) {
				_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(782);
				_la = _input.LA(1);
				if (_la==LIKE) {
					{
					setState(780);
					match(LIKE);
					setState(781);
					((ShowOrgsContext)_localctx).pattern = match(STRING);
					}
				}

				}
				break;
			case 83:
				_localctx = new ShowSasContext(_localctx);
				enterOuterAlt(_localctx, 83);
				{
				setState(784);
				match(SHOW);
				setState(785);
				match(SAS);
				setState(788);
				_la = _input.LA(1);
				if (_la==LIKE) {
					{
					setState(786);
					match(LIKE);
					setState(787);
					((ShowSasContext)_localctx).pattern = match(STRING);
					}
				}

				}
				break;
			case 84:
				_localctx = new ShowUsersContext(_localctx);
				enterOuterAlt(_localctx, 84);
				{
				setState(790);
				match(SHOW);
				setState(791);
				match(USERS);
				setState(794);
				_la = _input.LA(1);
				if (_la==LIKE) {
					{
					setState(792);
					match(LIKE);
					setState(793);
					((ShowUsersContext)_localctx).pattern = match(STRING);
					}
				}

				}
				break;
			case 85:
				_localctx = new ShowGroupsContext(_localctx);
				enterOuterAlt(_localctx, 85);
				{
				setState(796);
				match(SHOW);
				setState(797);
				match(GROUPS);
				setState(800);
				_la = _input.LA(1);
				if (_la==LIKE) {
					{
					setState(798);
					match(LIKE);
					setState(799);
					((ShowGroupsContext)_localctx).pattern = match(STRING);
					}
				}

				}
				break;
			case 86:
				_localctx = new ShowUsersInGroupContext(_localctx);
				enterOuterAlt(_localctx, 86);
				{
				setState(802);
				match(SHOW);
				setState(803);
				match(USERS);
				setState(804);
				match(IN);
				setState(805);
				match(GROUP);
				setState(806);
				((ShowUsersInGroupContext)_localctx).name = identifier();
				setState(809);
				_la = _input.LA(1);
				if (_la==LIKE) {
					{
					setState(807);
					match(LIKE);
					setState(808);
					((ShowUsersInGroupContext)_localctx).pattern = match(STRING);
					}
				}

				}
				break;
			case 87:
				_localctx = new ShowProceduresContext(_localctx);
				enterOuterAlt(_localctx, 87);
				{
				setState(811);
				match(SHOW);
				setState(812);
				_la = _input.LA(1);
				if ( !(_la==PROCS || _la==PROCEDURES) ) {
				_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(815);
				_la = _input.LA(1);
				if (_la==LIKE) {
					{
					setState(813);
					match(LIKE);
					setState(814);
					((ShowProceduresContext)_localctx).pattern = match(STRING);
					}
				}

				}
				break;
			case 88:
				_localctx = new ShowVariableContext(_localctx);
				enterOuterAlt(_localctx, 88);
				{
				setState(817);
				match(SHOW);
				setState(818);
				match(VARIABLES);
				setState(821);
				_la = _input.LA(1);
				if (_la==LIKE) {
					{
					setState(819);
					match(LIKE);
					setState(820);
					((ShowVariableContext)_localctx).pattern = match(STRING);
					}
				}

				}
				break;
			case 89:
				_localctx = new ShowGrantsContext(_localctx);
				enterOuterAlt(_localctx, 89);
				{
				setState(823);
				match(SHOW);
				setState(824);
				match(GRANTS);
				setState(825);
				match(FOR);
				setState(826);
				((ShowGrantsContext)_localctx).user = identifier();
				}
				break;
			case 90:
				_localctx = new ShowCreateTableContext(_localctx);
				enterOuterAlt(_localctx, 90);
				{
				setState(827);
				match(SHOW);
				setState(828);
				match(CREATE);
				setState(829);
				match(TABLE);
				setState(830);
				((ShowCreateTableContext)_localctx).name = tableIdentifier();
				}
				break;
			case 91:
				_localctx = new ShowSchemaContext(_localctx);
				enterOuterAlt(_localctx, 91);
				{
				setState(831);
				match(SHOW);
				setState(832);
				match(SCHEMA);
				setState(833);
				match(FOR);
				setState(834);
				query();
				}
				break;
			case 92:
				_localctx = new DescEventContext(_localctx);
				enterOuterAlt(_localctx, 92);
				{
				setState(835);
				_la = _input.LA(1);
				if ( !(_la==DESC || _la==DESCRIBE) ) {
				_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(836);
				match(EVENT);
				setState(837);
				((DescEventContext)_localctx).name = identifier();
				}
				break;
			case 93:
				_localctx = new DescProcedureContext(_localctx);
				enterOuterAlt(_localctx, 93);
				{
				setState(838);
				_la = _input.LA(1);
				if ( !(_la==DESC || _la==DESCRIBE) ) {
				_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(839);
				_la = _input.LA(1);
				if ( !(_la==PROC || _la==PROCEDURE) ) {
				_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(840);
				((DescProcedureContext)_localctx).name = identifier();
				}
				break;
			case 94:
				_localctx = new DescDatabaseContext(_localctx);
				enterOuterAlt(_localctx, 94);
				{
				setState(841);
				_la = _input.LA(1);
				if ( !(_la==DESC || _la==DESCRIBE) ) {
				_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(842);
				match(DATABASE);
				setState(843);
				((DescDatabaseContext)_localctx).name = identifier();
				}
				break;
			case 95:
				_localctx = new DescTableContext(_localctx);
				enterOuterAlt(_localctx, 95);
				{
				setState(844);
				_la = _input.LA(1);
				if ( !(_la==DESC || _la==DESCRIBE) ) {
				_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(846);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,70,_ctx) ) {
				case 1:
					{
					setState(845);
					match(TABLE);
					}
					break;
				}
				setState(849);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,71,_ctx) ) {
				case 1:
					{
					setState(848);
					match(EXTENDED);
					}
					break;
				}
				setState(851);
				tableIdentifier();
				}
				break;
			case 96:
				_localctx = new DescFunctionContext(_localctx);
				enterOuterAlt(_localctx, 96);
				{
				setState(852);
				_la = _input.LA(1);
				if ( !(_la==DESC || _la==DESCRIBE) ) {
				_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(853);
				match(FUNCTION);
				setState(855);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,72,_ctx) ) {
				case 1:
					{
					setState(854);
					match(EXTENDED);
					}
					break;
				}
				setState(857);
				funcIdentifier();
				}
				break;
			case 97:
				_localctx = new DescUserContext(_localctx);
				enterOuterAlt(_localctx, 97);
				{
				setState(858);
				_la = _input.LA(1);
				if ( !(_la==DESC || _la==DESCRIBE) ) {
				_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(859);
				match(USER);
				setState(860);
				((DescUserContext)_localctx).name = identifier();
				}
				break;
			case 98:
				_localctx = new DescOrgContext(_localctx);
				enterOuterAlt(_localctx, 98);
				{
				setState(861);
				_la = _input.LA(1);
				if ( !(_la==DESC || _la==DESCRIBE) ) {
				_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(862);
				match(ORG);
				setState(863);
				((DescOrgContext)_localctx).name = identifier();
				}
				break;
			case 99:
				_localctx = new ExplainContext(_localctx);
				enterOuterAlt(_localctx, 99);
				{
				setState(864);
				match(EXPLAIN);
				setState(866);
				_la = _input.LA(1);
				if (_la==EXTENDED) {
					{
					setState(865);
					match(EXTENDED);
					}
				}

				setState(869);
				_la = _input.LA(1);
				if (_la==PLAN) {
					{
					setState(868);
					match(PLAN);
					}
				}

				setState(871);
				query();
				}
				break;
			case 100:
				_localctx = new CreateTemporaryViewContext(_localctx);
				enterOuterAlt(_localctx, 100);
				{
				setState(872);
				match(CREATE);
				setState(875);
				_la = _input.LA(1);
				if (_la==OR) {
					{
					setState(873);
					match(OR);
					setState(874);
					match(REPLACE);
					}
				}

				setState(878);
				_la = _input.LA(1);
				if (_la==CACHE) {
					{
					setState(877);
					match(CACHE);
					}
				}

				setState(880);
				_la = _input.LA(1);
				if ( !(_la==TEMP || _la==TEMPORARY) ) {
				_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(881);
				match(VIEW);
				setState(882);
				((CreateTemporaryViewContext)_localctx).name = identifier();
				setState(884);
				_la = _input.LA(1);
				if (_la==AS) {
					{
					setState(883);
					match(AS);
					}
				}

				setState(886);
				query();
				}
				break;
			case 101:
				_localctx = new StatementContext(_localctx);
				enterOuterAlt(_localctx, 101);
				{
				setState(888);
				((StatementContext)_localctx).statement = _input.LT(1);
				_la = _input.LA(1);
				if ( !(_la==ANALYZE || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (INSERT - 64)) | (1L << (REFRESH - 64)) | (1L << (SELECT - 64)) | (1L << (SET - 64)) | (1L << (SHOW - 64)) | (1L << (WITH - 64)))) != 0)) ) {
					((StatementContext)_localctx).statement = (Token)_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(892);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,78,_ctx);
				while ( _alt!=1 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1+1 ) {
						{
						{
						setState(889);
						matchWildcard();
						}
						} 
					}
					setState(894);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,78,_ctx);
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
			setState(897);
			match(T__0);
			setState(898);
			mql();
			setState(903);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__3) {
				{
				{
				setState(899);
				match(T__3);
				setState(900);
				mql();
				}
				}
				setState(905);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(906);
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
			setState(909);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,81,_ctx) ) {
			case 1:
				{
				setState(908);
				match(EQ);
				}
				break;
			}
			setState(913);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,82,_ctx) ) {
			case 1:
				{
				setState(911);
				((DefinerContext)_localctx).user = identifier();
				}
				break;
			case 2:
				{
				setState(912);
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
			setState(930);
			switch (_input.LA(1)) {
			case SELECT:
				enterOuterAlt(_localctx, 1);
				{
				setState(915);
				match(SELECT);
				setState(919);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,83,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(916);
						_la = _input.LA(1);
						if ( _la <= 0 || (_la==T__3) ) {
						_errHandler.recoverInline(this);
						} else {
							consume();
						}
						}
						} 
					}
					setState(921);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,83,_ctx);
				}
				}
				break;
			case WITH:
				enterOuterAlt(_localctx, 2);
				{
				setState(922);
				ctes();
				setState(923);
				match(SELECT);
				setState(927);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,84,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(924);
						_la = _input.LA(1);
						if ( _la <= 0 || (_la==T__3) ) {
						_errHandler.recoverInline(this);
						} else {
							consume();
						}
						}
						} 
					}
					setState(929);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,84,_ctx);
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
			setState(932);
			match(WITH);
			setState(933);
			namedQuery();
			setState(938);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__2) {
				{
				{
				setState(934);
				match(T__2);
				setState(935);
				namedQuery();
				}
				}
				setState(940);
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
			setState(941);
			match(PARTITION);
			setState(942);
			match(T__0);
			setState(943);
			identifier();
			setState(948);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__2) {
				{
				{
				setState(944);
				match(T__2);
				setState(945);
				identifier();
				}
				}
				setState(950);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(951);
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
			setState(953);
			match(COALESCE);
			setState(954);
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
			setState(990);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,92,_ctx) ) {
			case 1:
				_localctx = new ComplexDataTypeContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(956);
				((ComplexDataTypeContext)_localctx).complex = match(ARRAY);
				setState(957);
				match(T__4);
				setState(958);
				dataType();
				setState(959);
				match(T__5);
				}
				break;
			case 2:
				_localctx = new ComplexDataTypeContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(961);
				((ComplexDataTypeContext)_localctx).complex = match(MAP);
				setState(962);
				match(T__4);
				setState(963);
				dataType();
				setState(964);
				match(T__2);
				setState(965);
				dataType();
				setState(966);
				match(T__5);
				}
				break;
			case 3:
				_localctx = new ComplexDataTypeContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(968);
				((ComplexDataTypeContext)_localctx).complex = match(STRUCT);
				setState(975);
				switch (_input.LA(1)) {
				case T__4:
					{
					setState(969);
					match(T__4);
					setState(971);
					_la = _input.LA(1);
					if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ACCOUNT) | (1L << ADD) | (1L << ALL) | (1L << ALTER) | (1L << ARRAY) | (1L << AT) | (1L << MAP) | (1L << STRUCT) | (1L << AS) | (1L << BY) | (1L << CACHE) | (1L << CALL) | (1L << CASCADE) | (1L << COLUMN) | (1L << COLUMNS) | (1L << COMMENT) | (1L << CHANGE) | (1L << CREATE) | (1L << CURRENT_USER) | (1L << DATABASE) | (1L << DATABASES) | (1L << DATASOURCE) | (1L << DATASOURCES) | (1L << DDL) | (1L << DEFINER) | (1L << DELETE) | (1L << DESC) | (1L << DESCRIBE) | (1L << DISABLE) | (1L << DO) | (1L << DCL) | (1L << DROP) | (1L << ENABLE) | (1L << EQ) | (1L << NEQ) | (1L << EVENT) | (1L << EVENTS) | (1L << EXISTS) | (1L << EXPLAIN) | (1L << EXTENDED) | (1L << FOR) | (1L << FROM) | (1L << FUNCTION) | (1L << FUNCTIONS) | (1L << GLOBAL) | (1L << GRANT) | (1L << GRANTS) | (1L << GROUP) | (1L << GROUPS) | (1L << HQL) | (1L << IDENTIFIED) | (1L << IF) | (1L << IN))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (INSERT - 64)) | (1L << (INTO - 64)) | (1L << (LIKE - 64)) | (1L << (JOBS - 64)) | (1L << (MOUNT - 64)) | (1L << (MQL - 64)) | (1L << (NOT - 64)) | (1L << (ON - 64)) | (1L << (OPTION - 64)) | (1L << (OPTIONS - 64)) | (1L << (OR - 64)) | (1L << (ORG - 64)) | (1L << (ORGANIZATION - 64)) | (1L << (OVERWRITE - 64)) | (1L << (PLAN - 64)) | (1L << (PARTITION - 64)) | (1L << (PROC - 64)) | (1L << (PROCS - 64)) | (1L << (PROCEDURE - 64)) | (1L << (PROCEDURES - 64)) | (1L << (REMOVE - 64)) | (1L << (RENAME - 64)) | (1L << (REFRESH - 64)) | (1L << (REPLACE - 64)) | (1L << (REVOKE - 64)) | (1L << (RUNNING - 64)) | (1L << (SA - 64)) | (1L << (SCHEMA - 64)) | (1L << (SCHEDULE - 64)) | (1L << (SELECT - 64)) | (1L << (SESSION - 64)) | (1L << (SET - 64)) | (1L << (SHOW - 64)) | (1L << (STAR - 64)) | (1L << (STREAM - 64)) | (1L << (SYSINFO - 64)) | (1L << (TABLE - 64)) | (1L << (TABLES - 64)) | (1L << (TEMP - 64)) | (1L << (TEMPORARY - 64)) | (1L << (TO - 64)) | (1L << (TYPE - 64)) | (1L << (TRUNCATE - 64)) | (1L << (UNMOUNT - 64)) | (1L << (UPDATE - 64)) | (1L << (USE - 64)) | (1L << (USING - 64)) | (1L << (USER - 64)) | (1L << (USERS - 64)) | (1L << (VARIABLES - 64)) | (1L << (VIEW - 64)) | (1L << (VIEWS - 64)) | (1L << (WITH - 64)) | (1L << (IDENTIFIER - 64)) | (1L << (BACKQUOTED_IDENTIFIER - 64)))) != 0)) {
						{
						setState(970);
						complexColTypeList();
						}
					}

					setState(973);
					match(T__5);
					}
					break;
				case NEQ:
					{
					setState(974);
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
				setState(977);
				identifier();
				setState(988);
				_la = _input.LA(1);
				if (_la==T__0) {
					{
					setState(978);
					match(T__0);
					setState(979);
					match(INTEGER_VALUE);
					setState(984);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==T__2) {
						{
						{
						setState(980);
						match(T__2);
						setState(981);
						match(INTEGER_VALUE);
						}
						}
						setState(986);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					setState(987);
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
			setState(992);
			colType();
			setState(997);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__2) {
				{
				{
				setState(993);
				match(T__2);
				setState(994);
				colType();
				}
				}
				setState(999);
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
			setState(1000);
			identifier();
			setState(1001);
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
			setState(1003);
			complexColType();
			setState(1008);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__2) {
				{
				{
				setState(1004);
				match(T__2);
				setState(1005);
				complexColType();
				}
				}
				setState(1010);
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
			setState(1011);
			identifier();
			setState(1012);
			match(T__6);
			setState(1013);
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
			setState(1015);
			((NamedQueryContext)_localctx).name = identifier();
			setState(1017);
			_la = _input.LA(1);
			if (_la==AS) {
				{
				setState(1016);
				match(AS);
				}
			}

			setState(1019);
			match(T__0);
			setState(1020);
			query();
			setState(1021);
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
			setState(1023);
			grantPrivilege();
			setState(1028);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__2) {
				{
				{
				setState(1024);
				match(T__2);
				setState(1025);
				grantPrivilege();
				}
				}
				setState(1030);
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
			setState(1031);
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
			setState(1033);
			privilege();
			setState(1038);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__2) {
				{
				{
				setState(1034);
				match(T__2);
				setState(1035);
				privilege();
				}
				}
				setState(1040);
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
			setState(1053);
			switch (_input.LA(1)) {
			case SELECT:
				enterOuterAlt(_localctx, 1);
				{
				setState(1041);
				match(SELECT);
				setState(1043);
				_la = _input.LA(1);
				if (_la==T__0) {
					{
					setState(1042);
					columnIdentifiers();
					}
				}

				}
				break;
			case UPDATE:
				enterOuterAlt(_localctx, 2);
				{
				setState(1045);
				match(UPDATE);
				setState(1047);
				_la = _input.LA(1);
				if (_la==T__0) {
					{
					setState(1046);
					columnIdentifiers();
					}
				}

				}
				break;
			case INSERT:
				enterOuterAlt(_localctx, 3);
				{
				setState(1049);
				match(INSERT);
				}
				break;
			case DELETE:
				enterOuterAlt(_localctx, 4);
				{
				setState(1050);
				match(DELETE);
				}
				break;
			case TRUNCATE:
				enterOuterAlt(_localctx, 5);
				{
				setState(1051);
				match(TRUNCATE);
				}
				break;
			case ALL:
				enterOuterAlt(_localctx, 6);
				{
				setState(1052);
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
			setState(1055);
			match(T__0);
			setState(1056);
			identifier();
			setState(1061);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__2) {
				{
				{
				setState(1057);
				match(T__2);
				setState(1058);
				identifier();
				}
				}
				setState(1063);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(1064);
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
			setState(1069);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,102,_ctx) ) {
			case 1:
				{
				setState(1066);
				((TableCollectionsContext)_localctx).db = identifierOrStar();
				setState(1067);
				match(T__7);
				}
				break;
			}
			setState(1071);
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
			setState(1075);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,103,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1073);
				identifier();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1074);
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
			setState(1077);
			match(ADD);
			setState(1078);
			match(USER);
			setState(1079);
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
			setState(1081);
			match(REMOVE);
			setState(1082);
			match(USER);
			setState(1083);
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
			setState(1085);
			identifier();
			setState(1090);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__2) {
				{
				{
				setState(1086);
				match(T__2);
				setState(1087);
				identifier();
				}
				}
				setState(1092);
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
			setState(1096);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,105,_ctx) ) {
			case 1:
				{
				setState(1093);
				((FuncIdentifierContext)_localctx).db = identifier();
				setState(1094);
				match(T__7);
				}
				break;
			}
			setState(1098);
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
			setState(1103);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,106,_ctx) ) {
			case 1:
				{
				setState(1100);
				((TableIdentifierContext)_localctx).db = identifier();
				setState(1101);
				match(T__7);
				}
				break;
			}
			setState(1105);
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
			setState(1107);
			match(T__0);
			setState(1108);
			property();
			setState(1113);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__2) {
				{
				{
				setState(1109);
				match(T__2);
				setState(1110);
				property();
				}
				}
				setState(1115);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(1116);
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
			setState(1118);
			((PropertyContext)_localctx).key = propertyKey();
			setState(1120);
			_la = _input.LA(1);
			if (_la==EQ) {
				{
				setState(1119);
				match(EQ);
				}
			}

			setState(1122);
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

	public static class PropertyKeyListContext extends ParserRuleContext {
		public List<PropertyKeyContext> propertyKey() {
			return getRuleContexts(PropertyKeyContext.class);
		}
		public PropertyKeyContext propertyKey(int i) {
			return getRuleContext(PropertyKeyContext.class,i);
		}
		public PropertyKeyListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_propertyKeyList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).enterPropertyKeyList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MqlBaseListener ) ((MqlBaseListener)listener).exitPropertyKeyList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MqlBaseVisitor ) return ((MqlBaseVisitor<? extends T>)visitor).visitPropertyKeyList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PropertyKeyListContext propertyKeyList() throws RecognitionException {
		PropertyKeyListContext _localctx = new PropertyKeyListContext(_ctx, getState());
		enterRule(_localctx, 56, RULE_propertyKeyList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1124);
			match(T__0);
			setState(1125);
			propertyKey();
			setState(1130);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__2) {
				{
				{
				setState(1126);
				match(T__2);
				setState(1127);
				propertyKey();
				}
				}
				setState(1132);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(1133);
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
		enterRule(_localctx, 58, RULE_propertyKey);
		int _la;
		try {
			setState(1144);
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
				setState(1135);
				identifier();
				setState(1140);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__7) {
					{
					{
					setState(1136);
					match(T__7);
					setState(1137);
					identifier();
					}
					}
					setState(1142);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
				break;
			case STRING:
				enterOuterAlt(_localctx, 2);
				{
				setState(1143);
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
		enterRule(_localctx, 60, RULE_password);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1149);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,112,_ctx);
			while ( _alt!=1 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1+1 ) {
					{
					{
					setState(1146);
					matchWildcard();
					}
					} 
				}
				setState(1151);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,112,_ctx);
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
		enterRule(_localctx, 62, RULE_identifier);
		try {
			setState(1155);
			switch (_input.LA(1)) {
			case IDENTIFIER:
				enterOuterAlt(_localctx, 1);
				{
				setState(1152);
				match(IDENTIFIER);
				}
				break;
			case BACKQUOTED_IDENTIFIER:
				enterOuterAlt(_localctx, 2);
				{
				setState(1153);
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
		enterRule(_localctx, 64, RULE_resource);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1157);
			identifier();
			setState(1158);
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
		enterRule(_localctx, 66, RULE_nonReserved);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1160);
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
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3\u0083\u048d\4\2\t"+
		"\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\3\2\3\2\3\2\3\3\3\3\3\3\3\3\3\3\5\3O\n\3\3\3\3\3\3\3"+
		"\5\3T\n\3\3\3\3\3\5\3X\n\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3\u0080\n\3\3\3\3\3\5\3\u0084"+
		"\n\3\3\3\3\3\3\3\3\3\3\3\5\3\u008b\n\3\3\3\3\3\3\3\5\3\u0090\n\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\5\3\u0098\n\3\3\3\3\3\3\3\3\3\3\3\5\3\u009f\n\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3\u00aa\n\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\5\3\u00b6\n\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\5\3\u00c2\n\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3\u00ce\n\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3\u00d9\n\3\3\3\3\3\3\3\5\3\u00de"+
		"\n\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3\u00e8\n\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\5\3\u00f2\n\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3\u010a\n\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\5\3\u0112\n\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3\u0128\n\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\5\3\u0132\n\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3\u014b\n\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\5\3\u0153\n\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3\u017b\n\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\5\3\u0183\n\3\3\3\3\3\3\3\5\3\u0188\n\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3\u01ac\n\3\3\3\3\3\5"+
		"\3\u01b0\n\3\3\3\3\3\5\3\u01b4\n\3\3\3\3\3\3\3\3\3\5\3\u01ba\n\3\3\3\3"+
		"\3\3\3\3\3\3\3\5\3\u01c1\n\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\5\3\u01e5\n\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3\u01ed"+
		"\n\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3\u01f7\n\3\3\3\3\3\5\3\u01fb\n"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\5\3\u0213\n\3\3\3\3\3\3\3\5\3\u0218\n\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\5\3\u0232\n\3\3\3\3\3\5\3\u0236\n\3\3\3\3\3\3\3\3\3\5"+
		"\3\u023c\n\3\3\3\3\3\3\3\3\3\5\3\u0242\n\3\3\3\3\3\3\3\3\3\5\3\u0248\n"+
		"\3\3\3\3\3\3\3\3\3\7\3\u024e\n\3\f\3\16\3\u0251\13\3\5\3\u0253\n\3\3\3"+
		"\3\3\5\3\u0257\n\3\3\3\3\3\3\3\5\3\u025c\n\3\3\3\3\3\3\3\3\3\5\3\u0262"+
		"\n\3\3\3\3\3\3\3\3\3\5\3\u0268\n\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\5\3\u0277\n\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3\u027f\n\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3\u029e\n\3\3\3\3\3\3\3"+
		"\3\3\5\3\u02a4\n\3\3\3\3\3\3\3\3\3\5\3\u02aa\n\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\5\3\u02b2\n\3\3\3\3\3\5\3\u02b6\n\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3\u02e0\n"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3\u02ee\n\3\3\3\3"+
		"\3\3\3\3\3\5\3\u02f4\n\3\3\3\3\3\3\3\3\3\5\3\u02fa\n\3\3\3\3\3\5\3\u02fe"+
		"\n\3\3\3\3\3\5\3\u0302\n\3\3\3\3\3\3\3\5\3\u0307\n\3\3\3\3\3\5\3\u030b"+
		"\n\3\3\3\3\3\3\3\3\3\5\3\u0311\n\3\3\3\3\3\3\3\3\3\5\3\u0317\n\3\3\3\3"+
		"\3\3\3\3\3\5\3\u031d\n\3\3\3\3\3\3\3\3\3\5\3\u0323\n\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\5\3\u032c\n\3\3\3\3\3\3\3\3\3\5\3\u0332\n\3\3\3\3\3\3\3\3"+
		"\3\5\3\u0338\n\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3\u0351\n\3\3\3\5\3\u0354\n\3"+
		"\3\3\3\3\3\3\3\3\5\3\u035a\n\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3"+
		"\u0365\n\3\3\3\5\3\u0368\n\3\3\3\3\3\3\3\3\3\5\3\u036e\n\3\3\3\5\3\u0371"+
		"\n\3\3\3\3\3\3\3\3\3\5\3\u0377\n\3\3\3\3\3\3\3\3\3\7\3\u037d\n\3\f\3\16"+
		"\3\u0380\13\3\5\3\u0382\n\3\3\4\3\4\3\4\3\4\7\4\u0388\n\4\f\4\16\4\u038b"+
		"\13\4\3\4\3\4\3\5\5\5\u0390\n\5\3\5\3\5\5\5\u0394\n\5\3\6\3\6\7\6\u0398"+
		"\n\6\f\6\16\6\u039b\13\6\3\6\3\6\3\6\7\6\u03a0\n\6\f\6\16\6\u03a3\13\6"+
		"\5\6\u03a5\n\6\3\7\3\7\3\7\3\7\7\7\u03ab\n\7\f\7\16\7\u03ae\13\7\3\b\3"+
		"\b\3\b\3\b\3\b\7\b\u03b5\n\b\f\b\16\b\u03b8\13\b\3\b\3\b\3\t\3\t\3\t\3"+
		"\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\5\n\u03ce\n"+
		"\n\3\n\3\n\5\n\u03d2\n\n\3\n\3\n\3\n\3\n\3\n\7\n\u03d9\n\n\f\n\16\n\u03dc"+
		"\13\n\3\n\5\n\u03df\n\n\5\n\u03e1\n\n\3\13\3\13\3\13\7\13\u03e6\n\13\f"+
		"\13\16\13\u03e9\13\13\3\f\3\f\3\f\3\r\3\r\3\r\7\r\u03f1\n\r\f\r\16\r\u03f4"+
		"\13\r\3\16\3\16\3\16\3\16\3\17\3\17\5\17\u03fc\n\17\3\17\3\17\3\17\3\17"+
		"\3\20\3\20\3\20\7\20\u0405\n\20\f\20\16\20\u0408\13\20\3\21\3\21\3\22"+
		"\3\22\3\22\7\22\u040f\n\22\f\22\16\22\u0412\13\22\3\23\3\23\5\23\u0416"+
		"\n\23\3\23\3\23\5\23\u041a\n\23\3\23\3\23\3\23\3\23\5\23\u0420\n\23\3"+
		"\24\3\24\3\24\3\24\7\24\u0426\n\24\f\24\16\24\u0429\13\24\3\24\3\24\3"+
		"\25\3\25\3\25\5\25\u0430\n\25\3\25\3\25\3\26\3\26\5\26\u0436\n\26\3\27"+
		"\3\27\3\27\3\27\3\30\3\30\3\30\3\30\3\31\3\31\3\31\7\31\u0443\n\31\f\31"+
		"\16\31\u0446\13\31\3\32\3\32\3\32\5\32\u044b\n\32\3\32\3\32\3\33\3\33"+
		"\3\33\5\33\u0452\n\33\3\33\3\33\3\34\3\34\3\34\3\34\7\34\u045a\n\34\f"+
		"\34\16\34\u045d\13\34\3\34\3\34\3\35\3\35\5\35\u0463\n\35\3\35\3\35\3"+
		"\36\3\36\3\36\3\36\7\36\u046b\n\36\f\36\16\36\u046e\13\36\3\36\3\36\3"+
		"\37\3\37\3\37\7\37\u0475\n\37\f\37\16\37\u0478\13\37\3\37\5\37\u047b\n"+
		"\37\3 \7 \u047e\n \f \16 \u0481\13 \3!\3!\3!\5!\u0486\n!\3\"\3\"\3\"\3"+
		"#\3#\3#\4\u037e\u047f\2$\2\4\6\b\n\f\16\20\22\24\26\30\32\34\36 \"$&("+
		"*,.\60\62\64\668:<>@BD\2\17\4\2NNPP\3\2lm\4\2UUWW\4\2>>HH\4\2))--\4\2"+
		"\66\66AA\4\2OOQQ\4\2VVXX\3\2\'(\b\2\17\17BB[[ccefzz\3\2\6\6\5\2\13\13"+
		"$$++\t\2\13\16\20\35\37DFNPPR_az\u0546\2F\3\2\2\2\4\u0381\3\2\2\2\6\u0383"+
		"\3\2\2\2\b\u038f\3\2\2\2\n\u03a4\3\2\2\2\f\u03a6\3\2\2\2\16\u03af\3\2"+
		"\2\2\20\u03bb\3\2\2\2\22\u03e0\3\2\2\2\24\u03e2\3\2\2\2\26\u03ea\3\2\2"+
		"\2\30\u03ed\3\2\2\2\32\u03f5\3\2\2\2\34\u03f9\3\2\2\2\36\u0401\3\2\2\2"+
		" \u0409\3\2\2\2\"\u040b\3\2\2\2$\u041f\3\2\2\2&\u0421\3\2\2\2(\u042f\3"+
		"\2\2\2*\u0435\3\2\2\2,\u0437\3\2\2\2.\u043b\3\2\2\2\60\u043f\3\2\2\2\62"+
		"\u044a\3\2\2\2\64\u0451\3\2\2\2\66\u0455\3\2\2\28\u0460\3\2\2\2:\u0466"+
		"\3\2\2\2<\u047a\3\2\2\2>\u047f\3\2\2\2@\u0485\3\2\2\2B\u0487\3\2\2\2D"+
		"\u048a\3\2\2\2FG\5\4\3\2GH\7\2\2\3H\3\3\2\2\2IJ\7\35\2\2JN\t\2\2\2KL\7"+
		"@\2\2LM\7I\2\2MO\7\62\2\2NK\3\2\2\2NO\3\2\2\2OP\3\2\2\2PS\5@!\2QR\7\33"+
		"\2\2RT\7{\2\2SQ\3\2\2\2ST\3\2\2\2TW\3\2\2\2UV\7L\2\2VX\5\66\34\2WU\3\2"+
		"\2\2WX\3\2\2\2X\u0382\3\2\2\2YZ\7Z\2\2Z[\t\2\2\2[\\\5@!\2\\]\7n\2\2]^"+
		"\5@!\2^\u0382\3\2\2\2_`\7\16\2\2`a\t\2\2\2ab\5@!\2bc\7Z\2\2cd\7n\2\2d"+
		"e\5@!\2e\u0382\3\2\2\2fg\7\16\2\2gh\t\2\2\2hi\5@!\2ij\7e\2\2jk\7L\2\2"+
		"kl\5\66\34\2l\u0382\3\2\2\2mn\7\16\2\2no\t\2\2\2op\5@!\2pq\7Y\2\2qr\7"+
		"L\2\2rs\5:\36\2s\u0382\3\2\2\2tu\7\16\2\2uv\t\2\2\2vw\5@!\2wx\7e\2\2x"+
		"y\7\33\2\2yz\7{\2\2z\u0382\3\2\2\2{|\7,\2\2|\177\t\2\2\2}~\7@\2\2~\u0080"+
		"\7\62\2\2\177}\3\2\2\2\177\u0080\3\2\2\2\u0080\u0081\3\2\2\2\u0081\u0083"+
		"\5@!\2\u0082\u0084\7\30\2\2\u0083\u0082\3\2\2\2\u0083\u0084\3\2\2\2\u0084"+
		"\u0382\3\2\2\2\u0085\u0086\7\35\2\2\u0086\u008a\7_\2\2\u0087\u0088\7@"+
		"\2\2\u0088\u0089\7I\2\2\u0089\u008b\7\62\2\2\u008a\u0087\3\2\2\2\u008a"+
		"\u008b\3\2\2\2\u008b\u008c\3\2\2\2\u008c\u008d\5@!\2\u008d\u008f\7A\2"+
		"\2\u008e\u0090\t\2\2\2\u008f\u008e\3\2\2\2\u008f\u0090\3\2\2\2\u0090\u0091"+
		"\3\2\2\2\u0091\u0092\5@!\2\u0092\u0093\7?\2\2\u0093\u0094\7\25\2\2\u0094"+
		"\u0097\5> \2\u0095\u0096\7L\2\2\u0096\u0098\5\66\34\2\u0097\u0095\3\2"+
		"\2\2\u0097\u0098\3\2\2\2\u0098\u0382\3\2\2\2\u0099\u009a\7Z\2\2\u009a"+
		"\u009b\7_\2\2\u009b\u009c\5@!\2\u009c\u009e\7A\2\2\u009d\u009f\t\2\2\2"+
		"\u009e\u009d\3\2\2\2\u009e\u009f\3\2\2\2\u009f\u00a0\3\2\2\2\u00a0\u00a1"+
		"\5@!\2\u00a1\u00a2\7n\2\2\u00a2\u00a3\5@!\2\u00a3\u0382\3\2\2\2\u00a4"+
		"\u00a5\7\16\2\2\u00a5\u00a6\7_\2\2\u00a6\u00a7\5@!\2\u00a7\u00a9\7A\2"+
		"\2\u00a8\u00aa\t\2\2\2\u00a9\u00a8\3\2\2\2\u00a9\u00aa\3\2\2\2\u00aa\u00ab"+
		"\3\2\2\2\u00ab\u00ac\5@!\2\u00ac\u00ad\7Z\2\2\u00ad\u00ae\7n\2\2\u00ae"+
		"\u00af\5@!\2\u00af\u0382\3\2\2\2\u00b0\u00b1\7\16\2\2\u00b1\u00b2\7_\2"+
		"\2\u00b2\u00b3\5@!\2\u00b3\u00b5\7A\2\2\u00b4\u00b6\t\2\2\2\u00b5\u00b4"+
		"\3\2\2\2\u00b5\u00b6\3\2\2\2\u00b6\u00b7\3\2\2\2\u00b7\u00b8\5@!\2\u00b8"+
		"\u00b9\7?\2\2\u00b9\u00ba\7\25\2\2\u00ba\u00bb\5> \2\u00bb\u0382\3\2\2"+
		"\2\u00bc\u00bd\7\16\2\2\u00bd\u00be\7_\2\2\u00be\u00bf\5@!\2\u00bf\u00c1"+
		"\7A\2\2\u00c0\u00c2\t\2\2\2\u00c1\u00c0\3\2\2\2\u00c1\u00c2\3\2\2\2\u00c2"+
		"\u00c3\3\2\2\2\u00c3\u00c4\5@!\2\u00c4\u00c5\7e\2\2\u00c5\u00c6\7L\2\2"+
		"\u00c6\u00c7\5\66\34\2\u00c7\u0382\3\2\2\2\u00c8\u00c9\7\16\2\2\u00c9"+
		"\u00ca\7_\2\2\u00ca\u00cb\5@!\2\u00cb\u00cd\7A\2\2\u00cc\u00ce\t\2\2\2"+
		"\u00cd\u00cc\3\2\2\2\u00cd\u00ce\3\2\2\2\u00ce\u00cf\3\2\2\2\u00cf\u00d0"+
		"\5@!\2\u00d0\u00d1\7Y\2\2\u00d1\u00d2\7L\2\2\u00d2\u00d3\5:\36\2\u00d3"+
		"\u0382\3\2\2\2\u00d4\u00d5\7,\2\2\u00d5\u00d8\7_\2\2\u00d6\u00d7\7@\2"+
		"\2\u00d7\u00d9\7\62\2\2\u00d8\u00d6\3\2\2\2\u00d8\u00d9\3\2\2\2\u00d9"+
		"\u00da\3\2\2\2\u00da\u00db\5@!\2\u00db\u00dd\7A\2\2\u00dc\u00de\t\2\2"+
		"\2\u00dd\u00dc\3\2\2\2\u00dd\u00de\3\2\2\2\u00de\u00df\3\2\2\2\u00df\u00e0"+
		"\5@!\2\u00e0\u0382\3\2\2\2\u00e1\u00e2\7:\2\2\u00e2\u00e3\7:\2\2\u00e3"+
		"\u00e4\7K\2\2\u00e4\u00e5\5\36\20\2\u00e5\u00e7\7n\2\2\u00e6\u00e8\7u"+
		"\2\2\u00e7\u00e6\3\2\2\2\u00e7\u00e8\3\2\2\2\u00e8\u00e9\3\2\2\2\u00e9"+
		"\u00ea\5@!\2\u00ea\u0382\3\2\2\2\u00eb\u00ec\7]\2\2\u00ec\u00ed\7:\2\2"+
		"\u00ed\u00ee\7K\2\2\u00ee\u00ef\5\36\20\2\u00ef\u00f1\7\66\2\2\u00f0\u00f2"+
		"\7u\2\2\u00f1\u00f0\3\2\2\2\u00f1\u00f2\3\2\2\2\u00f2\u00f3\3\2\2\2\u00f3"+
		"\u00f4\5@!\2\u00f4\u0382\3\2\2\2\u00f5\u00f6\7:\2\2\u00f6\u00f7\7:\2\2"+
		"\u00f7\u00f8\7K\2\2\u00f8\u00f9\5\36\20\2\u00f9\u00fa\7n\2\2\u00fa\u00fb"+
		"\7<\2\2\u00fb\u00fc\5@!\2\u00fc\u0382\3\2\2\2\u00fd\u00fe\7]\2\2\u00fe"+
		"\u00ff\7:\2\2\u00ff\u0100\7K\2\2\u0100\u0101\5\36\20\2\u0101\u0102\7\66"+
		"\2\2\u0102\u0103\7<\2\2\u0103\u0104\5@!\2\u0104\u0382\3\2\2\2\u0105\u0106"+
		"\7:\2\2\u0106\u0107\5\36\20\2\u0107\u0109\7n\2\2\u0108\u010a\7u\2\2\u0109"+
		"\u0108\3\2\2\2\u0109\u010a\3\2\2\2\u010a\u010b\3\2\2\2\u010b\u010c\5@"+
		"!\2\u010c\u0382\3\2\2\2\u010d\u010e\7]\2\2\u010e\u010f\5\36\20\2\u010f"+
		"\u0111\7\66\2\2\u0110\u0112\7u\2\2\u0111\u0110\3\2\2\2\u0111\u0112\3\2"+
		"\2\2\u0112\u0113\3\2\2\2\u0113\u0114\5@!\2\u0114\u0382\3\2\2\2\u0115\u0116"+
		"\7:\2\2\u0116\u0117\5\36\20\2\u0117\u0118\7n\2\2\u0118\u0119\7<\2\2\u0119"+
		"\u011a\5@!\2\u011a\u0382\3\2\2\2\u011b\u011c\7]\2\2\u011c\u011d\5\36\20"+
		"\2\u011d\u011e\7\66\2\2\u011e\u011f\7<\2\2\u011f\u0120\5@!\2\u0120\u0382"+
		"\3\2\2\2\u0121\u0122\7:\2\2\u0122\u0123\5\"\22\2\u0123\u0124\7J\2\2\u0124"+
		"\u0125\5(\25\2\u0125\u0127\7n\2\2\u0126\u0128\7u\2\2\u0127\u0126\3\2\2"+
		"\2\u0127\u0128\3\2\2\2\u0128\u0129\3\2\2\2\u0129\u012a\5@!\2\u012a\u0382"+
		"\3\2\2\2\u012b\u012c\7]\2\2\u012c\u012d\5\"\22\2\u012d\u012e\7J\2\2\u012e"+
		"\u012f\5(\25\2\u012f\u0131\7\66\2\2\u0130\u0132\7u\2\2\u0131\u0130\3\2"+
		"\2\2\u0131\u0132\3\2\2\2\u0132\u0133\3\2\2\2\u0133\u0134\5@!\2\u0134\u0382"+
		"\3\2\2\2\u0135\u0136\7:\2\2\u0136\u0137\5\"\22\2\u0137\u0138\7J\2\2\u0138"+
		"\u0139\5(\25\2\u0139\u013a\7n\2\2\u013a\u013b\7<\2\2\u013b\u013c\5@!\2"+
		"\u013c\u0382\3\2\2\2\u013d\u013e\7]\2\2\u013e\u013f\5\"\22\2\u013f\u0140"+
		"\7J\2\2\u0140\u0141\5(\25\2\u0141\u0142\7\66\2\2\u0142\u0143\7<\2\2\u0143"+
		"\u0144\5@!\2\u0144\u0382\3\2\2\2\u0145\u0146\7\35\2\2\u0146\u014a\7u\2"+
		"\2\u0147\u0148\7@\2\2\u0148\u0149\7I\2\2\u0149\u014b\7\62\2\2\u014a\u0147"+
		"\3\2\2\2\u014a\u014b\3\2\2\2\u014b\u014c\3\2\2\2\u014c\u014d\5@!\2\u014d"+
		"\u014e\7?\2\2\u014e\u014f\7\25\2\2\u014f\u0152\5> \2\u0150\u0151\7L\2"+
		"\2\u0151\u0153\5\66\34\2\u0152\u0150\3\2\2\2\u0152\u0153\3\2\2\2\u0153"+
		"\u0382\3\2\2\2\u0154\u0155\7Z\2\2\u0155\u0156\7u\2\2\u0156\u0157\5@!\2"+
		"\u0157\u0158\7n\2\2\u0158\u0159\5@!\2\u0159\u0382\3\2\2\2\u015a\u015b"+
		"\7\16\2\2\u015b\u015c\7u\2\2\u015c\u015d\5@!\2\u015d\u015e\7Z\2\2\u015e"+
		"\u015f\7n\2\2\u015f\u0160\5@!\2\u0160\u0382\3\2\2\2\u0161\u0162\7\16\2"+
		"\2\u0162\u0163\7u\2\2\u0163\u0164\5@!\2\u0164\u0165\7?\2\2\u0165\u0166"+
		"\7\25\2\2\u0166\u0167\5> \2\u0167\u0382\3\2\2\2\u0168\u0169\7\16\2\2\u0169"+
		"\u016a\7u\2\2\u016a\u016b\5@!\2\u016b\u016c\7e\2\2\u016c\u016d\7L\2\2"+
		"\u016d\u016e\5\66\34\2\u016e\u0382\3\2\2\2\u016f\u0170\7\16\2\2\u0170"+
		"\u0171\7u\2\2\u0171\u0172\5@!\2\u0172\u0173\7Y\2\2\u0173\u0174\7L\2\2"+
		"\u0174\u0175\5:\36\2\u0175\u0382\3\2\2\2\u0176\u0177\7,\2\2\u0177\u017a"+
		"\7u\2\2\u0178\u0179\7@\2\2\u0179\u017b\7\62\2\2\u017a\u0178\3\2\2\2\u017a"+
		"\u017b\3\2\2\2\u017b\u017c\3\2\2\2\u017c\u0382\5@!\2\u017d\u017e\7\35"+
		"\2\2\u017e\u0182\7<\2\2\u017f\u0180\7@\2\2\u0180\u0181\7I\2\2\u0181\u0183"+
		"\7\62\2\2\u0182\u017f\3\2\2\2\u0182\u0183\3\2\2\2\u0183\u0184\3\2\2\2"+
		"\u0184\u0187\5@!\2\u0185\u0186\7\33\2\2\u0186\u0188\7{\2\2\u0187\u0185"+
		"\3\2\2\2\u0187\u0188\3\2\2\2\u0188\u0382\3\2\2\2\u0189\u018a\7Z\2\2\u018a"+
		"\u018b\7<\2\2\u018b\u018c\5@!\2\u018c\u018d\7n\2\2\u018d\u018e\5@!\2\u018e"+
		"\u0382\3\2\2\2\u018f\u0190\7\16\2\2\u0190\u0191\7<\2\2\u0191\u0192\5@"+
		"!\2\u0192\u0193\7Z\2\2\u0193\u0194\7n\2\2\u0194\u0195\5@!\2\u0195\u0382"+
		"\3\2\2\2\u0196\u0197\7\16\2\2\u0197\u0198\7<\2\2\u0198\u0199\5@!\2\u0199"+
		"\u019a\7e\2\2\u019a\u019b\7\33\2\2\u019b\u019c\7{\2\2\u019c\u0382\3\2"+
		"\2\2\u019d\u019e\7\16\2\2\u019e\u019f\7<\2\2\u019f\u01a0\5@!\2\u01a0\u01a1"+
		"\5,\27\2\u01a1\u0382\3\2\2\2\u01a2\u01a3\7\16\2\2\u01a3\u01a4\7<\2\2\u01a4"+
		"\u01a5\5@!\2\u01a5\u01a6\5.\30\2\u01a6\u0382\3\2\2\2\u01a7\u01a8\7,\2"+
		"\2\u01a8\u01ab\7<\2\2\u01a9\u01aa\7@\2\2\u01aa\u01ac\7\62\2\2\u01ab\u01a9"+
		"\3\2\2\2\u01ab\u01ac\3\2\2\2\u01ac\u01ad\3\2\2\2\u01ad\u01af\5@!\2\u01ae"+
		"\u01b0\7\30\2\2\u01af\u01ae\3\2\2\2\u01af\u01b0\3\2\2\2\u01b0\u0382\3"+
		"\2\2\2\u01b1\u01b3\7G\2\2\u01b2\u01b4\7h\2\2\u01b3\u01b2\3\2\2\2\u01b3"+
		"\u01b4\3\2\2\2\u01b4\u01b5\3\2\2\2\u01b5\u01b9\7j\2\2\u01b6\u01b7\7@\2"+
		"\2\u01b7\u01b8\7I\2\2\u01b8\u01ba\7\62\2\2\u01b9\u01b6\3\2\2\2\u01b9\u01ba"+
		"\3\2\2\2\u01ba\u01bb\3\2\2\2\u01bb\u01c0\5\64\33\2\u01bc\u01bd\7\3\2\2"+
		"\u01bd\u01be\5\24\13\2\u01be\u01bf\7\4\2\2\u01bf\u01c1\3\2\2\2\u01c0\u01bc"+
		"\3\2\2\2\u01c0\u01c1\3\2\2\2\u01c1\u01c2\3\2\2\2\u01c2\u01c3\7L\2\2\u01c3"+
		"\u01c4\5\66\34\2\u01c4\u0382\3\2\2\2\u01c5\u01c6\7Z\2\2\u01c6\u01c7\7"+
		"j\2\2\u01c7\u01c8\5\64\33\2\u01c8\u01c9\7n\2\2\u01c9\u01ca\5\64\33\2\u01ca"+
		"\u0382\3\2\2\2\u01cb\u01cc\7\16\2\2\u01cc\u01cd\7j\2\2\u01cd\u01ce\5\64"+
		"\33\2\u01ce\u01cf\7Z\2\2\u01cf\u01d0\7n\2\2\u01d0\u01d1\5\64\33\2\u01d1"+
		"\u0382\3\2\2\2\u01d2\u01d3\7\16\2\2\u01d3\u01d4\7j\2\2\u01d4\u01d5\5\64"+
		"\33\2\u01d5\u01d6\7e\2\2\u01d6\u01d7\7L\2\2\u01d7\u01d8\5\66\34\2\u01d8"+
		"\u0382\3\2\2\2\u01d9\u01da\7\16\2\2\u01da\u01db\7j\2\2\u01db\u01dc\5\64"+
		"\33\2\u01dc\u01dd\7Y\2\2\u01dd\u01de\7L\2\2\u01de\u01df\5:\36\2\u01df"+
		"\u0382\3\2\2\2\u01e0\u01e1\7q\2\2\u01e1\u01e4\7j\2\2\u01e2\u01e3\7@\2"+
		"\2\u01e3\u01e5\7\62\2\2\u01e4\u01e2\3\2\2\2\u01e4\u01e5\3\2\2\2\u01e5"+
		"\u01e6\3\2\2\2\u01e6\u0382\5\64\33\2\u01e7\u01e8\7G\2\2\u01e8\u01ec\7"+
		" \2\2\u01e9\u01ea\7@\2\2\u01ea\u01eb\7I\2\2\u01eb\u01ed\7\62\2\2\u01ec"+
		"\u01e9\3\2\2\2\u01ec\u01ed\3\2\2\2\u01ed\u01ee\3\2\2\2\u01ee\u01ef\5@"+
		"!\2\u01ef\u01f0\7L\2\2\u01f0\u01f1\5\66\34\2\u01f1\u0382\3\2\2\2\u01f2"+
		"\u01f3\7q\2\2\u01f3\u01f6\7 \2\2\u01f4\u01f5\7@\2\2\u01f5\u01f7\7\62\2"+
		"\2\u01f6\u01f4\3\2\2\2\u01f6\u01f7\3\2\2\2\u01f7\u01f8\3\2\2\2\u01f8\u01fa"+
		"\5@!\2\u01f9\u01fb\7\30\2\2\u01fa\u01f9\3\2\2\2\u01fa\u01fb\3\2\2\2\u01fb"+
		"\u0382\3\2\2\2\u01fc\u01fd\7\16\2\2\u01fd\u01fe\7 \2\2\u01fe\u01ff\5@"+
		"!\2\u01ff\u0200\7e\2\2\u0200\u0201\7L\2\2\u0201\u0202\5\66\34\2\u0202"+
		"\u0382\3\2\2\2\u0203\u0204\7\16\2\2\u0204\u0205\7 \2\2\u0205\u0206\5@"+
		"!\2\u0206\u0207\7Y\2\2\u0207\u0208\7L\2\2\u0208\u0209\5:\36\2\u0209\u0382"+
		"\3\2\2\2\u020a\u020b\7[\2\2\u020b\u020c\7 \2\2\u020c\u0382\5@!\2\u020d"+
		"\u020e\7\35\2\2\u020e\u0212\7 \2\2\u020f\u0210\7@\2\2\u0210\u0211\7I\2"+
		"\2\u0211\u0213\7\62\2\2\u0212\u020f\3\2\2\2\u0212\u0213\3\2\2\2\u0213"+
		"\u0214\3\2\2\2\u0214\u0217\5@!\2\u0215\u0216\7\33\2\2\u0216\u0218\7{\2"+
		"\2\u0217\u0215\3\2\2\2\u0217\u0218\3\2\2\2\u0218\u0382\3\2\2\2\u0219\u021a"+
		"\7Z\2\2\u021a\u021b\7 \2\2\u021b\u021c\5@!\2\u021c\u021d\7n\2\2\u021d"+
		"\u021e\5@!\2\u021e\u0382\3\2\2\2\u021f\u0220\7\16\2\2\u0220\u0221\7 \2"+
		"\2\u0221\u0222\5@!\2\u0222\u0223\7Z\2\2\u0223\u0224\7n\2\2\u0224\u0225"+
		"\5@!\2\u0225\u0382\3\2\2\2\u0226\u0227\7\16\2\2\u0227\u0228\7 \2\2\u0228"+
		"\u0229\5@!\2\u0229\u022a\7e\2\2\u022a\u022b\7\33\2\2\u022b\u022c\7{\2"+
		"\2\u022c\u0382\3\2\2\2\u022d\u022e\7,\2\2\u022e\u0231\7 \2\2\u022f\u0230"+
		"\7@\2\2\u0230\u0232\7\62\2\2\u0231\u022f\3\2\2\2\u0231\u0232\3\2\2\2\u0232"+
		"\u0233\3\2\2\2\u0233\u0235\5@!\2\u0234\u0236\7\30\2\2\u0235\u0234\3\2"+
		"\2\2\u0235\u0236\3\2\2\2\u0236\u0382\3\2\2\2\u0237\u0238\7s\2\2\u0238"+
		"\u0382\5@!\2\u0239\u023b\7\35\2\2\u023a\u023c\t\3\2\2\u023b\u023a\3\2"+
		"\2\2\u023b\u023c\3\2\2\2\u023c\u023d\3\2\2\2\u023d\u0241\7\67\2\2\u023e"+
		"\u023f\7@\2\2\u023f\u0240\7I\2\2\u0240\u0242\7\62\2\2\u0241\u023e\3\2"+
		"\2\2\u0241\u0242\3\2\2\2\u0242\u0243\3\2\2\2\u0243\u0244\5\62\32\2\u0244"+
		"\u0245\7\24\2\2\u0245\u0247\7{\2\2\u0246\u0248\7{\2\2\u0247\u0246\3\2"+
		"\2\2\u0247\u0248\3\2\2\2\u0248\u0252\3\2\2\2\u0249\u024a\7t\2\2\u024a"+
		"\u024f\5B\"\2\u024b\u024c\7\5\2\2\u024c\u024e\5B\"\2\u024d\u024b\3\2\2"+
		"\2\u024e\u0251\3\2\2\2\u024f\u024d\3\2\2\2\u024f\u0250\3\2\2\2\u0250\u0253"+
		"\3\2\2\2\u0251\u024f\3\2\2\2\u0252\u0249\3\2\2\2\u0252\u0253\3\2\2\2\u0253"+
		"\u0382\3\2\2\2\u0254\u0256\7,\2\2\u0255\u0257\t\3\2\2\u0256\u0255\3\2"+
		"\2\2\u0256\u0257\3\2\2\2\u0257\u0258\3\2\2\2\u0258\u025b\7\67\2\2\u0259"+
		"\u025a\7@\2\2\u025a\u025c\7\62\2\2\u025b\u0259\3\2\2\2\u025b\u025c\3\2"+
		"\2\2\u025c\u025d\3\2\2\2\u025d\u0382\5\62\32\2\u025e\u0261\7\35\2\2\u025f"+
		"\u0260\7M\2\2\u0260\u0262\7\\\2\2\u0261\u025f\3\2\2\2\u0261\u0262\3\2"+
		"\2\2\u0262\u0263\3\2\2\2\u0263\u0264\7x\2\2\u0264\u0267\5\64\33\2\u0265"+
		"\u0266\7\33\2\2\u0266\u0268\7{\2\2\u0267\u0265\3\2\2\2\u0267\u0268\3\2"+
		"\2\2\u0268\u0269\3\2\2\2\u0269\u026a\7\24\2\2\u026a\u026b\5\n\6\2\u026b"+
		"\u0382\3\2\2\2\u026c\u026d\7\16\2\2\u026d\u026e\7x\2\2\u026e\u026f\5\64"+
		"\33\2\u026f\u0270\7\24\2\2\u0270\u0271\5\n\6\2\u0271\u0382\3\2\2\2\u0272"+
		"\u0273\7,\2\2\u0273\u0276\7x\2\2\u0274\u0275\7@\2\2\u0275\u0277\7\62\2"+
		"\2\u0276\u0274\3\2\2\2\u0276\u0277\3\2\2\2\u0277\u0278\3\2\2\2\u0278\u0382"+
		"\5\64\33\2\u0279\u027a\7\35\2\2\u027a\u027e\t\4\2\2\u027b\u027c\7@\2\2"+
		"\u027c\u027d\7I\2\2\u027d\u027f\7\62\2\2\u027e\u027b\3\2\2\2\u027e\u027f"+
		"\3\2\2\2\u027f\u0280\3\2\2\2\u0280\u0281\5@!\2\u0281\u0282\7t\2\2\u0282"+
		"\u0283\t\5\2\2\u0283\u0284\7\24\2\2\u0284\u0285\5\6\4\2\u0285\u0382\3"+
		"\2\2\2\u0286\u0287\7Z\2\2\u0287\u0288\t\4\2\2\u0288\u0289\5@!\2\u0289"+
		"\u028a\7n\2\2\u028a\u028b\5@!\2\u028b\u0382\3\2\2\2\u028c\u028d\7\16\2"+
		"\2\u028d\u028e\t\4\2\2\u028e\u028f\5@!\2\u028f\u0290\7Z\2\2\u0290\u0291"+
		"\7n\2\2\u0291\u0292\5@!\2\u0292\u0382\3\2\2\2\u0293\u0294\7\16\2\2\u0294"+
		"\u0295\t\4\2\2\u0295\u0296\5@!\2\u0296\u0297\7\24\2\2\u0297\u0298\5\6"+
		"\4\2\u0298\u0382\3\2\2\2\u0299\u029a\7,\2\2\u029a\u029d\t\4\2\2\u029b"+
		"\u029c\7@\2\2\u029c\u029e\7\62\2\2\u029d\u029b\3\2\2\2\u029d\u029e\3\2"+
		"\2\2\u029e\u029f\3\2\2\2\u029f\u0382\5@!\2\u02a0\u02a3\7\35\2\2\u02a1"+
		"\u02a2\7%\2\2\u02a2\u02a4\5\b\5\2\u02a3\u02a1\3\2\2\2\u02a3\u02a4\3\2"+
		"\2\2\u02a4\u02a5\3\2\2\2\u02a5\u02a9\7\60\2\2\u02a6\u02a7\7@\2\2\u02a7"+
		"\u02a8\7I\2\2\u02a8\u02aa\7\62\2\2\u02a9\u02a6\3\2\2\2\u02a9\u02aa\3\2"+
		"\2\2\u02aa\u02ab\3\2\2\2\u02ab\u02ac\5@!\2\u02ac\u02ad\7J\2\2\u02ad\u02ae"+
		"\7b\2\2\u02ae\u02af\7\21\2\2\u02af\u02b1\7{\2\2\u02b0\u02b2\t\6\2\2\u02b1"+
		"\u02b0\3\2\2\2\u02b1\u02b2\3\2\2\2\u02b2\u02b5\3\2\2\2\u02b3\u02b4\7\33"+
		"\2\2\u02b4\u02b6\7{\2\2\u02b5\u02b3\3\2\2\2\u02b5\u02b6\3\2\2\2\u02b6"+
		"\u02b7\3\2\2\2\u02b7\u02b8\7*\2\2\u02b8\u02b9\7\27\2\2\u02b9\u02ba\5@"+
		"!\2\u02ba\u0382\3\2\2\2\u02bb\u02bc\7Z\2\2\u02bc\u02bd\7\60\2\2\u02bd"+
		"\u02be\5@!\2\u02be\u02bf\7n\2\2\u02bf\u02c0\5@!\2\u02c0\u0382\3\2\2\2"+
		"\u02c1\u02c2\7\16\2\2\u02c2\u02c3\7%\2\2\u02c3\u02c4\5\b\5\2\u02c4\u02c5"+
		"\7\60\2\2\u02c5\u02c6\5@!\2\u02c6\u0382\3\2\2\2\u02c7\u02c8\7\16\2\2\u02c8"+
		"\u02c9\7\60\2\2\u02c9\u02ca\5@!\2\u02ca\u02cb\7Z\2\2\u02cb\u02cc\7n\2"+
		"\2\u02cc\u02cd\5@!\2\u02cd\u0382\3\2\2\2\u02ce\u02cf\7\16\2\2\u02cf\u02d0"+
		"\7\60\2\2\u02d0\u02d1\5@!\2\u02d1\u02d2\7J\2\2\u02d2\u02d3\7b\2\2\u02d3"+
		"\u02d4\7\21\2\2\u02d4\u02d5\7{\2\2\u02d5\u0382\3\2\2\2\u02d6\u02d7\7\16"+
		"\2\2\u02d7\u02d8\7\60\2\2\u02d8\u02d9\5@!\2\u02d9\u02da\t\6\2\2\u02da"+
		"\u0382\3\2\2\2\u02db\u02dc\7,\2\2\u02dc\u02df\7\60\2\2\u02dd\u02de\7@"+
		"\2\2\u02de\u02e0\7\62\2\2\u02df\u02dd\3\2\2\2\u02df\u02e0\3\2\2\2\u02e0"+
		"\u02e1\3\2\2\2\u02e1\u0382\5@!\2\u02e2\u02e3\7f\2\2\u02e3\u0382\7i\2\2"+
		"\u02e4\u02e5\7f\2\2\u02e5\u0382\7F\2\2\u02e6\u02e7\7f\2\2\u02e7\u02e8"+
		"\7^\2\2\u02e8\u0382\7\61\2\2\u02e9\u02ea\7f\2\2\u02ea\u02ed\7\61\2\2\u02eb"+
		"\u02ec\7D\2\2\u02ec\u02ee\7{\2\2\u02ed\u02eb\3\2\2\2\u02ed\u02ee\3\2\2"+
		"\2\u02ee\u0382\3\2\2\2\u02ef\u02f0\7f\2\2\u02f0\u02f3\7!\2\2\u02f1\u02f2"+
		"\7D\2\2\u02f2\u02f4\7{\2\2\u02f3\u02f1\3\2\2\2\u02f3\u02f4\3\2\2\2\u02f4"+
		"\u0382\3\2\2\2\u02f5\u02f6\7f\2\2\u02f6\u02f9\7k\2\2\u02f7\u02f8\t\7\2"+
		"\2\u02f8\u02fa\5@!\2\u02f9\u02f7\3\2\2\2\u02f9\u02fa\3\2\2\2\u02fa\u02fd"+
		"\3\2\2\2\u02fb\u02fc\7D\2\2\u02fc\u02fe\7{\2\2\u02fd\u02fb\3\2\2\2\u02fd"+
		"\u02fe\3\2\2\2\u02fe\u0382\3\2\2\2\u02ff\u0301\7f\2\2\u0300\u0302\5@!"+
		"\2\u0301\u0300\3\2\2\2\u0301\u0302\3\2\2\2\u0302\u0303\3\2\2\2\u0303\u0306"+
		"\78\2\2\u0304\u0305\t\7\2\2\u0305\u0307\5@!\2\u0306\u0304\3\2\2\2\u0306"+
		"\u0307\3\2\2\2\u0307\u030a\3\2\2\2\u0308\u0309\7D\2\2\u0309\u030b\7{\2"+
		"\2\u030a\u0308\3\2\2\2\u030a\u030b\3\2\2\2\u030b\u0382\3\2\2\2\u030c\u030d"+
		"\7f\2\2\u030d\u0310\t\b\2\2\u030e\u030f\7D\2\2\u030f\u0311\7{\2\2\u0310"+
		"\u030e\3\2\2\2\u0310\u0311\3\2\2\2\u0311\u0382\3\2\2\2\u0312\u0313\7f"+
		"\2\2\u0313\u0316\7`\2\2\u0314\u0315\7D\2\2\u0315\u0317\7{\2\2\u0316\u0314"+
		"\3\2\2\2\u0316\u0317\3\2\2\2\u0317\u0382\3\2\2\2\u0318\u0319\7f\2\2\u0319"+
		"\u031c\7v\2\2\u031a\u031b\7D\2\2\u031b\u031d\7{\2\2\u031c\u031a\3\2\2"+
		"\2\u031c\u031d\3\2\2\2\u031d\u0382\3\2\2\2\u031e\u031f\7f\2\2\u031f\u0322"+
		"\7=\2\2\u0320\u0321\7D\2\2\u0321\u0323\7{\2\2\u0322\u0320\3\2\2\2\u0322"+
		"\u0323\3\2\2\2\u0323\u0382\3\2\2\2\u0324\u0325\7f\2\2\u0325\u0326\7v\2"+
		"\2\u0326\u0327\7A\2\2\u0327\u0328\7<\2\2\u0328\u032b\5@!\2\u0329\u032a"+
		"\7D\2\2\u032a\u032c\7{\2\2\u032b\u0329\3\2\2\2\u032b\u032c\3\2\2\2\u032c"+
		"\u0382\3\2\2\2\u032d\u032e\7f\2\2\u032e\u0331\t\t\2\2\u032f\u0330\7D\2"+
		"\2\u0330\u0332\7{\2\2\u0331\u032f\3\2\2\2\u0331\u0332\3\2\2\2\u0332\u0382"+
		"\3\2\2\2\u0333\u0334\7f\2\2\u0334\u0337\7w\2\2\u0335\u0336\7D\2\2\u0336"+
		"\u0338\7{\2\2\u0337\u0335\3\2\2\2\u0337\u0338\3\2\2\2\u0338\u0382\3\2"+
		"\2\2\u0339\u033a\7f\2\2\u033a\u033b\7;\2\2\u033b\u033c\7\65\2\2\u033c"+
		"\u0382\5@!\2\u033d\u033e\7f\2\2\u033e\u033f\7\35\2\2\u033f\u0340\7j\2"+
		"\2\u0340\u0382\5\64\33\2\u0341\u0342\7f\2\2\u0342\u0343\7a\2\2\u0343\u0344"+
		"\7\65\2\2\u0344\u0382\5\n\6\2\u0345\u0346\t\n\2\2\u0346\u0347\7\60\2\2"+
		"\u0347\u0382\5@!\2\u0348\u0349\t\n\2\2\u0349\u034a\t\4\2\2\u034a\u0382"+
		"\5@!\2\u034b\u034c\t\n\2\2\u034c\u034d\7 \2\2\u034d\u0382\5@!\2\u034e"+
		"\u0350\t\n\2\2\u034f\u0351\7j\2\2\u0350\u034f\3\2\2\2\u0350\u0351\3\2"+
		"\2\2\u0351\u0353\3\2\2\2\u0352\u0354\7\64\2\2\u0353\u0352\3\2\2\2\u0353"+
		"\u0354\3\2\2\2\u0354\u0355\3\2\2\2\u0355\u0382\5\64\33\2\u0356\u0357\t"+
		"\n\2\2\u0357\u0359\7\67\2\2\u0358\u035a\7\64\2\2\u0359\u0358\3\2\2\2\u0359"+
		"\u035a\3\2\2\2\u035a\u035b\3\2\2\2\u035b\u0382\5\62\32\2\u035c\u035d\t"+
		"\n\2\2\u035d\u035e\7u\2\2\u035e\u0382\5@!\2\u035f\u0360\t\n\2\2\u0360"+
		"\u0361\7N\2\2\u0361\u0382\5@!\2\u0362\u0364\7\63\2\2\u0363\u0365\7\64"+
		"\2\2\u0364\u0363\3\2\2\2\u0364\u0365\3\2\2\2\u0365\u0367\3\2\2\2\u0366"+
		"\u0368\7S\2\2\u0367\u0366\3\2\2\2\u0367\u0368\3\2\2\2\u0368\u0369\3\2"+
		"\2\2\u0369\u0382\5\n\6\2\u036a\u036d\7\35\2\2\u036b\u036c\7M\2\2\u036c"+
		"\u036e\7\\\2\2\u036d\u036b\3\2\2\2\u036d\u036e\3\2\2\2\u036e\u0370\3\2"+
		"\2\2\u036f\u0371\7\26\2\2\u0370\u036f\3\2\2\2\u0370\u0371\3\2\2\2\u0371"+
		"\u0372\3\2\2\2\u0372\u0373\t\3\2\2\u0373\u0374\7x\2\2\u0374\u0376\5@!"+
		"\2\u0375\u0377\7\24\2\2\u0376\u0375\3\2\2\2\u0376\u0377\3\2\2\2\u0377"+
		"\u0378\3\2\2\2\u0378\u0379\5\n\6\2\u0379\u0382\3\2\2\2\u037a\u037e\t\13"+
		"\2\2\u037b\u037d\13\2\2\2\u037c\u037b\3\2\2\2\u037d\u0380\3\2\2\2\u037e"+
		"\u037f\3\2\2\2\u037e\u037c\3\2\2\2\u037f\u0382\3\2\2\2\u0380\u037e\3\2"+
		"\2\2\u0381I\3\2\2\2\u0381Y\3\2\2\2\u0381_\3\2\2\2\u0381f\3\2\2\2\u0381"+
		"m\3\2\2\2\u0381t\3\2\2\2\u0381{\3\2\2\2\u0381\u0085\3\2\2\2\u0381\u0099"+
		"\3\2\2\2\u0381\u00a4\3\2\2\2\u0381\u00b0\3\2\2\2\u0381\u00bc\3\2\2\2\u0381"+
		"\u00c8\3\2\2\2\u0381\u00d4\3\2\2\2\u0381\u00e1\3\2\2\2\u0381\u00eb\3\2"+
		"\2\2\u0381\u00f5\3\2\2\2\u0381\u00fd\3\2\2\2\u0381\u0105\3\2\2\2\u0381"+
		"\u010d\3\2\2\2\u0381\u0115\3\2\2\2\u0381\u011b\3\2\2\2\u0381\u0121\3\2"+
		"\2\2\u0381\u012b\3\2\2\2\u0381\u0135\3\2\2\2\u0381\u013d\3\2\2\2\u0381"+
		"\u0145\3\2\2\2\u0381\u0154\3\2\2\2\u0381\u015a\3\2\2\2\u0381\u0161\3\2"+
		"\2\2\u0381\u0168\3\2\2\2\u0381\u016f\3\2\2\2\u0381\u0176\3\2\2\2\u0381"+
		"\u017d\3\2\2\2\u0381\u0189\3\2\2\2\u0381\u018f\3\2\2\2\u0381\u0196\3\2"+
		"\2\2\u0381\u019d\3\2\2\2\u0381\u01a2\3\2\2\2\u0381\u01a7\3\2\2\2\u0381"+
		"\u01b1\3\2\2\2\u0381\u01c5\3\2\2\2\u0381\u01cb\3\2\2\2\u0381\u01d2\3\2"+
		"\2\2\u0381\u01d9\3\2\2\2\u0381\u01e0\3\2\2\2\u0381\u01e7\3\2\2\2\u0381"+
		"\u01f2\3\2\2\2\u0381\u01fc\3\2\2\2\u0381\u0203\3\2\2\2\u0381\u020a\3\2"+
		"\2\2\u0381\u020d\3\2\2\2\u0381\u0219\3\2\2\2\u0381\u021f\3\2\2\2\u0381"+
		"\u0226\3\2\2\2\u0381\u022d\3\2\2\2\u0381\u0237\3\2\2\2\u0381\u0239\3\2"+
		"\2\2\u0381\u0254\3\2\2\2\u0381\u025e\3\2\2\2\u0381\u026c\3\2\2\2\u0381"+
		"\u0272\3\2\2\2\u0381\u0279\3\2\2\2\u0381\u0286\3\2\2\2\u0381\u028c\3\2"+
		"\2\2\u0381\u0293\3\2\2\2\u0381\u0299\3\2\2\2\u0381\u02a0\3\2\2\2\u0381"+
		"\u02bb\3\2\2\2\u0381\u02c1\3\2\2\2\u0381\u02c7\3\2\2\2\u0381\u02ce\3\2"+
		"\2\2\u0381\u02d6\3\2\2\2\u0381\u02db\3\2\2\2\u0381\u02e2\3\2\2\2\u0381"+
		"\u02e4\3\2\2\2\u0381\u02e6\3\2\2\2\u0381\u02e9\3\2\2\2\u0381\u02ef\3\2"+
		"\2\2\u0381\u02f5\3\2\2\2\u0381\u02ff\3\2\2\2\u0381\u030c\3\2\2\2\u0381"+
		"\u0312\3\2\2\2\u0381\u0318\3\2\2\2\u0381\u031e\3\2\2\2\u0381\u0324\3\2"+
		"\2\2\u0381\u032d\3\2\2\2\u0381\u0333\3\2\2\2\u0381\u0339\3\2\2\2\u0381"+
		"\u033d\3\2\2\2\u0381\u0341\3\2\2\2\u0381\u0345\3\2\2\2\u0381\u0348\3\2"+
		"\2\2\u0381\u034b\3\2\2\2\u0381\u034e\3\2\2\2\u0381\u0356\3\2\2\2\u0381"+
		"\u035c\3\2\2\2\u0381\u035f\3\2\2\2\u0381\u0362\3\2\2\2\u0381\u036a\3\2"+
		"\2\2\u0381\u037a\3\2\2\2\u0382\5\3\2\2\2\u0383\u0384\7\3\2\2\u0384\u0389"+
		"\5\4\3\2\u0385\u0386\7\6\2\2\u0386\u0388\5\4\3\2\u0387\u0385\3\2\2\2\u0388"+
		"\u038b\3\2\2\2\u0389\u0387\3\2\2\2\u0389\u038a\3\2\2\2\u038a\u038c\3\2"+
		"\2\2\u038b\u0389\3\2\2\2\u038c\u038d\7\4\2\2\u038d\7\3\2\2\2\u038e\u0390"+
		"\7.\2\2\u038f\u038e\3\2\2\2\u038f\u0390\3\2\2\2\u0390\u0393\3\2\2\2\u0391"+
		"\u0394\5@!\2\u0392\u0394\7\37\2\2\u0393\u0391\3\2\2\2\u0393\u0392\3\2"+
		"\2\2\u0394\t\3\2\2\2\u0395\u0399\7c\2\2\u0396\u0398\n\f\2\2\u0397\u0396"+
		"\3\2\2\2\u0398\u039b\3\2\2\2\u0399\u0397\3\2\2\2\u0399\u039a\3\2\2\2\u039a"+
		"\u03a5\3\2\2\2\u039b\u0399\3\2\2\2\u039c\u039d\5\f\7\2\u039d\u03a1\7c"+
		"\2\2\u039e\u03a0\n\f\2\2\u039f\u039e\3\2\2\2\u03a0\u03a3\3\2\2\2\u03a1"+
		"\u039f\3\2\2\2\u03a1\u03a2\3\2\2\2\u03a2\u03a5\3\2\2\2\u03a3\u03a1\3\2"+
		"\2\2\u03a4\u0395\3\2\2\2\u03a4\u039c\3\2\2\2\u03a5\13\3\2\2\2\u03a6\u03a7"+
		"\7z\2\2\u03a7\u03ac\5\34\17\2\u03a8\u03a9\7\5\2\2\u03a9\u03ab\5\34\17"+
		"\2\u03aa\u03a8\3\2\2\2\u03ab\u03ae\3\2\2\2\u03ac\u03aa\3\2\2\2\u03ac\u03ad"+
		"\3\2\2\2\u03ad\r\3\2\2\2\u03ae\u03ac\3\2\2\2\u03af\u03b0\7T\2\2\u03b0"+
		"\u03b1\7\3\2\2\u03b1\u03b6\5@!\2\u03b2\u03b3\7\5\2\2\u03b3\u03b5\5@!\2"+
		"\u03b4\u03b2\3\2\2\2\u03b5\u03b8\3\2\2\2\u03b6\u03b4\3\2\2\2\u03b6\u03b7"+
		"\3\2\2\2\u03b7\u03b9\3\2\2\2\u03b8\u03b6\3\2\2\2\u03b9\u03ba\7\4\2\2\u03ba"+
		"\17\3\2\2\2\u03bb\u03bc\7\36\2\2\u03bc\u03bd\7|\2\2\u03bd\21\3\2\2\2\u03be"+
		"\u03bf\7\20\2\2\u03bf\u03c0\7\7\2\2\u03c0\u03c1\5\22\n\2\u03c1\u03c2\7"+
		"\b\2\2\u03c2\u03e1\3\2\2\2\u03c3\u03c4\7\22\2\2\u03c4\u03c5\7\7\2\2\u03c5"+
		"\u03c6\5\22\n\2\u03c6\u03c7\7\5\2\2\u03c7\u03c8\5\22\n\2\u03c8\u03c9\7"+
		"\b\2\2\u03c9\u03e1\3\2\2\2\u03ca\u03d1\7\23\2\2\u03cb\u03cd\7\7\2\2\u03cc"+
		"\u03ce\5\30\r\2\u03cd\u03cc\3\2\2\2\u03cd\u03ce\3\2\2\2\u03ce\u03cf\3"+
		"\2\2\2\u03cf\u03d2\7\b\2\2\u03d0\u03d2\7/\2\2\u03d1\u03cb\3\2\2\2\u03d1"+
		"\u03d0\3\2\2\2\u03d2\u03e1\3\2\2\2\u03d3\u03de\5@!\2\u03d4\u03d5\7\3\2"+
		"\2\u03d5\u03da\7|\2\2\u03d6\u03d7\7\5\2\2\u03d7\u03d9\7|\2\2\u03d8\u03d6"+
		"\3\2\2\2\u03d9\u03dc\3\2\2\2\u03da\u03d8\3\2\2\2\u03da\u03db\3\2\2\2\u03db"+
		"\u03dd\3\2\2\2\u03dc\u03da\3\2\2\2\u03dd\u03df\7\4\2\2\u03de\u03d4\3\2"+
		"\2\2\u03de\u03df\3\2\2\2\u03df\u03e1\3\2\2\2\u03e0\u03be\3\2\2\2\u03e0"+
		"\u03c3\3\2\2\2\u03e0\u03ca\3\2\2\2\u03e0\u03d3\3\2\2\2\u03e1\23\3\2\2"+
		"\2\u03e2\u03e7\5\26\f\2\u03e3\u03e4\7\5\2\2\u03e4\u03e6\5\26\f\2\u03e5"+
		"\u03e3\3\2\2\2\u03e6\u03e9\3\2\2\2\u03e7\u03e5\3\2\2\2\u03e7\u03e8\3\2"+
		"\2\2\u03e8\25\3\2\2\2\u03e9\u03e7\3\2\2\2\u03ea\u03eb\5@!\2\u03eb\u03ec"+
		"\5\22\n\2\u03ec\27\3\2\2\2\u03ed\u03f2\5\32\16\2\u03ee\u03ef\7\5\2\2\u03ef"+
		"\u03f1\5\32\16\2\u03f0\u03ee\3\2\2\2\u03f1\u03f4\3\2\2\2\u03f2\u03f0\3"+
		"\2\2\2\u03f2\u03f3\3\2\2\2\u03f3\31\3\2\2\2\u03f4\u03f2\3\2\2\2\u03f5"+
		"\u03f6\5@!\2\u03f6\u03f7\7\t\2\2\u03f7\u03f8\5\22\n\2\u03f8\33\3\2\2\2"+
		"\u03f9\u03fb\5@!\2\u03fa\u03fc\7\24\2\2\u03fb\u03fa\3\2\2\2\u03fb\u03fc"+
		"\3\2\2\2\u03fc\u03fd\3\2\2\2\u03fd\u03fe\7\3\2\2\u03fe\u03ff\5\n\6\2\u03ff"+
		"\u0400\7\4\2\2\u0400\35\3\2\2\2\u0401\u0406\5 \21\2\u0402\u0403\7\5\2"+
		"\2\u0403\u0405\5 \21\2\u0404\u0402\3\2\2\2\u0405\u0408\3\2\2\2\u0406\u0404"+
		"\3\2\2\2\u0406\u0407\3\2\2\2\u0407\37\3\2\2\2\u0408\u0406\3\2\2\2\u0409"+
		"\u040a\t\r\2\2\u040a!\3\2\2\2\u040b\u0410\5$\23\2\u040c\u040d\7\5\2\2"+
		"\u040d\u040f\5$\23\2\u040e\u040c\3\2\2\2\u040f\u0412\3\2\2\2\u0410\u040e"+
		"\3\2\2\2\u0410\u0411\3\2\2\2\u0411#\3\2\2\2\u0412\u0410\3\2\2\2\u0413"+
		"\u0415\7c\2\2\u0414\u0416\5&\24\2\u0415\u0414\3\2\2\2\u0415\u0416\3\2"+
		"\2\2\u0416\u0420\3\2\2\2\u0417\u0419\7r\2\2\u0418\u041a\5&\24\2\u0419"+
		"\u0418\3\2\2\2\u0419\u041a\3\2\2\2\u041a\u0420\3\2\2\2\u041b\u0420\7B"+
		"\2\2\u041c\u0420\7&\2\2\u041d\u0420\7p\2\2\u041e\u0420\7\r\2\2\u041f\u0413"+
		"\3\2\2\2\u041f\u0417\3\2\2\2\u041f\u041b\3\2\2\2\u041f\u041c\3\2\2\2\u041f"+
		"\u041d\3\2\2\2\u041f\u041e\3\2\2\2\u0420%\3\2\2\2\u0421\u0422\7\3\2\2"+
		"\u0422\u0427\5@!\2\u0423\u0424\7\5\2\2\u0424\u0426\5@!\2\u0425\u0423\3"+
		"\2\2\2\u0426\u0429\3\2\2\2\u0427\u0425\3\2\2\2\u0427\u0428\3\2\2\2\u0428"+
		"\u042a\3\2\2\2\u0429\u0427\3\2\2\2\u042a\u042b\7\4\2\2\u042b\'\3\2\2\2"+
		"\u042c\u042d\5*\26\2\u042d\u042e\7\n\2\2\u042e\u0430\3\2\2\2\u042f\u042c"+
		"\3\2\2\2\u042f\u0430\3\2\2\2\u0430\u0431\3\2\2\2\u0431\u0432\5*\26\2\u0432"+
		")\3\2\2\2\u0433\u0436\5@!\2\u0434\u0436\7g\2\2\u0435\u0433\3\2\2\2\u0435"+
		"\u0434\3\2\2\2\u0436+\3\2\2\2\u0437\u0438\7\f\2\2\u0438\u0439\7u\2\2\u0439"+
		"\u043a\5\60\31\2\u043a-\3\2\2\2\u043b\u043c\7Y\2\2\u043c\u043d\7u\2\2"+
		"\u043d\u043e\5\60\31\2\u043e/\3\2\2\2\u043f\u0444\5@!\2\u0440\u0441\7"+
		"\5\2\2\u0441\u0443\5@!\2\u0442\u0440\3\2\2\2\u0443\u0446\3\2\2\2\u0444"+
		"\u0442\3\2\2\2\u0444\u0445\3\2\2\2\u0445\61\3\2\2\2\u0446\u0444\3\2\2"+
		"\2\u0447\u0448\5@!\2\u0448\u0449\7\n\2\2\u0449\u044b\3\2\2\2\u044a\u0447"+
		"\3\2\2\2\u044a\u044b\3\2\2\2\u044b\u044c\3\2\2\2\u044c\u044d\5@!\2\u044d"+
		"\63\3\2\2\2\u044e\u044f\5@!\2\u044f\u0450\7\n\2\2\u0450\u0452\3\2\2\2"+
		"\u0451\u044e\3\2\2\2\u0451\u0452\3\2\2\2\u0452\u0453\3\2\2\2\u0453\u0454"+
		"\5@!\2\u0454\65\3\2\2\2\u0455\u0456\7\3\2\2\u0456\u045b\58\35\2\u0457"+
		"\u0458\7\5\2\2\u0458\u045a\58\35\2\u0459\u0457\3\2\2\2\u045a\u045d\3\2"+
		"\2\2\u045b\u0459\3\2\2\2\u045b\u045c\3\2\2\2\u045c\u045e\3\2\2\2\u045d"+
		"\u045b\3\2\2\2\u045e\u045f\7\4\2\2\u045f\67\3\2\2\2\u0460\u0462\5<\37"+
		"\2\u0461\u0463\7.\2\2\u0462\u0461\3\2\2\2\u0462\u0463\3\2\2\2\u0463\u0464"+
		"\3\2\2\2\u0464\u0465\7{\2\2\u04659\3\2\2\2\u0466\u0467\7\3\2\2\u0467\u046c"+
		"\5<\37\2\u0468\u0469\7\5\2\2\u0469\u046b\5<\37\2\u046a\u0468\3\2\2\2\u046b"+
		"\u046e\3\2\2\2\u046c\u046a\3\2\2\2\u046c\u046d\3\2\2\2\u046d\u046f\3\2"+
		"\2\2\u046e\u046c\3\2\2\2\u046f\u0470\7\4\2\2\u0470;\3\2\2\2\u0471\u0476"+
		"\5@!\2\u0472\u0473\7\n\2\2\u0473\u0475\5@!\2\u0474\u0472\3\2\2\2\u0475"+
		"\u0478\3\2\2\2\u0476\u0474\3\2\2\2\u0476\u0477\3\2\2\2\u0477\u047b\3\2"+
		"\2\2\u0478\u0476\3\2\2\2\u0479\u047b\7{\2\2\u047a\u0471\3\2\2\2\u047a"+
		"\u0479\3\2\2\2\u047b=\3\2\2\2\u047c\u047e\13\2\2\2\u047d\u047c\3\2\2\2"+
		"\u047e\u0481\3\2\2\2\u047f\u0480\3\2\2\2\u047f\u047d\3\2\2\2\u0480?\3"+
		"\2\2\2\u0481\u047f\3\2\2\2\u0482\u0486\7}\2\2\u0483\u0486\7~\2\2\u0484"+
		"\u0486\5D#\2\u0485\u0482\3\2\2\2\u0485\u0483\3\2\2\2\u0485\u0484\3\2\2"+
		"\2\u0486A\3\2\2\2\u0487\u0488\5@!\2\u0488\u0489\7{\2\2\u0489C\3\2\2\2"+
		"\u048a\u048b\t\16\2\2\u048bE\3\2\2\2tNSW\177\u0083\u008a\u008f\u0097\u009e"+
		"\u00a9\u00b5\u00c1\u00cd\u00d8\u00dd\u00e7\u00f1\u0109\u0111\u0127\u0131"+
		"\u014a\u0152\u017a\u0182\u0187\u01ab\u01af\u01b3\u01b9\u01c0\u01e4\u01ec"+
		"\u01f6\u01fa\u0212\u0217\u0231\u0235\u023b\u0241\u0247\u024f\u0252\u0256"+
		"\u025b\u0261\u0267\u0276\u027e\u029d\u02a3\u02a9\u02b1\u02b5\u02df\u02ed"+
		"\u02f3\u02f9\u02fd\u0301\u0306\u030a\u0310\u0316\u031c\u0322\u032b\u0331"+
		"\u0337\u0350\u0353\u0359\u0364\u0367\u036d\u0370\u0376\u037e\u0381\u0389"+
		"\u038f\u0393\u0399\u03a1\u03a4\u03ac\u03b6\u03cd\u03d1\u03da\u03de\u03e0"+
		"\u03e7\u03f2\u03fb\u0406\u0410\u0415\u0419\u041f\u0427\u042f\u0435\u0444"+
		"\u044a\u0451\u045b\u0462\u046c\u0476\u047a\u047f\u0485";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}