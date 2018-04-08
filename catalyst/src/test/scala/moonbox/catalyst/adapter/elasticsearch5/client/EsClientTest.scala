package moonbox.catalyst.adapter.elasticsearch5.client

import org.scalatest.FunSuite

class EsClientTest extends FunSuite{
    val map: Map[String, String] = Map("es.nodes"->"testserver1:9200", /*"es.port"->"9200", */"es.index" -> "mb_test_100", "es.type" -> "my_table")
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
    //service.close()
}
