package moonbox.core


import moonbox.common.{MbConf, MbLogging}
import moonbox.core.catalog.CatalogSession
import moonbox.core.command._
import moonbox.core.config._
import org.apache.spark.sql.{DataFrame, MixcalContext, SaveMode}


class MbSession(conf: MbConf) extends MbLogging {
	implicit private  var catalogSession: CatalogSession = _
	val pushdown = conf.get(MIXCAL_PUSHDOWN_ENABLE.key, MIXCAL_PUSHDOWN_ENABLE.defaultValue.get)
	val catalog = new CatalogContext(conf)
	val mixcal = new MixcalContext(conf)

	def bindUser(username: String): this.type = {
		this.catalogSession = {
			catalog.getUserOption(username) match {
				case Some(catalogUser) =>
					val organization = catalog.getOrganization(catalogUser.organizationId)
					val database = catalog.getDatabase(catalogUser.organizationId, "default")
					new CatalogSession(
						catalogUser.id.get,
						catalogUser.name,
						database.id.get,
						database.name,
						organization.id.get,
						organization.name
					)
				case None =>
					throw new Exception(s"$username does not exist.")
			}
		}
		this
	}

	def execute(jobId: String, cmds: Seq[MbCommand]): Any = {
		cmds.map{cmd => execute(jobId, cmds)}.last
	}

	def execute(jobId: String, cmd: MbCommand): Any = {
		cmd match {
			case runnable: MbRunnableCommand => // direct
				runnable.run(this)
			case createTempView: CreateTempView =>
				val df = sql(createTempView.query)
				if (createTempView.isCache) {
					df.cache()
				}
				if (createTempView.replaceIfExists) {
					df.createOrReplaceTempView(createTempView.name)
				} else {
					df.createTempView(createTempView.name)
				}
			case mbQuery: MQLQuery => // cached
				sql(mbQuery.query).write
					.format("org.apache.spark.sql.execution.datasoruces.redis")
					.option("jobId", jobId)
					.options(conf.getAll.filter(_._1.startsWith("moonbox.cache.")))
					.save()
			case insert: InsertInto => // external
				// TODO insert to external system
				sql(insert.query).write.format("redis")
					.option("key", "")
					.option("server", "")
					.mode(SaveMode.Overwrite)
					.save()
			case _ => throw new Exception("Unsupported command.")
		}
	}

	def sql(sqlText: String): DataFrame = {
		val parsedLogicalPlan = mixcal.parsedLogicalPlan(sqlText)
		val analyzedLogicalPlan = mixcal.analyzedLogicalPlan(parsedLogicalPlan)
		val optimizedLogicalPlan = mixcal.optimizedLogicalPlan(analyzedLogicalPlan)
		val lastLogicalPlan = if (pushdown) {
			mixcal.furtherOptimizedLogicalPlan(optimizedLogicalPlan)
		} else optimizedLogicalPlan
		mixcal.treeToDF(lastLogicalPlan)
	}

}

object MbSession extends MbLogging {
	def getMbSession(conf: MbConf): MbSession = new MbSession(conf)
}
