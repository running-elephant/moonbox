#!/usr/bin/env bash

#import env firstly
moonbox_home_dir="$(cd "`dirname "$0"`/.."; pwd)"
if [ -f "${moonbox_home_dir}/conf/moonbox-env.sh" ]; then
  . "${moonbox_home_dir}/conf/moonbox-env.sh"
fi


cat "${MOONBOX_HOME}/conf/nodes" | grep "moonbox.grid.worker" | awk '{print $2}' | while read line
do
    hostname=`echo $line | awk '{print $1}' |cut -d ':' -f 1`
    akka_port=`echo $line | awk '{print $1}' |cut -d ':' -f 2`
    ssh_options="$MOONBOX_SSH_OPTIONS"
    if [ -z "$ssh_options" ]; then
        ssh_options="-p 22"
    fi

    echo "ssh $hostname $ssh_options ..."

    ssh -T ${ssh_options} ${hostname}  << EEOF
        echo "remote moonbox_home is empty \${MOONBOX_HOME}, use master ${MOONBOX_HOME}"
        ${MOONBOX_HOME}/sbin/stop-worker.sh

EEOF
        if [ $? -ne 0 ] ;then
            echo "ERROR: ssh ${hostname} and run command failed, please check"
            echo ""
        else
            echo "all in ${hostname} done"
            echo ""
        fi
        
done

