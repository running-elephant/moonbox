#!/bin/env bash

local_host=`hostname`
date=`date +%Y%m%d_%H%M%S`

#import env firstly
if [ -z "${MOONBOX_HOME}" ]; then
    moonbox_home_dir="$(cd "`dirname "$0"`/.."; pwd)"
    if [ -f "${moonbox_home_dir}/conf/moonbox-env.sh" ]; then
      . "${moonbox_home_dir}/conf/moonbox-env.sh"
    fi
    if [[ $? != 0 ]]; then
        exit 1
    fi
fi

echo "start worker in $MOONBOX_HOME ... $#"


if [ $# -ne 3 ]; then

    tmp_file="/tmp/master.txt"
    cat "${MOONBOX_HOME}/conf/nodes" | grep -v '^#' | grep -w "moonbox.grid.master" | awk '{print $2}'  | sed 's/grid:\/\///g'  > ${tmp_file}

    master_url=""
    while read line
    do
        master_url="${master_url},${line}"
    done < ${tmp_file}
    master_url="grid://"`echo $master_url | cut -d ',' -f 2-`

    rm -f ${tmp_file}

    echo "start MbWorker --host ${local_host} --masters ${master_url} --port [random port]"
    java ${WORKER_JAVA_OPTS} -cp ${MOONBOX_HOME}/libs/*:${MOONBOX_HOME}/libs/moonbox-common_2.11-0.2.0-SNAPSHOT.jar:${MOONBOX_HOME}/libs/moonbox-core_2.11-0.2.0-SNAPSHOT.jar:${MOONBOX_HOME}/libs/moonbox-grid_2.11-0.2.0-SNAPSHOT.jar moonbox.grid.deploy.worker.MbWorker --host ${local_host}  --masters ${master_url}  1>${MOONBOX_HOME}/log/"worker-$USER-$date.log" 2>&1 &

else
    echo "start MbWorker --host ${1} --port ${2} --masters ${3} "
    java ${WORKER_JAVA_OPTS} -cp ${MOONBOX_HOME}/libs/*:${MOONBOX_HOME}/libs/moonbox-common_2.11-0.2.0-SNAPSHOT.jar:${MOONBOX_HOME}/libs/moonbox-core_2.11-0.2.0-SNAPSHOT.jar:${MOONBOX_HOME}/libs/moonbox-grid_2.11-0.2.0-SNAPSHOT.jar moonbox.grid.deploy.worker.MbWorker --host ${1} --port ${2} --masters ${3}  1>${MOONBOX_HOME}/log/"worker-$USER-$date.log" 2>&1 &
fi

if [ $? -eq 0 ]; then
   echo "successfully"
else
   echo "failed"
fi

sleep 2


ps -fe|grep 'MbWorker' |grep -v grep 1>/dev/null 2>&1
if [ $? -ne 0 ]
then
    echo -e "\033[31m!!!NO MbWorker is running.....\033[0m"
else
    echo -e "\033[32mMbWorker [$local_host] is running.....\033[0m"
fi



