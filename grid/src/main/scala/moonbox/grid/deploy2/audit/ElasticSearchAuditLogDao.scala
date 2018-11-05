package moonbox.grid.deploy2.audit

import java.sql.Timestamp

class ElasticSearchAuditLogDao(conf: Map[String, String]) extends AuditLogDao {

    val index: String = conf.getOrElse("moonbox.audit.index", "audit")
    val mtype: String = conf.getOrElse("moonbox.audit.type", "table")
    val version: Int = 5
    val client: ElasticSearchRestClient = {
        if (ElasticSearchAuditLogDao.elasticsearchClient == null) {
            synchronized {
                if (ElasticSearchAuditLogDao.elasticsearchClient == null) {
                    ElasticSearchAuditLogDao.elasticsearchClient = new ElasticSearchRestClient(conf)
                }
            }
        }
        ElasticSearchAuditLogDao.elasticsearchClient
    }

    createTableIfNotExist()
    override def getBatchSize = conf.getOrElse("moonbox.audit.batch.size", "100").toInt

    override def createTableIfNotExist(): Unit = {
        if(!client.indexExist(index)) {
            val schema: Map[String, String] = extractFieldNameType[AuditLogEsTable]
            client.createIndexWithSchema(index, mtype, schema)
        }
    }

    override def postEvent(event: AuditEvent): Unit = {
        val schema: Map[String, Int] = extractFieldNameIndex[AuditLogEsTable]
        val ret = client.sendBatchData(index, mtype, schema, Seq(event))
        println(ret)
    }

    override def postEvents(events: Seq[AuditEvent]): Unit = {
        val schema: Map[String, Int] = extractFieldNameIndex[AuditLogEsTable]
        val ret = client.sendBatchData(index, mtype, schema, events)
    }

    def extractFieldNameIndex[T<:Product:Manifest]: Map[String, Int] = {  //column name, column index
        implicitly[Manifest[T]].runtimeClass.getDeclaredFields.map(_.getName).zipWithIndex.toMap
    }

    def extractFieldNameType[T<:Product:Manifest]: Map[String, String] = {  //column name, column type
        implicitly[Manifest[T]].runtimeClass.getDeclaredFields.map { e =>
                (e.getName, e.getType.getSimpleName)
        }.toMap
    }

    override def close() = {
        client.close()
    }


}

//no option here
case class AuditLogEsTable(id: Long, action: String, user: String, access: String, clientIp: String, moonboxIp: String,
                           sql: String, detail: String, uid: String, time: Timestamp)

object ElasticSearchAuditLogDao{
    var elasticsearchClient: ElasticSearchRestClient = _

}