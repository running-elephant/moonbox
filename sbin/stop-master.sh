#!/usr/bin/env bash


function kill_process()
{
    process_name=$1
    kill_time=$2

    echo "kill $process_name ... "
    pre_pid=`ps -ef|grep "${process_name}" | grep -v "grep" | grep -v "stop" | awk '{print $2}'`
    if [ -n "${pre_pid}" ]; then  #have previous pid
        if [ ${kill_time} -ge 4 ];then # 4  ge 大于等于
            kill -9 ${pre_pid}
        else # 0, 1, 2, 3
            kill ${pre_pid}
        fi
        echo "moonbox server is running as PID=${pre_pid}. stopping it ... "
        return 1
    else
        echo "no process naming [${process_name}] is found in server"
        return 0
    fi
}

exist=1
kill_time=0
while [[ "$exist" != 0  && ${kill_time} -lt 5 ]];  #-lt  小于
do
    kill_process "Moonbox" ${kill_time}
    sleep 2
    if [[ $? !=  0 ]] ; then
        exist=1
    else
        exist=0
    fi
    let kill_time++
done

exit 0
