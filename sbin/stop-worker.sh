#!/usr/bin/env bash

function kill_process()
{
    process_name=$1

    echo "kill $process_name ... "
    pre_pid=`ps -ef|grep "${process_name}" | grep -v "grep" | grep -v "stop" | awk '{print $2}'`
    if [ -n "${pre_pid}" ]; then  #have previous pid
        kill ${pre_pid}
        echo "moonbox server is running as PID=${pre_pid}. stop it firstly "
        return 1
    else
        echo "no process ${process_name} found"
        return 0
    fi
}

exist=1
while [[ "$exist" != 0 ]]
do
kill_process "moonbox"
sleep 2
if [[ $? !=  0 ]] ; then
    exist=1
else
    exist=0
fi
done

exit 0
