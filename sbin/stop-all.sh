#!/usr/bin/env bash

# Start all server daemons

# Set moonbox home

if [ -z "${MOONBOX_HOME}" ]; then
    export MOONBOX_HOME="$(cd "`dirname "$0"`"/..; pwd)"
fi


# Start all worker
"${MOONBOX_HOME}/sbin/stop-workers.sh"


# Start all master
"${MOONBOX_HOME}/sbin/stop-masters.sh"






