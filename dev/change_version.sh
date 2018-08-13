#!/usr/bin/env bash

if [ $# -ne 1 ];then
    echo "ERROR: must have one input param"
    exit 0
fi

echo "new version is $1"

mvn --version 1>/dev/null 2>&1
if [ $? -ne 0 ]; then
    echo "ERROR: no install maven, please install maven firstly."
    exit 0
fi

MVN_VERSION=`mvn help:evaluate -Dexpression=project.version | grep -v INFO | grep -v WARNING | grep -v Download`

if [ "$MVN_VERSION" == "$1" ]; then
    echo "ERROR: no need to change version, the maven version is $MVN_VERSION"
    exit 0
fi

current_dir=`pwd`
script_dir=$(cd `dirname $0`; pwd)

(cd ${script_dir}
cd ..
mvn versions:set -DnewVersion=$1
if [ $? -ne 0 ];then
    echo "ERROR: mvn versions:set -DnewVersion=$1 runs failed"
    exit 1
fi
mvn -N versions:update-child-modules
if [ $? -ne 0  ]; then
    echo "ERROR: mvn -N versions:update-child-modules runs failed"
    exit 2
fi
mvn versions:commit
if [ $? -ne 0  ];then
    echo "ERROR: mvn versions:commit runs failed"
    exit 3
fi
echo "maven new version is changed to $1 succeed"
)

echo "Bye..."

#(
#cd ${script_dir}
#cd ..
#root_dir=`pwd`
#echo `grep ${MVN_VERSION} -rl ${root_dir}/sbin/` | xargs sed  -i "s/${MVN_VERSION}/$1/g"
#echo `grep ${MVN_VERSION} -rl ${root_dir}/bin/`  | xargs sed  -i "s/${MVN_VERSION}/$1/g"
#echo "script new version in bin/sbin is changed to $1 succeed"
#)

