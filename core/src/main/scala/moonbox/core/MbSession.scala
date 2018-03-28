package moonbox.core

import java.util.Locale
import java.util.concurrent.ConcurrentHashMap

import moonbox.common.{MbConf, MbLogging}
import moonbox.core.catalog.CatalogSession
import moonbox.core.command.{InsertInto, MQLQuery, MbCommand, MbRunnableCommand}
import org.apache.spark.{SparkConf, SparkContext}
import moonbox.core.config._
import scala.collection.JavaConversions._

class MbSession(conf: MbConf) extends MbLogging {
	implicit private  var catalogSession: CatalogSession = _
	val catalog = new CatalogContext(conf)

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

	def execute(cmds: Seq[MbCommand]): Any = {
		cmds.map{execute}.last
	}

	def execute(cmd: MbCommand): Any = {
		cmd match {
			case runnable: MbRunnableCommand =>
				runnable.run(this)
			case query: MQLQuery =>

			case insert: InsertInto =>
		}
	}

}

object MbSession extends MbLogging {
	private val resources = ConcurrentHashMap.newKeySet[String]()
	private var sparkContext: SparkContext = _

	private def getSparkContext(conf: MbConf): SparkContext = {
		synchronized {
			if (sparkContext == null || sparkContext.isStopped) {
				val sparkConf = new SparkConf().setAll(conf.getAll.filter {
					case (key, value) => key.startsWith("moonbox.mixcal")
				}.map{case (key, value) => (key.stripPrefix("moonbox.mixcal."), value)})
				sparkContext = new SparkContext(sparkConf)
				val toUpperCased = conf.get(MIXCAL_SPARK_LOGLEVEL.key, MIXCAL_SPARK_LOGLEVEL.defaultValueString).toUpperCase(Locale.ROOT)
				val loglevel = org.apache.log4j.Level.toLevel(toUpperCased)
				//org.apache.log4j.Logger.getRootLogger.setLevel(loglevel)
				logInfo("New a sparkContext instance.")
				resources.foreach(sparkContext.addJar)
				resources.clear()
			} else {
				logInfo("Using an exists sparkContext.")
			}
			sparkContext
		}
	}

	def addJar(path: String): Unit = resources.add(path)
}
