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

package moonbox.catalog


trait CatalogItem

case class CatalogApplication(
	name: String,
	labels: Seq[String],
	appType: String,
	state: String,
	config: Map[String, String]
) extends CatalogItem


case class CatalogDatabase(
	name: String,
	description: Option[String] = None,
	properties: Map[String, String],
	isLogical: Boolean,
	owner: Option[String] = None
) extends CatalogItem


case class CatalogTableType(name: String)
object CatalogTableType {
	val TABLE = new CatalogTableType("TABLE")
	val VIEW = new CatalogTableType("VIEW")
}

case class CatalogTable(
	name: String,
	db: Option[String],
	tableType: CatalogTableType,
	description: Option[String] = None,
	properties: Map[String, String] = Map.empty,
	viewText: Option[String] = None,
	isStream: Boolean = false,
	tableSize: Option[Long] = None,
	owner: Option[String] = None
) extends CatalogItem {

	def database: String = db.getOrElse{
		throw new Exception(s"table $name did not specify database")
	}
}

case class CatalogOrganization(
	name: String,
	config: Map[String, String],
	description: Option[String] = None) extends CatalogItem

case class CatalogUser(
	org: String,
	name: String,
	password: String,
	account: Boolean = false,
	ddl: Boolean = false,
	dcl: Boolean = false,
	grantAccount: Boolean = false,
	grantDdl: Boolean = false,
	grantDcl: Boolean = false,
	isSA: Boolean = false,
	configuration: Map[String, String] = Map(),
	createBy: Option[String] = None
) extends CatalogItem

case class CatalogFunction(
	name: String,
	db: Option[String],
	description: Option[String],
	className: String,
	methodName: Option[String],
	resources: Seq[FunctionResource],
	owner: Option[String] = None
) extends CatalogItem {

	def database: String = db.getOrElse{
		throw new Exception(s"function $name did not specify database")
	}

}

case class CatalogFunctionResource(
	func: String,
	db: Option[String],
	resourceType: String,
	resource: String) extends CatalogItem {

	def database: String = db.getOrElse{
		throw new Exception(s"function resource $func did not specify database")
	}

}

case class CatalogProcedure(
	name: String,
	sqls: Seq[String],
	lang: String,
	description: Option[String] = None,
	owner: Option[String] = None
) extends CatalogItem

case class CatalogTimedEvent(
	name: String,
	definer: String,
	schedule: String,
	enable: Boolean,
	description: Option[String] = None,
	procedure: String,
	owner: Option[String] = None
) extends CatalogItem

case class CatalogDatabasePrivilege(
	user: String,
	database: String,
	privileges: Seq[String]) extends CatalogItem

case class CatalogTablePrivilege(
	user: String,
	database: String,
	table: String,
	privileges: Seq[String]
	) extends CatalogItem

case class CatalogColumnPrivilege(
	user: String,
	database: String,
	table: String,
	privilege: Map[String, Seq[String]] // (column, Seq(privilegeType))
	) extends CatalogItem

case class CatalogVariable(
	user: String,
	name: String,
	value: String) extends CatalogItem

case class CatalogGroup(
	name: String,
	desc: Option[String]
)

case class CatalogGroupUserRel(
	group: String,
	users: Seq[String]
)
