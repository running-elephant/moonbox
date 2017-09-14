edp-moonbox
===========

set dev environment
-------------------
Prepare 3 machines at least. In every machine, make sure spark2.0 is installed, AND   
1. add export MOONBOX_HOME=/path/to/project/root in /ect/profile
2. reboot or source /etc/profile
3. edit nodes.template, change the name to nodes 
4. edit moonbox-default.conf.template, change the name to moonbox-default.conf
5. start spark in standalone cluster 
6. cd $MOONBOX_HOME/sbin, run start-all.sh to start moonbox


how to make a dist package
--------------------------

1. run build.sh in dev
package is in edp-moonbox/assembly/target/ directory

2. build a jar for hbase  
cd edp-moonbox/external/spark-hbase-connector/
mvn clean package -DskipTests -Phbase
copy the package to HBASE lib

3. download and copy mysql jdbc driver (for example: mysql-connector-java-5.1.38-bin.jar ) to ${MOONBOX_HOME}/lib in test environment
       


how to generate THIRD-PARTY.txt?
------
mvn clean install -DskipTests

mvn license:aggregate-add-third-party


how to use
--------------------------
####mysql:
mount table aaa options(type'mysql', url 'jdbc:mysql://host:port/test', dbname 'test', dbtable 'test1_10w', user 'root', password 'xxx', driver 'com.mysql.jdbc.Driver')
mount table bbb options(type'mysql', url 'jdbc:mysql://host:port/test', dbname 'test', dbtable 'test2_10w', user 'root', password 'xxx', driver 'com.mysql.jdbc.Driver')
select * from aaa inner join bbb on aaa.outer_key = bbb.index_key limit 1

####hbase 1.1.2:
######prepare:
    cd moonbox/external/spark-hbase-connector && 
    mvn package -DskipTests -Phbase &&
    cp ./target/spark-hbase-connector-1.0-SNAPSHOT.jar path/to/HBASE_HOME/lib
mount table eee options(type 'hbase', hbase.zookeeper.quorum 'host', catalog '{"table":{"namespace":"default", "name":"test1000"},"rowkey":"key","columns":{"row":{"cf":"rowkey", "col":"key", "type":"string"},"male":{"cf":"family", "col":"male", "type":"boolean"},"address":{"cf":"family", "col":"address", "type":"string"},"height":{"cf":"family", "col":"height", "type":"float"},"score":{"cf":"family", "col":"score", "type":"double"},"salary":{"cf":"family", "col":"salary", "type":"long"}}}')
select * from eee;

####es 5.3.2:
mount table q5 options(type 'es', coordinates 'host:port', index 'test_mb_1000', dbtable 'my_table', cluster.name 'cluster-name')
select * from q5

#####parquet:
mount table par options(type 'parquet', path 'hdfs://host:port/test/helloworld.parquet')
select * from par

---------------------
#####BATCH REST API:
send query for mysql:
curl -XPOST host:port/sql/batch/query -d '{"sqlList": ["mount table aaa options(type \"mysql\", url \"jdbc:mysql://host:port/test\",user \"root\", password \"xxx\", dbname \"test\", dbtable \"book_list\", driver \"com.mysql.jdbc.Driver\")", "select * from aaa"]}'
send query for es5:
curl -XPOST host:port/sql/batch/query -d '{"sqlList": ["mount table bbb options(type \"es\", coordinates \"host:port\", index \"test_mb_1000\", dbtable \"my_table\", cluster.name \"edp-es\")", "select event_id from bbb"]}'
send query for hbase:
curl -XPOST host:port/sql/batch/query -d '{ "sqlList": [ "mount table hhh options(type \"hbase\", hbase.zookeeper.quorum \"zkhost\", catalog \"{ \\\"rowkey\\\": \\\"key\\\", \\\"columns\\\": { \\\"row\\\": { \\\"cf\\\": \\\"rowkey\\\", \\\"col\\\": \\\"key\\\", \\\"type\\\": \\\"string\\\" } }, \\\"table\\\": { \\\"namespace\\\": \\\"default\\\", \\\"name\\\": \\\"test1000\\\" } }\" )" , "select * from hhh" ] }'

get progress:
curl -XPOST host:port/sql/batch/progress -d '{"jobId": "xxx"}'

get result:
curl -XPOST host:port/sql/batch/result -d '{"jobId": "xxx", "offset": 0, "size": 10000}'

#####ADHOC REST API:
curl -XPOST host:port/sql/adhoc/query -d '{"sqlList": ["mount table aaa options(type \"mysql\", url \"jdbc:mysql://host:port/test\",user \"root\", password \"xxx\", dbname \"test\", dbtable \"book_list\", driver \"com.mysql.jdbc.Driver\")", "select * from aaa"]}'
curl -XPOST host:port/sql/adhoc/progress -d '{"jobId": "xxx"}'
curl -XPOST host:port/sql/adhoc/result -d '{"jobId": "xxx", "offset"：0, "size": 10000}'

---------------------
#####Calcite ES5 Notice:
Support:
Select, Where, Order By, Group By, AND & OR, Like, Between, DISTINCT, IN
count(), avg(), max(), min(), sum()
scroll select

For example:
1. select event_id, col_int_a from my_table where col_int_a between 1 AND 5
2. select col_time_b from my_table where col_time_b>'2017-12-21T23:00:33Z'
3. select event_id from my_table order by event_id DESC limit 12
4. select event_id from my_table where col_str_k like 'i*'
5. select max(height) from employee group by gender, haschild
6. select count(*) from my_table where event_id >= 500  and event_id <= 700
7. select max(event_id), min(col_long_c) from my_table group by col_long_a
8. select c from ( select max(age) c, avg(age) d from employee  group by gender ) abc where c < 50


######Limitation:
1. Not support : select col + 1 from tbl
2. Not support : subquery
3. Not support : join
4. Not support : union
5. in ES5, date type has three type, it can either be:                                
strings containing formatted dates, e.g. "2015-01-01" or "2015/01/01 12:10:30".
a long number representing milliseconds-since-the-epoch.
an integer representing seconds-since-the-epoch.
ONLY string is supported.
6. Aggregation result size is 10000000 

