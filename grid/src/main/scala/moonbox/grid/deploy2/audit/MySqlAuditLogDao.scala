package moonbox.grid.deploy2.audit

import java.sql.Timestamp
import java.util.concurrent.atomic.AtomicBoolean

import slick.jdbc.MySQLProfile
import slick.jdbc.meta.MTable
import slick.lifted._
import slick.sql.SqlProfile.ColumnOption.SqlType

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

class MySqlAuditLogDao(conf: Map[String, String]) extends Aliases with AuditLogDao {
    val profile = MySQLProfile
    import profile.api._

    val url: String = conf("moonbox.audit.url")
    val user = conf("moonbox.audit.user")
    val password = conf("moonbox.audit.password")
    val driver = conf.getOrElse("moonbox.audit.driver", "com.mysql.jdbc.Driver")
    val timeout = conf.getOrElse("moonbox.audit.timeout", "10000").toLong

    override def getBatchSize = conf.getOrElse("batch.size", "100").toInt

    class AuditLogSlickTable(tag: Tag) extends Table[AuditEvent](tag, "audit") {
        def id = column[Long]("id", O.AutoInc, O.PrimaryKey)
        def action = column[String]("action")
        def user = column[String]("user")
        def access = column[String]("access")
        def clientIp = column[String]("clientIp")
        def moonboxIp = column[String]("moonboxIp")
        def uid = column[String]("uid")
        def time = column[Timestamp]("time", SqlType("timestamp not null default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP"))
        def sql = column[Option[String]]("sql")
        def detail = column[Option[String]]("detail")
        override def * = (id.?, action, user, access, clientIp, moonboxIp, sql, detail, uid, time) <> (AuditEvent.tupled, AuditEvent.unapply)
    }

    val database: slick.jdbc.JdbcBackend.Database = {
        if (MySqlAuditLogDao.database == null) {
            synchronized {
                if (MySqlAuditLogDao.database == null) {
                    Class.forName(driver)
                    MySqlAuditLogDao.database = slick.jdbc.JdbcBackend.Database.forURL(url = url, user = user, password = password, driver = driver)
                }
            }
        }
        MySqlAuditLogDao.database
    }

    val auditLogTable = TableQuery[AuditLogSlickTable]

    createTableIfNotExist()
    override def createTableIfNotExist(): Unit = {
        if (!MySqlAuditLogDao.isInitialized.getAndSet(true)) {
            initialize()
        }
    }

    private def await[T](f: Future[T]): T = {
        Await.result(f, new FiniteDuration(timeout, MILLISECONDS))
    }

    private def initialize() = {
        val tables: Set[String] = await(database.run(MTable.getTables("%").map(_.map(_.name.name)))).toSet
        if (!tables.contains(auditLogTable.shaped.value.tableName)) {
            await(database.run(DBIO.seq(auditLogTable.schema.create)))
        }
    }

    private def auditLogExists(uid: String) = {
        auditLogTable.filter(t => t.uid === uid).exists.result
    }

    private def doCreateAuditLog(auditLog: AuditEvent, ignoreIfExists: Boolean): Unit = {
        await {
            database.run(auditLogExists(auditLog.uid)).flatMap {
                case true => ignoreIfExists match {
                    case true => Future(Unit)
                    case false => throw new Exception(auditLog.uid)
                }
                case false => database.run(auditLogTable += auditLog)
            }
        }
    }

    override def postEvent(event: AuditEvent): Unit = {
        await(database.run(auditLogTable += event))  //doCreateAuditLog(event, true)
    }

    override def postEvents(events: Seq[AuditEvent]): Unit = {
        if ( events.nonEmpty ) {
            await(database.run(auditLogTable ++= events))
        }
    }

    override def close(): Unit = {
        database.close()
    }
}

object MySqlAuditLogDao {
    var database: slick.jdbc.JdbcBackend.Database = _
    val isInitialized: AtomicBoolean = new AtomicBoolean(false)
}

