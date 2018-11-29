package moonbox.grid.deploy.audit

import moonbox.common.{MbConf, MbLogging}

class FileAuditLogger(conf: MbConf) extends AbstractAuditLogger(conf) with MbLogging {
	override def init(): Unit = {}

	override def persist(events: Seq[AuditEvent]): Unit = {}

	override def close(): Unit = {}
}
