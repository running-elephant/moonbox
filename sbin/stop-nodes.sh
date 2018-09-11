#!/usr/bin/env bash

#import env firstly
moonbox_home_dir="$(cd "`dirname "$0"`/.."; pwd)"
if [ -f "${moonbox_home_dir}/conf/moonbox-env.sh" ]; then
  . "${moonbox_home_dir}/conf/moonbox-env.sh"
fi


cat "${MOONBOX_HOME}/conf/nodes" | grep -v '#' | sed '/^$/d' | sed 's/[[:space:]]//g'| while read line
do
    hostname=`echo ${line}  |cut -d ':' -f 1`
    akka_port=`echo ${line} |cut -d ':' -f 2`
    ssh_options="$MOONBOX_SSH_OPTIONS"
    if [ -z "$ssh_options" ]; then
        ssh_options="-p 22"
    fi

    echo "SSH ${hostname} ${ssh_options} ..."

    ssh -T ${ssh_options} ${hostname}  << EEOF
        echo "remote moonbox_home is empty \${MOONBOX_HOME}, use moonbox_home ${MOONBOX_HOME}"
        ${MOONBOX_HOME}/sbin/stop-node.sh

EEOF
    if [ $? -ne 0 ] ;then
        echo "ERROR: ssh ${hostname} and run command failed, please check parameter"
        echo ""
    else
        echo "all done in ${hostname}, bye."
        echo ""
    fi

done


