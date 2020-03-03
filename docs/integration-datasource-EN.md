---
layout: global
title: Integration DataSource
---

Moonbox supports a variety of data sources. Following are examples of mounting different kinds of data sources.  

#### Mount virtual tables in TYPE 1 database  

- MySQL

```
mount table mysql_test_table options(
    type 'mysql',                           # data source type is MySQL; required field
    url 'jdbc:mysql://host:3306/database',  # url; required field
    dbtable 'table_name' ,                  # table name; required field
    user 'root',                            # user name; required field
    password 'pwd'                          # password; required field
);
```
Note: <br/>
1 If you want to compile source code, you have to add `-Pmysql` as a support for MySQL.<br/>
2 You are recommended to copy MySQL driver into `$MOONBOX_HOME/runtime`. 

- MyCat

```
mount table mycat_test_table options(
    type 'mysql',                           # data source type is MySQL; required field
    url 'jdbc:mysql://host:8066/database',  # url; required field
    dbtable 'table_name' ,                  # table name; required field
    user 'root',                            # user name; required field
    password 'pwd'                          # password; required field
);
```
Note: <br/>
1 If you want to compile source code, you have to add `-Pmysql` as a support for MySQL.<br/>
2 You are recommended to copy MySQL driver into `$MOONBOX_HOME/runtime`. 
   
- SQLServer

```
mount table sqlserver_test_booklist options(
    type 'sqlserver',                                           # data source type is kudu; required field
    url 'jdbc:sqlserver://host:1433;DatabaseName=database',     # url; required field
    dbtable 'table_name' ,                                      # table name; required field
    user 'root',                                                # user name; required field
    password 'pwd'                                              # password; required field
);
```
Note: <br/>
1 If you want to compile source code, you have to add `-Psqlserver` as a support for SQLServer.<br/>
2 You are recommended to copy SQLServer driver into `$MOONBOX_HOME/runtime`. 

- Oracle

```
mount table oracle_test_booklist options(
    type 'oracle',                                  # data source type is oracle; required field
    url 'jdbc:oracle:thin:@host:1521:database',     # url; required field
    dbtable 'table_name' ,                          # table name; required field
    user 'root',                                    # user name; required field
    password 'pwd'                                  # password; required field
);
```
Note: <br/>
1 If you want to compile source code, you have to add `-Poracle` as a support for Oracle.<br/>
2 You are recommended to copy Oracle driver into `$MOONBOX_HOME/runtime`. 

- Mongo

```
mount table mongo_test_booklist options(
    type 'mongo',                                                                                 # data source type is mongo; required field
    spark.mongodb.input.uri 'mongodb://[username:password@]host[:27017]/database.collection', # for reading uri from mongo; required field
    spark.mongodb.output.uri 'mongodb://[username:password@]host[:27017]/database.collection' # for writing uri into mongo; required field 
);
```
Note: <br/>
1 If you want to compile source code, you have to add `-Pmongo` as a support for Mongo.<br/>
2 Please refer to configuration of MongoDB Spark Connector for details.

- Elasticsearch

```
mount table test_es5_100 options(
    type 'es',                                      # data source type is es; required field
    es.nodes 'host',                                # es nodes list; required field
    es.port '9200',                                 # es port; required field
    es.resource 'indexName/typeName',               # index and type; required field
    es.net.http.auth.user "username",               # User name; optional field
    es.net.http.auth.pass "password"                # password; required field
);
```
Note: <br/>
1 If you want to compile source code, you have to add `-Pes` as a support for Elasticsearch.<br/>
2 Only Elasticsearch Version 5.3 has been tested.

- Presto

```
mount table presto_test_booklist options(
    type 'presto',                                  # data source type is presto; required field
    url 'jdbc:presto://host:8181/mysql/database',   # preseto url; required field
    dbtable 'table_name' ,                          # table name; required field
    user 'root'                                     # user name; required field
);
```
Note: <br/>
1 If you want to compile source code, you have to add `-Ppresto` as a support for Presto.<br/>

- Kudu

```
mount table kudu_test_booklist options(
    type 'kudu',                                    # data source type is kudu; required field
    kudu.master 'host:7051',                        # kudu master address and port; required field
    kudu.table 'table_name'                         # table name; required field
);
```
Note: <br/>
1 If you want to compile source code, you have to add `-Pkudu` as a support for Kudu.

- HBase

```
mount table hbase_test_booklist options(
    type 'hbase',                                       # data source type is hbase; required field
    'hbase.spark.use.hbasecontext' 'false',             # false; required field
    hbase.zookeeper.quorum 'host',                      # zk address list; required field
    catalog '{"table":{"namespace":"default", "name":"table_name"},"rowkey":"key","columns":{"row":{"cf":"rowkey", "col":"key", "type":"string"},"column0":{"cf":"cf1", "col":"column0", "type":"string"},"column1":{"cf":"cf1", "col":"column1", "type":"string"},"column2":{"cf":"cf1", "col":"column2", "type":"string"}}}'   # namespace, rowkey, columns, etc. in hbase; required field
);
```
Note: <br/>
1 If you want to compile source code, you have to add `-Phbase` as a support for HBase.

- Cassandra

```
mount table cass_test_booklist options(
    type 'cassandra',                                       # data source type is cassandra; required field
    spark.cassandra.connection.host 'master,slave1,slave2', # cassandra address; required field
    keyspace 'default',                                     # keyspace information; required field
    table 'table_name'                                      # table name; required field
);
```
Note: <br/>
1 If you want to compile source code, you have to add `-Pcassandra` as a support for Cassandra.

- Hive

```
mount table hive_test_booklist options(
    type 'hive',                                        # data source type is hive; required field
    metastore.url 'jdbc:mysql://host:3306/database',    # metastore url; required field
    metastore.driver 'com.mysql.jdbc.Driver',           # metastore driver; required field
    hivedb 'default',                                   # database name in hive; required field
    hivetable 'table_name'                              # table name in hive; required field
    metastore.user 'root',                              # metastore user name; required field
    metastore.password 'pwd'                            # metastore password; required field
);
```
Note: <br/>
1 If Hive metadata is stored in MySQL, you should copy MySQL driver into `$MOONBOX_HOME/runtime`. 


#### Mount TYPE 2 Database

- MySQL

```
mount database mysql_test_test options(
    type 'mysql',                               # data source type is mysql; required field
    url 'jdbc:mysql://host:3306/database',      # url; required field
    user 'root',                                # user name; required field
    password 'pwd'                              # password; required field
);
```
Note: <br/>
1 If you want to compile source code, you have to add `-Pmysql` as a support for MySQL.<br/>
2 You are recommended to copy MySQL driver into `$MOONBOX_HOME/runtime`. 

- MyCat

```
mount database mycat_test_testdb options(
    type 'mysql',                                       # data source type is mysql; required field
    url 'jdbc:mysql://localhost:8066/database',            # url; required field
    user 'root',                                        # user name; required field
    password 'pwd'                                      # password; required field
);
```
Note: <br/>
1 If you want to compile source code, you have to add `-Pmysql` as a support for MySQL.<br/>
2 You are recommended to copy MySQL driver into `$MOONBOX_HOME/runtime`. 

- SQLServer

```
mount table sqlserver_test_booklist options(
    type 'sqlserver',                                           # data source type is kudu; required field
    url 'jdbc:sqlserver://host:1433;DatabaseName=database',     # url; required field
    user 'root',                                                # user name; required field
    password 'pwd'                                              # password; required field
);
```
Note: <br/>
1 If you want to compile source code, you have to add `-Psqlserver` as a support for SQLServer.<br/>
2 You are recommended to copy SQLServer driver into `$MOONBOX_HOME/runtime`. 

- Oracle

```
mount database oracle_test_orcl options(
    type 'oracle',                              # data source type is oracle; required field
    url 'jdbc:oracle:thin:@host:1521:database', # url; required field
    user 'root',                                # user name; required field
    password 'pwd'                              # password; required field
);
```
Note: <br/>
1 If you want to compile source code, you have to add `-Poracle` as a support for Oracle.<br/>
2 You are recommended to copy Oracle driver into `$MOONBOX_HOME/runtime`. 

- Cassandra

```
mount database cass_test_default options(
    type 'cassandra',                                        # data source type is cassandra; required field
    spark.cassandra.connection.host 'master,slave1,slave2',  # cassandra host list; required field
    keyspace 'default'                                       # keyspace name; required field
);
```
Note: <br/>
1 If you want to compile source code, you have to add `-Pcassandra` as a support for Cassandra.

- Elasticsearch

```
mount database es5_test_default options(
    type 'es',                                          # data source type is es; required field
    es.nodes 'master,slave1,slave2',                    # host list; required field
    es.port '9200',                                     # port; required field
    es.resource 'index_name'                            # index name; required field
);
```
Note: <br/>
1 If you want to compile source code, you have to add `-Pes` as a support for Elasticsearch.<br/>
2 Only Elasticsearch Version 5.3 has been tested.

- Kudu

```
mount database kudu_test_default options(
    type 'kudu',                                        # data source type is kudu; required field
    kudu.master 'master:7051'                           # kudu master list; required field
);
```
Note: <br/>
1 If you want to compile source code, you have to add `-Pkudu` as a support for Kudu.

- Hive

```
mount database hive_test_default options(
    type 'hive',                                        # data source type is hive; required field
    metastore.url 'jdbc:mysql://host:3306/database',    # metastore url; required field
    metastore.driver 'com.mysql.jdbc.Driver',           # metastore driver; required field
    hivedb 'default',                                   # database name in hive; required field
    metastore.user 'root',                              # metastore user name; required field
    metastore.password 'pwd'                            # metastore password; required field
);
```
Note: <br/>
1 If Hive metadata is stored in MySQL, you should copy MySQL driver into `$MOONBOX_HOME/runtime`. 

- Presto

```
mount database presto_test_test options(
    type 'presto',                                          # data source type is presto; required field
    url 'jdbc:presto://master:8181/mysql/database',         # password; required field
    user 'root'                                             # user name; required field
);
```
Note: <br/>
1 If you want to compile source code, you have to add `-Ppresto` as a support for Presto.<br/>

- Mongo

```
mount database mongo_test_test options(
    type 'mongo',                                               # data source type is mongo; required field
    spark.mongodb.input.uri 'mongodb://host:27017/database',    # for reading uri from mongo; required field
    spark.mongodb.output.uri 'mongodb://host:27017/database'    # for writing uri from mongo; required field
);
```
Note: <br/>
1 If you want to compile source code, you have to add `-Pmongo` as a support for Mongo.<br/>
2 Please refer to configuration of MongoDB Spark Connector for details.

#### Notices
- For HBase:
    + Not support 'mount database'
