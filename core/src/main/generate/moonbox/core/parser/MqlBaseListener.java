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
	 * Enter a parse tree produced by the {@code setOrganizationOptions}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void enterSetOrganizationOptions(MqlBaseParser.SetOrganizationOptionsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code setOrganizationOptions}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void exitSetOrganizationOptions(MqlBaseParser.SetOrganizationOptionsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code removeOrganizationOptions}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void enterRemoveOrganizationOptions(MqlBaseParser.RemoveOrganizationOptionsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code removeOrganizationOptions}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void exitRemoveOrganizationOptions(MqlBaseParser.RemoveOrganizationOptionsContext ctx);
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
	 * Enter a parse tree produced by the {@code setSaOptions}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void enterSetSaOptions(MqlBaseParser.SetSaOptionsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code setSaOptions}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void exitSetSaOptions(MqlBaseParser.SetSaOptionsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code removeSaOptions}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void enterRemoveSaOptions(MqlBaseParser.RemoveSaOptionsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code removeSaOptions}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void exitRemoveSaOptions(MqlBaseParser.RemoveSaOptionsContext ctx);
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
	 * Enter a parse tree produced by the {@code grantPrivilegeToUsers}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void enterGrantPrivilegeToUsers(MqlBaseParser.GrantPrivilegeToUsersContext ctx);
	/**
	 * Exit a parse tree produced by the {@code grantPrivilegeToUsers}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void exitGrantPrivilegeToUsers(MqlBaseParser.GrantPrivilegeToUsersContext ctx);
	/**
	 * Enter a parse tree produced by the {@code revokePrivilegeFromUsers}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void enterRevokePrivilegeFromUsers(MqlBaseParser.RevokePrivilegeFromUsersContext ctx);
	/**
	 * Exit a parse tree produced by the {@code revokePrivilegeFromUsers}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void exitRevokePrivilegeFromUsers(MqlBaseParser.RevokePrivilegeFromUsersContext ctx);
	/**
	 * Enter a parse tree produced by the {@code grantPrivilegeToGroup}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void enterGrantPrivilegeToGroup(MqlBaseParser.GrantPrivilegeToGroupContext ctx);
	/**
	 * Exit a parse tree produced by the {@code grantPrivilegeToGroup}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void exitGrantPrivilegeToGroup(MqlBaseParser.GrantPrivilegeToGroupContext ctx);
	/**
	 * Enter a parse tree produced by the {@code revokePrivilegeFromGroup}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void enterRevokePrivilegeFromGroup(MqlBaseParser.RevokePrivilegeFromGroupContext ctx);
	/**
	 * Exit a parse tree produced by the {@code revokePrivilegeFromGroup}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void exitRevokePrivilegeFromGroup(MqlBaseParser.RevokePrivilegeFromGroupContext ctx);
	/**
	 * Enter a parse tree produced by the {@code grantResourcePrivilegeToUsers}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void enterGrantResourcePrivilegeToUsers(MqlBaseParser.GrantResourcePrivilegeToUsersContext ctx);
	/**
	 * Exit a parse tree produced by the {@code grantResourcePrivilegeToUsers}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void exitGrantResourcePrivilegeToUsers(MqlBaseParser.GrantResourcePrivilegeToUsersContext ctx);
	/**
	 * Enter a parse tree produced by the {@code revokeResourcePrivilegeFromUsers}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void enterRevokeResourcePrivilegeFromUsers(MqlBaseParser.RevokeResourcePrivilegeFromUsersContext ctx);
	/**
	 * Exit a parse tree produced by the {@code revokeResourcePrivilegeFromUsers}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void exitRevokeResourcePrivilegeFromUsers(MqlBaseParser.RevokeResourcePrivilegeFromUsersContext ctx);
	/**
	 * Enter a parse tree produced by the {@code grantResourcePrivilegeToGroup}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void enterGrantResourcePrivilegeToGroup(MqlBaseParser.GrantResourcePrivilegeToGroupContext ctx);
	/**
	 * Exit a parse tree produced by the {@code grantResourcePrivilegeToGroup}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void exitGrantResourcePrivilegeToGroup(MqlBaseParser.GrantResourcePrivilegeToGroupContext ctx);
	/**
	 * Enter a parse tree produced by the {@code revokeResourcePrivilegeFromGroup}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void enterRevokeResourcePrivilegeFromGroup(MqlBaseParser.RevokeResourcePrivilegeFromGroupContext ctx);
	/**
	 * Exit a parse tree produced by the {@code revokeResourcePrivilegeFromGroup}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void exitRevokeResourcePrivilegeFromGroup(MqlBaseParser.RevokeResourcePrivilegeFromGroupContext ctx);
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
	 * Enter a parse tree produced by the {@code setUserOptions}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void enterSetUserOptions(MqlBaseParser.SetUserOptionsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code setUserOptions}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void exitSetUserOptions(MqlBaseParser.SetUserOptionsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code removeUserOptions}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void enterRemoveUserOptions(MqlBaseParser.RemoveUserOptionsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code removeUserOptions}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void exitRemoveUserOptions(MqlBaseParser.RemoveUserOptionsContext ctx);
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
	 * Enter a parse tree produced by the {@code addGroupUser}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void enterAddGroupUser(MqlBaseParser.AddGroupUserContext ctx);
	/**
	 * Exit a parse tree produced by the {@code addGroupUser}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void exitAddGroupUser(MqlBaseParser.AddGroupUserContext ctx);
	/**
	 * Enter a parse tree produced by the {@code removeGroupUser}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void enterRemoveGroupUser(MqlBaseParser.RemoveGroupUserContext ctx);
	/**
	 * Exit a parse tree produced by the {@code removeGroupUser}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void exitRemoveGroupUser(MqlBaseParser.RemoveGroupUserContext ctx);
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
	 * Enter a parse tree produced by the {@code removeTableProperties}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void enterRemoveTableProperties(MqlBaseParser.RemoveTablePropertiesContext ctx);
	/**
	 * Exit a parse tree produced by the {@code removeTableProperties}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void exitRemoveTableProperties(MqlBaseParser.RemoveTablePropertiesContext ctx);
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
	 * Enter a parse tree produced by the {@code mountDatabase}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void enterMountDatabase(MqlBaseParser.MountDatabaseContext ctx);
	/**
	 * Exit a parse tree produced by the {@code mountDatabase}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void exitMountDatabase(MqlBaseParser.MountDatabaseContext ctx);
	/**
	 * Enter a parse tree produced by the {@code unmountDatabase}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void enterUnmountDatabase(MqlBaseParser.UnmountDatabaseContext ctx);
	/**
	 * Exit a parse tree produced by the {@code unmountDatabase}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void exitUnmountDatabase(MqlBaseParser.UnmountDatabaseContext ctx);
	/**
	 * Enter a parse tree produced by the {@code setDatabaseProperties}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void enterSetDatabaseProperties(MqlBaseParser.SetDatabasePropertiesContext ctx);
	/**
	 * Exit a parse tree produced by the {@code setDatabaseProperties}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void exitSetDatabaseProperties(MqlBaseParser.SetDatabasePropertiesContext ctx);
	/**
	 * Enter a parse tree produced by the {@code removeDatabaseProperties}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void enterRemoveDatabaseProperties(MqlBaseParser.RemoveDatabasePropertiesContext ctx);
	/**
	 * Exit a parse tree produced by the {@code removeDatabaseProperties}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void exitRemoveDatabaseProperties(MqlBaseParser.RemoveDatabasePropertiesContext ctx);
	/**
	 * Enter a parse tree produced by the {@code refreshDatabase}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void enterRefreshDatabase(MqlBaseParser.RefreshDatabaseContext ctx);
	/**
	 * Exit a parse tree produced by the {@code refreshDatabase}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void exitRefreshDatabase(MqlBaseParser.RefreshDatabaseContext ctx);
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
	 * Enter a parse tree produced by the {@code createProcedure}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void enterCreateProcedure(MqlBaseParser.CreateProcedureContext ctx);
	/**
	 * Exit a parse tree produced by the {@code createProcedure}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void exitCreateProcedure(MqlBaseParser.CreateProcedureContext ctx);
	/**
	 * Enter a parse tree produced by the {@code renameProcedure}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void enterRenameProcedure(MqlBaseParser.RenameProcedureContext ctx);
	/**
	 * Exit a parse tree produced by the {@code renameProcedure}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void exitRenameProcedure(MqlBaseParser.RenameProcedureContext ctx);
	/**
	 * Enter a parse tree produced by the {@code setProcedureName}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void enterSetProcedureName(MqlBaseParser.SetProcedureNameContext ctx);
	/**
	 * Exit a parse tree produced by the {@code setProcedureName}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void exitSetProcedureName(MqlBaseParser.SetProcedureNameContext ctx);
	/**
	 * Enter a parse tree produced by the {@code setProcedureQuerys}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void enterSetProcedureQuerys(MqlBaseParser.SetProcedureQuerysContext ctx);
	/**
	 * Exit a parse tree produced by the {@code setProcedureQuerys}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void exitSetProcedureQuerys(MqlBaseParser.SetProcedureQuerysContext ctx);
	/**
	 * Enter a parse tree produced by the {@code dropProcedure}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void enterDropProcedure(MqlBaseParser.DropProcedureContext ctx);
	/**
	 * Exit a parse tree produced by the {@code dropProcedure}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void exitDropProcedure(MqlBaseParser.DropProcedureContext ctx);
	/**
	 * Enter a parse tree produced by the {@code createEvent}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void enterCreateEvent(MqlBaseParser.CreateEventContext ctx);
	/**
	 * Exit a parse tree produced by the {@code createEvent}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void exitCreateEvent(MqlBaseParser.CreateEventContext ctx);
	/**
	 * Enter a parse tree produced by the {@code renameEvent}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void enterRenameEvent(MqlBaseParser.RenameEventContext ctx);
	/**
	 * Exit a parse tree produced by the {@code renameEvent}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void exitRenameEvent(MqlBaseParser.RenameEventContext ctx);
	/**
	 * Enter a parse tree produced by the {@code setDefiner}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void enterSetDefiner(MqlBaseParser.SetDefinerContext ctx);
	/**
	 * Exit a parse tree produced by the {@code setDefiner}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void exitSetDefiner(MqlBaseParser.SetDefinerContext ctx);
	/**
	 * Enter a parse tree produced by the {@code setEventName}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void enterSetEventName(MqlBaseParser.SetEventNameContext ctx);
	/**
	 * Exit a parse tree produced by the {@code setEventName}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void exitSetEventName(MqlBaseParser.SetEventNameContext ctx);
	/**
	 * Enter a parse tree produced by the {@code setEventSchedule}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void enterSetEventSchedule(MqlBaseParser.SetEventScheduleContext ctx);
	/**
	 * Exit a parse tree produced by the {@code setEventSchedule}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void exitSetEventSchedule(MqlBaseParser.SetEventScheduleContext ctx);
	/**
	 * Enter a parse tree produced by the {@code setEventEnable}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void enterSetEventEnable(MqlBaseParser.SetEventEnableContext ctx);
	/**
	 * Exit a parse tree produced by the {@code setEventEnable}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void exitSetEventEnable(MqlBaseParser.SetEventEnableContext ctx);
	/**
	 * Enter a parse tree produced by the {@code dropEvent}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void enterDropEvent(MqlBaseParser.DropEventContext ctx);
	/**
	 * Exit a parse tree produced by the {@code dropEvent}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void exitDropEvent(MqlBaseParser.DropEventContext ctx);
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
	 * Enter a parse tree produced by the {@code showJobs}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void enterShowJobs(MqlBaseParser.ShowJobsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showJobs}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void exitShowJobs(MqlBaseParser.ShowJobsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showRunningEvents}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void enterShowRunningEvents(MqlBaseParser.ShowRunningEventsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showRunningEvents}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void exitShowRunningEvents(MqlBaseParser.ShowRunningEventsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showEvents}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void enterShowEvents(MqlBaseParser.ShowEventsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showEvents}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void exitShowEvents(MqlBaseParser.ShowEventsContext ctx);
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
	 * Enter a parse tree produced by the {@code showOrgs}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void enterShowOrgs(MqlBaseParser.ShowOrgsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showOrgs}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void exitShowOrgs(MqlBaseParser.ShowOrgsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showSas}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void enterShowSas(MqlBaseParser.ShowSasContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showSas}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void exitShowSas(MqlBaseParser.ShowSasContext ctx);
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
	 * Enter a parse tree produced by the {@code showUsersInGroup}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void enterShowUsersInGroup(MqlBaseParser.ShowUsersInGroupContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showUsersInGroup}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void exitShowUsersInGroup(MqlBaseParser.ShowUsersInGroupContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showProcedures}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void enterShowProcedures(MqlBaseParser.ShowProceduresContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showProcedures}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void exitShowProcedures(MqlBaseParser.ShowProceduresContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showVariable}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void enterShowVariable(MqlBaseParser.ShowVariableContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showVariable}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void exitShowVariable(MqlBaseParser.ShowVariableContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showGrants}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void enterShowGrants(MqlBaseParser.ShowGrantsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showGrants}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void exitShowGrants(MqlBaseParser.ShowGrantsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showCreateTable}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void enterShowCreateTable(MqlBaseParser.ShowCreateTableContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showCreateTable}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void exitShowCreateTable(MqlBaseParser.ShowCreateTableContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showSchema}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void enterShowSchema(MqlBaseParser.ShowSchemaContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showSchema}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void exitShowSchema(MqlBaseParser.ShowSchemaContext ctx);
	/**
	 * Enter a parse tree produced by the {@code descEvent}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void enterDescEvent(MqlBaseParser.DescEventContext ctx);
	/**
	 * Exit a parse tree produced by the {@code descEvent}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void exitDescEvent(MqlBaseParser.DescEventContext ctx);
	/**
	 * Enter a parse tree produced by the {@code descProcedure}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void enterDescProcedure(MqlBaseParser.DescProcedureContext ctx);
	/**
	 * Exit a parse tree produced by the {@code descProcedure}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void exitDescProcedure(MqlBaseParser.DescProcedureContext ctx);
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
	 * Enter a parse tree produced by the {@code descOrg}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void enterDescOrg(MqlBaseParser.DescOrgContext ctx);
	/**
	 * Exit a parse tree produced by the {@code descOrg}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void exitDescOrg(MqlBaseParser.DescOrgContext ctx);
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
	 * Enter a parse tree produced by the {@code refreshTable}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void enterRefreshTable(MqlBaseParser.RefreshTableContext ctx);
	/**
	 * Exit a parse tree produced by the {@code refreshTable}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void exitRefreshTable(MqlBaseParser.RefreshTableContext ctx);
	/**
	 * Enter a parse tree produced by the {@code refreshFunction}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void enterRefreshFunction(MqlBaseParser.RefreshFunctionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code refreshFunction}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void exitRefreshFunction(MqlBaseParser.RefreshFunctionContext ctx);
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
	 * Enter a parse tree produced by the {@code statement}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void enterStatement(MqlBaseParser.StatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code statement}
	 * labeled alternative in {@link MqlBaseParser#mql}.
	 * @param ctx the parse tree
	 */
	void exitStatement(MqlBaseParser.StatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link MqlBaseParser#procCmds}.
	 * @param ctx the parse tree
	 */
	void enterProcCmds(MqlBaseParser.ProcCmdsContext ctx);
	/**
	 * Exit a parse tree produced by {@link MqlBaseParser#procCmds}.
	 * @param ctx the parse tree
	 */
	void exitProcCmds(MqlBaseParser.ProcCmdsContext ctx);
	/**
	 * Enter a parse tree produced by {@link MqlBaseParser#definer}.
	 * @param ctx the parse tree
	 */
	void enterDefiner(MqlBaseParser.DefinerContext ctx);
	/**
	 * Exit a parse tree produced by {@link MqlBaseParser#definer}.
	 * @param ctx the parse tree
	 */
	void exitDefiner(MqlBaseParser.DefinerContext ctx);
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
	 * Enter a parse tree produced by {@link MqlBaseParser#partitionSpec}.
	 * @param ctx the parse tree
	 */
	void enterPartitionSpec(MqlBaseParser.PartitionSpecContext ctx);
	/**
	 * Exit a parse tree produced by {@link MqlBaseParser#partitionSpec}.
	 * @param ctx the parse tree
	 */
	void exitPartitionSpec(MqlBaseParser.PartitionSpecContext ctx);
	/**
	 * Enter a parse tree produced by {@link MqlBaseParser#coalesceSpec}.
	 * @param ctx the parse tree
	 */
	void enterCoalesceSpec(MqlBaseParser.CoalesceSpecContext ctx);
	/**
	 * Exit a parse tree produced by {@link MqlBaseParser#coalesceSpec}.
	 * @param ctx the parse tree
	 */
	void exitCoalesceSpec(MqlBaseParser.CoalesceSpecContext ctx);
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
	 * Enter a parse tree produced by {@link MqlBaseParser#grantPrivilegeList}.
	 * @param ctx the parse tree
	 */
	void enterGrantPrivilegeList(MqlBaseParser.GrantPrivilegeListContext ctx);
	/**
	 * Exit a parse tree produced by {@link MqlBaseParser#grantPrivilegeList}.
	 * @param ctx the parse tree
	 */
	void exitGrantPrivilegeList(MqlBaseParser.GrantPrivilegeListContext ctx);
	/**
	 * Enter a parse tree produced by {@link MqlBaseParser#grantPrivilege}.
	 * @param ctx the parse tree
	 */
	void enterGrantPrivilege(MqlBaseParser.GrantPrivilegeContext ctx);
	/**
	 * Exit a parse tree produced by {@link MqlBaseParser#grantPrivilege}.
	 * @param ctx the parse tree
	 */
	void exitGrantPrivilege(MqlBaseParser.GrantPrivilegeContext ctx);
	/**
	 * Enter a parse tree produced by {@link MqlBaseParser#privileges}.
	 * @param ctx the parse tree
	 */
	void enterPrivileges(MqlBaseParser.PrivilegesContext ctx);
	/**
	 * Exit a parse tree produced by {@link MqlBaseParser#privileges}.
	 * @param ctx the parse tree
	 */
	void exitPrivileges(MqlBaseParser.PrivilegesContext ctx);
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
	 * Enter a parse tree produced by {@link MqlBaseParser#columnIdentifiers}.
	 * @param ctx the parse tree
	 */
	void enterColumnIdentifiers(MqlBaseParser.ColumnIdentifiersContext ctx);
	/**
	 * Exit a parse tree produced by {@link MqlBaseParser#columnIdentifiers}.
	 * @param ctx the parse tree
	 */
	void exitColumnIdentifiers(MqlBaseParser.ColumnIdentifiersContext ctx);
	/**
	 * Enter a parse tree produced by {@link MqlBaseParser#tableCollections}.
	 * @param ctx the parse tree
	 */
	void enterTableCollections(MqlBaseParser.TableCollectionsContext ctx);
	/**
	 * Exit a parse tree produced by {@link MqlBaseParser#tableCollections}.
	 * @param ctx the parse tree
	 */
	void exitTableCollections(MqlBaseParser.TableCollectionsContext ctx);
	/**
	 * Enter a parse tree produced by {@link MqlBaseParser#identifierOrStar}.
	 * @param ctx the parse tree
	 */
	void enterIdentifierOrStar(MqlBaseParser.IdentifierOrStarContext ctx);
	/**
	 * Exit a parse tree produced by {@link MqlBaseParser#identifierOrStar}.
	 * @param ctx the parse tree
	 */
	void exitIdentifierOrStar(MqlBaseParser.IdentifierOrStarContext ctx);
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
	 * Enter a parse tree produced by {@link MqlBaseParser#propertyKeyList}.
	 * @param ctx the parse tree
	 */
	void enterPropertyKeyList(MqlBaseParser.PropertyKeyListContext ctx);
	/**
	 * Exit a parse tree produced by {@link MqlBaseParser#propertyKeyList}.
	 * @param ctx the parse tree
	 */
	void exitPropertyKeyList(MqlBaseParser.PropertyKeyListContext ctx);
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
	 * Enter a parse tree produced by {@link MqlBaseParser#resource}.
	 * @param ctx the parse tree
	 */
	void enterResource(MqlBaseParser.ResourceContext ctx);
	/**
	 * Exit a parse tree produced by {@link MqlBaseParser#resource}.
	 * @param ctx the parse tree
	 */
	void exitResource(MqlBaseParser.ResourceContext ctx);
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