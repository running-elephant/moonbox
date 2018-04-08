package moonbox.catalyst.adapter.elasticsearch5.jdbc

import java.sql.Statement
import java.util.Properties

import moonbox.catalyst.adapter.elasticsearch5.EsCatalystQueryExecutor
import moonbox.catalyst.adapter.elasticsearch5.client.EsRestClient
import moonbox.catalyst.core.Schema
import moonbox.catalyst.jdbc.CatalystConnection

class EsCatalystConnection(url: String, info: Properties) extends CatalystConnection{

    override def createStatement(): Statement = {
        new EsCatalystStatement(url, info)
    }


    override def close(): Unit = {
        //client.close()
    }
}
