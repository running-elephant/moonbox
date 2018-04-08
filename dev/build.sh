#!/usr/bin/env bash

current_dir=`pwd`
script_dir=$(cd `dirname $0`; pwd)
cd $script_dir
cd ..
(mvn install:install-file -DgroupId=edp.moonbox -DartifactId=moonbox-parent_2.11 -Dversion=0.2.0-SNAPSHOT -Dpackaging=pom -Dfile=./pom.xml)
(cd ./external/spark-hbase-connector && mvn clean install -DskipTests)
(mvn clean install -Pdist -DskipTests)
cd $current_dir
