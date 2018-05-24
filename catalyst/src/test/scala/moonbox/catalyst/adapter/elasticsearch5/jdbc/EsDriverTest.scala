package moonbox.catalyst.adapter.elasticsearch5.jdbc

import java.sql.{Connection, DriverManager, ResultSet, Statement}
import java.util.Properties

import org.scalatest.FunSuite

class EsDriverTest extends FunSuite{
    def printArray(a: Array[Any]): Unit = {
        a.foreach {
                case x: Array[Any] => {
                    print("[ ")
                    printArray(x)
                    print(" ]")
                }
                case v: Any => print(v.toString + " ")
                //print(" | ")

        }
    }

    def printObject(a: Any): Unit = {
        a match {
            case a: Array[Any] =>
                print("[ ")
                printArray(a)
                print(" ]")
            case null => print("null")
            case v: Any => print(v.toString)
        }
        print(" | ")
    }
    //-----------------------------------
    test("zhicheng, project complex object and array"){
        doQuery("basic_info_mix_index", "basic_info_mix_type", "select index, orgnization_code, registerAgency, member, history_names, investor from basic_info_mix_index", Map("es.read.field.as.array.include" -> "member,investor"),"slave1")
    }

    //对于下推的array_exists,要求在project中写要过滤的列名，否则过滤不准确，处理困难
    //history name 作为条件过来失败，不确定是否为数组如何处理，schema也有问题
    test("zhicheng, filter complex object and array"){  //TODO: history is or is not array
        doQuery("basic_info_mix_index", "basic_info_mix_type", """select index, orgnization_code, member, history_names, investor from basic_info_mix_index where array_exists(history_names, "x='北京乐驰体育文化有限公司'")""", Map("es.read.field.as.array.include" -> "member,history_names,investor"), "slave1")
    }

    test("zhicheng, filter complex object and array2"){
        doQuery("basic_info_mix_index", "basic_info_mix_type", """select index, orgnization_code, member, history_names, investor from basic_info_mix_index where array_exists(member.name, "x='张一凡'")""", Map("es.read.field.as.array.include" -> "member,investor"), "slave1")
    }

    test("zhicheng, filter complex object and array3"){  //not accurate
        doQuery("basic_info_mix_index", "basic_info_mix_type", """select index, orgnization_code, member, history_names, investor from basic_info_mix_index where array_exists(member.name, "x='刘东良'")""", Map("es.read.field.as.array.include" -> "member,investor"), "slave1")
    }

    test("zhicheng, filter complex object and array3 in select"){  //accurate
        doQuery("basic_info_mix_index", "basic_info_mix_type", """select index, orgnization_code, member, history_names, investor, member.name as aaa from basic_info_mix_index where array_exists(member.name, "x='刘东良'")""", Map("es.read.field.as.array.include" -> "member,investor"), "slave1")
    }

    test("zhicheng, filter complex object and array4"){  //赵东波 not accurate
        doQuery("basic_info_mix_index", "basic_info_mix_type", """select index, orgnization_code, member, history_names, investor from basic_info_mix_index where array_exists(investor.investorName, "x='代广宇'")""", Map("es.read.field.as.array.include" -> "investor,member"), "slave1")
    }

    test("zhicheng, filter complex object and array5"){  //not accurate
        doQuery("basic_info_mix_index", "basic_info_mix_type", """select index, orgnization_code, member, member.name as MNAME, history_names, investor from basic_info_mix_index where array_exists(investor.investorName, "x='代广宇' or x='李建京'")""", Map("es.read.field.as.array.include" -> "investor,member"), "slave1")
    }

    test("zhicheng, filter complex object and array5 in select, string expression"){
        doQuery("basic_info_mix_index", "basic_info_mix_type", """select index, orgnization_code, member, member.name as MNAME, history_names, investor, investor.investorName from basic_info_mix_index where array_exists(investor.investorName, "x='代广宇' or x='李建京'")""", Map("es.read.field.as.array.include" -> "investor,member"), "slave1")
    }

    test("zhicheng, filter complex object and array5 in select, lambda expression"){
        doQuery("basic_info_mix_index", "basic_info_mix_type", """select index, orgnization_code, member, member.name as MNAME, history_names, investor, investor.investorName from basic_info_mix_index where array_exists(investor.investorName, y => y='代广宇' or y='李建京')""", Map("es.read.field.as.array.include" -> "investor,member"), "slave1")
    }

    test("array filter1 string expression"){
        doQuery("nest_test_table", "my_type", """SELECT user.age, user from nest_test_table where array_exists(user.age, "x>=45 and x<=60")""", Map("es.read.field.as.array.include" -> "user"))
    }

    test("array filter1 lambda expression"){
        doQuery("nest_test_table", "my_type", """SELECT user.age, user from nest_test_table where array_exists(user.age, x => x>=45 and x<=60)""", Map("es.read.field.as.array.include" -> "user"))
    }

    //------------------------------------
    test("complex array test0"){
        doQuery("nest_test_table", "my_type", "select user from nest_test_table", Map("es.read.field.as.array.include" -> "user"))
    }

    test("complex array test"){
        doQuery("nest_test_table", "my_type", "select group, user, user.first, user.last, user as aaa, user.first as bbb from nest_test_table", Map("es.read.field.as.array.include" -> "user"))
    }

    test("select array0 "){
        doQuery("shape_geo", "doc", """select name, location from shape_geo""")
    }

    test("select array1 "){
        doQuery("my_locations", "location", """select name , pin  from my_locations""")
    }

    test("select array2 "){
        doQuery("attractions", "restaurant", """select name , location  from attractions""")
    }

    test("select mul column and alias"){
        doQuery("nest_test_table", "my_type", """select user, user.first, user.age, user as aaa, user.first as bbb, user.last as ccc  from nest_test_table where array_exists(user.age, "x>=45")""", Map("es.read.field.as.array.include" -> "user"))
    }

    test("select array element1"){
        doQuery("nest_table2", "my_type", """select group, user.first  from nest_table2""", Map("es.read.field.as.array.include" -> "user"))
    }

    test("select array element2"){
        doQuery("nest_table2", "my_type", """select group, user.first as aaa  from nest_table2""", Map("es.read.field.as.array.include" -> "user"))
    }

    test("select array element3"){
        doQuery("nest_table2", "my_type", """select group, user as aaa  from nest_table2""", Map("es.read.field.as.array.include" -> "user"))
    }

    test("select array element4"){
        doQuery("nest_table2", "my_type", """select group, user from nest_table2""", Map("es.read.field.as.array.include" -> "user"))
    }

    test("array exist0"){
        doQuery("people_nest", "blogpost", """SELECT comments.name as aaa from  people_nest """, Map("es.read.field.as.array.include" -> "user"))
    }

    test("array exist1"){
        doQuery("people_nest", "blogpost", """SELECT comments.name as aaa, comments.age as bbb from  people_nest where array_exists(comments.age, "x>19") """, Map("es.read.field.as.array.include" -> "user"))
    }

    test("array exist2"){
        doQuery("nest_test_table", "my_type", """select user, user.first from nest_test_table where array_exists(user.first, "x='John'")""", Map("es.read.field.as.array.include" -> "user"))
    }

    test("array exist not work"){
        doQuery("nest_test_table", "my_type", """select * from nest_test_table where array_exists(user.age, "x<=80 and x>=55")""", Map("es.read.field.as.array.include" -> "user"))
    }

    test("array exist work (in project)"){
        doQuery("nest_test_table", "my_type", """select user, user.age from nest_test_table where array_exists(user.age, "x<=80 and x>=55")""", Map("es.read.field.as.array.include" -> "user"))
    }

    test("array map1"){
        doQuery("nest_test_table", "my_type", """SELECT array_map(user.age, "x+100") as aaa , user.age as bbb from  nest_test_table""", Map("es.read.field.as.array.include" -> "user"))
    }

    test("array map2"){
        doQuery("nest_test_table", "my_type", """select array_map(user.first, "x+\"aaa\""), user.first from  nest_test_table""", Map("es.read.field.as.array.include" -> "user"))
    }

    test("array filter2"){
        doQuery("nest_test_table", "my_type", """SELECT array_filter(user.age, "x>20") as aaa , user.age as bbb from  nest_test_table""", Map("es.read.field.as.array.include" -> "user"))
    }
    //------geo distance---------

    test("geo1 distance and filter"){  //GEO_DISTANCE
        doQuery("attractions", "restaurant", """select name from attractions where geo_distance('1km', 40.715, -73.988)""" )
    }

    test("geo1 distance 2 and filter"){
        doQuery("attractions", "restaurant", "select * from attractions where geo_distance('1km', 40.715, -73.988) and name='Pala Pizza'" )
    }

    test("geo2  select "){
        doQuery("shape_geo", "doc", "select * from shape_geo" )
    }

    test("geo2 shape and filter"){
        doQuery("shape_geo", "doc", "select name,location from shape_geo where geo_shape('envelope', 13.0, 53.0, 14.0, 52.0, 'within')" )
    }


    test("geo3 box"){  //GEO_BOUNDING_BOX
        doQuery("my_locations", "location", "select name, pin from my_locations where geo_bounding_box('pin.location', 40.73, -74.1, 40.01, -71.12)" )
    }


    test("geo4 polygon"){
        doQuery("my_locations", "location", "select name, pin from my_locations where geo_polygon('pin.location', array(40,30,20,50), array(-70,-80,-90,-90) )" )
    }


    test("geo hash grid aggregation") {

    }

    //---------compute and literal------
    test("col normal"){
        doQuery("test_mb_100", "my_table", "select * from test_mb_100 " )
    }

    test("col 1000 lines"){
        doQuery("test_mb_1000", "my_table", "select * from test_mb_1000 order by event_id asc" )
    }

    test("col 100w of lines"){
        //doQuery("test_mb_100w", "my_table", "select * from test_mb_100w order by event_id asc" )
    }

    test("col + nothing"){
        doQuery("test_mb_100", "my_table", "select col_int_f, col_int_f  from test_mb_100 " )
    }

    //case when, substring, round, cast
    test("col + substring"){  //mb_100 can not substring
        doQuery("test_mb_1000", "my_table", "select substring(col_str_k, 0, 2) from test_mb_1000 " )
    }

    test("col + case when 1"){  //mb_100 can not substring
        doQuery("test_mb_1000", "my_table", "select case when col_long_a >50 then 1 else 0 end from test_mb_1000 " )
    }

    test("col + case when 2"){  //mb_100 can not substring
        doQuery("test_mb_1000", "my_table", "select case when col_long_a >50 and col_long_a<70 then 1 else 0 end from test_mb_1000 " )
    }

    test("col + case when 3"){  //mb_100 can not substring
        doQuery("test_mb_1000", "my_table", "select case when col_str_k in ('xlOquLuKjH', 'uTQrFXPOoc', 'RgTcVREWxMpI') then 1 else 0 end from test_mb_1000 " )
    }

    test("col + case when 4"){  //mb_100 can not substring
        doQuery("test_mb_1000", "my_table", "select case when col_str_k in ('xlOquLuKjH', 'uTQrFXPOoc', 'RgTcVREWxMpI') and col_long_a >50 then 1 else 0 end from test_mb_1000 " )
    }

    test("col + round"){  //mb_100 can not substring
        doQuery("test_mb_1000", "my_table", "select round(col_float_g) from test_mb_1000 " )
    }

    test("col + cast"){  //mb_100 can not substring
        doQuery("test_mb_1000", "my_table", "select cast(col_float_g as double) from test_mb_1000 " )
    }

    test("col + sum + case when 1"){  //mb_100 can not substring
        doQuery("test_mb_1000", "my_table", "select sum(case when col_long_a >50 and col_long_a<70 then 1 else 0 end) as aaa from test_mb_1000 " )
    }

    test("col + sum + case when 2"){  //mb_100 can not substring
        doQuery("test_mb_1000", "my_table", "select sum(case when col_long_a >50 and col_long_a<70 then 1 else 0 end) as aaa, col_int_f, col_bool_e  from test_mb_1000 group by col_int_f, col_bool_e " )
    }

    test("col + sum + case when 3"){  //mb_100 can not substring
        doQuery("test_mb_1000", "my_table", "select sum(case when col_str_k > 'AB' then 1 else 0 end) as aaa, col_int_f, col_bool_e  from test_mb_1000 group by col_int_f, col_bool_e ") //, Map("es.mapping.date.rich" -> "false")
    }

    test("col + sum + case when 4"){  //mb_100 can not substring
        doQuery("test_mb_1000", "my_table", "select sum(case when substring(col_str_k, 0, 2) > 'AB' then 1 else 0 end) as aaa, col_int_f, col_bool_e  from test_mb_1000 group by col_int_f, col_bool_e ") //, Map("es.mapping.date.rich" -> "false")
    }

    test("col + sum + case when 5"){  //mb_100 can not substring
        doQuery("test_mb_1000", "my_table", "select sum(case when substring(col_str_k, 0, 2) >= 'AB' and substring(col_str_l, 0, 2) <= 'DF' and col_int_f >10  then 1 else 0 end) as aaa, col_int_f, col_bool_e  from test_mb_1000 group by col_int_f, col_bool_e ") //, Map("es.mapping.date.rich" -> "false")
    }

    test("col + sum + case when 6"){  //mb_100 can not substring
        doQuery("test_mb_1000", "my_table", "select sum(case when substring(col_str_k, 0, 2) >= 'AB'  and col_int_f in (1, 2, 3, 4, 5, 6, 7)  then 1 else 0 end) as aaa, col_int_f, col_bool_e  from test_mb_1000 group by col_int_f, col_bool_e ") //, Map("es.mapping.date.rich" -> "false")
    }

    test("col + count + case when 1"){  //mb_100 can not substring
        doQuery("test_mb_1000", "my_table", "select count(case when col_str_k is not null then col_int_f  end) as aaa, col_int_f, col_bool_e  from test_mb_1000 group by col_int_f, col_bool_e ") //, Map("es.mapping.date.rich" -> "false")
    }

    test("col + count + distinct case when 2"){  //mb_100 can not substring
        doQuery("test_mb_1000", "my_table", "select count(distinct case when  col_str_k is not null then col_int_f  end) as aaa, col_int_f, col_bool_e  from test_mb_1000 group by col_int_f, col_bool_e ") //, Map("es.mapping.date.rich" -> "false")
    }

    test("col + count + distinct case when 3"){  //mb_100 can not substring
        doQuery("test_mb_1000", "my_table", "select count(distinct case when  col_int_f + 1 > 1  then col_int_f  end) as aaa, col_int_f, col_bool_e  from test_mb_1000 group by col_int_f, col_bool_e ") //, Map("es.mapping.date.rich" -> "false")
    }

    test("col + if "){  //mb_100 can not substring
        //doQuery("test_mb_1000", "my_table", "select if(count(distinct col_int_f)>100, 0, 1) as aaa, col_int_f, col_bool_e  from test_mb_1000 group by col_int_f, col_bool_e ") //, Map("es.mapping.date.rich" -> "false")
    }


    test("col + literal"){
        doQuery("test_mb_100", "my_table", "select col_int_f + 1 from test_mb_100 " )
    }

    test("col + literal and alias"){
        doQuery("test_mb_100", "my_table", "select col_int_f + 1 as aaa from test_mb_100 " )
    }

    test("col * literal"){
        doQuery("test_mb_100", "my_table", "select col_int_f * 2 + 10 * 20 from test_mb_100 " )
    }

    test("query select 1") {
        doQuery("test_mb_100", "my_table", "select 1 from test_mb_100" )
    }

    test("query select 1 as alias") {
        doQuery("test_mb_100", "my_table", "select 1 as aaa from test_mb_100" )
    }

    //------agg test_mb_100--------

    test("select and filter") {
        doQuery("test_mb_100", "my_table", "select * from test_mb_100 where col_int_f >1" )
    }

    test("select distinct") {
        doQuery("test_mb_100", "my_table", "select distinct(col_int_a) from test_mb_100" )
    }

    test("select count distinct") {
        doQuery("test_mb_100", "my_table", "select count(distinct(col_int_a)) from test_mb_100" )
    }


    test("select count distinct groupby"){
        doQuery("test_mb_100", "my_table", "select col_int_a, count(distinct(col_int_f)) from test_mb_100 group by col_int_a" )
    }

    test("select max avg"){
        doQuery("test_mb_100", "my_table", "select max(col_int_f), avg(col_int_a) from test_mb_100" )
    }

    test("select max avg and alias"){
        doQuery("test_mb_100", "my_table", "select max(col_int_f) as aaa, avg(col_int_a) as bbb from test_mb_100" )
    }

    test("select max avg groupby 0 col"){
        doQuery("test_mb_100", "my_table", "select max(col_int_f), avg(col_int_f) from test_mb_100" )
    }

    test("select max avg groupby 1 col no select group by"){
        doQuery("test_mb_100", "my_table", "select max(col_int_f), avg(col_int_f) from test_mb_100 group by col_int_a" )
    }

    test("select max avg groupby 1 col select group by"){
        doQuery("test_mb_100", "my_table", "select max(col_int_f), avg(col_int_f), col_int_a from test_mb_100 group by col_int_a" )
    }

    test("select max avg groupby 2 col"){
        doQuery("test_mb_100", "my_table", "select max(col_int_f), avg(col_int_f), avg(col_int_a), col_int_f, col_int_a  from test_mb_100 group by col_int_f, col_int_a" )
    }

    test("avg max count alias filter"){
        doQuery("test_mb_100", "my_table", "select avg(col_int_f) as aaa, max(col_float_g) as ccc, count(col_float_g) as bbb, count(col_int_a) from test_mb_100 where col_int_f < 5" )
    }


    test("query count *") {
        doQuery("test_mb_100", "my_table", "select count(*) from test_mb_100" )
    }

    test("query count 1") {
        doQuery("test_mb_100", "my_table", "select count(1) from test_mb_100" )
    }

    test("query order by asc") {
        doQuery("test_mb_100", "my_table", "select event_id, col_int_f, col_int_a from test_mb_100 order by col_int_f, col_int_a" )
    }

    test("query order by desc") {
        doQuery("test_mb_100", "my_table", "select event_id, col_int_f, col_int_a, col_double_d from test_mb_100 order by col_double_d desc" )
    }

    test("query order by asc/desc") {
        doQuery("test_mb_100", "my_table", "select event_id, col_int_f, col_int_a from test_mb_100 order by col_int_f desc, col_int_a asc" )
    }

    test("query filter between") {
        doQuery("test_mb_100", "my_table", "select event_id, col_int_f, col_int_a, col_double_d from test_mb_100 where col_double_d between 10 and 20" )
    }

    test("query filter limit 2") {  //if set limit, no repeat
        doQuery("test_mb_100", "my_table", "select event_id, col_double_d from test_mb_100  limit 2" )
    }

    test("query avg group by having") {
        doQuery("test_mb_100", "my_table", "select avg(event_id), col_int_f from test_mb_100 group by col_int_f having col_int_f < 5" )
    }

    test("query avg group by having max") {  //TODO: having aggfun, not support yet
        //doQuery("test_mb_100", "my_table", "select avg(event_id) from test_mb_100 group by col_int_f having max(col_int_f) < 5 and col_int_f > 1" )
    }

    test("select date") {
        doQuery("date_table", "my_type", "select date from date_table" )
    }

    test("select date TZ") {  //println("+"+ rs.getTimestamp(0))
        doQuery("my_date", "my_type", "select mydate from my_date" )
    }

    test("select array with filter") {
        doQuery("people", "employee", """select about from people where array_exists(about, "x='playing'")""", Map("es.read.field.as.array.include" -> "about"))
    }

    test("select array with all") {
        doQuery("people", "employee", """select about from people""", Map("es.read.field.as.array.include" -> "about"))
    }

    test("select like") {
        doQuery("attractions", "restaurant", "select * from attractions where name like 'Mini%'")
    }

    test("select like2") {
        doQuery("attractions", "restaurant", "select * from attractions where name like '%Munchies%'")
    }

    test("select like3") {
        doQuery("attractions", "restaurant", "select * from attractions where name like 'Mini Munchies Piz__'")
    }

    test("select like4") {
        doQuery("attractions", "restaurant", "select * from attractions where name like '__ni Munchies Pizza'")
    }

    test("select like5") {
        doQuery("attractions", "restaurant", "select * from attractions where name like '__ni Munchies Pizz__'")
    }

    test("select like6") {
        doQuery("attractions", "restaurant", "select * from attractions where name like '% Munchies Piz__'")
    }

    test("select in") {
        doQuery("test_mb_100", "my_table", "select * from test_mb_100 where event_id in (1, 2, 3, 4, 5)" )
    }

    test("select between") {
        doQuery("test_mb_100", "my_table", "select * from test_mb_100 where event_id between 1 and 5" )
    }

    test("select Not Null") {
        doQuery("test_mb_100", "my_table", "select * from test_mb_100 where event_id between 1 and 5" )
    }

    test("select Nulls") {
        doQuery("basic_info_mix_index", "basic_info_mix_type", "select history_names, foreign_key from basic_info_mix_index where history_names is not null", server = "slave1")
    }

    test("select isnull") {
        doQuery("basic_info_mix_index", "basic_info_mix_type", "select history_names, foreign_key from basic_info_mix_index where history_names is null", server = "slave1")

    }

    //Class.forName("moonbox.catalyst.adapter.elasticsearch5.jdbc.Driver")
    Class.forName("moonbox.catalyst.adapter.jdbc.Driver")

    def doQuery(tbl: String, mtype: String, sql: String, map: Map[String, String]=Map(), server: String="testserver1"): Unit = {
        import scala.collection.JavaConversions._
        var connection: Connection = null
        try {
            val url = s"jdbc:es://${server}:9200/$tbl?table=$mtype"
            //val url = s"jdbc:es://slave1:9200,slave2:9200/$tbl"
            val prop = EsUtilTest.url2Prop(url)

            map.foreach{elem => prop.put(elem._1, elem._2)}  //TODO: must have this line
            connection = DriverManager.getConnection(url, prop)
            val statement: Statement = connection.createStatement()

            val rs: ResultSet = statement.executeQuery(s""" $sql """)
            val fieldSize = statement.getMaxFieldSize + 1
            while (rs.next()) {
                (1 until fieldSize).map{ idx =>
                    val obj = rs.getObject(idx)
                    printObject(obj)

                }
                println("")
            }
            connection.close()
        }catch {
            case e: Any => e.printStackTrace()
        }
        finally {
            if(connection != null) { connection.close() }
        }
    }
}
