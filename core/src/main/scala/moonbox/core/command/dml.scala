package moonbox.core.command

import moonbox.common.util.Utils
import moonbox.core.catalog.{CatalogGroup, CatalogSession, CatalogUser}
import moonbox.core.{MbFunctionIdentifier, MbSession, MbTableIdentifier}
import org.apache.spark.sql.Row

sealed trait DML

case object ShowSysInfo extends MbCommand with DML

case class UseDatabase(db: String) extends MbRunnableCommand with DML {
	override def run(mbSession: MbSession)(implicit ctx: CatalogSession): Seq[Row] = {
		val currentDb = mbSession.catalog.getDatabase(ctx.organizationId, db)
		ctx.databaseId = currentDb.id.get
		ctx.databaseName = currentDb.name
		Seq.empty[Row]
	}
}

case class ShowDatasources(
	pattern: Option[String]) extends MbRunnableCommand with DML {

	override def run(mbSession: MbSession)(implicit ctx: CatalogSession): Seq[Row] = {
		val datasources = pattern.map { p =>
			mbSession.catalog.listDatasource(ctx.organizationId, p)
		}.getOrElse(mbSession.catalog.listDatasource(ctx.organizationId))
		datasources.map { d => Row(d.name)}
	}
}

case class ShowDatabases(
	pattern: Option[String]) extends MbRunnableCommand with DML {

	override def run(mbSession: MbSession)(implicit ctx: CatalogSession): Seq[Row] = {
		val databases = pattern.map { p =>
			mbSession.catalog.listDatabase(ctx.organizationId, p)
		}.getOrElse(mbSession.catalog.listDatabase(ctx.organizationId))
		databases.map { d => Row(d.name)}
	}
}

case class ShowTables(
	database: Option[String],
	pattern: Option[String]) extends MbRunnableCommand with DML {

	override def run(mbSession: MbSession)(implicit ctx: CatalogSession): Seq[Row] = {
		val databaseId = database.map(db => mbSession.catalog.getDatabase(ctx.organizationId, db).id.get)
		    .getOrElse(ctx.databaseId)
		val tables = pattern.map { p =>
			mbSession.catalog.listTables(databaseId, p)
		}.getOrElse(mbSession.catalog.listTables(databaseId))
		tables.map { t => Row(t.name) }
	}
}

case class ShowViews(
	database: Option[String],
	pattern: Option[String]) extends MbRunnableCommand with DML {

	override def run(mbSession: MbSession)(implicit ctx: CatalogSession): Seq[Row] = {
		val databaseId = database.map(db => mbSession.catalog.getDatabase(ctx.organizationId, db).id.get)
			.getOrElse(ctx.databaseId)
		val views = pattern.map { p =>
			mbSession.catalog.listViews(databaseId, p)
		}.getOrElse(mbSession.catalog.listViews(databaseId))
		views.map { v => Row(v.name) }
	}
}

case class ShowFunctions(
	database: Option[String],
	pattern: Option[String]) extends MbRunnableCommand with DML {

	override def run(mbSession: MbSession)(implicit ctx: CatalogSession): Seq[Row] = {
		val databaseId = database.map(db => mbSession.catalog.getDatabase(ctx.organizationId, db).id.get)
			.getOrElse(ctx.databaseId)
		val functions = pattern.map { p =>
			mbSession.catalog.listFunctions(databaseId, p)
		}.getOrElse(mbSession.catalog.listFunctions(databaseId))
		functions.map { f => Row(f.name) }
	}
}

case class ShowUsers(
	pattern: Option[String]) extends MbRunnableCommand with DML {

	override def run(mbSession: MbSession)(implicit ctx: CatalogSession): Seq[Row] = {
		val users = pattern.map { p =>
			mbSession.catalog.listUsers(ctx.organizationId, p)
		}.getOrElse(mbSession.catalog.listUsers(ctx.organizationId))
		users.map { u => Row(u.name) }
	}
}

case class ShowGroups(
	pattern: Option[String]) extends MbRunnableCommand with DML {
	override def run(mbSession: MbSession)(implicit ctx: CatalogSession): Seq[Row] = {
		val groups = pattern.map { p =>
			mbSession.catalog.listGroups(ctx.organizationId, p)
		}.getOrElse(mbSession.catalog.listGroups(ctx.organizationId))
		groups.map { g => Row(g.name) }
	}
}

case class ShowApplications(
	pattern: Option[String]) extends MbRunnableCommand with DML {

	override def run(mbSession: MbSession)(implicit ctx: CatalogSession): Seq[Row] = {
		val applications = pattern.map { p =>
			mbSession.catalog.listApplications(ctx.organizationId, p)
		}.getOrElse(mbSession.catalog.listApplications(ctx.organizationId))
		applications.map { a => Row(a.name) }
	}
}

case class DescDatasource(name: String, extended: Boolean) extends MbRunnableCommand with DML {
	override def run(mbSession: MbSession)(implicit ctx: CatalogSession): Seq[Row] = {
		val datasource = mbSession.catalog.getDatasource(ctx.organizationId, name)
		val result = Row("Datasource Name", datasource.name) ::
			Row("Description", datasource.description.getOrElse("")) :: Nil
		if (extended) {
			val properties = if (datasource.properties.isEmpty) {
				""
			} else {
				datasource.properties.toSeq.mkString("(", ", ", ")")
			}
			result :+ Row("Properties", properties)
		}
		result
	}
}

case class DescDatabase(name: String) extends MbRunnableCommand with DML {

	override def run(mbSession: MbSession)(implicit ctx: CatalogSession): Seq[Row] = {
		val database = mbSession.catalog.getDatabase(ctx.organizationId, name)
		val result = Row("Database Name", database.name) ::
			Row("Description", database.description.getOrElse("")) :: Nil
		result
	}
}

case class DescTable(table: MbTableIdentifier, extended: Boolean) extends MbRunnableCommand with DML {

	override def run(mbSession: MbSession)(implicit ctx: CatalogSession): Seq[Row] = {
		val databaseId = table.database.map(db => mbSession.catalog.getDatabase(ctx.organizationId, db).id.get)
			.getOrElse(ctx.databaseId)
		val catalogTable = mbSession.catalog.getTable(databaseId, table.table)
		val result = Row("Table Name", catalogTable.name) ::
			Row("Description", catalogTable.description.getOrElse("")) ::
			Row("IsStream", catalogTable.isStream) :: Nil
		if (extended) {
			val properties = if (catalogTable.properties.isEmpty) {
				""
			} else {
				catalogTable.properties.toSeq.mkString("(", ", ", ")")
			}
			result :+ Row("Properties", properties)
		}
		result
	}
}

case class DescView(view: MbTableIdentifier) extends MbRunnableCommand with DML {

	override def run(mbSession: MbSession)(implicit ctx: CatalogSession): Seq[Row] = {
		val databaseId = view.database.map(db => mbSession.catalog.getDatabase(ctx.organizationId, db).id.get)
			.getOrElse(ctx.databaseId)
		val catalogView = mbSession.catalog.getView(databaseId, view.table)
		val result = Row("View Name", catalogView.name) ::
			Row("Description", catalogView.description.getOrElse("")) ::
			Row("SQL", catalogView.cmd) :: Nil
		result
	}

}

case class DescFunction(function: MbFunctionIdentifier, extended: Boolean)
	extends MbRunnableCommand with DML {

	override def run(mbSession: MbSession)(implicit ctx: CatalogSession): Seq[Row] = {
		val databaseId = function.database.map(db => mbSession.catalog.getDatabase(ctx.organizationId, db).id.get)
			.getOrElse(ctx.databaseId)
		val catalogFunction = mbSession.catalog.getFunction(databaseId, function.func)
		val result = Row("Function Name", catalogFunction.name) ::
			Row("Description", catalogFunction.description.getOrElse("")) :: Nil
		if (extended) {
			// TODO
			result :+ Row("Class", catalogFunction.className)
		}
		result
	}
}

case class DescUser(user: String) extends MbRunnableCommand with DML {

	override def run(mbSession: MbSession)(implicit ctx: CatalogSession): Seq[Row] = {
		val catalogUser: CatalogUser = mbSession.catalog.getUser(ctx.organizationId, user)
		val result = Row("User Name", catalogUser.name) ::
			Row("Account", catalogUser.account) ::
			Row("DDL", catalogUser.ddl) ::
			Row("Grant Account", catalogUser.grantAccount) ::
			Row("Grant DDL", catalogUser.grantDdl) ::
			Row("Grant DML ON", catalogUser.grantDmlOn) ::
			Row("IsSA", catalogUser.isSA) :: Nil
		result
	}
}

case class DescGroup(group: String) extends MbRunnableCommand with DML {

	override def run(mbSession: MbSession)(implicit ctx: CatalogSession): Seq[Row] = {
		val catalogGroup: CatalogGroup = mbSession.catalog.getGroup(ctx.organizationId, group)
		val result = Row("Group Name", catalogGroup.name) ::
			Row("Description", catalogGroup.description.getOrElse("")) :: Nil
		result
	}
}

case class SetConfiguration(key: String, value: String) extends MbRunnableCommand with DML {
	override def run(mbSession: MbSession)(implicit ctx: CatalogSession): Seq[Row] = {
		val user = mbSession.catalog.getUser(ctx.userId)
		mbSession.catalog.alterUser(
			user.copy(
				configuration = user.configuration.+(key -> value),
				updateBy = ctx.userId,
				updateTime = Utils.now
			)
		)
		Seq.empty[Row]
	}
}

case class MQLQuery(query: String) extends MbCommand with DML

