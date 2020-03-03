/*-
 * <<
 * Moonbox
 * ==
 * Copyright (C) 2016 - 2019 EDP
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


/**
  *
  */
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

	protected final val databases = TableQuery[DatabaseEntityTable]
	protected final val tables = TableQuery[TableEntityTable]
	protected final val organizations = TableQuery[OrganizationEntityTable]
	protected final val users = TableQuery[UserEntityTable]
	protected final val functions = TableQuery[FunctionEntityTable]
	protected final val functionResources = TableQuery[FunctionResourceEntityTable]
	protected final val procedures = TableQuery[ProcedureEntityTable]
	protected final val timedEvents = TableQuery[TimedEventEntityTable]
	protected final val databasePrivileges = TableQuery[DatabasePrivilegeEntityTable]
	protected final val tablePrivileges = TableQuery[TablePrivilegeEntityTable]
	protected final val columnPrivileges = TableQuery[ColumnPrivilegeEntityTable]
	protected final val variables = TableQuery[VariableEntityTable]
	protected final val applications = TableQuery[ApplicationEntityTable]
	protected final val groups = TableQuery[GroupEntityTable]
	protected final val groupUserRels = TableQuery[GroupUserRelEntityTable]

	protected final val tableQueries = Seq(
		databases,
		tables,
		organizations,
		users,
		functions,
		functionResources,
		procedures,
		timedEvents,
		databasePrivileges,
		tablePrivileges,
		columnPrivileges,
		variables,
		applications,
		groups,
		groupUserRels
	)

	abstract class BaseTable[T](tag: Tag, desc: String) extends Table[T](tag, desc) {

		def id = column[Long]("id", O.AutoInc, O.PrimaryKey)

		def createBy = column[Long]("create_by")

		def createTime = column[Long]("create_time")

		def updateBy = column[Long]("update_by")

		def updateTime = column[Long]("update_time")

	}

	class DatabaseEntityTable(tag: Tag) extends BaseTable[DatabaseEntity](tag, "databases") {
		def name = column[String]("name")
		def description = column[Option[String]]("description")
		def organizationId = column[Long]("organizationId")
		def properties = column[Map[String, String]]("properties")
		def isLogical = column[Boolean]("isLogical")
		override def * = (id.?, name, description, organizationId, properties, isLogical, createBy, createTime,
			updateBy, updateTime) <> (DatabaseEntity.tupled, DatabaseEntity.unapply)
	}

	class TableEntityTable(tag: Tag) extends BaseTable[TableEntity](tag, "tables") {
		def name = column[String]("name")
		def tableType = column[String]("tableType")
		def description = column[Option[String]]("description")
		def databaseId = column[Long]("databaseId")
		def properties = column[Map[String, String]]("properties")
		def viewText = column[Option[String]]("viewText")
		def isStream = column[Boolean]("isStream")
		def tableSize = column[Option[Long]]("tableSize")
		override def * = (id.?, name, tableType, description, databaseId, properties, viewText,
			isStream, tableSize, createBy, createTime, updateBy, updateTime) <> (TableEntity.tupled, TableEntity.unapply)
	}

	class OrganizationEntityTable(tag: Tag) extends BaseTable[OrganizationEntity](tag, "organizations") {
		def name = column[String]("name")
		def config = column[Map[String, String]]("config")
		def description = column[Option[String]]("description")
		override def * = (id.?, name, config, description, createBy, createTime,
			updateBy, updateTime) <> (OrganizationEntity.tupled, OrganizationEntity.unapply)
	}

	class UserEntityTable(tag: Tag) extends BaseTable[UserEntity](tag, "users") {
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
			updateBy, updateTime) <> (UserEntity.tupled, UserEntity.unapply)
	}

	class FunctionEntityTable(tag: Tag) extends BaseTable[FunctionEntity](tag, "functions") {
		def name = column[String]("name")
		def databaseId = column[Long]("databaseId")
		def description = column[Option[String]]("description")
		def className = column[String]("className")
		def methodName = column[Option[String]]("methodName")

		override def * = {
			(id.?, name, databaseId, description, className, methodName,
				createBy, createTime, updateBy, updateTime
				) <> ({ case (id, name, databaseId, description, className, methodName, createBy, createTime, updateBy, updateTime) =>
				FunctionEntity(id, name, databaseId, description, className, methodName, createBy, createTime, updateBy, updateTime)}
				, { function: FunctionEntity =>
				Some(
					(function.id, function.name, function.databaseId, function.description, function.className, function.methodName,
					function.createBy, function.createTime, function.updateBy, function.updateTime)
				)
			})
		}
	}

	class FunctionResourceEntityTable(tag: Tag) extends BaseTable[FunctionResourceEntity](tag, "function_resource") {
		def funcId = column[Long]("funcId")
		def resourceType = column[String]("resourceType")
		def resource = column[String]("resource")
		override def * : ProvenShape[FunctionResourceEntity] = {
			(id.?, funcId, resourceType, resource, createBy, createTime, updateBy, updateTime) <>
				(FunctionResourceEntity.tupled, FunctionResourceEntity.unapply)
		}
	}

	class ProcedureEntityTable(tag: Tag) extends BaseTable[ProcedureEntity](tag, "procedures") {

		implicit val seqColumnType = MappedColumnType.base[Seq[String], String](
			// Seq to String
			seq => seq.mkString("; "),
			// String to Seq
			string => string.split("; ").map(_.trim)
		)

		def name = column[String]("name")
		def cmds = column[Seq[String]]("cmds")
		def lang = column[String]("lang")
		def organizationId = column[Long]("organizationId")
		def description = column[Option[String]]("description")
		override def * = (id.?, name, cmds, lang, organizationId, description, createBy,
			createTime, updateBy, updateTime) <> (ProcedureEntity.tupled, ProcedureEntity.unapply)
	}

	class TimedEventEntityTable(tag: Tag) extends BaseTable[TimedEventEntity](tag, "events") {
		def name = column[String]("name")
		def organizationId = column[Long]("organizationId")
		def definer = column[Long]("definer")
		def schedule = column[String]("schedule")
		def enable = column[Boolean]("enable")
		def description = column[Option[String]]("description")
		def procedure = column[Long]("procedure")
		override def * = (id.?, name, organizationId, definer, schedule, enable, description, procedure, createBy,
			createTime, updateBy, updateTime) <> (TimedEventEntity.tupled, TimedEventEntity.unapply)
	}

	class DatabasePrivilegeEntityTable(tag: Tag) extends BaseTable[DatabasePrivilegeEntity](tag, "database_privileges") {
		def userId = column[Long]("userId")
		def databaseId = column[Long]("databaseId")
		def privilegeType = column[String]("privilege_type")
		override def *  = (id.?, userId, databaseId, privilegeType,
			createBy, createTime, updateBy, updateTime) <> (DatabasePrivilegeEntity.tupled, DatabasePrivilegeEntity.unapply)
	}

	class TablePrivilegeEntityTable(tag: Tag) extends BaseTable[TablePrivilegeEntity](tag, "table_privileges") {
		def userId = column[Long]("userId")
		def databaseId = column[Long]("databaseId")
		def tableId = column[Long]("tableId")
		def privilegeType = column[String]("privilege_type")
		override def * = (id.?, userId, databaseId, tableId, privilegeType,
			createBy, createTime, updateBy, updateTime) <> (TablePrivilegeEntity.tupled, TablePrivilegeEntity.unapply)
	}

	class ColumnPrivilegeEntityTable(tag: Tag) extends BaseTable[ColumnPrivilegeEntity](tag, "column_privileges") {
		def userId = column[Long]("userId")
		def databaseId = column[Long]("databaseId")
		def tableId = column[Long]("tableId")
		def columnName = column[String]("column")
		def privilegeType = column[String]("privilege_type")
		override def * = (id.?, userId, databaseId, tableId, columnName, privilegeType,
			createBy, createTime, updateBy, updateTime) <> (ColumnPrivilegeEntity.tupled, ColumnPrivilegeEntity.unapply)
	}

	class VariableEntityTable(tag: Tag) extends BaseTable[VariableEntity](tag, "variables") {
		def name = column[String]("name")
		def value = column[String]("value")
		def userId = column[Long]("userId")
		override def *  = (id.?, name, value, userId, createBy, createTime, updateBy, updateTime) <> (VariableEntity.tupled, VariableEntity.unapply)
	}

	class ApplicationEntityTable(tag: Tag) extends BaseTable[ApplicationEntity](tag, "applications") {
		def name = column[String]("name")
		def labels = column[Seq[String]]("labels")
		def appType = column[String]("appType")
		def state = column[String]("state")
		def config = column[Map[String, String]]("config")
		override def * = (id.?, name, labels, appType, config, state, createBy, createTime, updateBy, updateTime) <> (ApplicationEntity.tupled, ApplicationEntity.unapply)
	}

	class GroupEntityTable(tag: Tag) extends BaseTable[GroupEntity](tag, "groups") {
		def name = column[String]("name")
		def organizationId = column[Long]("organizationId")
		def description = column[Option[String]]("description")
		override def * = (id.?, name, organizationId, description, createBy, createTime, updateBy, updateTime) <> (GroupEntity.tupled, GroupEntity.unapply)
	}

	class GroupUserRelEntityTable(tag: Tag) extends BaseTable[GroupUserRelEntity](tag, "group_user_rels") {
		def groupId = column[Long]("groupId")
		def userId = column[Long]("userId")
		override def * = (id.?, groupId, userId, createBy, createTime, updateBy, updateTime) <> (GroupUserRelEntity.tupled, GroupUserRelEntity.unapply)
	}

}

object EntityComponent {
	val isInitialized: AtomicBoolean = new AtomicBoolean(false)
}
