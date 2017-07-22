#!/bin/bash
cd $(dirname $0)
. ../common.sh

./kill.sh $DOC_DB

echo "Running db container ..."
docker run -d --name $DOC_DB --net $DOC_NET -p $DB_PORT:$DB_PORT -e MYSQL_ALLOW_EMPTY_PASSWORD=$MYSQL_ALLOW_EMPTY_PASSWORD -e MYSQL_ROOT_PASSWORD=$DB_PWD $DOC_MYSQL
echo "Run db container is done"

