#!/usr/bin/env bash


if [ -z "${MOONBOX_HOME}" ]; then
  export MOONBOX_HOME="$(cd "`dirname "$0"`/.."; pwd)"
fi

if [ -f "${MOONBOX_HOME}/conf/moonbox-env.sh" ]; then
    . "${MOONBOX_HOME}/conf/moonbox-env.sh"
fi

HOST=`hostname`
LOGFILE=${MOONBOX_HOME}/logs/"moonbox-$USER-master-$HOST.log"

# Find the java binary
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

$RUNNER -cp "${MOONBOX_HOME}/libs/*" moonbox.grid.deploy.master.MoonboxMaster $@ 1>>${LOGFILE} 2>&1 &

if [ $? -eq 0 ]; then
   echo "${HOST}: starting moonbox master, logging to ${LOGFILE} "
fi

# sleep 2
# ps -fe|grep 'MoonboxMaster' |grep -v grep 1>/dev/null 2>&1
# if [ $? -ne 0 ]
# then
#    echo -e "\033[31m!!!moonbox master is not running.\033[0m"
# else
#     echo -e "\033[32m moonbox master is running.\033[0m"
# fi

