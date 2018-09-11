#!/usr/bin/env bash

# Start all server daemons

# Set moonbox home


moonbox_home_dir="$(cd "`dirname "$0"`/.."; pwd)"
if [ -f "${moonbox_home_dir}/conf/moonbox-env.sh" ]; then
  . "${moonbox_home_dir}/conf/moonbox-env.sh"
fi

# Start all nodes
"${MOONBOX_HOME}/sbin/start-nodes.sh"

