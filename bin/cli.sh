#!/bin/bash

moonbox_home_dir="$(cd "`dirname "$0"`/.."; pwd)"
if [ -f "${moonbox_home_dir}/conf/moonbox-env.sh" ]; then
  . "${moonbox_home_dir}/conf/moonbox-env.sh"
fi

java -cp $MOONBOX_HOME/libs/*:$MOONBOX_HOME/libs/moonbox-repl_2.11-0.2.0-SNAPSHOT.jar  moonbox.repl.Main "$@"

