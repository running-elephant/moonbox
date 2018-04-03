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
		APPLICATIONS=18, ARRAY=19, MAP=20, STRUCT=21, AS=22, BY=23, CACHE=24, 
		CASCADE=25, COLUMN=26, COLUMNS=27, COMMENT=28, CHANGE=29, CREATE=30, DATABASE=31, 
		DATABASES=32, DATASOURCE=33, DATASOURCES=34, DDL=35, DESC=36, DESCRIBE=37, 
		DML=38, DMLON=39, DROP=40, EQ=41, NEQ=42, EXISTS=43, EXTENDED=44, FROM=45, 
		FUNCTION=46, FUNCTIONS=47, GRANT=48, GROUP=49, GROUPS=50, IDENTIFIED=51, 
		IF=52, IN=53, INSERT=54, INTO=55, LIKE=56, MOUNT=57, NOT=58, ON=59, OPTION=60, 
		OPTIONS=61, OR=62, ORG=63, ORGANIZATION=64, OVERWRITE=65, REMOVE=66, RENAME=67, 
		REPLACE=68, REVOKE=69, SA=70, SELECT=71, SET=72, SHOW=73, STAR=74, STREAM=75, 
		SYSINFO=76, TABLE=77, TABLES=78, TEMP=79, TEMPORARY=80, TO=81, TYPE=82, 
		UNMOUNT=83, USE=84, USER=85, USERS=86, VIEW=87, VIEWS=88, WITH=89, STRING=90, 
		INTEGER_VALUE=91, IDENTIFIER=92, SIMPLE_COMMENT=93, BRACKETED_COMMENT=94, 
		WS=95, UNRECOGNIZED=96, DELIMITER=97;
	public static final int
		RULE_single = 0, RULE_mql = 1, RULE_appCmds = 2, RULE_nonLastCmdList = 3, 
		RULE_nonLastCmd = 4, RULE_lastCmd = 5, RULE_insertIntoCmd = 6, RULE_insertOverwriteCmd = 7, 
		RULE_createTemporaryViewCmd = 8, RULE_createTemporaryFunctionCmd = 9, 
		RULE_query = 10, RULE_ctes = 11, RULE_dataType = 12, RULE_colTypeList = 13, 
		RULE_colType = 14, RULE_complexColTypeList = 15, RULE_complexColType = 16, 
		RULE_namedQuery = 17, RULE_mountTableList = 18, RULE_mountTableOptions = 19, 
		RULE_privilegeList = 20, RULE_privilege = 21, RULE_qualifiedColumnList = 22, 
		RULE_columnIdentifier = 23, RULE_identifierStarList = 24, RULE_addUser = 25, 
		RULE_removeUser = 26, RULE_identifierList = 27, RULE_funcIdentifier = 28, 
		RULE_tableIdentifier = 29, RULE_propertyList = 30, RULE_property = 31, 
		RULE_propertyKey = 32, RULE_password = 33, RULE_identifier = 34, RULE_generalIdentifier = 35, 
		RULE_nonReserved = 36;
	public static final String[] ruleNames = {
		"single", "mql", "appCmds", "nonLastCmdList", "nonLastCmd", "lastCmd", 
		"insertIntoCmd", "insertOverwriteCmd", "createTemporaryViewCmd", "createTemporaryFunctionCmd", 
		"query", "ctes", "dataType", "colTypeList", "colType", "complexColTypeList", 
		"complexColType", "namedQuery", "mountTableList", "mountTableOptions", 
		"privilegeList", "privilege", "qualifiedColumnList", "columnIdentifier", 
		"identifierStarList", "addUser", "removeUser", "identifierList", "funcIdentifier", 
		"tableIdentifier", "propertyList", "property", "propertyKey", "password", 
		"identifier", "generalIdentifier", "nonReserved"
	};

	private static final String[] _LITERAL_NAMES = {
		null, "'('", "')'", "','", "';'", "'<'", "'>'", "':'", "'.'", "'['", "']'", 
		"'{'", "'}'", "'/'", "'ACCOUNT'", "'ADD'", "'ALTER'", "'APPLICATION'", 
		"'APPLICATIONS'", "'ARRAY'", "'MAP'", "'STRUCT'", "'AS'", "'BY'", "'CACHE'", 
		"'CASCADE'", "'COLUMN'", "'COLUMNS'", "'COMMENT'", "'CHANGE'", "'CREATE'", 
		"'DATABASE'", "'DATABASES'", "'DATASOURCE'", "'DATASOURCES'", "'DDL'", 
		"'DESC'", "'DESCRIBE'", "'DML'", "'DMLON'", "'DROP'", null, "'<>'", "'EXISTS'", 
		"'EXTENDED'", "'FROM'", "'FUNCTION'", "'FUNCTIONS'", "'GRANT'", "'GROUP'", 
		"'GROUPS'", "'IDENTIFIED '", "'IF'", "'IN'", "'INSERT'", "'INTO'", "'LIKE'", 
		"'MOUNT'", "'NOT'", "'ON'", "'OPTION'", "'OPTIONS'", "'OR'", "'ORG'", 
		"'ORGANIZATION'", "'OVERWRITE'", "'REMOVE'", "'RENAME'", "'REPLACE'", 
		"'REVOKE'", "'SA'", "'SELECT'", "'SET'", "'SHOW'", "'*'", "'STREAM'", 
		"'SYSINFO'", "'TABLE'", "'TABLES'", "'TEMP'", "'TEMPORARY'", "'TO'", "'TYPE'", 
		"'UNMOUNT'", "'USE'", "'USER'", "'USERS'", "'VIEW'", "'VIEWS'", "'WITH'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, null, "ACCOUNT", "ADD", "ALTER", "APPLICATION", "APPLICATIONS", 
		"ARRAY", "MAP", "STRUCT", "AS", "BY", "CACHE", "CASCADE", "COLUMN", "COLUMNS", 
		"COMMENT", "CHANGE", "CREATE", "DATABASE", "DATABASES", "DATASOURCE", 
		"DATASOURCES", "DDL", "DESC", "DESCRIBE", "DML", "DMLON", "DROP", "EQ", 
		"NEQ", "EXISTS", "EXTENDED", "FROM", "FUNCTION", "FUNCTIONS", "GRANT", 
		"GROUP", "GROUPS", "IDENTIFIED", "IF", "IN", "INSERT", "INTO", "LIKE", 
		"MOUNT", "NOT", "ON", "OPTION", "OPTIONS", "OR", "ORG", "ORGANIZATION", 
		"OVERWRITE", "REMOVE", "RENAME", "REPLACE", "REVOKE", "SA", "SELECT", 
		"SET", "SHOW", "STAR", "STREAM", "SYSINFO", "TABLE", "TABLES", "TEMP", 
		"TEMPORARY", "TO", "TYPE", "UNMOUNT", "USE", "USER", "USERS", "VIEW", 
		"VIEWS", "WITH", "STRING", "INTEGER_VALUE", "IDENTIFIER", "SIMPLE_COMMENT", 
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
			setState(74);
			mql();
			setState(75);
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
			setState(765);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,48,_ctx) ) {
			case 1:
				_localctx = new CreateOrganizationContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(77);
				match(CREATE);
				setState(78);
				_la = _input.LA(1);
				if ( !(_la==ORG || _la==ORGANIZATION) ) {
				_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(82);
				_la = _input.LA(1);
				if (_la==IF) {
					{
					setState(79);
					match(IF);
					setState(80);
					match(NOT);
					setState(81);
					match(EXISTS);
					}
				}

				setState(84);
				((CreateOrganizationContext)_localctx).name = identifier();
				setState(87);
				_la = _input.LA(1);
				if (_la==COMMENT) {
					{
					setState(85);
					match(COMMENT);
					setState(86);
					((CreateOrganizationContext)_localctx).comment = match(STRING);
					}
				}

				}
				break;
			case 2:
				_localctx = new RenameOrganizationContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(89);
				match(RENAME);
				setState(90);
				_la = _input.LA(1);
				if ( !(_la==ORG || _la==ORGANIZATION) ) {
				_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(91);
				((RenameOrganizationContext)_localctx).name = identifier();
				setState(92);
				match(TO);
				setState(93);
				((RenameOrganizationContext)_localctx).newName = identifier();
				}
				break;
			case 3:
				_localctx = new SetOrganizationNameContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(95);
				match(ALTER);
				setState(96);
				_la = _input.LA(1);
				if ( !(_la==ORG || _la==ORGANIZATION) ) {
				_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(97);
				((SetOrganizationNameContext)_localctx).name = identifier();
				setState(98);
				match(RENAME);
				setState(99);
				match(TO);
				setState(100);
				((SetOrganizationNameContext)_localctx).newName = identifier();
				}
				break;
			case 4:
				_localctx = new SetOrganizationCommentContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(102);
				match(ALTER);
				setState(103);
				_la = _input.LA(1);
				if ( !(_la==ORG || _la==ORGANIZATION) ) {
				_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(104);
				((SetOrganizationCommentContext)_localctx).name = identifier();
				setState(105);
				match(SET);
				setState(106);
				match(COMMENT);
				setState(107);
				((SetOrganizationCommentContext)_localctx).comment = match(STRING);
				}
				break;
			case 5:
				_localctx = new DropOrganizationContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(109);
				match(DROP);
				setState(110);
				_la = _input.LA(1);
				if ( !(_la==ORG || _la==ORGANIZATION) ) {
				_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(113);
				_la = _input.LA(1);
				if (_la==IF) {
					{
					setState(111);
					match(IF);
					setState(112);
					match(EXISTS);
					}
				}

				setState(115);
				((DropOrganizationContext)_localctx).name = identifier();
				setState(117);
				_la = _input.LA(1);
				if (_la==CASCADE) {
					{
					setState(116);
					match(CASCADE);
					}
				}

				}
				break;
			case 6:
				_localctx = new CreateSaContext(_localctx);
				enterOuterAlt(_localctx, 6);
				{
				setState(119);
				match(CREATE);
				setState(120);
				match(SA);
				setState(124);
				_la = _input.LA(1);
				if (_la==IF) {
					{
					setState(121);
					match(IF);
					setState(122);
					match(NOT);
					setState(123);
					match(EXISTS);
					}
				}

				setState(126);
				((CreateSaContext)_localctx).name = identifier();
				setState(127);
				match(IN);
				setState(128);
				_la = _input.LA(1);
				if ( !(_la==ORG || _la==ORGANIZATION) ) {
				_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(129);
				((CreateSaContext)_localctx).org = identifier();
				setState(130);
				match(IDENTIFIED);
				setState(131);
				match(BY);
				setState(132);
				((CreateSaContext)_localctx).pwd = password();
				}
				break;
			case 7:
				_localctx = new RenameSaContext(_localctx);
				enterOuterAlt(_localctx, 7);
				{
				setState(134);
				match(RENAME);
				setState(135);
				match(SA);
				setState(136);
				((RenameSaContext)_localctx).name = identifier();
				setState(137);
				match(TO);
				setState(138);
				((RenameSaContext)_localctx).newName = identifier();
				setState(139);
				match(IN);
				setState(140);
				_la = _input.LA(1);
				if ( !(_la==ORG || _la==ORGANIZATION) ) {
				_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(141);
				((RenameSaContext)_localctx).org = identifier();
				}
				break;
			case 8:
				_localctx = new SetSaNameContext(_localctx);
				enterOuterAlt(_localctx, 8);
				{
				setState(143);
				match(ALTER);
				setState(144);
				match(SA);
				setState(145);
				((SetSaNameContext)_localctx).name = identifier();
				setState(146);
				match(RENAME);
				setState(147);
				match(TO);
				setState(148);
				((SetSaNameContext)_localctx).newName = identifier();
				setState(149);
				match(IN);
				setState(150);
				_la = _input.LA(1);
				if ( !(_la==ORG || _la==ORGANIZATION) ) {
				_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(151);
				((SetSaNameContext)_localctx).org = identifier();
				}
				break;
			case 9:
				_localctx = new SetSaPasswordContext(_localctx);
				enterOuterAlt(_localctx, 9);
				{
				setState(153);
				match(ALTER);
				setState(154);
				match(SA);
				setState(155);
				((SetSaPasswordContext)_localctx).name = identifier();
				setState(156);
				match(IDENTIFIED);
				setState(157);
				match(BY);
				setState(158);
				((SetSaPasswordContext)_localctx).pwd = password();
				setState(159);
				match(IN);
				setState(160);
				_la = _input.LA(1);
				if ( !(_la==ORG || _la==ORGANIZATION) ) {
				_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(161);
				((SetSaPasswordContext)_localctx).org = identifier();
				}
				break;
			case 10:
				_localctx = new DropSaContext(_localctx);
				enterOuterAlt(_localctx, 10);
				{
				setState(163);
				match(DROP);
				setState(164);
				match(SA);
				setState(167);
				_la = _input.LA(1);
				if (_la==IF) {
					{
					setState(165);
					match(IF);
					setState(166);
					match(EXISTS);
					}
				}

				setState(169);
				((DropSaContext)_localctx).name = identifier();
				setState(170);
				match(IN);
				setState(171);
				_la = _input.LA(1);
				if ( !(_la==ORG || _la==ORGANIZATION) ) {
				_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(172);
				((DropSaContext)_localctx).org = identifier();
				}
				break;
			case 11:
				_localctx = new GrantGrantToUserContext(_localctx);
				enterOuterAlt(_localctx, 11);
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
				match(USER);
				setState(180);
				((GrantGrantToUserContext)_localctx).users = identifierList();
				}
				break;
			case 12:
				_localctx = new GrantGrantToGroupContext(_localctx);
				enterOuterAlt(_localctx, 12);
				{
				setState(182);
				match(GRANT);
				setState(183);
				match(GRANT);
				setState(184);
				match(OPTION);
				setState(185);
				privilegeList();
				setState(186);
				match(TO);
				setState(187);
				match(GROUP);
				setState(188);
				((GrantGrantToGroupContext)_localctx).groups = identifierList();
				}
				break;
			case 13:
				_localctx = new RevokeGrantFromUserContext(_localctx);
				enterOuterAlt(_localctx, 13);
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
				match(USER);
				setState(196);
				((RevokeGrantFromUserContext)_localctx).users = identifierList();
				}
				break;
			case 14:
				_localctx = new RevokeGrantFromGroupContext(_localctx);
				enterOuterAlt(_localctx, 14);
				{
				setState(198);
				match(REVOKE);
				setState(199);
				match(GRANT);
				setState(200);
				match(OPTION);
				setState(201);
				privilegeList();
				setState(202);
				match(FROM);
				setState(203);
				match(GROUP);
				setState(204);
				((RevokeGrantFromGroupContext)_localctx).groups = identifierList();
				}
				break;
			case 15:
				_localctx = new GrantAccountToUsersContext(_localctx);
				enterOuterAlt(_localctx, 15);
				{
				setState(206);
				match(GRANT);
				setState(207);
				match(ACCOUNT);
				setState(208);
				match(TO);
				setState(209);
				match(USER);
				setState(210);
				((GrantAccountToUsersContext)_localctx).users = identifierList();
				}
				break;
			case 16:
				_localctx = new GrantAccountToGroupsContext(_localctx);
				enterOuterAlt(_localctx, 16);
				{
				setState(211);
				match(GRANT);
				setState(212);
				match(ACCOUNT);
				setState(213);
				match(TO);
				setState(214);
				match(GROUP);
				setState(215);
				((GrantAccountToGroupsContext)_localctx).groups = identifierList();
				}
				break;
			case 17:
				_localctx = new RevokeAccountFromUsersContext(_localctx);
				enterOuterAlt(_localctx, 17);
				{
				setState(216);
				match(REVOKE);
				setState(217);
				match(ACCOUNT);
				setState(218);
				match(FROM);
				setState(219);
				match(USER);
				setState(220);
				((RevokeAccountFromUsersContext)_localctx).users = identifierList();
				}
				break;
			case 18:
				_localctx = new RevokeAccountFromGroupsContext(_localctx);
				enterOuterAlt(_localctx, 18);
				{
				setState(221);
				match(REVOKE);
				setState(222);
				match(ACCOUNT);
				setState(223);
				match(FROM);
				setState(224);
				match(GROUP);
				setState(225);
				((RevokeAccountFromGroupsContext)_localctx).groups = identifierList();
				}
				break;
			case 19:
				_localctx = new GrantDdlToUsersContext(_localctx);
				enterOuterAlt(_localctx, 19);
				{
				setState(226);
				match(GRANT);
				setState(227);
				match(DDL);
				setState(228);
				match(TO);
				setState(229);
				match(USER);
				setState(230);
				((GrantDdlToUsersContext)_localctx).users = identifierList();
				}
				break;
			case 20:
				_localctx = new GrantDdlToGroupsContext(_localctx);
				enterOuterAlt(_localctx, 20);
				{
				setState(231);
				match(GRANT);
				setState(232);
				match(DDL);
				setState(233);
				match(TO);
				setState(234);
				match(GROUP);
				setState(235);
				((GrantDdlToGroupsContext)_localctx).groups = identifierList();
				}
				break;
			case 21:
				_localctx = new RevokeDdlFromUsersContext(_localctx);
				enterOuterAlt(_localctx, 21);
				{
				setState(236);
				match(REVOKE);
				setState(237);
				match(DDL);
				setState(238);
				match(FROM);
				setState(239);
				match(USER);
				setState(240);
				((RevokeDdlFromUsersContext)_localctx).users = identifierList();
				}
				break;
			case 22:
				_localctx = new RevokeDdlFromGroupsContext(_localctx);
				enterOuterAlt(_localctx, 22);
				{
				setState(241);
				match(REVOKE);
				setState(242);
				match(DDL);
				setState(243);
				match(FROM);
				setState(244);
				match(GROUP);
				setState(245);
				((RevokeDdlFromGroupsContext)_localctx).groups = identifierList();
				}
				break;
			case 23:
				_localctx = new GrantDmlOnToUsersContext(_localctx);
				enterOuterAlt(_localctx, 23);
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
				match(USER);
				setState(252);
				((GrantDmlOnToUsersContext)_localctx).users = identifierList();
				}
				break;
			case 24:
				_localctx = new GrantDmlOnToGroupsContext(_localctx);
				enterOuterAlt(_localctx, 24);
				{
				setState(254);
				match(GRANT);
				setState(255);
				match(DML);
				setState(256);
				match(ON);
				setState(257);
				qualifiedColumnList();
				setState(258);
				match(TO);
				setState(259);
				match(GROUP);
				setState(260);
				((GrantDmlOnToGroupsContext)_localctx).groups = identifierList();
				}
				break;
			case 25:
				_localctx = new RevokeDmlOnFromUsersContext(_localctx);
				enterOuterAlt(_localctx, 25);
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
				match(USER);
				setState(268);
				((RevokeDmlOnFromUsersContext)_localctx).users = identifierList();
				}
				break;
			case 26:
				_localctx = new RevokeDmlOnFromGroupsContext(_localctx);
				enterOuterAlt(_localctx, 26);
				{
				setState(270);
				match(REVOKE);
				setState(271);
				match(DML);
				setState(272);
				match(ON);
				setState(273);
				qualifiedColumnList();
				setState(274);
				match(FROM);
				setState(275);
				match(GROUP);
				setState(276);
				((RevokeDmlOnFromGroupsContext)_localctx).groups = identifierList();
				}
				break;
			case 27:
				_localctx = new CreateUserContext(_localctx);
				enterOuterAlt(_localctx, 27);
				{
				setState(278);
				match(CREATE);
				setState(279);
				match(USER);
				setState(283);
				_la = _input.LA(1);
				if (_la==IF) {
					{
					setState(280);
					match(IF);
					setState(281);
					match(NOT);
					setState(282);
					match(EXISTS);
					}
				}

				setState(285);
				((CreateUserContext)_localctx).name = identifier();
				setState(286);
				match(IDENTIFIED);
				setState(287);
				match(BY);
				setState(288);
				((CreateUserContext)_localctx).pwd = password();
				}
				break;
			case 28:
				_localctx = new RenameUserContext(_localctx);
				enterOuterAlt(_localctx, 28);
				{
				setState(290);
				match(RENAME);
				setState(291);
				match(USER);
				setState(292);
				((RenameUserContext)_localctx).name = identifier();
				setState(293);
				match(TO);
				setState(294);
				((RenameUserContext)_localctx).newName = identifier();
				}
				break;
			case 29:
				_localctx = new SetUserNameContext(_localctx);
				enterOuterAlt(_localctx, 29);
				{
				setState(296);
				match(ALTER);
				setState(297);
				match(USER);
				setState(298);
				((SetUserNameContext)_localctx).name = identifier();
				setState(299);
				match(RENAME);
				setState(300);
				match(TO);
				setState(301);
				((SetUserNameContext)_localctx).newName = identifier();
				}
				break;
			case 30:
				_localctx = new SetUserPasswordContext(_localctx);
				enterOuterAlt(_localctx, 30);
				{
				setState(303);
				match(ALTER);
				setState(304);
				match(USER);
				setState(305);
				((SetUserPasswordContext)_localctx).name = identifier();
				setState(306);
				match(IDENTIFIED);
				setState(307);
				match(BY);
				setState(308);
				((SetUserPasswordContext)_localctx).pwd = password();
				}
				break;
			case 31:
				_localctx = new DropUserContext(_localctx);
				enterOuterAlt(_localctx, 31);
				{
				setState(310);
				match(DROP);
				setState(311);
				match(USER);
				setState(314);
				_la = _input.LA(1);
				if (_la==IF) {
					{
					setState(312);
					match(IF);
					setState(313);
					match(EXISTS);
					}
				}

				setState(316);
				((DropUserContext)_localctx).name = identifier();
				}
				break;
			case 32:
				_localctx = new CreateGroupContext(_localctx);
				enterOuterAlt(_localctx, 32);
				{
				setState(317);
				match(CREATE);
				setState(318);
				match(GROUP);
				setState(322);
				_la = _input.LA(1);
				if (_la==IF) {
					{
					setState(319);
					match(IF);
					setState(320);
					match(NOT);
					setState(321);
					match(EXISTS);
					}
				}

				setState(324);
				((CreateGroupContext)_localctx).name = identifier();
				setState(327);
				_la = _input.LA(1);
				if (_la==COMMENT) {
					{
					setState(325);
					match(COMMENT);
					setState(326);
					((CreateGroupContext)_localctx).comment = match(STRING);
					}
				}

				}
				break;
			case 33:
				_localctx = new RenameGroupContext(_localctx);
				enterOuterAlt(_localctx, 33);
				{
				setState(329);
				match(RENAME);
				setState(330);
				match(GROUP);
				setState(331);
				((RenameGroupContext)_localctx).name = identifier();
				setState(332);
				match(TO);
				setState(333);
				((RenameGroupContext)_localctx).newName = identifier();
				}
				break;
			case 34:
				_localctx = new SetGroupNameContext(_localctx);
				enterOuterAlt(_localctx, 34);
				{
				setState(335);
				match(ALTER);
				setState(336);
				match(GROUP);
				setState(337);
				((SetGroupNameContext)_localctx).name = identifier();
				setState(338);
				match(RENAME);
				setState(339);
				match(TO);
				setState(340);
				((SetGroupNameContext)_localctx).newName = identifier();
				}
				break;
			case 35:
				_localctx = new SetGroupCommentContext(_localctx);
				enterOuterAlt(_localctx, 35);
				{
				setState(342);
				match(ALTER);
				setState(343);
				match(GROUP);
				setState(344);
				((SetGroupCommentContext)_localctx).name = identifier();
				setState(345);
				match(SET);
				setState(346);
				match(COMMENT);
				setState(347);
				((SetGroupCommentContext)_localctx).comment = match(STRING);
				}
				break;
			case 36:
				_localctx = new AddUsersToGroupContext(_localctx);
				enterOuterAlt(_localctx, 36);
				{
				setState(349);
				match(ALTER);
				setState(350);
				match(GROUP);
				setState(351);
				((AddUsersToGroupContext)_localctx).name = identifier();
				setState(352);
				addUser();
				setState(354);
				_la = _input.LA(1);
				if (_la==REMOVE) {
					{
					setState(353);
					removeUser();
					}
				}

				}
				break;
			case 37:
				_localctx = new RemoveUsersFromGroupContext(_localctx);
				enterOuterAlt(_localctx, 37);
				{
				setState(356);
				match(ALTER);
				setState(357);
				match(GROUP);
				setState(358);
				((RemoveUsersFromGroupContext)_localctx).name = identifier();
				setState(359);
				removeUser();
				setState(361);
				_la = _input.LA(1);
				if (_la==ADD) {
					{
					setState(360);
					addUser();
					}
				}

				}
				break;
			case 38:
				_localctx = new DropGroupContext(_localctx);
				enterOuterAlt(_localctx, 38);
				{
				setState(363);
				match(DROP);
				setState(364);
				match(GROUP);
				setState(367);
				_la = _input.LA(1);
				if (_la==IF) {
					{
					setState(365);
					match(IF);
					setState(366);
					match(EXISTS);
					}
				}

				setState(369);
				((DropGroupContext)_localctx).name = identifier();
				setState(371);
				_la = _input.LA(1);
				if (_la==CASCADE) {
					{
					setState(370);
					match(CASCADE);
					}
				}

				}
				break;
			case 39:
				_localctx = new MountDatasourceContext(_localctx);
				enterOuterAlt(_localctx, 39);
				{
				setState(373);
				match(MOUNT);
				setState(374);
				match(DATASOURCE);
				setState(378);
				_la = _input.LA(1);
				if (_la==IF) {
					{
					setState(375);
					match(IF);
					setState(376);
					match(NOT);
					setState(377);
					match(EXISTS);
					}
				}

				setState(380);
				((MountDatasourceContext)_localctx).name = identifier();
				setState(381);
				match(OPTIONS);
				setState(382);
				propertyList();
				}
				break;
			case 40:
				_localctx = new RenameDatasourceContext(_localctx);
				enterOuterAlt(_localctx, 40);
				{
				setState(384);
				match(RENAME);
				setState(385);
				match(DATASOURCE);
				setState(386);
				((RenameDatasourceContext)_localctx).name = identifier();
				setState(387);
				match(TO);
				setState(388);
				((RenameDatasourceContext)_localctx).newName = identifier();
				}
				break;
			case 41:
				_localctx = new SetDatasourceNameContext(_localctx);
				enterOuterAlt(_localctx, 41);
				{
				setState(390);
				match(ALTER);
				setState(391);
				match(DATASOURCE);
				setState(392);
				((SetDatasourceNameContext)_localctx).name = identifier();
				setState(393);
				match(RENAME);
				setState(394);
				match(TO);
				setState(395);
				((SetDatasourceNameContext)_localctx).newName = identifier();
				}
				break;
			case 42:
				_localctx = new SetDatasourcePropertiesContext(_localctx);
				enterOuterAlt(_localctx, 42);
				{
				setState(397);
				match(ALTER);
				setState(398);
				match(DATASOURCE);
				setState(399);
				((SetDatasourcePropertiesContext)_localctx).name = identifier();
				setState(400);
				match(SET);
				setState(401);
				match(OPTIONS);
				setState(402);
				propertyList();
				}
				break;
			case 43:
				_localctx = new UnmountDatasourceContext(_localctx);
				enterOuterAlt(_localctx, 43);
				{
				setState(404);
				match(UNMOUNT);
				setState(405);
				match(DATASOURCE);
				setState(408);
				_la = _input.LA(1);
				if (_la==IF) {
					{
					setState(406);
					match(IF);
					setState(407);
					match(EXISTS);
					}
				}

				setState(410);
				((UnmountDatasourceContext)_localctx).name = identifier();
				}
				break;
			case 44:
				_localctx = new MountTableContext(_localctx);
				enterOuterAlt(_localctx, 44);
				{
				setState(411);
				match(MOUNT);
				setState(413);
				_la = _input.LA(1);
				if (_la==STREAM) {
					{
					setState(412);
					match(STREAM);
					}
				}

				setState(415);
				match(TABLE);
				setState(419);
				_la = _input.LA(1);
				if (_la==IF) {
					{
					setState(416);
					match(IF);
					setState(417);
					match(NOT);
					setState(418);
					match(EXISTS);
					}
				}

				setState(421);
				tableIdentifier();
				setState(426);
				_la = _input.LA(1);
				if (_la==T__0) {
					{
					setState(422);
					match(T__0);
					setState(423);
					((MountTableContext)_localctx).columns = colTypeList();
					setState(424);
					match(T__1);
					}
				}

				setState(428);
				match(OPTIONS);
				setState(429);
				propertyList();
				}
				break;
			case 45:
				_localctx = new MountTableWithDatasourceContext(_localctx);
				enterOuterAlt(_localctx, 45);
				{
				setState(431);
				match(WITH);
				setState(432);
				match(DATASOURCE);
				setState(433);
				((MountTableWithDatasourceContext)_localctx).ds = identifier();
				setState(434);
				match(MOUNT);
				setState(436);
				_la = _input.LA(1);
				if (_la==STREAM) {
					{
					setState(435);
					match(STREAM);
					}
				}

				setState(438);
				match(TABLE);
				setState(442);
				_la = _input.LA(1);
				if (_la==IF) {
					{
					setState(439);
					match(IF);
					setState(440);
					match(NOT);
					setState(441);
					match(EXISTS);
					}
				}

				setState(444);
				mountTableList();
				}
				break;
			case 46:
				_localctx = new RenameTableContext(_localctx);
				enterOuterAlt(_localctx, 46);
				{
				setState(446);
				match(RENAME);
				setState(447);
				match(TABLE);
				setState(448);
				((RenameTableContext)_localctx).name = tableIdentifier();
				setState(449);
				match(TO);
				setState(450);
				((RenameTableContext)_localctx).newName = tableIdentifier();
				}
				break;
			case 47:
				_localctx = new SetTableNameContext(_localctx);
				enterOuterAlt(_localctx, 47);
				{
				setState(452);
				match(ALTER);
				setState(453);
				match(TABLE);
				setState(454);
				((SetTableNameContext)_localctx).name = tableIdentifier();
				setState(455);
				match(RENAME);
				setState(456);
				match(TO);
				setState(457);
				((SetTableNameContext)_localctx).newName = tableIdentifier();
				}
				break;
			case 48:
				_localctx = new SetTablePropertiesContext(_localctx);
				enterOuterAlt(_localctx, 48);
				{
				setState(459);
				match(ALTER);
				setState(460);
				match(TABLE);
				setState(461);
				((SetTablePropertiesContext)_localctx).name = tableIdentifier();
				setState(462);
				match(SET);
				setState(463);
				match(OPTIONS);
				setState(464);
				propertyList();
				}
				break;
			case 49:
				_localctx = new AddTableColumnsContext(_localctx);
				enterOuterAlt(_localctx, 49);
				{
				setState(466);
				match(ALTER);
				setState(467);
				match(TABLE);
				setState(468);
				((AddTableColumnsContext)_localctx).name = tableIdentifier();
				setState(469);
				match(ADD);
				setState(470);
				match(COLUMNS);
				{
				setState(471);
				match(T__0);
				setState(472);
				((AddTableColumnsContext)_localctx).columns = colTypeList();
				setState(473);
				match(T__1);
				}
				}
				break;
			case 50:
				_localctx = new ChangeTableColumnContext(_localctx);
				enterOuterAlt(_localctx, 50);
				{
				setState(475);
				match(ALTER);
				setState(476);
				match(TABLE);
				setState(477);
				((ChangeTableColumnContext)_localctx).name = tableIdentifier();
				setState(478);
				match(CHANGE);
				setState(480);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,21,_ctx) ) {
				case 1:
					{
					setState(479);
					match(COLUMN);
					}
					break;
				}
				setState(482);
				identifier();
				setState(483);
				colType();
				}
				break;
			case 51:
				_localctx = new DropTableColumnContext(_localctx);
				enterOuterAlt(_localctx, 51);
				{
				setState(485);
				match(ALTER);
				setState(486);
				match(TABLE);
				setState(487);
				((DropTableColumnContext)_localctx).name = tableIdentifier();
				setState(488);
				match(DROP);
				setState(489);
				match(COLUMN);
				setState(490);
				identifier();
				}
				break;
			case 52:
				_localctx = new UnmountTableContext(_localctx);
				enterOuterAlt(_localctx, 52);
				{
				setState(492);
				match(UNMOUNT);
				setState(493);
				match(TABLE);
				setState(496);
				_la = _input.LA(1);
				if (_la==IF) {
					{
					setState(494);
					match(IF);
					setState(495);
					match(EXISTS);
					}
				}

				setState(498);
				((UnmountTableContext)_localctx).name = tableIdentifier();
				}
				break;
			case 53:
				_localctx = new CreateDatabaseContext(_localctx);
				enterOuterAlt(_localctx, 53);
				{
				setState(499);
				match(CREATE);
				setState(500);
				match(DATABASE);
				setState(504);
				_la = _input.LA(1);
				if (_la==IF) {
					{
					setState(501);
					match(IF);
					setState(502);
					match(NOT);
					setState(503);
					match(EXISTS);
					}
				}

				setState(506);
				((CreateDatabaseContext)_localctx).name = identifier();
				setState(509);
				_la = _input.LA(1);
				if (_la==COMMENT) {
					{
					setState(507);
					match(COMMENT);
					setState(508);
					((CreateDatabaseContext)_localctx).comment = match(STRING);
					}
				}

				}
				break;
			case 54:
				_localctx = new RenameDatabaseContext(_localctx);
				enterOuterAlt(_localctx, 54);
				{
				setState(511);
				match(RENAME);
				setState(512);
				match(DATABASE);
				setState(513);
				((RenameDatabaseContext)_localctx).name = identifier();
				setState(514);
				match(TO);
				setState(515);
				((RenameDatabaseContext)_localctx).newName = identifier();
				}
				break;
			case 55:
				_localctx = new SetDatabaseNameContext(_localctx);
				enterOuterAlt(_localctx, 55);
				{
				setState(517);
				match(ALTER);
				setState(518);
				match(DATABASE);
				setState(519);
				((SetDatabaseNameContext)_localctx).name = identifier();
				setState(520);
				match(RENAME);
				setState(521);
				match(TO);
				setState(522);
				((SetDatabaseNameContext)_localctx).newName = identifier();
				}
				break;
			case 56:
				_localctx = new SetDatabaseCommentContext(_localctx);
				enterOuterAlt(_localctx, 56);
				{
				setState(524);
				match(ALTER);
				setState(525);
				match(DATABASE);
				setState(526);
				((SetDatabaseCommentContext)_localctx).name = identifier();
				setState(527);
				match(SET);
				setState(528);
				match(COMMENT);
				setState(529);
				((SetDatabaseCommentContext)_localctx).comment = match(STRING);
				}
				break;
			case 57:
				_localctx = new DropDatabaseContext(_localctx);
				enterOuterAlt(_localctx, 57);
				{
				setState(531);
				match(DROP);
				setState(532);
				match(DATABASE);
				setState(535);
				_la = _input.LA(1);
				if (_la==IF) {
					{
					setState(533);
					match(IF);
					setState(534);
					match(EXISTS);
					}
				}

				setState(537);
				((DropDatabaseContext)_localctx).name = identifier();
				setState(539);
				_la = _input.LA(1);
				if (_la==CASCADE) {
					{
					setState(538);
					match(CASCADE);
					}
				}

				}
				break;
			case 58:
				_localctx = new UseDatabaseContext(_localctx);
				enterOuterAlt(_localctx, 58);
				{
				setState(541);
				match(USE);
				setState(542);
				((UseDatabaseContext)_localctx).db = identifier();
				}
				break;
			case 59:
				_localctx = new CreateFunctionContext(_localctx);
				enterOuterAlt(_localctx, 59);
				{
				setState(543);
				match(CREATE);
				setState(544);
				match(FUNCTION);
				setState(548);
				_la = _input.LA(1);
				if (_la==IF) {
					{
					setState(545);
					match(IF);
					setState(546);
					match(NOT);
					setState(547);
					match(EXISTS);
					}
				}

				setState(550);
				((CreateFunctionContext)_localctx).name = funcIdentifier();
				setState(551);
				match(OPTIONS);
				setState(552);
				propertyList();
				}
				break;
			case 60:
				_localctx = new RenameFunctionContext(_localctx);
				enterOuterAlt(_localctx, 60);
				{
				setState(554);
				match(RENAME);
				setState(555);
				match(FUNCTION);
				setState(556);
				((RenameFunctionContext)_localctx).name = funcIdentifier();
				setState(557);
				match(TO);
				setState(558);
				((RenameFunctionContext)_localctx).newName = funcIdentifier();
				}
				break;
			case 61:
				_localctx = new SetFunctionNameContext(_localctx);
				enterOuterAlt(_localctx, 61);
				{
				setState(560);
				match(ALTER);
				setState(561);
				match(FUNCTION);
				setState(562);
				((SetFunctionNameContext)_localctx).name = funcIdentifier();
				setState(563);
				match(RENAME);
				setState(564);
				match(TO);
				setState(565);
				((SetFunctionNameContext)_localctx).newName = funcIdentifier();
				}
				break;
			case 62:
				_localctx = new SetFunctionPropertiesContext(_localctx);
				enterOuterAlt(_localctx, 62);
				{
				setState(567);
				match(ALTER);
				setState(568);
				match(FUNCTION);
				setState(569);
				((SetFunctionPropertiesContext)_localctx).name = funcIdentifier();
				setState(570);
				match(SET);
				setState(571);
				match(OPTIONS);
				setState(572);
				propertyList();
				}
				break;
			case 63:
				_localctx = new DropFunctionContext(_localctx);
				enterOuterAlt(_localctx, 63);
				{
				setState(574);
				match(DROP);
				setState(575);
				match(FUNCTION);
				setState(578);
				_la = _input.LA(1);
				if (_la==IF) {
					{
					setState(576);
					match(IF);
					setState(577);
					match(EXISTS);
					}
				}

				setState(580);
				((DropFunctionContext)_localctx).name = funcIdentifier();
				}
				break;
			case 64:
				_localctx = new CreateViewContext(_localctx);
				enterOuterAlt(_localctx, 64);
				{
				setState(581);
				match(CREATE);
				setState(582);
				match(VIEW);
				setState(586);
				_la = _input.LA(1);
				if (_la==IF) {
					{
					setState(583);
					match(IF);
					setState(584);
					match(NOT);
					setState(585);
					match(EXISTS);
					}
				}

				setState(588);
				((CreateViewContext)_localctx).name = tableIdentifier();
				setState(591);
				_la = _input.LA(1);
				if (_la==COMMENT) {
					{
					setState(589);
					match(COMMENT);
					setState(590);
					((CreateViewContext)_localctx).comment = match(STRING);
					}
				}

				setState(593);
				match(AS);
				setState(594);
				query();
				}
				break;
			case 65:
				_localctx = new RenameViewContext(_localctx);
				enterOuterAlt(_localctx, 65);
				{
				setState(596);
				match(RENAME);
				setState(597);
				match(VIEW);
				setState(598);
				((RenameViewContext)_localctx).name = tableIdentifier();
				setState(599);
				match(TO);
				setState(600);
				((RenameViewContext)_localctx).newName = tableIdentifier();
				}
				break;
			case 66:
				_localctx = new SetViewNameContext(_localctx);
				enterOuterAlt(_localctx, 66);
				{
				setState(602);
				match(ALTER);
				setState(603);
				match(VIEW);
				setState(604);
				((SetViewNameContext)_localctx).name = tableIdentifier();
				setState(605);
				match(RENAME);
				setState(606);
				match(TO);
				setState(607);
				((SetViewNameContext)_localctx).newName = tableIdentifier();
				}
				break;
			case 67:
				_localctx = new SetViewCommentContext(_localctx);
				enterOuterAlt(_localctx, 67);
				{
				setState(609);
				match(ALTER);
				setState(610);
				match(VIEW);
				setState(611);
				((SetViewCommentContext)_localctx).name = tableIdentifier();
				setState(612);
				match(SET);
				setState(613);
				match(COMMENT);
				setState(614);
				((SetViewCommentContext)_localctx).comment = match(STRING);
				}
				break;
			case 68:
				_localctx = new SetViewQueryContext(_localctx);
				enterOuterAlt(_localctx, 68);
				{
				setState(616);
				match(ALTER);
				setState(617);
				match(VIEW);
				setState(618);
				((SetViewQueryContext)_localctx).name = tableIdentifier();
				setState(619);
				match(AS);
				setState(620);
				query();
				}
				break;
			case 69:
				_localctx = new DropViewContext(_localctx);
				enterOuterAlt(_localctx, 69);
				{
				setState(622);
				match(DROP);
				setState(623);
				match(VIEW);
				setState(626);
				_la = _input.LA(1);
				if (_la==IF) {
					{
					setState(624);
					match(IF);
					setState(625);
					match(EXISTS);
					}
				}

				setState(628);
				((DropViewContext)_localctx).name = tableIdentifier();
				}
				break;
			case 70:
				_localctx = new CreateApplicationContext(_localctx);
				enterOuterAlt(_localctx, 70);
				{
				setState(629);
				match(CREATE);
				setState(630);
				match(APPLICATION);
				setState(634);
				_la = _input.LA(1);
				if (_la==IF) {
					{
					setState(631);
					match(IF);
					setState(632);
					match(NOT);
					setState(633);
					match(EXISTS);
					}
				}

				setState(636);
				((CreateApplicationContext)_localctx).name = identifier();
				setState(637);
				match(AS);
				setState(638);
				appCmds();
				}
				break;
			case 71:
				_localctx = new RenameApplicationContext(_localctx);
				enterOuterAlt(_localctx, 71);
				{
				setState(640);
				match(RENAME);
				setState(641);
				match(APPLICATION);
				setState(642);
				((RenameApplicationContext)_localctx).name = identifier();
				setState(643);
				match(TO);
				setState(644);
				((RenameApplicationContext)_localctx).newName = identifier();
				}
				break;
			case 72:
				_localctx = new SetApplicationNameContext(_localctx);
				enterOuterAlt(_localctx, 72);
				{
				setState(646);
				match(ALTER);
				setState(647);
				match(APPLICATION);
				setState(648);
				((SetApplicationNameContext)_localctx).name = identifier();
				setState(649);
				match(RENAME);
				setState(650);
				match(TO);
				setState(651);
				((SetApplicationNameContext)_localctx).newName = identifier();
				}
				break;
			case 73:
				_localctx = new SetApplicationQuerysContext(_localctx);
				enterOuterAlt(_localctx, 73);
				{
				setState(653);
				match(ALTER);
				setState(654);
				match(APPLICATION);
				setState(655);
				((SetApplicationQuerysContext)_localctx).name = identifier();
				setState(656);
				match(AS);
				setState(657);
				appCmds();
				}
				break;
			case 74:
				_localctx = new DropApplicationContext(_localctx);
				enterOuterAlt(_localctx, 74);
				{
				setState(659);
				match(DROP);
				setState(660);
				match(APPLICATION);
				setState(663);
				_la = _input.LA(1);
				if (_la==IF) {
					{
					setState(661);
					match(IF);
					setState(662);
					match(EXISTS);
					}
				}

				setState(665);
				((DropApplicationContext)_localctx).name = identifier();
				}
				break;
			case 75:
				_localctx = new ShowSysInfoContext(_localctx);
				enterOuterAlt(_localctx, 75);
				{
				setState(666);
				match(SHOW);
				setState(667);
				match(SYSINFO);
				}
				break;
			case 76:
				_localctx = new ShowDatasourcesContext(_localctx);
				enterOuterAlt(_localctx, 76);
				{
				setState(668);
				match(SHOW);
				setState(669);
				match(DATASOURCES);
				setState(672);
				_la = _input.LA(1);
				if (_la==LIKE) {
					{
					setState(670);
					match(LIKE);
					setState(671);
					((ShowDatasourcesContext)_localctx).pattern = match(STRING);
					}
				}

				}
				break;
			case 77:
				_localctx = new ShowDatabaseContext(_localctx);
				enterOuterAlt(_localctx, 77);
				{
				setState(674);
				match(SHOW);
				setState(675);
				match(DATABASES);
				setState(678);
				_la = _input.LA(1);
				if (_la==LIKE) {
					{
					setState(676);
					match(LIKE);
					setState(677);
					((ShowDatabaseContext)_localctx).pattern = match(STRING);
					}
				}

				}
				break;
			case 78:
				_localctx = new ShowTablesContext(_localctx);
				enterOuterAlt(_localctx, 78);
				{
				setState(680);
				match(SHOW);
				setState(681);
				match(TABLES);
				setState(684);
				_la = _input.LA(1);
				if (_la==FROM || _la==IN) {
					{
					setState(682);
					_la = _input.LA(1);
					if ( !(_la==FROM || _la==IN) ) {
					_errHandler.recoverInline(this);
					} else {
						consume();
					}
					setState(683);
					((ShowTablesContext)_localctx).db = identifier();
					}
				}

				setState(688);
				_la = _input.LA(1);
				if (_la==LIKE) {
					{
					setState(686);
					match(LIKE);
					setState(687);
					((ShowTablesContext)_localctx).pattern = match(STRING);
					}
				}

				}
				break;
			case 79:
				_localctx = new ShowViewsContext(_localctx);
				enterOuterAlt(_localctx, 79);
				{
				setState(690);
				match(SHOW);
				setState(691);
				match(VIEWS);
				setState(694);
				_la = _input.LA(1);
				if (_la==FROM || _la==IN) {
					{
					setState(692);
					_la = _input.LA(1);
					if ( !(_la==FROM || _la==IN) ) {
					_errHandler.recoverInline(this);
					} else {
						consume();
					}
					setState(693);
					((ShowViewsContext)_localctx).db = identifier();
					}
				}

				setState(698);
				_la = _input.LA(1);
				if (_la==LIKE) {
					{
					setState(696);
					match(LIKE);
					setState(697);
					((ShowViewsContext)_localctx).pattern = match(STRING);
					}
				}

				}
				break;
			case 80:
				_localctx = new ShowFunctionsContext(_localctx);
				enterOuterAlt(_localctx, 80);
				{
				setState(700);
				match(SHOW);
				setState(701);
				match(FUNCTIONS);
				setState(704);
				_la = _input.LA(1);
				if (_la==FROM || _la==IN) {
					{
					setState(702);
					_la = _input.LA(1);
					if ( !(_la==FROM || _la==IN) ) {
					_errHandler.recoverInline(this);
					} else {
						consume();
					}
					setState(703);
					((ShowFunctionsContext)_localctx).db = identifier();
					}
				}

				setState(708);
				_la = _input.LA(1);
				if (_la==LIKE) {
					{
					setState(706);
					match(LIKE);
					setState(707);
					((ShowFunctionsContext)_localctx).pattern = match(STRING);
					}
				}

				}
				break;
			case 81:
				_localctx = new ShowUsersContext(_localctx);
				enterOuterAlt(_localctx, 81);
				{
				setState(710);
				match(SHOW);
				setState(711);
				match(USERS);
				setState(714);
				_la = _input.LA(1);
				if (_la==LIKE) {
					{
					setState(712);
					match(LIKE);
					setState(713);
					((ShowUsersContext)_localctx).pattern = match(STRING);
					}
				}

				}
				break;
			case 82:
				_localctx = new ShowGroupsContext(_localctx);
				enterOuterAlt(_localctx, 82);
				{
				setState(716);
				match(SHOW);
				setState(717);
				match(GROUPS);
				setState(720);
				_la = _input.LA(1);
				if (_la==LIKE) {
					{
					setState(718);
					match(LIKE);
					setState(719);
					((ShowGroupsContext)_localctx).pattern = match(STRING);
					}
				}

				}
				break;
			case 83:
				_localctx = new ShowApplicationsContext(_localctx);
				enterOuterAlt(_localctx, 83);
				{
				setState(722);
				match(SHOW);
				setState(723);
				match(APPLICATIONS);
				setState(726);
				_la = _input.LA(1);
				if (_la==LIKE) {
					{
					setState(724);
					match(LIKE);
					setState(725);
					((ShowApplicationsContext)_localctx).pattern = match(STRING);
					}
				}

				}
				break;
			case 84:
				_localctx = new DescDatasourceContext(_localctx);
				enterOuterAlt(_localctx, 84);
				{
				setState(728);
				_la = _input.LA(1);
				if ( !(_la==DESC || _la==DESCRIBE) ) {
				_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(729);
				match(DATASOURCE);
				setState(731);
				_la = _input.LA(1);
				if (_la==EXTENDED) {
					{
					setState(730);
					match(EXTENDED);
					}
				}

				setState(733);
				((DescDatasourceContext)_localctx).name = identifier();
				}
				break;
			case 85:
				_localctx = new DescDatabaseContext(_localctx);
				enterOuterAlt(_localctx, 85);
				{
				setState(734);
				_la = _input.LA(1);
				if ( !(_la==DESC || _la==DESCRIBE) ) {
				_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(735);
				match(DATABASE);
				setState(736);
				((DescDatabaseContext)_localctx).name = identifier();
				}
				break;
			case 86:
				_localctx = new DescTableContext(_localctx);
				enterOuterAlt(_localctx, 86);
				{
				setState(737);
				_la = _input.LA(1);
				if ( !(_la==DESC || _la==DESCRIBE) ) {
				_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(738);
				match(TABLE);
				setState(740);
				_la = _input.LA(1);
				if (_la==EXTENDED) {
					{
					setState(739);
					match(EXTENDED);
					}
				}

				setState(742);
				tableIdentifier();
				}
				break;
			case 87:
				_localctx = new DescViewContext(_localctx);
				enterOuterAlt(_localctx, 87);
				{
				setState(743);
				_la = _input.LA(1);
				if ( !(_la==DESC || _la==DESCRIBE) ) {
				_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(744);
				match(VIEW);
				setState(745);
				tableIdentifier();
				}
				break;
			case 88:
				_localctx = new DescFunctionContext(_localctx);
				enterOuterAlt(_localctx, 88);
				{
				setState(746);
				_la = _input.LA(1);
				if ( !(_la==DESC || _la==DESCRIBE) ) {
				_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(747);
				match(FUNCTION);
				setState(749);
				_la = _input.LA(1);
				if (_la==EXTENDED) {
					{
					setState(748);
					match(EXTENDED);
					}
				}

				setState(751);
				funcIdentifier();
				}
				break;
			case 89:
				_localctx = new DescUserContext(_localctx);
				enterOuterAlt(_localctx, 89);
				{
				setState(752);
				_la = _input.LA(1);
				if ( !(_la==DESC || _la==DESCRIBE) ) {
				_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(753);
				match(USER);
				setState(754);
				((DescUserContext)_localctx).name = identifier();
				}
				break;
			case 90:
				_localctx = new DescGroupContext(_localctx);
				enterOuterAlt(_localctx, 90);
				{
				setState(755);
				_la = _input.LA(1);
				if ( !(_la==DESC || _la==DESCRIBE) ) {
				_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(756);
				match(GROUP);
				setState(757);
				((DescGroupContext)_localctx).name = identifier();
				}
				break;
			case 91:
				_localctx = new SetConfigurationContext(_localctx);
				enterOuterAlt(_localctx, 91);
				{
				setState(758);
				match(SET);
				setState(759);
				property();
				}
				break;
			case 92:
				_localctx = new MqlQueryContext(_localctx);
				enterOuterAlt(_localctx, 92);
				{
				setState(760);
				query();
				}
				break;
			case 93:
				_localctx = new InsertIntoContext(_localctx);
				enterOuterAlt(_localctx, 93);
				{
				setState(761);
				insertIntoCmd();
				}
				break;
			case 94:
				_localctx = new InsertOverwriteContext(_localctx);
				enterOuterAlt(_localctx, 94);
				{
				setState(762);
				insertOverwriteCmd();
				}
				break;
			case 95:
				_localctx = new CreateTemporaryViewContext(_localctx);
				enterOuterAlt(_localctx, 95);
				{
				setState(763);
				createTemporaryViewCmd();
				}
				break;
			case 96:
				_localctx = new CreateTemporaryFunctionContext(_localctx);
				enterOuterAlt(_localctx, 96);
				{
				setState(764);
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
		enterRule(_localctx, 4, RULE_appCmds);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(770);
			_la = _input.LA(1);
			if (_la==CREATE) {
				{
				setState(767);
				nonLastCmdList();
				setState(768);
				match(T__2);
				}
			}

			setState(772);
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
		enterRule(_localctx, 6, RULE_nonLastCmdList);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(774);
			nonLastCmd();
			setState(779);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,50,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(775);
					match(T__2);
					setState(776);
					nonLastCmd();
					}
					} 
				}
				setState(781);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,50,_ctx);
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
		enterRule(_localctx, 8, RULE_nonLastCmd);
		try {
			setState(784);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,51,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(782);
				createTemporaryViewCmd();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(783);
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
		enterRule(_localctx, 10, RULE_lastCmd);
		try {
			setState(789);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,52,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(786);
				insertIntoCmd();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(787);
				insertOverwriteCmd();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(788);
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
		enterRule(_localctx, 12, RULE_insertIntoCmd);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(791);
			match(INSERT);
			setState(792);
			match(INTO);
			setState(794);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,53,_ctx) ) {
			case 1:
				{
				setState(793);
				match(TABLE);
				}
				break;
			}
			setState(796);
			tableIdentifier();
			setState(797);
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
		enterRule(_localctx, 14, RULE_insertOverwriteCmd);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(799);
			match(INSERT);
			setState(800);
			match(OVERWRITE);
			setState(801);
			match(TABLE);
			setState(802);
			tableIdentifier();
			setState(803);
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
		enterRule(_localctx, 16, RULE_createTemporaryViewCmd);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(805);
			match(CREATE);
			setState(808);
			_la = _input.LA(1);
			if (_la==OR) {
				{
				setState(806);
				match(OR);
				setState(807);
				match(REPLACE);
				}
			}

			setState(811);
			_la = _input.LA(1);
			if (_la==CACHE) {
				{
				setState(810);
				match(CACHE);
				}
			}

			setState(813);
			_la = _input.LA(1);
			if ( !(_la==TEMP || _la==TEMPORARY) ) {
			_errHandler.recoverInline(this);
			} else {
				consume();
			}
			setState(814);
			match(VIEW);
			setState(815);
			((CreateTemporaryViewCmdContext)_localctx).name = identifier();
			setState(816);
			match(AS);
			setState(817);
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
		enterRule(_localctx, 18, RULE_createTemporaryFunctionCmd);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(819);
			match(CREATE);
			setState(822);
			_la = _input.LA(1);
			if (_la==OR) {
				{
				setState(820);
				match(OR);
				setState(821);
				match(REPLACE);
				}
			}

			setState(824);
			_la = _input.LA(1);
			if ( !(_la==TEMP || _la==TEMPORARY) ) {
			_errHandler.recoverInline(this);
			} else {
				consume();
			}
			setState(825);
			match(FUNCTION);
			setState(826);
			((CreateTemporaryFunctionCmdContext)_localctx).name = identifier();
			setState(827);
			match(OPTIONS);
			setState(828);
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
		enterRule(_localctx, 20, RULE_query);
		int _la;
		try {
			int _alt;
			setState(845);
			switch (_input.LA(1)) {
			case SELECT:
				enterOuterAlt(_localctx, 1);
				{
				setState(830);
				match(SELECT);
				setState(834);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,57,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(831);
						_la = _input.LA(1);
						if ( _la <= 0 || (_la==T__3) ) {
						_errHandler.recoverInline(this);
						} else {
							consume();
						}
						}
						} 
					}
					setState(836);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,57,_ctx);
				}
				}
				break;
			case WITH:
				enterOuterAlt(_localctx, 2);
				{
				setState(837);
				ctes();
				setState(838);
				match(SELECT);
				setState(842);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,58,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(839);
						_la = _input.LA(1);
						if ( _la <= 0 || (_la==T__3) ) {
						_errHandler.recoverInline(this);
						} else {
							consume();
						}
						}
						} 
					}
					setState(844);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,58,_ctx);
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
		enterRule(_localctx, 22, RULE_ctes);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(847);
			match(WITH);
			setState(848);
			namedQuery();
			setState(853);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__2) {
				{
				{
				setState(849);
				match(T__2);
				setState(850);
				namedQuery();
				}
				}
				setState(855);
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
		enterRule(_localctx, 24, RULE_dataType);
		int _la;
		try {
			setState(890);
			switch (_input.LA(1)) {
			case ARRAY:
				_localctx = new ComplexDataTypeContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(856);
				((ComplexDataTypeContext)_localctx).complex = match(ARRAY);
				setState(857);
				match(T__4);
				setState(858);
				dataType();
				setState(859);
				match(T__5);
				}
				break;
			case MAP:
				_localctx = new ComplexDataTypeContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(861);
				((ComplexDataTypeContext)_localctx).complex = match(MAP);
				setState(862);
				match(T__4);
				setState(863);
				dataType();
				setState(864);
				match(T__2);
				setState(865);
				dataType();
				setState(866);
				match(T__5);
				}
				break;
			case STRUCT:
				_localctx = new ComplexDataTypeContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(868);
				((ComplexDataTypeContext)_localctx).complex = match(STRUCT);
				setState(875);
				switch (_input.LA(1)) {
				case T__4:
					{
					setState(869);
					match(T__4);
					setState(871);
					_la = _input.LA(1);
					if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ACCOUNT) | (1L << ADD) | (1L << ALTER) | (1L << APPLICATION) | (1L << APPLICATIONS) | (1L << AS) | (1L << CACHE) | (1L << CASCADE) | (1L << COLUMN) | (1L << COLUMNS) | (1L << DATABASE) | (1L << DATABASES) | (1L << DATASOURCE) | (1L << DATASOURCES) | (1L << FUNCTION) | (1L << FUNCTIONS) | (1L << GRANT) | (1L << GROUP) | (1L << GROUPS) | (1L << ORG))) != 0) || ((((_la - 66)) & ~0x3f) == 0 && ((1L << (_la - 66)) & ((1L << (REMOVE - 66)) | (1L << (RENAME - 66)) | (1L << (REVOKE - 66)) | (1L << (SA - 66)) | (1L << (SET - 66)) | (1L << (SHOW - 66)) | (1L << (TABLE - 66)) | (1L << (TABLES - 66)) | (1L << (TO - 66)) | (1L << (TYPE - 66)) | (1L << (USER - 66)) | (1L << (VIEW - 66)) | (1L << (VIEWS - 66)) | (1L << (WITH - 66)) | (1L << (IDENTIFIER - 66)))) != 0)) {
						{
						setState(870);
						complexColTypeList();
						}
					}

					setState(873);
					match(T__5);
					}
					break;
				case NEQ:
					{
					setState(874);
					match(NEQ);
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				break;
			case ACCOUNT:
			case ADD:
			case ALTER:
			case APPLICATION:
			case APPLICATIONS:
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
				_localctx = new PrimitiveDataTypeContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(877);
				identifier();
				setState(888);
				_la = _input.LA(1);
				if (_la==T__0) {
					{
					setState(878);
					match(T__0);
					setState(879);
					match(INTEGER_VALUE);
					setState(884);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==T__2) {
						{
						{
						setState(880);
						match(T__2);
						setState(881);
						match(INTEGER_VALUE);
						}
						}
						setState(886);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					setState(887);
					match(T__1);
					}
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
		enterRule(_localctx, 26, RULE_colTypeList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(892);
			colType();
			setState(897);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__2) {
				{
				{
				setState(893);
				match(T__2);
				setState(894);
				colType();
				}
				}
				setState(899);
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
		enterRule(_localctx, 28, RULE_colType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(900);
			identifier();
			setState(901);
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
		enterRule(_localctx, 30, RULE_complexColTypeList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(903);
			complexColType();
			setState(908);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__2) {
				{
				{
				setState(904);
				match(T__2);
				setState(905);
				complexColType();
				}
				}
				setState(910);
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
		enterRule(_localctx, 32, RULE_complexColType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(911);
			identifier();
			setState(912);
			match(T__6);
			setState(913);
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
		enterRule(_localctx, 34, RULE_namedQuery);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(915);
			((NamedQueryContext)_localctx).name = identifier();
			setState(917);
			_la = _input.LA(1);
			if (_la==AS) {
				{
				setState(916);
				match(AS);
				}
			}

			setState(919);
			match(T__0);
			setState(920);
			query();
			setState(921);
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
		enterRule(_localctx, 36, RULE_mountTableList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(923);
			mountTableOptions();
			setState(928);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__2) {
				{
				{
				setState(924);
				match(T__2);
				setState(925);
				mountTableOptions();
				}
				}
				setState(930);
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
		enterRule(_localctx, 38, RULE_mountTableOptions);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(931);
			tableIdentifier();
			setState(936);
			_la = _input.LA(1);
			if (_la==T__0) {
				{
				setState(932);
				match(T__0);
				setState(933);
				((MountTableOptionsContext)_localctx).columns = colTypeList();
				setState(934);
				match(T__1);
				}
			}

			setState(938);
			match(OPTIONS);
			setState(939);
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
		enterRule(_localctx, 40, RULE_privilegeList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(941);
			privilege();
			setState(946);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__2) {
				{
				{
				setState(942);
				match(T__2);
				setState(943);
				privilege();
				}
				}
				setState(948);
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
		enterRule(_localctx, 42, RULE_privilege);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(949);
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
		enterRule(_localctx, 44, RULE_qualifiedColumnList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(951);
			columnIdentifier();
			setState(956);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__2) {
				{
				{
				setState(952);
				match(T__2);
				setState(953);
				columnIdentifier();
				}
				}
				setState(958);
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
		enterRule(_localctx, 46, RULE_columnIdentifier);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(962);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,73,_ctx) ) {
			case 1:
				{
				setState(959);
				((ColumnIdentifierContext)_localctx).db = identifier();
				setState(960);
				match(T__7);
				}
				break;
			}
			setState(964);
			((ColumnIdentifierContext)_localctx).table = identifierStarList();
			setState(965);
			match(T__7);
			setState(966);
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
		enterRule(_localctx, 48, RULE_identifierStarList);
		int _la;
		try {
			setState(992);
			switch (_input.LA(1)) {
			case T__8:
				enterOuterAlt(_localctx, 1);
				{
				setState(968);
				match(T__8);
				setState(969);
				identifier();
				setState(974);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__2) {
					{
					{
					setState(970);
					match(T__2);
					setState(971);
					identifier();
					}
					}
					setState(976);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(977);
				match(T__9);
				}
				break;
			case T__10:
				enterOuterAlt(_localctx, 2);
				{
				setState(979);
				match(T__10);
				setState(980);
				identifier();
				setState(985);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__2) {
					{
					{
					setState(981);
					match(T__2);
					setState(982);
					identifier();
					}
					}
					setState(987);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(988);
				match(T__11);
				}
				break;
			case ACCOUNT:
			case ADD:
			case ALTER:
			case APPLICATION:
			case APPLICATIONS:
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
				setState(990);
				identifier();
				}
				break;
			case STAR:
				enterOuterAlt(_localctx, 4);
				{
				setState(991);
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
		enterRule(_localctx, 50, RULE_addUser);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(994);
			match(ADD);
			setState(995);
			match(USER);
			setState(996);
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
		enterRule(_localctx, 52, RULE_removeUser);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(998);
			match(REMOVE);
			setState(999);
			match(USER);
			setState(1000);
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
		enterRule(_localctx, 54, RULE_identifierList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1002);
			identifier();
			setState(1007);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__2) {
				{
				{
				setState(1003);
				match(T__2);
				setState(1004);
				identifier();
				}
				}
				setState(1009);
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
		enterRule(_localctx, 56, RULE_funcIdentifier);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1013);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,78,_ctx) ) {
			case 1:
				{
				setState(1010);
				((FuncIdentifierContext)_localctx).db = identifier();
				setState(1011);
				match(T__7);
				}
				break;
			}
			setState(1015);
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
		enterRule(_localctx, 58, RULE_tableIdentifier);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1020);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,79,_ctx) ) {
			case 1:
				{
				setState(1017);
				((TableIdentifierContext)_localctx).db = identifier();
				setState(1018);
				match(T__7);
				}
				break;
			}
			setState(1022);
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
		enterRule(_localctx, 60, RULE_propertyList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1024);
			match(T__0);
			setState(1025);
			property();
			setState(1030);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__2) {
				{
				{
				setState(1026);
				match(T__2);
				setState(1027);
				property();
				}
				}
				setState(1032);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(1033);
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
		enterRule(_localctx, 62, RULE_property);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1035);
			((PropertyContext)_localctx).key = propertyKey();
			setState(1040);
			_la = _input.LA(1);
			if (_la==EQ || _la==STRING) {
				{
				setState(1037);
				_la = _input.LA(1);
				if (_la==EQ) {
					{
					setState(1036);
					match(EQ);
					}
				}

				setState(1039);
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
		enterRule(_localctx, 64, RULE_propertyKey);
		int _la;
		try {
			setState(1051);
			switch (_input.LA(1)) {
			case ACCOUNT:
			case ADD:
			case ALTER:
			case APPLICATION:
			case APPLICATIONS:
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
				setState(1042);
				identifier();
				setState(1047);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__7) {
					{
					{
					setState(1043);
					match(T__7);
					setState(1044);
					identifier();
					}
					}
					setState(1049);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
				break;
			case STRING:
				enterOuterAlt(_localctx, 2);
				{
				setState(1050);
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
		enterRule(_localctx, 66, RULE_password);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1056);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,85,_ctx);
			while ( _alt!=1 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1+1 ) {
					{
					{
					setState(1053);
					matchWildcard();
					}
					} 
				}
				setState(1058);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,85,_ctx);
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
		enterRule(_localctx, 68, RULE_identifier);
		try {
			setState(1061);
			switch (_input.LA(1)) {
			case IDENTIFIER:
				enterOuterAlt(_localctx, 1);
				{
				setState(1059);
				match(IDENTIFIER);
				}
				break;
			case ACCOUNT:
			case ADD:
			case ALTER:
			case APPLICATION:
			case APPLICATIONS:
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
				setState(1060);
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
		enterRule(_localctx, 70, RULE_generalIdentifier);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1064);
			_la = _input.LA(1);
			if (_la==T__12) {
				{
				setState(1063);
				match(T__12);
				}
			}

			setState(1066);
			identifier();
			setState(1071);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__7 || _la==T__12) {
				{
				{
				setState(1067);
				_la = _input.LA(1);
				if ( !(_la==T__7 || _la==T__12) ) {
				_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(1068);
				identifier();
				}
				}
				setState(1073);
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
		enterRule(_localctx, 72, RULE_nonReserved);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1074);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ACCOUNT) | (1L << ADD) | (1L << ALTER) | (1L << APPLICATION) | (1L << APPLICATIONS) | (1L << AS) | (1L << CACHE) | (1L << CASCADE) | (1L << COLUMN) | (1L << COLUMNS) | (1L << DATABASE) | (1L << DATABASES) | (1L << DATASOURCE) | (1L << DATASOURCES) | (1L << FUNCTION) | (1L << FUNCTIONS) | (1L << GRANT) | (1L << GROUP) | (1L << GROUPS) | (1L << ORG))) != 0) || ((((_la - 66)) & ~0x3f) == 0 && ((1L << (_la - 66)) & ((1L << (REMOVE - 66)) | (1L << (RENAME - 66)) | (1L << (REVOKE - 66)) | (1L << (SA - 66)) | (1L << (SET - 66)) | (1L << (SHOW - 66)) | (1L << (TABLE - 66)) | (1L << (TABLES - 66)) | (1L << (TO - 66)) | (1L << (TYPE - 66)) | (1L << (USER - 66)) | (1L << (VIEW - 66)) | (1L << (VIEWS - 66)) | (1L << (WITH - 66)))) != 0)) ) {
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
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3c\u0437\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\3\2\3\2\3\2\3\3\3\3\3\3\3\3\3\3\5"+
		"\3U\n\3\3\3\3\3\3\3\5\3Z\n\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3t\n\3\3\3\3"+
		"\3\5\3x\n\3\3\3\3\3\3\3\3\3\3\3\5\3\177\n\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\5"+
		"\3\u00aa\n\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3\u011e\n\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3\u013d\n\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\5\3\u0145\n\3\3\3\3\3\3\3\5\3\u014a\n\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\5\3\u0165\n\3\3\3\3\3\3\3\3\3\3\3\5\3\u016c\n\3\3\3\3\3\3\3\3\3"+
		"\5\3\u0172\n\3\3\3\3\3\5\3\u0176\n\3\3\3\3\3\3\3\3\3\3\3\5\3\u017d\n\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3\u019b\n\3\3\3\3\3\3\3\5"+
		"\3\u01a0\n\3\3\3\3\3\3\3\3\3\5\3\u01a6\n\3\3\3\3\3\3\3\3\3\3\3\5\3\u01ad"+
		"\n\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3\u01b7\n\3\3\3\3\3\3\3\3\3\5\3"+
		"\u01bd\n\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\5\3\u01e3\n\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\5\3\u01f3\n\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3\u01fb\n\3\3\3"+
		"\3\3\3\3\5\3\u0200\n\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3\u021a\n\3\3\3\3\3"+
		"\5\3\u021e\n\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3\u0227\n\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3\u0245\n\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3"+
		"\u024d\n\3\3\3\3\3\3\3\5\3\u0252\n\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3\u0275\n\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3"+
		"\u027d\n\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3\u029a\n\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\5\3\u02a3\n\3\3\3\3\3\3\3\3\3\5\3\u02a9\n\3\3\3\3"+
		"\3\3\3\3\3\5\3\u02af\n\3\3\3\3\3\5\3\u02b3\n\3\3\3\3\3\3\3\3\3\5\3\u02b9"+
		"\n\3\3\3\3\3\5\3\u02bd\n\3\3\3\3\3\3\3\3\3\5\3\u02c3\n\3\3\3\3\3\5\3\u02c7"+
		"\n\3\3\3\3\3\3\3\3\3\5\3\u02cd\n\3\3\3\3\3\3\3\3\3\5\3\u02d3\n\3\3\3\3"+
		"\3\3\3\3\3\5\3\u02d9\n\3\3\3\3\3\3\3\5\3\u02de\n\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\5\3\u02e7\n\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3\u02f0\n\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3\u0300\n\3\3\4\3"+
		"\4\3\4\5\4\u0305\n\4\3\4\3\4\3\5\3\5\3\5\7\5\u030c\n\5\f\5\16\5\u030f"+
		"\13\5\3\6\3\6\5\6\u0313\n\6\3\7\3\7\3\7\5\7\u0318\n\7\3\b\3\b\3\b\5\b"+
		"\u031d\n\b\3\b\3\b\3\b\3\t\3\t\3\t\3\t\3\t\3\t\3\n\3\n\3\n\5\n\u032b\n"+
		"\n\3\n\5\n\u032e\n\n\3\n\3\n\3\n\3\n\3\n\3\n\3\13\3\13\3\13\5\13\u0339"+
		"\n\13\3\13\3\13\3\13\3\13\3\13\3\13\3\f\3\f\7\f\u0343\n\f\f\f\16\f\u0346"+
		"\13\f\3\f\3\f\3\f\7\f\u034b\n\f\f\f\16\f\u034e\13\f\5\f\u0350\n\f\3\r"+
		"\3\r\3\r\3\r\7\r\u0356\n\r\f\r\16\r\u0359\13\r\3\16\3\16\3\16\3\16\3\16"+
		"\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\5\16\u036a\n\16\3\16"+
		"\3\16\5\16\u036e\n\16\3\16\3\16\3\16\3\16\3\16\7\16\u0375\n\16\f\16\16"+
		"\16\u0378\13\16\3\16\5\16\u037b\n\16\5\16\u037d\n\16\3\17\3\17\3\17\7"+
		"\17\u0382\n\17\f\17\16\17\u0385\13\17\3\20\3\20\3\20\3\21\3\21\3\21\7"+
		"\21\u038d\n\21\f\21\16\21\u0390\13\21\3\22\3\22\3\22\3\22\3\23\3\23\5"+
		"\23\u0398\n\23\3\23\3\23\3\23\3\23\3\24\3\24\3\24\7\24\u03a1\n\24\f\24"+
		"\16\24\u03a4\13\24\3\25\3\25\3\25\3\25\3\25\5\25\u03ab\n\25\3\25\3\25"+
		"\3\25\3\26\3\26\3\26\7\26\u03b3\n\26\f\26\16\26\u03b6\13\26\3\27\3\27"+
		"\3\30\3\30\3\30\7\30\u03bd\n\30\f\30\16\30\u03c0\13\30\3\31\3\31\3\31"+
		"\5\31\u03c5\n\31\3\31\3\31\3\31\3\31\3\32\3\32\3\32\3\32\7\32\u03cf\n"+
		"\32\f\32\16\32\u03d2\13\32\3\32\3\32\3\32\3\32\3\32\3\32\7\32\u03da\n"+
		"\32\f\32\16\32\u03dd\13\32\3\32\3\32\3\32\3\32\5\32\u03e3\n\32\3\33\3"+
		"\33\3\33\3\33\3\34\3\34\3\34\3\34\3\35\3\35\3\35\7\35\u03f0\n\35\f\35"+
		"\16\35\u03f3\13\35\3\36\3\36\3\36\5\36\u03f8\n\36\3\36\3\36\3\37\3\37"+
		"\3\37\5\37\u03ff\n\37\3\37\3\37\3 \3 \3 \3 \7 \u0407\n \f \16 \u040a\13"+
		" \3 \3 \3!\3!\5!\u0410\n!\3!\5!\u0413\n!\3\"\3\"\3\"\7\"\u0418\n\"\f\""+
		"\16\"\u041b\13\"\3\"\5\"\u041e\n\"\3#\7#\u0421\n#\f#\16#\u0424\13#\3$"+
		"\3$\5$\u0428\n$\3%\5%\u042b\n%\3%\3%\3%\7%\u0430\n%\f%\16%\u0433\13%\3"+
		"&\3&\3&\3\u0422\2\'\2\4\6\b\n\f\16\20\22\24\26\30\32\34\36 \"$&(*,.\60"+
		"\62\64\668:<>@BDFHJ\2\n\3\2AB\4\2//\67\67\3\2&\'\3\2QR\3\2\6\6\5\2\20"+
		"\20%%))\4\2\n\n\17\17\17\2\20\24\30\30\32\35!$\60\64AADEGHJKOPSTWWY[\u04cd"+
		"\2L\3\2\2\2\4\u02ff\3\2\2\2\6\u0304\3\2\2\2\b\u0308\3\2\2\2\n\u0312\3"+
		"\2\2\2\f\u0317\3\2\2\2\16\u0319\3\2\2\2\20\u0321\3\2\2\2\22\u0327\3\2"+
		"\2\2\24\u0335\3\2\2\2\26\u034f\3\2\2\2\30\u0351\3\2\2\2\32\u037c\3\2\2"+
		"\2\34\u037e\3\2\2\2\36\u0386\3\2\2\2 \u0389\3\2\2\2\"\u0391\3\2\2\2$\u0395"+
		"\3\2\2\2&\u039d\3\2\2\2(\u03a5\3\2\2\2*\u03af\3\2\2\2,\u03b7\3\2\2\2."+
		"\u03b9\3\2\2\2\60\u03c4\3\2\2\2\62\u03e2\3\2\2\2\64\u03e4\3\2\2\2\66\u03e8"+
		"\3\2\2\28\u03ec\3\2\2\2:\u03f7\3\2\2\2<\u03fe\3\2\2\2>\u0402\3\2\2\2@"+
		"\u040d\3\2\2\2B\u041d\3\2\2\2D\u0422\3\2\2\2F\u0427\3\2\2\2H\u042a\3\2"+
		"\2\2J\u0434\3\2\2\2LM\5\4\3\2MN\7\2\2\3N\3\3\2\2\2OP\7 \2\2PT\t\2\2\2"+
		"QR\7\66\2\2RS\7<\2\2SU\7-\2\2TQ\3\2\2\2TU\3\2\2\2UV\3\2\2\2VY\5F$\2WX"+
		"\7\36\2\2XZ\7\\\2\2YW\3\2\2\2YZ\3\2\2\2Z\u0300\3\2\2\2[\\\7E\2\2\\]\t"+
		"\2\2\2]^\5F$\2^_\7S\2\2_`\5F$\2`\u0300\3\2\2\2ab\7\22\2\2bc\t\2\2\2cd"+
		"\5F$\2de\7E\2\2ef\7S\2\2fg\5F$\2g\u0300\3\2\2\2hi\7\22\2\2ij\t\2\2\2j"+
		"k\5F$\2kl\7J\2\2lm\7\36\2\2mn\7\\\2\2n\u0300\3\2\2\2op\7*\2\2ps\t\2\2"+
		"\2qr\7\66\2\2rt\7-\2\2sq\3\2\2\2st\3\2\2\2tu\3\2\2\2uw\5F$\2vx\7\33\2"+
		"\2wv\3\2\2\2wx\3\2\2\2x\u0300\3\2\2\2yz\7 \2\2z~\7H\2\2{|\7\66\2\2|}\7"+
		"<\2\2}\177\7-\2\2~{\3\2\2\2~\177\3\2\2\2\177\u0080\3\2\2\2\u0080\u0081"+
		"\5F$\2\u0081\u0082\7\67\2\2\u0082\u0083\t\2\2\2\u0083\u0084\5F$\2\u0084"+
		"\u0085\7\65\2\2\u0085\u0086\7\31\2\2\u0086\u0087\5D#\2\u0087\u0300\3\2"+
		"\2\2\u0088\u0089\7E\2\2\u0089\u008a\7H\2\2\u008a\u008b\5F$\2\u008b\u008c"+
		"\7S\2\2\u008c\u008d\5F$\2\u008d\u008e\7\67\2\2\u008e\u008f\t\2\2\2\u008f"+
		"\u0090\5F$\2\u0090\u0300\3\2\2\2\u0091\u0092\7\22\2\2\u0092\u0093\7H\2"+
		"\2\u0093\u0094\5F$\2\u0094\u0095\7E\2\2\u0095\u0096\7S\2\2\u0096\u0097"+
		"\5F$\2\u0097\u0098\7\67\2\2\u0098\u0099\t\2\2\2\u0099\u009a\5F$\2\u009a"+
		"\u0300\3\2\2\2\u009b\u009c\7\22\2\2\u009c\u009d\7H\2\2\u009d\u009e\5F"+
		"$\2\u009e\u009f\7\65\2\2\u009f\u00a0\7\31\2\2\u00a0\u00a1\5D#\2\u00a1"+
		"\u00a2\7\67\2\2\u00a2\u00a3\t\2\2\2\u00a3\u00a4\5F$\2\u00a4\u0300\3\2"+
		"\2\2\u00a5\u00a6\7*\2\2\u00a6\u00a9\7H\2\2\u00a7\u00a8\7\66\2\2\u00a8"+
		"\u00aa\7-\2\2\u00a9\u00a7\3\2\2\2\u00a9\u00aa\3\2\2\2\u00aa\u00ab\3\2"+
		"\2\2\u00ab\u00ac\5F$\2\u00ac\u00ad\7\67\2\2\u00ad\u00ae\t\2\2\2\u00ae"+
		"\u00af\5F$\2\u00af\u0300\3\2\2\2\u00b0\u00b1\7\62\2\2\u00b1\u00b2\7\62"+
		"\2\2\u00b2\u00b3\7>\2\2\u00b3\u00b4\5*\26\2\u00b4\u00b5\7S\2\2\u00b5\u00b6"+
		"\7W\2\2\u00b6\u00b7\58\35\2\u00b7\u0300\3\2\2\2\u00b8\u00b9\7\62\2\2\u00b9"+
		"\u00ba\7\62\2\2\u00ba\u00bb\7>\2\2\u00bb\u00bc\5*\26\2\u00bc\u00bd\7S"+
		"\2\2\u00bd\u00be\7\63\2\2\u00be\u00bf\58\35\2\u00bf\u0300\3\2\2\2\u00c0"+
		"\u00c1\7G\2\2\u00c1\u00c2\7\62\2\2\u00c2\u00c3\7>\2\2\u00c3\u00c4\5*\26"+
		"\2\u00c4\u00c5\7/\2\2\u00c5\u00c6\7W\2\2\u00c6\u00c7\58\35\2\u00c7\u0300"+
		"\3\2\2\2\u00c8\u00c9\7G\2\2\u00c9\u00ca\7\62\2\2\u00ca\u00cb\7>\2\2\u00cb"+
		"\u00cc\5*\26\2\u00cc\u00cd\7/\2\2\u00cd\u00ce\7\63\2\2\u00ce\u00cf\58"+
		"\35\2\u00cf\u0300\3\2\2\2\u00d0\u00d1\7\62\2\2\u00d1\u00d2\7\20\2\2\u00d2"+
		"\u00d3\7S\2\2\u00d3\u00d4\7W\2\2\u00d4\u0300\58\35\2\u00d5\u00d6\7\62"+
		"\2\2\u00d6\u00d7\7\20\2\2\u00d7\u00d8\7S\2\2\u00d8\u00d9\7\63\2\2\u00d9"+
		"\u0300\58\35\2\u00da\u00db\7G\2\2\u00db\u00dc\7\20\2\2\u00dc\u00dd\7/"+
		"\2\2\u00dd\u00de\7W\2\2\u00de\u0300\58\35\2\u00df\u00e0\7G\2\2\u00e0\u00e1"+
		"\7\20\2\2\u00e1\u00e2\7/\2\2\u00e2\u00e3\7\63\2\2\u00e3\u0300\58\35\2"+
		"\u00e4\u00e5\7\62\2\2\u00e5\u00e6\7%\2\2\u00e6\u00e7\7S\2\2\u00e7\u00e8"+
		"\7W\2\2\u00e8\u0300\58\35\2\u00e9\u00ea\7\62\2\2\u00ea\u00eb\7%\2\2\u00eb"+
		"\u00ec\7S\2\2\u00ec\u00ed\7\63\2\2\u00ed\u0300\58\35\2\u00ee\u00ef\7G"+
		"\2\2\u00ef\u00f0\7%\2\2\u00f0\u00f1\7/\2\2\u00f1\u00f2\7W\2\2\u00f2\u0300"+
		"\58\35\2\u00f3\u00f4\7G\2\2\u00f4\u00f5\7%\2\2\u00f5\u00f6\7/\2\2\u00f6"+
		"\u00f7\7\63\2\2\u00f7\u0300\58\35\2\u00f8\u00f9\7\62\2\2\u00f9\u00fa\7"+
		"(\2\2\u00fa\u00fb\7=\2\2\u00fb\u00fc\5.\30\2\u00fc\u00fd\7S\2\2\u00fd"+
		"\u00fe\7W\2\2\u00fe\u00ff\58\35\2\u00ff\u0300\3\2\2\2\u0100\u0101\7\62"+
		"\2\2\u0101\u0102\7(\2\2\u0102\u0103\7=\2\2\u0103\u0104\5.\30\2\u0104\u0105"+
		"\7S\2\2\u0105\u0106\7\63\2\2\u0106\u0107\58\35\2\u0107\u0300\3\2\2\2\u0108"+
		"\u0109\7G\2\2\u0109\u010a\7(\2\2\u010a\u010b\7=\2\2\u010b\u010c\5.\30"+
		"\2\u010c\u010d\7/\2\2\u010d\u010e\7W\2\2\u010e\u010f\58\35\2\u010f\u0300"+
		"\3\2\2\2\u0110\u0111\7G\2\2\u0111\u0112\7(\2\2\u0112\u0113\7=\2\2\u0113"+
		"\u0114\5.\30\2\u0114\u0115\7/\2\2\u0115\u0116\7\63\2\2\u0116\u0117\58"+
		"\35\2\u0117\u0300\3\2\2\2\u0118\u0119\7 \2\2\u0119\u011d\7W\2\2\u011a"+
		"\u011b\7\66\2\2\u011b\u011c\7<\2\2\u011c\u011e\7-\2\2\u011d\u011a\3\2"+
		"\2\2\u011d\u011e\3\2\2\2\u011e\u011f\3\2\2\2\u011f\u0120\5F$\2\u0120\u0121"+
		"\7\65\2\2\u0121\u0122\7\31\2\2\u0122\u0123\5D#\2\u0123\u0300\3\2\2\2\u0124"+
		"\u0125\7E\2\2\u0125\u0126\7W\2\2\u0126\u0127\5F$\2\u0127\u0128\7S\2\2"+
		"\u0128\u0129\5F$\2\u0129\u0300\3\2\2\2\u012a\u012b\7\22\2\2\u012b\u012c"+
		"\7W\2\2\u012c\u012d\5F$\2\u012d\u012e\7E\2\2\u012e\u012f\7S\2\2\u012f"+
		"\u0130\5F$\2\u0130\u0300\3\2\2\2\u0131\u0132\7\22\2\2\u0132\u0133\7W\2"+
		"\2\u0133\u0134\5F$\2\u0134\u0135\7\65\2\2\u0135\u0136\7\31\2\2\u0136\u0137"+
		"\5D#\2\u0137\u0300\3\2\2\2\u0138\u0139\7*\2\2\u0139\u013c\7W\2\2\u013a"+
		"\u013b\7\66\2\2\u013b\u013d\7-\2\2\u013c\u013a\3\2\2\2\u013c\u013d\3\2"+
		"\2\2\u013d\u013e\3\2\2\2\u013e\u0300\5F$\2\u013f\u0140\7 \2\2\u0140\u0144"+
		"\7\63\2\2\u0141\u0142\7\66\2\2\u0142\u0143\7<\2\2\u0143\u0145\7-\2\2\u0144"+
		"\u0141\3\2\2\2\u0144\u0145\3\2\2\2\u0145\u0146\3\2\2\2\u0146\u0149\5F"+
		"$\2\u0147\u0148\7\36\2\2\u0148\u014a\7\\\2\2\u0149\u0147\3\2\2\2\u0149"+
		"\u014a\3\2\2\2\u014a\u0300\3\2\2\2\u014b\u014c\7E\2\2\u014c\u014d\7\63"+
		"\2\2\u014d\u014e\5F$\2\u014e\u014f\7S\2\2\u014f\u0150\5F$\2\u0150\u0300"+
		"\3\2\2\2\u0151\u0152\7\22\2\2\u0152\u0153\7\63\2\2\u0153\u0154\5F$\2\u0154"+
		"\u0155\7E\2\2\u0155\u0156\7S\2\2\u0156\u0157\5F$\2\u0157\u0300\3\2\2\2"+
		"\u0158\u0159\7\22\2\2\u0159\u015a\7\63\2\2\u015a\u015b\5F$\2\u015b\u015c"+
		"\7J\2\2\u015c\u015d\7\36\2\2\u015d\u015e\7\\\2\2\u015e\u0300\3\2\2\2\u015f"+
		"\u0160\7\22\2\2\u0160\u0161\7\63\2\2\u0161\u0162\5F$\2\u0162\u0164\5\64"+
		"\33\2\u0163\u0165\5\66\34\2\u0164\u0163\3\2\2\2\u0164\u0165\3\2\2\2\u0165"+
		"\u0300\3\2\2\2\u0166\u0167\7\22\2\2\u0167\u0168\7\63\2\2\u0168\u0169\5"+
		"F$\2\u0169\u016b\5\66\34\2\u016a\u016c\5\64\33\2\u016b\u016a\3\2\2\2\u016b"+
		"\u016c\3\2\2\2\u016c\u0300\3\2\2\2\u016d\u016e\7*\2\2\u016e\u0171\7\63"+
		"\2\2\u016f\u0170\7\66\2\2\u0170\u0172\7-\2\2\u0171\u016f\3\2\2\2\u0171"+
		"\u0172\3\2\2\2\u0172\u0173\3\2\2\2\u0173\u0175\5F$\2\u0174\u0176\7\33"+
		"\2\2\u0175\u0174\3\2\2\2\u0175\u0176\3\2\2\2\u0176\u0300\3\2\2\2\u0177"+
		"\u0178\7;\2\2\u0178\u017c\7#\2\2\u0179\u017a\7\66\2\2\u017a\u017b\7<\2"+
		"\2\u017b\u017d\7-\2\2\u017c\u0179\3\2\2\2\u017c\u017d\3\2\2\2\u017d\u017e"+
		"\3\2\2\2\u017e\u017f\5F$\2\u017f\u0180\7?\2\2\u0180\u0181\5> \2\u0181"+
		"\u0300\3\2\2\2\u0182\u0183\7E\2\2\u0183\u0184\7#\2\2\u0184\u0185\5F$\2"+
		"\u0185\u0186\7S\2\2\u0186\u0187\5F$\2\u0187\u0300\3\2\2\2\u0188\u0189"+
		"\7\22\2\2\u0189\u018a\7#\2\2\u018a\u018b\5F$\2\u018b\u018c\7E\2\2\u018c"+
		"\u018d\7S\2\2\u018d\u018e\5F$\2\u018e\u0300\3\2\2\2\u018f\u0190\7\22\2"+
		"\2\u0190\u0191\7#\2\2\u0191\u0192\5F$\2\u0192\u0193\7J\2\2\u0193\u0194"+
		"\7?\2\2\u0194\u0195\5> \2\u0195\u0300\3\2\2\2\u0196\u0197\7U\2\2\u0197"+
		"\u019a\7#\2\2\u0198\u0199\7\66\2\2\u0199\u019b\7-\2\2\u019a\u0198\3\2"+
		"\2\2\u019a\u019b\3\2\2\2\u019b\u019c\3\2\2\2\u019c\u0300\5F$\2\u019d\u019f"+
		"\7;\2\2\u019e\u01a0\7M\2\2\u019f\u019e\3\2\2\2\u019f\u01a0\3\2\2\2\u01a0"+
		"\u01a1\3\2\2\2\u01a1\u01a5\7O\2\2\u01a2\u01a3\7\66\2\2\u01a3\u01a4\7<"+
		"\2\2\u01a4\u01a6\7-\2\2\u01a5\u01a2\3\2\2\2\u01a5\u01a6\3\2\2\2\u01a6"+
		"\u01a7\3\2\2\2\u01a7\u01ac\5<\37\2\u01a8\u01a9\7\3\2\2\u01a9\u01aa\5\34"+
		"\17\2\u01aa\u01ab\7\4\2\2\u01ab\u01ad\3\2\2\2\u01ac\u01a8\3\2\2\2\u01ac"+
		"\u01ad\3\2\2\2\u01ad\u01ae\3\2\2\2\u01ae\u01af\7?\2\2\u01af\u01b0\5> "+
		"\2\u01b0\u0300\3\2\2\2\u01b1\u01b2\7[\2\2\u01b2\u01b3\7#\2\2\u01b3\u01b4"+
		"\5F$\2\u01b4\u01b6\7;\2\2\u01b5\u01b7\7M\2\2\u01b6\u01b5\3\2\2\2\u01b6"+
		"\u01b7\3\2\2\2\u01b7\u01b8\3\2\2\2\u01b8\u01bc\7O\2\2\u01b9\u01ba\7\66"+
		"\2\2\u01ba\u01bb\7<\2\2\u01bb\u01bd\7-\2\2\u01bc\u01b9\3\2\2\2\u01bc\u01bd"+
		"\3\2\2\2\u01bd\u01be\3\2\2\2\u01be\u01bf\5&\24\2\u01bf\u0300\3\2\2\2\u01c0"+
		"\u01c1\7E\2\2\u01c1\u01c2\7O\2\2\u01c2\u01c3\5<\37\2\u01c3\u01c4\7S\2"+
		"\2\u01c4\u01c5\5<\37\2\u01c5\u0300\3\2\2\2\u01c6\u01c7\7\22\2\2\u01c7"+
		"\u01c8\7O\2\2\u01c8\u01c9\5<\37\2\u01c9\u01ca\7E\2\2\u01ca\u01cb\7S\2"+
		"\2\u01cb\u01cc\5<\37\2\u01cc\u0300\3\2\2\2\u01cd\u01ce\7\22\2\2\u01ce"+
		"\u01cf\7O\2\2\u01cf\u01d0\5<\37\2\u01d0\u01d1\7J\2\2\u01d1\u01d2\7?\2"+
		"\2\u01d2\u01d3\5> \2\u01d3\u0300\3\2\2\2\u01d4\u01d5\7\22\2\2\u01d5\u01d6"+
		"\7O\2\2\u01d6\u01d7\5<\37\2\u01d7\u01d8\7\21\2\2\u01d8\u01d9\7\35\2\2"+
		"\u01d9\u01da\7\3\2\2\u01da\u01db\5\34\17\2\u01db\u01dc\7\4\2\2\u01dc\u0300"+
		"\3\2\2\2\u01dd\u01de\7\22\2\2\u01de\u01df\7O\2\2\u01df\u01e0\5<\37\2\u01e0"+
		"\u01e2\7\37\2\2\u01e1\u01e3\7\34\2\2\u01e2\u01e1\3\2\2\2\u01e2\u01e3\3"+
		"\2\2\2\u01e3\u01e4\3\2\2\2\u01e4\u01e5\5F$\2\u01e5\u01e6\5\36\20\2\u01e6"+
		"\u0300\3\2\2\2\u01e7\u01e8\7\22\2\2\u01e8\u01e9\7O\2\2\u01e9\u01ea\5<"+
		"\37\2\u01ea\u01eb\7*\2\2\u01eb\u01ec\7\34\2\2\u01ec\u01ed\5F$\2\u01ed"+
		"\u0300\3\2\2\2\u01ee\u01ef\7U\2\2\u01ef\u01f2\7O\2\2\u01f0\u01f1\7\66"+
		"\2\2\u01f1\u01f3\7-\2\2\u01f2\u01f0\3\2\2\2\u01f2\u01f3\3\2\2\2\u01f3"+
		"\u01f4\3\2\2\2\u01f4\u0300\5<\37\2\u01f5\u01f6\7 \2\2\u01f6\u01fa\7!\2"+
		"\2\u01f7\u01f8\7\66\2\2\u01f8\u01f9\7<\2\2\u01f9\u01fb\7-\2\2\u01fa\u01f7"+
		"\3\2\2\2\u01fa\u01fb\3\2\2\2\u01fb\u01fc\3\2\2\2\u01fc\u01ff\5F$\2\u01fd"+
		"\u01fe\7\36\2\2\u01fe\u0200\7\\\2\2\u01ff\u01fd\3\2\2\2\u01ff\u0200\3"+
		"\2\2\2\u0200\u0300\3\2\2\2\u0201\u0202\7E\2\2\u0202\u0203\7!\2\2\u0203"+
		"\u0204\5F$\2\u0204\u0205\7S\2\2\u0205\u0206\5F$\2\u0206\u0300\3\2\2\2"+
		"\u0207\u0208\7\22\2\2\u0208\u0209\7!\2\2\u0209\u020a\5F$\2\u020a\u020b"+
		"\7E\2\2\u020b\u020c\7S\2\2\u020c\u020d\5F$\2\u020d\u0300\3\2\2\2\u020e"+
		"\u020f\7\22\2\2\u020f\u0210\7!\2\2\u0210\u0211\5F$\2\u0211\u0212\7J\2"+
		"\2\u0212\u0213\7\36\2\2\u0213\u0214\7\\\2\2\u0214\u0300\3\2\2\2\u0215"+
		"\u0216\7*\2\2\u0216\u0219\7!\2\2\u0217\u0218\7\66\2\2\u0218\u021a\7-\2"+
		"\2\u0219\u0217\3\2\2\2\u0219\u021a\3\2\2\2\u021a\u021b\3\2\2\2\u021b\u021d"+
		"\5F$\2\u021c\u021e\7\33\2\2\u021d\u021c\3\2\2\2\u021d\u021e\3\2\2\2\u021e"+
		"\u0300\3\2\2\2\u021f\u0220\7V\2\2\u0220\u0300\5F$\2\u0221\u0222\7 \2\2"+
		"\u0222\u0226\7\60\2\2\u0223\u0224\7\66\2\2\u0224\u0225\7<\2\2\u0225\u0227"+
		"\7-\2\2\u0226\u0223\3\2\2\2\u0226\u0227\3\2\2\2\u0227\u0228\3\2\2\2\u0228"+
		"\u0229\5:\36\2\u0229\u022a\7?\2\2\u022a\u022b\5> \2\u022b\u0300\3\2\2"+
		"\2\u022c\u022d\7E\2\2\u022d\u022e\7\60\2\2\u022e\u022f\5:\36\2\u022f\u0230"+
		"\7S\2\2\u0230\u0231\5:\36\2\u0231\u0300\3\2\2\2\u0232\u0233\7\22\2\2\u0233"+
		"\u0234\7\60\2\2\u0234\u0235\5:\36\2\u0235\u0236\7E\2\2\u0236\u0237\7S"+
		"\2\2\u0237\u0238\5:\36\2\u0238\u0300\3\2\2\2\u0239\u023a\7\22\2\2\u023a"+
		"\u023b\7\60\2\2\u023b\u023c\5:\36\2\u023c\u023d\7J\2\2\u023d\u023e\7?"+
		"\2\2\u023e\u023f\5> \2\u023f\u0300\3\2\2\2\u0240\u0241\7*\2\2\u0241\u0244"+
		"\7\60\2\2\u0242\u0243\7\66\2\2\u0243\u0245\7-\2\2\u0244\u0242\3\2\2\2"+
		"\u0244\u0245\3\2\2\2\u0245\u0246\3\2\2\2\u0246\u0300\5:\36\2\u0247\u0248"+
		"\7 \2\2\u0248\u024c\7Y\2\2\u0249\u024a\7\66\2\2\u024a\u024b\7<\2\2\u024b"+
		"\u024d\7-\2\2\u024c\u0249\3\2\2\2\u024c\u024d\3\2\2\2\u024d\u024e\3\2"+
		"\2\2\u024e\u0251\5<\37\2\u024f\u0250\7\36\2\2\u0250\u0252\7\\\2\2\u0251"+
		"\u024f\3\2\2\2\u0251\u0252\3\2\2\2\u0252\u0253\3\2\2\2\u0253\u0254\7\30"+
		"\2\2\u0254\u0255\5\26\f\2\u0255\u0300\3\2\2\2\u0256\u0257\7E\2\2\u0257"+
		"\u0258\7Y\2\2\u0258\u0259\5<\37\2\u0259\u025a\7S\2\2\u025a\u025b\5<\37"+
		"\2\u025b\u0300\3\2\2\2\u025c\u025d\7\22\2\2\u025d\u025e\7Y\2\2\u025e\u025f"+
		"\5<\37\2\u025f\u0260\7E\2\2\u0260\u0261\7S\2\2\u0261\u0262\5<\37\2\u0262"+
		"\u0300\3\2\2\2\u0263\u0264\7\22\2\2\u0264\u0265\7Y\2\2\u0265\u0266\5<"+
		"\37\2\u0266\u0267\7J\2\2\u0267\u0268\7\36\2\2\u0268\u0269\7\\\2\2\u0269"+
		"\u0300\3\2\2\2\u026a\u026b\7\22\2\2\u026b\u026c\7Y\2\2\u026c\u026d\5<"+
		"\37\2\u026d\u026e\7\30\2\2\u026e\u026f\5\26\f\2\u026f\u0300\3\2\2\2\u0270"+
		"\u0271\7*\2\2\u0271\u0274\7Y\2\2\u0272\u0273\7\66\2\2\u0273\u0275\7-\2"+
		"\2\u0274\u0272\3\2\2\2\u0274\u0275\3\2\2\2\u0275\u0276\3\2\2\2\u0276\u0300"+
		"\5<\37\2\u0277\u0278\7 \2\2\u0278\u027c\7\23\2\2\u0279\u027a\7\66\2\2"+
		"\u027a\u027b\7<\2\2\u027b\u027d\7-\2\2\u027c\u0279\3\2\2\2\u027c\u027d"+
		"\3\2\2\2\u027d\u027e\3\2\2\2\u027e\u027f\5F$\2\u027f\u0280\7\30\2\2\u0280"+
		"\u0281\5\6\4\2\u0281\u0300\3\2\2\2\u0282\u0283\7E\2\2\u0283\u0284\7\23"+
		"\2\2\u0284\u0285\5F$\2\u0285\u0286\7S\2\2\u0286\u0287\5F$\2\u0287\u0300"+
		"\3\2\2\2\u0288\u0289\7\22\2\2\u0289\u028a\7\23\2\2\u028a\u028b\5F$\2\u028b"+
		"\u028c\7E\2\2\u028c\u028d\7S\2\2\u028d\u028e\5F$\2\u028e\u0300\3\2\2\2"+
		"\u028f\u0290\7\22\2\2\u0290\u0291\7\23\2\2\u0291\u0292\5F$\2\u0292\u0293"+
		"\7\30\2\2\u0293\u0294\5\6\4\2\u0294\u0300\3\2\2\2\u0295\u0296\7*\2\2\u0296"+
		"\u0299\7\23\2\2\u0297\u0298\7\66\2\2\u0298\u029a\7-\2\2\u0299\u0297\3"+
		"\2\2\2\u0299\u029a\3\2\2\2\u029a\u029b\3\2\2\2\u029b\u0300\5F$\2\u029c"+
		"\u029d\7K\2\2\u029d\u0300\7N\2\2\u029e\u029f\7K\2\2\u029f\u02a2\7$\2\2"+
		"\u02a0\u02a1\7:\2\2\u02a1\u02a3\7\\\2\2\u02a2\u02a0\3\2\2\2\u02a2\u02a3"+
		"\3\2\2\2\u02a3\u0300\3\2\2\2\u02a4\u02a5\7K\2\2\u02a5\u02a8\7\"\2\2\u02a6"+
		"\u02a7\7:\2\2\u02a7\u02a9\7\\\2\2\u02a8\u02a6\3\2\2\2\u02a8\u02a9\3\2"+
		"\2\2\u02a9\u0300\3\2\2\2\u02aa\u02ab\7K\2\2\u02ab\u02ae\7P\2\2\u02ac\u02ad"+
		"\t\3\2\2\u02ad\u02af\5F$\2\u02ae\u02ac\3\2\2\2\u02ae\u02af\3\2\2\2\u02af"+
		"\u02b2\3\2\2\2\u02b0\u02b1\7:\2\2\u02b1\u02b3\7\\\2\2\u02b2\u02b0\3\2"+
		"\2\2\u02b2\u02b3\3\2\2\2\u02b3\u0300\3\2\2\2\u02b4\u02b5\7K\2\2\u02b5"+
		"\u02b8\7Z\2\2\u02b6\u02b7\t\3\2\2\u02b7\u02b9\5F$\2\u02b8\u02b6\3\2\2"+
		"\2\u02b8\u02b9\3\2\2\2\u02b9\u02bc\3\2\2\2\u02ba\u02bb\7:\2\2\u02bb\u02bd"+
		"\7\\\2\2\u02bc\u02ba\3\2\2\2\u02bc\u02bd\3\2\2\2\u02bd\u0300\3\2\2\2\u02be"+
		"\u02bf\7K\2\2\u02bf\u02c2\7\61\2\2\u02c0\u02c1\t\3\2\2\u02c1\u02c3\5F"+
		"$\2\u02c2\u02c0\3\2\2\2\u02c2\u02c3\3\2\2\2\u02c3\u02c6\3\2\2\2\u02c4"+
		"\u02c5\7:\2\2\u02c5\u02c7\7\\\2\2\u02c6\u02c4\3\2\2\2\u02c6\u02c7\3\2"+
		"\2\2\u02c7\u0300\3\2\2\2\u02c8\u02c9\7K\2\2\u02c9\u02cc\7X\2\2\u02ca\u02cb"+
		"\7:\2\2\u02cb\u02cd\7\\\2\2\u02cc\u02ca\3\2\2\2\u02cc\u02cd\3\2\2\2\u02cd"+
		"\u0300\3\2\2\2\u02ce\u02cf\7K\2\2\u02cf\u02d2\7\64\2\2\u02d0\u02d1\7:"+
		"\2\2\u02d1\u02d3\7\\\2\2\u02d2\u02d0\3\2\2\2\u02d2\u02d3\3\2\2\2\u02d3"+
		"\u0300\3\2\2\2\u02d4\u02d5\7K\2\2\u02d5\u02d8\7\24\2\2\u02d6\u02d7\7:"+
		"\2\2\u02d7\u02d9\7\\\2\2\u02d8\u02d6\3\2\2\2\u02d8\u02d9\3\2\2\2\u02d9"+
		"\u0300\3\2\2\2\u02da\u02db\t\4\2\2\u02db\u02dd\7#\2\2\u02dc\u02de\7.\2"+
		"\2\u02dd\u02dc\3\2\2\2\u02dd\u02de\3\2\2\2\u02de\u02df\3\2\2\2\u02df\u0300"+
		"\5F$\2\u02e0\u02e1\t\4\2\2\u02e1\u02e2\7!\2\2\u02e2\u0300\5F$\2\u02e3"+
		"\u02e4\t\4\2\2\u02e4\u02e6\7O\2\2\u02e5\u02e7\7.\2\2\u02e6\u02e5\3\2\2"+
		"\2\u02e6\u02e7\3\2\2\2\u02e7\u02e8\3\2\2\2\u02e8\u0300\5<\37\2\u02e9\u02ea"+
		"\t\4\2\2\u02ea\u02eb\7Y\2\2\u02eb\u0300\5<\37\2\u02ec\u02ed\t\4\2\2\u02ed"+
		"\u02ef\7\60\2\2\u02ee\u02f0\7.\2\2\u02ef\u02ee\3\2\2\2\u02ef\u02f0\3\2"+
		"\2\2\u02f0\u02f1\3\2\2\2\u02f1\u0300\5:\36\2\u02f2\u02f3\t\4\2\2\u02f3"+
		"\u02f4\7W\2\2\u02f4\u0300\5F$\2\u02f5\u02f6\t\4\2\2\u02f6\u02f7\7\63\2"+
		"\2\u02f7\u0300\5F$\2\u02f8\u02f9\7J\2\2\u02f9\u0300\5@!\2\u02fa\u0300"+
		"\5\26\f\2\u02fb\u0300\5\16\b\2\u02fc\u0300\5\20\t\2\u02fd\u0300\5\22\n"+
		"\2\u02fe\u0300\5\24\13\2\u02ffO\3\2\2\2\u02ff[\3\2\2\2\u02ffa\3\2\2\2"+
		"\u02ffh\3\2\2\2\u02ffo\3\2\2\2\u02ffy\3\2\2\2\u02ff\u0088\3\2\2\2\u02ff"+
		"\u0091\3\2\2\2\u02ff\u009b\3\2\2\2\u02ff\u00a5\3\2\2\2\u02ff\u00b0\3\2"+
		"\2\2\u02ff\u00b8\3\2\2\2\u02ff\u00c0\3\2\2\2\u02ff\u00c8\3\2\2\2\u02ff"+
		"\u00d0\3\2\2\2\u02ff\u00d5\3\2\2\2\u02ff\u00da\3\2\2\2\u02ff\u00df\3\2"+
		"\2\2\u02ff\u00e4\3\2\2\2\u02ff\u00e9\3\2\2\2\u02ff\u00ee\3\2\2\2\u02ff"+
		"\u00f3\3\2\2\2\u02ff\u00f8\3\2\2\2\u02ff\u0100\3\2\2\2\u02ff\u0108\3\2"+
		"\2\2\u02ff\u0110\3\2\2\2\u02ff\u0118\3\2\2\2\u02ff\u0124\3\2\2\2\u02ff"+
		"\u012a\3\2\2\2\u02ff\u0131\3\2\2\2\u02ff\u0138\3\2\2\2\u02ff\u013f\3\2"+
		"\2\2\u02ff\u014b\3\2\2\2\u02ff\u0151\3\2\2\2\u02ff\u0158\3\2\2\2\u02ff"+
		"\u015f\3\2\2\2\u02ff\u0166\3\2\2\2\u02ff\u016d\3\2\2\2\u02ff\u0177\3\2"+
		"\2\2\u02ff\u0182\3\2\2\2\u02ff\u0188\3\2\2\2\u02ff\u018f\3\2\2\2\u02ff"+
		"\u0196\3\2\2\2\u02ff\u019d\3\2\2\2\u02ff\u01b1\3\2\2\2\u02ff\u01c0\3\2"+
		"\2\2\u02ff\u01c6\3\2\2\2\u02ff\u01cd\3\2\2\2\u02ff\u01d4\3\2\2\2\u02ff"+
		"\u01dd\3\2\2\2\u02ff\u01e7\3\2\2\2\u02ff\u01ee\3\2\2\2\u02ff\u01f5\3\2"+
		"\2\2\u02ff\u0201\3\2\2\2\u02ff\u0207\3\2\2\2\u02ff\u020e\3\2\2\2\u02ff"+
		"\u0215\3\2\2\2\u02ff\u021f\3\2\2\2\u02ff\u0221\3\2\2\2\u02ff\u022c\3\2"+
		"\2\2\u02ff\u0232\3\2\2\2\u02ff\u0239\3\2\2\2\u02ff\u0240\3\2\2\2\u02ff"+
		"\u0247\3\2\2\2\u02ff\u0256\3\2\2\2\u02ff\u025c\3\2\2\2\u02ff\u0263\3\2"+
		"\2\2\u02ff\u026a\3\2\2\2\u02ff\u0270\3\2\2\2\u02ff\u0277\3\2\2\2\u02ff"+
		"\u0282\3\2\2\2\u02ff\u0288\3\2\2\2\u02ff\u028f\3\2\2\2\u02ff\u0295\3\2"+
		"\2\2\u02ff\u029c\3\2\2\2\u02ff\u029e\3\2\2\2\u02ff\u02a4\3\2\2\2\u02ff"+
		"\u02aa\3\2\2\2\u02ff\u02b4\3\2\2\2\u02ff\u02be\3\2\2\2\u02ff\u02c8\3\2"+
		"\2\2\u02ff\u02ce\3\2\2\2\u02ff\u02d4\3\2\2\2\u02ff\u02da\3\2\2\2\u02ff"+
		"\u02e0\3\2\2\2\u02ff\u02e3\3\2\2\2\u02ff\u02e9\3\2\2\2\u02ff\u02ec\3\2"+
		"\2\2\u02ff\u02f2\3\2\2\2\u02ff\u02f5\3\2\2\2\u02ff\u02f8\3\2\2\2\u02ff"+
		"\u02fa\3\2\2\2\u02ff\u02fb\3\2\2\2\u02ff\u02fc\3\2\2\2\u02ff\u02fd\3\2"+
		"\2\2\u02ff\u02fe\3\2\2\2\u0300\5\3\2\2\2\u0301\u0302\5\b\5\2\u0302\u0303"+
		"\7\5\2\2\u0303\u0305\3\2\2\2\u0304\u0301\3\2\2\2\u0304\u0305\3\2\2\2\u0305"+
		"\u0306\3\2\2\2\u0306\u0307\5\f\7\2\u0307\7\3\2\2\2\u0308\u030d\5\n\6\2"+
		"\u0309\u030a\7\5\2\2\u030a\u030c\5\n\6\2\u030b\u0309\3\2\2\2\u030c\u030f"+
		"\3\2\2\2\u030d\u030b\3\2\2\2\u030d\u030e\3\2\2\2\u030e\t\3\2\2\2\u030f"+
		"\u030d\3\2\2\2\u0310\u0313\5\22\n\2\u0311\u0313\5\24\13\2\u0312\u0310"+
		"\3\2\2\2\u0312\u0311\3\2\2\2\u0313\13\3\2\2\2\u0314\u0318\5\16\b\2\u0315"+
		"\u0318\5\20\t\2\u0316\u0318\5\26\f\2\u0317\u0314\3\2\2\2\u0317\u0315\3"+
		"\2\2\2\u0317\u0316\3\2\2\2\u0318\r\3\2\2\2\u0319\u031a\78\2\2\u031a\u031c"+
		"\79\2\2\u031b\u031d\7O\2\2\u031c\u031b\3\2\2\2\u031c\u031d\3\2\2\2\u031d"+
		"\u031e\3\2\2\2\u031e\u031f\5<\37\2\u031f\u0320\5\26\f\2\u0320\17\3\2\2"+
		"\2\u0321\u0322\78\2\2\u0322\u0323\7C\2\2\u0323\u0324\7O\2\2\u0324\u0325"+
		"\5<\37\2\u0325\u0326\5\26\f\2\u0326\21\3\2\2\2\u0327\u032a\7 \2\2\u0328"+
		"\u0329\7@\2\2\u0329\u032b\7F\2\2\u032a\u0328\3\2\2\2\u032a\u032b\3\2\2"+
		"\2\u032b\u032d\3\2\2\2\u032c\u032e\7\32\2\2\u032d\u032c\3\2\2\2\u032d"+
		"\u032e\3\2\2\2\u032e\u032f\3\2\2\2\u032f\u0330\t\5\2\2\u0330\u0331\7Y"+
		"\2\2\u0331\u0332\5F$\2\u0332\u0333\7\30\2\2\u0333\u0334\5\26\f\2\u0334"+
		"\23\3\2\2\2\u0335\u0338\7 \2\2\u0336\u0337\7@\2\2\u0337\u0339\7F\2\2\u0338"+
		"\u0336\3\2\2\2\u0338\u0339\3\2\2\2\u0339\u033a\3\2\2\2\u033a\u033b\t\5"+
		"\2\2\u033b\u033c\7\60\2\2\u033c\u033d\5F$\2\u033d\u033e\7?\2\2\u033e\u033f"+
		"\5> \2\u033f\25\3\2\2\2\u0340\u0344\7I\2\2\u0341\u0343\n\6\2\2\u0342\u0341"+
		"\3\2\2\2\u0343\u0346\3\2\2\2\u0344\u0342\3\2\2\2\u0344\u0345\3\2\2\2\u0345"+
		"\u0350\3\2\2\2\u0346\u0344\3\2\2\2\u0347\u0348\5\30\r\2\u0348\u034c\7"+
		"I\2\2\u0349\u034b\n\6\2\2\u034a\u0349\3\2\2\2\u034b\u034e\3\2\2\2\u034c"+
		"\u034a\3\2\2\2\u034c\u034d\3\2\2\2\u034d\u0350\3\2\2\2\u034e\u034c\3\2"+
		"\2\2\u034f\u0340\3\2\2\2\u034f\u0347\3\2\2\2\u0350\27\3\2\2\2\u0351\u0352"+
		"\7[\2\2\u0352\u0357\5$\23\2\u0353\u0354\7\5\2\2\u0354\u0356\5$\23\2\u0355"+
		"\u0353\3\2\2\2\u0356\u0359\3\2\2\2\u0357\u0355\3\2\2\2\u0357\u0358\3\2"+
		"\2\2\u0358\31\3\2\2\2\u0359\u0357\3\2\2\2\u035a\u035b\7\25\2\2\u035b\u035c"+
		"\7\7\2\2\u035c\u035d\5\32\16\2\u035d\u035e\7\b\2\2\u035e\u037d\3\2\2\2"+
		"\u035f\u0360\7\26\2\2\u0360\u0361\7\7\2\2\u0361\u0362\5\32\16\2\u0362"+
		"\u0363\7\5\2\2\u0363\u0364\5\32\16\2\u0364\u0365\7\b\2\2\u0365\u037d\3"+
		"\2\2\2\u0366\u036d\7\27\2\2\u0367\u0369\7\7\2\2\u0368\u036a\5 \21\2\u0369"+
		"\u0368\3\2\2\2\u0369\u036a\3\2\2\2\u036a\u036b\3\2\2\2\u036b\u036e\7\b"+
		"\2\2\u036c\u036e\7,\2\2\u036d\u0367\3\2\2\2\u036d\u036c\3\2\2\2\u036e"+
		"\u037d\3\2\2\2\u036f\u037a\5F$\2\u0370\u0371\7\3\2\2\u0371\u0376\7]\2"+
		"\2\u0372\u0373\7\5\2\2\u0373\u0375\7]\2\2\u0374\u0372\3\2\2\2\u0375\u0378"+
		"\3\2\2\2\u0376\u0374\3\2\2\2\u0376\u0377\3\2\2\2\u0377\u0379\3\2\2\2\u0378"+
		"\u0376\3\2\2\2\u0379\u037b\7\4\2\2\u037a\u0370\3\2\2\2\u037a\u037b\3\2"+
		"\2\2\u037b\u037d\3\2\2\2\u037c\u035a\3\2\2\2\u037c\u035f\3\2\2\2\u037c"+
		"\u0366\3\2\2\2\u037c\u036f\3\2\2\2\u037d\33\3\2\2\2\u037e\u0383\5\36\20"+
		"\2\u037f\u0380\7\5\2\2\u0380\u0382\5\36\20\2\u0381\u037f\3\2\2\2\u0382"+
		"\u0385\3\2\2\2\u0383\u0381\3\2\2\2\u0383\u0384\3\2\2\2\u0384\35\3\2\2"+
		"\2\u0385\u0383\3\2\2\2\u0386\u0387\5F$\2\u0387\u0388\5\32\16\2\u0388\37"+
		"\3\2\2\2\u0389\u038e\5\"\22\2\u038a\u038b\7\5\2\2\u038b\u038d\5\"\22\2"+
		"\u038c\u038a\3\2\2\2\u038d\u0390\3\2\2\2\u038e\u038c\3\2\2\2\u038e\u038f"+
		"\3\2\2\2\u038f!\3\2\2\2\u0390\u038e\3\2\2\2\u0391\u0392\5F$\2\u0392\u0393"+
		"\7\t\2\2\u0393\u0394\5\32\16\2\u0394#\3\2\2\2\u0395\u0397\5F$\2\u0396"+
		"\u0398\7\30\2\2\u0397\u0396\3\2\2\2\u0397\u0398\3\2\2\2\u0398\u0399\3"+
		"\2\2\2\u0399\u039a\7\3\2\2\u039a\u039b\5\26\f\2\u039b\u039c\7\4\2\2\u039c"+
		"%\3\2\2\2\u039d\u03a2\5(\25\2\u039e\u039f\7\5\2\2\u039f\u03a1\5(\25\2"+
		"\u03a0\u039e\3\2\2\2\u03a1\u03a4\3\2\2\2\u03a2\u03a0\3\2\2\2\u03a2\u03a3"+
		"\3\2\2\2\u03a3\'\3\2\2\2\u03a4\u03a2\3\2\2\2\u03a5\u03aa\5<\37\2\u03a6"+
		"\u03a7\7\3\2\2\u03a7\u03a8\5\34\17\2\u03a8\u03a9\7\4\2\2\u03a9\u03ab\3"+
		"\2\2\2\u03aa\u03a6\3\2\2\2\u03aa\u03ab\3\2\2\2\u03ab\u03ac\3\2\2\2\u03ac"+
		"\u03ad\7?\2\2\u03ad\u03ae\5> \2\u03ae)\3\2\2\2\u03af\u03b4\5,\27\2\u03b0"+
		"\u03b1\7\5\2\2\u03b1\u03b3\5,\27\2\u03b2\u03b0\3\2\2\2\u03b3\u03b6\3\2"+
		"\2\2\u03b4\u03b2\3\2\2\2\u03b4\u03b5\3\2\2\2\u03b5+\3\2\2\2\u03b6\u03b4"+
		"\3\2\2\2\u03b7\u03b8\t\7\2\2\u03b8-\3\2\2\2\u03b9\u03be\5\60\31\2\u03ba"+
		"\u03bb\7\5\2\2\u03bb\u03bd\5\60\31\2\u03bc\u03ba\3\2\2\2\u03bd\u03c0\3"+
		"\2\2\2\u03be\u03bc\3\2\2\2\u03be\u03bf\3\2\2\2\u03bf/\3\2\2\2\u03c0\u03be"+
		"\3\2\2\2\u03c1\u03c2\5F$\2\u03c2\u03c3\7\n\2\2\u03c3\u03c5\3\2\2\2\u03c4"+
		"\u03c1\3\2\2\2\u03c4\u03c5\3\2\2\2\u03c5\u03c6\3\2\2\2\u03c6\u03c7\5\62"+
		"\32\2\u03c7\u03c8\7\n\2\2\u03c8\u03c9\5\62\32\2\u03c9\61\3\2\2\2\u03ca"+
		"\u03cb\7\13\2\2\u03cb\u03d0\5F$\2\u03cc\u03cd\7\5\2\2\u03cd\u03cf\5F$"+
		"\2\u03ce\u03cc\3\2\2\2\u03cf\u03d2\3\2\2\2\u03d0\u03ce\3\2\2\2\u03d0\u03d1"+
		"\3\2\2\2\u03d1\u03d3\3\2\2\2\u03d2\u03d0\3\2\2\2\u03d3\u03d4\7\f\2\2\u03d4"+
		"\u03e3\3\2\2\2\u03d5\u03d6\7\r\2\2\u03d6\u03db\5F$\2\u03d7\u03d8\7\5\2"+
		"\2\u03d8\u03da\5F$\2\u03d9\u03d7\3\2\2\2\u03da\u03dd\3\2\2\2\u03db\u03d9"+
		"\3\2\2\2\u03db\u03dc\3\2\2\2\u03dc\u03de\3\2\2\2\u03dd\u03db\3\2\2\2\u03de"+
		"\u03df\7\16\2\2\u03df\u03e3\3\2\2\2\u03e0\u03e3\5F$\2\u03e1\u03e3\7L\2"+
		"\2\u03e2\u03ca\3\2\2\2\u03e2\u03d5\3\2\2\2\u03e2\u03e0\3\2\2\2\u03e2\u03e1"+
		"\3\2\2\2\u03e3\63\3\2\2\2\u03e4\u03e5\7\21\2\2\u03e5\u03e6\7W\2\2\u03e6"+
		"\u03e7\58\35\2\u03e7\65\3\2\2\2\u03e8\u03e9\7D\2\2\u03e9\u03ea\7W\2\2"+
		"\u03ea\u03eb\58\35\2\u03eb\67\3\2\2\2\u03ec\u03f1\5F$\2\u03ed\u03ee\7"+
		"\5\2\2\u03ee\u03f0\5F$\2\u03ef\u03ed\3\2\2\2\u03f0\u03f3\3\2\2\2\u03f1"+
		"\u03ef\3\2\2\2\u03f1\u03f2\3\2\2\2\u03f29\3\2\2\2\u03f3\u03f1\3\2\2\2"+
		"\u03f4\u03f5\5F$\2\u03f5\u03f6\7\n\2\2\u03f6\u03f8\3\2\2\2\u03f7\u03f4"+
		"\3\2\2\2\u03f7\u03f8\3\2\2\2\u03f8\u03f9\3\2\2\2\u03f9\u03fa\5F$\2\u03fa"+
		";\3\2\2\2\u03fb\u03fc\5F$\2\u03fc\u03fd\7\n\2\2\u03fd\u03ff\3\2\2\2\u03fe"+
		"\u03fb\3\2\2\2\u03fe\u03ff\3\2\2\2\u03ff\u0400\3\2\2\2\u0400\u0401\5F"+
		"$\2\u0401=\3\2\2\2\u0402\u0403\7\3\2\2\u0403\u0408\5@!\2\u0404\u0405\7"+
		"\5\2\2\u0405\u0407\5@!\2\u0406\u0404\3\2\2\2\u0407\u040a\3\2\2\2\u0408"+
		"\u0406\3\2\2\2\u0408\u0409\3\2\2\2\u0409\u040b\3\2\2\2\u040a\u0408\3\2"+
		"\2\2\u040b\u040c\7\4\2\2\u040c?\3\2\2\2\u040d\u0412\5B\"\2\u040e\u0410"+
		"\7+\2\2\u040f\u040e\3\2\2\2\u040f\u0410\3\2\2\2\u0410\u0411\3\2\2\2\u0411"+
		"\u0413\7\\\2\2\u0412\u040f\3\2\2\2\u0412\u0413\3\2\2\2\u0413A\3\2\2\2"+
		"\u0414\u0419\5F$\2\u0415\u0416\7\n\2\2\u0416\u0418\5F$\2\u0417\u0415\3"+
		"\2\2\2\u0418\u041b\3\2\2\2\u0419\u0417\3\2\2\2\u0419\u041a\3\2\2\2\u041a"+
		"\u041e\3\2\2\2\u041b\u0419\3\2\2\2\u041c\u041e\7\\\2\2\u041d\u0414\3\2"+
		"\2\2\u041d\u041c\3\2\2\2\u041eC\3\2\2\2\u041f\u0421\13\2\2\2\u0420\u041f"+
		"\3\2\2\2\u0421\u0424\3\2\2\2\u0422\u0423\3\2\2\2\u0422\u0420\3\2\2\2\u0423"+
		"E\3\2\2\2\u0424\u0422\3\2\2\2\u0425\u0428\7^\2\2\u0426\u0428\5J&\2\u0427"+
		"\u0425\3\2\2\2\u0427\u0426\3\2\2\2\u0428G\3\2\2\2\u0429\u042b\7\17\2\2"+
		"\u042a\u0429\3\2\2\2\u042a\u042b\3\2\2\2\u042b\u042c\3\2\2\2\u042c\u0431"+
		"\5F$\2\u042d\u042e\t\b\2\2\u042e\u0430\5F$\2\u042f\u042d\3\2\2\2\u0430"+
		"\u0433\3\2\2\2\u0431\u042f\3\2\2\2\u0431\u0432\3\2\2\2\u0432I\3\2\2\2"+
		"\u0433\u0431\3\2\2\2\u0434\u0435\t\t\2\2\u0435K\3\2\2\2[TYsw~\u00a9\u011d"+
		"\u013c\u0144\u0149\u0164\u016b\u0171\u0175\u017c\u019a\u019f\u01a5\u01ac"+
		"\u01b6\u01bc\u01e2\u01f2\u01fa\u01ff\u0219\u021d\u0226\u0244\u024c\u0251"+
		"\u0274\u027c\u0299\u02a2\u02a8\u02ae\u02b2\u02b8\u02bc\u02c2\u02c6\u02cc"+
		"\u02d2\u02d8\u02dd\u02e6\u02ef\u02ff\u0304\u030d\u0312\u0317\u031c\u032a"+
		"\u032d\u0338\u0344\u034c\u034f\u0357\u0369\u036d\u0376\u037a\u037c\u0383"+
		"\u038e\u0397\u03a2\u03aa\u03b4\u03be\u03c4\u03d0\u03db\u03e2\u03f1\u03f7"+
		"\u03fe\u0408\u040f\u0412\u0419\u041d\u0422\u0427\u042a\u0431";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}