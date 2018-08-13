#!/bin/env bash

#import env firstly
moonbox_home_dir="$(cd "`dirname "$0"`/.."; pwd)"
if [ -f "${moonbox_home_dir}/conf/moonbox-env.sh" ]; then
  . "${moonbox_home_dir}/conf/moonbox-env.sh"
fi

master_lines=`cat "${MOONBOX_HOME}/conf/nodes" | grep -v '^#' | grep -w "moonbox.grid.master" | awk '{print $2}' | sed 's/grid:\/\///g' | sed 's/[[:space:]]//g'`
master_url="grid://"`echo $master_lines | sed 's/[[:space:]]/,/g'`


cat "${MOONBOX_HOME}/conf/nodes" | grep "moonbox.grid.worker" | awk '{print $2 " " $4}' | while read line
do
    hostname=`echo $line | awk '{print $1}' |cut -d ':' -f 1`
    akka_port=`echo $line | awk '{print $1}' |cut -d ':' -s -f 2`
    ssh_options="$MOONBOX_SSH_OPTIONS"
    if [ -z "$ssh_options" ]; then
        ssh_options="-p 22"
    fi

    if [ -z "$akka_port" ]; then
        akka_port="0"
    fi

    #akka_ip=`cat /etc/hosts | grep -w ${hostname} | awk '{print $1}'`

    echo "ssh $hostname $ssh_options ..."

    ssh -T ${ssh_options} ${hostname}  << EEOF
        echo "remote moonbox_home is empty \${MOONBOX_HOME}, use local MOONBOX_HOME ${MOONBOX_HOME}, ip:${hostname},  akka_port:${akka_port}, ssh_options:${ssh_options}"
        ${MOONBOX_HOME}/sbin/start-worker.sh ${hostname}  ${akka_port}  ${master_url}

EEOF
    if [ $? -ne 0 ] ;then
        echo "ERROR: ssh ${ip} and run command failed, please check"
        echo ""
    else
        echo "all in ${ip} done"
        echo ""
    fi
done

