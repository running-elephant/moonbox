#!/usr/bin/env bash

if [ -z "${MOONBOX_HOME}" ]; then
  export MOONBOX_HOME="$(cd "`dirname "$0"`"/..; pwd)"
fi

if [ -z "$MOONBOX_ENV_LOADED" ]; then
  export MOONBOX_ENV_LOADED=1

  # Returns the parent of the directory this script lives in.
  parent_dir="${MOONBOX_HOME}"

  user_conf_dir="${MOONBOX_CONF_DIR:-"$parent_dir"/conf}"

  if [ -f "${user_conf_dir}/moonbox-env.sh" ]; then
    # Promote all variable declarations to environment (exported) variables
    set -a
    . "${user_conf_dir}/moonbox-env.sh"
    set +a
  fi
fi