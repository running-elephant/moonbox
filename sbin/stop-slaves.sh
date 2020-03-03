#!/usr/bin/env bash

if [ -z "${MOONBOX_HOME}" ]; then
  export MOONBOX_HOME="$(cd "`dirname "$0"`/.."; pwd)"
fi

if [ -f "${MOONBOX_HOME}/conf/moonbox-env.sh" ]; then
  . "${MOONBOX_HOME}/conf/moonbox-env.sh"
fi

"${MOONBOX_HOME}/sbin/slaves.sh" cd "${MOONBOX_HOME}" \; "${MOONBOX_HOME}"/sbin/stop-slave.sh




