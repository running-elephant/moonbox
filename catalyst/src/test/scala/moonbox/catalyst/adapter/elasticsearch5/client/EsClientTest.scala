package moonbox.catalyst.adapter.elasticsearch5.client

import org.apache.spark.sql.types._
import org.scalatest.FunSuite

class EsClientTest extends FunSuite{
    val map: Map[String, String] = Map("nodes"->"testserver1:9200", /*"es.port"->"9200", */"index" -> "mb_test_100", "type" -> "my_table")
    val service: EsRestClient = new EsRestClient(map)

    def getAgg(): String = {
        """
          |{
          |	"from": 0,
          |	"size": 0,
          |	"_source": {
          |		"includes": [
          |			"MAX",
          |			"AVG"
          |		],
          |		"excludes": []
          |	},
          |	"aggregations": {
          |		"col_int_f": {
          |			"terms": {
          |				"field": "col_int_f",
          |				"size": 200
          |			},
          |			"aggregations": {
          |				"col_bool_b": {
          |					"terms": {
          |						"field": "col_bool_e",
          |						"size": 100
          |					},
          |					"aggregations": {
          |						"MAX(event_id)": {
          |							"max": {
          |								"field": "event_id"
          |							}
          |						},
          |						"AVG(col_int_f)": {
          |							"avg": {
          |								"field": "col_int_f"
          |							}
          |						}
          |					}
          |				}
          |			}
          |		}
          |	}
          |}
        """.stripMargin

    }
    def getSelect(): String = {
        """
          |{
          |	"from": 0,
          |	"size": 1,
          |	"_source": {
          |		"includes": [
          |			"event_id"
          |		],
          |		"excludes": []
          |	}
          |}
        """.stripMargin
    }

    test("get select data") {
        import scala.collection.JavaConversions._
        val str = getSelect()
        val s = service.performScrollRequest("test_mb_100", "my_table", str)
        s.getHits.foreach{e =>
            e.getMap.foreach(print(_))
            println()
        }
    }

    test("get data agg") {
        import scala.collection.JavaConversions._
        val str = getAgg()
        val s = service.performScrollRequest("test_mb_100", "my_table", str)
        s.getAggregations.foreach{e =>
            e.getMap.foreach(print(_))
            println()
        }
    }

    test("get schema common") {
        val s = service.getSchema("test_mb_100", "my_table")
        println(s)
    }

    test("get schema nest") {
        val s = service.getSchema("nest_table", "my_type")
        println(s)
    }

    test("get schema nest2") {
        val s = service.getSchema("nest_table2", "my_type")
        println(s)
    }

    test("get schema nest3") {
        val s = service.getSchema("nest_test_table", "my_type")
        println(s)
    }


    test("get schema nest5") {
        val s = service.getSchema("my_locations", "location")
        println(s)
    }

    test("get index") {
        val s = service.getIndices()
        println(s)
    }

    test("get stats") {
        val index = "nest_table2"
        val (c, s) = service.getStats(index)
        println(s"index=${index}, count=${c}, size=${s}")
    }

    test("get index and type") {
        val tuples :Seq[(String, String)] = service.getIndicesAndType()
        tuples.foreach(elem => println(s"${elem._1} : ${elem._2}"))
    }

    test("create index"){
        val ret1 = service.deleteIndex("temp_test_table")
        println(ret1)

        val stype = StructType(
            Seq(StructField("col1", StringType),
                StructField("col2", LongType),
                StructField("col3", DoubleType),
                StructField("col4", ShortType),
                StructField("col5", FloatType),
                StructField("col6", BooleanType)
            ))
        val ret2 = service.putSchema("temp_test_table", "my_table", stype)
        println(ret2)

        val stype3 = StructType(
            Seq(StructField("col1", StringType),
                StructField("col2", LongType),
                StructField("col3", DoubleType),
                StructField("col4", ShortType),
                StructField("col5", FloatType),
                StructField("col6", BooleanType)
            ))
        val ret3 = service.updateSchema("temp_test_table", "my_table3", stype3)
        println(ret3)

    }

    test("batch insert primitive data") {
        val stype = StructType(
            Seq(StructField("col1", StringType),
                StructField("col2", LongType),
                StructField("col3", DoubleType),
                StructField("col4", ShortType),
                StructField("col5", FloatType),
                StructField("col6", BooleanType)
            ))
        val data1 = Seq(
            Seq("aaaaaa", 100l, 12.40d, 14, 16f, true ),
            Seq("bbbbbb", 200l, 18.25d, 11, 14f, true ),
            Seq("cccccc", 300l, 36.85d, 15, 18f, false ),
            Seq("dddddd", 400l, 62.30d, 18, 11f, false ))

        val data2 = Seq(
            Seq("eeeeee", 500l, 52.40d, 44, 36f, true ),
            Seq("ffffff", 600l, 78.25d, 51, 44f, true ),
            Seq("gggggg", 700l, 96.85d, 75, 58f, false ),
            Seq("hhhhhh", 800l, 42.30d, 38, 61f, false ))

        val ret = service.putBatchData("temp_test_table", "my_table", stype, data1)
        println(s"${ret._1} ${ret._2}")
        val ret2 = service.putBatchData("temp_test_table", "my_table3", stype, data1)
        println(s"${ret2._1} ${ret2._2}")

    }

    test("truncate index") {
        val ret = service.truncateIndex("temp_test_table", "my_table3")
        println(ret)
    }

    test("create complex index"){
        val ret1 = service.deleteIndex("temp_complex_table")
        println(ret1)

        val stype = StructType(
            Seq(StructField("col1", TimestampType),
                StructField("col2", DateType),
                StructField("col3", ArrayType(LongType)),
                StructField("col4", MapType(StringType, LongType)),
                StructField("col5", StructType(Seq(
                    StructField("col5-a", StringType),
                    StructField("col5-b", StringType)
                )))
            ))
        val ret2 = service.putSchema("temp_complex_table", "my_table", stype)
        println(ret2)

    }

    test("batch insert complex data") {
        val stype = StructType(
            Seq(StructField("col1", TimestampType),
                StructField("col2", DateType),
                StructField("col3", ArrayType(LongType)),
                StructField("col4", MapType(StringType, LongType)),
                StructField("col5", StructType(Seq(
                    StructField("col5-a", StringType),
                    StructField("col5-b", StringType)
                )))
            ))
        val data1 = Seq(
            Seq(
                new java.sql.Timestamp(2018-1900, 10, 10, 10, 10, 10, 0),
                new java.sql.Date(2018-1900, 10, 10),
                Seq(1l, 2l, 3l, 4l),
                Map("aaa" -> 1l, "bbb"-> 2l),
                Seq("ggg", "fff" )),
            Seq(
                new java.sql.Timestamp(2019-1900, 2, 2, 10, 10, 10, 0),
                new java.sql.Date(2019-1900, 10, 10),
                Seq(52l, 26l, 75l, 23l),
                Map("ccc" -> 4l, "ddd"-> 5l),
                Seq("tttt", "qqqq" ))
        )


        val ret = service.putBatchData("temp_complex_table", "my_table", stype, data1)
        println(s"${ret._1} ${ret._2}")
    }


    test("update one data") {
        val schema = StructType(
            Seq(StructField("col1", StringType),
                StructField("col2", LongType),
                StructField("col3", DoubleType),
                StructField("col4", ShortType),
                StructField("col5", FloatType),
                StructField("col6", BooleanType)
            ))

        val data1 = Seq(("col1","eee"), ("col2", 541l), ("col3", 72.18d), ("col4", 43), ("col5", 24f), ("col6", true))
        //val ret1 = service.update("temp_test_table", "my_table", "111", data1, schema)
        //println(ret1)

        val data2 = Seq(("col1","fff"), ("col2", 365l), ("col3", 38.12d), ("col4", 43), ("col5", 24f), ("col6", true))
        //val ret2 = service.update("temp_test_table", "my_table", "111", data2, schema)
        //println(ret2)

        val data3 = Seq(("col1","ggg"), ("col2", 752l), ("col3", 75.11d), ("col4", 32), ("col5", 52f), ("col6", false))
        //val ret3 = service.update("temp_test_table", "my_table", "111", data3, schema)
        //println(ret3)


    }


    //service.close()
}
