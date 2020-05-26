---
layout: global
title: Integration DataSource
---

Moonbox支持多种数据源,以下为挂载各种类型的数据源的示例。所有配置参数仅仅列出了必需参数，其他优化相关的参数请参阅对应的spark datasource connector参数。

#### 在TYPE 1类型数据库中挂载虚拟表。

- MySQL

```
mount table mysql_test_table options(
    type 'mysql',                           # 数据源类型，必填，为mysql
    url 'jdbc:mysql://host:3306/database',  # url，必填
    dbtable 'table_name' ,                  # 表名，必填
    user 'root',                            # 用户名 ，必填
    password 'pwd'                          # 密码 ，必填
);
```
Note: <br/>
1 如果自己编译源码需要添加-Pmysql选项增加MySQL支持<br/>
2 需要将MySQL驱动拷贝到$MOONBOX_HOME/runtime中

- MyCat

```
mount table mycat_test_table options(
    type 'mysql',                           # 类型，必填，为mysql
    url 'jdbc:mysql://host:8066/database',  # url, 必填
    dbtable 'table_name' ,                  # 表名，必填
    user 'root',                            # 用户名 ，必填
    password 'pwd'                          # 密码 ，必填
);
```
Note: <br/>
1 如果自己编译源码需要添加-Pmysql选项增加MySQL支持<br/>
2 需要将MySQL驱动拷贝到$MOONBOX_HOME/runtime中
   
- SqlServer

```
mount database sqlserver_test_booklist options(
    type 'sqlserver',                                           # 类型，必填，为sqlserver
    url 'jdbc:sqlserver://host:1433;DatabaseName=database',     # url，必填，
    dbtable 'table_name' ,                                      # 表名，必填
    user 'root',                                                # 用户名 ，必填
    password 'pwd'                                              # 密码 ，必填
);
```
Note: <br/>
1 如果自己编译源码需要添加-Psqlserver选项增加SqlServer支持<br/>
2 需要将SqlServer驱动拷贝到$MOONBOX_HOME/runtime中

- Oracle

```
mount table oracle_test_booklist options(
    type 'oracle',                                  # 类型，必填，为oracle
    url 'jdbc:oracle:thin:@host:1521:database',     # url，必填
    dbtable 'table_name' ,                          # 表名，必填
    user 'root',                                    # 用户名 ，必填
    password 'pwd'                                  # 密码 ，必填
);
```
Note: <br/>
1 如果自己编译源码需要添加-Poracle选项增加Oracle支持<br/>
2 需要将Oracle驱动拷贝到$MOONBOX_HOME/runtime中

- Mongo

```
mount table mongo_test_booklist options(
    type 'mongo',                                                                                 # 类型，必填，为mongo
    spark.mongodb.input.uri 'mongodb://[username:password@]host[:27017]/database.collection', # mongo读uri，如果目的为读，必填
    spark.mongodb.output.uri 'mongodb://[username:password@]host[:27017]/database.collection' # mongo写uri，如果目的为写，必填 
);
```
Note: <br/>
1 如果自己编译源码需要添加-Pmongo选项增加Mongo支持<br/>
2 更多配置参考MongoDB Spark Connector配置

- Elasticsearch

```
mount table test_es5_100 options(
    type 'es',                                      # 类型，必填，为es
    es.nodes 'host',                                # es nodes 列表，必填
    es.port '9200',                                 # es端口，必填
    es.resource 'indexName/typeName',               # index和type，必填
    es.net.http.auth.user "username",               # 用户名 ，可选
    es.net.http.auth.pass "password"                # 密码 ，必填
);
```
Note: <br/>
1 如果自己编译源码需要添加-Pes选项增加Elasticsearch支持
2 仅测试过5.3版本, 其他版本可能会有问题。

- Presto

```
mount table presto_test_booklist options(
    type 'presto',                                  # 类型，必填，为presto
    url 'jdbc:presto://host:8181/mysql/database',   # preseto url，必填，
    dbtable 'table_name' ,                          # 表，必填
    user 'root'                                     # 用户名 ，必填
);
```
Note: <br/>
1 如果自己编译源码需要添加-Ppresto选项增加Presto支持<br/>

- Kudu

```
mount table kudu_test_booklist options(
    type 'kudu',                                    # 类型，必填，为kudu
    kudu.master 'host:7051',                        # kudu master 地址和端口，必填
    kudu.table 'table_name'                         # 表，必填
);
```
Note: <br/>
1 如果自己编译源码需要添加-Pkudu选项增加Kudu支持

- HBase

```
mount table hbase_test_booklist options(
    type 'hbase',                                       # 类型，必填，为hbase
    'hbase.spark.use.hbasecontext' 'false',             # 必填，为false
    hbase.zookeeper.quorum 'host',                      # zk地址列表，必填
    catalog '{"table":{"namespace":"default", "name":"table_name"},"rowkey":"key","columns":{"row":{"cf":"rowkey", "col":"key", "type":"string"},"column0":{"cf":"cf1", "col":"column0", "type":"string"},"column1":{"cf":"cf1", "col":"column1", "type":"string"},"column2":{"cf":"cf1", "col":"column2", "type":"string"}}}'   # hbase中namespace ， rowkey，columns等，必填
);
```
Note: <br/>
1 如果自己编译源码需要添加-Phbase选项增加HBase支持

- Cassandra

```
mount table cass_test_booklist options(
    type 'cassandra',                                       # 类型，必填，为cassandra
    spark.cassandra.connection.host 'master,slave1,slave2', # cassandra 连接信息，必填
    keyspace 'default',                                     # keyspace信息，必填
    table 'table_name'                                      # 表，必填
);
```
Note: <br/>
1 如果自己编译源码需要添加-Pcassandra选项增加Cassandra支持

- Hive

```
mount table hive_test_booklist options(
    type 'hive',                                        # 类型，必填，为hive
    metastore.url 'jdbc:mysql://host:3306/database',    # metastore的url地址，必填
    metastore.driver 'com.mysql.jdbc.Driver',           # metastore driver，必填
    hivedb 'default',                                   # hive的database名，必填
    hivetable 'table_name'                              # hive的表名, 必填
    metastore.user 'root',                              # metastore 用户名，必填
    metastore.password 'pwd'                            # metastore 密码，必填
);
```
Note: <br/>
1 如果Hive元数据使用MySQL存储,需要将MySQL驱动拷贝到$MOONBOX_HOME/runtime中


#### 挂载TYPE 2类型数据库

- MySQL

```
mount database mysql_test_test options(
    type 'mysql',                               # 类型，必填，为mysql
    url 'jdbc:mysql://host:3306/database',      # url，必填
    user 'root',                                # 用户名，必填
    password 'pwd'                              # 密码，必填
);
```
Note: <br/>
1 如果自己编译源码需要添加-Pmysql选项增加MySQL支持<br/>
2 需要将MySQL驱动拷贝到$MOONBOX_HOME/runtime中

- MyCat

```
mount database mycat_test_testdb options(
    type 'mysql',                                       # 类型，必填，为mysql
    url 'jdbc:mysql://localhost:8066/database',            # url，必填
    user 'root',                                        # 用户名，必填
    password 'pwd'                                      # 密码，必填
);
```
Note: <br/>
1 如果自己编译源码需要添加-Pmysql选项增加MySQL支持<br/>
2 需要将MySQL驱动拷贝到$MOONBOX_HOME/runtime中

- SqlServer

```
mount table sqlserver_test_booklist options(
    type 'sqlserver',                                           # 类型，必填，为kudu
    url 'jdbc:sqlserver://host:1433;DatabaseName=database',     # url，必填，
    user 'root',                                                # 用户名 ，必填
    password 'pwd'                                              # 密码 ，必填
);
```
Note: <br/>
1 如果自己编译源码需要添加-Psqlserver选项增加SqlServer支持<br/>
2 需要将SqlServer驱动拷贝到$MOONBOX_HOME/runtime中

- Oracle

```
mount database oracle_test_orcl options(
    type 'oracle',                              # 类型，必填，为oracle
    url 'jdbc:oracle:thin:@host:1521:database', # url，必填
    user 'root',                                #  用户名，必填
    password 'pwd'                              # 密码，必填
);
```
Note: <br/>
1 如果自己编译源码需要添加-Poracle选项增加Oracle支持<br/>
2 需要将Oracle驱动拷贝到$MOONBOX_HOME/runtime中

- Cassandra

```
mount database cass_test_default options(
    type 'cassandra',                                        # 类型，必填，为cassandra
    spark.cassandra.connection.host 'master,slave1,slave2',  # cassandra host列表，必填
    keyspace 'default'                                       # keyspace名，必填
);
```
Note: <br/>
1 如果自己编译源码需要添加-Pcassandra选项增加Cassandra支持

- Elasticsearch

```
mount database es5_test_default options(
    type 'es',                                          # 类型，必填，为es
    es.nodes 'master,slave1,slave2',                    # host列表，必填
    es.port '9200',                                     # port，必填
    es.resource 'index_name'                            # index名字，必填
);
```
Note: <br/>
1 如果自己编译源码需要添加-Pes选项增加Elasticsearch支持
2 仅测试过5.3版本, 其他版本可能会有问题。

- Kudu

```
mount database kudu_test_default options(
    type 'kudu',                                        # 类型，必填，为kudu
    kudu.master 'master:7051'                           # kudu master列表，必填
);
```
Note: <br/>
1 如果自己编译源码需要添加-Pkudu选项增加Kudu支持

- Hive

```
mount database hive_test_default options(
    type 'hive',                                        # 类型，必填，为hive
    metastore.url 'jdbc:mysql://host:3306/database',    # metastore的url地址，必填
    metastore.driver 'com.mysql.jdbc.Driver',           # metastore driver，必填
    hivedb 'default',                                   # hive的database名，必填
    metastore.user 'root',                              # metastore 用户名，必填
    metastore.password 'pwd'                            # metastore 密码，必填
);
```
Note: <br/>
1 如果Hive元数据使用MySQL存储,需要将MySQL驱动拷贝到$MOONBOX_HOME/runtime中

- Presto

```
mount database presto_test_test options(
    type 'presto',                                          # 类型，必填，为presto
    url 'jdbc:presto://master:8181/mysql/database',         # 密码，必填
    user 'root'                                             # 用户名，必填
);
```
Note: <br/>
1 如果自己编译源码需要添加-Ppresto选项增加Presto支持<br/>

- Mongo

```
mount database mongo_test_test options(
    type 'mongo',                                               # 类型，必填，为mongo
    spark.mongodb.input.uri 'mongodb://host:27017/database',    # 读url，如果目的为读，必填
    spark.mongodb.output.uri 'mongodb://host:27017/database'    # 写url，如果目的为写，必填
);
```
Note: <br/>
1 如果自己编译源码需要添加-Pmongo选项增加Mongo支持<br/>
2 更多配置请参考MongoDB Spark Connector配置

#### Notices
- For HBase:
    + Not support 'mount database'
