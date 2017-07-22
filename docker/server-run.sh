#!/bin/bash
cd $(dirname $0)
. ../common.sh

./kill.sh $DOC_SERVER

DEBUG="-Xdebug -Xrunjdwp:server=y,transport=dt_socket,address=$SERVER_DEBUG_PORT,suspend=n"

docker run -d --name $DOC_SERVER --net $DOC_NET -p $SERVER_PORT:$SERVER_PORT -p $SERVER_DEBUG_PORT:$SERVER_DEBUG_PORT -v `pwd`/../server/:/data $DOC_JAVA java $DEBUG -jar $SERVER_JAR

