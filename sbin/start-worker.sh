#!/bin/env bash
dir=`pwd`
echo "start worker in $dir ... $#"

if [ $# -ne 3 ]; then
    echo "not input host and port , use default configuration"
    local_host=`hostname`

    tmp_file="/tmp/master.txt"
    cat "${MOONBOX_HOME}/conf/nodes" | grep -v '^#' | grep -w master | awk '{print $2}'  | sed 's/grid:\/\///g'  > ${tmp_file}

    master_url=""
    while read line
    do
        master_url="${master_url},${line}"
    done < ${tmp_file}
    master_url="grid://"`echo $master_url | cut -d ',' -f 2-`

    rm -f ${tmp_file}

    akka_port=`cat "${MOONBOX_HOME}/conf/nodes" | grep -v '^#' | grep $local_host | awk '{print $2}' | cut -d '/' -f 3 | awk '{print $1}' | grep ':' |cut -d ':' -f 2`
    if [ -z "$akka_port" ];then
        echo "start worker with --port $akka_ip with random port "
        java -cp ${MOONBOX_HOME}/lib/*:${MOONBOX_HOME}/lib/moonbox-common_2.11-0.1.0-SNAPSHOT.jar:${MOONBOX_HOME}/lib/moonbox-core_2.11-0.1.0-SNAPSHOT.jar:${MOONBOX_HOME}/lib/moonbox-grid_2.11-0.1.0-SNAPSHOT.jar edp.moonbox.grid.worker.MbWorker --host ${local_host}  --masters ${master_url}  1>${MOONBOX_HOME}/log/"worker.log" 2>&1 &
    else
        echo "start worker with --host $akka_ip  --port $akka_port"
        java -cp ${MOONBOX_HOME}/lib/*:${MOONBOX_HOME}/lib/moonbox-common_2.11-0.1.0-SNAPSHOT.jar:${MOONBOX_HOME}/lib/moonbox-core_2.11-0.1.0-SNAPSHOT.jar:${MOONBOX_HOME}/lib/moonbox-grid_2.11-0.1.0-SNAPSHOT.jar edp.moonbox.grid.worker.MbWorker --host ${local_host} --port ${akka_port} --masters ${master_url} 1>${MOONBOX_HOME}/log/"worker.log" 2>&1 &
    fi
else
    echo "start worker with --host $1  --port $2"
    java -cp ${MOONBOX_HOME}/lib/*:${MOONBOX_HOME}/lib/moonbox-common_2.11-0.1.0-SNAPSHOT.jar:${MOONBOX_HOME}/lib/moonbox-core_2.11-0.1.0-SNAPSHOT.jar:${MOONBOX_HOME}/lib/moonbox-grid_2.11-0.1.0-SNAPSHOT.jar edp.moonbox.grid.worker.MbWorker --host ${1} --port ${2} --masters ${3}  1>${MOONBOX_HOME}/log/"worker.log" 2>&1 &
fi

if [ $? -eq 0 ]; then
   echo "successfully"
else
   echo "failed"
fi


