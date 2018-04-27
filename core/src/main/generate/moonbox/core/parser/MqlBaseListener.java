// Generated from moonbox/core/parser/MqlBase.g4 by ANTLR 4.5.3
package moonbox.core.parser;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link MqlBaseParser}.
 */
public interface MqlBaseListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link MqlBaseParser#single}.
	 * @param ctx the parse tree
	 */
	void enterSingle(MqlBaseParser.SingleContext ctx);
	/**
	 * Exit a parse tree produced by {@link MqlBaseParser#single}.
	 * @param ctx the parse tree
	 */
	void exitSingle(MqlBaseParser.SingleContext ctx);
	/**
	 * Enter a parse tree produced by the {@code createOrganization}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void enterCreateOrganization(MqlBaseParser.CreateOrganizationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code createOrganization}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void exitCreateOrganization(MqlBaseParser.CreateOrganizationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code renameOrganization}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void enterRenameOrganization(MqlBaseParser.RenameOrganizationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code renameOrganization}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void exitRenameOrganization(MqlBaseParser.RenameOrganizationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code setOrganizationName}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void enterSetOrganizationName(MqlBaseParser.SetOrganizationNameContext ctx);
	/**
	 * Exit a parse tree produced by the {@code setOrganizationName}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void exitSetOrganizationName(MqlBaseParser.SetOrganizationNameContext ctx);
	/**
	 * Enter a parse tree produced by the {@code setOrganizationComment}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void enterSetOrganizationComment(MqlBaseParser.SetOrganizationCommentContext ctx);
	/**
	 * Exit a parse tree produced by the {@code setOrganizationComment}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void exitSetOrganizationComment(MqlBaseParser.SetOrganizationCommentContext ctx);
	/**
	 * Enter a parse tree produced by the {@code dropOrganization}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void enterDropOrganization(MqlBaseParser.DropOrganizationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code dropOrganization}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void exitDropOrganization(MqlBaseParser.DropOrganizationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code createSa}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void enterCreateSa(MqlBaseParser.CreateSaContext ctx);
	/**
	 * Exit a parse tree produced by the {@code createSa}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void exitCreateSa(MqlBaseParser.CreateSaContext ctx);
	/**
	 * Enter a parse tree produced by the {@code renameSa}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void enterRenameSa(MqlBaseParser.RenameSaContext ctx);
	/**
	 * Exit a parse tree produced by the {@code renameSa}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void exitRenameSa(MqlBaseParser.RenameSaContext ctx);
	/**
	 * Enter a parse tree produced by the {@code setSaName}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void enterSetSaName(MqlBaseParser.SetSaNameContext ctx);
	/**
	 * Exit a parse tree produced by the {@code setSaName}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void exitSetSaName(MqlBaseParser.SetSaNameContext ctx);
	/**
	 * Enter a parse tree produced by the {@code setSaPassword}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void enterSetSaPassword(MqlBaseParser.SetSaPasswordContext ctx);
	/**
	 * Exit a parse tree produced by the {@code setSaPassword}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void exitSetSaPassword(MqlBaseParser.SetSaPasswordContext ctx);
	/**
	 * Enter a parse tree produced by the {@code dropSa}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void enterDropSa(MqlBaseParser.DropSaContext ctx);
	/**
	 * Exit a parse tree produced by the {@code dropSa}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void exitDropSa(MqlBaseParser.DropSaContext ctx);
	/**
	 * Enter a parse tree produced by the {@code grantGrantToUser}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void enterGrantGrantToUser(MqlBaseParser.GrantGrantToUserContext ctx);
	/**
	 * Exit a parse tree produced by the {@code grantGrantToUser}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void exitGrantGrantToUser(MqlBaseParser.GrantGrantToUserContext ctx);
	/**
	 * Enter a parse tree produced by the {@code grantGrantToGroup}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void enterGrantGrantToGroup(MqlBaseParser.GrantGrantToGroupContext ctx);
	/**
	 * Exit a parse tree produced by the {@code grantGrantToGroup}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void exitGrantGrantToGroup(MqlBaseParser.GrantGrantToGroupContext ctx);
	/**
	 * Enter a parse tree produced by the {@code revokeGrantFromUser}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void enterRevokeGrantFromUser(MqlBaseParser.RevokeGrantFromUserContext ctx);
	/**
	 * Exit a parse tree produced by the {@code revokeGrantFromUser}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void exitRevokeGrantFromUser(MqlBaseParser.RevokeGrantFromUserContext ctx);
	/**
	 * Enter a parse tree produced by the {@code revokeGrantFromGroup}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void enterRevokeGrantFromGroup(MqlBaseParser.RevokeGrantFromGroupContext ctx);
	/**
	 * Exit a parse tree produced by the {@code revokeGrantFromGroup}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void exitRevokeGrantFromGroup(MqlBaseParser.RevokeGrantFromGroupContext ctx);
	/**
	 * Enter a parse tree produced by the {@code grantAccountToUsers}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void enterGrantAccountToUsers(MqlBaseParser.GrantAccountToUsersContext ctx);
	/**
	 * Exit a parse tree produced by the {@code grantAccountToUsers}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void exitGrantAccountToUsers(MqlBaseParser.GrantAccountToUsersContext ctx);
	/**
	 * Enter a parse tree produced by the {@code grantAccountToGroups}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void enterGrantAccountToGroups(MqlBaseParser.GrantAccountToGroupsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code grantAccountToGroups}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void exitGrantAccountToGroups(MqlBaseParser.GrantAccountToGroupsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code revokeAccountFromUsers}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void enterRevokeAccountFromUsers(MqlBaseParser.RevokeAccountFromUsersContext ctx);
	/**
	 * Exit a parse tree produced by the {@code revokeAccountFromUsers}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void exitRevokeAccountFromUsers(MqlBaseParser.RevokeAccountFromUsersContext ctx);
	/**
	 * Enter a parse tree produced by the {@code revokeAccountFromGroups}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void enterRevokeAccountFromGroups(MqlBaseParser.RevokeAccountFromGroupsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code revokeAccountFromGroups}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void exitRevokeAccountFromGroups(MqlBaseParser.RevokeAccountFromGroupsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code grantDdlToUsers}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void enterGrantDdlToUsers(MqlBaseParser.GrantDdlToUsersContext ctx);
	/**
	 * Exit a parse tree produced by the {@code grantDdlToUsers}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void exitGrantDdlToUsers(MqlBaseParser.GrantDdlToUsersContext ctx);
	/**
	 * Enter a parse tree produced by the {@code grantDdlToGroups}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void enterGrantDdlToGroups(MqlBaseParser.GrantDdlToGroupsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code grantDdlToGroups}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void exitGrantDdlToGroups(MqlBaseParser.GrantDdlToGroupsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code revokeDdlFromUsers}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void enterRevokeDdlFromUsers(MqlBaseParser.RevokeDdlFromUsersContext ctx);
	/**
	 * Exit a parse tree produced by the {@code revokeDdlFromUsers}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void exitRevokeDdlFromUsers(MqlBaseParser.RevokeDdlFromUsersContext ctx);
	/**
	 * Enter a parse tree produced by the {@code revokeDdlFromGroups}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void enterRevokeDdlFromGroups(MqlBaseParser.RevokeDdlFromGroupsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code revokeDdlFromGroups}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void exitRevokeDdlFromGroups(MqlBaseParser.RevokeDdlFromGroupsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code grantDmlOnToUsers}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void enterGrantDmlOnToUsers(MqlBaseParser.GrantDmlOnToUsersContext ctx);
	/**
	 * Exit a parse tree produced by the {@code grantDmlOnToUsers}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void exitGrantDmlOnToUsers(MqlBaseParser.GrantDmlOnToUsersContext ctx);
	/**
	 * Enter a parse tree produced by the {@code grantDmlOnToGroups}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void enterGrantDmlOnToGroups(MqlBaseParser.GrantDmlOnToGroupsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code grantDmlOnToGroups}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void exitGrantDmlOnToGroups(MqlBaseParser.GrantDmlOnToGroupsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code revokeDmlOnFromUsers}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void enterRevokeDmlOnFromUsers(MqlBaseParser.RevokeDmlOnFromUsersContext ctx);
	/**
	 * Exit a parse tree produced by the {@code revokeDmlOnFromUsers}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void exitRevokeDmlOnFromUsers(MqlBaseParser.RevokeDmlOnFromUsersContext ctx);
	/**
	 * Enter a parse tree produced by the {@code revokeDmlOnFromGroups}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void enterRevokeDmlOnFromGroups(MqlBaseParser.RevokeDmlOnFromGroupsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code revokeDmlOnFromGroups}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void exitRevokeDmlOnFromGroups(MqlBaseParser.RevokeDmlOnFromGroupsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code createUser}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void enterCreateUser(MqlBaseParser.CreateUserContext ctx);
	/**
	 * Exit a parse tree produced by the {@code createUser}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void exitCreateUser(MqlBaseParser.CreateUserContext ctx);
	/**
	 * Enter a parse tree produced by the {@code renameUser}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void enterRenameUser(MqlBaseParser.RenameUserContext ctx);
	/**
	 * Exit a parse tree produced by the {@code renameUser}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void exitRenameUser(MqlBaseParser.RenameUserContext ctx);
	/**
	 * Enter a parse tree produced by the {@code setUserName}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void enterSetUserName(MqlBaseParser.SetUserNameContext ctx);
	/**
	 * Exit a parse tree produced by the {@code setUserName}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void exitSetUserName(MqlBaseParser.SetUserNameContext ctx);
	/**
	 * Enter a parse tree produced by the {@code setUserPassword}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void enterSetUserPassword(MqlBaseParser.SetUserPasswordContext ctx);
	/**
	 * Exit a parse tree produced by the {@code setUserPassword}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void exitSetUserPassword(MqlBaseParser.SetUserPasswordContext ctx);
	/**
	 * Enter a parse tree produced by the {@code dropUser}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void enterDropUser(MqlBaseParser.DropUserContext ctx);
	/**
	 * Exit a parse tree produced by the {@code dropUser}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void exitDropUser(MqlBaseParser.DropUserContext ctx);
	/**
	 * Enter a parse tree produced by the {@code createGroup}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void enterCreateGroup(MqlBaseParser.CreateGroupContext ctx);
	/**
	 * Exit a parse tree produced by the {@code createGroup}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void exitCreateGroup(MqlBaseParser.CreateGroupContext ctx);
	/**
	 * Enter a parse tree produced by the {@code renameGroup}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void enterRenameGroup(MqlBaseParser.RenameGroupContext ctx);
	/**
	 * Exit a parse tree produced by the {@code renameGroup}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void exitRenameGroup(MqlBaseParser.RenameGroupContext ctx);
	/**
	 * Enter a parse tree produced by the {@code setGroupName}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void enterSetGroupName(MqlBaseParser.SetGroupNameContext ctx);
	/**
	 * Exit a parse tree produced by the {@code setGroupName}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void exitSetGroupName(MqlBaseParser.SetGroupNameContext ctx);
	/**
	 * Enter a parse tree produced by the {@code setGroupComment}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void enterSetGroupComment(MqlBaseParser.SetGroupCommentContext ctx);
	/**
	 * Exit a parse tree produced by the {@code setGroupComment}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void exitSetGroupComment(MqlBaseParser.SetGroupCommentContext ctx);
	/**
	 * Enter a parse tree produced by the {@code addUsersToGroup}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void enterAddUsersToGroup(MqlBaseParser.AddUsersToGroupContext ctx);
	/**
	 * Exit a parse tree produced by the {@code addUsersToGroup}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void exitAddUsersToGroup(MqlBaseParser.AddUsersToGroupContext ctx);
	/**
	 * Enter a parse tree produced by the {@code removeUsersFromGroup}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void enterRemoveUsersFromGroup(MqlBaseParser.RemoveUsersFromGroupContext ctx);
	/**
	 * Exit a parse tree produced by the {@code removeUsersFromGroup}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void exitRemoveUsersFromGroup(MqlBaseParser.RemoveUsersFromGroupContext ctx);
	/**
	 * Enter a parse tree produced by the {@code dropGroup}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void enterDropGroup(MqlBaseParser.DropGroupContext ctx);
	/**
	 * Exit a parse tree produced by the {@code dropGroup}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void exitDropGroup(MqlBaseParser.DropGroupContext ctx);
	/**
	 * Enter a parse tree produced by the {@code mountDatasource}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void enterMountDatasource(MqlBaseParser.MountDatasourceContext ctx);
	/**
	 * Exit a parse tree produced by the {@code mountDatasource}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void exitMountDatasource(MqlBaseParser.MountDatasourceContext ctx);
	/**
	 * Enter a parse tree produced by the {@code renameDatasource}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void enterRenameDatasource(MqlBaseParser.RenameDatasourceContext ctx);
	/**
	 * Exit a parse tree produced by the {@code renameDatasource}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void exitRenameDatasource(MqlBaseParser.RenameDatasourceContext ctx);
	/**
	 * Enter a parse tree produced by the {@code setDatasourceName}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void enterSetDatasourceName(MqlBaseParser.SetDatasourceNameContext ctx);
	/**
	 * Exit a parse tree produced by the {@code setDatasourceName}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void exitSetDatasourceName(MqlBaseParser.SetDatasourceNameContext ctx);
	/**
	 * Enter a parse tree produced by the {@code setDatasourceProperties}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void enterSetDatasourceProperties(MqlBaseParser.SetDatasourcePropertiesContext ctx);
	/**
	 * Exit a parse tree produced by the {@code setDatasourceProperties}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void exitSetDatasourceProperties(MqlBaseParser.SetDatasourcePropertiesContext ctx);
	/**
	 * Enter a parse tree produced by the {@code unmountDatasource}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void enterUnmountDatasource(MqlBaseParser.UnmountDatasourceContext ctx);
	/**
	 * Exit a parse tree produced by the {@code unmountDatasource}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void exitUnmountDatasource(MqlBaseParser.UnmountDatasourceContext ctx);
	/**
	 * Enter a parse tree produced by the {@code mountTable}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void enterMountTable(MqlBaseParser.MountTableContext ctx);
	/**
	 * Exit a parse tree produced by the {@code mountTable}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void exitMountTable(MqlBaseParser.MountTableContext ctx);
	/**
	 * Enter a parse tree produced by the {@code mountTableWithDatasource}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void enterMountTableWithDatasource(MqlBaseParser.MountTableWithDatasourceContext ctx);
	/**
	 * Exit a parse tree produced by the {@code mountTableWithDatasource}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void exitMountTableWithDatasource(MqlBaseParser.MountTableWithDatasourceContext ctx);
	/**
	 * Enter a parse tree produced by the {@code renameTable}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void enterRenameTable(MqlBaseParser.RenameTableContext ctx);
	/**
	 * Exit a parse tree produced by the {@code renameTable}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void exitRenameTable(MqlBaseParser.RenameTableContext ctx);
	/**
	 * Enter a parse tree produced by the {@code setTableName}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void enterSetTableName(MqlBaseParser.SetTableNameContext ctx);
	/**
	 * Exit a parse tree produced by the {@code setTableName}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void exitSetTableName(MqlBaseParser.SetTableNameContext ctx);
	/**
	 * Enter a parse tree produced by the {@code setTableProperties}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void enterSetTableProperties(MqlBaseParser.SetTablePropertiesContext ctx);
	/**
	 * Exit a parse tree produced by the {@code setTableProperties}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void exitSetTableProperties(MqlBaseParser.SetTablePropertiesContext ctx);
	/**
	 * Enter a parse tree produced by the {@code addTableColumns}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void enterAddTableColumns(MqlBaseParser.AddTableColumnsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code addTableColumns}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void exitAddTableColumns(MqlBaseParser.AddTableColumnsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code changeTableColumn}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void enterChangeTableColumn(MqlBaseParser.ChangeTableColumnContext ctx);
	/**
	 * Exit a parse tree produced by the {@code changeTableColumn}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void exitChangeTableColumn(MqlBaseParser.ChangeTableColumnContext ctx);
	/**
	 * Enter a parse tree produced by the {@code dropTableColumn}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void enterDropTableColumn(MqlBaseParser.DropTableColumnContext ctx);
	/**
	 * Exit a parse tree produced by the {@code dropTableColumn}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void exitDropTableColumn(MqlBaseParser.DropTableColumnContext ctx);
	/**
	 * Enter a parse tree produced by the {@code unmountTable}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void enterUnmountTable(MqlBaseParser.UnmountTableContext ctx);
	/**
	 * Exit a parse tree produced by the {@code unmountTable}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void exitUnmountTable(MqlBaseParser.UnmountTableContext ctx);
	/**
	 * Enter a parse tree produced by the {@code createDatabase}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void enterCreateDatabase(MqlBaseParser.CreateDatabaseContext ctx);
	/**
	 * Exit a parse tree produced by the {@code createDatabase}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void exitCreateDatabase(MqlBaseParser.CreateDatabaseContext ctx);
	/**
	 * Enter a parse tree produced by the {@code renameDatabase}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void enterRenameDatabase(MqlBaseParser.RenameDatabaseContext ctx);
	/**
	 * Exit a parse tree produced by the {@code renameDatabase}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void exitRenameDatabase(MqlBaseParser.RenameDatabaseContext ctx);
	/**
	 * Enter a parse tree produced by the {@code setDatabaseName}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void enterSetDatabaseName(MqlBaseParser.SetDatabaseNameContext ctx);
	/**
	 * Exit a parse tree produced by the {@code setDatabaseName}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void exitSetDatabaseName(MqlBaseParser.SetDatabaseNameContext ctx);
	/**
	 * Enter a parse tree produced by the {@code setDatabaseComment}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void enterSetDatabaseComment(MqlBaseParser.SetDatabaseCommentContext ctx);
	/**
	 * Exit a parse tree produced by the {@code setDatabaseComment}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void exitSetDatabaseComment(MqlBaseParser.SetDatabaseCommentContext ctx);
	/**
	 * Enter a parse tree produced by the {@code dropDatabase}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void enterDropDatabase(MqlBaseParser.DropDatabaseContext ctx);
	/**
	 * Exit a parse tree produced by the {@code dropDatabase}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void exitDropDatabase(MqlBaseParser.DropDatabaseContext ctx);
	/**
	 * Enter a parse tree produced by the {@code useDatabase}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void enterUseDatabase(MqlBaseParser.UseDatabaseContext ctx);
	/**
	 * Exit a parse tree produced by the {@code useDatabase}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void exitUseDatabase(MqlBaseParser.UseDatabaseContext ctx);
	/**
	 * Enter a parse tree produced by the {@code createFunction}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void enterCreateFunction(MqlBaseParser.CreateFunctionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code createFunction}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void exitCreateFunction(MqlBaseParser.CreateFunctionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code renameFunction}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void enterRenameFunction(MqlBaseParser.RenameFunctionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code renameFunction}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void exitRenameFunction(MqlBaseParser.RenameFunctionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code setFunctionName}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void enterSetFunctionName(MqlBaseParser.SetFunctionNameContext ctx);
	/**
	 * Exit a parse tree produced by the {@code setFunctionName}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void exitSetFunctionName(MqlBaseParser.SetFunctionNameContext ctx);
	/**
	 * Enter a parse tree produced by the {@code setFunctionProperties}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void enterSetFunctionProperties(MqlBaseParser.SetFunctionPropertiesContext ctx);
	/**
	 * Exit a parse tree produced by the {@code setFunctionProperties}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void exitSetFunctionProperties(MqlBaseParser.SetFunctionPropertiesContext ctx);
	/**
	 * Enter a parse tree produced by the {@code dropFunction}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void enterDropFunction(MqlBaseParser.DropFunctionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code dropFunction}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void exitDropFunction(MqlBaseParser.DropFunctionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code createView}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void enterCreateView(MqlBaseParser.CreateViewContext ctx);
	/**
	 * Exit a parse tree produced by the {@code createView}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void exitCreateView(MqlBaseParser.CreateViewContext ctx);
	/**
	 * Enter a parse tree produced by the {@code renameView}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void enterRenameView(MqlBaseParser.RenameViewContext ctx);
	/**
	 * Exit a parse tree produced by the {@code renameView}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void exitRenameView(MqlBaseParser.RenameViewContext ctx);
	/**
	 * Enter a parse tree produced by the {@code setViewName}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void enterSetViewName(MqlBaseParser.SetViewNameContext ctx);
	/**
	 * Exit a parse tree produced by the {@code setViewName}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void exitSetViewName(MqlBaseParser.SetViewNameContext ctx);
	/**
	 * Enter a parse tree produced by the {@code setViewComment}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void enterSetViewComment(MqlBaseParser.SetViewCommentContext ctx);
	/**
	 * Exit a parse tree produced by the {@code setViewComment}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void exitSetViewComment(MqlBaseParser.SetViewCommentContext ctx);
	/**
	 * Enter a parse tree produced by the {@code setViewQuery}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void enterSetViewQuery(MqlBaseParser.SetViewQueryContext ctx);
	/**
	 * Exit a parse tree produced by the {@code setViewQuery}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void exitSetViewQuery(MqlBaseParser.SetViewQueryContext ctx);
	/**
	 * Enter a parse tree produced by the {@code dropView}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void enterDropView(MqlBaseParser.DropViewContext ctx);
	/**
	 * Exit a parse tree produced by the {@code dropView}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void exitDropView(MqlBaseParser.DropViewContext ctx);
	/**
	 * Enter a parse tree produced by the {@code createApplication}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void enterCreateApplication(MqlBaseParser.CreateApplicationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code createApplication}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void exitCreateApplication(MqlBaseParser.CreateApplicationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code renameApplication}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void enterRenameApplication(MqlBaseParser.RenameApplicationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code renameApplication}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void exitRenameApplication(MqlBaseParser.RenameApplicationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code setApplicationName}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void enterSetApplicationName(MqlBaseParser.SetApplicationNameContext ctx);
	/**
	 * Exit a parse tree produced by the {@code setApplicationName}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void exitSetApplicationName(MqlBaseParser.SetApplicationNameContext ctx);
	/**
	 * Enter a parse tree produced by the {@code setApplicationQuerys}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void enterSetApplicationQuerys(MqlBaseParser.SetApplicationQuerysContext ctx);
	/**
	 * Exit a parse tree produced by the {@code setApplicationQuerys}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void exitSetApplicationQuerys(MqlBaseParser.SetApplicationQuerysContext ctx);
	/**
	 * Enter a parse tree produced by the {@code dropApplication}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void enterDropApplication(MqlBaseParser.DropApplicationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code dropApplication}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void exitDropApplication(MqlBaseParser.DropApplicationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showSysInfo}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void enterShowSysInfo(MqlBaseParser.ShowSysInfoContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showSysInfo}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void exitShowSysInfo(MqlBaseParser.ShowSysInfoContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showDatasources}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void enterShowDatasources(MqlBaseParser.ShowDatasourcesContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showDatasources}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void exitShowDatasources(MqlBaseParser.ShowDatasourcesContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showDatabase}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void enterShowDatabase(MqlBaseParser.ShowDatabaseContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showDatabase}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void exitShowDatabase(MqlBaseParser.ShowDatabaseContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showTables}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void enterShowTables(MqlBaseParser.ShowTablesContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showTables}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void exitShowTables(MqlBaseParser.ShowTablesContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showViews}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void enterShowViews(MqlBaseParser.ShowViewsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showViews}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void exitShowViews(MqlBaseParser.ShowViewsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showFunctions}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void enterShowFunctions(MqlBaseParser.ShowFunctionsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showFunctions}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void exitShowFunctions(MqlBaseParser.ShowFunctionsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showUsers}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void enterShowUsers(MqlBaseParser.ShowUsersContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showUsers}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void exitShowUsers(MqlBaseParser.ShowUsersContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showGroups}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void enterShowGroups(MqlBaseParser.ShowGroupsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showGroups}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void exitShowGroups(MqlBaseParser.ShowGroupsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showApplications}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void enterShowApplications(MqlBaseParser.ShowApplicationsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showApplications}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void exitShowApplications(MqlBaseParser.ShowApplicationsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code descDatasource}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void enterDescDatasource(MqlBaseParser.DescDatasourceContext ctx);
	/**
	 * Exit a parse tree produced by the {@code descDatasource}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void exitDescDatasource(MqlBaseParser.DescDatasourceContext ctx);
	/**
	 * Enter a parse tree produced by the {@code descDatabase}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void enterDescDatabase(MqlBaseParser.DescDatabaseContext ctx);
	/**
	 * Exit a parse tree produced by the {@code descDatabase}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void exitDescDatabase(MqlBaseParser.DescDatabaseContext ctx);
	/**
	 * Enter a parse tree produced by the {@code descTable}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void enterDescTable(MqlBaseParser.DescTableContext ctx);
	/**
	 * Exit a parse tree produced by the {@code descTable}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void exitDescTable(MqlBaseParser.DescTableContext ctx);
	/**
	 * Enter a parse tree produced by the {@code descView}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void enterDescView(MqlBaseParser.DescViewContext ctx);
	/**
	 * Exit a parse tree produced by the {@code descView}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void exitDescView(MqlBaseParser.DescViewContext ctx);
	/**
	 * Enter a parse tree produced by the {@code descFunction}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void enterDescFunction(MqlBaseParser.DescFunctionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code descFunction}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void exitDescFunction(MqlBaseParser.DescFunctionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code descUser}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void enterDescUser(MqlBaseParser.DescUserContext ctx);
	/**
	 * Exit a parse tree produced by the {@code descUser}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void exitDescUser(MqlBaseParser.DescUserContext ctx);
	/**
	 * Enter a parse tree produced by the {@code descGroup}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void enterDescGroup(MqlBaseParser.DescGroupContext ctx);
	/**
	 * Exit a parse tree produced by the {@code descGroup}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void exitDescGroup(MqlBaseParser.DescGroupContext ctx);
	/**
	 * Enter a parse tree produced by the {@code explain}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void enterExplain(MqlBaseParser.ExplainContext ctx);
	/**
	 * Exit a parse tree produced by the {@code explain}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void exitExplain(MqlBaseParser.ExplainContext ctx);
	/**
	 * Enter a parse tree produced by the {@code setConfiguration}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void enterSetConfiguration(MqlBaseParser.SetConfigurationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code setConfiguration}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void exitSetConfiguration(MqlBaseParser.SetConfigurationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code mqlQuery}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void enterMqlQuery(MqlBaseParser.MqlQueryContext ctx);
	/**
	 * Exit a parse tree produced by the {@code mqlQuery}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void exitMqlQuery(MqlBaseParser.MqlQueryContext ctx);
	/**
	 * Enter a parse tree produced by the {@code insertInto}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void enterInsertInto(MqlBaseParser.InsertIntoContext ctx);
	/**
	 * Exit a parse tree produced by the {@code insertInto}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void exitInsertInto(MqlBaseParser.InsertIntoContext ctx);
	/**
	 * Enter a parse tree produced by the {@code insertOverwrite}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void enterInsertOverwrite(MqlBaseParser.InsertOverwriteContext ctx);
	/**
	 * Exit a parse tree produced by the {@code insertOverwrite}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void exitInsertOverwrite(MqlBaseParser.InsertOverwriteContext ctx);
	/**
	 * Enter a parse tree produced by the {@code createTemporaryView}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void enterCreateTemporaryView(MqlBaseParser.CreateTemporaryViewContext ctx);
	/**
	 * Exit a parse tree produced by the {@code createTemporaryView}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void exitCreateTemporaryView(MqlBaseParser.CreateTemporaryViewContext ctx);
	/**
	 * Enter a parse tree produced by the {@code createTemporaryFunction}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void enterCreateTemporaryFunction(MqlBaseParser.CreateTemporaryFunctionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code createTemporaryFunction}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void exitCreateTemporaryFunction(MqlBaseParser.CreateTemporaryFunctionContext ctx);
	/**
	 * Enter a parse tree produced by {@link MqlBaseParser#appCmds}.
	 * @param ctx the parse tree
	 */
	void enterAppCmds(MqlBaseParser.AppCmdsContext ctx);
	/**
	 * Exit a parse tree produced by {@link MqlBaseParser#appCmds}.
	 * @param ctx the parse tree
	 */
	void exitAppCmds(MqlBaseParser.AppCmdsContext ctx);
	/**
	 * Enter a parse tree produced by {@link MqlBaseParser#nonLastCmdList}.
	 * @param ctx the parse tree
	 */
	void enterNonLastCmdList(MqlBaseParser.NonLastCmdListContext ctx);
	/**
	 * Exit a parse tree produced by {@link MqlBaseParser#nonLastCmdList}.
	 * @param ctx the parse tree
	 */
	void exitNonLastCmdList(MqlBaseParser.NonLastCmdListContext ctx);
	/**
	 * Enter a parse tree produced by {@link MqlBaseParser#nonLastCmd}.
	 * @param ctx the parse tree
	 */
	void enterNonLastCmd(MqlBaseParser.NonLastCmdContext ctx);
	/**
	 * Exit a parse tree produced by {@link MqlBaseParser#nonLastCmd}.
	 * @param ctx the parse tree
	 */
	void exitNonLastCmd(MqlBaseParser.NonLastCmdContext ctx);
	/**
	 * Enter a parse tree produced by {@link MqlBaseParser#lastCmd}.
	 * @param ctx the parse tree
	 */
	void enterLastCmd(MqlBaseParser.LastCmdContext ctx);
	/**
	 * Exit a parse tree produced by {@link MqlBaseParser#lastCmd}.
	 * @param ctx the parse tree
	 */
	void exitLastCmd(MqlBaseParser.LastCmdContext ctx);
	/**
	 * Enter a parse tree produced by {@link MqlBaseParser#insertIntoCmd}.
	 * @param ctx the parse tree
	 */
	void enterInsertIntoCmd(MqlBaseParser.InsertIntoCmdContext ctx);
	/**
	 * Exit a parse tree produced by {@link MqlBaseParser#insertIntoCmd}.
	 * @param ctx the parse tree
	 */
	void exitInsertIntoCmd(MqlBaseParser.InsertIntoCmdContext ctx);
	/**
	 * Enter a parse tree produced by {@link MqlBaseParser#insertOverwriteCmd}.
	 * @param ctx the parse tree
	 */
	void enterInsertOverwriteCmd(MqlBaseParser.InsertOverwriteCmdContext ctx);
	/**
	 * Exit a parse tree produced by {@link MqlBaseParser#insertOverwriteCmd}.
	 * @param ctx the parse tree
	 */
	void exitInsertOverwriteCmd(MqlBaseParser.InsertOverwriteCmdContext ctx);
	/**
	 * Enter a parse tree produced by {@link MqlBaseParser#createTemporaryViewCmd}.
	 * @param ctx the parse tree
	 */
	void enterCreateTemporaryViewCmd(MqlBaseParser.CreateTemporaryViewCmdContext ctx);
	/**
	 * Exit a parse tree produced by {@link MqlBaseParser#createTemporaryViewCmd}.
	 * @param ctx the parse tree
	 */
	void exitCreateTemporaryViewCmd(MqlBaseParser.CreateTemporaryViewCmdContext ctx);
	/**
	 * Enter a parse tree produced by {@link MqlBaseParser#createTemporaryFunctionCmd}.
	 * @param ctx the parse tree
	 */
	void enterCreateTemporaryFunctionCmd(MqlBaseParser.CreateTemporaryFunctionCmdContext ctx);
	/**
	 * Exit a parse tree produced by {@link MqlBaseParser#createTemporaryFunctionCmd}.
	 * @param ctx the parse tree
	 */
	void exitCreateTemporaryFunctionCmd(MqlBaseParser.CreateTemporaryFunctionCmdContext ctx);
	/**
	 * Enter a parse tree produced by {@link MqlBaseParser#query}.
	 * @param ctx the parse tree
	 */
	void enterQuery(MqlBaseParser.QueryContext ctx);
	/**
	 * Exit a parse tree produced by {@link MqlBaseParser#query}.
	 * @param ctx the parse tree
	 */
	void exitQuery(MqlBaseParser.QueryContext ctx);
	/**
	 * Enter a parse tree produced by {@link MqlBaseParser#ctes}.
	 * @param ctx the parse tree
	 */
	void enterCtes(MqlBaseParser.CtesContext ctx);
	/**
	 * Exit a parse tree produced by {@link MqlBaseParser#ctes}.
	 * @param ctx the parse tree
	 */
	void exitCtes(MqlBaseParser.CtesContext ctx);
	/**
	 * Enter a parse tree produced by the {@code complexDataType}
	 * labeled alternative in {@link MqlBaseParser#dataType}.
	 * @param ctx the parse tree
	 */
	void enterComplexDataType(MqlBaseParser.ComplexDataTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code complexDataType}
	 * labeled alternative in {@link MqlBaseParser#dataType}.
	 * @param ctx the parse tree
	 */
	void exitComplexDataType(MqlBaseParser.ComplexDataTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code primitiveDataType}
	 * labeled alternative in {@link MqlBaseParser#dataType}.
	 * @param ctx the parse tree
	 */
	void enterPrimitiveDataType(MqlBaseParser.PrimitiveDataTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code primitiveDataType}
	 * labeled alternative in {@link MqlBaseParser#dataType}.
	 * @param ctx the parse tree
	 */
	void exitPrimitiveDataType(MqlBaseParser.PrimitiveDataTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link MqlBaseParser#colTypeList}.
	 * @param ctx the parse tree
	 */
	void enterColTypeList(MqlBaseParser.ColTypeListContext ctx);
	/**
	 * Exit a parse tree produced by {@link MqlBaseParser#colTypeList}.
	 * @param ctx the parse tree
	 */
	void exitColTypeList(MqlBaseParser.ColTypeListContext ctx);
	/**
	 * Enter a parse tree produced by {@link MqlBaseParser#colType}.
	 * @param ctx the parse tree
	 */
	void enterColType(MqlBaseParser.ColTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link MqlBaseParser#colType}.
	 * @param ctx the parse tree
	 */
	void exitColType(MqlBaseParser.ColTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link MqlBaseParser#complexColTypeList}.
	 * @param ctx the parse tree
	 */
	void enterComplexColTypeList(MqlBaseParser.ComplexColTypeListContext ctx);
	/**
	 * Exit a parse tree produced by {@link MqlBaseParser#complexColTypeList}.
	 * @param ctx the parse tree
	 */
	void exitComplexColTypeList(MqlBaseParser.ComplexColTypeListContext ctx);
	/**
	 * Enter a parse tree produced by {@link MqlBaseParser#complexColType}.
	 * @param ctx the parse tree
	 */
	void enterComplexColType(MqlBaseParser.ComplexColTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link MqlBaseParser#complexColType}.
	 * @param ctx the parse tree
	 */
	void exitComplexColType(MqlBaseParser.ComplexColTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link MqlBaseParser#namedQuery}.
	 * @param ctx the parse tree
	 */
	void enterNamedQuery(MqlBaseParser.NamedQueryContext ctx);
	/**
	 * Exit a parse tree produced by {@link MqlBaseParser#namedQuery}.
	 * @param ctx the parse tree
	 */
	void exitNamedQuery(MqlBaseParser.NamedQueryContext ctx);
	/**
	 * Enter a parse tree produced by {@link MqlBaseParser#mountTableList}.
	 * @param ctx the parse tree
	 */
	void enterMountTableList(MqlBaseParser.MountTableListContext ctx);
	/**
	 * Exit a parse tree produced by {@link MqlBaseParser#mountTableList}.
	 * @param ctx the parse tree
	 */
	void exitMountTableList(MqlBaseParser.MountTableListContext ctx);
	/**
	 * Enter a parse tree produced by {@link MqlBaseParser#mountTableOptions}.
	 * @param ctx the parse tree
	 */
	void enterMountTableOptions(MqlBaseParser.MountTableOptionsContext ctx);
	/**
	 * Exit a parse tree produced by {@link MqlBaseParser#mountTableOptions}.
	 * @param ctx the parse tree
	 */
	void exitMountTableOptions(MqlBaseParser.MountTableOptionsContext ctx);
	/**
	 * Enter a parse tree produced by {@link MqlBaseParser#privilegeList}.
	 * @param ctx the parse tree
	 */
	void enterPrivilegeList(MqlBaseParser.PrivilegeListContext ctx);
	/**
	 * Exit a parse tree produced by {@link MqlBaseParser#privilegeList}.
	 * @param ctx the parse tree
	 */
	void exitPrivilegeList(MqlBaseParser.PrivilegeListContext ctx);
	/**
	 * Enter a parse tree produced by {@link MqlBaseParser#privilege}.
	 * @param ctx the parse tree
	 */
	void enterPrivilege(MqlBaseParser.PrivilegeContext ctx);
	/**
	 * Exit a parse tree produced by {@link MqlBaseParser#privilege}.
	 * @param ctx the parse tree
	 */
	void exitPrivilege(MqlBaseParser.PrivilegeContext ctx);
	/**
	 * Enter a parse tree produced by {@link MqlBaseParser#qualifiedColumnList}.
	 * @param ctx the parse tree
	 */
	void enterQualifiedColumnList(MqlBaseParser.QualifiedColumnListContext ctx);
	/**
	 * Exit a parse tree produced by {@link MqlBaseParser#qualifiedColumnList}.
	 * @param ctx the parse tree
	 */
	void exitQualifiedColumnList(MqlBaseParser.QualifiedColumnListContext ctx);
	/**
	 * Enter a parse tree produced by {@link MqlBaseParser#columnIdentifier}.
	 * @param ctx the parse tree
	 */
	void enterColumnIdentifier(MqlBaseParser.ColumnIdentifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link MqlBaseParser#columnIdentifier}.
	 * @param ctx the parse tree
	 */
	void exitColumnIdentifier(MqlBaseParser.ColumnIdentifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link MqlBaseParser#identifierStarList}.
	 * @param ctx the parse tree
	 */
	void enterIdentifierStarList(MqlBaseParser.IdentifierStarListContext ctx);
	/**
	 * Exit a parse tree produced by {@link MqlBaseParser#identifierStarList}.
	 * @param ctx the parse tree
	 */
	void exitIdentifierStarList(MqlBaseParser.IdentifierStarListContext ctx);
	/**
	 * Enter a parse tree produced by {@link MqlBaseParser#addUser}.
	 * @param ctx the parse tree
	 */
	void enterAddUser(MqlBaseParser.AddUserContext ctx);
	/**
	 * Exit a parse tree produced by {@link MqlBaseParser#addUser}.
	 * @param ctx the parse tree
	 */
	void exitAddUser(MqlBaseParser.AddUserContext ctx);
	/**
	 * Enter a parse tree produced by {@link MqlBaseParser#removeUser}.
	 * @param ctx the parse tree
	 */
	void enterRemoveUser(MqlBaseParser.RemoveUserContext ctx);
	/**
	 * Exit a parse tree produced by {@link MqlBaseParser#removeUser}.
	 * @param ctx the parse tree
	 */
	void exitRemoveUser(MqlBaseParser.RemoveUserContext ctx);
	/**
	 * Enter a parse tree produced by {@link MqlBaseParser#identifierList}.
	 * @param ctx the parse tree
	 */
	void enterIdentifierList(MqlBaseParser.IdentifierListContext ctx);
	/**
	 * Exit a parse tree produced by {@link MqlBaseParser#identifierList}.
	 * @param ctx the parse tree
	 */
	void exitIdentifierList(MqlBaseParser.IdentifierListContext ctx);
	/**
	 * Enter a parse tree produced by {@link MqlBaseParser#funcIdentifier}.
	 * @param ctx the parse tree
	 */
	void enterFuncIdentifier(MqlBaseParser.FuncIdentifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link MqlBaseParser#funcIdentifier}.
	 * @param ctx the parse tree
	 */
	void exitFuncIdentifier(MqlBaseParser.FuncIdentifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link MqlBaseParser#tableIdentifier}.
	 * @param ctx the parse tree
	 */
	void enterTableIdentifier(MqlBaseParser.TableIdentifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link MqlBaseParser#tableIdentifier}.
	 * @param ctx the parse tree
	 */
	void exitTableIdentifier(MqlBaseParser.TableIdentifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link MqlBaseParser#propertyList}.
	 * @param ctx the parse tree
	 */
	void enterPropertyList(MqlBaseParser.PropertyListContext ctx);
	/**
	 * Exit a parse tree produced by {@link MqlBaseParser#propertyList}.
	 * @param ctx the parse tree
	 */
	void exitPropertyList(MqlBaseParser.PropertyListContext ctx);
	/**
	 * Enter a parse tree produced by {@link MqlBaseParser#property}.
	 * @param ctx the parse tree
	 */
	void enterProperty(MqlBaseParser.PropertyContext ctx);
	/**
	 * Exit a parse tree produced by {@link MqlBaseParser#property}.
	 * @param ctx the parse tree
	 */
	void exitProperty(MqlBaseParser.PropertyContext ctx);
	/**
	 * Enter a parse tree produced by {@link MqlBaseParser#propertyKey}.
	 * @param ctx the parse tree
	 */
	void enterPropertyKey(MqlBaseParser.PropertyKeyContext ctx);
	/**
	 * Exit a parse tree produced by {@link MqlBaseParser#propertyKey}.
	 * @param ctx the parse tree
	 */
	void exitPropertyKey(MqlBaseParser.PropertyKeyContext ctx);
	/**
	 * Enter a parse tree produced by {@link MqlBaseParser#password}.
	 * @param ctx the parse tree
	 */
	void enterPassword(MqlBaseParser.PasswordContext ctx);
	/**
	 * Exit a parse tree produced by {@link MqlBaseParser#password}.
	 * @param ctx the parse tree
	 */
	void exitPassword(MqlBaseParser.PasswordContext ctx);
	/**
	 * Enter a parse tree produced by {@link MqlBaseParser#identifier}.
	 * @param ctx the parse tree
	 */
	void enterIdentifier(MqlBaseParser.IdentifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link MqlBaseParser#identifier}.
	 * @param ctx the parse tree
	 */
	void exitIdentifier(MqlBaseParser.IdentifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link MqlBaseParser#generalIdentifier}.
	 * @param ctx the parse tree
	 */
	void enterGeneralIdentifier(MqlBaseParser.GeneralIdentifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link MqlBaseParser#generalIdentifier}.
	 * @param ctx the parse tree
	 */
	void exitGeneralIdentifier(MqlBaseParser.GeneralIdentifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link MqlBaseParser#nonReserved}.
	 * @param ctx the parse tree
	 */
	void enterNonReserved(MqlBaseParser.NonReservedContext ctx);
	/**
	 * Exit a parse tree produced by {@link MqlBaseParser#nonReserved}.
	 * @param ctx the parse tree
	 */
	void exitNonReserved(MqlBaseParser.NonReservedContext ctx);
}