#!/usr/bin/env bash

moonbox_home_dir="$(cd "`dirname "$0"`/.."; pwd)"
if [ -f "${moonbox_home_dir}/conf/moonbox-env.sh" ]; then
  . "${moonbox_home_dir}/conf/moonbox-env.sh"
fi

JAVA="java"
JAVA_OPTS="${MOONBOX_HOME}/libs/moonbox-repl_2.11-0.3.0-SNAPSHOT.jar"

case $1 in
cluster)
    CLASS="moonbox.tool.Cluster"
    shift
    ;;
node)
    CLASS="moonbox.tool.Node"
    shift
    ;;
sql)
    CLASS="moonbox.repl.Main"
    shift
    ;;
--help)
    echo "Usage: moonbox cluster | node | sql )"
    exit 0
    ;;
*)
    echo "Error: no param, please use --help"
    exit 0
    ;;
esac

"$JAVA" -cp "$JAVA_OPTS" "$CLASS" "$@"

#some command example as follow:
#----add yarn app----
#>moonbox.sh node -c '{"implementation":"spark", "name": "test-bbb", "spark.master": "yarn", "spark.submit.deployMode": "cluster", "spark.yarn.resourcemanager.address" :"master:8032", "spark.yarn.resourcemanager.hostname": "master", "spark.yarn.access.namenodes": "hdfs://master:8020", "spark.loglevel": "INFO", "spark.app.name": "test1", "spark.cores.max": 1 ,"spark.yarn.am.memory": "64m", "spark.yarn.am.cores": 1, "spark.executor.instances":1,  "spark.executor.cores": 1, "spark.executor.memory": "512m", "pushdown.enable":true  }' -a
#<app-20181113104428-00001_test-bbb_adhoc
#
#----list yarn app----
#>moonbox.sh node -c '{"yarn.resourcemanager.address":"master:8032","yarn.resourcemanager.hostname":"master"}'  -l
#<+------------------------------+--------------------------------------+--------+-------------------+-------------------+
#|appid                         |name                                  |progress|starttime          |endtime            |
#+------------------------------+--------------------------------------+--------+-------------------+-------------------+
#|application_1541992362560_0003|app-20181112164750-00000_yarnapp_adhoc|1.0     |2018-11-12 16:48:07|2018-11-12 17:26:58|
#|application_1541992362560_0004|app-20181112172727-00000_yarnapp_adhoc|0.1     |2018-11-12 17:27:43|1970-01-01 08:00:00|
#+------------------------------+--------------------------------------+--------+-------------------+-------------------+
#
#----show databases----
#>moonbox.sh node  -u sally -p 111111 -st
#<data:{
#  "databases" : [ {
#    "name" : "default",
#    "isLogical" : true,
#    "properties" : { },
#    "description" : ""
#  }, {
#    "name" : "oracle_test_orcl",
#    "isLogical" : false,
#    "properties" : {
#      "url" : "jdbc:oracle:thin:@host:1521:orcl",
#      "type" : "oracle",
#      "user" : "xiaoming"
#    },
#    "description" : ""
#  }
#}
#
#----show tables----
#>moonbox.sh node  -u sally -p 111111 -d ck5 -st
#<data:{
#  "tables" : [ "user", "province" ]
#}
#
#----describe table----
#>moonbox.sh node  -u sally -p 111111 -d ck5 -t user -dt
#<data:{
#  "table" : {
#    "name" : "user",
#    "properties" : {
#      "url" : "jdbc:clickhouse://ck1:8123/moonbox",
#      "dbtable" : "user", "type" : "clickhouse","user" : "default"},
#      "columns" : [ {"id" : "int"}, {"name" : "string" }, {"age" : "int"}, {"prov_id" : "int"} ],
#      "description" : "" } }
#
#----show all nodes-----
#>moonbox.sh cluster --shownodesinfo
#<+-------------------------------+------+----+-----+-----+----------+---------+---------+----------+-------------------+
#|id                             |host  |port|jdbc |rest |local_core|local_mem|yarn_core|yarn_mem  |heart beat         |
#+-------------------------------+------+----+-----+-----+----------+---------+---------+----------+-------------------+
#|node-20181112164746-slave2-2551|slave2|2551|10010|18090|8         |420317608|0        |0         |1970-01-01 08:00:00|
#|node-20181112172719-master-2551|master|2551|10010|18090|8         |454195536|0        |0         |1970-01-01 08:00:00|
#|node-20181112202130-slave1-2551|slave1|2551|10010|18090|8         |103239824|2        |1152280164|1970-01-01 08:00:00|
#+-------------------------------+------+----+-----+-----+----------+---------+---------+----------+-------------------+
#
#----show running events-----
#>moonbox.sh cluster --showrunningevents
#<+-----+----+----+------+-----+---+----+----+
#|group|name|desc|status|start|end|prev|next|
#+-----+----+----+------+-----+---+----+----+
#
#----show node jobs-----
#>moonbox.sh cluster --shownodejobs
#<+-----+----+-------+---+------+---------+-----------+-----------+
#|jobid|type|command|seq|status|submit by|submit time|update time|
#+-----+----+-------+---+------+---------+-----------+-----------+
#
#-----show cluster jobs-----
#>moonbox.sh cluster --showclusterjobs
#<+-----+----+-------+------+---------+-----------+-----------+--------------+
#|jobid|type|command|status|submit by|submit time|update time|running worker|
#+-----+----+-------+------+---------+-----------+-----------+--------------+
#



