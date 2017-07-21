#!/bin/bash
cd $(dirname $0)
. ../common.sh

DEBUG="-Xdebug -Xrunjdwp:server=y,transport=dt_socket,address=$SERVER_DEBUG_PORT,suspend=n"
JAR=target/scala-2.12/$SERVER_JAR_NAME

docker run -d --name $DOC_SERVER --net $DOC_NET -p $SERVER_PORT:$SERVER_PORT -p $SERVER_DEBUG_PORT:$SERVER_DEBUG_PORT -v `pwd`/../server/:/data $DOC_SCALA scala $DEBUG $JAR

