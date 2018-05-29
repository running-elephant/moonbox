// Generated from moonbox/core/parser/MqlBase.g4 by ANTLR 4.5.3
package moonbox.core.parser;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link MqlBaseParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface MqlBaseVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link MqlBaseParser#single}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSingle(MqlBaseParser.SingleContext ctx);
	/**
	 * Visit a parse tree produced by the {@code createOrganization}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateOrganization(MqlBaseParser.CreateOrganizationContext ctx);
	/**
	 * Visit a parse tree produced by the {@code renameOrganization}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRenameOrganization(MqlBaseParser.RenameOrganizationContext ctx);
	/**
	 * Visit a parse tree produced by the {@code setOrganizationName}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSetOrganizationName(MqlBaseParser.SetOrganizationNameContext ctx);
	/**
	 * Visit a parse tree produced by the {@code setOrganizationComment}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSetOrganizationComment(MqlBaseParser.SetOrganizationCommentContext ctx);
	/**
	 * Visit a parse tree produced by the {@code dropOrganization}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDropOrganization(MqlBaseParser.DropOrganizationContext ctx);
	/**
	 * Visit a parse tree produced by the {@code createSa}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateSa(MqlBaseParser.CreateSaContext ctx);
	/**
	 * Visit a parse tree produced by the {@code renameSa}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRenameSa(MqlBaseParser.RenameSaContext ctx);
	/**
	 * Visit a parse tree produced by the {@code setSaName}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSetSaName(MqlBaseParser.SetSaNameContext ctx);
	/**
	 * Visit a parse tree produced by the {@code setSaPassword}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSetSaPassword(MqlBaseParser.SetSaPasswordContext ctx);
	/**
	 * Visit a parse tree produced by the {@code dropSa}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDropSa(MqlBaseParser.DropSaContext ctx);
	/**
	 * Visit a parse tree produced by the {@code grantGrantToUser}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGrantGrantToUser(MqlBaseParser.GrantGrantToUserContext ctx);
	/**
	 * Visit a parse tree produced by the {@code grantGrantToGroup}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGrantGrantToGroup(MqlBaseParser.GrantGrantToGroupContext ctx);
	/**
	 * Visit a parse tree produced by the {@code revokeGrantFromUser}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRevokeGrantFromUser(MqlBaseParser.RevokeGrantFromUserContext ctx);
	/**
	 * Visit a parse tree produced by the {@code revokeGrantFromGroup}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRevokeGrantFromGroup(MqlBaseParser.RevokeGrantFromGroupContext ctx);
	/**
	 * Visit a parse tree produced by the {@code grantAccountToUsers}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGrantAccountToUsers(MqlBaseParser.GrantAccountToUsersContext ctx);
	/**
	 * Visit a parse tree produced by the {@code grantAccountToGroups}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGrantAccountToGroups(MqlBaseParser.GrantAccountToGroupsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code revokeAccountFromUsers}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRevokeAccountFromUsers(MqlBaseParser.RevokeAccountFromUsersContext ctx);
	/**
	 * Visit a parse tree produced by the {@code revokeAccountFromGroups}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRevokeAccountFromGroups(MqlBaseParser.RevokeAccountFromGroupsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code grantDdlToUsers}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGrantDdlToUsers(MqlBaseParser.GrantDdlToUsersContext ctx);
	/**
	 * Visit a parse tree produced by the {@code grantDdlToGroups}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGrantDdlToGroups(MqlBaseParser.GrantDdlToGroupsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code revokeDdlFromUsers}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRevokeDdlFromUsers(MqlBaseParser.RevokeDdlFromUsersContext ctx);
	/**
	 * Visit a parse tree produced by the {@code revokeDdlFromGroups}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRevokeDdlFromGroups(MqlBaseParser.RevokeDdlFromGroupsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code grantDmlOnToUsers}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGrantDmlOnToUsers(MqlBaseParser.GrantDmlOnToUsersContext ctx);
	/**
	 * Visit a parse tree produced by the {@code grantDmlOnToGroups}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGrantDmlOnToGroups(MqlBaseParser.GrantDmlOnToGroupsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code revokeDmlOnFromUsers}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRevokeDmlOnFromUsers(MqlBaseParser.RevokeDmlOnFromUsersContext ctx);
	/**
	 * Visit a parse tree produced by the {@code revokeDmlOnFromGroups}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRevokeDmlOnFromGroups(MqlBaseParser.RevokeDmlOnFromGroupsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code createUser}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateUser(MqlBaseParser.CreateUserContext ctx);
	/**
	 * Visit a parse tree produced by the {@code renameUser}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRenameUser(MqlBaseParser.RenameUserContext ctx);
	/**
	 * Visit a parse tree produced by the {@code setUserName}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSetUserName(MqlBaseParser.SetUserNameContext ctx);
	/**
	 * Visit a parse tree produced by the {@code setUserPassword}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSetUserPassword(MqlBaseParser.SetUserPasswordContext ctx);
	/**
	 * Visit a parse tree produced by the {@code dropUser}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDropUser(MqlBaseParser.DropUserContext ctx);
	/**
	 * Visit a parse tree produced by the {@code createGroup}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateGroup(MqlBaseParser.CreateGroupContext ctx);
	/**
	 * Visit a parse tree produced by the {@code renameGroup}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRenameGroup(MqlBaseParser.RenameGroupContext ctx);
	/**
	 * Visit a parse tree produced by the {@code setGroupName}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSetGroupName(MqlBaseParser.SetGroupNameContext ctx);
	/**
	 * Visit a parse tree produced by the {@code setGroupComment}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSetGroupComment(MqlBaseParser.SetGroupCommentContext ctx);
	/**
	 * Visit a parse tree produced by the {@code addUsersToGroup}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAddUsersToGroup(MqlBaseParser.AddUsersToGroupContext ctx);
	/**
	 * Visit a parse tree produced by the {@code removeUsersFromGroup}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRemoveUsersFromGroup(MqlBaseParser.RemoveUsersFromGroupContext ctx);
	/**
	 * Visit a parse tree produced by the {@code dropGroup}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDropGroup(MqlBaseParser.DropGroupContext ctx);
	/**
	 * Visit a parse tree produced by the {@code mountDatasource}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMountDatasource(MqlBaseParser.MountDatasourceContext ctx);
	/**
	 * Visit a parse tree produced by the {@code renameDatasource}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRenameDatasource(MqlBaseParser.RenameDatasourceContext ctx);
	/**
	 * Visit a parse tree produced by the {@code setDatasourceName}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSetDatasourceName(MqlBaseParser.SetDatasourceNameContext ctx);
	/**
	 * Visit a parse tree produced by the {@code setDatasourceProperties}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSetDatasourceProperties(MqlBaseParser.SetDatasourcePropertiesContext ctx);
	/**
	 * Visit a parse tree produced by the {@code unmountDatasource}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnmountDatasource(MqlBaseParser.UnmountDatasourceContext ctx);
	/**
	 * Visit a parse tree produced by the {@code mountTable}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMountTable(MqlBaseParser.MountTableContext ctx);
	/**
	 * Visit a parse tree produced by the {@code mountTableWithDatasource}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMountTableWithDatasource(MqlBaseParser.MountTableWithDatasourceContext ctx);
	/**
	 * Visit a parse tree produced by the {@code renameTable}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRenameTable(MqlBaseParser.RenameTableContext ctx);
	/**
	 * Visit a parse tree produced by the {@code setTableName}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSetTableName(MqlBaseParser.SetTableNameContext ctx);
	/**
	 * Visit a parse tree produced by the {@code setTableProperties}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSetTableProperties(MqlBaseParser.SetTablePropertiesContext ctx);
	/**
	 * Visit a parse tree produced by the {@code addTableColumns}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAddTableColumns(MqlBaseParser.AddTableColumnsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code changeTableColumn}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitChangeTableColumn(MqlBaseParser.ChangeTableColumnContext ctx);
	/**
	 * Visit a parse tree produced by the {@code dropTableColumn}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDropTableColumn(MqlBaseParser.DropTableColumnContext ctx);
	/**
	 * Visit a parse tree produced by the {@code unmountTable}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnmountTable(MqlBaseParser.UnmountTableContext ctx);
	/**
	 * Visit a parse tree produced by the {@code createDatabase}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateDatabase(MqlBaseParser.CreateDatabaseContext ctx);
	/**
	 * Visit a parse tree produced by the {@code renameDatabase}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRenameDatabase(MqlBaseParser.RenameDatabaseContext ctx);
	/**
	 * Visit a parse tree produced by the {@code setDatabaseName}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSetDatabaseName(MqlBaseParser.SetDatabaseNameContext ctx);
	/**
	 * Visit a parse tree produced by the {@code setDatabaseComment}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSetDatabaseComment(MqlBaseParser.SetDatabaseCommentContext ctx);
	/**
	 * Visit a parse tree produced by the {@code dropDatabase}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDropDatabase(MqlBaseParser.DropDatabaseContext ctx);
	/**
	 * Visit a parse tree produced by the {@code useDatabase}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUseDatabase(MqlBaseParser.UseDatabaseContext ctx);
	/**
	 * Visit a parse tree produced by the {@code createFunction}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateFunction(MqlBaseParser.CreateFunctionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code dropFunction}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDropFunction(MqlBaseParser.DropFunctionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code createView}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateView(MqlBaseParser.CreateViewContext ctx);
	/**
	 * Visit a parse tree produced by the {@code renameView}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRenameView(MqlBaseParser.RenameViewContext ctx);
	/**
	 * Visit a parse tree produced by the {@code setViewName}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSetViewName(MqlBaseParser.SetViewNameContext ctx);
	/**
	 * Visit a parse tree produced by the {@code setViewComment}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSetViewComment(MqlBaseParser.SetViewCommentContext ctx);
	/**
	 * Visit a parse tree produced by the {@code setViewQuery}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSetViewQuery(MqlBaseParser.SetViewQueryContext ctx);
	/**
	 * Visit a parse tree produced by the {@code dropView}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDropView(MqlBaseParser.DropViewContext ctx);
	/**
	 * Visit a parse tree produced by the {@code createApplication}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateApplication(MqlBaseParser.CreateApplicationContext ctx);
	/**
	 * Visit a parse tree produced by the {@code renameApplication}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRenameApplication(MqlBaseParser.RenameApplicationContext ctx);
	/**
	 * Visit a parse tree produced by the {@code setApplicationName}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSetApplicationName(MqlBaseParser.SetApplicationNameContext ctx);
	/**
	 * Visit a parse tree produced by the {@code setApplicationQuerys}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSetApplicationQuerys(MqlBaseParser.SetApplicationQuerysContext ctx);
	/**
	 * Visit a parse tree produced by the {@code dropApplication}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDropApplication(MqlBaseParser.DropApplicationContext ctx);
	/**
	 * Visit a parse tree produced by the {@code createEvent}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateEvent(MqlBaseParser.CreateEventContext ctx);
	/**
	 * Visit a parse tree produced by the {@code renameEvent}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRenameEvent(MqlBaseParser.RenameEventContext ctx);
	/**
	 * Visit a parse tree produced by the {@code setDefiner}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSetDefiner(MqlBaseParser.SetDefinerContext ctx);
	/**
	 * Visit a parse tree produced by the {@code setEventName}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSetEventName(MqlBaseParser.SetEventNameContext ctx);
	/**
	 * Visit a parse tree produced by the {@code setEventSchedule}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSetEventSchedule(MqlBaseParser.SetEventScheduleContext ctx);
	/**
	 * Visit a parse tree produced by the {@code setEventEnable}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSetEventEnable(MqlBaseParser.SetEventEnableContext ctx);
	/**
	 * Visit a parse tree produced by the {@code dropEvent}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDropEvent(MqlBaseParser.DropEventContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showSysInfo}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowSysInfo(MqlBaseParser.ShowSysInfoContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showDatasources}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowDatasources(MqlBaseParser.ShowDatasourcesContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showDatabase}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowDatabase(MqlBaseParser.ShowDatabaseContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showTables}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowTables(MqlBaseParser.ShowTablesContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showViews}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowViews(MqlBaseParser.ShowViewsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showFunctions}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowFunctions(MqlBaseParser.ShowFunctionsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showUsers}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowUsers(MqlBaseParser.ShowUsersContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showGroups}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowGroups(MqlBaseParser.ShowGroupsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showApplications}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowApplications(MqlBaseParser.ShowApplicationsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code descDatasource}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDescDatasource(MqlBaseParser.DescDatasourceContext ctx);
	/**
	 * Visit a parse tree produced by the {@code descDatabase}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDescDatabase(MqlBaseParser.DescDatabaseContext ctx);
	/**
	 * Visit a parse tree produced by the {@code descTable}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDescTable(MqlBaseParser.DescTableContext ctx);
	/**
	 * Visit a parse tree produced by the {@code descView}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDescView(MqlBaseParser.DescViewContext ctx);
	/**
	 * Visit a parse tree produced by the {@code descFunction}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDescFunction(MqlBaseParser.DescFunctionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code descUser}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDescUser(MqlBaseParser.DescUserContext ctx);
	/**
	 * Visit a parse tree produced by the {@code descGroup}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDescGroup(MqlBaseParser.DescGroupContext ctx);
	/**
	 * Visit a parse tree produced by the {@code explain}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExplain(MqlBaseParser.ExplainContext ctx);
	/**
	 * Visit a parse tree produced by the {@code setConfiguration}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSetConfiguration(MqlBaseParser.SetConfigurationContext ctx);
	/**
	 * Visit a parse tree produced by the {@code insertInto}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInsertInto(MqlBaseParser.InsertIntoContext ctx);
	/**
	 * Visit a parse tree produced by the {@code createTemporaryView}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateTemporaryView(MqlBaseParser.CreateTemporaryViewContext ctx);
	/**
	 * Visit a parse tree produced by the {@code mqlQuery}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMqlQuery(MqlBaseParser.MqlQueryContext ctx);
	/**
	 * Visit a parse tree produced by {@link MqlBaseParser#appCmds}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAppCmds(MqlBaseParser.AppCmdsContext ctx);
	/**
	 * Visit a parse tree produced by {@link MqlBaseParser#definer}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDefiner(MqlBaseParser.DefinerContext ctx);
	/**
	 * Visit a parse tree produced by {@link MqlBaseParser#schedule}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSchedule(MqlBaseParser.ScheduleContext ctx);
	/**
	 * Visit a parse tree produced by {@link MqlBaseParser#starOrInteger}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStarOrInteger(MqlBaseParser.StarOrIntegerContext ctx);
	/**
	 * Visit a parse tree produced by {@link MqlBaseParser#query}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQuery(MqlBaseParser.QueryContext ctx);
	/**
	 * Visit a parse tree produced by {@link MqlBaseParser#ctes}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCtes(MqlBaseParser.CtesContext ctx);
	/**
	 * Visit a parse tree produced by the {@code complexDataType}
	 * labeled alternative in {@link MqlBaseParser#dataType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComplexDataType(MqlBaseParser.ComplexDataTypeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code primitiveDataType}
	 * labeled alternative in {@link MqlBaseParser#dataType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrimitiveDataType(MqlBaseParser.PrimitiveDataTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link MqlBaseParser#colTypeList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColTypeList(MqlBaseParser.ColTypeListContext ctx);
	/**
	 * Visit a parse tree produced by {@link MqlBaseParser#colType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColType(MqlBaseParser.ColTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link MqlBaseParser#complexColTypeList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComplexColTypeList(MqlBaseParser.ComplexColTypeListContext ctx);
	/**
	 * Visit a parse tree produced by {@link MqlBaseParser#complexColType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComplexColType(MqlBaseParser.ComplexColTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link MqlBaseParser#namedQuery}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNamedQuery(MqlBaseParser.NamedQueryContext ctx);
	/**
	 * Visit a parse tree produced by {@link MqlBaseParser#mountTableList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMountTableList(MqlBaseParser.MountTableListContext ctx);
	/**
	 * Visit a parse tree produced by {@link MqlBaseParser#mountTableOptions}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMountTableOptions(MqlBaseParser.MountTableOptionsContext ctx);
	/**
	 * Visit a parse tree produced by {@link MqlBaseParser#privilegeList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrivilegeList(MqlBaseParser.PrivilegeListContext ctx);
	/**
	 * Visit a parse tree produced by {@link MqlBaseParser#privilege}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrivilege(MqlBaseParser.PrivilegeContext ctx);
	/**
	 * Visit a parse tree produced by {@link MqlBaseParser#qualifiedColumnList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQualifiedColumnList(MqlBaseParser.QualifiedColumnListContext ctx);
	/**
	 * Visit a parse tree produced by {@link MqlBaseParser#columnIdentifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumnIdentifier(MqlBaseParser.ColumnIdentifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link MqlBaseParser#identifierStarList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdentifierStarList(MqlBaseParser.IdentifierStarListContext ctx);
	/**
	 * Visit a parse tree produced by {@link MqlBaseParser#addUser}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAddUser(MqlBaseParser.AddUserContext ctx);
	/**
	 * Visit a parse tree produced by {@link MqlBaseParser#removeUser}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRemoveUser(MqlBaseParser.RemoveUserContext ctx);
	/**
	 * Visit a parse tree produced by {@link MqlBaseParser#identifierList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdentifierList(MqlBaseParser.IdentifierListContext ctx);
	/**
	 * Visit a parse tree produced by {@link MqlBaseParser#funcIdentifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFuncIdentifier(MqlBaseParser.FuncIdentifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link MqlBaseParser#tableIdentifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTableIdentifier(MqlBaseParser.TableIdentifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link MqlBaseParser#propertyList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPropertyList(MqlBaseParser.PropertyListContext ctx);
	/**
	 * Visit a parse tree produced by {@link MqlBaseParser#property}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProperty(MqlBaseParser.PropertyContext ctx);
	/**
	 * Visit a parse tree produced by {@link MqlBaseParser#propertyKey}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPropertyKey(MqlBaseParser.PropertyKeyContext ctx);
	/**
	 * Visit a parse tree produced by {@link MqlBaseParser#password}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPassword(MqlBaseParser.PasswordContext ctx);
	/**
	 * Visit a parse tree produced by {@link MqlBaseParser#identifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdentifier(MqlBaseParser.IdentifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link MqlBaseParser#resource}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitResource(MqlBaseParser.ResourceContext ctx);
	/**
	 * Visit a parse tree produced by {@link MqlBaseParser#nonReserved}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNonReserved(MqlBaseParser.NonReservedContext ctx);
}