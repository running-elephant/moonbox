package moonbox.grid.deploy2.audit

import java.util.Properties

import org.apache.log4j.PropertyConfigurator
import org.slf4j.{Logger, LoggerFactory}

import scala.collection.JavaConverters._


class LocalFileAuditLogDao(conf: Map[String, String]) extends AuditLogDao {

    val batchSize: Int = conf.getOrElse("moonbox.audit.batch.size", "100").toInt
    val prop = new Properties()
    prop.setProperty("log4j.logger.moonbox.grid.deploy2.auditlog", "warn, audit")
    prop.setProperty("log4j.appender.audit", "org.apache.log4j.DailyRollingFileAppender")
    prop.setProperty("log4j.appender.audit.Append", "true")
    prop.setProperty("log4j.appender.audit.DatePattern", """'_'yyyy-MM-dd""")
    prop.setProperty("log4j.appender.audit.Threshold", "INFO")
    prop.setProperty("log4j.appender.audit.layout", "org.apache.log4j.PatternLayout")
    prop.setProperty("log4j.appender.audit.layout.ConversionPattern", "%d %m%n")

    if (conf.contains("moonbox.audit.file.path")) {
        prop.setProperty("log4j.appender.audit.File", conf("moonbox.audit.file.path"))
    } else {
        if (System.getenv().asScala.get("MOONBOX_HOME").isDefined) {
            val logPath = System.getenv().asScala("MOONBOX_HOME")
            val pathSep = System.getProperty("path.separator", "/")
            prop.setProperty("log4j.appender.audit.File", logPath + pathSep + "logs" + pathSep + "audit" + pathSep + "log")
        } else {
            prop.setProperty("log4j.appender.audit.File", "/tmp/audit/log")
        }
    }
    PropertyConfigurator.configure(prop)

    val logger:Logger = LoggerFactory.getLogger(classOf[LocalFileAuditLogDao])  ////val logger = Logger.getLogger(classOf[LocalFileAuditLogDao])

    override def getBatchSize: Int = 100

    override def postEvent(event: AuditEvent): Unit = {
        val log = s"""[${event.user}]: ${event.action}  ${event.access} ${event.moonboxIp} -> ${event.clientIp} ${event.sql} ${event.detail} ${event.uid}""".stripMargin
        logger.warn(log)
    }

    override def createTableIfNotExist(): Unit = {
        //nothing to do
    }

    override def postEvents(events: Seq[AuditEvent]): Unit = {
        events.foreach{ event => postEvent(event) }  //TODO: user log4j2 async
    }

    override def close(): Unit = {
    }
}

object LocalFileAuditLogDao{

}
