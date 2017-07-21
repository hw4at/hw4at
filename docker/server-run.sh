#!/bin/bash
cd $(dirname $0)
. ../common.sh

./kill.sh $DOC_SERVER

DEBUG="-J-Xdebug -J-Xrunjdwp:server=y,transport=dt_socket,address=$SERVER_DEBUG_PORT,suspend=n"

CMD="bash -c 'sleep 300'"
#CMD="scala $DEBUG $SERVER_JAR"

#docker run -d --name $DOC_SERVER --net $DOC_NET -p $SERVER_PORT:$SERVER_PORT -p $SERVER_DEBUG_PORT:$SERVER_DEBUG_PORT -v `pwd`/../server/:/data $DOC_SCALA bash -c "sleep 300"

docker run -d --name $DOC_SERVER --net $DOC_NET -p $SERVER_PORT:$SERVER_PORT -p $SERVER_DEBUG_PORT:$SERVER_DEBUG_PORT -v `pwd`/../server/:/data $DOC_SCALA scala $DEBUG $SERVER_JAR

