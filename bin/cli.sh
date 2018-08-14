#!/usr/bin/env bash

moonbox_home_dir="$(cd "`dirname "$0"`/.."; pwd)"
if [ -f "${moonbox_home_dir}/conf/moonbox-env.sh" ]; then
  . "${moonbox_home_dir}/conf/moonbox-env.sh"
fi

java -cp "$MOONBOX_HOME/libs/*"  moonbox.repl.Main "$@"

