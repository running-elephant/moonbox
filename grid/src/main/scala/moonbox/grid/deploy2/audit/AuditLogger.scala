package moonbox.grid.deploy2.audit

import java.util.{Timer, TimerTask}

import moonbox.common.MbLogging

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class AuditLogger(conf: Map[String, String]) extends MbLogging{
    //TODO: use MbConf as input param
    val mode = conf.getOrElse("moonbox.audit.implementation", "local")
    val initialDelay = conf.getOrElse("moonbox.audit.initDelay", "1000").toInt
    val intervalDelay = conf.getOrElse("moonbox.audit.intervalDelay", "5000").toInt
    val enable = conf.getOrElse("moonbox.audit.enable", "false").toBoolean

    val factory = mode.toUpperCase match {
        case "MYSQL" =>
            logInfo("audit to mysql")
            new MySqlAuditLogDaoFactory(conf)
        case ("ELASTICSEARCH" | "ES") =>
            logInfo("audit to es")
            new ElasticSearchAuditLogDaoFactory(conf)
        case ("LOCALFILE" | "LOCAL") =>
            logInfo("audit to local")
            new LocalFileAuditLogDaoFactory(conf)
        case _ =>
            logInfo("audit to black hole")
            new BlackHoleAuditLogDaoFactory(conf)
    }

    val dao = factory.createAuditLogDao()
    val timer = if (enable) {
        val t = new Timer()
        t.scheduleAtFixedRate(new TimerTask {
            override def run(): Unit = {
                dao.postBatchEvent()
            }
        }, initialDelay, intervalDelay)
        t
    }

    private def makeEvent(info: AuditInfo): AuditEvent = {
        AuditEvent(action = info.action,
            user = info.user, access=info.connection.getAccess, clientIp = info.connection.getClient, moonboxIp = info.connection.getLocal,
            sql = info.sql, detail = info.detail)
    }

    def log(info: AuditInfo) = {  //ASYNC
        val event = makeEvent(info)
        dao.postAsynEvent(event)
        if(dao.hasPlentySize()) {
            Future{  dao.postBatchEvent() }
        }
    }


    def close = dao.close()
}

object AuditLogger{
    def apply(conf: Map[String, String]): AuditLogger = new AuditLogger(conf)
}