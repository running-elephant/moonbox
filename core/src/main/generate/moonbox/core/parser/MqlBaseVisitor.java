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
	 * Visit a parse tree produced by the {@code setOrganizationOptions}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSetOrganizationOptions(MqlBaseParser.SetOrganizationOptionsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code removeOrganizationOptions}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRemoveOrganizationOptions(MqlBaseParser.RemoveOrganizationOptionsContext ctx);
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
	 * Visit a parse tree produced by the {@code setSaOptions}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSetSaOptions(MqlBaseParser.SetSaOptionsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code removeSaOptions}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRemoveSaOptions(MqlBaseParser.RemoveSaOptionsContext ctx);
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
	 * Visit a parse tree produced by the {@code revokeGrantFromUser}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRevokeGrantFromUser(MqlBaseParser.RevokeGrantFromUserContext ctx);
	/**
	 * Visit a parse tree produced by the {@code grantGrantToGroup}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGrantGrantToGroup(MqlBaseParser.GrantGrantToGroupContext ctx);
	/**
	 * Visit a parse tree produced by the {@code revokeGrantFromGroup}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRevokeGrantFromGroup(MqlBaseParser.RevokeGrantFromGroupContext ctx);
	/**
	 * Visit a parse tree produced by the {@code grantPrivilegeToUsers}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGrantPrivilegeToUsers(MqlBaseParser.GrantPrivilegeToUsersContext ctx);
	/**
	 * Visit a parse tree produced by the {@code revokePrivilegeFromUsers}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRevokePrivilegeFromUsers(MqlBaseParser.RevokePrivilegeFromUsersContext ctx);
	/**
	 * Visit a parse tree produced by the {@code grantPrivilegeToGroup}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGrantPrivilegeToGroup(MqlBaseParser.GrantPrivilegeToGroupContext ctx);
	/**
	 * Visit a parse tree produced by the {@code revokePrivilegeFromGroup}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRevokePrivilegeFromGroup(MqlBaseParser.RevokePrivilegeFromGroupContext ctx);
	/**
	 * Visit a parse tree produced by the {@code grantResourcePrivilegeToUsers}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGrantResourcePrivilegeToUsers(MqlBaseParser.GrantResourcePrivilegeToUsersContext ctx);
	/**
	 * Visit a parse tree produced by the {@code revokeResourcePrivilegeFromUsers}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRevokeResourcePrivilegeFromUsers(MqlBaseParser.RevokeResourcePrivilegeFromUsersContext ctx);
	/**
	 * Visit a parse tree produced by the {@code grantResourcePrivilegeToGroup}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGrantResourcePrivilegeToGroup(MqlBaseParser.GrantResourcePrivilegeToGroupContext ctx);
	/**
	 * Visit a parse tree produced by the {@code revokeResourcePrivilegeFromGroup}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRevokeResourcePrivilegeFromGroup(MqlBaseParser.RevokeResourcePrivilegeFromGroupContext ctx);
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
	 * Visit a parse tree produced by the {@code setUserOptions}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSetUserOptions(MqlBaseParser.SetUserOptionsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code removeUserOptions}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRemoveUserOptions(MqlBaseParser.RemoveUserOptionsContext ctx);
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
	 * Visit a parse tree produced by the {@code addGroupUser}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAddGroupUser(MqlBaseParser.AddGroupUserContext ctx);
	/**
	 * Visit a parse tree produced by the {@code removeGroupUser}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRemoveGroupUser(MqlBaseParser.RemoveGroupUserContext ctx);
	/**
	 * Visit a parse tree produced by the {@code dropGroup}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDropGroup(MqlBaseParser.DropGroupContext ctx);
	/**
	 * Visit a parse tree produced by the {@code mountTable}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMountTable(MqlBaseParser.MountTableContext ctx);
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
	 * Visit a parse tree produced by the {@code removeTableProperties}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRemoveTableProperties(MqlBaseParser.RemoveTablePropertiesContext ctx);
	/**
	 * Visit a parse tree produced by the {@code unmountTable}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnmountTable(MqlBaseParser.UnmountTableContext ctx);
	/**
	 * Visit a parse tree produced by the {@code mountDatabase}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMountDatabase(MqlBaseParser.MountDatabaseContext ctx);
	/**
	 * Visit a parse tree produced by the {@code unmountDatabase}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnmountDatabase(MqlBaseParser.UnmountDatabaseContext ctx);
	/**
	 * Visit a parse tree produced by the {@code setDatabaseProperties}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSetDatabaseProperties(MqlBaseParser.SetDatabasePropertiesContext ctx);
	/**
	 * Visit a parse tree produced by the {@code removeDatabaseProperties}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRemoveDatabaseProperties(MqlBaseParser.RemoveDatabasePropertiesContext ctx);
	/**
	 * Visit a parse tree produced by the {@code refreshDatabase}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRefreshDatabase(MqlBaseParser.RefreshDatabaseContext ctx);
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
	 * Visit a parse tree produced by the {@code createProcedure}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateProcedure(MqlBaseParser.CreateProcedureContext ctx);
	/**
	 * Visit a parse tree produced by the {@code renameProcedure}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRenameProcedure(MqlBaseParser.RenameProcedureContext ctx);
	/**
	 * Visit a parse tree produced by the {@code setProcedureName}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSetProcedureName(MqlBaseParser.SetProcedureNameContext ctx);
	/**
	 * Visit a parse tree produced by the {@code setProcedureQuerys}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSetProcedureQuerys(MqlBaseParser.SetProcedureQuerysContext ctx);
	/**
	 * Visit a parse tree produced by the {@code dropProcedure}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDropProcedure(MqlBaseParser.DropProcedureContext ctx);
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
	 * Visit a parse tree produced by the {@code showJobs}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowJobs(MqlBaseParser.ShowJobsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showRunningEvents}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowRunningEvents(MqlBaseParser.ShowRunningEventsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showEvents}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowEvents(MqlBaseParser.ShowEventsContext ctx);
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
	 * Visit a parse tree produced by the {@code showFunctions}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowFunctions(MqlBaseParser.ShowFunctionsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showOrgs}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowOrgs(MqlBaseParser.ShowOrgsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showSas}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowSas(MqlBaseParser.ShowSasContext ctx);
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
	 * Visit a parse tree produced by the {@code showUsersInGroup}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowUsersInGroup(MqlBaseParser.ShowUsersInGroupContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showProcedures}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowProcedures(MqlBaseParser.ShowProceduresContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showVariable}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowVariable(MqlBaseParser.ShowVariableContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showGrants}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowGrants(MqlBaseParser.ShowGrantsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showCreateTable}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowCreateTable(MqlBaseParser.ShowCreateTableContext ctx);
	/**
	 * Visit a parse tree produced by the {@code showSchema}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowSchema(MqlBaseParser.ShowSchemaContext ctx);
	/**
	 * Visit a parse tree produced by the {@code descEvent}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDescEvent(MqlBaseParser.DescEventContext ctx);
	/**
	 * Visit a parse tree produced by the {@code descProcedure}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDescProcedure(MqlBaseParser.DescProcedureContext ctx);
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
	 * Visit a parse tree produced by the {@code descOrg}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDescOrg(MqlBaseParser.DescOrgContext ctx);
	/**
	 * Visit a parse tree produced by the {@code explain}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExplain(MqlBaseParser.ExplainContext ctx);
	/**
	 * Visit a parse tree produced by the {@code createTemporaryView}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateTemporaryView(MqlBaseParser.CreateTemporaryViewContext ctx);
	/**
	 * Visit a parse tree produced by the {@code statement}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatement(MqlBaseParser.StatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link MqlBaseParser#procCmds}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProcCmds(MqlBaseParser.ProcCmdsContext ctx);
	/**
	 * Visit a parse tree produced by {@link MqlBaseParser#definer}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDefiner(MqlBaseParser.DefinerContext ctx);
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
	 * Visit a parse tree produced by {@link MqlBaseParser#partitionSpec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPartitionSpec(MqlBaseParser.PartitionSpecContext ctx);
	/**
	 * Visit a parse tree produced by {@link MqlBaseParser#coalesceSpec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCoalesceSpec(MqlBaseParser.CoalesceSpecContext ctx);
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
	 * Visit a parse tree produced by {@link MqlBaseParser#grantPrivilegeList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGrantPrivilegeList(MqlBaseParser.GrantPrivilegeListContext ctx);
	/**
	 * Visit a parse tree produced by {@link MqlBaseParser#grantPrivilege}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGrantPrivilege(MqlBaseParser.GrantPrivilegeContext ctx);
	/**
	 * Visit a parse tree produced by {@link MqlBaseParser#privileges}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrivileges(MqlBaseParser.PrivilegesContext ctx);
	/**
	 * Visit a parse tree produced by {@link MqlBaseParser#privilege}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrivilege(MqlBaseParser.PrivilegeContext ctx);
	/**
	 * Visit a parse tree produced by {@link MqlBaseParser#columnIdentifiers}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumnIdentifiers(MqlBaseParser.ColumnIdentifiersContext ctx);
	/**
	 * Visit a parse tree produced by {@link MqlBaseParser#tableCollections}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTableCollections(MqlBaseParser.TableCollectionsContext ctx);
	/**
	 * Visit a parse tree produced by {@link MqlBaseParser#identifierOrStar}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdentifierOrStar(MqlBaseParser.IdentifierOrStarContext ctx);
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
	 * Visit a parse tree produced by {@link MqlBaseParser#propertyKeyList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPropertyKeyList(MqlBaseParser.PropertyKeyListContext ctx);
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