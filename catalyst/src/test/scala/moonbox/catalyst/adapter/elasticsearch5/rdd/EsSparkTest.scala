package moonbox.catalyst.adapter.elasticsearch5.rdd

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


    def createDF(sql: String, map: Map[String, String]): DataFrame = {
        val parsed = spark.sessionState.sqlParser.parsePlan(sql)
        val analyzed = spark.sessionState.analyzer.execute(parsed)
        val optimized = spark.sessionState.optimizer.execute(analyzed)

        val rdd = new MbEsv5RDD[Row](spark.sparkContext, optimized, 1, map, SparkUtil.resultListToJdbcRow)

        val df = spark.createDataFrame(rdd, optimized.schema)
        df
    }

    test("esRdd") {
        //val spark: SparkSession = SparkSession.builder().master("local[*]").appName("estest").getOrCreate()
        val map = Map("nodes"->"testserver1:9200", "database" -> "test_mb_100", "type" -> "my_table")
        val sys: MbEsv5DataSys = new MbEsv5DataSys(map)

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
        val df3 = createDF(sql, map)
        df3.show(false)

        assert(df2.collect().sameElements(df3.collect()))

    }


    test("default Rdd") {

        val map = Map("nodes"->"testserver1:9200", "database" -> "nest_test_table", "type" -> "my_type")
        val sys: MbEsv5DataSys = new MbEsv5DataSys(map)

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

        val df3 = createDF(sql, map)
        df3.show(false)

        assert(df2.collect().sameElements(df3.collect()))
    }


    test("default Rdd2") {
        val map = Map("nodes"->"slave1:9200",  "database" -> "basic_info_mix_index", "type" -> "basic_info_mix_type")
        val sys: MbEsv5DataSys = new MbEsv5DataSys(map)

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

        val df3 = createDF(sql, map)
        df3.show(false)

        assert(df2.collect().sameElements(df3.collect()))
    }

    test("nest filter") {
        //val spark: SparkSession = SparkSession.builder().master("local[*]").appName("estest").getOrCreate()
        val map = Map("nodes"->"testserver1:9200", "database" -> "people_nest", "type" -> "blogpost")
        val sys: MbEsv5DataSys = new MbEsv5DataSys(map)

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

        val df3 = createDF(sql, map)
        df3.show(false)

        assert(df2.collect().sameElements(df3.collect()))
    }

}
