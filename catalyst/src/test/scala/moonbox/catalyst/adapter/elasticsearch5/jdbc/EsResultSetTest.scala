package moonbox.catalyst.adapter.elasticsearch5.jdbc

import java.sql.{Connection, DriverManager, ResultSet, Statement}
import java.util.Properties
import org.scalatest.{BeforeAndAfterAll, FunSuite}

class EsResultSetTest extends FunSuite with BeforeAndAfterAll{

    override protected def beforeAll() :Unit = {
        Class.forName("moonbox.catalyst.adapter.jdbc.Driver")
    }

    def printArray(a: Array[Any]): Unit = {
        a.foreach{
            case x: Array[Any] => {
                print("[ ")
                printArray(x)
                print(" ]")
            }
            case v => print(v.toString +" ")
            //print(" | ")
        }
    }

    test("test1") {
        var connection: Connection = null
        val url = "jdbc:es://testserver1:9200/shape_geo?type=doc"
        val prop = EsUtilTest.url2Prop(url)

        connection = DriverManager.getConnection(url, prop)
        val statement: Statement = connection.createStatement()
        val rs: ResultSet = statement.executeQuery("select name, location from shape_geo")
        while (rs.next()) {
            val ida = rs.getString("name")
            val idb = rs.getString("location")
            println(s"id -> $ida , v -> $idb ")
        }
        connection.close()
    }

    test("test2") {
        var connection: Connection = null
        val url = "jdbc:es://testserver1:9200/my_locations?type=location"
        val prop = EsUtilTest.url2Prop(url)

        connection = DriverManager.getConnection(url, prop)
        val statement: Statement = connection.createStatement()
        val rs: ResultSet = statement.executeQuery("select name , pin  from my_locations")
        while (rs.next()) {
            val ida = rs.getString("name")
            val idb = rs.getString("pin")
            println(s"id -> $ida , v -> $idb ")
        }
        connection.close()
    }

    test("test3") {
        var connection: Connection = null
        val url = "jdbc:es://testserver1:9200/attractions?type=restaurant"
        val prop = EsUtilTest.url2Prop(url)
        connection = DriverManager.getConnection(url, prop)
        val statement: Statement = connection.createStatement()
        val rs: ResultSet = statement.executeQuery("select name , location  from attractions")
        while (rs.next()) {
            val ida = rs.getString("name")
            val idb = rs.getString("location")
            println(s"id -> $ida , v -> $idb ")
        }
        connection.close()
    }

    test("basic sql type test, col name") {
        var connection: Connection = null
        val url = "jdbc:es://testserver1:9200/test_mb_100?type=my_table"
        val prop = EsUtilTest.url2Prop(url)

        connection = DriverManager.getConnection(url, prop)
        val statement: Statement = connection.createStatement()
        //val rs: ResultSet = statement.executeQuery("select max(event_id) as aaa, min(col_int_a) as bbb from test_mb_100 group by event_id")
        val rs: ResultSet = statement.executeQuery("select event_id, col_int_a, col_long_c, col_double_d, col_bool_e, col_float_g, col_str_h, col_time_b, col_int_f   from test_mb_100 where event_id < 10 order by event_id")
        while (rs.next()) {
            val event_id = rs.getLong("event_id")
            val col_int_a = rs.getInt("col_int_a")
            val col_long_c = rs.getLong("col_long_c")
            val col_double_d = rs.getDouble("col_double_d")
            val col_bool_e = rs.getBoolean("col_bool_e")
            val col_int_f = rs.getInt("col_int_f")
            val col_float_g = rs.getFloat("col_float_g")
            val col_str_h = rs.getString("col_str_h")
            val col_time_b = rs.getTimestamp("col_time_b")
            print(s" event_id -> $event_id , col_int_a -> $col_int_a, col_long_c -> $col_long_c, col_double_d ->$col_double_d")
            print(s" col_bool_e -> $col_bool_e, col_int_f-> $col_int_f, col_float_g-> $col_float_g, col_str_h->$col_str_h")
            print(s" col_time_b -> $col_time_b")
            println("")
        }
        connection.close()
    }

    test("basic sql type, col seq num") {
        var connection: Connection = null
        val url = "jdbc:es://testserver1:9200/test_mb_100?type=my_table"
        val prop = EsUtilTest.url2Prop(url)

        connection = DriverManager.getConnection(url, prop)
        val statement: Statement = connection.createStatement()
        //val rs: ResultSet = statement.executeQuery("select max(event_id) as aaa, min(col_int_a) as bbb from test_mb_100 group by event_id")
        val rs: ResultSet = statement.executeQuery("select event_id, col_int_a, col_time_b, col_long_c, col_double_d, col_bool_e, col_int_f, col_float_g, col_str_h from test_mb_100 where event_id < 10 order by event_id")
        while (rs.next()) {
            val event_id = rs.getLong(1)
            val col_int_a = rs.getInt(2)
            val col_time_b = rs.getTimestamp(3)
            val col_long_c = rs.getLong(4)
            val col_double_d = rs.getDouble(5)
            val col_bool_e = rs.getBoolean(6)
            val col_int_f = rs.getInt(7)
            val col_float_g = rs.getFloat(8)
            val col_str_h = rs.getString(8)
            val col_time_b1 = rs.getDate(3)
            val col_time_b2 = rs.getTime(3)

            print(s" event_id -> $event_id , col_int_a -> $col_int_a, col_long_c -> $col_long_c, col_double_d ->$col_double_d")
            print(s" col_bool_e -> $col_bool_e, col_int_f-> $col_int_f, col_float_g-> $col_float_g, col_str_h->$col_str_h")
            print(s" col_time_b -> $col_time_b, col_time_b1 -> $col_time_b1, col_time_b2  -> $col_time_b2 ")
            println("")
        }
        connection.close()
    }



    test("test4") {
        var connection: Connection = null
        val url = "jdbc:es://testserver1:9200/nest_table2?type=my_type"

        val prop = EsUtilTest.url2Prop(url)
        prop.put("es.read.field.as.array.include", "user")
        connection = DriverManager.getConnection(url, prop)
        val statement: Statement = connection.createStatement()
        val rs: ResultSet = statement.executeQuery("select group, user.first as aaa  from nest_table2")
        while (rs.next()) {
            val ida = rs.getString("group")
            val idb = rs.getString("aaa")
            val fname = rs.getArray("aaa")
            val array = fname.getArray.asInstanceOf[Array[Any]]
            println(s"group -> $ida , first -> $idb ")
            array.foreach(e => println("array= " + e))
        }
        connection.close()
    }

    test("test5") {
        var connection: Connection = null
        val url = "jdbc:es://testserver1:9200/people_nest?type=blogpost"
        val prop = EsUtilTest.url2Prop(url)

        prop.put("es.read.field.as.array.include", "comments")   //comments is array or nest ???
        connection = DriverManager.getConnection(url, prop)
        val statement: Statement = connection.createStatement()
        //val rs: ResultSet = statement.executeQuery("""select group as bbb, user.first as aaa  from nest_test_table where array_exists(user.age, "x>45") """)
        //val rs: ResultSet = statement.executeQuery("""select group from nest_test_table where array_exists(user.age, "x>45") """)
        val rs: ResultSet = statement.executeQuery("""SELECT comments as ccc, comments.name as aaa, comments.age as bbb from  people_nest  where array_exists(comments.age, "x>19") """) //
        while (rs.next()) {
            val ida = rs.getString("bbb")
            val idb = rs.getString("aaa")
            println(s"id -> $ida , v -> $idb ")

            val arr1 = rs.getObject("bbb")
            val arr2 = rs.getObject("aaa")
            printA(arr1)
            print("|")
            printA(arr2)
            println("||")
            //val array = rs.getArray("ccc").asInstanceOf[Array[Any]]
            //array.foreach(e => println("array= " + e))
        }
        connection.close()
    }

    test("test6") {
        var connection: Connection = null
        val url = "jdbc:es://testserver1:9200/nest_test_table?type=my_type"
        val prop = EsUtilTest.url2Prop(url)

        prop.put("es.read.field.as.array.include", "user")
        connection = DriverManager.getConnection(url, prop)
        val statement: Statement = connection.createStatement()
        val rs: ResultSet = statement.executeQuery("""SELECT array_map(user.age, "x+100") as aaa , user.age as bbb from  nest_test_table """)
        while (rs.next()) {
            val ida = rs.getString("bbb")
            val idb = rs.getString("aaa")
            println(s"id -> $ida , v -> $idb ")

            val arr1 = rs.getObject("bbb")
            val arr2 = rs.getObject("aaa")
            printA(arr1.asInstanceOf[Array[Any]])
            print("|")
            printA(arr2.asInstanceOf[Array[Any]])
            println("|")
        }
        connection.close()
    }

    test("test7") {
        var connection: Connection = null
        val url = "jdbc:es://testserver1:9200/nest_test_table?type=my_type"
        val prop = EsUtilTest.url2Prop(url)

        prop.put("es.read.field.as.array.include", "user")
        connection = DriverManager.getConnection(url, prop)
        val statement: Statement = connection.createStatement()
        val rs: ResultSet = statement.executeQuery("""SELECT array_filter(user.age, "x>20") as aaa , user.age as bbb from  nest_test_table """)
        while (rs.next()) {
            val ida = rs.getObject("bbb")
            val idb = rs.getObject("aaa")
            printA(ida)
            print("|")
            printA(idb)
            println("")
        }
        connection.close()
    }

    test("test8") {
        var connection: Connection = null
        val url = "jdbc:es://testserver1:9200/attractions?type=restaurant"
        val prop = EsUtilTest.url2Prop(url)

        //prop.put("es.read.field.as.array.include", "user")
        connection = DriverManager.getConnection(url, prop)
        val statement: Statement = connection.createStatement()
        val rs: ResultSet = statement.executeQuery("""select name, location from attractions where geo_distance('1km', 40.715, -73.988) """)
        while (rs.next()) {
            val ida = rs.getObject("name")
            val idb = rs.getObject("location")
            printA(ida)
            print(" | ")
            printA(idb)
            println("")
        }
        connection.close()
    }

    test("prop has type user pwd") {
        var connection: Connection = null
        val url = "jdbc:es://testserver1:9200/people?type=employee"
        val prop = EsUtilTest.url2Prop(url)

        prop.put("es.read.field.as.array.include", "about")
        connection = DriverManager.getConnection(url, prop)
        val statement: Statement = connection.createStatement()
        val rs: ResultSet = statement.executeQuery("""select first_name, about from people """)
        while (rs.next()) {
            val ida = rs.getString("first_name")
            val idb = rs.getArray("about")
            printA(ida)
            print(" | ")
            printA(idb)
            print(" || ")
            idb.getArray.asInstanceOf[Array[Any]].foreach(print(_))
            println("")
        }
        connection.close()
    }

    test("url has type user pwd") {
        var connection: Connection = null
        val url = "jdbc:es://testserver1:9200/people?type=employee&user=root&password=123456&es.read.field.as.array.include=about"
        val prop = EsUtilTest.url2Prop(url)

        connection = DriverManager.getConnection(url, prop)
        val statement: Statement = connection.createStatement()
        val rs: ResultSet = statement.executeQuery("""select first_name, about from people """)
        while (rs.next()) {
            val ida = rs.getString("first_name")
            val idb = rs.getArray("about")
            printA(ida)
            print(" | ")
            printA(idb)
            print(" | ")
            idb.getArray.asInstanceOf[Array[Any]].foreach(print(_))
            println("")
        }
        connection.close()
    }

    def printA(a: Any): Unit = {
        a match {
            case a: Array[Any] => printArray(a)
            case v => print(v.toString)
        }

    }

}
