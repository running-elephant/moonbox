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

package moonbox.catalog.jdbc

import java.util.concurrent.atomic.AtomicBoolean

import moonbox.catalog._
import moonbox.common.util.ParseUtils
import slick.lifted.ProvenShape

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

	protected final val catalogDatabases = TableQuery[CatalogDatabaseTable]
	protected final val catalogTables = TableQuery[CatalogTableTable]
	protected final val catalogOrganizations = TableQuery[CatalogOrganizationTable]
	protected final val catalogGroups = TableQuery[CatalogGroupTable]
	protected final val catalogUsers = TableQuery[CatalogUserTable]
	protected final val catalogFunctions = TableQuery[CatalogFunctionTable]
	protected final val catalogFunctionResources = TableQuery[CatalogFunctionResourceTable]
	protected final val catalogViews = TableQuery[CatalogViewTable]
	protected final val catalogProcedures = TableQuery[CatalogProcedureTable]
	protected final val catalogTimedEvents = TableQuery[CatalogTimedEventTable]
	protected final val catalogDatabasePrivileges = TableQuery[CatalogDatabasePrivilegeTable]
	protected final val catalogTablePrivileges = TableQuery[CatalogTablePrivilegeTable]
	protected final val catalogColumnPrivileges = TableQuery[CatalogColumnPrivilegeTable]
	protected final val catalogUserGroupRels = TableQuery[CatalogUserGroupRelTable]
	protected final val catalogVariables = TableQuery[CatalogVariableTable]

	protected final val tableQuerys = Seq(
		catalogDatabases,
		catalogTables,
		catalogOrganizations,
		catalogGroups,
		catalogUsers,
		catalogFunctions,
		catalogFunctionResources,
		catalogViews,
		catalogProcedures,
		catalogTimedEvents,
		catalogDatabasePrivileges,
		catalogTablePrivileges,
		catalogColumnPrivileges,
		catalogUserGroupRels,
		catalogVariables
	)

	abstract class BaseTable[T](tag: Tag, desc: String) extends Table[T](tag, desc) {

		def id = column[Long]("id", O.AutoInc, O.PrimaryKey)

		def createBy = column[Long]("create_by")

		def createTime = column[Long]("create_time")

		def updateBy = column[Long]("update_by")

		def updateTime = column[Long]("update_time")

	}

	/*class CatalogDatasourceTable(tag: Tag) extends BaseTable[CatalogDatasource](tag, "datasources") {
		def name = column[String]("name")
		def properties = column[Map[String, String]]("properties")
		def description = column[Option[String]]("description")
		def organizationId = column[Long]("organizationId")
		override def * = (id.?, name, properties, description, organizationId,
			createBy, createTime, updateBy, updateTime) <> (CatalogDatasource.tupled, CatalogDatasource.unapply)
	}*/

	class CatalogDatabaseTable(tag: Tag) extends BaseTable[CatalogDatabase](tag, "databases") {
		def name = column[String]("name")
		def description = column[Option[String]]("description")
		def organizationId = column[Long]("organizationId")
		def properties = column[Map[String, String]]("properties")
		def isLogical = column[Boolean]("isLogical")
		override def * = (id.?, name, description, organizationId, properties, isLogical, createBy, createTime,
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
		def account = column[Boolean]("account_privilege")
		def ddl = column[Boolean]("ddl_privilege")
		def dcl = column[Boolean]("dcl_privilege")
		def grantAccount = column[Boolean]("grant_account_privilege")
		def grantDdl = column[Boolean]("grant_ddl_privilege")
		def grantDcl = column[Boolean]("grant_dcl_privilege")
		def isSA = column[Boolean]("isSA")
		def organizationId = column[Long]("organizationId")
		def configuration = column[Map[String, String]]("configuration")
		override def * = (id.?, name, password, account, ddl, dcl, grantAccount,
			grantDdl, grantDcl, isSA, organizationId, configuration, createBy, createTime,
			updateBy, updateTime) <> (CatalogUser.tupled, CatalogUser.unapply)
	}

	class CatalogFunctionTable(tag: Tag) extends BaseTable[CatalogFunction](tag, "functions") {
		def name = column[String]("name")
		def databaseId = column[Long]("databaseId")
		def description = column[Option[String]]("description")
		def className = column[String]("className")
		def methodName = column[Option[String]]("methodName")

		override def * = {
			(id.?, name, databaseId, description, className, methodName,
				createBy, createTime, updateBy, updateTime
				) <> ({ case (id, name, databaseId, description, className, methodName, createBy, createTime, updateBy, updateTime) =>
				CatalogFunction(id, name, databaseId, description, className, methodName, Seq(), createBy, createTime, updateBy, updateTime)}
				, { function: CatalogFunction =>
				Some(
					(function.id, function.name, function.databaseId, function.description, function.className, function.methodName,
					function.createBy, function.createTime, function.updateBy, function.updateTime)
				)
			})
		}
	}

	class CatalogFunctionResourceTable(tag: Tag) extends BaseTable[CatalogFunctionResource](tag, "function_resource") {
		def funcId = column[Long]("funcId")
		def resourceType = column[String]("resourceType")
		def resource = column[String]("resource")
		override def * : ProvenShape[CatalogFunctionResource] = {
			(id.?, funcId, resourceType, resource, createBy, createTime, updateBy, updateTime) <>
				(CatalogFunctionResource.tupled, CatalogFunctionResource.unapply)
		}
	}

	class CatalogViewTable(tag: Tag) extends BaseTable[CatalogView](tag, "views") {
		def name = column[String]("name")
		def databaseId = column[Long]("databaseId")
		def description = column[Option[String]]("description")
		def cmds = column[String]("cmds")
		override def * = (id.?, name, databaseId, description, cmds,
			createBy, createTime, updateBy, updateTime) <> (CatalogView.tupled, CatalogView.unapply)
	}

	class CatalogProcedureTable(tag: Tag) extends BaseTable[CatalogProcedure](tag, "procedures") {

		implicit val seqColumnType = MappedColumnType.base[Seq[String], String](
			// Seq to String
			seq => seq.mkString("; "),
			// String to Seq
			string => string.split("; ").map(_.trim)
		)

		def name = column[String]("name")
		def cmds = column[Seq[String]]("cmds")
		def config = column[String]("config")
		def organizationId = column[Long]("organizationId")
		def description = column[Option[String]]("description")
		override def * = (id.?, name, cmds, config, organizationId, description, createBy,
			createTime, updateBy, updateTime) <> (CatalogProcedure.tupled, CatalogProcedure.unapply)
	}

	class CatalogTimedEventTable(tag: Tag) extends BaseTable[CatalogTimedEvent](tag, "event") {
		def name = column[String]("name")
		def organizationId = column[Long]("organizationId")
		def definer = column[Long]("definer")
		def schedule = column[String]("schedule")
		def enable = column[Boolean]("enable")
		def description = column[Option[String]]("description")
		def procedure = column[Long]("procedure")
		override def * = (id.?, name, organizationId, definer, schedule, enable, description, procedure, createBy,
			createTime, updateBy, updateTime) <> (CatalogTimedEvent.tupled, CatalogTimedEvent.unapply)
	}

	class CatalogDatabasePrivilegeTable(tag: Tag) extends BaseTable[CatalogDatabasePrivilege](tag, "database_privileges") {
		def userId = column[Long]("userId")
		def databaseId = column[Long]("databaseId")
		def privilegeType = column[String]("privilege_type")
		override def *  = (id.?, userId, databaseId, privilegeType,
			createBy, createTime, updateBy, updateTime) <> (CatalogDatabasePrivilege.tupled, CatalogDatabasePrivilege.unapply)
	}

	class CatalogTablePrivilegeTable(tag: Tag) extends BaseTable[CatalogTablePrivilege](tag, "table_privileges") {
		def userId = column[Long]("userId")
		def databaseId = column[Long]("databaseId")
		def table = column[String]("table")
		def privilegeType = column[String]("privilege_type")
		override def * = (id.?, userId, databaseId, table, privilegeType,
			createBy, createTime, updateBy, updateTime) <> (CatalogTablePrivilege.tupled, CatalogTablePrivilege.unapply)
	}

	class CatalogColumnPrivilegeTable(tag: Tag) extends BaseTable[CatalogColumnPrivilege](tag, "column_privileges") {
		def userId = column[Long]("userId")
		def databaseId = column[Long]("databaseId")
		def table = column[String]("table")
		def columnName = column[String]("column")
		def privilegeType = column[String]("privilege_type")
		override def * = (id.?, userId, databaseId, table, columnName, privilegeType,
			createBy, createTime, updateBy, updateTime) <> (CatalogColumnPrivilege.tupled, CatalogColumnPrivilege.unapply)
	}

	class CatalogUserGroupRelTable(tag: Tag) extends BaseTable[CatalogUserGroupRel](tag, "user_group_rel") {
		def groupId = column[Long]("groupId")
		def userId = column[Long]("userId")
		override def * = (id.?, groupId, userId, createBy, createTime,
			updateBy, updateTime) <> (CatalogUserGroupRel.tupled, CatalogUserGroupRel.unapply)
	}

	class CatalogVariableTable(tag: Tag) extends BaseTable[CatalogVariable](tag, "variables") {
		def name = column[String]("name")
		def value = column[String]("value")
		def userId = column[Long]("userId")
		override def *  = (id.?, name, value, userId, createBy, createTime, updateBy, updateTime) <> (CatalogVariable.tupled, CatalogVariable.unapply)
	}
}

object EntityComponent {
	val isInitialized: AtomicBoolean = new AtomicBoolean(false)
}
