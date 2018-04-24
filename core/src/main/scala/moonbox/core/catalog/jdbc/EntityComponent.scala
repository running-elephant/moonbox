package moonbox.core.catalog.jdbc

import moonbox.common.util.ParseUtils
import moonbox.core.catalog._

trait EntityComponent extends DatabaseComponent {
	import profile.api._

	implicit val mapColumnType = MappedColumnType.base[Map[String, String], String](
		// Map to String
		map => map.map { case (key, value) => s"$key '$value'" }.mkString(", "),
		// String to Map
		ParseUtils.parseProperties(_).toMap[String, String]
	)
	implicit val seqColumnType = MappedColumnType.base[Seq[String], String](
		// Seq to String
		seq => seq.mkString(", "),
		// String to Seq
		string => string.split(",").map(_.trim)
	)
	implicit val seqLongColumnType = MappedColumnType.base[Seq[Long], String](
		seq => seq.mkString(", "),
		string => string.split(",").map(_.trim.toLong)
	)
	implicit val functionResourcesColumnType = MappedColumnType.base[Seq[FunctionResource], String](
		// Seq[FunctionResource] to String
		seq => seq.map { case FunctionResource(resourceType, uri) => s"${resourceType.`type`} '$uri'" }.mkString(", "),
		// String to Seq[FunctionResource]
		ParseUtils.parseProperties(_).map { case (resourceType, uri) => FunctionResource(resourceType, uri) }
	)

	protected final val catalogDatasources = TableQuery[CatalogDatasourceTable]
	protected final val catalogDatabases = TableQuery[CatalogDatabaseTable]
	protected final val catalogTables = TableQuery[CatalogTableTable]
	protected final val catalogOrganizations = TableQuery[CatalogOrganizationTable]
	protected final val catalogGroups = TableQuery[CatalogGroupTable]
	protected final val catalogUsers = TableQuery[CatalogUserTable]
	protected final val catalogFunctions = TableQuery[CatalogFunctionTable]
	protected final val catalogViews = TableQuery[CatalogViewTable]
	protected final val catalogApplications = TableQuery[CatalogApplicationTable]
	protected final val catalogColumns = TableQuery[CatalogColumnTable]
	protected final val catalogUserTableRels = TableQuery[CatalogUserTableRelTable]
	protected final val catalogUserGroupRels = TableQuery[CatalogUserGroupRelTable]

	protected final val tableQuerys = Seq(catalogDatasources,
		catalogDatabases,
		catalogTables,
		catalogOrganizations,
		catalogGroups,
		catalogUsers,
		catalogFunctions,
		catalogViews,
		catalogApplications,
		catalogColumns,
		catalogUserTableRels,
		catalogUserGroupRels)

	abstract class BaseTable[T](tag: Tag, desc: String) extends Table[T](tag, desc) {

		def id = column[Long]("id", O.AutoInc, O.PrimaryKey)

		def createBy = column[Long]("create_by")

		def createTime = column[Long]("create_time")

		def updateBy = column[Long]("update_by")

		def updateTime = column[Long]("update_time")

	}

	class CatalogDatasourceTable(tag: Tag) extends BaseTable[CatalogDatasource](tag, "datasources") {
		def name = column[String]("name")
		def properties = column[Map[String, String]]("properties")
		def description = column[Option[String]]("description")
		def organizationId = column[Long]("organizationId")
		override def * = (id.?, name, properties, description, organizationId,
			createBy, createTime, updateBy, updateTime) <> (CatalogDatasource.tupled, CatalogDatasource.unapply)
	}

	class CatalogDatabaseTable(tag: Tag) extends BaseTable[CatalogDatabase](tag, "databases") {
		def name = column[String]("name")
		def description = column[Option[String]]("description")
		def organizationId = column[Long]("organizationId")
		override def * = (id.?, name, description, organizationId, createBy, createTime,
			updateBy, updateTime) <> (CatalogDatabase.tupled, CatalogDatabase.unapply)
	}

	class CatalogTableTable(tag: Tag) extends BaseTable[CatalogTable](tag, "tables") {
		def name = column[String]("name")
		def description = column[Option[String]]("description")
		def databaseId = column[Long]("databaseId")
		def properties = column[Map[String, String]]("properties")
		def isStream = column[Boolean]("isStream")
		override def * = (id.?, name, description, databaseId, properties,
			isStream, createBy, createTime, updateBy, updateTime) <> (CatalogTable.tupled, CatalogTable.unapply)
	}

	class CatalogOrganizationTable(tag: Tag) extends BaseTable[CatalogOrganization](tag, "organizations") {
		def name = column[String]("name")
		def description = column[Option[String]]("description")
		override def * = (id.?, name, description, createBy, createTime,
			updateBy, updateTime) <> (CatalogOrganization.tupled, CatalogOrganization.unapply)
	}

	class CatalogGroupTable(tag: Tag) extends BaseTable[CatalogGroup](tag, "groups") {
		def name = column[String]("name")
		def description = column[Option[String]]("description")
		def organizationId = column[Long]("organizationId")
		override def * = (id.?, name, description, organizationId, createBy,
			createTime, updateBy, updateTime) <> (CatalogGroup.tupled, CatalogGroup.unapply)
	}

	class CatalogUserTable(tag: Tag) extends BaseTable[CatalogUser](tag, "users") {
		def name = column[String]("name")
		def password = column[String]("password")
		def account = column[Boolean]("account")
		def ddl = column[Boolean]("ddl")
		def grantAccount = column[Boolean]("grantAccount")
		def grantDdl = column[Boolean]("grantDdl")
		def grantDmlOn = column[Boolean]("grantDmlOn")
		def isSA = column[Boolean]("isSA")
		def organizationId = column[Long]("organizationId")
		def configuration = column[Map[String, String]]("configuration")
		override def * = (id.?, name, password, account, ddl, grantAccount,
			grantDdl, grantDmlOn, isSA, organizationId, configuration, createBy, createTime,
			updateBy, updateTime) <> (CatalogUser.tupled, CatalogUser.unapply)
	}

	class CatalogFunctionTable(tag: Tag) extends BaseTable[CatalogFunction](tag, "functions") {
		def name = column[String]("name")
		def databaseId = column[Long]("databaseId")
		def description = column[Option[String]]("description")
		def className = column[String]("className")
		def resources = column[Seq[FunctionResource]]("resources")
		override def * = (id.?, name, databaseId, description, className,
			resources, createBy, createTime, updateBy, updateTime
			) <> (CatalogFunction.tupled, CatalogFunction.unapply)
	}

	class CatalogViewTable(tag: Tag) extends BaseTable[CatalogView](tag, "views") {
		def name = column[String]("name")
		def databaseId = column[Long]("databaseId")
		def description = column[Option[String]]("description")
		def cmds = column[String]("cmds")
		override def * = (id.?, name, databaseId, description, cmds,
			createBy, createTime, updateBy, updateTime) <> (CatalogView.tupled, CatalogView.unapply)
	}

	class CatalogApplicationTable(tag: Tag) extends BaseTable[CatalogApplication](tag, "applications") {
		def name = column[String]("name")
		def cmds = column[Seq[String]]("cmds")
		def organizationId = column[Long]("organizationId")
		def description = column[Option[String]]("description")
		override def * = (id.?, name, cmds, organizationId, description, createBy,
			createTime, updateBy, updateTime) <> (CatalogApplication.tupled, CatalogApplication.unapply)
	}

	class CatalogColumnTable(tag: Tag) extends BaseTable[CatalogColumn](tag, "columns") {
		def name = column[String]("name")
		def dataType = column[String]("dataType")
		def readOnly = column[Boolean]("readOnly")
		def tableId = column[Long]("tableId")
		override def * = (id.?, name, dataType, readOnly, tableId, createBy, createTime,
			updateBy, updateTime) <> (CatalogColumn.tupled, CatalogColumn.unapply)
	}

	class CatalogUserTableRelTable(tag: Tag) extends BaseTable[CatalogUserTableRel](tag, "user_table_rel") {
		def userId = column[Long]("userId")
		def tableId = column[Long]("tableId")
		def columnId = column[Long]("columnId")
		override def * = (id.?, userId, tableId, columnId,
			createBy, createTime, updateBy, updateTime) <> (CatalogUserTableRel.tupled, CatalogUserTableRel.unapply)
	}

	class CatalogUserGroupRelTable(tag: Tag) extends BaseTable[CatalogUserGroupRel](tag, "user_group_rel") {
		def groupId = column[Long]("groupId")
		def userId = column[Long]("userId")
		override def * = (id.?, groupId, userId, createBy, createTime,
			updateBy, updateTime) <> (CatalogUserGroupRel.tupled, CatalogUserGroupRel.unapply)
	}
}
