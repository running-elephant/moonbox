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

package moonbox.application.batch.hive

import java.util

import moonbox.catalog.AbstractCatalog.User
import moonbox.catalog.JdbcCatalog
import moonbox.common.MbConf
import org.apache.hadoop.hive.ql.exec.DDLTask
import org.apache.hadoop.hive.ql.hooks.HookContext.HookType
import org.apache.hadoop.hive.ql.hooks._

import scala.collection.JavaConversions._


class ExecutionHook extends ExecuteWithHookContext {

	private val conf = new MbConf()
	private val catalog = new JdbcCatalog(conf)

	override def run(hookContext: HookContext): Unit = {
		hookContext.getHookType match {
			case HookType.PRE_EXEC_HOOK =>
				val queryPlan = hookContext.getQueryPlan
				val rootTasks = queryPlan.getRootTasks

				if (!rootTasks.head.isInstanceOf[DDLTask]) {

					val orguser = hookContext.getUserName.split("@")
					val org = orguser(0)
					val username = orguser(1)

					implicit val user: User = {
						val orgId = catalog.organizationId(org)
						val userId = catalog.userId(orgId, username)
						User(orgId, org, userId, username)
					}

					val dbToTable = new util.HashMap[String, util.Map[String, util.List[String]]]
					val columnInfo = queryPlan.getColumnAccessInfo
					catalog.getUserOption(org, username) match {
						case Some(catalogUser) =>
							val tableToColumn = columnInfo.getTableToColumnAccessMap
							tableToColumn.foreach { case (tableWithDb, columns) =>
								val tableAndDb = tableWithDb.split("@")
								val database = tableAndDb(0)
								val table  = tableAndDb(1)
								if (dbToTable.containsKey(database)) {
									dbToTable.get(database).put(table, columns)
								} else {
									val map = new util.HashMap[String, util.List[String]]
									map.put(table, columns)
									dbToTable.put(database, map)
								}
							}
							dbToTable.foreach { case (db, tables) =>
								val dbPrivilege = catalog.getDatabasePrivilege(username, db)

								if (!dbPrivilege.privileges.contains("SELECT")) {
									tables.foreach { case (table, columns) =>
										val tablePrivilege = catalog.getTablePrivilege(username, db, table)

										if (!tablePrivilege.privileges.contains("SELECT")) {
											val columnPrivileges = catalog.getColumnPrivilege(username, db, table).privilege

											val availableColumns = columnPrivileges.filterKeys(key => columnPrivileges(key).contains("SELECT"))

											if (!columns.forall(availableColumns.contains)) {
												val unavailableColumns = columns.filter(!availableColumns.contains(_))
												throw new Exception(s"Permission denied to user $username for columns ${unavailableColumns.mkString(",")}")
											}
										}
									}
								}
							}
						case None =>
							throw new Exception(s"${hookContext.getUserName} does not exist.")
					}
				}

			case HookType.POST_EXEC_HOOK =>

			case HookType.ON_FAILURE_HOOK =>
		}
	}
}


