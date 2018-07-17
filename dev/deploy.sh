#!/usr/bin/env bash

function clean_previous_package(){
    cd assembly
    cd target
    rm libs -rf
    rm sbin -rf
    rm bin -rf
    rm plugin -rf
    rm runtime -rf
    rm test-classes -rf
    rm maven-archiver -rf
    rm moonbox -rf
}

function release_new_package(){
    tar xf moonbox-assembly_2.11-0.2.0-SNAPSHOT-dist.tar.gz
}



function sync_file_to_server(){
    cd moonbox
    name=$1[@]
    array=("${!name}")

    moonbox_home=$2
    echo $array
    echo $moonbox_home

    for host in "${array[@]}"
    do
        echo "${host}"
        ssh "${host}" "mkdir -p ${moonbox_home}/bin"
        ssh "${host}" "mkdir -p ${moonbox_home}/sbin"
        ssh "${host}" "mkdir -p ${moonbox_home}/plugin"
        ssh "${host}" "mkdir -p ${moonbox_home}/libs"
        ssh "${host}" "mkdir -p ${moonbox_home}/runtime"

        ssh "${host}" "rm -f ${moonbox_home}/bin/*"
        scp bin/* root@${host}:${moonbox_home}/bin/
        ssh "${host}" "rm -f ${moonbox_home}/sbin/*"
        scp sbin/* root@${host}:${moonbox_home}/sbin/
        ssh "${host}" "rm -f ${moonbox_home}/plugin/*"
        scp plugin/* root@${host}:${moonbox_home}/plugin/
        ssh "${host}" "rm -f ${moonbox_home}/libs/*"
        scp libs/* root@${host}:${moonbox_home}/libs/
        ssh "${host}" "rm -f ${moonbox_home}/runtime/*"
        scp runtime/* root@${host}:${moonbox_home}/runtime/
    done
}

host_array=("master" "slave1" "slave2")
moonbox_home="/home/yxgly/moonbox3"


######################################

current_dir=`pwd`
script_dir=$(cd `dirname $0`; pwd)
cd $script_dir
cd ..

clean_previous_package

release_new_package

sync_file_to_server host_array "$moonbox_home"
