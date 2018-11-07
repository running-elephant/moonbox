#!/usr/bin/env bash

current_dir=`pwd`
script_dir=$(cd `dirname $0`; pwd)
cd $script_dir
cd ..
###please add your own extra modules
(mvn clean package -Pdist -Pmysql -Poracle -Psqlserver -Pkudu -Phbase -Pes -Ppresto -Pcassandra -Pmongo -Pclickhouse -DskipTests)
cd $current_dir
