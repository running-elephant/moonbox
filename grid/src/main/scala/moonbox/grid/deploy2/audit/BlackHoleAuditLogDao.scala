package moonbox.grid.deploy2.audit

class BlackHoleAuditLogDao(conf: Map[String, String]) extends AuditLogDao {

    override def createTableIfNotExist(): Unit = {}

    override def postEvent(event: AuditEvent): Unit = {}

    override def postEvents(event: Seq[AuditEvent]): Unit = {}

    override def getBatchSize: Int = 0

    override def close(): Unit = {}

    override def postAsynEvent(event: AuditEvent): Boolean = { false }

    override def hasPlentySize(): Boolean = { false }


}
