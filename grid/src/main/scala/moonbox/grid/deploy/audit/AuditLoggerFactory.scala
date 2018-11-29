package moonbox.grid.deploy.audit

import moonbox.common.MbConf

abstract class AuditLoggerFactory {
	def createAuditLogger(): AuditLogger
}

class MySQLAuditLoggerFactory(conf: MbConf) extends AuditLoggerFactory {
	override def createAuditLogger(): AuditLogger = {
		new MySQLAuditLogger(conf)
	}
}

class ElasticSearchAuditLoggerFactory(conf: MbConf) extends AuditLoggerFactory {
	override def createAuditLogger(): AuditLogger = {
		new ElasticSearchAuditLogger(conf)
	}
}

class FileAuditLoggerFactory(conf: MbConf) extends AuditLoggerFactory {
	override def createAuditLogger(): AuditLogger = {
		new FileAuditLogger(conf)
	}
}

class HdfsAuditLoggerFactory(conf: MbConf) extends AuditLoggerFactory {
	override def createAuditLogger(): AuditLogger = {
		new HdfsAuditLogger(conf)
	}
}

class BlackHoleAuditLoggerFactory extends AuditLoggerFactory {
	override def createAuditLogger(): AuditLogger = {
		new BlackHoleAuditLogger
	}
}