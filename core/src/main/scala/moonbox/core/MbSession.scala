package moonbox.core

import moonbox.common.{MbConf, MbLogging}
import moonbox.core.catalog.CatalogSession
import moonbox.core.command.{InsertInto, MQLQuery, MbCommand, MbRunnableCommand}

class MbSession(conf: MbConf) extends MbLogging {

	implicit private  var catalogSession: CatalogSession = _
	val catalog = new CatalogContext(conf)

	def bindSession(session: CatalogSession): this.type = {
		this.catalogSession = session
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
