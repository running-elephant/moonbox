#!/usr/bin/env bash

if [ -z "${MOONBOX_HOME}" ]; then
  export MOONBOX_HOME="$(cd "`dirname "$0"`/.."; pwd)"
fi

if [ -f "${MOONBOX_HOME}/conf/moonbox-env.sh" ]; then
  . "${MOONBOX_HOME}/conf/moonbox-env.sh"
fi

if [ -f "${MOONBOX_HOME}/conf/slaves" ]; then
  HOSTLIST=`cat "${MOONBOX_HOME}/conf/slaves"`
else
  HOSTLIST=localhost
fi

if [ "$MOONBOX_SSH_OPTS" == "" ]; then
    MOONBOX_SSH_OPTS="-o StrictHostKeyChecking=no"
fi

for slave in `echo "$HOSTLIST" | sed "s/#.*$//;/^$/d"`; do
  if [ -n "${MOONBOX_SSH_FOREGROUND}" ]; then
    ssh $MOONBOX_SSH_OPTS "$slave" $"${@// /\\ }" \
      2>&1 | sed "s/^/$slave: /"
  else
    ssh $MOONBOX_SSH_OPTS "$slave" $"${@// /\\ }" \
      2>&1 | sed "s/^/$slave: /" &
  fi
  if [ "$MOONBOX_SLAVE_SLEEP" != "" ]; then
    sleep $MOONBOX_SLAVE_SLEEP
  fi
done

wait