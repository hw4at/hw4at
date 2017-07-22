#!/bin/bash
cd $(dirname $0)
. ../common.sh

DIR=/tmp/surl
rm -rf $DIR
mkdir $DIR

cp -f $SERVER_JAR $DIR

cd $DIR
unzip $SERVER_JAR_NAME

