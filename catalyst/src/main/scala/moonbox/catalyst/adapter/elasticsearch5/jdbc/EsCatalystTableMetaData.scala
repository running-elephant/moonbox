package moonbox.catalyst.adapter.elasticsearch5.jdbc

import moonbox.catalyst.adapter.elasticsearch5.client.EsRestClient
import moonbox.catalyst.adapter.jdbc.JdbcSchema
import moonbox.catalyst.core.TableMetaData
import org.apache.spark.sql.types.{StringType, StructField, StructType}

class EsCatalystTableMetaData(client: EsRestClient, name: String, tpe: String) extends TableMetaData {
	override def getTableSchema: StructType = {
		client.getSchema(name, tpe)._1
	}

    override def getTableStats: (Long, Long) = {
        client.getStats(name)
    }
}
