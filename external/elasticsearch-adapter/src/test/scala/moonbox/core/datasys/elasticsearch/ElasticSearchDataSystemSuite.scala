/*-
 * <<
 * Moonbox
 * ==
 * Copyright (C) 2016 - 2019 EDP
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

package moonbox.core.datasys.elasticsearch

import moonbox.core.datasys.DataTable
import org.apache.spark.sql.catalyst.plans.logical.LogicalPlan
import org.apache.spark.sql.{SaveMode, SparkSession}
import org.scalatest.{BeforeAndAfterAll, FunSuite}

class ElasticSearchDataSystemSuite extends FunSuite with BeforeAndAfterAll{

    val spark: SparkSession = SparkSession.builder().master("local[*]").appName("estest").getOrCreate()
    test("show") {
        val map = Map("es.nodes" -> "testserver1:9200", "es.resource" -> "test_mb_1000")
        val esDbSystem: ElasticSearchDataSystem = new ElasticSearchDataSystem(map)
        val tables: Seq[String] = esDbSystem.tableNames()
        tables.foreach(println(_))

        val tableMap: Map[String, String] = esDbSystem.tableProperties("my_table")
        tableMap.foreach(e => println(s"${e._1} : ${e._2}"))

    }
    test("read") {
        spark.sql("create table my_table using org.elasticsearch.spark.sql options(type 'es', es.nodes 'testserver1', es.port '9200', es.resource 'temp_test_table/my_table')")
        val parsed: LogicalPlan = spark.sessionState.sqlParser.parsePlan("""select * from my_table""")
        val analyzed: LogicalPlan = spark.sessionState.analyzer.execute(parsed)
        val optimized: LogicalPlan = spark.sessionState.optimizer.execute(analyzed)

        val map = Map("es.nodes" -> "testserver1:9200", "es.resource" -> "temp_test_table/my_table")
        val esTbSystem: ElasticSearchDataSystem = new ElasticSearchDataSystem(map)
        esTbSystem.buildScan(optimized, spark).show(false) //test 1

        val table: DataTable = esTbSystem.buildQuery(optimized, null) //test 2
        val iter = table.iterator
        while (iter.hasNext) {
            val row = iter.next()
            row.toSeq.foreach(e => print(" " + e))
            println()
        }
    }

    test("write"){
        spark.sql("create table my_table using org.elasticsearch.spark.sql options(type 'es', es.nodes 'testserver1', es.port '9200', es.resource 'temp_test_table/my_table')")
        val parsed: LogicalPlan = spark.sessionState.sqlParser.parsePlan("""select * from my_table""")
        val analyzed: LogicalPlan = spark.sessionState.analyzer.execute(parsed)
        val optimized: LogicalPlan = spark.sessionState.optimizer.execute(analyzed)

        val map = Map("es.nodes" -> "testserver1:9200", "es.resource" -> "temp_test_table/my_table")
        val esTbSystem: ElasticSearchDataSystem = new ElasticSearchDataSystem(map)
        val table: DataTable = esTbSystem.buildQuery(optimized, null) //test 1
        val table2: DataTable = esTbSystem.buildQuery(optimized, null) //test 1

        val map2 = Map("es.nodes" -> "testserver1:9200", "es.resource" -> "temp_test_table2/my_table")
        val esTbSystem2: ElasticSearchDataSystem  = new ElasticSearchDataSystem(map2)

        esTbSystem2.truncate()  //test 1

        esTbSystem2.insert(table, SaveMode.Overwrite)  //test 2

        esTbSystem2.insert(table2, SaveMode.Append)  //test 3

        esTbSystem2.update("111", Seq(("col1",""""eee""""), ("col2", "541"), ("col3", "72.18"), ("col4", "43"), ("col5", "24"), ("col6", "true")))
        esTbSystem2.update("222", Seq(("col1",""""fff""""), ("col2", "365"), ("col3", "38.12"), ("col4", "43"), ("col5", "24"), ("col6", "true")))
        esTbSystem2.update("333", Seq(("col1",""""ggg""""), ("col2", "752"), ("col3", "75.11"), ("col4", "32"), ("col5", "52"), ("col6", "false")))


    }

    test("exist") {
        val map1 = Map("es.nodes" -> "testserver1:9200", "es.resource" -> "test_mb_1000")
        val esDbSystem1: ElasticSearchDataSystem = new ElasticSearchDataSystem(map1)
		esDbSystem1.test()

        val map2 = Map("es.nodes" -> "testserver1:9300", "es.resource" -> "test_mb_1000")
        val esDbSystem2: ElasticSearchDataSystem = new ElasticSearchDataSystem(map2)
		assertThrows(esDbSystem2.test())
    }

}
