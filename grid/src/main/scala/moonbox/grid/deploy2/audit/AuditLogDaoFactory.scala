package moonbox.grid.deploy2.audit

abstract class AuditLogDaoFactory {
    def createAuditLogDao(): AuditLogDao
}

class LocalFileAuditLogDaoFactory(conf: Map[String, String]) extends AuditLogDaoFactory {
    override def createAuditLogDao(): AuditLogDao = {
        new LocalFileAuditLogDao(conf)
    }
}

class MySqlAuditLogDaoFactory(conf: Map[String, String]) extends AuditLogDaoFactory {
    override def createAuditLogDao(): AuditLogDao = {
        new MySqlAuditLogDao(conf)
    }
}


class ElasticSearchAuditLogDaoFactory(conf: Map[String, String]) extends AuditLogDaoFactory {
    override def createAuditLogDao(): AuditLogDao = {
        new ElasticSearchAuditLogDao(conf)
    }
}

class BlackHoleAuditLogDaoFactory(conf: Map[String, String]) extends AuditLogDaoFactory {
    override def createAuditLogDao(): AuditLogDao = {
        new BlackHoleAuditLogDao(conf)
    }
}
