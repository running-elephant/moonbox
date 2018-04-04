package moonbox.catalyst.adapter.elasticsearch5.jdbc

import java.sql.ResultSet
import java.util.Properties

import moonbox.catalyst.adapter.elasticsearch5.EsCatalystQueryExecutor
import moonbox.catalyst.core.parser.SqlParser
import moonbox.catalyst.jdbc.CatalystStatement
import moonbox.common.MbLogging


class EsCatalystStatement(url: String, props: Properties) extends CatalystStatement with MbLogging{

    var fieldSize: Int = 0
    override def getMaxFieldSize: Int = {
        fieldSize
    }

    override def setMaxFieldSize(max: Int): Unit = {
        fieldSize = max
    }

    override def executeQuery(sql: String): ResultSet = {
        val parser = new SqlParser()
        val executor = new EsCatalystQueryExecutor(props)
        val schema = executor.getTableSchema
        val tableName = props.getProperty("database")
        parser.registerTable(tableName, schema, "es")
        executor.adaptorFunctionRegister(parser.getRegister)
        val plan = parser.parse(sql)
        val (iter, index2SqlType, alias2ColIdx) = executor.execute4Jdbc(plan)
        fieldSize = index2SqlType.size
        new EsCatalystResultSet(iter, alias2ColIdx)
    }

}
