#!/usr/bin/env bash

HOST=`hostname`

function kill_process()
{
    PROCESS_NAME=$1
    KILL_TIMES=$2

    pre_pid=`ps -ef|grep "${PROCESS_NAME}" | grep -v "grep" | grep -v "stop" | awk '{print $2}'`
    if [ -n "${pre_pid}" ]; then  #have previous pid
        if [ ${KILL_TIMES} -ge 4 ];then # 4  ge 大于等于
            kill -9 ${pre_pid}
        else # 0, 1, 2, 3
            kill ${pre_pid}
        fi
        echo "$HOST: stopping $PROCESS_NAME"
        return 1
    else
        echo "$HOST: no $PROCESS_NAME to stop"
        return 0
    fi
}

EXIST=1
KILL_TIMES=0
while [[ "$EXIST" != 0  && ${KILL_TIMES} -lt 5 ]];  #-lt  小于
do
    kill_process "moonbox.MoonboxMaster" ${KILL_TIMES}
    sleep 2
    if [[ $? !=  0 ]] ; then
        EXIST=1
    else
        EXIST=0
    fi
    let KILL_TIMES++
done

exit 0
