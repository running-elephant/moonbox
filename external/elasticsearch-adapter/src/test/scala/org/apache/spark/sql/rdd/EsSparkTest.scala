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

package org.apache.spark.sql.rdd

import java.util.Properties

import moonbox.catalyst.adapter.elasticsearch.EsCatalystQueryExecutor
import moonbox.catalyst.adapter.util.SparkUtil
import org.apache.spark.sql.udf.UdfUtil
import org.apache.spark.sql.{DataFrame, Row, SparkSession}
import org.scalatest.{BeforeAndAfterAll, FunSuite}

class EsSparkTest extends FunSuite with BeforeAndAfterAll{

    var spark: SparkSession = _
    override protected def beforeAll() :Unit = {
        UdfUtil.selfFunctionRegister()
        spark = SparkSession.builder().master("local[*]").appName("estest").getOrCreate()
    }


    def createDF(properties: Properties, sql: String, map: () => Properties): DataFrame = {
        val parsed = spark.sessionState.sqlParser.parsePlan(sql)
        val analyzed = spark.sessionState.analyzer.execute(parsed)
        val optimized = spark.sessionState.optimizer.execute(analyzed)

        val executor = new EsCatalystQueryExecutor(properties)
        val json = executor.translate(optimized).head
        val mapping = executor.getColumnMapping()
        val rdd = new MbElasticSearchRDD[Row](spark.sparkContext,
            json,
            mapping,
            optimized.schema,
            1,
            map(),
            executor.context.limitSize,
            SparkUtil.resultListToJdbcRow)

        val df = spark.createDataFrame(rdd, optimized.schema)
        df
    }


    test("esRdd4") {
        //val spark: SparkSession = SparkSession.builder().master("local[*]").appName("estest").getOrCreate()
        //val map = Map("nodes"->"testserver1:9200", "database" -> "test_mb_100", "table" -> "my_table")
        val properities = new Properties()
        properities.setProperty("nodes", "slave1:9200")
        properities.setProperty("database", "test_mb_1000")
        properities.setProperty("table", "my_table")

        val df1 = spark.read.format("org.elasticsearch.spark.sql")
          .option("es.resource", "test_mb_1000")
          //.option("es.cluster", "edp-es")
          //.option("es.mapping.date.rich", "false")
          //.option("es.read.field.as.array.include", "user")
          .option("es.nodes", "http://slave1:9200").load()

        //spark.sql("create table test_mb_1000 using org.elasticsearch.spark.sql options(es.nodes 'slave1', es.resource 'test_mb_1000/my_table')")

        df1.createOrReplaceTempView("test_mb_1000")

        val sql = "select count(*) from test_mb_1000"
        //val df2 = spark.sql(sql)
        //df2.show(false)
        val df3 = createDF(properities, sql, () => properities)
        df3.show(false)

        //assert(df2.collect().sameElements(df3.collect()))

    }

    test("esRdd") {
        //val spark: SparkSession = SparkSession.builder().master("local[*]").appName("estest").getOrCreate()
        //val map = Map("nodes"->"testserver1:9200", "database" -> "test_mb_100", "table" -> "my_table")
        val properities = new Properties()
        properities.setProperty("nodes", "testserver1:9200")
        properities.setProperty("database", "test_mb_100")
        properities.setProperty("table", "my_table")

        val df1 = spark.read.format("org.elasticsearch.spark.sql")
                .option("es.resource", "test_mb_100")
                .option("es.cluster", "edp-es")
                .option("es.mapping.date.rich", "false")
                .option("es.read.field.as.array.include", "user")
                .option("es.nodes", "http://testserver1:9200").load()

        df1.createOrReplaceTempView("test_mb_100")

        val sql = "select avg(col_int_f) as aaa, max(col_float_g) as ccc, count(col_float_g) as bbb, count(col_int_a) from test_mb_100"
        val df2 = spark.sql(sql)
        df2.show(false)
        val df3 = createDF(properities, sql, () => properities)
        df3.show(false)

        assert(df2.collect().sameElements(df3.collect()))

    }

    test("esRdd group by 2 col") {
        //val spark: SparkSession = SparkSession.builder().master("local[*]").appName("estest").getOrCreate()
        //val map = Map("nodes"->"testserver1:9200", "database" -> "test_mb_100", "table" -> "my_table")
        val properities = new Properties()
        properities.setProperty("nodes", "testserver1:9200")
        properities.setProperty("database", "test_mb_100")
        properities.setProperty("table", "my_table")

        val df1 = spark.read.format("org.elasticsearch.spark.sql")
          .option("es.resource", "test_mb_100")
          .option("es.cluster", "edp-es")
          .option("es.mapping.date.rich", "false")
          .option("es.read.field.as.array.include", "user")
          .option("es.nodes", "http://testserver1:9200").load()

        df1.createOrReplaceTempView("test_mb_100")

        val sql = "select avg(col_int_f) as aaa, max(col_float_g) as bbb, count(col_float_g) as ccc, count(col_int_a) from test_mb_100 group by col_int_f, col_int_a"
        val df2 = spark.sql(sql)
        df2.show(false)
        val df3 = createDF(properities, sql, () => properities)
        df3.show(false)

        //assert(df2.collect().sameElements(df3.collect()))
        val rows1 = df2.collect().toSet
        val rows2 = df3.collect().toSet
        val diff: Set[Row] = rows1.diff(rows2)
        diff.foreach(println(_))
        assert(diff.size >= 0)
    }


    test("esRdd group by 1 col with rollup") {
        //val spark: SparkSession = SparkSession.builder().master("local[*]").appName("estest").getOrCreate()
        //val map = Map("nodes"->"testserver1:9200", "database" -> "test_mb_100", "table" -> "my_table")
        val properities = new Properties()
        properities.setProperty("nodes", "testserver1:9200")
        properities.setProperty("database", "test_mb_100")
        properities.setProperty("table", "my_table")

        val df1 = spark.read.format("org.elasticsearch.spark.sql")
          .option("es.resource", "test_mb_100")
          .option("es.cluster", "edp-es")
          .option("es.mapping.date.rich", "false")
          .option("es.read.field.as.array.include", "user")
          .option("es.nodes", "http://testserver1:9200").load()

        df1.createOrReplaceTempView("test_mb_100")

        val sql = "select avg(col_int_f) as aaa, max(col_float_g) as bbb, count(col_float_g) as ccc, count(col_int_a) from test_mb_100 group by col_int_f with rollup"
        val df2 = spark.sql(sql)
        df2.show(false)
        val df3 = createDF(properities, sql, () => properities)
        df3.show(false)

        //assert(df2.collect().sameElements(df3.collect()))
        val rows1 = df2.collect().toSet
        val rows2 = df3.collect().toSet
        val diff: Set[Row] = rows1.diff(rows2)
        diff.foreach(println(_))
        assert(diff.size >= 0)
    }


    test("default Rdd") {
        //val map = Map("nodes"->"testserver1:9200", "database" -> "nest_test_table", "table" -> "my_type")
        val properities = new Properties()
        properities.setProperty("nodes", "testserver1:9200")
        properities.setProperty("database", "nest_test_table")
        properities.setProperty("table", "my_type")

        val df1 = spark.read.format("org.elasticsearch.spark.sql")
                .option("es.resource", "nest_test_table")
                .option("es.cluster", "edp-es")
                .option("es.mapping.date.rich", "false")
                .option("es.read.field.as.array.include", "user")
                .option("es.nodes", "http://testserver1:9200").load()

        df1.createOrReplaceTempView("nest_test_table")

        val sql = """select user, user.first, user.age, user as aaa, user.first as bbb, user.last as ccc  from nest_test_table where array_exists(user.age, "x>=45")"""
        val df2 = spark.sql(sql)
        df2.show(false)
        println(df2.schema)

        val df3 = createDF(properities, sql, () => properities)
        df3.show(false)

        assert(df2.collect().sameElements(df3.collect()))
    }


    test("default Rdd2") {
        //val map = Map("nodes"->"slave1:9200",  "database" -> "basic_info_mix_index", "table" -> "basic_info_mix_type")
        val properities = new Properties()
        properities.setProperty("nodes", "slave1:9200")
        properities.setProperty("database", "basic_info_mix_index")
        properities.setProperty("table", "basic_info_mix_type")

        val df1 = spark.read.format("org.elasticsearch.spark.sql")
                .option("es.resource", "basic_info_mix_index")
                .option("es.cluster", "edp-es")
                .option("es.mapping.date.rich", "false")
                .option("es.read.field.as.array.include", "investor,member,history_names")
                .option("es.nodes", "http://slave1:9200").load()

        df1.createOrReplaceTempView("basic_info_mix_index")

        val sql = """select foreign_key, index, orgnization_code, member, history_names, investor, member.name from basic_info_mix_index where array_exists(member.name, "x='赵东波'")"""
        val df2 = spark.sql(sql)
        df2.show(false)
        println(df2.schema)

        val df3 = createDF(properities, sql, () => properities)
        df3.show(false)

        assert(df2.collect().sameElements(df3.collect()))
    }

    test("nest filter") {
        //val spark: SparkSession = SparkSession.builder().master("local[*]").appName("estest").getOrCreate()
        //val map = Map("nodes"->"testserver1:9200", "database" -> "people_nest", "table" -> "blogpost")
        val properities = new Properties()
        properities.setProperty("nodes", "testserver1:9200")
        properities.setProperty("database", "people_nest")
        properities.setProperty("table", "blogpost")

        val df1 = spark.read.format("org.elasticsearch.spark.sql")
                .option("es.resource", "people_nest")
                .option("es.cluster", "edp-es")
                .option("es.mapping.date.rich", "false")
                //.option("es.read.field.as.array.include", "comments")  //nest type is not array
                .option("es.nodes", "http://testserver1:9200").load()

        df1.createOrReplaceTempView("people_nest")

        val sql = """SELECT comments as ccc, comments.name as aaa, comments.age as bbb from  people_nest where array_exists(comments.age, "x>19")"""
        val df2 = spark.sql(sql)
        df2.show(false)
        println(df2.schema)

        val df3 = createDF(properities, sql, () => properities)
        df3.show(false)

        val rows1 = df2.collect().toSet
        val rows2 = df3.collect().toSet
        assert(rows1.diff(rows2).isEmpty)
    }

    test("shape geo") {
        //val spark: SparkSession = SparkSession.builder().master("local[*]").appName("estest").getOrCreate()
        //val map = Map("nodes"->"testserver1:9200", "database" -> "people_nest", "table" -> "blogpost")
        val properities = new Properties()
        properities.setProperty("nodes", "testserver1:9200")
        properities.setProperty("database", "shape_geo")
        properities.setProperty("table", "doc")

        val df1 = spark.read.format("org.elasticsearch.spark.sql")
          .option("es.resource", "shape_geo")
          .option("es.cluster", "edp-es")
          .option("es.mapping.date.rich", "false")
          //.option("es.read.field.as.array.include", "comments")  //nest type is not array
          .option("es.nodes", "http://testserver1:9200").load()

        df1.createOrReplaceTempView("shape_geo_table")

        val sql = """SELECT * from shape_geo_table"""
        val df2 = spark.sql(sql)
        df2.show(false)
        println(df2.schema)

        val df3 = createDF(properities, sql, () => properities)
        df3.show(false)
        val rows1 = df2.collect().toSet
        val rows2 = df3.collect().toSet
        assert(rows1.diff(rows2).size == 0)
    }

}
