#!/usr/bin/env bash

moonbox_home_dir="$(cd "`dirname "$0"`/.."; pwd)"
if [ -f "${moonbox_home_dir}/conf/moonbox-env.sh" ]; then
  . "${moonbox_home_dir}/conf/moonbox-env.sh"
fi

JAVA="java"
JAVA_OPTS="${MOONBOX_HOME}/libs/moonbox-repl_2.11-0.3.0-SNAPSHOT.jar"


case $1 in
cluster-info)
    RESOURCE="management/cluster-info"
    shift
    ;;
apps-info)
    RESOURCE="management/apps-info"
    shift
    ;;
shell)
    CLASS="moonbox.repl.MoonboxShell"
    shift
    ;;
--help)
    echo "Usage: moonbox shell | cluster-info | apps-info"
    exit 0
    ;;
*)
    echo "Error: no param, please use --help"
    exit 0
    ;;
esac

if [ "$RESOURCE" != "" ]; then
    curl -XGET "http://$MOONBOX_MASTER_HOST:9099/$RESOURCE"
else
    "$JAVA" -cp "$JAVA_OPTS" "$CLASS" -h "$MOONBOX_MASTER_HOST" -P 10010 "$@"
fi