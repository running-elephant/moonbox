#!/usr/bin/env bash

if [ -z "${MOONBOX_HOME}" ]; then
  export MOONBOX_HOME="$(cd "`dirname "$0"`"/..; pwd)"
fi

export MOONBOX_CONF_DIR="${MOONBOX_CONF_DIR:-"${MOONBOX_HOME}/conf"}"