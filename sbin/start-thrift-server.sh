#!/usr/bin/env bash

if [ -z "${MOONBOX_HOME}" ]; then
  export MOONBOX_HOME="$(cd "`dirname "$0"`/.."; pwd)"
fi

if [ -f "${MOONBOX_HOME}/conf/moonbox-env.sh" ]; then
    . "${MOONBOX_HOME}/conf/moonbox-env.sh"
fi

HOST=`hostname`
LOGFILE=${MOONBOX_HOME}/logs/"moonbox-$USER-thrift-server-$HOST.log"

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

$RUNNER -Dlog4j.configuration="" -cp "${MOONBOX_HOME}/libs/*:${SPARK_HOME}/jars/*" moonbox.thriftserver.MoonboxThriftServer $@ 1>>${LOGFILE} 2>&1 &

if [ $? -eq 0 ]; then
   echo "${HOST}: starting moonbox thrift server, logging to ${LOGFILE} "
fi

