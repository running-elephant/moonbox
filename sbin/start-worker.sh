#!/usr/bin/env bash


#import env firstly
moonbox_home_dir="$(cd "`dirname "$0"`/.."; pwd)"
if [ -f "${moonbox_home_dir}/conf/moonbox-env.sh" ]; then
  . "${moonbox_home_dir}/conf/moonbox-env.sh"
fi

echo "start worker in $MOONBOX_HOME with param($@)"

local_host=`hostname`
date=`date +%Y%m%d_%H%M%S`

if [ $# -ne 3 ]; then

    master_lines=`cat "${MOONBOX_HOME}/conf/nodes" | grep -v '^#' | grep -w "moonbox.grid.master" | awk '{print $2}' | sed 's/grid:\/\///g' | sed 's/[[:space:]]//g'`
    master_url="grid://"`echo $master_lines | sed 's/[[:space:]]/,/g'`

    echo "start MbWorker --host ${local_host} --masters ${master_url} --port [random port]"
    java ${WORKER_JAVA_OPTS} -cp "${MOONBOX_HOME}/libs/*"  moonbox.grid.deploy.worker.MbWorker --host ${local_host}  --masters ${master_url}  1>${MOONBOX_HOME}/log/"worker-$USER-$date.log" 2>&1 &

else
    echo "start MbWorker(with input) --host ${1} --port ${2} --masters ${3} "
    java ${WORKER_JAVA_OPTS} -cp "${MOONBOX_HOME}/libs/*"  moonbox.grid.deploy.worker.MbWorker --host ${1} --port ${2} --masters ${3}  1>${MOONBOX_HOME}/log/"worker-$USER-$date.log" 2>&1 &
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



