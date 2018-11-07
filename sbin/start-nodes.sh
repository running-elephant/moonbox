#!/usr/bin/env bash

moonbox_home_dir="$(cd "`dirname "$0"`/.."; pwd)"
if [ -f "${moonbox_home_dir}/conf/moonbox-env.sh" ]; then
  . "${moonbox_home_dir}/conf/moonbox-env.sh"
fi


nodes_lines=`cat "${MOONBOX_HOME}/conf/nodes" | grep -v '^#' | sed '/^$/d' | sed 's/[[:space:]]//g'`
nodes_address=`echo ${nodes_lines} | sed 's/[[:space:]]/,/g'`

cat "${MOONBOX_HOME}/conf/nodes" | grep -v '#' | sed '/^$/d' | sed 's/[[:space:]]//g'| while read line
do
    host_name=`echo ${line} | cut -d ':' -f 1`
    ssh_options="$MOONBOX_SSH_OPTIONS"
    if [ -z "$ssh_options" ]; then
        ssh_options="-p 22"
    fi

    echo "SSH $host_name $ssh_options ..."

    ssh -T ${ssh_options} ${host_name}  << EEOF
        echo "remote moonbox_home is empty \${MOONBOX_HOME}, use local MOONBOX_HOME ${MOONBOX_HOME}, host:${host_name}, ssh_options:${ssh_options}"
        ${MOONBOX_HOME}/sbin/start-node.sh  ${nodes_address}

EEOF
    if [ $? -ne 0 ] ;then
        echo "ERROR: ssh ${host_name} and run start command failed, please check nodes file in conf directory."
        echo ""
    else
        echo "all done in ${host_name}."
        echo ""
    fi
done

