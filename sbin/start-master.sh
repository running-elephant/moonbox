#!/usr/bin/env bash


#import env firstly
moonbox_home_dir="$(cd "`dirname "$0"`/.."; pwd)"
if [ -f "${moonbox_home_dir}/conf/moonbox-env.sh" ]; then
  . "${moonbox_home_dir}/conf/moonbox-env.sh"
fi

echo "start master in ${MOONBOX_HOME} with param($@)"

local_host="`hostname`"
date=`date +%Y%m%d_%H%M%S`

if [ $# -ne 1 ]; then #if start from local, it has 3 parameters
    nodes_lines=`cat "${MOONBOX_HOME}/conf/nodes" | grep -v '^#' | sed 's/[[:space:]]//g'`
    nodes_address=`echo ${nodes_lines} | sed 's/[[:space:]]/,/g'`

    echo "start Moonbox --address $nodes_address"
    java ${JAVA_OPTS} -cp "${MOONBOX_HOME}/libs/*"  moonbox.grid.deploy2.node.Moonbox -n ${nodes_address} 1>${MOONBOX_HOME}/log/"master-$USER-$date.log" 2>&1 &
else
    #start from remote
    echo "start Moonbox(with input) --nodes ${1}"
    java ${JAVA_OPTS} -cp "${MOONBOX_HOME}/libs/*"  moonbox.grid.deploy2.node.Moonbox -n ${1}  1>${MOONBOX_HOME}/log/"master-$USER-$date.log" 2>&1 &
fi

if [ $? -eq 0 ]; then
   echo "successfully."
else
   echo "failed."
fi

sleep 2

ps -fe|grep 'Moonbox' |grep -v grep 1>/dev/null 2>&1
if [ $? -ne 0 ]
then
    echo -e "\033[31m!!!NO Moonbox is running......\033[0m"
else
    echo -e "\033[32mMoonbox [$local_host] is running......\033[0m"
fi

