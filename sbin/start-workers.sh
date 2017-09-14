#!/bin/env bash

cat "${MOONBOX_HOME}/conf/nodes" | grep worker | awk '{print $2 " " $3}' | while read line
do
    hostname=`echo $line | awk '{print $1}' |cut -d ':' -f 1`
    akka_port=`echo $line | awk '{print $1}' |cut -d ':' -f 2`
    ssh_port=`echo $line | awk '{print $2}' `

    if [ -z "$ssh_port" ]; then
        ssh_port=22
    fi

    akka_ip=`cat /etc/hosts | grep $hostname | awk '{print $1}'`

    echo "ssh $akka_ip $ssh_port ..."

    ssh -o Port=${ssh_port} ${akka_ip}  << EEOF
        work_home=\${MOONBOX_HOME}
        echo "work home is \${MOONBOX_HOME}  ip ${akka_ip}   port ${akka_port}"
        if [ -z \$work_home  ]; then
            echo 'NOTICE: Not found MOONBOX_HOME env in remote machine: ${hostname}, please set MOONBOX_HOME and MOONBOX_JAVA_HOME firstly'
        else
            cd \${work_home}
            \${work_home}/sbin/start-worker.sh
	fi
EEOF
    if [ $? -ne 0 ] ;then
        echo "ERROR: ssh ${ip} and run command failed, please check"
        echo ""
    else
        echo "all in ${ip} done"
        echo ""
    fi
done

