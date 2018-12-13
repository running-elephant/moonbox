#!/usr/bin/env bash

if [ -z "${MOONBOX_HOME}" ]; then
  export MOONBOX_HOME="$(cd "`dirname "$0"`/.."; pwd)"
fi

if [ -f "${MOONBOX_HOME}/conf/moonbox-env.sh" ]; then
  . "${MOONBOX_HOME}/conf/moonbox-env.sh"
fi

# Stop MoonboxMaster
"${MOONBOX_HOME}/sbin"/stop-master.sh

# Stop MoonboxWorker
"${MOONBOX_HOME}/sbin"/stop-slaves.sh


