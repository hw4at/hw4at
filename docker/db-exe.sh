#!/bin/bash
cd $(dirname $0)
. ../common.sh

docker exec $DOC_DB mysql -u$DB_USER -p$DB_PWD -e "$@"
