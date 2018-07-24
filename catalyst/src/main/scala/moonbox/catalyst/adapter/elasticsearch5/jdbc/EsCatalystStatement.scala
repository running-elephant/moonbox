/*-
 * <<
 * Moonbox
 * ==
 * Copyright (C) 2016 - 2018 EDP
 * ==
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * >>
 */

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
