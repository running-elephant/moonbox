#!/usr/bin/env bash

moonbox_home_dir="$(cd "`dirname "$0"`/.."; pwd)"
if [ -f "${moonbox_home_dir}/conf/moonbox-env.sh" ]; then
  . "${moonbox_home_dir}/conf/moonbox-env.sh"
fi

JAVA="java"
JAVA_OPTS="-cp ${MOONBOX_HOME}/libs/moonbox-repl_2.11-0.3.0-SNAPSHOT.jar"

case $1 in
cluster)
    CLASS="moonbox.tool.Cluster"
    shift
    ;;
node)
    CLASS="moonbox.tool.Node"
    shift
    ;;
sql)
    CLASS="moonbox.repl.Main"
    shift
    ;;
help)
    echo "Usage: moonbox cluster | node | sql )"
    ;;
esac

"$JAVA" "$JAVA_OPTS" "$CLASS" "$@"


