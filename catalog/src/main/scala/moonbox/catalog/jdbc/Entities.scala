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

import moonbox.common.util.Utils

trait CatalogEntity

case class DatabaseEntity(
	id: Option[Long] = None,
	name: String,
	description: Option[String] = None,
	organizationId: Long,
	properties: Map[String, String],
	isLogical: Boolean,
	createBy: Long,
	createTime: Long = Utils.now,
	updateBy: Long,
	updateTime: Long = Utils.now) extends CatalogEntity

case class TableEntity(
	id: Option[Long] = None,
	name: String,
	tableType: String,
	description: Option[String] = None,
	databaseId: Long,
	properties: Map[String, String],
	viewText: Option[String],
	isStream: Boolean = false,
	tableSize: Option[Long] = None,
	createBy: Long,
	createTime: Long = Utils.now,
	updateBy: Long,
	updateTime: Long = Utils.now) extends CatalogEntity

/*case class ViewEntity(
	id: Option[Long] = None,
	name: String,
	databaseId: Long,
	description: Option[String],
	cmd: String,
	createBy: Long,
	createTime: Long = Utils.now,
	updateBy: Long,
	updateTime: Long = Utils.now) extends CatalogEntity*/

case class OrganizationEntity(
	id: Option[Long] = None,
	name: String,
	config: Map[String, String],
	description: Option[String] = None,
	createBy: Long,
	createTime: Long = Utils.now,
	updateBy: Long,
	updateTime: Long = Utils.now) extends CatalogEntity

case class UserEntity(
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
	updateTime: Long = Utils.now) extends CatalogEntity

case class FunctionEntity(
	id: Option[Long] = None,
	name: String,
	databaseId: Long,
	description: Option[String],
	className: String,
	methodName: Option[String],
	createBy: Long,
	createTime: Long = Utils.now,
	updateBy: Long,
	updateTime: Long = Utils.now) extends CatalogEntity

case class FunctionResourceEntity(
	id: Option[Long] = None,
	funcId: Long,
	resourceType: String,
	resource: String,
	createBy: Long,
	createTime: Long = Utils.now,
	updateBy: Long,
	updateTime: Long = Utils.now) extends CatalogEntity

case class ProcedureEntity(
	id: Option[Long] = None,
	name: String,
	cmds: Seq[String],
	lang: String,
	organizationId: Long,
	description: Option[String] = None,
	createBy: Long,
	createTime: Long = Utils.now,
	updateBy: Long,
	updateTime: Long = Utils.now) extends CatalogEntity

case class TimedEventEntity(
	id: Option[Long] = None,
	name: String,
	organizationId: Long,
	definer: Long,
	schedule: String,
	enable: Boolean,
	description: Option[String] = None,
	procedure: Long,
	createBy: Long,
	createTime: Long = Utils.now,
	updateBy: Long,
	updateTime: Long = Utils.now) extends CatalogEntity

case class CatalogColumn(
	id: Option[Long] = None,
	name: String,
	dataType: String,
	databaseId: Long,
	table: String,
	createBy: Long,
	createTime: Long = Utils.now,
	updateBy: Long,
	updateTime: Long = Utils.now) extends CatalogEntity

case class DatabasePrivilegeEntity(
	id: Option[Long] = None,
	userId: Long,
	databaseId: Long,
	privilegeType: String,
	createBy: Long,
	createTime: Long = Utils.now,
	updateBy: Long,
	updateTime: Long = Utils.now) extends CatalogEntity

case class TablePrivilegeEntity(
	id: Option[Long] = None,
	userId: Long,
	databaseId: Long,
	tableId: Long,
	privilegeType: String,
	createBy: Long,
	createTime: Long = Utils.now,
	updateBy: Long,
	updateTime: Long = Utils.now) extends CatalogEntity

case class ColumnPrivilegeEntity(
	id: Option[Long] = None,
	userId: Long,
	databaseId: Long,
	tableId: Long,
	column: String,
	privilegeType: String,
	createBy: Long,
	createTime: Long = Utils.now,
	updateBy: Long,
	updateTime: Long = Utils.now) extends CatalogEntity

case class VariableEntity(
	id: Option[Long] = None,
	name: String,
	value: String,
	userId: Long,
	createBy: Long,
	createTime: Long = Utils.now,
	updateBy: Long,
	updateTime: Long = Utils.now) extends CatalogEntity

case class ApplicationEntity(
	id: Option[Long] = None,
	name: String,
	labels: Seq[String],
	appType: String,
	config: Map[String, String],
	state: String,
	createBy: Long,
	createTime: Long = Utils.now,
	updateBy: Long,
	updateTime: Long = Utils.now) extends CatalogEntity

case class GroupEntity(
	id: Option[Long] = None,
	name: String,
	organizationId: Long,
	description: Option[String],
	createBy: Long,
	createTime: Long = Utils.now,
	updateBy: Long,
	updateTime: Long = Utils.now) extends CatalogEntity

case class GroupUserRelEntity(
	id: Option[Long] = None,
	groupId: Long,
	userId: Long,
	createBy: Long,
	createTime: Long = Utils.now,
	updateBy: Long,
	updateTime: Long = Utils.now) extends CatalogEntity
