#!/usr/bin/env bash

current_dir=`pwd`
script_dir=$(cd `dirname $0`; pwd)
cd $script_dir
cd ..
(mvn clean package -Pdist -DskipTests)
cd $current_dir
