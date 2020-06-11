#!/usr/bin/env bash


#!/usr/bin/env bash


if [ -z "${MOONBOX_HOME}" ]; then
  export MOONBOX_HOME="$(cd "`dirname "$0"`/.."; pwd)"
fi

if [ -f "${MOONBOX_HOME}/conf/moonbox-env.sh" ]; then
    . "${MOONBOX_HOME}/conf/moonbox-env.sh"
fi

HOST=`hostname`
LOGFILE=${MOONBOX_HOME}/logs/"moonbox-$USER-slave-$HOST.log"

if [ -n "${JAVA_HOME}" ]; then
  RUNNER="${JAVA_HOME}/bin/java"
else
  if [ "$(command -v java)" ]; then
    RUNNER="java"
  else
    echo "JAVA_HOME is not set" >&2
    exit 1
  fi
fi

#$RUNNER -cp "${MOONBOX_HOME}/libs/*" moonbox.grid.deploy.worker.MoonboxWorker $@ 1>>${LOGFILE} 2>&1 &
$RUNNER -cp "${MOONBOX_HOME}/libs/*" -Dlog.file=${LOGFILE} moonbox.grid.deploy.worker.MoonboxWorker $@  &

if [ $? -eq 0 ]; then
   echo "starting moonbox slave, logging to ${LOGFILE} "
fi

# sleep 2
# ps -fe|grep 'MoonboxMaster' |grep -v grep 1>/dev/null 2>&1
# if [ $? -ne 0 ]
# then
#    echo -e "\033[31m${HOST}: moonbox slave is not running.\033[0m"
# else
#    echo -e "\033[32m${HOST}: moonbox slave is running.\033[0m"
# fi

