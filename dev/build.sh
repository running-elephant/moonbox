#!/usr/bin/env bash

current_dir=`pwd`
script_dir=$(cd `dirname $0`; pwd)
cd $script_dir
cd ..
###please add your own extra modules
(
mvn clean install package -Pdist -DskipTests \
-Pmysql \
-Poracle \
-Pimpala \
-Psqlserver \
-Pkudu \
-Phbase \
-Pes \
-Ppresto \
-Pcassandra \
-Pmongo \
-Pclickhouse \
-Pkafka \
-Predis \
-Ppostgres \
)
cd $current_dir
