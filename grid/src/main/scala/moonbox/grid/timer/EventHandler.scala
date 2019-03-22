package moonbox.grid.timer

import moonbox.grid.deploy.master.MoonboxMaster
import moonbox.grid.deploy.messages.Message.JobSubmit

class EventHandler extends Serializable {
	def apply(username: String, language: String, sqls: Seq[String], config: Map[String, String]): Unit = {
		MoonboxMaster.MASTER_REF ! JobSubmit(username, language, sqls, config)
	}
}
