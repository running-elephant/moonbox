#!/usr/bin/env bash

moonbox_home_dir="$(cd "`dirname "$0"`/.."; pwd)"
if [ -f "${moonbox_home_dir}/conf/moonbox-env.sh" ]; then
  . "${moonbox_home_dir}/conf/moonbox-env.sh"
fi

date=`date +%Y%m%d_%H%M%S`

cat "${MOONBOX_HOME}/cluster/appid" | grep -v '#' | sed '/^$/d' | sed 's/[[:space:]]//g'| while read line
do
    application_id=`echo $line | cut -d '|' -f 1`
    yarn_config=`echo $line | cut -d '|' -f 2`
    echo "kill yarn application $application_id ... "
    java -cp "${MOONBOX_HOME}/libs/*" "moonbox.tool.Node" "-c" "$yarn_config" "-k" "$application_id" 1>${MOONBOX_HOME}/log/"kill-yarn-$USER-$date.log" 2>&1 &
    if [ $? -ne 0 ] ; then
        echo "Done."
    else
        echo "Failed."
    fi
done