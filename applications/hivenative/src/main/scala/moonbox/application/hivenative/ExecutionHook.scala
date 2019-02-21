package moonbox.application.hivenative

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

				if (rootTasks.head.isInstanceOf[DDLTask]) {

				} else {
					val tableInfo = queryPlan.getTableAccessInfo
					val columnInfo = queryPlan.getColumnAccessInfo

					catalog.getUserOption(hookContext.getUserName) match {
						case Some(catalogUser) =>
							columnInfo.getTableToColumnAccessMap
						//catalog.getDatabase(catalogUser.organizationId, databaseName)
						case None =>
					}
				}

			case HookType.POST_EXEC_HOOK =>
				val lineageInfo = hookContext.getLinfo
			case HookType.ON_FAILURE_HOOK =>

		}
	}
}


