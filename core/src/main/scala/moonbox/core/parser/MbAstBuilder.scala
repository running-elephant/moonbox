/*-
 * <<
 * Moonbox
 * ==
 * Copyright (C) 2016 - 2018 EDP
 * ==
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * >>
 */

package moonbox.core.parser

import java.util
import java.util.Locale

import moonbox.core.catalog.FunctionResource
import moonbox.core.command.PrivilegeType.{PrivilegeType, _}
import moonbox.core.{MbColumnIdentifier, MbFunctionIdentifier, MbTableIdentifier}
import moonbox.core.command._
import moonbox.core.parser.MqlBaseParser._
import org.antlr.v4.runtime.misc.Interval
import org.antlr.v4.runtime.tree.{ParseTree, TerminalNode}
import org.apache.spark.sql.catalyst.parser.ParseException
import org.apache.spark.sql.types.{DecimalType, _}

import scala.collection.JavaConversions._

class MbAstBuilder extends MqlBaseBaseVisitor[AnyRef] {

	protected def typedVisit[T](ctx: ParseTree): T = {
		ctx.accept(this).asInstanceOf[T]
	}

	override def visitSingle(ctx: SingleContext): MbCommand = {
		visit(ctx.mql()).asInstanceOf[MbCommand]
	}

	override def visitMqlQuery(ctx: MqlQueryContext): MbCommand = {
		visitQuery(ctx.query())
	}

	override def visitCreateOrganization(ctx: CreateOrganizationContext): MbCommand = {
		val name = ctx.name.getText
		val desc = Option(ctx.comment).map(_.getText).map(ParserUtils.tripQuotes)
		val ignoreIfExists = ctx.EXISTS() != null
		CreateOrganization(name, desc, ignoreIfExists)
	}

	override def visitRenameOrganization(ctx: RenameOrganizationContext): MbCommand = {
		val name = ctx.name.getText
		val newName = ctx.newName.getText
		AlterOrganizationSetName(name, newName)
	}

	override def visitSetOrganizationName(ctx: SetOrganizationNameContext): MbCommand = {
		val name = ctx.name.getText
		val newName = ctx.newName.getText
		AlterOrganizationSetName(name, newName)
	}

	override def visitSetOrganizationComment(ctx: SetOrganizationCommentContext): MbCommand = {
		val name = ctx.name.getText
		val comment = ParserUtils.tripQuotes(ctx.comment.getText)
		AlterOrganizationSetComment(name, comment)
	}

	override def visitDropOrganization(ctx: DropOrganizationContext): MbCommand = {
		val name = ctx.name.getText
		val ignoreIfNotExists = ctx.EXISTS() != null
		val cascade = ctx.CASCADE() != null
		DropOrganization(name, ignoreIfNotExists, cascade)
	}

	override def visitCreateSa(ctx: CreateSaContext): MbCommand = {
		val name = ctx.name.getText
		val password = ctx.pwd.getText
		val organization = ctx.org.getText
		val ignoreIfExists = ctx.EXISTS() != null
		CreateSa(name, password, organization, ignoreIfExists)
	}

	override def visitRenameSa(ctx: RenameSaContext): MbCommand = {
		val name = ctx.name.getText
		val newName = ctx.newName.getText
		val organization = ctx.org.getText
		AlterSaSetName(name, newName, organization)
	}

	override def visitSetSaName(ctx: SetSaNameContext): MbCommand = {
		val name = ctx.name.getText
		val newName = ctx.newName.getText
		val organization = ctx.org.getText
		AlterSaSetName(name, newName, organization)
	}

	override def visitSetSaPassword(ctx: SetSaPasswordContext): MbCommand = {
		val name = ctx.name.getText
		val password = ctx.pwd.getText
		val organization = ctx.org.getText
		AlterSaSetPassword(name, password, organization)
	}

	override def visitDropSa(ctx: DropSaContext): MbCommand = {
		val name = ctx.name.getText
		val organization = ctx.org.getText
		val ignoreIfNotExists = ctx.EXISTS() != null
		DropSa(name, organization, ignoreIfNotExists)
	}

	override def visitCreateUser(ctx: CreateUserContext): MbCommand = {
		val name = ctx.name.getText
		val password = ctx.pwd.getText
		val ignoreIfExists = ctx.EXISTS() != null
		CreateUser(name, password, ignoreIfExists)
	}

	override def visitRenameUser(ctx: RenameUserContext): MbCommand = {
		val name = ctx.name.getText
		val newName = ctx.newName.getText
		AlterUserSetName(name, newName)
	}

	override def visitSetUserName(ctx: SetUserNameContext): MbCommand = {
		val name = ctx.name.getText
		val newName = ctx.newName.getText
		AlterUserSetName(name, newName)
	}

	override def visitSetUserPassword(ctx: SetUserPasswordContext): MbCommand = {
		val name = ctx.name.getText
		val password = ctx.pwd.getText
		AlterUserSetPassword(name, password)
	}

	override def visitDropUser(ctx: DropUserContext): MbCommand = {
		val name = ctx.name.getText
		val ignoreIfNotExists = ctx.EXISTS() != null
		DropUser(name, ignoreIfNotExists)
	}

	override def visitCreateGroup(ctx: CreateGroupContext): MbCommand = {
		val name = ctx.name.getText
		val desc = Option(ctx.comment).map(_.getText).map(ParserUtils.tripQuotes)
		val ignoreIfExists = ctx.EXISTS() != null
		CreateGroup(name, desc, ignoreIfExists)
	}

	override def visitRenameGroup(ctx: RenameGroupContext): MbCommand = {
		val name = ctx.name.getText
		val newName = ctx.newName.getText
		AlterGroupSetName(name, newName)
	}

	override def visitSetGroupName(ctx: SetGroupNameContext): MbCommand = {
		val name = ctx.name.getText
		val newName = ctx.newName.getText
		AlterGroupSetName(name, newName)
	}

	override def visitSetGroupComment(ctx: SetGroupCommentContext): MbCommand = {
		val name = ctx.name.getText
		val desc = ParserUtils.tripQuotes(ctx.comment.getText)
		AlterGroupSetComment(name, desc)
	}

	override def visitAddUsersToGroup(ctx: AddUsersToGroupContext): MbCommand = {
		val name = ctx.name.getText
		val addUsers = visitAddUser(ctx.addUser())
		val removeUsers = Option(ctx.removeUser()).map(visitRemoveUser).getOrElse(Seq())
		val addFirst = if (removeUsers.nonEmpty) {
			ctx.addUser().start.getStartIndex < ctx.removeUser().start.getStartIndex
		} else true
		AlterGroupSetUser(name, addUsers, removeUsers, addFirst)
	}

	override def visitRemoveUsersFromGroup(ctx: RemoveUsersFromGroupContext): MbCommand = {
		val name = ctx.name.getText
		val addUsers = Option(ctx.addUser()).map(visitAddUser).getOrElse(Seq())
		val removeUsers = visitRemoveUser(ctx.removeUser())
		val addFirst = if (addUsers.nonEmpty) {
			ctx.addUser().start.getStartIndex < ctx.removeUser().start.getStartIndex
		} else false
		AlterGroupSetUser(name, addUsers, removeUsers, addFirst)
	}

	override def visitDropGroup(ctx: DropGroupContext): MbCommand = {
		val name = ctx.name.getText
		val ignoreIfNotExists = ctx.EXISTS() != null
		val cascade = ctx.CASCADE() != null
		DropGroup(name, ignoreIfNotExists, cascade)
	}

	override def visitAddUser(ctx: AddUserContext): Seq[String] = {
		visitIdentifierList(ctx.identifierList())
	}

	override def visitRemoveUser(ctx: RemoveUserContext): Seq[String] = {
		visitIdentifierList(ctx.identifierList())
	}

	override def visitIdentifierList(ctx: IdentifierListContext): Seq[String] = {
		ctx.identifier().map(_.getText)
	}

	override def visitMountDatabase(ctx: MountDatabaseContext): MbCommand = {
		val name = ctx.name.getText
		val properties = visitPropertyList(ctx.propertyList())
		val ignoreIfExists = ctx.EXISTS() != null
		MountDatabase(name, properties, ignoreIfExists)
	}

	override def visitSetDatabaseProperties(ctx: SetDatabasePropertiesContext): MbCommand = {
		val name = ctx.name.getText
		val properties = visitPropertyList(ctx.propertyList())
		AlterDatabaseSetOptions(name, properties)
	}

	override def visitUnmountDatabase(ctx: UnmountDatabaseContext): MbCommand = {
		val name = ctx.name.getText
		val ignoreIfNotExists = ctx.EXISTS() != null
		UnmountDatabase(name, ignoreIfNotExists)
	}

	override def visitPropertyList(ctx: PropertyListContext): Map[String, String] = {
		ctx.property().map { prop =>
			val key = visitPropertyKey(prop.key)
			val value = Option(prop.value).map(_.getText).map(ParserUtils.tripQuotes).orNull
			key -> value
		}.toMap[String, String]
	}

	override def visitPropertyKey(ctx: PropertyKeyContext): String = {
		if (ctx.STRING() != null) ParserUtils.string(ctx.STRING().getText)
		else ctx.getText
	}

	override def visitCreateDatabase(ctx: CreateDatabaseContext): MbCommand = {
		val name = ctx.name.getText
		val desc = Option(ctx.comment).map(_.getText).map(ParserUtils.tripQuotes)
		val ignoreIfExists = ctx.EXISTS() != null
		CreateDatabase(name, desc, ignoreIfExists)
	}

	override def visitRenameDatabase(ctx: RenameDatabaseContext): MbCommand = {
		val name = ctx.name.getText
		val newName = ctx.newName.getText
		AlterDatabaseSetName(name, newName)
	}

	override def visitSetDatabaseName(ctx: SetDatabaseNameContext): MbCommand = {
		val name = ctx.name.getText
		val newName = ctx.newName.getText
		AlterDatabaseSetName(name, newName)
	}

	override def visitSetDatabaseComment(ctx: SetDatabaseCommentContext): MbCommand = {
		val name = ctx.name.getText
		val desc = ParserUtils.tripQuotes(ctx.comment.getText)
		AlterDatabaseSetComment(name, desc)
	}

	override def visitDropDatabase(ctx: DropDatabaseContext): MbCommand = {
		val name = ctx.name.getText
		val ignoreIfNotExists = ctx.EXISTS() != null
		val cascade = ctx.CASCADE() != null
		DropDatabase(name, ignoreIfNotExists, cascade)
	}

	override def visitUseDatabase(ctx: UseDatabaseContext): MbCommand = {
		UseDatabase(ctx.db.getText)
	}

	override def visitMountTable(ctx: MountTableContext): MbCommand = {
		val tableIdentifier = visitTableIdentifier(ctx.tableIdentifier())
		val properties = visitPropertyList(ctx.propertyList())
		val isStream = ctx.STREAM() != null
		val ignoreIfExists = ctx.EXISTS() != null

		val schema = if (ctx.colTypeList() != null) {
			Some(createSchema(ctx.colTypeList()))
		} else {
			None
		}
		MountTable(tableIdentifier, schema, properties, isStream, ignoreIfExists)
	}

	protected def createSchema(ctx: ColTypeListContext): StructType = {
		StructType(visitColTypeList(ctx))
	}

	override def visitColTypeList(ctx: ColTypeListContext): Seq[StructField] = {
		ctx.colType().map(visitColType)
	}

	override def visitColType(ctx: ColTypeContext): StructField = {
		val dataType: DataType = typedVisit[DataType](ctx.dataType)
		StructField(
			ctx.identifier().getText,
			dataType,
			nullable = true
		)
	}

	override def visitComplexColType(ctx: ComplexColTypeContext): StructField = {
		StructField(ctx.identifier().getText, typedVisit(ctx.dataType()), nullable = true)
	}

	override def visitComplexColTypeList(ctx: ComplexColTypeListContext): Seq[StructField] = {
		ctx.complexColType().map(visitComplexColType)
	}

	override def visitComplexDataType(ctx: ComplexDataTypeContext): DataType = {
		ctx.complex.getType match {
			case MqlBaseParser.ARRAY =>
				ArrayType(typedVisit(ctx.dataType(0)))
			case MqlBaseParser.MAP =>
				MapType(typedVisit(ctx.dataType(0)), typedVisit(ctx.dataType(1)))
			case MqlBaseParser.STRUCT =>
				StructType(Option(ctx.complexColTypeList).toSeq.flatMap(visitComplexColTypeList))
		}
	}

	override def visitPrimitiveDataType(ctx: PrimitiveDataTypeContext): DataType = {
		val dataType = ctx.identifier.getText.toLowerCase(Locale.ROOT)
		val integer_value: util.List[TerminalNode] = ctx.INTEGER_VALUE()
		(dataType, ctx.INTEGER_VALUE().toList) match {
			case ("boolean", Nil) => BooleanType
			case ("tinyint" | "byte", Nil) => ByteType
			case ("smallint" | "short", Nil) => ShortType
			case ("int" | "integer", Nil) => IntegerType
			case ("bigint" | "long", Nil) => LongType
			case ("float", Nil) => FloatType
			case ("double", Nil) => DoubleType
			case ("date", Nil) => DateType
			case ("timestamp", Nil) => TimestampType
			case ("string", Nil) => StringType
			case ("char", length :: Nil) => CharType(length.getText.toInt)
			case ("varchar", length :: Nil) => VarcharType(length.getText.toInt)
			case ("binary", Nil) => BinaryType
			case ("decimal", Nil) => DecimalType.USER_DEFAULT
			case ("decimal", precision :: Nil) => DecimalType(precision.getText.toInt, 0)
			case ("decimal", precision :: scale :: Nil) =>
				DecimalType(precision.getText.toInt, scale.getText.toInt)
			case (dt, params) =>
				val dtStr = if (params.nonEmpty) s"$dt(${params.mkString(",")})" else dt
				throw new Exception(s"DataType $dtStr is not supported.")
		}
	}

	override def visitRenameTable(ctx: RenameTableContext): MbCommand = {
		val table = visitTableIdentifier(ctx.name)
		val newTable = visitTableIdentifier(ctx.newName)
		AlterTableSetName(table, newTable)
	}

	override def visitSetTableName(ctx: SetTableNameContext): MbCommand = {
		val tableIdentifier = visitTableIdentifier(ctx.name)
		val newTableIdentifier = visitTableIdentifier(ctx.newName)
		AlterTableSetName(tableIdentifier, newTableIdentifier)
	}

	override def visitSetTableProperties(ctx: SetTablePropertiesContext): MbCommand = {
		val tableIdentifier = visitTableIdentifier(ctx.name)
		val properties = visitPropertyList(ctx.propertyList())
		AlterTableSetOptions(tableIdentifier, properties)
	}

	override def visitUnmountTable(ctx: UnmountTableContext): MbCommand = {
		val tableIdentifier = visitTableIdentifier(ctx.name)
		val ignoreIfNotExists = ctx.EXISTS() != null
		UnmountTable(tableIdentifier, ignoreIfNotExists)
	}

	override def visitTableIdentifier(ctx: TableIdentifierContext): MbTableIdentifier = {
		val database = Option(ctx.db).map(_.getText).map(ParserUtils.tripQuotes)
		val table = ParserUtils.tripQuotes(ctx.table.getText)
		MbTableIdentifier(table, database)
	}

	override def visitCreateView(ctx: CreateViewContext): MbCommand = {
		val tableIdentifier = visitTableIdentifier(ctx.name)
		val query = visitQuery(ctx.query()).query
		val desc = Option(ctx.comment).map(_.getText).map(ParserUtils.tripQuotes)
		val ignoreIfExists = ctx.EXISTS() != null
		CreateView(tableIdentifier, query, desc, ignoreIfExists)
	}

	override def visitRenameView(ctx: RenameViewContext): MbCommand = {
		val tableIdentifier = visitTableIdentifier(ctx.name)
		val newTableIdentifier = visitTableIdentifier(ctx.newName)
		AlterViewSetName(tableIdentifier, newTableIdentifier)
	}

	override def visitSetViewName(ctx: SetViewNameContext): MbCommand = {
		val tableIdentifier = visitTableIdentifier(ctx.name)
		val newTableIdentifier = visitTableIdentifier(ctx.newName)
		AlterViewSetName(tableIdentifier, newTableIdentifier)
	}

	override def visitSetViewComment(ctx: SetViewCommentContext): MbCommand = {
		val tableIdentifier = visitTableIdentifier(ctx.name)
		val desc = ParserUtils.tripQuotes(ctx.comment.getText)
		AlterViewSetComment(tableIdentifier, desc)
	}

	override def visitSetViewQuery(ctx: SetViewQueryContext): MbCommand = {
		val tableIdentifier = visitTableIdentifier(ctx.name)
		val query = visitQuery(ctx.query()).query
		AlterViewSetQuery(tableIdentifier, query)
	}

	override def visitDropView(ctx: DropViewContext): MbCommand = {
		val tableIdentifier = visitTableIdentifier(ctx.name)
		val ignoreIfNotExists = ctx.EXISTS() != null
		DropView(tableIdentifier, ignoreIfNotExists)
	}

	override def visitQuery(ctx: QueryContext): MQLQuery = {
		val mql = ctx.start.getInputStream.toString.substring(ctx.start.getStartIndex, ctx.stop.getStopIndex + 1)
		MQLQuery(mql)
	}

	override def visitCreateFunction(ctx: CreateFunctionContext): MbCommand = {
		val functionIdentifier = visitFuncIdentifier(ctx.name)
		val isTemp = ctx.TEMP() != null || ctx.TEMPORARY() != null
		val className = ParserUtils.tripQuotes(ctx.className.getText)
		val methodName = Option(ctx.methodName).map(_.getText).map(ParserUtils.tripQuotes)
		val resources = ctx.resource().map { rctx =>
			val resourceType = rctx.identifier().getText.toLowerCase
			val resource = if (resourceType.equals("java") || resourceType.equals("scala")) {
				ParserUtils.tripQuotes(rctx.STRING().getText).trim.stripPrefix("(").stripSuffix(")")
			} else {
				ParserUtils.tripQuotes(rctx.STRING().getText).trim
			}
			FunctionResource(resourceType, resource)
		}
		val ignoreIfExists = ctx.EXISTS() != null
		if (isTemp) {
			CreateTempFunction(functionIdentifier, className, methodName, resources, ignoreIfExists)
		} else {
			CreateFunction(functionIdentifier, className, methodName, resources, ignoreIfExists)
		}
	}

	override def visitDropFunction(ctx: DropFunctionContext): MbCommand = {
		val functionIdentifier = visitFuncIdentifier(ctx.name)
		val ignoreIfNotExists = ctx.EXISTS() != null
		val isTemp = ctx.TEMP() != null || ctx.TEMPORARY() != null
		if (isTemp) {
			DropTempFunction(functionIdentifier, ignoreIfNotExists)
		} else {
			DropFunction(functionIdentifier, ignoreIfNotExists)
		}
	}

	override def visitFuncIdentifier(ctx: FuncIdentifierContext): MbFunctionIdentifier = {
		val function = ctx.func.getText
		val database = Option(ctx.db).map(_.getText)
		MbFunctionIdentifier(function, database)
	}

	override def visitCreateApplication(ctx: CreateApplicationContext): MbCommand = {
		val name = ctx.name.getText
		val mqlList = visitAppCmds(ctx.appCmds())
		val ignoreIfExists = ctx.EXISTS() != null
		CreateApplication(name, mqlList, ignoreIfExists)
	}

	override def visitRenameApplication(ctx: RenameApplicationContext): MbCommand = {
		val name = ctx.name.getText
		val newName = ctx.newName.getText
		AlterApplicationSetName(name, newName)
	}

	override def visitSetApplicationName(ctx: SetApplicationNameContext): MbCommand = {
		val name = ctx.name.getText
		val newName = ctx.newName.getText
		AlterApplicationSetName(name, newName)
	}

	override def visitSetApplicationQuerys(ctx: SetApplicationQuerysContext): MbCommand = {
		val name = ctx.name.getText
		val querys = visitAppCmds(ctx.appCmds())
		AlterApplicationSetQuery(name, querys)
	}

	override def visitDropApplication(ctx: DropApplicationContext): MbCommand = {
		val name = ctx.name.getText
		val ignoreIfNotExists = ctx.EXISTS() != null
		DropApplication(name, ignoreIfNotExists)
	}

	override def visitAppCmds(ctx: AppCmdsContext): Seq[String] = {
		ctx.mql().map { mqlCtx =>
			mqlCtx.start.getInputStream.toString.substring(mqlCtx.start.getStartIndex, mqlCtx.stop.getStopIndex + 1)
		}
	}

	override def visitCreateEvent(ctx: CreateEventContext): MbCommand = {
		val name = ctx.name.getText
		val definer = if (ctx.DEFINER() != null) visitDefiner(ctx.definer()) else None
		val scheduler = ParserUtils.tripQuotes(ctx.cronExpression.getText)
		val desc = Option(ctx.comment).map(_.getText).map(ParserUtils.tripQuotes)
		val application = ctx.app.getText
		val enable = if (ctx.ENABLE() != null) true else false
		val ignoreIfExists = ctx.EXISTS() != null
		CreateTimedEvent(name, definer, scheduler, desc, application, enable, ignoreIfExists)
	}

	override def visitRenameEvent(ctx: RenameEventContext): MbCommand = {
		val name = ctx.name.getText
		val newName = ctx.newName.getText
		AlterTimedEventSetName(name, newName)
	}

	override def visitSetDefiner(ctx: SetDefinerContext): MbCommand = {
		val name = ctx.name.getText
		val definer = visitDefiner(ctx.definer())
		AlterTimedEventSetDefiner(name, definer)
	}

	override def visitSetEventName(ctx: SetEventNameContext): MbCommand = {
		val name = ctx.name.getText
		val newName = ctx.newName.getText
		AlterTimedEventSetName(name, newName)
	}

	override def visitSetEventSchedule(ctx: SetEventScheduleContext): MbCommand = {
		val name = ctx.name.getText
		val scheduler = ParserUtils.tripQuotes(ctx.cronExpression.getText)
		AlterTimedEventSetSchedule(name, scheduler)
	}

	override def visitSetEventEnable(ctx: SetEventEnableContext): MbCommand = {
		val name = ctx.name.getText
		val enable = ctx.ENABLE() != null
		AlterTimedEventSetEnable(name, enable)
	}

	override def visitDropEvent(ctx: DropEventContext): MbCommand = {
		val name = ctx.name.getText
		val ignoreIfNotExists = ctx.EXISTS() != null
		DropTimedEvent(name, ignoreIfNotExists)
	}

	override def visitDefiner(ctx: DefinerContext): Option[String] = {
		if (ctx.CURRENT_USER() != null) None else Some(ctx.user.getText)
	}

	override def visitCreateTemporaryView(ctx: CreateTemporaryViewContext): MbCommand = {
		val name = ctx.name.getText
		val query = visitQuery(ctx.query())
		val needCache = ctx.CACHE() != null
		val replace = ctx.REPLACE() != null
		CreateTempView(name, query.query, needCache, replace)
	}

	override def visitGrantGrantToUser(ctx: GrantGrantToUserContext): MbCommand = {
		val users = visitIdentifierList(ctx.users).distinct
		val grants = visitGrantPrivilegeList(ctx.grantPrivilegeList())
		GrantGrantToUser(grants, users)
	}

	override def visitGrantGrantToGroup(ctx: GrantGrantToGroupContext): MbCommand = {
		val groups = visitIdentifierList(ctx.groups).distinct
		val grants = visitGrantPrivilegeList(ctx.grantPrivilegeList())
		GrantGrantToGroup(grants, groups)
	}

	override def visitRevokeGrantFromUser(ctx: RevokeGrantFromUserContext): MbCommand = {
		val users = visitIdentifierList(ctx.users).distinct
		val grants = visitGrantPrivilegeList(ctx.grantPrivilegeList())
		RevokeGrantFromUser(grants, users)
	}

	override def visitRevokeGrantFromGroup(ctx: RevokeGrantFromGroupContext): MbCommand = {
		val groups = visitIdentifierList(ctx.groups).distinct
		val grants = visitGrantPrivilegeList(ctx.grantPrivilegeList())
		RevokeGrantFromGroup(grants, groups)
	}

	override def visitGrantPrivilegeList(ctx: GrantPrivilegeListContext): Seq[PrivilegeType] = {
		ctx.grantPrivilege().map(visitGrantPrivilege)
	}

	override def visitGrantPrivilege(ctx: GrantPrivilegeContext): PrivilegeType = {
		if (ctx.ACCOUNT() != null) PrivilegeType.ACCOUNT
		else if (ctx.DDL() != null) PrivilegeType.DDL
		else PrivilegeType.DCL
	}

	override def visitGrantPrivilegeToUsers(ctx: GrantPrivilegeToUsersContext): MbCommand = {
		val users = visitIdentifierList(ctx.users).distinct
		val privileges = visitGrantPrivilegeList(ctx.grantPrivilegeList())
		GrantPrivilegeToUser(privileges, users)
	}

	override def visitGrantPrivilegeToGroups(ctx: GrantPrivilegeToGroupsContext): MbCommand = {
		val groups = visitIdentifierList(ctx.groups).distinct
		val privileges = visitGrantPrivilegeList(ctx.grantPrivilegeList())
		GrantPrivilegeToGroup(privileges, groups)
	}

	override def visitRevokePrivilegeFromUsers(ctx: RevokePrivilegeFromUsersContext): MbCommand = {
		val users = visitIdentifierList(ctx.users).distinct
		val privileges = visitGrantPrivilegeList(ctx.grantPrivilegeList())
		RevokePrivilegeFromUser(privileges, users)
	}

	override def visitRevokePrivilegeFromGroups(ctx: RevokePrivilegeFromGroupsContext): MbCommand = {
		val groups = visitIdentifierList(ctx.groups).distinct
		val privileges = visitGrantPrivilegeList(ctx.grantPrivilegeList())
		RevokePrivilegeFromGroup(privileges, groups)
	}

	override def visitGrantResourcePrivilegeToUsers(ctx: GrantResourcePrivilegeToUsersContext): MbCommand = {
		val users = visitIdentifierList(ctx.users).distinct
		val resourcePrivileges = visitPrivileges(ctx.privileges())
		val tableIdentifier = visitTableCollections(ctx.tableCollections())
		GrantResourceToUser(resourcePrivileges, tableIdentifier, users)
	}

	override def visitGrantResourcePrivilegeToGroups(ctx: GrantResourcePrivilegeToGroupsContext): MbCommand = {
		val groups = visitIdentifierList(ctx.groups).distinct
		val resourcePrivileges = visitPrivileges(ctx.privileges())
		val tableIdentifier = visitTableCollections(ctx.tableCollections())
		GrantResourceToGroup(resourcePrivileges, tableIdentifier, groups)
	}

	override def visitRevokeResourcePrivilegeFromUsers(ctx: RevokeResourcePrivilegeFromUsersContext): MbCommand = {
		val users = visitIdentifierList(ctx.users).distinct
		val resourcePrivileges = visitPrivileges(ctx.privileges())
		val tableIdentifier = visitTableCollections(ctx.tableCollections())
		RevokeResourceFromUser(resourcePrivileges, tableIdentifier, users)
	}

	override def visitRevokeResourcePrivilegeFromGroups(ctx: RevokeResourcePrivilegeFromGroupsContext): MbCommand = {
		val groups = visitIdentifierList(ctx.groups).distinct
		val resourcePrivileges = visitPrivileges(ctx.privileges())
		val tableIdentifier = visitTableCollections(ctx.tableCollections())
		RevokeResourceFromGroup(resourcePrivileges, tableIdentifier, groups)
	}

	override def visitPrivileges(ctx: PrivilegesContext): Seq[ResourcePrivilege] = {
		ctx.privilege().map(visitPrivilege)
	}

	override def visitPrivilege(ctx: PrivilegeContext): ResourcePrivilege = {
		if (ctx.SELECT() != null) {
			val columns = Option(ctx.columnIdentifiers()).map(visitColumnIdentifiers).getOrElse(Seq())
			SelectPrivilege(columns)
		} else if (ctx.UPDATE() != null) {
			val columns = Option(ctx.columnIdentifiers()).map(visitColumnIdentifiers).getOrElse(Seq())
			UpdatePrivilege(columns)
		} else if (ctx.INSERT() != null) {
			InsertPrivilege
		} else if (ctx.DELETE() != null) {
			DeletePrivilege
		} else if (ctx.TRUNCATE() != null) {
			TruncatePrivilege
		} else {
			AllPrivilege
		}
	}

	override def visitColumnIdentifiers(ctx: ColumnIdentifiersContext): Seq[String] = {
		ctx.identifier().map(_.getText)
	}

	override def visitTableCollections(ctx: TableCollectionsContext): MbTableIdentifier = {
		val database = Option(ctx.db).map(_.getText)
		val table = ctx.table.getText
		MbTableIdentifier(table, database)
	}

	override def visitInsertInto(ctx: InsertIntoContext): MbCommand = {
		val tableIdentifier = visitTableIdentifier(ctx.tableIdentifier())
		val query = visitQuery(ctx.query()).query
		val overwrite = ctx.OVERWRITE() != null
		InsertInto(tableIdentifier, query, overwrite)
	}

	override def visitShowSysInfo(ctx: ShowSysInfoContext): MbCommand = {
		ShowSysInfo
	}

	override def visitShowJobs(ctx: ShowJobsContext): MbCommand = {
		ShowJobInfo
	}

	override def visitShowRunningEvents(ctx: ShowRunningEventsContext) : MbCommand = {
		ShowRunningEventInfo
	}

	override def visitShowEvents(ctx: ShowEventsContext): MbCommand = {
		val pattern = Option(ctx.pattern).map(_.getText).map(ParserUtils.tripQuotes)
		ShowEvents(pattern)
	}

	override def visitShowDatabase(ctx: ShowDatabaseContext): MbCommand = {
		val pattern = Option(ctx.pattern).map(_.getText).map(ParserUtils.tripQuotes)
		ShowDatabases(pattern)
	}

	override def visitShowTables(ctx: ShowTablesContext): MbCommand = {
		val database = Option(ctx.db).map(_.getText)
		val pattern = Option(ctx.pattern).map(_.getText).map(ParserUtils.tripQuotes)
		ShowTables(database, pattern)
	}

	override def visitShowViews(ctx: ShowViewsContext): MbCommand = {
		val database = Option(ctx.db).map(_.getText)
		val pattern = Option(ctx.pattern).map(_.getText).map(ParserUtils.tripQuotes)
		ShowViews(database, pattern)
	}

	override def visitShowFunctions(ctx: ShowFunctionsContext): MbCommand = {
		val database = Option(ctx.db).map(_.getText)
		val pattern = Option(ctx.pattern).map(_.getText).map(ParserUtils.tripQuotes)
		ShowFunctions(database, pattern)
	}

	override def visitShowUsers(ctx: ShowUsersContext): MbCommand = {
		val pattern = Option(ctx.pattern).map(_.getText).map(ParserUtils.tripQuotes)
		ShowUsers(pattern)
	}

	override def visitShowGroups(ctx: ShowGroupsContext): MbCommand = {
		val pattern = Option(ctx.pattern).map(_.getText).map(ParserUtils.tripQuotes)
		ShowGroups(pattern)
	}

	override def visitShowApplications(ctx: ShowApplicationsContext): MbCommand = {
		val pattern = Option(ctx.pattern).map(_.getText).map(ParserUtils.tripQuotes)
		ShowApplications(pattern)
	}

	override def visitShowVariable(ctx: ShowVariableContext): MbCommand = {
		val pattern = Option(ctx.pattern).map(_.getText).map(ParserUtils.tripQuotes)
		ShowVariables(pattern)
	}

	override def visitDescDatabase(ctx: DescDatabaseContext): MbCommand = {
		val database = ctx.name.getText
		DescDatabase(database)
	}

	override def visitDescTable(ctx: DescTableContext): MbCommand = {
		val table = visitTableIdentifier(ctx.tableIdentifier())
		val extended = ctx.EXTENDED() != null
		DescTable(table, extended)
	}

	override def visitDescView(ctx: DescViewContext): MbCommand = {
		val view = visitTableIdentifier(ctx.tableIdentifier())
		DescView(view)
	}

	override def visitDescFunction(ctx: DescFunctionContext): MbCommand = {
		val function = visitFuncIdentifier(ctx.funcIdentifier())
		val extended = ctx.EXTENDED() != null
		DescFunction(function, extended)
	}

	override def visitDescUser(ctx: DescUserContext): MbCommand = {
		val user = ctx.name.getText
		DescUser(user)
	}

	override def visitDescGroup(ctx: DescGroupContext): MbCommand = {
		val group = ctx.name.getText
		DescGroup(group)
	}

	override def visitDescEvent(ctx: DescEventContext): MbCommand = {
		val event = ctx.name.getText
		DescEvent(event)
	}

	override def visitSetVariable(ctx: SetVariableContext): MbCommand = {
		val key = ctx.key.getText
		// val value = Option(ctx.value.getText).orNull
		val stream = ctx.key.stop.getInputStream

		val start = Option(ctx.EQ()).map(_.getSymbol.getStopIndex + 1).getOrElse {
			ctx.key.stop.getStopIndex + 1
		}
		val interval = Interval.of(start, stream.size())
		val value = stream.getText(interval).trim

		SetVariable(key, value, ctx.GLOBAL() != null)
	}

	override def visitExplain(ctx: ExplainContext): MbCommand = {
		val query = visitQuery(ctx.query())
		if (ctx.EXTENDED() != null) {
			Explain(query.query, extended = true)
		} else {
			Explain(query.query)
		}
	}
}
