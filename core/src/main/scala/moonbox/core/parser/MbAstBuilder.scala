package moonbox.core.parser

import java.util
import java.util.Locale

import moonbox.core.{MbColumnIdentifier, MbFunctionIdentifier, MbTableIdentifier}
import moonbox.core.command._
import moonbox.core.parser.MqlBaseParser._
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

	override def visitMountDatasource(ctx: MountDatasourceContext): MbCommand = {
		val name = ctx.name.getText
		val properties = visitPropertyList(ctx.propertyList())
		val ignoreIfExists = ctx.EXISTS() != null
		MountDatasource(name, properties, ignoreIfExists)
	}

	override def visitRenameDatasource(ctx: RenameDatasourceContext): MbCommand = {
		val name = ctx.name.getText
		val newName = ctx.newName.getText
		AlterDatasourceSetName(name, newName)
	}

	override def visitSetDatasourceName(ctx: SetDatasourceNameContext): MbCommand = {
		val name = ctx.name.getText
		val newName = ctx.newName.getText
		AlterDatasourceSetName(name, newName)
	}

	override def visitSetDatasourceProperties(ctx: SetDatasourcePropertiesContext): MbCommand = {
		val name = ctx.name.getText
		val properties = visitPropertyList(ctx.propertyList())
		AlterDatasourceSetOptions(name, properties)
	}

	override def visitUnmountDatasource(ctx: UnmountDatasourceContext): MbCommand = {
		val name = ctx.name.getText
		val ignoreIfNotExists = ctx.EXISTS() != null
		UnmountDatasource(name, ignoreIfNotExists)
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

	override def visitMountTableWithDatasource(ctx: MountTableWithDatasourceContext): MbCommand = {
		val datasource = ctx.ds.getText
		val tables = visitMountTableList(ctx.mountTableList())
		val isStream = ctx.STREAM() != null
		val ignoreIfExists = ctx.EXISTS() != null
		MountTableWithDatasoruce(datasource, tables, isStream, ignoreIfExists)
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

	override def visitAddTableColumns(ctx: AddTableColumnsContext): MbCommand = {
		AlterTableAddColumns(
			visitTableIdentifier(ctx.tableIdentifier),
			createSchema(ctx.colTypeList())
		)
	}

	override def visitChangeTableColumn(ctx: ChangeTableColumnContext): MbCommand = {
		AlterTableChangeColumn(
			visitTableIdentifier(ctx.tableIdentifier),
			visitColType(ctx.colType())
		)
	}

	override def visitDropTableColumn(ctx: DropTableColumnContext): MbCommand = {
		AlterTableDropColumn(
			visitTableIdentifier(ctx.tableIdentifier),
			ctx.identifier().getText
		)
	}

	override def visitUnmountTable(ctx: UnmountTableContext): MbCommand = {
		val tableIdentifier = visitTableIdentifier(ctx.name)
		val ignoreIfNotExists = ctx.EXISTS() != null
		UnmountTable(tableIdentifier, ignoreIfNotExists)
	}

	override def visitTableIdentifier(ctx: TableIdentifierContext): MbTableIdentifier = {
		val database = Option(ctx.db).map(_.getText)
		val table = ctx.table.getText
		MbTableIdentifier(table, database)
	}

	override def visitMountTableList(ctx: MountTableListContext): Seq[(MbTableIdentifier, Option[StructType], Map[String, String])] = {
		ctx.mountTableOptions().map(visitMountTableOptions)
	}

	override def visitMountTableOptions(ctx: MountTableOptionsContext): (MbTableIdentifier, Option[StructType], Map[String, String]) = {
		val tableIdentifier = visitTableIdentifier(ctx.tableIdentifier())
		val properties = visitPropertyList(ctx.propertyList())
		val schema = if (ctx.colTypeList() != null) {
			Some(createSchema(ctx.colTypeList()))
		} else {
			None
		}
		(tableIdentifier, schema, properties)
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
		val properties = visitPropertyList(ctx.propertyList())
		val ignoreIfExists = ctx.EXISTS() != null
		CreateFunction(functionIdentifier, properties, ignoreIfExists)
	}

	override def visitRenameFunction(ctx: RenameFunctionContext): MbCommand = {
		val functionIdentifier = visitFuncIdentifier(ctx.name)
		val newFunctionIdentifier = visitFuncIdentifier(ctx.newName)
		AlterFunctionSetName(functionIdentifier, newFunctionIdentifier)
	}

	override def visitSetFunctionName(ctx: SetFunctionNameContext): MbCommand = {
		val functionIdentifier = visitFuncIdentifier(ctx.name)
		val newFunctionIdentifier = visitFuncIdentifier(ctx.newName)
		AlterFunctionSetName(functionIdentifier, newFunctionIdentifier)
	}

	override def visitSetFunctionProperties(ctx: SetFunctionPropertiesContext): MbCommand = {
		val functionIdentifier = visitFuncIdentifier(ctx.name)
		val properties = visitPropertyList(ctx.propertyList())
		AlterFunctionSetOptions(functionIdentifier, properties)
	}

	override def visitDropFunction(ctx: DropFunctionContext): MbCommand = {
		val functionIdentifier = visitFuncIdentifier(ctx.name)
		val ignoreIfNotExists = ctx.EXISTS() != null
		DropFunction(functionIdentifier, ignoreIfNotExists)
	}

	override def visitFuncIdentifier(ctx: FuncIdentifierContext): MbFunctionIdentifier = {
		val function = ctx.func.getText
		val database = Option(ctx.db).map(_.getText)
		MbFunctionIdentifier(function, database)
	}

	override def visitCreateApplication(ctx: CreateApplicationContext): MbCommand = {
		val name = ctx.name.getText
		val mqlList = visitAppCmds(ctx.appCmds()).map(_.query)
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
		val querys = visitAppCmds(ctx.appCmds()).map(_.query)
		AlterApplicationSetQuery(name, querys)
	}

	override def visitDropApplication(ctx: DropApplicationContext): MbCommand = {
		val name = ctx.name.getText
		val ignoreIfNotExists = ctx.EXISTS() != null
		DropApplication(name, ignoreIfNotExists)
	}

	override def visitAppCmds(ctx: AppCmdsContext): Seq[MQLQuery] = {
		val nonLastCmds = Option(ctx.nonLastCmdList()).map(visitNonLastCmdList)
		val lastCmds = visitLastCmd(ctx.lastCmd())
		if (nonLastCmds.nonEmpty) {
			nonLastCmds.get.:+(lastCmds)
		} else {
			Seq(lastCmds)
		}
	}

	override def visitNonLastCmdList(ctx: NonLastCmdListContext): Seq[MQLQuery] = {
		ctx.nonLastCmd().map(visitNonLastCmd)
	}

	override def visitNonLastCmd(ctx: NonLastCmdContext): MQLQuery = {
		Option(ctx.createTemporaryViewCmd()).map(visitCreateTemporaryViewCmd).getOrElse(
			visitCreateTemporaryFunctionCmd(ctx.createTemporaryFunctionCmd())
		)._1
	}

	override def visitCreateTemporaryFunction(ctx: CreateTemporaryFunctionContext): MbCommand = {
		visitCreateTemporaryFunctionCmd(ctx.createTemporaryFunctionCmd())._2
	}

	override def visitCreateTemporaryFunctionCmd(ctx: CreateTemporaryFunctionCmdContext): (MQLQuery, MbCommand) = {
		val mql = ctx.start.getInputStream.toString.substring(ctx.start.getStartIndex, ctx.stop.getStopIndex + 1)
		val name = ctx.name.getText
		val props = visitPropertyList(ctx.propertyList())
		val replacedIfExists = ctx.REPLACE() != null
		(MQLQuery(mql), CreateTempFunction(name, props, replacedIfExists))
	}

	override def visitCreateTemporaryView(ctx: CreateTemporaryViewContext): MbCommand = {
		visitCreateTemporaryViewCmd(ctx.createTemporaryViewCmd())._2
	}

	override def visitCreateTemporaryViewCmd(ctx: CreateTemporaryViewCmdContext): (MQLQuery, MbCommand) = {
		val mql = ctx.start.getInputStream.toString.substring(ctx.start.getStartIndex, ctx.stop.getStopIndex + 1)
		val name = ctx.name.getText
		val query = visitQuery(ctx.query()).query
		val isCache = ctx.CACHE() != null
		val isReplacedIfExists = ctx.REPLACE() != null
		(MQLQuery(mql), CreateTempView(name, query, isCache, isReplacedIfExists))
	}

	override def visitLastCmd(ctx: LastCmdContext): MQLQuery = {
		if (ctx.insertIntoCmd() != null) visitInsertIntoCmd(ctx.insertIntoCmd())._1
		else if (ctx.insertOverwriteCmd() != null) visitInsertOverwriteCmd(ctx.insertOverwriteCmd())._1
		else visitQuery(ctx.query())
	}

	override def visitGrantGrantToUser(ctx: GrantGrantToUserContext): MbCommand = {
		val users = visitIdentifierList(ctx.users)
		val grants = visitPrivilegeList(ctx.privilegeList())
		GrantGrantToUser(grants, users)
	}

	override def visitGrantGrantToGroup(ctx: GrantGrantToGroupContext): MbCommand = {
		val groups = visitIdentifierList(ctx.groups)
		val grants = visitPrivilegeList(ctx.privilegeList())
		GrantGrantToGroup(grants, groups)
	}

	override def visitRevokeGrantFromUser(ctx: RevokeGrantFromUserContext): MbCommand = {
		val users = visitIdentifierList(ctx.users)
		val grants = visitPrivilegeList(ctx.privilegeList())
		RevokeGrantFromUser(grants, users)
	}

	override def visitRevokeGrantFromGroup(ctx: RevokeGrantFromGroupContext): MbCommand = {
		val groups = visitIdentifierList(ctx.groups)
		val grants = visitPrivilegeList(ctx.privilegeList())
		RevokeGrantFromGroup(grants, groups)
	}

	override def visitPrivilegeList(ctx: PrivilegeListContext): Seq[PrivilegeType] = {
		ctx.privilege().map(visitPrivilege)
	}

	override def visitPrivilege(ctx: PrivilegeContext): PrivilegeType = {
		if (ctx.ACCOUNT() != null) GrantAccount
		else if (ctx.DDL() != null) GrantDdl
		else GrantDmlOn
	}

	override def visitGrantAccountToUsers(ctx: GrantAccountToUsersContext): MbCommand = {
		val users = visitIdentifierList(ctx.users)
		GrantAccountToUser(users)
	}

	override def visitGrantAccountToGroups(ctx: GrantAccountToGroupsContext): MbCommand = {
		val groups = visitIdentifierList(ctx.groups)
		GrantAccountToGroup(groups)
	}

	override def visitRevokeAccountFromUsers(ctx: RevokeAccountFromUsersContext): MbCommand = {
		val users = visitIdentifierList(ctx.users)
		RevokeAccountFromUser(users)
	}

	override def visitRevokeAccountFromGroups(ctx: RevokeAccountFromGroupsContext): MbCommand = {
		val groups = visitIdentifierList(ctx.groups)
		RevokeAccountFromGroup(groups)
	}

	override def visitGrantDdlToUsers(ctx: GrantDdlToUsersContext): MbCommand = {
		val users = visitIdentifierList(ctx.users)
		GrantDdlToUser(users)
	}

	override def visitGrantDdlToGroups(ctx: GrantDdlToGroupsContext): MbCommand = {
		val groups = visitIdentifierList(ctx.groups)
		GrantDdlToGroup(groups)
	}

	override def visitRevokeDdlFromUsers(ctx: RevokeDdlFromUsersContext): MbCommand = {
		val users = visitIdentifierList(ctx.users)
		RevokeDdlFromUser(users)
	}

	override def visitRevokeDdlFromGroups(ctx: RevokeDdlFromGroupsContext): MbCommand = {
		val groups = visitIdentifierList(ctx.groups)
		RevokeDdlFromGroup(groups)
	}

	override def visitGrantDmlOnToUsers(ctx: GrantDmlOnToUsersContext): MbCommand = {
		val users = visitIdentifierList(ctx.users)
		val columns = visitQualifiedColumnList(ctx.qualifiedColumnList())
		GrantDmlOnToUser(columns, users)
	}

	override def visitGrantDmlOnToGroups(ctx: GrantDmlOnToGroupsContext): MbCommand = {
		val groups = visitIdentifierList(ctx.groups)
		val columns = visitQualifiedColumnList(ctx.qualifiedColumnList())
		GrantDmlOnToGroup(columns, groups)
	}

	override def visitRevokeDmlOnFromUsers(ctx: RevokeDmlOnFromUsersContext): MbCommand = {
		val users = visitIdentifierList(ctx.users)
		val columns = visitQualifiedColumnList(ctx.qualifiedColumnList())
		RevokeDmlOnFromUser(columns, users)
	}

	override def visitRevokeDmlOnFromGroups(ctx: RevokeDmlOnFromGroupsContext): MbCommand = {
		val groups = visitIdentifierList(ctx.groups)
		val columns = visitQualifiedColumnList(ctx.qualifiedColumnList())
		RevokeDmlOnFromGroup(columns, groups)
	}

	override def visitQualifiedColumnList(ctx: QualifiedColumnListContext): Seq[MbColumnIdentifier] = {
		ctx.columnIdentifier().flatMap(visitColumnIdentifier)
	}

	override def visitColumnIdentifier(ctx: ColumnIdentifierContext): Seq[MbColumnIdentifier] = {
		val db = Option(ctx.db).map(_.getText)
		val table = visitIdentifierStarList(ctx.table)
		val col = visitIdentifierStarList(ctx.colunm)
		for (t <- table; c <- col) yield MbColumnIdentifier(c, t, db)
	}

	override def visitIdentifierStarList(ctx: IdentifierStarListContext): Seq[String] = {
		val identifiers = ctx.identifier()
		if (identifiers.nonEmpty) {
			identifiers.map(_.getText)
		} else {
			Seq(Option(ctx.STAR()).map(_.getText).getOrElse(ctx.getText))
		}
	}

	override def visitInsertInto(ctx: InsertIntoContext): MbCommand = {
		visitInsertIntoCmd(ctx.insertIntoCmd())._2
	}


	override def visitInsertIntoCmd(ctx: InsertIntoCmdContext): (MQLQuery, MbCommand) = {
		val mql = ctx.start.getInputStream.toString.substring(ctx.start.getStartIndex, ctx.stop.getStopIndex + 1)
		val tableIdentifier = visitTableIdentifier(ctx.tableIdentifier())
		val query = visitQuery(ctx.query()).query
		(MQLQuery(mql), InsertInto(tableIdentifier, query, overwrite = false))
	}

	override def visitInsertOverwrite(ctx: InsertOverwriteContext): MbCommand = {
		visitInsertOverwriteCmd(ctx.insertOverwriteCmd())._2
	}

	override def visitInsertOverwriteCmd(ctx: InsertOverwriteCmdContext): (MQLQuery, MbCommand) = {
		val mql = ctx.start.getInputStream.toString.substring(ctx.start.getStartIndex, ctx.stop.getStopIndex + 1)
		val tableIdentifier = visitTableIdentifier(ctx.tableIdentifier())
		val query = visitQuery(ctx.query()).query
		(MQLQuery(mql), InsertInto(tableIdentifier, query, overwrite = true))
	}

	override def visitShowSysInfo(ctx: ShowSysInfoContext): MbCommand = {
		ShowSysInfo
	}

	override def visitShowDatasources(ctx: ShowDatasourcesContext): MbCommand = {
		val pattern = Option(ctx.pattern).map(_.getText).map(ParserUtils.tripQuotes)
		ShowDatasources(pattern)
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

	override def visitDescDatasource(ctx: DescDatasourceContext): MbCommand = {
		val datasource = ctx.name.getText
		val extended = ctx.EXTENDED() != null
		DescDatasource(datasource, extended)
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

	override def visitSetConfiguration(ctx: SetConfigurationContext): MbCommand = {
		val key = visitPropertyKey(ctx.property().key)
		val value = Option(ctx.property().value).map(v => ParserUtils.tripQuotes(v.getText)).orNull
		key -> value
		SetConfiguration(key, value)
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
