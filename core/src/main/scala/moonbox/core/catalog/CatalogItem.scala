package moonbox.core.catalog

import moonbox.common.util.Utils
import moonbox.core.command.PrivilegeType.PrivilegeType

trait CatalogItem

/*case class CatalogDatasource(
	id: Option[Long] = None,
	name: String,
	properties: Map[String, String],
	description: Option[String] = None,
	organizationId: Long,
	createBy: Long,
	createTime: Long = Utils.now,
	updateBy: Long,
	updateTime: Long = Utils.now) extends CatalogItem*/

case class CatalogDatabase(
	id: Option[Long] = None,
	name: String,
	description: Option[String] = None,
	organizationId: Long,
	properties: Map[String, String],
	isLogical: Boolean,
	createBy: Long,
	createTime: Long = Utils.now,
	updateBy: Long,
	updateTime: Long = Utils.now) extends CatalogItem

case class CatalogTable(
	id: Option[Long] = None,
	name: String,
	description: Option[String] = None,
	databaseId: Long,
	properties: Map[String, String],
	isStream: Boolean = false,
	createBy: Long,
	createTime: Long = Utils.now,
	updateBy: Long,
	updateTime: Long = Utils.now) extends CatalogItem

case class CatalogOrganization(
	id: Option[Long] = None,
	name: String,
	description: Option[String] = None,
	createBy: Long,
	createTime: Long = Utils.now,
	updateBy: Long,
	updateTime: Long = Utils.now) extends CatalogItem

case class CatalogGroup(
	id: Option[Long] = None,
	name: String,
	description: Option[String] = None,
	organizationId: Long,
	createBy: Long,
	createTime: Long = Utils.now,
	updateBy: Long,
	updateTime: Long = Utils.now) extends CatalogItem

case class CatalogUser(
	id: Option[Long] = None,
	name: String,
	password: String,
	account: Boolean = false,
	ddl: Boolean = false,
	dcl: Boolean = false,
	grantAccount: Boolean = false,
	grantDdl: Boolean = false,
	grantDcl: Boolean = false,
	isSA: Boolean = false,
	organizationId: Long,
	configuration: Map[String, String] = Map(),
	createBy: Long,
	createTime: Long = Utils.now,
	updateBy: Long,
	updateTime: Long = Utils.now) extends CatalogItem

case class CatalogFunction(
	id: Option[Long] = None,
	name: String,
	databaseId: Long,
	description: Option[String],
	className: String,
	methodName: Option[String],
	resources: Seq[FunctionResource],
	createBy: Long,
	createTime: Long = Utils.now,
	updateBy: Long,
	updateTime: Long = Utils.now) extends CatalogItem

case class CatalogFunctionResource(
	id: Option[Long] = None,
	funcId: Long,
	resourceType: String,
	resource: String,
	createBy: Long,
	createTime: Long = Utils.now,
	updateBy: Long,
	updateTime: Long = Utils.now) extends CatalogItem

case class CatalogView(
	id: Option[Long] = None,
	name: String,
	databaseId: Long,
	description: Option[String],
	cmd: String,
	createBy: Long,
	createTime: Long = Utils.now,
	updateBy: Long,
	updateTime: Long = Utils.now) extends CatalogItem

case class CatalogApplication(
	id: Option[Long] = None,
	name: String,
	cmds: Seq[String],
	organizationId: Long,
	description: Option[String] = None,
	createBy: Long,
	createTime: Long = Utils.now,
	updateBy: Long,
	updateTime: Long = Utils.now) extends CatalogItem

case class CatalogTimedEvent(
	id: Option[Long] = None,
	name: String,
	organizationId: Long,
	definer: Long,
	schedule: String,
	enable: Boolean,
	description: Option[String] = None,
	application: Long,
	createBy: Long,
	createTime: Long = Utils.now,
	updateBy: Long,
	updateTime: Long = Utils.now) extends CatalogItem

case class CatalogColumn(
	id: Option[Long] = None,
	name: String,
	dataType: String,
	databaseId: Long,
	table: String,
	createBy: Long,
	createTime: Long = Utils.now,
	updateBy: Long,
	updateTime: Long = Utils.now) extends CatalogItem

case class CatalogDatabasePrivilege(
	id: Option[Long] = None,
	userId: Long,
	databaseId: Long,
	privilegeType: String,
	createBy: Long,
	createTime: Long = Utils.now,
	updateBy: Long,
	updateTime: Long = Utils.now) extends CatalogItem

case class CatalogTablePrivilege(
	id: Option[Long] = None,
	userId: Long,
	databaseId: Long,
	table: String,
	privilegeType: String,
	createBy: Long,
	createTime: Long = Utils.now,
	updateBy: Long,
	updateTime: Long = Utils.now) extends CatalogItem

case class CatalogColumnPrivilege(
	id: Option[Long] = None,
	userId: Long,
	databaseId: Long,
	table: String,
	column: String,
	privilegeType: String,
	createBy: Long,
	createTime: Long = Utils.now,
	updateBy: Long,
	updateTime: Long = Utils.now) extends CatalogItem

case class CatalogUserGroupRel(
	id: Option[Long] = None,
	groupId: Long,
	userId: Long,
	createBy: Long,
	createTime: Long = Utils.now,
	updateBy: Long,
	updateTime: Long = Utils.now) extends CatalogItem

case class CatalogVariable(
	id: Option[Long] = None,
	name: String,
	value: String,
	userId: Long,
	createBy: Long,
	createTime: Long = Utils.now,
	updateBy: Long,
	updateTime: Long = Utils.now) extends CatalogItem
