#!/usr/bin/env bash

if [ -z "${MOONBOX_HOME}" ]; then
  export MOONBOX_HOME="$(cd "`dirname "$0"`/.."; pwd)"
fi

if [ -f "${MOONBOX_HOME}/conf/moonbox-env.sh" ]; then
  . "${MOONBOX_HOME}/conf/moonbox-env.sh"
fi

# Start MoonboxMaster
"${MOONBOX_HOME}/sbin"/start-master.sh

# Start MoonboxWorker
"${MOONBOX_HOME}/sbin"/start-slaves.sh

