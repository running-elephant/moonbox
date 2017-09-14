#!/usr/bin/env bash

current_dir=`pwd`
script_dir=$(cd `dirname $0`; pwd)
cd $script_dir
cd ..
(cd ./external/calcite && mvn install -DskipTests)
(cd ./external/calcite-elasticsearch5-adapter && mvn install -DskipTests)
(cd ./external/spark-hbase-connector && mvn install -DskipTests -Pspark)
(mvn package -Pdist -DskipTests)
cd $current_dir