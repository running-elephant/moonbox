package moonbox.grid.timer

import moonbox.grid.api.JobSubmit
import moonbox.grid.deploy.master.MbMaster

case class EventCall(definer: String, sqls: Seq[String]) extends Function0[Unit] {
	override def apply(): Unit = {
		MbMaster.singleton ! JobSubmit(definer, sqls, async = true)
	}
}
