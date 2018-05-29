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
		T__9=10, T__10=11, T__11=12, ACCOUNT=13, ADD=14, ALTER=15, APPLICATION=16, 
		APPLICATIONS=17, ARRAY=18, AT=19, MAP=20, STRUCT=21, AS=22, BY=23, CACHE=24, 
		CALL=25, CASCADE=26, COLUMN=27, COLUMNS=28, COMMENT=29, CHANGE=30, CREATE=31, 
		CURRENT_USER=32, DATABASE=33, DATABASES=34, DATASOURCE=35, DATASOURCES=36, 
		DDL=37, DEFINER=38, DESC=39, DESCRIBE=40, DISABLE=41, DO=42, DML=43, DMLON=44, 
		DROP=45, ENABLE=46, EQ=47, NEQ=48, EVENT=49, EXISTS=50, EXPLAIN=51, EXTENDED=52, 
		FROM=53, FUNCTION=54, FUNCTIONS=55, GRANT=56, GROUP=57, GROUPS=58, IDENTIFIED=59, 
		IF=60, IN=61, INSERT=62, INTO=63, LIKE=64, MOUNT=65, NOT=66, ON=67, OPTION=68, 
		OPTIONS=69, OR=70, ORG=71, ORGANIZATION=72, OVERWRITE=73, PLAN=74, REMOVE=75, 
		RENAME=76, REPLACE=77, REVOKE=78, SA=79, SCHEDULE=80, SELECT=81, SET=82, 
		SHOW=83, STAR=84, STREAM=85, SYSINFO=86, TABLE=87, TABLES=88, TEMP=89, 
		TEMPORARY=90, TO=91, TYPE=92, UNMOUNT=93, USE=94, USING=95, USER=96, USERS=97, 
		VIEW=98, VIEWS=99, WITH=100, STRING=101, INTEGER_VALUE=102, IDENTIFIER=103, 
		SIMPLE_COMMENT=104, BRACKETED_COMMENT=105, WS=106, UNRECOGNIZED=107, DELIMITER=108;
	public static final int
		RULE_single = 0, RULE_mql = 1, RULE_appCmds = 2, RULE_definer = 3, RULE_schedule = 4, 
		RULE_starOrInteger = 5, RULE_query = 6, RULE_ctes = 7, RULE_dataType = 8, 
		RULE_colTypeList = 9, RULE_colType = 10, RULE_complexColTypeList = 11, 
		RULE_complexColType = 12, RULE_namedQuery = 13, RULE_mountTableList = 14, 
		RULE_mountTableOptions = 15, RULE_privilegeList = 16, RULE_privilege = 17, 
		RULE_qualifiedColumnList = 18, RULE_columnIdentifier = 19, RULE_identifierStarList = 20, 
		RULE_addUser = 21, RULE_removeUser = 22, RULE_identifierList = 23, RULE_funcIdentifier = 24, 
		RULE_tableIdentifier = 25, RULE_propertyList = 26, RULE_property = 27, 
		RULE_propertyKey = 28, RULE_password = 29, RULE_identifier = 30, RULE_resource = 31, 
		RULE_nonReserved = 32;
	public static final String[] ruleNames = {
		"single", "mql", "appCmds", "definer", "schedule", "starOrInteger", "query", 
		"ctes", "dataType", "colTypeList", "colType", "complexColTypeList", "complexColType", 
		"namedQuery", "mountTableList", "mountTableOptions", "privilegeList", 
		"privilege", "qualifiedColumnList", "columnIdentifier", "identifierStarList", 
		"addUser", "removeUser", "identifierList", "funcIdentifier", "tableIdentifier", 
		"propertyList", "property", "propertyKey", "password", "identifier", "resource", 
		"nonReserved"
	};

	private static final String[] _LITERAL_NAMES = {
		null, "'('", "')'", "','", "';'", "'<'", "'>'", "':'", "'.'", "'['", "']'", 
		"'{'", "'}'", "'ACCOUNT'", "'ADD'", "'ALTER'", "'APPLICATION'", "'APPLICATIONS'", 
		"'ARRAY'", "'AT'", "'MAP'", "'STRUCT'", "'AS'", "'BY'", "'CACHE'", "'CALL'", 
		"'CASCADE'", "'COLUMN'", "'COLUMNS'", "'COMMENT'", "'CHANGE'", "'CREATE'", 
		"'CURRENT_USER'", "'DATABASE'", "'DATABASES'", "'DATASOURCE'", "'DATASOURCES'", 
		"'DDL'", "'DEFINER'", "'DESC'", "'DESCRIBE'", "'DISABLE'", "'DO'", "'DML'", 
		"'DMLON'", "'DROP'", "'ENABLE'", null, "'<>'", "'EVENT'", "'EXISTS'", 
		"'EXPLAIN'", "'EXTENDED'", "'FROM'", "'FUNCTION'", "'FUNCTIONS'", "'GRANT'", 
		"'GROUP'", "'GROUPS'", "'IDENTIFIED '", "'IF'", "'IN'", "'INSERT'", "'INTO'", 
		"'LIKE'", "'MOUNT'", "'NOT'", "'ON'", "'OPTION'", "'OPTIONS'", "'OR'", 
		"'ORG'", "'ORGANIZATION'", "'OVERWRITE'", "'PLAN'", "'REMOVE'", "'RENAME'", 
		"'REPLACE'", "'REVOKE'", "'SA'", "'SCHEDULE'", "'SELECT'", "'SET'", "'SHOW'", 
		"'*'", "'STREAM'", "'SYSINFO'", "'TABLE'", "'TABLES'", "'TEMP'", "'TEMPORARY'", 
		"'TO'", "'TYPE'", "'UNMOUNT'", "'USE'", "'USING'", "'USER'", "'USERS'", 
		"'VIEW'", "'VIEWS'", "'WITH'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, "ACCOUNT", "ADD", "ALTER", "APPLICATION", "APPLICATIONS", "ARRAY", 
		"AT", "MAP", "STRUCT", "AS", "BY", "CACHE", "CALL", "CASCADE", "COLUMN", 
		"COLUMNS", "COMMENT", "CHANGE", "CREATE", "CURRENT_USER", "DATABASE", 
		"DATABASES", "DATASOURCE", "DATASOURCES", "DDL", "DEFINER", "DESC", "DESCRIBE", 
		"DISABLE", "DO", "DML", "DMLON", "DROP", "ENABLE", "EQ", "NEQ", "EVENT", 
		"EXISTS", "EXPLAIN", "EXTENDED", "FROM", "FUNCTION", "FUNCTIONS", "GRANT", 
		"GROUP", "GROUPS", "IDENTIFIED", "IF", "IN", "INSERT", "INTO", "LIKE", 
		"MOUNT", "NOT", "ON", "OPTION", "OPTIONS", "OR", "ORG", "ORGANIZATION", 
		"OVERWRITE", "PLAN", "REMOVE", "RENAME", "REPLACE", "REVOKE", "SA", "SCHEDULE", 
		"SELECT", "SET", "SHOW", "STAR", "STREAM", "SYSINFO", "TABLE", "TABLES", 
		"TEMP", "TEMPORARY", "TO", "TYPE", "UNMOUNT", "USE", "USING", "USER", 
		"USERS", "VIEW", "VIEWS", "WITH", "STRING", "INTEGER_VALUE", "IDENTIFIER", 
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
		public TerminalNode INSERT() { return getToken(MqlBaseParser.INSERT, 0); }
		public TableIdentifierContext tableIdentifier() {
			return getRuleContext(TableIdentifierContext.class,0);
		}
		public QueryContext query() {
			return getRuleContext(QueryContext.class,0);
		}
		public TerminalNode INTO() { return getToken(MqlBaseParser.INTO, 0); }
		public TerminalNode OVERWRITE() { return getToken(MqlBaseParser.OVERWRITE, 0); }
		public TerminalNode TABLE() { return getToken(MqlBaseParser.TABLE, 0); }
		public TerminalNode AS() { return getToken(MqlBaseParser.AS, 0); }
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
		public TerminalNode ORG() { return getToken(MqlBaseParser.ORG, 0); }
		public TerminalNode ORGANIZATION() { return getToken(MqlBaseParser.ORGANIZATION, 0); }
		public PasswordContext password() {
			return getRuleContext(PasswordContext.class,0);
		}
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
			setState(851);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,64,_ctx) ) {
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
				_la = _input.LA(1);
				if (_la==IF) {
					{
					setState(71);
					match(IF);
					setState(72);
					match(NOT);
					setState(73);
					match(EXISTS);
					}
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

				}
				break;
			case 2:
				_localctx = new RenameOrganizationContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(81);
				match(RENAME);
				setState(82);
				_la = _input.LA(1);
				if ( !(_la==ORG || _la==ORGANIZATION) ) {
				_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(83);
				((RenameOrganizationContext)_localctx).name = identifier();
				setState(84);
				match(TO);
				setState(85);
				((RenameOrganizationContext)_localctx).newName = identifier();
				}
				break;
			case 3:
				_localctx = new SetOrganizationNameContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(87);
				match(ALTER);
				setState(88);
				_la = _input.LA(1);
				if ( !(_la==ORG || _la==ORGANIZATION) ) {
				_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(89);
				((SetOrganizationNameContext)_localctx).name = identifier();
				setState(90);
				match(RENAME);
				setState(91);
				match(TO);
				setState(92);
				((SetOrganizationNameContext)_localctx).newName = identifier();
				}
				break;
			case 4:
				_localctx = new SetOrganizationCommentContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(94);
				match(ALTER);
				setState(95);
				_la = _input.LA(1);
				if ( !(_la==ORG || _la==ORGANIZATION) ) {
				_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(96);
				((SetOrganizationCommentContext)_localctx).name = identifier();
				setState(97);
				match(SET);
				setState(98);
				match(COMMENT);
				setState(99);
				((SetOrganizationCommentContext)_localctx).comment = match(STRING);
				}
				break;
			case 5:
				_localctx = new DropOrganizationContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(101);
				match(DROP);
				setState(102);
				_la = _input.LA(1);
				if ( !(_la==ORG || _la==ORGANIZATION) ) {
				_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(105);
				_la = _input.LA(1);
				if (_la==IF) {
					{
					setState(103);
					match(IF);
					setState(104);
					match(EXISTS);
					}
				}

				setState(107);
				((DropOrganizationContext)_localctx).name = identifier();
				setState(109);
				_la = _input.LA(1);
				if (_la==CASCADE) {
					{
					setState(108);
					match(CASCADE);
					}
				}

				}
				break;
			case 6:
				_localctx = new CreateSaContext(_localctx);
				enterOuterAlt(_localctx, 6);
				{
				setState(111);
				match(CREATE);
				setState(112);
				match(SA);
				setState(116);
				_la = _input.LA(1);
				if (_la==IF) {
					{
					setState(113);
					match(IF);
					setState(114);
					match(NOT);
					setState(115);
					match(EXISTS);
					}
				}

				setState(118);
				((CreateSaContext)_localctx).name = identifier();
				setState(119);
				match(IN);
				setState(120);
				_la = _input.LA(1);
				if ( !(_la==ORG || _la==ORGANIZATION) ) {
				_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(121);
				((CreateSaContext)_localctx).org = identifier();
				setState(122);
				match(IDENTIFIED);
				setState(123);
				match(BY);
				setState(124);
				((CreateSaContext)_localctx).pwd = password();
				}
				break;
			case 7:
				_localctx = new RenameSaContext(_localctx);
				enterOuterAlt(_localctx, 7);
				{
				setState(126);
				match(RENAME);
				setState(127);
				match(SA);
				setState(128);
				((RenameSaContext)_localctx).name = identifier();
				setState(129);
				match(IN);
				setState(130);
				_la = _input.LA(1);
				if ( !(_la==ORG || _la==ORGANIZATION) ) {
				_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(131);
				((RenameSaContext)_localctx).org = identifier();
				setState(132);
				match(TO);
				setState(133);
				((RenameSaContext)_localctx).newName = identifier();
				}
				break;
			case 8:
				_localctx = new SetSaNameContext(_localctx);
				enterOuterAlt(_localctx, 8);
				{
				setState(135);
				match(ALTER);
				setState(136);
				match(SA);
				setState(137);
				((SetSaNameContext)_localctx).name = identifier();
				setState(138);
				match(IN);
				setState(139);
				_la = _input.LA(1);
				if ( !(_la==ORG || _la==ORGANIZATION) ) {
				_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(140);
				((SetSaNameContext)_localctx).org = identifier();
				setState(141);
				match(RENAME);
				setState(142);
				match(TO);
				setState(143);
				((SetSaNameContext)_localctx).newName = identifier();
				}
				break;
			case 9:
				_localctx = new SetSaPasswordContext(_localctx);
				enterOuterAlt(_localctx, 9);
				{
				setState(145);
				match(ALTER);
				setState(146);
				match(SA);
				setState(147);
				((SetSaPasswordContext)_localctx).name = identifier();
				setState(148);
				match(IN);
				setState(149);
				_la = _input.LA(1);
				if ( !(_la==ORG || _la==ORGANIZATION) ) {
				_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(150);
				((SetSaPasswordContext)_localctx).org = identifier();
				setState(151);
				match(IDENTIFIED);
				setState(152);
				match(BY);
				setState(153);
				((SetSaPasswordContext)_localctx).pwd = password();
				}
				break;
			case 10:
				_localctx = new DropSaContext(_localctx);
				enterOuterAlt(_localctx, 10);
				{
				setState(155);
				match(DROP);
				setState(156);
				match(SA);
				setState(159);
				_la = _input.LA(1);
				if (_la==IF) {
					{
					setState(157);
					match(IF);
					setState(158);
					match(EXISTS);
					}
				}

				setState(161);
				((DropSaContext)_localctx).name = identifier();
				setState(162);
				match(IN);
				setState(163);
				_la = _input.LA(1);
				if ( !(_la==ORG || _la==ORGANIZATION) ) {
				_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(164);
				((DropSaContext)_localctx).org = identifier();
				}
				break;
			case 11:
				_localctx = new GrantGrantToUserContext(_localctx);
				enterOuterAlt(_localctx, 11);
				{
				setState(166);
				match(GRANT);
				setState(167);
				match(GRANT);
				setState(168);
				match(OPTION);
				setState(169);
				privilegeList();
				setState(170);
				match(TO);
				setState(171);
				match(USER);
				setState(172);
				((GrantGrantToUserContext)_localctx).users = identifierList();
				}
				break;
			case 12:
				_localctx = new GrantGrantToGroupContext(_localctx);
				enterOuterAlt(_localctx, 12);
				{
				setState(174);
				match(GRANT);
				setState(175);
				match(GRANT);
				setState(176);
				match(OPTION);
				setState(177);
				privilegeList();
				setState(178);
				match(TO);
				setState(179);
				match(GROUP);
				setState(180);
				((GrantGrantToGroupContext)_localctx).groups = identifierList();
				}
				break;
			case 13:
				_localctx = new RevokeGrantFromUserContext(_localctx);
				enterOuterAlt(_localctx, 13);
				{
				setState(182);
				match(REVOKE);
				setState(183);
				match(GRANT);
				setState(184);
				match(OPTION);
				setState(185);
				privilegeList();
				setState(186);
				match(FROM);
				setState(187);
				match(USER);
				setState(188);
				((RevokeGrantFromUserContext)_localctx).users = identifierList();
				}
				break;
			case 14:
				_localctx = new RevokeGrantFromGroupContext(_localctx);
				enterOuterAlt(_localctx, 14);
				{
				setState(190);
				match(REVOKE);
				setState(191);
				match(GRANT);
				setState(192);
				match(OPTION);
				setState(193);
				privilegeList();
				setState(194);
				match(FROM);
				setState(195);
				match(GROUP);
				setState(196);
				((RevokeGrantFromGroupContext)_localctx).groups = identifierList();
				}
				break;
			case 15:
				_localctx = new GrantAccountToUsersContext(_localctx);
				enterOuterAlt(_localctx, 15);
				{
				setState(198);
				match(GRANT);
				setState(199);
				match(ACCOUNT);
				setState(200);
				match(TO);
				setState(201);
				match(USER);
				setState(202);
				((GrantAccountToUsersContext)_localctx).users = identifierList();
				}
				break;
			case 16:
				_localctx = new GrantAccountToGroupsContext(_localctx);
				enterOuterAlt(_localctx, 16);
				{
				setState(203);
				match(GRANT);
				setState(204);
				match(ACCOUNT);
				setState(205);
				match(TO);
				setState(206);
				match(GROUP);
				setState(207);
				((GrantAccountToGroupsContext)_localctx).groups = identifierList();
				}
				break;
			case 17:
				_localctx = new RevokeAccountFromUsersContext(_localctx);
				enterOuterAlt(_localctx, 17);
				{
				setState(208);
				match(REVOKE);
				setState(209);
				match(ACCOUNT);
				setState(210);
				match(FROM);
				setState(211);
				match(USER);
				setState(212);
				((RevokeAccountFromUsersContext)_localctx).users = identifierList();
				}
				break;
			case 18:
				_localctx = new RevokeAccountFromGroupsContext(_localctx);
				enterOuterAlt(_localctx, 18);
				{
				setState(213);
				match(REVOKE);
				setState(214);
				match(ACCOUNT);
				setState(215);
				match(FROM);
				setState(216);
				match(GROUP);
				setState(217);
				((RevokeAccountFromGroupsContext)_localctx).groups = identifierList();
				}
				break;
			case 19:
				_localctx = new GrantDdlToUsersContext(_localctx);
				enterOuterAlt(_localctx, 19);
				{
				setState(218);
				match(GRANT);
				setState(219);
				match(DDL);
				setState(220);
				match(TO);
				setState(221);
				match(USER);
				setState(222);
				((GrantDdlToUsersContext)_localctx).users = identifierList();
				}
				break;
			case 20:
				_localctx = new GrantDdlToGroupsContext(_localctx);
				enterOuterAlt(_localctx, 20);
				{
				setState(223);
				match(GRANT);
				setState(224);
				match(DDL);
				setState(225);
				match(TO);
				setState(226);
				match(GROUP);
				setState(227);
				((GrantDdlToGroupsContext)_localctx).groups = identifierList();
				}
				break;
			case 21:
				_localctx = new RevokeDdlFromUsersContext(_localctx);
				enterOuterAlt(_localctx, 21);
				{
				setState(228);
				match(REVOKE);
				setState(229);
				match(DDL);
				setState(230);
				match(FROM);
				setState(231);
				match(USER);
				setState(232);
				((RevokeDdlFromUsersContext)_localctx).users = identifierList();
				}
				break;
			case 22:
				_localctx = new RevokeDdlFromGroupsContext(_localctx);
				enterOuterAlt(_localctx, 22);
				{
				setState(233);
				match(REVOKE);
				setState(234);
				match(DDL);
				setState(235);
				match(FROM);
				setState(236);
				match(GROUP);
				setState(237);
				((RevokeDdlFromGroupsContext)_localctx).groups = identifierList();
				}
				break;
			case 23:
				_localctx = new GrantDmlOnToUsersContext(_localctx);
				enterOuterAlt(_localctx, 23);
				{
				setState(238);
				match(GRANT);
				setState(239);
				match(DML);
				setState(240);
				match(ON);
				setState(241);
				qualifiedColumnList();
				setState(242);
				match(TO);
				setState(243);
				match(USER);
				setState(244);
				((GrantDmlOnToUsersContext)_localctx).users = identifierList();
				}
				break;
			case 24:
				_localctx = new GrantDmlOnToGroupsContext(_localctx);
				enterOuterAlt(_localctx, 24);
				{
				setState(246);
				match(GRANT);
				setState(247);
				match(DML);
				setState(248);
				match(ON);
				setState(249);
				qualifiedColumnList();
				setState(250);
				match(TO);
				setState(251);
				match(GROUP);
				setState(252);
				((GrantDmlOnToGroupsContext)_localctx).groups = identifierList();
				}
				break;
			case 25:
				_localctx = new RevokeDmlOnFromUsersContext(_localctx);
				enterOuterAlt(_localctx, 25);
				{
				setState(254);
				match(REVOKE);
				setState(255);
				match(DML);
				setState(256);
				match(ON);
				setState(257);
				qualifiedColumnList();
				setState(258);
				match(FROM);
				setState(259);
				match(USER);
				setState(260);
				((RevokeDmlOnFromUsersContext)_localctx).users = identifierList();
				}
				break;
			case 26:
				_localctx = new RevokeDmlOnFromGroupsContext(_localctx);
				enterOuterAlt(_localctx, 26);
				{
				setState(262);
				match(REVOKE);
				setState(263);
				match(DML);
				setState(264);
				match(ON);
				setState(265);
				qualifiedColumnList();
				setState(266);
				match(FROM);
				setState(267);
				match(GROUP);
				setState(268);
				((RevokeDmlOnFromGroupsContext)_localctx).groups = identifierList();
				}
				break;
			case 27:
				_localctx = new CreateUserContext(_localctx);
				enterOuterAlt(_localctx, 27);
				{
				setState(270);
				match(CREATE);
				setState(271);
				match(USER);
				setState(275);
				_la = _input.LA(1);
				if (_la==IF) {
					{
					setState(272);
					match(IF);
					setState(273);
					match(NOT);
					setState(274);
					match(EXISTS);
					}
				}

				setState(277);
				((CreateUserContext)_localctx).name = identifier();
				setState(278);
				match(IDENTIFIED);
				setState(279);
				match(BY);
				setState(280);
				((CreateUserContext)_localctx).pwd = password();
				}
				break;
			case 28:
				_localctx = new RenameUserContext(_localctx);
				enterOuterAlt(_localctx, 28);
				{
				setState(282);
				match(RENAME);
				setState(283);
				match(USER);
				setState(284);
				((RenameUserContext)_localctx).name = identifier();
				setState(285);
				match(TO);
				setState(286);
				((RenameUserContext)_localctx).newName = identifier();
				}
				break;
			case 29:
				_localctx = new SetUserNameContext(_localctx);
				enterOuterAlt(_localctx, 29);
				{
				setState(288);
				match(ALTER);
				setState(289);
				match(USER);
				setState(290);
				((SetUserNameContext)_localctx).name = identifier();
				setState(291);
				match(RENAME);
				setState(292);
				match(TO);
				setState(293);
				((SetUserNameContext)_localctx).newName = identifier();
				}
				break;
			case 30:
				_localctx = new SetUserPasswordContext(_localctx);
				enterOuterAlt(_localctx, 30);
				{
				setState(295);
				match(ALTER);
				setState(296);
				match(USER);
				setState(297);
				((SetUserPasswordContext)_localctx).name = identifier();
				setState(298);
				match(IDENTIFIED);
				setState(299);
				match(BY);
				setState(300);
				((SetUserPasswordContext)_localctx).pwd = password();
				}
				break;
			case 31:
				_localctx = new DropUserContext(_localctx);
				enterOuterAlt(_localctx, 31);
				{
				setState(302);
				match(DROP);
				setState(303);
				match(USER);
				setState(306);
				_la = _input.LA(1);
				if (_la==IF) {
					{
					setState(304);
					match(IF);
					setState(305);
					match(EXISTS);
					}
				}

				setState(308);
				((DropUserContext)_localctx).name = identifier();
				}
				break;
			case 32:
				_localctx = new CreateGroupContext(_localctx);
				enterOuterAlt(_localctx, 32);
				{
				setState(309);
				match(CREATE);
				setState(310);
				match(GROUP);
				setState(314);
				_la = _input.LA(1);
				if (_la==IF) {
					{
					setState(311);
					match(IF);
					setState(312);
					match(NOT);
					setState(313);
					match(EXISTS);
					}
				}

				setState(316);
				((CreateGroupContext)_localctx).name = identifier();
				setState(319);
				_la = _input.LA(1);
				if (_la==COMMENT) {
					{
					setState(317);
					match(COMMENT);
					setState(318);
					((CreateGroupContext)_localctx).comment = match(STRING);
					}
				}

				}
				break;
			case 33:
				_localctx = new RenameGroupContext(_localctx);
				enterOuterAlt(_localctx, 33);
				{
				setState(321);
				match(RENAME);
				setState(322);
				match(GROUP);
				setState(323);
				((RenameGroupContext)_localctx).name = identifier();
				setState(324);
				match(TO);
				setState(325);
				((RenameGroupContext)_localctx).newName = identifier();
				}
				break;
			case 34:
				_localctx = new SetGroupNameContext(_localctx);
				enterOuterAlt(_localctx, 34);
				{
				setState(327);
				match(ALTER);
				setState(328);
				match(GROUP);
				setState(329);
				((SetGroupNameContext)_localctx).name = identifier();
				setState(330);
				match(RENAME);
				setState(331);
				match(TO);
				setState(332);
				((SetGroupNameContext)_localctx).newName = identifier();
				}
				break;
			case 35:
				_localctx = new SetGroupCommentContext(_localctx);
				enterOuterAlt(_localctx, 35);
				{
				setState(334);
				match(ALTER);
				setState(335);
				match(GROUP);
				setState(336);
				((SetGroupCommentContext)_localctx).name = identifier();
				setState(337);
				match(SET);
				setState(338);
				match(COMMENT);
				setState(339);
				((SetGroupCommentContext)_localctx).comment = match(STRING);
				}
				break;
			case 36:
				_localctx = new AddUsersToGroupContext(_localctx);
				enterOuterAlt(_localctx, 36);
				{
				setState(341);
				match(ALTER);
				setState(342);
				match(GROUP);
				setState(343);
				((AddUsersToGroupContext)_localctx).name = identifier();
				setState(344);
				addUser();
				setState(346);
				_la = _input.LA(1);
				if (_la==REMOVE) {
					{
					setState(345);
					removeUser();
					}
				}

				}
				break;
			case 37:
				_localctx = new RemoveUsersFromGroupContext(_localctx);
				enterOuterAlt(_localctx, 37);
				{
				setState(348);
				match(ALTER);
				setState(349);
				match(GROUP);
				setState(350);
				((RemoveUsersFromGroupContext)_localctx).name = identifier();
				setState(351);
				removeUser();
				setState(353);
				_la = _input.LA(1);
				if (_la==ADD) {
					{
					setState(352);
					addUser();
					}
				}

				}
				break;
			case 38:
				_localctx = new DropGroupContext(_localctx);
				enterOuterAlt(_localctx, 38);
				{
				setState(355);
				match(DROP);
				setState(356);
				match(GROUP);
				setState(359);
				_la = _input.LA(1);
				if (_la==IF) {
					{
					setState(357);
					match(IF);
					setState(358);
					match(EXISTS);
					}
				}

				setState(361);
				((DropGroupContext)_localctx).name = identifier();
				setState(363);
				_la = _input.LA(1);
				if (_la==CASCADE) {
					{
					setState(362);
					match(CASCADE);
					}
				}

				}
				break;
			case 39:
				_localctx = new MountDatasourceContext(_localctx);
				enterOuterAlt(_localctx, 39);
				{
				setState(365);
				match(MOUNT);
				setState(366);
				match(DATASOURCE);
				setState(370);
				_la = _input.LA(1);
				if (_la==IF) {
					{
					setState(367);
					match(IF);
					setState(368);
					match(NOT);
					setState(369);
					match(EXISTS);
					}
				}

				setState(372);
				((MountDatasourceContext)_localctx).name = identifier();
				setState(373);
				match(OPTIONS);
				setState(374);
				propertyList();
				}
				break;
			case 40:
				_localctx = new RenameDatasourceContext(_localctx);
				enterOuterAlt(_localctx, 40);
				{
				setState(376);
				match(RENAME);
				setState(377);
				match(DATASOURCE);
				setState(378);
				((RenameDatasourceContext)_localctx).name = identifier();
				setState(379);
				match(TO);
				setState(380);
				((RenameDatasourceContext)_localctx).newName = identifier();
				}
				break;
			case 41:
				_localctx = new SetDatasourceNameContext(_localctx);
				enterOuterAlt(_localctx, 41);
				{
				setState(382);
				match(ALTER);
				setState(383);
				match(DATASOURCE);
				setState(384);
				((SetDatasourceNameContext)_localctx).name = identifier();
				setState(385);
				match(RENAME);
				setState(386);
				match(TO);
				setState(387);
				((SetDatasourceNameContext)_localctx).newName = identifier();
				}
				break;
			case 42:
				_localctx = new SetDatasourcePropertiesContext(_localctx);
				enterOuterAlt(_localctx, 42);
				{
				setState(389);
				match(ALTER);
				setState(390);
				match(DATASOURCE);
				setState(391);
				((SetDatasourcePropertiesContext)_localctx).name = identifier();
				setState(392);
				match(SET);
				setState(393);
				match(OPTIONS);
				setState(394);
				propertyList();
				}
				break;
			case 43:
				_localctx = new UnmountDatasourceContext(_localctx);
				enterOuterAlt(_localctx, 43);
				{
				setState(396);
				match(UNMOUNT);
				setState(397);
				match(DATASOURCE);
				setState(400);
				_la = _input.LA(1);
				if (_la==IF) {
					{
					setState(398);
					match(IF);
					setState(399);
					match(EXISTS);
					}
				}

				setState(402);
				((UnmountDatasourceContext)_localctx).name = identifier();
				}
				break;
			case 44:
				_localctx = new MountTableContext(_localctx);
				enterOuterAlt(_localctx, 44);
				{
				setState(403);
				match(MOUNT);
				setState(405);
				_la = _input.LA(1);
				if (_la==STREAM) {
					{
					setState(404);
					match(STREAM);
					}
				}

				setState(407);
				match(TABLE);
				setState(411);
				_la = _input.LA(1);
				if (_la==IF) {
					{
					setState(408);
					match(IF);
					setState(409);
					match(NOT);
					setState(410);
					match(EXISTS);
					}
				}

				setState(413);
				tableIdentifier();
				setState(418);
				_la = _input.LA(1);
				if (_la==T__0) {
					{
					setState(414);
					match(T__0);
					setState(415);
					((MountTableContext)_localctx).columns = colTypeList();
					setState(416);
					match(T__1);
					}
				}

				setState(420);
				match(OPTIONS);
				setState(421);
				propertyList();
				}
				break;
			case 45:
				_localctx = new MountTableWithDatasourceContext(_localctx);
				enterOuterAlt(_localctx, 45);
				{
				setState(423);
				match(WITH);
				setState(424);
				match(DATASOURCE);
				setState(425);
				((MountTableWithDatasourceContext)_localctx).ds = identifier();
				setState(426);
				match(MOUNT);
				setState(428);
				_la = _input.LA(1);
				if (_la==STREAM) {
					{
					setState(427);
					match(STREAM);
					}
				}

				setState(430);
				match(TABLE);
				setState(434);
				_la = _input.LA(1);
				if (_la==IF) {
					{
					setState(431);
					match(IF);
					setState(432);
					match(NOT);
					setState(433);
					match(EXISTS);
					}
				}

				setState(436);
				mountTableList();
				}
				break;
			case 46:
				_localctx = new RenameTableContext(_localctx);
				enterOuterAlt(_localctx, 46);
				{
				setState(438);
				match(RENAME);
				setState(439);
				match(TABLE);
				setState(440);
				((RenameTableContext)_localctx).name = tableIdentifier();
				setState(441);
				match(TO);
				setState(442);
				((RenameTableContext)_localctx).newName = tableIdentifier();
				}
				break;
			case 47:
				_localctx = new SetTableNameContext(_localctx);
				enterOuterAlt(_localctx, 47);
				{
				setState(444);
				match(ALTER);
				setState(445);
				match(TABLE);
				setState(446);
				((SetTableNameContext)_localctx).name = tableIdentifier();
				setState(447);
				match(RENAME);
				setState(448);
				match(TO);
				setState(449);
				((SetTableNameContext)_localctx).newName = tableIdentifier();
				}
				break;
			case 48:
				_localctx = new SetTablePropertiesContext(_localctx);
				enterOuterAlt(_localctx, 48);
				{
				setState(451);
				match(ALTER);
				setState(452);
				match(TABLE);
				setState(453);
				((SetTablePropertiesContext)_localctx).name = tableIdentifier();
				setState(454);
				match(SET);
				setState(455);
				match(OPTIONS);
				setState(456);
				propertyList();
				}
				break;
			case 49:
				_localctx = new AddTableColumnsContext(_localctx);
				enterOuterAlt(_localctx, 49);
				{
				setState(458);
				match(ALTER);
				setState(459);
				match(TABLE);
				setState(460);
				((AddTableColumnsContext)_localctx).name = tableIdentifier();
				setState(461);
				match(ADD);
				setState(462);
				match(COLUMNS);
				{
				setState(463);
				match(T__0);
				setState(464);
				((AddTableColumnsContext)_localctx).columns = colTypeList();
				setState(465);
				match(T__1);
				}
				}
				break;
			case 50:
				_localctx = new ChangeTableColumnContext(_localctx);
				enterOuterAlt(_localctx, 50);
				{
				setState(467);
				match(ALTER);
				setState(468);
				match(TABLE);
				setState(469);
				((ChangeTableColumnContext)_localctx).name = tableIdentifier();
				setState(470);
				match(CHANGE);
				setState(472);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,21,_ctx) ) {
				case 1:
					{
					setState(471);
					match(COLUMN);
					}
					break;
				}
				setState(474);
				identifier();
				setState(475);
				colType();
				}
				break;
			case 51:
				_localctx = new DropTableColumnContext(_localctx);
				enterOuterAlt(_localctx, 51);
				{
				setState(477);
				match(ALTER);
				setState(478);
				match(TABLE);
				setState(479);
				((DropTableColumnContext)_localctx).name = tableIdentifier();
				setState(480);
				match(DROP);
				setState(481);
				match(COLUMN);
				setState(482);
				identifier();
				}
				break;
			case 52:
				_localctx = new UnmountTableContext(_localctx);
				enterOuterAlt(_localctx, 52);
				{
				setState(484);
				match(UNMOUNT);
				setState(485);
				match(TABLE);
				setState(488);
				_la = _input.LA(1);
				if (_la==IF) {
					{
					setState(486);
					match(IF);
					setState(487);
					match(EXISTS);
					}
				}

				setState(490);
				((UnmountTableContext)_localctx).name = tableIdentifier();
				}
				break;
			case 53:
				_localctx = new CreateDatabaseContext(_localctx);
				enterOuterAlt(_localctx, 53);
				{
				setState(491);
				match(CREATE);
				setState(492);
				match(DATABASE);
				setState(496);
				_la = _input.LA(1);
				if (_la==IF) {
					{
					setState(493);
					match(IF);
					setState(494);
					match(NOT);
					setState(495);
					match(EXISTS);
					}
				}

				setState(498);
				((CreateDatabaseContext)_localctx).name = identifier();
				setState(501);
				_la = _input.LA(1);
				if (_la==COMMENT) {
					{
					setState(499);
					match(COMMENT);
					setState(500);
					((CreateDatabaseContext)_localctx).comment = match(STRING);
					}
				}

				}
				break;
			case 54:
				_localctx = new RenameDatabaseContext(_localctx);
				enterOuterAlt(_localctx, 54);
				{
				setState(503);
				match(RENAME);
				setState(504);
				match(DATABASE);
				setState(505);
				((RenameDatabaseContext)_localctx).name = identifier();
				setState(506);
				match(TO);
				setState(507);
				((RenameDatabaseContext)_localctx).newName = identifier();
				}
				break;
			case 55:
				_localctx = new SetDatabaseNameContext(_localctx);
				enterOuterAlt(_localctx, 55);
				{
				setState(509);
				match(ALTER);
				setState(510);
				match(DATABASE);
				setState(511);
				((SetDatabaseNameContext)_localctx).name = identifier();
				setState(512);
				match(RENAME);
				setState(513);
				match(TO);
				setState(514);
				((SetDatabaseNameContext)_localctx).newName = identifier();
				}
				break;
			case 56:
				_localctx = new SetDatabaseCommentContext(_localctx);
				enterOuterAlt(_localctx, 56);
				{
				setState(516);
				match(ALTER);
				setState(517);
				match(DATABASE);
				setState(518);
				((SetDatabaseCommentContext)_localctx).name = identifier();
				setState(519);
				match(SET);
				setState(520);
				match(COMMENT);
				setState(521);
				((SetDatabaseCommentContext)_localctx).comment = match(STRING);
				}
				break;
			case 57:
				_localctx = new DropDatabaseContext(_localctx);
				enterOuterAlt(_localctx, 57);
				{
				setState(523);
				match(DROP);
				setState(524);
				match(DATABASE);
				setState(527);
				_la = _input.LA(1);
				if (_la==IF) {
					{
					setState(525);
					match(IF);
					setState(526);
					match(EXISTS);
					}
				}

				setState(529);
				((DropDatabaseContext)_localctx).name = identifier();
				setState(531);
				_la = _input.LA(1);
				if (_la==CASCADE) {
					{
					setState(530);
					match(CASCADE);
					}
				}

				}
				break;
			case 58:
				_localctx = new UseDatabaseContext(_localctx);
				enterOuterAlt(_localctx, 58);
				{
				setState(533);
				match(USE);
				setState(534);
				((UseDatabaseContext)_localctx).db = identifier();
				}
				break;
			case 59:
				_localctx = new CreateFunctionContext(_localctx);
				enterOuterAlt(_localctx, 59);
				{
				setState(535);
				match(CREATE);
				setState(537);
				_la = _input.LA(1);
				if (_la==TEMP || _la==TEMPORARY) {
					{
					setState(536);
					_la = _input.LA(1);
					if ( !(_la==TEMP || _la==TEMPORARY) ) {
					_errHandler.recoverInline(this);
					} else {
						consume();
					}
					}
				}

				setState(539);
				match(FUNCTION);
				setState(543);
				_la = _input.LA(1);
				if (_la==IF) {
					{
					setState(540);
					match(IF);
					setState(541);
					match(NOT);
					setState(542);
					match(EXISTS);
					}
				}

				setState(545);
				((CreateFunctionContext)_localctx).name = funcIdentifier();
				setState(546);
				match(AS);
				setState(547);
				((CreateFunctionContext)_localctx).className = match(STRING);
				setState(549);
				_la = _input.LA(1);
				if (_la==STRING) {
					{
					setState(548);
					((CreateFunctionContext)_localctx).methodName = match(STRING);
					}
				}

				setState(560);
				_la = _input.LA(1);
				if (_la==USING) {
					{
					setState(551);
					match(USING);
					setState(552);
					resource();
					setState(557);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==T__2) {
						{
						{
						setState(553);
						match(T__2);
						setState(554);
						resource();
						}
						}
						setState(559);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					}
				}

				}
				break;
			case 60:
				_localctx = new DropFunctionContext(_localctx);
				enterOuterAlt(_localctx, 60);
				{
				setState(562);
				match(DROP);
				setState(564);
				_la = _input.LA(1);
				if (_la==TEMP || _la==TEMPORARY) {
					{
					setState(563);
					_la = _input.LA(1);
					if ( !(_la==TEMP || _la==TEMPORARY) ) {
					_errHandler.recoverInline(this);
					} else {
						consume();
					}
					}
				}

				setState(566);
				match(FUNCTION);
				setState(569);
				_la = _input.LA(1);
				if (_la==IF) {
					{
					setState(567);
					match(IF);
					setState(568);
					match(EXISTS);
					}
				}

				setState(571);
				((DropFunctionContext)_localctx).name = funcIdentifier();
				}
				break;
			case 61:
				_localctx = new CreateViewContext(_localctx);
				enterOuterAlt(_localctx, 61);
				{
				setState(572);
				match(CREATE);
				setState(573);
				match(VIEW);
				setState(577);
				_la = _input.LA(1);
				if (_la==IF) {
					{
					setState(574);
					match(IF);
					setState(575);
					match(NOT);
					setState(576);
					match(EXISTS);
					}
				}

				setState(579);
				((CreateViewContext)_localctx).name = tableIdentifier();
				setState(582);
				_la = _input.LA(1);
				if (_la==COMMENT) {
					{
					setState(580);
					match(COMMENT);
					setState(581);
					((CreateViewContext)_localctx).comment = match(STRING);
					}
				}

				setState(584);
				match(AS);
				setState(585);
				query();
				}
				break;
			case 62:
				_localctx = new RenameViewContext(_localctx);
				enterOuterAlt(_localctx, 62);
				{
				setState(587);
				match(RENAME);
				setState(588);
				match(VIEW);
				setState(589);
				((RenameViewContext)_localctx).name = tableIdentifier();
				setState(590);
				match(TO);
				setState(591);
				((RenameViewContext)_localctx).newName = tableIdentifier();
				}
				break;
			case 63:
				_localctx = new SetViewNameContext(_localctx);
				enterOuterAlt(_localctx, 63);
				{
				setState(593);
				match(ALTER);
				setState(594);
				match(VIEW);
				setState(595);
				((SetViewNameContext)_localctx).name = tableIdentifier();
				setState(596);
				match(RENAME);
				setState(597);
				match(TO);
				setState(598);
				((SetViewNameContext)_localctx).newName = tableIdentifier();
				}
				break;
			case 64:
				_localctx = new SetViewCommentContext(_localctx);
				enterOuterAlt(_localctx, 64);
				{
				setState(600);
				match(ALTER);
				setState(601);
				match(VIEW);
				setState(602);
				((SetViewCommentContext)_localctx).name = tableIdentifier();
				setState(603);
				match(SET);
				setState(604);
				match(COMMENT);
				setState(605);
				((SetViewCommentContext)_localctx).comment = match(STRING);
				}
				break;
			case 65:
				_localctx = new SetViewQueryContext(_localctx);
				enterOuterAlt(_localctx, 65);
				{
				setState(607);
				match(ALTER);
				setState(608);
				match(VIEW);
				setState(609);
				((SetViewQueryContext)_localctx).name = tableIdentifier();
				setState(610);
				match(AS);
				setState(611);
				query();
				}
				break;
			case 66:
				_localctx = new DropViewContext(_localctx);
				enterOuterAlt(_localctx, 66);
				{
				setState(613);
				match(DROP);
				setState(614);
				match(VIEW);
				setState(617);
				_la = _input.LA(1);
				if (_la==IF) {
					{
					setState(615);
					match(IF);
					setState(616);
					match(EXISTS);
					}
				}

				setState(619);
				((DropViewContext)_localctx).name = tableIdentifier();
				}
				break;
			case 67:
				_localctx = new CreateApplicationContext(_localctx);
				enterOuterAlt(_localctx, 67);
				{
				setState(620);
				match(CREATE);
				setState(621);
				match(APPLICATION);
				setState(625);
				_la = _input.LA(1);
				if (_la==IF) {
					{
					setState(622);
					match(IF);
					setState(623);
					match(NOT);
					setState(624);
					match(EXISTS);
					}
				}

				setState(627);
				((CreateApplicationContext)_localctx).name = identifier();
				setState(628);
				match(AS);
				setState(629);
				appCmds();
				}
				break;
			case 68:
				_localctx = new RenameApplicationContext(_localctx);
				enterOuterAlt(_localctx, 68);
				{
				setState(631);
				match(RENAME);
				setState(632);
				match(APPLICATION);
				setState(633);
				((RenameApplicationContext)_localctx).name = identifier();
				setState(634);
				match(TO);
				setState(635);
				((RenameApplicationContext)_localctx).newName = identifier();
				}
				break;
			case 69:
				_localctx = new SetApplicationNameContext(_localctx);
				enterOuterAlt(_localctx, 69);
				{
				setState(637);
				match(ALTER);
				setState(638);
				match(APPLICATION);
				setState(639);
				((SetApplicationNameContext)_localctx).name = identifier();
				setState(640);
				match(RENAME);
				setState(641);
				match(TO);
				setState(642);
				((SetApplicationNameContext)_localctx).newName = identifier();
				}
				break;
			case 70:
				_localctx = new SetApplicationQuerysContext(_localctx);
				enterOuterAlt(_localctx, 70);
				{
				setState(644);
				match(ALTER);
				setState(645);
				match(APPLICATION);
				setState(646);
				((SetApplicationQuerysContext)_localctx).name = identifier();
				setState(647);
				match(AS);
				setState(648);
				appCmds();
				}
				break;
			case 71:
				_localctx = new DropApplicationContext(_localctx);
				enterOuterAlt(_localctx, 71);
				{
				setState(650);
				match(DROP);
				setState(651);
				match(APPLICATION);
				setState(654);
				_la = _input.LA(1);
				if (_la==IF) {
					{
					setState(652);
					match(IF);
					setState(653);
					match(EXISTS);
					}
				}

				setState(656);
				((DropApplicationContext)_localctx).name = identifier();
				}
				break;
			case 72:
				_localctx = new CreateEventContext(_localctx);
				enterOuterAlt(_localctx, 72);
				{
				setState(657);
				match(CREATE);
				setState(660);
				_la = _input.LA(1);
				if (_la==DEFINER) {
					{
					setState(658);
					match(DEFINER);
					setState(659);
					definer();
					}
				}

				setState(662);
				match(EVENT);
				setState(666);
				_la = _input.LA(1);
				if (_la==IF) {
					{
					setState(663);
					match(IF);
					setState(664);
					match(NOT);
					setState(665);
					match(EXISTS);
					}
				}

				setState(668);
				((CreateEventContext)_localctx).name = identifier();
				setState(669);
				match(ON);
				setState(670);
				match(SCHEDULE);
				setState(671);
				match(AT);
				setState(672);
				schedule();
				setState(674);
				_la = _input.LA(1);
				if (_la==DISABLE || _la==ENABLE) {
					{
					setState(673);
					_la = _input.LA(1);
					if ( !(_la==DISABLE || _la==ENABLE) ) {
					_errHandler.recoverInline(this);
					} else {
						consume();
					}
					}
				}

				setState(678);
				_la = _input.LA(1);
				if (_la==COMMENT) {
					{
					setState(676);
					match(COMMENT);
					setState(677);
					((CreateEventContext)_localctx).comment = match(STRING);
					}
				}

				setState(680);
				match(DO);
				setState(681);
				match(CALL);
				setState(682);
				((CreateEventContext)_localctx).app = identifier();
				}
				break;
			case 73:
				_localctx = new RenameEventContext(_localctx);
				enterOuterAlt(_localctx, 73);
				{
				setState(684);
				match(RENAME);
				setState(685);
				match(EVENT);
				setState(686);
				((RenameEventContext)_localctx).name = identifier();
				setState(687);
				match(TO);
				setState(688);
				((RenameEventContext)_localctx).newName = identifier();
				}
				break;
			case 74:
				_localctx = new SetDefinerContext(_localctx);
				enterOuterAlt(_localctx, 74);
				{
				setState(690);
				match(ALTER);
				setState(691);
				match(DEFINER);
				setState(692);
				definer();
				setState(693);
				match(EVENT);
				setState(694);
				((SetDefinerContext)_localctx).name = identifier();
				}
				break;
			case 75:
				_localctx = new SetEventNameContext(_localctx);
				enterOuterAlt(_localctx, 75);
				{
				setState(696);
				match(ALTER);
				setState(697);
				match(EVENT);
				setState(698);
				((SetEventNameContext)_localctx).name = identifier();
				setState(699);
				match(RENAME);
				setState(700);
				match(TO);
				setState(701);
				((SetEventNameContext)_localctx).newName = identifier();
				}
				break;
			case 76:
				_localctx = new SetEventScheduleContext(_localctx);
				enterOuterAlt(_localctx, 76);
				{
				setState(703);
				match(ALTER);
				setState(704);
				match(EVENT);
				setState(705);
				((SetEventScheduleContext)_localctx).name = identifier();
				setState(706);
				match(ON);
				setState(707);
				match(SCHEDULE);
				setState(708);
				match(AT);
				setState(709);
				schedule();
				}
				break;
			case 77:
				_localctx = new SetEventEnableContext(_localctx);
				enterOuterAlt(_localctx, 77);
				{
				setState(711);
				match(ALTER);
				setState(712);
				match(EVENT);
				setState(713);
				((SetEventEnableContext)_localctx).name = identifier();
				setState(714);
				_la = _input.LA(1);
				if ( !(_la==DISABLE || _la==ENABLE) ) {
				_errHandler.recoverInline(this);
				} else {
					consume();
				}
				}
				break;
			case 78:
				_localctx = new DropEventContext(_localctx);
				enterOuterAlt(_localctx, 78);
				{
				setState(716);
				match(DROP);
				setState(717);
				match(EVENT);
				setState(720);
				_la = _input.LA(1);
				if (_la==IF) {
					{
					setState(718);
					match(IF);
					setState(719);
					match(EXISTS);
					}
				}

				setState(722);
				((DropEventContext)_localctx).name = identifier();
				}
				break;
			case 79:
				_localctx = new ShowSysInfoContext(_localctx);
				enterOuterAlt(_localctx, 79);
				{
				setState(723);
				match(SHOW);
				setState(724);
				match(SYSINFO);
				}
				break;
			case 80:
				_localctx = new ShowDatasourcesContext(_localctx);
				enterOuterAlt(_localctx, 80);
				{
				setState(725);
				match(SHOW);
				setState(726);
				match(DATASOURCES);
				setState(729);
				_la = _input.LA(1);
				if (_la==LIKE) {
					{
					setState(727);
					match(LIKE);
					setState(728);
					((ShowDatasourcesContext)_localctx).pattern = match(STRING);
					}
				}

				}
				break;
			case 81:
				_localctx = new ShowDatabaseContext(_localctx);
				enterOuterAlt(_localctx, 81);
				{
				setState(731);
				match(SHOW);
				setState(732);
				match(DATABASES);
				setState(735);
				_la = _input.LA(1);
				if (_la==LIKE) {
					{
					setState(733);
					match(LIKE);
					setState(734);
					((ShowDatabaseContext)_localctx).pattern = match(STRING);
					}
				}

				}
				break;
			case 82:
				_localctx = new ShowTablesContext(_localctx);
				enterOuterAlt(_localctx, 82);
				{
				setState(737);
				match(SHOW);
				setState(738);
				match(TABLES);
				setState(741);
				_la = _input.LA(1);
				if (_la==FROM || _la==IN) {
					{
					setState(739);
					_la = _input.LA(1);
					if ( !(_la==FROM || _la==IN) ) {
					_errHandler.recoverInline(this);
					} else {
						consume();
					}
					setState(740);
					((ShowTablesContext)_localctx).db = identifier();
					}
				}

				setState(745);
				_la = _input.LA(1);
				if (_la==LIKE) {
					{
					setState(743);
					match(LIKE);
					setState(744);
					((ShowTablesContext)_localctx).pattern = match(STRING);
					}
				}

				}
				break;
			case 83:
				_localctx = new ShowViewsContext(_localctx);
				enterOuterAlt(_localctx, 83);
				{
				setState(747);
				match(SHOW);
				setState(748);
				match(VIEWS);
				setState(751);
				_la = _input.LA(1);
				if (_la==FROM || _la==IN) {
					{
					setState(749);
					_la = _input.LA(1);
					if ( !(_la==FROM || _la==IN) ) {
					_errHandler.recoverInline(this);
					} else {
						consume();
					}
					setState(750);
					((ShowViewsContext)_localctx).db = identifier();
					}
				}

				setState(755);
				_la = _input.LA(1);
				if (_la==LIKE) {
					{
					setState(753);
					match(LIKE);
					setState(754);
					((ShowViewsContext)_localctx).pattern = match(STRING);
					}
				}

				}
				break;
			case 84:
				_localctx = new ShowFunctionsContext(_localctx);
				enterOuterAlt(_localctx, 84);
				{
				setState(757);
				match(SHOW);
				setState(758);
				match(FUNCTIONS);
				setState(761);
				_la = _input.LA(1);
				if (_la==FROM || _la==IN) {
					{
					setState(759);
					_la = _input.LA(1);
					if ( !(_la==FROM || _la==IN) ) {
					_errHandler.recoverInline(this);
					} else {
						consume();
					}
					setState(760);
					((ShowFunctionsContext)_localctx).db = identifier();
					}
				}

				setState(765);
				_la = _input.LA(1);
				if (_la==LIKE) {
					{
					setState(763);
					match(LIKE);
					setState(764);
					((ShowFunctionsContext)_localctx).pattern = match(STRING);
					}
				}

				}
				break;
			case 85:
				_localctx = new ShowUsersContext(_localctx);
				enterOuterAlt(_localctx, 85);
				{
				setState(767);
				match(SHOW);
				setState(768);
				match(USERS);
				setState(771);
				_la = _input.LA(1);
				if (_la==LIKE) {
					{
					setState(769);
					match(LIKE);
					setState(770);
					((ShowUsersContext)_localctx).pattern = match(STRING);
					}
				}

				}
				break;
			case 86:
				_localctx = new ShowGroupsContext(_localctx);
				enterOuterAlt(_localctx, 86);
				{
				setState(773);
				match(SHOW);
				setState(774);
				match(GROUPS);
				setState(777);
				_la = _input.LA(1);
				if (_la==LIKE) {
					{
					setState(775);
					match(LIKE);
					setState(776);
					((ShowGroupsContext)_localctx).pattern = match(STRING);
					}
				}

				}
				break;
			case 87:
				_localctx = new ShowApplicationsContext(_localctx);
				enterOuterAlt(_localctx, 87);
				{
				setState(779);
				match(SHOW);
				setState(780);
				match(APPLICATIONS);
				setState(783);
				_la = _input.LA(1);
				if (_la==LIKE) {
					{
					setState(781);
					match(LIKE);
					setState(782);
					((ShowApplicationsContext)_localctx).pattern = match(STRING);
					}
				}

				}
				break;
			case 88:
				_localctx = new DescDatasourceContext(_localctx);
				enterOuterAlt(_localctx, 88);
				{
				setState(785);
				_la = _input.LA(1);
				if ( !(_la==DESC || _la==DESCRIBE) ) {
				_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(786);
				match(DATASOURCE);
				setState(788);
				_la = _input.LA(1);
				if (_la==EXTENDED) {
					{
					setState(787);
					match(EXTENDED);
					}
				}

				setState(790);
				((DescDatasourceContext)_localctx).name = identifier();
				}
				break;
			case 89:
				_localctx = new DescDatabaseContext(_localctx);
				enterOuterAlt(_localctx, 89);
				{
				setState(791);
				_la = _input.LA(1);
				if ( !(_la==DESC || _la==DESCRIBE) ) {
				_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(792);
				match(DATABASE);
				setState(793);
				((DescDatabaseContext)_localctx).name = identifier();
				}
				break;
			case 90:
				_localctx = new DescTableContext(_localctx);
				enterOuterAlt(_localctx, 90);
				{
				setState(794);
				_la = _input.LA(1);
				if ( !(_la==DESC || _la==DESCRIBE) ) {
				_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(795);
				match(TABLE);
				setState(797);
				_la = _input.LA(1);
				if (_la==EXTENDED) {
					{
					setState(796);
					match(EXTENDED);
					}
				}

				setState(799);
				tableIdentifier();
				}
				break;
			case 91:
				_localctx = new DescViewContext(_localctx);
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
				match(VIEW);
				setState(802);
				tableIdentifier();
				}
				break;
			case 92:
				_localctx = new DescFunctionContext(_localctx);
				enterOuterAlt(_localctx, 92);
				{
				setState(803);
				_la = _input.LA(1);
				if ( !(_la==DESC || _la==DESCRIBE) ) {
				_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(804);
				match(FUNCTION);
				setState(806);
				_la = _input.LA(1);
				if (_la==EXTENDED) {
					{
					setState(805);
					match(EXTENDED);
					}
				}

				setState(808);
				funcIdentifier();
				}
				break;
			case 93:
				_localctx = new DescUserContext(_localctx);
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
				match(USER);
				setState(811);
				((DescUserContext)_localctx).name = identifier();
				}
				break;
			case 94:
				_localctx = new DescGroupContext(_localctx);
				enterOuterAlt(_localctx, 94);
				{
				setState(812);
				_la = _input.LA(1);
				if ( !(_la==DESC || _la==DESCRIBE) ) {
				_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(813);
				match(GROUP);
				setState(814);
				((DescGroupContext)_localctx).name = identifier();
				}
				break;
			case 95:
				_localctx = new ExplainContext(_localctx);
				enterOuterAlt(_localctx, 95);
				{
				setState(815);
				match(EXPLAIN);
				setState(817);
				_la = _input.LA(1);
				if (_la==EXTENDED) {
					{
					setState(816);
					match(EXTENDED);
					}
				}

				setState(820);
				_la = _input.LA(1);
				if (_la==PLAN) {
					{
					setState(819);
					match(PLAN);
					}
				}

				setState(822);
				query();
				}
				break;
			case 96:
				_localctx = new SetConfigurationContext(_localctx);
				enterOuterAlt(_localctx, 96);
				{
				setState(823);
				match(SET);
				setState(824);
				property();
				}
				break;
			case 97:
				_localctx = new InsertIntoContext(_localctx);
				enterOuterAlt(_localctx, 97);
				{
				setState(825);
				match(INSERT);
				setState(826);
				_la = _input.LA(1);
				if ( !(_la==INTO || _la==OVERWRITE) ) {
				_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(828);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,60,_ctx) ) {
				case 1:
					{
					setState(827);
					match(TABLE);
					}
					break;
				}
				setState(830);
				tableIdentifier();
				setState(832);
				_la = _input.LA(1);
				if (_la==AS) {
					{
					setState(831);
					match(AS);
					}
				}

				setState(834);
				query();
				}
				break;
			case 98:
				_localctx = new CreateTemporaryViewContext(_localctx);
				enterOuterAlt(_localctx, 98);
				{
				setState(836);
				match(CREATE);
				setState(839);
				_la = _input.LA(1);
				if (_la==OR) {
					{
					setState(837);
					match(OR);
					setState(838);
					match(REPLACE);
					}
				}

				setState(842);
				_la = _input.LA(1);
				if (_la==CACHE) {
					{
					setState(841);
					match(CACHE);
					}
				}

				setState(844);
				_la = _input.LA(1);
				if ( !(_la==TEMP || _la==TEMPORARY) ) {
				_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(845);
				match(VIEW);
				setState(846);
				((CreateTemporaryViewContext)_localctx).name = identifier();
				setState(847);
				match(AS);
				setState(848);
				query();
				}
				break;
			case 99:
				_localctx = new MqlQueryContext(_localctx);
				enterOuterAlt(_localctx, 99);
				{
				setState(850);
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

	public static class AppCmdsContext extends ParserRuleContext {
		public List<MqlContext> mql() {
			return getRuleContexts(MqlContext.class);
		}
		public MqlContext mql(int i) {
			return getRuleContext(MqlContext.class,i);
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
		enterRule(_localctx, 4, RULE_appCmds);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(853);
			mql();
			setState(858);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,65,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(854);
					match(T__3);
					setState(855);
					mql();
					}
					} 
				}
				setState(860);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,65,_ctx);
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
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(862);
			_la = _input.LA(1);
			if (_la==EQ) {
				{
				setState(861);
				match(EQ);
				}
			}

			setState(866);
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
				{
				setState(864);
				((DefinerContext)_localctx).user = identifier();
				}
				break;
			case CURRENT_USER:
				{
				setState(865);
				match(CURRENT_USER);
				}
				break;
			default:
				throw new NoViableAltException(this);
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
		enterRule(_localctx, 8, RULE_schedule);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(868);
			starOrInteger();
			setState(869);
			starOrInteger();
			setState(870);
			starOrInteger();
			setState(871);
			starOrInteger();
			setState(872);
			starOrInteger();
			setState(873);
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
		enterRule(_localctx, 10, RULE_starOrInteger);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(875);
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
		enterRule(_localctx, 12, RULE_query);
		int _la;
		try {
			int _alt;
			setState(892);
			switch (_input.LA(1)) {
			case SELECT:
				enterOuterAlt(_localctx, 1);
				{
				setState(877);
				match(SELECT);
				setState(881);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,68,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(878);
						_la = _input.LA(1);
						if ( _la <= 0 || (_la==T__3) ) {
						_errHandler.recoverInline(this);
						} else {
							consume();
						}
						}
						} 
					}
					setState(883);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,68,_ctx);
				}
				}
				break;
			case WITH:
				enterOuterAlt(_localctx, 2);
				{
				setState(884);
				ctes();
				setState(885);
				match(SELECT);
				setState(889);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,69,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(886);
						_la = _input.LA(1);
						if ( _la <= 0 || (_la==T__3) ) {
						_errHandler.recoverInline(this);
						} else {
							consume();
						}
						}
						} 
					}
					setState(891);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,69,_ctx);
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
		enterRule(_localctx, 14, RULE_ctes);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(894);
			match(WITH);
			setState(895);
			namedQuery();
			setState(900);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__2) {
				{
				{
				setState(896);
				match(T__2);
				setState(897);
				namedQuery();
				}
				}
				setState(902);
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
		enterRule(_localctx, 16, RULE_dataType);
		int _la;
		try {
			setState(937);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,76,_ctx) ) {
			case 1:
				_localctx = new ComplexDataTypeContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(903);
				((ComplexDataTypeContext)_localctx).complex = match(ARRAY);
				setState(904);
				match(T__4);
				setState(905);
				dataType();
				setState(906);
				match(T__5);
				}
				break;
			case 2:
				_localctx = new ComplexDataTypeContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(908);
				((ComplexDataTypeContext)_localctx).complex = match(MAP);
				setState(909);
				match(T__4);
				setState(910);
				dataType();
				setState(911);
				match(T__2);
				setState(912);
				dataType();
				setState(913);
				match(T__5);
				}
				break;
			case 3:
				_localctx = new ComplexDataTypeContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(915);
				((ComplexDataTypeContext)_localctx).complex = match(STRUCT);
				setState(922);
				switch (_input.LA(1)) {
				case T__4:
					{
					setState(916);
					match(T__4);
					setState(918);
					_la = _input.LA(1);
					if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ACCOUNT) | (1L << ADD) | (1L << ALTER) | (1L << APPLICATION) | (1L << APPLICATIONS) | (1L << ARRAY) | (1L << AS) | (1L << CACHE) | (1L << CASCADE) | (1L << COLUMN) | (1L << COLUMNS) | (1L << DATABASE) | (1L << DATABASES) | (1L << DATASOURCE) | (1L << DATASOURCES) | (1L << FUNCTION) | (1L << FUNCTIONS) | (1L << GRANT) | (1L << GROUP) | (1L << GROUPS))) != 0) || ((((_la - 71)) & ~0x3f) == 0 && ((1L << (_la - 71)) & ((1L << (ORG - 71)) | (1L << (REMOVE - 71)) | (1L << (RENAME - 71)) | (1L << (REVOKE - 71)) | (1L << (SA - 71)) | (1L << (SET - 71)) | (1L << (SHOW - 71)) | (1L << (TABLE - 71)) | (1L << (TABLES - 71)) | (1L << (TO - 71)) | (1L << (TYPE - 71)) | (1L << (USER - 71)) | (1L << (VIEW - 71)) | (1L << (VIEWS - 71)) | (1L << (WITH - 71)) | (1L << (IDENTIFIER - 71)))) != 0)) {
						{
						setState(917);
						complexColTypeList();
						}
					}

					setState(920);
					match(T__5);
					}
					break;
				case NEQ:
					{
					setState(921);
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
				setState(924);
				identifier();
				setState(935);
				_la = _input.LA(1);
				if (_la==T__0) {
					{
					setState(925);
					match(T__0);
					setState(926);
					match(INTEGER_VALUE);
					setState(931);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==T__2) {
						{
						{
						setState(927);
						match(T__2);
						setState(928);
						match(INTEGER_VALUE);
						}
						}
						setState(933);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					setState(934);
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
			setState(939);
			colType();
			setState(944);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__2) {
				{
				{
				setState(940);
				match(T__2);
				setState(941);
				colType();
				}
				}
				setState(946);
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
			setState(947);
			identifier();
			setState(948);
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
			setState(950);
			complexColType();
			setState(955);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__2) {
				{
				{
				setState(951);
				match(T__2);
				setState(952);
				complexColType();
				}
				}
				setState(957);
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
			setState(958);
			identifier();
			setState(959);
			match(T__6);
			setState(960);
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
			setState(962);
			((NamedQueryContext)_localctx).name = identifier();
			setState(964);
			_la = _input.LA(1);
			if (_la==AS) {
				{
				setState(963);
				match(AS);
				}
			}

			setState(966);
			match(T__0);
			setState(967);
			query();
			setState(968);
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
		enterRule(_localctx, 28, RULE_mountTableList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(970);
			mountTableOptions();
			setState(975);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__2) {
				{
				{
				setState(971);
				match(T__2);
				setState(972);
				mountTableOptions();
				}
				}
				setState(977);
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
		enterRule(_localctx, 30, RULE_mountTableOptions);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(978);
			tableIdentifier();
			setState(983);
			_la = _input.LA(1);
			if (_la==T__0) {
				{
				setState(979);
				match(T__0);
				setState(980);
				((MountTableOptionsContext)_localctx).columns = colTypeList();
				setState(981);
				match(T__1);
				}
			}

			setState(985);
			match(OPTIONS);
			setState(986);
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
		enterRule(_localctx, 32, RULE_privilegeList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(988);
			privilege();
			setState(993);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__2) {
				{
				{
				setState(989);
				match(T__2);
				setState(990);
				privilege();
				}
				}
				setState(995);
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
		enterRule(_localctx, 34, RULE_privilege);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(996);
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
		enterRule(_localctx, 36, RULE_qualifiedColumnList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(998);
			columnIdentifier();
			setState(1003);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__2) {
				{
				{
				setState(999);
				match(T__2);
				setState(1000);
				columnIdentifier();
				}
				}
				setState(1005);
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
		enterRule(_localctx, 38, RULE_columnIdentifier);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1009);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,84,_ctx) ) {
			case 1:
				{
				setState(1006);
				((ColumnIdentifierContext)_localctx).db = identifier();
				setState(1007);
				match(T__7);
				}
				break;
			}
			setState(1011);
			((ColumnIdentifierContext)_localctx).table = identifierStarList();
			setState(1012);
			match(T__7);
			setState(1013);
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
		enterRule(_localctx, 40, RULE_identifierStarList);
		int _la;
		try {
			setState(1039);
			switch (_input.LA(1)) {
			case T__8:
				enterOuterAlt(_localctx, 1);
				{
				setState(1015);
				match(T__8);
				setState(1016);
				identifier();
				setState(1021);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__2) {
					{
					{
					setState(1017);
					match(T__2);
					setState(1018);
					identifier();
					}
					}
					setState(1023);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(1024);
				match(T__9);
				}
				break;
			case T__10:
				enterOuterAlt(_localctx, 2);
				{
				setState(1026);
				match(T__10);
				setState(1027);
				identifier();
				setState(1032);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__2) {
					{
					{
					setState(1028);
					match(T__2);
					setState(1029);
					identifier();
					}
					}
					setState(1034);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(1035);
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
				setState(1037);
				identifier();
				}
				break;
			case STAR:
				enterOuterAlt(_localctx, 4);
				{
				setState(1038);
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
		enterRule(_localctx, 42, RULE_addUser);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1041);
			match(ADD);
			setState(1042);
			match(USER);
			setState(1043);
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
			setState(1045);
			match(REMOVE);
			setState(1046);
			match(USER);
			setState(1047);
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
			setState(1049);
			identifier();
			setState(1054);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__2) {
				{
				{
				setState(1050);
				match(T__2);
				setState(1051);
				identifier();
				}
				}
				setState(1056);
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
			setState(1060);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,89,_ctx) ) {
			case 1:
				{
				setState(1057);
				((FuncIdentifierContext)_localctx).db = identifier();
				setState(1058);
				match(T__7);
				}
				break;
			}
			setState(1062);
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
			setState(1067);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,90,_ctx) ) {
			case 1:
				{
				setState(1064);
				((TableIdentifierContext)_localctx).db = identifier();
				setState(1065);
				match(T__7);
				}
				break;
			}
			setState(1069);
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
			setState(1071);
			match(T__0);
			setState(1072);
			property();
			setState(1077);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__2) {
				{
				{
				setState(1073);
				match(T__2);
				setState(1074);
				property();
				}
				}
				setState(1079);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(1080);
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
			setState(1082);
			((PropertyContext)_localctx).key = propertyKey();
			setState(1087);
			_la = _input.LA(1);
			if (_la==EQ || _la==STRING) {
				{
				setState(1084);
				_la = _input.LA(1);
				if (_la==EQ) {
					{
					setState(1083);
					match(EQ);
					}
				}

				setState(1086);
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
		enterRule(_localctx, 56, RULE_propertyKey);
		int _la;
		try {
			setState(1098);
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
				setState(1089);
				identifier();
				setState(1094);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__7) {
					{
					{
					setState(1090);
					match(T__7);
					setState(1091);
					identifier();
					}
					}
					setState(1096);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
				break;
			case STRING:
				enterOuterAlt(_localctx, 2);
				{
				setState(1097);
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
			setState(1103);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,96,_ctx);
			while ( _alt!=1 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1+1 ) {
					{
					{
					setState(1100);
					matchWildcard();
					}
					} 
				}
				setState(1105);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,96,_ctx);
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
		enterRule(_localctx, 60, RULE_identifier);
		try {
			setState(1108);
			switch (_input.LA(1)) {
			case IDENTIFIER:
				enterOuterAlt(_localctx, 1);
				{
				setState(1106);
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
				setState(1107);
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
			setState(1110);
			identifier();
			setState(1111);
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
		enterRule(_localctx, 64, RULE_nonReserved);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1113);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ACCOUNT) | (1L << ADD) | (1L << ALTER) | (1L << APPLICATION) | (1L << APPLICATIONS) | (1L << ARRAY) | (1L << AS) | (1L << CACHE) | (1L << CASCADE) | (1L << COLUMN) | (1L << COLUMNS) | (1L << DATABASE) | (1L << DATABASES) | (1L << DATASOURCE) | (1L << DATASOURCES) | (1L << FUNCTION) | (1L << FUNCTIONS) | (1L << GRANT) | (1L << GROUP) | (1L << GROUPS))) != 0) || ((((_la - 71)) & ~0x3f) == 0 && ((1L << (_la - 71)) & ((1L << (ORG - 71)) | (1L << (REMOVE - 71)) | (1L << (RENAME - 71)) | (1L << (REVOKE - 71)) | (1L << (SA - 71)) | (1L << (SET - 71)) | (1L << (SHOW - 71)) | (1L << (TABLE - 71)) | (1L << (TABLES - 71)) | (1L << (TO - 71)) | (1L << (TYPE - 71)) | (1L << (USER - 71)) | (1L << (VIEW - 71)) | (1L << (VIEWS - 71)) | (1L << (WITH - 71)))) != 0)) ) {
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
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3n\u045e\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\3\2\3\2\3\2\3\3\3\3\3\3\3\3\3\3\5\3M\n\3\3\3\3\3\3\3\5\3R"+
		"\n\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3l\n\3\3\3\3\3\5\3p\n\3\3\3\3\3\3\3\3"+
		"\3\3\3\5\3w\n\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3\u00a2\n\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\5\3\u0116\n\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\5\3\u0135\n\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3\u013d\n\3\3\3\3\3"+
		"\3\3\5\3\u0142\n\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3\u015d\n\3\3\3\3\3"+
		"\3\3\3\3\3\3\5\3\u0164\n\3\3\3\3\3\3\3\3\3\5\3\u016a\n\3\3\3\3\3\5\3\u016e"+
		"\n\3\3\3\3\3\3\3\3\3\3\3\5\3\u0175\n\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\5\3\u0193\n\3\3\3\3\3\3\3\5\3\u0198\n\3\3\3\3\3\3\3\3\3\5\3"+
		"\u019e\n\3\3\3\3\3\3\3\3\3\3\3\5\3\u01a5\n\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\5\3\u01af\n\3\3\3\3\3\3\3\3\3\5\3\u01b5\n\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3\u01db\n\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3\u01eb\n\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\5\3\u01f3\n\3\3\3\3\3\3\3\5\3\u01f8\n\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\5\3\u0212\n\3\3\3\3\3\5\3\u0216\n\3\3\3\3\3\3\3\3\3"+
		"\5\3\u021c\n\3\3\3\3\3\3\3\3\3\5\3\u0222\n\3\3\3\3\3\3\3\3\3\5\3\u0228"+
		"\n\3\3\3\3\3\3\3\3\3\7\3\u022e\n\3\f\3\16\3\u0231\13\3\5\3\u0233\n\3\3"+
		"\3\3\3\5\3\u0237\n\3\3\3\3\3\3\3\5\3\u023c\n\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\5\3\u0244\n\3\3\3\3\3\3\3\5\3\u0249\n\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3\u026c\n\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\5\3\u0274\n\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3\u0291\n\3\3\3"+
		"\3\3\3\3\3\3\5\3\u0297\n\3\3\3\3\3\3\3\3\3\5\3\u029d\n\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\5\3\u02a5\n\3\3\3\3\3\5\3\u02a9\n\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3"+
		"\u02d3\n\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3\u02dc\n\3\3\3\3\3\3\3\3\3\5"+
		"\3\u02e2\n\3\3\3\3\3\3\3\3\3\5\3\u02e8\n\3\3\3\3\3\5\3\u02ec\n\3\3\3\3"+
		"\3\3\3\3\3\5\3\u02f2\n\3\3\3\3\3\5\3\u02f6\n\3\3\3\3\3\3\3\3\3\5\3\u02fc"+
		"\n\3\3\3\3\3\5\3\u0300\n\3\3\3\3\3\3\3\3\3\5\3\u0306\n\3\3\3\3\3\3\3\3"+
		"\3\5\3\u030c\n\3\3\3\3\3\3\3\3\3\5\3\u0312\n\3\3\3\3\3\3\3\5\3\u0317\n"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3\u0320\n\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\5\3\u0329\n\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3\u0334\n\3\3\3"+
		"\5\3\u0337\n\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3\u033f\n\3\3\3\3\3\5\3\u0343"+
		"\n\3\3\3\3\3\3\3\3\3\3\3\5\3\u034a\n\3\3\3\5\3\u034d\n\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\5\3\u0356\n\3\3\4\3\4\3\4\7\4\u035b\n\4\f\4\16\4\u035e"+
		"\13\4\3\5\5\5\u0361\n\5\3\5\3\5\5\5\u0365\n\5\3\6\3\6\3\6\3\6\3\6\3\6"+
		"\3\6\3\7\3\7\3\b\3\b\7\b\u0372\n\b\f\b\16\b\u0375\13\b\3\b\3\b\3\b\7\b"+
		"\u037a\n\b\f\b\16\b\u037d\13\b\5\b\u037f\n\b\3\t\3\t\3\t\3\t\7\t\u0385"+
		"\n\t\f\t\16\t\u0388\13\t\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3"+
		"\n\3\n\3\n\3\n\5\n\u0399\n\n\3\n\3\n\5\n\u039d\n\n\3\n\3\n\3\n\3\n\3\n"+
		"\7\n\u03a4\n\n\f\n\16\n\u03a7\13\n\3\n\5\n\u03aa\n\n\5\n\u03ac\n\n\3\13"+
		"\3\13\3\13\7\13\u03b1\n\13\f\13\16\13\u03b4\13\13\3\f\3\f\3\f\3\r\3\r"+
		"\3\r\7\r\u03bc\n\r\f\r\16\r\u03bf\13\r\3\16\3\16\3\16\3\16\3\17\3\17\5"+
		"\17\u03c7\n\17\3\17\3\17\3\17\3\17\3\20\3\20\3\20\7\20\u03d0\n\20\f\20"+
		"\16\20\u03d3\13\20\3\21\3\21\3\21\3\21\3\21\5\21\u03da\n\21\3\21\3\21"+
		"\3\21\3\22\3\22\3\22\7\22\u03e2\n\22\f\22\16\22\u03e5\13\22\3\23\3\23"+
		"\3\24\3\24\3\24\7\24\u03ec\n\24\f\24\16\24\u03ef\13\24\3\25\3\25\3\25"+
		"\5\25\u03f4\n\25\3\25\3\25\3\25\3\25\3\26\3\26\3\26\3\26\7\26\u03fe\n"+
		"\26\f\26\16\26\u0401\13\26\3\26\3\26\3\26\3\26\3\26\3\26\7\26\u0409\n"+
		"\26\f\26\16\26\u040c\13\26\3\26\3\26\3\26\3\26\5\26\u0412\n\26\3\27\3"+
		"\27\3\27\3\27\3\30\3\30\3\30\3\30\3\31\3\31\3\31\7\31\u041f\n\31\f\31"+
		"\16\31\u0422\13\31\3\32\3\32\3\32\5\32\u0427\n\32\3\32\3\32\3\33\3\33"+
		"\3\33\5\33\u042e\n\33\3\33\3\33\3\34\3\34\3\34\3\34\7\34\u0436\n\34\f"+
		"\34\16\34\u0439\13\34\3\34\3\34\3\35\3\35\5\35\u043f\n\35\3\35\5\35\u0442"+
		"\n\35\3\36\3\36\3\36\7\36\u0447\n\36\f\36\16\36\u044a\13\36\3\36\5\36"+
		"\u044d\n\36\3\37\7\37\u0450\n\37\f\37\16\37\u0453\13\37\3 \3 \5 \u0457"+
		"\n \3!\3!\3!\3\"\3\"\3\"\3\u0451\2#\2\4\6\b\n\f\16\20\22\24\26\30\32\34"+
		"\36 \"$&(*,.\60\62\64\668:<>@B\2\f\3\2IJ\3\2[\\\4\2++\60\60\4\2\67\67"+
		"??\3\2)*\4\2AAKK\4\2VVhh\3\2\6\6\5\2\17\17\'\'..\20\2\17\24\30\30\32\32"+
		"\34\36#&8<IIMNPQTUYZ]^bbdf\u0503\2D\3\2\2\2\4\u0355\3\2\2\2\6\u0357\3"+
		"\2\2\2\b\u0360\3\2\2\2\n\u0366\3\2\2\2\f\u036d\3\2\2\2\16\u037e\3\2\2"+
		"\2\20\u0380\3\2\2\2\22\u03ab\3\2\2\2\24\u03ad\3\2\2\2\26\u03b5\3\2\2\2"+
		"\30\u03b8\3\2\2\2\32\u03c0\3\2\2\2\34\u03c4\3\2\2\2\36\u03cc\3\2\2\2 "+
		"\u03d4\3\2\2\2\"\u03de\3\2\2\2$\u03e6\3\2\2\2&\u03e8\3\2\2\2(\u03f3\3"+
		"\2\2\2*\u0411\3\2\2\2,\u0413\3\2\2\2.\u0417\3\2\2\2\60\u041b\3\2\2\2\62"+
		"\u0426\3\2\2\2\64\u042d\3\2\2\2\66\u0431\3\2\2\28\u043c\3\2\2\2:\u044c"+
		"\3\2\2\2<\u0451\3\2\2\2>\u0456\3\2\2\2@\u0458\3\2\2\2B\u045b\3\2\2\2D"+
		"E\5\4\3\2EF\7\2\2\3F\3\3\2\2\2GH\7!\2\2HL\t\2\2\2IJ\7>\2\2JK\7D\2\2KM"+
		"\7\64\2\2LI\3\2\2\2LM\3\2\2\2MN\3\2\2\2NQ\5> \2OP\7\37\2\2PR\7g\2\2QO"+
		"\3\2\2\2QR\3\2\2\2R\u0356\3\2\2\2ST\7N\2\2TU\t\2\2\2UV\5> \2VW\7]\2\2"+
		"WX\5> \2X\u0356\3\2\2\2YZ\7\21\2\2Z[\t\2\2\2[\\\5> \2\\]\7N\2\2]^\7]\2"+
		"\2^_\5> \2_\u0356\3\2\2\2`a\7\21\2\2ab\t\2\2\2bc\5> \2cd\7T\2\2de\7\37"+
		"\2\2ef\7g\2\2f\u0356\3\2\2\2gh\7/\2\2hk\t\2\2\2ij\7>\2\2jl\7\64\2\2ki"+
		"\3\2\2\2kl\3\2\2\2lm\3\2\2\2mo\5> \2np\7\34\2\2on\3\2\2\2op\3\2\2\2p\u0356"+
		"\3\2\2\2qr\7!\2\2rv\7Q\2\2st\7>\2\2tu\7D\2\2uw\7\64\2\2vs\3\2\2\2vw\3"+
		"\2\2\2wx\3\2\2\2xy\5> \2yz\7?\2\2z{\t\2\2\2{|\5> \2|}\7=\2\2}~\7\31\2"+
		"\2~\177\5<\37\2\177\u0356\3\2\2\2\u0080\u0081\7N\2\2\u0081\u0082\7Q\2"+
		"\2\u0082\u0083\5> \2\u0083\u0084\7?\2\2\u0084\u0085\t\2\2\2\u0085\u0086"+
		"\5> \2\u0086\u0087\7]\2\2\u0087\u0088\5> \2\u0088\u0356\3\2\2\2\u0089"+
		"\u008a\7\21\2\2\u008a\u008b\7Q\2\2\u008b\u008c\5> \2\u008c\u008d\7?\2"+
		"\2\u008d\u008e\t\2\2\2\u008e\u008f\5> \2\u008f\u0090\7N\2\2\u0090\u0091"+
		"\7]\2\2\u0091\u0092\5> \2\u0092\u0356\3\2\2\2\u0093\u0094\7\21\2\2\u0094"+
		"\u0095\7Q\2\2\u0095\u0096\5> \2\u0096\u0097\7?\2\2\u0097\u0098\t\2\2\2"+
		"\u0098\u0099\5> \2\u0099\u009a\7=\2\2\u009a\u009b\7\31\2\2\u009b\u009c"+
		"\5<\37\2\u009c\u0356\3\2\2\2\u009d\u009e\7/\2\2\u009e\u00a1\7Q\2\2\u009f"+
		"\u00a0\7>\2\2\u00a0\u00a2\7\64\2\2\u00a1\u009f\3\2\2\2\u00a1\u00a2\3\2"+
		"\2\2\u00a2\u00a3\3\2\2\2\u00a3\u00a4\5> \2\u00a4\u00a5\7?\2\2\u00a5\u00a6"+
		"\t\2\2\2\u00a6\u00a7\5> \2\u00a7\u0356\3\2\2\2\u00a8\u00a9\7:\2\2\u00a9"+
		"\u00aa\7:\2\2\u00aa\u00ab\7F\2\2\u00ab\u00ac\5\"\22\2\u00ac\u00ad\7]\2"+
		"\2\u00ad\u00ae\7b\2\2\u00ae\u00af\5\60\31\2\u00af\u0356\3\2\2\2\u00b0"+
		"\u00b1\7:\2\2\u00b1\u00b2\7:\2\2\u00b2\u00b3\7F\2\2\u00b3\u00b4\5\"\22"+
		"\2\u00b4\u00b5\7]\2\2\u00b5\u00b6\7;\2\2\u00b6\u00b7\5\60\31\2\u00b7\u0356"+
		"\3\2\2\2\u00b8\u00b9\7P\2\2\u00b9\u00ba\7:\2\2\u00ba\u00bb\7F\2\2\u00bb"+
		"\u00bc\5\"\22\2\u00bc\u00bd\7\67\2\2\u00bd\u00be\7b\2\2\u00be\u00bf\5"+
		"\60\31\2\u00bf\u0356\3\2\2\2\u00c0\u00c1\7P\2\2\u00c1\u00c2\7:\2\2\u00c2"+
		"\u00c3\7F\2\2\u00c3\u00c4\5\"\22\2\u00c4\u00c5\7\67\2\2\u00c5\u00c6\7"+
		";\2\2\u00c6\u00c7\5\60\31\2\u00c7\u0356\3\2\2\2\u00c8\u00c9\7:\2\2\u00c9"+
		"\u00ca\7\17\2\2\u00ca\u00cb\7]\2\2\u00cb\u00cc\7b\2\2\u00cc\u0356\5\60"+
		"\31\2\u00cd\u00ce\7:\2\2\u00ce\u00cf\7\17\2\2\u00cf\u00d0\7]\2\2\u00d0"+
		"\u00d1\7;\2\2\u00d1\u0356\5\60\31\2\u00d2\u00d3\7P\2\2\u00d3\u00d4\7\17"+
		"\2\2\u00d4\u00d5\7\67\2\2\u00d5\u00d6\7b\2\2\u00d6\u0356\5\60\31\2\u00d7"+
		"\u00d8\7P\2\2\u00d8\u00d9\7\17\2\2\u00d9\u00da\7\67\2\2\u00da\u00db\7"+
		";\2\2\u00db\u0356\5\60\31\2\u00dc\u00dd\7:\2\2\u00dd\u00de\7\'\2\2\u00de"+
		"\u00df\7]\2\2\u00df\u00e0\7b\2\2\u00e0\u0356\5\60\31\2\u00e1\u00e2\7:"+
		"\2\2\u00e2\u00e3\7\'\2\2\u00e3\u00e4\7]\2\2\u00e4\u00e5\7;\2\2\u00e5\u0356"+
		"\5\60\31\2\u00e6\u00e7\7P\2\2\u00e7\u00e8\7\'\2\2\u00e8\u00e9\7\67\2\2"+
		"\u00e9\u00ea\7b\2\2\u00ea\u0356\5\60\31\2\u00eb\u00ec\7P\2\2\u00ec\u00ed"+
		"\7\'\2\2\u00ed\u00ee\7\67\2\2\u00ee\u00ef\7;\2\2\u00ef\u0356\5\60\31\2"+
		"\u00f0\u00f1\7:\2\2\u00f1\u00f2\7-\2\2\u00f2\u00f3\7E\2\2\u00f3\u00f4"+
		"\5&\24\2\u00f4\u00f5\7]\2\2\u00f5\u00f6\7b\2\2\u00f6\u00f7\5\60\31\2\u00f7"+
		"\u0356\3\2\2\2\u00f8\u00f9\7:\2\2\u00f9\u00fa\7-\2\2\u00fa\u00fb\7E\2"+
		"\2\u00fb\u00fc\5&\24\2\u00fc\u00fd\7]\2\2\u00fd\u00fe\7;\2\2\u00fe\u00ff"+
		"\5\60\31\2\u00ff\u0356\3\2\2\2\u0100\u0101\7P\2\2\u0101\u0102\7-\2\2\u0102"+
		"\u0103\7E\2\2\u0103\u0104\5&\24\2\u0104\u0105\7\67\2\2\u0105\u0106\7b"+
		"\2\2\u0106\u0107\5\60\31\2\u0107\u0356\3\2\2\2\u0108\u0109\7P\2\2\u0109"+
		"\u010a\7-\2\2\u010a\u010b\7E\2\2\u010b\u010c\5&\24\2\u010c\u010d\7\67"+
		"\2\2\u010d\u010e\7;\2\2\u010e\u010f\5\60\31\2\u010f\u0356\3\2\2\2\u0110"+
		"\u0111\7!\2\2\u0111\u0115\7b\2\2\u0112\u0113\7>\2\2\u0113\u0114\7D\2\2"+
		"\u0114\u0116\7\64\2\2\u0115\u0112\3\2\2\2\u0115\u0116\3\2\2\2\u0116\u0117"+
		"\3\2\2\2\u0117\u0118\5> \2\u0118\u0119\7=\2\2\u0119\u011a\7\31\2\2\u011a"+
		"\u011b\5<\37\2\u011b\u0356\3\2\2\2\u011c\u011d\7N\2\2\u011d\u011e\7b\2"+
		"\2\u011e\u011f\5> \2\u011f\u0120\7]\2\2\u0120\u0121\5> \2\u0121\u0356"+
		"\3\2\2\2\u0122\u0123\7\21\2\2\u0123\u0124\7b\2\2\u0124\u0125\5> \2\u0125"+
		"\u0126\7N\2\2\u0126\u0127\7]\2\2\u0127\u0128\5> \2\u0128\u0356\3\2\2\2"+
		"\u0129\u012a\7\21\2\2\u012a\u012b\7b\2\2\u012b\u012c\5> \2\u012c\u012d"+
		"\7=\2\2\u012d\u012e\7\31\2\2\u012e\u012f\5<\37\2\u012f\u0356\3\2\2\2\u0130"+
		"\u0131\7/\2\2\u0131\u0134\7b\2\2\u0132\u0133\7>\2\2\u0133\u0135\7\64\2"+
		"\2\u0134\u0132\3\2\2\2\u0134\u0135\3\2\2\2\u0135\u0136\3\2\2\2\u0136\u0356"+
		"\5> \2\u0137\u0138\7!\2\2\u0138\u013c\7;\2\2\u0139\u013a\7>\2\2\u013a"+
		"\u013b\7D\2\2\u013b\u013d\7\64\2\2\u013c\u0139\3\2\2\2\u013c\u013d\3\2"+
		"\2\2\u013d\u013e\3\2\2\2\u013e\u0141\5> \2\u013f\u0140\7\37\2\2\u0140"+
		"\u0142\7g\2\2\u0141\u013f\3\2\2\2\u0141\u0142\3\2\2\2\u0142\u0356\3\2"+
		"\2\2\u0143\u0144\7N\2\2\u0144\u0145\7;\2\2\u0145\u0146\5> \2\u0146\u0147"+
		"\7]\2\2\u0147\u0148\5> \2\u0148\u0356\3\2\2\2\u0149\u014a\7\21\2\2\u014a"+
		"\u014b\7;\2\2\u014b\u014c\5> \2\u014c\u014d\7N\2\2\u014d\u014e\7]\2\2"+
		"\u014e\u014f\5> \2\u014f\u0356\3\2\2\2\u0150\u0151\7\21\2\2\u0151\u0152"+
		"\7;\2\2\u0152\u0153\5> \2\u0153\u0154\7T\2\2\u0154\u0155\7\37\2\2\u0155"+
		"\u0156\7g\2\2\u0156\u0356\3\2\2\2\u0157\u0158\7\21\2\2\u0158\u0159\7;"+
		"\2\2\u0159\u015a\5> \2\u015a\u015c\5,\27\2\u015b\u015d\5.\30\2\u015c\u015b"+
		"\3\2\2\2\u015c\u015d\3\2\2\2\u015d\u0356\3\2\2\2\u015e\u015f\7\21\2\2"+
		"\u015f\u0160\7;\2\2\u0160\u0161\5> \2\u0161\u0163\5.\30\2\u0162\u0164"+
		"\5,\27\2\u0163\u0162\3\2\2\2\u0163\u0164\3\2\2\2\u0164\u0356\3\2\2\2\u0165"+
		"\u0166\7/\2\2\u0166\u0169\7;\2\2\u0167\u0168\7>\2\2\u0168\u016a\7\64\2"+
		"\2\u0169\u0167\3\2\2\2\u0169\u016a\3\2\2\2\u016a\u016b\3\2\2\2\u016b\u016d"+
		"\5> \2\u016c\u016e\7\34\2\2\u016d\u016c\3\2\2\2\u016d\u016e\3\2\2\2\u016e"+
		"\u0356\3\2\2\2\u016f\u0170\7C\2\2\u0170\u0174\7%\2\2\u0171\u0172\7>\2"+
		"\2\u0172\u0173\7D\2\2\u0173\u0175\7\64\2\2\u0174\u0171\3\2\2\2\u0174\u0175"+
		"\3\2\2\2\u0175\u0176\3\2\2\2\u0176\u0177\5> \2\u0177\u0178\7G\2\2\u0178"+
		"\u0179\5\66\34\2\u0179\u0356\3\2\2\2\u017a\u017b\7N\2\2\u017b\u017c\7"+
		"%\2\2\u017c\u017d\5> \2\u017d\u017e\7]\2\2\u017e\u017f\5> \2\u017f\u0356"+
		"\3\2\2\2\u0180\u0181\7\21\2\2\u0181\u0182\7%\2\2\u0182\u0183\5> \2\u0183"+
		"\u0184\7N\2\2\u0184\u0185\7]\2\2\u0185\u0186\5> \2\u0186\u0356\3\2\2\2"+
		"\u0187\u0188\7\21\2\2\u0188\u0189\7%\2\2\u0189\u018a\5> \2\u018a\u018b"+
		"\7T\2\2\u018b\u018c\7G\2\2\u018c\u018d\5\66\34\2\u018d\u0356\3\2\2\2\u018e"+
		"\u018f\7_\2\2\u018f\u0192\7%\2\2\u0190\u0191\7>\2\2\u0191\u0193\7\64\2"+
		"\2\u0192\u0190\3\2\2\2\u0192\u0193\3\2\2\2\u0193\u0194\3\2\2\2\u0194\u0356"+
		"\5> \2\u0195\u0197\7C\2\2\u0196\u0198\7W\2\2\u0197\u0196\3\2\2\2\u0197"+
		"\u0198\3\2\2\2\u0198\u0199\3\2\2\2\u0199\u019d\7Y\2\2\u019a\u019b\7>\2"+
		"\2\u019b\u019c\7D\2\2\u019c\u019e\7\64\2\2\u019d\u019a\3\2\2\2\u019d\u019e"+
		"\3\2\2\2\u019e\u019f\3\2\2\2\u019f\u01a4\5\64\33\2\u01a0\u01a1\7\3\2\2"+
		"\u01a1\u01a2\5\24\13\2\u01a2\u01a3\7\4\2\2\u01a3\u01a5\3\2\2\2\u01a4\u01a0"+
		"\3\2\2\2\u01a4\u01a5\3\2\2\2\u01a5\u01a6\3\2\2\2\u01a6\u01a7\7G\2\2\u01a7"+
		"\u01a8\5\66\34\2\u01a8\u0356\3\2\2\2\u01a9\u01aa\7f\2\2\u01aa\u01ab\7"+
		"%\2\2\u01ab\u01ac\5> \2\u01ac\u01ae\7C\2\2\u01ad\u01af\7W\2\2\u01ae\u01ad"+
		"\3\2\2\2\u01ae\u01af\3\2\2\2\u01af\u01b0\3\2\2\2\u01b0\u01b4\7Y\2\2\u01b1"+
		"\u01b2\7>\2\2\u01b2\u01b3\7D\2\2\u01b3\u01b5\7\64\2\2\u01b4\u01b1\3\2"+
		"\2\2\u01b4\u01b5\3\2\2\2\u01b5\u01b6\3\2\2\2\u01b6\u01b7\5\36\20\2\u01b7"+
		"\u0356\3\2\2\2\u01b8\u01b9\7N\2\2\u01b9\u01ba\7Y\2\2\u01ba\u01bb\5\64"+
		"\33\2\u01bb\u01bc\7]\2\2\u01bc\u01bd\5\64\33\2\u01bd\u0356\3\2\2\2\u01be"+
		"\u01bf\7\21\2\2\u01bf\u01c0\7Y\2\2\u01c0\u01c1\5\64\33\2\u01c1\u01c2\7"+
		"N\2\2\u01c2\u01c3\7]\2\2\u01c3\u01c4\5\64\33\2\u01c4\u0356\3\2\2\2\u01c5"+
		"\u01c6\7\21\2\2\u01c6\u01c7\7Y\2\2\u01c7\u01c8\5\64\33\2\u01c8\u01c9\7"+
		"T\2\2\u01c9\u01ca\7G\2\2\u01ca\u01cb\5\66\34\2\u01cb\u0356\3\2\2\2\u01cc"+
		"\u01cd\7\21\2\2\u01cd\u01ce\7Y\2\2\u01ce\u01cf\5\64\33\2\u01cf\u01d0\7"+
		"\20\2\2\u01d0\u01d1\7\36\2\2\u01d1\u01d2\7\3\2\2\u01d2\u01d3\5\24\13\2"+
		"\u01d3\u01d4\7\4\2\2\u01d4\u0356\3\2\2\2\u01d5\u01d6\7\21\2\2\u01d6\u01d7"+
		"\7Y\2\2\u01d7\u01d8\5\64\33\2\u01d8\u01da\7 \2\2\u01d9\u01db\7\35\2\2"+
		"\u01da\u01d9\3\2\2\2\u01da\u01db\3\2\2\2\u01db\u01dc\3\2\2\2\u01dc\u01dd"+
		"\5> \2\u01dd\u01de\5\26\f\2\u01de\u0356\3\2\2\2\u01df\u01e0\7\21\2\2\u01e0"+
		"\u01e1\7Y\2\2\u01e1\u01e2\5\64\33\2\u01e2\u01e3\7/\2\2\u01e3\u01e4\7\35"+
		"\2\2\u01e4\u01e5\5> \2\u01e5\u0356\3\2\2\2\u01e6\u01e7\7_\2\2\u01e7\u01ea"+
		"\7Y\2\2\u01e8\u01e9\7>\2\2\u01e9\u01eb\7\64\2\2\u01ea\u01e8\3\2\2\2\u01ea"+
		"\u01eb\3\2\2\2\u01eb\u01ec\3\2\2\2\u01ec\u0356\5\64\33\2\u01ed\u01ee\7"+
		"!\2\2\u01ee\u01f2\7#\2\2\u01ef\u01f0\7>\2\2\u01f0\u01f1\7D\2\2\u01f1\u01f3"+
		"\7\64\2\2\u01f2\u01ef\3\2\2\2\u01f2\u01f3\3\2\2\2\u01f3\u01f4\3\2\2\2"+
		"\u01f4\u01f7\5> \2\u01f5\u01f6\7\37\2\2\u01f6\u01f8\7g\2\2\u01f7\u01f5"+
		"\3\2\2\2\u01f7\u01f8\3\2\2\2\u01f8\u0356\3\2\2\2\u01f9\u01fa\7N\2\2\u01fa"+
		"\u01fb\7#\2\2\u01fb\u01fc\5> \2\u01fc\u01fd\7]\2\2\u01fd\u01fe\5> \2\u01fe"+
		"\u0356\3\2\2\2\u01ff\u0200\7\21\2\2\u0200\u0201\7#\2\2\u0201\u0202\5>"+
		" \2\u0202\u0203\7N\2\2\u0203\u0204\7]\2\2\u0204\u0205\5> \2\u0205\u0356"+
		"\3\2\2\2\u0206\u0207\7\21\2\2\u0207\u0208\7#\2\2\u0208\u0209\5> \2\u0209"+
		"\u020a\7T\2\2\u020a\u020b\7\37\2\2\u020b\u020c\7g\2\2\u020c\u0356\3\2"+
		"\2\2\u020d\u020e\7/\2\2\u020e\u0211\7#\2\2\u020f\u0210\7>\2\2\u0210\u0212"+
		"\7\64\2\2\u0211\u020f\3\2\2\2\u0211\u0212\3\2\2\2\u0212\u0213\3\2\2\2"+
		"\u0213\u0215\5> \2\u0214\u0216\7\34\2\2\u0215\u0214\3\2\2\2\u0215\u0216"+
		"\3\2\2\2\u0216\u0356\3\2\2\2\u0217\u0218\7`\2\2\u0218\u0356\5> \2\u0219"+
		"\u021b\7!\2\2\u021a\u021c\t\3\2\2\u021b\u021a\3\2\2\2\u021b\u021c\3\2"+
		"\2\2\u021c\u021d\3\2\2\2\u021d\u0221\78\2\2\u021e\u021f\7>\2\2\u021f\u0220"+
		"\7D\2\2\u0220\u0222\7\64\2\2\u0221\u021e\3\2\2\2\u0221\u0222\3\2\2\2\u0222"+
		"\u0223\3\2\2\2\u0223\u0224\5\62\32\2\u0224\u0225\7\30\2\2\u0225\u0227"+
		"\7g\2\2\u0226\u0228\7g\2\2\u0227\u0226\3\2\2\2\u0227\u0228\3\2\2\2\u0228"+
		"\u0232\3\2\2\2\u0229\u022a\7a\2\2\u022a\u022f\5@!\2\u022b\u022c\7\5\2"+
		"\2\u022c\u022e\5@!\2\u022d\u022b\3\2\2\2\u022e\u0231\3\2\2\2\u022f\u022d"+
		"\3\2\2\2\u022f\u0230\3\2\2\2\u0230\u0233\3\2\2\2\u0231\u022f\3\2\2\2\u0232"+
		"\u0229\3\2\2\2\u0232\u0233\3\2\2\2\u0233\u0356\3\2\2\2\u0234\u0236\7/"+
		"\2\2\u0235\u0237\t\3\2\2\u0236\u0235\3\2\2\2\u0236\u0237\3\2\2\2\u0237"+
		"\u0238\3\2\2\2\u0238\u023b\78\2\2\u0239\u023a\7>\2\2\u023a\u023c\7\64"+
		"\2\2\u023b\u0239\3\2\2\2\u023b\u023c\3\2\2\2\u023c\u023d\3\2\2\2\u023d"+
		"\u0356\5\62\32\2\u023e\u023f\7!\2\2\u023f\u0243\7d\2\2\u0240\u0241\7>"+
		"\2\2\u0241\u0242\7D\2\2\u0242\u0244\7\64\2\2\u0243\u0240\3\2\2\2\u0243"+
		"\u0244\3\2\2\2\u0244\u0245\3\2\2\2\u0245\u0248\5\64\33\2\u0246\u0247\7"+
		"\37\2\2\u0247\u0249\7g\2\2\u0248\u0246\3\2\2\2\u0248\u0249\3\2\2\2\u0249"+
		"\u024a\3\2\2\2\u024a\u024b\7\30\2\2\u024b\u024c\5\16\b\2\u024c\u0356\3"+
		"\2\2\2\u024d\u024e\7N\2\2\u024e\u024f\7d\2\2\u024f\u0250\5\64\33\2\u0250"+
		"\u0251\7]\2\2\u0251\u0252\5\64\33\2\u0252\u0356\3\2\2\2\u0253\u0254\7"+
		"\21\2\2\u0254\u0255\7d\2\2\u0255\u0256\5\64\33\2\u0256\u0257\7N\2\2\u0257"+
		"\u0258\7]\2\2\u0258\u0259\5\64\33\2\u0259\u0356\3\2\2\2\u025a\u025b\7"+
		"\21\2\2\u025b\u025c\7d\2\2\u025c\u025d\5\64\33\2\u025d\u025e\7T\2\2\u025e"+
		"\u025f\7\37\2\2\u025f\u0260\7g\2\2\u0260\u0356\3\2\2\2\u0261\u0262\7\21"+
		"\2\2\u0262\u0263\7d\2\2\u0263\u0264\5\64\33\2\u0264\u0265\7\30\2\2\u0265"+
		"\u0266\5\16\b\2\u0266\u0356\3\2\2\2\u0267\u0268\7/\2\2\u0268\u026b\7d"+
		"\2\2\u0269\u026a\7>\2\2\u026a\u026c\7\64\2\2\u026b\u0269\3\2\2\2\u026b"+
		"\u026c\3\2\2\2\u026c\u026d\3\2\2\2\u026d\u0356\5\64\33\2\u026e\u026f\7"+
		"!\2\2\u026f\u0273\7\22\2\2\u0270\u0271\7>\2\2\u0271\u0272\7D\2\2\u0272"+
		"\u0274\7\64\2\2\u0273\u0270\3\2\2\2\u0273\u0274\3\2\2\2\u0274\u0275\3"+
		"\2\2\2\u0275\u0276\5> \2\u0276\u0277\7\30\2\2\u0277\u0278\5\6\4\2\u0278"+
		"\u0356\3\2\2\2\u0279\u027a\7N\2\2\u027a\u027b\7\22\2\2\u027b\u027c\5>"+
		" \2\u027c\u027d\7]\2\2\u027d\u027e\5> \2\u027e\u0356\3\2\2\2\u027f\u0280"+
		"\7\21\2\2\u0280\u0281\7\22\2\2\u0281\u0282\5> \2\u0282\u0283\7N\2\2\u0283"+
		"\u0284\7]\2\2\u0284\u0285\5> \2\u0285\u0356\3\2\2\2\u0286\u0287\7\21\2"+
		"\2\u0287\u0288\7\22\2\2\u0288\u0289\5> \2\u0289\u028a\7\30\2\2\u028a\u028b"+
		"\5\6\4\2\u028b\u0356\3\2\2\2\u028c\u028d\7/\2\2\u028d\u0290\7\22\2\2\u028e"+
		"\u028f\7>\2\2\u028f\u0291\7\64\2\2\u0290\u028e\3\2\2\2\u0290\u0291\3\2"+
		"\2\2\u0291\u0292\3\2\2\2\u0292\u0356\5> \2\u0293\u0296\7!\2\2\u0294\u0295"+
		"\7(\2\2\u0295\u0297\5\b\5\2\u0296\u0294\3\2\2\2\u0296\u0297\3\2\2\2\u0297"+
		"\u0298\3\2\2\2\u0298\u029c\7\63\2\2\u0299\u029a\7>\2\2\u029a\u029b\7D"+
		"\2\2\u029b\u029d\7\64\2\2\u029c\u0299\3\2\2\2\u029c\u029d\3\2\2\2\u029d"+
		"\u029e\3\2\2\2\u029e\u029f\5> \2\u029f\u02a0\7E\2\2\u02a0\u02a1\7R\2\2"+
		"\u02a1\u02a2\7\25\2\2\u02a2\u02a4\5\n\6\2\u02a3\u02a5\t\4\2\2\u02a4\u02a3"+
		"\3\2\2\2\u02a4\u02a5\3\2\2\2\u02a5\u02a8\3\2\2\2\u02a6\u02a7\7\37\2\2"+
		"\u02a7\u02a9\7g\2\2\u02a8\u02a6\3\2\2\2\u02a8\u02a9\3\2\2\2\u02a9\u02aa"+
		"\3\2\2\2\u02aa\u02ab\7,\2\2\u02ab\u02ac\7\33\2\2\u02ac\u02ad\5> \2\u02ad"+
		"\u0356\3\2\2\2\u02ae\u02af\7N\2\2\u02af\u02b0\7\63\2\2\u02b0\u02b1\5>"+
		" \2\u02b1\u02b2\7]\2\2\u02b2\u02b3\5> \2\u02b3\u0356\3\2\2\2\u02b4\u02b5"+
		"\7\21\2\2\u02b5\u02b6\7(\2\2\u02b6\u02b7\5\b\5\2\u02b7\u02b8\7\63\2\2"+
		"\u02b8\u02b9\5> \2\u02b9\u0356\3\2\2\2\u02ba\u02bb\7\21\2\2\u02bb\u02bc"+
		"\7\63\2\2\u02bc\u02bd\5> \2\u02bd\u02be\7N\2\2\u02be\u02bf\7]\2\2\u02bf"+
		"\u02c0\5> \2\u02c0\u0356\3\2\2\2\u02c1\u02c2\7\21\2\2\u02c2\u02c3\7\63"+
		"\2\2\u02c3\u02c4\5> \2\u02c4\u02c5\7E\2\2\u02c5\u02c6\7R\2\2\u02c6\u02c7"+
		"\7\25\2\2\u02c7\u02c8\5\n\6\2\u02c8\u0356\3\2\2\2\u02c9\u02ca\7\21\2\2"+
		"\u02ca\u02cb\7\63\2\2\u02cb\u02cc\5> \2\u02cc\u02cd\t\4\2\2\u02cd\u0356"+
		"\3\2\2\2\u02ce\u02cf\7/\2\2\u02cf\u02d2\7\63\2\2\u02d0\u02d1\7>\2\2\u02d1"+
		"\u02d3\7\64\2\2\u02d2\u02d0\3\2\2\2\u02d2\u02d3\3\2\2\2\u02d3\u02d4\3"+
		"\2\2\2\u02d4\u0356\5> \2\u02d5\u02d6\7U\2\2\u02d6\u0356\7X\2\2\u02d7\u02d8"+
		"\7U\2\2\u02d8\u02db\7&\2\2\u02d9\u02da\7B\2\2\u02da\u02dc\7g\2\2\u02db"+
		"\u02d9\3\2\2\2\u02db\u02dc\3\2\2\2\u02dc\u0356\3\2\2\2\u02dd\u02de\7U"+
		"\2\2\u02de\u02e1\7$\2\2\u02df\u02e0\7B\2\2\u02e0\u02e2\7g\2\2\u02e1\u02df"+
		"\3\2\2\2\u02e1\u02e2\3\2\2\2\u02e2\u0356\3\2\2\2\u02e3\u02e4\7U\2\2\u02e4"+
		"\u02e7\7Z\2\2\u02e5\u02e6\t\5\2\2\u02e6\u02e8\5> \2\u02e7\u02e5\3\2\2"+
		"\2\u02e7\u02e8\3\2\2\2\u02e8\u02eb\3\2\2\2\u02e9\u02ea\7B\2\2\u02ea\u02ec"+
		"\7g\2\2\u02eb\u02e9\3\2\2\2\u02eb\u02ec\3\2\2\2\u02ec\u0356\3\2\2\2\u02ed"+
		"\u02ee\7U\2\2\u02ee\u02f1\7e\2\2\u02ef\u02f0\t\5\2\2\u02f0\u02f2\5> \2"+
		"\u02f1\u02ef\3\2\2\2\u02f1\u02f2\3\2\2\2\u02f2\u02f5\3\2\2\2\u02f3\u02f4"+
		"\7B\2\2\u02f4\u02f6\7g\2\2\u02f5\u02f3\3\2\2\2\u02f5\u02f6\3\2\2\2\u02f6"+
		"\u0356\3\2\2\2\u02f7\u02f8\7U\2\2\u02f8\u02fb\79\2\2\u02f9\u02fa\t\5\2"+
		"\2\u02fa\u02fc\5> \2\u02fb\u02f9\3\2\2\2\u02fb\u02fc\3\2\2\2\u02fc\u02ff"+
		"\3\2\2\2\u02fd\u02fe\7B\2\2\u02fe\u0300\7g\2\2\u02ff\u02fd\3\2\2\2\u02ff"+
		"\u0300\3\2\2\2\u0300\u0356\3\2\2\2\u0301\u0302\7U\2\2\u0302\u0305\7c\2"+
		"\2\u0303\u0304\7B\2\2\u0304\u0306\7g\2\2\u0305\u0303\3\2\2\2\u0305\u0306"+
		"\3\2\2\2\u0306\u0356\3\2\2\2\u0307\u0308\7U\2\2\u0308\u030b\7<\2\2\u0309"+
		"\u030a\7B\2\2\u030a\u030c\7g\2\2\u030b\u0309\3\2\2\2\u030b\u030c\3\2\2"+
		"\2\u030c\u0356\3\2\2\2\u030d\u030e\7U\2\2\u030e\u0311\7\23\2\2\u030f\u0310"+
		"\7B\2\2\u0310\u0312\7g\2\2\u0311\u030f\3\2\2\2\u0311\u0312\3\2\2\2\u0312"+
		"\u0356\3\2\2\2\u0313\u0314\t\6\2\2\u0314\u0316\7%\2\2\u0315\u0317\7\66"+
		"\2\2\u0316\u0315\3\2\2\2\u0316\u0317\3\2\2\2\u0317\u0318\3\2\2\2\u0318"+
		"\u0356\5> \2\u0319\u031a\t\6\2\2\u031a\u031b\7#\2\2\u031b\u0356\5> \2"+
		"\u031c\u031d\t\6\2\2\u031d\u031f\7Y\2\2\u031e\u0320\7\66\2\2\u031f\u031e"+
		"\3\2\2\2\u031f\u0320\3\2\2\2\u0320\u0321\3\2\2\2\u0321\u0356\5\64\33\2"+
		"\u0322\u0323\t\6\2\2\u0323\u0324\7d\2\2\u0324\u0356\5\64\33\2\u0325\u0326"+
		"\t\6\2\2\u0326\u0328\78\2\2\u0327\u0329\7\66\2\2\u0328\u0327\3\2\2\2\u0328"+
		"\u0329\3\2\2\2\u0329\u032a\3\2\2\2\u032a\u0356\5\62\32\2\u032b\u032c\t"+
		"\6\2\2\u032c\u032d\7b\2\2\u032d\u0356\5> \2\u032e\u032f\t\6\2\2\u032f"+
		"\u0330\7;\2\2\u0330\u0356\5> \2\u0331\u0333\7\65\2\2\u0332\u0334\7\66"+
		"\2\2\u0333\u0332\3\2\2\2\u0333\u0334\3\2\2\2\u0334\u0336\3\2\2\2\u0335"+
		"\u0337\7L\2\2\u0336\u0335\3\2\2\2\u0336\u0337\3\2\2\2\u0337\u0338\3\2"+
		"\2\2\u0338\u0356\5\16\b\2\u0339\u033a\7T\2\2\u033a\u0356\58\35\2\u033b"+
		"\u033c\7@\2\2\u033c\u033e\t\7\2\2\u033d\u033f\7Y\2\2\u033e\u033d\3\2\2"+
		"\2\u033e\u033f\3\2\2\2\u033f\u0340\3\2\2\2\u0340\u0342\5\64\33\2\u0341"+
		"\u0343\7\30\2\2\u0342\u0341\3\2\2\2\u0342\u0343\3\2\2\2\u0343\u0344\3"+
		"\2\2\2\u0344\u0345\5\16\b\2\u0345\u0356\3\2\2\2\u0346\u0349\7!\2\2\u0347"+
		"\u0348\7H\2\2\u0348\u034a\7O\2\2\u0349\u0347\3\2\2\2\u0349\u034a\3\2\2"+
		"\2\u034a\u034c\3\2\2\2\u034b\u034d\7\32\2\2\u034c\u034b\3\2\2\2\u034c"+
		"\u034d\3\2\2\2\u034d\u034e\3\2\2\2\u034e\u034f\t\3\2\2\u034f\u0350\7d"+
		"\2\2\u0350\u0351\5> \2\u0351\u0352\7\30\2\2\u0352\u0353\5\16\b\2\u0353"+
		"\u0356\3\2\2\2\u0354\u0356\5\16\b\2\u0355G\3\2\2\2\u0355S\3\2\2\2\u0355"+
		"Y\3\2\2\2\u0355`\3\2\2\2\u0355g\3\2\2\2\u0355q\3\2\2\2\u0355\u0080\3\2"+
		"\2\2\u0355\u0089\3\2\2\2\u0355\u0093\3\2\2\2\u0355\u009d\3\2\2\2\u0355"+
		"\u00a8\3\2\2\2\u0355\u00b0\3\2\2\2\u0355\u00b8\3\2\2\2\u0355\u00c0\3\2"+
		"\2\2\u0355\u00c8\3\2\2\2\u0355\u00cd\3\2\2\2\u0355\u00d2\3\2\2\2\u0355"+
		"\u00d7\3\2\2\2\u0355\u00dc\3\2\2\2\u0355\u00e1\3\2\2\2\u0355\u00e6\3\2"+
		"\2\2\u0355\u00eb\3\2\2\2\u0355\u00f0\3\2\2\2\u0355\u00f8\3\2\2\2\u0355"+
		"\u0100\3\2\2\2\u0355\u0108\3\2\2\2\u0355\u0110\3\2\2\2\u0355\u011c\3\2"+
		"\2\2\u0355\u0122\3\2\2\2\u0355\u0129\3\2\2\2\u0355\u0130\3\2\2\2\u0355"+
		"\u0137\3\2\2\2\u0355\u0143\3\2\2\2\u0355\u0149\3\2\2\2\u0355\u0150\3\2"+
		"\2\2\u0355\u0157\3\2\2\2\u0355\u015e\3\2\2\2\u0355\u0165\3\2\2\2\u0355"+
		"\u016f\3\2\2\2\u0355\u017a\3\2\2\2\u0355\u0180\3\2\2\2\u0355\u0187\3\2"+
		"\2\2\u0355\u018e\3\2\2\2\u0355\u0195\3\2\2\2\u0355\u01a9\3\2\2\2\u0355"+
		"\u01b8\3\2\2\2\u0355\u01be\3\2\2\2\u0355\u01c5\3\2\2\2\u0355\u01cc\3\2"+
		"\2\2\u0355\u01d5\3\2\2\2\u0355\u01df\3\2\2\2\u0355\u01e6\3\2\2\2\u0355"+
		"\u01ed\3\2\2\2\u0355\u01f9\3\2\2\2\u0355\u01ff\3\2\2\2\u0355\u0206\3\2"+
		"\2\2\u0355\u020d\3\2\2\2\u0355\u0217\3\2\2\2\u0355\u0219\3\2\2\2\u0355"+
		"\u0234\3\2\2\2\u0355\u023e\3\2\2\2\u0355\u024d\3\2\2\2\u0355\u0253\3\2"+
		"\2\2\u0355\u025a\3\2\2\2\u0355\u0261\3\2\2\2\u0355\u0267\3\2\2\2\u0355"+
		"\u026e\3\2\2\2\u0355\u0279\3\2\2\2\u0355\u027f\3\2\2\2\u0355\u0286\3\2"+
		"\2\2\u0355\u028c\3\2\2\2\u0355\u0293\3\2\2\2\u0355\u02ae\3\2\2\2\u0355"+
		"\u02b4\3\2\2\2\u0355\u02ba\3\2\2\2\u0355\u02c1\3\2\2\2\u0355\u02c9\3\2"+
		"\2\2\u0355\u02ce\3\2\2\2\u0355\u02d5\3\2\2\2\u0355\u02d7\3\2\2\2\u0355"+
		"\u02dd\3\2\2\2\u0355\u02e3\3\2\2\2\u0355\u02ed\3\2\2\2\u0355\u02f7\3\2"+
		"\2\2\u0355\u0301\3\2\2\2\u0355\u0307\3\2\2\2\u0355\u030d\3\2\2\2\u0355"+
		"\u0313\3\2\2\2\u0355\u0319\3\2\2\2\u0355\u031c\3\2\2\2\u0355\u0322\3\2"+
		"\2\2\u0355\u0325\3\2\2\2\u0355\u032b\3\2\2\2\u0355\u032e\3\2\2\2\u0355"+
		"\u0331\3\2\2\2\u0355\u0339\3\2\2\2\u0355\u033b\3\2\2\2\u0355\u0346\3\2"+
		"\2\2\u0355\u0354\3\2\2\2\u0356\5\3\2\2\2\u0357\u035c\5\4\3\2\u0358\u0359"+
		"\7\6\2\2\u0359\u035b\5\4\3\2\u035a\u0358\3\2\2\2\u035b\u035e\3\2\2\2\u035c"+
		"\u035a\3\2\2\2\u035c\u035d\3\2\2\2\u035d\7\3\2\2\2\u035e\u035c\3\2\2\2"+
		"\u035f\u0361\7\61\2\2\u0360\u035f\3\2\2\2\u0360\u0361\3\2\2\2\u0361\u0364"+
		"\3\2\2\2\u0362\u0365\5> \2\u0363\u0365\7\"\2\2\u0364\u0362\3\2\2\2\u0364"+
		"\u0363\3\2\2\2\u0365\t\3\2\2\2\u0366\u0367\5\f\7\2\u0367\u0368\5\f\7\2"+
		"\u0368\u0369\5\f\7\2\u0369\u036a\5\f\7\2\u036a\u036b\5\f\7\2\u036b\u036c"+
		"\5\f\7\2\u036c\13\3\2\2\2\u036d\u036e\t\b\2\2\u036e\r\3\2\2\2\u036f\u0373"+
		"\7S\2\2\u0370\u0372\n\t\2\2\u0371\u0370\3\2\2\2\u0372\u0375\3\2\2\2\u0373"+
		"\u0371\3\2\2\2\u0373\u0374\3\2\2\2\u0374\u037f\3\2\2\2\u0375\u0373\3\2"+
		"\2\2\u0376\u0377\5\20\t\2\u0377\u037b\7S\2\2\u0378\u037a\n\t\2\2\u0379"+
		"\u0378\3\2\2\2\u037a\u037d\3\2\2\2\u037b\u0379\3\2\2\2\u037b\u037c\3\2"+
		"\2\2\u037c\u037f\3\2\2\2\u037d\u037b\3\2\2\2\u037e\u036f\3\2\2\2\u037e"+
		"\u0376\3\2\2\2\u037f\17\3\2\2\2\u0380\u0381\7f\2\2\u0381\u0386\5\34\17"+
		"\2\u0382\u0383\7\5\2\2\u0383\u0385\5\34\17\2\u0384\u0382\3\2\2\2\u0385"+
		"\u0388\3\2\2\2\u0386\u0384\3\2\2\2\u0386\u0387\3\2\2\2\u0387\21\3\2\2"+
		"\2\u0388\u0386\3\2\2\2\u0389\u038a\7\24\2\2\u038a\u038b\7\7\2\2\u038b"+
		"\u038c\5\22\n\2\u038c\u038d\7\b\2\2\u038d\u03ac\3\2\2\2\u038e\u038f\7"+
		"\26\2\2\u038f\u0390\7\7\2\2\u0390\u0391\5\22\n\2\u0391\u0392\7\5\2\2\u0392"+
		"\u0393\5\22\n\2\u0393\u0394\7\b\2\2\u0394\u03ac\3\2\2\2\u0395\u039c\7"+
		"\27\2\2\u0396\u0398\7\7\2\2\u0397\u0399\5\30\r\2\u0398\u0397\3\2\2\2\u0398"+
		"\u0399\3\2\2\2\u0399\u039a\3\2\2\2\u039a\u039d\7\b\2\2\u039b\u039d\7\62"+
		"\2\2\u039c\u0396\3\2\2\2\u039c\u039b\3\2\2\2\u039d\u03ac\3\2\2\2\u039e"+
		"\u03a9\5> \2\u039f\u03a0\7\3\2\2\u03a0\u03a5\7h\2\2\u03a1\u03a2\7\5\2"+
		"\2\u03a2\u03a4\7h\2\2\u03a3\u03a1\3\2\2\2\u03a4\u03a7\3\2\2\2\u03a5\u03a3"+
		"\3\2\2\2\u03a5\u03a6\3\2\2\2\u03a6\u03a8\3\2\2\2\u03a7\u03a5\3\2\2\2\u03a8"+
		"\u03aa\7\4\2\2\u03a9\u039f\3\2\2\2\u03a9\u03aa\3\2\2\2\u03aa\u03ac\3\2"+
		"\2\2\u03ab\u0389\3\2\2\2\u03ab\u038e\3\2\2\2\u03ab\u0395\3\2\2\2\u03ab"+
		"\u039e\3\2\2\2\u03ac\23\3\2\2\2\u03ad\u03b2\5\26\f\2\u03ae\u03af\7\5\2"+
		"\2\u03af\u03b1\5\26\f\2\u03b0\u03ae\3\2\2\2\u03b1\u03b4\3\2\2\2\u03b2"+
		"\u03b0\3\2\2\2\u03b2\u03b3\3\2\2\2\u03b3\25\3\2\2\2\u03b4\u03b2\3\2\2"+
		"\2\u03b5\u03b6\5> \2\u03b6\u03b7\5\22\n\2\u03b7\27\3\2\2\2\u03b8\u03bd"+
		"\5\32\16\2\u03b9\u03ba\7\5\2\2\u03ba\u03bc\5\32\16\2\u03bb\u03b9\3\2\2"+
		"\2\u03bc\u03bf\3\2\2\2\u03bd\u03bb\3\2\2\2\u03bd\u03be\3\2\2\2\u03be\31"+
		"\3\2\2\2\u03bf\u03bd\3\2\2\2\u03c0\u03c1\5> \2\u03c1\u03c2\7\t\2\2\u03c2"+
		"\u03c3\5\22\n\2\u03c3\33\3\2\2\2\u03c4\u03c6\5> \2\u03c5\u03c7\7\30\2"+
		"\2\u03c6\u03c5\3\2\2\2\u03c6\u03c7\3\2\2\2\u03c7\u03c8\3\2\2\2\u03c8\u03c9"+
		"\7\3\2\2\u03c9\u03ca\5\16\b\2\u03ca\u03cb\7\4\2\2\u03cb\35\3\2\2\2\u03cc"+
		"\u03d1\5 \21\2\u03cd\u03ce\7\5\2\2\u03ce\u03d0\5 \21\2\u03cf\u03cd\3\2"+
		"\2\2\u03d0\u03d3\3\2\2\2\u03d1\u03cf\3\2\2\2\u03d1\u03d2\3\2\2\2\u03d2"+
		"\37\3\2\2\2\u03d3\u03d1\3\2\2\2\u03d4\u03d9\5\64\33\2\u03d5\u03d6\7\3"+
		"\2\2\u03d6\u03d7\5\24\13\2\u03d7\u03d8\7\4\2\2\u03d8\u03da\3\2\2\2\u03d9"+
		"\u03d5\3\2\2\2\u03d9\u03da\3\2\2\2\u03da\u03db\3\2\2\2\u03db\u03dc\7G"+
		"\2\2\u03dc\u03dd\5\66\34\2\u03dd!\3\2\2\2\u03de\u03e3\5$\23\2\u03df\u03e0"+
		"\7\5\2\2\u03e0\u03e2\5$\23\2\u03e1\u03df\3\2\2\2\u03e2\u03e5\3\2\2\2\u03e3"+
		"\u03e1\3\2\2\2\u03e3\u03e4\3\2\2\2\u03e4#\3\2\2\2\u03e5\u03e3\3\2\2\2"+
		"\u03e6\u03e7\t\n\2\2\u03e7%\3\2\2\2\u03e8\u03ed\5(\25\2\u03e9\u03ea\7"+
		"\5\2\2\u03ea\u03ec\5(\25\2\u03eb\u03e9\3\2\2\2\u03ec\u03ef\3\2\2\2\u03ed"+
		"\u03eb\3\2\2\2\u03ed\u03ee\3\2\2\2\u03ee\'\3\2\2\2\u03ef\u03ed\3\2\2\2"+
		"\u03f0\u03f1\5> \2\u03f1\u03f2\7\n\2\2\u03f2\u03f4\3\2\2\2\u03f3\u03f0"+
		"\3\2\2\2\u03f3\u03f4\3\2\2\2\u03f4\u03f5\3\2\2\2\u03f5\u03f6\5*\26\2\u03f6"+
		"\u03f7\7\n\2\2\u03f7\u03f8\5*\26\2\u03f8)\3\2\2\2\u03f9\u03fa\7\13\2\2"+
		"\u03fa\u03ff\5> \2\u03fb\u03fc\7\5\2\2\u03fc\u03fe\5> \2\u03fd\u03fb\3"+
		"\2\2\2\u03fe\u0401\3\2\2\2\u03ff\u03fd\3\2\2\2\u03ff\u0400\3\2\2\2\u0400"+
		"\u0402\3\2\2\2\u0401\u03ff\3\2\2\2\u0402\u0403\7\f\2\2\u0403\u0412\3\2"+
		"\2\2\u0404\u0405\7\r\2\2\u0405\u040a\5> \2\u0406\u0407\7\5\2\2\u0407\u0409"+
		"\5> \2\u0408\u0406\3\2\2\2\u0409\u040c\3\2\2\2\u040a\u0408\3\2\2\2\u040a"+
		"\u040b\3\2\2\2\u040b\u040d\3\2\2\2\u040c\u040a\3\2\2\2\u040d\u040e\7\16"+
		"\2\2\u040e\u0412\3\2\2\2\u040f\u0412\5> \2\u0410\u0412\7V\2\2\u0411\u03f9"+
		"\3\2\2\2\u0411\u0404\3\2\2\2\u0411\u040f\3\2\2\2\u0411\u0410\3\2\2\2\u0412"+
		"+\3\2\2\2\u0413\u0414\7\20\2\2\u0414\u0415\7b\2\2\u0415\u0416\5\60\31"+
		"\2\u0416-\3\2\2\2\u0417\u0418\7M\2\2\u0418\u0419\7b\2\2\u0419\u041a\5"+
		"\60\31\2\u041a/\3\2\2\2\u041b\u0420\5> \2\u041c\u041d\7\5\2\2\u041d\u041f"+
		"\5> \2\u041e\u041c\3\2\2\2\u041f\u0422\3\2\2\2\u0420\u041e\3\2\2\2\u0420"+
		"\u0421\3\2\2\2\u0421\61\3\2\2\2\u0422\u0420\3\2\2\2\u0423\u0424\5> \2"+
		"\u0424\u0425\7\n\2\2\u0425\u0427\3\2\2\2\u0426\u0423\3\2\2\2\u0426\u0427"+
		"\3\2\2\2\u0427\u0428\3\2\2\2\u0428\u0429\5> \2\u0429\63\3\2\2\2\u042a"+
		"\u042b\5> \2\u042b\u042c\7\n\2\2\u042c\u042e\3\2\2\2\u042d\u042a\3\2\2"+
		"\2\u042d\u042e\3\2\2\2\u042e\u042f\3\2\2\2\u042f\u0430\5> \2\u0430\65"+
		"\3\2\2\2\u0431\u0432\7\3\2\2\u0432\u0437\58\35\2\u0433\u0434\7\5\2\2\u0434"+
		"\u0436\58\35\2\u0435\u0433\3\2\2\2\u0436\u0439\3\2\2\2\u0437\u0435\3\2"+
		"\2\2\u0437\u0438\3\2\2\2\u0438\u043a\3\2\2\2\u0439\u0437\3\2\2\2\u043a"+
		"\u043b\7\4\2\2\u043b\67\3\2\2\2\u043c\u0441\5:\36\2\u043d\u043f\7\61\2"+
		"\2\u043e\u043d\3\2\2\2\u043e\u043f\3\2\2\2\u043f\u0440\3\2\2\2\u0440\u0442"+
		"\7g\2\2\u0441\u043e\3\2\2\2\u0441\u0442\3\2\2\2\u04429\3\2\2\2\u0443\u0448"+
		"\5> \2\u0444\u0445\7\n\2\2\u0445\u0447\5> \2\u0446\u0444\3\2\2\2\u0447"+
		"\u044a\3\2\2\2\u0448\u0446\3\2\2\2\u0448\u0449\3\2\2\2\u0449\u044d\3\2"+
		"\2\2\u044a\u0448\3\2\2\2\u044b\u044d\7g\2\2\u044c\u0443\3\2\2\2\u044c"+
		"\u044b\3\2\2\2\u044d;\3\2\2\2\u044e\u0450\13\2\2\2\u044f\u044e\3\2\2\2"+
		"\u0450\u0453\3\2\2\2\u0451\u0452\3\2\2\2\u0451\u044f\3\2\2\2\u0452=\3"+
		"\2\2\2\u0453\u0451\3\2\2\2\u0454\u0457\7i\2\2\u0455\u0457\5B\"\2\u0456"+
		"\u0454\3\2\2\2\u0456\u0455\3\2\2\2\u0457?\3\2\2\2\u0458\u0459\5> \2\u0459"+
		"\u045a\7g\2\2\u045aA\3\2\2\2\u045b\u045c\t\13\2\2\u045cC\3\2\2\2dLQko"+
		"v\u00a1\u0115\u0134\u013c\u0141\u015c\u0163\u0169\u016d\u0174\u0192\u0197"+
		"\u019d\u01a4\u01ae\u01b4\u01da\u01ea\u01f2\u01f7\u0211\u0215\u021b\u0221"+
		"\u0227\u022f\u0232\u0236\u023b\u0243\u0248\u026b\u0273\u0290\u0296\u029c"+
		"\u02a4\u02a8\u02d2\u02db\u02e1\u02e7\u02eb\u02f1\u02f5\u02fb\u02ff\u0305"+
		"\u030b\u0311\u0316\u031f\u0328\u0333\u0336\u033e\u0342\u0349\u034c\u0355"+
		"\u035c\u0360\u0364\u0373\u037b\u037e\u0386\u0398\u039c\u03a5\u03a9\u03ab"+
		"\u03b2\u03bd\u03c6\u03d1\u03d9\u03e3\u03ed\u03f3\u03ff\u040a\u0411\u0420"+
		"\u0426\u042d\u0437\u043e\u0441\u0448\u044c\u0451\u0456";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}